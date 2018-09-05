﻿var indexdata=null,selectdata=null,g1=null,g2=null,area=[],position=[],sn_list=[],noallot=new Array(),package=[],colors=0,table={select:'select'}

//基础数据加载
function init(){
    showloading();
    if(selectdata!=null){
        $.each(selectdata,function(i,item){
            $.each(item.position_list,function(j,itemer){
                position.push({
                    position_id:itemer.position_id,
                    position_name:itemer.position_name,
                    area_id:item.area_id
                })
            })
        })

        recommendPosition()
        return false
    }
    var url=uriapi+"/biz/task/shelves/area_list?location_id="+indexdata.location_id
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata=data.body
            parent.setSelectData(selectdata)
            $.each(selectdata,function(i,item){
                $.each(item.position_list,function(j,itemer){
                    position.push({
                        position_id:itemer.position_id,
                        position_name:itemer.position_name,
                        area_id:item.area_id
                    })
                })
            })
            recommendPosition()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//加载推荐仓位
function recommendPosition(){
    if(sn_list.length==0){
        var obj={
            location_id:indexdata.location_id,
            production_batch:indexdata.production_batch,
            qty: indexdata.unstock_task_qty
        }
        var url=uriapi+"/biz/task/shelves/advise_area_list"
        ajax(url,"post",JSON.stringify(obj),function(data){
            if(data.head.status==true){
                layer.close(indexloading);
                $.each(data.body,function(i,item){
                    sn_list.push({
                        wh_id:item.wh_id,
                        area_id:item.area_id,
                        position_id:item.position_id,
                        pallet_code:"",
                        pallet_id:"",
                        max_payload:"",
                        have_payload:"",
                        allot:[],
                    })
                })
                dataPosition()
                dataPack()
            }else{
                layer.close(indexloading)
            }
        }, function(e) {
            layer.close(indexloading);
        })
    }else{
        layer.close(indexloading);
        dataPosition()
        dataPack()
    }
}

//搜索
function search(){
    var is_ajax=true
    var kw=$(".ico-search").val().toUpperCase()
    if(kw=="") return false
    var index=$("#id_div_grid_position tr.selected").attr("data-index")
    var is_delete=false
    //查找本地包装物
    $.each(noallot,function(i,item){
        if(item.package_code==undefined) return true
        if(item.package_code==kw){
            layer.msg("包装物已存在")
            is_ajax=false
        }
    })
    $.each(sn_list,function(i,item){
        $.each(item.allot,function(j,itemer){
            if(itemer.package_code==undefined) return true
            if(itemer.package_code==kw){
                layer.msg("包装物已存在")
                is_ajax=false
            }
        })
    })
    //查找本地托盘
    $.each(sn_list,function(i,item){
        if(item.pallet_code==undefined) return true
        if(item.pallet_code==kw){
            layer.msg("托盘已存在")
            is_ajax=false
        }
    })
    $.each(noallot,function(i,item){
        if(item.pallet_code==undefined) return true
        var is_add=true
        if(item.pallet_code==kw){
            $.each(sn_list,function(j,itemer){
                if(itemer.pallet_code==kw){
                    itemer.allot.push(item)
                    is_add=false
                    return false
                }
            })
            if(is_add && $("#id_div_grid_position tr.selected").length!=0){
                if(sn_list[index].position_id==""){
                    layersMoretips("必填项",$("#id_div_grid_position .body tr").eq(index).find("td").eq(3))
                    is_ajax=false
                    return false
                }
                sn_list[index].allot.push(item)
                sn_list[index].pallet_code=item.pallet_code
                sn_list[index].pallet_id=item.pallet_id
                sn_list[index].max_payload=item.max_payload
                sn_list[index].have_payload=item.have_payload
            }
            is_delete=true
            is_ajax=false
            dataPosition()
            dataPack()
        }
    })
    if(is_delete){
        for(var i=noallot.length-1;i>=0;i--){
            if(noallot[i].pallet_code==kw){
                noallot.splice(i,1)
            }
        }
        dataPosition()
        dataPack()
    }
    if(is_ajax==false) return false
    var obj={
        mat_id:indexdata.mat_id,
        item_id:indexdata.item_id,
        location_id: indexdata.location_id,
        condition: kw,
        type:3,
        receipt_type:indexdata.receipt_type,
        package_type_id:indexdata.package_type_id,
        production_batch:indexdata.production_batch

    }
    showloading();
    var url=uriapi+"/biz/task/shelves/check_code_type"
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.type==1){  //托盘
                if($("#id_div_grid_position tr.selected").length==0){
                    layer.msg("请选择左侧存储区")
                    return false
                }
                if(sn_list[index].position_id==""){
                    layersMoretips("必填项",$("#id_div_grid_position .body tr").eq(index).find("td").eq(3))
                    return false
                }
                if(sn_list[index].position_id!=data.body.position_id && data.body.position_id!=""){
                    layer.msg("仓位信息不一致")
                }else{
                    $.each(sn_list[index].allot,function(i,item){
                        noallot.push(item)
                    })
                    sn_list[index].allot=[]

                    if(data.body.package_list.length==0){
                        sn_list[index].allot=[]
                        sn_list[index].pallet_id=data.body.pallet_id
                        sn_list[index].pallet_code=data.body.pallet_code
                        sn_list[index].max_payload=data.body.max_payload
                        sn_list[index].yuan_have_payload=data.body.have_payload
                        dataPosition()
                        dataPack()
                        return false
                    }

                    $.each(data.body.package_list,function(i,item){
                        var type=0
                        $.each(noallot,function(j,itemer){
                            if(itemer.package_id==item.package_id){
                                noallot.splice(j,1)
                                type=1
                                return false
                            }
                        })
                        $.each(sn_list,function(j,itemer){
                            $.each(itemer.allot,function(t,itemers){
                                if(itemers.package_id==item.package_id){
                                    type=2
                                    return false
                                }
                            })
                            if(type!=0){
                                return false
                            }
                        })

                        if(type!=2){
                            sn_list[index].allot.push({
                                package_code:item.package_code,
                                package_id:item.package_id,
                                unit_zh:item.unit_zh,
                                package_type_code:item.package_type_code,
                                package_type_id:item.package_type_id,
                                qty:item.qty
                            })
                            sn_list[index].pallet_id=data.body.pallet_id
                            sn_list[index].pallet_code=data.body.pallet_code
                            sn_list[index].max_payload=data.body.max_payload
                            sn_list[index].yuan_have_payload=data.body.have_payload
                        }
                    })
                    dataPosition()
                    dataPack()
                }
            }else if(data.body.type==2){    //包
                noallot.push({
                    qty:data.body.qty,
                    package_code:data.body.package_code,
                    package_id:data.body.package_id,
                    unit_zh:data.body.unit_zh,
                    package_type_code:data.body.package_type_code,
                    package_type_id:data.body.package_type_id
                })
                dataPack()
            }
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//增加新托盘
function addpallet(){
    var index=$("#id_div_grid_position tr.selected").attr("data-index")
    if(index==undefined){
        layer.msg("请选择左侧存储区")
        return false
    }
    if(sn_list[index].position_id==""){
        layersMoretips("必填项",$("#id_div_grid_position .body tr").eq(index).find("td").eq(3))
        return false
    }
    showloading()
    var url=uriapi+"/biz/task/shelves/get_new_pallet"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            $.each(sn_list[index].allot,function(i,item){
                noallot.push(item)
            })
            sn_list[index].pallet_id=data.body.pallet_id
            sn_list[index].pallet_code=data.body.pallet_code
            sn_list[index].max_payload=data.body.max_payload
            sn_list[index].have_payload=0
            sn_list[index].allot=[]
            dataPack()
            dataPosition()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//仓位表格数据处理
function dataPosition(){
    if(sn_list.length==0){
        sn_list.push({
            wh_id:selectdata[0].wh_id,
            area_id:"",
            position_id:"",
            submit_menge:"",
            allot:[]
        })
    }
    $.each(sn_list,function(i,item){
        var qty=0
        item.org_name=item.allot.length
        $.each(item.allot,function(j,itemer){
            qty=countData.add(qty,itemer.qty)
        })
        item.have_payload=(item.yuan_have_payload||0)+qty||0
    })
    dataBindPosition()
}

//仓位表格绑定
function dataBindPosition(){
    g1=$("#id_div_grid_position").iGrid({
        columns: [
            { th: '存储区', col: 'area_id', sort:false,type:table.select,data:selectdata,text:"area_name",value:"area_id",children:"position_id",width:110},
            { th: '仓位', col: 'position_id', sort:false,type:table.select,data:position,text:"position_name",value:"position_id",parent:"area_id",width:130},
            { th: '托盘编号', col: 'pallet_code', sort:false,width:80},
            { th: '量大载重', col: 'max_payload', sort:false,width:80},
            { th: '已存放重量', col: 'have_payload', sort:false,width:95},
            { th: '件数', col: 'org_name', sort:false,width:60},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center',width:60},
        ],
        data: sn_list,
        skin:"tablestyle4",
        radio:true,
        isHideRadio:true,
        mb_YesNoCancelRadio:true,
        GetRadioData(a,b){
            colors=b
            dataPack(b)
        },
        delete:function(a,b){
            $.each(sn_list[b].allot,function(i,item){
                noallot.push(item)
            })
            sn_list.splice(b,1)
            dataPosition()
            dataPack()
        },
        cselect:function(a,b,c){
            $.each(sn_list[c].allot,function(i,item){
                noallot.push(item)
            })
            sn_list[c].allot=[]
            sn_list[c].have_payload=""
            sn_list[c].max_payload=""
            sn_list[c].pallet_code=""
            sn_list[c].pallet_id=""
            dataBindPosition()
            dataPack()
        },
        loadcomplete:function(a){//页面绘制完成，返回所有数据。
            color()
        }

    })
    color()
    addOrdel(1)
    numeration()
}

//打包码表格数据处理
function dataPack(index){
    $(".tab a").removeClass("on")
    if(index==undefined || index==null || index==-1){
        package=noallot
        $(".tab a").eq(0).addClass("on")
        $(".relevance").show()
        $("#id_div_grid_pack").addClass("height_overflow")
    }else{
        package=sn_list[index].allot
        $(".tab a").eq(1).addClass("on")
        $(".relevance").hide()
        $("#id_div_grid_pack").removeClass("height_overflow")
    }
    dataBindPack()
}

//变色
function color(){
    if(colors!=-1){
        $("#id_div_grid_position .body tr input").eq(colors).prop("checked","true")
    }
    $("#id_div_grid_position .body tr").removeClass("selected")
    $("#id_div_grid_pack .body tr").removeClass("selected")
    $("#id_div_grid_position .body tr input").each(function(i){
        if($("#id_div_grid_position .body tr input").eq(i).is(":checked")){
            $("#id_div_grid_position .body tr").eq($(this).val()).addClass("selected")
        }
    })
    $("#id_div_grid_pack .body tr input").each(function(i){
        if($("#id_div_grid_pack .body tr input").eq(i).is(":checked")){
            $("#id_div_grid_pack .body tr").eq($(this).val()).addClass("selected")
        }
    })
}

//打包码表格绑定
function dataBindPack(){
    g2=$("#id_div_grid_pack").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false,width:160},
            { th: '包装类型', col: 'package_type_code', sort:false,width:100},
            { th: '单位', col: 'unit_zh', sort:false},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: package,
        skin:"tablestyle4",
        checkbox:true,
        isHideCheckbox:true,
        delete:function(a,b){
            var index=$("#id_div_grid_position tr.selected").attr("data-index")
            noallot.push(a)
            sn_list[index].allot.splice(b,1)
            dataPack(index)
            dataPosition()
        },
        loadcomplete:function(a){//页面绘制完成，返回所有数据。
            $("#id_div_grid_pack .body tr input").removeAttr("checked")
            color()
        },
        GetSelectedData(a){//选择行后触发
            color()
        }
    })
    addOrdel(2)
    if($(".tab a").eq(0).is(".on")){
        $("#id_div_grid_pack .btn_table_delete").remove()
    }
    numeration()
    check()
    color()
}

