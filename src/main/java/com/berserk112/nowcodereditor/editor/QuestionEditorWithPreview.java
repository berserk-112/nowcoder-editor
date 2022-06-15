package com.berserk112.nowcodereditor.editor;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.TextEditorWithPreview;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author shuzijun
 */
public class QuestionEditorWithPreview extends TextEditorWithPreview {
    public static final Key<QuestionEditorWithPreview> PARENT_SPLIT_EDITOR_KEY = Key.create(PluginConstant.PLUGIN_ID + "Question Split");

    public QuestionEditorWithPreview(@NotNull TextEditor editor, @NotNull FileEditor preview) {
        super(editor, preview, "Question " + Layout.SHOW_EDITOR_AND_PREVIEW.getName(), Layout.SHOW_EDITOR_AND_PREVIEW);
        editor.putUserData(PARENT_SPLIT_EDITOR_KEY, this);
        preview.putUserData(PARENT_SPLIT_EDITOR_KEY, this);
    }

    @Nullable
    @Override
    protected ActionGroup createLeftToolbarActionGroup() {
        return (ActionGroup) ActionManager.getInstance().getAction(PluginConstant.NOWCODER_EDITOR_GROUP);
    }
}
