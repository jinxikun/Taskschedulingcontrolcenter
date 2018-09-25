package com.telecom.ccs.utils.file;

import com.alibaba.fastjson.JSON;
import com.telecom.ccs.config.PropertiesConfig;
import com.telecom.ccs.config.SpringApplicationContextUtil;
import com.telecom.ccs.entities.*;
import com.telecom.ccs.entities.xml.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Dom4jUtil {

    private Logger logger = LoggerFactory.getLogger(Dom4jUtil.class);

    private  FTPClient ftp ;
    private PropertiesConfig propertiesConfig;

    public Dom4jUtil(){
        ftp =  new FTPClient();
        propertiesConfig = SpringApplicationContextUtil.getBean("propertiesConfig",PropertiesConfig.class);
    }



    public  SttInfo  parseSttInfo(String xmlPath) {


        //首先创建ftp连接，读取指定目录xml文件
        try {
            boolean flag = login(propertiesConfig.getSystem_ftp_server(),propertiesConfig.getSystem_ftp_port(),propertiesConfig.getSystem_ftp_username(),propertiesConfig.getSystem_ftp_password());
            logger.info("functon(xmlparse) ： ftp login flag: "+flag);

            // to do  ftp 异常（登陆异常）
            if(flag==false)
                return null;
        } catch (IOException e) {
            logger.error("functon(xmlparse) ： ftp login failed. "+ e.getMessage());
        }


        System.out.println("progress start ...");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String mydate = sdf.format(date);

        SttInfo sttInfo = new SttInfo();
        List<SttSentence> sttSentences = new ArrayList<SttSentence>();
        List<Silence> silences =new ArrayList<Silence> ();
        List<Interrupt> interrupted = new ArrayList<Interrupt>();

        Instance instance  = parseXml(xmlPath);


        List<Channel_search> channel_searchList=  instance.getSubject_search().getChannel_searchList();

        for(Channel_search  everyChanel: channel_searchList ){


            if(everyChanel.getFunction_long_silence()!=null){      //解析静音区
               if(everyChanel.getFunction_long_silence().getItems()!=null){
                   Items_search items_search =  everyChanel.getFunction_long_silence().getItems();
                   if(!items_search.getCount().equals("0")){
                       List<Item_search> item_searches= items_search.getItems();
                       for(Item_search item_search: item_searches){
                           Silence silence = new Silence();
                           silence.setStart(Double.parseDouble(item_search.getStart()));
                           silence.setEnd(Double.parseDouble(item_search.getEnd()));
                           silence.setInputTime(mydate);
                           silences.add(silence);
                       }
                   }
               }
            }else if(everyChanel.getFunction_interrupted()!=null){     //解析叠音区

                if(everyChanel.getFunction_interrupted().getItems()!=null){
                    Items_search items_search = everyChanel.getFunction_interrupted().getItems();
                    if(!items_search.getCount().equals("0")){
                        List<Item_search> item_searches= items_search.getItems();
                        for(Item_search item_search: item_searches){
                            Interrupt interrupt = new Interrupt();
                            interrupt.setStart(Double.parseDouble(item_search.getStart()));
                            interrupt.setEnd(Double.parseDouble(item_search.getEnd()));
                            interrupt.setInputTime(mydate);
                            interrupted.add(interrupt);

                        }
                    }
                }

            }
        }





        //

        List<Channel> channelList = instance.getSubject_speaker_separation().getChannelList();

        List<Item> itemList_n0 = null;
        List<Item> itemList_n1 = null;

        for(Channel channel : channelList){
            if(channel.getNo().equals("n0")){
                 itemList_n0 =  channel.getItems().getItemList();
            }else if(channel.getNo().equals("n1")){
                 itemList_n1 =  channel.getItems().getItemList();
            }
        }
        // 断句
        TextAndTime textAndTime = getTextAndTime(instance.getSubject_search());
        // spliteSentences(instance.getSubject_search());

        String[] n0_text_splits = textAndTime.getText_n0().split(" ");  // 坐席
        String[] n0_time_splits = textAndTime.getTime_n0().split(" ");

        String[] n1_text_splits = textAndTime.getText_n1().split(" ");  // 客户
        String[] n1_time_splits = textAndTime.getTime_n1().split(" ");


        // 解析坐席话术
        List<Integer> points_n0 = new ArrayList<Integer>();

        for(Item item : itemList_n0){
            for(int i=0;i<n0_time_splits.length;i++){
                String[] time_two = n0_time_splits[i].split(",");
                if(Integer.parseInt(time_two[1])>Integer.parseInt(item.getEnd())){

                    System.out.println("time_two[1]: "+ time_two[1]);
                    points_n0.add(i);
                    break;
                }

            }
        }
        points_n0.add(n0_time_splits.length);


        for(int i:points_n0){
            System.out.println("points_n0: "+ i);
        }


        List<String> sentences_text_n0 = new ArrayList<String>();
        List<String> sentences_time_n0 = new ArrayList<String>();
        String sentence ="";
        String time = "";
        int offset = 0;
        for(int k:points_n0){

                for(int i = offset;i<n0_text_splits.length;i++){

                    if(i<k){
                        sentence +=  n0_text_splits[i]+" ";
                        time    +=   n0_time_splits[i]+" ";
                    }
                }

                System.out.println("sentence: "+sentence);
                System.out.println("time: "+time);

                sentences_text_n0.add(sentence.trim());  // 去除 末尾 空格
                sentences_time_n0.add(time.trim());
                sentence = "";
                time = "";
                offset = k;

            }



        List<Sentence> sentencesList_n0 = new ArrayList<Sentence>();
        for(int i=0;i<itemList_n0.size();i++){
            Sentence s = new Sentence();
            s.setRole("n0");
            s.setSpeed(itemList_n0.get(i).getSpeed());
            s.setStart(itemList_n0.get(i).getStart());
            s.setEnd(itemList_n0.get(i).getEnd());
            s.setEnergy(itemList_n0.get(i).getEnergy());
            s.setEmotion_type(itemList_n0.get(i).getEmotion_type());
            s.setEmotion_score(itemList_n0.get(i).getEmotion_score());

            s.setText(sentences_text_n0.get(i));

            String[] keywords = sentences_text_n0.get(i).split(" ");
            String[] keywordstime = sentences_time_n0.get(i).split(" ");
            List<String> keywordslist = new ArrayList<String>();
            List<String> keywordslisttime = new ArrayList<String>();
            for(String keyword: keywords){
                keywordslist.add(keyword);

            }
            for(String keywordtime:keywordstime){
                keywordslisttime.add(keywordtime);
            }

            s.setKeywordsList(keywordslist);
            s.setKeywordsListTime(keywordslisttime);

            sentencesList_n0.add(s);

        }
        System.out.println("n0_text_splits length: "+n0_text_splits.length);
        System.out.println("n0_time_splits length: "+n0_time_splits.length);


        for(Sentence sentence1 : sentencesList_n0){
            System.out.println(sentence1.toString());
        }

        // *** 解析客户话术

        List<Integer> points_n1 = new ArrayList<Integer>();
        for(Item item : itemList_n1){
            for(int i=0;i<n1_time_splits.length;i++){
                String[] time_two = n1_time_splits[i].split(",");
                if(Integer.parseInt(time_two[1])>Integer.parseInt(item.getEnd())){

                    System.out.println("time_two[1]: "+ time_two[1]);
                    points_n1.add(i);
                    break;
                }

            }
        }
        points_n1.add(n1_time_splits.length);

        System.out.println("points_n1 size : "+points_n1.size());
        for(int i:points_n1){
            System.out.println("points_n1 : "+ i);
        }

        List<String> sentences_text_n1 = new ArrayList<String>();
        List<String> sentences_time_n1 = new ArrayList<String>();
        String sentence_n1 ="";
        String time_n1 = "";
        int offset_n1 = 0;
        for(int k:points_n1){

            for(int i = offset_n1;i<n1_text_splits.length;i++){

                if(i<k){
                    sentence_n1 +=  n1_text_splits[i]+" ";
                    time_n1    +=   n1_time_splits[i]+" ";
                }
            }

            System.out.println("sentence_n1: "+sentence_n1);
            System.out.println("time_n1: "+time_n1);

            sentences_text_n1.add(sentence_n1.trim());  // 去除 末尾 空格
            sentences_time_n1.add(time_n1.trim());
            sentence_n1 = "";
            time_n1 = "";
            offset_n1 = k;

        }

       // List<Sentence> sentencesList_n1 = new ArrayList<Sentence>();
        for(int i=0;i<itemList_n1.size();i++){
            Sentence s = new Sentence();
            s.setRole("n1");
            s.setSpeed(itemList_n1.get(i).getSpeed());
            s.setStart(itemList_n1.get(i).getStart());
            s.setEnd(itemList_n1.get(i).getEnd());
            s.setEnergy(itemList_n1.get(i).getEnergy());
            s.setEmotion_type(itemList_n1.get(i).getEmotion_type());
            s.setEmotion_score(itemList_n1.get(i).getEmotion_score());

            s.setText(sentences_text_n1.get(i));

            String[] keywords = sentences_text_n1.get(i).split(" ");
            String[] keywordstime = sentences_time_n1.get(i).split(" ");
            List<String> keywordslist = new ArrayList<String>();
            List<String> keywordslisttime = new ArrayList<String>();
            for(String keyword: keywords){
                keywordslist.add(keyword);

            }
            for(String keywordtime:keywordstime){
                keywordslisttime.add(keywordtime);
            }

            s.setKeywordsList(keywordslist);
            s.setKeywordsListTime(keywordslisttime);

            sentencesList_n0.add(s);

        }
        System.out.println("n1_text_splits length: "+n1_text_splits.length);
        System.out.println("n1_time_splits length: "+n1_time_splits.length);


        for(Sentence sentence1 : sentencesList_n0){
            System.out.println(sentence1.toString());
        }


        Collections.sort(sentencesList_n0, new Comparator<Sentence>() {
            @Override
            public int compare(Sentence o1, Sentence o2) {
                if(Integer.parseInt(o1.getStart())<Integer.parseInt(o2.getStart())){
                    return -1;
                }else{
                    return 1;
                }
            }
        });


        System.out.println("排序后：");
        for(Sentence sentence1 : sentencesList_n0){
            System.out.println(sentence1.toString());
        }


      for(Sentence xmlSentence : sentencesList_n0){
            SttSentence sttSentence = new SttSentence();
            if(xmlSentence.getRole().equals("n0")){
                sttSentence.setChannel(0);
            }else{
                sttSentence.setChannel(1);
            }

            sttSentence.setStart(Double.parseDouble(xmlSentence.getStart()));
            sttSentence.setEnd(Double.parseDouble(xmlSentence.getEnd()));
            sttSentence.setCentent(xmlSentence.getText());


            if(xmlSentence.getEmotion_type()!=null) {
                if (xmlSentence.getEmotion_type().equals("负面情绪")) {
                    sttSentence.setEmotion(0);
                }
            }
            sttSentence.setSpeed(Double.parseDouble(xmlSentence.getSpeed()));
            sttSentence.setEnergy(Double.parseDouble(xmlSentence.getEnergy()));
            sttSentence.setInputTime(mydate);

           List<String> keywordlist = xmlSentence.getKeywordsList();
           List<String> keywordlisttime =  xmlSentence.getKeywordsListTime();

           List<KeyWord> KeyWordlist = new ArrayList<KeyWord>();
           if(keywordlist.size()==keywordlisttime.size()){

               for(int i = 0 ;i<keywordlist.size();i++){
                   KeyWord keyWord = new KeyWord();
                   keyWord.setKeyword(keywordlist.get(i));
                   String[] times = keywordlisttime.get(i).split(",");
                   keyWord.setKeywordStart(Long.parseLong(times[0]));
                   keyWord.setKeywordEnd(Long.parseLong(times[1]));
                   KeyWordlist.add(keyWord);
               }

           }else{
               System.out.println("关键词解析出错。");
           }

           sttSentence.setKeywords(KeyWordlist);


          sttSentences.add(sttSentence);
      }

        sttInfo.setSttSentences(sttSentences);
        sttInfo.setSilences(silences);
        sttInfo.setInterrupted(interrupted);


        close(ftp);
        return  sttInfo;
    }





    public static TextAndTime getTextAndTime(Subject_search subject_search){

        TextAndTime textAndTime = new TextAndTime();

        List<Channel_search> channel_searchList = subject_search.getChannel_searchList();
        for(Channel_search  channel : channel_searchList){
            if(channel.getNo().equals("n0")){
              String text_n0 =   channel.getFuncticn_1_best().getText().getText();
              String time_n0 = channel.getFuncticn_1_best().getTime().getTime();

              textAndTime.setText_n0(text_n0);
              textAndTime.setTime_n0(time_n0);
            }else if(channel.getNo().equals("n1")){
                String text_n1 =   channel.getFuncticn_1_best().getText().getText();
                String time_n1 = channel.getFuncticn_1_best().getTime().getTime();

                textAndTime.setText_n1(text_n1);
                textAndTime.setTime_n1(time_n1);
            }
        }

        return textAndTime;
    }













    public  Instance parseXml(String xmlpath){


        Instance instance = null;
        Subject_speaker_separation subject_speaker_separation = null;
        Subject_search subject_search = null;
        List<Channel> channelList = null;


        //String filepath = xmlpath;
       // File file  = new File(filepath);
        Document document = null;
        SAXReader reader = new SAXReader();
        try {
           //  document = reader.read(file);
             document =  reader.read(ftp.retrieveFileStream(xmlpath));

        } catch (Exception e) {
            e.printStackTrace();
        }

        Element instance_ele = document.getRootElement().element("instance");
        System.out.println(instance_ele.getName()+ "  --   " +instance_ele.getText());

        // 属性
        if(instance_ele!=null){
            instance = new Instance();
            instance.setWaveuri(instance_ele.attributeValue("waveuri"));
            instance.setFmt(instance_ele.attributeValue("fmt"));
            instance.setSample_rate(instance_ele.attributeValue("sample-rate"));
            instance.setBit_rate(instance_ele.attributeValue("bit-rate"));
            instance.setChannel(instance_ele.attributeValue("channel"));
            instance.setDuration(instance_ele.attributeValue("duration"));
            instance.setFile_comment(instance_ele.attributeValue("file_comment"));
        }

        System.out.println("instance: "+instance.toString());


        List<Element>  childElements = instance_ele.elements();
        for(Element element : childElements){
            if(element.attributeValue("value").equals("speaker-separation")){
                subject_speaker_separation = new Subject_speaker_separation();
                channelList = new ArrayList<Channel>();

               List<Element> speaker_separation_children =  element.elements();
               for(Element speaker_separation_child : speaker_separation_children){
                   if(speaker_separation_child.getName().equals("channel")){
                       Channel channel = new Channel();
                       channel.setNo(speaker_separation_child.attributeValue("no"));
                       channel.setChanneluri(speaker_separation_child.attributeValue("channeluri"));

                      Element items =  speaker_separation_child.element("items");
                      Items items_s = new Items();

                      List<Element> itemList = items.elements();
                      List<Item>  list_item = new ArrayList<Item>();

                      for(Element element1 : itemList){
                          Item item = new Item();
                          item.setStart(element1.attributeValue("start"));
                          item.setEnd(element1.attributeValue("end"));
                          item.setEnergy(element1.attributeValue("energy"));
                          item.setSpeed(element1.attributeValue("speed"));
                          item.setEmotion_type(element1.attributeValue("emotion_type"));
                          item.setEmotion_score(element1.attributeValue("emotion_score"));
                          list_item.add(item);
                      }

                       items_s.setCount(items.attributeValue("count"));
                       items_s.setDuration(items.attributeValue("duration"));
                       items_s.setItemList(list_item);
                       channel.setItems(items_s);

                       channelList.add(channel);


                   }
               }

                subject_speaker_separation.setChannelList(channelList);


                instance.setSubject_speaker_separation(subject_speaker_separation);

            }else if(element.attributeValue("value").equals("search")){

                  subject_search = new Subject_search();
                  List<Channel_search> channel_searchList = new ArrayList<Channel_search>();

                  List<Element> elements = element.elements();
                  for(Element channel_element : elements){
                      if(channel_element.getName().equals("channel")){

                          Channel_search channel_search = new Channel_search();
                          channel_search.setNo(channel_element.attributeValue("no"));
                          List<Element> functionlist = channel_element.elements();
                          for(Element function_element:functionlist){
                              if(function_element.attributeValue("value").equals("long-silence")){
                                  Function_long_silence function_long_silence = new Function_long_silence();
                                  Items_search items_search = new Items_search();
                                  Element items_element = function_element.element("items");

                                  items_search.setCount(items_element.attributeValue("count"));
                                  items_search.setDuration(items_element.attributeValue("duration"));
                                  List<Item_search> item_search_list = new ArrayList<Item_search>();
                                  List<Element>  items_list = items_element.elements();

                                  for(Element element1:items_list){
                                      Item_search item = new Item_search();
                                      item.setStart(element1.attributeValue("start"));
                                      item.setEnd(element1.attributeValue("end"));
                                      item.setDuration(element1.attributeValue("duration"));
                                      item_search_list.add(item);
                                  }
                                  items_search.setItems(item_search_list);


                                  function_long_silence.setItems(items_search);
                                  channel_search.setFunction_long_silence(function_long_silence);  //设置静音区

                              }else if(function_element.attributeValue("value").equals("interrupted")){
                                  Function_interrupted function_interrupted = new Function_interrupted();
                                  Items_search items_search = new Items_search();
                                  Element items_element = function_element.element("items");
                                  List<Item_search> item_search_list = new ArrayList<Item_search>();
                                  List<Element>  items_list = items_element.elements();
                                  for(Element element1:items_list ){
                                      Item_search item = new Item_search();
                                      item.setStart(element1.attributeValue("start"));
                                      item.setEnd(element1.attributeValue("end"));
                                      item.setDuration(element1.attributeValue("duration"));
                                      item_search_list.add(item);
                                  }
                                  items_search.setItems(item_search_list);

                                  function_interrupted.setItems(items_search);
                                  channel_search.setFunction_interrupted(function_interrupted);  //设置叠音区



                              }else if(function_element.attributeValue("value").equals("high-energy")){
                                  Function_high_energy function_high_energy = new Function_high_energy();
                                  Items_search items_search = new Items_search();
                                  Element items_element = function_element.element("items");
                                  List<Item_search> item_search_list = new ArrayList<Item_search>();
                                  List<Element>  items_list = items_element.elements();
                                  for(Element element1:items_list ){
                                      Item_search item = new Item_search();
                                      item.setStart(element1.attributeValue("start"));
                                      item.setEnd(element1.attributeValue("end"));
                                      item.setDuration(element1.attributeValue("duration"));
                                      item_search_list.add(item);
                                  }
                                  items_search.setItems(item_search_list);
                                  function_high_energy.setItems(items_search);

                                  channel_search.setFunction_high_energy(function_high_energy);  //设置能量区

                              }else if(function_element.attributeValue("value").equals("1-best")){

                                  Functicn_1_best functicn_1_best = new Functicn_1_best();
                                  functicn_1_best.setValue(function_element.attributeValue("value"));
                                  functicn_1_best.setCount(function_element.attributeValue("count"));
                                  functicn_1_best.setSpeed(function_element.attributeValue("speed"));

                                  List<Element>  elements1 = function_element.elements();
                                  for(Element el : elements1){
                                      if(el.getName().equals("text")){
                                          Text text = new Text();
                                          text.setText(el.getText());
                                          functicn_1_best.setText(text);
                                      }else if(el.getName().equals("time")){
                                          Time time = new Time();
                                          time.setTime(el.getText());
                                          functicn_1_best.setTime(time);
                                      }
                                  }

                                  channel_search.setFuncticn_1_best(functicn_1_best);
                              }
                          }

                          channel_searchList.add(channel_search);

                      }else{
                          System.out.println("xml 格式中此处应该无其他标签，如出现请仔细检查xml格式。");
                      }

                  }

                subject_search.setChannel_searchList(channel_searchList);


                instance.setSubject_search(subject_search);
            }





        }




        System.out.println(instance.toString());
        System.out.println(JSON.toJSONString(instance));

         return instance;


    }


    /* 关闭ftp 连接 */
    public  void close(FTPClient client){
        if(client.isConnected()){
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* 登陆 */
    public   boolean login(String hostname,int port ,String username,String password) throws  IOException{

        ftp.setConnectTimeout(10*1000); // 设置10s 连接不上就包超时异常
        ftp.connect(hostname,port);

        int replyCode = ftp.getReplyCode();
        if(!FTPReply.isPositiveCompletion(replyCode)){
            logger.error("Connect FTP failed");
            return false;
        }

        boolean success = ftp.login(username,password);

        if(success){
            logger.info("Login ftp server success");
            return true;
        }else {
            logger.error("Login ftp server failed");
            return false;

        }

    }


}
