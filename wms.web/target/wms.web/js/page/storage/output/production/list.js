var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true;

function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '生产直发出库单', col: 'stock_output_code', type:"link",
                link:{
                    href:"./detail.html",
                    args:["stock_output_id|id"]
                }
            } ,
            { th: '单据类型', col: 'order_type_name' },
            { th: '交货单号', col: 'refer_receipt_code'},
            { th: '客户名称', col: 'receive_name' },
            { th: '出库ERP凭证', col: 'mat_doc_code' },
            { th: '出库MES凭证', col: 'mes_doc_code' },
            { th: '创建人', col: 'create_user' },
            { th: '创建日期', col: 'create_time' },
            { th: '状态', col: 'status_name', sort:false }//送货单状态  0 草稿 1未验收2部分验收3已完成
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
            actionpager();
        }
    });
}
// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
        is_number:true,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}

function showPage(){
    $.cookie("pageSize"+GetQueryString("key"),$(".showPage").val())
    actionpager()
}

function actionpager(pageindex) {
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
    var url = uriapi + '/biz/output/straigth/get_order_list';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: indexdata.head.total||-1,
        keyword:kw,
        status:status_list,
        sort_column:strorder,
        receipt_type:25,
        receipt_type_list:[],
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
    indexdata.head=[];
    indexdata.head.total=""
    actionpager(0);
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            indexdata.head.total=""
            kw=$("input.search").val()
            actionpager(0);
        }
    });
    $("a.search").click(function () {
        indexdata.head.total=""
        kw=$("input.search").val()
        actionpager(0);
    });
    $(".chkbox input").change(function(){
    	actionpager(0);
    })
    tableedit($("body"))
});