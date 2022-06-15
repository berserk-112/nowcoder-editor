package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.berserk112.nowcodereditor.utils.*;
import com.berserk112.nowcodereditor.window.*;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import org.apache.commons.lang.StringUtils;

import java.net.HttpCookie;
import java.util.List;

/**
 * @author shuzijun
 */
public class LoginAction extends AbstractAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {

        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);

        if (StringUtils.isBlank(HttpRequestUtils.getToken())) {
            HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderVerify());
            HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
            if (response == null) {
                MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("warning", PropertiesUtils.getInfo("request.failed"));
                return;
            }
            if (response.getStatusCode() != 200) {
                MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("warning", PropertiesUtils.getInfo("request.failed"));
                return;
            }
        } else {
            if (HttpRequestUtils.isLogin()) {
                MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("login.exist"));
                return;
            }
        }

        if (StringUtils.isBlank(config.getLoginName())) {
            MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("config.user"));
            return;
        }

        if (StringUtils.isNotBlank(config.getCookie(config.getUrl() + config.getLoginName()))) {
            List<HttpCookie> cookieList = CookieUtils.toHttpCookie(config.getCookie(config.getUrl() + config.getLoginName()));
            HttpRequestUtils.setCookie(cookieList);
            if (HttpRequestUtils.isLogin()) {
                MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("login", PropertiesUtils.getInfo("login.success"));
                ViewManager.loadServiceData(navigatorTable, anActionEvent.getProject());
                return;
            } else {
                config.addCookie(config.getUrl() + config.getLoginName(), null);
                PersistentConfig.getInstance().setInitConfig(config);
            }
        }

        ApplicationManager.getApplication().invokeAndWait(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame;
                loginFrame = new CookieLogin(anActionEvent.getProject(), navigatorTable);
                loginFrame.loadComponent();
            }
        });
    }


}
