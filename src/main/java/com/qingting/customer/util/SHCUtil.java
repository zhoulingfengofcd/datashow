package com.qingting.customer.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.alipay.simplehbase.cache.CachedFileSystemResource;
import com.alipay.simplehbase.client.SimpleHbaseAdminClient;
import com.alipay.simplehbase.client.SimpleHbaseAdminClientImpl;
import com.alipay.simplehbase.client.SimpleHbaseClient;
import com.alipay.simplehbase.client.SimpleHbaseClientFactory;
import com.alipay.simplehbase.client.SimpleHbaseClientImpl;
import com.alipay.simplehbase.config.HBaseDataSource;
import com.alipay.simplehbase.config.HBaseTableConfig;
import com.alipay.simplehbase.util.AdminUtil;
import com.alipay.simplehbase.util.StringUtil;
import com.alipay.simplehbase.util.TableNameUtil;



public class SHCUtil{
	/** log. */
    final private static Logger log = LoggerFactory.getLogger(SHCUtil.class);
    
	public static String classpath=null;

	public static String HbaseSiteFile;
	public static String ZkConfigFile;

	/*final public static List<String> xmlFile=
			new ArrayList<>(Arrays.asList(
					"project","equip","monitor",
					"user","message"
					));;*/
	
	/*static String[][] project={
		{"projectFamily"},{}
	};*/
	private static final Map<String,Object> project=new HashMap<String,Object>(){
		{
			put("family","projectFamily");
		}
	};
	/*static String[][] equip={
		{"equipFamily"},{}
	};*/
	private static final Map<String,Object> equip=new HashMap<String,Object>(){
		{
			put("family","equipFamily");
		}
	};
	/*static String[][] monitor={
		{"monitorFamily"},
			new String[]{
				new String(new byte[]{48,48,48,48,48,48,48,48,48}),
				new String(new byte[]{49,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{50,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{51,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{52,48,48,48,48,48,48,48,48}),    
				new String(new byte[]{(byte)53,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{(byte)54,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{(byte)55,48,48,48,48,48,48,48,48}), 
				new String(new byte[]{(byte)56,48,48,48,48,48,48,48,48}) 
			}
	};*/
	private static final Map<String,Object> monitor=new HashMap<String,Object>(){
	
		private static final long serialVersionUID = 9112245605102867437L;
		{
			put("family",new String[]{"monitorFamily"});
			put("split",
				new byte[][]{
     		   		{25,0,0,0,0,0,0,0,0},
    				{50,0,0,0,0,0,0,0,0}, 
    				{75,0,0,0,0,0,0,0,0}, 
    				{100,0,0,0,0,0,0,0,0}, 
    				{125,0,0,0,0,0,0,0,0},    
    				{(byte)150,0,0,0,0,0,0,0,0}, 
    				{(byte)175,0,0,0,0,0,0,0,0}, 
    				{(byte)200,0,0,0,0,0,0,0,0}, 
    				{(byte)225,0,0,0,0,0,0,0,0} 
     		   	}
			);
		}
	};
	/*static String[][] user={
		{"userFamily"},{}
	};*/
	private static final Map<String,Object> user=new HashMap<String,Object>(){
		{
			put("family","userFamily");
		}
	};
	/*static String[][] message={
		{"messageFamily"},{}
	};*/
	private static final Map<String,Object> message=new HashMap<String,Object>(){
		{
			put("family","messageFamily");
		}
	};
	/*static String[][] province={
		{"provinceFamily"},{}
	};*/
	private static final Map<String,Object> province=new HashMap<String,Object>(){
		{
			put("family","provinceFamily");
		}
	};
	/*static String[][] city={
		{"cityFamily"},{}
	};*/
	private static final Map<String,Object> city=new HashMap<String,Object>(){
		{
			put("family","cityFamily");
		}
	};
	/*static String[][] area={
		{"areaFamily"},{}
	};*/
	private static final Map<String,Object> area=new HashMap<String,Object>(){
		{
			put("family","areaFamily");
		}
	};
	/**
	 * key:表名 
	 * value:列族+预分区
	 */
	private static final Map<String,Map<String,Object>> map=new HashMap<String,Map<String,Object>>(){
		private static final long serialVersionUID = 9112245605102867437L;
		{
			put("project", project);
			put("equip", equip);
			put("monitor", monitor);
			put("user", user);
			put("message", message);
			put("province",province);
			put("city",city);
			put("area",area);
		}
	};
	
