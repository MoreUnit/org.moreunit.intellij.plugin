package org.moreunit.intellij.plugin.actions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;
import org.moreunit.intellij.plugin.fixtures.MoreUnitTestCase;

public class JumpToTestOrCodeActionTest extends MoreUnitTestCase {

	public void test__using_CamelCase_naming_and_Test_suffix__should_jump_from_production_code_to_test_code() throws Exception {
		// given
		VirtualFile srcFile = mainModule.addFile("src/pack/Foo.java", "package pack; public class Foo() {}");
		VirtualFile testFile = mainModule.addFile("test/pack/FooTest.java", "package pack; public class FooTest() {}");

		openFileInEditor(srcFile);

		// when
		performEditorAction("org.moreunit.actions.jump");

		// then
		assertEquals(testFile, getEditedFile());
	}

	public void test_should_fail_silently_when_no_corresponding_file_is_found() {
		// given
		VirtualFile srcFileWithoutTestCounterpart = mainModule.addFile("src/FileWithoutTestCounterpart.java",
				"public class FileWithoutTestCounterpart {}");

		Editor editor = openFileInEditor(srcFileWithoutTestCounterpart);

		System.out.println(ApplicationManager.getApplication().isUnitTestMode());

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
		performEditorAction("org.moreunit.actions.jump");

		// then
		assertEquals(srcFile, getEditedFile());
	}
}