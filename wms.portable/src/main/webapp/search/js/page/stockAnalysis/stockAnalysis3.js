var indexdata={},base_obj=null

//加载概览
function getDataView(){
    showloading()
    var url=uriapi+"/conf/over_stock/serach_stock_days_third";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata=data.body
            $(".title_b p").html(indexdata[0].wh_name+indexdata[0].day_type+" 明细")
            dataBindTable()
        }else{
            indexdata=""
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
    $("#start_date").val(base_obj.date+"年")
}

//绑定表格
function dataBindTable(){
    if($(".on").attr("datatype")=="1"){
        $(".tablestyle1 th").eq(2).html("积压数量")
    }else{
        $(".tablestyle1 th").eq(2).html("积压金额")
    }
    $(".tablestyle1 tr:gt(0)").remove()
    $.each(indexdata,function(i,item){
        var str=""
        str+="<tr>"
        str+="<td>"+(i+1)+"</td>"
        str+="<td>"+item.mat_name+"</td>"
        str+="<td align='right' style='color:#5599EE'>"+item.qty+" "+ item.unit_name+"</td>"
        $(".tablestyle1").append(str)
    })
    dataIsNull($(".tablestyle1"))
}

$(function(){
    base_obj=parseQueryString()
    dataBindURL()
    getDataView()

    $('.back').click(function(){
        base_obj.wh_id=""
        window.location.href = "stockAnalysis2.html?"+parseParam(base_obj);
    })

    $('.type').click(function(){
        $(".on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })

})