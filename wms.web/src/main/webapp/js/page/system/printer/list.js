var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true,pldata=[];

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '打印机名称', col: 'printer_name'},
			{ th: '打印机ip', col: 'printer_ip' },
			{ th: '打印机类型', col: 'type_name' },
			{ th: '操作', col: 'printer_id', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"edit.html?action=edit",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:[]//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
			{ th: '操作', col: 'class_type_id', class:"", sort:false, min:60,align: 'left',type:"delete",tips:"确定要删除吗"},
		], 
		data: indexdata.body,
		checkbox: false,
		rownumbers:true,
		absolutelyWidth:true,
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
		delete:function(a,b){//删除时调用
				console.log(a);//当前的对象
				console.log(b);//索引
				showloading();
				var url=uriapi+"/conf/printer/delete_printer?printer_id="+a.printer_id;
			 	ajax(url,"get",null,function(data){
		        layer.close(indexloading);
				if(data.head.status==true){
					location.href="list.html"
				}
		    })
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
function showPage(){
    $.cookie("pageSize"+GetQueryString("key"),$(".showPage").val())
    actionpager()
}
function getinfo(index){
	return indexdata.body[index];
}
function actionpager(pageindex) {
	var totalcount=pageindex?indexdata.head.total:-1
    showloading();
    var url = uriapi + '/conf/printer/get_dic_printer_list';
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
        layer.alert("数据请求失败");
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