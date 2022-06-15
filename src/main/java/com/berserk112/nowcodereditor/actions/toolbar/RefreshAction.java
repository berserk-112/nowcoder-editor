package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.utils.DataKeys;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author shuzijun
 */
public class RefreshAction extends AbstractAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {

        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        navigatorTable.getPageInfo().clear();
        ViewManager.loadServiceData(navigatorTable, anActionEvent.getProject());

    }


}
