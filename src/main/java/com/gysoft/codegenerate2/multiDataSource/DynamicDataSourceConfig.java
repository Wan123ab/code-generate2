package com.gysoft.codegenerate2.multiDataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author 万强
 * @date 2019/5/31 09:36
 * @desc 动态数据源配置，并整合mybatis
 */
@Configuration
public class DynamicDataSourceConfig {

    /**
     * 注入文件资源，也可以注入单个Resource
     */
    @Value("classpath:mybatis/*.xml")
    private Resource[] mapperLocations;

    /**
     * 默认初始化一个，以避免启动报错
     * @Primary 注解用于标识默认使用的 DataSource Bean，因为有4个 DataSource Bean，该注解可用于 master
     * 或 slave DataSource Bean, 但不能用于 dynamicDataSource Bean, 否则会产生循环调用报错：
     * The dependencies of some of the beans in the application context form a cycle
     *
     * @return
     */
    @Bean
    @Primary//非常重要，不加上将导致启动报错
    public DataSource projectDataSource(){
        //直接new一个数据源供Spring初始化SqlSessionFactory，因避免报错
        return new DruidDataSource();
    }

    /**
     * 配置mybatis
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DynamicDataSource dataSource) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        /** 实体类路径 */
        sqlSessionFactoryBean.setTypeAliasesPackage("com.gysoft.codegenerate2.model");
        /** mapper映射xml文件的所在路径 */
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/*.xml"));
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事务管理器。目前都是读操作，可有可无
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(@Autowired DynamicDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
