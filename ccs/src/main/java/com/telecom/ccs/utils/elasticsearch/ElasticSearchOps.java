package com.telecom.ccs.utils.elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ElasticSearchOps {

    private static  Logger logger = LoggerFactory.getLogger(ElasticSearchOps.class);

    @Autowired
    private ElasticsearchOperations esop;

    private static ElasticsearchOperations  myesop;

    private static Client client;

    @PostConstruct
    public void init() {
        myesop = esop;
        client = myesop.getClient();
    }


    /* 判断索引是否存在 */
    public static  boolean indexIsExisted(String index){

        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();

        if(response.isExists()){
            return  true;
        }else{
            return false;
        }

    }

    /**
     * 判断type是否存在
     *
     * @param _type
     * @return
     */
    public static boolean typeIsExists(String _index, String _type) {
        String[] indices = {_index};
        TypesExistsRequest request = new TypesExistsRequest(indices, _type);
        TypesExistsResponse response = client.admin().indices().typesExists(request).actionGet();
        if (response.isExists()) {
            logger.info(_index + "." + _type + " 存在");
            return true;
        } else {
            logger.info(_index + "." + _type + " 不存在");
            return false;
        }
    }

    /**
     * 创建索引
     *
     * @param indexName
     */
    public static boolean createIndex(String indexName) {

        boolean flag = false;
        try {
            CreateIndexResponse indexResponse = client
                    .admin()
                    .indices()
                    .prepareCreate(indexName)
                    .get();

            if (indexResponse.isAcknowledged()){
                logger.info("索引 " + indexName + " 创建成功");
                return true;
            }else{
                logger.info("索引 " + indexName + " 创建失败");
                return false;
            }

        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }

        return flag;
    }

    /**
     * 创建类型
     *
     * @param indexName
     */
    public static boolean createType(String indexName,String type){

        boolean flag = false;
        try {
           flag =  createMapper(indexName,type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }



    /**
     * 创建 语音相关  es Mapping
     */
    public static boolean createMapper(String index, String type) throws IOException {
        XContentBuilder mapping = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("serialNumber").field("type", "keyword").endObject()
                .startObject("audioPath").field("type", "keyword").endObject()

                //voiceInfo
                .startObject("voiceInfo")
                .startObject("properties")
                .startObject("audioSize").field("type", "long").endObject()
                .startObject("callStartTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("callEndTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("holdDuration").field("type", "long").endObject()
                .startObject("customerNumber").field("type", "keyword").endObject()
                .startObject("seatNumber").field("type", "keyword").endObject()
                .startObject("callDirection").field("type", "long").endObject()
                .startObject("groupId").field("type", "keyword").endObject()
                .startObject("seatGroup").field("type", "keyword").endObject()
                .startObject("seatId").field("type", "keyword").endObject()
                .startObject("seatName").field("type", "keyword").endObject()
                .startObject("proPhoneNum").field("type", "keyword").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("province").field("type", "keyword").endObject()
                .startObject("isEachRecord").field("type", "keyword").endObject()
                .startObject("onHook").field("type", "keyword").endObject()
                .startObject("callerloc").field("type", "keyword").endObject()
                .startObject("customerStart").field("type", "keyword").endObject()
                .startObject("satisfaction").field("type", "keyword").endObject()
                .startObject("dissatisfactionMsg").field("type", "keyword").endObject()
                .startObject("reCallFlag").field("type", "integer").endObject()
                .startObject("workNumber").field("type", "keyword").endObject()
                .startObject("sendMsg").field("type", "integer").endObject()
                .startObject("message").field("type", "keyword").endObject()
                .startObject("proStatus").field("type", "integer").endObject()
                .endObject()
                .endObject()

                //sttInfo
                .startObject("sttInfo")
                .startObject("properties")
                .startObject("sttStartTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("sttEndTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()

                //interrupted
                .startObject("interrupted").field("type", "nested")
                .startObject("properties")
                .startObject("start").field("type", "double").endObject()
                .startObject("end").field("type", "double").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()

                //silences
                .startObject("silences").field("type", "nested")
                .startObject("properties")
                .startObject("start").field("type", "double").endObject()
                .startObject("end").field("type", "double").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()

                //sttSentences
                .startObject("sttSentences").field("type", "nested")
                .startObject("properties")
                .startObject("channel").field("type", "integer").endObject()
                .startObject("start").field("type", "double").endObject()
                .startObject("end").field("type", "double").endObject()
                .startObject("centent").field("type", "keyword").endObject()
                .startObject("emotion").field("type", "integer").endObject()
                .startObject("speed").field("type", "double").endObject()
                .startObject("energy").field("type", "integer").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("keywords").field("type", "nested")
                .startObject("properties")
                .startObject("keywordStart").field("type", "double").endObject()
                .startObject("keywordEnd").field("type", "double").endObject()
                .startObject("keyword").field("type", "keyword").endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                //businessTags
                .startObject("businessTags").field("type", "nested")
                .startObject("properties")
                .startObject("businessId").field("type", "long").endObject()
                .startObject("business").field("type", "keyword").endObject()
                .startObject("proType").field("type", "integer").endObject()
                .startObject("proValue").field("type", "integer").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()

                //ruleInfos
                .startObject("ruleInfos").field("type", "nested")
                .startObject("properties")
                .startObject("ruleId").field("type", "long").endObject()
                .startObject("ruleName").field("type", "keyword").endObject()
                .startObject("startTime").field("type", "double").endObject()
                .startObject("endTime").field("type", "double").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()

                //workScores
                .startObject("workScores").field("type", "nested")
                .startObject("properties")
                .startObject("workId").field("type", "long").endObject()
                .startObject("workName").field("type", "keyword").endObject()
                .startObject("preQuality").field("type", "integer").endObject()
                .startObject("proStatus").field("type", "integer").endObject()
                .startObject("workScore").field("type", "integer").endObject()
                .startObject("scoreNote").field("type", "keyword").endObject()
                .startObject("proUserId").field("type", "long").endObject()
                .startObject("modelRules").field("type", "nested")
                .startObject("properties")
                .startObject("modelId").field("type", "long").endObject()
                .startObject("modelName").field("type", "keyword").endObject()
                .startObject("ruleId").field("type", "long").endObject()
                .startObject("preQuality").field("type", "integer").endObject()
                .startObject("ruleName").field("type", "keyword").endObject()
                .startObject("fateful").field("type", "integer").endObject()
                .startObject("ruleTimes").field("type", "integer").endObject()
                .startObject("ruleScore").field("type", "integer").endObject()
                .startObject("ruleDetails").field("type", "nested")
                .startObject("properties")
                .startObject("startTime").field("type", "double").endObject()
                .startObject("endTime").field("type", "double").endObject()
                .startObject("ruleScore").field("type", "integer").endObject()
                .startObject("matchStatus").field("type", "integer").endObject()
                .startObject("proStatus").field("type", "integer").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("appealNote").field("type", "keyword").endObject()
                .startObject("appealTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("appealUserId").field("type", "long").endObject()
                .startObject("proNote").field("type", "keyword").endObject()
                .startObject("proTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("proUserId").field("type", "long").endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()

                //wordFrequency
                .startObject("wordFrequency").field("type", "nested")
                .startObject("properties")
                .startObject("keyword").field("type", "keyword").endObject()
                .startObject("exists").field("type", "integer").endObject()
                .startObject("count").field("type", "integer").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .endObject()
                .endObject()

                //testInfos
                .startObject("testInfos").field("type", "nested")
                .startObject("properties")
                .startObject("workId").field("type", "long").endObject()
                .startObject("workName").field("type", "keyword").endObject()
                .startObject("workScore").field("type", "integer").endObject()
                .startObject("testModelRules").field("type", "nested")
                .startObject("properties")
                .startObject("modelId").field("type", "long").endObject()
                .startObject("modelName").field("type", "keyword").endObject()
                .startObject("ruleId").field("type", "long").endObject()
                .startObject("ruleName").field("type", "keyword").endObject()
                .startObject("startTime").field("type", "double").endObject()
                .startObject("endTime").field("type", "double").endObject()
                .startObject("ruleScore").field("type", "integer").endObject()
                .startObject("matchStatus").field("type", "integer").endObject()
                .startObject("inputTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("proNote").field("type", "keyword").endObject()
                .startObject("proTime").field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd").field("type", "date").endObject()
                .startObject("proUserId").field("type", "long").endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        //index：索引名   type：类型名（可以自己定义）
        PutMappingRequest putmap = Requests.putMappingRequest(index).type(type).source(mapping);
        //为索引添加映射
        PutMappingResponse response  =  client.admin().indices().putMapping(putmap).actionGet();

        if(response.isAcknowledged()){
            return true;
        }else{
            return false;
        }
    }


    public static void insertData(String index, String type, String serialNumber) throws IOException {
        if (!indexIsExisted(index)) {
            createIndex(index);
        }
        if (!typeIsExists(index, type)) {
            createMapper(index, type);
        }
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("serialNumber", serialNumber)
                .field("audioPath", "/home/audio/now_audio.wav")
                .field("voiceInfo")
                .startObject()
                .field("audioSize", 1234)
                .field("callStartTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("callEndTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("holdDuration", 30)
                .field("customerNumber", "10000")
                .field("seatNumber", "110")
                .field("callDirection", 0)
                .field("groupId", "A")
                .field("seatGroup", "A")
                .field("seatId", "333333a")
                .field("seatName", "测试坐席")
                .field("proPhoneNum", "123456789")
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("province", "bj")
                .field("isEachRecord", "呼入")
                .field("onHook", "0")
                .field("callerloc", "北京")
                .field("customerStart", "3星")
                .field("satisfaction", "10")
                .field("dissatisfactionMsg", "")
                .field("reCallFlag", "0")
                .field("workNumber", "00000000")
                .field("sendMsg", "0")
                .field("message", "")
                .field("proStatus", 1)
                .endObject()
                .field("sttInfo")
                .startObject()
                .field("sttStartTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("sttEndTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("sttSentences")
                .startArray()
                .startObject()
                .field("channel", 0)
                .field("start", 0)
                .field("end", 10)
                .field("centent", "你好")
                .field("emotion", "")
                .field("speed", 0.2)
                .field("energy", 60)
                .field("inputTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("keywords")
                .startArray()
                .startObject()
                .field("keywordStart", 0)
                .field("keywordEnd", 10)
                .field("keyword", "你好")
                .endObject()
                .endArray()
                .endObject()
                .startObject()
                .field("channel", 1)
                .field("start", 10)
                .field("end", 20)
                .field("centent", "你好")
                .field("emotion", 1)
                .field("speed", 0.2)
                .field("energy", 60)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("keywords")
                .startArray()
                .startObject()
                .field("keywordStart", 10)
                .field("keywordEnd", 20)
                .field("keyword", "你好")
                .endObject()
                .endArray()
                .endObject()
                .endArray()
                .field("silences")
                .startArray()
                .startObject()
                .field("start", 20)
                .field("end", 30)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .endObject()
                .endArray()
                .field("interrupted")
                .startArray()
                .startObject()
                .field("start", 30)
                .field("end", 40)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .endObject()
                .endArray()
                .endObject()

                .field("businessTags")
                .startArray()
                .startObject()
                .field("businessId", 100)
                .field("business", "标签")
                .field("proType", 0)
                .field("proValue", 1)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .endObject()
                .endArray()

                .field("ruleInfos")
                .startArray()
                .startObject()
                .field("ruleId", 222)
                .field("ruleName", "你好")
                .field("startTime", 0)
                .field("endTime", 10)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .endObject()
                .endArray()

                .field("workScores")
                .startArray()
                .startObject()
                .field("workId", 3333)
                .field("workName", "任务一")
                .field("preQuality", 0)
                .field("proStatus", 0)
                .field("workScore", -10)
                .field("scoreNote", "")
                .field("proUserId", 98765)
                .field("modelRules")
                .startArray()
                .startObject()
                .field("modelId", 4444)
                .field("modelName", "模型名称")
                .field("ruleId", 222)
                .field("preQuality", 0)
                .field("ruleName", "你好")
                .field("fateful", 1)
                .field("ruleTimes", 1)
                .field("ruleScore", -10)
                .field("ruleDetails")
                .startArray()
                .startObject()
                .field("startTime", 0)
                .field("endTime", 10)
                .field("ruleScore", -10)
                .field("matchStatus", 0)
                .field("proStatus", 0)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("appealNote", "")
                .field("appealTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("appealUserId", "")
                .field("proNote", "")
                .field("proTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("proUserId", "")
                .endObject()
                .endArray()
                .endObject()
                .endArray()
                .endObject()
                .endArray()

                .field("wordFrequency")
                .startArray()
                .startObject()
                .field("keyword", "你好")
                .field("exists", 1)
                .field("count", 2)
                .field("inputTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .endObject()
                .endArray()

                .field("testInfos")
                .startArray()
                .startObject()
                .field("workId", "")
                .field("workName", "")
                .field("workScore", "")
                .field("testModelRules")
                .startArray()
                .startObject()
                .field("modelId", "")
                .field("modelName", "")
                .field("ruleId", "")
                .field("ruleName", "")
                .field("startTime", "")
                .field("endTime", "")
                .field("ruleScore", "")
                .field("matchStatus", "")
                .field("inputTime",  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("proNote", "")
                .field("proTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .field("proUserId", "")
                .endObject()
                .endArray()
                .endObject()
                .endArray()
                .endObject();
        IndexResponse response = client.prepareIndex(index, type, serialNumber).setSource(builder).get();

       logger.info("insert date: "+response.status().getStatus());
    }


    public static void insertJsonData(String index, String type, String serialNumber,String json) throws IOException {
        if (!indexIsExisted(index)) {
            createIndex(index);
        }
        if (!typeIsExists(index, type)) {
            createMapper(index, type);
        }

        IndexResponse response = client.prepareIndex(index, type, serialNumber).setSource(json,XContentType.JSON).get();

        logger.info("insert date: "+response.status().getStatus());
    }



    public static String searchDate(String index, String type, String serialNumber) {

        String result = "";
        SearchResponse  response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.termQuery("_id",serialNumber))
                .get();

        logger.info("search date: "+response.status().getStatus());
        SearchHits searchHits = response.getHits();
        logger.info("xml serialNumber："+serialNumber+" 对应elasticsearch 查询结果条数："+ searchHits.totalHits);
        if(searchHits.totalHits!=1){
            return "";
        }
        for(SearchHit searchHit : searchHits){
             result = searchHit.getSourceAsString();
        }

        logger.info("search date: "+result);
        return result;
    }

    public static String getDate(String index, String type, String serialNumber) {


        GetResponse response = client.prepareGet()
                .setIndex(index)
                .setType(type)
                .setId(serialNumber)
                .get();


        logger.info("get date: "+response.getSourceAsString());

        return response.getSourceAsString();
    }
}
