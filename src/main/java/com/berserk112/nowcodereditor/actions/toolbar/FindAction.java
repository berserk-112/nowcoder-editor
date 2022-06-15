package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.utils.DataKeys;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;

import javax.swing.*;

/**
 * @author shuzijun
 */
public class FindAction extends ToggleAction {


    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        if (anActionEvent.getProject() == null) {
            //Why is it null?
            return false;
        }
        JPanel panel = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TERRFIND);
        if (panel == null) {
            return false;
        }
        return panel.isVisible();
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        JPanel panel = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TERRFIND);
        if (panel == null) {
            return;
        }
        panel.setVisible(b);
    }

}
