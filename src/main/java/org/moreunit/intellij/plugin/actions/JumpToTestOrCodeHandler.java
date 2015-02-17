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
import com.intellij.psi.search.ProjectScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreunit.intellij.plugin.files.SubjectFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

		SubjectFile subject = new SubjectFile(srcVFile);

		List<String> candidates = new ArrayList<String>();

		// Warning: returned file names may not exist anymore in the project! Deleted or moved files
		// may stay in the index, therefore we must check for their existence.
		// FilenameIndex.getFilesByName() does the trick and only returns existing files.
		for (String name : FilenameIndex.getAllFilenames(project)) {
			if (subject.isCorrespondingFilename(name)) {
				candidates.add(name);
			}
		}

		// only consider files under content roots of the project, ignoring libraries
		GlobalSearchScope searchScope = ProjectScope.getContentScope(project);

		List<PsiFile> allDestFiles = new ArrayList<PsiFile>();
		for (String candidate : candidates) {
			PsiFile[] destFiles = FilenameIndex.getFilesByName(project, candidate, searchScope);
			Collections.addAll(allDestFiles, destFiles);
		}

		if (!allDestFiles.isEmpty()) {
			FileEditorManager.getInstance(project).openFile(allDestFiles.get(0).getVirtualFile(), true);
		}

		// for next steps, have a look at:
		// - GotoTestOrCodeHandler
		// - ProjectRootManager.getInstance(clazz.getProject()).getFileIndex().isInTestSourceContent(vFile);

		return new GotoData(srcFile, allDestFiles.toArray(new PsiElement[allDestFiles.size()]), Collections.<AdditionalAction>emptyList());
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