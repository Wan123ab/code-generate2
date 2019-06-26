package com.gysoft.codegenerate2.util;

import java.util.UUID;

public class IDUtils {

    public static String getId(){

        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
