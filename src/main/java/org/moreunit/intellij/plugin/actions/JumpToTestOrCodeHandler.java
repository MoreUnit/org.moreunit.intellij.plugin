package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.codeInsight.navigation.GotoTargetHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.ProjectScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moreunit.intellij.plugin.Patterns.PathPattern;
import org.moreunit.intellij.plugin.Patterns.SimpleSrcPattern;
import org.moreunit.intellij.plugin.Patterns.SimpleTestsPattern;
import org.moreunit.intellij.plugin.files.SubjectFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JumpToTestOrCodeHandler extends GotoTargetHandler {

	@Override
	protected String getFeatureUsedKey() {
		return "org.moreunit.actions.jump";
	}

	@Override
	protected boolean shouldSortTargets() {
		return false;
	}

	@Nullable
	@Override
	protected GotoData getSourceAndTargetElements(Editor editor, PsiFile srcFile) {
		Project project = editor.getProject();

		VirtualFile srcVFile = srcFile.getVirtualFile();
		SubjectFile subject = new SubjectFile(srcVFile);

		PathPattern fromPattern = SimpleSrcPattern.create();
		if(subject.isTestFile())
		{
			fromPattern = SimpleTestsPattern.create();
		}
		String srcPath = srcVFile.getPath();
		Pattern p = Pattern.compile(fromPattern.toString());
		Matcher m = p.matcher(srcPath);

		if(! m.find())
		{
			return new GotoData(srcFile, new ArrayList<PsiFile>().toArray(new PsiElement[0]), Collections.<AdditionalAction>emptyList());
		}

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

		List<PsiFile> destFiles = new ArrayList<PsiFile>();
		for (String candidate : candidates) {
			PsiFile[] potentialDestFiles = FilenameIndex.getFilesByName(project, candidate, searchScope);
			for(PsiFile potentialDestFile: potentialDestFiles)
			{
				VirtualFile file = potentialDestFile.getVirtualFile();
				String destPath = file.getPath();

				PathPattern toPattern = fromPattern.createTargetPatternFromMatcher(m, file.getName());
				Pattern p2 = Pattern.compile(toPattern.toString());
				Matcher m2 = p2.matcher(destPath);

				if(m2.find())
				{
					destFiles.add(potentialDestFile);
				}
			}
		}

		placeFilesHavingThatExtensionFirst(destFiles, extensionOf(srcVFile));

		// for next steps, have a look at:
		// - GotoTestOrCodeHandler
		// - ProjectRootManager.getInstance(clazz.getProject()).getFileIndex().isInTestSourceContent(vFile);

		return new GotoData(srcFile, destFiles.toArray(new PsiElement[destFiles.size()]), Collections.<AdditionalAction>emptyList());
	}

	private void placeFilesHavingThatExtensionFirst(List<PsiFile> files, final String ext) {
		Collections.sort(files, new Comparator<PsiFile>() {
			@Override
			public int compare(PsiFile f1, PsiFile f2) {
				String ext1 = extensionOf(f1);
				String ext2 = extensionOf(f2);
				if (ext1.equals(ext) && !ext2.equals(ext)) {
					return -1;
				}
				if (!ext1.equals(ext) && ext2.equals(ext)) {
					return 1;
				}
				// otherwise keep current order
				return 0;
			}
		});
	}

	private String extensionOf(PsiFile file) {
		return extensionOf(file.getVirtualFile());
	}

	private String extensionOf(VirtualFile file) {
		return file.getExtension().toLowerCase();
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