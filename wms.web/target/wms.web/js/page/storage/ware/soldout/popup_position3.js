﻿﻿var indexdata=null,selectdata=null,g1=null,g2=null,area=[],position=[],sn_list=[],noallot=new Array(),package=[],colors=-1,table={select:'select'}

//基础数据加载
function init(){
    showloading();
    if(selectdata){
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
    var obj={};
    obj.mat_id=indexdata.mat_id;
    obj.batch=indexdata.batch;
    obj.location_id=indexdata.location_id;
    obj.refer_receipt_type = indexdata.receipt_type;
    var url=uriapi+"/biz/task/removal/area_list";
    ajax(url,"POST",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            selectdata=data.body
            parent.setSelectData(selectdata,GetQueryString("index"))
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
            setTimeout(function(){
                $(".btn_iframe_close_window").click();
            },2000)
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
            qty: indexdata.unstock_task_qty,
            mat_id:indexdata.mat_id,
            batch:indexdata.batch,
            refer_receipt_type:indexdata.receipt_type
        }
        var url=uriapi+"/biz/task/removal/advise_area_list"
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
        item.org_name=item.allot.length
    })
    dataBindPosition()
}

//仓位表格绑定
function dataBindPosition(){
    g1=$("#id_div_grid_position").iGrid({
        columns: [
            { th: '存储区', col: 'area_id', sort:false,type:table.select,data:selectdata,text:"area_name",value:"area_id",children:"position_id",width:200},
            { th: '仓位', col: 'position_id', sort:false,type:table.select,data:position,text:"position_name",value:"position_id",parent:"area_id",width:200},
            { th: '件数', col: 'org_name', sort:false,width:120},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
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
            sn_list.splice(b,1)
            dataPosition()
            dataPack()
        },
        cselect:function(a,b,c){
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
    check()
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

//打包码表格数据处理
function dataPack(){
    var index=$("#id_div_grid_position tr.selected").attr("data-index")
    if(index==undefined || index==null || index==-1){
        package=[]
    }else {
        package = sn_list[index].allot
        $("#id_div_grid_pack").removeClass("height_overflow")
    }
    dataBindPack()
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

//搜索
function search(){
    var is_ajax=true
    var kw=$(".ico-search").val().toLowerCase()
    var index=$("#id_div_grid_position tr.selected").attr("data-index")
    var is_delete=false
    if(kw=="")  return false
    //查找本地包装物
    $.each(sn_list,function(i,item){
        $.each(item.allot,function(j,itemer){
            if(itemer.package_code==undefined) return true
            if(itemer.package_code.toLowerCase()==kw){
                layer.msg("包装物已存在")
                is_ajax=false
            }
        })
    })

    $.each(noallot,function(i,item){
        if(item.package_code==undefined) return true
        if(item.package_code.toLowerCase()==kw){
            if($("#id_div_grid_position tr.selected").length==0){
                layer.msg("请选择左侧存储区")
                return false
            }
            sn_list[index].allot.push(item)
            noallot.splice(i,1)
            is_delete=false
            is_ajax=false
            dataPosition()
            dataPack()
        }
    })


    //查找本地托盘
    $.each(noallot,function(i,item){
        if(item.pallet_code==undefined) return true
        if(item.pallet_code.toLowerCase()==kw && $("#id_div_grid_position tr.selected").length!=0){
            sn_list[index].allot.push(item)
            sn_list[index].pallet_code=item.pallet_code
            sn_list[index].pallet_id=item.pallet_id
            sn_list[index].max_payload=item.max_payload
            sn_list[index].have_payload=item.have_payload
            is_delete=true
            is_ajax=false
        }
    })
    if(is_delete){
        for(var i=noallot.length-1;i>=0;i--){
            if(noallot[i].pallet_code.toLowerCase()==kw){
                noallot.splice(i,1)
            }
        }
    }
    if($("#id_div_grid_position tr.selected").length==0){
        layer.msg("请选择左侧存储区")
        return false
    }
    if(is_ajax==false) return false
    var obj={
        mat_id:indexdata.mat_id,
        item_id:indexdata.item_id,
        mat_store_type:indexdata.mat_store_type,
        location_id: indexdata.location_id,
        condition: kw,
        type:3,
        production_batch:indexdata.production_batch,
        batch:indexdata.batch,
        refer_receipt_type:indexdata.receipt_type,
        package_type_id:indexdata.package_type_id,
        position_id:sn_list[index].position_id
    }
    showloading();
    var url=uriapi+"/biz/task/removal/check_code_type"
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.type==1) {
                layer.msg("无效包装物")
            }
            if(data.body.type==2){
                if(sn_list[index].position_id!=data.body.position_id && data.body.position_id!=""){
                    layer.msg("数据不合法")
                    return false
                }

                sn_list[index].allot.push({
                    package_code:data.body.package_code,
                    package_id:data.body.package_id,
                    unit_zh:data.body.unit_zh,
                    package_type_code:data.body.package_type_code,
                    package_type_id:data.body.package_type_id,
                    qty:data.body.qty,
                    position_id:data.body.position_id,
                })
                dataPack(index)
                dataPosition()
            }
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
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

//提交
function submit(){
    if(parseFloat($(".unstock_task_qty").html())<parseFloat($(".stock_task_qty").html())){
        layer.msg("本次上架数量不能大于未上架数量")
        return false
    }
    parent.setPosition3(sn_list,noallot,$(".stock_task_qty").html(),GetQueryString("index"))
    $(".btn_iframe_close_window").click()
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
        var rid=0
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
                sn_list[rid]={}
                sn_list[rid].allot=[]
                sn_list[rid].allot.push({
                    package_code:item.package_code,
                    package_id:item.package_id,
                    unit_zh:item.unit_zh,
                    package_type_code:item.package_type_code,
                    package_type_id:item.package_type_id,
                    qty:item.qty
                })
                sn_list[rid].area_id=item.area_code
                sn_list[rid].have_payload=item.have_payload
                sn_list[rid].max_payload=item.max_payload
                sn_list[rid].org_name=item.org_name
                sn_list[rid].pallet_code=item.pallet_code
                sn_list[rid].pallet_id=item.pallet_id
                sn_list[rid].position_id=item.position_code
                sn_list[rid].position_code=item.position_code
                sn_list[rid].submit_menge=item.submit_menge
                sn_list[rid].wh_id=item.wh_id
                rid++
            }
        })
    }
}

$(function () {
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    selectdata=indexdata.selectdata
    $(".unstock_task_qty").html(indexdata.unstock_task_qty)
    check()
    scan()
    if(GetQueryString("view")==null) {
        if (indexdata.position3.length == 0) {
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

                noallot[i].wh_id = item.wh_id || ""
                noallot[i].area_id = item.area_id || ""
                noallot[i].position_id = item.position_id || ""
                noallot[i].submit_menge = item.submit_menge || ""
            })
        } else {
            $.each(data.noallot3, function (i, item) {
                noallot[i] = {}
                noallot[i].batch = item.batch
                noallot[i].package_code = item.package_code
                noallot[i].package_id = item.package_id
                noallot[i].package_type_code = item.package_type_code
                noallot[i].package_type_id = item.package_type_id
                noallot[i].pallet_code = item.pallet_code
                noallot[i].pallet_id = item.pallet_id
                noallot[i].qty = item.qty

                noallot[i].wh_id = item.wh_id || ""
                noallot[i].area_id = item.area_id || ""
                noallot[i].position_id = item.position_id || ""
                noallot[i].submit_menge = item.submit_menge || ""
            })
        }
    }


    if(indexdata.position3.length!=0){
        $.each(indexdata.position3,function(i,item){
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