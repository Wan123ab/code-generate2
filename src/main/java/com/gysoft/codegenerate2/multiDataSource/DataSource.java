package com.gysoft.codegenerate2.multiDataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 万强
 * @date 2019/5/31 09:55
 * @desc 数据源注解，用于指定数据源
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

    DataSourceEnum value();

}
