package com.gysoft.codegenerate2.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

/**
 * @author 万强
 * @date 2019/5/28 09:27
 * @desc ThreadLocal工具类，实现线程级别全局缓存
 */
public class ThreadLocalUtils {

    private static final ThreadLocal<Map<String,Object>> cache = new TransmittableThreadLocal<>();

    private static final Predicate predicate = t -> t == null;

    public static void set(String key,Object value){
        if(predicate.test(cache.get())){
            reset();
        }
        cache.get().put(key,value);
    }

    public static <T> T get(String key){
        return (T) Optional.ofNullable(cache.get()).map(m -> m.get(key)).orElse(null);
    }

    public static void remove(String key){
        if(predicate.test(cache.get())){
            return;
        }
        cache.get().remove(key);
    }

    public static void clear(){
        cache.remove();
    }

    private static void reset(){
        cache.set(new HashMap<>());
    }

    /**
     * 基于ttl创建指定ExecutorService的代理，并返回
     * @param executorService
     * @return
     */
    public static ExecutorService getWappedExecutorService(ExecutorService executorService){
        return TtlExecutors.getTtlExecutorService(executorService);
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService = getWappedExecutorService(executorService);
        set("num",1);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                System.out.println(String.format("子线程名称-%s, 变量值=%s",
                        Thread.currentThread().getName(), get("num")));
            });
        }
        executorService.shutdown();
    }


}
