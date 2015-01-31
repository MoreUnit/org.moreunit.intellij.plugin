#!/bin/bash

# attempt at understanding why Travis caching doesn't work
echo "travis env var: $TRAVIS_BUILD_DIR/../idea-ce"
echo "pwd: $(pwd)/../idea-ce"

if [ ! -d ../idea-ce ]; then
  # the repo is huge, let's only get its last state
  git clone --depth 1 git://git.jetbrains.org/idea/community.git ../idea-ce
fi

