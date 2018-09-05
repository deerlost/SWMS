var indexdata={},g=null,selectdata={},table={text:'text',popup:'',select:'select',checkbox:true};

//加载基础下拉
function baseInfo(){
    showloading();
    var url=uriapi+"/biz/transport/baseinfo_byuser"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            selectdata=data.body
            dataBindBase()
            if(!GetQueryString("id")){
                closeTip()
            }
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//加载入库单
function info(){
    var url=uriapi+"/biz/transport/urgentdump_byid/"+GetQueryString("id")
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            indexdata=data.body
            dataBind()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定下拉数据
function dataBindBase() {
    $(".move_type").append("<option value='' style='display:none'>请选择</option>")
    $.each(selectdata.move_list, function (i, item) {
        $(".move_type").append("<option value='" + item.move_type_id + "'>" + item.move_type_code + "-" + item.move_type_name + "</option>")
    })

    $(".out_fty").append("<option value='' style='display:none'>请选择</option>")
    $.each(selectdata.fty_out_list, function (i, item) {
        $(".out_fty").append("<option value='" + item.fty_id + "' data-index='" + i + "'>" + item.fty_name + "</option>")
    })

    $(".in_fty").append("<option value='' style='display:none'>请选择</option>")
    $.each(selectdata.fty_in_list, function (i, item) {
        $(".in_fty").append("<option value='" + item.fty_id + "' data-index='" + i + "'>" + item.fty_name + "</option>")
    })

    $.each(selectdata.class_type_list, function (i, item) {
        $(".class_type_id").append("<option value='" + item.class_type_id + "'>" + item.class_name + "</option>")
    })

    $.each(selectdata.syn_list, function (i, item) {
        $(".syn_type_id").append("<option value='" + item.syn_type + "'>" + item.syn_type_name + "</option>")
    })

    $(".class_type_id").val(selectdata.class_type_id)
    $(".syn_type_id").val(selectdata.syn_type_id)
    $("[c-model='create_user']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    dataInputFty()
    outputLoction()

}

//绑定数据
function dataBind(){

    $(".move_type").parent().html(indexdata.move_type_code+"-"+indexdata.move_type_name);
    $(".class_type_id").parent().html("<span c-model='class_name'>--</span>");
    $(".syn_type_id").parent().html("<span c-model='syn_type_name'>--</span>");
    $(".out_fty").parent().html("<span c-model='fty_output_name'>--</span>");
    $(".out_loction").parent().html("<span>"+indexdata.loc_output_code+"-"+indexdata.loc_output_name+"</span>");
    $(".in_fty").parent().html("<span c-model='fty_input_name'>--</span>");
    $(".in_loction").parent().html("<span>"+indexdata.loc_input_code+"-"+indexdata.loc_input_name+"</span>");
    $(".spanno").html(indexdata.stock_transport_code)
    $(".h2red").html(indexdata.status_name)
    $(".matData").show()
    $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
    $(".btnAddMaterial").remove()

    if(indexdata.status!=0){
        $(".btnsubmit").remove()
    }
    if(indexdata.status==10){
        $(".doc_code").show()
        $(".writeOff").show()
    }
    if(indexdata.status==0){
        $(".btnoperate").show()
    }
    if(indexdata.status==20){
        $(".doc_code").show()
        $(".writeType").show()
        $(".mat_color").show()
        $.each(indexdata.item_list,function(i,item){
            item.tr=1
        })
    }
    CApp.initBase("base",indexdata)

    $.each(indexdata.item_list,function(i,item){
        item.fty_input=item.fty_code+"-"+item.fty_name
        item.location_input=item.location_code+"-"+item.location_name
    })
    dataBindTable()
}

//加载发出库存地点
function outputLoction(){
    $(".out_loction option").remove();
    $(".out_loction").append("<option value='' style='display:none'>请选择</option>");
    var index=$(".out_fty option:selected").attr("data-index");
    if(index!=undefined){
        $.each(selectdata.fty_out_list[index].location_ary,function(i,item){
            $(".out_loction").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
        })
    }
}

//接收工厂 库存地点数据处理
function dataInputFty(){
    $(".in_loction option").remove();
    $(".in_loction").append("<option value='' style='display:none'>请选择</option>");
    var index=$(".in_fty option:selected").attr("data-index");
    if(index!=undefined){
        $.each(selectdata.fty_in_list[index].location_ary,function(i,item){
            $(".in_loction").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
        })
    }
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code',sort:false},
            { th: '物料描述', col: 'mat_name',sort:false},
            { th: '单位', col: 'unit_name',sort:false},
            { th: '库存数量', col: 'stock_qty',sort:false,type:'number'},
            { th: '包装类型', col: 'package_type_code',sort:false},
            { th: '转储数量', col: 'transport_qty',sort:false,width:150,type:table.text,class:'transport_qty',
                dowhile:{regex:'>rowdata[stock_qty]',tips:"转储数量不能大于库存数量"},
                decimal:{regex:3}
            },
            { th: '生产批次', col: 'product_batch',sort:false},
            { th: 'ERP批次', col: 'erp_batch',sort:false},
            { th: '备注', col: 'remark', sort:false,type:"popup",width:80,
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.popup,
                    area:"500px,400px",
                    title:"备注",
                    args:["mat_code|id"]
                }
            },
            {th: '行类型', col: 'tr', class: "", type: "tr", tr: {
                value: 1,
                class: "pinkbg"
            }}
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        absolutelyWidth:true,
        checkbox:table.checkbox,
        required:{
            columns:["transport_qty"],
            isAll:false
        }
    });
}

