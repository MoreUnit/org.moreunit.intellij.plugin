package org.moreunit.intellij.plugin.actions;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import org.jetbrains.annotations.NotNull;

public class JumpToTestOrCodeAction extends BaseCodeInsightAction {

	@NotNull
	@Override
	protected CodeInsightActionHandler getHandler() {
		return new JumpToTestOrCodeHandler();
	}
}
