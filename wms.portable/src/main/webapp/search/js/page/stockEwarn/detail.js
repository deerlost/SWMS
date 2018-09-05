var indexdata=null,selectdata=null,base_obj=null;
function drawdata(data){
	var myChart = echarts.init(document.getElementById('main'));
	var option = {
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    series: [
	        {
	            name: '业务指标',
	            type: 'gauge',
	            detail: {
	            	formatter:data[0].value+"%",
	            },
	            data: data,
	            axisLine: {
	                lineStyle: {
	                    width: 10 // 这个是修改宽度的属性
	                },
	            },
	        }
	    ],
	};
	myChart.setOption(option);
}
//整理数据
function arrangdata(){
	var draw=[]
	var obj={}
	var index=parseInt(GetQueryString("index"))
	var types=GetQueryString("types")
	var datainfo=indexdata.body[types].data[index];
	$(".count1").html(datainfo.volume_max+"KG")
	$(".count2").html(datainfo.reminding_qty_two+"KG")
	$(".count3").html(datainfo.reminding_qty_one+"KG")
	$(".count4").html(datainfo.volume_min+"KG")
	$(".count5").html(datainfo.qty+"KG")
	obj.value=datainfo.persont
	obj.name="使用率"
	draw.push(obj)
	drawdata(draw)
}
function btnsearch(){
    var url=uriapi+"/conf/warehouse_volunm_warring/get_wearhouse_warring";
    showloading()
    ajax(url,"GET",null,function(data){
		if(data.head.status=="true"){
			indexdata=data
			arrangdata()
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}
$(function(){
	btnsearch();
		$('.margin').click(function(e){
			window.location.href = "stockEwarndetails2.html?index="+GetQueryString("index")+"&types="+GetQueryString("types")+"&area_id="+indexdata.body[GetQueryString("types")].data[parseInt(GetQueryString("index"))].area_id;
		})
	$('.back').click(function(){
		window.location.href = "stockEwarningl.html?types="+GetQueryString("types");
	})
	// 例子的文本懒的改了，就把环形图的属性样式改了改
});