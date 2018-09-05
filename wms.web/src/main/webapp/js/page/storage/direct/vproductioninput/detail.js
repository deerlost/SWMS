var indexdata={},g=null,selectdata={},table={text:'text',popup:'',select:'select',checkbox:true,popup:'',popupmsg:"添加",qty:'text'};

//加载基础数据
function init(){
    if(GetQueryString("id")==null) return false
    showloading();
    var url=uriapi+"/biz/input/virtual/production/production_input_info/"+GetQueryString("id")
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            indexdata=data.body
            $.each(indexdata.item_list,function(i,item){
                item.rid=i
            })
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//搜索生产订单
function search(){
    if($(".search").val()==""){
        layer.msg("请输入生产订单号")
        return false
    }
    var url=uriapi+"/biz/input/virtual/production/contract_info/"+$(".search").val()
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            indexdata=data.body
            indexdata.item_list=[]
            indexdata.erp_batch_list=[]
            dataBindSelect()
            dataBindBase()
            dataBindTable()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定下拉数据
function dataBindSelect(){
    $(".class_type_id option").remove()
    $(".product_line_id option").remove()
    $(".syn_type_name option").remove()
    $.each(indexdata.class_type_list,function(i,item){
        $(".class_type_id").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(indexdata.product_line_list,function(i,item){
        $(".product_line_id").append("<option value='"+item.product_line_id+"' data-index='"+i+"'>"+item.product_line_name+"</option>")
    })
    $.each(indexdata.syn_type_list,function(i,item){
        $(".syn_type_name").append("<option value='"+item.syn_type_id+"'>"+item.syn_type_name+"</option>")
    })
    $.each(indexdata.package_type_list,function(i,item){
        $.each(item.erp_batch_list,function(j,itemer){
            indexdata.erp_batch_list.push({
                erp_batch_code:itemer.erp_batch_code,
                package_type_id:item.package_type_id
            })
        })
    })
    dataProduct()
}

//绑定基础数据
function dataBindBase() {
    $(".disabled").removeClass("disabled")
    $(".syn_type_name").val(indexdata.syn_type_id)
    if(indexdata.status!=null){
        $(".is_input_search,.btnAddMaterial").remove()
        $(".matData").show()
        $(".product_line_id").parent().html("<span c-model='product_line_name'></span>")
        $(".device_id").parent().html("<span c-model='device_name'></span>")
        $(".class_type_id").parent().html("<span c-model='class_type_name'></span>")
        $(".syn_type_name").parent().html("<span c-model='syn_type_name'></span>")
        $(".stck_type").parent().html("<span c-model='stck_type_name'></span>")
        $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
        $(".h2red").html(indexdata.status_name)
        $(".spanno").html(indexdata.stock_input_code)
        $.each(indexdata.item_list,function(i,item){
            item.package_type_id=item.package_type_name
            item.package_standard_ch=item.package_standard
            item.location_ids=item.location_id
            item.location_id=item.location_code+"-"+item.location_name
        })
        if(indexdata.status==10 || indexdata.status==5){
            $(".btn_submit").hide()
            $(".doc_code").show()
        }else if(indexdata.status==20){
            $(".btn_submit,.writeOff").hide()
            $(".writeType").show()
            $(".mat_color").show()
            $(".doc_code").show()
            $.each(indexdata.item_list,function(i,item){
                item.tr=1
            })
            
        }else if(indexdata.status==0){
            $(".btnoperate").show()
        }
        if(indexdata.transport_status=="0" && indexdata.status==10){
            $(".writeOff").show()
        }
        dataBindTable()
    }
    CApp.initBase("base",indexdata)
    $("[c-model='input_stock_num']").html(toThousands($("[c-model='input_stock_num']").html()))
}

//点击事件
function clickEvent(){
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

    //删除此单据
    $(".btndelete").click(function() {
        layer.confirm("是否删除", {
            btn: ['删除', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            var url=uriapi+"/biz/input/virtual/production/delete/"+GetQueryString("id")
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
}

//装置选择
function dataProduct(){
    $(".device_id option").remove()
    var index=$(".product_line_id option:selected").attr("data-index")
    $.each(indexdata.product_line_list[index].installations,function(i,item){
        $(".device_id").append("<option value='"+item.installation_id+"' data-index='i'>"+item.installation_name+"</option>")
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
        if (item.rid == id) {
            note = item.remark;
            return true;
        }
    });
    return note;
}

//修改物料备注
function SetNote(id, text) {
    var indexid = -1;
    $.each(indexdata.item_list, function (i, item) {
        if (item.rid == id) {
            indexid = i;
            return true;
        }
    });
    //更新数据
    indexdata.item_list[indexid].remark = text;
}

//添加物料
function AddMaterial(){
    if($(".btnAddMaterial").is(".disabled")) return false

    indexdata.item_list.push({
        rid:indexdata.item_list.length,
        package_type_id:indexdata.package_type_list[0].package_type_id,
        package_standard_ch:indexdata.package_type_list[0].package_standard_ch,
        sn_used:indexdata.package_type_list[0].sn_used,
        package_num:"--",
        location_id:indexdata.location_list[0].location_id,
        package_standard:indexdata.package_type_list[0].package_standard,
        pack_list:[],
        decimal_place:0,
        input_stock_num:"",
        mat_id:indexdata.mat_id
    })
    dataBindTable()



}

//获取关联包装单
function getPack(index){
    return indexdata.item_list[index]
}

//关联包装单错误提示
function getPackTips(index){
    layersMoretips("请添写生产批次",$(".txtproduction_batch").eq(GetQueryString("index")))
}

//更新关联包装单
function setPack(data,index,standard_num){
    indexdata.item_list[index].pack_list=data
    indexdata.item_list[index].standard_num=standard_num
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '包装类型', col: 'package_type_id',sort:false,width:110,type:table.select,data:indexdata.package_type_list,value:"package_type_id",text:"package_type_code"},
            { th: '包装规格', col: 'package_standard_ch',sort:false,width:110},
            { th: '件数', col: 'package_num',sort:false,width:80},
            { th: '入库数量', col: 'input_stock_num',sort:false,width:150,type:table.qty,decimal:{regex:3},class:'input_stock_num'},
            { th: '生产批次', col: 'production_batch',sort:false,width:150,type:table.text,maxlength:20},
            { th: 'ERP批次', col: 'erp_batch',sort:false,type:table.select,data:indexdata.erp_batch_list,value:'erp_batch_code',parent:'package_type_id',text:'erp_batch_code',width:200,class:'erp_batch'},
            { th: '质检批次', col: 'quality_batch',sort:false,width:150},
            { th: '库存地点', col: 'location_id',sort:false,type:table.select,data:indexdata.location_list,value:'location_id',text:["location_code","location_name"],width:230,class:'location_id'},
            { th: '备注', col: 'remark', sort:false,width:"80",type:"popup",
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.popup,
                    area:"500px,400px",
                    title:"备注",
                    args:["rid|id"]
                }
            },
            {th: '行类型', col: 'tr', class: "", type: "tr", tr: {
                value: 1,
                class: "pinkbg"
            }}
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        checkbox:table.checkbox,
        absolutelyWidth:true,
        required:{
            columns:["input_stock_num"],
            isAll:false
        },
        loadcomplete:function(){

        },
        cselect:function(a,b,c,d,e){
            if(a=="package_type_id"){
                indexdata.item_list[c].package_standard_ch= e.package_standard_ch
                indexdata.item_list[c].package_standard= e.package_standard
                indexdata.item_list[c].sn_used= e.sn_used
                if(indexdata.item_list[c].input_stock_num>0){
                    indexdata.item_list[c].package_num= Math.ceil(countData.div(indexdata.item_list[c].input_stock_num,e.package_standard))
                }
                dataBindTable()
            }
        },
        cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
            if(a=="input_stock_num" && d>0){
                if(e.package_standard_ch==1){
                    indexdata.item_list[c].package_num=""
                }else{
                    indexdata.item_list[c].package_num= Math.ceil(countData.div(indexdata.item_list[c].input_stock_num,indexdata.item_list[c].package_standard))
                }

                dataBindTable()
            }
            if(a=="production_batch"){
                indexdata.item_list[c].quality_batch=d
                dataBindTable()
            }
        }
    });
}

//过帐
function submit(){
    var is_checked=true,item_list=[],stock_input_rid=1
    if(!indexdata.produce_order_code){
        layer.msg("请输入生产订单号")
        return false
    }
    if(indexdata.item_list.length==0){
        layer.msg("请添加物料")
        return false
    }
    if(!g.checkData()){
        is_checked=false
    }
    $.each(indexdata.item_list,function(i,item){
        var position_list=[]
        if(item.input_stock_num>0){
            if($(".txtproduction_batch").eq(i).val()==""){
                layersMoretips("必填项",$(".txtproduction_batch").eq(i))
                is_checked=false
            }
            if($(".erp_batch").eq(i).find("select").val()==""){
                layersMoretips("必填项",$(".erp_batch").eq(i))
                is_checked=false
            }
            if($(".location_id").eq(i).find("select").val()==""){
                layersMoretips("必填项",$(".location_id").eq(i))
                is_checked=false
            }

            if(item.package_standard_ch!=1 && countData.mod(item.input_stock_num,item.package_standard)!=0){
                layersMoretips("入库数量为包装规格的整数倍",$(".input_stock_num").eq(i))
                is_checked=false
            }


            item_list.push({
                stock_input_rid:stock_input_rid,
                mat_id:item.mat_id,
                package_type_id:item.package_type_id,
                package_standard: item.package_standard,
                package_num:item.package_num,
                production_batch:item.production_batch,
                erp_batch:item.erp_batch,
                location_id:item.location_id,
                quality_batch:item.quality_batch,
                remark:item.remark,
                input_stock_num:item.input_stock_num,
                ret_pkg_operation_id:position_list
            })
            if(GetQueryString("id")){
                item_list[item_list.length-1].location_id=item.location_ids
            }
            stock_input_rid++
        }
    })


    if(item_list.length==0){
        layer.msg("至少添加一条物料信息")
        is_checked=false
    }
    if($(".device_id").val()==""){
        layersMoretips("必填项",$(".device_id").eq(i))
        is_checked=false
    }

    if(is_checked==false) return false

    var obj={
        mat_id:indexdata.mat_id,
        mat_code:indexdata.mat_code,
        unit_id:indexdata.unit_id,
        input_stock_num:indexdata.input_stock_num,
        business_type_id:indexdata.business_type_id,
        stock_input_id:GetQueryString("id")||"",
        produce_order_code:indexdata.produce_order_code,
        fty_id:indexdata.fty_id,
        product_line_id:$(".product_line_id").val(),
        device_id:$(".device_id").val(),
        stck_type:$(".stck_type").val(),
        class_type_id:$(".class_type_id").val(),
        syn_type_id:$(".syn_type_name").val(),
        remark:indexdata.remark,
        item_list:item_list
    }
    var url=uriapi+"/biz/input/virtual/production/production"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
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


//冲销
function writeOff(check){
	$.each(indexdata.item_list,function(i,item){
    	item.location_id = item.location_ids
    })
    var obj={
        stock_input_id:GetQueryString("id"),
        item_id:indexdata.item_id_list,
        is_check:check,
        doc_year:indexdata.doc_year_list,
        item_list:indexdata.item_list,
        mat_code:indexdata.mat_code,
        mat_id:indexdata.mat_id,
        fty_id:indexdata.fty_id,
        unit_id:indexdata.unit_id
    }
    var url=uriapi+"/biz/input/virtual/production/write_off"
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            setTimeout(function () { location.href = "detail.html?id="+GetQueryString("id"); }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
}



$(function(){
    indexdata.item_list=[]
    $("[c-model='create_user']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    tableedit($("body"))
    init()
    clickEvent()
    if(GetQueryString("id")){
        table={text:'',popup:'',select:'',checkbox:false,popup:'?view=1',popupmsg:"查看",qty:'number'}
    }else{
        $(".deltable").show()
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
    $(".product_line_id").change(function(){
        dataProduct()
    })
    $(".btnAddMaterial").click(function(){
        AddMaterial()
    })
})