//判断目标物料
function is_move(data,index){
    if(data==""){
        indexdata.item_list[index].is_mat_input=true
        return false
    }
    var obj={
        mat_input:data,
        move_type_id:17
    }
    showloading()
    var url=uriapi+"/biz/transport/is_move"
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.keyword==true){
                indexdata.item_list[index].is_mat_input=true
            }else{
                indexdata.item_list[index].is_mat_input=false
                layersMoretips("目标物料未找到",$(".txtmat_input").eq(index))
            }
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//获取基础备注信息
function GetRemark(){
    return indexdata.remark||""
}

//更换基础备注信息
function SetRemark(data){
    indexdata.remark=data
}

//获取物料备注信息
function GetNote(id,index) {
    return indexdata.item_list[index].remark||"";
}

//修改物料备注
function SetNote(id, text,index) {
    indexdata.item_list[index].remark = text;
}

//添加物料
function addMaterial(data){
    $.each(data,function(i,item){
        var is_add=true
        $.each(indexdata.item_list,function(j,itemer){
            if(item.unique_id==itemer.unique_id){
                is_add=false
                return false
            }
        })
        if(is_add){
            indexdata.item_list.push(item)
        }
    })
    dataBindTable()
}

//切换移动类型
function moveType(){
    $(".btnAddMaterial").attr({"data-title":"添加物料","data-href":"popup_material.html","data-size":"950px,500px"})
    tableedit($("body"))
    $(".disabled").removeClass("disabled")
}

