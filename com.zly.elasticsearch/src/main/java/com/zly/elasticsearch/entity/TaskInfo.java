package com.zly.elasticsearch.entity;

import com.zly.elasticsearch.common.utils.APP;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = APP.ESProp.INDEX_NAME, type = APP.ESProp.TYPE_TASK_INFO)
public class TaskInfo {
    @Id
    @Field(index = FieldIndex.not_analyzed, store = true)
    private String taskId;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer userId;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String taskContent;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String taskArea;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String taskTags;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer taskState;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String updateTime;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String userNickName;

    public TaskInfo(String taskId, Integer userId, String taskContent, String taskArea, String taskTags, Integer taskState, String updateTime, String userNickName) {
        this.taskId = taskId;
        this.userId = userId;
        this.taskContent = taskContent;
        this.taskArea = taskArea;
        this.taskTags = taskTags;
        this.taskState = taskState;
        this.updateTime = updateTime;
        this.userNickName = userNickName;
    }

    public TaskInfo() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public String getTaskArea() {
        return taskArea;
    }

    public void setTaskArea(String taskArea) {
        this.taskArea = taskArea;
    }

    public String getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(String taskTags) {
        this.taskTags = taskTags;
    }

    public Integer getTaskState() {
        return taskState;
    }

    public void setTaskState(Integer taskState) {
        this.taskState = taskState;
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
