package com.gysoft.codegenerate2.enums;

public enum ClassTypeEnmu {

	POJO("pojo"), DAO("dao"), DAOIMPL("daoImpl"), XML("xml"), SERVICE("service"), SERVICEIMPL(
			"serviceImpl");

	private String type;

	private ClassTypeEnmu(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
