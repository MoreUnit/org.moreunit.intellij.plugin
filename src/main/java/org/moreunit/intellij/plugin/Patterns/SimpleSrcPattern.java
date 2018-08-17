package org.moreunit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public class SimpleSrcPattern implements PathPattern
{
    private String pattern;

    private SimpleSrcPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public static SimpleSrcPattern create()
    {
        return new SimpleSrcPattern("src/?(?<path>.*)/(.*)$");
    }

    public static SimpleSrcPattern create(Matcher matcher, String filename) throws RuntimeException
    {
        if(! matcher.find())
        {
            throw new RuntimeException();
        }

        return new SimpleSrcPattern(String.format("src/%s/%s$", matcher.group("path"), filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename) {
        return SimpleTestsPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
