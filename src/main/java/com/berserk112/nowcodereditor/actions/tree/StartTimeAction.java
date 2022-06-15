package com.berserk112.nowcodereditor.actions.tree;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.timer.TimerBarWidget;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.WindowManager;

/**
 * @author shuzijun
 */
public class StartTimeAction extends  AbstractTreeAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, NavigatorTable navigatorTable, Question question) {
        TimerBarWidget timerBarWidget = (TimerBarWidget) WindowManager.getInstance().getStatusBar(anActionEvent.getProject()).getWidget(TimerBarWidget.ID);
        if (timerBarWidget != null) {
            timerBarWidget.startTimer(question.getTitle());
        } else {
            //For possible reasons, the IDE version is not supported
        }
    }
}
