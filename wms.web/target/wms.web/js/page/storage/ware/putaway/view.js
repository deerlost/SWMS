var indexdata={},g1=null,table={remark:""},selectdata=null,isdelete=false,files=[];

//数据加载
function init(){
    showloading();
    var url=uriapi+"/biz/task/shelves/get_inner_task_details/"+GetQueryString("id")
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            indexdata=data.body
            files=indexdata.file_list;
            uploadresult()
            dataBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//基础信息
function dataBase(){
    layer.close(indexloading);
    if(indexdata.status==0){
        $(".btnoperate").show()
    }
    $(".mapadress a").eq(1).attr("href","detail.html?id="+indexdata.stock_task_req_id).html(indexdata.stock_task_req_code)
    $(".spanno").html(indexdata.stock_task_code)
    $(".h2red").html(indexdata.status_name)
    CApp.initBase("base",indexdata)
    dataTable()
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

//获取物料信息
function getDataMateriel(index){
    return indexdata.item_list[index]
}

//更新弹框信息1
function setPosition1(data,num,index){
    indexdata.item_list[index].position1=data
    indexdata.item_list[index].qty=num
    dataBindTable()
}

//提交数据整理1
function tidyPosition1(data){
    var pallet_package_list=[]
    $.each(data,function(i,item){
        if(item.qty!=""){
            pallet_package_list.push({
                pallet_id:"",
                pallet_code:"",
                package_id:"",
                package_code:"",
                wh_id:item.wh_id,
                area_id:item.area_id,
                position_id:item.position_id,
                qty:item.qty
            })
        }
    })
    return pallet_package_list
}

//更新弹框信息2
function setPosition2(data,noallot,num,index){
    indexdata.item_list[index].position2=data
    indexdata.item_list[index].noallot2=noallot
    indexdata.item_list[index].qty=num
    dataBindTable()
}

//提交数据整理2
function tidyPosition2(data){
    var pallet_package_list=[]
    $.each(data,function(i,item){
        $.each(item.allot,function(j,itemer){
            if(item.pallet_id!="" && item.allot.length!=0){
                pallet_package_list.push({
                    pallet_id:item.pallet_id,
                    pallet_code:item.pallet_code,
                    package_id:itemer.package_id,
                    package_code:itemer.package_code,
                    wh_id:item.wh_id,
                    area_id:item.area_id,
                    position_id:item.position_id,
                    qty:itemer.qty
                })
            }
        })
    })
    return pallet_package_list
}

//更新弹框信息3
function setPosition3(data,noallot,num,index){
    indexdata.item_list[index].position3=data
    indexdata.item_list[index].noallot3=noallot
    indexdata.item_list[index].qty=num
    dataBindTable()
}

//提交数据整理3
function tidyPosition3(data){
    var pallet_package_list=[]
    $.each(data,function(i,item){
        $.each(item.allot,function(j,itemer){
            if(item.allot.length!=0){
                pallet_package_list.push({
                    pallet_id:"",
                    pallet_code:"",
                    package_id:itemer.package_id,
                    package_code:itemer.package_code,
                    wh_id:item.wh_id,
                    area_id:item.area_id,
                    position_id:item.position_id,
                    qty:itemer.qty
                })
            }
        })
    })
    return pallet_package_list
}

//获取弹框下拉
function getSelectData(){
    return selectdata
}

//更新弹框下拉
function setSelectData(data){
    selectdata=data
}

//绑定表格数据处理
function dataTable(){
    $.each(indexdata.item_list,function(i,item){
        item.array=[]
        item.receipt_type=indexdata.receipt_type
        if(item.work_model=="3"){
            item.work_model_name="不启用包装、托盘"
        }else if(item.work_model=="1"){
            item.work_model_name="启用包装、托盘"
        }else{
            item.work_model_name="启用包装"
        }
        item.position1=[]
        item.position2=[]
        item.position3=[]
        item.noallot2=[]
        item.noallot3=[]
    })
    dataBindTable()
}

//绑定表格
function dataBindTable(){
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false,width:150 },
            { th: '物料描述', col: 'mat_name', sort:false,width:300 },
            { th: '单位', col: 'unit_zh', sort:false,width:100 },
            { th: '库存地点', col: ["location_code","location_name"], sort:false,width:200 },
            { th: '未上架数量', col: 'unstock_task_qty', sort:false,width:100,type:'number'},
            { th: '本次上架数量', col: 'qty', sort:false,width:150,type:'number'},
            { th: '作业模式', col: 'work_model_name', sort:false,width:200},
            { th: '选择仓位', col: 'qty', sort:false,type:'popup',align:'center',class:'qty',
                popup:{
                    display:"查看",
                    href:false,
                    area:"1230px,650px",
                    title:"仓位信息",
                    args:["mat_id"],
                    column:"work_model",
                    links:[{value:3,href:"popup_position1.html?view=1"},{value:1,href:"popup_position2.html?view=1"},{value:2,href:"popup_position3.html?view=1"}]
                }
            },
            { th: '生产批次', col: 'production_batch', sort:false,width:100 },
            { th: 'ERP批次', col: 'erp_batch', sort:false },
            { th: '包装类型', col: 'package_type_code', sort:false,width:100 },
            { th: '备注', col: 'remark', sort:false,width:"80",type:"popup",
                popup:{
                    display:"查看",
                    href:"../../../../html/common/note.html",
                    area:"500px,400px",
                    title:"备注",
                    args:["mat_code|id"]
                }
            }
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        absolutelyWidth:true,
        cselect:function(a,b,c,d,e){
            indexdata.item_list[c].position1=[];
            indexdata.item_list[c].position2=[];
            indexdata.item_list[c].position3=[];
            indexdata.item_list[c].noallot2=[]
            indexdata.item_list[c].noallot3=[]
            indexdata.item_list[c].qty=""
            dataBindTable()
        },
    })
}

//提交
function submit(){
    var item_list=[]
    var url=uriapi+"/biz/task/shelves/save_remark"
    $.each(indexdata.item_list,function(i,item){
        item_list.push({
            remark:item.remark,
            item_id:item.item_id
        })
    })
    var obj={
        stock_task_id:GetQueryString("id"),
        remark:indexdata.remark,
        item_list:item_list
    }
    showloading()
    ajax(url,"put",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            init()
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
            var url=uriapi+"/biz/task/shelves/delete/"+GetQueryString("id")
            showloading();
            ajax(url,"delete",null,function(data){
                layer.close(indexloading)
                if(data.head.status==true){
                    setTimeout(function () { location.href = "list.html"; }, 2000);
                }
            },function(e){
                layer.close(indexloading)
            },true)
        })
    })

    //提交
    $(".btnsubmit").click(function() {
        layer.confirm("是否修改", {
            btn: ['修改', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submit()
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
    var url=uriapi+"/biz/print/print_up_task/"+GetQueryString("id")
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

$(function(){
    init()
    clickEvent()
    tableedit($("body"))
    $(".is_work_show").click(function(){
        if($(".acceptances").is(":hidden")){
            $(".acceptances").show(500)
            $(this).html("▼")
        }else{
            $(".acceptances").hide(500)
            $(this).html("▲")
        }
    })
})