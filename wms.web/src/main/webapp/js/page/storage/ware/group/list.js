var indexdata = [],kw="",strorder="", strsort=true;

function databindTable() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '组盘作业单号', col: 'stock_task_code', type:"link",
                link:{
                    href:"detail.html",
                    autocreate:false,
                    args:["stock_task_id|id"]
                }
            } ,
            { th: '托盘号', col: 'pallet_code'},
            { th: '库存地点', col: ['location_code','location_name']},
            { th: '存储区', col: 'area_name' },
            { th: '仓位信息', col: 'position_code' },
            { th: '创建人', col: 'create_user' },
            { th: '创建日期', col: 'create_time' },
            { th: '状态', col: 'status_name', sort:false }
        ],
        data: indexdata.body,
        percent:40,
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

    if($(".chkbox input:checked").val()==1 || $(".chkbox input:checked").val()==2){
        $("td [colnum='1']").hide()
    }else{
        $("td [colnum='1']").show()
    }

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
    showloading();
    var url = uriapi + '/biz/group_task/head_list';
    var obj={
        page_index: pageindex||1,
        page_size: 20,
        total: totalcount,
        condition:kw,
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
    $(":radio").change(function () {
        actionpager();
    });
    $("label :radio").change(function() {
        $("label").removeClass("on");
        $(this).parent().addClass("on");
    });
});