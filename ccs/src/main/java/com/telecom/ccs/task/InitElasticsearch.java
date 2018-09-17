package com.telecom.ccs.task;

import com.telecom.ccs.utils.elasticsearch.ElasticSearchOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InitElasticsearch  implements Runnable{

    private Logger logger = LoggerFactory.getLogger(InitElasticsearch.class);
    private  String province;
    private  String index;
    private  String type;

    public InitElasticsearch(String province,String index,String type) {
        this.province = province;
        this.index = index;
        this.type = type;
    }

    @Override
    public void run() {
       if(!ElasticSearchOps.indexIsExisted(province)){
           if(!ElasticSearchOps.indexIsExisted(index)){
               ElasticSearchOps.createIndex(index);
           }

           if(!ElasticSearchOps.typeIsExists(index,type)){
               ElasticSearchOps.createType(index,type);
           }

           logger.info("Init elasticsearch  province:"+province+" , index:"+index+" ,type:"+type);

       }else{
           logger.info("CCS:  "+"Elasticsearch index:"+index+" is existed, no need to create.");
       }

  /*      try {
            ElasticSearchOps.insertData(index,type,"0000001");
            logger.info("CCS:  "+"insert date to Elasticsearch .");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
