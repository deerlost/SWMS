var indexdata = null,
	basedata = null,
	base = {
		s1: 0,
		s2: 0
	},
	pldata = [],
	strorder="",
	strsort=true;

function baseinit() {
	var url = uriapi + "/conf/position/corp_id_and_corp_name_of_crop_list";
	showloading();
	ajax(url, "GET", null, function(data) {
		layer.close(indexloading);
		$.each(data.body, function(i, item) {
			$(".bukrs").append("<option value='" + item.corp_id + "'>" + item.corp_name + "</option>")
		});
		werks()
	})
}

function werks() {
	$(".werks option:gt(0)").remove();
	var obj = {}
	var corp_id = $(".bukrs").val();
	obj.corp_id = corp_id;
	var url = uriapi + "/auth/get_fty_for_corpid";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		$.each(data.body, function(i, item) {
			$(".werks").append("<option value='" + item.fty_id + "'>"+item.fty_code +"-"+ item.fty_name + "</option>")
		});
	})
}
function lgort() {
	$(".lgort option:gt(0)").remove()
	var obj = {}
	var fty_id = $(".werks").val();
	obj.fty_id = fty_id;
		var url = uriapi + "/auth/get_location_for_ftyid";
		showloading();
		ajax(url, "POST", JSON.stringify(obj), function(data) {
			layer.close(indexloading);
			$.each(data.body, function(i, item) {
				$(".lgort").append("<option value='" + item.location_id + "'data-id='" + item.wh_code + "'>" +item.location_code +"-"+ item.location_name + "</option>")
			});
		})
}

function lgnum() {
	 var url = uriapi + "/conf/position/get_warehouse_list";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body, function(i, item) {
				$(".lgnum").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}

//绑定
function tableBind() {
	$("#id_div_grid").iGrid({
		columns: [{
				th: '仓位编码',
				col: 'position_code',
				type:"popup",
				popup: {
					href: "editcw.html", //默认带index= 数据row下标。
					area: "700px,400px",
					title: "修改",
					args: ["position_id"] //还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
			{
				th: '仓库号',
				col: 'wh_code',
				min: 60
			},
			{
				th: '仓库号描述',
				col: 'wh_name'
			},
			{
				th: '存储区',
				col: 'area_code'
			},
			{
				th: '出库冻结标识',
				col: 'freeze_output_name'
			},
			{
				th: '入库冻结标识',
				col: 'freeze_input_name'
			},
			{
				th: '仓储类型',
				col: 'storage_type_code'
			},
			{
				th: '仓储类型描述',
				col: 'storage_type_name'
			},
		],
		data: indexdata.body,
		rownumbers: false,
		checkbox: true,
		percent: 60, //空占位百分比
		skin: "tablestyle4", //皮肤
		csort: function(a, b) {
			if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
			actionpager();
		},
		GetSelectedData:function(a){
			pldata=a;
		}
	});
}
function printwl(){
	return pldata
}
function clicksubmit(){
	    $(".print").click(function() {
		if(pldata.length==0){
			layer.msg("请选择要打印的数据");
			return false
		}else{
			layer.confirm("是否打印选中数据？", {
	            btn: ['打印', '取消'],
	            icon: 3
	        }, function() {
	            printf();
	            layer.close(parseInt($(".layui-layer-shade").attr("times")));
	        }, function() {
	
	        });
		}
    });
}
//打印功能
function printf() {
	var arr = [];
	var printdata={};
	$.each(pldata,function(i,item){
		arr.push({
			"type":"L3",
			"content":item.wh_code+"-"+item.area_code+"-"+item.position_code,
		})
	})
		var url = uriapi + "/biz/print/cwLabelZpl";
		printdata.print_data=arr;
		ajax(url, 'POST', JSON.stringify(printdata), function(data) {
			setTimeout(function() {
				layer.close(indexloading);
			}, 50);
			window.open(data.body.url,"_blank");
		}, function(e) {
			layer.close(indexloading);
			layer.alert("数据请求失败");
		});
}
// 初始化分页
function initPagination(total, current) {
	$("#J_ListPagination").unbind().pagination({
		total_pages: total,
		current_page: current,
		is_number: true,
		callback: function(event, page) {
			actionpager(page);
		}
	});
}

function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	actionpager()
}
        //存储类型
function getpacktype(){
	var url=uriapi+"/conf/position/select_storage_type_list";
     showloading();
     ajax(url,"get",null,function(data){
        layer.close(indexloading);
	    $.each(data.body,function(i,item){
	         $(".pack_type").append("<option value='"+item.storage_type_id+"'>"+item.storage_type_code+"-"+item.storage_type_name+"</option>")
	          });
         })
 }
//搜索
function actionpager(page) {
	var totalcount=-1
	if(page==0||page==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	var ischecked = true;
	if(ischecked == false) return false
	var obj = {
		location_id:$(".lgort").val(),
		fty_id:$(".werks").val(),
		corp_id:$(".bukrs").val(),
		wh_id: $(".lgnum").val(),
		area_code: $(".lgtyp").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount,
		position_code: $(".lgpla").val(),
		storage_type_id:$(".pack_type").val(),
		sort_column:strorder,
		sort_ascend:strsort
	};
	showloading();
	var url = uriapi + "/conf/position/position_list"
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		$.each(data.body,function(i,item){
			if(item.freeze_output==0){
				item.freeze_output_name="否"
			}else{
				item.freeze_output_name="是"
			}
			if(item.freeze_input==0){
				item.freeze_input_name="否"
			}else{
				item.freeze_input_name="是"
			}
		})
		indexdata = data;
		if(indexdata.body.length > 0) {
			$.cookie("dumptotal", data.head.total)
			initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
		} else {
			$("#J_ListPagination").html("")
		}
		tableBind()
	})
}

$(function() {
	getpacktype();
	lgnum();
	$(".btn_search").click(function() {
		actionpager();
	})
	tableedit($("body"));
	baseinit()
	$(".bukrs").change(function() {
		werks()
	})

	clicksubmit();
	$(".werks").change(function(){
		if($(".werks").val()==""){
			$(".lgnum").removeAttr("disabled");
		}else{
			$(".lgnum").attr("disabled","disabled");
		}
		lgort();
	});
	$(".lgnum").change(function(){
		if($(".lgnum").val()==""){
			$(".werks").removeAttr("disabled");
			$(".lgort").removeAttr("disabled");
		}else{
			$(".werks").attr("disabled","disabled");
			$(".lgort").attr("disabled","disabled");
		}
	})
})