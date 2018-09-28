var indexdata=null,g=null,sn_list=new Array(),table={disabled:false}

//搜索托盘或打包码
function search(){
    var kw=$(".ico-search").val()
    if(kw=="") return false
    var obj={
        location_id:indexdata.location_id,
        mat_id:indexdata.mat_id,
        batch:indexdata.batch,
        package_type_id:indexdata.package_type_id,
        condition:kw,
        item_id:indexdata.item_id,
        production_batch:indexdata.production_batch
    }
    showloading();
    var url=uriapi+"/biz/group_task/check_code_type"
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            console.log(data)
            if(data.head.status==false) return false
            if(data.body.type==1){                  //托盘
                $.each(data.body.package_list,function(i,item){
                    var is_add=true
                    $.each(sn_list,function(j,itemer){
                        if(item.package_id==itemer.package_id){
                            is_add=false
                            return false
                        }
                    })
                    if(is_add){
                        sn_list.push({
                            target_area_id:data.body.area_id,
                            target_position_id:data.body.position_id,
                            pallet_id:data.body.pallet_id,
                            package_id:item.package_id,
                            qty:item.qty,
                            package_code:item.package_code,
                            package_type_code:item.package_type_code,
                            unit_zh:item.unit_zh
                        })
                    }
                })
            }else if(data.body.type==2){           //包
                var is_add=true
                $.each(sn_list,function(i,item){
                    if(item.package_id==data.body.package_id){
                        is_add=false
                        return false
                    }
                })
                if(is_add){
                    sn_list.push({
                        target_area_id:data.body.area_id,
                        target_position_id:data.body.position_id,
                        pallet_id:data.body.pallet_id,
                        package_id:data.body.package_id,
                        qty:data.body.qty,
                        package_code:data.body.package_code,
                        package_type_code:data.body.package_type_code,
                        unit_zh:data.body.unit_zh
                    })
                }
            }
            dataBindTable()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定表格数据
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false,min:120},
            { th: '包装类型', col: 'package_type_code', sort:false,width:120},
            { th: '单位', col: 'unit_zh', sort:false,width:90},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center',disabled:table.disabled},
        ],
        data: sn_list,
        skin:"tablestyle4",
    })
}

//添加
function submit(){
    var qty=0
    $.each(sn_list,function(i,item){
        qty=countData.add(qty,item.qty)
    })
    if(sn_list.length==0){
        layer.msg("至少添加一条包装物")
        return false
    }
    parent.setPack(sn_list,GetQueryString("index"),qty)
    $(".btn_iframe_close_window").click()
}

$(function(){
    indexdata=parent.getDataMateriel(GetQueryString("index"))
    if(GetQueryString("view")){
        $(".btn_submit,.search").remove()
        $(".btn_iframe_close_window").html("　关闭　")
        table.disabled=true
        $.each(indexdata.pallet_package_list,function(i,item){
            sn_list[i]={}
            sn_list[i].target_area_id=item.target_area_id
            sn_list[i].target_position_id=item.target_position_id
            sn_list[i].pallet_id=item.pallet_id
            sn_list[i].package_id=item.package_id
            sn_list[i].qty=item.qty
            sn_list[i].package_code=item.package_code
            sn_list[i].package_type_code=item.package_type_code
            sn_list[i].unit_zh=item.unit_zh
        })
    }else{
        $.each(indexdata.sn_list,function(i,item){
            sn_list[i]={}
            sn_list[i].target_area_id=item.target_area_id
            sn_list[i].target_position_id=item.target_position_id
            sn_list[i].pallet_id=item.pallet_id
            sn_list[i].package_id=item.package_id
            sn_list[i].qty=item.qty
            sn_list[i].package_code=item.package_code
            sn_list[i].package_type_code=item.package_type_code
            sn_list[i].unit_zh=item.unit_zh
        })
    }
    dataBindTable()

    $(".btn_submit").click(function(){
        submit()
    })

    $("a.search").click(function(){
        search()
    })
    $("input.ico-search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
})