package org.moreunit.intellij.plugin.files

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SubjectFileTest {

    @Test
    fun using_CamelCase_naming__should_ensure_letter_following_test_prefix_is_upper_cased() {
        assertFalse(SubjectFile("ableThing").isCorrespondingFilename("TestableThing.txt"))
        assertFalse(SubjectFile("TestableThing").isCorrespondingFilename("ableThing.txt"))
    }

    @Test
    fun should_consider_files_starting_with_dot_when_starting_from_a_file_with_leading_dot() {
        assertTrue(SubjectFile(".Hidden").isCorrespondingFilename(".HiddenTest"))
        assertTrue(SubjectFile(".HiddenTest").isCorrespondingFilename(".Hidden"))
    }

    @Test
    fun should_ignore_files_starting_with_dot_when_starting_from_a_file_without_leading_dot() {
        assertFalse(SubjectFile("Something").isCorrespondingFilename(".SomethingTest"))
        assertFalse(SubjectFile("SomethingTest").isCorrespondingFilename(".Something"))
    }

    @Test
    fun should_tell_whether_it_is_a_test_file() {
        assertFalse(SubjectFile("Something").isTestFile)
        assertFalse(SubjectFile("my-module").isTestFile)
        assertFalse(SubjectFile("some_file").isTestFile)
        assertFalse(SubjectFile("someFile").isTestFile)
        assertFalse(SubjectFile("someTestsOfClassX").isTestFile)

        assertTrue(SubjectFile("SomethingTest").isTestFile)
        assertTrue(SubjectFile("my-module-should").isTestFile)
        assertTrue(SubjectFile("spec_some_file").isTestFile)
        assertTrue(SubjectFile("someFile.spec").isTestFile)
        assertTrue(SubjectFile("testsForClassX").isTestFile)
        assertTrue(SubjectFile("ClassXyzTests").isTestFile)
    }
}