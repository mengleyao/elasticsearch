package com.zly.elasticsearch.service;

import com.zly.elasticsearch.common.utils.APP;
import com.zly.elasticsearch.entity.BrandCaseInfo;
import com.zly.elasticsearch.entity.NewsInfo;
import com.zly.elasticsearch.entity.TaskInfo;
import com.zly.elasticsearch.entity.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("elasticsearchService")
public class ElasticsearchService {
    private static final Logger logger = Logger.getLogger(ElasticsearchService.class);

    private String esIndexName = "heros";

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private Client esClient;

    @Resource
    private Client client;

    /**
     * 初始化ES
     */
    public void init() {
        if (!elasticsearchTemplate.indexExists(APP.ESProp.INDEX_NAME)) {
            elasticsearchTemplate.createIndex(APP.ESProp.INDEX_NAME);
        }
        elasticsearchTemplate.putMapping(TaskInfo.class);
        elasticsearchTemplate.putMapping(NewsInfo.class);
    }

    /**
     * 检查健康状态
     */
    public boolean ping() {
        try {
            ActionFuture<ClusterHealthResponse> health = esClient.admin().cluster().health(new ClusterHealthRequest());
            ClusterHealthStatus status = health.actionGet().getStatus();
            if (status.value() == ClusterHealthStatus.RED.value()) {
                throw new RuntimeException("elasticsearch cluster health status is red.");
            }
            return true;
        } catch (Exception e) {
            logger.error("ping elasticsearch error.", e);
            return false;
        }
    }

    /**
     * 更新任务信息
     * @param taskInfoList
     * @return
     */
    public boolean update(List<TaskInfo> taskInfoList) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (TaskInfo taskInfo : taskInfoList) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(taskInfo.getTaskId()).withObject(taskInfo).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }

    public boolean insertOrUpdateTaskInfo(List<TaskInfo> taskInfoList) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (TaskInfo taskInfo : taskInfoList) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(taskInfo.getTaskId()).withObject(taskInfo).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }

    public boolean insertOrUpdateNewsInfo(List<NewsInfo> newsInfos) {
        List<IndexQuery> queries = new ArrayList<IndexQuery>();
        for (NewsInfo newsInfo : newsInfos) {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(newsInfo.getNewsId()).withObject(newsInfo).build();
            queries.add(indexQuery);
        }
        elasticsearchTemplate.bulkIndex(queries);
        return true;
    }

    public boolean insertOrUpdateNewsInfo(NewsInfo newsInfo) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(newsInfo.getNewsId()).withObject(newsInfo).build();
            elasticsearchTemplate.index(indexQuery);
            return true;
        } catch (Exception e) {
            logger.error("insert or update news info error.", e);
            return false;
        }
    }

    public boolean insertOrUpdateTaskInfo(TaskInfo taskInfo) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(taskInfo.getTaskId()).withObject(taskInfo).build();
            elasticsearchTemplate.index(indexQuery);
            return true;
        } catch (Exception e) {
            logger.error("insert or update task info error.", e);
            return false;
        }
    }

    public boolean insertOrUpdateUserInfo(UserInfo userInfo) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder().withId(userInfo.getUserId()).withObject(userInfo).build();
            elasticsearchTemplate.index(indexQuery);
            return true;
        } catch (Exception e) {
            logger.error("insert or update user info error.", e);
            return false;
        }
    }

    public <T> boolean deleteById(String id, Class<T> clzz) {
        try {
            elasticsearchTemplate.delete(clzz, id);
            return true;
        } catch (Exception e) {
            logger.error("delete " + clzz + " by id " + id + " error.", e);
            return false;
        }
    }



    public boolean insertOrUpdateBrandCaseInfo(BrandCaseInfo brandCaseInfo) {
        try {
            IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(brandCaseInfo.getId()).withObject(brandCaseInfo).build();
            elasticsearchTemplate.index(indexQuery);
            return true;
        } catch (Exception e) {
            logger.error("insert or update brandcase info error.", e);
            return false;
        }
    }

