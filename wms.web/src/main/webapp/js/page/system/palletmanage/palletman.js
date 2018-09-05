var indexdata = null,
	totalcount = -1,
	pldata = [],
	strorder="",
	strsort=true;
function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '	托盘号', col: 'pallet_code', class:"", min:60,align: 'left',type:"popup",
			popup:{
					href:"palletview.html",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"查看",
				}
		},
			{ th: '是否冻结', col: 'frozen', min: 60,sort:false},
			{ th: '最大承重', col: 'max_weight', class:"", align: 'right',type:"number" },
			{ th: '操作', col: 'pallet_name', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"modifytray.html",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:['pallet_name']//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		checkbox: true,
		rownumbers:false,
		selectedrow:{
			args:["pallet_code","wh_id","wh_name","pallet_name","max_weight","freeze","freeze_id","pallet_id","unit_weight","status"],
			isHasindex:true
		},
		percent:50,//空占位百分比
		skin:"tablestyle4",//皮肤
		csort:function(a,b){
			if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
			search();
		},
		GetSelectedData(a){
		console.log(a);
			pldata=a;
			}
	   });
}

function pledit() {
	return pldata
}

function datainfo(index) {
	return indexdata.body[index];
}
function printwl(){
	return pldata
}
function search(page) {
	 var totalcount=-1
 if(page==0||page==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	showloading();
	var url = (uriapi + '/conf/pallet/list_pallet');
	var jsondata = {
		condition: $("input.search").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}
	ajax(url, 'POST', JSON.stringify(jsondata), function(data) {
		$.each(data.body, function(i, item) {
			if(item.freeze == 0) {
				item.frozen = "非冻结";
			} else if(item.freeze == 1) {
				item.frozen = "冻结";
			}
		});
		indexdata = data;
		dataBind();
		if(!page) {
			if(data.body != null) {
				if(data.body.length > 0) {
					totalcount = data.head.total;
					initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
				} else {
					$("#J_ListPagination").html("");
				}
			} else
				$("#J_ListPagination").html("");
		}
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
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
			search(page);
		}
	});
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
	var printdata = {};
	$.each(pldata,function(i,item){
		arr.push({
			"type":"L1",
			"content":item.pallet_code,
		})
	})
		printdata.print_data=arr
		var url = uriapi + "/biz/print/cwLabelZpl";
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
function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	search()
}

$(function() {
	clicksubmit();
	search();
	$("input.search").keyup(function(e) {
		var keycode = e.which;
		if(keycode == 13) {
			totalcount = -1
			search();
		}
	});
	$("a.search").click(function() {
		totalcount = -1
		search();
	});
	tableedit($("body"));
});