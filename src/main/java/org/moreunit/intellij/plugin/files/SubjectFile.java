package org.moreunit.intellij.plugin.files;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Character.isUpperCase;
import static java.util.Arrays.asList;
import static org.moreunit.intellij.plugin.files.Files.withoutExtension;
import static org.moreunit.intellij.plugin.files.Files.withoutLeadingDot;
import static org.moreunit.intellij.plugin.util.Strings.applySameCapitalization;
import static org.moreunit.intellij.plugin.util.Strings.capitalize;

public class SubjectFile {

	private static final List<String> COMMON_TEST_PREFIXES = asList("spec", "test");
	private static final List<String> COMMON_TEST_SUFFIXES = asList("spec", "test", "should");

	private final String fileNameWithoutExtension;
	private final String prefix;
	private final String suffix;
	private final boolean testFile;

	public SubjectFile(VirtualFile srcVFile) {
		this(srcVFile.getNameWithoutExtension());
	}

	public SubjectFile(String fileNameWithoutExtension) {
		this.fileNameWithoutExtension = fileNameWithoutExtension;
		prefix = findTestPrefix(fileNameWithoutExtension);
		suffix = findTestSuffix(fileNameWithoutExtension);
		testFile = prefix != null || suffix != null;
	}

	@Nullable
	private static String findTestPrefix(String name) {
		for (String prefix : COMMON_TEST_PREFIXES) {
			if (name.startsWith(applySameCapitalization(prefix, name))) {
				return prefix;
			}
		}
		return null;
	}

	@Nullable
	private static String findTestSuffix(String name) {
		for (String suffix : COMMON_TEST_SUFFIXES) {
			if (name.endsWith(capitalize(suffix))) {
				return suffix;
			}
		}
		return null;
	}

	public boolean isCorrespondingFilename(String name) {
		String srcName = fileNameWithoutExtension;
		String destName = withoutExtension(name);

		// Let's assume that if the user triggered an action from an hidden file,
		// she actually wanted to do something with it...
		if (srcName.startsWith(".")) {
			if (!destName.startsWith(".")) {
				return false;
			}
			srcName = withoutLeadingDot(srcName);
			destName = withoutLeadingDot(destName);
		}

		if (isUpperCase(srcName.charAt(0)) != isUpperCase(destName.charAt(0))) {
			return false;
		}

		if (testFile) {
			return isCorrespondingProductionFilename(srcName, destName);
		}
		return isCorrespondingTestFilename(srcName, destName);
	}

	private boolean isCorrespondingTestFilename(String srcName, String destName) {
		for (String suf : COMMON_TEST_SUFFIXES) {
			if (isSuffixBetween(suf, srcName, destName)) {
				return true;
			}
		}

		for (String pre : COMMON_TEST_PREFIXES) {
			if (isPrefixBetween(pre, srcName, destName)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCorrespondingProductionFilename(String srcName, String destName) {
		if (suffix != null && isSuffixBetween(suffix, destName, srcName)) {
			return true;
		}
		return prefix != null && isPrefixBetween(prefix, destName, srcName);
	}

	private static boolean isPrefixBetween(String pre, String base, String maybePrefixed) {
		return maybePrefixed.equals(applySameCapitalization(pre, maybePrefixed) + capitalize(base));
	}

	private static boolean isSuffixBetween(String suf, String base, String maybeSuffixed) {
		return maybeSuffixed.equals(base + capitalize(suf));
	}
}