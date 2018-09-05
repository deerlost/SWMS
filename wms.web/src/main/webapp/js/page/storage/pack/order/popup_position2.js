var indexdata=null,position_list=[],del_list=[],g1=null,g2=null,table={del:"delete"},is_run=true;

//搜索
function search(){
    var kw=$(".ico-search").val().toUpperCase()
    var is_checked=true
    var index=palletRid()
    if(kw=="") return false

    //判断已存在的
    $.each(position_list,function(i,item){
        if(item.pallet_code==kw){
            delPalletRid()
            item.radio=1
            dataBindPallet()
            dataBindPack()
            is_checked=false
            return false
        }
        $.each(item.package_list,function(j,itemer){
            if(itemer.package_code==kw){
                is_checked=false
                delPackage(j,i)
                return false
            }
        })

    })

    //判断假删除的
    $.each(del_list,function(i,item){
        if(item.pallet_code==kw){
            delPalletRid()
            position_list.push({
                max_weight:item.max_weight,
                pallet_code:item.pallet_code,
                pallet_id:item.pallet_id,
                have_payload:0,
                org_name:0,
                package_list:[],
                radio:1
            })
            del_list.splice(i,1)
            dataHavePayload()
            dataBindPallet()
            dataBindPack()
            number()
            is_checked=false
            return false
        }
        if(item.package_code==kw){
            if(position_list.length==0){
                layer.msg("请添加托盘")
                return false
            }
            if(is_weigth(item.package_standard)){
                layer.msg("已超出最大载重")
                return false
            }
            position_list[index].package_list.push({
                package_id:item.package_id,
                package_code:item.package_code,
                package_type_code:item.package_type_code,
                unit_name:item.unit_name,
                package_standard:item.package_standard,
            })
            del_list.splice(i,1)
            dataHavePayload()
            dataBindPallet()
            dataBindPack()
            number()
            is_checked=false
            return false
        }
    })

    if(is_checked==false) return false
    if(is_run){
        is_run=false
    }else{
        return false
    }
    var url=uriapi+"/biz/pkg/get_pkg_pallet_list?keyword="+kw+"&package_type_id="+GetQueryString("package_type_id")
    showloading()
    ajax(url,"get",null,function(data){
        is_run=true
        if(data.head.status==true){
            layer.close(indexloading);
            //托盘
            if(data.body.type=="pallet"){
                delPalletRid()
                position_list.push({
                    max_weight:data.body.pallet_list[0].max_weight,
                    pallet_code:data.body.pallet_list[0].pallet_code,
                    pallet_id:data.body.pallet_list[0].pallet_id,
                    have_payload:0,
                    org_name:0,
                    package_list:[],
                    radio:1
                })
            }else if(data.body.type=="pkgtype"){
                if(position_list.length==0){
                    layer.msg("请添加托盘")
                    return false
                }
                if(is_weigth(data.body.pkgtype_list[0].package_standard)){
                    layer.msg("已超出最大载重")
                    return false
                }
                position_list[index].package_list.push({
                    package_id:data.body.pkgtype_list[0].package_id,
                    package_code:data.body.pkgtype_list[0].package_code,
                    package_type_code:data.body.pkgtype_list[0].package_type_code,
                    unit_name:data.body.pkgtype_list[0].unit_name,
                    package_standard:data.body.pkgtype_list[0].package_standard,
                })
            }else{
                layer.msg(data.body.msg)
                return false
            }
            dataHavePayload()
            dataBindPallet()
            dataBindPack()
            number()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//增加新托盘
function addPallet(){
    if($(".printer").val()==""){
        layersMoretips("请选择打印机",$(".printer"))
        return false
    }
    var url=uriapi+"/biz/pkg/create_pkg_warehouse_pallet"
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            delPalletRid()
            position_list.push({
                max_weight:data.body.max_weight,
                pallet_code:data.body.pallet_code,
                pallet_id:data.body.pallet_id,
                have_payload:0,
                org_name:0,
                package_list:[],
                radio:1
            })
            dataHavePayload()
            dataBindPallet()
            dataBindPack()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定表格托盘
function dataBindPallet(){
    var org=indexdata.name_zh+"数"
    g1=$("#id_div_grid_position").iGrid({
        columns: [
            { th: '托盘编号', col: 'pallet_code', sort:false,width:200},
            { th: '量大载重', col: 'max_weight', sort:false,width:80},
            { th: '已存放重量', col: 'have_payload', sort:false,width:90},
            { th: org, col: 'org_name', sort:false,width:60},
            { th: '',type:table.del,tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: position_list,
        skin:"tablestyle4",
        radio:true,
        isHideRadio:true,
        mb_YesNoCancelRadio:false,
        GetRadioData(a,b){
            $.each(position_list,function(i,item){
                item.radio=0
            })
            a.radio=1
            dataBindPack()
        },
        delete:function(a,b){
            delPallet(b)
        }
    })
    $.each(position_list,function(i,item){
        if(item.radio==1){
            g1.setSelectedRadio(i)
            return false
        }
    })
}

//绑定表格包
function dataBindPack(){
    var pack=[]
    var index=palletRid()
    if(position_list.length!=0){
        $.each(position_list[index].package_list,function(i,item){
            pack.push(item)
        })
    }

    g2=$("#id_div_grid_pack").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false},
            { th: '包装类型', col: 'package_type_code', sort:false,width:80},
            { th: '单位', col: 'unit_name', sort:false,width:90},
            { th: '',type:table.del,tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: pack,
        skin:"tablestyle4",
        delete:function(a,b){
            delPack(b)
        }
    })
}

//删除托盘
function delPallet(index){
    var rid=palletRid()
    if(rid==index){
        position_list[0].radio=1
    }


    virtualDel("pallet",index)
    position_list.splice(index,1)
    dataHavePayload()
    dataBindPallet()
    dataBindPack()
    number()
}

//删除物料
function delPack(index){
    virtualDel("package",index)
    position_list[palletRid()].package_list.splice(index,1)
    dataHavePayload()
    dataBindPallet()
    dataBindPack()
    number()
}

//虚拟删除
function virtualDel(type,rid,index){
    var index=index||palletRid()
    if(type=="pallet"){
        $.each(position_list,function(i,item){
            if(rid==i){
                del_list.push({
                    max_weight:item.max_weight,
                    pallet_code:item.pallet_code,
                    pallet_id:item.pallet_id,
                    have_payload:0,
                    org_name:0,
                    package_list:[],
                    radio:1
                })
                $.each(item.package_list,function(j,itemer){
                    del_list.push({
                        package_id:itemer.package_id,
                        package_code:itemer.package_code,
                        package_type_code:itemer.package_type_code,
                        unit_name:itemer.unit_name,
                        package_standard:itemer.package_standard
                    })
                })
                return false
            }
        })
    }else{
        $.each(position_list[index].package_list,function(i,item){
            if(rid==i){
                del_list.push({
                    package_id:item.package_id,
                    package_code:item.package_code,
                    package_type_code:item.package_type_code,
                    unit_name:item.unit_name,
                    package_standard:item.package_standard
                })
                return false
            }
        })
    }
}

//删除二次扫描包装物
function delPackage(j,i){
    layer.confirm("包装物已存在，是否删除", {
        btn: ['删除', '取消'],
        icon: 3
    }, function() {
        virtualDel("package",j,i)
        position_list[i].package_list.splice(j,1)
        dataHavePayload()
        dataBindPallet()
        dataBindPack()
        number()
        layer.close(parseInt($(".layui-layer-shade").attr("times")));
    }, function() {
    });
}

//清除托盘行
function delPalletRid(){
    $.each(position_list,function(i,item){
        item.radio=0
    })
}

//获取托盘行数
function palletRid(){
    var rid=""
    $.each(position_list,function(i,item){
        if(item.radio==1){
            rid=i
            return false
        }
    })
    return rid
}

//最大载重
function is_weigth(data){
    var index=palletRid()
    if(parseInt(position_list[index].max_weight)<parseInt(position_list[index].have_payload)+parseInt(data)){
        return true
    }else{
        return false
    }
}

//已存放重量  件数计算
function dataHavePayload(){
    $.each(position_list,function(i,item){
        var have_payload=0
        $.each(item.package_list,function(j,itemer){
            have_payload=countData.add(have_payload,itemer.package_standard)
        })
        item.have_payload=have_payload
        item.org_name=item.package_list.length
    })
}

//上架数量汇总
function number(){
    var num=0
    if(position_list.length!=0){
        $.each(position_list,function(i,item){
            $.each(item.package_list,function(j,itemer){
                num+=1
            })
        })
    }
    $(".stock_task_qty").html(num)
}

//回车事件
function clickEvent(){
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
}

//提交
function submit(){
    parent.setDataPack2(GetQueryString("index"),position_list,$(".stock_task_qty").html())
    $(".btn_iframe_close_window").click()
}

$(function(){
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    $(".name_zh").html(indexdata.name_zh)

    $.each(data.position_list,function(i,item){
        if(indexdata.position_type){
            position_list.push({
                max_weight:item.max_weight,
                pallet_code:item.pallet_code,
                pallet_id:item.pallet_id,
                have_payload:0,
                org_name:0,
                package_list:item.package_list,
                radio:i==0?1:0
            })
        }else{
            var is_pallet=true
            $.each(position_list,function(j,itemer){
                if(itemer.pallet_id==item.pallet_id){
                    itemer.package_list.push({
                        package_id:item.package_id,
                        package_code:item.package_code,
                        package_type_code:item.package_type_code,
                        unit_name:item.unit_name,
                        package_standard:item.package_standard,
                    })
                    is_pallet=false
                    return false
                }
            })
            if(is_pallet){
                position_list.push({
                    max_weight:item.max_weight,
                    pallet_code:item.pallet_code,
                    pallet_id:item.pallet_id,
                    have_payload:0,
                    org_name:0,
                    package_list:[{
                        package_id:item.package_id,
                        package_code:item.package_code,
                        package_type_code:item.package_type_code,
                        unit_name:item.unit_name,
                        package_standard:item.package_standard,
                    }],
                    radio:i==0?1:0
                })
            }
        }
    })

    if(GetQueryString("view")!=null){
        $(".ico-search,.addpallet,.btn_table_delete,.btn_submit").remove()
        table.del=""
        $(".btn_iframe_close_window").html("　关闭　")
    }
    scan()
    dataHavePayload()
    dataBindPallet()
    dataBindPack()
    number()
    clickEvent()
})