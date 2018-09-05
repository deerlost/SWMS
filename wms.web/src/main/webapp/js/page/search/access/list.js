var indexdata={},selectdata=null,base_obj=null


//加载下拉
function getDataSelect(){
	var url=uriapi+"/biz/query/stock_analyse/base_info"
	showloading();
	ajax(url,"get",null,function(data){
		layer.close(indexloading);
		if(data.head.status){
			selectdata=data.body;
			dataBindSelect()
		}
	},function(e){
	})
}

//加载概览
function getDataView(){
	if($("#corp").val()==null){
		layer.msg("公司不能为空");
		return false;
	}
	showloading()
	var obj={
		board_id:$("#board").val(),
		corp_id:$("#corp").val(),
		wh_id:$("#wh").val(),
		type:$("#type").val(),
		date:$("#date").val()
	}
	base_obj=obj
	var url=uriapi+"/biz/query/stock_analyse/info";
	ajax(url,"post",JSON.stringify(obj),function(data){
		if(data.head.status){
			indexdata.info=data.body
			indexdata.infostatus=true
			dataBindInit()
		}
	},function(e){})
	var url=uriapi+"/biz/query/stock_analyse/over_view";
	ajax(url,"post",JSON.stringify(obj),function(data){
		if(data.head.status){
			indexdata.view=data.body
			indexdata.viewstatus=true
			dataBindInit()
		}
	},function(e){})
	var url=uriapi+"/biz/query/stock_analyse/get_wh_info"
	ajax(url,"post",JSON.stringify(obj),function(data){
		if(data.head.status){
			indexdata.wh=data.body.return_list
			indexdata.whstatus=true
			dataBindInit()
		}
	},function(e){})
}

//加载库存占比
function getDataWh(){
	showloading();
	var url=uriapi+"/biz/query/stock_analyse/get_wh_info"
	ajax(url,"post",JSON.stringify(base_obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.wh=data.body.return_list
			dataBindTable()
		}
	},function(e){})
}

//绑定下拉
function dataBindSelect(){
	$.each(selectdata.board_list,function(i,item){
		$("#board").append("<option data-index='"+i+"' value='"+item.board_id+"'>"+item.board_name+"</option>")
	})
	$("#board").val(selectdata.deault_board_id)
	dataBindCorp()
	$("#corp").val(selectdata.default_corp_id)
	dataBindWh()
	$("#date").val(selectdata.current_date)
	getDataView()
}

//绑定公司
function dataBindCorp(){
	var index=$("#board option:selected").attr("data-index")
	$("#corp option").remove()
	$.each(selectdata.board_list[index].corp_list,function(i,item){
		$("#corp").append("<option data-index='"+i+"' value='"+item.corp_id+"'>"+item.corp_name+"</option>")
	})
}

//绑定仓位
function dataBindWh(){
	var index1=$("#board option:selected").attr("data-index")
	var index2=$("#corp option:selected").attr("data-index")
	$("#wh option").remove()
	$("#wh").append("<option value=''>全部</option>")
	if(selectdata.board_list[index1].corp_list[index2]!=undefined&&selectdata.board_list[index1].corp_list[index2]!=null){
		$.each(selectdata.board_list[index1].corp_list[index2].warehouse_list,function(i,item){
			$("#wh").append("<option value='"+item.wh_id+"'>"+item.wh_code+"-"+item.wh_name+"</option>")
		})
	}
}

//绑定表格
function dataBindTable(){
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '仓库号', col: 'wh_code', sort:false,width:80},
			{ th: '描述', col: 'wh_name', sort:false},
			{ th: '期初', col: 'start_balance_qty', sort:false},
			{ th: '本期入库', col: 'input_qty' , sort:false},
			{ th: '本期出库', col: 'output_qty', sort:false },
			{ th: '期末结存', col: 'ending_balance_qty', sort:false }
		],
		data: indexdata.wh,
		skin:"tablestyle4",
	});
}

