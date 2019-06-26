package com.gysoft.codegenerate2.disconf;

import com.alibaba.druid.pool.DruidDataSource;
import com.baidu.disconf.client.DisconfMgrBean;
import com.gysoft.codegenerate2.util.PropertiesUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author 万强
 * @date 2019/6/2 10:33
 * @desc 自定义BeanFactoryPostProcessor，用于Disconf下载配置文件到本地后，beanDefinition实例化之前
 * 进一步修改（补充）动态数据源的原始配置信息，比如添加数据源，修改数据源连接信息，
 * 达到通过Disconf动态控制Spring bean(此处即指数据源)创建的目的。
 *
 * @see DisconfMgrBean#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)
 */
@Component
public class DisconfBeanFactoryPostProcessor implements BeanFactoryPostProcessor,PriorityOrdered {

    private static ConversionService conversionService = new DefaultConversionService();
    private static PropertyValues dataSourcePropertyValues;

    //Disconf下载的code.properties文件属性，项目启动后会下载到本地，然后进行读取
    private static Map<String,Object> codePropertiesMap = new LinkedHashMap<>();

    // 如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";

    // 默认数据源
    private static DataSource defaultDataSource;

    // 更多的数据源,k-数据源标识(用于识别数据源，方便动态切换数据源)，v-数据源
    private static Map<Object, Object> customDataSources = new HashMap<>();

    //Disconf中配置的数据库集合
    private static Set<String> disconfDataBase = new HashSet<>();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 从beanFactory中拿到我们感兴趣的BeanDefinition，然后进行修改
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        // 读取code.properties，解析后将配置信息存入codePropertiesMap和disconfDataBase
        parseProperties();

        // 初始化默认数据源和自定义数据源
        initDataSource();

        // 取出指定beanDefinition进行修改
        BeanDefinition dataSource = beanFactory.getBeanDefinition("dataSource");
        MutablePropertyValues mpv = dataSource.getPropertyValues();
        // 添加默认数据源
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        // 添加所有数据源
        mpv.addPropertyValue("targetDataSources", customDataSources);

    }

    /**
     * 读取code.properties，解析后将配置信息存入codePropertiesMap和disconfDataBase
     */
    public static void parseProperties(){
        // 先清空数据，避免Disconf修改后重新解析code.properties并加入disconfDataBase
        // 出现已经删除的数据源在disconfDataBase中仍存在的问题
        disconfDataBase.clear();
        //读取code.properties
        Properties properties = PropertiesUtils.parse("code.properties");
        properties.entrySet().forEach(kv -> {
            //存入codePropertiesMap缓存Map
            codePropertiesMap.put(String.valueOf(kv.getKey()),kv.getValue());
            disconfDataBase.add(String.valueOf(kv.getKey()));
        });
    }

    /**
     * 初始化默认数据源和自定义数据源
     */
    private static void initDataSource(){
        //初始化默认数据源
        initDefaultDataSource(DynamicDataSourceRegister.getEnv());
        //初始化自定义更多的数据源
        initCustomDataSources(DynamicDataSourceRegister.getEnv());
    }

    /**
     * TODO 所有数据源的顶级接口javax.sql.DataSource，并没有定义close()方法，此处只能强转为DruidDataSource关闭，也就默认都是DruidDataSource类型，这一点有待改进
     * @see DataSource
     * 销毁默认数据源和更多的自定义数据源
     */
    private static void destroyDataSource(){
        //关闭更多的自定义数据源(包括默认数据源)
        customDataSources.entrySet().forEach(kv -> {
            ((DruidDataSource)kv.getValue()).close();
        });
    }

    /**
     * 重新初始化默认数据源和自定义数据源，需要先销毁之前的数据源
     */
    public static void reInitDataSource(){
        destroyDataSource();
        initDataSource();
    }

    /**
     * 初始化默认数据源，这里指的code.properties中第一个url
     * @param env
     */
    public static void initDefaultDataSource(Environment env) {
        Map<String, Object> dsMap ;
        //赋值给dsMap
        dsMap = DynamicDataSourceRegister.getYmlMap();

        //第一个url为默认数据源
        dsMap.put("url", codePropertiesMap.values().iterator().next());

        defaultDataSource = buildDataSource(dsMap);

        dataBinder(defaultDataSource, env);
    }

    /**
     * 初始化更多数据源
     *
     */
    public static void initCustomDataSources(Environment env) {

        //k-数据源名，v-数据源url
        codePropertiesMap.forEach((k,v) -> {
            Map<String, Object> dsMap ;
            dsMap = DynamicDataSourceRegister.getYmlMap();
            dsMap.put("url",v);
            DataSource ds = buildDataSource(dsMap);
            customDataSources.put(k, ds);
            dataBinder(ds, env);
        });
    }

    /**
     * 创建DataSource
     * @param dsMap
     * @return
     */
    @SuppressWarnings("unchecked")
    public static DataSource buildDataSource(Map<String, Object> dsMap) {
        try {
            Object type = dsMap.get("type");
            if (type == null)
                type = DATASOURCE_TYPE_DEFAULT;// 默认DataSource

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) type);

            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);
            return factory.build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为DataSource绑定更多数据
     *
     * @param dataSource
     * @param env
     */
    public static void dataBinder(DataSource dataSource, Environment env){
        RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        //dataBinder.setValidator(new LocalValidatorFactory().run(this.applicationContext));
        dataBinder.setConversionService(conversionService);
        dataBinder.setIgnoreNestedProperties(false);//false
        dataBinder.setIgnoreInvalidFields(false);//false
        dataBinder.setIgnoreUnknownFields(true);//true
        if(dataSourcePropertyValues == null){
            Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
            Map<String, Object> values = new HashMap<>(rpr);
            // 排除已经设置的属性
            values.remove("type");
            values.remove("driver-class-name");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(dataSourcePropertyValues);
    }

    public static Set<String> getDisconfDataBase() {
        return disconfDataBase;
    }

    public static DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    public static Map<Object, Object> getCustomDataSources() {
        return customDataSources;
    }
}
