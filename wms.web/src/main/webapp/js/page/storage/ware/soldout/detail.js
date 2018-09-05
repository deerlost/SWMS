var indexdata={},g1=null,table={remark:""},selectdata=null,menges=[],isdelete=false,files=[];

//数据加载
function init(){
    showloading();
    var url=uriapi+"/biz/task/removal/biz_stock_task_req_details/"+GetQueryString("id")
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
    $(".spanno").html(indexdata.stock_task_req_code)
    $(".h2red").html(indexdata.status_name)

    $.each(indexdata.forklift_worker_list,function(i,item){
        $(".forklift_worker").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })

    $.each(indexdata.remover_list,function(i,item){
        $(".remover").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })

    $.each(indexdata.tally_clerk_list,function(i,item){
        $(".tally_clerk").append("<option value='"+item.id+"'>"+item.name+"</option>")
    })

    var delivery_driver_name=true
    $.each(indexdata.driver_list,function(i,item){
        $(".delivery_driver").append("<option value='"+item.delivery_driver_name+"'>"+item.delivery_driver_name+"</option>")
        if(item.delivery_driver_name==indexdata.delivery_driver){
            delivery_driver_name=false
        }
    })
    if(delivery_driver_name && indexdata.delivery_driver!=""){
        $(".delivery_driver").append("<option value='"+indexdata.delivery_driver+"'>"+indexdata.delivery_driver+"</option>")
    }

    var delivery_vehicle_name=true
    $.each(indexdata.vehicle_list,function(i,item){
        $(".delivery_vehicle").append("<option value='"+item.delivery_vehicle_name+"'>"+item.delivery_vehicle_name+"</option>")
        if(item.delivery_vehicle_name==indexdata.delivery_vehicle){
            delivery_vehicle_name=false
        }
    })
    if(delivery_vehicle_name && indexdata.delivery_vehicle!=""){
        $(".delivery_vehicle").append("<option value='"+indexdata.delivery_vehicle+"'>"+indexdata.delivery_vehicle+"</option>")
    }

    if(indexdata.driver_list.length==0){
        $(".delivery_driver").parent().parent().remove()
    }
    if(indexdata.vehicle_list.length==0){
        $(".delivery_vehicle").parent().parent().remove()
    }

    CApp.initBase("base",indexdata)


    dataTask()
    classType()
    dataTable()

    $(".class_type").val(indexdata.class_type_id)
    if(indexdata.status_name=="已完成"){
        $(".mat,#id_div_grid,.btnsubmit,.finish_hide").remove()
        $(".acceptances").show()
        $(".is_work_show").html("▼")
        $(".class_type").attr("disabled","disabled")
    }else{
        closeTip()
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

//班次数据处理
function classType(){
    $.each(indexdata.class_type_list,function(i,item){
        $(".class_type").append("<option value='"+item.class_type_id+"'>"+item.class_type_name+"（"+item.class_type_start+"-"+item.class_type_end+")</option>")
    })
}

//获取物料信息
function getDataMateriel(index){
	indexdata.item_list[index].receipt_type=indexdata.receipt_type;
    return indexdata.item_list[index]
}
//更新弹框下拉
function setSelectData(data,index){
    indexdata.item_list[index].selectdata=data
}

//更新弹框信息1
function setPosition1(items,index){
    var position_list=[];
	var cksl = 0.0;
	var arrs = [];
	$.each(items.body, function(i, item) {
		item.indexes=index
		arrs.push(item)
		if(item.out_qty>0){
			cksl +=item.out_qty * 1000;
		}	
	});
	menges[index] = arrs
	indexdata.item_list[index].qty = cksl / 1000;
	indexdata.item_list[index].position1=items;
	dataBindTable();
}
//更新数据

//提交数据整理1
function tidyPosition1(data){
    var pallet_package_list=[]
    $.each(data.body,function(i,item){
     if(item.out_qty>0){
		pallet_package_list.push({
			"pallet_id":null,
			"pallet_code":"",
			"package_id":null,
			"package_code":"",
			"wh_id":item.wh_id,
			"source_area_id":item.area_id,
			"source_position_id":item.position_id,
			"qty":item.out_qty,
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
                    source_area_id:item.area_id,
                    source_position_id:item.position_id,
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
                    pallet_id:null,
                    pallet_code:"",
                    package_id:itemer.package_id,
                    package_code:itemer.package_code,
                    wh_id:item.wh_id,
                    source_area_id:item.area_id,
                    source_position_id:item.position_id,
                    qty:itemer.qty
                })
            }
        })
    })
    return pallet_package_list
}

