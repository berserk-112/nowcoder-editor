package com.berserk112.nowcodereditor.manager;

import com.berserk112.nowcodereditor.model.*;
import com.berserk112.nowcodereditor.utils.MessageUtils;
import com.berserk112.nowcodereditor.utils.PropertiesUtils;
import com.berserk112.nowcodereditor.utils.URLUtils;
import com.berserk112.nowcodereditor.window.NavigatorTable;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author shuzijun
 */
public class ViewManager {

    private static Map<String, Question> questions = Maps.newLinkedHashMap();

    private static Map<String, List<Tag>> filter = Maps.newLinkedHashMap();

    private static Map<String, Sort> sortMap = Maps.newLinkedHashMap();

    static {
        sortMap.put(Constant.SORT_TYPE_TITLE, new Sort(Constant.SORT_TYPE_TITLE, "questionNo"));
        sortMap.put(Constant.SORT_TYPE_ACCEPTANCE, new Sort(Constant.SORT_TYPE_ACCEPTANCE, "acceptRate"));
        sortMap.put(Constant.SORT_TYPE_FREQUENCY, new Sort(Constant.SORT_TYPE_FREQUENCY, "postCount"));
    }


    public static void loadServiceData(NavigatorTable navigatorTable, Project project) {
        PageInfo pageInfo = QuestionManager.getQuestionService(project, navigatorTable.getPageInfo());
        if ((pageInfo.getRows() == null || pageInfo.getRows().isEmpty()) && pageInfo.getRowTotal() != 0) {
            MessageUtils.getInstance(project).showErrorMsg("error", PropertiesUtils.getInfo("response.question"));
            return;
        }

        if (filter.isEmpty()) {
            filter.put(Constant.FIND_TYPE_CATEGORY, QuestionManager.getCategory());
            filter.put(Constant.FIND_TYPE_DIFFICULTY, QuestionManager.getDifficulty());
            filter.put(Constant.FIND_TYPE_STATUS, QuestionManager.getStatus());
            filter.put(Constant.FIND_TYPE_TAGS, QuestionManager.getTags());
        }
        filter.put(Constant.FIND_TYPE_LISTS, QuestionManager.getLists());
/*
        if (CollectionUtils.isNotEmpty(filter.get(Constant.FIND_TYPE_LISTS))) {
            Map<String, Tag> oldListsMap = Maps.uniqueIndex(filter.get(Constant.FIND_TYPE_LISTS), tag -> tag.getName());
            List<Tag> newLists = QuestionManager.getLists();
            Map<String, Tag> newListsMap = Maps.uniqueIndex(newLists, tag -> tag.getName());
            newListsMap.forEach((s, tag) -> {
                if (oldListsMap.containsKey(s)) {
                    tag.setSelect(oldListsMap.get(s).isSelect());
                }
            });
            filter.put(Constant.FIND_TYPE_LISTS, newLists);
        } else {
            filter.put(Constant.FIND_TYPE_LISTS, QuestionManager.getLists());
        }
*/


        navigatorTable.loadData(pageInfo);
    }

    public static List<Tag> getFilter(String key) {
        return filter.get(key);
    }

    public static void clearFilter() {
        for (String key : filter.keySet()) {
            List<Tag> tagList = filter.get(key);
            for (Tag tag : tagList) {
                tag.setSelect(Boolean.FALSE);
            }
        }
    }

    public static Question getQuestionByUUid(String UUid, CodeTypeEnum codeTypeEnum, Project project) {
        if (StringUtils.isBlank(UUid)) {
            return null;
        }
        String key = URLUtils.getNowcoderHost() + UUid;
        if (!questions.containsKey(key)) {
            Question question = new Question();
            question.setQuestionUUid(UUid);
            if (QuestionManager.fillQuestion(question, codeTypeEnum, project)) {
                questions.put(key, question);
            } else {
                return null;
            }
        }
        return questions.get(key);
    }

    public static Question getCaCheQuestionByTitleSlug(String titleSlug, CodeTypeEnum codeTypeEnum, Project project) {
        String key = URLUtils.getNowcoderHost() + titleSlug;
        return questions.get(key);
    }

/*
    public static void pick(CodeTypeEnum codeTypeEnum, Project project) {
        String titleSlug = QuestionManager.pick();
        Question question = getQuestionByTitleSlug(titleSlug, codeTypeEnum, project);
        if (question != null) {
            CodeManager.openCode(question, project);
        }
    }
*/

    public static Sort getSort(String key) {
        return sortMap.get(key);
    }

    public static void operationType(String key) {
        sortMap.forEach((s, sort) -> {
            if (!s.equals(key)) {
                sort.resetType();
            } else {
                sort.operationType();
            }
        });
    }
}
