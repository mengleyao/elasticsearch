package com.zly.elasticsearch.entity;

import com.zly.elasticsearch.common.utils.APP;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = APP.ESProp.INDEX_NAME, type = APP.ESProp.TYPE_NEWS_INFO)
public class NewsInfo {
    @Id
    @Field(index = FieldIndex.not_analyzed, store = true)
    private String newsId;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer userId;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String newsContent;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String newsArea;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String newsTags = "";

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer newsState;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String updateTime;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String userNickName;

    public NewsInfo(String newsId, Integer userId, String newsContent, String newsArea, String newsTags, Integer newsState, String updateTime, String userNickName) {
        this.newsId = newsId;
        this.userId = userId;
        this.newsContent = newsContent;
        this.newsArea = newsArea;
        this.newsTags = newsTags;
        this.newsState = newsState;
        this.updateTime = updateTime;
        this.userNickName = userNickName;
    }

    public NewsInfo() {
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsArea() {
        return newsArea;
    }

    public void setNewsArea(String newsArea) {
        this.newsArea = newsArea;
    }

    public String getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(String newsTags) {
        this.newsTags = newsTags;
    }

    public Integer getNewsState() {
        return newsState;
    }

    public void setNewsState(Integer newsState) {
        this.newsState = newsState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}
