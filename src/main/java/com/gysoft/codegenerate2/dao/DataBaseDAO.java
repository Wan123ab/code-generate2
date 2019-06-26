package com.gysoft.codegenerate2.dao;

import com.gysoft.codegenerate2.model.TableColumnDO;
import com.gysoft.codegenerate2.model.TableDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
* DataBase映射Mapper
* @author 万强
* @date 2019/6/2 14:54
*/
public interface DataBaseDAO {

	/**
	 * 获取库里面所有的表
	 * 
	 * @return
	 */
	List<String> listTables();

	/**
	 * 获取database库里表名为table的属性
	 *
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	List<TableColumnDO> listTableColumns(@Param("dataBase") String dataBase,
										 @Param("tableName") String tableName);

	/**
	 * 获取表注释
	 *
	 * @param dataBase
	 * @param tableName
	 * @return
	 */
	TableDO getTable(@Param("dataBase") String dataBase,
                     @Param("tableName") String tableName);

	/**
	 * 获取指定库所有表信息
	 *
	 * @param dataBase
	 * @return
	 */
	List<TableDO> getListTableName(@Param("dataBase") String dataBase);


}