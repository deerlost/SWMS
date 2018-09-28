var indexdata={},g=null,selectdata={},table={text:'text',popup:'',select:'select',checkbox:true,mat_input:'popup',qty:'text'},mat_list=null,isdelete=true,files=[];

//加载基础下拉
function baseInfo(){
    showloading();
    var url=uriapi+"/biz/transport/get_move_info"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.move=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    var url=uriapi+"/biz/transport/get_class";
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.get_class=data.body;
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    var url=uriapi+"/biz/transport/getMaterial?matcode=";
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            mat_list=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//加载接收工厂和发出工厂
function getFty(){
    var url=uriapi+"/biz/transport/get_out_factory_info/"+$(".move_type").val();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.out_loction=data.body
            dataBindFty()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    var url=uriapi+"/biz/transport/get_in_factory_info/"+$(".move_type").val();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.input_loction=data.body;
            dataBindFty()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定工厂下拉
function dataBindFty(){
    if (selectdata.input_loction == undefined ||  selectdata.out_loction == undefined) return false
    $(".out_fty").append("<option value='' style='display:none'>请选择</option>")
    $.each(selectdata.out_loction, function (i, item) {
        $(".out_fty").append("<option value='" + item.fty_id + "' data-index='" + i + "'>" + item.fty_name + "</option>")
    })

    dataInputFty()
    outputLoction()
}

//加载入库单
function info(){
    var url=uriapi+"/biz/transport/get_dump_items"
    var obj={
        stock_transport_id:GetQueryString("id")
    }
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            indexdata=data.body
            isdelete=false
            files=indexdata.file_list;
            $(".addfile").parent().remove()
            dataBind()
            dataBindBase()
            uploadresult()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//绑定下拉数据
function dataBindBase() {
    if (selectdata.move == undefined ||  selectdata.get_class == undefined || mat_list==null) return false
    layer.close(indexloading)

    $(".move_type").append("<option value='' style='display:none'>请选择</option>")
    $(".driver_list").append("<option value=''>请选择</option>")
    $(".vehicle_list").append("<option value=''>请选择</option>")
    $.each(selectdata.move, function (i, item) {
        $(".move_type").append("<option value='" + item.move_type_id + "'>" + item.move_type_code + "-" + item.move_type_name + "</option>")
    })

    $.each(selectdata.get_class.class_type, function (i, item) {
        $(".class_type_id").append("<option value='" + item.class_type_id + "'>" + item.class_name + "</option>")
    })

    $.each(selectdata.get_class.syn_type, function (i, item) {
        $(".syn_type_id").append("<option value='" + item.syn_type + "'>" + item.syn_type_name + "</option>")
    })

    $.each(selectdata.get_class.driver_list, function (i, item) {
        $(".driver_list").append("<option value='" + item.delivery_driver_id + "'>" + item.delivery_driver_name + "</option>")
    })

    $.each(selectdata.get_class.vehicle_list, function (i, item) {
        $(".vehicle_list").append("<option value='" + item.delivery_vehicle_id + "'>" + item.delivery_vehicle_name + "</option>")
    })
    $(".out_fty").append("<option value='' style='display:none'>请选择</option>")
    $(".out_loction").append("<option value='' style='display:none'>请选择</option>");
    $(".class_type_id").val(selectdata.get_class.class_type_id)
    $(".syn_type_id").val(selectdata.get_class.syn_type_id)
    $("[c-model='create_user']").html(selectdata.get_class.create_user)
    $("[c-model='create_time']").html(selectdata.get_class.create_time)
    g=null
    dataBindTable()
    dataBindTable()
    if(!GetQueryString("id")){
        closeTip()
    }
}

//绑定数据
function dataBind(){
    if(indexdata.move_type_id!=3){
        $(".driver_vehicle").show()
    }
    if(indexdata.file_list.length==0){
        $(".filewapper").remove()
    }
    $(".move_type").parent().html(indexdata.move_type_code+"-"+indexdata.move_type_name);
    $(".class_type_id").parent().html("<span c-model='class_type_name'>--</span>");
    $(".syn_type_id").parent().html("<span c-model='syn_type_name'>--</span>");
    $(".driver_list").parent().html("<span c-model='delivery_driver_name'>--</span>");
    $(".vehicle_list").parent().html("<span c-model='delivery_vehicle_name'>--</span>");
    $(".out_fty").parent().html("<span c-model='fty_name'>--</span>");
    $(".out_loction").parent().html("<span>"+indexdata.location_code+"-"+indexdata.location_name+"</span>");
    $(".spanno").html(indexdata.stock_transport_code)
    $(".h2red").html(indexdata.status_name)
    $(".matData").show()
    //table.type=true
    $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
    $(".btnAddMaterial,.btnsubmit").remove()
    if(indexdata.status==2){
        $(".btnoperate").show()
    }
    CApp.initBase("base",indexdata)

    $.each(indexdata.item_list,function(i,item){
        item.fty_input=item.fty_code+"-"+item.fty_name
        item.location_input=item.location_code+"-"+item.location_name

        if(item.package_standard_ch!=1){
            item.package_num=Math.ceil(countData.div(item.transport_qty,item.package_standard))
        }
    })
    dataBindTable()
}

//加载发出库存地点
function outputLoction(){
    $(".out_loction option").remove();
    $(".out_loction").append("<option value='' style='display:none'>请选择</option>");
    var index=$(".out_fty option:selected").attr("data-index");
    if(index!=undefined){
        $.each(selectdata.out_loction[index].location_ary,function(i,item){
            $(".out_loction").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
        })
    }
}

//接收工厂 库存地点数据处理
function dataInputFty(){
    selectdata.fty_input=[]
    selectdata.loction_input=[]
    $.each(selectdata.input_loction,function(i,item){
        selectdata.fty_input.push({
            fty_name:item.fty_code+"-"+item.fty_name,
            fty_input:item.fty_id,
        })
        $.each(item.location_ary,function(j,itemer){
            selectdata.loction_input.push({
                fty_name:item.fty_code+"-"+item.fty_name,
                fty_input:item.fty_id,
                location_name:itemer.loc_code+"-"+itemer.loc_name,
                location_id:itemer.loc_id,
            })
        })
    })
}

//添加附件
function accessory() {
    var no=GetQueryString('id')==null?"0":GetQueryString('id')
    var filehtml = location.origin+"/wms/web/html/common/addfile.html?no="+no+"&type=33&actionurl="+location.origin+":8100/wms/file/file/";
    $(".addfile").attr("data-href",filehtml);
}
function uploadresult(obj){
    var receiptId=GetQueryString("id")||0;
    var receiptType="";
    pushfile(obj,receiptId,receiptType,files,isdelete);
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code',sort:false},
            { th: '物料描述', col: 'mat_name',sort:false,width:200},
            { th: '单位', col: 'unit_name',sort:false},
            { th: '库存数量', col: 'stock_qty',sort:false,width:100,type:'number'},
            { th: '包装类型', col: 'package_type_name',sort:false},
            { th: '件数', col: 'package_num',sort:false,width:80,type:table.qty,decimal:{regex:0},class:'package_num'},
            { th: '转储数量', col: 'transport_qty',sort:false,width:150,type:table.qty,
                dowhile:{regex:'>rowdata[stock_qty]',tips:"转储数量不能大于库存数量"},
                decimal:{regex:3}
            },
            { th: '接收工厂', col: 'fty_input',sort:false,type:table.select,data:selectdata.fty_input,value:'fty_input',text:'fty_name',width:200,class:"fty"},
            { th: '接收库存地点', col: 'location_input',sort:false,type:table.select,data:selectdata.loction_input,parent:'fty_input',value:'location_id',text:'location_name',width:200,class:"loction"},
            { th: '生产批次', col: 'product_batch',sort:false},
            { th: 'ERP批次', col: 'erp_batch',sort:false},
            { th: '目标物料', col: 'mat_input',sort:false,width:200,type:table.mat_input,class:'mat_input',
                popup:{
                    href:"popup_mat_input.html",
                    area:"800px,500px",
                    title:"目标物料",
                    args:["mat_code|id"]
                }},
            { th: '目标物料描述', col: 'mat_input_name',sort:false,width:200},
            { th: '目标生产批次', col: 'production_batch_input',sort:false,width:150,type:table.text,class:'input_batch',maxlength:20},
            { th: '目标ERP批次', col: 'erp_batch_input',sort:false,width:150,type:table.select,data:'erp_list',value:'erp_batch_code',text:'erp_batch_code',class:'input_erp_batch',maxlength:20},
            { th: '备注', col: 'remark', sort:false,type:"popup",width:80,
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.popup,
                    area:"500px,400px",
                    title:"备注",
                    args:["mat_code|id"]
                }
            }
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        checkbox:table.checkbox,
        absolutelyWidth:true,
        required:{
            columns:["transport_qty"],
            isAll:false
        },
        loadcomplete:function(a){
            if($(".move_type").val()=="3" || indexdata.move_type_id=="3"){
                $("[colnum='11'],[colnum='12'],[colnum='13'],[colnum='14']").show()
            }else{
                $("[colnum='11'],[colnum='12'],[colnum='13'],[colnum='14']").hide()
            }
            if($(".move_type").val()==4 || $(".move_type").val()==3){
                $(".fty select").attr("disabled","disabled")
            }
            $.each(a,function(i,item){
                if(item.package_standard_ch==1){
                    $(".package_num").eq(i).html("")
                }
            })
        },
        cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
            if(a=="transport_qty"){
                if(e.package_standard_ch==1){
                    indexdata.item_list[c].package_num=""
                }else{
                    indexdata.item_list[c].package_num= Math.ceil(countData.div(indexdata.item_list[c].transport_qty,indexdata.item_list[c].package_standard))
                }
                dataBindTable()
            }else if(a=='package_num'){
                indexdata.item_list[c].transport_qty=countData.mul(indexdata.item_list[c].package_num,indexdata.item_list[c].package_standard)
                dataBindTable()
            }
        }
    });
}

