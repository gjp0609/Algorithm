package com.onysakura.algorithm.utilities.basic.str.color;

public enum AnsiColorType {
    CLEAR(0),
    BOLD(1),
    ITALIC(3),
    UNDERLINE(4),
    REVERSE(7),
    CROSSED_OUT(9),
    FOREGROUND(30),
    BACKGROUND(40),
    FRAMED(51),
    ENCIRCLED(51),
    LIGHT_FOREGROUND(90),
    LIGHT_BACKGROUND(100),
    ;

    /**
     * @param code ansi code or begin code
     */
    AnsiColorType(int code) {
        this.code = code;
    }

    private final int code;

    public int getCode() {
        return code;
    }
}
