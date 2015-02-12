package org.moreunit.intellij.plugin.files;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class SubjectFileTest {

	@Test
	public void using_CamelCase_naming__should_ensure_letter_following_test_prefix_is_upper_cased() {
		assertFalse(new SubjectFile("ableThing").isCorrespondingFilename("TestableThing.txt"));
		assertFalse(new SubjectFile("TestableThing").isCorrespondingFilename("ableThing.txt"));
	}
}