var indexdata = {},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list = [],
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

function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}

function returnOption1(value, value1, text) {
	return "<option value='" + value + "' data-wh_id='" + value1 + "'>" + text + "</option>";
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
//库存状态下拉信息
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
	$("[name='area_id']").val($(".areainfo").val());
	$("[name='position_code']").val($(".position_code").val());
	$("[name='input_date_begin']").val($(".txtstartdate").val());
	$("[name='pallet_code']").val($(".pallet_code").val());
	$("[name='input_date_end']").val($(".txtenddate").val());
	$("[name='fty_id']").val($(".selectwerks").val());//工厂id
	$("[name='location_id']").val($(".selectlgort").val());//库存地点id
	$("[name='stock_type_id']").val($(".invent_state").val());//库存状态
	$("[name='status']").val($(".kc_state").val());//库存状态
	$("[name='product_batch']").val($(".product_batch").val());//生产批次
	$("[name='erp_batch']").val($(".erp_batch").val());//库存状态
	$("[name='wh_id']").val($(".selectwh").val());//仓库号
	$("#download").attr("action", uriapi + "/biz/stockquery/download_stock_position");
	$("#download").submit();
}
function bindata() {
	g1=$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '物料编码', col: 'mat_code', min: 60},
			{ th: '物料描述', col: 'mat_name'},
			{ th: '工厂', col: 'fty_name', min: 60 },
			{ th: '库存地点', col: 'location_name'},
			{ th: '存储区编码', col: 'area_code'},
			{ th: '存储区描述', col: 'area_name'},
			{ th: '仓位', col: 'position_code'}, 
			{ th: '托盘标签', col: 'pallet_code'}, 
			{ th: '仓位库存数量', col: 'stock_qty', type:"number"},
			{ th: '计量单位', col: 'unit_name', min: 60},
			{ th: '公斤数量', col: 'qty', type:"number"},
			{ th: '库存状态', col: 'status_name' },
			{ th: '库存类型', col: 'stock_type_name' },
			{ th: '包装物信息 ', col: 'mat_code',type:"popup",
				popup:{
					display:"查看",
					href:"snview.html",
					area:"800px,450px",
					title:"包装物信息",
				}
			},
		], 
		data: indexdata.body,
		checkbox:false,
		rownumbers:true,
		resizehead:true,
		absolutelyWidth:true,
		selectedrow: {
		 args: ["id","position_code","area_code"],
		 isHasindex: true
		},
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
function getareainfo(){
	$(".areainfo option:gt(0)").remove();
	if($(".selectwh").val()!=""){
		var url = uriapi + "/biz/stockquery/get_warehouse_area?wh_id="+$(".selectwh").val();
		ajax(url, 'GET', null, function(data) {
			$.each(data.body, function(i, item) {
				$(".areainfo").append("<option value='" + item.area_id + "'data-index='" + i + "'>" +item.area_name + "</option>");
			});
			layer.close(indexloading);
		}, function(e) {
			layer.close(indexloading);
		});
	}
}
function sninfo(index){
	indexdata.body[index].product_batch=$(".product_batch").val();
	indexdata.body[index].erp_batch=$(".erp_batch").val();
	indexdata.body[index].date_begin=$(".txtstartdate").val();
	indexdata.body[index].data_end=$(".txtenddate").val();
	return indexdata.body[index]
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
		position_code:$(".position_code").val(),
		area_id:$(".areainfo").val(),
		fty_id:$(".selectwerks").val(),
		location_id:$(".selectlgort").val(),
		product_batch:$(".product_batch").val(),
		erp_batch:$(".erp_batch").val(),
		pallet_code:$(".pallet_code").val(),
		stock_type_id:$(".invent_state").val(),
		input_date_begin:$(".txtstartdate").val(),
		input_date_end:$(".txtenddate").val(),
		status:$(".kc_state").val(),
		wh_id:$(".selectwh").val(),
		sort_column: strorder,
		sort_ascend: strsort,
		page_index: pageindex || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	var url = uriapi + "/biz/stockquery/serach_stock_position_group";
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
			$(".count").html("共" + data.head.total + "条数据&nbsp;&nbsp;&nbsp;&nbsp;"+"共"+data.body[0].sum_qty+"千克")
			$.cookie("stocktotal", data.head.total);
			initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
		} else {
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
	initData();
	getBaseInfo();
	$(".btn_search").click(function() {
		search();
	})
	$(".btn_files").click(function() {
		files();
	})
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
		getareainfo();
	})
});