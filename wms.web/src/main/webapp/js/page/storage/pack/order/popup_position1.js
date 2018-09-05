var indexdata=null,position_list=[],g1=null,del_pack=[],is_run=true

//搜索
function search(){
    var is_ajax=true
    var kw=$(".ico-search").val();
    if(kw==""){
        return false
    }
    $.each(position_list,function(i,item){
        if(item.package_code==kw){
            delPackage(kw,i)
            is_ajax=false
            return false
        }
    })
    $.each(del_pack,function(i,item){
        if(item.package_code==kw){
            is_ajax=false
            position_list.push({
                package_id:item.package_id,
                package_code:item.package_code,
                package_type_code:item.package_type_code,
                unit_name:item.unit_name,
                pallet_id:"",
            })
            del_pack.splice(i,1)
            number()
            dataBindPack()
            return false
        }
    })



    if(is_ajax==false) return false

    if(is_run){
        is_run=false
    }else{
        return false
    }
    var url=uriapi+"/biz/pkg/get_pkg_pallet_list?keyword="+kw+"&package_type_id="+GetQueryString("package_type_id")
    showloading();
    ajax(url,"get",null,function(data){
        is_run=true
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.type=="pkgtype"){
                position_list.push({
                    package_id:data.body.pkgtype_list[0].package_id,
                    package_code:data.body.pkgtype_list[0].package_code,
                    package_type_code:data.body.pkgtype_list[0].package_type_code,
                    unit_name:data.body.pkgtype_list[0].unit_name,
                    pallet_id:"",
                })
            }else if(data.body.type=="pallet"){
                layer.msg("请输入包装物信息")
                return false
            }else{
                layer.msg(data.body.msg)
                return false
            }
            number()
            dataBindPack()
        }else{
            layer.close(indexloading)
        }
    }, function(e) {
        layer.close(indexloading);
    })
}

//删除二次扫描包装物
function delPackage(kw,index){
    layer.confirm("包装物已存在，是否删除", {
        btn: ['删除', '取消'],
        icon: 3
    }, function() {
        $.each(position_list,function(i,item){
            if(item.package_code==kw){
                delRePack(item)
                return false
            }
        })
        position_list.splice(index,1)
        number()
        dataBindPack()
        layer.close(parseInt($(".layui-layer-shade").attr("times")));
    }, function() {
    });
}



//绑定表格包
function dataBindPack(){
    g1=$("#id_div_grid_pack").iGrid({
        columns: [
            { th: '包装物条码', col: 'package_code', sort:false,width:300},
            { th: '包装类型', col: 'package_type_code', sort:false,width:150},
            { th: '单位', col: 'unit_name', sort:false,width:150},
            { th: '',type:"delete",tips:"确定删除吗？",width:60,align:'center'},
        ],
        data: position_list,
        skin:"tablestyle4",
        delete:function(a,b){
            delRePack(a)
            position_list.splice(b,1)
            number()
            dataBindPack()
        }
    })
}

//上架数量汇总
function number(){
    $(".stock_task_qty").html(position_list.length)
}

//记录删除物料
function delRePack(data){
    var is_add=true
    $.each(del_pack,function(i,item){
        if(item.package_code==data.package_code){
            is_add=false
            return false
        }
    })
    if(is_add){
        del_pack.push(data)
    }
}

//提交
function submit(){
    parent.setDataPack(GetQueryString("index"),position_list,$(".stock_task_qty").html())
    $(".btn_iframe_close_window").click()
}

$(function(){
    window.focus()
    var data = parent.getDataMateriel(GetQueryString("index"))
    indexdata = data
    $(".name_zh").html(indexdata.name_zh)
    $.each(data.position_list,function(i,item){
        position_list[i]={}
        position_list[i].package_id=item.package_id                       //包装物id
        position_list[i].package_code=item.package_code                   //包装物code
        position_list[i].package_type_code=item.package_type_code         //包装物类型
        position_list[i].unit_name=item.unit_name                         //包装物单位
        position_list[i].package_standard=item.package_standard                         //包装物重量
        position_list[i].pallet_id=item.pallet_id               //托盘id
        position_list[i].pallet_code=item.pallet_code           //托盘code
        position_list[i].max_weight=item.max_weight             //托盘最大重量
    })
    dataBindPack()
    number()
    scan()
    if(GetQueryString("view")!=null){
        $(".keyinput").html("")
        $(".btn_table_delete,.btn_submit").remove()
        $(".btn_iframe_close_window").html("　关闭　")
    }

    $("a.search").click(function(){
        search()
    })
    $("input.ico-search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            search();
        }
    });
    $(".addpallet").click(function(){
        addPallet()
    })
    $(".btn_submit").click(function(){
        submit()
    })

})