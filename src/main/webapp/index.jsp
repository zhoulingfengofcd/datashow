<%@page contentType="text/html;charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<script type="text/javascript" src="/datashow/js/jquery.min.js"></script>
		<script type="text/javascript">
			$(function(){
				function updateTable(){
					$.ajax({
						"type":"get",
						"url":"${pageContext.request.contextPath }/monitor/list",
						//"data":{"adminId":0},
						"dataType":"json",
						"success":function(result){
							update(result);
						}
					});
				}
				setInterval(updateTable,1000);
				function update(data){
					var tb = document.getElementById("table1");
				    //删除原先数据，2 为保留表头，值为表头行数减1
				    for (var n = tb.rows.length-1 ; n > 0; n--) {
				        tb.deleteRow(n);
				    }
					$.each(data,function(index,element){//用javascript的for/in循环遍历对象的属性 
						var row = tb.insertRow(1);
						
				        var equipCode = row.insertCell(0);
				        equipCode.innerHTML = element.equipCode;
				        
				        var rawTds = row.insertCell(1);
				        rawTds.innerHTML = element.rawTds;
				        
				        var purTds = row.insertCell(2);
				        purTds.innerHTML = element.purTds;
				        
				        var temp = row.insertCell(3);
				        temp.innerHTML = element.temp;
				        
				        var humidity = row.insertCell(4);
				        humidity.innerHTML = element.humidity;
				        
				        var flow = row.insertCell(5);
				        flow.innerHTML = element.flow;
				        
				        var leak = row.insertCell(6);
				        leak.innerHTML = element.leak;
				        
				        var magnetic = row.insertCell(7);
				        magnetic.innerHTML = element.magnetic;
				        
				        var outRelay = row.insertCell(8);
				        outRelay.innerHTML = element.outRelay;
				        
				        var powerRelay = row.insertCell(9);
				        powerRelay.innerHTML = element.powerRelay;
				        
				        var createTime = row.insertCell(10);
				        createTime.innerHTML = element.time;
					}); 
				    console.log("数据条数:"+data.length);
				}
			});
		</script>
	</head>
	<body>
		<table id="table1" border="1" cellspacing="0">
			<tr>
				<th>设备编号(ASCII编码,如二进制48为0)</th>
				
				
	 			<th>原水TDS值</th>
	 


	 			<th>净水TDS值</th>
	

	
	  			<th>温度值</th>
	 

	
	  			<th>湿度值</th>
	 

	
	  			<th>流量值</th>
	 

	
	  			<th>漏水状态开关(true-漏水、false-无漏水)</th>
	 
	
	
	  			<th>电磁阀输出状态(true:电磁阀供电 false:电池阀不供电)</th>
	 
	
	
	 			<th> 输出继电器(true:闭合 false:断开)</th>
	 
	
	
	 			<th> 电源继电器 (true:有输出 false:无输出)</th>
	 
	
	
	  			<th>采集时间</th>
	 
				
			</tr>
		</table>
		
	</body>
</html>