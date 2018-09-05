var indexdata = {},
	strorder = "",
	strsort = true,
	jsondata = {},
	g1=null,
	status_list = [],
	cjinfo=[];
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function initData() {
	var url = uriapi + "/conf/serial_package/get_dic_data_list";
	ajax(url, 'GET', null, function(data) {
		$.each(data.body, function(i, item) {
				$(".packtype").append(returnOption(item.classificat_id,item.classificat_name))
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function files() {
	$("[name='start_create_time']").val($(".txtstartdate").val());//物料编码
	$("[name='end_create_time']").val($(".txtenddate").val());//物料描述
	$("[name='package_code']").val($(".packsn").val());
	$("[name='classificat_id']").val($(".packtype").val());
	$("#download").attr("action", uriapi + "/conf/serial_package/download_package_data");
	$("#download").submit();
}
function bindata() {
	g1=$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '包装物SN', col: 'package_code', min: 60},
			{ th: '包装物分类', col: 'classificat_name'},
			{ th: '包装物类型', col: 'package_type_code'},
			{ th: '状态', col: 'status_name', min: 60 },
			{ th: '供应商名称', col: 'supplier_name'},
			{ th: '创建日期', col: 'create_time'}, 
		], 
		data: indexdata.body,
		checkbox:true,
		resizehead:true,
		absolutelyWidth:true,
		percent:50,//空占位百分比
		skin:"tablestyle4",//皮肤
		GetSelectedData(a) {
		   cjinfo = a;
		},
		loadcomplete:function(a){//页面绘制完成，返回所有数据。
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
function clicksubmit(){
	    $(".print").click(function() {
		if(cjinfo.length==0){
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
	$.each(cjinfo,function(i,item){
		arr.push({
			"type":"L2",
			"content":item.package_code,
		})
	})
		var url = uriapi + "/biz/print/cwLabelZpl";
		printdata.print_data=arr;
		ajax(url, 'POST', JSON.stringify(printdata), function(data) {
			setTimeout(function() {
				layer.close(indexloading);
			}, 50);
			window.open(data.body.url);
		}, function(e) {
			layer.close(indexloading);
			layer.alert("数据请求失败");
		});
}
//批量删除
function delTabel(){
    layer.confirm("是否删除？", {
        btn: ['删除', '取消'],
        icon: 3
    }, function(index) {
    	layer.close(index);
    	var obj={};
    	var index=false;
    	var arr=[];
		$.each(cjinfo,function(i,item){
			arr.push(item.package_id)
			if(item.status!=0){
				index=true
			}
			if(index==true){
				return false
			}
		})
		if(index==true){
			layer.msg("状态为Y-激活的数据不可以删除,请重新选择")
			return false
		}else{
			obj.ids=arr;
			var url = uriapi + "/conf/serial_package/delete_serial_package";
			ajax(url, 'POST', JSON.stringify(obj), function(data) {
				if(data.head.status==true){
					location.href="list.html"
				}else{
					layer.close(indexloading);	
				}
			}, function(e) {
				layer.close(indexloading);
			});
		}
    }, function() {
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
		start_create_time:$(".txtstartdate").val(),
		end_create_time:$(".txtenddate").val(),
		package_code:$(".packsn").val(),
		classificat_id:$(".packtype").val(),
		sort_column: strorder,
		sort_ascend: strsort,
		page_index: pageindex || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount
	};
	var url = uriapi + "/conf/serial_package/get_serial_package_list";
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
			$.cookie("stocktotal", data.head.total);
			$(".delet").show();
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
	initData();
	bindDatePicker();
	checkDateDifference('txtstartdate', 'txtenddate');
	indexdata.head = [];
	indexdata.head.total = "";
	search();
	tableedit($("body"));
	$(".btn_search").click(function() {
		search();
	})
	$(".btn_files").click(function() {
		files();
	})
	$(".selectwerks").change(function(){
		getlocinfo();
	})
	 $(".delet").click(function(){
        delTabel();
     })
	 clicksubmit();
});