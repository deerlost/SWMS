var indexdata={},selectdata=null,base_obj=null,board=1,corp=1;
//绑定仓位下拉
function dataBindSelect(){
	$(".fbox ul").empty()
    $(".fbox ul").append("<li data-index='-1'>全部库房</li>")
    $(".wh p").html("全部库房")
    $.each(selectdata.body.wh_list,function(i,item){
        $(".fbox ul").append("<li data-index='"+i+"'>"+item.wh_name+"</li>")
    })
    $(".fbox .banner li").click(function(){
    	var datainfo={};
    	datainfo.body={};
    	datainfo.body.wh_list=[];
    	datainfo.body.nameinfo=[];
        var index =parseInt($(this).attr("data-index"));
        if(index!=-1){
        	selectdata.body.wh_list[index].value=selectdata.body.wh_list[index].qty;
        	selectdata.body.wh_list[index].name=selectdata.body.wh_list[index].wh_name;
	        datainfo.body.wh_list.push(selectdata.body.wh_list[index]);
	        datainfo.body.nameinfo.push(selectdata.body.wh_list[index].wh_name)
        }else{
	      var name=[];
	      var max=0;
		$.each(selectdata.body.wh_list,function(i,item){
			 item.name=item.wh_name;
			 item.value=item.qty;
			 name.push(item.wh_name)
		  })
			datainfo=selectdata
			datainfo.body.nameinfo=name;
		}
        $(".wh p").html($(this).html())
        $(".fbox").toggle();
        drawChar1(datainfo)
    })
}
//时间改变走接口重新加载
function callback(date){
    btnsearch()
}
//加载图表数据
function btnsearch(){
	var obj={
        "board_id":board,
        "corp_id": corp,
        "type":$(".on").attr("data-value"),
    }
	if(GetQueryString("city_id")==undefined||GetQueryString("city_id")==null){
		obj.countries=""
	}else{
		obj.city_id=GetQueryString("city_id")
	}
    var url=uriapi+"/biz/stquery/query_loc_price";
    showloading()
    ajax(url,"POST",JSON.stringify(obj),function(data){
    	var name=[];
    	var max=0;
		if(data.head.status=="true"){
			indexdata=data.body.wh_list;
			if($(".on").attr("data-value")=="qty"){
				$.each(data.body.wh_list,function(i,item){
					item.name=item.wh_name;
					item.value=item.qty;
					name.push(item.wh_name)
				})
			}else{
				 $.each(data.body.wh_list,function(i,item){
					item.name=item.wh_name;
					item.value=item.wh_price;
					name.push(item.wh_name)
				  })
			}
			data.body.nameinfo=name;
			$.each(data.body.city_list,function(i,item){
				item.name=item.city_name
				if(max<item.value){
					max=item.value;
				}
			})
			data.body.max=max
			selectdata=data;
			dataBindSelect()
			$(".c1").html(data.body.sum_qty)
			$(".c2").html(data.body.sum_price)
			$(".c3").html(data.body.loc_num)
			$(".c4").html(data.body.city_list.length)
				drawChar1(data)
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
//饼图图标
function drawChar1(data){
	var myChart = echarts.init(document.getElementById('main'));
    myChart.clear()
	option = {
    tooltip : {
        trigger: 'item',
        formatter: "{a} {b} : {c} ({d}%)",
        position:[0, 10]
    },
    legend: {
        orient : 'vertical',
        x : 'right',
        data:data.body.nameinfo
    },
    calculable : true,
    series : [
        {
            name:'访问来源',
            type:'pie',
            radius : ['50%', '70%'],
            itemStyle : {
                normal : {
                    label : {
                        show : false
                    },
                    labelLine : {
                        show : false
                    }
                }
            },
            data:data.body.wh_list
        }
    ]
 }
	myChart.setOption(option);
}
function getbord(a,b){
	board=a
	corp=b
	btnsearch();
}
$(function(){
	if(GetQueryString("board")!=null||GetQueryString("board")!=undefined){
		board=GetQueryString("board")
	}
	if(GetQueryString("corp")!=null||GetQueryString("corp")!=undefined){
		corp=GetQueryString("corp")
	}
	tableedit($("body"))
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
	$(".demo").click(function(){
		 window.location.href = "kcjg3.html?board="+board+"&corp="+corp;
	})
	// 例子的文本懒的改了，就把环形图的属性样式改了改
});