package com.berserk112.nowcodereditor.editor;

import com.berserk112.nowcodereditor.model.Config;
import com.berserk112.nowcodereditor.model.NowcoderEditor;
import com.berserk112.nowcodereditor.setting.NowCoderPersistentConfig;
import com.berserk112.nowcodereditor.setting.ProjectConfig;
import com.berserk112.nowcodereditor.utils.LogUtils;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author shuzijun
 */
public class QuestionEditorProvider extends SplitTextEditorProvider {

    public QuestionEditorProvider() {
        super(new PsiAwareTextEditorProvider(), new ContentProvider());
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        try {
            Config config = NowCoderPersistentConfig.getInstance().getInitConfig();
            if (config == null || !config.getQuestionEditor()) {
                return false;
            }
            NowcoderEditor nowcoderEditor = ProjectConfig.getInstance(project).getEditor(file.getPath());
            if (nowcoderEditor == null || StringUtils.isBlank(nowcoderEditor.getContentPath())) {
                return false;
            }
            File contentFile = new File(nowcoderEditor.getContentPath());
            if (!contentFile.exists()) {
                return false;
            }
        } catch (Throwable e) {
            LogUtils.LOG.error("QuestionEditorProvider -> accept", e);
            return false;
        }
        return this.myFirstProvider.accept(project, file);
    }

    @Override
    public Builder createEditorAsync(@NotNull Project project, @NotNull VirtualFile file) {
        NowcoderEditor nowcoderEditor = ProjectConfig.getInstance(project).getEditor(file.getPath());

        final Builder firstBuilder = getBuilderFromEditorProvider(this.myFirstProvider, project, file);

        VirtualFile contentVf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(nowcoderEditor.getContentPath()));
        final Builder secondBuilder = getBuilderFromEditorProvider(this.mySecondProvider, project, contentVf);
        return new Builder() {
            public FileEditor build() {
                return createSplitEditor(firstBuilder.build(), secondBuilder.build());
            }
        };
    }

    @Override
    protected FileEditor createSplitEditor(@NotNull FileEditor firstEditor, @NotNull FileEditor secondEditor) {

        //if (firstEditor instanceof TextEditor && secondEditor instanceof MarkdownSplitEditor) {
        return new QuestionEditorWithPreview((TextEditor) firstEditor, secondEditor);
        //} else {
        //    throw new IllegalArgumentException("Main editor should be TextEditor");
        //}
    }


}
