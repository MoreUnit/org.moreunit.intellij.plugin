package org.naounit.intellij.plugin.actions;

import com.intellij.codeInsight.navigation.GotoTargetHandler.GotoData;
import com.intellij.openapi.vfs.VirtualFile;
import org.naounit.intellij.plugin.fixtures.JumpActionHandlerTestCase;

/**
 * Almost full integration tests (headless), specifying the action main behavior. More specific
 * features are covered by unit tests.
 * <p/>
 * Note that the "handler" associated to the action is invoked directly, because within test mode:
 * <ul>
 * <li>focusing editors does not seem to work (thus we can't test whether the destination file has
 * been opened)
 * <li>it is impossible which file to jump to when several candidates are displayed
 * <li>IntelliJ fails to display a hint in a non-displayed editor when no destination files are
 * found, leading to the following exception:
 * <tt>NullPointerException at com.intellij.codeInsight.hint.HintManagerImpl.getHintPositionRelativeTo</tt>
 * </ul>
 */
public class JumpToTestOrCodeHandlerTest extends JumpActionHandlerTestCase {

	public void test__using_CamelCase_naming_and_Test_suffix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/pack/Foo.java", "package pack; public class Foo() {}");
		VirtualFile testFile = mainModule.addFile("test1/pack/FooTest.java", "package pack; public class FooTest() {}");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);
	}

	public void test_should_fail_silently_when_no_corresponding_file_is_found() {
		// given
		VirtualFile srcFileWithoutTestCounterpart = mainModule.addFile("src1/FileWithoutTestCounterpart.java",
				"public class FileWithoutTestCounterpart {}");

		// when
		GotoData gotoData = jumpFrom(srcFileWithoutTestCounterpart);

		// then no exception is thrown, and:
		assertEquals(0, gotoData.targets.length);
	}

	public void test__using_CamelCase_naming_and_Test_suffix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/pack/Foo.java", "package pack; public class Foo() {}");
		VirtualFile testFile = mainModule.addFile("test1/pack/FooTest.java", "package pack; public class FooTest() {}");

		// when
		GotoData gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_CamelCase_naming_and_Spec_suffix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Something.rb");
		VirtualFile testFile = mainModule.addFile("test1/SomethingSpec.rb");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);
	}

	public void test__using_CamelCase_naming_and_Spec_suffix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Something.rb");
		VirtualFile testFile = mainModule.addFile("test1/SomethingSpec.rb");

		// when
		GotoData gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_CamelCase_naming_and_Test_prefix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Something.cpp");
		VirtualFile testFile = mainModule.addFile("test1/TestSomething.cpp");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);
	}

	public void test__using_CamelCase_naming_and_Test_prefix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Something.cpp");
		VirtualFile testFile = mainModule.addFile("test1/TestSomething.cpp");

		// when
		GotoData gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test_should_handle_lowerCamelCase() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/something.js");
		VirtualFile testFile = mainModule.addFile("test1/testSomething.js");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_CamelCase_naming_and_Spec_prefix() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/powers/Baby.txt");
		VirtualFile testFile = mainModule.addFile("test1/powers/SpecBaby.txt");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_CamelCase_naming_and_Should_suffix() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/pack/AThing.java");
		VirtualFile testFile = mainModule.addFile("test1/pack/AThingShould.java");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_hyphen_separator_and_test_suffix() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/some/module.js");
		VirtualFile testFile = mainModule.addFile("test1/some/module-test.js");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_hyphen_separator_and_test_prefix() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/some/module.js");
		VirtualFile testFile = mainModule.addFile("test1/some/test-module.js");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_underscore_separator_and_spec_suffix() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/concept.js");
		VirtualFile testFile = mainModule.addFile("test1/concept_spec.js");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_space_separator() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/concept.js");
		VirtualFile testFile = mainModule.addFile("test1/spec concept.js");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_separator__should_ignore_suffix_case() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Concept.txt");
		VirtualFile testFile = mainModule.addFile("test1/Concept Spec.txt");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__using_separator__should_ignore_prefix_case() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src1/Concept.txt");
		VirtualFile testFile = mainModule.addFile("test1/Spec_Concept.txt");

		// when
		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, testFile);

		// when
		gotoData = jumpFrom(testFile);

		// then
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test__regardless_of_source_folders__should_put_destination_files_having_same_extension_in_first_place_when_several_matches() throws Exception {
		// given (by default, without sorting files, this situation makes the test fail)
		VirtualFile srcFile = mainModule.addFile("SomeConcept.js");
		VirtualFile maybeTestFile1 = mainModule.addFile("SomeConceptSpec.rb");
		VirtualFile maybeTestFile2 = mainModule.addFile("SomeConceptSpec.js");

		GotoData gotoData = jumpFrom(srcFile);

		// then
		assertTargetFilesInOrder(gotoData, maybeTestFile2, maybeTestFile1);
	}

	private GotoData jumpFrom(VirtualFile startFile) {
		openFileInEditor(startFile);
		return invokeJumpActionHandler();
	}

	private GotoData invokeJumpActionHandler() {
		return new JumpToTestOrCodeHandler().getSourceAndTargetElements(lastOpenedEditor, psiFileFor(lastOpenedFile));
	}
}