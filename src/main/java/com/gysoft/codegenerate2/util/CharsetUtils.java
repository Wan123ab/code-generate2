package com.gysoft.codegenerate2.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author 万强
 * @date 2019/6/3 17:39
 * @desc
 */
@Slf4j
public class CharsetUtils {

    /**
     * 当前操作系统默认编码
     */
    public static final String systemCoding = System.getProperty("file.encoding");

    /**
     * 默认编码为U8
     */
    public static final String defaultCoding = "utf-8";

    public static final String ISO_8859_1 = "ISO-8859-1";

    public static String transcoding(String content, String sourceCharset,String targetCharset) {

        try {
            return new String(content.getBytes(sourceCharset),targetCharset);
        } catch (UnsupportedEncodingException e) {
            log.error("转码失败，cotent={}",content,e);
            return content;
        }
    }

    @Test
    public void test1(){
        System.out.println(systemCoding);
    }

    @Test
    public void test2(){
        System.out.println(transcoding("哈哈",systemCoding,ISO_8859_1));
        System.out.println(transcoding("哈哈","GB2312",systemCoding));
        System.out.println(transcoding("哈哈",systemCoding,defaultCoding));
    }
}
