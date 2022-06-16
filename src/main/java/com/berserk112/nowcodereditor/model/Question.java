package com.berserk112.nowcodereditor.model;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author shuzijun
 */
public class Question {

    private String title;
    private String questionId;
    /**
     * use for submission history
     */
    private String problemId;
    /**
     * 题目所属topicId
     */
    private String tpId;
    private String tqId;
    private Integer level;
    /**
     * 通过率
     */
    private Double acceptRate;
    private String questionUUid;
    private String status;
//    private String titleSlug;
//    private boolean leaf = Boolean.FALSE;
//    private Map<String, String> testCase;//samples
    private String inputSample;
    private String outputSample;
    private String langSlug;
    /**
     * 考察次数
     */
    private Integer postCount;
//    private String nodeType = Constant.NODETYPE_DEF;
    /**
     * 页面的题目编号
     */
    private String questionNo;
//    private String frontendQuestionId;
    /**
     * 题目描述
     */
    private String content;

    private String code;

    /**
     * 解题成功
     */
    @Deprecated
    private Integer acs = 0;
    /**
     * 提交数
     */
    @Deprecated
    private Integer submitted = 0;

    public Question() {

    }

    public Question(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getFormTitle() {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(questionNo)) {
            sb.append("[").append(questionNo).append("]");
        }
        return sb.append(title).toString();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {this.questionId = questionId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setLevel(String difficulty) {
        if(difficulty == null){
            this.level = 0;
        }else if("入门".equalsIgnoreCase(difficulty)){
            this.level = 1;
        }else if("简单".equalsIgnoreCase(difficulty)){
            this.level = 2;
        }else if("中等".equalsIgnoreCase(difficulty)){
            this.level = 3;
        }else if("较难".equalsIgnoreCase(difficulty)){
            this.level = 4;
        }else if("困难".equalsIgnoreCase(difficulty)){
            this.level = 5;
        }else {
            this.level = 0;
        }
    }

    public String getTpId() {
        return tpId;
    }

    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    public String getTqId() {
        return tqId;
    }

    public void setTqId(String tqId) {
        this.tqId = tqId;
    }

    public String getQuestionUUid() {
        return questionUUid;
    }

    public void setQuestionUUid(String questionUUid) {
        this.questionUUid = questionUUid;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInputSample() {
        return inputSample;
    }

    public void setInputSample(String inputSample) {
        this.inputSample = inputSample;
    }

    public String getOutputSample() {
        return outputSample;
    }

    public void setOutputSample(String outputSample) {
        this.outputSample = outputSample;
    }

    public String getLangSlug() {
        return langSlug;
    }

    public void setLangSlug(String langSlug) {
        this.langSlug = langSlug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Deprecated
    public Integer getAcs() {
        return acs;
    }

    @Deprecated
    public void setAcs(Integer acs) {
        this.acs = acs;
    }

    @Deprecated
    public Integer getSubmitted() {
        return submitted;
    }

    @Deprecated
    public void setSubmitted(Integer submitted) {
        this.submitted = submitted;
    }

    public Double getAcceptRate() {
        return acceptRate;
    }

    public void setAcceptRate(Double acceptRate) {
        this.acceptRate = acceptRate;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public String getStatusSign(){

        if ("notac".equalsIgnoreCase(status) || "TRIED".equalsIgnoreCase(status)) {
            return  "❓";
        } else if ("ac".equalsIgnoreCase(status)) {
            return  "✔";
        }
        return  "   ";
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getStatusSign());
        if (StringUtils.isNotBlank(questionNo)) {
            sb.append("[").append(questionNo).append("]");
        }
        return sb.append(title).toString();

    }
}
