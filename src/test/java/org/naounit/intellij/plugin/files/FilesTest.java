package org.naounit.intellij.plugin.files;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FilesTest {

	@Test
	public void withoutExtension_should_return_file_name_without_last_do_separated_part() {
		Assert.assertEquals("some/file", Files.withoutExtension("some/file.ext"));
	}

	@Test
	public void withoutExtension_should_return_whole_file_name_when_it_has_no_extension() {
		Assert.assertEquals("some/file-without-ext", Files.withoutExtension("some/file-without-ext"));
	}

	@Test
	public void withoutExtension_should_only_remove_last_dot_separated_part() {
		Assert.assertEquals("some.file", Files.withoutExtension("some.file.ext"));
	}

	@Test
	public void withoutExtension_should_handle_files_starting_with_dot() {
		Assert.assertEquals(".file", Files.withoutExtension(".file"));
		Assert.assertEquals(".file", Files.withoutExtension(".file.ext"));
	}

	@Test
	public void withoutLeadingDot_should_remove_leading_dot() {
		Assert.assertEquals("file", Files.withoutLeadingDot(".file"));
	}

	@Test
	public void withoutLeadingDot_should_keep_non_leading_dots() {
		Assert.assertEquals("file.ext", Files.withoutLeadingDot(".file.ext"));
		Assert.assertEquals("file.ext", Files.withoutLeadingDot("file.ext"));
	}
}