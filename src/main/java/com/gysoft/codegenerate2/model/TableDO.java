package com.gysoft.codegenerate2.model;

/**
 * 表信息
 */
public class TableDO {

	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 描述
	 */
	private String tableComment;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

}