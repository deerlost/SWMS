/**
 * Created by ly131 on 2018/8/23.
 */
//临期库存概览页
function getFirst_view(){
    var url=uriapi+"/conf/warring/select_first_view"
    ajax(url,"get",null,function(data){
        console.log(data.body);
        $("#lq_qty").html(data.body.lq_qty);
        $("#lq_qty_money").html(data.body.lq_qty_money);
        $("#cq_qty").html(data.body.cq_qty);
        $("#cq_qty_money").html(data.body.cq_qty_money);
    })
}
//图表
function first_report(){
    var url=uriapi+"/conf/warring/select_first_report"
    ajax(url,"get",null,function(data){
        console.log(data.body);
        first_reportData(data.body);
    })
}
//整理图表数据
function first_reportData(data){
    var obj = {};
    obj.data = [];obj.name = [];
    $.each(data.x_axis_list[0].y_axis_value_list,function(i,item){
        obj.data.push({'value':item.y_axis_value,'name':data.x_axis_list[0].x_axis_name[i].name});
    })
    $.each(data.x_axis_list[0].x_axis_name,function(i,item){
        obj.name.push(item.name);
    })
    console.log("obj.data"+obj.data);
    console.log("obj.name"+obj.name);
    drawChar1(obj);
}
//图表
function drawChar1(data){
    var myChart = echarts.init(document.getElementById('main'));
    var option = {
        title : {
            text: '',
            subtext: '',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: data.name
        },
        series : [
            {
                name: '访问来源',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:data.data,
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
}
$(function(){
    getFirst_view();
    first_report();
})