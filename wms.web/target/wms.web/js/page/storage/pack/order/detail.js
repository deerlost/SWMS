var indexdata={},g1=null,selectdata={class_type:null,fty:null},table={href:"popup_position1.html",area:"700px,400px"}

//加载基础信息
function init(){
    var url=uriapi+"/biz/pkg/get_pkg_class_line_list"
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.class_type=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    var url=uriapi+"/auth/get_fty_location_for_user?fty_type=1"
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            selectdata.fty=data.body
            dataBindBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
    if(GetQueryString("id")!=null){
        var url=uriapi+"/biz/pkg/get_pkg_detail/"+GetQueryString("id")
        ajax(url,"get",null,function(data){
            if(data.head.status==true){
                indexdata=data.body
                dataBindBase()
            }else{
                layer.close(indexloading)
            }
        }, function(e) {
            layer.close(indexloading);
        })
    }

}

//绑定基础数据
function dataBindBase(){
    if(selectdata.fty==null || selectdata.class_type==null || (GetQueryString("id")!=null && indexdata.status==undefined)) return false
    layer.close(indexloading)

    if(GetQueryString("id")){
        selectdata.class_type.product_line_list=indexdata.product_line_list
    }

    $.each(selectdata.class_type.class_type_list,function(i,item){
        $("[c-model='class_type_id']").append("<option value='"+item.class_type_id+"'>"+item.class_name+"</option>")
    })
    $.each(selectdata.class_type.product_line_list,function(i,item){
        $(".product").append("<option value='"+item.product_line_id+"' data-index='"+i+"'>"+item.product_line_name+"</option>")
    })
    $.each(selectdata.fty,function(i,item){
        $(".factory").append("<option value='"+item.fty_id+"'>"+item.fty_name+"</option>")
    })
    $("[c-model='class_type_id']").val(selectdata.class_type.class_type_id)
    $("[c-model='create_name']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    dataProduct()
    if(indexdata.status==4){
        finish()
    }else if(indexdata.status==1 || indexdata.status==2){
        $(".h2red").html(indexdata.status_name)
        $(".spanno").html(indexdata.pkg_operation_code)
        $(".pallet").val(indexdata.is_pallet)
        if(indexdata.is_pallet==0){
            table.href="popup_position1.html"
            table.area="700px,400px"
        }else{
            table.href="popup_position2.html"
            table.area="1200px,650px"
        }
        setDataBase()
    }
    if(indexdata.status==1 || indexdata.status==2){
        $(".btnoperate").show()
        closeTip()
    }
    if(!GetQueryString("id")){
        closeTip()
    }
}

//已提交状态
function finish(){
    $(".h2red").html(indexdata.status_name)
    $(".submitbutton a").hide()
    $(".back").show()
    $(".input-addon").remove()
    $(".fty").html("<span  c-model='fty_name'>--</span>")
    $(".product").parent().html("<span  c-model='product_line_name'>--</span>")
    $(".installation").parent().html("<span  c-model='installation_name'>--</span>")
    $("[c-model='class_type_id']").parent().html("<span  c-model='class_type_name'>--</span>")
    $("[c-model='product_batch']").parent().html("<span  c-model='product_batch'>--</span>")
    $("[c-model='package_team']").parent().html("<span  c-model='package_team'>--</span>")
    $(".pallet").parent().html(indexdata.is_pallet==1?"启用":"不启用")
    $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
    if(indexdata.is_pallet==0){
        table.href="popup_position1.html?view=1"
        table.area="700px,400px"
    }else{
        table.href="popup_position2.html?view=1"
        table.area="1200px,650px"
        $(".pallet").attr("checked",true)
    }
    $(".pallet").attr("disabled",true)
    CApp.initBase("base",indexdata)
    $(".matData").show()
    $(".btnAddMaterial").hide()
    $(".spanno").html(indexdata.pkg_operation_code)
    dataTable()
}

//装置选择
function dataProduct(){
    $(".installation option").remove()
    var index=$(".product option:selected").attr("data-index")
    $.each(selectdata.class_type.product_line_list[index].installations,function(i,item){
        $(".installation").append("<option value='"+item.installation_id+"' data-index='i'>"+item.installation_name+"</option>")
    })
}

//添加物料
function addMaterial(data){
    if(indexdata.item_list.length==0){
        indexdata.item_list=data
    }else{
        if(indexdata.item_list[0].mat_id!=data.mat_id){
            indexdata.item_list=data
        }
    }
    indexdata.item_list[0].order_qty=indexdata.item_list[0].order_qty==undefined?"--":indexdata.item_list[0].order_qty
    dataTable()
}

//搜索
function search(){
    var url=uriapi+"/biz/pkg/get_refer_receipt?code="+$(".search").val()
    if($(".search").val()==""){
        return false
    }
    showloading()
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading)
            indexdata=data.body
            setDataBase()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//更新基础数据
function setDataBase(){
    $(".fty").html("<span  c-model='fty_name'>--</span>")
    $(".product").val(indexdata.product_line_id)
    dataProduct()
    $(".installation").val(indexdata.installation_id)
    CApp.initBase("base",indexdata)
    $(".matData").show()
    $(".btnAddMaterial").hide()
    dataTable()
}

//获取基础备注信息
function GetRemark(){
    return indexdata.remark||""
}

//更换基础备注信息
function SetRemark(data){
    indexdata.remark=data
}

//获取物料信息
function getDataMateriel(index){
    return indexdata.item_list[index]
}

//更新包装信息
function setDataPack(index,data,pkg_qty){
    indexdata.item_list[index].position_list=data
    indexdata.item_list[index].position_type=false
    indexdata.item_list[index].pkg_qty=pkg_qty==0?"添加":pkg_qty
    indexdata.item_list[index].name_kg=pkg_qty==0?"KG":(countData.mul(pkg_qty,indexdata.item_list[index].package_standard))+"KG"
    dataBindTable()
}

//更新包装信息
function setDataPack2(index,data,pkg_qty){
    indexdata.item_list[index].position_list=data
    indexdata.item_list[index].position_type=true
    indexdata.item_list[index].pkg_qty=pkg_qty==0?"添加":pkg_qty
    indexdata.item_list[index].name_kg=pkg_qty==0?"KG":(countData.mul(pkg_qty,indexdata.item_list[index].package_standard))+"KG"
    dataBindTable()
}

//获取物料
function getItemList(){
    return indexdata.item_list
}

//表格数据
function dataTable(){
    $.each(indexdata.item_list,function(i,item){
        if(indexdata.item_list[0].pkg_qty==undefined || indexdata.item_list[0].pkg_qty=="" || indexdata.item_list[0].pkg_qty=="0"){
            item.pkg_qty="添加"
        }
        if(indexdata.item_list[0].position_list==undefined){
            indexdata.item_list[0].position_list=[]
        }
        item.package_type_id=item.package_type_id||item.package_type_list[0].package_type_id
        item.package_standard=item.package_standard||item.package_type_list[0].package_standard
        item.name_zh=item.name_zh||item.package_type_list[0].name_zh
        item.name_kg=item.pkg_qty=="添加"?"KG":(countData.mul(item.pkg_qty,item.package_standard))+"KG"
    })
    dataBindTable()
}

//绑定表格
function dataBindTable(){
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', sort:false},
            { th: '物料描述', col: 'mat_name', sort:false},
            { th: '单位', col: 'unit_name', sort:false},
            { th: '订单数量', col: 'order_qty',type:'number', sort:false },
            { th: '包装类型', col: 'package_type_id', sort:false,type:'select',data:'package_type_list',value:'package_type_id',text:'package_type_code'},
            { th: '本次包装数量', col: 'pkg_qty', sort:false,type:'popup',align:'center',class:'qty',width:120,
                popup:{
                    href:table.href,
                    area:table.area,
                    title:"本次包装数量",
                    args:['package_type_id']
                }
            },
            { th: '包装重量', col: 'name_kg', sort:false,width:80},
        ],
        data: indexdata.item_list,
        skin:"tablestyle4",
        rownumbers:false,
        absolutelyWidth:true,
        percent:20,
        loadcomplete:function(){
            if(indexdata.status==4){
                g1.displaycolumn([4],false)
            }
        },
        cselect:function(a,b,c,d,e){//下拉框选择后触发
            indexdata.item_list[0].pkg_qty=""
            indexdata.item_list[0].position_list=[]
            indexdata.item_list[0].name_zh= e.name_zh
            indexdata.item_list[0].package_standard= e.package_standard
            indexdata.item_list[0].position_type=false
            dataTable()
        },

    });
}

