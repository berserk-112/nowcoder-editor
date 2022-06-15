package com.berserk112.nowcodereditor.listener;

import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.berserk112.nowcodereditor.utils.SentryUtils;
import com.intellij.diagnostic.AbstractMessage;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author shuzijun
 */
public class ErrorReportHandler extends ErrorReportSubmitter {
    @NotNull
    @Override
    public String getReportActionText() {
        return PropertiesUtils.getInfo("report");
    }

    @Override
    public boolean submit(@NotNull IdeaLoggingEvent[] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer consumer) {
        for (IdeaLoggingEvent event : events) {
            Throwable throwable = event.getThrowable();
            if (event.getData() instanceof AbstractMessage) {
                throwable = ((AbstractMessage) event.getData()).getThrowable();
            }

            SentryUtils.submitErrorReport(throwable, additionalInfo);
        }

        return true;
    }
}
