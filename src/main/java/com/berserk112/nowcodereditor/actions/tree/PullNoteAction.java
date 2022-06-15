package com.berserk112.nowcodereditor.actions.tree;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author shuzijun
 */
public class PullNoteAction extends  AbstractTreeAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, NavigatorTable navigatorTable, Question question) {
/*
        NoteManager.pull(question,anActionEvent.getProject());
        NoteManager.show(question,anActionEvent.getProject());
*/
    }
}
