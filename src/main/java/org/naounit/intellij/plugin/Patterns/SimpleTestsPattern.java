package org.naounit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public class SimpleTestsPattern implements PathPattern
{
    private String pattern;

    private SimpleTestsPattern(String pattern)
    {
        this.pattern = pattern;
    }

    public static SimpleTestsPattern create()
    {
        return new SimpleTestsPattern("tests/?(?<path>.*/)?(.*)$");
    }

    public static SimpleTestsPattern create(Matcher matcher, String $filename) throws RuntimeException
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

        return new SimpleTestsPattern(String.format("tests/%s%s$", path, $filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename)
    {
        return SimpleSrcPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
