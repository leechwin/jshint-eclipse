package com.leechwin.jshint.eclipse.preferences;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.leechwin.jshint.eclipse.Activator;
import com.leechwin.jshint.eclipse.engine.Option;

/**
 * Set up the default preferences. By default,we enable:
 */
public class PreferencesInitializer extends AbstractPreferenceInitializer {

    public static final String PREDEF_ID = Activator.PLUGIN_ID + ".preference.predef";
    public static final int DEFAULT_ESVERSION = 6;
    public static final int DEFAULT_MAXERR = 100;
    public static final int DEFAULT_MAXPARAMS = 5;

    private final Set<Option> defaultEnable = EnumSet.of(Option.BITWISE, Option.CURLY, Option.EQEQEQ, Option.UNDEF, Option.UNUSED, Option.DEBUG, Option.MOZ,
            Option.BROWSER, Option.DEVEL, Option.MOCHA, Option.NODE, Option.QUNIT);

    @Override
    public void initializeDefaultPreferences() {
        IEclipsePreferences node = DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID);
        for (Option o : defaultEnable) {
            node.putBoolean(o.getLowerName(), true);
        }
        // Hand code these.
        node.putInt(Option.ESVERSION.getLowerName(), DEFAULT_ESVERSION);
        node.putInt(Option.MAXERR.getLowerName(), DEFAULT_MAXERR);
        node.putInt(Option.MAXPARAMS.getLowerName(), DEFAULT_MAXPARAMS);
    }

}
