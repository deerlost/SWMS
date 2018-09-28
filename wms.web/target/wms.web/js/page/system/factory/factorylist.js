var indexdata = null,
	totalcount = -1,
	strorder="",
	strsort=true;
function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '工厂', col: 'fty_code' },
			{ th: '工厂名称', col: 'fty_name' },
			{ th: '工厂地址', col: 'address' },
			{ th: '创建日期', col: 'create_time' },
			{ th: '所属公司', col: 'corp_code' },
			{ th: '公司名称', col: 'corp_name' },
			{ th: '操作', col: 'fty_id', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"editfactory.html?action=modify",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:['fty_id']//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		rownumbers:false,
		percent:60,//空占位百分比
		skin:"tablestyle4",//皮肤
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
function search(page) {
	var haspage = page || -1;
	showloading();
	var url = uriapi + '/conf/fty/factory_list';
	var jsondata = {
		condition: $("input.search").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}
	ajax(url, 'POST', JSON.stringify(jsondata) , function(data) {
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

function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	search()
}

$(function() {
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