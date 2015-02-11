package org.moreunit.intellij.plugin.files;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static org.moreunit.intellij.plugin.files.Files.withoutExtension;

public class SubjectFile {

	private static final List<String> COMMON_TEST_SUFFIXES = asList("Spec", "Test");

	private final String fileNameWithoutExtension;
	private final String suffix;
	private final boolean testFile;

	public SubjectFile(VirtualFile srcVFile) {
		fileNameWithoutExtension = srcVFile.getNameWithoutExtension();
		suffix = findTestSuffix(fileNameWithoutExtension);
		testFile = suffix != null;
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

		if (testFile) {
			return isSuffixBetween(destName, srcName).test(suffix);
		}
		return COMMON_TEST_SUFFIXES.stream().anyMatch(isSuffixBetween(srcName, destName));
	}

	private static Predicate<String> isSuffixBetween(String base, String maybeSuffixed) {
		return suf -> maybeSuffixed.equals(base + suf);
	}
}