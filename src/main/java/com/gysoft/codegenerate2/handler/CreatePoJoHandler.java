package com.gysoft.codegenerate2.handler;

import com.gysoft.codegenerate2.dao.DataBaseDAO;
import com.gysoft.codegenerate2.enums.ClassTypeEnmu;
import com.gysoft.codegenerate2.model.TableColumnDO;
import com.gysoft.codegenerate2.model.TableDO;
import com.gysoft.codegenerate2.multiDataSource.DynamicDataSource;
import com.gysoft.codegenerate2.util.CharsetUtils;
import com.gysoft.codegenerate2.util.ClassUtil;
import com.gysoft.codegenerate2.util.TableUtil;
import com.gysoft.codegenerate2.util.WriteUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 创建pojo代码
 * @author 万强
 * @date 2019/6/2 10:33
 */
@Service
public class CreatePoJoHandler {

	@Resource
	private DataBaseDAO dataBaseDao;

	/**
	 * PoJo添加引用
	 * 
	 * @param columnList
	 * @return
	 */
	private String importPoJoProperties(List<TableColumnDO> columnList) {
		StringBuilder sb = new StringBuilder();
		if (columnList == null || columnList.isEmpty()) {
			return "";
		}
		// import属性
		Set<String> colunSet = new HashSet<>();
		for (TableColumnDO column : columnList) {
			colunSet.add(TableUtil.typeMap.get(column.getDataType()));
		}
		//lombok相关依赖
		sb.append("import com.gysoft.jdbc.annotation.Table;").append("\n");
		sb.append("import lombok.AllArgsConstructor;").append("\n");
		sb.append("import lombok.Builder;").append("\n");
		sb.append("import lombok.Data;").append("\n");
		sb.append("import lombok.NoArgsConstructor;").append("\n");

//		sb.append("import java.util.*;").append("\n");
		if (colunSet.contains("Date")) {
			sb.append("import java.util.Date;").append("\n");
		}
		if (colunSet.contains("BigDecimal")) {
			sb.append("import java.math.BigDecimal;");
		}

		return sb.toString();
	}

	/**
	 * 定义属性
	 * 
	 * @param columnDo
	 * @return
	 */
	private String getDefineProperty(TableColumnDO columnDo) {
		StringBuilder sb = new StringBuilder();
		String comment = columnDo.getColumnComment();
		//转换为U8编码
		comment = CharsetUtils.transcoding(comment,CharsetUtils.systemCoding,CharsetUtils.defaultCoding);
		if (!(comment == null || "".equals(comment))) {
			sb.append("	/**").append("\n").append("	 *").append(comment)
					.append("\n").append("	 */").append("\n");
		}
		String dataType = columnDo.getDataType();
		if (!(dataType == null || "".equals(dataType))) {
			sb.append("	private ").append(TableUtil.typeMap.get(dataType))
					.append(" ");
		}
		sb.append(ClassUtil.translateHump(columnDo.getColumnName()))
				.append(";").append("\n");
		return sb.toString();
	}

	//=========================================================================================

    /**
     * 根据页面传参动态创建pojo
     * @param table 表名
     * @param basePath 文件存放路径
     * @param packagePath 包名，如com.gysoft.impl.project
     */
    public String createPojoDynamic(String packagePath,String dataBase,String dataSource, String table, String basePath) {

		DynamicDataSource.setDataSource(dataSource);

        // 获取表的列属性
		List<TableColumnDO> columnList = dataBaseDao.listTableColumns(dataBase,table);
        String tableDesc = "";
        String tableName = "";
		TableDO tableDo = dataBaseDao.getTable(dataBase,table);
        if (tableDo != null) {
            tableDesc = tableDo.getTableComment();
			tableName = tableDo.getTableName();
        }
        // 类的名字
        String className = ClassUtil.translateFirstUp(table);

        //转换为com.gysoft.impl.project.device.pojo
		packagePath = packagePath + "." + className.toLowerCase() + ".pojo";

        // 获取内容
        String content = getCreatePojoContentDynamic(packagePath,columnList, className, tableDesc,tableName);
        // 写文件
        WriteUtil.write(basePath, className, content, ClassTypeEnmu.POJO);

        //返回生成的pojo的全路径名，用于导包
        return packagePath + "." + className ;
    }

    /**
     * 获取生成pojo需要的内容
     * @param packagePath
     * @param columnList
     * @param className
     * @param tableDesc
     * @return
     */
    private String getCreatePojoContentDynamic(String packagePath, List<TableColumnDO> columnList,
                                                String className, String tableDesc,String tableName) {
        StringBuilder content = new StringBuilder();
        // 类具体内容
        // basePage内容，如com.gysoft.impl.project.device.pojo
        content.append("package ")
                .append(packagePath)
                .append(";\n\n");
        // import 属性
        content.append(importPoJoProperties(columnList));
        // 注释
        content.append(ClassUtil.classNote(tableDesc));
        //加上@Data注解
        content.append("@Data").append("\n");
        content.append("@Builder").append("\n");
        content.append("@AllArgsConstructor").append("\n");
        content.append("@NoArgsConstructor").append("\n");
        //加上@Table注解
        content.append("@Table(name=\""+tableName+"\")").append("\n");
        // 类
        content.append("public class ").append(className).append(" ")
                .append("{").append("\n");

        // 属性定义
        for (TableColumnDO columnDo : columnList) {
            content.append(getDefineProperty(columnDo));
        }
        content.append("\n").append("}");
        return content.toString();
    }

}