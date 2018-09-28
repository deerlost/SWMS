var indexdata = null,
	totalcount = -1,
	fty_info=[],
	strorder="",
	strsort=true;

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '库存地点', col: 'location_code', min: 60 },
			{ th: '库存地点描述', col: 'location_name', min: 60 },
			{ th: '库存地点地址', col: 'location_address' },
			{ th: '工厂', col: 'fty_code', min: 60 },
			{ th: '工厂名称', col: 'fty_name', min: 60 },
			{ th: '工厂地址', col: 'fty_address' },
			{ th: '所属公司', col: 'corp_code', min: 60 },
			{ th: '公司名称', col: 'corp_name', min: 60 },
			{ th: '操作', col: 'location_id', class:"", min:60,align: 'left',type:"button",sort:false,
			button:{
					text:"添加",
					class:"",//默认带index= 数据row下标。
					//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		rownumbers:false,
		percent:60,//空占位百分比
		skin:"tablestyle4",//皮肤
		clickbutton:function(a,b,c){//Button点击后触发
//					console.log(a); //返回的字段名
//					console.log(b); //行的索引
//					console.log(c);//当前的对象
					var istrue=true;
					if(fty_info.length>0){
						$.each(fty_info,function(i,item){
							if(item.location_id==c.location_id){
								istrue=false;
							}
						})
						if(istrue==true){
							parent.appendinfo(c);
						}else{
							layer.msg("已经选过该条数据")
						}
					}else{
						parent.appendinfo(c);
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
function search(page) {
	var haspage = page || -1;
	showloading();
	var url = uriapi + '/conf/wh/location_list';
	var jsondata = {
		condition: $("input.search").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 10,
		total: totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}
	ajax(url, 'POST', JSON.stringify(jsondata) , function(data) {
		var indexs=[];
		indexdata=data;
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
	fty_info=parent.fty_infos();
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