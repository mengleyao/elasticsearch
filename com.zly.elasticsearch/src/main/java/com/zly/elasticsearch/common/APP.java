package com.zly.elasticsearch.common;

import java.util.HashMap;
import java.util.Map;

public class APP {

    public static final Map<String, String> map = new HashMap<String, String>();
    public static final String CLOSED_MSG = "#################closed####################";
    public static final long DELIVERIED_TAG = -1;

    public class ESProp {
        public static final String INDEX_NAME = "heros";
        public static final String TYPE_NEWS_INFO = "news_info";
        public static final String TYPE_TASK_INFO = "task_info";
        public static final String TYPE_USER_INFO = "user_info";
        public static final String TYPE_BRANDCASE_INFO = "brandcase_info";
    }
}