//获取目标物料
function getMatInput(){
    return mat_list
}

//更新目标物料
function setMatInput(data,index){
    if(data==""){
        indexdata.item_list[index].mat_input="添加"
    }else if(data.unit_id!=indexdata.item_list[index].unit_id){
        layer.msg("库存数量单位KG和TO不同")
    }else{
        //indexdata.item_list[index].mat_id=data.mat_id
		indexdata.item_list[index].mat_input=data.mat_code
        indexdata.item_list[index].mat_input_name=data.mat_name

        showloading()
        var url=uriapi+"/biz/transport/get_erp"
        var obj={
            mat_id:data.mat_id,
            fty_id:indexdata.item_list[index].fty_id,
            package_id:indexdata.item_list[index].package_type_id
        }
        ajax(url,"get",obj,function(data){
            layer.close(indexloading)
            if(data.head.status){
                indexdata.item_list[index].erp_list=data.body
                dataBindTable()
            }
        },function(e){
            layer.close(indexloading)
        })
    }
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
    indexdata.item_list[index].remark = text;
}

//获取物料
function getItemList(){
    return indexdata.item_list
}

//添加物料
function addMaterial(data){
    $.each(data,function(i,item){
        var is_add=true
        item.mat_input=""
        item.production_batch_input=item.production_batch_input||item.product_batch
        item.erp_batch_input=item.erp_batch_input||item.erp_batch
        $.each(indexdata.item_list,function(j,itemer){
            if(item.unique_id==itemer.unique_id){
                is_add=false
                return false
            }
        })
        if(is_add){
            indexdata.item_list.push(item)
            indexdata.item_list[indexdata.item_list.length-1].is_mat_input=true
            indexdata.item_list[indexdata.item_list.length-1].mat_input="添加"
            indexdata.item_list[indexdata.item_list.length-1].transport_qty=""
            indexdata.item_list[indexdata.item_list.length-1].erp_list=[]
            if($(".move_type").val()==4 || $(".move_type").val()==3){
                var fty_id=$(".out_fty").val()
                indexdata.item_list[indexdata.item_list.length-1].fty_input=fty_id
            }
        }
    })
    dataBindTable()
}

