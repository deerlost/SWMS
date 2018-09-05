var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true;

function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '销售退货单号', col: 'return_code', type:"link",
                link:{
                    href:"./detail.html",
                    autocreate:false,
                    args:["return_id|id"]
                }
            } ,
            { th: '交货单号', col: 'refer_receipt_code'},
            { th: '客户名称', col: 'customer_name' },
            { th: 'ERP凭证', col: 'mat_doc_code'},
            { th: 'MES凭证', col: 'mes_doc_code' },
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
        callback: function(event, page) {
            actionpager(page);
        }
    });
}
function actionpager(pageindex) {
	var totalcount=pageindex?indexdata.head.total:-1
    var status_list=[]
    $(".chkbox input:checked").each(function(i,item){
        status_list.push($(this).val())
    })
    showloading();
    var url = uriapi + '/biz/return/sale/get_return_list';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: totalcount,
        condition:kw,
        status:status_list.join(","),
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
    $(".chkbox input").change(function(){
    	actionpager();
    })
    tableedit($("body"))
});