//    /** 查询 id */
//    public List<String> queryId(String type, String[] fields, String content,
//                                String sortField, SortOrder order, int from, int size) {
//        SearchRequestBuilder reqBuilder = client.prepareSearch(esIndexName)
//                .setTypes(type).setSearchType(SearchType.DEFAULT)
//                .setExplain(true);
//        QueryStringQueryBuilder queryString = QueryBuilders.queryString("\""
//                + content + "\"");
//        for (String k : fields) {
//            queryString.field(k);
//        }
//        queryString.minimumShouldMatch("10");
//        reqBuilder.setQuery(QueryBuilders.boolQuery().should(queryString))
//                .setExplain(true);
//        if (StringUtils.isNotEmpty(sortField) && order != null) {
//            reqBuilder.addSort(sortField, order);
//        }
//        if (from >= 0 && size > 0) {
//            reqBuilder.setFrom(from).setSize(size);
//        }
//        SearchResponse resp = reqBuilder.execute().actionGet();
//        SearchHit[] hits = resp.getHits().getHits();
//        ArrayList<String> results = new ArrayList<String>();
//        for (SearchHit hit : hits) {
//            results.add(hit.getId());
//        }
//        return results;
//    }
//
//    /**
//     * 查询得到结果为Map集合
//     *
//     * @param type 表
//     * @param fields 字段索引
//     * @param content 查询的值
//     * @param sortField 排序的字段
//     * @param order 排序的規則
//     * @param from 分頁
//     * @param size
//     * @return
//     */
//    public List<Map<String, Object>> queryForObject(String type,
//                                                    String[] fields, String content, String sortField, SortOrder order,
//                                                    int from, int size) {
//        SearchRequestBuilder reqBuilder = client.prepareSearch(esIndexName)
//                .setTypes(type).setSearchType(SearchType.DEFAULT)
//                .setExplain(true);
//        QueryStringQueryBuilder queryString = QueryBuilders.queryString("\""
//                + content + "\"");
//        for (String k : fields) {
//            queryString.field(k);
//        }
//        queryString.minimumShouldMatch("10");
//        reqBuilder.setQuery(QueryBuilders.boolQuery().should(queryString))
//                .setExplain(true);
//        if (StringUtils.isNotEmpty(sortField) && order != null) {
//            reqBuilder.addSort(sortField, order);
//        }
//        if (from >= 0 && size > 0) {
//            reqBuilder.setFrom(from).setSize(size);
//        }
//
//        SearchResponse resp = reqBuilder.execute().actionGet();
//        SearchHit[] hits = resp.getHits().getHits();
//
//        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
//        for (SearchHit hit : hits) {
//            results.add(hit.getSource());
//        }
//        return results;
//    }
//
//    /**
//     * QueryBuilders 所有查询入口
//     */
//    public List<Map<String, Object>> queryForObjectEq(String type,
//                                                      String[] fields, String content, String sortField, SortOrder order,
//                                                      int from, int size) {
//        SearchRequestBuilder reqBuilder = client.prepareSearch(esIndexName)
//                .setTypes(type).setSearchType(SearchType.DEFAULT)
//                .setExplain(true);
//        QueryStringQueryBuilder queryString = QueryBuilders.queryString("\""
//                + content + "\"");
//        for (String k : fields) {
//            queryString.field(k);
//        }
//        queryString.minimumShouldMatch("10");
//        reqBuilder.setQuery(QueryBuilders.boolQuery().must(queryString))
//                .setExplain(true);
//        if (StringUtils.isNotEmpty(sortField) && order != null) {
//            reqBuilder.addSort(sortField, order);
//        }
//        if (from >= 0 && size > 0) {
//            reqBuilder.setFrom(from).setSize(size);
//        }
//
//        SearchResponse resp = reqBuilder.execute().actionGet();
//        SearchHit[] hits = resp.getHits().getHits();
//
//        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
//        for (SearchHit hit : hits) {
//            results.add(hit.getSource());
//        }
//        return results;
//    }
//
//    /**
//     * 多个文字记不清是那些字,然后放进去查询
//     *
//     * @param type
//     * @param field
//     * @param countents
//     * @param sortField
//     * @param order
//     * @param from
//     * @param size
//     * @return
//     */
//    public List<Map<String, Object>> queryForObjectNotEq(String type,
//                                                         String field, Collection<String> countents, String sortField,
//                                                         SortOrder order, int from, int size) {
//
//        SearchRequestBuilder reqBuilder = client.prepareSearch(esIndexName)
//                .setTypes(type).setSearchType(SearchType.DEFAULT)
//                .setExplain(true);
//        List<String> contents = new ArrayList<String>();
//        for (String content : countents) {
//            contents.add("\"" + content + "\"");
//        }
//        TermsQueryBuilder inQuery = QueryBuilders.inQuery(field, contents);
//        inQuery.minimumShouldMatch("10");
//        reqBuilder.setQuery(QueryBuilders.boolQuery().mustNot(inQuery))
//                .setExplain(true);
//        if (StringUtils.isNotEmpty(sortField) && order != null) {
//            reqBuilder.addSort(sortField, order);
//        }
//        if (from >= 0 && size > 0) {
//            reqBuilder.setFrom(from).setSize(size);
//        }
//
//        SearchResponse resp = reqBuilder.execute().actionGet();
//        SearchHit[] hits = resp.getHits().getHits();
//
//        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
//        for (SearchHit hit : hits) {
//            results.add(hit.getSource());
//        }
//        return results;
//    }
//
//    /**
//     * Filters 查询方式
//     *
//     * 1. 1)QueryBuilders.queryString 获得基本查询
//     *    2)FilteredQueryBuilder query = QueryBuilders.filteredQuery(queryString,FilterBuilder)
//     *    3)通过上面封装成为查询,将这个query插入到reqBuilder中;完成操作
//     *
//     * 2.在   reqBuilder.setQuery(query);
//     *
//     * 3.介绍在2)中的FilterBuilder各种构造方式-参数都可以传String类型即可
//     * FilterBuilders.rangeFilter("taskState").lt(20) 小于 、 lte(20) 小于等于
//     * FilterBuilders.rangeFilter("taskState").gt(20)) 大于  、 gte(20) 大于等于
//     * FilterBuilders.rangeFilter("taskState").from(start).to(end)) 范围,也可以指定日期,用字符串就ok了
//     * @param type
//     * @param field
//     * @return
//     */
//    public List<Map<String, Object>> queryForObjectForElasticSerch(String type,
//                                                                   String field, String content,int start,int end) {
//
//        SearchRequestBuilder reqBuilder = client.prepareSearch(esIndexName)
//                .setTypes(type).setSearchType(SearchType.DEFAULT)
//                .setExplain(true);
//        QueryStringQueryBuilder queryString = QueryBuilders.queryString("\""
//                + content + "\"");
//        queryString.field(field);
//        queryString.minimumShouldMatch("10");
//
//        reqBuilder.setQuery(QueryBuilders.filteredQuery(queryString, FilterBuilders.rangeFilter("taskState").from(start).to(end)))
//                .setExplain(true);
//
//        SearchResponse resp = reqBuilder.execute().actionGet();
//        SearchHit[] hits = resp.getHits().getHits();
//
//        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
//        for (SearchHit hit : hits) {
//            results.add(hit.getSource());
//        }
//        return results;
//    }
//
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("init...");
//
//    }
}
