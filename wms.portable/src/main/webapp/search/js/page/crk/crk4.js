var indexdata={},selectdata=null,base_obj=null

//加载下拉
function getDataSelect(){
    var url=uriapi+"/biz/query/stock_analyse/base_info"
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        if(data.head.status){
            selectdata=data.body;
            dataBindSelect()
            dataBindURL()
        }
    },function(e){
    })
}

//加载概览
function getDataView(){
    showloading()
    var url=uriapi+"/biz/query/stock_analyse/get_mat_detial";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata=data.body
            dataBindTable()
        }
    },function(e){})
}

//绑定url回调信息
function dataBindURL(){
    if(base_obj.type==1){
        $("[datatype='1']").addClass("on")
    }else{
        $("[datatype='2']").addClass("on")
    }
    $.each(selectdata.board_list,function(i,item1){
        $.each(item1.corp_list,function(j,item2){
            $.each(item2.warehouse_list,function(t,item3){
                if(item2.corp_id==base_obj.corp_id && item3.wh_id==base_obj.wh_id && item1.board_id==base_obj.board_id){
                    $(".wh p").html(item3.wh_name)
                    return false
                }
            })
        })
    })
}

//绑定仓位下拉
function dataBindSelect(){
   $("#start_date").val(base_obj.date)
    var warehouse_list=""
    $.each(selectdata.board_list,function(i,item1){
        if(item1.board_id==base_obj.board_id){
            $.each(item1.corp_list,function(j,item2){
                if(item2.corp_id==base_obj.corp_id){
                    warehouse_list=item2.warehouse_list
                    return false
                }
            })
            return false
        }
    })

    $(".fbox ul").append("<li data-id=''>全部库房</li>")
    $.each(warehouse_list,function(i,item){
        $(".fbox ul").append("<li data-id='"+item.wh_id+"'>"+item.wh_name+"</li>")
    })

    $(".fbox .banner li").click(function(){
        base_obj.wh_id=$(this).attr("data-id")
        $(".wh p").html($(this).html())
        $(".fbox").toggle();
        getDataView()
    })
    getDataView()
}

//绑定表格
function dataBindTable(){
    var type=$(".tititib .on").attr("data-value")
    var unit=""
    if(base_obj.type==1){
    	unit="万元"
    }else{
    	unit="KG"
    }
    if(type==1){
        var data=indexdata.output_mat_list
        $(".tablestyle1 th").eq(2).html("出库量")
    }else{
        var data=indexdata.input_mat_list
        $(".tablestyle1 th").eq(2).html("入库量")
    }


    $(".tablestyle1 tr:gt(0)").remove()
    $.each(data,function(i,item){
        var str=""
        str+="<tr>"
        str+="<td align='left'>"+(i+1)+"</td>"
        str+="<td align='left'>"+item.name+"</td>"
        str+="<td align='right' style='color:#5599EE'><b>"+item.value+unit+"</b></td>"
        $(".tablestyle1").append(str)
    })
    dataIsNull($(".tablestyle1"))
}

//日期
function bindDatePicker1(){
    var data=new Date()
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#start_date', //标签id
        'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
        'minDate':'2018-8-1',//最小日期 注意：该值会覆盖标签内定义的日期范围
        'maxDate':data.getFullYear()+"-"+(data.getMonth()+1)+"-"+data.getDate(),//最大日期 注意：该值会覆盖标签内定义的日期范围
    })
}

function callback(date){
    base_obj.date=date
    getDataView()
}

$(function(){
    base_obj=parseQueryString()
    base_obj.type=base_obj.type||1
    bindDatePicker1()
    getDataSelect()

    $('.back').click(function(){
        base_obj.wh_id=""
        window.location.href = "crk3.html?"+parseParam(base_obj);
    })
    $(".wh").click(function(){
        $(".fbox").toggle();
        var height=$(".banner").height()
        $(".banner").css("margin-top",-(height/2)+"px")
    })

    $(".banner img").click(function(){
        $(".fbox").toggle();
    })

    $('.type').click(function(){
        $("span.on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })
    $(".tititib a").click(function(){
        $(".tititib a").removeClass("on")
        $(this).addClass("on")
        dataBindTable()
    })

})