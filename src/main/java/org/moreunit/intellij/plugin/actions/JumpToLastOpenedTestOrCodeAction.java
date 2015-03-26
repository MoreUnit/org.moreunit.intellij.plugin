package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import org.jetbrains.annotations.NotNull;

public class JumpToLastOpenedTestOrCodeAction extends BaseJumpToTestOrCodeAction {

	@NotNull
	@Override
	protected CodeInsightActionHandler getHandler() {
		return new JumpToLastOpenedTestOrCodeHandler();
	}

	@Override
	protected String getTextWhenProductionFileSelected() {
		return "Jump to Last Opened Test";
	}

	@Override
	protected String getDescriptionWhenProductionFileSelected() {
		return "Jump to the last opened test or, is none has been opened yet, jump to a test of the selected file";
	}

	@Override
	protected String getTextWhenTestFileSelected() {
		return "Jump to Last Opened Test Subject";
	}

	@Override
	protected String getDescriptionWhenTestFileSelected() {
		return "Jump to the last opened non-test file or, is none has been opened yet, jump to the main subject of the selected test file";
	}
}
