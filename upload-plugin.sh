#!/bin/bash

if [ "$1" == "-h" -o "$1" == "--help" ]; then
	echo "Usage: $0 PLUGIN_VERSION RELEASE_NOTES"
	echo "    PLUGIN_VERSION: vX.Y.Z"
	exit 0
fi

readonly JETBRAINS_PLUGIN_ID=7105
readonly COOKIES_FILE=cookies.txt

readonly PLUGIN_VERSION="$1"
[ -z "${PLUGIN_VERSION}" ] && echo "Plugin version required." && exit 1

readonly RELEASE_NOTES="$2"
[ -z "${RELEASE_NOTES}" ] && echo "Release notes required." && exit 1

readonly PLUGIN_JAR=target/org.moreunit.intellij.plugin-${PLUGIN_VERSION}.jar
[ ! -f "${PLUGIN_JAR}" ] && echo "File not found: ${PLUGIN_JAR}" && exit 1

[ -z "${JETBRAINS_USER}" ] && echo "JETBRAINS_USER undefined" && exit 1
[ -z "${JETBRAINS_PWD}" ] && echo "JETBRAINS_PWD undefined" && exit 1


# login
curl --request POST \
	--include \
	--fail \
	--cookie-jar "${COOKIES_FILE}" \
	--data j_username="${JETBRAINS_USER}" \
	--data j_password="${JETBRAINS_PWD}" \
	--data _spring_security_remember_me=on \
	https://plugins.jetbrains.com/j_spring_security_check | grep -v 'Location: .*/login/authfail'

[ $? -ne 0 ] && echo "Could not log-in to JetBrains website" && exit 1


# upload
# The space before RELEASE_NOTES is intentional, it prevent curl to search for
# a file, as release notes start with '<'
curl --request POST \
	--include \
	--fail \
	--cookie "${COOKIES_FILE}" \
	--form pluginId="${JETBRAINS_PLUGIN_ID}" \
	--form file=@"${PLUGIN_JAR}" \
	--form notes=" <pre>${RELEASE_NOTES}</pre>" \
	https://plugins.jetbrains.com/plugin/uploadPlugin

[ $? -ne 0 ] && echo "Could not upload plugin to JetBrains repository" && exit 1


echo "Plugin should be uploaded to JetBrains repository. That said, you should
check by yourself at https://plugins.jetbrains.com/plugin/7105"

