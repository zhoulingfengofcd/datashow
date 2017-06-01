package com.qingting.customer.entity;

import java.util.Calendar;

public class Monitor {
	private String rowKey;
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 设备Id
	 */
	private Integer equipId;
	/**
	 * 数据
	 */
	private String data;
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 创建时间
	 */
	private Calendar calendar;

	/**
	 * 数据的版本，更新时用，监测数据只需要一个版本
	 */
	private final Byte version = 0;

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Byte getVersion() {
		return version;
	}

	

	
	
}
