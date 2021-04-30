package com.onysakura.algorithm.utilities.basic.str.color;

public class AnsiColorTypes {

    private AnsiColor color;
    private AnsiColorType type;

    public static AnsiColorTypes build(AnsiColor color, AnsiColorType type) {
        AnsiColorTypes ansiColorTypes = new AnsiColorTypes();
        ansiColorTypes.color = color;
        ansiColorTypes.type = type;
        return ansiColorTypes;
    }

    public static AnsiColorTypes build(AnsiColorType type) {
        AnsiColorTypes ansiColorTypes = new AnsiColorTypes();
        ansiColorTypes.color = null;
        ansiColorTypes.type = type;
        return ansiColorTypes;
    }

    public AnsiColor getColor() {
        return color;
    }

    public AnsiColorType getType() {
        return type;
    }

    public void setColor(AnsiColor color) {
        this.color = color;
    }

    public void setType(AnsiColorType type) {
        this.type = type;
    }
}
