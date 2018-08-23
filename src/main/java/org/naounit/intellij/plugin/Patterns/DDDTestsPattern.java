package org.naounit.intellij.plugin.Patterns;

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
        return new DDDTestsPattern("(?<root>src|upgrades)/?(?<bc>.*)/tests/1-unit/(?<path>.*/)?(.*)$");
    }

    public static DDDTestsPattern create(Matcher matcher, String filename) throws RuntimeException
    {
        if(! matcher.find())
        {
            throw new RuntimeException();
        }

        String path = matcher.group("path");
        if(path == null)
        {
            path = "";
        }

        return new DDDTestsPattern(String.format("%s/%s/tests/1-unit/%s%s$", matcher.group("root"), matcher.group("bc"), path, filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename)
    {
        return DDDSrcPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
