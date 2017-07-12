package com.qingting.customer.dao;


import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.common.pojo.model.Pagination;


public interface MonitorDAO {
	void insertMonitor(Monitor monitor);
	Pagination<Monitor> listMonitor(String equipCode,Integer pageNo,Integer pageSize);
}
