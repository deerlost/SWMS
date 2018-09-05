var indexdata=null,basedata=null

//读取数据
function getdata(){
    var url=uriapi+"/conf/jobstatis/get_base_info1"
    showloading();
    ajax(url,"get",null,function(data){
        basedata=data.body;
        $("#board").append("<option value='"+basedata.board_id+"'>"+basedata.board_name+"</option>")

        $.each(basedata.fty_list,function(i,item){
            $("#fty").append("<option value='"+item.fty_id+"' data-index='"+i+"'>"+item.fty_name+"</option>")
        });
        $.each(basedata.ware_house_list,function(i,item){
            $("#location").append("<option value='"+item.wh_id+"'>"+item.wh_code+"-"+item.wh_name+"</option>")
        })
        bindData()
        getName()
        btnsearch()
    })
}


//数据绑定
function bindData(){
    $("#board").val(basedata.board_id)
    $("#fty").val(basedata.fty_list[0].fty_id)
//  $("#location").val(basedata.ware_house_list[0].wh_id)
    $("#timefr").val(basedata.start_time)
    $("#timeto").val(basedata.end_time)
}

//加载人员
function getName(){
    var url=uriapi+"/conf/jobstatis/get_user_list_by_location_id?fty_id="+$("#fty").val()+"&wh_id="+$("#location").val()
    ajax(url,"get",null,function(data){
        layer.close(indexloading)
        $("#name option").remove()
        $("#name").append("<option value=''>全部</option>")
        $.each(data.body,function(i,item){
            $("#name").append("<option value='"+item.user_id+"'>"+item.user_name+"</option>")
        })
    })
}

//时间比较
function diy_time(time1,time2){
    var isccheck=true
    time1 = Date.parse(new Date(time1));
    time2 = Date.parse(new Date(time2));
    if(isNaN(time1)){
        layersMoretips("格式不正确",$("#timefr"))
        isccheck=false
    }
    if(isNaN(time2)){
        layersMoretips("格式不正确",$("#timeto"))
        isccheck=false
    }
    if(isccheck==true){
        var time3 = parseInt((time2 - time1)/1000/3600/24)
        if(time3>365){
            layer.msg("时间在一年以内")
            return false
        }else if(time3<0){
        	layer.msg("开始时间不能大于结束时间")
            return false
        }else{
            return true
        }
    }else{
        return false
    }
}

//添加创建人
function addName(data){
    $("#name option").removeAttr("selected")
    $("#name").append("<option selected value='"+data.user_id+"' style='display:none' >"+data.user_name+"</option>")
}

//加载图表数据
function btnsearch(){
    var name=""
    var ischecked=diy_time($("#timefr").val(),$("#timeto").val());
    if(ischecked==false) return false
    if($("#name").val()!=null) {
        if ($("#name").val() != '') {
            name = $("#name").val()
        }
    }
    var obj={
        "board_id": $("#board").val(),
        "fty_id": $("#fty").val(),
        "wh_id": $("#location").val(),
        "start_time": $("#timefr").val(),
        "end_time": $("#timeto").val(),
        "create_user": name
    }
    console.log(obj)
    var url=uriapi+"/conf/jobstatis/get_job_statis_pictureList";
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        layer.close(indexloading)
        indexdata=data.body
        Chardata()
    })
}

function Chardata(){
    var xAxisData=[],legendData=[],yAxis=[],series=[];
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
            barGap:0.1//柱间距离
        })
    });
    yAxis.push({
        type: 'value',
        min: 0,
        axisLabel: {
            formatter: '{value} '+indexdata.left_axis_unit
        }
    });
    if(indexdata.right_axis_max_value!=''){
        yAxis.push({
            type: 'value',
            min: 0,
            axisLabel: {
                formatter: '{value} '+indexdata.right_axis_unit
            },
            splitLine: {
                show: false
            }
        })
    }

    //默认显示数量
    var num=10;
    var drawChardata={
        color:['#FFC0CB','#9932CC','#4169E1','#008B8B','#7FFFAA','#FFD700','#FFFF00','#FFA500','#CD5C5C','#808080','#CD853F','#808000'],
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

//导出excel
function formDownload(){
    var ischecked=diy_time($("#timefr").val(),$("#timeto").val());
    if(ischecked==false) return false
    $("form [name='board_id']").val($("#board").val());
    $("form [name='fty_id']").val($("#fty").val());
    $("form [name='wh_id']").val($("#location").val());
    $("form [name='start_time']").val($("#timefr").val());
    $("form [name='end_time']").val($("#timeto").val());
    $("form [name='create_user']").val($("#name").val());
    $("#download").attr("action", uriapi + "/conf/jobstatis/download_job_statis_pictureList");
    $("#download").submit();
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
                    var unit=indexdata.chart_info_list[i].dependence=="0"?indexdata.left_axis_unit:indexdata.right_axis_unit;
                    paramsData+='<br>'+params[i].marker+params[i].seriesName+"："+params[i].value+unit
                })
                return paramsData
            }
        },
        title:{
            text:data.title,
            x:'center'
        },
        color: data.color,
        dataZoom: {
            show: data.dataZoom,
            realtime: true,
            height: 20,
            dataZoomIndex: 1,
            startValue: 0,
            endValue: data.endValue,
        },
        legend: {
            type: 'scroll',
            left: 'center',
            bottom: 0,
            data: data.legendData
        },
        xAxis: [
            {
                type: 'category',
                data: data.xAxisData,
                axisPointer: {
                    type: 'shadow'
                }
            }
        ],
        yAxis: data.yAxis,
        series: data.seriesData
    };
    myChart.setOption(option);
}
$(function(){
    bindDatePicker();
	checkDateDifference('timefr', 'timeto');
    tableedit($("body"))
    getdata()
    $(".btnsearch").click(function(){
        btnsearch()
    })
    $("#fty").change(function(){
        getName()
        if($("#fty").val()==""){
			$("#location").removeAttr("disabled")
		}else{
			$("#location").attr("disabled","disabled")
		}
    })
    $("#location").change(function(){
        getName()
         if($("#location").val()==""){
			$("#fty").removeAttr("disabled")
		}else{
			$("#fty").attr("disabled","disabled")
		}
    })
})



