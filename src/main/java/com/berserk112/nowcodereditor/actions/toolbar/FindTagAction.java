package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.model.Tag;
import com.berserk112.nowcodereditor.utils.DataKeys;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author shuzijun
 */
public class FindTagAction extends ToggleAction {

    private Tag tag;

    private String filterKey;

    private boolean onlyOne;

    private List<Tag> typeTags;

    public FindTagAction(@Nullable String text, Tag tag, List<Tag> typeTags, boolean onlyOne, String filterKey) {
        super(text);
        this.tag = tag;
        this.typeTags = typeTags;
        this.onlyOne = onlyOne;
        this.filterKey = filterKey;
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return tag.isSelect();
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        if (onlyOne && b) {
            typeTags.forEach(tag -> tag.setSelect(false));
        }
        tag.setSelect(b);
        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        if (navigatorTable == null) {
            return;
        }
        ProgressManager.getInstance().run(new Task.Backgroundable(anActionEvent.getProject(), PluginConstant.PLUGIN_NAME + "." + tag.getName(), false) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                if ("categorySlug".equals(filterKey)) {
                    if (b) {
                        //TODO tag.getSlug() 0r Name()
                        navigatorTable.getPageInfo().setCategorySlug(String.valueOf(tag.getTagId()));
                    } else {
                        navigatorTable.getPageInfo().setCategorySlug("");
                    }
                } else {
                    navigatorTable.getPageInfo().disposeFilters(filterKey, String.valueOf(tag.getTagId()), b);
                }
                navigatorTable.getPageInfo().setPageIndex(1);
                ViewManager.loadServiceData(navigatorTable, anActionEvent.getProject());
            }
        });
    }


}
