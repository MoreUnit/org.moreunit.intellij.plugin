package org.naounit.intellij.plugin.navigation;

import com.intellij.openapi.vfs.VirtualFile;
import org.naounit.intellij.plugin.files.SubjectFile;

public class FileEditorHistory {

	private VirtualFile lastFocusedTestFile;
	private VirtualFile lastFocusedProdFile;

	public void fileFocused(VirtualFile focusedFile) {
		if (focusedFile == null) {
			return;
		}

		if (new SubjectFile(focusedFile).isTestFile()) {
			lastFocusedTestFile = focusedFile;
		} else {
			lastFocusedProdFile = focusedFile;
		}
	}

	public VirtualFile getLastFocusedTestFile() {
		return lastFocusedTestFile;
	}

	public VirtualFile getLastFocusedProdFile() {
		return lastFocusedProdFile;
	}
}
