package com.gysoft.codegenerate2.multiDataSource;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author 万强
 * @date 2019/5/31 09:57
 * @desc 数据源切面
 */
@Component
@Aspect
@Log
public class DataSourceAspect {

    @Pointcut("@annotation(com.gysoft.codegenerate2.multiDataSource.DataSource)")
    public void dataSource() {}

    /**
     * 使用环绕通知切换数据源
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("dataSource()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        //获取方法上的@DataSource
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if(dataSource == null){
            DynamicDataSource.setDataSource(DataSourceEnum.GY_PROJECT.getDesc());
            log.info("set datasource={} " + DataSourceEnum.GY_PROJECT.getDesc());
        }else {
            DynamicDataSource.setDataSource(dataSource.value().getDesc());
            log.info("set datasource={} " + dataSource.value().getDesc());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.info("clean dataSourceHolder");
        }
    }

    /**
     * 这种方式更加简单粗暴，直接注入注解
     * @param point
     * @param dataSource
     * @return
     * @throws Throwable
     */
//    @Around("@annotation(dataSource)")
//    public Object around(ProceedingJoinPoint point, DataSource dataSource) throws Throwable {
//
//        DynamicDataSource.setDataSource(dataSource.value().getDesc());
//        log.info("set datasource={} " + dataSource.value().getDesc());
//
//        try {
//            return point.proceed();
//        } finally {
//            DynamicDataSource.clearDataSource();
//            log.info("clean dataSourceHolder");
//        }
//    }


}
