package com.berserk112.nowcodereditor.listener;

import com.berserk112.nowcodereditor.model.Question;
import com.intellij.util.messages.Topic;

/**
 * @author shuzijun
 */
public interface QuestionStatusListener {

    @Topic.ProjectLevel
    Topic<QuestionStatusListener> QUESTION_STATUS_TOPIC = Topic.create("Question Status", QuestionStatusListener.class);

    public void updateTable(Question question);


}
