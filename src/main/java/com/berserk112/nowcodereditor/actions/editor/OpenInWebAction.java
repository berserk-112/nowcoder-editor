package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author zzdcon
 */
public class OpenInWebAction extends AbstractEditAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
//        BrowserUtil.browse(URLUtils.getLeetcodeProblems() + question.getTitleSlug());
        BrowserUtil.browse("");
    }
}
