package com.qingting.kafka;

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

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.dao.impl.MonitorDAOImpl;

public class ConsumerBase extends HttpServlet{
	
	//@Resource
	//MonitorDAO monitorDAO;
	MonitorDAO monitorDAO=new MonitorDAOImpl();
	
	private static final long serialVersionUID = 6482582654527221173L;
	static final int maxSize = 20;
	public static Map<String,String> monitorMap=null;
	static private Properties props =null;
	static private KafkaConsumer<String, String> consumer =null;
	
	static{
		props = new Properties();
		
		props.put("bootstrap.servers", "119.29.225.162:9092");
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

        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<String, String>(props);
        //订阅主题列表topic
        consumer.subscribe(Arrays.asList("monitor"));
	}
	public void init(){
		monitorMap = new LinkedHashMap<String, String>(){
	            private static final long    serialVersionUID    = 1L;
	            @Override
	            protected boolean removeEldestEntry(Map.Entry<String, String> pEldest) {
	                return size() > maxSize;
	            }
        };
        new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
		            ConsumerRecords<String, String> records = consumer.poll(100);
		            for (ConsumerRecord<String, String> record : records){
		                //　正常这里应该使用线程池处理，不应该在这里处理
		            	monitorMap.put(record.key(), record.value());
		            	monitorDAO.insertMonitor(converter(record.key(),record.value()));
		                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
		            }	
		        }
			}
        }).start();
	}
	public Monitor converter(String key,String value){
		String[] split = key.split(":");
		Monitor monitor;
		if(split.length==2){
			monitor=new Monitor();
			//date
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			String dataStr = format.format(Long.valueOf(split[1]));
			Date date=null;
			try {
				date = format.parse(dataStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			monitor.setCalendar(calendar);
			monitor.setDate(dataStr);
			//data
			monitor.setData(value);
			
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
			monitor.setEquipId(id);
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
