package org.naounit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public class DDDSrcPattern implements PathPattern
{
    private String pattern;

    private DDDSrcPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public static DDDSrcPattern create()
    {
        return new DDDSrcPattern("(?<root>src|upgrades)/?(?<bc>.*)/src/(?<path>.*/)?(.*)$");
    }

    public static DDDSrcPattern create(Matcher matcher, String filename) throws RuntimeException
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

        return new DDDSrcPattern(String.format("%s/%s/src/%s%s$", matcher.group("root"), matcher.group("bc"), path, filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename)
    {
        return DDDTestsPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
