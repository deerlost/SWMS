//预警详情上表
function detail_table_tap(){
    var url1=uriapi+"/conf/warring/select_detail?search_type=2"
    var detailsArray = [];
    $("#select_detail").html("");
    ajax(url1,"get",null,function(data){
        detailsArray = data.body;
        var str='';
        $.each(detailsArray,function(i,item){
            str+='<li data-id="'+item.wh_id+'">';
            str+='<span class="ol1" style="text-align: left">'+item.wh_name+'</span>';
            str+='<span class="ol2" style="text-align: left">'+item.lq_qty+'</span>';
            str+='<b class="ol3">'+item.lq_qty_money+'</b>';
            str+='</li>'
            if(i ==0 ){
                wh_id = item.wh_id;
                detail_table_bottom(wh_id);
            }
        })
        $("#select_detail").append(str);
        $("#select_detail li").eq(0).addClass("btn_color")
    })
}
//临期详情下表
function detail_table_bottom(wh_id){
    var url1=uriapi+"/conf/warring/select_detail_mat?search_type=2&wh_id="+wh_id
    var detailsArray = [];
    $("#select_detail_mat").html("");
    ajax(url1,"get",null,function(data){
        detailsArray = data.body;
        var str='';
        $.each(detailsArray,function(i,item){
            str+='<li>';
            str+='<span class="ol2" style="text-align: left">'+item.mat_id+'</span>';
            str+='<span class="ol1" style="text-align: left">'+item.mat_name+'</span>';
            str+='<b class="ol3">'+item.lq_qty+'</b>';
            str+='</li>'
        })
        $("#select_detail_mat").append(str);
    })
}
$(function(){
    $(document).on('click',"#select_detail li" ,function () {
    	$(".btn_color").removeClass("btn_color")
    	$(this).addClass("btn_color")
        detail_table_bottom($(this).data('id'));
    })
    detail_table_tap();
})