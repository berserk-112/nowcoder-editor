package com.berserk112.nowcodereditor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuzijun
 */
public class Tag {

    /**
     * topicId, tabId
     */
    private Integer tagId;
    private String name;
    private String type; //topic tab pid
    private boolean isSelect = false;

    private List<Tag> Lists = new ArrayList<>();
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getType() {
        return type;
    }

    public List<Tag> getLists() {
        return Lists;
    }

    public void setLists(List<Tag> lists) {
        Lists = lists;
    }

    public void setType(String type) {
        this.type = type;
    }
}
