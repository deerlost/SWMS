var indexdata=[],selectdata=null,base_obj=null,param="-1",citys="";
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
        		citys=index
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
function btnsearch(value){
	var obj={
        "board_id":GetQueryString("board"),
        "corp_id": GetQueryString("corp"),
        "type":$(".on").attr("data-value"),
    }
	if(value==undefined||value==null){
		obj.countries=""
	}else{
		obj.city_id=value
	}
    var url=uriapi+"/biz/stquery/query_loc_price";
    showloading()
    ajax(url,"POST",JSON.stringify(obj),function(data){
    	var name=[];
    	var max=0;
		if(data.head.status=="true"){
			indexdata=data.body.wh_list;
			$.each(data.body.wh_list,function(i,item){
				item.name=item.wh_name;
				item.value=item.qty;
				name.push(item.wh_name)
			})
			data.body.nameinfo=name;
			$.each(data.body.city_list,function(i,item){
				item.name=item.city_name
				if(max<item.value){
					max=item.value;
				}
			})
			data.body.max=max
			selectdata=data;
			if(value==null||value==undefined){
				dataBindSelect()
				drawChar(data)
			}
			datebind()
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
//绑定表格
function datebind(){
	if($(".on").attr("data-value")=="wh_price"){
		$(".qty").hide()
		$(".price").show()
		$(".price tr:gt(0)").remove()
	    $.each(indexdata,function(i,item){
	        var str=""
	        str+="<tr data-index="+i+">"
	        str+="<td>"+item.wh_name+"</td>"
	        str+="<td>"+item.wh_price+"</td>"
	        str+="<td>"+item.price_percentage+"</td>"
	        $(".price").append(str)
	    })
	     dataIsNull($(".price"))
	}else{
		$(".price").hide()
		$(".qty").show()
		$(".qty tr:gt(0)").remove()
	    $.each(indexdata,function(i,item){
	        var str=""
	        str+="<tr data-index="+i+">"
	        str+="<td>"+item.wh_name+"</td>"
	        str+="<td>"+item.qty+"</td>"
	        str+="<td>"+item.qty_percentage+"</td>"
	        $(".qty").append(str)
	    })
	     dataIsNull($(".qty"))
	}
    $(".tablestyle1 tr").click(function(){
        $(".tablestyle1 tr").removeClass("hover")
        $(this).addClass("hover")
        var index=parseInt($(this).attr("data-index"));
        window.location.href = "kcjg4.html?board="+GetQueryString("board")+"&corp="+GetQueryString("corp")+"&type="+$(".on").attr("data-value")+"&wh_id="+indexdata[index].wh_id+"&city_id="+citys                            
    })
   
}
//地图图表
function drawChar(data){
	var datatime=new Date();
	var month=datatime.getMonth() + 1;
	var strDate = datatime.getDate()
	 if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var myChart = echarts.init(document.getElementById('main'));
    myChart.clear()
   option = {
    title : {
        subtext:"统计日期:"+datatime.getFullYear()+"-"+month+"-"+strDate,
    },
    dataRange: {
        orient: 'horizontal',
        min: 0,
        max: data.body.max,
        text:['高','低'],           // 文本，默认为数值文本
        splitNumber:0
    },
    series : [
        {
            name: '2011全国GDP分布',
            type: 'map',
            mapType: 'china',
            mapLocation: {
                x: 'left'
            },
            selectedMode : 'single',
            itemStyle:{
                normal:{label:{show:true,textStyle:{color:"rgb(0,0,0)"}},color:'#8DEEEE'},
                emphasis:{
                	borderWidth:2,
                    borderColor:'#fff',
                    color:'#ffff00',
                	label:{show:true,textStyle:{color:"rgb(122,0,110)"}}},
            },
            data:data.body.city_list
        }
    ],
    animation: false
};
	myChart.setOption(option);
	myChart.on('click', function (params) {
		console.log(params)
		if(params.value=="-"){
			indexdata=[];
			datebind();
		}else{
		  btnsearch(params.data.city_id)
		  citys=params.data.city_id
		}
	});

}
$(function(){
	var data=new Date();
	$("#start_date").val(data.getFullYear()+"-"+(data.getMonth()+1));
	btnsearch();
	$(".is_click").click(function(){
		$(".is_click").removeClass("on")
		$(this).addClass("on")
		btnsearch()
	})
	$(".Choice a").click(function(e){
		$(".fbox").toggle();
	})
	$(".banner img").click(function(e){
		$(".fbox").toggle();
	})
	$(".banner li").click(function(e){
		$(".fbox").toggle();
	})
	 $(".wh").click(function(){
        $(".fbox").toggle();
        var height=$(".banner").height()
        $(".banner").css("margin-top",-(height/2)+"px")
   })
	$(".getdemo").click(function(){
		 window.location.href = "kcjg2.html?board="+GetQueryString("board")+"&corp="+GetQueryString("corp");
	})
	$(".back").click(function(){
		 window.location.href = "kcjg2.html?board="+GetQueryString("board")+"&corp="+GetQueryString("corp");
	})
	// 例子的文本懒的改了，就把环形图的属性样式改了改
});