package com.qingting.customer.dto;

import java.util.Calendar;

public class MonitorDTO {
	
	/**
	 * 原水TDS值
	 */
	private Float rawTds;

	/**
	 * 净水TDS值
	 */
	private Float purTds;

	/**
	 * 温度值
	 */
	private Byte temp;

	/**
	 * 湿度值
	 */
	private Byte humidity;

	/**
	 * 流量值
	 */
	private Long flow;

	/**
	 * 漏水状态开关：true-漏水、false-无漏水
	 */
	private Boolean leak;
	
	/**
	 * 电磁阀输出状态 true:电磁阀供电 false:电池阀不供电
	 */
	private Boolean magnetic;
	
	/**
	 * 输出继电器 true:闭合 false:断开
	 */
	private Boolean outRelay;
	
	/**
	 * 电源继电器 true:有输出 false:无输出
	 */
	private Boolean powerRelay;

	/**
	 * 设备编号
	 */
	private String equipCode;
	/**
	 * 创建时间
	 */
	private Calendar createTime;
	
	
}
