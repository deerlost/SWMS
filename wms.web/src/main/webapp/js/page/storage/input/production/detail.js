var indexdata={},g=null,selectdata={},table={text:'text',popup:'',select:'select',checkbox:true,popup:'',qty:"text",input_stock_num:""},pack_all_list=[];

//加载基础数据
function init(){
    if(GetQueryString("id")==null) return false
    showloading();
    var url=uriapi+"/biz/input/production/production_input_info/"+GetQueryString("id")
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            indexdata=data.body

            $.each(indexdata.item_list,function(i,item){
                item.rid=i
                //indexdata.mes_mat_list=[100,200,150]
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
    var url=uriapi+"/biz/input/production/contract_info/"+$(".search").val()
    showloading();
    ajax(url,"get",null,function(data){
        if(data.head.status==true){
            layer.close(indexloading);
            if(data.body.msg!=undefined){
                layer.msg(data.body.msg)
                return false
            }
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
    $(".mat_code option").remove()
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
    $.each(indexdata.mat_other,function(i,item){
        $(".mat_code").append("<option value='"+item.mat_id+"' data-num='"+item.input_stock_num+"' data-afpos='"+item.afpos+"' data-wms='"+item.wms_unit_code+"'>"+item.mat_code+"</option>")
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

//搜索物料
function searchMat(){
    var obj={
        fty_id:indexdata.fty_id,
        mat_id:$(".mat_code").val(),
        mat_code:$(".mat_code option:selected").html(),
    }
    var url=uriapi+"/biz/input/production/select_package_type_list"
    showloading()
    ajax(url,"post",JSON.stringify(obj),function(data){
        layer.close(indexloading)
        if(data.head.status){
            indexdata.mat_id=$(".mat_code").val()
            indexdata.mat_code=data.body.mat_code
            indexdata.mat_name=data.body.mat_name
            indexdata.wms_unit_code=$(".mat_code option:selected").attr("data-wms")
            indexdata.package_type_list=data.body.package_type_list
            indexdata.erp_batch_list=[]
            $.each(indexdata.package_type_list,function(i,item){
                $.each(item.erp_batch_list,function(j,itemer){
                    indexdata.erp_batch_list.push({
                        erp_batch_code:itemer.erp_batch_code,
                        package_type_id:item.package_type_id
                    })
                })
            })
            indexdata.input_stock_num=$(".mat_code option:selected").attr("data-num")
            indexdata.item_list=[]
            $("[c-model='mat_name']").html(data.body.mat_name)
            $("[c-model='input_stock_num']").html(indexdata.input_stock_num)
            $("[c-model='input_stock_num']").html(toThousands($("[c-model='input_stock_num']").html()))
            dataBindTable()
        }
    },function(e){
        layer.close(indexloading)
    })
}

//绑定基础数据
function dataBindBase() {
    $(".disabled").removeClass("disabled")
    if(indexdata.status!=null){
        $(".is_input_search,.btnAddMaterial").remove()
        $(".matData").show()
        $(".product_line_id").parent().html("<span c-model='product_line_name'></span>")
        $(".device_id").parent().html("<span c-model='device_name'></span>")
        $(".class_type_id").parent().html("<span c-model='class_type_name'></span>")
        $(".syn_type_name").parent().html("<span c-model='syn_type_name'></span>")
        $(".mat_code").parent().html("<span c-model='mat_code'></span>")
        $(".stck_type").parent().html("<span c-model='stck_type_name'></span>")
        $(".btn_remark").attr("data-href","../../../../html/common/note.html?view=1")
        $(".h2red").html(indexdata.status_name)
        $(".spanno").html(indexdata.stock_input_code)
        if(indexdata.mes_doc_code=="" && indexdata.status==10){
            $(".btn_mes").show()
        }
        if(indexdata.status==10){
            $(".btn_submit").hide()
            $(".writeOff").show()
            $(".doc_code").show()
        }else if(indexdata.status==20){
            $(".btn_submit").hide()
            $(".mat_color").show()
            $(".writeType").show()
            $(".doc_code").show()
            $.each(indexdata.item_list,function(i,item){
                item.tr=1
            })
        }else if(indexdata.status==0){
            $(".btnoperate").show()
        }
        $.each(indexdata.item_list,function(i,item){
            item.package_type_id=item.package_type_name
            item.location_ids=item.location_id
            item.location_id=item.location_code+"-"+item.location_name
            item.rec_package_id="查看"

            if(item.location_code=="6006"){
                $(".btn_mes").hide()
            }
        })
        dataBindTable()
    }else{
        if(indexdata.mat_other.length==0){
            $(".mat_code").parent().html("<span c-model='mat_code'></span>")
        }
        $(".syn_type_name").val(indexdata.syn_type_id)
        $(".mat_code").val(indexdata.mat_id)
    }
    CApp.initBase("base",indexdata)
    $("[c-model='input_stock_num']").html(toThousands($("[c-model='input_stock_num']").html()))
}

//装置选择
function dataProduct(){
    $(".device_id option").remove()
    var index=$(".product_line_id option:selected").attr("data-index")
	console.log(indexdata.product_line_list[index].installations);
    $.each(indexdata.product_line_list[index].installations,function(i,item){
        $(".device_id").append("<option value='"+item.installation_id+"' data-index='i' data-code='"+item.mes_installation_code+"'>"+item.installation_name+"</option>")
    })
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
            var url=uriapi+"/biz/input/production/delete/"+GetQueryString("id")
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

    $(".mat_code").change(function(){
        searchMat()
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

    if(indexdata.package_type_list.length==0){
        layer.msg("该物料无包装类型")
        return false
    }

    indexdata.item_list.push({
        rid:indexdata.item_list.length,
        package_type_id:indexdata.package_type_list[0].package_type_id,
        name_zh:indexdata.package_type_list[0].name_zh,
        package_standard_ch:indexdata.package_type_list[0].package_standard_ch,
        sn_used:indexdata.package_type_list[0].sn_used,
        package_num:"",
        location_id:indexdata.location_list[0].location_id,
        package_standard:indexdata.package_type_list[0].package_standard,
        pack_list:[],
        decimal_place:0,
        input_stock_num:"添加",
        mat_id:indexdata.mat_id,
        rec_package_id:"添加",
        mes_list:[]
    })
    dataBindTable()
}

//获取MES数据
function getMES(index){
    return [indexdata.fty_code,$(".product_line_id option:selected").html(),indexdata.item_list[index].mes_list,indexdata.item_list[index],indexdata.mat_code,indexdata.wms_unit_code]
}

//获取MES详情数据
function getViewMES(index){
    return indexdata.item_list[index].mes_mat_list
}

//更新MES数据
function setMES(data,index,qty,num,mes_location_id){
    indexdata.item_list[index].mes_list=data
    indexdata.item_list[index].mes_location_id=mes_location_id||indexdata.item_list[index].mes_location_id
    if(qty==0 || qty==""){
        indexdata.item_list[index].input_stock_num="添加"
        indexdata.item_list[index].package_num=""
    }else{
        indexdata.item_list[index].input_stock_num=qty
        indexdata.item_list[index].package_num=num
    }
    dataBindTable()
}

//更新MES数据
function setMESinput(data,index,qty,num,mes_location_id){
    indexdata.item_list[index].mes_list=[];//data
    indexdata.item_list[index].mes_location_id="";//mes_location_id||indexdata.item_list[index].mes_location_id
    if(qty==0 || qty==""){
        indexdata.item_list[index].input_stock_num="添加"
        indexdata.item_list[index].package_num=""
    }else{
        
		if(indexdata.item_list[index].package_standard_ch=='1'){
			indexdata.item_list[index].package_num="";
			indexdata.item_list[index].input_stock_num=qty
		}else{
			indexdata.item_list[index].package_num=Math.ceil(countData.div(qty,indexdata.item_list[index].package_standard));
			indexdata.item_list[index].input_stock_num=qty;
		}
        
    }
    dataBindTable()
}


//获取全部关联包装单
function getPackAll(){
    return pack_all_list
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
function setPack(data,index,standard_num,dataBatch){
    pack_all_list[index]=data
    indexdata.item_list[index].pack_list=data
    indexdata.item_list[index].standard_num=standard_num
    indexdata.item_list[index].dataBatch=dataBatch
    indexdata.item_list[index].production_batch=dataBatch
    indexdata.item_list[index].quality_batch=dataBatch
    if(standard_num>0){
        indexdata.item_list[index].rec_package_id=standard_num
    }else{
        indexdata.item_list[index].rec_package_id="添加"
    }

    dataBindTable()
}
/*{ th: '入库数量', col: 'input_stock_num',sort:false,width:150,type:'popup',class:'input_stock_num',
                popup:{
                    href:"popup_mes.html"+table.input_stock_num,
                    area:"900px,500px",
                    title:"入库数量"
            }},*/
//绑定表格
function dataBindTable(){
    g=$("#id_div_grid").iGrid({
        columns: [
            { th: '包装类型', col: 'package_type_id',sort:false,width:110,type:table.select,data:indexdata.package_type_list,value:"package_type_id",text:"package_type_code"},
            { th: '包装规格', col: 'package_standard_ch',sort:false,width:110},
            { th: '件数', col: 'package_num',sort:false,width:150},
            { th: '入库数量', col: 'input_stock_num',sort:false,width:150,type:'button',class:'input_stock_num',button:{
				text:"input_stock_num"
			}},
            { th: '生产批次', col: 'production_batch',sort:false,width:150,type:table.text,class:'production_batch',maxlength:20},
            { th: 'ERP批次', col: 'erp_batch',sort:false,type:table.select,data:indexdata.erp_batch_list,value:'erp_batch_code',parent:'package_type_id',text:'erp_batch_code',width:200,class:'erp_batch'},
            { th: '质检批次', col: 'quality_batch',sort:false,width:150},
//            { th: '批次', col: 'batch',sort:false,width:150},
            { th: '库存地点', col: 'location_id',sort:false,type:table.select,data:indexdata.location_list,value:'location_id',text:["location_code","location_name"],width:230,class:'location_id'},
            { th: '关联包装单(KG)', col: 'rec_package_id',sort:false,width:"120",type:"popup",class:'rec_package_id',
                popup:{
                    href:"popup_package.html"+table.popup,
                    area:"950px,500px",
                    title:"关联包装单"
                }},
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
        resizehead:true,
        required:{
            columns:["input_stock_num"],
            isAll:false
        },
        loadcomplete:function(){
            $.each(indexdata.item_list,function(i,item){
                if(item.sn_used==1){
                    $(".txtproduction_batch").eq(i).attr("disabled","disabled")
                }
            });
			
			//将最后一条数据加上默认的location_code（第一条的location_code）
			if(indexdata.item_list[indexdata.item_list.length-1].location_code==undefined)
				indexdata.item_list[indexdata.item_list.length-1].location_code=indexdata.location_list[0].location_code;
			
        },
        cselect:function(a,b,c,d,e){
            if(a=="package_type_id"){
                indexdata.item_list[c].package_standard_ch= e.package_standard_ch
                indexdata.item_list[c].package_standard= e.package_standard
                indexdata.item_list[c].sn_used= e.sn_used
                indexdata.item_list[c].rec_package_id="添加"
                indexdata.item_list[c].pack_list=[]
                indexdata.item_list[c].dataBatch=""
                indexdata.item_list[c].production_batch=""
                indexdata.item_list[c].quality_batch=""
                indexdata.item_list[c].name_zh=e.name_zh
                indexdata.item_list[c].input_stock_num="添加"
                indexdata.item_list[c].package_num=""
                indexdata.item_list[c].mes_list=[]
                indexdata.item_list[c].mes_location_id=""
                dataBindTable()
            }
			//当地点下拉框时，给location_code赋值
			if(a=="location_id"){
				indexdata.item_list[c].location_code=e.location_code;
				indexdata.item_list[c].package_standard_ch= d.package_standard_ch
                indexdata.item_list[c].package_standard= d.package_standard
                indexdata.item_list[c].sn_used= d.sn_used
                indexdata.item_list[c].rec_package_id="添加"
                indexdata.item_list[c].pack_list=[]
                indexdata.item_list[c].dataBatch=""
                indexdata.item_list[c].production_batch=""
                indexdata.item_list[c].quality_batch=""
                indexdata.item_list[c].name_zh=d.name_zh
                indexdata.item_list[c].input_stock_num="添加"
                indexdata.item_list[c].package_num=""
                indexdata.item_list[c].mes_list=[]
                indexdata.item_list[c].mes_location_id=""
                dataBindTable()
			}
        },
        cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
            if(a=="input_stock_num" && d>0){
                indexdata.item_list[c].package_num= Math.ceil(countData.div(indexdata.item_list[c].input_stock_num,indexdata.item_list[c].package_standard))
                dataBindTable()
            }else if(a=="package_num"){
                indexdata.item_list[c].input_stock_num= countData.mul(indexdata.item_list[c].package_num,indexdata.item_list[c].package_standard)
                dataBindTable()
            }
            if(a=="production_batch"){
                indexdata.item_list[c].quality_batch=indexdata.item_list[c].production_batch
                dataBindTable()
            }
        },clickbutton(a,b,c){//Button点击后触发
			console.log(c);
			var mes_installation_code = $(".device_id option:selected").attr("data-code");
            if(c.location_code=='6006'&&mes_installation_code==undefined){
                return false
            }
			if(c.location_code=='6006'&&mes_installation_code=='Z5SW042'){
				layer.prompt({title: '入库数量', formType: 3}, function(text, index){
					if(!FormDataCheck.isDecimal(text)){
						layersMoretips("请输入数字",$(".layui-layer-content .layui-layer-input"));
					}else{
						layer.close(index);
						setMESinput(null,b,text,0,null);
					}
					//layer.msg(text);
				});
			}else{
				var href="popup_mes.html?index="+b+table.input_stock_num;
				layer.open({
					type: 2,
					title: '入库数量',
					shadeClose: false,
					shade: 0.8,
					area: ['1100px','500px'],
					content: href
				});
			}
		}
    });
}

//过帐
function submit(){
    var is_checked=true,item_list=[],stock_input_rid= 1,num=0
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
            if(item.pack_list!=undefined){
                $.each(item.pack_list,function(j,itemer){
                    if(itemer.select==1){
                        position_list.push(itemer.package_id)
                    }
                })
            }
            if(GetQueryString("id")){
                $.each(item.ret_package_list,function(j,itemer){
                    position_list.push(itemer.package_id)
                })
            }
            if(position_list.length==0 && GetQueryString("id")==null && item.sn_used==1){
                layersMoretips("必填项",$(".rec_package_id").eq(i))
                is_checked=false
            }

            if(item.sn_used==1 && item.input_stock_num!=item.standard_num){
                layersMoretips("入库数量与包装单数量不一致",$(".input_stock_num").eq(i))
                is_checked=false
            }

            if(item.package_standard_ch!=1 && countData.mod(item.input_stock_num,item.package_standard)!=0){
                layersMoretips("入库数量为包装规格的整数倍",$(".input_stock_num").eq(i))
                is_checked=false
            }

            if(item.sn_used!=1 && item.production_batch==""){
                layersMoretips("必填项",$(".txtproduction_batch").eq(i))
                is_checked=false
            }

            num=countData.add(num,item.input_stock_num)
            var mes_mat_list=[]
            if(GetQueryString("id")==null){
                $.each(item.mes_list,function(j,itemer){
                    if((itemer.input_stock_num!="" && itemer.input_stock_num!="0") || (itemer.package_num!="" && itemer.package_num!="0")){
                        mes_mat_list.push({
                            mes_code:itemer.mes_code,
                            mes_package_standard:itemer.mes_package_standard,
                            mes_rank:itemer.mes_rank,
                            input_stock_num:itemer.input_stock_num,
                            package_num:itemer.package_num,
                            mes_bch:itemer.mes_bch,
                            mes_location_id:itemer.mes_location_id,
                            mes_location_name:itemer.mes_location_name
                        })
                    }
                })
            }

            var is_return=false
            $.each(item_list,function(j,itemer){
                if(item.package_type_id==itemer.package_type_id && item.production_batch==itemer.production_batch && item.erp_batch==itemer.erp_batch){
                    layer.msg("不允许添加两行相同包装类型的行项目")
                    is_checked=false
                    is_return=true
                    return false
                }
            })

            if(is_return) return false

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
                mes_location_id:item.mes_location_id,
                ret_pkg_operation_id:position_list,
                mes_mat_list:mes_mat_list
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

/*    if(parseFloat(num)>parseFloat($("[c-model='input_stock_num']").html().replace(/,/g,""))){
        layer.msg("入库数量总和不能大于订单数量")
        is_checked=false
    }*/

    if(is_checked==false) return false

    if(indexdata.mat_other==undefined){
    	indexdata.mat_other=[];
    }
    var obj={
        mat_id:indexdata.mat_id,
        mat_code:indexdata.mat_code,
        afpos:indexdata.mat_other.length==0?null:$(".mat_code option:selected").attr("data-afpos"),
        unit_id:indexdata.unit_id,
        input_stock_num:indexdata.input_stock_num,
        business_type_id:indexdata.business_type_id,
        stock_input_id:GetQueryString("id")||"",
        produce_order_code:indexdata.produce_order_code,
        fty_id:indexdata.fty_id,
        product_line_id:$(".product_line_id").val(),
        stck_type:$(".stck_type").val(),
        device_id:$(".device_id").val(),
        class_type_id:$(".class_type_id").val(),
        syn_type_id:$(".syn_type_name").val(),
        remark:indexdata.remark,
        item_list:item_list
    }
    var url=uriapi+"/biz/input/production/production"
    showloading();
    ajax(url,"post",JSON.stringify(obj),function(data){
        closeTipCancel()
        setTimeout(function () { location.href = "detail.html?id="+data.body.stock_input_id; }, 2000);
    }, function(e) {
        layer.close(indexloading);
    },true)
}

//mes同步
function submitMES(){
    var url=uriapi+"/biz/input/production/takeMes/"+GetQueryString("id")
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

//冲销
function writeOff(check){
    var item_list=[]
    $.each(indexdata.item_list,function(i,item){
    	item.location_id = item.location_ids
        var ret_package_list=[]
        $.each(item.ret_package_list,function(j,itemer){
            ret_package_list.push(itemer.package_id)
        })
        item_list.push({
            ret_package_list:ret_package_list
        })
    })


    var obj={
        stock_input_id:GetQueryString("id"),
        item_id:indexdata.item_id_list,
        doc_year:indexdata.doc_year_list,
        is_check:check,
        item_list:indexdata.item_list,
        mat_code:indexdata.mat_code,
        mat_id:indexdata.mat_id,
        fty_id:indexdata.fty_id,
        unit_id:indexdata.unit_id
    }
    var url=uriapi+"/biz/input/production/write_off"
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


$(function () {
    indexdata.item_list = []
    $("[c-model='create_user']").html(loginuserdata.body.user_name)
    $("[c-model='create_time']").html(GetCurrData())
    tableedit($("body"))
    init()
    clickEvent()
    if (GetQueryString("id")) {
        table = {text: '', popup: '', select: '', checkbox: false, popup: '?view=1', qty: "number", input_stock_num: "&view=1"}
    } else {
        closeTip()
        $(".deltable").show()
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
    $(".product_line_id").change(function () {
        $.each(indexdata.item_list,function(i,item){
            item.package_num=""
            item.input_stock_num="添加"
            item.mes_list=[]
            item.mes_location_id=""
        })
        dataProduct()
        dataBindTable()
    })
    $(".btnAddMaterial").click(function () {
        AddMaterial()
    })
});