//增加 删除样式处理
function addOrdel(type){
    if(type==1){
        $(".btn_table_add").remove()
        $("#id_div_grid_position .coldelete").each(function(i){
            $(this).prepend("<a href='javascript:void(0)' class='btn_table_add' data-index-row='"+i+"'></a>")
            $(this).find(".btn_table_delete").html("")
        })
        if(sn_list.length<=1){
            $("#id_div_grid_position .coldelete a").eq(1).remove()
        }
    }
    if(type==2){
        $("#id_div_grid_pack .coldelete").each(function(i){
            $(this).find(".btn_table_delete").html("")
        })
    }
    addPosition()
}

//计算本次上架数量
function numeration(){
    var qty=0
    $.each(sn_list,function(i,item){
        $.each(item.allot,function(j,itemer){
            if(itemer.qty!=undefined){
                qty=countData.add(qty,itemer.qty)
            }
        })
    })
    $(".stock_task_qty").html(qty)
}

//关联到托盘
function relevance(){
    var left=$("#id_div_grid_position tr.selected").attr("data-index")
    if(left==undefined){
        layer.msg("请选择左侧存储区进行关联")
        return false
    }
    if($("#id_div_grid_pack .chk input:checked").length==0){
        layer.msg("请选择右侧包装物进行关联")
        return false
    }
    if(sn_list[left].area_id=="" || sn_list[left].position_id==""){
        layer.msg("请选择左侧存储区和仓位")
        return false
    }
    if(sn_list[left].pallet_id=="" || sn_list[left].pallet_id==undefined){
        layer.msg("请先添加托盘")
        return false
    }
    var packdiv=$("#id_div_grid_pack .chk input:checked")
    var qty=0
    packdiv.each(function(i){
        var index=$(this).val()
        qty=countData.add(qty,noallot[index].qty)
    })

    if(sn_list[left].max_payload<(sn_list[left].have_payload+qty)){
        layer.msg("已超过最大重量")
        return false
    }

    packdiv.each(function(i){
        var index=$(this).val()
        sn_list[left].allot.push(noallot[index])
    })
    for(var i=packdiv.length-1;i>=0;i--){
        var index=packdiv.val()
        noallot.splice(index,1)
    }
    dataPack()
    dataPosition()
}

