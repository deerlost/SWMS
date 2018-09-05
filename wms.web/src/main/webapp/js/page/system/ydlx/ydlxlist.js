var indexdata = [],datastatus = { "s1": 1, "s2": 0}, pagecount = 10,kw="", strorder="", strsort=true;
function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
            {th: '移动类型', col: 'move_type_code'} ,
            { th: '移动类型描述', col: 'move_type_name' },
            { th: '业务类型', col: 'biz_type'},
            { th: '	业务类型描述', col: 'biz_type_name'},
            {
                th: '', col: 'zshdztms', sort: false, type: "popup",
                popup: {
                    display: "修改",
                    href: "editydlx.html?action=modify",
                    area: "700px,400px",
                    title: "备注",
                    args: ["move_type_code","spec_stock"]
                }
            }
        ],
        data: indexdata.body,
        percent:50,//空占位百分比
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
    showloading();
    var url = uriapi + '/conf/movetype/list_move_type';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: indexdata.head.total||-1,
        keyword:kw,
        sort_column:strorder,
		sort_ascend:strsort
    }
    ajax(url, 'post', JSON.stringify(obj), function (data) {
        indexdata = data;
        if (indexdata.body.length>0) {
            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
        }else{
            $("#J_ListPagination").html("")
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
    tableedit($("body"));

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
});