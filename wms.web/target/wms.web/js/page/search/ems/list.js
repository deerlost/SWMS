var indexdata ={},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list=[];

function bindata(){
		g1=$("#id_div_grid").iGrid({
			columns: [
				{ th: '单据编号', col: 'refer_receipt_code', width:130},
				{ th: 'MES业务类型', col: 'business_type', width:120},
				{ th: 'MES业务名称', col: 'business_name', width:130},
				{ th: '业务名称', col: 'receipt_type_name', width:140},
				{ th: '创建人', col: 'create_user',sort:false, width:120 },
				{ th: '创建时间', col: 'create_time',sort:false, width:130},
				{ th: '错误信息', col: 'error_info'}					
			],
			data:indexdata.body,
			checkbox: false,
			absolutelyWidth:true,
			selectedrow:{
			isHasindex:true
			},
			percent:40,
			skin:"tablestyle4",//皮肤
			resizehead:true,
		})
}
function search(pageindex) {
var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	var param = {
		refer_receipt_code:$(".refer_receipt_code").val(),
		create_user:$(".create_user").val(),
		create_time:$(".create_time").val(),
		sort_column:strorder,
		sort_ascend:strsort,
		page_index: pageindex||1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};

	var url = uriapi + "/biz/emsquery/query_fail_mes";
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
		$.each(data.body,function(i,item){
			item.create_time_start=$(".time_begin").val();
			item.create_time_end=$(".time_end").val();
		})
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
	var mydate=new Date()
	$("#time").val(mydate.getFullYear()+"-"+(mydate.getMonth()+1)+"-"+mydate.getDate())
	bindDatePicker();
	indexdata.head = [];
	indexdata.head.total = "";
	tableedit($("body"));
	//showloading();
	bindata()
	$(".btn_search").click(function() {
		search();
	})
});