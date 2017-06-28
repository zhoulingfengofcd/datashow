package com.qingting.customer.dto;

public class MonitorDTO {
	/**
	 * ID
	 */
	private Integer equipId;
	/**
	 * 调试用数据字段
	 */
	private String data;
	/**
	 * 调试用时间字段
	 */
	private String date;
	
	
	public Integer getEquipId() {
		return equipId;
	}
	public void setEquipId(Integer equipId) {
		this.equipId = equipId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
