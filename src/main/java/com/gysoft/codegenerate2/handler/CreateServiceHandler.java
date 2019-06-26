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
 * 创建service代码
 * @author 万强
 * @date 2019/6/2 10:33
 */
@Service
public class CreateServiceHandler {

	@Resource
	private DataBaseDAO dataBaseDao;

	/**
	 * 创建service
	 * @param packagePath
	 * @param dataBase
	 * @param dataSource
	 * @param table
	 * @param baseFile
	 * @return
	 */
	public String createService(String packagePath, String dataBase,String dataSource, String table, String baseFile) {
		DynamicDataSource.setDataSource(dataSource);

		String tableDesc = "";
		TableDO tableDo = dataBaseDao.getTable(dataBase,table);
		if (tableDo != null) {
			tableDesc = tableDo.getTableComment();
		}
		String className = ClassUtil.translateFirstUp(table);
		// 获取内容
		// basePage内容
		// 转换为com.gysoft.impl.project.device.dao
		packagePath = packagePath + "." + className.toLowerCase() + ".service";

		String content = getCreateDaoContent(packagePath, table, tableDesc);
		// 写文件
		WriteUtil.write(baseFile, className, content, ClassTypeEnmu.SERVICE);

		return packagePath + "." + className + "Service";
	}

	/**
	 * 获取生成PO类内容
	 * 
	 * @param table
	 * @param tableDesc
	 * @return
	 */
	private String getCreateDaoContent(String packagePath, String table, String tableDesc) {
		StringBuilder content = new StringBuilder();
		// 类的名字
		String className = ClassUtil.translateFirstUp(table);
		// 类具体内容
		content.append("package ")
				.append(packagePath)
				.append(";\n\n");
		// 注释
		content.append(ClassUtil.classNote(tableDesc));
		// 类的头部
		content.append("public interface ").append(className)
				.append("Service ").append("{").append("\n\n");
		content.append("\n").append("}");
		return content.toString();
	}

}