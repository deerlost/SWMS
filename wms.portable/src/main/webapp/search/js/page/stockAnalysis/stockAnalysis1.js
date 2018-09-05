var indexdata={},selectdata=null,base_obj={}

//加载下拉
function getDataSelect(){
    var url=uriapi+"/conf/over_stock/base_info"
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        if(data.head.status){
            selectdata=data.body;
            dataBindSelect()
            dataBindURL()
            getDataView()
        }
    },function(e){
    })
}

//加载概览
function getDataView(){
    showloading()
    var url=uriapi+"/conf/over_stock/serach_stock_days_first";
    ajax(url,"post",JSON.stringify(base_obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata=data.body
            dataBindInit()
            drawChar()
        }else{
            indexdata=""
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
            drawChar()
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
    $("#start_date").val(selectdata.year+"年")
    base_obj.date=selectdata.year
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
}


//数据整理
function dataBindInit(){
    $(".c1").html(indexdata.amout_list.one_year+"<span>"+indexdata.amout_list.unit_name+"</span>")
    $(".c2").html(indexdata.amout_list.six_to_year+"<span>"+indexdata.amout_list.unit_name+"</span>")
    $(".c3").html(indexdata.amout_list.four_to_six+"<span>"+indexdata.amout_list.unit_name+"</span>")
    $(".c4").html(indexdata.amout_list.small_four+"<span>"+indexdata.amout_list.unit_name+"</span>")
}

//图表
function drawChar(){
    var myChart = echarts.init(document.getElementById('main'));
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
            formatter: "{b} : {c}"+indexdata.amout_list.unit_name+" ({d}%)",
            position:[0, 5],
        },
        calculable : true,
        series : [
            {
                type:'pie',
                radius : '80%',
                center: ['50%', '60%'],
                selectedMode:'single',
                data:[
                    {value:indexdata.amout_list.small_four, name:'4个月以内'},
                    {value:indexdata.amout_list.four_to_six, name:'4-6个月'},
                    {value:indexdata.amout_list.six_to_year, name:'半年以上'},
                    {value:indexdata.amout_list.one_year, name:'一年以上'},
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
            getUTL()
        }
        console.log(params)
    });
}

//地址跳转
function getUTL(){
    window.location.href = "stockAnalysis2.html?"+parseParam(base_obj);
}
//获取板块公司id
function getbord(a,b){
	base_obj.board_id=a
	base_obj.corp_id=b
	getDataView();
}
$(function(){
	tableedit($("body"))
    base_obj=parseQueryString()
    base_obj.type=base_obj.type||2
    base_obj.wh_id=base_obj.wh_id||""
    getDataSelect()
    $(".wh").click(function(){
        $(".fbox").toggle();
        var height=$(".banner").height()
        $(".banner").css("margin-top",-(height/2)+"px")
    })

    $(".banner img").click(function(){
        $(".fbox").toggle();
    })

    $('.f4_li div').click(function(){
        if($(this).find(".level").html()=="一年以上") base_obj.search_type=1
        if($(this).find(".level").html()=="半年以上") base_obj.search_type=2
        if($(this).find(".level").html()=="4-6个月") base_obj.search_type=3
        if($(this).find(".level").html()=="4个月以内") base_obj.search_type=4
        getUTL()
    })
    $('.type').click(function(){
        $(".on").removeClass("on")
        $(this).addClass("on")
        base_obj.type=$(this).attr("datatype")
        getDataView()
    })

})