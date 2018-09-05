var indexdata={},selectdata=null,base_obj=null,whId=""

//加载下拉
function getDataSelect(){
    var url=uriapi+"/biz/turnover/base_info"
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        if(data.head.status){
            selectdata=data.body;
            dataBindSelect()
            dataBindURL()
        }
    },function(e){
    })
}

//加载概览
function getDataView(){
    indexdata.avg_view=false;indexdata.viewstatus=false;
    showloading()
    var url=uriapi+"/biz/turnover/mat_group_view"
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        indexdata.matstatus=true
        if(data.head.status){
            indexdata.mat=data.body
            dataBindTable()
        }
    },function(e){})
}

//加载周转率分析
function getMatDay(){
    base_obj.show_type=3
    base_obj.mat_group_id=$(".tablestyle1 tr.hover").attr("data-id")
    whId=$(".tablestyle1 tr.hover").attr("wh-id")
    if(base_obj.mat_group_id==undefined || base_obj.mat_group_id==null){
        base_obj.mat_group_id=""
        return false
    }
    showloading()
    var url=uriapi+"/biz/turnover/mat_view"
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata.mat_char=data.body
            dataChar1()
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
    $.each(selectdata.board_list,function(i,item1){
        $.each(item1.corp_list,function(j,item2){
            $.each(item2.warehouse_list,function(t,item3){
                if(item2.corp_id==base_obj.corp_id && item3.wh_id==base_obj.wh_id && item1.board_id==base_obj.board_id){
                    $(".wh p").html(item3.wh_name)
                    return false
                }
            })
        })
    })
}

//绑定仓位下拉
function dataBindSelect(){
    $("#start_date").val(base_obj.date)
    var warehouse_list=""
    $.each(selectdata.board_list,function(i,item1){
        if(item1.board_id==base_obj.board_id){
            $.each(item1.corp_list,function(j,item2){
                if(item2.corp_id==base_obj.corp_id){
                    warehouse_list=item2.warehouse_list
                    return false
                }
            })
            return false
        }
    })

    $(".fbox ul").append("<li data-id=''>全部库房</li>")
    $.each(warehouse_list,function(i,item){
        $(".fbox ul").append("<li data-id='"+item.wh_id+"'>"+item.wh_name+"</li>")
    })

    $(".fbox .banner li").click(function(){
        base_obj.wh_id=$(this).attr("data-id")
        $(".wh p").html($(this).html())
        $(".fbox").toggle();
        getDataView()
    })
    getDataView()

}

//绑定表格
function dataBindTable(){
    $(".tablestyle1 tr:gt(0)").remove()
    var is_color=true
    $.each(indexdata.mat,function(i,item){
        var str=""
        if(base_obj.mat_group_id==item.mat_group_id && whId==item.wh_id){
            str+="<tr class='hover' wh-id='"+item.wh_id+"' data-id='"+item.mat_group_id+"'>"
            is_color=false
        }else{
            str+="<tr data-id='"+item.mat_group_id+"'  wh-id='"+item.wh_id+"'>"
        }
        str+="<td>"+item.wh_name+"</td>"
        str+="<td>"+item.mat_group_name+"</td>"
        str+="<td>"+item.over_day+"</td>"
        str+="<td>"+item.over_tims+"</td>"
        $(".tablestyle1").append(str)
    })
    dataIsNull($(".tablestyle1"))
    if(is_color==true && indexdata.mat.length>0){
        $(".tablestyle1 tr").eq(1).addClass("hover")
    }
    getMatDay()

    $(".tablestyle1 tr").click(function(){
        base_obj.mat_group_id=null
        $(".tablestyle1 tr").removeClass("hover")
        $(this).addClass("hover")
        getMatDay()
    })

}

//图表数据整理
function dataChar1(){
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

    drawChar1(obj)
}

//图表
function drawChar1(data){
    var myChart1 = echarts.init(document.getElementById('main'));
    myChart1.clear()
    option1 = {
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
            left:'15%',
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
    myChart1.setOption(option1);
}


//日期
function bindDatePicker1(){
    var data=new Date()
    var calendar = new LCalendar();
    calendar.init({
        'trigger': '#start_date', //标签id
        'type': 'ym', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
        'minDate':'2018-8-1',//最小日期 注意：该值会覆盖标签内定义的日期范围
        'maxDate':data.getFullYear()+"-"+(data.getMonth()+1)+"-"+data.getDate(),//最大日期 注意：该值会覆盖标签内定义的日期范围
    })
}

function callback(date){
    base_obj.date=date
    getDataView()
}

$(function(){
    base_obj=parseQueryString()
    bindDatePicker1()
    getDataSelect()


    $('.back').click(function(){
        window.location.href = "turnoverdetails.html?"+parseParam(base_obj);
    })
    $(".wh").click(function(){
        $(".fbox").toggle();
        var height=$(".banner").height()
        $(".banner").css("margin-top",-(height/2)+"px")
    })

    $(".banner img").click(function(){
        $(".fbox").toggle();
    })
    $('.margin').click(function(){
        window.location.href = "turnoverList.html?"+parseParam(base_obj);
    })
    $('.type').click(function(){
        $(".on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })

})