//点击事件
function clickEvent(){
    //删除此单据
    $(".btndelete").click(function() {
        layer.confirm("是否删除", {
            btn: ['删除', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            var url=uriapi+"/biz/pkg/delete_pkg_operation?pkg_operation_id="+GetQueryString("id")
            showloading();
            ajax(url,"get",null,function(data){
                closeTipCancel()
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
    $(".btn_submit").click(function() {
        layer.confirm("是否提交", {
            btn: ['提交', '取消'],
            icon: 3
        }, function () {
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submit(2)
        })
    })
}

//提交
function submit(status){
    var checked=true
    if(indexdata.item_list.length==0){
        layer.msg("至少添加一条物料信息")
        return false
    }
    if(indexdata.item_list[0].pkg_qty=="添加" || indexdata.item_list[0].pkg_qty=="0"){
        layersMoretips("必填项",$(".qty"))
        checked=false
    }
    if($("[c-model='product_batch']").val()==""){
        layersMoretips("必填项",$("[c-model='product_batch']"))
        checked=false
    }
    if($("[c-model='package_team']").val()==""){
        layersMoretips("必填项",$("[c-model='package_team']"))
        checked=false
    }
    if(indexdata.item_list[0].order_qty!=undefined && indexdata.item_list[0].order_qty!=0 && parseFloat(indexdata.item_list[0].pkg_qty)>parseFloat(indexdata.item_list[0].order_qty)){
        layersMoretips("本次包装数量不能大于订单数量",$(".qty"))
        checked=false
    }
    if(checked==false) return false

    var position_list=[],pkg_operation_position_id= 1,item_list=[],pkg_operation_rid=1
    $.each(indexdata.item_list[0].position_list,function(i,item){
        if(indexdata.item_list[0].position_type){
            $.each(item.package_list,function(j,itemer){
                if(item.package_id!==""){
                    position_list.push({
                        package_id: itemer.package_id,
                        pallet_id: item.pallet_id,
                        pkg_operation_position_id:pkg_operation_position_id,
                        package_code:itemer.package_code,
                    })
                    pkg_operation_position_id++
                }
            })
        }else{
            if(item.package_id!==""){
                position_list.push({
                    package_id: item.package_id,
                    pallet_id: item.pallet_id,
                    pkg_operation_position_id:pkg_operation_position_id,
                    package_code:item.package_code,
                })
                pkg_operation_position_id++
            }
        }
    })
    item_list.push({
        fty_id: $(".factory").val()||indexdata.fty_id,
        mat_id: indexdata.item_list[0].mat_id,
        order_qty: indexdata.item_list[0].order_qty||0,
        pkg_operation_rid: pkg_operation_rid,
        pkg_qty: indexdata.item_list[0].pkg_qty,
        package_type_id:indexdata.item_list[0].package_type_id,
        position_list:position_list
    })
    var obj={
        class_type_id: $("[c-model='class_type_id']").val(),
        installation_id: $(".installation").val(),
        package_team: $("[c-model='package_team']").val(),
        product_batch:$("[c-model='product_batch']").val(),
        product_line_id: $(".product").val(),
        refer_receipt_code:indexdata.refer_receipt_code||"",
        refer_receipt_type:indexdata.refer_receipt_type||"",
        remark:indexdata.remark||"",
        status: status,
        is_pallet:$(".pallet").val(),
        pkg_operation_id:GetQueryString("id")||0,
        item_list:item_list
    }
    console.log(obj)
    var url=uriapi+"/biz/pkg/save_pkg_operation"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            closeTipCancel()
            setTimeout(function () { location.href = "detail.html?id="+data.body.pkg_operation_id; }, 2000);
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
    init()
    dataBindTable()
    clickEvent()
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
    $("a.btn-search").click(function () {
        search();
    });
    $(".product").change(function(){
        dataProduct()
    })
    $("[c-model='fty_id']").change(function(){
        indexdata.item_list=[]
        dataBindTable()
    })
    $(".pallet").change(function(){
        if($(".pallet").val()==1){
            table.href="popup_position2.html"
            table.area="1200px,650px"
        }else{
            table.href="popup_position1.html"
            table.area="700px,400px"
        }
        if(indexdata.item_list.length!=0){
            indexdata.item_list[0].pkg_qty=""
            indexdata.item_list[0].position_list=[]
            indexdata.item_list[0].position_type=false
            dataTable()
        }
    })

})