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
		$.each(data.body, function(i, item) {
				$(".selectwh").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function files() {
	$("[name='mat_code']").val($(".matnr").val());//物料编码
	$("[name='mat_name']").val($(".maktx").val());//物料描述
	$("[name='create_time_s']").val($(".txtstartdate").val());//入库开始
	$("[name='create_time_e']").val($(".txtenddate").val());//入库结束
	$("[name='fty_id']").val($(".selectwerks").val());//工厂id
	$("[name='location_id']").val($(".selectlgort").val());//库存地点id
	$("[name='product_batch']").val($(".product_batch").val());//生产批次
	$("[name='erp_batch']").val($(".erp_batch").val());//库存状态
	$("[name='wh_id']").val($(".selectwh").val());//仓库号
	$("#download").attr("action", uriapi + "/biz/query/download_task_info_list");
	$("#download").submit();
}
function bindata() {
	g1=$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '物料编码', col: 'mat_code', min: 200},
			{ th: '物料描述', col: 'mat_name',min: 200},
			{ th: '作业请求号', col: 'stock_task_req_code', type:"link",sort:false,
			   link:{
					href:false,
					autocreate:false,
					args:["stock_task_req_id|id"],
					column:"stock_task_req_receipt_type",
					target:"_blank",
					links:[
						{value:31,href:uriapi+"/html/storage/ware/putaway/detail.html"},
						{value:32,href:uriapi+"/html/storage/ware/soldout/detail.html"}
					]
					
				}
			},
			{ th: '作业单号', col: 'stock_task_code', type:"link",sort:false,
			   link:{
					href:false,
					autocreate:false,
					args:["stock_task_id|id"],
					column:"receipt_type",
					target:"_blank",
					links:[
						{value:33,href:uriapi+"/html/storage/ware/putaway/view.html"},
						{value:34,href:uriapi+"/html/storage/ware/soldout/view.html"}
					]
					
				}
			},
			{ th: '作业单类型', col: 'receipt_type_name'},
			{ th: '作业单行号', col: 'stock_task_rid'},
            { th: '业务单号', col: 'refer_receipt_code', type:"link",
                link:{
                    href:false,
                    autocreate:false,
                    target: "_blank",
                    args:["refer_receipt_id|id"],
                    column:"refer_receipt_type",
                    links:[
                        {value:11,href:uriapi+"/html/storage/pack/order/detail.html"},
                        {value:12,href:uriapi+"/html/storage/input/production/detail.html"},
                        {value:13,href:uriapi+"/html/storage/input/other/detail.html"},
                        {value:14,href:uriapi+"/html/storage/direct/vproductioninput/detail.html"},
                        {value:15,href:uriapi+"/html/storage/direct/productioninput/detail.html"},
                        {value:16,href:uriapi+"/html/storage/specificword/productioninput/detail.html"},
                        {value:17,href:uriapi+"/html/storage/specificrecord/productioninput/detail.html"},
                        {value:21,href:uriapi+"/html/storage/ware/invoice/detail.html"},
                        {value:22,href:uriapi+"/html/storage/output/sell/detail.html"},
                        {value:23,href:uriapi+"/html/storage/output/scrap/detail.html"},
                        {value:24,href:uriapi+"/html/storage/output/other/detail.html"},
                        {value:25,href:uriapi+"/html/storage/output/production/detail.html"},
                        {value:26,href:uriapi+"/html/storage/specificword/selloutput/detail.html"},
                        {value:27,href:uriapi+"/html/storage/specificword/selloutput/detail.html"},
                        {value:28,href:uriapi+"/html/storage/specificrecord/output/detail.html"},
                        {value:31,href:uriapi+"/html/storage/ware/putaway/detail.html"},
                        {value:32,href:uriapi+"/html/storage/ware/soldout/detail.html"},
                        {value:33,href:uriapi+"/html/storage/ware/putaway/view.html"},
                        {value:34,href:uriapi+"/html/storage/ware/soldout/view.html"},
                        {value:35,href:uriapi+"/html/storage/ware/group/detail.html"},
                        {value:36,href:uriapi+"/html/storage/ware/ware/detail.html"},
                        {value:37,href:uriapi+"/html/storage/ware/ware/detail.html"},
                        {value:38,href:uriapi+"/html/storage/ware/ware/detail.html"},
                        {value:39,href:uriapi+"/html/storage/ware/ware/detail.html"},
                        {value:41,href:uriapi+"/html/storage/input/dump/detail.html"},
                        {value:42,href:uriapi+"/html/storage/direct/vdumpinput/detail.html"},
                        {value:43,href:uriapi+"/html/storage/specificword/dump/detail.html"},
                        {value:44,href:uriapi+"/html/storage/specificrecord/dump/detail.html"},
                        {value:51,href:uriapi+"/html/storage/dump/materiel/detail.html"},
                        {value:52,href:uriapi+"/html/storage/dump/materiel/detail.html"},
                        {value:53,href:uriapi+"/html/storage/dump/materiel/detail.html"},
                        {value:54,href:uriapi+"/html/storage/dump/production/detail.html"},
                        {value:55,href:uriapi+"/html/storage/specificword/productionrevolve/detail.html"},
                        {value:62,href:uriapi+"/html/storage/return/detail.html"}
                    ]

                }
            } ,
            { th: '业务单类型', col: 'refer_receipt_type_name'},
            { th: '司机', col: 'delivery_driver'},
            { th: '车辆', col: 'delivery_vehicle'},
            { th: '理货员', col: 'tally_clerk_name'},
            { th: '叉车工', col: 'forklift_worker_name'},
            { th: '搬运工', col: 'remover_name'},
            { th: '作业单抬头备注', col: 'head_remark'},
            { th: '单位', col: 'unit_name'},
            { th: '作业量', col: 'qty',type:"number"},
            { th: '作业量公斤数', col: 'qty_kg',type:"number"},
            { th: '包装类型', col: 'package_type_name'},
            { th: 'ERP批次', col: 'erp_batch'},
            { th: '生产批次', col: 'product_batch'},
            { th: '作业单行备注', col: 'item_remark'},
			{ th: '创建人', col: 'create_user'},
			{ th: '创建时间', col: 'create_time'},
			{ th: '状态', col: 'status_name'},
		], 
		data: indexdata.body,
		checkbox:false,
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
		create_time_s:$(".txtstartdate").val(),
		create_time_e:$(".txtenddate").val(),
		receipt_type_list:[],
		fty_id:$(".selectwerks").val(),
		location_id:$(".selectlgort").val(),
		product_batch:$(".product_batch").val(),
		erp_batch:$(".erp_batch").val(),
		wh_id:$(".selectwh").val(),
		sort_column: strorder,
		sort_ascend: strsort,
		page_index: pageindex || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	var url = uriapi + "/biz/query/select_task_info_list";
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
			$(".count").html("共" + data.head.total + "条数据&nbsp;&nbsp;&nbsp;&nbsp;")
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
});