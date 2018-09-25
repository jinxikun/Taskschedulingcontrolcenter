package com.telecom.ccs;

import com.telecom.ccs.config.SpringApplicationContextUtil;
import com.telecom.ccs.task.CcsTaskDispatch;
import com.telecom.ccs.task.InitElasticsearch;
import com.telecom.ccs.task.consumer.Consumer_Voice_queue;
import com.telecom.ccs.utils.file.thread.ThreadExecutorUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
//@EnableAutoConfiguration(exclude =  {DataSourceAutoConfiguration.class})
//@MapperScan("com.telecom.ccs.dao")
public class CcsApplication {


	public static void main(String[] args) {


		System.out.println("CCS Springboot service is started ...");

		ApplicationContext app  = SpringApplication.run(CcsApplication.class, args);
		SpringApplicationContextUtil.setApplicationContext(app);


		//ThreadExecutorUtil.executeTask(new Consumer_Voice_queue("SD"));



		ThreadExecutorUtil.executeTask(new InitElasticsearch("SD".toLowerCase(),"task"));
		ThreadExecutorUtil.executeTask(new InitElasticsearch("HB".toLowerCase(),"task"));
		ThreadExecutorUtil.executeTask(new InitElasticsearch("BJ".toLowerCase(),"task"));
		ThreadExecutorUtil.executeTask(new CcsTaskDispatch());





	}
}
