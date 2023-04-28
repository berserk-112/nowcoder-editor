package com.berserk112.nowcodereditor.model;

public class CodeRquestBodyBuilder {
    private String content;
    private String questionId;
    private String language;
    private int tagId;
    private int appId;
    private int userId;
    private String token;
    private int submitType;

    private String selfInputData;

    private String selfOutputData;

    public CodeRquestBodyBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public CodeRquestBodyBuilder setQuestionId(String questionId) {
        this.questionId = questionId;
        return this;
    }

    public CodeRquestBodyBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public CodeRquestBodyBuilder setTagId(int tagId) {
        this.tagId = tagId;
        return this;
    }

    public CodeRquestBodyBuilder setAppId(int appId) {
        this.appId = appId;
        return this;
    }

    public CodeRquestBodyBuilder setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public CodeRquestBodyBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public CodeRquestBodyBuilder setSubmitType(int submitType) {
        this.submitType = submitType;
        return this;
    }

    public CodeRquestBodyBuilder setSelfInputData(String selfInputData) {
        this.selfInputData = selfInputData;
        return this;
    }

    public CodeRquestBodyBuilder setSelfOutData(String selfOutputData) {
        this.selfOutputData = selfOutputData;
        return this;
    }
    public CodeRequestBody createCodeRquestBody() {
        return new CodeRequestBody(content, questionId, language, tagId, appId, userId, token, submitType, selfInputData, selfOutputData);
    }

}