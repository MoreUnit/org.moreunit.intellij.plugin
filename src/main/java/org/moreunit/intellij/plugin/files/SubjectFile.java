package org.moreunit.intellij.plugin.files;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.moreunit.intellij.plugin.files.Files.withoutExtension;
import static org.moreunit.intellij.plugin.files.Files.withoutLeadingDot;
import static org.moreunit.intellij.plugin.util.Strings.applySameCapitalization;
import static org.moreunit.intellij.plugin.util.Strings.capitalize;

public class SubjectFile {

	// For the next 3 lists, order matters as it impacts evaluation: keep strings ordered from longest to shortest!
	private static final List<String> WORD_SEPARATORS = asList("-", "_", " ", "");
	private static final List<String> COMMON_TEST_PREFIXES_STR = asList("spec", "test");
	private static final List<String> COMMON_TEST_SUFFIXES_STR = asList("spec", "test", "should");

	private static final List<TestMarker> COMMON_TEST_PREFIXES;

	static {
		List<TestMarker> prefixes = new ArrayList<TestMarker>();
		for (String prefix : COMMON_TEST_PREFIXES_STR) {
			for (String separator : WORD_SEPARATORS) {
				prefixes.add(new TestMarker(prefix, separator));
			}
		}

		COMMON_TEST_PREFIXES = Collections.unmodifiableList(prefixes);
	}

	private static final List<TestMarker> COMMON_TEST_SUFFIXES;

	static {
		List<TestMarker> suffixes = new ArrayList<TestMarker>();
		for (String suffix : COMMON_TEST_SUFFIXES_STR) {
			for (String separator : WORD_SEPARATORS) {
				suffixes.add(new TestMarker(suffix, separator));
			}
		}

		COMMON_TEST_SUFFIXES = Collections.unmodifiableList(suffixes);
	}

	private final String fileNameWithoutExtension;
	private final TestMarker prefix;
	private final TestMarker suffix;
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
	private static TestMarker findTestPrefix(String name) {
		for (TestMarker prefix : COMMON_TEST_PREFIXES) {
			if (prefix.isPrefixIn(name)) {
				return prefix;
			}
		}
		return null;
	}

	@Nullable
	private static TestMarker findTestSuffix(String name) {
		for (TestMarker suffix : COMMON_TEST_SUFFIXES) {
			if (suffix.isSuffixIn(name)) {
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
		for (TestMarker suf : COMMON_TEST_SUFFIXES) {
			if (suf.isSuffixBetween(srcName, destName)) {
				return true;
			}
		}

		for (TestMarker pre : COMMON_TEST_PREFIXES) {
			if (pre.isPrefixBetween(srcName, destName)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCorrespondingProductionFilename(String srcName, String destName) {
		if (suffix != null && suffix.isSuffixBetween(destName, srcName)) {
			return true;
		}
		return prefix != null && prefix.isPrefixBetween(destName, srcName);
	}

	public boolean isTestFile() {
		return testFile;
	}

	private static class TestMarker {
		final String marker;
		final String separator;

		TestMarker(String marker, String separator) {
			this.marker = marker;
			this.separator = separator;
		}

		boolean isPrefixBetween(String base, String maybePrefixed) {
			if (isSeparatedByCase()) {
				return maybePrefixed.equals(applySameCapitalization(marker, maybePrefixed) + capitalize(base));
			}

			String prefixPart = marker + separator;
			if (maybePrefixed.length() != prefixPart.length() + base.length()) {
				return false;
			}

			String start = maybePrefixed.substring(0, prefixPart.length());
			String end = maybePrefixed.substring(prefixPart.length());
			return end.equals(base) && start.toLowerCase().equals(prefixPart);
		}

		boolean isPrefixIn(String name) {
			if (isSeparatedByCase()) {
				return name.startsWith(applySameCapitalization(marker, name));
			}
			return name.toLowerCase().startsWith(marker + separator);
		}

		boolean isSuffixBetween(String base, String maybeSuffixed) {
			if (isSeparatedByCase()) {
				return maybeSuffixed.equals(base + capitalize(marker));
			}

			String suffixPart = separator + marker;
			if (maybeSuffixed.length() != base.length() + suffixPart.length()) {
				return false;
			}

			String start = maybeSuffixed.substring(0, maybeSuffixed.length() - suffixPart.length());
			String end = maybeSuffixed.substring(maybeSuffixed.length() - suffixPart.length());
			return start.equals(base) && end.toLowerCase().equals(suffixPart);
		}

		boolean isSuffixIn(String name) {
			if (isSeparatedByCase()) {
				return name.endsWith(capitalize(marker));
			}
			return name.toLowerCase().endsWith(separator + marker);
		}

		boolean isSeparatedByCase() {
			return separator.equals("");
		}
	}
}