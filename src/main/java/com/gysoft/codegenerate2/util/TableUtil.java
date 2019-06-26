package com.gysoft.codegenerate2.util;

import java.util.HashMap;
import java.util.Map;

public class TableUtil {

	public static Map<String, String> typeMap = new HashMap<>();
	static {
		typeMap.put("smallint", "Integer");
		typeMap.put("mediumint", "Integer");
		typeMap.put("int", "Integer");
		typeMap.put("integer", "Integer");
		typeMap.put("tinyint", "Integer");
		typeMap.put("bigint", "Integer");
		typeMap.put("bit", "Integer");
		typeMap.put("double", "Double");
		typeMap.put("float", "Float");
		typeMap.put("decimal", "BigDecimal");
		typeMap.put("char", "char");
		typeMap.put("varchar", "String");
		typeMap.put("timestamp", "Date");
		typeMap.put("datetime", "Date");
		typeMap.put("text", "String");
	}
}