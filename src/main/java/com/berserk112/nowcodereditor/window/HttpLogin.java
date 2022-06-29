package com.berserk112.nowcodereditor.window;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.berserk112.nowcodereditor.utils.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpCookie;
import java.util.List;

/**
 * @author shuzijun
 */
public class HttpLogin {
    public static void loginSuccess(NavigatorTable navigatorTable, Project project, List<HttpCookie> cookieList) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, PluginConstant.ACTION_PREFIX+".loginSuccess", false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                Config config = PersistentConfig.getInstance().getInitConfig();
                config.addCookie(config.getUrl() + config.getLoginName(), CookieUtils.httpCookieToJSONString(cookieList));
                PersistentConfig.getInstance().setInitConfig(config);
                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("login.success"));
                ViewManager.loadServiceData(navigatorTable, project);
            }
        });
    }

    public static boolean isEnabledJcef() {
        Config config = PersistentConfig.getInstance().getInitConfig();
        return config != null && config.getJcef() && isSupportedJcef();
    }

    public static boolean isSupportedJcef() {
        try {
            Class<?> JBCefAppClass = Class.forName("com.intellij.ui.jcef.JBCefApp");
            Method method = JBCefAppClass.getMethod("isSupported");
            return (boolean) method.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Boolean.FALSE;
        }
    }

}
