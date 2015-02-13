package org.moreunit.intellij.plugin.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.moreunit.intellij.plugin.util.Strings.applySameCapitalization;
import static org.moreunit.intellij.plugin.util.Strings.capitalize;

public class StringsTest {

	@Test
	public void applySameCapitalization_should_handle_empty_strings() {
		assertEquals("", applySameCapitalization("", "bar"));
		assertEquals("foo", applySameCapitalization("foo", ""));
	}

	@Test
	public void applySameCapitalization_should_capitalize_target_string_when_sample_string_is_capitalized() {
		assertEquals("Foo", applySameCapitalization("foo", "Bar"));
		assertEquals("Foo", applySameCapitalization("Foo", "Bar"));
		assertEquals("Foo", applySameCapitalization("foo", "BAR"));
		assertEquals("FOO", applySameCapitalization("fOO", "Bar"));
	}

	@Test
	public void applySameCapitalization_should_uncapitalize_target_string_when_sample_string_is_not_capitalized() {
		assertEquals("foo", applySameCapitalization("foo", "bar"));
		assertEquals("foo", applySameCapitalization("Foo", "bar"));
		assertEquals("foo", applySameCapitalization("Foo", "bAR"));
		assertEquals("fOO", applySameCapitalization("FOO", "bar"));
	}

	@Test
	public void capitalize_should_handle_empty_strings_gracefully() {
		assertEquals("", capitalize(""));
	}

	@Test
	public void capitalize_should_upper_case_first_char_if_not_already_done() {
		assertEquals("Foo", capitalize("foo"));
		assertEquals("Bar", capitalize("Bar"));
	}

	@Test
	public void capitalize_should_keep_chars_after_first_as_is() {
		assertEquals("ABcDeF", capitalize("aBcDeF"));
	}
}