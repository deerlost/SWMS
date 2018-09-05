var indexdata=null,selectdata=null,base_obj=null,param="-1";
//绑定仓位下拉
function dataBindSelect(){
	$(".fbox ul").empty()
    $(".fbox ul").append("<li data-city=''>全部城市</li>")
    $(".wh p").html("全部城市")
    $.each(selectdata.body.city_list,function(i,item){
        $(".fbox ul").append("<li data-city='"+item.city_id+"'>"+item.city_name+"</li>")
    })
    $(".fbox .banner li").click(function(){
        var index =$(this).attr("data-city");
        if(index!=param){
        	if(index!=""){
        		btnsearch(index)
        	}else{
        		btnsearch()
        	}
        	param=index
        }
        $(".wh p").html($(this).html())
        $(".fbox").toggle();
    })
}
//加载图表数据
function btnsearch(){
	var obj={
        "board_id":GetQueryString("board"),
        "corp_id": GetQueryString("corp"),
        "type":GetQueryString("type"),
        "wh_id":GetQueryString("wh_id")
    }
	if(GetQueryString("city_id")!=""){
		obj.city_id=GetQueryString("city_id")
	}
    var url=uriapi+"/biz/stquery/query_loc_price";
    showloading()
    ajax(url,"POST",JSON.stringify(obj),function(data){
		indexdata=data
		datebind()
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
//绑定表格
function datebind(){
	    $.each(indexdata.body.loc_list,function(i,item){
	        var str=""
	        str+="<tr data-index="+i+">"
	        str+="<td>"+(i+1)+"</td>"
	        str+="<td>"+item.loc_name+"</td>"
	        str+="<td>"+item.qty+"</td>"
	        str+="<td>"+item.wh_price+"</td>"
	        $(".tablestyle1").append(str)
	    })
	     dataIsNull($(".price"))
}
$(function(){
	btnsearch();
	$(".Choice a").click(function(e){
		$(".fbox").toggle();
	})
	$(".banner img").click(function(e){
		$(".fbox").toggle();
	})
	$(".banner li").click(function(e){
		$(".fbox").toggle();
	})
	$(".back").click(function(){
		 window.location.href = "kcjg3.html?board="+GetQueryString("board")+"&corp="+GetQueryString("corp");
	})
	// 例子的文本懒的改了，就把环形图的属性样式改了改
});