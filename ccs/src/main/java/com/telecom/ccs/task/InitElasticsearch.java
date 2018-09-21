package com.telecom.ccs.task;

import com.telecom.ccs.utils.elasticsearch.ElasticSearchOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitElasticsearch  implements Runnable{

    private Logger logger = LoggerFactory.getLogger(InitElasticsearch.class);
    private  String index;
    private  String type;

    public InitElasticsearch(String index,String type) {
        this.index = index;
        this.type = type;
    }

    @Override
    public void run() {
       if(!ElasticSearchOps.indexIsExisted(index)){

           ElasticSearchOps.createIndex(index);


           if(!ElasticSearchOps.typeIsExists(index,type)){
               ElasticSearchOps.createType(index,type);
           }

           logger.info("Init elasticsearch  index:"+index+" ,type:"+type);

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
