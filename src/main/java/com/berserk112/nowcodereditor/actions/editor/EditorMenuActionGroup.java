package com.berserk112.nowcodereditor.actions.editor;

import com.berserk112.nowcodereditor.model.NowcoderEditor;
import com.berserk112.nowcodereditor.setting.ProjectConfig;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author shuzijun
 */
public class EditorMenuActionGroup extends DefaultActionGroup {

    @Override
    public void update(AnActionEvent e) {
        VirtualFile vf = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        boolean menuAllowed = false;
        if (vf != null) {
            NowcoderEditor nowcoderEditor = ProjectConfig.getInstance(e.getProject()).getEditor(vf.getPath());
            if (nowcoderEditor != null) {
                menuAllowed = true;
            }
        }
        e.getPresentation().setEnabledAndVisible(menuAllowed);
    }
}
