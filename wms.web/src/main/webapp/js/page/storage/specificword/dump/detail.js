var indexdata={},g1=null,selectdata=null,table={check:true,remark:""}

//基础数据加载
function baseInit(){
    var url=uriapi+"/biz/input/urgent/transport/to_transport_add"
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        selectdata=data.body
        dataBindSelect()
        if(!GetQueryString("id")){
            closeTip()
        }
    })
}

//加载作业单
function init(data){
    showloading()
    var url=uriapi+"/biz/input/urgent/transport/stock_task_add"
    var obj={
        task_id:data
    }
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            if(indexdata.item_list.length==0){
                indexdata.item_list=data.body
            }else{
                if(data.body[0].business_type_name!=indexdata.item_list[0].business_type_name){
                    indexdata.item_list=data.body
                }else{
                    $.each(data.body,function(i,item){
                        var is_add=true
                        $.each(indexdata.item_list,function(j,itemer){
                            if(item.mat_id==itemer.mat_id){
                                is_add=false
                                return false
                            }
                        })
                        if(is_add){
                            indexdata.item_list.push(item)
                        }
                    })
                }
            }
            dataBindTable()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//加载入库单id
function initId(){
    showloading()
    var url=uriapi+"/biz/input/urgent/transport/transport_input_info/"+GetQueryString("id");
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            indexdata=data.body
            baseData()
            dataBindTable()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//基础数据
function baseData(){
    $(".btn-stock").remove()
    $(".spanno").html(indexdata.stock_input_code)
    $("[c-model='class_type_id']").parent().html("<span c-model='class_type_name'></span>")
    $("[c-model='syn_type_id']").parent().html("<span c-model='syn_status_name'></span>")
    $(".h2red").html(indexdata.status_name)
    CApp.initBase("base",indexdata)
    if(indexdata.mes_doc_code=="" && indexdata.status==10){
        $(".btn_mes").show()
    }

    if(indexdata.status=="10"){
        $(".btnsubmit").remove()
        $(".doc_code").show()
    }else if(indexdata.status=="2") {
        $(".btnsubmit").html("　过帐　")
    }else if(indexdata.status=="0"){
        $(".btnsubmit").html("　过帐　")
    }else if(indexdata.status=='20'){
        $(".writeType").show()
    }
}

//绑定下拉
function dataBindSelect(){
    $.each(selectdata.class_type,function(i,item){
        $("[c-model='class_type_id']").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(selectdata.syn_type,function(i,item){
        $("[c-model='syn_type_id']").append("<option value='"+item.syn_type_id+"'>"+item.syn_type_name+"</option>")
    })
    CApp.initBase("base",selectdata)
    $("[c-model='create_name']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
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
    return indexdata.item_list[index].remark;
}

//修改物料备注
function SetNote(id, text,index) {
    indexdata.item_list[index].remark=text;
}

//绑定表格
function dataBindTable(){
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false},
            { th: '物料描述', col: 'mat_name', sort:false},
            { th: '单位', col: 'name_zh', sort:false},
            { th: '已上架数量', col: 'qty', sort:false,type:'number'},
            { th: '发出工厂', col: 'output_fty_name', sort:false},
            { th: '发出库存地点', col: ['output_location_code','output_location_name'], sort:false},
            { th: '接收工厂', col: 'fty_name', sort:false},
            { th: '接收库存地点', col: ['location_code','location_name'], sort:false},
            { th: '生产批次', col: 'production_batch', sort:false},
            { th: 'ERP批次', col: 'erp_batch', sort:false},
            {th: '作业单号', col: 'stock_task_code', type:"link", sort:false,
                link:{
                    href:"../../ware/putaway/view.html",
                    autocreate:false,
                    args:["stock_task_id|id"]
                }
            },
            { th: '业务类型', col: 'business_type_name', sort:false},
            { th: '备注', col: 'remark', sort:false,width:"80",type:"popup",
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.remark,
                    area:"500px,400px",
                    title:"备注",
                    args:["mat_code|id"]
                }
            },
            {
                th: '行类型', col: 'tr', class: "", type: "tr", tr: {
                value: 1,
                class: "pinkbg"
            }
            }
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        checkbox:table.check,
        absolutelyWidth:true
    });
}

//click事件
function clickEvent(){
    $(".btnsubmit").click(function(){
        var keys="提交"
        if(indexdata.status==2 || indexdata.status==0){
            keys="过帐"
        }
        layer.confirm("是否"+keys, {
            btn: [keys, '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submit()
        })
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

    //冲销
    $(".btn_write_off").click(function() {
        layer.confirm("是否冲销", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            var url=uriapi+"/biz/input/urgent/transport/write_off/"+GetQueryString("id")
            showloading();
            ajax(url,"POST",null,function(data){
                if(data.head.status==true){
                    setTimeout(function () { location.href = "detail.html?id="+GetQueryString("id"); }, 2000);
                }else{
                    layer.close(indexloading)
                }
            }, function(e) {
                layer.close(indexloading);
            },true)
        })
    })

    //mes
    $(".btn_mes").click(function() {
        layer.confirm("是否同步MES", {
            btn: ['同步', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submitMES()
        })
    })
}

//提交
function submit(){
    if(indexdata.item_list==0){
        layer.msg("请添加作业单")
        return false
    }
    var item_list=[]
    $.each(indexdata.item_list,function(i,item){
        item_list.push({
            item_id:item.item_id,
            remark:item.remark,
            output_location_id:item.output_location_id,
            output_fty_id:item.output_fty_id,
            output_location_code:item.output_location_code,
            output_fty_code:item.output_fty_code,
            wh_id:item.wh_id,
            refer_receipt_code:item.refer_receipt_code,
            production_batch:item.production_batch,
            erp_batch:item.erp_batch,
            stock_task_code:item.stock_task_code,
            qty:item.qty

        })
    })
    var obj={
        stock_task_id:GetQueryString("id")||"",
        class_type:$("[c-model='class_type_id']").val(),
        remark:indexdata.remark||"",
        business_type_id:indexdata.item_list[0].business_type_id,
        item_list:item_list,
        stock_input_code:indexdata.stock_input_code
    }
    showloading();
    var url=uriapi+"/biz/input/urgent/transport/transport"
    ajax(url,"post",JSON.stringify(obj),function(data){
    	if(data.head.status==true){
	    	 closeTipCancel()
	        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
    	}else{
    	  layer.close(indexloading);
    	}
    }, function(e) {
         layer.close(indexloading);
    },true)
}

//mes同步
function submitMES(){
    var url=uriapi+"/biz/input/urgent/transport/takeMes/"+GetQueryString("id")
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            closeTipCancel()
            setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
}

$(function(){
    indexdata.item_list=[]
    tableedit($("body"))
    if(GetQueryString("id")==null){
        $(".btn_delete_mat").show()
        baseInit()
    }else{
        table.check=false
        table.remark="?view=1"
        $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
        initId()
    }
    clickEvent()
    dataBindTable()


})