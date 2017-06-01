package com.qingting.kafka;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerBase extends HttpServlet{
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
		                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
		            }	
		        }
			}
        }).start();
	}
}
