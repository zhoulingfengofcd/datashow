package com.qingting.customer.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alipay.simplehbase.client.rowkey.BytesRowKey;
import com.alipay.simplehbase.util.SHCUtil;
import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.common.pojo.util.DateUtil;
import com.qingting.customer.common.pojo.util.RowKeyUtil;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.hbase.doandkey.SimpleHbaseDOWithKeyResult;
import com.qingting.customer.hbase.rowkey.RowKey;
@Repository("monitorDAO")
public class MonitorDAOImpl implements MonitorDAO {

	@Override
	public void insertMonitor(Monitor monitor) {
		RowKey rowKey = new BytesRowKey(RowKeyUtil.getBytes(monitor.getEquipId(), DateUtil.getMillisOfStart(),DateUtil.getMillisOfDay()));
		SHCUtil.getSHC("monitor").insertObject(rowKey, monitor);
	}

	@Override
	public List<Monitor> listMonitor() {
		RowKey startRowKey=new BytesRowKey(RowKeyUtil.getBytes(0, DateUtil.getStartOfMillis()));
		RowKey endRowKey=new BytesRowKey(RowKeyUtil.getBytes(Integer.MAX_VALUE, DateUtil.getMillisOfStart()));
		List<SimpleHbaseDOWithKeyResult<Monitor>> listDOWithKey = SHCUtil.getSHC("monitor").findObjectAndKeyList(startRowKey,endRowKey, Monitor.class);
		List<Monitor> list=new ArrayList<Monitor>();
		for (SimpleHbaseDOWithKeyResult<Monitor> result : listDOWithKey) {
			Monitor monitor = result.getT();
			monitor.setContentOfRowKey(result.getRowKey().toBytes());
			list.add(monitor);
		}
		return list;
	}

}
