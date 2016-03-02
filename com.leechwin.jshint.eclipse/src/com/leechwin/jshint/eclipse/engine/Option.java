package com.leechwin.jshint.eclipse.engine;

import java.util.Locale;

public enum Option {
    // Enforcing and Relaxing options
    BITWISE("Prohibit bitwise operators (&, |, ^, etc.)", Boolean.class),
    CURLY("Require {} for every new block or scope", Boolean.class),
    EQEQEQ("Require triple equals (===) for comparison", Boolean.class),
    FORIN("Require filtering for..in loops with obj.hasOwnProperty()", Boolean.class),
    FREEZE("Prohibits overwriting prototypes of native objects such as Array, Date etc.", Boolean.class),
    FUNCSCOPE("Tolerate defining variables inside control statements", Boolean.class),
    ITERATOR("Tolerate using the `__iterator__` property", Boolean.class),
    LATEDEF("Require variables/functions to be defined before being used", Boolean.class),
    NOARG("Prohibit use of `arguments.caller` and `arguments.callee`", Boolean.class),
    NOCOMMA("Prohibit use of the comma operator", Boolean.class),
    NONBSP("Prohibit \"non-breaking whitespace\" characters", Boolean.class),
    NONEW("Prohibit use of constructors for side-effects (without assignment)", Boolean.class),
    NOTYPEOF("Tolerate invalid typeof operator values", Boolean.class),
    SHADOW("Allows re-define variables later in code e.g. `var x=1; x=2;`", Boolean.class),
    STRICT("Requires all functions run in ES5 Strict Mode", Boolean.class),
    UNDEF("Require all non-global variables to be declared (prevents global leaks)", Boolean.class),
    UNUSED("Prohibits never use variables", Boolean.class),
    VARSTMT("Disallow any var statements. Only `let` and `const` are allowed", Boolean.class),

    ASI("Tolerate Automatic Semicolon Insertion (no semicolons)", Boolean.class),
    BOSS("Tolerate assignments where comparisons would be expected", Boolean.class),
    DEBUG("Allow debugger statements e.g. browser breakpoints", Boolean.class),
    EQNULL("Tolerate use of `== null`", Boolean.class),
    EVIL("Tolerate use of `eval` and `new Function()`", Boolean.class),
    EXPR("Tolerate `ExpressionStatement` as Programs", Boolean.class),
    LASTSEMIC("Tolerate omitting a semicolon for the last statement of a 1-line block", Boolean.class),
    LOOPFUNC("Tolerate functions being defined in loops", Boolean.class),
    MOZ("Allow Mozilla specific syntax (extends and overrides esnext features)", Boolean.class),
    NOYIELD("Tolerate generator functions with no yield statement in them", Boolean.class),
    PLUSPLUS("Prohibit use of `++` and `--`", Boolean.class),
    PROTO("Tolerate using the `__proto__` property", Boolean.class),
    SCRIPTURL("Tolerate script-targeted URLs", Boolean.class),
    SUPERNEW("Tolerate `new function () { ... };` and `new Object;`", Boolean.class),
    VALILLDTHIS("Tolerate using this in a non-constructor function", Boolean.class),
    WITHSTMT("Tolerate use of 'with'", Boolean.class),

    ESVERSION("Specify the ECMAScript version to which the code must adhere", Integer.class),
    MAXERR("Maximum error before stopping", Integer.class),
    MAXPARAMS("Maximum parameters allowed per function", Integer.class),

    /*
     * To hard options
     * MAXDEPTH("This option lets you control how nested do you want your blocks", Integer.class),
     * MAXSTATEMENTS("This option lets you set the max number of statements allowed per function", Boolean.class),
     * MAXCOMPLEXITY("This option lets you control cyclomatic complexity throughout your code", Integer.class)
    */

    // Environments Options
    BROWSER("If the standard browser globals should be predefined", Boolean.class),
    BROWSERIFY("If browserify globals(node.js code in the browser) should be predefined", Boolean.class),
    COUCH("If Couch DB globals should be predefined", Boolean.class),
    DEVEL("If browser globals that are useful in development should be predefined", Boolean.class),
    DOJO("If Dojo globals should be predefined", Boolean.class),
    JASMINE("If Jasmine globals should be predefined", Boolean.class),
    JQUERY("If jQuery globals should be predefined", Boolean.class),
    MOCHA("If Mocha globals should be predefined", Boolean.class),
    MODULE("If Module globals should be predefined", Boolean.class),
    MOOTOOLS("If MooTools globals should be predefined", Boolean.class),
    NODE("If Node.js globals should be predefined", Boolean.class),
    NONSTANDARD("If widely adopted globals(escape, unescape, etc) should be predefined", Boolean.class),
    PHANTOM("If PhantomJS globals should be predefined", Boolean.class),
    PROTOTYPEJS("If Prototype and Scriptaculous globals should be predefined", Boolean.class),
    QUNIT("If QUnit globals should be predefined", Boolean.class),
    RHINO("If Rhnio globals should be predefined", Boolean.class),
    SHELLJS("If ShellJS globals should be predefined", Boolean.class),
    TYPED("If globals for typed array constructions should be predefined", Boolean.class),
    WORKER("If Web Workers should be predefined", Boolean.class),
    WSH("If Windows Scripting Host should be predefined", Boolean.class),
    YUI("If Yahoo User Interface should be predefined", Boolean.class),

    PREDEF("The names of predefined global variables", String.class);

    private String description;
    private Class<?> type;

    private Option(String description, Class<?> type) {
        this.description = description;
        this.type = type;
    }

    /**
     * Return a description of what this option affects.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Return the lowercase name of this option.
     */
    public String getLowerName() {
        return name().toLowerCase(Locale.getDefault());
    }

    /**
     * What type does the value of this option have?
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Calculate the maximum length of all of the {@link Option} names.
     * @return the length of the largest name.
     */
    public static int maximumNameLength() {
        int maxOptLen = 0;
        for (Option o : values()) {
            int len = o.name().length();
            if (len > maxOptLen) {
                maxOptLen = len;
            }
        }
        return maxOptLen;
    }

    /**
     * Show this option and its description.
     */
    @Override
    public String toString() {
        return getLowerName() + "[" + getDescription() + "]";
    }
}
