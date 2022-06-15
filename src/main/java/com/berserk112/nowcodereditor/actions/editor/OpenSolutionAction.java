package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Constant;
import com.berserk112.nowcodereditor.model.NowcoderEditor;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.setting.ProjectConfig;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class OpenSolutionAction extends AbstractEditAction {

    @Override
    public void update(@NotNull AnActionEvent anActionEvent) {
/*
        VirtualFile vf = anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (vf == null) {
            return;
        }
        NowcoderEditor nowcoderEditor = ProjectConfig.getInstance(anActionEvent.getProject()).getEditor(vf.getPath());
        if (nowcoderEditor == null || StringUtils.isBlank(nowcoderEditor.getTitleSlug()) || !URLUtils.equalsHost(nowcoderEditor.getHost())) {
            anActionEvent.getPresentation().setEnabled(false);
            return;
        }
        Question question = ViewManager.getCaCheQuestionByTitleSlug(nowcoderEditor.getTitleSlug(), null, anActionEvent.getProject());
        if (question != null) {
//            anActionEvent.getPresentation().setEnabled(!Constant.ARTICLE_LIVE_NONE.equals(question.getArticleLive()));
            anActionEvent.getPresentation().setEnabled(!Constant.ARTICLE_LIVE_NONE.equals(question.getTitle()));
        } else {
            ProgressManager.getInstance().run(new Task.Backgroundable(anActionEvent.getProject(), "Get Question", false) {
                @Override
                public void run(@NotNull ProgressIndicator progressIndicator) {
                    Question question = ViewManager.getQuestionByTitleSlug(nowcoderEditor.getTitleSlug(), null, anActionEvent.getProject());
                    if (question == null) {
                        anActionEvent.getPresentation().setEnabled(false);
                        return;
                    }
//                    anActionEvent.getPresentation().setEnabled(!Constant.ARTICLE_LIVE_NONE.equals(question.getArticleLive()));
                    anActionEvent.getPresentation().setEnabled(!Constant.ARTICLE_LIVE_NONE.equals(question.getTitle()));
                }
            });
        }
*/
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
/*
        Project project = anActionEvent.getProject();
//        if (Constant.ARTICLE_LIVE_ONE.equals(question.getArticleLive())) {
        if (Constant.ARTICLE_LIVE_ONE.equals(question.getTitle())) {
            ArticleManager.openArticle(question, project);
        } else if (Constant.ARTICLE_LIVE_LIST.equals(question.getArticleLive())) {
            List<Solution> solutionList = ArticleManager.getSolutionList(question, anActionEvent.getProject());
            if (solutionList.isEmpty()) {
                return;
            }
            AtomicReference<Solution> solution = new AtomicReference<>();
            ApplicationManager.getApplication().invokeAndWait(() -> {
                SolutionPanel.TableModel tableModel = new SolutionPanel.TableModel(solutionList);
                SolutionPanel dialog = new SolutionPanel(anActionEvent.getProject(), tableModel);
                dialog.setTitle(question.getFormTitle() + " Solutions");

                if (dialog.showAndGet()) {
                    solution.set(solutionList.get(dialog.getSelectedRow()));
                }
            });
            if (solution.get() != null) {
                question.setArticleSlug(solution.get().getSlug());
                ArticleManager.openArticle(question, project);
            }

        }
*/

    }
}
