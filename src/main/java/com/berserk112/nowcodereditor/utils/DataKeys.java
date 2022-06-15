package com.berserk112.nowcodereditor.utils;

import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;

/**
 * @author shuzijun
 */
public class DataKeys {

    public static final DataKey<NavigatorTable> NOWCODER_PROJECTS_TREE = DataKey.create("NOWCODER_PROJECTS_TREE");
    public static final DataKey<JBScrollPane> NOWCODER_PROJECTS_SCROLL = DataKey.create("NOWCODER_PROJECTS_SCROLL");
    public static final DataKey<JPanel> NOWCODER_PROJECTS_TERRFIND = DataKey.create("NOWCODER_PROJECTS_TERRFIND");
    public static final DataKey<ActionToolbar> NOWCODER_TOOLBAR_FIND = DataKey.create("NOWCODER_TOOLBAR_FIND");
    public static final DataKey<ActionToolbar> NOWCODER_TOOLBAR_SORT = DataKey.create("NOWCODER_TOOLBAR_SORT");
}
