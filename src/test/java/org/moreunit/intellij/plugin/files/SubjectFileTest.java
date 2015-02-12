package org.moreunit.intellij.plugin.files;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SubjectFileTest {

	@Test
	public void using_CamelCase_naming__should_ensure_letter_following_test_prefix_is_upper_cased() {
		assertFalse(new SubjectFile("ableThing").isCorrespondingFilename("TestableThing.txt"));
		assertFalse(new SubjectFile("TestableThing").isCorrespondingFilename("ableThing.txt"));
	}

	@Test
	public void should_consider_files_starting_with_dot_when_starting_from_a_file_with_leading_dot() {
		assertTrue(new SubjectFile(".Hidden").isCorrespondingFilename(".HiddenTest"));
		assertTrue(new SubjectFile(".HiddenTest").isCorrespondingFilename(".Hidden"));
	}

	@Test
	public void should_ignore_files_starting_with_dot_when_starting_from_a_file_without_leading_dot() {
		assertFalse(new SubjectFile("Something").isCorrespondingFilename(".SomethingTest"));
		assertFalse(new SubjectFile("SomethingTest").isCorrespondingFilename(".Something"));
	}
}