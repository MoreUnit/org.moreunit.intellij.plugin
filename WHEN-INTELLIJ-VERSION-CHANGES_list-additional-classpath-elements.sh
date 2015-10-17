#!/bin/bash

INTELLIJ_SEMVER=14.1.5
INTELLIJ_FULLVER=141.2735.5

# to be adapted for next version
tar tvf target/dependencies/intellij-ce-${INTELLIJ_SEMVER}.tar.gz \
  | grep -E "idea-IC-${INTELLIJ_FULLVER}/lib/[^/]+.jar" \
  | sed -r "s/.*idea-IC-${INTELLIJ_FULLVER}\/lib\/([^\/]+).jar/"'            <additionalClasspathElement>${project.build.directory}\/dependency\/IntelliJ-IDEA-CE\/lib\/\1.jar<\/additionalClasspathElement>/g'
