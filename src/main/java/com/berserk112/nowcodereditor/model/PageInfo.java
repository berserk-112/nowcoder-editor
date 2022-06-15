package com.berserk112.nowcodereditor.model;

import com.alibaba.fastjson.JSON;
import com.berserk112.nowcodereditor.manager.ViewManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuzijun
 */
public class PageInfo<T> {

    private int pageIndex;
    public int pageSize;
    private int rowTotal;

    private String categorySlug = "";

    private Filters filters = new Filters();

    private List<T> rows;

    public PageInfo() {
    }

    public PageInfo(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        if (rowTotal <= 0) {
            pageIndex = 1;
        } else if (pageIndex > getPageTotal()) {
            pageIndex = getPageTotal();
        }
    }

    public int getPageTotal() {
        return (rowTotal / pageSize) + ((rowTotal % pageSize) > 0 ? 1 : 0);
    }

    public int getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(int rowTotal) {
        this.rowTotal = rowTotal;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getSkip() {
        return (pageIndex - 1) * pageSize;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public Filters getFilters() {
        return filters;
    }

    public void disposeFilters(String key, String value, boolean select) {
        Field[] fields = filters.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(key)) {
                field.setAccessible(true);
                try {
                    if (List.class.isAssignableFrom(field.getType())) {
                        List list = (List) field.get(filters);
                        if (list == null && select) {
                            list = new ArrayList();
                        }
                        if (select) {
                            list.add(value);
                        } else if (list != null) {
                            list.remove(value);
                            if (list.isEmpty()) {
                                list = null;
                            }
                        }
                        field.set(filters, list);
                    } else {
                        field.set(filters, select ? value : null);
                    }
                } catch (IllegalAccessException e) {
                }
                break;
            }
        }
    }

    public void clear() {
        this.pageIndex = 1;
        this.categorySlug = "";
        this.filters.clear();
        ViewManager.clearFilter();
        ViewManager.operationType("");
    }

    public void clearFilter() {
        this.pageIndex = 1;
        this.categorySlug = "";
        this.filters.clearFilter();
        ViewManager.clearFilter();
    }

    public static class Filters {
        private String order;
        private String acs;
        public String topicId;
        public String tags;
        public String difficulty;
        public String status;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String title;

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTopicId() {
            return topicId;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void clearFilter() {
            this.topicId = null;
            this.tags = null;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getTabId() {
            return tags;
        }

        public void setTabId(String tabId) {
            this.tags = tabId;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getAcs() {
            return acs;
        }

        public void setAcs(String acs) {
            this.acs = acs;
        }

        public void clear() {
            this.order = null;
            this.acs = null;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }
}
