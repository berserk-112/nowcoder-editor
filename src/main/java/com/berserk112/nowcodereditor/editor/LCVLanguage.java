package com.berserk112.nowcodereditor.editor;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.intellij.lang.Language;

public class LCVLanguage extends Language {

    public static final String LANGUAGE_NAME = PluginConstant.NOWCODER_EDITOR_VIEW+"Doc";

    public static final LCVLanguage INSTANCE = new LCVLanguage();

    protected LCVLanguage() {
        super(LANGUAGE_NAME);
    }
}
