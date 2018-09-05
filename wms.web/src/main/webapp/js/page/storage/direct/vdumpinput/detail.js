var indexdata={},g=null,selectdata={},table={popup:''};

//加载基础数据
function init(){
    if(GetQueryString("id")==null) return false
    showloading();
    var url=uriapi+"/biz/input/transport/virtualdump_byid/"+GetQueryString("id")
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            indexdata=data.body
            selectdata.fty_list=true
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    });
}

//搜索生产订单
function search(){
    if($(".search").val()==""){
        layer.msg("请输入生产订单号")
        return false
    }
    var url=uriapi+"/biz/input/transport/stockinput_bycode/"+$(".search").val()
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            if(data.body.msg!=undefined){
                layer.msg(data.body.msg)
                return false
            }
            indexdata=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })

    if(selectdata.fty_list != undefined) return false
    var url=uriapi+"/biz/input/transport/baseinfo_byuser"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata=data.body
            dataBindSelect()
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定下拉数据
function dataBindSelect(){
    $.each(selectdata.class_type_list,function(i,item){
        $(".class_type_id").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(selectdata.fty_list,function(i,item){
        $(".fty_list").append("<option value='"+item.fty_id+"' data-index='"+i+"'>"+item.fty_name+"</option>")
    })
    $.each(selectdata.syn_list,function(i,item){
        $(".syn_list").append("<option value='"+item.syn_type+"'>"+item.syn_type_name+"</option>")
    })
    dataLocation()
}

//绑定基础数据
function dataBindBase() {
    if (indexdata.item_list == undefined || selectdata.fty_list == undefined) return false
    layer.close(indexloading)
    $(".syn_list").val(selectdata.syn_type_id)
    $(".class_type_id").val(selectdata.class_type_id)
    if(GetQueryString("id")){
        $(".is_input_search").remove()
        $(".fty_list").parent().html("<span>"+indexdata.fty_input_code+"-"+indexdata.fty_input_name+"</span>")
        $(".loc_list").parent().html("<span>"+indexdata.location_input_code+"-"+indexdata.location_input_name+"</span>")
        $(".class_type_id").parent().html("<span c-model='class_name'></span>")
        $(".syn_list").parent().html("<span c-model='syn_type_name'></span>")
        $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
        $(".h2red").html(indexdata.status_name)
        $(".spanno").html(indexdata.stock_transport_code)
        $.each(indexdata.item_list,function(i,item){
            item.package_type_id=item.package_type_name
            item.location_id=item.location_code+"-"+item.location_name
        })
        if(indexdata.status==10){
            $(".btn_submit").hide()
            $(".btn_write_off").show()
            $(".doc_code").show()
        }else if(indexdata.status==20){
            $(".btn_submit").hide()
            $(".writeType").show()
            $(".doc_code,.mat_color").show()
            $.each(indexdata.item_list,function(i,item){
                item.tr=1
            })
        }else if(indexdata.status==0){
            $(".btnoperate").show()
        }
    }
    CApp.initBase("base",indexdata)
    dataBindTable()
}

//库存地点
function dataLocation(){
    $(".loc_list option").remove()
    var index=$(".fty_list option:selected").attr("data-index")
    $.each(selectdata.fty_list[index].location_ary,function(i,item){
        $(".loc_list").append("<option value='"+item.loc_id+"' data-index='i'>"+item.loc_code+"-"+item.loc_name+"</option>")
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
    indexdata.item_list[index].remark= text;
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '包装类型', col: 'package_type_code',sort:false},
            { th: '包装规格', col: 'package_standard_ch',sort:false},
            { th: '件数', col: 'num',sort:false},
            { th: '转储数量', col: 'qty',sort:false,type:'number'},
            { th: '生产批次', col: 'production_batch',sort:false},
            { th: 'ERP批次', col: 'erp_batch',sort:false},
            { th: '质检批次', col: 'quality_batch',sort:false},
            { th: '发出工厂', col: ["fty_output_code","fty_output_name"],sort:false},
            { th: '发出库存地点', col:["location_output_code","location_output_name"],sort:false},
            { th: '备注', col: 'remark', sort:false,width:"80",type:"popup",
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
        absolutelyWidth:true
    });
}

//过帐
function submit(){
    var item_list=[],stock_input_rid=1
    if(!indexdata.stock_input_code){
        layer.msg("请输入虚拟生产入库单号")
        return false
    }
    $.each(indexdata.item_list,function(i,item){
        item_list.push({
            remark:item.remark||""
        })
    })

    var obj={
        stock_input_code:indexdata.stock_input_code,
        stock_transport_id:GetQueryString("id")||"",
        mat_id:indexdata.mat_id,
        fty_id:$(".fty_list").val(),
        location_id:$(".loc_list").val(),
        class_type_id:$(".class_type_id").val(),
        syn_type:$(".syn_list").val(),
        remark:indexdata.remark||"",
        item_list:item_list
    }
    var url=uriapi+"/biz/input/transport/create_virtualdump"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            closeTipCancel()
            setTimeout(function () { location.href = "detail.html?id="+data.body.stock_transport_id; }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
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
            var url=uriapi+"/biz/input/transport/virtual/delete/"+GetQueryString("id")
            showloading();
            ajax(url,"get",null,function(data){
                if(data.head.status==true){
                    layer.msg(data.head.msg)
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
    $(".btn_submit").click(function() {
        layer.confirm("是否过帐", {
            btn: ['过帐', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submit()
        })
    })

    //冲销
    $(".btn_write_off").click(function() {
        layer.confirm("是否冲销", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            var url=uriapi+"/biz/input/transport/virtual/write_off"
            var obj={
                stock_task_id:GetQueryString("id")
            }
            showloading();
            ajax(url,"POST",JSON.stringify(obj),function(data){
                closeTipCancel()
                setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
            }, function(e) {
                layer.close(indexloading);
            },true)
        })
    })
}

$(function(){
    $("[c-model='create_user']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    tableedit($(".divbase"))
    init()
    clickEvent()
    if(GetQueryString("id")){
        table={popup:'?view=1'}
    }else{
        closeTip()
    }
    dataBindTable()
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
    $("a.btn-search").click(function () {
        search();
    });
    $(".fty_list").change(function(){
        dataLocation()
    })
    $(".btnAddMaterial").click(function(){
        AddMaterial()
    })


})