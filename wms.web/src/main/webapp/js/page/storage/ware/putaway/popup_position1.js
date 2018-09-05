var indexdata=null,selectdata=null,g1=null,sn_list=new Array(),position=[],material=[],tabletype={input:"text",select:"select",qty:"text",number:"text"},req=/^(0|[1-9]\d*)(\.\d{1,1})?$/

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
    if(sn_list.length==0) {
        var obj = {
            location_id: indexdata.location_id,
            production_batch: indexdata.production_batch,
            qty: indexdata.unstock_task_qty
        }
        var url = uriapi + "/biz/task/shelves/advise_area_list"
        ajax(url, "post", JSON.stringify(obj), function (data) {
            if (data.head.status == true) {
                layer.close(indexloading);
                $.each(data.body, function (i, item) {
                    sn_list.push({
                        area_id: item.area_id,
                        position_id: item.position_id,
                        pallet_code: "",
                        max_payload: "",
                        decimal_place: indexdata.decimal_place,
                        qty: "",
                        number: "",
                        wh_id: item.wh_id
                    })
                    dataMaterial(item.area_id, item.position_id, i)
                })
                dataBindTable()
            } else {
                layer.close(indexloading)
                dataBindTable()
            }
        }, function (e) {
            layer.close(indexloading);
        })
    }else{
        layer.close(indexloading);
        dataBindTable()
    }
}

//获取已有物资
function dataMaterial(area_id,position_id,index){
    var is_load=true
    $.each(material,function(i,item){
        if(item.position_id==position_id){
            sn_list[index].pallet_code=item.haven_material.replace(/,/g,"<br/>")
            is_load=false
            dataBindTable()
            return false
        }
    })
    if(is_load==false) return false
    var url=uriapi+"/biz/task/shelves/position_list"
    var obj={
        position_id:position_id,
        area_id: area_id,
        wh_id: selectdata[0].wh_id
    }

    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            material.push(data.body)
            dataMaterial(area_id,data.body.position_id,index)
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定表格
function dataBindTable(){
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '存储区', col: 'area_id', sort:false,type:tabletype.select,data:selectdata,text:"area_name",value:"area_id",children:"position_id",width:180},
            { th: '目标仓位', col: 'position_id', sort:false,type:tabletype.select,data:position,text:"position_name",value:"position_id",parent:"area_id",width:180},
            { th: '已有物资', col: 'pallet_code', sort:false,min:390},
            { th: '件数', col: 'number', sort:false,type:tabletype.number,width:130,decimal:{regex:0}},
            { th: '上架数量', col: 'qty', sort:false,class:'qty',type:tabletype.qty,width:130,decimal:{regex:3}},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: sn_list,
        skin:"tablestyle4",
        required:{
            columns:["qty"],
            isAll:false
        },
        percent:10,
        cselect:function(a,b,c,d){
            if(a=="area_id"){
                sn_list[c].pallet_code=""
                dataBindTable()
            }else if(a=="position_id"){
                dataMaterial(d.area_id,d.position_id,c)
            }
        },
        cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
            var is_return=qtyNum(a,d,c)
            if(is_return==false) return false

            numeration()
        },
        delete:function(a,b){//删除时调用
            sn_list.splice(b,1)
            dataBindTable()
        },
        loadcomplete:function(a){
            if(indexdata.package_standard_ch==1){
                $(".txtnumber").remove()
            }
        }
    })
    numeration()
    if(GetQueryString("view")!=null) {
        g1.displaycolumn([2,5],false)
        $(".btn_table_delete").remove()
        $(".body .colselect select").each(function () {
            var html = $(this).find("option:selected").html()
            $(this).parent().html(html)
        })
    }
}

//计算件数
function qtyNum(key,data,index){
    var is_checked=true
    if(data==""){
        sn_list[index].qty=""
        sn_list[index].number=""
        $(".txtnumber").eq(index).val("")
        $(".txtqty").eq(index).val("")
        if(indexdata.package_standard_ch!=1){
        }else{
            $(".txtqty").eq(index).val("")
        }



    }else{
        if(key=="qty"){
            sn_list[index].number=countData.div(sn_list[index].qty,indexdata.package_standard)
            sn_list[index].qty=data
            $(".txtnumber").eq(index).val(sn_list[index].number)
        }else{
            sn_list[index].qty=countData.mul(sn_list[index].number,indexdata.package_standard)
            sn_list[index].number=data
            $(".qty").eq(index).find("input").val(sn_list[index].qty)
        }
    }
    return is_checked
}

