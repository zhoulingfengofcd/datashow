package com.qingting.customer.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.alipay.simplehbase.client.SimpleHbaseClient;
import com.alipay.simplehbase.client.rowkey.BytesRowKey;
import com.alipay.simplehbase.client.rowkey.RowKeyUtil;
import com.alipay.simplehbase.util.SHCUtil;
import com.qingting.customer.common.pojo.hbasedo.Area;
import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.common.pojo.hbasedo.User;
import com.qingting.customer.common.pojo.util.DateUtil;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.hbase.doandkey.SimpleHbaseDOWithKeyResult;
import com.qingting.customer.hbase.rowkey.RowKey;
@Repository("monitorDAO")
public class MonitorDAOImpl implements MonitorDAO {
	
	/*@Autowired
	public RedisTemplate<String, Integer> redisTemplate;*/
	
	private static SimpleHbaseClient tClient=SHCUtil.getSHC("monitor");
	private final static String SEQUENCE="monitor_id_seq";
	private final static byte dataVersion=0;
	/**
	 * RowKey=(8字节)
	 */
	private static RowKey createRowKey(String equipCode,Long millis,Integer days){
		//反转
		return RowKeyUtil.getRowKey(equipCode,millis,days);
	}
	private static List<Monitor> setContentOfRowKey(List<SimpleHbaseDOWithKeyResult<Monitor>> listHbase){
		/*List<User> list=new ArrayList<User>();
		for (SimpleHbaseDOWithKeyResult<User> result : listHbase) {
			User user = result.getT();
			byte[] rowkey=result.getRowKey().toBytes();
			
			//user.setRowKey(new String(rowkey));
			
			byte[] mobile=new byte[8];
			System.arraycopy(rowkey, 0, mobile, 0, 8);//前8个字节mobile
			String temp=String.valueOf(Bytes.toLong(mobile));
			user.setMobile(new StringBuffer(temp).reverse().toString());
			byte[] id=new byte[4];
			System.arraycopy(rowkey, 0, id, 0, 4);//前4个字节id
			user.setId(Bytes.toInt(id));
			
			list.add(user);
		}
		return list;*/
		
		List<Monitor> list=new ArrayList<Monitor>();
		for (SimpleHbaseDOWithKeyResult<Monitor> result : listHbase) {
			Monitor monitor = result.getT();
			byte[] rowkey=result.getRowKey().toBytes();
			
			byte[] equipCode=new byte[rowkey.length-4-8];
			System.arraycopy(rowkey, 0, equipCode, 0, rowkey.length-4-8);//最前边是设备编号
			
			monitor.setEquipCode(new String(equipCode));
			list.add(monitor);
		}
		return list;
	}
	
	@Override
	public void insertMonitor(Monitor monitor) {
		tClient.putObject(createRowKey(monitor.getEquipCode(),DateUtil.getMillisOfStart(),DateUtil.getMillisOfDay()), monitor);
	}

	@Override
	public List<Monitor> listMonitor() {
		return setContentOfRowKey(
				tClient.findObjectAndKeyList(RowKeyUtil.getMinRowKey(null,0l,0),RowKeyUtil.getMaxRowKey(null,Long.MAX_VALUE,Integer.MAX_VALUE), Monitor.class)
				);
	}

}
