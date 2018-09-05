var indexdata={},selectdata={},g=null,table={select:"select",popup:"",checkbox:true},isdelete=true,files=[]

//加载数据
function getBaseInfo(){
    showloading();
    if(GetQueryString("id")==null){
        indexdata={}
        indexdata.item_list=[]
        var url=uriapi+"/biz/product/transport/base_info"
        ajax(url,"get",null,function(data){
            if(data.head.status==true){
                layer.close(indexloading);
                selectdata=data.body
                selectdata.location_list_in=[]
                $.each(selectdata.fty_list_in,function(i,item){
                    item.fty_input=item.fty_id
                    $.each(item.location_ary,function(j,itemer){
                        selectdata.location_list_in.push(itemer)
                        selectdata.location_list_in[selectdata.location_list_in.length-1].fty_input=item.fty_id

                    })
                })
                dataBindSelect()
            }else{
                layer.close(indexloading)
            }
        }, function(e) {
            layer.close(indexloading);
        })
    }else{
        var url=uriapi+"/biz/product/transport/details/"+GetQueryString("id")

        ajax(url,"get",null,function(data){
            if(data.head.status==true){
                layer.close(indexloading);
                indexdata=data.body
                isdelete=false
                files=indexdata.file_list;
                $(".addfile").parent().remove()
                dataBindBase()
                uploadresult()
            }else{
                layer.close(indexloading)
            }
        }, function(e) {
            layer.close(indexloading);
        })
    }
}

