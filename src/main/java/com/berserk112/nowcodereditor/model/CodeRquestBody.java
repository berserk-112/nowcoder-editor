package com.berserk112.nowcodereditor.model;

public class CodeRquestBody {
    private String content;
    private String questionId;
    private String language;
    private int tagId;
    private int appId;
    private int userId;
    private String token;
    private int submitType;

    public CodeRquestBody(String content, String questionId, String language, int tagId, int appId, int userId, String token, int submintType) {
        this.content = content;
        this.questionId = questionId;
        this.language = language;
        this.tagId = tagId;
        this.appId = appId;
        this.userId = userId;
        this.token = token;
        this.submitType = submintType;
    }

    public String getContent() {
        return content;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getLanguage() {
        return language;
    }

    public int getTagId() {
        return tagId;
    }

    public int getAppId() {
        return appId;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public int getSubmitType() {
        return submitType;
    }
}
