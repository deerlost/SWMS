var indexdata = [],kw="",strorder="", strsort=true,g=null;

function databindTable() {
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '叉车工', col: 'person_name',width:150},                                       //0
            { th: '搬运工', col: 'person_name',width:150},                                       //1
            { th: '理货员', col: 'person_name',width:150},                                       //2
            { th: '仓库描述', col: 'wh_name',width:200},                                         //3
            { th: '司机', col: 'driver_name',width:150},                                //4
            { th: '车辆', col: 'vehicle_name',width:150},                                   //5
            { th: '产品线', col: 'product_line_name',width:200},                                 //6
            { th: '操作', col: 'wh_name',type:'delete',tips:"确定删除吗？"},
        ],
        data: indexdata.body,
        percent:50,
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
        },
        loadcomplete:function(a){//页面绘制完成，返回所有数据。
            g.displaycolumn([0,1,2,3,4,5,6],false)
            switch($(".chkbox input:checked").val()){
                case "1":g.displaycolumn([0,3],true);break;
                case "2":g.displaycolumn([1,3],true);break;
                case "3":g.displaycolumn([2,3],true);break;
                case "4":g.displaycolumn([4,6],true);break;
                case "5":g.displaycolumn([5,6],true);break;
            }
        },
        delete:function(a,b){//删除时调用
            console.log(a);//当前的对象
            console.log(b);//索引
            delData(a)
        }
    });
}

//删除
function delData(data){
    var url=uriapi+"/conf/personnel/delete_person?type="+data.type+"&id="+data.id
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            actionpager()
        }else{
            layer.close(indexloading)
            }
        },function(e) {
        layer.close(indexloading);
    },true)
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
    showloading();
    var url = uriapi + '/conf/personnel/query_person';
    var obj={
        page_index: pageindex||1,
        page_size: 20,
        total: totalcount,
        keyword:kw,
        type:$(".searchstyle1 [type='radio']:checked").val(),
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
    tableedit($("body"))
    databindTable()
    g.displaycolumn([1,2,4,5,6],false)
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
        var type=$(this).val()
        $(".searchstyle3 a").attr("data-href","detail.html?type="+type)
        $("label").removeClass("on");
        $(this).parent().addClass("on");
        actionpager();
    });
});