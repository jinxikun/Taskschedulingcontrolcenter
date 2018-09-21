package com.telecom.ccs.utils.file.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 普通线程池
 */

public class ThreadExecutorUtil {

    private static Logger logger = LoggerFactory.getLogger(ThreadExecutorUtil.class);

    private static final ThreadFactory factory = new ExceptionThreadFactory(new MyExceptionHandler());

    private static ExecutorService executorService = Executors.newCachedThreadPool(factory);


    public static  void executeTask(Runnable task){
        executorService.execute(task);
    }


    public static  class ExceptionThreadFactory implements  ThreadFactory{

        private static  final  ThreadFactory deaultFactory = Executors.defaultThreadFactory();

        private final  Thread.UncaughtExceptionHandler handler;

        public ExceptionThreadFactory(Thread.UncaughtExceptionHandler handler) {
            this.handler = handler;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = deaultFactory.newThread(r);
            thread.setUncaughtExceptionHandler(handler);
            return thread;
        }
    }


    public static class  MyExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("ThreadExecutorUtil: catch RunTimeException "+ t.getName()+" " +e.getMessage());
            e.printStackTrace();
        }
    }
}