//数据整理
function dataBindInit(){
	if(indexdata.viewstatus!=true || indexdata.infostatus!=true || indexdata.whstatus!=true) return false
	layer.close(indexloading)

	$(".overview1").html(indexdata.view.input_data)
	$(".overview2").html(indexdata.view.output_data)
	$(".overview3").html(indexdata.view.avg_input)
	$(".overview4").html(indexdata.view.avg_output)
	$(".unit").html(indexdata.view.unit_name)

	dataBindTable()
	dataChar()
}

//图表数据整理
function dataChar(){
	var obj={}
	obj.legend=[];obj.xAxis=[];obj.series=[];
	$.each(indexdata.info.chart_info_list,function(i,item){
		obj.legend.push(item.name)
	})
	$.each(indexdata.info.x_axis_list,function(i,item){
		obj.xAxis.push(item.x_axis_name)
	})
	obj.unit=indexdata.info.left_axis_unit

	var data=[]
	$.each(indexdata.info.chart_info_list,function(i,item){
		var value=[]
		$.each(indexdata.info.x_axis_list,function(j,itemer){
			value.push(itemer.y_axis_value_list[i].y_axis_value)
		})
		data.push(value)
	})
	$.each(indexdata.info.chart_info_list,function(i,item){
		obj.series.push({
			name:obj.legend[i],
			type:'line',
			data:data[i],

			markPoint : {
				data : [
					{type : 'max', name: '最大值'},
					{type : 'min', name: '最小值'}
				]
			},
			markLine : {
				silent:true,
				data : [
					{yAxis :indexdata.info.y_limit_line[i].y_axis_value, name: '平均值'}
				]
			}
		})
	})

	drawChar(obj)
}

//图表
function drawChar(data){
	var myChart = echarts.init(document.getElementById('Chart'));
	myChart.clear()
	option = {
		legend: {
			data:data.legend,
			top:10,
		},
		xAxis : [
			{
				type : 'category',
				data : data.xAxis,
				axisLine:{
					lineStyle:{
						color:"#008acd",
						width:2,
					}
				},
				splitLine:{
					show:true,
					lineStyle:{
						color:"#efefef"
					}
				},
				axisLabel : {
					color:"#000000",
				}
			}
		],
		yAxis : [
			{
				type : 'value',
				splitNumber:3,
				axisLabel : {
					color:"#000000",
					formatter: '{value} '+data.unit,
				},
				splitLine:{
					show:true,
					lineStyle:{
						color:"#efefef"
					}
				},
				axisLine:{
					lineStyle:{
						color:"#008acd",
						width:2,
					}
				},
				splitArea:{
					show:true,
					areaStyle:{
						color:['#f9f9f9','#fefefe']
					}
				}
			}
		],
		series : data.series,

		grid:{
			left:'8%',
			right:'8%',
			top:85,
		},
		color: [
			'#f57b4e','#83c6f0'
		],
		// 提示框
		tooltip: {
			trigger: 'axis',
			backgroundColor: 'rgba(50,50,50,0.5)',     // 提示背景颜色，默认为透明度为0.7的黑色
			axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				show:true,
				type : 'line',         // 默认为直线，可选为：'line' | 'shadow'
				lineStyle : {          // 直线指示器样式设置
					color: '#008acd',
					width : 1,
					type:'solid'
				}
			}
		}
	};
	myChart.setOption(option);
	myChart.on('click', function (params) {
		if(params.componentType=='series'){
			base_obj.date=params.name
			getDataWh()
		}
		console.log(params)
	});
}


//日期
function bindDatePicker1(){
	$(".txtdatepicker").datetimepicker({
		language: 'zh-CN',
		minView: 3,
		weekStart: 1,
		todayBtn: 1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 3,
		forceParse: 0,
	});
}


$(function(){
	bindDatePicker1()
	getDataSelect()
	$("#date").datetimepicker('setStartDate','2018-08');
	$(".btn_search").click(function(){
		getDataView()
	})

	$("#board").change(function(){
		dataBindCorp()
		dataBindWh()
	})
	$("#corp").change(function(){
		dataBindWh()
	})

})