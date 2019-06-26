package com.gysoft.codegenerate2.disconf;

import com.gysoft.codegenerate2.multiDataSource.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源注册<br/>
 * 启动动态数据源请在启动类中（如SpringBootApplication）
 * 添加 @Import(DynamicDataSourceRegister.class)
 * @author 万强
 * @date 2019/6/2 10:33
 */
public class DynamicDataSourceRegister
        implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    private static Environment env ;

    //yml配置文件属性
    private static Map<String,Object> ymlMap = new HashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        logger.info("Dynamic DataSource Registry");
    }

    /**
     * 加载多数据源配置
     * 实现EnvironmentAware接口后，可以获取到系统环境变量和application配置文件中的变量
     * ApplicationContextAwareProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        initYmlMap(env);
    }

    /**
     * 初始化yml配置属性map
     *
     */
    private void initYmlMap(Environment env) {
        // 读取yml中通用配置属性
        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        ymlMap.put("type", propertyResolver.getProperty("type"));
        ymlMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
        ymlMap.put("username", propertyResolver.getProperty("username"));
        ymlMap.put("password", propertyResolver.getProperty("password"));
    }

    public static Map<String, Object> getYmlMap() {
        return ymlMap;
    }

    public static Environment getEnv() {
        return env;
    }
}
