
$(function(){
    showloading()

    var url = uriapi+"/conf/jobstatis/get_job_statis_pictureList"

    ajax(url,"get",null,function(data){

        layer.close(indexloading)
        selectdata=data.body

        dataBindSelect(data.body)
        dataBindList()
    })
})


//概况上部内容
function dataBindSelect(data){

        $(".cs_1").append("<p class='nub c1''>"+data.head.allnum+"</p>");

        $(".cs_2").append("<p class='nub c2''>"+data.head.pda_percent+"</p>");

        $(".cs_3").append("<p class='nub c3''>"+data.head.web_num+"</p>");

        $(".cs_4").append("<p class='nub c4''>"+data.head.pda_num+"</p>")

}


//底部下拉列表
function dataBindList(){

    $.each(selectdata.return_list,function(i,item){
        $(".list_ul_1").append("<li><span class='ol1'>"+item.wh_code+"-"+item.wh_name+"</span><span class='ol2'>"+item.all_num+"</span><b class='ol3'>"+item.pda_percent_str+"</b></li>")
    });

}

















