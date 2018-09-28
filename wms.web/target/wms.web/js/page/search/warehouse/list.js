var indexdata ={},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list=[];

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

function initData() {
	var url = uriapi + "/biz/stquery/baseinfo_byuser";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body.fty_list, function(i, item) {
				$(".selectwerks").append("<option value='" + item.fty_id + "'data-index='" + i + "'>" +item.fty_code +"-"+item.fty_name + "</option>");
				$(".selecttargetwerks").append("<option value='" + item.fty_id + "'data-index='" + i + "'>" +item.fty_code +"-"+item.fty_name + "</option>");
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
	$.each(kcdddata.body.fty_list[index].location_ary, function(i, item) {
			$(".selectlgort").append(returnOption1(item.loc_id, item.wh_id, item.loc_code+"-"+item.loc_name));
		})
	}	
}
function gettargetlocinfo(){
	$(".selecttargetlgort option:gt(0)").remove();
	var index=parseInt($(".selecttargetwerks option:selected").attr("data-index"));
  if(index!=-1){
	$.each(kcdddata.body.fty_list[index].location_ary, function(i, item) {
			$(".selecttargetlgort").append(returnOption1(item.loc_id, item.wh_id, item.loc_code+"-"+item.loc_name));
		})
	}	
}
function files() {
	$("[name='create_time_begin']").val($(".txtstartdate").val())
	$("[name='create_time_end']").val($(".txtenddate").val())
	$("[name='product_batch']").val($(".product_batch").val())
	$("[name='erp_batch']").val($(".erp_batch").val())
	$("[name='stock_code']").val($(".docnum").val())
	$("[name='create_user']").val($(".creat_user").val())
	$("[name='fty_output_id']").val($(".selectwerks").val())
	$("[name='location_output_id']").val($(".selectlgort").val())
	$("[name='fty_input_id']").val($(".selecttargetwerks").val())
	$("[name='location_input_id']").val($(".selecttargetlgort").val())
	$("[name='mat_doc_code']").val($(".mat_doc").val())
	$("[name='move_type_code']").val("")
	$("[name='wh_output_id']").val($(".selectwh").val());//发出仓库号
	$("[name='wh_input_id']").val($(".selectwh1").val());//接收仓库号
	$("[name='mat_code']").val($(".matnr").val())
	$("[name='mat_name']").val($(".maktx").val())
	$("[name='biz_types']").val(status_list)
	$("#download").attr("action", uriapi + "/biz/stquery/download_outandin_stock");
	$("#download").submit();
}
function bindata(){
		g1=$("#id_div_grid").iGrid({
			columns: [
				{ th: '业务类型', col: 'biz_type_name'},
				{ th: '物料编码', col: 'mat_code', min:200},
				{ th: '物料描述', col: 'mat_name', min:200 },
				{ th: '发出库存地点', col: 'location_output_name'},
				{ th: '接收库存地点', col: 'location_input_name'},
				{ th: '批次号', col: 'batch'},
				{ th: '生产批次', col: 'product_batch'},
				{ th: 'ERP批次', col: 'erp_batch'},
				{ th: '仓储单据类型', col: 'mat_doc_type_name'},//enmng
				{ th: '单据号', col: 'refer_receipt_code', type:"link",
				  link:{
	                    href:false,
	                    column:"receipt_type",
	                    target: "_blank",
	                    args:["refer_receipt_id|id"],
	                    links:[{value:"12",href:uriapi + "/html/storage/input/production/detail.html"},//生产入库
	                           {value:"13",href:uriapi + "/html/storage/input/other/detail.html"},//其他出库
	                           {value:"14",href:uriapi + "/html/storage/direct/vproductioninput/detail.html"},//虚拟生产入库
	                           {value:"15",href:uriapi + "/html/storage/direct/productioninput/detail.html"},//直发生产入库
	                           {value:"16",href:uriapi + "/html/storage/specificword/productioninput/detail.html"},//紧急作业生产入库
	                           {value:"17",href:uriapi + "/html/storage/specificrecord/productioninput/detail.html"},//紧急记账生产入库
	                           {value:"41",href:uriapi + "/html/storage/input/dump/detail.html"},//转出入库
	                           {value:"42",href:uriapi + "/html/storage/direct/vdumpinput/detail.html"},//虚拟转储入库
	                           {value:"43",href:uriapi + "/html/storage/specificword/dump/detail.html"},//紧急作业转储入库
	                           {value:"44",href:uriapi + "/html/storage/specificrecord/dump/detail.html"},//紧急记账转储入库
	                           {value:"22",href:uriapi + "/html/storage/output/sell/detail.html"},//销售出库
	                           {value:"23",href:uriapi + "/html/storage/output/scrap/detail.html"},//报废出库
	                           {value:"24",href:uriapi + "/html/storage/output/other/detail.html"},//其他出库
	                           {value:"25",href:uriapi + "/html/storage/output/production/detail.html"},//生产直发出库
	                           {value:"26",href:uriapi + "/html/storage/specificword/selloutput/detail.html"},//紧急作业销售出库
	                           {value:"27",href:uriapi + "/html/storage/specificword/selloutput/detail.html"},
	                           {value:"28",href:uriapi + "/html/storage/specificrecord/output/detail.html"},//紧急记账销售出库
	                    		]
                    }
				},
				{ th: '单据行号', col: 'refer_receipt_rid'},
				{ th: '数量', col: 'qty',type:"number"},
				{ th: '计量单位', col: 'name_zh'},
				{ th: '公斤数量', col: 'kg_qty',type:"number"},
				{ th: '状态', col: 'status'},
				{ th: '正向ERP凭证', col: 'mat_doc_code' },
				{ th: '正向MES凭证', col: 'mes_doc_code' },
				{ th: '逆向ERP凭证', col: 'mat_off_code' },
				{ th: '逆向MES凭证', col: 'mes_off_code' },
				{ th: '创建日期', col: 'create_time'},
				{ th: '过账日期', col: 'posting_date'},
				{ th: '创建人', col: 'user_name' },
				{ th: '司机', col: 'driver' },
				{ th: '车辆', col: 'vehicle' },
				{ th: '抬头备注', col: 'headremark',width:100 },
				{ th: '行项目备注', col: 'itemremark',width:100 },
			],
			data:indexdata.body,
			checkbox: false,
			absolutelyWidth:true,
			selectedrow:{
			args:["stock_output_rid"],
			isHasindex:true
			},
			skin:"tablestyle4",//皮肤
			resizehead:true,
			csort:function(a,b){
				if(b=="asc"){
						strsort=true;
					}else{
						strsort=false;
					}
					strorder=a;
		            search();
	        }
		})
}
//获取仓库号下拉数据
function getwhlist(){
 var url = uriapi + "/biz/stquery/get_warehouse_list";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body, function(i, item) {
				$(".selectwh").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
				$(".selectwh1").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function search(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	status_list=[];
	$(".chkbox input:checked").each(function() {
		status_list.push($(this).val())
	})
	var param = {
		product_batch:$(".product_batch").val(),//生产批次
		mat_doc_code:$(".mat_doc").val(),//物料凭证
		stock_code:$(".docnum").val(),//单据编号
		create_time_begin: $(".txtstartdate").val(), // 创建开始时间
		create_time_end: $(".txtenddate").val(), //  创建结束时间
		create_user: $(".creat_user").val(), //创建人
		move_type_code: "", //移动类型编码
		wh_output_id:$(".selectwh").val(),
		wh_input_id:$(".selectwh1").val(),
		fty_output_id:$(".selectwerks").val(), // 工厂
		location_output_id: $(".selectlgort").val(), // 库存地点
		fty_input_id:$(".selecttargetwerks").val(), // 工厂
		location_input_id: $(".selecttargetlgort").val(), // 库存地点
		mat_code: $(".matnr").val(), // 物料编码
		mat_name: $(".maktx").val(), //物料描述
		erp_batch: $(".erp_batch").val(),
		biz_types:status_list,
		sort_column:strorder,
		sort_ascend:strsort,
		page_index: pageindex||1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	var url = uriapi + "/biz/stquery/serach_outandin_stock";
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
			$(".count").html("共" + data.head.total + "条数据")
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
	showloading();
	initData();
	$(".btn_search").click(function() {
		search();
	})
	$(".btn_files").click(function() {
		files();
	})
	$(".chkbox input").change(function() {
		search(0);
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
	})
	
	$(".selecttargetwerks").change(function(){
		gettargetlocinfo();
	})
		$(".selecttargetwerks").change(function(){
		if($(".selecttargetwerks").val()==""){
			$(".selectwh1").removeAttr("disabled");
		}else{
			$(".selectwh1").attr("disabled","disabled");
		}
		gettargetlocinfo();
	});
	$(".selectwh1").change(function(){
		if($(".selectwh1").val()==""){
			$(".selecttargetwerks").removeAttr("disabled");
			$(".selecttargetlgort").removeAttr("disabled");
		}else{
			$(".selecttargetwerks").attr("disabled","disabled");
			$(".selecttargetlgort").attr("disabled","disabled");
		}
	})
});