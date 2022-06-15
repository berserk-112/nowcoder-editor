package com.berserk112.nowcodereditor.editor;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.intellij.openapi.fileTypes.LanguageFileType;
import icons.NowCoderEditorIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class LCVFileType extends LanguageFileType {
    public static final LCVFileType INSTANCE = new LCVFileType();

    private LCVFileType() {
        super(LCVLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return PluginConstant.NOWCODER_EDITOR_VIEW+"Doc";
    }

    @NotNull
    @Override
    public String getDescription() {
        return PluginConstant.NOWCODER_EDITOR_VIEW;
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return PluginConstant.NOWCODER_EDITOR_VIEW;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return NowCoderEditorIcons.LCV;
    }
}