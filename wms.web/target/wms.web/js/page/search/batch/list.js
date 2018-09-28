var indexdata = {},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list = [],
	kcdddata=null,
	cjinfo=[];
function tishowinfo() {
	var indextips = -1;
	$(".tiinfo").unbind("hover");
	$(".tiinfo").hover(function() {
		if(indextips == -1) {
			indextips = layer.tips("1、支持多物料查询，用' , '分隔：60000001,60000002,60000003<br />2、支持物料号段查询，用' - '连接：60000001-60000010<br />3、支持以上俩种一起使用：60000001-60000010,60000030-60000050,60000070-60000090", $(".tiinfo"), {
				tips: [2, '#f7b824'],
				tipsMore: true,
				time: 5000,
				end: function(index) {
					indextips = -1
				}
			});
		}
	})
}
function returnOption1(value, value1, text) {
	return "<option value='" + value + "' data-wh_id='" + value1 + "'>" + text + "</option>";
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function initData() {
	var url = uriapi + "/auth/get_fty_location_for_user";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body, function(i, item) {
				$(".selectwerks").append("<option value='" + item.fty_id + "'data-index='" + i + "'>" +item.fty_code +"-"+item.fty_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
//工厂改变库存地点改变
function getlocinfo(){
	$(".selectlgort option:gt(0)").remove();
	var index=parseInt($(".selectwerks option:selected").attr("data-index"));
  if(index!=-1){
	$.each(kcdddata.body[index].location_ary, function(i, item) {
			$(".selectlgort").append(returnOption1(item.loc_id, item.wh_id, item.loc_code+"-"+item.loc_name));
	})
  }	
}
//获取仓库号下拉数据
function getwhlist(){
 var url = uriapi + "/biz/stockquery/get_warehouse_list";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body, function(i, item) {
				$(".selectwh").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
//特殊库存与库存状态下拉信息
function getBaseInfo() {
	showloading();
	ajax(httpApi + '/biz/stockquery/get_status_and_spec_stock', 'GET', null, function(data) {
		layer.close(indexloading);
		if(data.head.status == true) {
				$.each(data.body.stock_type,function(i,item){
					$(".invent_state").append(returnOption(item.value,item.name));
				})
				$.each(data.body.status,function(i,item){
					$(".kc_state").append(returnOption(item.value,item.name))
				})
		} else {
			layer.msg(data.head.msg, {
				time: 2000
			});
		}
	});
}
function files() {
	$("[name='mat_code']").val($(".matnr").val());//物料编码
	$("[name='mat_name']").val($(".maktx").val());//物料描述
	$("[name='input_date_begin']").val($(".txtstartdate").val());//入库开始
	$("[name='input_date_end']").val($(".txtenddate").val());//入库结束
	$("[name='fty_id']").val($(".selectwerks").val());//工厂id
	$("[name='location_id']").val($(".selectlgort").val());//库存地点id
	$("[name='stock_type_id']").val($(".invent_state").val());//库存类型
	$("[name='mat_group_code']").val($(".groups").val());//物料组
	$("[name='product_batch']").val($(".product_batch").val());//生产批次
	$("[name='status']").val($(".kc_state").val());//库存状态
	$("[name='erp_batch']").val($(".erp_batch").val());//库存状态
	$("[name='wh_id']").val($(".selectwh").val());//仓库号
	$("#download").attr("action", uriapi + "/biz/stockquery/download_stocd_batch");
	$("#download").submit();
}
function bindata() {
	g1=$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '物料组', col: 'mat_group_code', min:120},
			{ th: '物料编码', col: 'mat_code', min: 200},
			{ th: '物料描述', col: 'mat_name',min: 200},
			{ th: '包装类型', col: 'package_type_code'}, 
			{ th: '包装规格', col: 'package_standard_ch'}, 
			{ th: '库存数量', col: 'stock_qty', type:"number",decimal:"decimal_place"}, 
			{ th: '计量单位', col: 'unit_name', min: 60},
			{ th: '公斤数量', col: 'qty', type:"number",decimal:"decimal_place"}, 
			{ th: '工厂', col: 'fty_name', min: 60 },
			{ th: '库存地点', col: 'location_name'},
			{ th: '生产批次', col: 'product_batch'}, 
			{ th: 'ERP批次', col: 'erp_batch'}, 
			{ th: '质检批次', col: 'quality_batch'}, 
			{ th: '批次号', col: 'batch'},//enmng
			{ th: '库存类型', col: 'stock_type_name' },
			{ th: '入库日期', col: 'input_date' },
			{ th: '批次信息', col: 'batch', type:"popup",sort:false,
			   popup:{
			   		display:"查看",
					href:"batchview.html",
					area:"600px,450px",
					title:"批次信息",
				}
			},
			{ th: '库存状态', col: 'status_name'},
			{ th: '冻结原因', col: 'reason',width:80},
		], 
		data: indexdata.body,
		checkbox:true,
		rownumbers:true,
		resizehead:true,
		absolutelyWidth:true,
		percent:30,//空占位百分比
		skin:"tablestyle4",//皮肤
		GetSelectedData(a) {
		   cjinfo = a;
		},
		loadcomplete:function(a){//页面绘制完成，返回所有数据。
		if(a.length==0){
			 $(".submitbutton").html("");
			}
		},
		csort:function(a,b){
		if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
            search();
        }
	});
}
function batchinfo(index){
	var item=indexdata.body[index];
	var arrs={
		"shelf_life":item.shelf_life,
		"purchase_order_code":item.purchase_order_code,
		"class_type_name":item.class_type_name,
		"installation_name":item.installation_name,
		"product_line_name":item.product_line_name
	}
	return arrs
}
function search(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	var param = {
		mat_code:$(".matnr").val(),
		mat_name:$(".maktx").val(),
		mat_group_code:$(".groups").val(),
		input_date_begin:$(".txtstartdate").val(),
		input_date_end:$(".txtenddate").val(),
		fty_id:$(".selectwerks").val(),
		location_id:$(".selectlgort").val(),
		stock_type_id:$(".invent_state").val(),
		product_batch:$(".product_batch").val(),
		erp_batch:$(".erp_batch").val(),
		wh_id:$(".selectwh").val(),
		status:$(".kc_state").val(),
		sort_column: strorder,
		sort_ascend: strsort,
		page_index: pageindex || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	var url = uriapi + "/biz/stockquery/serach_stoch_batch";
	showloading();
	ajax(url, 'post', JSON.stringify(param), function(data) {
		layer.close(indexloading);
		if(data.body.length > 0) {
			initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
		} else {
			$("#J_ListPagination").html("")
		}
		if(data == null || data == "") {
			layer.msg("数据有误");
			return false;
		}
		if(data.body.length > 0) {
			$(".btnsave").show();
			$(".count").html("共" + data.head.total + "条数据&nbsp;&nbsp;&nbsp;&nbsp;"+"共"+data.body[0].sum_qty+"千克")
			$.cookie("stocktotal", data.head.total);
			self.initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
		} else {
			$(".btnsave").hide();
			$("#J_ListPagination").html("")
			$(".count").html("")
		}
		indexdata = data;
		bindata();
	}, function(e) {
		layer.close(indexloading);
		layer.msg("数据请求失败");
	});
}
// 初始化分页
function initPagination(total, current) {
	$("#J_ListPagination").unbind().pagination({
		total_pages: total,
		current_page: current,
		is_number: true,
		callback: function(event, page) {
			search(page);
		}
	});
}
function isfro(value,isdj){
	var obj={};
	obj.id=[]
	obj.reason=value;
	if(isdj==1){
		obj.status=4
	}else if(isdj==0){
		obj.status=1
	}
	obj.stock_type_id=$(".invent_state").val();
	if(cjinfo.length>0){
		$.each(cjinfo,function(i,item){
			obj.id.push(item.id)
		})
	}
	console.log(JSON.stringify(obj))
	var url=uriapi+"/biz/stockquery/update_status";
	showloading();
	 ajax(url,"POST",JSON.stringify(obj),function(data){
        layer.close(indexloading);
		if(data.head.status==true){
			location.href="list.html"
		}
    })
}
function frozen(){
	if(cjinfo.length==0){
		layer.msg("请选择要冻结的数据")
	}else{
		    layer.confirm("是否冻结？", {
            btn: ['冻结', '取消'],
            icon: 3
        }, function (index) {
         layer.close(index);	
		       var title = "冻结原因";
				var href = "frozen.html";
				var size = ["500px","400px"];
				layer.open({
					type: 2,
					title: title,
					shadeClose: false,
					shade: 0.8,
					area: size,
					content: href
				});
        }, function () {
        });
	}
}
function traw(){
	if(cjinfo.length==0){
		layer.msg("请选择要解冻的数据")
	}else{
		    layer.confirm("是否解冻？", {
            btn: ['解冻', '取消'],
            icon: 3
        }, function (index) {
			isfro("",0)
        }, function () {
        });
	}
}
function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	search()
}
$(function() {
	getwhlist();
	bindDatePicker();
	checkDateDifference('txtstartdate', 'txtenddate');
	bindata();
	tishowinfo();
	indexdata.head = [];
	indexdata.head.total = "";
	tableedit($("body"));
	showloading();
	initData();
	getBaseInfo();
	$(".btn_search").click(function() {
		search();
	})
	$(".btn_files").click(function() {
		files();
	})
	$(".isfrozen").click(function(){
		frozen();
	})
	$(".thaw").click(function(){
		traw();
	})
	$(".btnsave").click(function() {
		if(cjinfo.length == 0) {
			layer.msg("请选择要打印的数据");
			return false;
		}
		var jsondata ={};
		var bath_list=[];
		$.each(cjinfo,function(i,item) {
			bath_list.push(item.id);
		});
		jsondata.batch_list=bath_list;
		showloading();
		var url = (uriapi + '/biz/print/print_stock_batch_for_batch');
		ajax(url, 'POST', JSON.stringify(jsondata), function(data) {
			if(data.body.url == undefined || data.body.url == "")
				layer.alert("打印失败。");
			else
				window.open(data.body.url);
			layer.close(indexloading);
		}, function(e) {
			layer.close(indexloading);
			layer.alert("数据请求失败");
		});
	});
	$(".selectwerks").change(function(){
		if($(".selectwerks").val()==""){
			$(".selectwh").removeAttr("disabled");
		}else{
			$(".selectwh").attr("disabled","disabled");
		}
		getlocinfo();
	});
	$(".selectwh").change(function(){
		if($(".selectwh").val()==""){
			$(".selectwerks").removeAttr("disabled");
			$(".selectlgort").removeAttr("disabled");
		}else{
			$(".selectwerks").attr("disabled","disabled");
			$(".selectlgort").attr("disabled","disabled");
		}
	})
	$(".btnprint").click(function(){
		if(cjinfo.length==0){
			layer.msg("没有选择要打印的数据");
			return false;
		}
		showloading();
		var jsonlist={param:[]};
		$.each(cjinfo,function(i,item){
			jsonlist.param.push({
				samp_name:item.samp_name,
				specification:item.specification,
				product_batch:item.product_batch
			})
		})
		var url = uriapi + "/biz/print/print_lims";

		ajax(url, "POST", JSON.stringify(jsonlist), function(data) {
			if(data.head.status){
				window.open(data.body.file_name_url);
			}else{
				layer.msg("文件返回失败");
			}
			layer.close(indexloading);
		}, function(e) {
			layer.close(indexloading);
		},1);
	});
});