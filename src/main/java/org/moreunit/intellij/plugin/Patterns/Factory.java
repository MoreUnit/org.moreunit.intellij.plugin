package org.moreunit.intellij.plugin.Patterns;

import org.moreunit.intellij.plugin.files.SubjectFile;

public class Factory
{
    public PathPattern create(SubjectFile subject)
    {
        PathPattern pattern = SimpleSrcPattern.create();
        if(subject.isTestFile())
        {
             pattern = SimpleTestsPattern.create();
        }

        return pattern;
    }
}
