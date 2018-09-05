var indexdata ={},
	strorder = "",
	strsort = true,
	jsondata = {},
	status_list=[],g=null;
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}

function returnOption1(value, value1, text) {
	return "<option value='" + value + "' data-wh_id='" + value1 + "'>" + text + "</option>";
}
//获取仓库号下拉数据
function getwhlist(){
 var url = uriapi + "/biz/stquery/get_warehouse_list";
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
	var url = uriapi + "/biz/stquery/baseinfo_byuser";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body.fty_list, function(i, item) {
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
	$.each(kcdddata.body.fty_list[index].location_ary, function(i, item) {
			$(".selectlgort").append(returnOption1(item.loc_id, item.wh_id, item.loc_code+"-"+item.loc_name));
		})
	}	
}
function files() {
	$("[name='check_date']").val($("#time").val());
	$("[name='date_begin']").val($("#day").val());
	$("[name='date_end']").val($("#day_to").val());
	$("[name='fty_id']").val($(".selectwerks").val());
	$("[name='wh_id']").val($(".selectwh").val());//仓库号
	$("[name='location_id']").val($(".selectlgort").val())
	$("#download").attr("action", uriapi + "/biz/stquery/download_stock_days");
	$("#download").submit();
}
function bindata(){
	if(g==null){
		g=$("#id_div_grid").iGrid({
			columns: [
				{ th: '查询日期', col: 'check_date'},
				{ th: '物料编码', col: 'mat_code', min:200},
				{ th: '物料描述', col: 'mat_name', min:200 },
				{ th: '计量单位', col: 'name_zh'},
				{ th: '工厂', col: 'fty_code_name'},
				{ th: '库存地点', col: 'loc_code_name'},
				{ th: '生产批次', col: 'product_batch'},
				{ th: '入库时间', col: 'input_date'},
				{ th: '库龄(天)', col: 'stcok_days'},
				{ th: '库存数量', col: 'qty',type:"number"},
				{ th: '移动平均价', col: 'move_average_sum'},
				{ th: '库存金额', col: 'stock_sum'},
			],
			data:null,
			checkbox: false,
			absolutelyWidth:true,
			selectedrow:{
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
	}else{
		g.showdata(indexdata.body);
	}
}
function search(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total;
	}
	var isChecked=true
	if($("#day").val()==""){
		 layersMoretips("必填项",$("#day"));
		isChecked=false;
	}
	if($("#day_to").val()==""){
		 layersMoretips("必填项",$("#day_to"));
		isChecked=false;
	}
	if($("#day").val()!=""&&$("#day_to").val()!=""){
		if(parseInt($("#day").val())>parseInt($("#day_to").val())){
			layersMoretips("库龄开始天数不能大于结束天数",$("#day_to"));
		}
	}
	var param = {
		check_date:$("#time").val(),//查询日期
		date_begin:$("#day").val(),//库龄开始时间
		date_end:$("#day_to").val(),//库龄结束时间
		fty_id:$(".selectwerks").val(), // 工厂
		wh_id:$(".selectwh").val(),//仓库号
		location_id: $(".selectlgort").val(), // 库存地点
		sort_column:strorder,
		sort_ascend:strsort,
		page_index: pageindex||1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	if(!isChecked){
		return false;
	}
	var url = uriapi + "/biz/stquery/serach_days_stock";
	showloading();
	ajax(url, 'post', JSON.stringify(param), function(data) {
		layer.close(indexloading);
		if(data.body.length > 0) {
			$.each(data.body,function(i,item){
				item.loc_code_name=item.location_code+"-"+item.location_name;
				item.fty_code_name=item.fty_code+"-"+item.fty_name;
			})
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
	bindata();
	var mydate=new Date()
	$("#time").val(mydate.getFullYear()+"-"+(mydate.getMonth()+1)+"-"+mydate.getDate())
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