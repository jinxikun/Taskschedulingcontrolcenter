package com.telecom.ccs.config;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;

public class SpringApplicationContextUtil {

    private static ApplicationContext applicationContext = null;

    public  static  void setApplicationContext(ApplicationContext app){
        applicationContext = app;
    }

    //  通过name获取bean
    public static Object getBean(String beanId){
        return  applicationContext.getBean(beanId);
    }

    // 通过class 获取bean
    public static <T> T getBean(Class<T> clazz){
        return  applicationContext.getBean(clazz);
    }

    //通过name 以及class 返回指定的bean
    public static  <T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name,clazz);
    }

}
