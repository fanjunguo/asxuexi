$(document).ready(function(){
	$("#message_grid").datagrid({
		url: "message/listOrgMessages.action",
		method: "GET",
		queryParams: {
			status: "2"
		},
	    columns:[[
	    	{field:"id",width:30,align:"center",resizable:true,
				formatter: function(value,row,index){
					if (typeof row.messages != "undefined"){
						return `<input type="checkbox" name=${index} value=${row.messages.messageId}>`;
					}
				},
				styler : function(value, row, index) {
					return "border:0";
				}
			},
	        {field:"title",title:"消息标题",width:350,
	        	formatter: function(value,row,index){
	        		if (typeof row.messages != "undefined"){
	        			if (row.messages.status == 0) {
		        			return `
			        		<div class="message_title unread">${row.messages.title}</div>
			        		<div class="message_content">${row.messages.content}</div>`;
						}else{
							return `
			        		<div class="message_title">${row.messages.title}</div>
			        		<div class="message_content">${row.messages.content}</div>`;
						}
	        		}else{
	        			return row.title;
	        		}
	        	},
				styler : function(value, row, index) {
					return "border:0";
				}
	        },    
	        {field:'time',title:"时间",width:200,
	        	formatter: function(value,row,index){
	        		if (typeof row.messages != "undefined"){
	        			let time=new Date(row.messages.gmtCreate).toLocaleString();
		        		return `
		        		<div class="message_time">${time}</div>`;
	        		}
	        	},
				styler : function(value, row, index) {
					return "border:0";
				}
	        },    
	        {field:'type',title:"类型",width:200,
	        	formatter: function(value,row,index){
	        		if (typeof row.messages != "undefined"){
	        			return `
		        		<div class="message_type">${row.messages.type}</div>`;
	        		}
	        	},
				styler : function(value, row, index) {
					return "border:0";
				}
	        }
	    ]],
	    singleSelect: true,
	    fitColumns: true,
	    striped: true,
	    pagination: true,
	    pageSize: 10,
	    pageList: [10,15],
	    loadFilter: function(map){
	    	if (map.code == 600) {
	    		map.data.code = map.code;
				return map.data;
			}else{
				return {
					code: 400,
					rows: [],
					total: 0
				};
			}
	    },
	    onLoadSuccess: function(data){
	    	// 每次刷新表格，重置[标记已读]和[删除消息]按钮
	    	$(".read, .delete").addClass("disabled");
	    	if (data.code == 400) {
	    		// 请求失败的提示
	    		$(this).datagrid("appendRow", {title: '<div style="text-align:center;color:red">请求数据失败，请联系网站工作人员！</div>' })
				.datagrid("mergeCells", { index: 0, field: "title", colspan: 3 });
			}
	    	if (data.code == 600){
				// 请求成功时，若数据为0，展示提示
	    		if (data.total == 0) {
	    			$(this).datagrid("appendRow", {title: '<div style="text-align:center;color:red">没有相关记录！</div>' })
	    				.datagrid("mergeCells", { index: 0, field: "title", colspan: 3 });
                }
			}
	    }
	});
	// 修改表格分页组件的样式
	$("#message_grid").datagrid("getPager").pagination({
		layout:["list","sep","prev","manual","next","sep"],
		displayMsg: "共{total}条",
		beforePageText: "第",
		afterPageText: "页&emsp;共{pages}页",
	});
	// 点击[全部消息]，[未读消息]选项卡
	$("body").on("click",".tab_container .tab",function(){
		let $tab = $(this);
		if (!$tab.hasClass("selected")) {
			$(".tab.selected").removeClass("selected");
			$tab.addClass("selected");
		}
		let index = $tab.index();
		// 点击了[全部消息]
		if (index == 0) {
			$("#message_grid").datagrid("load",{
				status: "2"
			});
		}
		// 点击了[未读消息]
		if (index == 1) {
			$("#message_grid").datagrid("load",{
				status: "0"
			});
		}
	})
	// 点击表格第一列的选择框
	$("body").on("click","input:checkbox",function(){
		// 根据选中的数量，开启或禁用[标记已读]和[删除消息]按钮
		if ($("input:checkbox:checked").length==0) {
			$(".read, .delete").addClass("disabled");
		}else{
			$(".read, .delete").removeClass("disabled");
		}
	})
	// 点击表格[消息标题单元格]的选择框
	$("body").on("click",".message_title",function(){
		let $messageTitle = $(this);
		let selectedRow=$("#message_grid").datagrid("getSelected");
		if ($messageTitle.hasClass("unread")) {
			console.log(selectedRow.messages.messageId)
			// 若是未读消息，则发送请求标记消息为已读，成功时将未读标识去除
			$.post("message/readOrgMessages.action", {
				list : `["${selectedRow.messages.messageId}"]` 
			},function(map){
			    if (map.code == 600) {
				    // 去除未读标识
				    $messageTitle.removeClass("unread");
				    // 改变未读消息数量
				    setUnreadMessageCount();
			    }
			});
		}
		$messageTitle.next(".message_content").slideToggle();
	})
	// 点击[标记已读]按钮
	$("body").on("click",".read:not(.disabled)",function(){
		let idArray = [];
		$("input:checkbox:checked").each(function(){
			idArray.push($(this).val());
		});
		$.post("message/readOrgMessages.action", {
			list : JSON.stringify(idArray)
		},function(map){
		    if (map.code == 600) {
		    	// 刷新表格当页
				$("#message_grid").datagrid("reload");
				// 改变未读消息数量
				setUnreadMessageCount();
		    }else{
		    	$("#notice_modal").modal("show");
		    }
		});
	})
	// 点击[删除消息]按钮
	$("body").on("click",".delete:not(.disabled)",function(){
		let idArray = [];
		$("input:checkbox:checked").each(function(){
			idArray.push($(this).val());
		});
		$("#warn_modal").modal("show");
		// 点击提示面板中的[确定]按钮
		$(".warn_buttons .btn-success").off("click").on("click",function(){
			$("#warn_modal").modal("hide");
			$.post("message/deleteOrgMessages.action", {
				list : JSON.stringify(idArray)
			},function(map){
			    if (map.code == 600) {
			    	// 刷新表格当页
					$("#message_grid").datagrid("reload");
					// 改变未读消息数量
					setUnreadMessageCount();
			    }else{
			    	$("#notice_modal").modal("show");
			    }
			});
		})
	})
	
	function setUnreadMessageCount(){
		$.get("message/countOrgMessages.action", { status:"0" }, function(map){
			if (map.code == 600) {
				if (map.data > 0 && map.data <= 99) {
					$(".unread_message_count").text(map.data).show();
				}
				if (map.data > 99) {
					$(".unread_message_count").text("···").show();
				}
				if (map.data == 0) {
					$(".unread_message_count").hide();
				}
			}
		});
	}
})