//计算本次上架数量
function numeration(){
    var qty=0
    $.each(sn_list, function (i, item) {
        if (item.qty != undefined && item.qty!="") {
            qty=countData.add(qty,item.qty)
        }
    });
    $("[c-model='task_qty']").html(qty)
}

//添加行项目
function addPosition(){
    sn_list.push({
        area_id:"",
        position_id:"",
        pallet_code:"",
        max_payload:"",
        decimal_place:indexdata.decimal_place,
        qty:"",
        number:"",
        wh_id:selectdata[0].wh_id
    })
    dataBindTable()
}

//提交
function submit(){
    var is_checked=true
    if(parseFloat($("[c-model='unstock_task_qty']").html())<parseFloat($("[c-model='task_qty']").html())){
        layer.msg("本次上架数量不能大于未上架数量")
        return false
    }
    if(!g1.checkData()){
        is_checked=false
    }
    $.each(sn_list,function(i,item){
        if(item.qty!="" && item.qty!="0"){
            if(item.area_id==""){
                layersMoretips("必填项",$(".body tr").eq(i).find("td").eq(1))
                is_checked=false
            }
            if(item.position_id==""){
                layersMoretips("必填项",$(".body tr").eq(i).find("td").eq(2))
                is_checked=false
            }

            if(indexdata.package_standard_ch!=1 && countData.mod(item.qty,indexdata.package_standard)!=0){
                layersMoretips("上架数量为包装规格的整数倍",$(".qty").eq(i))
                is_checked=false
            }
        }
    })

    if(is_checked==false) return false
    parent.setPosition1(sn_list,$("[c-model='task_qty']").html(),GetQueryString("index"))
    $(".btn_iframe_close_window").click()
}

$(function () {
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    selectdata=parent.getSelectData()
    CApp.initBase("base",indexdata)
    $("[c-model='unstock_task_qty']").html(indexdata.unstock_task_qty)
    if(GetQueryString("view")!=null){
        $(".btn_submit,.add_position").remove()
        $(".btn_iframe_close_window").html("关闭")
        tabletype.qty=""
        tabletype.number=""
        tabletype.select=""
        $.each(indexdata.pallet_package_list,function(i,item){
            sn_list[i]={}
            sn_list[i].area_id=item.area_code
            sn_list[i].decimal_place=item.decimal_place
            sn_list[i].max_payload=item.max_payload
            sn_list[i].pallet_code=item.pallet_code
            sn_list[i].position_id=item.position_code
            sn_list[i].qty=item.qty
            sn_list[i].wh_id=item.wh_id
            if(item.qty!="" && !req.test(data)){
                sn_list[i].number=Math.ceil(countData.div(item.qty,indexdata.package_standard))
                sn_list[i].number=parseFloat(sn_list[i].number)
            }

        })
    }
    if(indexdata.position1.length!=0){
        $.each(indexdata.position1,function(i,item){
            sn_list[i]={}
            sn_list[i].area_id=item.area_id
            sn_list[i].decimal_place=item.decimal_place
            sn_list[i].max_payload=item.max_payload
            sn_list[i].pallet_code=item.pallet_code
            sn_list[i].position_id=item.position_id
            sn_list[i].qty=item.qty
            sn_list[i].wh_id=item.wh_id
            sn_list[i].number=""
            if(item.qty!="" && !req.test(data)){
                sn_list[i].number=Math.ceil(countData.div(item.qty,indexdata.package_standard))
                sn_list[i].number=parseFloat(sn_list[i].number)
            }
        })
    }

    if(GetQueryString("view")==null){
        init()
    }else{
        dataBindTable()
    }

    $(".add_position").click(function(){
        addPosition()
    })

    $(".btn_submit").click(function(){
        submit()
    })
});