//绑定基础下拉
function dataBindSelect(){
    $.each(selectdata.fty_list_out,function(i,item){
        $(".fty_list_out").append("<option value='"+item.fty_id+"' data-index='"+i+"'>"+item.fty_code+"-"+item.fty_name+"</option>")
    })
    $.each(selectdata.class_type_list,function(i,item){
        $(".class_type_list").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(selectdata.driver_list,function(i,item){
        $(".driver").append("<option value='"+item.delivery_driver_id+"'>"+item.delivery_driver_name+"</option>")
    })
    $.each(selectdata.vehicle_list,function(i,item){
        $(".vehicle").append("<option value='"+item.delivery_vehicle_id+"'>"+item.delivery_vehicle_name+"</option>")
    })
    $(".class_type_list").val(selectdata.class_type_id)
    location_ary_out()
    if(!GetQueryString("id")){
        closeTip()
    }
}

//发出库存地点
function location_ary_out(){
    $(".location_ary option").remove();
    var index=$(".fty_list_out option:selected").attr("data-index");
    if(index!=undefined){
        $.each(selectdata.fty_list_out[index].location_ary,function(i,item){
            $(".location_ary").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
        })
    }
}

//获取物料
function getItemList(){
    return indexdata.item_list
}

//绑定基础数据
function dataBindBase(){
    $(".fty_list_out").parent().html("<span c-model='fty_name'></span>")
    $(".driver").parent().html("<span c-model='driver'></span>")
    $(".vehicle").parent().html("<span c-model='vehicle'></span>")
    $(".location_ary").parent().html("<span>"+indexdata.location_code+"-"+indexdata.location_name+"</span>")
    $(".class_type_list").parent().html("<span c-model='class_type_name'></span>")
    $("[c-model='driver']").parent().html("<span c-model='driver'></span>")
    $("[c-model='vehicle']").parent().html("<span c-model='vehicle'></span>")
    $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
    $(".btnAddMaterial,.btnsubmit").remove()
    $(".matData,.print").show()
    $(".spanno").html(indexdata.stock_transport_code)
    $(".h2red").html(indexdata.status_name)
    if(indexdata.status==2){
        $(".btnoperate").show()
    }
    $.each(indexdata.item_list,function(i,item){
        item.location_input=item.location_code+"-"+item.location_name
        item.fty_input=item.fty_name
        item.id=i
        if(item.work_model==3){
            item.work_model_id="3"
            item.work_model="不启用包装、托盘"
        }else if(item.work_model==1){
            item.work_model_id="1"
            item.work_model="启用包装、托盘"
        }else if(item.work_model==2){
            item.work_model_id="2"
            item.work_model="启用包装"
        }
    })
    CApp.initBase("base",indexdata)
    dataBindTable()
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

//获取库存地点
function getLoction(){
    return $(".location_ary").val()
}

//添加物料
function addMaterial(data){
    $.each(data,function(i,item){
        var is_add=true
        $.each(indexdata.item_list,function(j,itemer){
            if(item.id==itemer.id){
                is_add=false
                return false
            }
        })
        if(is_add){
            indexdata.item_list.push(item)
            indexdata.item_list[indexdata.item_list.length-1].position_list=[]
            indexdata.item_list[indexdata.item_list.length-1].transport_qty=0
            indexdata.item_list[indexdata.item_list.length-1].transport_num=""
        }
    })
    dataTable()
}

//绑定表格数据处理
function dataTable(){
    $.each(indexdata.item_list,function(i,item){
        if(item.mat_store_type==1){
            item.word_type=[{name:"启用包装、托盘",value:1},{name:"启用包装",value:2}]
            item.work_model=item.work_model||2
        }else{
            item.word_type=[{name:"不启用包装、托盘",value:3},{name:"启用包装、托盘",value:1},{name:"启用包装",value:2}]
            item.work_model=item.work_model||3
        }

    })
    g=null
    dataBindTable()
    dataBindTable()
}

//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code',sort:false},
            { th: '物料描述', col: 'mat_name',sort:false,width:200},
            { th: '包装类型', col: 'package_type_code',sort:false},
            { th: '件数', col: 'pack_num',sort:false},
            { th: '单位', col: 'unit_name',sort:false},
            { th: '库存数量', col: 'stock_qty',sort:false,type:'number'},
            { th: '作业模式', col: 'work_model',sort:false,type:table.select,data:"word_type",value:'value',text:'name',width:200,class:"word_type"},
            { th: '接收工厂', col: 'fty_input',sort:false,type:table.select,data:selectdata.fty_list_in,value:'fty_input',text:'fty_name',width:200,class:"fty"},
            { th: '接收库存地点', col: 'location_input',sort:false,type:table.select,data:selectdata.location_list_in,parent:'fty_input',value:'loc_id',text:['loc_code','loc_name'],width:200,class:"loction"},
            { th: '本次转运数量', col: 'transport_qty',sort:false,width:150,type:'text',class:'transport_qty',
                dowhile:{regex:'>rowdata[stock_qty]',tips:"转储数量不能大于库存数量"},
                decimal:{regex:'decimal_place'}
            },
            { th: '本次转运件数', col: 'transport_num',sort:false,class:'transport_num',width:150},
            { th: '生产批次', col: 'production_batch',sort:false},
            { th: 'ERP批次', col: 'erp_batch',sort:false},
            { th: '备注', col: 'remark', sort:false,type:"popup",width:80,
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html"+table.popup,
                    area:"500px,400px",
                    title:"备注",
                    args:["id|id"]
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
            $.each(a,function(i,item){
                if(GetQueryString("id")==null){
                    if(item.work_model==3){
                        $(".transport_qty").eq(i).find("div").html('<input type="text" class="form-control txttransport_qty" style="" data-index-row="'+i+'" data-index-col="9" value="'+item.transport_qty+'">')
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html('<input type="text" class="form-control txttransport_num" style="" data-index-row="'+i+'" data-index-col="10" value="'+item.transport_num+'">')
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }else if(item.work_model==2){
                        $(".transport_qty").eq(i).find("div").html('<a href="javascript:void(0)" data-title="本次转运数量" data-href="popup_position1.html?index='+i+'" data-size="700px,400px" class="btn_table_edit btn_table_popup">'+(toThousands(item.transport_qty)|| "添加")+'</a>')
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html(item.transport_num)
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }else if(item.work_model==1){
                        $(".transport_qty").eq(i).find("div").html('<a href="javascript:void(0)" data-title="本次转运数量" data-href="popup_position2.html?index='+i+'" data-size="1200px,650px" class="btn_table_edit btn_table_popup">'+(toThousands(item.transport_qty)|| "添加")+'</a>')
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html(item.transport_num)
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }
                }else{
                    if(item.work_model_id==3){
                        $(".transport_qty").eq(i).find("div").html(toThousands(item.transport_qty))
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html(Math.ceil(countData.div(item.transport_qty,item.package_standard)))
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }else if(item.work_model_id==2){
                        $(".transport_qty").eq(i).find("div").html('<a href="javascript:void(0)" data-title="本次转运数量" data-href="popup_position1.html?index='+i+'&view=1" data-size="700px,400px" class="btn_table_edit btn_table_popup">'+(toThousands(item.transport_qty)|| "添加")+'</a>')
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html(Math.ceil(countData.div(item.transport_qty,item.package_standard)))
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }else if(item.work_model_id==1){
                        $(".transport_qty").eq(i).find("div").html('<a href="javascript:void(0)" data-title="本次转运数量" data-href="popup_position2.html?index='+i+'&view=1" data-size="1200px,650px" class="btn_table_edit btn_table_popup">'+(toThousands(item.transport_qty)|| "添加")+'</a>')
                        if(item.package_standard_ch!="1"){
                            $(".transport_num").eq(i).find("div").html(Math.ceil(countData.div(item.transport_qty,item.package_standard)))
                        }else{
                            $(".transport_num").eq(i).find("div").html("")
                        }
                    }
                }
            })
            tableedit($(".transport_qty"))
            $(".txttransport_qty").blur(function(){
                var index=$(this).attr("data-index-row")
                indexdata.item_list[index].transport_qty=$(this).val()
            })
            $(".txttransport_num").blur(function(){
                var index=$(this).attr("data-index-row")
                indexdata.item_list[index].transport_num=$(this).val()
            })
            tableBlur()
        },
        cselect:function(a,b,c,d,e){//下拉框选择后触发
            if(a=="work_model"){
                indexdata.item_list[c].position_list=[]
                indexdata.item_list[c].transport_qty=0
                indexdata.item_list[c].transport_num=""
                indexdata.item_list[c].position_type=false
                dataBindTable()
            }
        }
    });
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
            var url=uriapi+"/biz/product/transport/delete/"+GetQueryString("id")
            showloading();
            ajax(url,"delete",null,function(data){
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

    $(".print").click(function(){
        print()
    })
}

//添加附件
function accessory() {
    var no=GetQueryString('id')==null?"0":GetQueryString('id')
    var filehtml = location.origin+"/wms/web/html/common/addfile.html?no="+no+"&type=33&actionurl="+location.origin+"/wms/file/file/";
    $(".addfile").attr("data-href",filehtml);
}
function uploadresult(obj){
    var receiptId=GetQueryString("id")||0;
    var receiptType="";
    pushfile(obj,receiptId,receiptType,files,isdelete);
}

//表格离开事件
function tableBlur(){
    //本次转运数量
    $(".txttransport_qty").blur(function(){
        var index=$(this).attr("data-index-row")
        var key=$(this).val()
        if(key==""){
            indexdata.item_list[index].transport_num=""
        }else if(!CheckisDecimal($(this))){
            return false
        }else{
            indexdata.item_list[index].transport_num=Math.ceil(countData.div(key,indexdata.item_list[index].package_standard))
        }
        dataBindTable()
    })

    //本次转运件数
    $(".txttransport_num").blur(function(){
        var index=$(this).attr("data-index-row")
        var key=$(this).val()
        if(key==""){
            indexdata.item_list[index].transport_qty=""
        }else if(!CheckisDecimal($(this),0)){
            return false
        }else{
            indexdata.item_list[index].transport_qty=countData.mul(key,indexdata.item_list[index].package_standard)
        }
        dataBindTable()
    })
}

//获取物料信息
function getDataMateriel(index){
    return indexdata.item_list[index]
}

//更新包装信息
function setDataPack(index,data,transport_qty){
    indexdata.item_list[index].position_list=data
    indexdata.item_list[index].transport_qty=transport_qty
    indexdata.item_list[index].position_type=false
    indexdata.item_list[index].transport_num=Math.ceil(countData.div(transport_qty,indexdata.item_list[index].package_standard))
    dataBindTable()
}

//更新包装信息
function setDataPack2(index,data,transport_qty){
    indexdata.item_list[index].position_list=data
    indexdata.item_list[index].transport_qty=transport_qty
    indexdata.item_list[index].position_type=true
    indexdata.item_list[index].transport_num=Math.ceil(countData.div(transport_qty,indexdata.item_list[index].package_standard))
    dataBindTable()
}

//提交包装物数据处理2
function tidyPosition2(data){
    var position_list=[]
    $.each(data,function(i,item){
        $.each(item.package_list,function(j,itemer){
            if(itemer.package_id!==""){
                position_list.push({
                    pallet_id: item.pallet_id,
                    package_id:itemer.package_id,
                    package_code:itemer.package_code,
                    qty:itemer.qty,
                    pallet_code:item.pallet_code
                })
            }
        })
    })
    return position_list
}

//提交包装物数据处理3
function tidyPosition3(data){
    var position_list=[]
    $.each(data,function(i,item){
        if(item.package_id!=""){
            position_list.push({
                pallet_id: null,
                package_id:item.package_id,
                package_code:item.package_code,
                qty:item.qty,
                pallet_code:null
            })
        }
    })
    return position_list
}

function print(){
    showloading()
    var url=uriapi+"/biz/print/print_product_trans/"+GetQueryString("id")
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

//提交
function submit(){
    var is_checked=true,item_list=[]
    if(indexdata.item_list.length==0){
        layer.msg("请添加物料信息")
        return false
    }
    if($("[c-model='driver']").val()==""){
        layersMoretips("必填项",$("[c-model='driver']"))
        is_checked=false
    }
    if($("[c-model='vehicle']").val()==""){
        layersMoretips("必填项",$("[c-model='vehicle']"))
        is_checked=false
    }
	
    $(".txttransport_num").each(function(i){
        if($(this).val()!=""){
            if(!CheckisDecimal($(this),0)){
                is_checked=false
            }
        }
    })
    $.each(indexdata.item_list,function(i,item){
        var position_list=[]
        if(item.transport_qty!="" && item.transport_qty!=0){
            if(parseFloat(item.transport_qty)>item.stock_qty){
                layersMoretips("本次转运数量不能大于库存数量",$(".transport_qty").eq(i))
                is_checked=false
            }
            if($(".txttransport_qty[data-index-row='"+i+"']").length!=0){
                if(!CheckisDecimal($(".txttransport_qty[data-index-row='"+i+"']"))){
                    is_checked=false
                }
                if(item.package_standard_ch!=1 && countData.mod($(".txttransport_qty[data-index-row='"+i+"']").val(),item.package_standard)){
                    layersMoretips("转运数量为包装规格的整数倍",$(".txttransport_qty[data-index-row='"+i+"']"))
                    is_checked=false
                }
            }
            if(item.fty_id==item.fty_input&&item.location_id==item.location_input){
            	 layersMoretips("发出工厂、库存地点与接收工厂、库存地点不能完全相同",$(".fty").eq(i));
            	 is_checked=false;
            }
            if(item.fty_input=="" || item.fty_input==undefined){
                layersMoretips("必填项",$(".fty select").eq(i))
                is_checked=false
            }
            
            if(item.location_input=="" || item.location_input==undefined){
                layersMoretips("必填项",$(".loction select").eq(i))
                is_checked=false
            }
            if(item.work_model==3){
                position_list=[]
            }else if(item.work_model==1){
                position_list=tidyPosition2(item.position_list)
            }else if(item.work_model==2){
                position_list=tidyPosition3(item.position_list)
            }
            item_list.push({
                rid:(i+1),
                mat_id:item.mat_id,
                fty_input:item.fty_input,
                location_input:item.location_input,
                package_type_id:item.package_type_id,
                production_batch:item.production_batch,
                erp_batch:item.erp_batch,
                quality_batch:item.quality_batch,
                transport_qty:item.transport_qty,
                stock_qty:item.stock_qty,
                unit_id:item.unit_id,
                mat_store_type:item.mat_store_type,
                batch:item.batch,
                work_model:item.work_model,
                remark:item.remark||"",
                position_list:position_list
            })
        }
    })
    if(item_list.length==0){
        layer.msg("至少添加一条物料信息")
        return false
    }
    if(is_checked==false) return false

    //判断包装物是否相同
    var arr=[]
    $.each(item_list,function(i,item){
        $.each(item.position_list,function(j,itemer){
            arr.push({
                rid:item.rid,
                pallet_code:itemer.pallet_code,
                package_code:itemer.package_code,
                is_alike:false
            })
        })
    })
    var txtmsg=""
    for(var i=0;i<arr.length;i++){
        var txtmat="第"+arr[i].rid+"行物料"+(arr[i].pallet_code!=""?"的"+arr[i].pallet_code+"托盘":"")
        var txtpack=arr[i].package_code
        var msg=false
        for(var j=i+1;j<arr.length;j++){
            if(arr[i].package_code==arr[j].package_code && !arr[j].is_alike && arr[i].package_code!=""){
                msg=true
                arr[j].is_alike=true
                txtmat+="、第"+arr[j].rid+"行物料"+(arr[j].pallet_code!=null?"的"+arr[j].pallet_code+"托盘":"")
            }
        }
        if(msg){
            txtmsg+=txtmat+",含有相同"+txtpack+"包装物<hr>"
        }
    }
    if(txtmsg!=""){
        layer.alert(txtmsg,{
            icon: 5,
            title: "操作失败",
            end: function(index) {
                layer.close(index);
            }
        });
        return false
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
        fty_id:$(".fty_list_out").val(),
        location_id:$(".location_ary").val(),
        class_type_id:$(".class_type_list").val(),
        delivery_driver_id:$(".driver").val(),
        driver:$(".driver option:selected").html(),
        delivery_vehicle_id:$(".vehicle").val(),
        vehicle:$(".vehicle option:selected").html(),
        remark:indexdata.remark||"",
        item_list:item_list,
        file_list:file_list
    }
    var url=uriapi+"/biz/product/transport/submit"

    console.log(JSON.stringify(obj))
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_transport_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
}

$(function () {
    indexdata.item_list=[]
    if(GetQueryString("id")==null){
        $("[c-model='user_name']").html(loginuserdata.body.user_name)
        $("[c-model='create_time']").html(GetCurrData())
        $(".btn_delete_mat").show()
    }else{
        table={select:"",popup:"?view=1",checkbox:false}
    }
    tableedit($("body"))
    getBaseInfo()
    dataBindTable()
    clickEvent()
    accessory()
    $(".fty_list_out").change(function () {
        location_ary_out()
    })
    $(".fty_list_out,.location_ary").change(function(){
        indexdata.item_list=[]
        dataBindTable()
    })
});