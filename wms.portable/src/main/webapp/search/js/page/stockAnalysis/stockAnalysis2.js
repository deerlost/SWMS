var indexdata={},selectdata=null,base_obj=null

//加载概览
function getDataView(){
    showloading()
    var url=uriapi+"/conf/over_stock/serach_stock_days_second";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata=data.body
            drawCharData()
            dataBindTable()
        }else{
            indexdata=""
        }
    },function(e){})
}

//绑定url回调信息
function dataBindURL(){
    if(base_obj.type==1){
        $("[datatype='1']").addClass("on")
    }else{
        $("[datatype='2']").addClass("on")
    }
    $("#start_date").val(base_obj.date+"年")
}

//绑定表格
function dataBindTable(){
    if($(".on").attr("datatype")=="1"){
        $(".tablestyle1 th").eq(1).html("积压数量")
    }else{
        $(".tablestyle1 th").eq(1).html("积压金额")
    }
    $(".tablestyle1 tr:gt(0)").remove()
    $.each(indexdata.wh_amout_list,function(i,item){
        var str=""
        str+="<tr data-id='"+item.wh_id+"'>"
        str+="<td>"+item.wh_name+"</td>"
        str+="<td align='right' style='color:#5599EE'>"+item.qty+" "+ indexdata.left_axis_unit+"</td>"
        $(".tablestyle1").append(str)
    })
    dataIsNull($(".tablestyle1"))

    $(".tablestyle1 tr").click(function(){
        base_obj.wh_id=$(this).attr("data-id")
        getView()
    })
}

//数据跳转
function getView(){
    window.location.href = "stockAnalysis3.html?"+parseParam(base_obj);
}

//图表数据处理
function drawCharData(){
    var obj={}
    obj.xAxis=[];obj.series=[]
    $.each(indexdata.chart_info_list,function(i,item){
        obj.xAxis.push(item.name)
    })
    $.each(indexdata.x_axis_list,function(i,item){
        obj.series.push(item.y_axis_value_list[0].y_axis_value)
    })
    drawChar(obj)
}

//图表
function drawChar(obj){
    var myChart = echarts.init(document.getElementById('main'));
    myChart.clear()
    option = {
        color: ['#3398DB'],
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data : obj.xAxis,
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: { //坐标轴刻度标签的相关设置。
                    interval: 0, //设置为 1，表示『隔一个标签显示一个标签』
                },
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : [
            {
                type:'bar',
                barWidth: '60%',
                data:obj.series,
                splitNumber:1,
                label:{
                    normal:{
                        show:true,
                        position:'top'

                    }
                }
            }
        ]
    };
    myChart.setOption(option);
    myChart.on('click', function (params) {
        if(params.componentType=='series'){
            $.each(indexdata.x_axis_list,function(i,item){
                if(item.x_axis_name==params.name){
                    base_obj.wh_id=item.x_axis_wh_id
                    return false
                }
            })
            getView()
        }
        console.log(params)
    });
}

$(function(){
    base_obj=parseQueryString()
    if(base_obj.search_type==1) $(".title_b p").html("一年以上")
    if(base_obj.search_type==2) $(".title_b p").html("半年以上")
    if(base_obj.search_type==3) $(".title_b p").html("4-6个月")
    if(base_obj.search_type==4) $(".title_b p").html("4个月以内")
    dataBindURL()
    getDataView()

    $('.back').click(function(){
        window.location.href = "stockAnalysis1.html?"+parseParam(base_obj);
    })

    $('.type').click(function(){
        $(".on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })

})