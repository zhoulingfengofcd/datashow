package com.qingting.kafka;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.dao.impl.MonitorDAOImpl;


public class ConsumerBase extends HttpServlet{
	
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	 public static final String US_ASCII = "US-ASCII";

	 /** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	 public static final String ISO_8859_1 = "ISO-8859-1";

	 /** 8 位 UCS 转换格式 */
	 public static final String UTF_8 = "UTF-8";

	 /** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	 public static final String UTF_16BE = "UTF-16BE";

	 /** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	 public static final String UTF_16LE = "UTF-16LE";

	 /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	 public static final String UTF_16 = "UTF-16";

	 /** 中文超大字符集 */
	 public static final String GBK = "GBK";
	
	
	//@Resource
	//MonitorDAO monitorDAO;
	MonitorDAO monitorDAO=new MonitorDAOImpl();
	
	private static final long serialVersionUID = 6482582654527221173L;
	static final int maxSize = 20;
	public static Map<String,byte[]> monitorMap=null;
	static private Properties props =null;
	static private KafkaConsumer<String, byte[]> consumer =null;
	
	static{
		props = new Properties();
		
		props.put("bootstrap.servers", "39.108.52.201:9092");
        System.out.println("this is the group part test 1");
        //消费者的组id
        props.put("group.id", "GroupA");//这里是GroupA或者GroupB

        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");

        //从poll(拉)的回话处理时长
        props.put("session.timeout.ms", "30000");
        //poll的数量限制
        //props.put("max.poll.records", "100");

        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");

        consumer = new KafkaConsumer<String, byte[]>(props);
        //订阅主题列表topic
        consumer.subscribe(Arrays.asList("monitor"));
	}
	public void init(){
		monitorMap = new LinkedHashMap<String, byte[]>(){
	            private static final long    serialVersionUID    = 1L;
	            @Override
	            protected boolean removeEldestEntry(Map.Entry<String, byte[]> pEldest) {
	                return size() > maxSize;
	            }
        };
        new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
		            ConsumerRecords<String, byte[]> records = consumer.poll(100);
		            for (ConsumerRecord<String, byte[]> record : records){
		                //　正常这里应该使用线程池处理，不应该在这里处理
		            	monitorMap.put(record.key(), record.value());
		            	System.out.println("Monitor:"+converter(record.key(),record.value()));
		            	monitorDAO.insertMonitor(converter(record.key(),record.value()));
		                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
		            }	
		        }
			}
        }).start();
	}
	/**
	 * 
	 * @Title: getDate
	 * @Description: 获得一个指定年月日时分秒+固定000毫秒的时间对象
	 * @param year
	 * @param month
	 * @param date
	 * @param hourOfDay
	 * @param minute
	 * @param second
	 * @return 
	 * @return Calendar
	 * @throws
	 */
	public Calendar getDate(int year, int month, int date,
			int hourOfDay, int minute, int second) {
		Date d = null;
		try {
			d = new SimpleDateFormat("SSS").parse("000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(year, month - 1, date, hourOfDay, minute, second);
		return cal;
	}
	public static long bytesToLong(byte[] byteNum,int index,int length) {  
	    long num = 0;  
	    for (int ix = 0; ix < length; ix++) {  
	        num <<= 8;  
	        num |= ( byteNum[index+ix] & 0xff); 
	    }  
	    return num;  
	}  
	public Monitor converterPayload(byte[] value){
		System.out.print("收到的值:");
		for (byte b : value) {
			System.out.print(b+" ");
		}
		System.out.println("");
		Monitor monitor=new Monitor();
		//时间
		monitor.setCreateTime(getDate(value[0]+2000, value[1], value[2], value[3], value[4], value[5]));
		//继电器电磁阀状态
		monitor.setLeak((value[6]&0x08)==0x08);
		monitor.setMagnetic((value[6]&0x04)==0x04);
		monitor.setOutRelay((value[6]&0x02)==0x02);
		monitor.setPowerRelay((value[6]&0x01)==0x01);
		//流量
		monitor.setFlow(bytesToLong(value,7,6));
		//原水TDS
		int rawTds=0;
		rawTds=(value[13]&0xff);
		rawTds<<=8;
		rawTds|=(value[14]&0xff);
		monitor.setRawTds(rawTds/10f);
		//净水TDS
		int purTds=0;
		purTds=(value[15]&0xff);
		purTds<<=8;
		purTds|=(value[16]&0xff);
		monitor.setPurTds(purTds/10f);
		//温度
		monitor.setTemp((byte)(value[17]-50));
		//湿度
		monitor.setHumidity(value[18]);
		return monitor;
	}
	/**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	public String changeCharset(String str, String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			//用默认字符编码解码字符串。
			byte[] bs = str.getBytes();
			//用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}
	public Monitor converter(String key,byte[] value){
		int splitIndex=key.indexOf(':');  
		String clientId=key.substring(0, splitIndex);
		String packgeId=key.substring(splitIndex+1);
		
		Monitor monitor;
		if(clientId!=null){
			monitor=converterPayload(value);
			String utf8ClientId=null;
			/*try {
				utf8ClientId=this.changeCharset(clientId, US_ASCII);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}*/
			utf8ClientId=clientId;
			monitor.setEquipCode(utf8ClientId);
			
			
			
			
			/*monitor.setData(value);
			
			//ID+(date+data)
			int id=0;
			byte[] bytes = split[0].getBytes();
			for (int i=0; i<bytes.length;i++) {
				if(bytes[i]!=0){
					System.out.print("ID_length:");
					System.out.print(bytes.length);
					
					System.out.print(" bytes:");
					for(int j=0;j<bytes.length;j++){
						System.out.print(bytes[j]+" ");
					}
					id=bytesToInt(bytes,i,bytes.length-i);
					System.out.print(" clientID:");
					System.out.println(id);
					//monitor.setEquipId(id);
					break;
				}
			}
			monitor.setEquipId(id);*/
			return monitor;
		}else{
			throw new RuntimeException("The data format error.reference 12:456.key="+key);
		}
	}
	/**  
	    * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用 
	    *   
	    * @param src  
	    *            byte数组  
	    * @param offset  
	    *            从数组的第offset位开始  
	    * @param length
	    * 			   长度
	    * @return int数值  
	    */    
	public static int bytesToInt(byte[] src, int offset,int length) {  
	    int value=0;  
	    int count=0;
	    if(length>4) throw new RuntimeException("输入的length大于4无法装换成int类型");
	    for(int i=length+offset-1;i>(offset-1);i--){
	    	value=(int)(
	    			value|(
	    			(src[i]&0xFF)<<(8*count)
	    			)
	    			);
	    	count++;
	    }
	    /*value = (int) ((src[offset] & 0xFF)   
	            | ((src[offset+1] & 0xFF)<<8)   
	            | ((src[offset+2] & 0xFF)<<16)   
	            | ((src[offset+3] & 0xFF)<<24)); */ 
	    return value;  
	}
}
