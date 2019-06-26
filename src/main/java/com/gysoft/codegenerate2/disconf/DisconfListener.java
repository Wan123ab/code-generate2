package com.gysoft.codegenerate2.disconf;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.gysoft.codegenerate2.multiDataSource.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 万强
 * @date 2019/6/3 14:35
 * @desc Disconf监听器，用于监控配置文件更新后动态刷新数据源
 */
@Component
@Slf4j
/** 指定Disconf配置文件 */
@DisconfFile(filename = "code.properties")
/** 当前类发生改变时，触发回调，由于当前类通过@DisconfFile进行了绑定，因此当code.properties改变时即触发回调 */
@DisconfUpdateService(classes = {DisconfListener.class})
public class DisconfListener implements IDisconfUpdate {

    @Resource
    private DynamicDataSource dynamicDataSource;

    /**
     * 不通过@DisconfItem标注配置项，因为code.properties可能会新增或删除数据源
     * 因此无法确定有几个配置项
     */


    /**
     * 回调方法
     * @throws Exception
     */
    @Override
    public void reload() throws Exception {
        log.info("****************监测到code.properties发生改变，开始重新初始化动态数据源！*******************");

        DisconfBeanFactoryPostProcessor.parseProperties();
        DisconfBeanFactoryPostProcessor.reInitDataSource();

        dynamicDataSource.setDefaultTargetDataSource(DisconfBeanFactoryPostProcessor.getDefaultDataSource());
        dynamicDataSource.setTargetDataSources(DisconfBeanFactoryPostProcessor.getCustomDataSources());

        log.info("*********************************动态数据源初始化完毕！**********************************");
    }
}
