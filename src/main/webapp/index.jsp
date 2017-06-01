<%@page contentType="text/html;charset=utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<script type="text/javascript" src="/datashow/js/jquery.min.js"></script>
		<script type="text/javascript">
			$(function(){
				function updateTable(){
					/*Ajax({
						//"type":"post", //默认get
						"type":"get",
						"url":"${pageContext.request.contextPath }/monitor/list",
						//"data":{"adminId":0},
						"dataType":"json",
						"autoCloseProgress":false, //success回调函数执行结束再关闭进度条
						"progressText":"正在发送请求...", //进度条显示文本
						"timeout":30000, //超时设置30s
						"successText":"操作成功！",
						"success":function(result){
							//obj=eval("("+result+")");
							$.each(
								result,
								function(index,element){
									console.log("处理结果："+index+element);
								}
							);
							//console.log("处理结果："+result.monitor);
							//$("#admin_table").datagrid("load");
						}
					});*/
					$.ajax({
						"type":"get",
						"url":"${pageContext.request.contextPath }/monitor/list",
						//"data":{"adminId":0},
						"dataType":"json",
						"success":function(result){
							//obj=eval("("+result+")");
							/*$.each(
								result,
								function(key,value){
									console.log("处理结果："+key+value);
								}
							);*/
							update(result);
							//console.log("处理结果："+result.monitor);
							//$("#admin_table").datagrid("load");
						}
					});
				}
				setInterval(updateTable,1000);
				function update(data){
					var tb = document.getElementById("table1");
				    //删除原先数据，2 为保留表头，值为表头行数减1
				    for (var n = tb.rows.length - 1; n > 1; n--) {
				        tb.deleteRow(n);
				    }
				    
				    /*$.each(
						data,
						function(key,value){
							console.log("处理结果："+key+value);
							alert("处理结果1："+key+value);
							var row = tb.insertRow(tb.rows.length);
					        var key = row.insertCell(0);
					        key.innerHTML = key;
					        var value = row.insertCell(1);
					        value.innerHTML = value.;
						}
					);*/
					var temp;
					var result;
					for(var i in data){//用javascript的for/in循环遍历对象的属性 
						temp += i+":"+data[i]+"\n"; 
						var row = tb.insertRow(1);
						
						
				        var key = row.insertCell(0);
				        key.innerHTML = data[i].equipId;
				        
				        var value_data = row.insertCell(1);
				        value_data.innerHTML = data[i].data;
				        console.log("value_data:"+data[i].data);
				        
				        var value_date = row.insertCell(2);
				        value_date.innerHTML = data[i].date;
				        console.log("value_date:"+data[i].date);
					} 
				    console.log("处理结果："+temp);
				   /*for (var i = 0; i < data.length; i++) {
				        var row = tb.insertRow(tb.rows.length);
				        var key = row.insertCell(0);
				        key.innerHTML = data[i].a;
				        var value = row.insertCell(1);
				        value.innerHTML = data[i].d;
				    }*/
				}
			});
		</script>
	</head>
	<body>
		<table id="table1" border="1" cellspacing="0">
			<tr>
				<th width="20%">设备ID</th>
				<th width="20%">监测数据</th>
				<th width="20%">时间</th>
			</tr>
		</table>
		
	</body>
</html>