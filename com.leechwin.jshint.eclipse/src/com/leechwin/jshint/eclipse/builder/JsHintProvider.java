package com.leechwin.jshint.eclipse.builder;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.leechwin.jshint.eclipse.Activator;
import com.leechwin.jshint.eclipse.JsHintLog;
import com.leechwin.jshint.eclipse.engine.JsHint;
import com.leechwin.jshint.eclipse.engine.JsHintBuilder;
import com.leechwin.jshint.eclipse.engine.Option;

/**
 * Provide a fully configured instance of {@link JsHint} on demand.
 */
public class JsHintProvider {

    private final JsHintBuilder builder = new JsHintBuilder();

    private JsHint jsHint;

    /**
     * Set up a listener for preference changes. This will ensure that the instance of {@link JsHint} that we have is kept in sync with the users choices. We do this by ensuring that a new lint will
     * be created and configured on the next request.
     */
    public void init() {
        IEclipsePreferences x = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
        x.addPreferenceChangeListener(new IPreferenceChangeListener() {
            public void preferenceChange(PreferenceChangeEvent ev) {
                jsHint = null;
                JsHintLog.info("pref %s changed; nulling jsHint", ev.getKey());
            }
        });
    }

    /**
     * Return a fully configured instance of lint. This should not be cached; each use should call this method.
     */
    public JsHint getJsHint() {
        if (jsHint == null) {
            jsHint = builder.fromDefault();
            configure();
        }
        return jsHint;
    }

    /** Set up the current instance of lint using the current preferences. */
    public void configure() {
        JsHint lint = getJsHint();
        lint.resetOptions();
        IPreferencesService prefs = Platform.getPreferencesService();
        for (Option o : Option.values()) {
            String value = prefs.getString(Activator.PLUGIN_ID, o.getLowerName(), null, null);
            if (value != null) {
                lint.addOption(o, value);
            }
        }
    }
}
