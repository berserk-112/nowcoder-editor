package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.manager.CodeManager;
import com.berserk112.nowcodereditor.model.CodeTypeEnum;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.setting.NowCoderPersistentConfig;
import com.berserk112.nowcodereditor.utils.MessageUtils;
import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.berserk112.nowcodereditor.window.TestcasePanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author shuzijun
 */
public class TestcaseAction extends AbstractEditAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config, Question question) {
        if (StringUtils.isBlank(question.getInputSample())) {
            String codeType = NowCoderPersistentConfig.getInstance().getInitConfig().getCodeType();
            CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);

            CodeManager.setTestCaeAndLang(question, codeTypeEnum, anActionEvent.getProject());
        }
        AtomicReference<String> text = new AtomicReference<>(TestcaseAction.class.getName());
        ApplicationManager.getApplication().invokeAndWait(() -> {
            TestcasePanel dialog = new TestcasePanel(anActionEvent.getProject());
            dialog.setTitle(question.getFormTitle() + " Testcase");
            dialog.setText(question.getInputSample());
            if (dialog.showAndGet()) {
                text.set(dialog.testcaseText());
            }
        });
        if (!TestcaseAction.class.getName().equals(text.get())) {
            if (StringUtils.isBlank(text.get())) {
                MessageUtils.getInstance(anActionEvent.getProject()).showWarnMsg("info", PropertiesUtils.getInfo("test.case"));
                return;
            } else {
                question.setInputSample(text.get());
                CodeManager.RunCode(question, anActionEvent.getProject());
            }
        }
    }
}
