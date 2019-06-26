package com.gysoft.codegenerate2.param;

import lombok.Data;

/**
 * @author 万强
 * @date 2019/6/2 15:01
 * @desc 数据库表代码生成参数
 */
@Data
public class TableParam {
    /**
     * 数据库名字
     */
    private String dataName;
    /**
     * 基础的包名
     */
    private String basePackage;
    /**
     * 表名
     */
    private String tableName;
}
