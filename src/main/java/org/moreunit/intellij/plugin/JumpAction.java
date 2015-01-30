package org.moreunit.intellij.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class JumpAction extends AnAction {

  public void actionPerformed(AnActionEvent event) {
    System.out.println("Jump!");
  }
}
