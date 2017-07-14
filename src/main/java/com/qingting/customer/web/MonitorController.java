package com.qingting.customer.web;

import java.nio.charset.Charset;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qingting.customer.common.pojo.hbasedo.Monitor;
import com.qingting.customer.common.pojo.model.Pagination;
import com.qingting.customer.dao.MonitorDAO;
import com.qingting.customer.dao.impl.MonitorDAOImpl;
import com.smart.mvc.model.ResultCode;
import com.smart.mvc.model.WebResult;
import com.smart.mvc.validator.Validator;
import com.smart.mvc.validator.annotation.ValidateParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/admin/monitor")
public class MonitorController {
	MonitorDAO monitorDAO=new MonitorDAOImpl();
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	@RequestMapping(method = RequestMethod.GET)
	public String execute(){
		return "index";
	}
	/*@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	public Pagination<Monitor> list(
			String equipCode,Integer pageNo,Integer pageSize
			){
		Pagination<Monitor> page = monitorDAO.listMonitor(equipCode,pageNo,pageSize); 
		for (Monitor monitor : page.getList()) {
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			System.out.println("Controller-monitor:"+monitor);
			System.out.println("equipCode:"+monitor.getEquipCode()+" time:"+format.format(monitor.getCreateTime().getTime()));
			monitor.setTime(format.format(monitor.getCreateTime().getTime()));
		}
		return  page;
	}*/
	@ApiOperation("查询所有监测值")
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public @ResponseBody WebResult<Pagination<Monitor>> listMonitor(
			@ApiParam(value = "设备编号", required = false) @RequestParam(value="equipCode", required=false) String equipCode,
			@ApiParam(value = "开始页码", required = true) @RequestParam @ValidateParam({ Validator.NOT_BLANK }) Integer pageNo,
			@ApiParam(value = "显示条数", required = true) @RequestParam @ValidateParam({ Validator.NOT_BLANK }) Integer pageSize
			){
		//别忘记验证传参和用户身份是否匹配
		Pagination<Monitor> page = monitorDAO.listMonitor(equipCode,pageNo,pageSize);
		WebResult<Pagination<Monitor>> result=new WebResult<Pagination<Monitor>>(ResultCode.SUCCESS);
		result.setData(page);
		return result;
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
