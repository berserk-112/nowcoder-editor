package com.berserk112.nowcodereditor.actions.tree;

import com.berserk112.nowcodereditor.manager.SubmissionManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.model.Submission;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class SubmissionsAction extends AbstractTreeAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, NavigatorTable navigatorTable, Question question) {

/*
        List<Submission> submissionList = SubmissionManager.getSubmissionService(question, anActionEvent.getProject());
        if (submissionList == null || submissionList.isEmpty()) {
            return;
        }
        AtomicReference<Submission> submission = new AtomicReference<>();
        ApplicationManager.getApplication().invokeAndWait(() -> {
            SubmissionsPanel.TableModel tableModel = new SubmissionsPanel.TableModel(submissionList);
            SubmissionsPanel dialog = new SubmissionsPanel(anActionEvent.getProject(), tableModel);
            dialog.setTitle(question.getFormTitle() + " Submissions");

            if (dialog.showAndGet()) {
                submission.set(submissionList.get(dialog.getSelectedRow()));
            }
        });
        if(submission.get() !=null){
            SubmissionManager.openSubmission(submission.get(), question, anActionEvent.getProject());
        }
*/

    }

}
