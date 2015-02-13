package org.moreunit.intellij.plugin.actions;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.moreunit.intellij.plugin.fixtures.MoreUnitTestCase;

/**
 * Almost full integration tests (headless), specifying the action main behavior. More specific
 * features are covered by unit tests.
 * <p>
 * Note that the "handler" associated to the action may be invoked directly to prevent an error
 * when IntelliJ attempt to display a hint in a non-displayed editor.
 */
public class JumpToTestOrCodeActionTest extends MoreUnitTestCase {

	public void test__using_CamelCase_naming_and_Test_suffix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/pack/Foo.java", "package pack; public class Foo() {}");
		VirtualFile testFile = mainModule.addFile("test/pack/FooTest.java", "package pack; public class FooTest() {}");

		openFileInEditor(srcFile);

		// when
		performJumpAction();

		// then
		assertEquals(testFile, getEditedFile());
	}

	public void test_should_fail_silently_when_no_corresponding_file_is_found() {
		// given
		VirtualFile srcFileWithoutTestCounterpart = mainModule.addFile("src/FileWithoutTestCounterpart.java",
				"public class FileWithoutTestCounterpart {}");

		Editor editor = openFileInEditor(srcFileWithoutTestCounterpart);

		// when
		// (handler is invoked directly to prevent an error when IntelliJ attempt to display a hint in a non-displayed editor)
		new JumpToTestOrCodeHandler().getSourceAndTargetElements(editor, psiFileFor(srcFileWithoutTestCounterpart));

		// then no exception is thrown, and:
		assertEquals(srcFileWithoutTestCounterpart, getEditedFile());
	}

	public void test__using_CamelCase_naming_and_Test_suffix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/pack/Foo.java", "package pack; public class Foo() {}");
		VirtualFile testFile = mainModule.addFile("test/pack/FooTest.java", "package pack; public class FooTest() {}");

		openFileInEditor(testFile);

		// when
		performJumpAction();

		// then
		assertEquals(srcFile, getEditedFile());
	}

	public void test__using_CamelCase_naming_and_Spec_suffix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/Something.rb");
		VirtualFile testFile = mainModule.addFile("test/SomethingSpec.rb");

		openFileInEditor(srcFile);

		// when
		performJumpAction();

		// then
		assertEquals(testFile, getEditedFile());
	}

	public void test__using_CamelCase_naming_and_Spec_suffix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/Something.rb");
		VirtualFile testFile = mainModule.addFile("test/SomethingSpec.rb");

		openFileInEditor(testFile);

		// when
		performJumpAction();

		// then
		assertEquals(srcFile, getEditedFile());
	}

	public void test__using_CamelCase_naming_and_Test_prefix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/Something.cpp");
		VirtualFile testFile = mainModule.addFile("test/TestSomething.cpp");

		openFileInEditor(srcFile);

		// when
		performJumpAction();

		// then
		assertEquals(testFile, getEditedFile());
	}

	public void test__using_CamelCase_naming_and_Test_prefix__should_jump_from_test_code_to_production_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/Something.cpp");
		VirtualFile testFile = mainModule.addFile("test/TestSomething.cpp");

		openFileInEditor(testFile);

		// when
		performJumpAction();

		// then
		assertEquals(srcFile, getEditedFile());
	}

	public void test_should_handle_lowerCamelCase() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/something.js");
		VirtualFile testFile = mainModule.addFile("test/testSomething.js");

		openFileInEditor(srcFile);

		// when
		performJumpAction();

		// then
		assertEquals(testFile, getEditedFile());

		// when
		performJumpAction();

		// then
		assertEquals(srcFile, getEditedFile());
	}

	private void performJumpAction() {
		performEditorAction("org.moreunit.actions.jump");
	}
}