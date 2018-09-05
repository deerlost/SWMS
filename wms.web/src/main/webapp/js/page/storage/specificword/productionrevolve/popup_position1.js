var indexdata=null,position_list=[],g1=null,is_run=true;

//搜索
function search(){
    var is_ajax=true
    var kw=$(".ico-search").val();
    if(kw=="") return false
    $.each(position_list,function(i,item){
        if(item.package_code==kw){
            layer.msg("包装物已存在")
            is_ajax=false
            return false
        }
    })
    if(is_ajax==false) return false
    var url=uriapi+"/biz/urgent/product/transport/check_code_type"
    var obj={
        mat_id:indexdata.mat_id,
        package_type_id:indexdata.package_type_id,
        location_id:indexdata.location_id,
        condition:kw,
        batch:indexdata.batch,
        mat_store_type:indexdata.mat_store_type
    }
    if(is_run){
        is_run=false
    }else{
        return false
    }
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        is_run=true
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.type=="2"){
                position_list.push({
                    package_id:data.body.package_id,
                    package_code:data.body.package_code,
                    package_type_code:data.body.package_type_code,
                    unit_code:data.body.unit_zh,
                    qty:data.body.qty,
                    pallet_id:null,
                })
            }else if(data.body.type=="1"){
                layer.msg("请输入包装物信息")
                return false
            }else{
                layer.msg("托盘/包装物不存在")
                return false
            }
            number()
            dataBindPack()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定表格包
function dataBindPack(){
    g1=$("#id_div_grid_pack").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false,width:300},
            { th: '包装类型', col: 'package_type_code', sort:false,width:150},
            { th: '单位', col: 'unit_code', sort:false,width:150},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: position_list,
        skin:"tablestyle4",
        delete:function(a,b){
            position_list.splice(b,1)
            number()
            dataBindPack()
        }
    })
}

//上架数量汇总
function number(){
    var qty=0
    $.each(position_list,function(i,item){
        qty=countData.add(qty,item.qty)
    })
    $(".stock_task_qty").html(qty)

}

//提交
function submit(){
    if(parseFloat($(".stock_task_qty").html())>parseFloat($(".stock_qty").html())){
        layer.msg("本次上架数量不能大于库存数量")
        return false
    }
    parent.setDataPack(GetQueryString("index"),position_list,$(".stock_task_qty").html())
    $(".btn_iframe_close_window").click()
}

$(function(){
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    $(".stock_qty").html(indexdata.stock_qty)
    $.each(data.position_list,function(i,item){
        position_list[i]={}
        position_list[i].package_id=item.package_id                       //包装物id
        position_list[i].package_code=item.package_code                   //包装物code
        position_list[i].package_type_code=item.package_type_code         //包装物类型
        position_list[i].unit_code=item.unit_code                         //包装物单位
        position_list[i].package_standard=item.package_standard                         //包装物重量
        position_list[i].pallet_id=item.pallet_id               //托盘id
        position_list[i].pallet_code=item.pallet_code           //托盘code
        position_list[i].max_weight=item.max_weight             //托盘最大重量
        position_list[i].qty=item.qty             //托盘最大重量
    })
    dataBindPack()
    number()
    if(GetQueryString("view")!=null){
        $(".keyinput").html("")
        $(".btn_table_delete,.btn_submit").remove()
        $(".btn_iframe_close_window").html("　关闭　")
    }

    $("a.search").click(function(){
        search()
    })
    $("input.ico-search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
    $(".addpallet").click(function(){
        addPallet()
    })
    $(".btn_submit").click(function(){
        submit()
    })
})