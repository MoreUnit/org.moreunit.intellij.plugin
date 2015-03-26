package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.moreunit.intellij.plugin.files.SubjectFile;

public abstract class BaseJumpToTestOrCodeAction extends BaseCodeInsightAction {

	@Override
	public void update(AnActionEvent event) {
		Presentation p = event.getPresentation();
		p.setEnabled(false);

		Editor editor = event.getData(CommonDataKeys.EDITOR);
		Project project = event.getData(CommonDataKeys.PROJECT);
		if (editor == null || project == null) {
			return;
		}

		PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
		if (psiFile == null) {
			return;
		}

		SubjectFile subject = new SubjectFile(psiFile.getVirtualFile());

		p.setEnabled(true);

		if (subject.isTestFile()) {
			p.setText(getTextWhenTestFileSelected());
			p.setDescription(getDescriptionWhenTestFileSelected());
		} else {
			p.setText(getTextWhenProductionFileSelected());
			p.setDescription(getDescriptionWhenProductionFileSelected());
		}
	}

	protected abstract String getDescriptionWhenProductionFileSelected();

	protected abstract String getDescriptionWhenTestFileSelected();

	protected abstract String getTextWhenProductionFileSelected();

	protected abstract String getTextWhenTestFileSelected();
}
