package com.berserk112.nowcodereditor.actions.toolbar;

import com.berserk112.nowcodereditor.manager.ViewManager;
import com.berserk112.nowcodereditor.model.Constant;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.model.Tag;
import com.berserk112.nowcodereditor.utils.DataKeys;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.NowCoderEditorIcons;

import java.util.List;

/**
 * @author shuzijun
 */
public class FindActionGroup extends ActionGroup {

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

    @Override
    public void update(AnActionEvent e) {
        ActionToolbar findToolbar = e.getDataContext().getData(DataKeys.NOWCODER_TOOLBAR_FIND);
        String id = e.getActionManager().getId(this);
        List<Tag> tags = getTags(id);

        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
                if (tag.isSelect()) {
                    e.getPresentation().setIcon(NowCoderEditorIcons.FILTER);
                    findToolbar.getComponent().updateUI();
                    return;
                }
            }
        }
        e.getPresentation().setIcon(null);
        if (findToolbar != null && findToolbar.getComponent() != null) {
            findToolbar.getComponent().updateUI();
        }
    }


    @Override
    public AnAction[] getChildren(AnActionEvent anActionEvent) {

        List<AnAction> anActionList = Lists.newArrayList();

        String id = anActionEvent.getActionManager().getId(this);

        List<Tag> tags = getTags(id);

        if (tags != null && !tags.isEmpty()) {
            for (Tag tag : tags) {
                anActionList.add(new FindTagAction(tag.getName(), tag, tags, onlyOne(id), getFilterKey(id)));
            }
        }
        AnAction[] anActions = new AnAction[anActionList.size()];
        anActionList.toArray(anActions);
        return anActions;
    }

    private List<Tag> getTags(String id) {
        List<Tag> tags = null;
        if (PluginConstant.NOWCODER_FIND_DIFFICULTY.equals(id)) {
            tags = ViewManager.getFilter(Constant.FIND_TYPE_DIFFICULTY);
        } else if (PluginConstant.NOWCODER_FIND_STATUS.equals(id)) {
            tags = ViewManager.getFilter(Constant.FIND_TYPE_STATUS);
        } else if (PluginConstant.NOWCODER_FIND_LISTS.equals(id)) {
            tags = ViewManager.getFilter(Constant.FIND_TYPE_LISTS);
        } else if (PluginConstant.NOWCODER_FIND_TAGS.equals(id)) {
            tags = ViewManager.getFilter(Constant.FIND_TYPE_TAGS);
        } else if (PluginConstant.NOWCODER_FIND_CATEGORY.equals(id)) {
            tags = ViewManager.getFilter(Constant.FIND_TYPE_CATEGORY);
        }

        return tags;
    }

    private boolean onlyOne(String id) {
        if (PluginConstant.NOWCODER_FIND_TAGS.equals(id)) {
            return false;
        }

        return true;
    }

    private String getFilterKey(String id) {
        if (PluginConstant.NOWCODER_FIND_DIFFICULTY.equals(id)) {
            return "difficulty";
        } else if (PluginConstant.NOWCODER_FIND_STATUS.equals(id)) {
            return "status";
        } else if (PluginConstant.NOWCODER_FIND_LISTS.equals(id)) {
            return "topicId";
        } else if (PluginConstant.NOWCODER_FIND_TAGS.equals(id)) {
            return "tags";
        } else if (PluginConstant.NOWCODER_FIND_CATEGORY.equals(id)) {
            return "categorySlug";
        }
        return "";
    }
}
