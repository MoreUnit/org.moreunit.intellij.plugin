#!/bin/bash

if [ ! -d ../idea-ce ]; then
  # the repo is huge, let's only get its last state
  git clone --depth 1 git://git.jetbrains.org/idea/community.git ../idea-ce
fi

