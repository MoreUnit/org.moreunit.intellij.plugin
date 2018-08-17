package org.moreunit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public class DDDTestsPattern implements PathPattern
{
    private String pattern;

    private DDDTestsPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public static DDDTestsPattern create()
    {
        return new DDDTestsPattern("src/?(?<bc>.*)/tests/unit/(?<path>.*)/(.*)$");
    }

    public static DDDTestsPattern create(Matcher matcher, String filename) throws RuntimeException
    {
        if(! matcher.find())
        {
            throw new RuntimeException();
        }

        return new DDDTestsPattern(String.format("src/%s/tests/unit/%s/%s$", matcher.group("bc"), matcher.group("path"), filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename) {
        return DDDSrcPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
