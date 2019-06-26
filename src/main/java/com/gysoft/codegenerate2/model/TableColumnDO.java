package com.gysoft.codegenerate2.model;

/**
 *         表字段属性
 */
public class TableColumnDO {
	/**
	 * 字段名
	 */
	private String columnName;
	/**
	 * 字段类型
	 */
	private String dataType;

	/**
	 * 字段描述
	 */
	private String columnComment;

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

}