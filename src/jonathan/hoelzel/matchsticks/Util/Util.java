package jonathan.hoelzel.matchsticks.Util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
    public static <T, U, V> String gridToStringWithBorderNumbers(
            Grid<T> grid,
            List<U> perRowBorder,
            List<V> perColumnBorder,
            int widthPerElement) {
        StringBuilder myBuilder = new StringBuilder();
        for (int y = 0; y < grid.height(); y ++) {
            for (int x = 0; x < grid.width(); x ++) {
                myBuilder.append(centered(grid.get(x, y).toString(), widthPerElement));
            }
            myBuilder.append(" " + perRowBorder.get(y) + "\n");
        }

        for (int x = 0; x < grid.width(); x ++) {
            myBuilder.append(centered(perColumnBorder.get(x).toString(), widthPerElement));
        }

        return myBuilder.toString();
    }

    private static String centered(String str, int length) {
        assert length >= str.length();

        int whitespace = length - str.length();
        int leftWhitespace = whitespace / 2;
        int rightWhitespace = whitespace - leftWhitespace;

        return spaces(leftWhitespace) + str + spaces(rightWhitespace);
    }

    private static String spaces(int length) {
        if (length == 0) return "";
        return " " + spaces(length - 1);
    }

    public static <U, T> List<List<U>> transform(List<List<T>> input, Function<T, U> map) {
        return input.stream()
                .map(list -> list.stream().map(map).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
