package fun.onysakura.algorithm.utils.core.basic.str.color;

@SuppressWarnings("unused")
public class AnsiColorUtil {

    private static final String TEMPLATE = "\033[%dm%s\033[" + AnsiColorType.CLEAR.getCode() + "m";

    public static String setColor(String str, AnsiColor color) {
        return setColor(str, color, AnsiColorType.FOREGROUND);
    }

    public static String setColor(String str, AnsiColorType... types) {
        for (AnsiColorType type : types) {
            str = String.format(TEMPLATE, type.getCode(), str);
        }
        return str;
    }

    public static String setColor(String str, AnsiColor color, AnsiColorType type) {
        return String.format(TEMPLATE, type.getCode() + color.ordinal(), str);
    }

    public static String setColor(String str, AnsiColor color, AnsiColorType... types) {
        for (AnsiColorType type : types) {
            str = String.format(TEMPLATE, type.getCode() + color.ordinal(), str);
        }
        return str;
    }

    public static String setColor(String str, AnsiColorTypes... types) {
        for (AnsiColorTypes ts : types) {
            if (ts.getColor() == null) {
                str = String.format(TEMPLATE, ts.getType().getCode(), str);
            } else {
                str = String.format(TEMPLATE, ts.getType().getCode() + ts.getColor().ordinal(), str);
            }
        }
        return str;
    }
}
