var indexdata={},selectdata=null,base_obj=null

//加载下拉
function getDataSelect(){
	var url=uriapi+"/biz/turnover/base_info"
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
	indexdata.avg_view=false;indexdata.viewstatus=false;indexdata.matstatus=false
	showloading()
	var obj={
		board_id:$("#board").val(),
		corp_id:$("#corp").val(),
		wh_id:$("#wh").val(),
		type:$("#type").val(),
		date:$("#date").val()
	}
	base_obj=obj
	var url=uriapi+"/biz/turnover/avg_view";
	ajax(url,"post",JSON.stringify(obj),function(data){
		indexdata.avg_view=true
		if(data.head.status){
			indexdata.avg=data.body
			dataBindInit()
		}else{
			indexdata.avg=""
		}
	},function(e){})
	var url=uriapi+"/biz/turnover/over_view";
	ajax(url,"post",JSON.stringify(obj),function(data){
		indexdata.viewstatus=true
		if(data.head.status){
			indexdata.view=data.body
			dataBindInit()
		}
	},function(e){})
	var url=uriapi+"/biz/turnover/mat_group_view"
	ajax(url,"post",JSON.stringify(obj),function(data){
		indexdata.matstatus=true
		if(data.head.status){
			indexdata.mat=data.body
			dataBindInit()
		}
	},function(e){})
}

//加载周转率分析
function getMatDay(){
	showloading()
	var url=uriapi+"/biz/turnover/mat_view"
	ajax(url,"post",JSON.stringify(base_obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.mat_char=data.body
			dataChar2()
		}
	},function(e){})
}

//加载库存占比
function getDatagroup(){
	showloading();
	var url=uriapi+"/biz/turnover/mat_group_view"
	ajax(url,"post",JSON.stringify(base_obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.mat=data.body
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
			{ th: '仓库号', col: 'wh_name', sort:false,width:150},
			{ th: '物料组', col: 'mat_group_name', sort:false},
			{ th: '周转天数', col: 'over_day', sort:false,width:80},
			{ th: '周转次数', col: 'over_tims' , sort:false,width:80},
		],
		data: indexdata.mat,
		rownumbers:false,
		isHideRadio:true,
		radio:true,
		skin:"tablestyle4",
		GetRadioData:function(a){//选择行后触发
			base_obj.mat_group_id=a.mat_group_id
			getMatDay()
		},
		loadcomplete:function(a){//页面绘制完成，返回所有数据。
			base_obj.show_type=3
			$(".body [data-index='0']").addClass("selected")
			$(".body input").eq(0).attr("checked","checked")
			var index=$(".body input:checked").val()
			base_obj.mat_group_id=indexdata.mat[index].mat_group_id
			getMatDay()
		}

	});
	if(indexdata.mat.length==0){
		var myChart2 = echarts.init(document.getElementById('Chart2'));
		myChart2.clear()
	}
}

//数据整理
function dataBindInit(){
	if(indexdata.viewstatus!=true || indexdata.avg_view!=true || indexdata.matstatus!=true) return false
	layer.close(indexloading)

	$(".overview1").html(indexdata.view.start_data)
	$(".overview2").html(indexdata.view.end_data)
	$(".overview3").html(indexdata.view.input_data)
	$(".overview4").html(indexdata.view.output_data)
	$(".unit").html(indexdata.view.unit_name)

	dataBindTable()
	if(indexdata.avg==""){
		var myChart1 = echarts.init(document.getElementById('Chart1'));
		myChart1.clear()
	}else{
		dataChar1()
	}

}

//图表数据整理
function dataChar1(){
	var obj={}
	obj.legend=[];obj.xAxis=[];obj.series=[];
	$.each(indexdata.avg.chart_info_list,function(i,item){
		obj.legend.push(item.name)
	})
	$.each(indexdata.avg.x_axis_list,function(i,item){
		obj.xAxis.push(item.x_axis_name)
	})
	obj.unit=indexdata.avg.left_axis_unit

	var data=[]
	$.each(indexdata.avg.chart_info_list,function(i,item){
		var value=[]
		$.each(indexdata.avg.x_axis_list,function(j,itemer){
			value.push(itemer.y_axis_value_list[i].y_axis_value)
		})
		data.push(value)
	})
	$.each(indexdata.avg.chart_info_list,function(i,item){
		obj.series.push({
			name:obj.legend[i],
			type:'line',
			data:data[i],

			markPoint : {
				data : [
					{type : 'max', name: '最大值'}
				]
			},
		})
	})

	drawChar1(obj)
}

//图表
function drawChar1(data){
	var myChart1 = echarts.init(document.getElementById('Chart1'));
	myChart1.clear()
	option1 = {
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
			left:'20%',
			right:'10%',
			top:65,
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
	myChart1.setOption(option1);
	myChart1.on('click', function (params) {
		if(params.componentType=='series'){
			base_obj.date=params.name
			getDatagroup()
		}
		console.log(params)
	});
}

//图表数据整理
function dataChar2(){
	var obj={}
	obj.legend=[];obj.xAxis=[];obj.series=[];
	$.each(indexdata.mat_char.chart_info_list,function(i,item){
		obj.legend.push(item.name)
	})
	$.each(indexdata.mat_char.x_axis_list,function(i,item){
		obj.xAxis.push(item.x_axis_name)
	})
	obj.unit=indexdata.mat_char.left_axis_unit

	var data=[]
	$.each(indexdata.mat_char.chart_info_list,function(i,item){
		var value=[]
		$.each(indexdata.mat_char.x_axis_list,function(j,itemer){
			value.push(itemer.y_axis_value_list[i].y_axis_value)
		})
		data.push(value)
	})
	$.each(indexdata.mat_char.chart_info_list,function(i,item){
		obj.series.push({
			name: obj.legend[i],
			type: 'bar',
			data: data[i]
		})
	})

	drawChar2(obj)
}

//图表
function drawChar2(data){
	var myChart2 = echarts.init(document.getElementById('Chart2'));
	myChart2.clear()
	option2 = {
		legend: {
			data: data.legend,
			align: 'right',
			right: 10,
			top:10
		},
		xAxis: {
			type: 'category',
			data:data.xAxis,
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
		},
		grid:{
			left:'8%',
			right:'5%',
		},
		yAxis: {
			type: 'value',
			splitNumber:3,
			axisLabel : {
				color:"#000000",
				formatter: '{value} ',
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
		},
		// 提示框
		tooltip: {
			trigger: 'axis',
			axisPointer: { // 坐标轴指示器，坐标轴触发有效
				type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		series: data.series
	};
	myChart2.setOption(option2);
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
	$("#date").datetimepicker('setStartDate','2018-08');
	getDataSelect()
	$(".btn_search").click(function(){
		if($("#type").val()==1){
			$(".type").html("金额")
		}else{
			$(".type").html("数量")
		}
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