package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.utils.*;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author shuzijun
 */
public class LogoutAction extends AbstractAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {

        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderLogout());
        HttpResponse httpResponse = HttpRequestUtils.executePost(httpRequest);
        HttpRequestUtils.resetHttpclient();
        MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("info", PropertiesUtils.getInfo("login.out"));
        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        if(navigatorTable == null){
            return;
        }
        ViewManager.loadServiceData(navigatorTable, anActionEvent.getProject());
    }
}
