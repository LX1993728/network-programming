package com.liuxun.bio2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerExecutorPool {
    private ExecutorService executor;

    public HandlerExecutorPool(int maxPoolSize,int queueSize) {
        this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));
    }

    public void execute(Runnable task){
        this.executor.execute(task);
    }

//    public static void main(String[] args){
//        System.out.println(Runtime.getRuntime().availableProcessors()); // 8个
//    }
}
