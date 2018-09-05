var indexdata = null,
	totalcount = -1,
	strorder="",
	strsort=true;

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '物料编码', col: 'mat_code', min: 60 },
			{ th: '物料描述', col: 'mat_name', min: 60 },
			{ th: '库计量单位', col: 'name_zh' },
			{ th: '物料组', col: 'mat_group_code' },
			{ th: '物料组描述', col: 'mat_group_name' },
			{ th: '长度', col: 'length' },
			{ th: '宽度', col: 'width' },
			{ th: '高度', col: 'height' },
			{ th: '毛重', col: 'gross_weight' },
			{ th: '净重', col: 'net_weight' },
			{ th: '体积', col: 'volume' },
			{ th: '危险品标识', col: 'is_danger' },
			{ th: '保质期(天)', col: 'shelf_life' },
			{ th: '样品名称', col: 'samp_name' },
			{ th: '样品规格', col: 'specification' },
			{ th: '执行标准', col: 'standard' },
			{ th: '操作', col: 'shelf_life', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"edit.html?action=edit",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:[]//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		rownumbers:false,
		absolutelyWidth:true,
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
function getwumsinfo(index){
	return indexdata.body[index]
}
function search(page) {
	var haspage = page || -1;
	showloading();
	var url = uriapi + '/conf/mat/mat_major_data';
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
//物料同步
function sync(){
    var sync=$(".sync").val();
    if(sync==""){
        layersMoretips("请填写物料单号",$(".sync"));
        return false
    }
    showloading()
    var url=uriapi+"/conf/mat/sync_mat?mat_code="+sync

    ajax(url,"GET",null,function(data){
        console.log(data)
        if(data.body.count=="0"){
            layer.msg("没有相关物料变更信息")
        }
        if(data.body.count>"0"){
            layer.msg("同步成功")
        }
        layer.close(indexloading)
    })
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
	$(".btn_tb").click(function(){
		sync();
	})
});

