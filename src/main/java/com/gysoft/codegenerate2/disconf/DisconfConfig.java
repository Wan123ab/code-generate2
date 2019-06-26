package com.gysoft.codegenerate2.disconf;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
import com.baidu.disconf.client.addons.properties.ReloadingPropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author 万强
 * @date 2019/6/1 14:33
 * @desc Disconf配置类
 */
@Configuration
//采用JavaConfig进行配置，不再需要disconf.xml
//@ImportResource({"classpath:disconf.xml"})
@Import(DynamicDataSourceRegister.class)
public class DisconfConfig {

    @Bean(destroyMethod = "destroy")
    public DisconfMgrBean disconfMgrBean(){
        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage("com.gysoft");
        return disconfMgrBean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DisconfMgrBeanSecond disconfMgrBeanSecond(){
        return new DisconfMgrBeanSecond();
    }

    @Bean(name = "reloadablePropertiesFactoryBean")
    @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
    public ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean() {
        ReloadablePropertiesFactoryBean propertiesFactoryBean = new ReloadablePropertiesFactoryBean();
        propertiesFactoryBean.setSingleton(true);

        // 需要托管的配置文件
        List<String> fileNames = new ArrayList<>();
        //使用@DisconfFile托管code.properties，此处不再进行配置，以避免触发2次回调
//        fileNames.add("classpath:code.properties");

        propertiesFactoryBean.setLocations(fileNames);
        return propertiesFactoryBean;
    }

    @Bean
    public ReloadingPropertyPlaceholderConfigurer reloadingPropertyPlaceholderConfigurer(ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean){
        ReloadingPropertyPlaceholderConfigurer placeholderConfigurer = new ReloadingPropertyPlaceholderConfigurer();
        placeholderConfigurer.setIgnoreResourceNotFound(true);
        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
        try {
            Properties properties = reloadablePropertiesFactoryBean.getObject();
            placeholderConfigurer.setProperties(properties);
        } catch (IOException e) {
            throw new RuntimeException("unable to find properties", e);
        }
        return placeholderConfigurer;
    }
}

