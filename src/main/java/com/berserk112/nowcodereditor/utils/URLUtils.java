package com.berserk112.nowcodereditor.utils;

import com.berserk112.nowcodereditor.setting.NowCoderPersistentConfig;
import org.apache.commons.lang.StringUtils;

/**
 * @author shuzijun
 */
public class URLUtils {

    public static final String nowcoder = "www.nowcoder.com";

    private static String nowcoderUrl = "https://";
//    private static String leetcodeLogin = "/accounts/login/";
//    private static String leetcodeLogout = "/accounts/logout/";
//    private static String leetcodeAll = "/api/problems/all/";
//    private static String leetcodeGraphql = "/graphql";
//    private static String leetcodePoints = "/points/api/";
    private static String nowcoderCategory = "gw-c.nowcoder.com/api/sparta/coding/getKnowledgeQuestionList";
    private static String nowcoderIsLogin = "/token/login-other-place";
    private static String nowcoderLogout = "/logout/do";
    /**
     * 运行代码 提交代码
     */
    private static String nowcoderCommon = "/nccommon";
    /**
     * 牛客修改了提交代码的逻辑，现在需要先访问以下url,获得token,再携带这个token去提交代码
     */
    private static String nowcoderAccessToken = "https://gw-c.nowcoder.com/api/sparta/base-oauth/access-token?token&sceneType=1";
    private static String nowcoderTopic = "/getQuestionTopic";
    //https://www.nowcoder.com/api/questiontraining/coding/knowledge-checklist?pid=520&_=1653922171549
//    https://www.nowcoder.com/api/questiontraining/coding/getTopicQuestion?pageSize=50&topicId=196&page=1&_=1653922187064

    private static String nowcoderApiService = "/api/service";
    private static String nowcoderVictorinox = "victorinox.nowcoder.com";


    private static String nowcoderJudge = "/judge";
    private static String nowcoderSubmit = "/submit";
    private static String nowcoderSubmitStatus = "/submit-status";
    private static String nowcoderSubmission = "/problem/coding/submission";
    private static String nowcoderSubmissionDetail = "/detail";
    private static String nowcoderSubmissionHistory = "/history";
    private static String nowcoderTags = "/getTopicQuestion"; //牛客没有单独获取tags的接口，每次获取题目时都传送tags
    private static String nowcoderQuestionInfo = "/practice/terminal";
    private static String nowcoderPractice = "/practice";
    private static String nowcoderQuestionApi = "/api/questiontraining/coding";
    private static String nowcoderRuncode = "/submit_cd_selftest_withoutdata";
    private static String nowcoderStatusTest = "/status-self-test";
    private static String nowcoderStatus = "/status";
    private static String nowcoderVerify = "/exam/oj";
//    private static String leetcodeFavorites = "/problems/api/favorites/";
//    private static String leetcodeVerify = "/problemset/all/";
//    private static String leetcodeProgress = "/api/progress/all/";
//    private static String leetcodeSession = "/session/";
//    private static String leetcodeCardInfo = "/problems/api/card-info/";
//    private static String leetcodeRandomOneQuestion = "/problems/random-one-question/all";

    public static String getNowcoderHost() {
        String host = NowCoderPersistentConfig.getInstance().getConfig().getUrl();
        if (StringUtils.isBlank(host)) {
            return nowcoder;
        }
        return host;
    }

    public static boolean equalsHost(String host) {
        String thisHost = getNowcoderHost();
        if(thisHost.equals(host)){
            return true;
        }else if(thisHost.equals(nowcoder)){
            return true;
        }else {
            return false;
        }
    }

    public static String getNowcoderUrl() {
        return nowcoderUrl + getNowcoderHost();
    }

    public static String getNowcoderVictorinox() {
        return nowcoderUrl + nowcoderVictorinox;
    }
    public static String getNowcoderIsLogin() {
        return getNowcoderCommon() + nowcoderIsLogin;
    }

    public static String getNowcoderCommon() {
        return getNowcoderUrl() + nowcoderCommon;
    }

    public static String getToken() {
        return nowcoderAccessToken;
    }
/*
    public static String getNowcoderSubmit() {
        return getNowcoderCommon() + nowcoderSubmit;
    }
*/
    public static String getNowcoderSubmit() {
        return getNowcoderVictorinox() + nowcoderApiService + nowcoderJudge + nowcoderSubmit;
    }

    public static String getNowcoderSubmitStatus() {
        return getNowcoderVictorinox() + nowcoderApiService + nowcoderJudge + nowcoderSubmitStatus;
    }

    public static String getNowcoderTags() {
        return getNowcoderUrl() + nowcoderQuestionApi + "/getTopicQuestion?pageSize=50&topicId=196";
    }

    public static String getNowcoderQuestion() {
        return getNowcoderUrl() + nowcoderQuestionApi + "/getTopicQuestion/";
    }

    public static String getNowcoderWebUrl() {
        return getNowcoderUrl() + "/practice/";
    }
    public static String getNowcoderTopic() {
        return getNowcoderUrl() + nowcoderQuestionApi + nowcoderTopic;
    }

    public static String getNowcoderQuestionInfo() {
        return getNowcoderUrl() + nowcoderQuestionInfo + "/";
    }

    public static String getNowcoderPractice() {
        return getNowcoderUrl() + nowcoderPractice + "/";
    }

    public static String getNowcoderStatusTest() {
        return getNowcoderCommon() + nowcoderStatusTest;
    }

    public static String getNowcoderStatus() {
        return getNowcoderCommon() + nowcoderStatus;
    }

    public static String getNowcoderVerify() {
        return getNowcoderUrl() + nowcoderVerify;
    }

    public static String getNowcoderRuncode() {
        return getNowcoderCommon() + nowcoderRuncode;
    }

    public static String getNowcoderSubmission() {
        return getNowcoderUrl() + nowcoderSubmission;
    }

    public static String getNowcoderSubmissionDetail() {
        return getNowcoderSubmission() + nowcoderSubmissionDetail;
    }

    public static String getNowcoderSubmissionHistory() {
        return getNowcoderSubmission() + nowcoderSubmissionHistory;
    }

    public static String getNowcoderLogout() {
        return getNowcoderCommon() + nowcoderLogout;
    }
}
