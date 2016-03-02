package com.leechwin.jshint.eclipse.engine;


/**
 * A single issue with the code that is being checked for problems.
 *
 * @author leechwin1@gmail.com
 */
public class Issue {

    private final int line;
    private final int character;
    private final String reason;
    private final String id;

    public Issue(int line, int character, String reason, String id) {
        this.line = line;
        this.character = character;
        this.reason = reason;
        this.id = id;
    }

    /**
     * @return the number of the line on which this issue occurs.
     */
    public int getLine() {
        return line;
    }

    /**
     * @return the position of the issue within the line. Starts at 0.
     */
    public int getCharacter() {
        return character;
    }

    /**
     * @return a textual description of this issue.
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return the name of the issue type.
     */
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getLine() + ":" + getCharacter() + ":" + getReason() + ":" + getId();
    }

}
