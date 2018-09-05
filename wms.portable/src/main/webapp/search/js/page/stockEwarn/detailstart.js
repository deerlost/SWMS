var indexdata=null,selectdata=null,base_obj=null,types="two_map";

function btnsearch(){
    var url=uriapi+"/conf/warehouse_volunm_warring/get_wearhouse_warring";
    showloading()
    ajax(url,"GET",null,function(data){
		if(data.head.status=="true"){
			indexdata=data
			$(".num1").html(data.body.two_map.num)
			$(".num2").html(data.body.one_map.num)
			$(".num3").html(data.body.min_map.num)
			$(".num4").html(data.body.normal_map.num)
			databind()
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
function databind(){
	if(indexdata!=null){
		$(".tablestyle1 tr:gt(0)").remove()
		    $.each(indexdata.body[types].data,function(i,item){
		        var str=""
		        str+="<tr data-index="+i+">"
		        str+="<td>"+item.wh_name+"&nbsp;"+item.area_name+"</td>"
		        str+="<td>"+item.persont+"</td>"
		        $(".tablestyle1").append(str)
		    })
		     dataIsNull($(".tablestyle1"))
	}

    $(".tablestyle1 tr").click(function(){
        $(".tablestyle1 tr").removeClass("hover")
        $(this).addClass("hover")
        var index=parseInt($(this).attr("data-index"));
        window.location.href = "stockEwarndetails.html?index="+index+"&types="+types  
    })
   
}
$(function(){
	if(GetQueryString("types")!=undefined||GetQueryString("types")!=null){
		types=GetQueryString("types")
	}
	btnsearch();
	$(".f4_li").click(function(){
        types =$(this).attr("data-type");
		databind()
    })
});