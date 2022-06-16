package com.berserk112.nowcodereditor.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.berserk112.nowcodereditor.model.*;
import com.berserk112.nowcodereditor.setting.PersistentConfig;
import com.berserk112.nowcodereditor.utils.*;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuzijun
 */
public class SubmissionManager {

    public static List<Submission> getSubmissionService(Question question, Project project) {

        if (!HttpRequestUtils.isLogin()) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("login.not"));
            return null;
        }

        List<Submission> submissionList = new ArrayList<Submission>();

        try {
            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.addParameter("problemId", question.getProblemId());
            HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderSubmissionHistory() + uriBuilder);
            HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
            if (response != null && response.getStatusCode() == 200) {
                String body = response.getBody();
                if (StringUtils.isNotBlank(body)) {

                    JSONArray jsonArray = JSONObject.parseObject(body).getJSONObject("data").getJSONArray("submissions");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Submission submission = new Submission();
                        submission.setId(object.getString("id"));
                        submission.setStatus(object.getString("judgeReply"));
                        submission.setLang(object.getString("language"));
                        submission.setRuntime(object.getString("timeConsumption"));
                        submission.setTime(object.getString("createDate"));
                        submission.setMemory(object.getString("memoryConsumption"));
                        submissionList.add(submission);
                    }
                    if (submissionList.size() == 0) {
                        MessageUtils.getInstance(project).showInfoMsg("info", PropertiesUtils.getInfo("submission.empty"));
                    }
                }
            } else {
                MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("request.failed"));
            }

        } catch (Exception io) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("request.failed"));
        }
        return submissionList;
    }

    public static void openSubmission(Submission submission, Question question, Project project) {

        if (!HttpRequestUtils.isLogin()) {
            MessageUtils.getInstance(project).showWarnMsg("info", PropertiesUtils.getInfo("login.not"));
            return;
        }
        Config config = PersistentConfig.getInstance().getInitConfig();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnumByLangSlug(submission.getLang());
        String filePath = PersistentConfig.getInstance().getTempFilePath() + Constant.DOC_SUBMISSION + VelocityUtils.convert(config.getCustomFileName(), question) + submission.getId() + ".txt";

        File file = new File(filePath);
        if (file.exists()) {
            FileUtils.openFileEditor(file, project);
        } else {
            try {

                JSONObject jsonObject;
                jsonObject = loadSubmission(submission,project);
                if (jsonObject == null) {
                    return;
                }

                StringBuffer sb = new StringBuffer();

                sb.append(jsonObject.getString("submissionCode").replaceAll("\\u000A", "\n")).append("\n");

                JSONObject submissionData = jsonObject.getJSONObject("submissionData");
                if ("答案正确".equals(submission.getStatus())) {
                    sb.append(codeTypeEnum.getComment()).append("runtime:").append(submissionData.getString("runtime")).append("\n");
                    sb.append(codeTypeEnum.getComment()).append("memory:").append(submissionData.getString("memory")).append("\n");
                } else if ("答案错误".equals(submission.getStatus())) {
                    sb.append(codeTypeEnum.getComment()).append("rightHundredRate:").append(submissionData.getString("rightHundredRate")).append("\n");
                    sb.append(codeTypeEnum.getComment()).append("error:").append(submission.getStatus()).append("\n");
                } else if ("编译错误".equals(submission.getStatus())) {
                    sb.append(codeTypeEnum.getComment()).append("runtime_error:").append(submission.getStatus()).append("\n");
                }
                FileUtils.saveFile(file, sb.toString());
                FileUtils.openFileEditor(file, project);


            } catch (Exception e) {
                LogUtils.LOG.error("获取提交详情失败", e);
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
                return;
            }

        }

    }

/*
    private static JSONObject loadSubmission(Submission submission, Project project) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("id", submission.getId());
        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderSubmissionDetail()  + uriBuilder);
        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            String body = response.getBody();
//            String body = CommentUtils.createSubmissions(html);
            if (StringUtils.isBlank(body)) {
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("submission.parse"));
            } else {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    return jsonObject;
                } catch (Exception e) {
                    LogUtils.LOG.error(body, e);
                    MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("submission.parse"));
                }
            }
        } else {
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
        }
        return null;
    }
*/

    private static JSONObject loadSubmission(Submission submission,Project project) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("id", submission.getId());
        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderSubmissionDetail()  + uriBuilder);
        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            String body = response.getBody();
            if (StringUtils.isNotBlank(body)) {
                JSONObject jsonObject = new JSONObject();
                JSONObject cnObject = JSONObject.parseObject(body).getJSONObject("data").getJSONObject("submission");

                jsonObject.put("submissionCode", cnObject.getString("content"));

                JSONObject submissionData = new JSONObject();
                submissionData.put("runtime", cnObject.getString("timeConsumption") + "ms");
                submissionData.put("memory", cnObject.getString("memoryConsumption") + "KB");
                submissionData.put("rightHundredRate", cnObject.getString("rightHundredRate") + "%");
                submissionData.put("runtime_error", cnObject.getString("judgeReply"));
                jsonObject.put("submissionData", submissionData);

                return jsonObject;

            }
        } else {
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("request.failed"));
        }
        return null;
    }

}