//添加存储区 仓位
function addPosition(){
    $("#id_div_grid_position .btn_table_add").unbind("click").click(function () {
        var index=$("#id_div_grid_position .btn_table_add").index(this)
        var rid=0
        sn_list.splice(index+1,0,{
            wh_id:selectdata[0].wh_id,
            area_id:sn_list[index].area_id,
            position_id:sn_list[index].position_id,
            submit_menge:sn_list[index].submit_menge,
            allot:[],
        })
        dataPosition()
    });
}

//查看
function check(){
    if(GetQueryString("view")!=null){
        $(".btn_submit,.relevance,.tab").remove()
        $(".search .keyinput").remove()
        $(".btn_iframe_close_window").html("关闭")
        $(".coldelete").html("")
        table.select=""
        $(".body .colselect select").each(function () {
            var html = $(this).find("option:selected").html()
            $(this).parent().html(html)
        })
        sn_list=[]
        $.each(indexdata.pallet_package_list,function(i,item){
            var is_add=true
            $.each(sn_list,function(j,itemer){
                if(item.pallet_code==itemer.pallet_code){
                    sn_list[j].allot.push({
                        package_code:item.package_code,
                        package_id:item.package_id,
                        unit_zh:item.unit_zh,
                        package_type_code:item.package_type_code,
                        package_type_id:item.package_type_id,
                        qty:item.qty
                    })
                    is_add=false
                }
            })

            if(is_add){
                var h=sn_list.length
                sn_list[h]={}
                sn_list[h].allot=[]
                sn_list[h].allot.push({
                    package_code:item.package_code,
                    package_id:item.package_id,
                    unit_zh:item.unit_zh,
                    package_type_code:item.package_type_code,
                    package_type_id:item.package_type_id,
                    qty:item.qty
                })
                sn_list[h].area_id=item.area_code
                sn_list[i].yuan_have_payload=item.have_payload-item.qty
                sn_list[h].max_payload=item.max_payload
                sn_list[h].org_name=item.org_name
                sn_list[h].pallet_code=item.pallet_code
                sn_list[h].pallet_id=item.pallet_id
                sn_list[h].position_id=item.position_code
                sn_list[h].submit_menge=item.submit_menge
                sn_list[h].wh_id=item.wh_id
            }
        })
    }
}

