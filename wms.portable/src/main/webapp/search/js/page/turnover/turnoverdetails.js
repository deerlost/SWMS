var indexdata={},selectdata=null,base_obj=null;

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
    var url=uriapi+"/biz/turnover/avg_view";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        indexdata.avg_view=true
        if(data.head.status){
            indexdata.avg=data.body
            dataBindInit()
        }else{
            indexdata.avg=""
        }
    },function(e){})
    var url=uriapi+"/biz/turnover/over_view";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        indexdata.viewstatus=true
        if(data.head.status){
            indexdata.view=data.body
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
	if(base_obj.date!=undefined||base_obj.date!=null){
		$("#start_date").val(base_obj.date)
	}else{
		$("#start_date").val(selectdata.current_date)
		 base_obj.date=selectdata.current_date
	}
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


//数据整理
function dataBindInit(){
    if(indexdata.viewstatus!=true || indexdata.avg_view!=true) return false
    layer.close(indexloading)

    $(".c1").html(indexdata.view.start_data+"<span>"+indexdata.view.unit_name+"</span>")
    $(".c2").html(indexdata.view.end_data+"<span>"+indexdata.view.unit_name+"</span>")
    $(".c3").html(indexdata.view.input_data+"<span>"+indexdata.view.unit_name+"</span>")
    $(".c4").html(indexdata.view.output_data+"<span>"+indexdata.view.unit_name+"</span>")

    if(indexdata.avg==""){
        var myChart1 = echarts.init(document.getElementById('main'));
        myChart1.clear()
    }else{
        dataChar1()
    }

}
//获取板块公司id
function getbord(a,b){
	base_obj.board_id=a
	base_obj.corp_id=b
	getDataView();
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
    var myChart1 = echarts.init(document.getElementById('main'));
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
    base_obj.type=base_obj.type||1
    base_obj.wh_id=base_obj.wh_id||""
    bindDatePicker1()
    getDataSelect()
    tableedit($("body"))
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
        if(base_obj.type==1){
            $(".f4_li span").html("金额")
        }else{
            $(".f4_li span").html("数量")
        }

        getDataView()
    })

})