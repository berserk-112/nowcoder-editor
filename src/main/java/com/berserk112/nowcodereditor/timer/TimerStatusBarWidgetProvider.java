package com.berserk112.nowcodereditor.timer;

import com.berserk112.nowcodereditor.model.PluginConstant;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidgetFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


/**
 * @author shuzijun
 */
public class TimerStatusBarWidgetProvider implements StatusBarWidgetFactory {


    @Override
    public @NonNls @NotNull String getId() {
        return PluginConstant.NOWCODER_EDITOR_TIMER_STATUS_BAR_ID;
    }

    @Override
    public @Nls @NotNull String getDisplayName() {
        return PluginConstant.NOWCODER_EDITOR_TIMER_STATUS_BAR_ID;
    }

    @Override
    public boolean isAvailable(@NotNull Project project) {
        return true;
    }

    @Override
    public @NotNull StatusBarWidget createWidget(@NotNull Project project) {
        return new TimerBarWidget(project);
    }

    @Override
    public void disposeWidget(@NotNull StatusBarWidget widget) {
    }

    @Override
    public boolean canBeEnabledOn(@NotNull StatusBar statusBar) {
        return true;
    }
}
