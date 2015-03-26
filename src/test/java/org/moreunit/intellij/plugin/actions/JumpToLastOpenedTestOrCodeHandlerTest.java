package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.navigation.GotoTargetHandler.GotoData;
import com.intellij.openapi.vfs.VirtualFile;
import org.moreunit.intellij.plugin.fixtures.JumpActionHandlerTestCase;

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
public class JumpToLastOpenedTestOrCodeHandlerTest extends JumpActionHandlerTestCase {

	public void test_should_jump_to_last_opened_test_file() throws Exception {
		// given some files
		VirtualFile srcFile1 = mainModule.addFile("src1/some/path/some_concept.js");
		VirtualFile srcFile2 = mainModule.addFile("src/some/other/path/something.js");
		VirtualFile srcFile3 = mainModule.addFile("src/somewhere/something_else.js");

		VirtualFile testFile = mainModule.addFile("test/some/path/some_concept_test.js");

		// when various production/test files have been opened
		openFileInEditor(srcFile1);
		openFileInEditor(testFile);
		openFileInEditor(srcFile2);
		openFileInEditor(srcFile3);

		// and "Jump to Last Opened Test" is eventually triggered
		GotoData gotoData = invokeJumpActionHandler();

		// then the last opened test file is shown again
		assertTargetFilesInOrder(gotoData, testFile);
	}

	public void test_should_jump_to_last_opened_production_file() throws Exception {
		// given some files
		VirtualFile srcFile = mainModule.addFile("src1/some/path/some_concept.js");

		VirtualFile testFile1 = mainModule.addFile("test1/some/path/some_concept_test.js");
		VirtualFile testFile2 = mainModule.addFile("test1/some/other/path/something_test.js");
		VirtualFile testFile3 = mainModule.addFile("src1/somewhere/something_else_test.js");

		// when various production/test files have been opened
		openFileInEditor(testFile1);
		openFileInEditor(srcFile);
		openFileInEditor(testFile2);
		openFileInEditor(testFile3);

		// and "Jump to Last Opened Test Subject" is eventually triggered
		GotoData gotoData = invokeJumpActionHandler();

		// then the last opened production file is shown again
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	public void test_should_jump_to_corresponding_test_file_when_none_has_been_opened_yet() throws Exception {
		// given some files
		VirtualFile srcFile1 = mainModule.addFile("src/some/other/path/something.js");
		VirtualFile srcFile2 = mainModule.addFile("src/somewhere/something_else.js");
		VirtualFile srcFile3 = mainModule.addFile("src1/some/path/some_concept.js");

		VirtualFile testFile = mainModule.addFile("test/some/path/some_concept_test.js");

		// when only production files have been opened yet
		openFileInEditor(srcFile1);
		openFileInEditor(srcFile2);
		openFileInEditor(srcFile3);

		// and "Jump to Last Opened Test" is eventually triggered
		GotoData gotoData = invokeJumpActionHandler();

		// then a test file corresponding to the last opened production file is opened
		assertTargetFilesInOrder(gotoData, testFile);
	}

	public void test_should_jump_to_corresponding_production_file_when_none_has_been_opened_yet() throws Exception {
		// given some files
		VirtualFile srcFile = mainModule.addFile("src1/some/path/some_concept.js");

		VirtualFile testFile1 = mainModule.addFile("test1/some/other/path/something_test.js");
		VirtualFile testFile2 = mainModule.addFile("src1/somewhere/something_else_test.js");
		VirtualFile testFile3 = mainModule.addFile("test1/some/path/some_concept_test.js");

		// when only test files have been opened yet
		openFileInEditor(testFile1);
		openFileInEditor(testFile2);
		openFileInEditor(testFile3);

		// and "Jump to Last Opened Test Subject" is eventually triggered
		GotoData gotoData = invokeJumpActionHandler();

		// then a production file corresponding to the last opened test file is opened
		assertTargetFilesInOrder(gotoData, srcFile);
	}

	private GotoData invokeJumpActionHandler() {
		return new JumpToLastOpenedTestOrCodeHandler().getSourceAndTargetElements(lastOpenedEditor, psiFileFor(lastOpenedFile));
	}
}