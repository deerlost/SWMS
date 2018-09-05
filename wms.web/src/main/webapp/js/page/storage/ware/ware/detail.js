var indexdata=null,btn_index="-1",jsondata={},g1=null,remarkinfo="",locdata=null,areadata=null,area_list=[],position_list=[],tpdata=null,isedit=true;
var jsonresult=[
	{
		fixed:false,
		columns:[
			{ th: '物料编码', col: 'mat_code',fixed:true },
			{ th: '物料描述', col: 'mat_name', min: 60,fixed:true },
			{ th: '单位', col: 'unit_name',fixed:true },
			{ th: '库存数量', col: 'stock_qty',type:"number"},
			{ th: '调整数量', col: 'qty',type:"number"},
			{ th: '生产批次', col: 'product_batch'}, 
			{ th: 'ERP批次', col: 'erp_batch'},
			{ th: '批次号', col: 'batch'},
		]
	},
	{
		fixed:false,
		columns:[
			{ th: '物料编码', col: 'mat_code'},
			{ th: '物料描述', col: 'mat_name', min: 60},
			{ th: '单位', col: 'unit_name'},
			{ th: '生产批次', col: 'product_batch'},
			{ th: 'ERP批次', col: 'erp_batch'}, 
			{ th: '批次号', col: 'batch'},
			{ th: '库存数量', col: 'stock_qty',type:"number",align:'right'},
			{ th: '调整数量', col: 'qty',type:"number"},
			{ th: '包装类型', col: 'package_type_code'}, 
			{ th: '包装规格', col: 'package_standard_ch'},
			{ th: '件数', col: 'pack_num',type:"number",align:'right' },
		]
	},
	{
		fixed:true,
		columns:[
			{ th: '包装物序号', col: 'package_code', min: 60 },
			{ th: '物料编码', col: 'mat_code', min: 60 },
			{ th: '物料描述', col: 'mat_name' },
			{ th: '包装类型', col: 'package_type_code'},
			{ th: '包装规格', col: 'package_standard_ch'},
			{ th: '件数', col: 'pack_num',type:'number',align:'right'},
			{ th: '源仓储区', col: 'area_name'}, 
			{ th: '源仓位', col: 'position_code'}, 
			{ th: '库存数量', col: 'stock_qty',type:'number',align:'right',class:"out_qty"},
			{ th: '调整数量', col: 'qty',type:"number"},
			{ th: '目标存储区', col: 'target_area_id', sort:false,type:"select",data:"area_list",text:"target_area_name",value:"target_area_id",children:"target_position_id",min:150,disabled:"isedit"},
            { th: '目标仓位', col: 'target_position_id', sort:false,type:"select",data:"position_list",text:"target_position_code",value:"target_position_id",parent:"target_area_id",min:150,disabled:"isedit"},
           	{ th: '目标托盘',col: 'target_pallet_code',min:100,type:"text",disabled:"istext1"},
           	{ th: '添加托盘', col: 'target_pallet_id', class:"", min:60,align: 'left',type:"button",sort:false,
			button:{
					text:"添加",
					class:"",//默认带index= 数据row下标。
					//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
           	{ th: '生产批次', col: 'product_batch'}, 
			{ th: 'ERP批次', col: 'erp_batch'}, 
			{ th: '批次号', col: 'batch'},
		
		]
	},
	{
		fixed:true,
		columns:[
			{ th: '物料编码', col: 'mat_code', min: 60 },
			{ th: '物料描述', col: 'mat_name' },
			{ th: '包装类型', col: 'package_type_code'},
			{ th: '包装规格', col: 'package_standard_ch'},
			{ th: '件数', col: 'pack_num',type:'number',align:'right'},
			{ th: '源仓储区', col: 'area_name'}, 
			{ th: '源仓位', col: 'position_code'}, 
			{ th: '库存数量', col: 'stock_qty',type:'number',align:'right'}, 
			{ th: '调整数量', col: 'qty',type:'text',align:'right',decimal:{regex:'decimal_place'},min:150,disabled:"istext",nullable:true,class:"out_qty",distype:"number"}, 
			{ th: '目标存储区', col: 'target_area_id', sort:false,type:"select",data:"area_list",text:"target_area_name",value:"target_area_id",children:"target_position_id",min:150,disabled:"isedit"},
            { th: '目标仓位', col: 'target_position_id', sort:false,type:"select",data:"position_list",text:"target_position_code",value:"target_position_id",parent:"target_area_id",min:150,disabled:"isedit"},
           	{ th: '生产批次', col: 'product_batch'}, 
			{ th: 'ERP批次', col: 'erp_batch'}, 
			{ th: '批次号', col: 'batch'},
		
		]
	}
]
function init(number){
   var url=uriapi+"/biz/task/get_order_info?stock_task_id="+number;
    showloading();
    ajax(url,"get",null,function(data){
    	$.each(data.body.item_list,function(i,item){
    		if(item.target_pallet_id==0){
    			item.target_pallet_id="";
    		}
    	})
        indexdata=data;  
        databindInit()
        layer.close(indexloading)
    },function(e) {
		layer.close(indexloading);
	})
	
}
function databindInit(){
	$(".status_name").html(indexdata.body.status_name)
	$(".bustype").empty();
	$(".bustype").html("<span class='input-group-text'>"+indexdata.body.receipt_type_name+"</span>");
	if(indexdata.body.receipt_type==38){
		btn_index=0;
	}else if(indexdata.body.receipt_type==37){
		btn_index=1	
	}else if(indexdata.body.receipt_type==36){
		btn_index=2
	}else if(indexdata.body.receipt_type==39){
		btn_index=3
	}
	if(indexdata.body.status==1){//草稿	
			closeTip();
			$(".create_user").html("<span class='input-group-text'>"+indexdata.body.user_name+"</span>");
	    	$(".create_time").html("<span class='input-group-text'>"+indexdata.body.create_time+"</span>");
	    	$(".wh_info").html("<span class='input-group-text'>"+indexdata.body.wh_code+"-"+indexdata.body.wh_name+"</span>")
	    	$(".fty_info").html("<span class='input-group-text'>"+indexdata.body.fty_code+"-"+indexdata.body.fty_name+"</span>");
	    	$('.loc_info').val(indexdata.body.location_id);
	    	getareainfo();
	    	getwhinfo();
	    	$(".tally").val(indexdata.body.tally_clerk_id);
	    	$(".truck").val(indexdata.body.forklift_worker_id);
	    	$(".hamal").val(indexdata.body.remover_id);
	    	if(indexdata.body.receipt_type==38){//基于仓库
		    	$(".source").show();
				$(".targets").show();
				$(".trays").hide();
				$(".jhdh").hide();
				$(".delet").hide();
	    	}else if(indexdata.body.receipt_type==37){//基于托盘
		    	$(".jhdh").hide();
				$(".source").hide();
				$(".targets").show();
				$(".trays").show();
				$(".delet").hide();
				$(".mpalletnum").val(indexdata.body.item_list[0].target_pallet_code)
				$(".ypalletnum").val(indexdata.body.item_list[0].pallet_code)
	    	}else if(indexdata.body.receipt_type==36){
		    	$(".yldh").attr("placeholder","包装物序号/生产批次/ERP批次");
				$(".source").hide();
				$(".targets").hide();
				$(".trays").hide();
				$(".jhdh").show();
				$(".delet").show();
	    	}else if(indexdata.body.receipt_type==39){
		    	$(".yldh").attr("placeholder","请输入物料编码/物料描述/生产批次/ERP批次");
				$(".source").hide();
				$(".targets").hide();
				$(".jhdh").show();
				$(".trays").hide();
				$(".delet").show();
	    	}
	    }else if(indexdata.body.status==2){//已提交
	    	$('.loc_info').remove();
	    	$(".tally").remove();
	    	$(".truck").remove();
	    	$(".hamal").remove();
	    	$(".tallyinfo").append("<span class='input-group-text tally'>"+indexdata.body.tally_clerk+"</span>");
	    	$(".truckinfo").append("<span class='input-group-text truck'>"+indexdata.body.forklift_worker+"</span>");
	    	$(".hamalinfo").append("<span class='input-group-text hamal'>"+indexdata.body.remover+"</span>");
	    	$(".wh_info").html("<span class='input-group-text'>"+indexdata.body.wh_code+"-"+indexdata.body.wh_name+"</span>")
	    	$(".fty_info").html("<span class='input-group-text'>"+indexdata.body.fty_code+"-"+indexdata.body.fty_name+"</span>");
	    	$(".getloc").append("<span class='input-group-text'>"+indexdata.body.location_code+"-"+indexdata.body.location_name+"</span>")
	    	$(".create_user").html("<span class='input-group-text'>"+indexdata.body.user_name+"</span>");
	    	$(".create_time").html("<span class='input-group-text'>"+indexdata.body.create_time+"</span>")
	    	$(".delet").hide();
			$(".btnsubmit").remove();
			$(".btnsave").remove();
			$(".addtp").remove();
			if(indexdata.body.receipt_type==38){//基于仓库
				$(".source").show();
				$(".targets").show();
				$(".trays").hide();
				$(".jhdh").hide();
				$(".addinfo").hide();
				$(".yarea_info").remove();
				$(".ypos_info").remove();
				$(".yarea").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].area_name+"</span>")
				$(".ypos").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].position_code+"</span>");
				$(".marea_info").remove();
				$(".mpos_info").remove();
				$(".marea").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].target_area_name+"</span>")
				$(".mpos").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].target_position_code+"</span>");
			}else if(indexdata.body.receipt_type==37){//基于托盘
				$(".jhdh").hide();
				$(".source").hide();
				$(".targets").show();
				$(".trays").show();
				$(".addinfo").remove();
				$(".ypalletnum").remove();
				$(".ypalle").append("<span class='col-xs-4 input-group-text'>"+indexdata.body.item_list[0].pallet_code+"</span>")
				$(".marea_info").remove();
				$(".mpos_info").remove();
				$(".marea").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].target_area_name+"</span>")
				$(".mpos").append("<span class='col-xs-3 input-group-text'>"+indexdata.body.item_list[0].target_position_code+"</span>");
				$(".mpalletnum").remove();
				$(".mpalle").append("<span class='col-xs-4 input-group-text'>"+indexdata.body.item_list[0].target_pallet_code+"</span>")
			}else if(indexdata.body.receipt_type==36){
				$(".jhdh").hide();
				$(".cpinfo").show();
				$(".source").hide();
				$(".targets").hide();
				$(".trays").hide();
			}else if(indexdata.body.receipt_type==39){
				$(".jhdh").hide();
				$(".cpinfo").show();
				$(".source").hide();
				$(".targets").hide();
				$(".trays").hide();
			}
			getareainfo();
	    }
	    $(".spanno").html(indexdata.body.stock_task_code)
		    
   }
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
//获取库存地点仓库
function getlocinfo(){
	showloading();
	var url = uriapi + "/biz/task/get_base_info";
	ajax(url, 'GET', null, function(data) {
		locdata=data
		$.each(data.body, function(i, item) {
			$(".loc_info").append("<option value='" + item.loc_id + "'data-index='" + i + "'>" + item.loc_code+"-"+item.loc_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function getwhinfo(){
	$(".wh_info").html("");
	$(".fty_info").html("");
	$(".tally").empty();
	$(".truck").empty();
	$(".hamal").empty();
	var index=parseInt($(".loc_info option:selected").attr("data-index"));
	if(index!=-1){
		$(".wh_info").html(locdata.body[index].wh_code+"-"+locdata.body[index].wh_name);
		$(".fty_info").html(locdata.body[index].fty_code+"-"+locdata.body[index].fty_name);
		if(locdata.body[index].tally_clerk_list.length>0){
			$.each(locdata.body[index].tally_clerk_list, function(i, item) {
				$(".tally").append("<option value='" + item.id+ "'>" +item.name + "</option>");
			});
		}else{
			$(".tally").append("<option value='0'></option>");
		}
		 if(locdata.body[index].forklift_worker_list.length>0){
		 	$.each(locdata.body[index].forklift_worker_list, function(i, item) {
				$(".truck").append("<option value='" + item.id+ "'>" + item.name + "</option>");
			});
		 }else{
		 	$(".truck").append("<option value='0'></option>");
		 }
		 if(locdata.body[index].remover_list.length>0){
			 $.each(locdata.body[index].remover_list, function(i, item) {
				$(".hamal").append("<option value='" + item.id+ "'>" + item.name + "</option>");
			});
		 }else{
		 	$(".hamal").append("<option value='0'></option>");
		 }
	}
}
function addtp(){
	var url=uriapi+"/biz/task/pallet/get_new_pallet";
	ajax(url, 'GET', null, function(data) {
		tpdata=data
		$(".mpalletnum").val(tpdata.body.pallet_code)
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
//获取存储区仓位
function getareainfo(){
	$(".yarea_info option:gt(0)").remove();
	$(".marea_info option:gt(0)").remove();
	$(".ypos_info option:gt(0)").remove();
	$(".mpos_info option:gt(0)").remove();
	if(GetQueryString("id")!=null){
		if(indexdata.body.status==1){
			if($(".loc_info").val()==""){
					return false
				}
			var url = uriapi + "/biz/task/position/area_list?location_id="+$(".loc_info").val();
		}else{
			var url = uriapi + "/biz/task/position/area_list?location_id="+indexdata.body.location_id;
		}
	}else{
			if($(".loc_info").val()==""){
				return false
			}
			var url = uriapi + "/biz/task/position/area_list?location_id="+$(".loc_info").val();
	}
	showloading()
	ajax(url, 'GET', null, function(data) {
		areadata=data
		$.each(data.body, function(i, item) {
			$(".yarea_info").append("<option value='" + item.area_id + "'data-index='" + i + "'>" + item.area_name + "</option>");
			$(".marea_info").append("<option value='" + item.area_id + "'data-index='" + i + "'>" + item.area_name + "</option>");
		});
		if(GetQueryString("id")!=null&&indexdata.body.item_list.length>0&&indexdata.body.status==1){
			if(indexdata.body.receipt_type==38){
				$(".yarea_info").val(indexdata.body.item_list[0].area_id);
				getyposinfo();
				$(".marea_info").val(indexdata.body.item_list[0].target_area_id);
				getmposinfo();
				$(".ypos_info").val(indexdata.body.item_list[0].position_id);
				$(".mpos_info").val(indexdata.body.item_list[0].target_position_id);
			}else if(indexdata.body.receipt_type==37){
				$(".marea_info").val(indexdata.body.item_list[0].target_area_id);
				getmposinfo();
				$(".mpos_info").val(indexdata.body.item_list[0].target_position_id);
			}
		}
		getdropinfo();
		if(GetQueryString("id")!=null){
			if(g1==null){
				dataBind();			
			}
	    	dataBind();
		}
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function getyposinfo(){
	$(".ypos_info option:gt(0)").remove();
	var index=parseInt($(".yarea_info option:selected").attr("data-index"));
	if(index!=-1){
		$.each(areadata.body[index].position_list,function(i,item){
			$(".ypos_info").append(returnOption(item.position_id,item.position_name))
		})
	}
}
function getmposinfo(){
	$(".mpos_info option:gt(0)").remove();
	var index=parseInt($(".marea_info option:selected").attr("data-index"));
	if(index!=-1){
		$.each(areadata.body[index].position_list,function(i,item){
			$(".mpos_info").append(returnOption(item.position_id,item.position_name))
		})
	}
}
function search(){
	var url=uriapi;
	var obj={};
	var num=parseInt($(".loc_info option:selected").attr("data-index"));
	var index=parseInt(btn_index);
	if($(".loc_info").val()==""){
		layer.msg("请选择库存地点")
		return false;
	}
	obj.location_id=$(".loc_info").val();
	obj.wh_id=locdata.body[num].wh_id;
	obj.fty_id=locdata.body[num].fty_id;
	if(index==0){
		url=url+"/biz/task/position/get_material_list";
		if($(".yarea_info").val()==""){
			layer.msg("请选择源存储区")
			return false
		}
		if($(".ypos_info").val()==""){
			layer.msg("请选择源仓位")
			return false
		}
		obj.area_id=$(".yarea_info").val();
		obj.position_id=$(".ypos_info").val();
	}else if(index==1){
		url=url+"/biz/task/pallet/get_material_list";
		if($(".ypalletnum").val()==""){
			layer.msg("请填写源托盘");
			return false;
		}
		obj.pallet_code=$(".ypalletnum").val()
		
	}else if(index==2){
		url=url+"/biz/task/package/get_material_list";
		if($(".yldh").val()==""){
			layersMoretips("请输入包装物序号/生产批次/ERP批次", $(".yldh"));
			return false;
		}
		obj.keyword=$(".yldh").val();
	}else if(index==3){
		url=url+"/biz/task/material/get_material_list";
		if($(".yldh").val()==""){
			layersMoretips("请输入物料编码/物料描述/生产批次/ERP批次", $(".yldh"));
			return false;
		}
		obj.keyword=$(".yldh").val();
	}
	showloading();
	ajax(url, 'POST',JSON.stringify(obj), function(data) {
		if(data.head.status==true){
		if(indexdata!=null){
			if(index==2||index==3){
				var item_lists=[];
				var itemstokid=[];
				$.each(indexdata.body.item_list,function(j,jtem){
					itemstokid.push(jtem.stock_id)
				})
				$.each(data.body,function(i,item){
					if(itemstokid.indexOf(item.stock_id)==-1)
						item_lists.push(item)
				})
				$.each(item_lists,function(i,item){
					indexdata.body.item_list.push(item)
				})
			}else{
				indexdata.body.item_list = data.body;
			}
		}else{
			indexdata={};
			indexdata.body={};
			indexdata.body.item_list = data.body;
		}
		dataBind();
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	 }else{
	 	layer.close(indexloading);
	 }
	}, function(e) {
		layer.close(indexloading);
	});
}
function dataBind(){
	var index=parseInt(btn_index);
	if(g1==null){
			g1=$("#id_div_grid").iGrid({
			columns: jsonresult[index].columns, 
			data: null,
			skin:"tablestyle4",//皮肤
			resizehead:true,
			checkbox:jsonresult[index].fixed,
           	rownumbers:true,
			percent:30,
			sortable:false,
			absolutelyWidth:jsonresult[index].fixed,
			csort:function(a,b){
				if(b=="asc"){
					strsort=true;
				}else{
					strsort=false;
				}
				strorder=a;
				search();
			},
			clickbutton:function(a,b,c){//Button点击后触发 //添加托盘
//					console.log(a); //返回的字段名
//					console.log(b); //行的索引
//					console.log(c);//当前的对象
				var url=uriapi+"/biz/task/pallet/get_new_pallet";
				ajax(url, 'GET', null, function(data) {
					indexdata.body.item_list[b].target_pallet_code=data.body.pallet_code;
					indexdata.body.item_list[b].target_pallet_id=data.body.pallet_id;
					dataBind();
					layer.close(indexloading);
				}, function(e) {
					layer.close(indexloading);
				});	
			},
			loadcomplete:function(a){
				if(GetQueryString("id")!=null&&indexdata!=null){
					if(indexdata.body.status==2){
						if(indexdata.body.receipt_type==36){
							g1.displaycolumn([11],false)
						}
					}
				}
			},
			GetSelectedData:function(a){
			},
			cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
//				console.log(a);//返回的字段名
//				console.log(b);//列的索引
//				console.log(c);//行的索引
//				console.log(d);//返回的文本框数据
//				console.log(e);//返回的整条数据
//				console.log(f);//返回的文本框
				if(indexdata.body.receipt_type==36||btn_index=="2"){
					if(f.val()!=""){
						obj={
							"location_id":$(".loc_info").val(),
							"condition":f.val(),
							"type":"1"
						}
						var url=uriapi+"/biz/task/check_code_type";
						ajax(url, 'POST', JSON.stringify(obj), function(data) {
							if(data.head.status==true){
								indexdata.body.item_list[c].target_pallet_code=data.body.pallet_code;
								indexdata.body.item_list[c].target_pallet_id=data.body.pallet_id;
								dataBind();
							}else{
								indexdata.body.item_list[c].target_pallet_code="";
								indexdata.body.item_list[c].target_pallet_id="";
								dataBind();
							}
							layer.close(indexloading);
						}, function(e) {
							layer.close(indexloading);
						});	
					}
				}
			},

		});
	}else{
		if(GetQueryString("id")==null){
			$.each(indexdata.body.item_list,function(i,item){
				if(btn_index=="2"||btn_index=="3"){
					item.area_list=area_list;
					item.position_list=position_list;
				}
				if(btn_index=="3"){
					if(item.sn_used==1){
						item.issn_used=false
					}else{
						item.issn_used=true
					}
				}
			})
		}else{
		$.each(indexdata.body.item_list,function(i,item){
			item.isedit=false;
			if(indexdata.body.receipt_type==36){
					item.area_list=area_list;
					item.position_list=position_list;
				}else if(indexdata.body.receipt_type==39){
					if(item.sn_used==1){
						item.issn_used=false
					}else{
						item.issn_used=true
					}
					item.area_list=area_list;
					item.position_list=position_list;
				}
				if(indexdata.body.status==2){
					item.isedit=true;
					item.istext=true;
					item.istext1=true;
					g1.setCheckboxdata(false); 
				}
			})
		}
		g1.showdata(indexdata.body.item_list);
	}
}
//校验托盘
function palletcheck(){
	var obj={};
	if($(".loc_info").val()==""){
		layer.msg("请选择库存地点");
		return false
	}
	if($(".mpalletnum").val()==""){
		layer.msg("请输入目标托盘进行校验");
		return false
	}
	obj={
		"location_id":$(".loc_info").val(),
		"condition":$(".mpalletnum").val(),
		"type":"1"
	}
	var url=uriapi+"/biz/task/check_code_type";
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			tpdata=data;
			$(".mpalletnum").val(data.body.pallet_code);
			$(".marea_info").val(data.body.area_id);
			getmposinfo();
			$(".mpos_info").val(data.body.position_id)
		}else{
			$(".mpalletnum").val("")
		}
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
//获取基础备注信息
function GetRemark(){
	var obj={};
	if(GetQueryString("id")==null){
		obj.isedit=true;
	}else{
		if(indexdata!=null&&indexdata.body.status==2){
			obj.isedit=false;
		}
	}
	if(indexdata!=null&&indexdata.body.remark!=""){
		remarkinfo=indexdata.body.remark;
	}
	obj.remark=remarkinfo;
     return obj;
}
//选中删除
function delTabel(){
    layer.confirm("是否删除？", {
        btn: ['删除', '取消'],
        icon: 3
    }, function() {
        if($(".body .chk input:checked").length==0){
            layer.msg("请选择删除物料")
            return false
        }
        for(var i=$(".body .chk input:checked").length-1;i>=0;i--){
            var index=$(".body .chk input:checked").eq(i).val()
            indexdata.body.item_list.splice(index,1)
        }
        dataBind();
        layer.close(parseInt($(".layui-layer-shade").attr("times")));
    }, function() {
    });
}
function submit(value){
	var isChecked=true;
	var obj={};
	var url=uriapi;
	if(GetQueryString("id")==null){
		var isselect=false;
		$("[name='check']").each(function(i,item){
			var check=$(this)
			if(check.is(":checked")){
				isselect=true
			}
		})
		if(!isselect){
			layer.msg("请选择业务类型")
			return false
		}
	}
	var num=parseInt($(".loc_info option:selected").attr("data-index"));
	var index=parseInt(btn_index);
	if($(".loc_info").val()==""){
		layer.msg("请选择库存地点")
		return false;
	}
	obj.location_id=$(".loc_info").val();
	obj.wh_id=locdata.body[num].wh_id;
	obj.fty_id=locdata.body[num].fty_id;
	obj.submit=value;
	
	if(indexdata!=null&&indexdata.body.remark!=undefined&&indexdata.body.remark!=""){
		obj.remark=indexdata.body.remark;
	}else{
		obj.remark=remarkinfo;
	}
	if(GetQueryString("id")!=null){
		obj.stock_task_id=indexdata.body.stock_task_id
	}else{
		obj.stock_task_id="0"
	}
	obj.remover_id=$(".hamal").val()
	obj.forklift_worker_id=$(".truck").val();
	obj.tally_clerk_id=$(".tally").val();
	obj.remover=$(".hamal option:selected").html();
	obj.forklift_worker=$(".truck option:selected").html();
	obj.tally_clerk=$(".tally option:selected").html();
	if(index==0){
		url=url+"/biz/task/position/save";
		if($(".yarea_info").val()==""){
			layer.msg("请选择源存储区")
			return false;
		}
		if($(".ypos_info").val()==""){
			layer.msg("请选择源仓位")
			return false;
		}
		if($(".marea_info").val()==""){
			layer.msg("请选择目标存储区")
			return false;
		}
		if($(".mpos_info").val()==""){
			layer.msg("请选择目标仓位")
			return false;
		}
		obj.s_area_id=$(".yarea_info").val();
		obj.s_position_id=$(".ypos_info").val();
		obj.t_area_id=$(".marea_info").val();
		obj.t_position_id=$(".mpos_info").val();
		if(indexdata==null||indexdata.body.item_list.length==0){
			layer.msg("请至少添加一条产品信息");
			isChecked=false;
		}
	}else if(index==1){
		url=url+"/biz/task/pallet/save";
		if($(".ypalletnum").val()==""){
			layer.msg("请填写源托盘")
			return false
		}
		if($(".marea_info").val()==""){
			layer.msg("请选择目标存储区")
			return false
		}
		if($(".mpos_info").val()==""){
			layer.msg("请选择目标仓位")
			return false
		}
		if($(".mpalletnum").val()==""){
			layer.msg("请填写目标托盘")
			return false
		}
		obj.s_pallet_code=$(".ypalletnum").val();
		obj.t_area_id=$(".marea_info").val();
		obj.t_position_id=$(".mpos_info").val();
		if(GetQueryString("id")!=null){
			obj.t_pallet_id=indexdata.body.item_list[0].target_pallet_id;
		}else{
			obj.t_pallet_id=tpdata.body.pallet_id;
		}
		if(indexdata==null||indexdata.body.item_list.length==0){
			layer.msg("请至少添加一条产品信息");
			isChecked=false;
		}
	}else if(index==2){
		var url=uriapi+"/biz/task/package/save";
		obj.item_list=[]
		$.each(indexdata.body.item_list,function(i,item){
			if(item.target_pallet_id==""||item.target_pallet_id==undefined){
				item.target_pallet_id=0
			}
			if(item.target_area_id!=undefined&&item.target_position_id!=undefined&&item.target_pallet_id!=undefined){
				obj.item_list.push({
					"stock_id":item.stock_id,
					"t_area_id":item.target_area_id,
					"t_position_id":item.target_position_id,
					"t_pallet_id":item.target_pallet_id,
					"qty":item.stock_qty
				})
			}
		})
		if(obj.item_list.length==0){
			layer.msg("请至少添加一条信息");
			isChecked=false;
		}
	}else if(index==3){
		url=url+"/biz/task/material/save";
		obj.item_list=[]
		if(!g1.checkData()){
			isChecked=false;
			return false;
		}
		$.each(indexdata.body.item_list,function(i,item){
		
		if(item.target_area_id!=undefined&&item.target_position_id!=undefined&&item.qty!=""){
		if(item.sn_used==1){
			if(item.target_pallet_id==undefined||item.target_pallet_id==""){
				layersMoretips("目标托盘不能为空",$(".target_pallet").eq(i))
			}
		 }else{
		 	item.target_pallet_id=0
		 }
		if(parseFloat(item.qty)>item.stock_qty){
				layersMoretips("调整数量不能大于库存数量",$(".out_qty").eq(i))
                isChecked=false
		 }
			if(item.target_position_id==undefined){
				item.target_position_id=0
			}
			obj.item_list.push({
					"stock_id":item.stock_id,
					"t_area_id":item.target_area_id,
					"t_position_id":item.target_position_id,
					"t_pallet_id":item.target_pallet_id,
					"qty":item.qty
				})
			}
		})
		if(obj.item_list.length==0){
			layer.msg("请至少添加一条信息")
			isChecked=false;
		}
	}
	if(!isChecked){
		return false;
	}
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
		  		closeTipCancel();
				location.href = "detail.html?id="+data.body.stock_task_id;
			}, 3000);	
		}else{
		layer.close(indexloading);
		}	
	}, function(e) {
		layer.close(indexloading);
	},true);
}
//更换基础备注信息
function SetRemark(data){
	if(indexdata!=null){
		indexdata.body.remark=data;
	}
   remarkinfo=data;
}
//行项目存储区仓位下拉数据
function getdropinfo(){
	area_list=[];
	position_list=[];
	$.each(areadata.body,function(i,item){
		area_list.push({
			"target_wh_id":item.wh_id,
			"target_area_id":item.area_id,
			"target_area_code":item.area_code,
			"target_area_name":item.area_name
		})
		$.each(item.position_list,function(j,jtem){
			jtem.area_id=item.area_id;
			position_list.push({
				"target_area_id":jtem.area_id,
				"target_position_id":jtem.position_id,
				"target_position_code":jtem.position_code,
				"target_position_name":jtem.position_name
			})
		})
	})
}
//点击事件  过帐/删除
function clicksubmit(){
    $(".btnsubmit").click(function () {
        layer.confirm("是否提交？", {
            btn: ['提交', '取消'],
            icon: 3
        }, function () {
            submit(true);
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
    //冲销
    $(".btnsave").click(function() {
        layer.confirm("是否保存？", {
            btn: ['保存', '取消'],
            icon: 3
        }, function() {
           submit(false);
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
}
//提交
$(function(){
	$(".create_user").html(loginuserdata.body.user_name);
	$(".create_time").html(GetCurrData());
	getlocinfo();
	$(".loc_info").change(function(){
        getwhinfo();
        getareainfo();
        g1=null;
        if(indexdata!=null){
        	indexdata.body.item_list=[];	
        }
        if(parseInt(btn_index)!=-1){
        	dataBind();
        }
    })
	$(".yarea_info").change(function(){
        getyposinfo()
     })
	$(".marea_info").change(function(){
        getmposinfo()
   })
if(GetQueryString("id")==null){
	closeTip();
	$("[name='check']").change(function(){
		btn_index=$(this).val();
		var value=$(this).val()
		if(value=="0"){
			$(".source").show();
			$(".targets").show();
			$(".trays").hide();
			$(".jhdh").hide();
			$(".delet").hide();
		}else if(value=="1"){
			$(".jhdh").hide();
			$(".source").hide();
			$(".targets").show();
			$(".trays").show();
			$(".delet").hide();
		}else if(value=="2"){
			$(".yldh").attr("placeholder","包装物序号/生产批次/ERP批次");
			$(".source").hide();
			$(".targets").hide();
			$(".trays").hide();
			$(".jhdh").show();
			$(".delet").show();
		}else if(value=="3"){
			$(".yldh").attr("placeholder","请输入物料编码/物料描述/生产批次/ERP批次");
			$(".source").hide();
			$(".targets").hide();
			$(".jhdh").show();
			$(".trays").hide();
			$(".delet").show();
		}
		indexdata=null
		g1=null;
		dataBind();
	})
	}else{
	var valuelocdata= setInterval(function(){
	    if(locdata!=null){
	    	init(GetQueryString("id"))
	    	clearInterval(valuelocdata);
	    		}
	    },50);
	}
	 $("input.yldh").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
    $("a.btn-search").click(function () {
        search();
    });
    $(".btn_trays").click(function(){
    	search();
    })
    $(".btn_pos").click(function(){
    	search();
    })
    $(".addtp").click(function(){
    	addtp()
    })
    $(".delet").click(function(){
        delTabel();
     })
	clicksubmit()
    tableedit($("body"))
   	 $("input.mpalletnum").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            palletcheck();
        }
    });
    $("a.btn_tp").click(function () {
        palletcheck();
    }); 
})
