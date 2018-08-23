package org.naounit.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import org.jetbrains.annotations.NotNull;

public class JumpToTestOrCodeAction extends BaseJumpToTestOrCodeAction {

	@NotNull
	@Override
	protected CodeInsightActionHandler getHandler() {
		return new JumpToTestOrCodeHandler();
	}

	@Override
	protected String getTextWhenProductionFileSelected() {
		return "Jump to Test case";
	}

	@Override
	protected String getDescriptionWhenProductionFileSelected() {
		return "Jump to a test of the selected file";
	}

	@Override
	protected String getTextWhenTestFileSelected() {
		return "Jump to tested class";
	}

	@Override
	protected String getDescriptionWhenTestFileSelected() {
		return "Jump to the subject of the selected test file";
	}
}
