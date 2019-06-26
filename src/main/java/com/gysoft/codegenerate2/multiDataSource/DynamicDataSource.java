package com.gysoft.codegenerate2.multiDataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 万强
 * @date 2019/5/31 09:43
 * @desc 动态数据源扩展
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /** 存放数据源beanName，线程安全 */
    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<String, DataSource> targetDataSources) {
        /** 默认数据源 */
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        /** 所有目标数据源 */
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        /**
         * 调用初始化方法
         */
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        dataSourceHolder.set(dataSource);
    }

    public static String getDataSource() {
        return dataSourceHolder.get();
    }

    public static void clearDataSource() {
        dataSourceHolder.remove();
    }
}
