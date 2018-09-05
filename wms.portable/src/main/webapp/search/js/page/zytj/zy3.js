$(function(){
    showloading()

    var url = uriapi+"/conf/jobstatis/get_job_statis_pictureList"

    ajax(url,"get",null,function(data){

        layer.close(indexloading)
        selectdata=data.body

        dataBindList()
    })
})

//底部下拉列表
function dataBindList(){

    var i = 1
    $.each(selectdata.detail,function(i,item){
        $(".list_ul_3").append("<li><span class='ol1' style='width:10%;'>"+ ++i +"</span><span class='ol1'style='width:50%;'>"+item.name+"</span><span class='ol2'>"+item.rate+"</span><b class='ol3'>"+item.allnum+"</b></li>")
    })

}