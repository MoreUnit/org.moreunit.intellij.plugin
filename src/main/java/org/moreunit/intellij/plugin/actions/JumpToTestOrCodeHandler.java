package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Collections.emptyList;

public class JumpToTestOrCodeHandler extends GotoTargetHandler {
	@Override
	protected String getFeatureUsedKey() {
		return "org.moreunit.actions.jump";
	}

	@Nullable
	@Override
	protected GotoData getSourceAndTargetElements(Editor editor, PsiFile srcFile) {
		Project project = editor.getProject();

		VirtualFile srcVFile = srcFile.getVirtualFile();
		String name = srcVFile.getNameWithoutExtension();


		String expectedDestName = name + "Test." + srcVFile.getExtension();
		PsiFile[] destFiles = FilenameIndex.getFilesByName(project, expectedDestName, GlobalSearchScope.allScope(project));

		if (destFiles.length != 0) {
			FileEditorManager.getInstance(project).openFile(destFiles[0].getVirtualFile(), true);
		}

		// for next steps, have a look at:
		// - GotoTestOrCodeHandler
		// - ProjectRootManager.getInstance(clazz.getProject()).getFileIndex().isInTestSourceContent(vFile);

		return new GotoData(srcFile, destFiles, emptyList());
	}

	@NotNull
	@Override
	protected String getChooserTitle(PsiElement sourceElement, String name, int length) {
		// TODO change message when searching for production code
		return CodeInsightBundle.message("goto.test.chooserTitle.test", name, length);
	}

	@NotNull
	@Override
	protected String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
		return CodeInsightBundle.message("goto.test.notFound");
	}
}