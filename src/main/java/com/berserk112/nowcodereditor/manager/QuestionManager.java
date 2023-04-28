package com.berserk112.nowcodereditor.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.berserk112.nowcodereditor.model.*;
import com.berserk112.nowcodereditor.setting.NowCoderPersistentConfig;
import com.berserk112.nowcodereditor.utils.*;
import com.berserk112.nowcodereditor.window.WindowFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shuzijun
 */
public class QuestionManager {

    private static Map<String, Question> dayMap = Maps.newLinkedHashMap();

    public static PageInfo<Question> getQuestionService(Project project, PageInfo pageInfo) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.addParameter("pageSize", String.valueOf(pageInfo.pageSize))
                .addParameter("page", String.valueOf(pageInfo.getPageIndex()))
                .addParameter("tags", pageInfo.getFilters().getTags())
                .addParameter("title", pageInfo.getFilters().getTitle())
                .addParameter("topicId", StringUtils.isBlank(pageInfo.getFilters().getTopicId()) ? "196"
                        : pageInfo.getFilters().getTopicId())
                .addParameter("difficulty", pageInfo.getFilters().getDifficulty())
                .addParameter("judgeStatus", pageInfo.getFilters().getStatus())
                .addParameter("order", pageInfo.getFilters().getOrder())
                .addParameter("asc", pageInfo.getFilters().getAcs());
//        StringBuilder requestSb = new StringBuilder();

        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderQuestion() + uriBuilder);

        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (HttpRequestUtils.isLogin()) {
            if (response != null && response.getStatusCode() == 200) {
                ApplicationManager.getApplication().invokeAndWait(() -> {
                    WindowFactory.updateTitle(project, NowCoderPersistentConfig.getInstance().getInitConfig().getLoginName());
                });
            } else {
                LogUtils.LOG.error("Request userStatus  failed, status:" + response == null ? "" : response.getStatusCode());
            }
        }