//切换移动类型
function moveType(){
    $(".btnAddMaterial").attr({"data-title":"添加物料","data-href":"popup_material.html","data-size":"950px,500px"})
    tableedit($("body"))
    $(".disabled").removeClass("disabled")
    selectdata.input_loction=undefined;
    selectdata.out_loction=undefined;
    $(".out_fty option").remove();
    $(".out_fty").append("<option value='' style='display:none'>请选择</option>")
    $(".out_loction option").remove();
    $(".out_loction").append("<option value='' style='display:none'>请选择</option>");
    getFty()
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
            var url=uriapi+"/biz/transport/delete_dump/"+GetQueryString("id")
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
    //打印
    $(".print").click(function(){
        print()
    })

}

//打印
function print(){
    showloading()
    var url=uriapi+"/biz/print/print_stock_transport/"+GetQueryString("id")
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            window.open(data.body.file_name_url)
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//获取当前工厂和库存地点
function getFtyLoction(){
    return [$(".out_fty").val(),$(".out_loction").val()]
}

//提交
function submit(){
    var is_checked=true,item_list=[],stock_transport_rid=1
    if(!g.checkData()){
        is_checked=false
    }
    $.each(indexdata.item_list,function(i,item){
        if($(".txttransport_qty").eq(i).val()>0){

            if($(".loction").eq(i).find("select").val()==""){
                layersMoretips("必填项",$(".loction").eq(i))
                is_checked=false
            }
            if($(".fty").eq(i).find("select").val()==""){
                layersMoretips("必填项",$(".fty").eq(i))
                is_checked=false
            }
            if(item.package_standard_ch!=1 && countData.mod(item.transport_qty,item.package_standard)!=0){
                layersMoretips("转储数量为包装类型的倍数",$(".txttransport_qty").eq(i))
                is_checked=false
            }
            if($(".move_type").val()=="3"){
                if($(".input_batch").eq(i).find("input").val()==""){
                    layersMoretips("必填项",$(".input_batch").eq(i))
                    is_checked=false
                }
                if(item.is_mat_input==false){
                    layersMoretips("目标物料未找到",$(".txtmat_input").eq(i))
                    is_checked=false
                }
            }
            item_list.push(item)
            item_list[item_list.length-1].stock_transport_rid=stock_transport_rid
            stock_transport_rid++
        }
    })

    if(item_list.length==0){
        layer.msg("至少添加一条物料信息")
        return false
    }
    if(is_checked==false) return false

    if($(".move_type").val()=="3") {
        $.each(item_list, function (i, item) {
            if(item.mat_input=="添加" || item.mat_input==undefined || item.mat_input==""){
                item.mat_input=item.mat_code
            }
            if($(".input_erp_batch").eq(i).find("select").val()==""){
                item.input_erp_batch=item.erp_batch
            }
        })
    }else{
        $.each(item_list, function (i, item) {
            item.mat_input=""
            item.input_erp_batch=""
        })
    }

    var file_list=[]
    $.each(files,function(i,item){
        file_list.push({
            file_id:item.file_id,
            file_name:item.file_name,
            file_size:item.file_size
        })
    })

    var obj={
        move_type_id:$(".move_type").val(),
        syn_type:$(".syn_type_id").val(),
        fty_id:$(".out_fty").val(),
        location_id:$(".out_loction").val(),
        remark:indexdata.remark||"",
        schedules:$(".class_type_id").val(),
        delivery_driver_id:$(".driver_list").val(),
        delivery_vehicle_id:$(".vehicle_list").val(),
        item_list:item_list,
        file_list:file_list

    }

    console.log(obj)
    var url=uriapi+"/biz/transport/create_dump"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_transport_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
}

$(function () {
    indexdata.item_list = []
    tableedit($(".divbase"))
    tableedit($(".filewapper"))
    if (GetQueryString("id") == null) {
        $(".btn_delete_mat").show()
        baseInfo()
    } else {
        $(".print").show()
        table = {text: '', popup: '?view=1', select: '', checkbox: false, mat_input: "",qty: 'number'}
        info()
    }
    accessory()
    dataBindTable()
    clickEvent()
    $(".move_type").change(function () {
        indexdata.item_list = []
        $(".driver_list").val("")
        $(".vehicle_list").val("")
        if($(this).val()==3){
            $(".driver_vehicle").hide()
        }else{
            $(".driver_vehicle").show()
        }
        dataBindTable()
        moveType()
    })

    $(".out_fty").change(function () {
        indexdata.item_list = []
        dataBindTable()
        outputLoction()
    })

    $(".out_loction").change(function () {
        indexdata.item_list = []
        dataBindTable()
    })

});