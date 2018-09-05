var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true,pldata=[];

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '班次名', col: 'class_type_name'},
			{ th: '开始时间', col: 'start_time' },
			{ th: '结束时间', col: 'end_time' },
			{ th: '操作', col: 'class_type_id', class:"", sort:false, min:60,align: 'left',type:"popup",
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
		checkbox: false,
		rownumbers:true,
		selectedrow:{
			args:["class_type_id"],
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
			actionpager=a;
			actionpager();
		},
		GetSelectedData(a){
			pldata=a;
			}
	   });
}
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}
function alldata(){
	var newarr= new Array();
	$.each(indexdata.body,function(i,item){
		newarr.push({
			"start_time":item.start_time,
			"end_time":item.end_time
		})
	})
	return newarr
}
function showPage(){
    $.cookie("pageSize"+GetQueryString("key"),$(".showPage").val())
    actionpager()
}
function getinfo(index){
	return indexdata.body[index];
}
function actionpager(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
    showloading();
    var url = uriapi + '/conf/class_type/get_dic_class_type_list';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: totalcount,
        condition:kw,
        sort_column:strorder,
		sort_ascend:strsort
    }
    ajax(url, 'post', JSON.stringify(obj), function (data) {
        indexdata = data;
        if (indexdata.body.length>0) {
            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
        }else{
            $("#J_ListPagination").html("");
        }
        dataBind();
        setTimeout(function () { layer.close(indexloading); }, 50);
    }, function (e) {
        layer.close(indexloading);
    });
}
$(function () {
    actionpager();
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            kw=$("input.search").val()
            actionpager(0);
        }
    });
    $("a.search").click(function () {
        kw=$("input.search").val()
        actionpager();
    });
    tableedit($("body"))
});