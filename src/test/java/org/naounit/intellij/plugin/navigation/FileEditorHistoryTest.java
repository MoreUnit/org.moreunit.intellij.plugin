package org.naounit.intellij.plugin.navigation;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileEditorHistoryTest {

	FileEditorHistory history = new FileEditorHistory();

	@Test
	public void should_remember_last_focused_test_file() {
		// given
		VirtualFile prodFile1 = file("src/some/path/some_concept.sh");
		VirtualFile prodFile2 = file("src/some/other/path/something.sh");
		VirtualFile prodFile3 = file("src/somewhere/something_else.sh");

		VirtualFile testFile1 = file("test/some/path/some_concept_test.sh");
		VirtualFile testFile2 = file("test/some/other/path/something_test.sh");

		// when
		history.fileFocused(prodFile1);
		history.fileFocused(testFile1);
		history.fileFocused(prodFile2);
		history.fileFocused(prodFile3);

		// then
		assertEquals(testFile1, history.getLastFocusedTestFile());

		// when
		history.fileFocused(testFile2);

		// then
		assertEquals(testFile2, history.getLastFocusedTestFile());
	}

	@Test
	public void should_remember_last_focused_prod_file() {
		// given
		VirtualFile prodFile1 = file("src/some/path/some_concept.sh");
		VirtualFile prodFile2 = file("src/some/other/path/something.sh");

		VirtualFile testFile1 = file("test/some/path/some_concept_test.sh");
		VirtualFile testFile2 = file("test/some/other/path/something_test.sh");
		VirtualFile testFile3 = file("src/somewhere/something_else_test.sh");

		// when
		history.fileFocused(testFile1);
		history.fileFocused(prodFile1);
		history.fileFocused(testFile2);
		history.fileFocused(testFile3);

		// then
		assertEquals(prodFile1, history.getLastFocusedProdFile());

		// when
		history.fileFocused(prodFile2);

		// then
		assertEquals(prodFile2, history.getLastFocusedProdFile());
	}

	@Test
	public void should_ignore_null_file() {
		// given
		VirtualFile prodFile = file("src/some/path/some_concept.sh");

		VirtualFile testFile = file("test/some/path/some_concept_test.sh");

		// when
		history.fileFocused(prodFile);
		history.fileFocused(testFile);

		// then
		assertEquals(prodFile, history.getLastFocusedProdFile());
		assertEquals(testFile, history.getLastFocusedTestFile());

		// when
		history.fileFocused(null);

		// then no exception has been thrown, and state is the same:
		assertEquals(prodFile, history.getLastFocusedProdFile());
		assertEquals(testFile, history.getLastFocusedTestFile());
	}

	private VirtualFile file(String name) {
		return new LightVirtualFile(name);
	}
}
