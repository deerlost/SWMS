var indexdata ={},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list=[];
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}

function returnOption1(value, value1, text) {
	return "<option value='" + value + "' data-wh_id='" + value1 + "'>" + text + "</option>";
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
	var url = uriapi + "/biz/query/get_warehouse_list";
	ajax(url, 'GET', null, function(data) {
		$.each(data.body, function(i, item) {
			 $(".selectlgort").append("<option value='" + item.wh_id + "'data-index='" + i + "'>"+item.wh_code+"-"+item.wh_name+"</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});	
}
function files() {
	$("[name='create_time_start']").val($(".time_begin").val())
	$("[name='create_time_end']").val($(".time_end").val())
	$("[name='mat_code']").val($(".mat_code").val())
	$("[name='fty_id']").val($(".selectwerks").val())
	$("[name='wh_id']").val($(".selectlgort").val())
	$("#download").attr("action", uriapi + "/biz/query/download_dispatcher_list");
	$("#download").submit();
}
function bindata(){
		g1=$("#id_div_grid").iGrid({
			columns: [
				{ th: '物料编码', col: 'mat_code', min:150},
				{ th: '物料描述', col: 'mat_name', min:150 },
				{ th: '计量单位', col: 'unit_name'},
				{ th: '期初库存', col: 'begin_qty',type:"number"},
				{ th: '入库数量', col: 'input_qty',type:"number"},
				{ th: '出库数量', col: 'output_qty',type:"number"},
				{ th: '结余库存', col: 'end_qty',type:"number"},
				{ th: 'ERP批次', col: 'erp_batch'},
				{ th: '工厂', col: 'fty_name'},
				{ th: '库存地点', col:["location_code","location_name"]},
				{ th: '开始日期', col: 'create_time_start'},
				{ th: '结束日期', col: 'create_time_end'},							
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
function getloc(){
	$(".lgort option:gt(0)").remove();
	var index=parseInt($(".selectwerks option:selected").attr("data-index"));
  if(index!=-1){
	$.each(kcdddata.body[index].location_ary, function(i, item) {
			$(".lgort").append(returnOption1(item.loc_id, item.wh_id, item.loc_code+"-"+item.loc_name));
	})
  }	
}
function search(pageindex) {
var totalcount=-1,refresh=1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
		refresh=1
	}else{
		totalcount=indexdata.head.total;
		refresh=0
	}
	var isChecked=true;
	var time_begin=0;
	var time_end=0;
//	if($(".selectwerks").val()==""){
//		layersMoretips("必填项",$(".selectwerks"));
//		isChecked=false;
//	}
//	if($(".selectlgort").val()==""){
//		layersMoretips("必填项",$(".selectlgort"));
//		isChecked=false;
//	}
	if($(".time_begin").val()==""){
		layersMoretips("必填项",$(".time_begin"));
		isChecked=false;
	}else{
		time_begin=new Date($(".time_begin").val().split('-').join("/")).getTime();
	}
	if($(".time_end").val()==""){
		layersMoretips("必填项",$(".time_end"));
		isChecked=false;
	}else{
		time_end=new Date($(".time_end").val().split('-').join("/")).getTime();
	}
	if(time_begin>time_end){
		layer.msg("开始时间不能大于结束时间");
		isChecked=false;
	}
	var param = {
		create_time_start:$(".time_begin").val(),//查询日期
		create_time_end:$(".time_end").val(),
		mat_code:$(".mat_code").val(),
		fty_id:$(".selectwerks").val(), // 工厂
		location_id:$(".lgort").val(),
		wh_id: $(".selectlgort").val(), // 库存地点
		sort_column:strorder,
		sort_ascend:strsort,
		page_index: pageindex||1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount,
		refresh:refresh
	};
	if(!isChecked){
		return false
	}
	var url = uriapi + "/biz/query/get_dispatcher_list";
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
	$(".time_begin").datetimepicker('setStartDate','2018-08-01');
	checkDateDifference('time_begin', 'time_end');
	indexdata.head = [];
	indexdata.head.total = "";
	tableedit($("body"));
	showloading();
	initData();
	getlocinfo();
	bindata()
	$(".btn_search").click(function() {
		search();
	})
	$(".btn_files").click(function() {
		files();
	})
	$(".selectwerks").change(function(){
		if($(".selectwerks").val()==""){
			$(".selectlgort").removeAttr("disabled");
		}else{
			$(".selectlgort").attr("disabled","disabled");
		}
		getloc();
	});
	$(".selectlgort").change(function(){
		if($(".selectlgort").val()==""){
			$(".selectwerks").removeAttr("disabled");
			$(".lgort").removeAttr("disabled");
		}else{
			$(".selectwerks").attr("disabled","disabled");
			$(".lgort").attr("disabled","disabled")
		}
		getareainfo();
	})
});