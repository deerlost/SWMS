var indexdata = null,
	totalcount = -1,
	strorder="",
	strsort=true;

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '部门id', col: 'department_id', min: 60 },
			{ th: '部门名称', col: 'name', min: 60 },
			{ th: '工厂名称', col: 'fty_name' },
			{ th: '操作', col: 'factory', class:"", min:60,align: 'left',type:"popup",sort:false,
			popup:{
					display:"修改",
					href:"addfac.html",//默认带index= 数据row下标。
					area:"500px,350px",
					title:"修改",
					args:['factory']//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
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

function indexshju(index){
	return indexdata.body[index];
}
function search(page) {
	var haspage = page || -1;
	showloading();
	var url = uriapi + '/conf/approvedept/list_approve_dept';
	var jsondata = {
		condition: $("input.search").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 10,
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