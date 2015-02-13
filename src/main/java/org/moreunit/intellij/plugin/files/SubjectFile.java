package org.moreunit.intellij.plugin.files;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.moreunit.intellij.plugin.files.Files.withoutExtension;
import static org.moreunit.intellij.plugin.files.Files.withoutLeadingDot;

public class SubjectFile {

	private static final List<String> COMMON_TEST_PREFIXES = asList("Test");
	private static final List<String> COMMON_TEST_SUFFIXES = asList("Spec", "Test");

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
			if (name.startsWith(prefix)) {
				return prefix;
			}
		}
		return null;
	}

	@Nullable
	private static String findTestSuffix(String name) {
		for (String suffix : COMMON_TEST_SUFFIXES) {
			if (name.endsWith(suffix)) {
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

		if (testFile) {
			return isCorrespondingProductionFilename(srcName, destName);
		}
		return isCorrespondingTestFilename(srcName, destName);
	}

	private boolean isCorrespondingTestFilename(String srcName, String destName) {
		if (!Character.isUpperCase(srcName.charAt(0))) {
			return false;
		}
		if (COMMON_TEST_SUFFIXES.stream().anyMatch(isSuffixBetween(srcName, destName))) {
			return true;
		}
		return COMMON_TEST_PREFIXES.stream().anyMatch(isPrefixBetween(srcName, destName));
	}

	private boolean isCorrespondingProductionFilename(String srcName, String destName) {
		if (!Character.isUpperCase(destName.charAt(0))) {
			return false;
		}
		if (isSuffixBetween(destName, srcName).test(suffix)) {
			return true;
		}
		return isPrefixBetween(destName, srcName).test(prefix);
	}

	private static Predicate<String> isPrefixBetween(String base, String maybePrefixed) {
		return pre -> maybePrefixed.equals(pre + base);
	}

	private static Predicate<String> isSuffixBetween(String base, String maybeSuffixed) {
		return suf -> maybeSuffixed.equals(base + suf);
	}
}