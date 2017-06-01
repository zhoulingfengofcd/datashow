package com.qingting.customer.dao.impl;

import java.util.List;

//import com.alipay.simplehbase.client.rowkey.BytesRowKey;
//import com.alipay.simplehbase.util.SHCUtil;
//import com.qingting.customer.common.pojo.util.DateUtil;
//import com.qingting.customer.common.pojo.util.RowKeyUtil;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.entity.Monitor;
//import com.qingting.customer.hbase.rowkey.RowKey;

public class MonitorDAOImpl implements MonitorDAO {

	@Override
	public void insertMonitor(Monitor monitor) {
		//RowKey rowKey = new BytesRowKey(RowKeyUtil.getBytes(monitor.getEquipId(), DateUtil.getMillisOfStart(),DateUtil.getMillisOfDay()));
		//SHCUtil.getSHC("monitor").insertObject(rowKey, monitor);
	}

	@Override
	public List<Monitor> listMonitor() {
		return null;
	}

}
