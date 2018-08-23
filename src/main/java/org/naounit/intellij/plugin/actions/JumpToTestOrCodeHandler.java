package org.naounit.intellij.plugin.actions;

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
import org.naounit.intellij.plugin.Patterns.Factory;
import org.naounit.intellij.plugin.Patterns.PathPattern;
import org.naounit.intellij.plugin.files.SubjectFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JumpToTestOrCodeHandler extends GotoTargetHandler {

	private Factory patternFactory;

	@Override
	protected String getFeatureUsedKey() {
		return "org.naounit.actions.jump";
	}

	@Override
	protected boolean shouldSortTargets() {
		return false;
	}

	public JumpToTestOrCodeHandler()
	{
		super();
		this.patternFactory = new Factory();
	}

	@Nullable
	@Override
	protected GotoData getSourceAndTargetElements(Editor editor, PsiFile fromFile) {
		Project project = editor.getProject();
		VirtualFile vFromFile = fromFile.getVirtualFile();
		PathPattern fromPattern = this.determinePattern(vFromFile);

		if(!(fromPattern instanceof PathPattern))
		{
			return new GotoData(fromFile, new ArrayList<PsiFile>().toArray(new PsiElement[0]), Collections.<AdditionalAction>emptyList());
		}

		List<String> candidates = new ArrayList<String>();

		// Warning: returned file names may not exist anymore in the project! Deleted or moved files
		// may stay in the index, therefore we must check for their existence.
		// FilenameIndex.getFilesByName() does the trick and only returns existing files.
		SubjectFile subject = new SubjectFile(vFromFile);
		for (String name : FilenameIndex.getAllFilenames(project)) {
			if (subject.isCorrespondingFilename(name)) {
				candidates.add(name);
			}
		}

		// only consider files under content roots of the project, ignoring libraries
		GlobalSearchScope searchScope = ProjectScope.getContentScope(project);

		List<PsiFile> targetFiles = new ArrayList<PsiFile>();
		for (String candidate : candidates) {
			PsiFile[] candidateFiles = FilenameIndex.getFilesByName(project, candidate, searchScope);
			for(PsiFile candidateFile: candidateFiles)
			{
				VirtualFile vTargetFile = candidateFile.getVirtualFile();
				PathPattern toPattern = fromPattern.createTargetPatternFromMatcher(this.match(fromPattern, vFromFile), vTargetFile.getName());
				Matcher m = this.match(toPattern, vTargetFile);
				if(m.find())
				{
					targetFiles.add(candidateFile);
				}
			}
		}

		// for next steps, have a look at:
		// - GotoTestOrCodeHandler
		// - ProjectRootManager.getInstance(clazz.getProject()).getFileIndex().isInTestSourceContent(vFile);

		return new GotoData(fromFile, targetFiles.toArray(new PsiElement[targetFiles.size()]), Collections.<AdditionalAction>emptyList());
	}

	@Nullable
	private PathPattern determinePattern(VirtualFile vFile)
	{
		SubjectFile subject = new SubjectFile(vFile);
		PathPattern[] patterns = this.patternFactory.create(subject);

		for(PathPattern pattern: patterns)
		{
			Matcher m = this.match(pattern, vFile);
			if(m.find())
			{
				return pattern;
			}
		}

		return null;
	}

	private Matcher match(PathPattern pattern, VirtualFile vFile)
	{
		Pattern p = Pattern.compile(pattern.toString());

		return p.matcher(vFile.getPath());
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