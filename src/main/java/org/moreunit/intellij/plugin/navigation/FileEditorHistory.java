package org.moreunit.intellij.plugin.navigation;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.moreunit.intellij.plugin.files.SubjectFile;

public class FileEditorHistory {

	private VirtualFile lastFocusedTestFile;
	private VirtualFile lastFocusedProdFile;

	public void fileFocused(@NotNull VirtualFile focusedFile) {
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
