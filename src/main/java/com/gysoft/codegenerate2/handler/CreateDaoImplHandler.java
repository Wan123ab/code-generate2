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
 * 创建daoImpl代码
 * @author 万强
 * @date 2019/6/2 10:33
 */
@Service
public class CreateDaoImplHandler {

	@Resource
	private DataBaseDAO dataBaseDao;

    /**
     *
     * @param daoPath
     * @param table
     * @param baseFile
     */
	public void createDaoImpl(String packagePath,String pojoPath, String daoPath,String dataBase,String dataSource, String table, String baseFile) {
		DynamicDataSource.setDataSource(dataSource);

		String tableDesc = "";
		TableDO tableDo = dataBaseDao.getTable(dataBase,table);
		if (tableDo != null) {
			tableDesc = tableDo.getTableComment();
		}
		String className = ClassUtil.translateFirstUp(table);
		// 获取内容
		// 转换为com.gysoft.impl.project.device.dao.impl
		packagePath = packagePath + "." + className.toLowerCase() + ".dao.impl";
		String content = getCreateDaoContent(packagePath,pojoPath, daoPath, table, tableDesc);
		// 写文件
		WriteUtil.write(baseFile, className, content, ClassTypeEnmu.DAOIMPL);
	}

	/**
	 * 获取生成PO类内容
	 * 
	 * @param table
	 * @param tableDesc
	 * @return
	 */
	private String getCreateDaoContent(String packagePath,String pojoPath, String daoPath, String table, String tableDesc) {
		StringBuilder content = new StringBuilder();
		// 类的名字
		String className = ClassUtil.translateFirstUp(table);
		String humpClass = ClassUtil.translateHump(table);
		String pojoName = className;
		// 类具体内容
		// basePage内容
		content.append("package ").append(packagePath)
				.append(";\n\n");
		//导入pojo
		content.append(importPoJoPropertiesDynamic(pojoPath));
		//导入Dao，如UserDao
		content.append("import ").append(daoPath).append(";").append("\n");
		//导入EntityDaoImpl
		content.append("import com.gysoft.jdbc.dao.impl.EntityDaoImpl;").append("\n");
		//导入@Repository
		content.append("import org.springframework.stereotype.Repository;").append("\n");
		// 注释
		content.append(ClassUtil.classNote(tableDesc));
		//添加注解@Repository
		content.append("@Repository").append("\n");

		// 类的头部
		content.append("public class ").append(className).append("DaoImpl ")
				.append("extends EntityDaoImpl<"+pojoName+", String> ")
				.append("implements "+className+"Dao  ")
				.append("{").append("\n\n");
		content.append("\n").append("}");
		return content.toString();
	}

	/**
	 * PoJo添加引用
	 * 
	 * @return
	 */
	private String importPoJoPropertiesDynamic(String packagePath) {
		StringBuilder sb = new StringBuilder();
		sb.append("import ")
				.append(packagePath)
				.append(";\n");
		return sb.toString();
	}


}