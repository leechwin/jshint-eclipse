package com.leechwin.jshint.eclipse.builder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.leechwin.jshint.eclipse.Activator;
import com.leechwin.jshint.eclipse.JsHintLog;
import com.leechwin.jshint.eclipse.engine.Issue;
import com.leechwin.jshint.eclipse.engine.JsHint;
import com.leechwin.jshint.eclipse.engine.JsHintResult;

public class JsHintBuilder extends IncrementalProjectBuilder {

    private class JsHintDeltaVisitor implements IResourceDeltaVisitor {
        private final IProgressMonitor monitor;

        public JsHintDeltaVisitor(IProgressMonitor monitor) {
            this.monitor = monitor;
        }

        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
                // handle added resource
                logProgress(monitor, resource);
                checkJavaScript(resource);
                break;
            case IResourceDelta.REMOVED:
                // handle removed resource
                break;
            case IResourceDelta.CHANGED:
                // handle changed resource
                logProgress(monitor, resource);
                checkJavaScript(resource);
                break;
            }
            // return true to continue visiting children.
            return true;
        }
    }

    private class JsHintResourceVisitor implements IResourceVisitor {
        private final IProgressMonitor monitor;

        public JsHintResourceVisitor(IProgressMonitor monitor) {
            this.monitor = monitor;
        }

        public boolean visit(IResource resource) {
            logProgress(monitor, resource);
            checkJavaScript(resource);
            // return true to continue visiting children.
            return true;
        }
    }

    // NB! Must match plugin.xml declaration.
    public static final String BUILDER_ID = Activator.PLUGIN_ID + ".builder.JsHintBuilder";

    // NB! Must match plugin.xml declaration.
    public static final String MARKER_TYPE = Activator.PLUGIN_ID + ".JSHintProblem";

    private final JsHintProvider lintProvider = new JsHintProvider();
    private final Excluder excluder = new Excluder();

    public JsHintBuilder() {
        lintProvider.init();
        excluder.init();
    }

    private void addMarker(IFile file, Issue issue) {
        try {
            IMarker m = file.createMarker(MARKER_TYPE);
            if (m.exists()) {
                m.setAttribute(IMarker.MESSAGE, issue.getReason());
                m.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
                m.setAttribute(IMarker.LINE_NUMBER, issue.getLine());
                m.setAttribute(IMarker.SOURCE_ID, "JSHint");
            }
        } catch (CoreException e) {
            JsHintLog.error(e);
        }
    }

    @Override
    protected IProject[] build(final int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor) throws CoreException {
        ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
            public void run(IProgressMonitor monitor) throws CoreException {
                if (kind == FULL_BUILD) {
                    fullBuild(monitor);
                } else {
                    IResourceDelta delta = getDelta(getProject());
                    if (delta == null) {
                        fullBuild(monitor);
                    } else {
                        incrementalBuild(delta, monitor);
                    }
                }
            }
        }, monitor);
        return null;
    }

    private void checkJavaScript(IResource resource) {
        if (!(resource instanceof IFile)) {
            return;
        }

        IFile file = (IFile) resource;
        if (!isJavaScript(file)) {
            return;
        }

        // Clear out any existing problems.
        deleteMarkers(file);

        if (excluded(file)) {
            return;
        }

        BufferedReader reader = null;
        try {
            JsHint lint = lintProvider.getJsHint();
            reader = new BufferedReader(new InputStreamReader(file.getContents(), file.getCharset()));
            JsHintResult result = lint.lint(file.getFullPath().toString(), reader);
            for (Issue issue : result.getIssues()) {
                addMarker(file, issue);
            }
        } catch (IOException e) {
            JsHintLog.error(e);
        } catch (CoreException e) {
            JsHintLog.error(e);
        } finally {
            close(reader);
        }
    }

    /**
     * Is {@code file} explicitly excluded? Check against a list of regexes in the <i>exclude_path_regexes</i> preference.
     */
    private boolean excluded(IFile file) {
        return excluder.isExcluded(file);
    }

    private boolean isJavaScript(IFile file) {
        return file.getName().endsWith(".js");
    }

    private void close(Closeable close) {
        if (close == null) {
            return;
        }
        try {
            close.close();
        } catch (IOException e) {
        }
    }

    private void deleteMarkers(IFile file) {
        try {
            file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
        } catch (CoreException e) {
            JsHintLog.error(e);
        }
    }

    private void fullBuild(final IProgressMonitor monitor) throws CoreException {
        try {
            startProgress(monitor);
            getProject().accept(new JsHintResourceVisitor(monitor));
        } catch (CoreException e) {
            JsHintLog.error(e);
        } finally {
            monitor.done();
        }
    }

    private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor)
            throws CoreException {
        try {
            startProgress(monitor);
            delta.accept(new JsHintDeltaVisitor(monitor));
        } finally {
            monitor.done();
        }
    }

    private void startProgress(IProgressMonitor monitor) {
        monitor.beginTask("JSHint", IProgressMonitor.UNKNOWN);
    }

    private void logProgress(IProgressMonitor monitor, IResource resource) {
        monitor.subTask("Linting " + resource.getName());
    }
}
