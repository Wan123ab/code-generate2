package com.gysoft.codegenerate2.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClassUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ahh:mm:ss");
	/**
	 * 将字符串转换为驼峰命名
	 * 
	 * @param target
	 * @return
	 */
	public static String translateHump(String target) {
		if (target == null || target == "") {
			return "";
		}

		String[] targets = target.split("_");
		StringBuilder sb = new StringBuilder();
		if (targets.length != 0) {
			sb.append(targets[0]);
		}

		for (int i = 1; i < targets.length; i++) {
			String temp = targets[i];
			sb.append(temp.substring(0, 1).toUpperCase()).append(
					temp.substring(1).toLowerCase());
		}
		return sb.toString();

	}

	/**
	 * 将字符串转换为首字母大写的驼峰命名
	 * 
	 * @param target
	 * @return
	 */
	public static String translateFirstUp(String target) {
		if (target == null || target == "") {
			return "";
		}

		String[] targets = target.split("_");
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < targets.length; i++) {
			String temp = targets[i];
			sb.append(temp.substring(0, 1).toUpperCase()).append(
					temp.substring(1).toLowerCase());
		}
		return sb.toString();
	}

	public static String classNote(String tableDesc) {
		//转换为U8编码
		tableDesc = CharsetUtils.transcoding(tableDesc,CharsetUtils.systemCoding,CharsetUtils.defaultCoding);
		StringBuilder sb = new StringBuilder();
		sb.append("\n/**").append("\n").append(" *").append("\n").append(" *")
				.append(" ").append(tableDesc).append("\n").append(" *")
				.append(" @author ").append("\n").append(" *")
				.append(" @date ").append(sdf.format(new Date())).append("\n")
				.append(" */").append("\n");
		return sb.toString();
	}

	// 方法的注释
	public static String methondNote() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n	/**").append("\n").append("	 *").append("\n");
		return sb.toString();
	}

	public static void main(String[] args) {
		//直接使用可能存在时区错误和上下午格式化为AM/PM的问题，比如在linux系统上
		System.out.println(sdf.format(new Date()));
		System.out.println(DateFormatUtils.formatUTC(new Date(),"yyyy-MM-dd ahh:mm:ss",Locale.CHINESE));
		System.out.println(DateFormatUtils.formatUTC(new Date(),"yyyy-MM-dd ahh:mm:ss",Locale.SIMPLIFIED_CHINESE));
	}

}