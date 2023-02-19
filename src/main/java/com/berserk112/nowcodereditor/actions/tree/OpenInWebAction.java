package com.berserk112.nowcodereditor.actions.tree;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author zzdcon
 */
public class OpenInWebAction extends AbstractTreeAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, NavigatorTable navigatorTable, Question question) {

        BrowserUtil.browse(URLUtils.getNowcoderWebUrl() + question.getQuestionUUid());
    }
}
