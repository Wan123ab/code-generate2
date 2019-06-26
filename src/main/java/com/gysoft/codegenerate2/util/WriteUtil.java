package com.gysoft.codegenerate2.util;

import com.gysoft.codegenerate2.enums.ClassTypeEnmu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WriteUtil {
	// 写文件
	public static void write(String baseFile, String className, String content,
			ClassTypeEnmu enums) {
		String fileName = "";

		//文件下载识别码，用于后续文件下载和删除
		String id = ThreadLocalUtils.get("fileId");
		baseFile = baseFile + File.separator + id + File.separator + className.toLowerCase() + File.separator;
		if (enums == ClassTypeEnmu.POJO) {
			fileName = baseFile + "pojo";
			className = className + ".java";
		}
		if (enums == ClassTypeEnmu.DAO) {
			fileName = baseFile + "dao";
			className = className + "Dao.java";
		}
		if (enums == ClassTypeEnmu.DAOIMPL) {
			fileName = baseFile + "dao"+ File.separator + "impl";
			className = className + "DaoImpl.java";
		}
		if (enums == ClassTypeEnmu.XML) {
			fileName = baseFile + "xml";
			className = className + "DAO.xml";
		}
		if (enums == ClassTypeEnmu.SERVICE) {
			fileName = baseFile + "service";
			className = className + "Service.java";
		}
		if (enums == ClassTypeEnmu.SERVICEIMPL) {
			fileName = baseFile + "service" + File.separator + "impl";
			className = className + "ServiceImpl.java";
		}
		File modelFile = new File(fileName, className);
		if (!modelFile.getParentFile().getParentFile().getParentFile().exists()) {
			modelFile.getParentFile().getParentFile().getParentFile().mkdirs();
		}
		if (!modelFile.getParentFile().getParentFile().exists()) {
			modelFile.getParentFile().getParentFile().mkdirs();
		}
		if (!modelFile.getParentFile().exists()) {
			modelFile.getParentFile().mkdirs();
		}
		try {
			modelFile.createNewFile();
		} catch (IOException e) {
		}

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(modelFile));
			writer.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
