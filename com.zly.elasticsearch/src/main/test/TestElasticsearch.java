import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zly.elasticsearch.entity.Student;
import com.zly.elasticsearch.entity.UserInfo;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class TestElasticsearch {
    TransportClient transportClient;
    //索引库名(库名)
    String index = "test_es";
    //类型名称(表名)
    String type = "user";

    @Before
    public void before()
    {
        /**
         * 1:通过 setting对象来指定集群配置信息
         */
        Settings settings = Settings.builder().put("cluster.name", "test").build();

        /**
         * 2：创建客户端
         * 通过setting来创建，若不指定则默认链接的集群名为elasticsearch
         * 链接使用tcp协议即9300
         */
        try {
            transportClient = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("116.196.95.212"), 9300));
        }catch (Exception e){

        }

        /**
         * 3：查看集群信息
         */
        List<DiscoveryNode> nodes = transportClient.connectedNodes();
        for (DiscoveryNode node : nodes) {
            System.out.println(node.getHostAddress());
        }
    }

    /**
     * 拼json串的方式存数据,通过prepareIndex方法
     */
    @Test
    public void testJson() {
        String json = "{" +
                "\"userId\":\"1\"," +
                "\"userName\":\"zhangmeng\"," +
                "\"nickName\":\"mengguo\"," +
                "\"headUrl\":\"www.jd.com\"," +
                "\"nickName\":\"mengguo\"," +
                "\"tags\":\"king\"," +
                "\"sex\":1," +
                "\"location\":\"beijing\"," +
                "\"type\":1" +
                "}";
        IndexResponse response = transportClient.prepareIndex(index, type,"1")
                .setSource(json, XContentType.JSON)
                .get();
        System.out.println(response.getIndex());
        System.out.println(response.getType());
        System.out.println(response.getId());
        System.out.println(response.getVersion());
    }

    /**
     * 用Map的方式插数据,通过prepareIndex方法
     */
    @Test
    public void testMap() {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("userId","2");
        json.put("userName","zhangliyao");
        json.put("nickName","xiannvjiejie");
        json.put("headUrl","www.jd.com");
        json.put("tags","nannan");
        json.put("sex",2);
        json.put("location","xian");
        json.put("type",1);
        IndexResponse response = transportClient.prepareIndex(index,type,"2")
                .setSource(json, XContentType.JSON)
                .get();
        System.out.println(response.getIndex());
    }

    /**
     * 用实体类的方式插入,通过prepareIndex方法
     */
    @Test
    public void testBean() throws Exception{
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId("3");
        userInfo.setUserName("liqiaoling");
        userInfo.setNickName("qiaoling");
        userInfo.setHeadUrl("www.jd.com");
        userInfo.setTags("nvshen");
        userInfo.setSex(2);
        userInfo.setLocation("wuhan");
        userInfo.setType(1);
        // 初始化json mapper
        ObjectMapper mapper = new ObjectMapper();
        // 把bean转化为json串
        byte[] json = mapper.writeValueAsBytes(userInfo);
        IndexResponse response = transportClient.prepareIndex(index,type,"3")
                .setSource(json,XContentType.JSON)
                .get();
        System.out.println(response.getIndex());
    }

    /**
     * 用esHelper的方式插入数据,通过prepareIndex方法
     */
    @Test
    public void testESHelper() throws Exception{
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("userId", "4")
                .field("userName", "sunxiaoliang")
                .field("nickName", "teacherSun")
                .field("headUrl","www.jd.com")
                .field("tags","daijian")
                .field("sex",1)
                .field("location","beijing")
                .field("type","1")
                .endObject();
        String json = builder.string();
        IndexResponse response = transportClient.prepareIndex(index,type,"4")
                .setSource(json,XContentType.JSON)
                .get();
        System.out.println(response.getIndex());
    }

    /**
     * 通过prepareGet方法获取指定文档(指定Id)信息
     */
    @Test
    public void testGet() {
        GetResponse getResponse = transportClient.prepareGet(index, type, "8").get();
        System.out.println(getResponse.getSourceAsString());
    }

    /**
     *通过prepareDelete方法删除指定文档(指定Id)信息
     */
    @Test
    public void testDelete() {
        DeleteResponse deleteResponse = transportClient.prepareDelete(index,type,"7").get();
        //验证是否删除成功
        GetResponse getResponse = transportClient.prepareGet(index, type, "1").get();
        System.out.println(getResponse.getSourceAsString());
    }

    /**
     *通过deleteByQueryAction删除符合查询条件的文档
     */
    @Test
    public void testDeleteByQuery() {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
                //删除所有符合sex为1的文档
                //TODO 当查询条件出错的时候会全部删除，比如sex的值给成String类型
                .filter(QueryBuilders.matchQuery("sex",1))
                .source(index)
                .get();
        long deleted = response.getDeleted();
        System.out.println(deleted);
    }

    /**
     * 删除文档的时候添加listener
     */

    /**
     * 通过新建UpdateRequest的方式更新文档，文档不存在也不会报错，但是插不进去（对比upsert）
     * @throws Exception
     */
    @Test
    public void testUpdateRequest() throws Exception{
        UpdateRequest request = new UpdateRequest();
        request.index(index);
        request.type(type);
        request.id("8");
        request.doc(jsonBuilder()
                    .startObject()
                    //把sex从2改成1
                    .field("sex",1)
                    .endObject());
        transportClient.update(request);
        //TODO 为什么直接调用this.testGet会出现结果延迟
    }

    /**
     * 通过prepareUpdate更新索引库中文档，如果文档不存在则会报错
     * @throws IOException
     *
     */
    @Test
    public void testPreparedUpdate() throws IOException {
        //方法一
        XContentBuilder source = jsonBuilder()
                .startObject()
                .field("userName", "will")
                .endObject();
        UpdateResponse updateResponse = transportClient
                .prepareUpdate(index, type, "1").setDoc(source).get();
        System.out.println(updateResponse.getVersion());
        //方法二
        UpdateResponse response = transportClient.prepareUpdate(index,type,"1")
                .setDoc(jsonBuilder()
                        .startObject()
                        .field("sex",2)
                        .endObject())
                .get();
        System.out.println(updateResponse.getVersion());
    }

    /**
     * 通过script更新文档，ctx._source.XXX是固定格式，如果文档不存在则会报错
     * @throws Exception
     */
    @Test
    public void testUpdateByScript() throws Exception{
        UpdateRequest request = new UpdateRequest(index,type,"1")
                .script(new Script("ctx._source.nickName=\"haha\""));
        transportClient.update(request).get();
    }

    /**
     * 更新的时候创建不存在的文档，并保存信息
     * @throws Exception
     */
    @Test
    public void testUpsert() throws Exception{
        IndexRequest indexRequest = new IndexRequest(index, type, "7")
                .source(jsonBuilder()
                        .startObject()
                        .field("userName", "Joe Smith")

                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest(index, type, "7")
                .doc(jsonBuilder()
                        .startObject()
                        .field("userName", "Wendy")
                        .field("sex",1)
                        .endObject())
                //TODO 运行结果有点奇怪
                .upsert(indexRequest);
        transportClient.update(updateRequest).get();
    }

    /**
     * 通过prepareMultiGet批量获取文档信息
     * 单Id，多Id，其他索引
     */
    @Test
    public void testMultiGet() {
        MultiGetResponse responses = transportClient.prepareMultiGet()
                .add(index,type,"1")
                .add(index,type,"2","3")
                .add("index","type","7")
                .get();
        for(MultiGetItemResponse response : responses) {
            GetResponse getResponse = response.getResponse();
            if(getResponse.isExists()) {
                System.out.print(getResponse.getSourceAsString());
            }
        }
    }

    /**
     * 通过prepareBulk执行批处理
     * @throws IOException
     */
    @Test
    public void testBulk() throws IOException {
        //1:生成bulk
        BulkRequestBuilder bulk = transportClient.prepareBulk();

        //2:新增
        IndexRequest add = new IndexRequest(index, type, "5");
        add.source(jsonBuilder()
                .startObject()
                .field("userName", "libin")
                .field("nickName", "leader")
                .endObject());

        //3:删除
        DeleteRequest del = new DeleteRequest(index, type, "1");

        //4:修改
        XContentBuilder source = jsonBuilder()
                .startObject()
                .field("userName", "jack_1")
                .endObject();
        UpdateRequest update = new UpdateRequest(index, type, "2");
        update.doc(source);

        bulk.add(del);
        bulk.add(add);
        bulk.add(update);

        //5:执行批处理
        BulkResponse bulkResponse = bulk.get();
        if(bulkResponse.hasFailures())
        {
            BulkItemResponse[] items = bulkResponse.getItems();
            for(BulkItemResponse item : items)
            {
                System.out.println(item.getFailureMessage());
            }
        }
        else
        {
            System.out.println("全部执行成功！");
        }
    }








    /**
     * 通过prepareSearch查询索引库
     * setQuery(QueryBuilders.matchQuery("name", "jack"))
     * setSearchType(SearchType.QUERY_THEN_FETCH)
     *
     */
    @Test
    public void testSearch()
    {
        SearchResponse searchResponse = transportClient.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery()) //查询所有
                //.setQuery(QueryBuilders.matchQuery("name", "tom").operator(Operator.AND)) //根据tom分词查询name,默认or
                //.setQuery(QueryBuilders.multiMatchQuery("tom", "name", "age")) //指定查询的字段
                //.setQuery(QueryBuilders.queryString("name:to* AND age:[0 TO 19]")) //根据条件查询,支持通配符大于等于0小于等于19
                //.setQuery(QueryBuilders.termQuery("name", "tom"))//查询时不分词
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setFrom(0).setSize(10)//分页
                .addSort("age", SortOrder.DESC)//排序
                .get();

        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits();
        System.out.println(total);
        SearchHit[] searchHits = hits.hits();
        for(SearchHit s : searchHits)
        {
            System.out.println(s.getSourceAsString());
        }
    }

    /**
     * 多索引，多类型查询
     * timeout
     */
    @Test
    public void testSearchsAndTimeout()
    {
        SearchResponse searchResponse = transportClient.prepareSearch("shb01","shb02").setTypes("stu","tea")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setTimeout(new TimeValue(3000l))
                .get();

        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        System.out.println(totalHits);
        SearchHit[] hits2 = hits.getHits();
        for(SearchHit h : hits2)
        {
            System.out.println(h.getSourceAsString());
        }
    }

    /**
     * 过滤，
     * lt 小于
     * gt 大于
     * lte 小于等于
     * gte 大于等于
     *
     */


    /**
     * 分组
     */
    @Test
    public void testGroupBy()
    {
        SearchResponse searchResponse = transportClient.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("group_age")
                        .field("age").size(0))//根据age分组，默认返回10，size(0)也是10
                .get();

        Terms terms = searchResponse.getAggregations().get("group_age");
        List<Terms.Bucket> buckets = (List<Terms.Bucket>) terms.getBuckets();
        for(Terms.Bucket bt : buckets)
        {
            System.out.println(bt.getKey() + " " + bt.getDocCount());
        }
    }

    /**
     * 聚合函数,本例之编写了sum，其他的聚合函数也可以实现
     *
     */
    @Test
    public void testMethod()
    {
        SearchResponse searchResponse = transportClient.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("group_name").field("name")
                        .subAggregation(AggregationBuilders.sum("sum_age").field("age")))
                .get();

        Terms terms = searchResponse.getAggregations().get("group_name");
        List<Terms.Bucket> buckets = (List<Terms.Bucket>)terms.getBuckets();
        for(Terms.Bucket bt : buckets)
        {
            Sum sum = bt.getAggregations().get("sum_age");
            System.out.println(bt.getKey() + "  " + bt.getDocCount() + " "+ sum.getValue());
        }

    }

    /**
     * 删除索引库，不可逆慎用
     */
    @Test
    public void testDeleteeIndex()
    {
        transportClient.admin().indices().prepareDelete("shb01","shb02").get();
    }

    /**
     * 求索引库文档总数
     */
}
