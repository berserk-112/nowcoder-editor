package com.berserk112.nowcodereditor.listener;

import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author shuzijun
 */
public class QueryKeyListener implements KeyListener {

    private final static Logger logger = LoggerFactory.getLogger(QueryKeyListener.class);

    private JTextField jTextField;
    private NavigatorTable navigatorTable;
    private Project project;

    public QueryKeyListener(JTextField jTextField, NavigatorTable navigatorTable, Project project) {
        this.jTextField = jTextField;
        this.navigatorTable = navigatorTable;
        this.project = project;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Search", false) {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    String selectText = jTextField.getText();
                    navigatorTable.getPageInfo().disposeFilters("title", selectText, StringUtils.isNotBlank(selectText));
                    navigatorTable.getPageInfo().setPageIndex(1);
                    ViewManager.loadServiceData(navigatorTable, project);
                }
            });
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
