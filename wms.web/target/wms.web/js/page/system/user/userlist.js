var indexdata = [], pagecount = 20,kw="", strorder="", strsort=true;

function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
            { th: '用户', col: 'user_id'},
            { th: '用户姓名', col: 'user_name' },
            { th: '所属部门', col: 'department_name'},
            { th: '创建日期', col: 'create_time' },
            { th: '所属工厂', col: 'fty_code' },
            { th: '工厂名称', col: 'fty_name' },
            { th: '所属公司', col: 'corp_code' },
            { th: '公司名称', col: 'corp_name' },
            {th: ' ', col: 'stock_input_code', type:"link",
                link:{
                    href:"edituser.html",
                    args:["user_id|no"],
                    autocreate:false,
                    display:"操作"
                }
            }
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
    $(".chkbox input:checked").each(function(){
        status_list.push($(this).val())
    })
    showloading();
    var url = uriapi + '/auth/get_users';
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

    $(":checkbox").change(function () {
        indexdata.head.total=""
        actionpager(0);
    });
});