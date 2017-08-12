<%@ page language="java" pageEncoding="utf-8"%>
<%  
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);   
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>${_systemName}</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<link type="images/x-icon" rel="shortcut icon" href="${_staticPath}/custom/assets/favicon.ico">
		
		<!--[if !IE]> -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/pace.css" />
		<script data-pace-options='{ "ajax": true, "document": true, "eventLag": false, "elements": false }' src="${_staticPath}/assets/js/pace.js"></script>
		<!-- <![endif]-->

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${_staticPath}/assets/css/font-awesome.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
		
		<!-- 自定义样式，表单多选框 -->
		<link rel="stylesheet" href="${_staticPath}/custom/assets/ace.custom.css" />

		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${_staticPath}/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${_staticPath}/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- ace settings handler -->
		<script src="${_staticPath}/assets/js/ace-extra.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="${_staticPath}/assets/js/html5shiv.js"></script>
		<script src="${_staticPath}/assets/js/respond.js"></script>
		<![endif]-->
		
		
		
		
		
		
		<!-- 文本框 -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/jquery-ui.custom.css" />
		<!-- 多选框 -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/bootstrap-multiselect.css" />
		<!-- 颜色选择  -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/colorpicker.css" />
		<!-- 时间  -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/bootstrap-timepicker.css" />
		<!-- 日期、日期+时间、日期范围  -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/daterangepicker.css" />
		<!-- 提示框  -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/jquery.gritter.css" />
		<!-- 拖拽式单文件上传 -->
		<link rel="stylesheet" href="${_staticPath}/assets/css/dropzone.css" />
		<link rel="stylesheet" href="${_staticPath}/assets/css/uploadifive.css" />
		
		<!--[if lte IE 8]>
		  <script src="${_staticPath}/assets/js/excanvas.js"></script>
		<![endif]-->
		<script type="text/javascript">
			var scripts = [
				// Form提交Json转换
				"${_staticPath}/custom/jquery.form.min.js?v=" + Math.random(),
				// 列表
				"${_staticPath}/custom/jquery.table.min.js?v=" + Math.random(),
				// 确认框
				"${_staticPath}/assets/js/bootbox.js?v=" + Math.random(),
				"${_staticPath}/custom/assets/bootbox.custom.js?v=" + Math.random(),
				// 自动隐藏的提醒框
				"${_staticPath}/assets/js/jquery.gritter.js?v=" + Math.random(),
				"${_staticPath}/custom/assets/jquery.gritter.custom.js?v=" + Math.random(),
				// UI
				"${_staticPath}/assets/js/jquery-ui.custom.js?v=" + Math.random(),
				// 验证
				"${_staticPath}/custom/jquery.validate-2.0.min.js?v=" + Math.random(),
				"${_staticPath}/custom/jquery.validate-2.0.custom.min.js?v=" + Math.random()
			];
		</script>
	</head>

	<body class="no-skin">
	
	
		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${_staticPath}/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${_staticPath}/assets/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${_staticPath}/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${_staticPath}/assets/js/bootstrap.js"></script>

		<!-- ace scripts -->
		<script src="${_staticPath}/assets/js/ace/elements.scroller.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.fileinput.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.typeahead.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.spinner.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.treeview.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.wizard.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.aside.js"></script>
		<!-- 修改默认首页 -->
		<script id="_ace" src="${_staticPath}/custom/assets/ace.js?v" data-path="${defaultPage}"></script>
		<!-- 切换菜单处理 -->
		<script id="_ajaxContent" src="${_staticPath}/custom/assets/ace.ajax-content.js?v" data-path="${_path}"></script>
		<!-- 权限处理 -->
		<script id="_permission" src="${_staticPath}/custom/jquery.permission.min.js?v" data="${sessionUserNoPermissions}"></script>
		<script src="${_staticPath}/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.sidebar.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.widget-box.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		<!-- 省市区联动 
		<script src="${_staticPath}/resource/addr/jquery.cityselect.js"></script> -->
		
		
		
		<div class="page-content-area" data-ajax-content="true">
							<!-- ajax content goes here -->
						
			<div class="page-header">
				<h1>
					监测列表
				</h1>
			</div>
			
			<div class="row">
				<div class="col-xs-12">
					<div class="row">
						<div class="col-xs-12">
							<div class="widget-box">
								<div class="widget-header widget-header-small">
									<h5 class="widget-title lighter">搜索栏</h5>
								</div>
			
								<div class="widget-body">
									<div class="widget-main">
										<form id="_form" class="form-inline">
											<label>
												<label class="control-label" for="form-field-1"> 设备编号： </label>
												<input name="equipCode" type="text" class="form-data input-medium search-data">
											</label>
											<!-- 
											<button id="_search" type="button" class="btn btn-info btn-sm">
												<i class="ace-icon fa fa-search bigger-110"></i>搜索
											</button>
											 -->
										</form>
									</div>
								</div>
							</div>
			
							<div>
								<div class="dataTables_wrapper form-inline no-footer">
									<table id="_table" class="table table-striped table-bordered table-hover dataTable no-footer">
									</table>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div><!-- /.page-content-area -->
		<div class="table-dialog"></div>
		
		<script type="text/javascript">
			scripts.push(
					"${_staticPath}/custom/zTree/js/jquery.ztree.core-3.5.min.js?v=" + Math.random(),
					"${_staticPath}/custom/zTree/js/jquery.ztree.excheck-3.5.min.js?v=" + Math.random());
					
			$('.page-content-area').ace_ajax('loadScripts', scripts, function() {
				jQuery(function($) {
					// 列表
		    		var $table = $("#_table").table({
		    			url : "${_path}/admin/monitor/list",
		    			formId : "_form",
		    			ajaxData : {//加载表格的请求设置
		    				type : "post",
		    				dataFormat : "json",
		    				contentType : "application/json; charset=utf-8"
		    			},
						tools : [
							{text : '删除', clazz : 'btn-danger', icon : 'fa fa-trash-o red', permission : '/admin/user/delete', handler : function(){
								$table.ajaxDelete({
									confirm : "删除管理员会影响关联的应用、角色、权限，确认要删除?",
									url : "${_path}/admin/user/delete"
								});
							}}
						],
						columns : [
					        {field:'id', hide : true},
					        {field:'rawTds', title:'原水TDS', mobileHide : true,validate:true},
					        {field:'purTds', title:'净水TDS', mobileHide : true,validate:true},
					        {field:'temp', title:'温度', mobileHide : true},
					        {field:'humidity', title:'湿度', mobileHide : true},
					        {field:'flow', title:'流量', mobileHide : true},
					        {field:'leak', title:'漏水开关(true-漏水、false-无漏水)', mobileHide : true},
					        {field:'magnetic', title:'电磁阀输出状态(true:电磁阀供电 false:电池阀不供电)', mobileHide : true},
					        {field:'outRelay', title:'输出继电器(true:闭合 false:断开)', mobileHide : true},
					        {field:'powerRelay', title:'电源继电器 (true:有输出 false:无输出)', mobileHide : true},
					        {field:'oneResult', title:'一级滤芯结果', mobileHide : true},
					        {field:'twoResult', title:'二级滤芯结果', mobileHide : true},
					        {field:'threeResult', title:'三级滤芯结果', mobileHide : true},
					        {field:'fourResult', title:'四级滤芯结果', mobileHide : true},
					        {field:'fiveResult', title:'五级滤芯结果', mobileHide : true},
					        {field:'microResult', title:'微生物芯结果', mobileHide : true},
					        
					        {field:'equipCode', title:'设备编号', align:'left'},
					        {field:'collectTime', title:'采集时间', mobileHide : true},
					        {field:'createTime', title:'创建时间', mobileHide : true}
						],
						operate : [
							{text : '修改', clazz : 'blue', icon : 'fa fa-pencil', permission : '/admin/user/edit', handler : function(d, i){
								//$.aceRedirect("${_path}/admin/user/edit?id=" + d.id);
								//savePortrait(d.id);
								//alert("handler~~");
								
								//$table.add_edit(d,i,"用户编辑");
								//$('#_editForm').validate();
								$table.dialog(d,i,"用户编辑").validate();
							}},
							{text : '删除', clazz : 'red', icon : 'fa fa-trash-o', permission : '/admin/user/delete', handler : function(d, i){
								$table.ajaxDelete({
									confirm : "删除管理员会影响关联的应用、角色、权限，确认要删除?",
									url : "${_path}/admin/user/delete"
								});
							}}
						],
						after : function(){
							// 权限处理
							$.permission();
							
						}
					});
					
					
					
					//弹出层初始化
			        $('.modal.aside').ace_aside();
				
					$(document).one('ajaxloadstart.page', function(e) {
						//in ajax mode, remove before leaving page
						$('.modal.aside').remove();
						$(window).off('.aside');
					});
					
					//搜索
					$(".search-data").blur(function () { 
						$table.search();
					});
		            
					// 取消
					$("#_cancel").click(function(){
						$table.search();
					});
				});
			});
		</script>
		

		

		<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${_staticPath}/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
		<script type="text/javascript">
		 window.jQuery || document.write("<script src='${_staticPath}/assets/js/jquery1x.js'>"+"<"+"/script>");
		</script>
		<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${_staticPath}/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${_staticPath}/assets/js/bootstrap.js"></script>

		<!-- ace scripts -->
		<script src="${_staticPath}/assets/js/ace/elements.scroller.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.fileinput.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.typeahead.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.spinner.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.treeview.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.wizard.js"></script>
		<script src="${_staticPath}/assets/js/ace/elements.aside.js"></script>
		<!-- 修改默认首页 -->
		<script id="_ace" src="${_staticPath}/custom/assets/ace.js?v" data-path="${defaultPage}"></script>
		<!-- 切换菜单处理 -->
		<script id="_ajaxContent" src="${_staticPath}/custom/assets/ace.ajax-content.js?v" data-path="${_path}"></script>
		<!-- 权限处理 -->
		<script id="_permission" src="${_staticPath}/custom/jquery.permission.min.js?v" data="${sessionUserNoPermissions}"></script>
		<script src="${_staticPath}/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.sidebar.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.widget-box.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${_staticPath}/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		<!-- 省市区联动 
		<script src="${_staticPath}/resource/addr/jquery.cityselect.js"></script> -->
		<script type="text/javascript">
			jQuery(function ($) {
				
			});
		</script>
	</body>
</html>
