var indexdata={},selectdata=null,table={remark:"",checkbox:true}

//数据加载
function init(){
    var url=uriapi+"/biz/input/other/other_base_info"
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata=data.body
            baseData()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    if(GetQueryString("id")==null){
        indexdata.item_list=[];
        baseData()
    }else{
        var url=uriapi+"/biz/input/other/other_input_info/"+GetQueryString("id")
        ajax(url,"get",null,function(data){
            if(data.head.status==true){
                indexdata=data.body
                baseData()
            }else{
                layer.close(indexloading)
            }
        }, function(e) {
            layer.close(indexloading);
        })
    }
}

//基础信息
function baseData(){;
    if(selectdata==null || JSON.stringify(indexdata) == "{}") return false;
    layer.close(indexloading);
    //indexdata.status=2
    $(".h2red").html(indexdata.status_name)
    if(indexdata.status==3){
        $(".btninput").show()
    }
    $("[c-model='move_type_id']").append("<option style='display: none'  value=''>请选择</option>");
    $("[c-model='fty_id']").append("<option style='display: none'  value=''>请选择</option>");
    $("[c-model='location_id']").append("<option style='display: none'  value=''>请选择</option>");
    $.each(selectdata.fty_list,function(i,item){
        $("[c-model='fty_id']").append("<option value='"+item.fty_id+"' data-index='"+i+"'>"+item.fty_name+"</option>")
    })
    $.each(selectdata.move_list,function(i,item){
        $("[c-model='move_type_id']").append("<option value='"+item.move_type_id+"'>"+item.move_type_code+"-"+item.move_type_name+"</option>")
    })
    $.each(selectdata.class_type_list,function(i,item){
        $("[c-model='class_type_id']").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(selectdata.product_line_list,function(i,item){
        $(".product_line_id").append("<option value='"+item.product_line_id+"' data-index='"+i+"'>"+item.product_line_name+"</option>")
    })

    $("[c-model='class_type_id']").val(selectdata.class_type_id)
    if(GetQueryString("id")!=null){
        $(".spanno").html(indexdata.stock_input_code)
        CApp.initBase("base",indexdata)
        getLocation()
        CApp.initBase("base",indexdata)
        SetRemark(indexdata.remark)
    	$("[c-model='fty_name']").html("<span class='issue_ftyinfo'>"+indexdata.fty_code+"-"+indexdata.fty_name+"</span>");
    	$("[c-model='location_name']").html("<span class='issue_locinfo'>"+indexdata.location_code+"-"+indexdata.location_name+"</span>")
    	$("[c-model='move_type_name']").html("<span class='move_type'>"+indexdata.move_type_code+"-"+indexdata.move_type_name+"</span>")
    }else{
        closeTip()
        getDevice()
    }
    dataTable()
}

//库存地点
function getLocation(){
    $("[c-model='location_id'] option").remove();
    $("[c-model='location_id']").append("<option style='display: none' value=''>请选择</option>");
    var index=$("[c-model='fty_id'] option:selected").attr("data-index");
    if(index==undefined){
        return false
    }
    $.each(selectdata.fty_list[index].location_ary,function(i,item){
        $("[c-model='location_id']").append("<option value='"+item.loc_id+"' data-index='"+i+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
    })
}

//装置
function getDevice(){
    $(".device_id option").remove();
    var index=$(".product_line_id option:selected").attr("data-index");
    $.each(selectdata.product_line_list[index].installations,function(i,item){
        $(".device_id").append("<option value='"+item.installation_id+"'>"+item.installation_name+"</option>")
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
function GetNote(id) {
    var note = "";
    $.each(indexdata.item_list, function (i, item) {
        if (item.mat_code == id) {
            note = item.remark;
            return true;
        }
    });
    return note;
}

//修改物料备注
function SetNote(id, text) {
    var ajaxdata = { "id": id, "note": text };

    var indexid = -1;
    $.each(indexdata.item_list, function (i, item) {
        if (item.mat_code == id) {
            indexid = i;
            return true;
        }
    });
    //更新数据
    indexdata.item_list[indexid].remark = text;
}

//添加物料
function addMaterial(data){
    $.each(data,function(i,item){
        var is_add=true
        $.each(indexdata.item_list,function(j,itemer){
            if(itemer.mat_id==item.mat_id){
                is_add=false
                return false
            }
        })
        if(is_add){
            indexdata.item_list.push(item)
        }
    })
    dataTable()
}

//获取物料
function getItemList(){
    return indexdata.item_list
}

//获取入库数量
function getQTY(index){
    return indexdata.item_list[index]
}

//获取工厂id
function getFty(){
    return $("[c-model='fty_id']").val()
    indexdata.item_list=[]
}

//更新入库数量
function setQTY(data,index){
    var qty=0
    indexdata.item_list[index].package_item_list=data
    $.each(indexdata.item_list[index].package_item_list,function(i,item){
        qty=countData.add(qty,item.qty)
    })
    indexdata.item_list[index].qty=qty
    dataBindTable()
}

//删除表格数据
function delTabel(){
    layer.confirm("是否删除？", {
        btn: ['删除', '取消'],
        icon: 3
    }, function() {
        if($(".body .chk input:checked").length==0){
            layer.msg("请选择删除物料")
            return false
        }
        for(var i=$(".body .chk input:checked").length-1;i>=0;i--){
            var index=$(".body .chk input:checked").eq(i).val()
            indexdata.item_list.splice(index,1)
        }
        dataBindTable()
        layer.close(parseInt($(".layui-layer-shade").attr("times")));
    }, function() {
    });
}


//绑定表格数据
function dataTable(){
    if(indexdata.item_list.length==0){
        return false
    }
    $.each(indexdata.item_list,function(i,item){
        item.qty=(item.qty==0 || item.qty==undefined)?"输入":item.qty
    })

    dataBindTable()
}

//绑定表格
function dataBindTable(){
    $("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false,min:200 },
            { th: '物料描述', col: 'mat_name', sort:false,width:300 },
            { th: '单位', col: 'name_zh', sort:false,width:100 },
            { th: '入库数量', col: 'qty', sort:false,type:'popup',align:'center',class:'qty',
                popup:{
                    href:"popup_qty.html"+table.remark,
                    area:"1000px,500px",
                    title:"入库数量",
                    args:["mat_id"],
                }
            },
            { th: '备注', col: 'remark', sort:false,width:"80",type:"popup",
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.remark,
                    area:"500px,400px",
                    title:"备注",
                    args:["mat_code|id"]
                }
            },
            {th: '行类型', col: 'ztrvalue', class: "", type: "tr",
                tr: {
                    value: 1,
                    class: "pinkbg"
                }
            },
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        percent:40,
        absolutelyWidth:true,
        checkbox:table.checkbox
    })
}

//click事件
function clickEvent(){
    //提交
    $(".btnsubmit").click(function() {
        layer.confirm("是否提交", {
            btn: ['提交', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submit()
        })
    })

    //过帐
    $(".btninput").click(function() {
        layer.confirm("是否过帐", {
            btn: ['过帐', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            btnInput()
        })
    })
}

//过帐
function btnInput(){
    var url=uriapi+"/biz/input/other/post/"+GetQueryString("id")
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            setTimeout(function () { location.href = "list.html" }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
}

//提交
function submit(){
    var is_checked=true,item_list=[],mat_num=true;


    if($("[c-model='move_type_id']").val()==null || $("[c-model='move_type_id']").val()==""){
        layersMoretips("必填项",$("[c-model='move_type_id']"))
        is_checked=false
    }
    if($("[c-model='fty_id']").val()==null || $("[c-model='fty_id']").val()==""){
        layersMoretips("必填项",$("[c-model='fty_id']"))
        is_checked=false
    }
    if($("[c-model='location_id']").val()==null || $("[c-model='location_id']").val()==""){
        layersMoretips("必填项",$("[c-model='location_id']"))
        is_checked=false
    }
    $.each(indexdata.item_list,function(i,item){
        if(item.qty!="输入"){
            mat_num=false
        }
    })
    if(mat_num){
        layer.msg("至少添加一条物料")
        return false
    }
    if(is_checked==false)return false

    $.each(indexdata.item_list,function(i,item){
        var package_item_list=[]
        if(item.qty!="输入"){
            $.each(item.package_item_list,function(j,itemer){
                package_item_list.push({
                    package_type_id:itemer.package_type_id,
                    qty:itemer.qty,
                    production_batch:itemer.production_batch,
                    erp_batch:itemer.erp_batch,
                    quality_batch:itemer.production_batch,
                    remark:itemer.remark
                })
            })
            item_list.push({
                mat_id:item.mat_id,
                unit_id: item.unit_id,
                qty: item.qty,
                package_item_list:package_item_list,
                remark:item.remark
            })
        }
    })
    var obj={
        move_type_id:$("[c-model='move_type_id']").val(),
        remark: indexdata.remark,
        fty_id: $("[c-model='fty_id']").val(),
        location_id: $("[c-model='location_id']").val(),
        class_type_id: $("[c-model='class_type_id']").val(),
        product_line_id:$(".product_line_id").val(),
        installation_id:$(".device_id").val(),
        item_list:item_list
    }
    console.log(obj)
    showloading();
    var url=uriapi+"/biz/input/other/other"
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
}

$(function(){
    $("[c-model='create_user_name']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    if(GetQueryString("id")!=null){
        $("[c-model='move_type_id']").parent().attr("c-model","move_type_name").html("")
        $("[c-model='fty_id']").parent().attr("c-model","fty_name").html("")
        $("[c-model='location_id']").parent().attr("c-model","location_name").html("")
        $("[c-model='class_type_id']").parent().attr("c-model","class_type_name").html("")
        $(".product_line_id").parent().attr("c-model","product_line_name").html("")
        $(".device_id").parent().attr("c-model","installation_name").html("")
        $(".matData").show()
        $(".btnAddMaterial,.btnsubmit").hide()
        $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
        table.remark="?view=1"
        table.checkbox=false
    }else{
        $(".deltable").show()
    }
    dataBindTable()
    clickEvent()
    tableedit($("body"))
    init()
    $("[c-model='fty_id']").change(function(){
        getLocation()
    })
    $(".product_line_id").change(function(){
        getDevice()
    })
})