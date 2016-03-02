package com.leechwin.jshint.eclipse.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

public class JsHintBuilder {
    private static final String JSHINT_FILE = "lib/jshint.js";

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private ContextFactory contextFactory = new ContextFactory();

    /**
     * Initialize the scope from a jshint.js found in the classpath. Assumes a UTF-8 encoding.
     * @param resource the location of jshint.js on the classpath.
     * @return a configured {@link JsHint}
     * @throws IOException if there are any problems reading the resource.
     */
    public JsHint fromClasspathResource(String resource) throws IOException {
        return fromClasspathResource(resource, UTF8);
    }

    /**
     * Initialize the scope from a jshint.js found in the classpath.
     * @param resource the location of jshint.js on the classpath.
     * @param encoding the encoding of the resource.
     * @return a configured {@link JsHint}
     * @throws IOException if there are any problems reading the resource.
     */
    public JsHint fromClasspathResource(String resource, Charset encoding) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream(resource), encoding));
        return fromReader(reader, resource);
    }

    /**
     * Initialize the scope with a default jshint.js.
     * @return a configured {@link JsHint}
     * @throws RuntimeException if we fail to load the default jshint.js.
     */
    public JsHint fromDefault() {
        try {
            return fromClasspathResource(JSHINT_FILE);
        } catch (IOException e) {
            // We wrap and rethrow, as there's nothing a caller can do in this
            // case.
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize the scope with the jshint.js passed in on the filesystem. Assumes a UTF-8 encoding.
     * @param f the path to jshint.js
     * @return a configured {@link JsHint}
     * @throws IOException if the file can't be read.
     */
    public JsHint fromFile(File f) throws IOException {
        return fromFile(f, UTF8);
    }

    /**
     * Initialize the scope with the jshint.js passed in on the filesystem.
     * @param f the path to jshint.js
     * @param encoding the encoding of the file
     * @return a configured {@link JsHint}
     * @throws IOException if the file can't be read.
     */
    public JsHint fromFile(File f, Charset encoding) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), encoding));
        return fromReader(reader, f.toString());
    }

    /**
     * Initialize the scope with an arbitrary jshint.
     * @param reader an input source providing jshint.js.
     * @param name the name of the resource backed by the reader
     * @return a configured {@link JsHint}
     * @throws IOException if there are any problems reading from {@code reader} .
     */
    @NeedsContext
    public JsHint fromReader(Reader reader, String name) throws IOException {
        try {
            Context cx = contextFactory.enterContext();
            ScriptableObject scope = cx.initStandardObjects();
            cx.evaluateReader(scope, reader, name, 1, null);
            Function lintFunc = (Function) scope.get("JSHINT", scope);
            return new JsHint(contextFactory, lintFunc);
        } finally {
            Context.exit();
        }
    }

}
