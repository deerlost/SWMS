selectdata = null
var myChart =null
var user_array=[],user_all_num_array=[],user_pda_num_array=[];
$(function(){
    showloading()
    var url = uriapi+"/conf/jobstatis/get_job_statis_pictureList"

    ajax(url,"get",null,function(data){

        layer.close(indexloading)
        selectdata=data.body
        dataBindList()
       	user_array=(selectdata.return_list[0].user_array);
        user_all_num_array=(selectdata.return_list[0].user_all_num_array);
        user_pda_num_array=(selectdata.return_list[0].user_pda_num_array);
        drawdata();
    })

    myChart = echarts.init(document.getElementById('main'));

})

function drawdata(){
	        option = {

            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['总操作量', '移动操作量']
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'value',
                    boundaryGap : [0, 0.01]
                }
            ],
            axisLabel: {
                interval:0,
                rotate:40
            },
            yAxis : [
                {
                    type : 'category',

                    data : user_array
                },

            ],

            series : [
                {
                    name:'总操作量',
                    type:'bar',
                    data:user_all_num_array
                },
                {
                    name:'移动操作量',
                    type:'bar',
                    data:user_pda_num_array
                }
            ]
        };
        myChart.setOption(option);
}

//底部echar
function dataBindList(){
    $.each(selectdata.return_list,function(i,item){
        $(".list_ul_2").append("<li  data-index='"+i+"'><span class='ol1'>"+item.wh_code+"-"+item.wh_name+"</span><span class='ol2'>"+item.num+"</span><b class='ol3'>"+item.all_num+"</b></li>")
    });
	$(".list_ul_2 li").eq(0).addClass("btn_color")
    $(".list_ul_2 li").click(function(){
    	$(".btn_color").removeClass("btn_color")
    	$(this).addClass("btn_color")
        var index=$(this).attr("data-index");
        user_array=(selectdata.return_list[parseInt(index)].user_array);
        user_all_num_array=(selectdata.return_list[parseInt(index)].user_all_num_array);
        user_pda_num_array=(selectdata.return_list[parseInt(index)].user_pda_num_array)
        drawdata()
    })
}







