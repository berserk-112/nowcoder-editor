package com.berserk112.nowcodereditor.model;

/**
 * @author shuzijun
 */
public class Sort {

    private String slug;
    private String name;
    private int type = 0;

    public Sort(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Sort(String slug, int type) {
        this.slug = slug;
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int operationType() {
        this.type = this.type + 1;
        return this.type % 3;
    }

    public void resetType() {
        this.type = 0;
    }

    /**
     * 0 means not sort
     * 1 means DESC sort
     * 2 means ASC sort
     */
    public int getType() {
        return this.type % 3;
    }
}
