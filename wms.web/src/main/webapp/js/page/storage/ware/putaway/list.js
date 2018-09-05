var indexdata = [],kw="",strorder="", strsort=true;

function databindTable() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '作业请求号', col: 'stock_task_req_code', type:"link",
                link:{
                    href:"detail.html",
                    autocreate:false,
                    args:["stock_task_req_id|id"]
                }
            } ,
            {th: '作业单号', col: 'stock_task_code', type:"link",
                link:{
                    href:"view.html",
                    autocreate:false,
                    args:["stock_task_id|id"]
                }
            } ,
            { th: '接收工厂', col: 'fty_name'},
            { th: '接收库存地点', col: ['location_code','location_name']},
            { th: '单据类型', col: 'type_name' },
            {th: '单据编号', col: 'refer_receipt_code', type:"link",
                link:{
                    href:false,
                    autocreate:false,
                    args:["refer_receipt_id|id"],
                    column:"refer_receipt_type",
                    links:[
                        {value:11,href:"../../pack/order/detail.html"},
                        {value:12,href:"../../input/production/detail.html"},
                        {value:13,href:"../../input/other/detail.html"},
                        {value:14,href:"../../direct/vproductioninput/detail.html"},
                        {value:15,href:"../../direct/productioninput/detail.html"},
                        {value:16,href:"../../specificword/productioninput/detail.html"},
                        {value:17,href:"../../specificrecord/productioninput/detail.html"},
                        {value:21,href:"../../ware/invoice/detail.html"},
                        {value:22,href:"../../output/sell/detail.html"},
                        {value:23,href:"../../output/scrap/detail.html"},
                        {value:24,href:"../../output/other/detail.html"},
                        {value:25,href:"../../output/production/detail.html"},
                        {value:26,href:"../../specificword/selloutput/detail.html"},
                        {value:27,href:"../../specificword/selloutput/detail.html"},
                        {value:28,href:"../../specificrecord/output/detail.html"},
                        {value:31,href:"../../ware/putaway/detail.html"},
                        {value:32,href:"../../ware/soldout/detail.html"},
                        {value:33,href:"../../ware/putaway/view.html"},
                        {value:34,href:"../../ware/soldout/view.html"},
                        {value:35,href:"../../ware/group/detail.html"},
                        {value:36,href:"../../ware/ware/detail.html"},
                        {value:37,href:"../../ware/ware/detail.html"},
                        {value:38,href:"../../ware/ware/detail.html"},
                        {value:39,href:"../../ware/ware/detail.html"},
                        {value:41,href:"../../input/dump/detail.html"},
                        {value:42,href:"../../direct/vdumpinput/detail.html"},
                        {value:43,href:"../../specificword/dump/detail.html"},
                        {value:44,href:"../../specificrecord/dump/detail.html"},
                        {value:51,href:"../../dump/materiel/detail.html"},
                        {value:52,href:"../../dump/materiel/detail.html"},
                        {value:53,href:"../../dump/materiel/detail.html"},
                        {value:54,href:"../../dump/production/detail.html"},
                        {value:55,href:"../../specificword/productionrevolve/detail.html"},
                        {value:62,href:"../../return/detail.html"}
                    ]

                }
            } ,
            { th: '创建人', col: 'create_user' },
            { th: '创建日期', col: 'create_time' },
            { th: '状态', col: 'status_name', sort:false },
            { th: '司机', col: 'delivery_driver'},
            { th: '车辆', col: 'delivery_vehicle'}
        ],
        data: indexdata.body,
        percent:10,
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
    var url = uriapi + '/biz/task/shelves/biz_stock_task_req_head_list';
    var obj={
        page_index: pageindex||1,
        page_size: 20,
        total: totalcount,
        condition:kw,
        choose_type:status_list.join(","),
        receipt_type: "31",
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