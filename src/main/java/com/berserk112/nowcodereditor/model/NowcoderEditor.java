package com.berserk112.nowcodereditor.model;

/**
 * @author shuzijun
 */
public class NowcoderEditor {

    private Integer version = 1;

    /**
     * file path
     */
    private String path;

    /**
     * frontendQuestionId
     */
    private String questionNo;


    /**
     * content file path
     */
    private String contentPath;

    /**
     * titleSlug
     */
    private String titleSlug;

    private String host;

    private String questionUUid;

    public String getQuestionUUid() {
        return questionUUid;
    }

    public void setQuestionUUid(String questionUUid) {
        this.questionUUid = questionUUid;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getTitleSlug() {
        return titleSlug;
    }

    public void setTitleSlug(String titleSlug) {
        this.titleSlug = titleSlug;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
