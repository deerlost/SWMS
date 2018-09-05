var indexdata=null,basedata=null,base={s1:0,s2:0},drawChardata=null,kcdddata=null,param={},indexdata=[];
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function werks() {
	var url = uriapi + "/biz/stquery/get_base_list";
	ajax(url, 'GET', null, function(data) {
		basedata = data;
		$.each(basedata.body.board_list, function(i, item) {
			$("#board").append("<option value='" + item.board_id + "'data-index='" + i + "'>" +item.board_name + "</option>");
		});
		$("#board").val(basedata.body.board_id);
		getcompuent();
		$("#werks").val(basedata.body.corp_id);
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function getcompuent(){
	$("#werks option").remove();
	var index=$("#board option:selected").attr("data-index");
	if($("#board").attr("data-index")!=-1){
		$.each(basedata.body.board_list[index].corp_list,function(i,item){
			$("#werks").append("<option value='" + item.corp_id + "'data-index='" + i + "'>" +item.corp_name + "</option>");
		})
	}
}
//加载图表数据
function btnsearch(value){
	var obj={
        "board_id":$("#board").val(),
        "corp_id": $("#werks").val(),
        "type":$("#wh_info").val(),
        "year":$(".timedate").val(),
    }
	if(value==null||value==undefined){
		obj.countries=""
	}else{
		obj.city_id=value
	}
    param=obj
    var url=uriapi+"/biz/stquery/query_loc_price";
    showloading()
    ajax(url,"POST",JSON.stringify(obj),function(data){
    	var name=[];
    	var max=0;
		if(data.head.status==true){
			indexdata=data.body.loc_list
			if(value==null||value==undefined){
			if($(".wh_info").val()=="qty"){
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
			$(".overview1").html(data.body.sum_qty)
			$(".overview2").html(data.body.sum_price)
			$(".overview3").html(data.body.loc_num)
			$(".overview4").html(data.body.city_list.length)
				drawChar1(data)
				drawChar(data)
			}
			datebind()
		}
		layer.close(indexloading)
    },function(e){
    	layer.close(indexloading)
    })
}

//导出excel
function formDownload(value){
    $("form [name='wh_id']").val(value);
    $("#download").attr("action", uriapi + "/biz/stquery/download_locprice");
    $("#download").submit();
}
//饼图图标
function drawChar1(data){
	var myChart = echarts.init(document.getElementById('Chart'));
    myChart.clear()
	option = {
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
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
                },
                emphasis : {
                    label : {
                        show : true,
                        position : 'center',
                        textStyle : {
                            fontSize : '18',
                            fontWeight : 'bold'
                        }
                    }
                }
            },
            data:data.body.wh_list
        }
    ]
 }
	myChart.setOption(option);
	myChart.on('click', function (params) {
		console.log(params)
		if(params.data.wh_id!=undefined){
			formDownload(params.data.wh_id)
		}
	});
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
    var myChart = echarts.init(document.getElementById('Chart1'));
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
		}
	});

}
function datebind(){
	 $("#id_div_grid").iGrid({
		columns: [ 
			{ th: '库存地点名称', col: 'loc_name', min: 200},
			{ th: '库存数量', col: 'qty',min: 200},
			{ th: '库存金额', col: 'wh_price'}, 
		], 
		data: indexdata,
		checkbox:false,
		rownumbers:true,
		resizehead:true,
		absolutelyWidth:true,
		percent:30,//空占位百分比
		skin:"tablestyle4",//皮肤
	});
}
//日期
function bindDatePicker1(){
   $(".txtdatepicker").datetimepicker({
      language: 'zh-CN',
      minView: 4,
      weekStart: 1,
      todayBtn: 1,
      autoclose: 1,
      todayHighlight: 1,
      startView: 4,
      forceParse: 0,
   });
}
$(function(){
	var data=new Date()
	$(".timedate").val(data.getFullYear())
	bindDatePicker1();
	$(".timedate").datetimepicker('setStartDate','2018');
    werks();
    btnsearch();
    $("#board").change(function(){
    	getcompuent();
    })
    $(".btn_search").click(function(){
        btnsearch()
    })
    $(".deletetime").click(function(){
    	if($(".timedate").val()!=""){
    		$(".timedate").val("")
    	}
    })
})



