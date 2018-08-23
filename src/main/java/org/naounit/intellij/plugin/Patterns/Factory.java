package org.naounit.intellij.plugin.Patterns;

import org.naounit.intellij.plugin.files.SubjectFile;

import java.util.ArrayList;
import java.util.List;

public class Factory
{
    public PathPattern[] create(SubjectFile subject)
    {
        List<PathPattern> patterns = new ArrayList<PathPattern>();

        if(subject.isTestFile())
        {
             patterns.add(DDDTestsPattern.create());
             patterns.add(SimpleTestsPattern.create());
        }
        else
        {
            patterns.add(DDDSrcPattern.create());
            patterns.add(SimpleSrcPattern.create());
        }

        return patterns.toArray(new PathPattern[patterns.size()]);
    }
}