//加载作业单
function dataTask(){
    if(indexdata.task_list.length==0){
        return false
    }
    $(".countoutput").html(indexdata.task_list.length)
    $.each(indexdata.task_list,function(i,item){
        var strtxt=""
        strtxt+="<div class='acceptance col-lg-3 col-md-4 col-xs-6'>"
        strtxt+="<div class='whitebg'></div>"
        strtxt+="<div class='static static"+item.status+"'>"+item.status_name+"</div>"
        strtxt+="<dl class='info'>"
        strtxt+="<dt>作业单号：</dt>"
        strtxt+="<dd>"+item.stock_task_code+"</dd>"
        strtxt+="<dt>下架日期：</dt>"
        strtxt+="<dd>"+item.create_time+"</dd>"
        strtxt+="<div style='bottom: 45px; position: absolute; width: 100%;'>"
        strtxt+="<dt>申请人：</dt>"
        strtxt+="<dd>"+item.user_name+"</dd>"
        strtxt+="</div>"
        strtxt+="</dl>"
        strtxt+="<div class='note'>"
        strtxt+="<a href='view.html?id="+item.stock_task_id+"'>查看详细</a>"
        strtxt+="</div>"
        strtxt+="</div>"
        $(".acceptances").append(strtxt)
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

//反选数据
function menginfo() {
	return menges;
}

//绑定表格数据处理
function dataTable(){
    $.each(indexdata.item_list,function(i,item){
        item.array=[]
        item.receipt_type=indexdata.receipt_type
        item.position1=[]
        item.position2=[]
        item.position3=[]
        item.noallot2=[]
        item.noallot3=[]
        if(item.mat_store_type=="1"){
            item.array=[{name:"启用包装、托盘",value:1},{name:"启用包装",value:2}]
            item.work_model=2
        }else{
            item.array=[{name:"不启用包装、托盘",value:3},{name:"启用包装、托盘",value:1},{name:"启用包装",value:2}]
            item.work_model=3
        }
    })
    dataBindTable()
}

//绑定表格
function dataBindTable(){
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false },
            { th: '物料描述', col: 'mat_name', sort:false},
            { th: '单位', col: 'unit_zh', sort:false },
            { th: '库存地点', col: ["location_code","location_name"], sort:false },
            { th: '未下架数量', col: 'unstock_task_qty', sort:false,type:'number'},
            { th: '本次下架数量', col: 'qty', sort:false,class:'qty',type:'number'},
            { th: '作业模式', col: 'work_model', sort:false,width:200,type:'select',data:'array',text:'name',value:'value'},
            { th: '选择仓位', col: 'qty', sort:false,type:'popup',class:'qty',
                popup:{
                    display:"添加",
                    href:false,
                    area:"1230px,650px",
                    title:"仓位信息",
                    args:["mat_id"],
                    column:"work_model",
                    links:[{value:3,href:"popup_position1.html"},{value:1,href:"popup_position2.html"},{value:2,href:"popup_position3.html"}]
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
    var is_checked=true,item_list=[],isbz=true;
    $.each(indexdata.item_list,function(i,item){
        var pallet_package_list=[]
        if(item.qty>0){
            if(item.work_model=="3"){
            	isbz=false;
                pallet_package_list=tidyPosition1(item.position1)
            }else if(item.work_model=="1"){
                pallet_package_list=tidyPosition2(item.position2)
            }else if(item.work_model=="2"){
                pallet_package_list=tidyPosition3(item.position3)
            }

            if(item.package_standard_ch!=1 && countData.mod(item.qty,item.package_standard)!=0){
                layersMoretips("入库数量为包装规格的整数倍",$(".qty").eq(i))
                is_checked=false
            }

            item_list.push({
                rid:(i+1),
                item_id:item.item_id,
                stock_task_req_rid:item.stock_task_req_rid,
                work_model:item.work_model,
                qty:item.qty,
                remark:item.remark,
                pallet_package_list:pallet_package_list
            })
        }
    })
    if(item_list.length==0){
        layer.msg("至少上架一条物料")
        return false
    }

    if(is_checked==false) return false

    //判断包装物是否相同
   	var arr=[]
    $.each(item_list,function(i,item){
        $.each(item.pallet_package_list,function(j,itemer){
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
                txtmat+="、第"+arr[j].rid+"行物料"+(arr[j].pallet_code!=""?"的"+arr[j].pallet_code+"托盘":"")
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
    var obj={
        stock_task_req_id:indexdata.stock_task_req_id,
        class_type_id:$(".class_type").val(),
        remover:$(".remover option:selected").html()||"",
        forklift_worker:$(".forklift_worker option:selected").html()||"",
        tally_clerk:$(".tally_clerk option:selected").html()||"",
        remark:indexdata.remark,
        item_list:item_list,
        delivery_driver:$(".delivery_driver").val()||"",
        delivery_vehicle:$(".delivery_vehicle").val()||""
    }
    var url=uriapi+"/biz/task/removal/submit"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){

        if(data.head.status==true) {
            closeTipCancel()
            setTimeout(function () {location.href = "detail.html?id=" +GetQueryString("id");
            }, 2000);
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    },true)
}

//click事件
function clickEvent(){
    //过帐
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