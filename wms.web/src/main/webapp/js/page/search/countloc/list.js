var indexdata=null,basedata=null

//加载数据
function getData(){
    var url=uriapi+"/biz/stquery/query_loc_price"
    showloading()
    ajax(url,"get",null,function(data){
        layer.close(indexloading)
        indexdata=data.body
        dataBind()
    })
}

//导出excel
function formDownload(){
    var ischecked=diy_time($("#timefr").val(),$("#timeto").val());
    if(ischecked==false) return false
    $("form [name='board_id']").val($("#board").val());
    $("form [name='fty_id']").val($("#fty").val());
    $("form [name='location_id']").val($("#location").val());
    $("form [name='time_fr']").val($("#timefr").val());
    $("form [name='time_to']").val($("#timeto").val());
    $("form [name='mat_group_id']").val($("#mat").val());
    $("#download").attr("action", uriapi + "/biz/turnover/download_turnover");
    $("#download").submit();
}

//数据整理
function dataBind(){
    var data=[],text=[]
    $.each(indexdata,function(i,item){
        data.push({
            value:item.num,
            name:item.wh_loc,
            wh_id:item.wh_id,
            location_id:item.location_id
        })
        text.push(item.wh_loc)
    })
    drawChar(data,text)
}

//图表
function drawChar(data,text){
    var myChart = echarts.init(document.getElementById('Chart'));
    myChart.clear()
    var option = {

        tooltip : {
            trigger: 'item',
            formatter: "{b} <br/>{c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            right: 'right',
            padding:[0,50,0,0],
            itemGap:11,
            top:'middle',
            data: text
        },
        series : [
            {
                type: 'pie',
                radius : '70%',
                center: ['50%', '50%'],
                data:data,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
    myChart.on('click', function (params) {
        fileExport(params.data)
    });

}

//导出excel
function fileExport(data){
    $("form [name='location_id']").val(data.location_id);
    $("form [name='wh_id']").val(data.wh_id);
    $("#download").attr("action", uriapi + "/biz/stquery/download_locprice");
    $("#download").submit();
}


$(function(){
    getData()
})



