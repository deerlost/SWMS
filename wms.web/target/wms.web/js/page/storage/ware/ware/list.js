var indexdata = {}, pagecount = 10,totalcount=-1,strorder="",strsort=true;

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{th: '作业单号', col: 'stock_task_code', align: 'left',class:"",type:"link",
				link:{
					href:"detail.html",
					args:["stock_task_id|id"],
					autocreate:false
				}
			} ,
			{ th: '库存地点', col: ['location_code','location_name'], min: 60 },
			{ th: '业务类型', col: 'receipt_type_name',sort:false },
			{ th: '操作人', col: 'user_name'},
			{ th: '创建日期', col: 'create_time'},
			{ th: '状态', col: 'status_name'}
		],
		data: indexdata.body,
		percent:30,//空占位百分比
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
function search(pageindex) {
	var totalcount=pageindex?indexdata.head.total:-1
    var status_list=[]
    $(".chkbox input:checked").each(function(i,item){
        status_list.push($(this).val())
    })
    if(status_list.length==0){
        indexdata.body=[]
        $("#J_ListPagination").html("")
        dataBind();
        return false
    }
	showloading();
	var url = (uriapi + '/biz/task/get_order_list');
	var jsondata={
		condition:$("input.search").val(),
		status:status_list.join(","),
		page_index:pageindex||1,
		page_size:20,
		total:totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}

	ajax(url, 'POST', JSON.stringify(jsondata), function (data) {
		indexdata = data;
		if (!pageindex) {
			if(data.body.length>0){
				initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
			}else
				$("#J_ListPagination").html("");
		}
		 dataBind();
		setTimeout(function () { layer.close(indexloading); }, 50);
	}, function (e) {
		layer.close(indexloading);
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

function showPage(){
    $.cookie("pageSize"+GetURLKey(),$(".showPage").val())
    search()
}

$(function () {
	indexdata.body=null;
	dataBind();
	search();

	$(":checkbox").change(function () {
		search();
	});

	$("input.search").keyup(function (e) {
		var keycode = e.which;
		if (keycode == 13) {
			search();
		}
	});
	$("a.search").click(function () {
		search();
	});
});