package com.zly.elasticsearch.common.utils;

import java.util.HashMap;
import java.util.Map;

public class APP {

    public static final Map<String, String> map = new HashMap<String, String>();
    public static final String CLOSED_MSG = "#################closed####################";
    public static final long DELIVERIED_TAG = -1;

    public class ESProp {
        public static final String INDEX_NAME = "heros";
        public static final String DAIDONGXI_INDEX_NAME = "daidongxi";
        public static final String TYPE_NEWS_INFO = "news_info";
        public static final String TYPE_PRODUCT_INFO = "product_info";
        public static final String TYPE_STORY_INFO = "story_info";
        public static final String TYPE_TASK_INFO = "task_info";
        public static final String TYPE_USER_INFO = "user_info";
        public static final String TYPE_BRANDCASE_INFO = "brandcase_info";
        public static final String INDEX_STORE_TYPE = "memory";
        public static final int SHARDS = 2;
        public static final int REPLICAS = 1;
        public static final String REFRESH_INTERVAL = "-1";
    }
}
