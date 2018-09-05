var selectdata={}
//加载下拉
function getSelectData(){
    showloading()
    var url=uriapi+"/conf/personnel/query_product_line"
    ajax(url,"get",null,function(data){
        selectdata.product_line=data.body
        dataBind()
    },function(e){
        layer.close(indexloading)
    })

    var url=uriapi+"/conf/personnel/query_warehouse"
    ajax(url,"get",null,function(data){
        selectdata.warehouse=data.body
        dataBind()
    },function(e){
        layer.close(indexloading)
    })
}

//下拉数据绑定
function dataBind(){
    if(selectdata.product_line==undefined || selectdata.warehouse==undefined) return false;
    layer.close(indexloading)
    $.each(selectdata.product_line,function(i,item){
        $("#product_line").append("<option value='"+item.product_line_id+"'>"+item.product_line_name+"</option>")
    })
    $.each(selectdata.warehouse,function(i,item){
        $("#warehouse").append("<option value='"+item.wh_id+"'>"+item.wh_name+"</option>")
    })
}

//提交
function submit(){
    var type=GetQueryString("type")
    var obj={}
    switch (type){
        case "1":
        case "2":
        case "3":
            obj.person_name=$("#driver_name").val()
            obj.wh_id=$("#warehouse").val()
            break;
        case "4":
            obj.driver_name=$("#driver_name").val()
            obj.product_line_id=$("#product_line").val()
            break;
        case "5":
            obj.vehicle_name=$("#driver_name").val()
            obj.product_line_id=$("#product_line").val()
            break;
    }
    obj.type=type
    var url=uriapi+"/conf/personnel/insert_person"
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status){
            parent.location.href = "list.html";
        }else{
            layer.close(indexloading)
        }
    },function(e){
        layer.close(indexloading)
    },true)
}


$(function(){
    var type=GetQueryString("type")
    switch (type){
        case "1":
            $(".name").html("叉车工：")
            $(".product_line_hide").hide()
            break;
        case "2":
            $(".name").html("搬运工：")
            $(".product_line_hide").hide()
            break;
        case "3":
            $(".name").html("理货员：")
            $(".product_line_hide").hide()
            break;
        case "4":
            $(".name").html("司机：")
            $(".warehouse_hide").hide()
            break;
        case "5":
            $(".name").html("车辆：")
            $(".warehouse_hide").hide()
            break;
    }
    getSelectData()
    $(".btn_submit").click(function(){
        submit()
    })
})