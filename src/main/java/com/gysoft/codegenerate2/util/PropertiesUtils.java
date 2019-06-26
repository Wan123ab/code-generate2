package com.gysoft.codegenerate2.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author 万强
 * @date 2019/6/1 16:14
 * @desc Properties工具类
 */
@Slf4j
public class PropertiesUtils {

    /**
     * 根据文件名获取Properties对象
     * @param fileName
     * @return
     */
    public static Properties parse(String fileName){
        InputStream in = null;
        try{
            Properties prop = new Properties();
            in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
            if(in == null){
                return null;
            }
            prop.load(in);
            return prop;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据文件名和键名获取值
     * @param fileName
     * @param key
     * @return
     */
    public static String get(String fileName, String key){
        Properties prop = parse(fileName);
        if(prop != null){
            return prop.getProperty(key);
        }
        return null;
    }

    /**
     * 根据键名获取值
     * @param prop
     * @param key
     * @return
     */
    public static String get(Properties prop, String key){
        if(prop != null){
            return prop.getProperty(key);
        }
        return null;
    }

    /**
     * 写入
     * @param fileName
     * @param key
     * @param value
     */
    public static void set(String fileName, String key, String value){
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(key, value);
        set(fileName, properties);
    }

    /**
     * 写入
     * @param fileName
     * @param properties
     */
    public static void set(String fileName, Map<String, String> properties){
        InputStream in = null;
        OutputStream out = null;
        try {
            in = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
            if(in == null){
                throw new RuntimeException("读取的文件（"+fileName+"）不存在，请确认！");               }
            Properties prop = new Properties();
            prop.load(in);
            String path = PropertiesUtils.class.getResource("/"+fileName).getPath();
            out = new FileOutputStream(path);
            if(properties != null){
                Set<String> set = properties.keySet();
                for (String string : set) {
                    prop.setProperty(string, properties.get(string));
                    log.info("更新"+fileName+"的键（"+string+"）值为："+properties.get(string));
                }
            }
            prop.store(out, "update properties");
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(in != null){
                    in.close();
                }
                if(out != null){
                    out.flush();
                    out.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String s = get("code.properties", "project");
        System.out.println(s);


    }
}
