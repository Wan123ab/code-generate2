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
public class CreateDaoHandler {

	@Resource
	private DataBaseDAO dataBaseDao;

	/**
	 * 动态创建dao
	 * @param packagePath 包名，如com.gysoft.impl.project
	 * @param table
	 * @param basePath
	 * @return
	 */
    public String createDaoDynamic(String packagePath,String pojoPath,String dataBase, String dataSource, String table, String basePath) {
		DynamicDataSource.setDataSource(dataSource);

        String tableDesc = "";
		TableDO tableDo = dataBaseDao.getTable(dataBase,table);
        if (tableDo != null) {
            tableDesc = tableDo.getTableComment();
        }
        String className = ClassUtil.translateFirstUp(table);
        // 获取内容
        // 转换为com.gysoft.impl.project.device.dao
		packagePath = packagePath + "." + className.toLowerCase() + ".dao";

        String content = getCreateDaoContentDynamic(packagePath,pojoPath, table, tableDesc );
        // 写文件
        WriteUtil.write(basePath, className, content, ClassTypeEnmu.DAO);

        /**
         * 返回创建的dao的全路径名
         */
        return packagePath + "." + className + "Dao";
    }

    /**
     * 获取生成Dao类内容
     * @param packagePath
     * @param table
     * @param tableDesc
     * @return
     */
    private String getCreateDaoContentDynamic(String packagePath,String pojoPath, String table, String tableDesc ) {
        StringBuilder content = new StringBuilder();
        // 类的名字
        String className = ClassUtil.translateFirstUp(table);
        String humpClass = ClassUtil.translateHump(table);
        String pojoName = className;

        // 类具体内容
        // basePage内容，如com.gysoft.impl.project.device.dao
		content.append("package ")
				.append(packagePath)
				.append(";\n\n");
        //导入pojo
        content.append(importPoJoPropertiesDynamic(pojoPath));
        //导入EntityDao
        content.append("import com.gysoft.jdbc.dao.EntityDao;");
        // 注释
        content.append(ClassUtil.classNote(tableDesc));
        // 类的头部
        content.append("public interface ").append(className).append("Dao ")
                .append("extends EntityDao<"+pojoName+", String>")
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