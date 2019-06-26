package com.gysoft.codegenerate2.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 万强
 * @date 2019/6/20 20:04
 * @desc 字段校验注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {

    boolean nullable() default true;

    boolean emptyable() default true;

    String message() default "{参数错误，请检查！}";

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    int length() default 0;

    boolean isEmail() default false;

    boolean isPhoneNum() default false;

    boolean isCardId() default false;

    /** 手机号校验正则 */
    String REGEX_MOBILE  = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /** 邮箱校验正则 */
    String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /** 18位身份证校验正则(非严格) */
    String REGEX_IDCARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";


}
