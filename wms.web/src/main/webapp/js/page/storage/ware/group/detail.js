var indexdata={},selectdata={},area_list={},g=null,table={checkbox:true,qty:""}

//加载基础数据
function getBase(){
    showloading();
    var url=uriapi+"/biz/group_task/base_info"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            selectdata=data.body
            dataBindSelect()
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

//加载数据
function init(){
    var url=uriapi+"/biz/group_task/details/"+GetQueryString("id")
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            indexdata=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定基础数据
function dataBindBase(){
    $(".spanno").html(indexdata.stock_task_code)
    $(".h2red").html(indexdata.status_name)
    $(".location").parent().html(indexdata.location_code+"-"+indexdata.location_name)
    $(".area").parent().html("<span c-model='area_name'></span>")
    $(".position").parent().html("<span c-model='position_code'></span>")
    $(".class_type").parent().html("<span c-model='class_type_name'></span>")
    $(".pallet_code").parent().html("<span c-model='pallet_code'></span>")
    $(".remover").parent().html("<span c-model='remover'></span>")
    $(".forklift_worker").parent().html("<span c-model='forklift_worker'></span>")
    $(".tally_clerk").parent().html("<span c-model='tally_clerk'></span>")
    CApp.initBase("base",indexdata)
    $(".add_pallet,.mat_input,.btn_pallet").remove()
    $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
    dataBindTable()
}

//搜索物料
function search(){
    var obj={
        refer_receipt_code:$(".refer_receipt_code").val(),
        mat_condition:$(".mat_condition").val(),
        production_batch:$(".production_batch").val(),
        location_id:$(".location").val(),
        refer_receipt_type:$("[name='urgent']:checked").val()
    }
    var url=uriapi+"/biz/group_task/biz_stock_task_req_item_list"
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            indexdata.item_list=data.body
            $.each(indexdata.item_list,function(i,item){
                item.qty="添加"
                item.sn_list=[]
            })
            dataBindTable()
        }else{
            indexdata.item_list=[]
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定下拉数据
function dataBindSelect(){
    $.each(selectdata.location_list,function(i,item){
        $(".location").append("<option value='"+item.loc_id+"' wh_id='"+item.wh_id+"' fty_id='"+item.fty_id+"' data-index='"+i+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
    })
    $.each(selectdata.class_type_list,function(i,item){
        $(".class_type").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $(".class_type").val(selectdata.class_type_id)

    forklift()
    remover()
    tally()
    is_show()
    getArea()
}

//叉车工
function forklift(){
    var index=$(".location option:selected").attr("data-index")
    $(".forklift_worker option").remove()
    $.each(selectdata.location_list[index].forklift_worker_list,function(i,item){
        $(".forklift_worker").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })
}

//搬运工
function remover(){
    var index=$(".location option:selected").attr("data-index")
    $(".remover option").remove()
    $.each(selectdata.location_list[index].remover_list,function(i,item){
        $(".remover").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })
}

//理货员
function tally(){
    var index=$(".location option:selected").attr("data-index")
    $(".tally_clerk option").remove()
    $.each(selectdata.location_list[index].tally_clerk_list,function(i,item){
        $(".tally_clerk").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })
}

//是否隐藏 理货员 搬运工 叉车工
function is_show(){
    var index=$(".location option:selected").attr("data-index")
    if(selectdata.location_list[index].forklift_worker_list.length==0 && selectdata.location_list[index].remover_list.length==0 && selectdata.location_list[index].tally_clerk_list.length==0){
        $(".all_show").hide()
    }else{
        $(".all_show").show()
    }
    if(selectdata.location_list[index].forklift_worker_list.length==0){
        $(".forklift_worker").hide()
    }else{
        $(".forklift_worker").show()
    }
    if(selectdata.location_list[index].remover_list.length==0){
        $(".remover").hide()
    }else{
        $(".remover").show()
    }
    if(selectdata.location_list[index].tally_clerk_list.length==0){
        $(".tally_clerk").hide()
    }else{
        $(".tally_clerk").show()
    }
}

//加载存储区和仓位
function getArea(){
    var loc_id=$(".location").val()
    var url=uriapi+"/biz/group_task/area_list?location_id="+loc_id
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            $(".area option").remove()
            $.each(data.body,function(i,item){
                $(".area").append("<option value='"+item.area_id+"' data-index='"+i+"'>"+item.area_name+"</option>")
            })
            area_list=data.body
            position()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//更换仓位数据
function position(){
    var index=$(".area option:selected").attr("data-index")
    $(".position option").remove()
    $.each(area_list[index].position_list,function(i,item){
        $(".position").append("<option value='"+item.position_id+"'>"+item.position_name+"</option>")
    })
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false,width:180 },
            { th: '物料描述', col: 'mat_name', sort:false,width:300 },
            { th: '单位', col: 'name_zh', sort:false,width:100 },
            { th: '待上架', col: 'un_stock_task_qty', sort:false,width:100 ,type:'number'},
            { th: '本次上架', col: 'qty', sort:false,type:'popup',align:'center',class:'qty',
                popup:{
                    href:"popup_position.html"+table.qty,
                    area:"500px,600px",
                    title:"本次上架"
                }
            },
            { th: '生产批次', col: 'production_batch', sort:false,width:100 },
            { th: 'ERP批次', col: 'erp_batch', sort:false,width:100 },
            { th: '作业请求号', col: 'stock_task_req_code', sort:false,width:150,type:'link',
                link:{
                    href:"../putaway/detail.html",
                    autocreate:false,
                    args:["stock_task_req_id|id"]
                }},
            { th: '业务类型', col: 'refer_receipt_type_name', sort:false,width:100 },
            { th: '单据号', col: 'refer_receipt_code', sort:false,width:100,type:'link',
                link:{
                    href:false,
                    autocreate:false,
                    args:["refer_receipt_id|id"],
                    column:"refer_receipt_type",
                    links:[
                        {value:11,href:"../../pack/order/detail.html"},
                        {value:12,href:"../../input/production/detail.html"},
                        {value:13,href:"../../input/other/detail.html"},
                        {value:14,href:"../../direct/vproductioninput/detail.html"},
                        {value:15,href:"../../direct/productioninput/detail.html"},
                        {value:16,href:"../../specificword/productioninput/detail.html"},
                        {value:17,href:"../../specificrecord/productioninput/detail.html"},
                        {value:21,href:"../../ware/invoice/detail.html"},
                        {value:22,href:"../../output/sell/detail.html"},
                        {value:23,href:"../../output/scrap/detail.html"},
                        {value:24,href:"../../output/other/detail.html"},
                        {value:25,href:"../../output/production/detail.html"},
                        {value:26,href:"../../specificword/selloutput/detail.html"},
                        {value:27,href:"../../specificword/selloutput/detail.html"},
                        {value:28,href:"../../specificrecord/output/detail.html"},
                        {value:31,href:"../../ware/putaway/detail.html"},
                        {value:32,href:"../../ware/soldout/detail.html"},
                        {value:33,href:"../../ware/putaway/view.html"},
                        {value:34,href:"../../ware/soldout/view.html"},
                        {value:35,href:"../../ware/group/detail.html"},
                        {value:36,href:"../../ware/ware/detail.html"},
                        {value:37,href:"../../ware/ware/detail.html"},
                        {value:38,href:"../../ware/ware/detail.html"},
                        {value:39,href:"../../ware/ware/detail.html"},
                        {value:41,href:"../../input/dump/detail.html"},
                        {value:42,href:"../../direct/vdumpinput/detail.html"},
                        {value:43,href:"../../specificword/dump/detail.html"},
                        {value:44,href:"../../specificrecord/dump/detail.html"},
                        {value:53,href:"../../dump/materiel/detail.html"},
                        {value:54,href:"../../dump/production/detail.html"},
                        {value:55,href:"../../specificword/productionrevolve/detail.html"},
                        {value:62,href:"../../return/detail.html"}
                    ]
                }},
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        checkbox:table.checkbox
    })
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

//获取当前物料
function getDataMateriel(index){
    return indexdata.item_list[index]
}

//更新当前包装物
function setPack(data,index,qty){
    indexdata.item_list[index].sn_list=data
    indexdata.item_list[index].qty=qty
    dataBindTable()
}

//增加全新托盘
function add_pallet(){
    var url=uriapi+"/biz/task/shelves/get_new_pallet"
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            $("[c-model='pallet_code']").html(data.body.pallet_code)
            indexdata.pallet_id=data.body.pallet_id
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//获取托盘
function getPallet(){
    return $(".location").val()
}

//更新托盘
function setPallet(data){
    $("[c-model='pallet_code']").html(data[0].pallet_code)
    indexdata.pallet_id=data[0].pallet_id
}

//获取基础备注信息
function GetRemark(){
    return indexdata.remark||""
}

//更换基础备注信息
function SetRemark(data){
    indexdata.remark=data
}

//提交
function submit(){
    var is_checked=true,item_list=[]
    if(indexdata.item_list.length==0){
        layer.msg("请添加物料信息")
        return false
    }
    if($("[c-model='pallet_code']").html()==""){
        layersMoretips("必填项",$("[c-model='pallet_code']"))
        is_checked=false
    }
    $.each(indexdata.item_list,function(i,item){
        var pallet_package_list=[]
        if(item.qty>0){
            if(item.qty>item.un_stock_task_qty){
                layersMoretips("本次上架不能大于待上架",$(".qty").eq(i))
                is_checked=false
            }
            $.each(item.sn_list,function(j,itemer){
                pallet_package_list.push({
                    target_area_id:$(".area").val(),
                    target_position_id:$(".position").val(),
                    pallet_id:indexdata.pallet_id,
                    package_id:itemer.package_id,
                    qty:itemer.qty,
                    wh_id:$(".location option:selected").attr("wh_id")
                })
            })
            item_list.push({
                item_id:item.item_id,
                work_model:item.work_model,
                qty:item.qty,
                pallet_package_list:pallet_package_list
            })
        }
    })
    if(item_list.length==0){
        layer.msg("至少添加一条物料")
        return false
    }

    if(is_checked==false) return false

    var obj={
        fty_id:$(".location option:selected").attr("fty_id"),
        location_id:$(".location").val(),
        wh_id:$(".location option:selected").attr("wh_id"),
        area_id:$(".area").val(),
        position_id:$(".position").val(),
        remover:$(".remover option:selected").html()||"",
        forklift_worker:$(".forklift_worker option:selected").html()||"",
        tally_clerk:$(".tally_clerk option:selected").html()||"",
        pallet_id:indexdata.pallet_id,
        class_type_id:$(".class_type").val(),
        remark:indexdata.remark||"",
        item_list:item_list
    }
    var url=uriapi+"/biz/group_task/submit"
    console.log(JSON.stringify(obj))
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            closeTipCancel()
            setTimeout(function () { location.href = "detail.html?id="+data.body.stock_task_id; }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
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
}

$(function(){
    tableedit($("body"))
    clickEvent()
    indexdata.item_list=[]
    $("[c-model='create_user']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    if(GetQueryString("id")){
        table.checkbox=false
        table.qty="?view=1"
        $(".btnsubmit").remove()
        init()
    }else{
        $(".deltable").show()
        getBase()
    }
    dataBindTable()

    $(".search").click(function(){
        search()
    })

    $(".location").change(function(){
        indexdata.item_list=[]
        $("[c-model='pallet_code']").html("")
        indexdata.pallet_id=""
        forklift()
        remover()
        tally()
        is_show()
        dataBindTable()
        getArea()
    })
    $(".area").change(function(){
        position()
    })
    $(".add_pallet").click(function(){
        add_pallet()
    })
})