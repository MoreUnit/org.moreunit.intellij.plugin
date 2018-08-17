package org.moreunit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public interface PathPattern
{
    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename);

    public String toString();
}
