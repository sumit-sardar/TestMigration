package com.ctb.contentBridge.core.publish.tools;


public class Format {
    public static String format(String pattern, Object[] arguments) {
        StringBuffer buf = new StringBuffer();
        int index = 0;

        while (index < pattern.length()) {
            int openBrace = pattern.indexOf('{', index);

            if (openBrace == -1) {
                buf.append(pattern.substring(index));
                break;
            }

            String prefix = pattern.substring(index, openBrace);

            buf.append(prefix);

            int closeBrace = pattern.indexOf('}', openBrace);

            if (closeBrace == -1) {
                throw new IllegalArgumentException("Unterminated argument substitution string: "
                        + pattern.substring(openBrace));
            }
            String argumentNumber = pattern.substring(openBrace + 1, closeBrace);
            int argument = Integer.parseInt(argumentNumber);

            if (argument < 0 || argument >= arguments.length) {
                throw new IllegalArgumentException("Argument " + argumentNumber
                        + " out of range");
            }
            buf.append(arguments[argument]);
            index = closeBrace + 1;
        }
        return buf.toString();
    }
}
