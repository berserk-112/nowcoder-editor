package com.berserk112.nowcodereditor.setting;

import com.berserk112.nowcodereditor.model.NowcoderEditor;
import com.berserk112.nowcodereditor.model.PluginConstant;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.annotations.MapAnnotation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author shuzijun
 */
@State(name = "NowcoderEditor" + PluginConstant.ACTION_SUFFIX, storages = {@Storage(value = PluginConstant.ACTION_PREFIX + "/editor.xml")})
public class ProjectConfig implements PersistentStateComponent<ProjectConfig.InnerState> {

    public Map<String, NowcoderEditor> idProjectConfig = new HashMap<>();

    @Nullable
    public static ProjectConfig getInstance(Project project) {
        return project.getService(ProjectConfig.class);
    }

    private InnerState innerState = new InnerState();

    @Nullable
    @Override
    public ProjectConfig.InnerState getState() {
        return innerState;
    }

    @Override
    public void loadState(@NotNull ProjectConfig.InnerState innerState) {
        this.innerState = innerState;
        idProjectConfig.clear();
        Iterator<String> iter = this.innerState.projectConfig.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            NowcoderEditor nowcoderEditor = this.innerState.projectConfig.get(key);
            if (StringUtils.isBlank(nowcoderEditor.getQuestionNo())) {
                iter.remove();
                continue;
            }
            idProjectConfig.put(nowcoderEditor.getQuestionNo(), nowcoderEditor);
        }
    }


    public NowcoderEditor getDefEditor(String questionNo) {
        NowcoderEditor nowcoderEditor = idProjectConfig.get(questionNo);
        if (nowcoderEditor == null) {
            nowcoderEditor = new NowcoderEditor();
            idProjectConfig.put(questionNo, nowcoderEditor);
        }
        return nowcoderEditor;
    }

    public void addNowcoderEditor(NowcoderEditor nowcoderEditor) {
        idProjectConfig.put(nowcoderEditor.getQuestionNo(), nowcoderEditor);
        if (StringUtils.isNotBlank(nowcoderEditor.getPath())) {
            innerState.projectConfig.put(nowcoderEditor.getPath(), nowcoderEditor);
        }
    }

    public NowcoderEditor getEditor(String path) {
        return innerState.projectConfig.get(path);
    }

    public static class InnerState {
        @NotNull
        @MapAnnotation
        public Map<String, NowcoderEditor> projectConfig;

        InnerState() {
            projectConfig = new HashMap<>();
        }
    }

    public String getComponentName() {
        return this.getClass().getName();
    }

}
