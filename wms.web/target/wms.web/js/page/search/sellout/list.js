var indexdata=null,basedata=null,base={s1:0,s2:0},drawChardata=null,kcdddata=null;

//加载图表数据
function btnsearch(){
    var is_checked=true
    var starttime=parseInt($("#start_date").val().split("-").join(""))
    var endtime=parseInt($("#end_date").val().split("-").join(""))
    if(starttime>endtime){
    	layer.msg("开始时间不能大于结束时间");
    	return false;
    }
    if($("#end_date").val()==""){
        layersMoretips("必填项",$("#end_date"))
        is_checked=false
    }
    if($("#start_date").val()==""){
        layersMoretips("必填项",$("#start_date"))
        is_checked=false
    }
    if(is_checked==false) return false
    var obj={
        start_date:$("#start_date").val(),
        end_date:$("#end_date").val()
    }
    var url=uriapi+"/biz/query/get_sale_chart_data";
    showloading()
    ajax(url,"get",obj,function(data){
        layer.close(indexloading)
        indexdata=data.body
        if(indexdata.x_axis_list.length==0){
/*            $(".creatChart").hide()
            layer.alert("当前搜索条件下无库存积压", {
                icon: 5,
                title: "库存积压"
            });
            return false*/
        }else{
            $(".creatChart").show()
        }
        Chardata()
    })
}

//导出excel
function formDownload(x,y,date){
    $("form [name='product_line_id']").val(indexdata.x_axis_list[x].y_axis_value_list[y].y_axis_id);
    $("form [name='date']").val(date);
    $("#download").attr("action", uriapi + "/biz/query/download_sale_data");
    $("#download").submit();
}

function Chardata(){
    layer.close(indexloading)
    var xAxisData=[],legendData=[],yAxis=[],series=[];
    var color=['#808000','#9932CC','#4169E1','#008B8B','#7FFFAA','#FFD700','#FFFF00','#FFA500','#CD5C5C','#808080','#CD853F','#808000']
    $.each(indexdata.x_axis_list,function(i,item){
        xAxisData.push(item.x_axis_name)
    });
    $.each(indexdata.chart_info_list,function(i,item){
        legendData.push(item.name)
        var seriesdata=[]
        $.each(indexdata.x_axis_list,function(j,items){
            seriesdata.push(items.y_axis_value_list[i].y_axis_value)
        })
        series.push({
            name:item.name,
            type:item.type==0?"bar":"line",
            yAxisIndex:item.dependence=="0"?"0":"1",
            data:seriesdata,
            barGap:0.1,//柱间距离,
            dataType:[1,2]
        })
    });
    yAxis.push({
        type: 'value',
        min: 0,
        axisLabel: {
            formatter: '{value} '+indexdata.left_axis_unit
        },
        axisLine: {
            lineStyle: {
                color: color[9]
            }
        }
    });


    //默认显示数量
    var num=8;
    var drawChardata={
        color:color,
        title:indexdata.title,
        dataZoom:indexdata.x_axis_list.length>num?true:false,
        endValue:(num-1),
        xAxisData:xAxisData,
        yAxis:yAxis,
        legendData:legendData,
        seriesData:series
    }
    drawChar(drawChardata);
}

//图表
function drawChar(data){
    var myChart = echarts.init(document.getElementById('Chart'));
    myChart.clear()
    option = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                crossStyle: {
                    color: '#999'
                }
            },
            formatter:function(params){
                var paramsData=params[0].name
                $.each(params,function(i,item){
                    var unit=indexdata.left_axis_unit
                    paramsData+='<br>'+params[i].marker+params[i].seriesName+"："+params[i].value+unit
                })
                return paramsData
            }
        },
        title:{
            text:data.title,
            x:'center'
        },
        grid: {
            right: '15%'
        },
        color: data.color,
        dataZoom: {
            show: data.dataZoom,
            realtime: true,
            height: 20,
            dataZoomIndex: 1,
            bottom:10,
            startValue: 0,
            endValue: data.endValue,
        },
        legend: {
            type: 'scroll',
            left: 'center',
            bottom: 0,
            data: data.legendData
        },
        xAxis:
        {
            type: 'category',
            data: data.xAxisData,
            axisPointer: {
                type: 'shadow'
            }
        },
        axisLabel: {
            interval:0
        },
        yAxis: data.yAxis,
        series: data.seriesData
    };
    myChart.setOption(option);
    myChart.on('click', function(params) {
        if (params.componentType === 'series' && params.seriesType === 'line') {
            formDownload(params.dataIndex,params.seriesIndex,params.name)
        }
    })
}

//时间控件
function bindDatePicker1(panel) {
    var txtdate = null;

    if(panel == undefined) {
        txtdate = $('.txtdatepicker');
    } else {
        txtdate = panel.find('.txtdatepicker');
    }

    txtdate.each(function() {
        var pos = $(this).attr("data-pickerPosition") == undefined ? "" : $(this).attr("data-pickerPosition");
        $(this).datetimepicker({
            language: 'zh-CN',
            minView: 3,
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 3,
            forceParse: 0,
            pickerPosition: pos,
        });
    });
}

function formartmonth(ndate){
	var m = (ndate.getMonth() + 1);
	m = (m.toString().length == 1) ? ("0" + m.toString()) : m;
	return ndate.getFullYear() + "-" + m;
}
$(function(){
	var start_date=new Date;
	var end_date=new Date;
	start_date.setMonth(start_date.getMonth()-2);
	$(".start_date").val(formartmonth(start_date));
	$(".end_date").val(formartmonth(end_date));
    bindDatePicker1();
	$(".end_date").datetimepicker('setStartDate',$(".start_date").val());
	$(".start_date").change(function(){
	 $(".end_date").datetimepicker('setStartDate',$(".start_date").val());
	})
	 btnsearch()
    $(".btnsearch").click(function(){
        btnsearch()
    })
})



