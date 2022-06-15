package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;

/**
 * @author shuzijun
 */
public class ConfigAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(anActionEvent.getProject(), PluginConstant.APPLICATION_CONFIGURABLE_DISPLAY_NAME);
    }
}
