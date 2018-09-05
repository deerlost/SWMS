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
            dataBindURL()
        }
    },function(e){
    })
}

//加载概览
function getDataView(){
    showloading()
    var url=uriapi+"/biz/query/stock_analyse/over_view";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata=data.body
            dataBindInit()
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
    $(".c1").html(indexdata.input_data+"<span>"+indexdata.unit_name+"</span>")
    $(".c2").html(indexdata.output_data+"<span>"+indexdata.unit_name+"</span>")
    $(".c3").html(indexdata.avg_input+"<span>"+indexdata.unit_name+"</span>")
    $(".c4").html(indexdata.avg_output+"<span>"+indexdata.unit_name+"</span>")
    drawData()
}

//图表数据整理
function drawData(){
    var type=$(".li_tit .on").attr("data-value")
    var obj={}
    obj.legend=[];obj.series=[]
    if(type==1){
        obj.text="出库前四名"
        $.each(indexdata.output_mat_list,function(i,item){
            obj.legend.push(item.value +"% "+ item.name)
            obj.series.push({
                    value:item.value,
                    name:item.value +"% "+ item.name
                })
        })
    }else{
        obj.text="入库前四名"
        $.each(indexdata.input_mat_list,function(i,item){
            obj.legend.push(item.value +"% "+ item.name)
            obj.series.push({
                    value:item.value,
                    name:item.value +"% "+ item.name
                })
        })
    }


    drawChar1(obj)
}

//图表
function drawChar1(obj){
    var myChart1 = echarts.init(document.getElementById('main'));
    myChart1.clear()
    option = {
        title: {
            text:obj.text,
            x: 'center',
            y: 'center',
            itemGap: 16,
            textStyle : {
                color : 'rgba(30,144,255,0.8)',
                fontFamily : '微软雅黑',
                fontSize : 20,
                fontWeight : 'bolder'
            }
        },

        legend: {
            orient : 'vertical',
            x : document.getElementById('main').offsetWidth / 2,
            y : 2,
            itemGap:12,
            data:obj.legend
        },
        color:['#ff7f50','#87cefa','#da70d6','#5fd150'],
        series : [
            {
                type:'pie',
                clockWise:false,
                radius : [125, 150],
                itemStyle : {
                    normal: {
                        label: {show:false},
                        labelLine: {show:false}
                    }
                },
                data:[obj.series[0],
                    {
                        value:100-obj.series[0].value,
                        name:'invisible',
                        itemStyle : {
                            normal : {
                                color: 'rgba(0,0,0,0)',
                                label: {show:false},
                                labelLine: {show:false}
                            },
                            emphasis : {
                                color: 'rgba(0,0,0,0)'
                            }
                        }
                    }
                ]
            },
            {
                type:'pie',
                clockWise:false,
                radius : [100, 125],
                itemStyle : {
                    normal: {
                        label: {show:false},
                        labelLine: {show:false}
                    }
                },
                data:[obj.series[1],
                    {
                        value:100-obj.series[1].value,
                        name:'invisible',
                        itemStyle : {
                            normal : {
                                color: 'rgba(0,0,0,0)',
                                label: {show:false},
                                labelLine: {show:false}
                            },
                            emphasis : {
                                color: 'rgba(0,0,0,0)'
                            }
                        }
                    }
                ]
            },
            {
                type:'pie',
                clockWise:false,
                radius : [75, 100],
                itemStyle : {
                    normal: {
                        label: {show:false},
                        labelLine: {show:false}
                    }
                },
                data:[obj.series[2],
                    {
                        value:100-obj.series[2].value,
                        name:'invisible',
                        itemStyle : {
                            normal : {
                                color: 'rgba(0,0,0,0)',
                                label: {show:false},
                                labelLine: {show:false}
                            },
                            emphasis : {
                                color: 'rgba(0,0,0,0)'
                            }
                        }
                    }
                ]
            },
            {
                type:'pie',
                clockWise:false,
                radius : [50, 75],
                itemStyle : {
                    normal: {
                        label: {show:false},
                        labelLine: {show:false}
                    }
                },
                data:[obj.series[3],
                    {
                        value:100-obj.series[3].value,
                        name:'invisible',
                        itemStyle : {
                            normal : {
                                color: 'rgba(0,0,0,0)',
                                label: {show:false},
                                labelLine: {show:false}
                            },
                            emphasis : {
                                color: 'rgba(0,0,0,0)'
                            }
                        }
                    }
                ]
            },
        ]
    };
    myChart1.setOption(option);
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
//获取板块公司id
function getbord(a,b){
	base_obj.board_id=a
	base_obj.corp_id=b
	getDataView();
}
$(function(){
	tableedit($("body"));
    base_obj=parseQueryString()
    base_obj.type=base_obj.type||1
    base_obj.wh_id=base_obj.wh_id||""
    bindDatePicker1()
    getDataSelect()
    $(".wh").click(function(){
        $(".fbox").toggle();
        var height=$(".banner").height()
        $(".banner").css("margin-top",-(height/2)+"px")
    })

    $(".banner img").click(function(){
        $(".fbox").toggle();
    })

    $('.type').click(function(){
        $("span.on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })
    $(".li_tit a").click(function(){
        $(".li_tit a").removeClass("on")
        $(this).addClass("on")
        drawData()
    })

    $(".tititia .href").click(function(){
        window.location.href = "crk3.html?"+parseParam(base_obj);
    })

})