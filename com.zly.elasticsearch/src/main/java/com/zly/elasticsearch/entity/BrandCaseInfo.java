package com.zly.elasticsearch.entity;

import com.zly.elasticsearch.common.utils.APP;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = APP.ESProp.INDEX_NAME, type = APP.ESProp.TYPE_BRANDCASE_INFO)
public class BrandCaseInfo {
    @Id
    @Field(type = FieldType.String, store = true)
    private String id;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer number;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer userId;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String brandId;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String content;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String picture;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String price;

    @Field(type = FieldType.String, indexAnalyzer="ik", searchAnalyzer="ik", store = true)
    private String tags;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String createTime;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String updateTime;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer state;

    public BrandCaseInfo(String id, Integer number, Integer userId, String brandId, String content, String picture, String price, String tags, String createTime, String updateTime, Integer state) {
        this.id = id;
        this.number = number;
        this.userId = userId;
        this.brandId = brandId;
        this.content = content;
        this.picture = picture;
        this.price = price;
        this.tags = tags;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.state = state;
    }

    public BrandCaseInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