//        response = HttpRequestUtils.executePost(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            List questionList = parseQuestion(response.getBody());

            Integer total = JSONObject.parseObject(response.getBody()).getJSONObject("data").getInteger("totalCount");
            pageInfo.setRowTotal(total);
            pageInfo.setRows(questionList);
        } else {
            LogUtils.LOG.error("Request question list failed, status:" + response == null ? "" : response.getStatusCode());
            throw new RuntimeException("Request question list failed");
        }

        return pageInfo;

    }

    public static List<Tag> getDifficulty() {
        Map<String, Integer> keyMap = Maps.newHashMap();
        keyMap.put(Constant.DIFFICULTY_SO_EASY, 1);
        keyMap.put(Constant.DIFFICULTY_EASY, 2);
        keyMap.put(Constant.DIFFICULTY_MEDIUM, 3);
        keyMap.put(Constant.DIFFICULTY_LITTLE_HARD, 4);
        keyMap.put(Constant.DIFFICULTY_HARD, 5);
        List<Tag> difficultyList = Lists.newArrayList();
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            Tag tag = new Tag();
            tag.setName(entry.getKey());
            tag.setTagId(entry.getValue());
            tag.setType("difficulty");
            difficultyList.add(tag);
        }
        return difficultyList;
    }

    public static List<Tag> getStatus() {
        List<String> keyList = Lists.newArrayList(Constant.STATUS_TODO, Constant.STATUS_SOLVED, Constant.STATUS_ATTEMPTED);

        List<Tag> statusList = Lists.newArrayList();
        for (String key : keyList) {
            Tag tag = new Tag();
            tag.setName(key);
            tag.setType("status");
            if (Constant.STATUS_TODO.equals(key)) {
                tag.setTagId(3);
            } else if (Constant.STATUS_SOLVED.equals(key)) {
                tag.setTagId(1);
            } else if (Constant.STATUS_ATTEMPTED.equals(key)) {
                tag.setTagId(2);
            }
            statusList.add(tag);
        }
        return statusList;
    }

    public static List<Tag> getTags() {
        List<Tag> tags = new ArrayList<>();

        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderTags());
        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            try {
                String body = response.getBody();
                tags = parseTag(body);
            } catch (Exception e1) {
                LogUtils.LOG.error("Request tags exception", e1);
            }
        } else {
            LogUtils.LOG.error("Request tags failed, status:" + response.getStatusCode() + "body:" + response.getBody());
        }

        return tags;
    }

    public static List<Tag> getLists() {
        List<Tag> tags = new ArrayList<>();

        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderTopic());
        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            try {
                String body = response.getBody();
                tags = parseList(body);
            } catch (Exception e1) {
                LogUtils.LOG.error("Request Lists exception", e1);
            }
        } else {
            LogUtils.LOG.error("Request Lists failed, status:" + response.getStatusCode() + "body:" + response.getBody());
        }
        return tags;
    }

    public static List<Tag> getCategory() {
        List<Tag> tags = new ArrayList<>();

        HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderTopic());
        HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
        if (response != null && response.getStatusCode() == 200) {
            try {
                String body = response.getBody();
                tags = parseCategory(body);
            } catch (Exception e1) {
                LogUtils.LOG.error("Request CardInfo exception", e1);
            }
        } else {
            LogUtils.LOG.error("Request CardInfo failed, status:" + response.getStatusCode() + "body:" + response.getBody());
        }
        return tags;
    }


    private static List<Question> parseQuestion(String str) {

        List<Question> questionList = new ArrayList<Question>();

        if (StringUtils.isNotBlank(str)) {
            JSONArray jsonArray = JSONObject.parseObject(str).getJSONObject("data").getJSONArray("questions");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Question question = parseOneQuestion(object);
                questionList.add(question);
            }
        }
        return questionList;
    }

    private static Question parseOneQuestion(JSONObject object) {
        Question question = new Question(object.getString("title"));
        question.setQuestionNo(object.getString("questionNo"));
        question.setAcceptRate(object.getDouble("acceptRate"));
        if (object.containsKey("accept")) {
            if (object.getBoolean("accept")) {
                question.setStatus(Constant.STATUS_SOLVED);
            } else {
                question.setStatus(Constant.STATUS_ATTEMPTED);
            }
        } else {
            question.setStatus(Constant.STATUS_TODO);
        }
        question.setQuestionId(object.getString("questionId"));
        question.setPostCount(object.getInteger("postCount"));
        question.setTitle(object.getString("questionTitle"));
        question.setLevel(object.getInteger("difficulty"));
        question.setQuestionUUid(object.getString("questionUUid"));
        return question;
    }

    private static List<Tag> parseTag(String str) {
        List<Tag> tags = new ArrayList<Tag>();

        if (StringUtils.isNotBlank(str)) {

            JSONObject jsonObject = JSONObject.parseObject(str);
            JSONObject a = (JSONObject) jsonObject.get("data");
            JSONArray jsonArray = a.getJSONArray("newTags");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONArray array = object.getJSONArray("childTags");
                for (int j = 0; j < array.size(); j++) {
                    JSONObject jObject = array.getJSONObject(j);
                    Tag tag = new Tag();
                    tag.setType("tag");
                    tag.setTagId(jObject.getInteger("id"));
                    tag.setName(jObject.getString("title"));
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    private static List<Tag> parseCategory(String str) {
        List<Tag> tags = new ArrayList<Tag>();

        if (StringUtils.isNotBlank(str)) {

            JSONArray jsonArray = JSONArray.parseObject(str).getJSONObject("data").getJSONArray("menu");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Tag tag = new Tag();
                tag.setType("category");
                Object pid = object.getJSONArray("list").getJSONObject(0).get("pid");
                if (pid != null) {
                    tag.setTagId((int)pid);
                } else {
                    tag.setTagId((int)object.getJSONArray("list").getJSONObject(0).get("topic"));
                    tag.setHasPid(false);
                }
                tag.setName(object.getString("name"));
                tags.add(tag);
            }
        }
        return tags;
    }

    private static List<Tag> parseList(String str) {
        List<Tag> tags = new ArrayList<Tag>();

        if (StringUtils.isNotBlank(str)) {

            JSONArray jsonArray = JSONObject.parseObject(str).getJSONObject("data").getJSONArray("menu");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONArray array = jsonArray.getJSONObject(i).getJSONArray("list");
                for (int j = 0; j < array.size(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    Tag tag = new Tag();
                    tag.setTagId(object.getInteger("topic"));
                    tag.setType(object.getString("list"));
                    String name = object.getString("name");
                    tag.setName(name);
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public static boolean fillQuestion(Question question, CodeTypeEnum codeTypeEnum, Project project) {

        if (StringUtils.isBlank(question.getContent())) {
            return getQuestion(question, codeTypeEnum, project);
        }
        return true;
    }

    private static boolean getQuestion(Question question, CodeTypeEnum codeTypeEnum, Project project) {
        String msg = "";
        try {
            HttpRequest httpRequest = HttpRequest.get(URLUtils.getNowcoderQuestionInfo() + question.getQuestionUUid());
            HttpResponse response = HttpRequestUtils.executeGet(httpRequest);
            if (response != null && response.getStatusCode() == 200) {

                String body = response.getBody();
                msg = JSONObject.parseObject(response.getBody()).get("msg").toString();

                JSONObject jObject = JSONObject.parseObject(body).getJSONObject("data");
                JSONObject jsonObject = jObject.getJSONObject("codingProblem");
                JSONObject questionInfo = jObject.getJSONObject("question");

//                question.setCode(jsonObject.getString(codeTypeEnum.getLangSlug() + "Template"));
                JSONObject samples = questionInfo.getJSONArray("samples").getJSONObject(0);
                question.setInputSample(samples.getString("input"));
                question.setOutputSample(samples.getString("output"));
                question.setContent(getContent(jObject, jsonObject, samples));
                question.setQuestionId(questionInfo.getString("id"));
                question.setProblemId(questionInfo.getString("problemId"));

                question.setTitle(jsonObject.getString("title"));
                question.setLevel(questionInfo.getInteger("difficulty"));

                Config config = NowCoderPersistentConfig.getInstance().getConfig();
                Document doc = Jsoup.connect(URLUtils.getNowcoderPractice() + question.getQuestionUUid()).get();
                String code = doc.getElementById(codeTypeEnum.getLangSlug() + "Tpl") == null ? "" : doc.getElementById(codeTypeEnum.getLangSlug() + "Tpl").text();
                String javaCode = doc.getElementById("javaTpl") == null ? "" : doc.getElementById("javaTpl").text();
//                code = code.substring(code.indexOf("public") + 7);
                if (!StringUtils.isBlank(javaCode)) {
                    String regex = "public\\s+[^\\s]+\\s+\\w+\\s*\\(";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(javaCode);
                    if (matcher.find()) {
                        String matchText = matcher.group();
                        matchText = matchText.substring(0, matchText.length() - 1);
                        question.setTitleSlug(question.getQuestionNo() + matchText.trim().split(" ")[2]);
                    }
                }
                if ((StringUtils.isNotBlank(code) && code.contains("Main")) || StringUtils.isBlank(code)) {
                    if (StringUtils.isNotBlank(codeTypeEnum.getLangSlug())) {
                        question.setACM(Boolean.TRUE);
                        StringBuffer sb = new StringBuffer();
                        sb.append(codeTypeEnum.getComment()).append(Constant.SUBMIT_REGION_BEGIN).append("\n");
                        sb.append(PropertiesUtils.getInfo("acm."+codeTypeEnum.getLangSlug() + "Template"));
                        sb.append(codeTypeEnum.getComment()).append(Constant.SUBMIT_REGION_END).append("\n");
                        String sbStr = sb.toString();
                        question.setTitleSlug(question.getQuestionNo() + "Main");
                        if (config.getCustomCode() && config.getCustomFileName().contains("titleSlug")) {
                            sbStr = sbStr.replace("Main", question.getTitleSlug());
                        }
                        question.setCode(sbStr);
                    } else {
                        question.setCode(codeTypeEnum.getComment() + "There is no code of " + codeTypeEnum.getType() + " type for this problem");
                    }
                } else if (StringUtils.isNotBlank(code)) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(codeTypeEnum.getComment()).append(Constant.SUBMIT_REGION_BEGIN).append("\n");
                    String codeReplace = code.replaceAll("\\n", "\n");
                    if (config.getCustomCode()) {
                        sb.append(codeReplace.substring(0, codeReplace.length() - 1));
                    } else {
                        sb.append(codeReplace).append("\n");
                    }
                    sb.append(codeTypeEnum.getComment()).append(Constant.SUBMIT_REGION_END).append("\n");
                    String sbStr = sb.toString();
                    if (config.getCustomCode() && config.getCustomFileName().contains("titleSlug") && StringUtils.isNotBlank(question.getTitleSlug())) {
                        sbStr = sbStr.replace("Solution", VelocityTool.camelCaseName(question.getTitleSlug()));
                    }
                    question.setCode(sbStr);
                }

                return Boolean.TRUE;
            } else {
                MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code"));
            }

        } catch (Exception e) {
            LogUtils.LOG.error("获取代码失败", e);
            MessageUtils.getInstance(project).showWarnMsg("error", PropertiesUtils.getInfo("response.code") + msg);
        }
        return Boolean.FALSE;
    }

    private static String getContent(JSONObject jsonObject, JSONObject codingProblem, JSONObject samples) {
        StringBuffer sb = new StringBuffer();
        sb.append(codingProblem.getString("content"));
        Config config = NowCoderPersistentConfig.getInstance().getConfig();
        if (config.getShowTopics()) {
            JSONArray topicTagsArray = jsonObject.getJSONArray("questionTags");
            if (topicTagsArray != null && !topicTagsArray.isEmpty()) {
                sb.append("<div><br></div><div><div>Related Topics</div><div>");
                for (int i = 0; i < topicTagsArray.size(); i++) {
                    JSONObject tag = topicTagsArray.getJSONObject(i);
                    sb.append("<li>");
                    sb.append(tag.getString("name"));
                    sb.append("</li>");
                }
                sb.append("</div></div>");
                sb.append("<br>");
            }
        }
//        sb.append("<div><li>\uD83D\uDC4D " + jsonObject.getInteger("likes") + "</li><li>\uD83D\uDC4E " + jsonObject.getInteger("dislikes") + "</li></div>");
        sb.append("示例:<br>").append("输入:" + samples.getString("input") + "<br>")
                .append("输出:" + samples.getString("output") + "<br>");
        return sb.toString();
    }

}
