package com.leechwin.jshint.eclipse.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of a lint run.
 * @author leechwin1@gmail.com
 */
public class JsHintResult {

    private final List<Issue> issues = new ArrayList<Issue>();

    JsHintResult(List<Issue> issues) {
        this.issues.addAll(issues);
    }

    /**
     * Return a list of all issues that lint found with this source code.
     */
    public List<Issue> getIssues() {
        return issues;
    }

}
