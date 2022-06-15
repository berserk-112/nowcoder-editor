package com.berserk112.nowcodereditor.window;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import icons.NowCoderEditorIcons;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class WindowFactory implements ToolWindowFactory {

    public static String ID = PluginConstant.TOOL_WINDOW_ID;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        JComponent navigatorPanel=  new NavigatorPanel(toolWindow,project);
        Content content = contentFactory.createContent(navigatorPanel, "", false);
        toolWindow.getContentManager().addContent(content);
        if(PersistentConfig.getInstance().getInitConfig()!=null && !PersistentConfig.getInstance().getInitConfig().getShowToolIcon()){
            toolWindow.setIcon(NowCoderEditorIcons.EMPEROR_NEW_CLOTHES);
        }
    }

    public static DataContext getDataContext(@NotNull Project project) {
        AtomicReference<DataContext> dataContext = new AtomicReference<>();
        ApplicationManager.getApplication().invokeAndWait(() -> {
            ToolWindow nowcoderToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
            dataContext.set(DataManager.getInstance().getDataContext(nowcoderToolWindows.getContentManager().getContent(0).getComponent()));
        });
        return dataContext.get();
    }

    public static void updateTitle(@NotNull Project project, String userName) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        if (StringUtils.isNotBlank(userName)) {
            leetcodeToolWindows.setTitle("[" + userName + "]");
        } else {
            leetcodeToolWindows.setTitle("");
        }
    }

    public static void activateToolWindow(@NotNull Project project) {
        ToolWindow leetcodeToolWindows = ToolWindowManager.getInstance(project).getToolWindow(ID);
        leetcodeToolWindows.activate(null);
    }

}
