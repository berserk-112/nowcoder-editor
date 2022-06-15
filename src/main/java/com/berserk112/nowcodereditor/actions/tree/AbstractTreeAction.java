package com.berserk112.nowcodereditor.actions.tree;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.utils.DataKeys;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author shuzijun
 */
public abstract class AbstractTreeAction extends AbstractAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
//        WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        if (navigatorTable == null) {
            return;
        }
        Question question = navigatorTable.getSelectedRowData();
        if (question == null) {
            return;
        }
        actionPerformed(anActionEvent, config, navigatorTable, question);
    }

    public abstract void actionPerformed(AnActionEvent anActionEvent, Config config, NavigatorTable navigatorTable, Question question);
}
