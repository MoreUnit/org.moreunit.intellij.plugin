package org.naounit.intellij.plugin.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringsTest {

	@Test
	public void applySameCapitalization_should_handle_empty_strings() {
		Assert.assertEquals("", Strings.applySameCapitalization("", "bar"));
		Assert.assertEquals("foo", Strings.applySameCapitalization("foo", ""));
	}

	@Test
	public void applySameCapitalization_should_capitalize_target_string_when_sample_string_is_capitalized() {
		Assert.assertEquals("Foo", Strings.applySameCapitalization("foo", "Bar"));
		Assert.assertEquals("Foo", Strings.applySameCapitalization("Foo", "Bar"));
		Assert.assertEquals("Foo", Strings.applySameCapitalization("foo", "BAR"));
		Assert.assertEquals("FOO", Strings.applySameCapitalization("fOO", "Bar"));
	}

	@Test
	public void applySameCapitalization_should_uncapitalize_target_string_when_sample_string_is_not_capitalized() {
		Assert.assertEquals("foo", Strings.applySameCapitalization("foo", "bar"));
		Assert.assertEquals("foo", Strings.applySameCapitalization("Foo", "bar"));
		Assert.assertEquals("foo", Strings.applySameCapitalization("Foo", "bAR"));
		Assert.assertEquals("fOO", Strings.applySameCapitalization("FOO", "bar"));
	}

	@Test
	public void capitalize_should_handle_empty_strings_gracefully() {
		Assert.assertEquals("", Strings.capitalize(""));
	}

	@Test
	public void capitalize_should_upper_case_first_char_if_not_already_done() {
		Assert.assertEquals("Foo", Strings.capitalize("foo"));
		Assert.assertEquals("Bar", Strings.capitalize("Bar"));
	}

	@Test
	public void capitalize_should_keep_chars_after_first_as_is() {
		Assert.assertEquals("ABcDeF", Strings.capitalize("aBcDeF"));
	}
}