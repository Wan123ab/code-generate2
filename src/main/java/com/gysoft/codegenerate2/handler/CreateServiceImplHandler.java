package com.gysoft.codegenerate2.handler;

import com.gysoft.codegenerate2.dao.DataBaseDAO;
import com.gysoft.codegenerate2.enums.ClassTypeEnmu;
import com.gysoft.codegenerate2.model.TableDO;
import com.gysoft.codegenerate2.multiDataSource.DynamicDataSource;
import com.gysoft.codegenerate2.util.ClassUtil;
import com.gysoft.codegenerate2.util.WriteUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 创建serviceImpl代码
 * @author 万强
 * @date 2019/6/2 10:33
 */
@Service
public class CreateServiceImplHandler {

	@Resource
	private DataBaseDAO dataBaseDao;

	/**
	 * 创建serviceImpl
	 * @param servicePath
	 * @param daoPath
	 * @param dataBase
	 * @param dataSource
	 * @param table
	 * @param baseFile
	 */
	public void createServiceImpl(String packagePath,String servicePath, String daoPath, String dataBase,String dataSource, String table, String baseFile) {
		DynamicDataSource.setDataSource(dataSource);

		String tableDesc = "";
		TableDO tableDo = dataBaseDao.getTable(dataBase,table);
		if (tableDo != null) {
			tableDesc = tableDo.getTableComment();
		}
		String className = ClassUtil.translateFirstUp(table);
		// 获取内容
		// 转换为com.gysoft.impl.project.device.service.impl
		packagePath = packagePath + "." + className.toLowerCase() + ".service.impl";
		String content = getCreateDaoContent(packagePath,servicePath, daoPath, table, tableDesc);
		// 写文件
		WriteUtil.write(baseFile, className, content, ClassTypeEnmu.SERVICEIMPL);
	}

	/**
	 * 获取生成PO类内容
	 * 
	 * @param table
	 * @param tableDesc
	 * @return
	 */
	private String getCreateDaoContent(String packagePath,String servicePath, String daoPath, String table, String tableDesc) {
		StringBuilder content = new StringBuilder();
		// 类的名字
		String className = ClassUtil.translateFirstUp(table);
		// 类具体内容
		// packagePath内容
		content.append("package ").append(packagePath)
				.append(";\n\n");
		// import 其它类
		content.append(importProperties(servicePath,daoPath,className));
		// 注释
		content.append(ClassUtil.classNote(tableDesc));
		// 类的头部
		content.append("@Service\n").append("public class ").append(className)
				.append("ServiceImpl implements ").append(className)
				.append("Service").append(" {").append("\n\n");

		// 引入类
		content.append(importClass(table));
		// 尾部
		content.append("\n").append("}");
		return content.toString();
	}

	private String importClass(String table) {
		// 类的名字
		String className = ClassUtil.translateFirstUp(table);
		String humpClass = ClassUtil.translateHump(table);
		className = className + "Dao";
		humpClass = humpClass + "Dao";
		StringBuilder sb = new StringBuilder();
		sb.append("	@Resource\n").append("	private ").append(className)
				.append(" ").append(humpClass).append(";\n");
		return sb.toString();
	}

	/**
	 * 导包
	 * 
	 * @return
	 */
	private String importProperties(String servicePath,String daoPath,String className) {
		StringBuilder sb = new StringBuilder();
		sb.append("import javax.annotation.Resource;\n");
		sb.append("import ").append(servicePath).append(";\n");
		sb.append("import ").append(daoPath).append(";\n");
		sb.append("import org.springframework.stereotype.Service;\n");
		return sb.toString();
	}

}