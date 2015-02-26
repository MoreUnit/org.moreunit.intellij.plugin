#!/bin/bash -e

readonly VERSION_TO_RELEASE=$(git tag -l --contains HEAD | grep -E '^v[0-9]+\.[0-9]+\.[0-9]+' | sed 's/v\(.*\)/\1/')

if [[ -n "${VERSION_TO_RELEASE}" ]]; then
	mvn versions:set -DnewVersion=${VERSION_TO_RELEASE} -DgenerateBackupPoms=false
else
	echo "No version tag detected, nothing to release."
fi

./build.sh

if [[ -n "${VERSION_TO_RELEASE}" ]]; then
	readonly RELEASE_NOTES="$(git show "v${VERSION_TO_RELEASE}" | tail -n+5 | grep -B 99 -E '^commit [0-9a-f]{40}' | grep -v -E '^commit [0-9a-f]{40}')"
	./upload-plugin.sh "${VERSION_TO_RELEASE}" "${RELEASE_NOTES}"
fi

