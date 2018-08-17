
package org.moreunit.intellij.plugin.Patterns;

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
        return new DDDSrcPattern("src/?(?<bc>.*)/src/(?<path>.*)/(.*)$");
    }

    public static DDDSrcPattern create(Matcher matcher, String filename) throws RuntimeException
    {
        if(! matcher.find())
        {
            throw new RuntimeException();
        }

        return new DDDSrcPattern(String.format("src/%s/src/%s/%s$", matcher.group("bc"), matcher.group("path"), filename));
    }

    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename) {
        return DDDTestsPattern.create(matcher, filename);
    }

    public String toString()
    {
        return this.pattern;
    }
}