//click事件
function clickEvent(){
    //删除此单据
    $(".btndelete").click(function() {
        layer.confirm("是否删除", {
            btn: ['删除', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            var url=uriapi+"/biz/transport/urgent/delete/"+GetQueryString("id")
            showloading();
            ajax(url,"get",null,function(data){
                if(data.head.status==true){
                    setTimeout(function () { location.href = "list.html"; }, 2000);
                }else{
                    layer.close(indexloading)
                }
            }, function(e) {
                layer.close(indexloading);
            },true)
        })
    })

    //过帐
    $(".btnsubmit").click(function() {
        layer.confirm("是否过帐", {
            btn: ['过帐', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            if(indexdata.status==0){
                submit0()
            }else{
                submit()
            }
        })
    })

    //冲销
    $(".writeOff").click(function(){
        layer.confirm("是否冲销", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function() {
            writeOff()
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {
        });

    })

    $(".btn_delete_mat").click(function() {
        if ($(".body .chk input:checked").length == 0) {
            layer.msg("请选择删除物料")
            return false
        }
        layer.confirm("是否删除物料信息", {
            btn: ['删除', '取消'],
            icon: 3
        }, function () {
            for(var i=$(".body .chk input:checked").length-1;i>=0;i--){
                var index=$(".body .chk input:checked").eq(i).val()
                indexdata.item_list.splice(index,1)
                layer.close(parseInt($(".layui-layer-shade").attr("times")));
            }
            dataBindTable()
        })
    })

}

//获取当前工厂和库存地点
function getFtyLoction(){
    return [$(".out_fty").val(),$(".out_loction").val()]
}

//冲销
function writeOff(){
    var url=uriapi+"/biz/transport/urgent/write_off/"+GetQueryString("id")
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            setTimeout(function () { location.href = "detail.html?id="+GetQueryString("id"); }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
}

//提交
function submit(){
    var is_checked=true,item_list=[],stock_transport_rid=1
    $.each(indexdata.item_list,function(i,item){

        if($(".txttransport_qty").eq(i).val()>0){

            if(item.package_standard_ch!=1 && countData.mod(item.transport_qty,item.package_standard)!=0){
                layersMoretips("转储数量为包装规格的整数倍",$(".transport_qty").eq(i))
                is_checked=false
            }

            item_list.push({
                batch:item.batch,
                erp_batch:item.erp_batch,
                wh_id:item.wh_id,
                mat_id:item.mat_id,
                package_type_id:item.package_type_id,
                quality_batch:item.quality_batch,
                transport_qty:item.transport_qty,
                pack_num:item.pack_num,
                stock_qty:item.stock_qty,
                product_batch:item.product_batch,
                unit_id:item.unit_id,
                remark:item.remark==undefined?"":item.remark,
            })
        }
    })

    if(item_list.length==0){
        layer.msg("至少添加一条物料信息")
        return false
    }

    if($(".in_fty").val()==""){
        layersMoretips("必填项",$(".in_fty"))
        is_checked=false
    }

    if($(".in_loction").val()==""){
        layersMoretips("必填项",$(".in_loction"))
        is_checked=false
    }
    if(!g.checkData()){
        is_checked=false
    }
    if(is_checked==false) return false

    var obj={
        stock_transport_id:GetQueryString("id")||"",
        move_type_id:$(".move_type").val(),
        class_type_id:$(".class_type_id").val(),
        syn_type:$(".syn_type_id").val(),
        fty_output_id:$(".out_fty").val(),
        loc_output_id:$(".out_loction").val(),
        fty_input_id:$(".in_fty").val(),
        loc_input_id:$(".in_loction").val(),
        remark:indexdata.remark==undefined?"":indexdata.remark,
        item_list:item_list
    }

    console.log(obj)
    var url=uriapi+"/biz/transport/create_urgentdump"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_transport_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)

}

//未完成提交
function submit0(){
    var item_list=[]
    $.each(indexdata.item_list,function(i,item){
        item_list.push({
            batch:item.batch,
            mat_id:item.mat_id,
            package_type_id:item.package_type_id,
            transport_qty:item.transport_qty,
            erp_batch:item.erp_batch,
        })
    })
    var obj={
        item_list:item_list,
        stock_transport_id:GetQueryString("id"),
        move_type_id:indexdata.move_type_id,
        fty_output_id:indexdata.fty_output_id,
        loc_output_id:indexdata.location_output_id,
        fty_input_id:indexdata.fty_input_id,
        loc_input_id:indexdata.location_input_id,
    }
    console.log(obj)
    var url=uriapi+"/biz/transport/create_urgentdump"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_transport_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
}

$(function(){
    indexdata.item_list=[]
    tableedit($(".divbase"))
    if(GetQueryString("id")==null){
        $(".btn_delete_mat").show()
        baseInfo()
    }else{
        table={text:'number',popup:'?view=1',select:'',checkbox:false}
        info()
    }

    dataBindTable()
    clickEvent()
    $(".move_type").change(function(){
        indexdata.item_list=[]
        dataBindTable()
        moveType()
    })

    $(".out_fty").change(function(){
        indexdata.item_list=[]
        dataBindTable()
        outputLoction()
    })

    $(".in_fty").change(function(){
        dataInputFty()
    })

    $(".out_loction").change(function(){
        indexdata.item_list=[]
        dataBindTable()
    })

})