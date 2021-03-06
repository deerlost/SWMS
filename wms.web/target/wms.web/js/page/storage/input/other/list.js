﻿var indexdata = [],kw="",strorder="", strsort=true;

function databindTable() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '入库单号', col: 'stock_input_code', type:"link",
                link:{
                    href:"detail.html",
                    autocreate:false,
                    args:["stock_input_id|id"]
                }
            },
            { th: '入库类型', col: 'receipt_type_name'},
            { th: '移动类型', col: ["move_type_code","move_type_name"],sort:false},
            { th: '接收工厂', col: 'fty_name'},
            { th: '接收库存地点', col: ['location_code','location_name'] },
            { th: '创建人', col: 'create_user_name' },
            { th: '创建日期', col: 'create_time' },
            { th: '状态', col: 'status_name', sort:false }
        ],
        data: indexdata.body,
        percent:30,
        skin:"tablestyle4",
        rownumbers:false,
        csort:function(a,b){
		if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
			actionpager();
        }
    });
}

// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}

function actionpager(pageindex) {
    var status_list=[]
    var totalcount=pageindex?indexdata.head.total:-1
    $(".chkbox input:checked").each(function(){
        status_list.push($(this).val())
    })
    if(status_list.length==0){
        indexdata.body=[]
        $("#J_ListPagination").html("")
        databindTable();
        return false
    }
    showloading();
    var url = uriapi + '/biz/input/other/other_input_list';
    var obj={
        page_index: pageindex||1,
        page_size: 20,
        total: totalcount,
        condition:kw,
        status_list:status_list,
        sort_column:strorder,
		sort_ascend:strsort
    }
    ajax(url, 'post', JSON.stringify(obj), function (data) {
        if(data.head.status==true){
            indexdata = data;
            if (indexdata.body.length>0) {
                initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
            }else{
                $("#J_ListPagination").html("")
            }
            databindTable();
            setTimeout(function () { layer.close(indexloading); }, 50);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}


$(function () {
    actionpager();
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            kw=$("input.search").val()
            actionpager();
        }
    });
    $("a.search").click(function () {
        kw=$("input.search").val()
        actionpager();
    });
    $(":checkbox").change(function () {
        actionpager();
    });
});