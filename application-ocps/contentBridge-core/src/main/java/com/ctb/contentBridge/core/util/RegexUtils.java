package com.ctb.contentBridge.core.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

import com.ctb.contentBridge.core.exception.SystemException;

public class RegexUtils {
    public static String getMatchedGroup(
        String expression,
        String string,
        int group) {
        Pattern pattern = getPattern(expression);

        PatternMatcher matcher = new Perl5Matcher();
        if (matcher.matches(string, pattern)) {
            return matcher.getMatch().group(group);
        }

        return null;
    }

    private static Pattern getPattern(String expression) {
        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = null;

        try {
            pattern =
                compiler.compile(
                    expression,
                    Perl5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException e) {
            throw new SystemException(
                "Cannot reconize regex: "
                    + expression
                    + " in environment properties.");
        }
        return pattern;
    }

    public static List getAllMatchedGroups(
        String expression,
        String string,
        String chomp) {
        Pattern pattern = getPattern(expression);

        PatternMatcher matcher = new Perl5Matcher();
        List result = new ArrayList();

        PatternMatcherInput input = new PatternMatcherInput(string);
        while (matcher.contains(input, pattern)) {
            result.add(StringUtils.chomp(matcher.getMatch().group(1), chomp));
        }

        return result;
    }

    public static boolean match(String expression, String string) {
        Pattern pattern = getPattern(expression);
        PatternMatcher matcher = new Perl5Matcher();
        return (matcher.matches(string, pattern));
    }
}
