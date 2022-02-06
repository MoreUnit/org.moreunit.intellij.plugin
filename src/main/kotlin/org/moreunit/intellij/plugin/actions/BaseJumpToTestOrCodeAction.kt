package org.moreunit.intellij.plugin.actions

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.BaseCodeInsightAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.util.PsiUtilBase

abstract class BaseJumpToTestOrCodeAction: BaseCodeInsightAction() {
    override fun getHandler(): CodeInsightActionHandler {
        TODO("Not yet implemented")
    }

    override fun update(event: AnActionEvent) {
        val presentation = event.presentation
        presentation.isEnabled = false

        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val project = event.getData(CommonDataKeys.PROJECT) ?: return

        val psiFile = PsiUtilBase.getPsiFileInEditor(editor, project) ?: return
        //val subjectFile = Subject FIXME

        presentation.isEnabled = true

        // FIXME
    }

    abstract fun descriptionWhenProductionFileSelected(): String
    abstract fun descriptionWhenTestFileSelected(): String
    abstract fun textWhenProductionFileSelected(): String
    abstract fun textWhenTestFileSelected(): String
}
