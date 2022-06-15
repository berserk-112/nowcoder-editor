package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.actions.AbstractAction;
import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.Constant;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.model.Sort;
import com.berserk112.nowcodereditor.utils.DataKeys;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.NowCoderEditorIcons;
import org.jetbrains.annotations.NotNull;

/**
 * @author shuzijun
 */
public class SortAction extends AbstractAction {

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Sort sort = getSort(e);
        if (sort == null) {
            return;
        }
        ActionToolbar sortToolbar = e.getDataContext().getData(DataKeys.NOWCODER_TOOLBAR_SORT);
        if (sort.getType() == Constant.SORT_ASC) {
            e.getPresentation().setIcon(NowCoderEditorIcons.SORT_ASC);
        } else if (sort.getType() == Constant.SORT_DESC) {
            e.getPresentation().setIcon(NowCoderEditorIcons.SORT_DESC);
        } else {
            e.getPresentation().setIcon(null);
        }
        if (sortToolbar != null && sortToolbar.getComponent() != null) {
            sortToolbar.getComponent().updateUI();
        }
        super.update(e);

    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, Config config) {
        NavigatorTable navigatorTable = WindowFactory.getDataContext(anActionEvent.getProject()).getData(DataKeys.NOWCODER_PROJECTS_TREE);
        if (navigatorTable == null || ViewManager.getFilter(Constant.FIND_TYPE_DIFFICULTY) == null) {
            return;
        }
        Sort sort = getSort(anActionEvent);
        if (sort == null) {
            return;
        }
        ViewManager.operationType(getKey(anActionEvent));
        if (sort.getType() == 0) {
            navigatorTable.getPageInfo().disposeFilters("order", "", false);
            navigatorTable.getPageInfo().disposeFilters("acs", "", false);
        } else if (sort.getType() == 1) {
            navigatorTable.getPageInfo().disposeFilters("order", sort.getSlug(), true);
            navigatorTable.getPageInfo().disposeFilters("acs", "false", true);
        } else if (sort.getType() == 2) {
            navigatorTable.getPageInfo().disposeFilters("order", sort.getSlug(), true);
            navigatorTable.getPageInfo().disposeFilters("acs", "true", true);
        }
        ViewManager.loadServiceData(navigatorTable, anActionEvent.getProject());
    }

    private Sort getSort(AnActionEvent anActionEvent) {
        return ViewManager.getSort(getKey(anActionEvent));
    }

    private String getKey(AnActionEvent anActionEvent) {
        return anActionEvent.getActionManager().getId(this).replace(PluginConstant.NOWCODER_SORT_PREFIX, "");
    }
}
