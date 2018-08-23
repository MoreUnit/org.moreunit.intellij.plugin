package org.naounit.intellij.plugin.Patterns;

import java.util.regex.Matcher;

public interface PathPattern
{
    public PathPattern createTargetPatternFromMatcher(Matcher matcher, String filename);

    public String toString();
}
