var indexdata=null,selectdata=null,base_obj=null;
function databind(){
	if(indexdata!=null){
		$(".tablestyle1 tr:gt(0)").remove()
		    $.each(indexdata.body.return_map,function(i,item){
		        var str=""
		        str+="<tr data-index="+i+">"
		        str+="<td>"+(i+1)+"</td>"
		        str+="<td>"+item.mat_name+"</td>"
		        str+="<td>"+item.qty+"</td>"
		        $(".tablestyle1").append(str)
		    })
		     dataIsNull($(".tablestyle1"))
	}
}
function btnsearch(){
    var url=uriapi+"/conf/warehouse_volunm_warring/get_wearhouse_warring_by_area?area_id="+GetQueryString("area_id");
    showloading()
    ajax(url,"GET",null,function(data){
		if(data.head.status=="true"){
			indexdata=data
			databind()
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
$(function(){
	btnsearch();
	$('.back').click(function(){
		window.location.href = "stockEwarndetails.html?index="+GetQueryString("index")+"&types="+GetQueryString("types")+"&area_id="+GetQueryString("area_id");
	})
	// 例子的文本懒的改了，就把环形图的属性样式改了改
});