var indexdata=null,position_list=[],g1=null,g2=null,pallet=[],pack=[],is_run=true

//搜索
function search(){
    var is_ajax=true
    var kw=$(".ico-search").val().toUpperCase();
    if(kw=="") return false
    $.each(position_list,function(i,item){
        if(item.package_code==kw){
            layer.msg("包装物已存在")
            is_ajax=false
            return false
        }
    })
    if(is_ajax==false) return false
    var obj={
        mat_id:indexdata.mat_id,
        package_type_id:indexdata.package_type_id,
        location_id:indexdata.location_id,
        condition:kw,
        batch:indexdata.batch,
        mat_store_type:indexdata.mat_store_type
    }
    var url=uriapi+"/biz/product/transport/check_code_type"
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
            if(data.body.type==2){
                var msg=true
                $.each(position_list,function(i,item){
                    if(item.radio==1){
                        msg=false
                        var is_max_payload=is_weight(item.pallet_id,data.body.qty)
                        if(is_max_payload){
                            layer.msg("已超过最大重量")
                            return false
                        }
                        position_list.push({
                            package_id:data.body.package_id,
                            package_code:data.body.package_code,
                            package_type_code:data.body.package_type_code,
                            unit_zh:data.body.unit_zh,
                            qty:data.body.qty,
                            pallet_id:item.pallet_id,
                            pallet_code:item.pallet_code,
                            max_payload:item.max_payload,
                            radio:1
                        })
                        return false
                    }
                })
                if(msg){
                    layer.msg("请选中托盘")
                    return false
                }
            }else if(data.body.type==1){
                $.each(data.body.package_list,function(i,item){
                    var is_add=true
                    $.each(position_list,function(j,itemer){
                        if(item.package_id==itemer.package_id){
                            is_add=false
                            return false
                        }
                    })

                    if(is_add){
                        position_list.push({
                            package_id:item.package_id,
                            package_code:item.package_code,
                            package_type_code:item.package_type_code,
                            unit_zh:item.unit_zh,
                            qty:item.qty,
                            pallet_id:data.body.pallet_id,
                            pallet_code:data.body.pallet_code,
                            max_payload:data.body.max_payload
                        })
                    }

                })

                deleteRdo(data.body.pallet_id)
            }else{
                layer.msg("托盘/包装物不存在")
                return false
            }
            clearUp()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//判断是否超重
function is_weight(pallet_id,new_pallet_id){
    var max_payload= 0,postion_weight=0
    $.each(position_list,function(i,item){
        if(item.pallet_id==pallet_id){
            postion_weight=item.max_payload
            max_payload+=parseInt(item.qty||0)
        }
    })
    return max_payload+parseInt(new_pallet_id)>postion_weight
}

//清空单选  选择指定行
function deleteRdo(pallet_id){
    $.each(position_list,function(i,item){
        if(pallet_id==item.pallet_id){
            item.radio=1
        }else{
            item.radio=0
        }
    })
}

//数据处理
function clearUp(){
    pack=[],pallet=[];
    $.each(position_list,function(i,item){
        var is_add=true
        $.each(pallet,function(j,itemer){
            if(item.pallet_id==itemer.pallet_id){
                is_add=false
                itemer.org_name+=1
                itemer.have_payload=countData.add(itemer.have_payload,item.qty)
                return false
            }
        })
        if(is_add){
            pallet.push(item)
            pallet[pallet.length-1].org_name=item.qty==""?0:1
            pallet[pallet.length-1].have_payload=item.qty
        }
    })
    $.each(position_list,function(i,item){
        if(item.radio==1 && item.package_id!==""){
            pack.push(item)
        }
    })
    dataBindPallet()
    dataBindPack()
    number()
}

//绑定表格托盘
function dataBindPallet(){
    g1=$("#id_div_grid_position").iGrid({
        columns: [
            { th: '托盘编号', col: 'pallet_code', sort:false,width:200},
            { th: '量大载重', col: 'max_payload', sort:false,width:80},
            { th: '已存放重量', col: 'have_payload', sort:false,width:90},
            { th: '件数', col: 'org_name', sort:false,width:60},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: pallet,
        skin:"tablestyle4",
        radio:true,
        isHideRadio:true,
        mb_YesNoCancelRadio:true,
        GetRadioData(a,b){
            if(b==undefined){
                return false
            }else{
                deleteRdo(a.pallet_id)
                clearUp()
            }
            if(GetQueryString("view")!=null){
                $(".ico-search,.addpallet,.btn_table_delete,.btn_submit").remove()
                $(".btn_iframe_close_window").html("　关闭　")
            }
        },
        delete:function(a,b){
            delPallet(a.pallet_id)
        }
    })
    $.each(pallet,function(i,item){
        if(item.radio==1){
            g1.setSelectedRadio(i)
            return false
        }
    })

}

//绑定表格包
function dataBindPack(){
    g2=$("#id_div_grid_pack").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false},
            { th: '包装类型', col: 'package_type_code', sort:false,width:80},
            { th: '单位', col: 'unit_zh', sort:false,width:90},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: pack,
        skin:"tablestyle4",
        delete:function(a,b){
            delPack(a.package_id)
        }
    })
}

//增加新托盘
function addPallet(){
    var url=uriapi+"/biz/product/transport/get_new_pallet"
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            position_list.push({
                package_id:"",
                package_code:"",
                package_type_code:"",
                unit_zh:"",
                qty:"",
                pallet_id:data.body.pallet_id,
                pallet_code:data.body.pallet_code,
                max_payload:data.body.max_payload
            })
            clearUp()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//上架数量汇总
function number(){
    var num=0
    $.each(position_list,function(i,item){
        if(item.qty!==""){
            num=countData.add(num,item.qty)
        }
    })
    $(".stock_task_qty").html(num)
}

//删除托盘
function delPallet(pallet_id){
    for(var i=position_list.length- 1;i>=0;i--){
        if(position_list[i].pallet_id==pallet_id){
            position_list.splice(i,1)
        }
    }
    clearUp()
}

//删除物料
function delPack(package_id){
    for(var i=position_list.length- 1;i>=0;i--){
        if(position_list[i].package_id==package_id){
            position_list.splice(i,1)
        }
    }
    clearUp()
}

//提交
function submit(){
    if(parseFloat($(".stock_task_qty").html())>parseFloat($(".stock_qty").html())){
        layer.msg("本次转运数量不能大于库存数量")
        return false
    }
    parent.setDataPack(GetQueryString("index"),position_list,$(".stock_task_qty").html())
    $(".btn_iframe_close_window").click()
}

$(function(){
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    $.each(data.position_list,function(i,item){
        position_list[i]={}
        position_list[i].package_id=item.package_id                       //包装物id
        position_list[i].package_code=item.package_code                   //包装物code
        position_list[i].package_type_code=item.package_type_code         //包装物类型
        position_list[i].unit_zh=item.unit_zh                         //包装物单位
        position_list[i].qty=item.qty                         //包装物重量
        position_list[i].pallet_id=item.pallet_id               //托盘id
        position_list[i].pallet_code=item.pallet_code           //托盘code
        position_list[i].max_payload=item.max_payload             //托盘最大重量
    })
    scan()
    $(".stock_qty").html(indexdata.stock_qty)
    clearUp()

    if(GetQueryString("view")!=null){
        $(".ico-search,.addpallet,.btn_table_delete,.btn_submit").remove()
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