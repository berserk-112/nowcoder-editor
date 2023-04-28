package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.CodeTypeEnum;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.NowcoderEditor;
import com.berserk112.nowcodereditor.model.Question;
import com.berserk112.nowcodereditor.setting.ProjectConfig;
import com.berserk112.nowcodereditor.utils.MessageUtils;
import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @author shuzijun
 */
abstract class AbstractEditAction extends AbstractAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        VirtualFile vf = ArrayUtil.getFirstElement(FileEditorManager.getInstance(anActionEvent.getProject()).getSelectedFiles());
        if (vf == null) {
            return;
        }
        NowcoderEditor nowcoderEditor = ProjectConfig.getInstance(anActionEvent.getProject()).getEditor(vf.getPath());
        if (nowcoderEditor == null) {
            return;
        }
        if (StringUtils.isBlank(nowcoderEditor.getTitleSlug())) {
            MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("info", PropertiesUtils.getInfo("tree.null"));
            return;
        }
        if(!URLUtils.equalsHost(nowcoderEditor.getHost())){
            MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("info", PropertiesUtils.getInfo("tree.host"));
            return;
        }
        Question question = ViewManager.getQuestionByUUid(nowcoderEditor.getQuestionUUid(), nowcoderEditor.getQuestionNo(),
                CodeTypeEnum.getCodeTypeEnum(config.getCodeType()), anActionEvent.getProject());
        if (question == null) {
            MessageUtils.getInstance(anActionEvent.getProject()).showInfoMsg("info", PropertiesUtils.getInfo("tree.null"));
            return;
        }

        actionPerformed(anActionEvent, config, question);


    }

    public abstract void actionPerformed(AnActionEvent anActionEvent, Config config, Question question);
}
