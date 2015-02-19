#!/bin/bash -e

readonly VERSION_TO_RELEASE=$(git tag -l --contains HEAD | grep -E '^v[0-9]+\.[0-9]+\.[0-9]+' | sed 's/v\(.*\)/\1/')

if [[ -n "${VERSION_TO_RELEASE}" ]]; then
	mvn versions:set -DnewVersion=${VERSION_TO_RELEASE} -DgenerateBackupPoms=false
else
	echo "No version tag detected, nothing to release."
fi

./build.sh
