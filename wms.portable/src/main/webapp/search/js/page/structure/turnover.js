var selectdata=null

//数据加载
function getDataSelect(){
    showloading()
    var url=uriapi+"/biz/stquery/get_base_list"
    ajax(url,"get",null,function(data){
        layer.close(indexloading)
        selectdata=data.body
        dataBindSelect()
    })
}

//绑定下拉数据
function dataBindSelect(){
    $.each(selectdata.board_list,function(i,item){
        $(".banner ul").append("<li data-id='"+item.board_id+"' data-index='"+i+"'>"+item.board_name+"</li>")
    })

    $(".banner li").click(function(){
        $(".Choice a").html($(this).html()).attr({"data-id":$(this).attr("data-id"),"data-index":$(this).attr("data-index")})
        $(".fbox").toggle();
        getDataCorp()
    })
}

//获取公司
function getDataCorp(){
    var index=$(".Choice a").attr("data-index")
    $(".Choice li").remove()
    $.each(selectdata.board_list[index].corp_list,function(i,item){
        $(".Choice ul").append("<li data-id='"+item.corp_id+"'>"+item.corp_name+"</li>")
    })
    $(".Choice ul li").click(function(){
        window.location.href = "kcjg2.html?board="+$(".Choice a").attr("data-id")+"&corp="+$(this).attr("data-id");
    })
}

$(function(){
    getDataSelect()
    $(".Choice a").click(function(){
        $(".fbox").toggle();
    })

    $(".banner img").click(function(){
        $(".fbox").toggle();
    })
})
