var indexdata={},selectdata=null,base_obj=null


//加载下拉
function getDataSelect(){
	var url=uriapi+"/conf/over_stock/base_info"
	showloading();
	ajax(url,"get",null,function(data){
		layer.close(indexloading);
		if(data.head.status){
			selectdata=data.body;
			dataBindSelect()
			getDataView()
		}
	},function(e){
	})
}

//加载概览
function getDataView(){
	showloading()
	var obj={
		board_id:$("#board").val(),
		corp_id:$("#corp").val(),
		wh_id:$("#wh").val(),
		type:$("#type").val()
	}
	base_obj=obj
	base_obj.wh_id1=$("#wh").val()
	var url=uriapi+"/conf/over_stock/serach_stock_days_first";
	ajax(url,"post",JSON.stringify(obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.info=data.body
			dataBindInit()
		}
	},function(e){})

}

//加载库存占比
function getDataWh(){
	showloading();
	var url=uriapi+"/conf/over_stock/serach_stock_days_second"
	ajax(url,"post",JSON.stringify(base_obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.wh=data.body
			dataBindTable()
		}
	},function(e){})
}

//加载积压物料清单
function getMatView(){
	showloading()
	var url=uriapi+"/conf/over_stock/serach_stock_days_third"
	ajax(url,"post",JSON.stringify(base_obj),function(data){
		layer.close(indexloading)
		if(data.head.status){
			indexdata.mat_view=data.body
			dataBindTable2()
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
	$(".year").html(selectdata.year+"年")
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
	$.each(selectdata.board_list[index1].corp_list[index2].warehouse_list,function(i,item){
		$("#wh").append("<option value='"+item.wh_id+"'>"+item.wh_code+"-"+item.wh_name+"</option>")
	})
}

//绑定表格
function dataBindTable(){
	var type=base_obj.type==1?"数量":"金额"
	$("#id_div_grid2").iGrid({
		columns: [
			{ th: '仓库名称', col: 'wh_name', sort:false,width:180},
			{ th: '库存总'+type, col: 'total_amount', sort:false},
			{ th: indexdata.wh.type_name+type, col: 'qty', sort:false},
		],
		data: indexdata.wh.wh_amout_list,
		skin:"tablestyle4",
		rownumbers:false,
		isHideRadio:true,
		 resizehead:true,
		radio:true,
		GetRadioData:function(a){//选择行后触发
			base_obj.wh_id=a.wh_id
			getMatView()
		},
		loadcomplete:function(a){//页面绘制完成，返回所有数据。
			$(".body [data-index='0']").addClass("selected")
			$(".body input").eq(0).attr("checked","checked")
			var index=$(".body input:checked").val()
			base_obj.wh_id=indexdata.wh.wh_amout_list[index].wh_id
			getMatView()
		}
	});

	if(indexdata.wh.wh_amout_list.length==0){
		indexdata.mat_view=[]
		dataBindTable2()
	}

}

//绑定表格2
function dataBindTable2(){
	$("#id_div_grid1").iGrid({
		columns: [
			{ th: '物料编码', col: 'mat_code', sort:false,width:180},
			{ th: '物料描述', col: 'mat_name', sort:false},
			{ th: '积压金额', col: 'amount', sort:false},
			{ th: '积压数量', col: 'qty', sort:false},
		],
		data: indexdata.mat_view,
		skin:"tablestyle4",
		rownumbers:false,
	});
}

//数据整理
function dataBindInit(){
	$(".overview1").html(indexdata.info.amout_list.one_year)
	$(".overview2").html(indexdata.info.amout_list.six_to_year)
	$(".overview3").html(indexdata.info.amout_list.four_to_six)
	$(".overview4").html(indexdata.info.amout_list.small_four)
	$(".unit").html(indexdata.info.amout_list.unit_name)
	drawChar()
	base_obj.search_type=4
	getDataWh()
}

//图表
function drawChar(){
	var myChart = echarts.init(document.getElementById('Chart'));
	myChart.clear()
	option = {
		color: [
			'#2ec7c9','#b6a2de','#5ab1ef','#ffb980','#d87a80',
			'#8d98b3','#e5cf0d','#97b552','#95706d','#dc69aa',
			'#07a2a4','#9a7fd1','#588dd5','#f5994e','#c05050',
			'#59678c','#c9ab00','#7eb00a','#6f5553','#c14089'
		],
		tooltip : {
			trigger: 'item',
			formatter: "{b} : {c}"+indexdata.info.amout_list.unit_name+" ({d}%)"
		},
		grid:{
			left:'8%',
			right:'8%',
			top:0,
		},
		calculable : true,
		series : [
			{
				type:'pie',
				radius : '70%',
				center: ['50%', '50%'],
				selectedMode:'single',
				data:[
					{value:indexdata.info.amout_list.small_four, name:'4个月以内',selected:true},
					{value:indexdata.info.amout_list.four_to_six, name:'4-6个月'},
					{value:indexdata.info.amout_list.six_to_year, name:'半年以上'},
					{value:indexdata.info.amout_list.one_year, name:'一年以上'},
				]
			}
		]
	};
	myChart.setOption(option);
	myChart.on('click', function (params) {
		if(params.componentType=='series'){
			if(params.name=='一年以上') base_obj.search_type=1
			if(params.name=='半年以上') base_obj.search_type=2
			if(params.name=='4-6个月') base_obj.search_type=3
			if(params.name=='4个月以内') base_obj.search_type=4
			base_obj.wh_id=base_obj.wh_id1
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