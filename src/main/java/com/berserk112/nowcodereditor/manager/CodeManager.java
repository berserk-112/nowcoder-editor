package com.berserk112.nowcodereditor.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.berserk112.nowcodereditor.listener.QuestionStatusListener;
import com.berserk112.nowcodereditor.model.*;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.berserk112.nowcodereditor.utils.*;
import com.google.common.base.Joiner;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

/**
 * @author shuzijun, berserk112
 */
public class CodeManager {

    public static void openCode(Question question, Project project) {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        if (codeTypeEnum == null) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("config.code"));
            return;
        }

        if (!QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
            return;
        }

        if (config.getQuestionEditor()) {
            openContent(question, project, false);
        }

        String filePath = PersistentConfig.getInstance().getTempFilePath() + VelocityUtils.convert(config.getCustomFileName(), question) + codeTypeEnum.getSuffix();

        File file = new File(filePath);
        BiConsumer<NowcoderEditor, String> fillPath = (e, s) -> e.setPath(s);
        if (file.exists()) {
            FileUtils.openFileEditorAndSaveState(file, project, question, fillPath, true);
        } else {
            question.setContent(CommentUtils.createComment(question.getContent(), codeTypeEnum, config));
            FileUtils.saveFile(file, VelocityUtils.convert(config.getCustomTemplate(), question));
            FileUtils.openFileEditorAndSaveState(file, project, question, fillPath, true);
        }
    }


    public static void openContent(Question question, Project project, boolean isOpen) {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        if (codeTypeEnum == null) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("config.code"));
            return;
        }

        if (!QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
            return;
        }

        String filePath = PersistentConfig.getInstance().getTempFilePath() + Constant.DOC_CONTENT + VelocityUtils.convert(config.getCustomFileName(), question) + ".md";

        File file = new File(filePath);
        BiConsumer<NowcoderEditor, String> fillPath = (e, s) -> e.setContentPath(s);
        if (file.exists()) {
            FileUtils.openFileEditorAndSaveState(file, project, question, fillPath, isOpen);
        } else {
            FileUtils.saveFile(file, question.getContent());
            FileUtils.openFileEditorAndSaveState(file, project, question, fillPath, isOpen);
        }
    }


    public static void SubmitCode(Question question, Project project) {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        String code = getCodeText(question, config, codeTypeEnum, project);
        if (StringUtils.isBlank(code)) {
            return;
        }

        if (!QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
            return;
        }

        try {
            HttpRequest httpRequest = HttpRequest.post(URLUtils.getNowcoderSubmissions(), "application/x-www-form-urlencoded; charset=UTF-8");
            httpRequest.addParam("questionId", question.getQuestionId());
            httpRequest.addParam("content", URLEncoder.encode(code, StandardCharsets.UTF_8.toString()));
            httpRequest.addParam("language", String.valueOf(codeTypeEnum.getLangId()));
            httpRequest.addHeader("Accept", "application/x-www-form-urlencoded; charset=UTF-8");
            HttpResponse response = HttpRequestUtils.executePost(httpRequest);
            if (response != null && response.getStatusCode() == 200) {
                String body = response.getBody();
                JSONObject returnObj = JSONObject.parseObject(body);
                ProgressManager.getInstance().run(new SubmitCheckTask(returnObj, codeTypeEnum, question, project));
                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("request.pending"));
            } else if (response != null && response.getStatusCode() == 429) {
                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("request.pending"));
            } else {
                LogUtils.LOG.error("提交失败：url：" + httpRequest.getUrl() + ";param:" + ";body:" + response.getBody());
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
            }
        } catch (Exception i) {
            LogUtils.LOG.error("SubmitCode error", i);
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code"));

        }

    }

    public static void RunCodeCode(Question question, Project project) {
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);

        String code = getCodeText(question, config, codeTypeEnum, project);
        if (StringUtils.isBlank(code)) {
            return;
        }

        if (!QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
            return;
        }

        try {
            HttpRequest httpRequest = HttpRequest.post(URLUtils.getNowcoderRuncode(), "application/x-www-form-urlencoded; charset=UTF-8");
/*
            StringBuilder sb = new StringBuilder();
            sb.append("selfType=14").append("&questionId=" + question.getQuestionId())
                    .append("&selfInput=" + question.getInputSample())
                    .append("&selfOutput=" + question.getOutputSample())
                    .append("&content=" + question.getCode())
                    .append("&language=" + question.getLangSlug());
*/
            httpRequest.addParam("selfType", "14");
            httpRequest.addParam("questionId", question.getQuestionId());
            httpRequest.addParam("selfInput", URLEncoder.encode(question.getInputSample(), StandardCharsets.UTF_8.toString()));
            httpRequest.addParam("selfOutput", URLEncoder.encode(question.getOutputSample(), StandardCharsets.UTF_8.toString()));
            httpRequest.addParam("content", URLEncoder.encode(code, StandardCharsets.UTF_8.toString()));
            httpRequest.addParam("language", String.valueOf(codeTypeEnum.getLangId()));
//            httpRequest.setBody(sb.toString());
            httpRequest.addHeader("Accept", "application/x-www-form-urlencoded; charset=UTF-8");
            HttpResponse response = HttpRequestUtils.executePost(httpRequest);
            if (response != null && response.getStatusCode() == 200) {

                String body = response.getBody();
                JSONObject returnObj = JSONObject.parseObject(body);
                ProgressManager.getInstance().run(new RunCodeCheckTask(returnObj, project, question.getInputSample(), question.getOutputSample()));
                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("request.pending"));
            } else if (response != null && response.getStatusCode() == 429) {
                MessageUtils.getInstance(project).showWarnMsg("error", "Please wait for the result.");
            } else {
                LogUtils.LOG.error("RuncodeCode failure " + response == null ? "" : response.getBody());
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
            }
        } catch (Exception i) {
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code"));
        }
    }

    private static String getCodeText(Question question, Config config, CodeTypeEnum codeTypeEnum, Project project) {
        if (codeTypeEnum == null) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("config.code"));
            return null;
        }
        if (!HttpRequestUtils.isLogin()) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("login.not"));
            return null;
        }
        String filePath = PersistentConfig.getInstance().getTempFilePath() + VelocityUtils.convert(config.getCustomFileName(), question) + codeTypeEnum.getSuffix();
        File file = new File(filePath);
        if (!file.exists()) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("request.code"));
            return null;
        } else {
//            setTestCaeAndLang(question, codeTypeEnum, project);
            if (StringUtils.isBlank(question.getInputSample()) || StringUtils.isBlank(question.getOutputSample())) {
                return null;
            }

            String code = FileUtils.getClearCommentFileBody(file, codeTypeEnum);
            if (StringUtils.isBlank(code)) {
                MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("request.empty"));
                return null;
            }


            return code;
        }
    }

    public static void setTestCaeAndLang(Question question, CodeTypeEnum codeTypeEnum, Project project) {
        if (codeTypeEnum == null) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("config.code"));
            return;
        }
        if (!QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
            return;
        }

        try {
            HttpRequest httpRequest = HttpRequest.post(URLUtils.getNowcoderQuestionInfo() + question.getQuestionUUid(), "application/json");
            HttpResponse response = HttpRequestUtils.executePost(httpRequest);
            if (response != null && response.getStatusCode() == 200) {

                String body = response.getBody();

                JSONObject jObject = JSONObject.parseObject(body);
                JSONObject jsonObject = jObject.getJSONObject("codingProblem");

//                question.setCode(jsonObject.getString(codeTypeEnum.getLangSlug() + "Template"));
                question.setInputSample(jsonObject.getString("inputSample"));
                question.setOutputSample(jsonObject.getString("outputSample"));
            } else {
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code"));
            }

        } catch (Exception e) {
            LogUtils.LOG.error("获取代码失败", e);
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code"));
        }

    }


    private static class SubmitCheckTask extends Task.Backgroundable {

        private Question question;
        private JSONObject returnObj;
        private CodeTypeEnum codeTypeEnum;
        private Project project;

        public SubmitCheckTask(JSONObject returnObj, CodeTypeEnum codeTypeEnum, Question question, Project project) {
            super(project, PluginConstant.PLUGIN_NAME + ".submitCheckTask", true);
            this.returnObj = returnObj;
            this.codeTypeEnum = codeTypeEnum;
            this.question = question;
            this.project = project;
        }

        @Override
        public void run(@NotNull ProgressIndicator progressIndicator) {
            String key = returnObj.getString("data");
            for (int i = 0; i < 100; i++) {
                if (progressIndicator.isCanceled()) {
                    MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.cancel"));
                    return;
                }
                try {
                    URIBuilder uriBuilder = new URIBuilder();
                    uriBuilder.addParameter("submissionId", key);
                    HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderStatus() + uriBuilder);
                    HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
                    if (response != null && response.getStatusCode() == 200) {
                        String body = response.getBody();
                        JSONObject jsonObject = JSONObject.parseObject(body);
                        if (!"等待判题".equals(jsonObject.getString("judgeReplyDesc"))) {
                            if (Integer.valueOf(5).equals(jsonObject.getInteger("status"))) {
                                String runtime = jsonObject.getString("timeConsumption") + "ms";
                                String runtimePercentile = jsonObject.getJSONObject("timeMemoryComparison").getBigDecimal("timePercent")
                                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%";
                                String memory = jsonObject.getString("memoryConsumption") + "KB";
                                String memoryPercentile = jsonObject.getJSONObject("timeMemoryComparison").getBigDecimal("memoryPercent")
                                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP).toString() + "%";

                                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo(
                                        "submit.success", runtime, runtimePercentile, codeTypeEnum.getType(),
                                        memory, memoryPercentile, codeTypeEnum.getType()));
                                question.setStatus("ac");
                                project.getMessageBus().syncPublisher(QuestionStatusListener.QUESTION_STATUS_TOPIC).updateTable(question);
                            } else {

                                String input = jsonObject.getString("userInput");
                                String output = jsonObject.getString("userOutput");
                                String outputs = jsonObject.getString("expectedOutput");
                                MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("submit.failed", input, output, outputs));

                                if (!"ac".equals(question.getStatus())) {
                                    question.setStatus("notac");
                                    project.getMessageBus().syncPublisher(QuestionStatusListener.QUESTION_STATUS_TOPIC).updateTable(question);
                                }
                            }
                            return;
                        }
                    }
                    Thread.sleep(300L);
                } catch (Exception e) {
                    LogUtils.LOG.error("提交出错", e);
                    MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
                    return;
                }

            }

            MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("response.timeout"));
        }
    }

    private static String buildErrorMsg(JSONObject errorBody) {
        String statusMsg = errorBody.getString("status_msg");
        if (StringUtils.isNotBlank(statusMsg)) {
            if (statusMsg.equals("Compile Error")) {
                return errorBody.getString("full_compile_error");
            } else if (statusMsg.equals("Runtime Error")) {
                return errorBody.getString("full_runtime_error");
            } else {
                return statusMsg;
            }
        }
        return "Unknown error";
    }


    private static class RunCodeCheckTask extends Task.Backgroundable {
        private JSONObject returnObj;
        private Project project;
        private String input;
        private String output;

        public RunCodeCheckTask(JSONObject returnObj, Project project, String input, String output) {
            super(project, PluginConstant.PLUGIN_NAME + ".runCodeCheckTask", true);
            this.returnObj = returnObj;
            this.project = project;
            this.input = input;
            this.output = output;
        }

        @Override
        public void run(@NotNull ProgressIndicator progressIndicator) {
            String key = returnObj.getString("data");
            if (StringUtils.isBlank(key)) {
                key = returnObj.getString("data");
            }
            for (int i = 0; i < 100; i++) {
                if (progressIndicator.isCanceled()) {
                    MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.cancel"));
                    return;
                }
                String body = null;
                try {
                    URIBuilder uriBuilder = new URIBuilder();
                    uriBuilder.addParameter("submissionId", key)
                            .addParameter("selfType", "14");
                    HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderStatusTest() + uriBuilder);
                    HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
                    if (response != null && response.getStatusCode() == 200) {
                        body = response.getBody();
                        JSONObject jsonObject = JSONObject.parseObject(body);
                        if ("编译错误".equals(jsonObject.getString("judgeReplyDesc"))) {
                            MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("test.success", "编译错误"));
                            return;
                        }
                        if (!"等待判题".equals(jsonObject.getString("judgeReplyDesc"))) {
                            String input = jsonObject.getString("userInput");
//                            String output = jsonObject.getString("userOutput");
                            String outputs = jsonObject.getString("expectedOutput");
//                            String outputs = jsonObject.getString("judgeReplyDesc");
                            MessageUtils.getInstance(project).showInfoMsg("info",
                                    PropertiesUtils.getInfo("test.success", input, outputs));
                            return;
                        }
                    }
                    Thread.sleep(300L);
                } catch (Exception e) {
                    LogUtils.LOG.error("提交出错，body:" + body + ",returnObj:" + returnObj.toJSONString(), e);
                    MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
                    return;
                }

            }
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("response.timeout"));
        }
    }
}
