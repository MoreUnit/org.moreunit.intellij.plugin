package org.moreunit.intellij.plugin.files;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.moreunit.intellij.plugin.files.Files.withoutExtension;
import static org.moreunit.intellij.plugin.files.Files.withoutLeadingDot;

public class FilesTest {

	@Test
	public void withoutExtension_should_return_file_name_without_last_do_separated_part() {
		assertEquals("some/file", withoutExtension("some/file.ext"));
	}

	@Test
	public void withoutExtension_should_return_whole_file_name_when_it_has_no_extension() {
		assertEquals("some/file-without-ext", withoutExtension("some/file-without-ext"));
	}

	@Test
	public void withoutExtension_should_only_remove_last_dot_separated_part() {
		assertEquals("some.file", withoutExtension("some.file.ext"));
	}

	@Test
	public void withoutExtension_should_handle_files_starting_with_dot() {
		assertEquals(".file", withoutExtension(".file"));
		assertEquals(".file", withoutExtension(".file.ext"));
	}

	@Test
	public void withoutLeadingDot_should_remove_leading_dot() {
		assertEquals("file", withoutLeadingDot(".file"));
	}

	@Test
	public void withoutLeadingDot_should_keep_non_leading_dots() {
		assertEquals("file.ext", withoutLeadingDot(".file.ext"));
		assertEquals("file.ext", withoutLeadingDot("file.ext"));
	}
}