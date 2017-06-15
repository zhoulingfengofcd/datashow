package com.qingting.customer.dao;

import java.util.List;

import com.qingting.customer.common.pojo.hbasedo.Monitor;


public interface MonitorDAO {
	void insertMonitor(Monitor monitor);
	List<Monitor> listMonitor();
}
