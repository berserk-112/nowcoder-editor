package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.manager.CodeManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author shuzijun
 */
public class RunCodeAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        CodeManager.RunCode(question, anActionEvent.getProject());
    }
}
