package fun.onysakura.algorithm.utils.core.basic.str;


import fun.onysakura.algorithm.utils.core.basic.str.color.AnsiColor;
import fun.onysakura.algorithm.utils.core.basic.str.color.AnsiColorType;
import fun.onysakura.algorithm.utils.core.basic.str.color.AnsiColorTypes;
import fun.onysakura.algorithm.utils.core.basic.str.color.AnsiColorUtil;
import org.junit.jupiter.api.Test;

public class ColorTest {

    @Test
    public void color() {
        System.out.println();
        for (AnsiColorType type : AnsiColorType.values()) {
            System.out.print(AnsiColorUtil.setColor(type.name(), type) + "  ");
        }
        System.out.println("\n");

        System.out.println(AnsiColorUtil.setColor("CYAN LIGHT_BACKGROUND", AnsiColor.CYAN, AnsiColorType.LIGHT_BACKGROUND));
        System.out.println();

        System.out.println(AnsiColorUtil.setColor("WHITE LIGHT_BACKGROUND, BLUE LIGHT_FOREGROUND, BOLD",
                AnsiColorTypes.build(AnsiColor.WHITE, AnsiColorType.LIGHT_BACKGROUND),
                AnsiColorTypes.build(AnsiColor.BLUE, AnsiColorType.LIGHT_FOREGROUND),
                AnsiColorTypes.build(AnsiColorType.BOLD)
        ));
        System.out.println();

        System.out.println(AnsiColorUtil.setColor("BLUE LIGHT_BACKGROUND, WHITE LIGHT_FOREGROUND, ITALIC, REVERSE",
                AnsiColorTypes.build(AnsiColor.WHITE, AnsiColorType.LIGHT_BACKGROUND),
                AnsiColorTypes.build(AnsiColor.BLUE, AnsiColorType.LIGHT_FOREGROUND),
                AnsiColorTypes.build(AnsiColorType.ITALIC),
                AnsiColorTypes.build(AnsiColorType.REVERSE)
        ));
    }
}
