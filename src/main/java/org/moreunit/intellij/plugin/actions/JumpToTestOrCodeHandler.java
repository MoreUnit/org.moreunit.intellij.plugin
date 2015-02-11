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
import org.moreunit.intellij.plugin.files.SubjectFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

		SubjectFile subject = new SubjectFile(srcVFile);

		// Warning: returned file names may not exist anymore in the project! Deleted or moved files
		// may stay in the index, therefore we must check for their existence.
		// FilenameIndex.getFilesByName() does the trick and only returns existing files.
		List<String> candidates = Arrays.stream(FilenameIndex.getAllFilenames(project))
				.filter(subject::isCorrespondingFilename)
				.collect(Collectors.toList());

		List<PsiFile> allDestFiles = new ArrayList<>();
		for (String candidate : candidates) {
			PsiFile[] destFiles = FilenameIndex.getFilesByName(project, candidate, GlobalSearchScope.allScope(project));
			Collections.addAll(allDestFiles, destFiles);
		}

		if (!allDestFiles.isEmpty()) {
			FileEditorManager.getInstance(project).openFile(allDestFiles.get(0).getVirtualFile(), true);
		}

		// for next steps, have a look at:
		// - GotoTestOrCodeHandler
		// - ProjectRootManager.getInstance(clazz.getProject()).getFileIndex().isInTestSourceContent(vFile);

		return new GotoData(srcFile, allDestFiles.toArray(new PsiElement[allDestFiles.size()]), emptyList());
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