//提交
function submit(){
    if(parseFloat($(".unstock_task_qty").html())<parseFloat($(".stock_task_qty").html())){
        layer.msg("本次上架数量不能大于未上架数量")
        return false
    }
    parent.setPosition2(sn_list,noallot,$(".stock_task_qty").html(),GetQueryString("index"),selectdata)
    $(".btn_iframe_close_window").click()
}

$(function () {
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    selectdata=parent.getSelectData()
    indexdata = data
    check()
    scan()
    $(".unstock_task_qty").html(indexdata.unstock_task_qty)
    if(GetQueryString("view")==null){
        if(indexdata.position2.length==0){
            $.each(data.pallet_package_list, function (i, item) {
                noallot[i] = {}
                noallot[i].batch = item.batch
                noallot[i].package_code = item.package_code
                noallot[i].package_id = item.package_id
                noallot[i].package_type_code = item.package_type_code
                noallot[i].package_type_id = item.package_type_id
                noallot[i].pallet_code = item.pallet_code
                noallot[i].pallet_id = item.pallet_id
                noallot[i].qty = item.qty
                noallot[i].max_payload = item.max_payload
                noallot[i].unit_zh = item.unit_zh

                noallot[i].wh_id = item.wh_id || ""
                noallot[i].area_id = item.area_id || ""
                noallot[i].position_id = item.position_id || ""
                noallot[i].submit_menge = item.submit_menge || ""
            })
        }else{
            $.each(data.noallot2, function (i, item) {
                noallot[i] = {}
                noallot[i].batch = item.batch
                noallot[i].package_code = item.package_code
                noallot[i].package_id = item.package_id
                noallot[i].package_type_code = item.package_type_code
                noallot[i].package_type_id = item.package_type_id
                noallot[i].pallet_code = item.pallet_code
                noallot[i].pallet_id = item.pallet_id
                noallot[i].qty = item.qty
                noallot[i].max_payload = item.max_payload

                noallot[i].wh_id = item.wh_id || ""
                noallot[i].area_id = item.area_id || ""
                noallot[i].position_id = item.position_id || ""
                noallot[i].submit_menge = item.submit_menge || ""
            })
        }
    }


    if(indexdata.position2.length!=0){
        $.each(indexdata.position2,function(i,item){
            sn_list[i]={}
            sn_list[i].allot=item.allot
            sn_list[i].area_id=item.area_id
            sn_list[i].have_payload=item.have_payload
            sn_list[i].max_payload=item.max_payload
            sn_list[i].org_name=item.org_name
            sn_list[i].pallet_code=item.pallet_code
            sn_list[i].pallet_id=item.pallet_id
            sn_list[i].position_id=item.position_id
            sn_list[i].submit_menge=item.submit_menge
            sn_list[i].wh_id=item.wh_id
        })
    }
    if(GetQueryString("view")==null){
        init()
    }else{
        dataPosition()
        dataPack()
    }


    $(".tab a").click(function () {
        $(".tab a").removeClass("on")
        var index = $(this).index()
        if (index == 0) {
            $(".tab a").eq(0).addClass("on")
            dataPack()
        } else {
            $(".tab a").eq(1).addClass("on")
            dataPack($("#id_div_grid_position .body tr.selected").attr("data-index"))
        }
    })
    $(".relevance").click(function () {
        relevance()
    })
    $("a.search").click(function(){
        search()
    })
    $(".btn_submit").click(function(){
        submit()
    })
    $(".addpallet").click(function(){
        addpallet()
    })
});