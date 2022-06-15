package com.berserk112.nowcodereditor.listener;

import com.berserk112.nowcodereditor.manager.CodeManager;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author shuzijun
 */
public class JTableKeyAdapter extends KeyAdapter {

    private NavigatorTable navigatorTable;
    private Project project;

    public JTableKeyAdapter(NavigatorTable navigatorTable, Project project) {
        this.navigatorTable = navigatorTable;
        this.project = project;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Question question = navigatorTable.getSelectedRowData();
        if (question != null) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                ProgressManager.getInstance().run(new Task.Backgroundable(project, PluginConstant.NOWCODER_EDITOR_OPEN_CODE, false) {
                    @Override
                    public void run(@NotNull ProgressIndicator progressIndicator) {
                        CodeManager.openCode(question, project);
                    }
                });
            }
        }
    }
}
