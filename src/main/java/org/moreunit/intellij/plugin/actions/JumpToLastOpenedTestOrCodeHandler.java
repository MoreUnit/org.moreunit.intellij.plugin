package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreunit.intellij.plugin.files.SubjectFile;
import org.moreunit.intellij.plugin.navigation.FileEditorHistory;
import org.moreunit.intellij.plugin.navigation.ProjectFileEditorHistory;

import java.util.Collections;

public class JumpToLastOpenedTestOrCodeHandler extends GotoTargetHandler {

	private final JumpToTestOrCodeHandler jumpToTestOrCodeHandler = new JumpToTestOrCodeHandler();

	@Override
	protected String getFeatureUsedKey() {
		return "org.moreunit.actions.jumpToLastOpenedTest";
	}

	@Override
	protected boolean shouldSortTargets() {
		return jumpToTestOrCodeHandler.shouldSortTargets();
	}

	@Nullable
	@Override
	protected GotoData getSourceAndTargetElements(Editor editor, PsiFile srcFile) {
		Project project = editor.getProject();
		FileEditorHistory fileEditorHistory = project.getComponent(ProjectFileEditorHistory.class).getHistory();

		SubjectFile subject = new SubjectFile(srcFile.getVirtualFile());

		final VirtualFile destFile;
		if (subject.isTestFile()) {
			destFile = fileEditorHistory.getLastFocusedProdFile();
		} else {
			destFile = fileEditorHistory.getLastFocusedTestFile();
		}

		if (destFile != null) {
			PsiFile psiFile = PsiManager.getInstance(project).findFile(destFile);
			return new GotoData(srcFile, new PsiElement[]{ psiFile }, Collections.<AdditionalAction>emptyList());
		}

		return jumpToTestOrCodeHandler.getSourceAndTargetElements(editor, srcFile);
	}

	@NotNull
	@Override
	protected String getChooserTitle(PsiElement sourceElement, String name, int length) {
		return jumpToTestOrCodeHandler.getChooserTitle(sourceElement, name, length);
	}

	@NotNull
	@Override
	protected String getNotFoundMessage(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
		return jumpToTestOrCodeHandler.getNotFoundMessage(project, editor, file);
	}
}