	//private static volatile SimpleHbaseClient simpleHbaseClient;
	private static volatile SimpleHbaseAdminClient simpleHbaseAdminClient;
	//private static List<SimpleHbaseClient> listSHC=new ArrayList<SimpleHbaseClient>();
	private static Map<String,SimpleHbaseClient> shcMap=new HashMap<String,SimpleHbaseClient>();
	
	public void createTable(String tableName,String columnFamily) {
		// create new table.
		HTableDescriptor tableDescriptor = new HTableDescriptor(
				TableNameUtil.getTableName(tableName));
		tableDescriptor.addFamily(new HColumnDescriptor(columnFamily)
				.setMaxVersions(1));
		simpleHbaseAdminClient.createTable(tableDescriptor);
	}
	public void deleteTable(String tableName) {
		// delete the Table
		simpleHbaseAdminClient.deleteTable(tableName);
	}
	static{
		String system=System.getProperty("os.name");
		if(system.indexOf("Windows")!=-1){//window系统
			String str=Thread.currentThread().getContextClassLoader().getResource("").getPath();
			System.out.println("str="+str);
			System.out.println("index="+str.indexOf("/"));
			if(str.indexOf("/")==0){
				System.out.println("str.substring(1)="+str.substring(1));
				classpath=str.substring(1);
			}else{
				classpath=str;
			}
		}else{
			classpath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
		}
		HbaseSiteFile = classpath+"hbase_site";
		ZkConfigFile = classpath+"zk_conf";
		HBaseDataSource hbaseDataSource = new HBaseDataSource();
		List<Resource> hbaseConfigResources = new ArrayList<Resource>();
		hbaseConfigResources.add(new CachedFileSystemResource(
				SHCUtil.HbaseSiteFile));
		hbaseConfigResources.add(new CachedFileSystemResource(
				SHCUtil.ZkConfigFile));
		hbaseDataSource.setHbaseConfigResources(hbaseConfigResources);
		hbaseDataSource.init();
		//获得hbase客户端管理对象，建表用
		simpleHbaseAdminClient = SimpleHbaseClientFactory.getWrapper(
				SimpleHbaseAdminClient.class, new SimpleHbaseAdminClientImpl());
		simpleHbaseAdminClient.setHbaseDataSource(hbaseDataSource);
		
		for (String str: map.keySet()) {
			HBaseTableConfig hbaseTableConfig = new HBaseTableConfig();
			System.out.println("classpath:"+classpath);
			hbaseTableConfig.setConfigResource(new CachedFileSystemResource(
					classpath+str+".xml"));
			hbaseTableConfig.init();
			
			SimpleHbaseClient tClient = new SimpleHbaseClientImpl();
			tClient.setHbaseDataSource(hbaseDataSource);
			tClient.setHbaseTableConfig(hbaseTableConfig);
			//simpleHbaseClient = SimpleHbaseClientFactory.getSimpleHbaseClient(tClient);
			//listSHC.add(tClient);
			shcMap.put(str, tClient);
			//System.out.println("xmlFile="+xmlFile);
			
			try {
				if(AdminUtil.getAdmin().tableExists(TableNameUtil.getTableName(str))){
					System.out.println("表存在："+str);
				}else{
					System.out.println("表不存在，准备创建:~~~~~");
					// 如表不存在则创建
					//HTableDescriptor tableDescriptor = new HTableDescriptor(TableNameUtil.getTableName(str));
					//tableDescriptor.addFamily(new HColumnDescriptor(str+"Family").setMaxVersions(1));
					//simpleHbaseAdminClient.createTable(tableDescriptor);
					
					createTableBySplitKeys(str,(String[])map.get(str).get("family"),(byte[][])map.get(str).get("split"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/*static {
		HBaseDataSource hbaseDataSource = new HBaseDataSource();

		List<Resource> hbaseConfigResources = new ArrayList<Resource>();
		// If run on hbase cluster, modify the following config files.
		// If run on hbase stand alone mode, comment out the following config
		// files.
		//System.out.println("路径："+Thread.currentThread().getContextClassLoader().getResource("").getPath());
		//File file=new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()+SHCUtil.HbaseSiteFile);
		//System.out.println("文件存在吗？"+file.exists());
		hbaseConfigResources.add(new CachedFileSystemResource(
				SHCUtil.HbaseSiteFile));
		hbaseConfigResources.add(new CachedFileSystemResource(
				SHCUtil.ZkConfigFile));
		hbaseDataSource.setHbaseConfigResources(hbaseConfigResources);

		hbaseDataSource.init();

		HBaseTableConfig hbaseTableConfig = new HBaseTableConfig();
		hbaseTableConfig.setConfigResource(new CachedFileSystemResource(
				MyRecordXmlFile));
		hbaseTableConfig.init();

		// SimpleHbaseClient tClient = new SimpleHbaseClientImpl();
		shc = new SimpleHbaseClientImpl();
		shc.setHbaseDataSource(hbaseDataSource);
		shc.setHbaseTableConfig(hbaseTableConfig);

		simpleHbaseClient = SimpleHbaseClientFactory.getSimpleHbaseClient(shc);

		simpleHbaseAdminClient = SimpleHbaseClientFactory.getWrapper(
				SimpleHbaseAdminClient.class, new SimpleHbaseAdminClientImpl());
		simpleHbaseAdminClient.setHbaseDataSource(simpleHbaseClient
				.getHbaseDataSource());
		
	}*/
	/**
	 * 
	 * @Title: getSHC
	 * @Description: 获得对应Table预先创建的simpleHbaseClient对象
	 * @param fileName
	 * @return 
	 * @return SimpleHbaseClient
	 * @throws
	 */
	public static SimpleHbaseClient getSHC(String fileName) {
		SimpleHbaseClient shc=shcMap.get(fileName);
		if(shc!=null){
			return shc;
		}else{
			System.out.println("找不到对应的实体xml文件，请确保xml文件名正确");
			return null;
		}
	}
	public static byte[][] getSplitKeys(String[] keys){
		/*String[] keys = new String[] { "10|", "20|", "30|", "40|", "50|",    
	            "60|", "70|", "80|", "90|" };*/    
	    byte[][] splitKeys = new byte[keys.length][];    
	    TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);//升序排序    
	    for (int i = 0; i < keys.length; i++) {    
	        rows.add(Bytes.toBytes(keys[i]));    
	    }    
	    Iterator<byte[]> rowKeyIter = rows.iterator();    
	    int i=0;    
	    while (rowKeyIter.hasNext()) {    
	        byte[] tempRow = rowKeyIter.next();    
	        rowKeyIter.remove();    
	        splitKeys[i] = tempRow;    
	        i++;    
	    }    
	    return splitKeys;
	}
	/* 创建预分区hbase表   
    * @param tableName 表名   
    * @param columnFamily 列簇   
    * @return   
    */    
   public static boolean createTableBySplitKeys(String tableName, String[] columnFamily,byte[][] keys) {    
       try {    
           if (StringUtil.isEmptyString(tableName) || columnFamily == null    
                   || columnFamily.length < 0) {    
               log.error("===Parameters tableName|columnFamily should not be null,Please check!===");    
           }    
           Admin admin = AdminUtil.getAdmin();    
           if (admin.tableExists(TableNameUtil.getTableName(tableName))) {    
               return true;    
           } else {    
               HTableDescriptor tableDescriptor = new HTableDescriptor(    
            		   TableNameUtil.getTableName(tableName));    
               for (String cf : columnFamily) {    
                   tableDescriptor.addFamily(new HColumnDescriptor(cf));    
               }    
               if(keys.length!=0){//预分区
            	   //byte[][] splitKeys = getSplitKeys(keys);
            	   /*byte[][] splitKeys=new byte[][]{
            		   {25,0,0,0,0,0,0,0,0},
	       				{50,0,0,0,0,0,0,0,0}, 
	       				{75,0,0,0,0,0,0,0,0}, 
	       				{100,0,0,0,0,0,0,0,0}, 
	       				{125,0,0,0,0,0,0,0,0},    
	       				{(byte)150,0,0,0,0,0,0,0,0}, 
	       				{(byte)175,0,0,0,0,0,0,0,0}, 
	       				{(byte)200,0,0,0,0,0,0,0,0}, 
	       				{(byte)225,0,0,0,0,0,0,0,0} 
            		   
            		   };*/
            	   admin.createTable(tableDescriptor,keys);//指定splitkeys    
               }else{//没有预分区
            	   admin.createTable(tableDescriptor);
               }
               log.info("===Create Table " + tableName    
                       + " Success!columnFamily:" + columnFamily.toString()    
                       + "===");    
           }    
       } catch (MasterNotRunningException e) {    
           e.printStackTrace(); 
           return false;    
       } catch (ZooKeeperConnectionException e) {    
           e.printStackTrace();   
           return false;    
       } catch (IOException e) {    
           e.printStackTrace();    
           return false;    
       }    
       return true;    
   }      
}
