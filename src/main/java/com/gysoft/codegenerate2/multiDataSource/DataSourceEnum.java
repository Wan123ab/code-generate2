package com.gysoft.codegenerate2.multiDataSource;

/**
 * @author 万强
 * @date 2019/5/31 09:47
 * @desc 数据源枚举
 */
public enum DataSourceEnum {

    GY_PROJECT("projectDataSource"), GY_ACCOUNT("accountDataSource"), GY_CORE("coreDataSource"), GY_MODEL("modelDataSource");

    private String desc;

    DataSourceEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

}
