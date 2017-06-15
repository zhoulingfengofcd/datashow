package com.qingting.customer.web;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.dao.impl.MonitorDAOImpl;
import com.qingting.kafka.ConsumerBase;

@Controller
@RequestMapping("/monitor")
public class MonitorController {
	MonitorDAO monitorDAO=new MonitorDAOImpl();
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	@RequestMapping(method = RequestMethod.GET)
	public String execute(){
		return "index";
	}
	@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	public List<Monitor> list(){
		return monitorDAO.listMonitor();
	}
	/*@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	public List<Monitor> list(){
		List<Monitor> list=null; 
		
		Set<String> key = ConsumerBase.monitorMap.keySet();
		for (String str : key) {//遍历ID+date
			String[] split = str.split(":");
			if(split.length==2){
				if(list==null){
					list=new ArrayList<Monitor>();
				}
				Monitor monitor=new Monitor();
				//date
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
				String dataStr = format.format(Long.valueOf(split[1]));
				monitor.setDate(dataStr);
				//data
				monitor.setData(ConsumerBase.monitorMap.get(str));
				//ID+(date+data)
				int id=0;
				System.out.print("clientID:");
				byte[] bytes = split[0].getBytes();
				for (int i=0; i<bytes.length;i++) {
					if(bytes[i]!=0){
						id=bytesToInt(bytes,i,bytes.length-i);
						System.out.println(id);
						//monitor.setEquipId(id);
						break;
					}
				}
				monitor.setEquipId(id);
				list.add(monitor);
			}else{
				throw new RuntimeException("The data format error.reference 12:456.key="+key);
			}	
		}
		return list;
	}*/
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
