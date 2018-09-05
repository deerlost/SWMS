var indexdata=null,g1=null,cjinfo=[],menges = [],indexes=-1,receipt_code="",remarkinfo='',indexinfo=false,basedatas=null;

function init(number){
   var url=uriapi+"/biz/return/sale/get_return_order_info?return_id="+number;
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        $.each(data.body.item_list, function(i,item) {
    		item.return_qty=(item.return_qty == undefined || item.return_qty == null|| item.return_qty == "") ? "添加" : item.return_qty;
    		 if(item.erp_batch!=null&&item.erp_batch!==""){
    			item.iserp=true;
    		}
    	});
        indexdata=data;       	
        databindInit()
    },function(e) {
		layer.close(indexloading);
	})
	
}
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/return/sale/get_base_info";
    showloading();
    ajax(url,"GET",null,function(data){
    layer.close(indexloading);
    basedatas=data;
    $.each(data.body.class_type_list, function(i, item) {
		$(".shiftinfo").append(returnOption(item.class_type_id,item.class_name));
		})
    	$(".shiftinfo").val(data.body.class_type_id);
    	$.each(data.body.product_line_list,function(i,item){
    		$(".product_line").append("<option value='" + item.product_line_id + "'data-index='" + i + "'>" + item.product_line_name + "</option>")
    	})
    	if(data.body.product_line_list[0].installations.length>0){
    		$.each(data.body.product_line_list[0].installations,function(i,item){
    			$(".devices").append(returnOption(item.installation_id,item.installation_name))
    		})
    	}
    },function(e) {
		layer.close(indexloading);
	}) 
}
//绑定基础数据
function databindInit(){
    CApp.initBase("zmkpf",indexdata.body);
    if(GetQueryString("id")==null){
    	$(".h2red").html("创建");
    }else{
    	closeTipCancel();
    	$(".jhdh").remove();
    	$(".shiftinfo").remove();
    	$(".product_line").remove();
    	$(".devices").remove()
    	$(".prodinfo").append("<span class='product_line'>"+indexdata.body.product_line_name+"</span>")
    	$(".devicinfo").append("<span class='devices'>"+indexdata.body.installation_name+"</span>")
    	$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_name+"</span>")
    	$(".deveryno").html(indexdata.body.delivery_code);
	    if(indexdata.body.status==0){//未完成	
	    	$(".btnoperate").show();
	    	$(".submission").hide();
	    	$(".btnsubmit").show();
	    	$(".against").hide();
	    }else if(indexdata.body.status==10){  //已完成
		   if(indexdata.body.mes_doc_code==""){
	    		$(".synmes").show();
	    	}
	    	$(".doc_code").show()
	        $(".against").show();
	        $(".submission").hide();
	        $(".btnsubmit").remove();
	    }else if(indexdata.body.status==20){//已冲销
	    	if(indexdata.body.mes_write_off_code==""){
	    		$(".write_synmes").show();
	    	}
	    	$(".doc_code").show();
	    	$(".write_doc_code").show();
	    	$(".submission").remove();
	        $(".btnsubmit").remove();
	        $(".against").hide();
	    }else if(indexdata.body.status==3){//已作业
	    	$(".btnoperate").show();
	    	$(".submission").hide();
	    	$(".against").hide();
	    	$(".btnsubmit").show();
	    }else if(indexdata.body.status==2){//已提交
	    	$(".btnoperate").show();
	    	$(".submission").hide();
	    	$(".against").hide();
	    	$(".btnsubmit").hide();
	    }
    	$(".spanno").html(indexdata.body.return_code)
    	$(".tbtype").val(indexdata.body.syn_type.toString())
    }
    databindTable()
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
//绑定表格
function databindTable(){
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
            	{ th: '序号', col: 'rid', sort:false},
                { th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false },
                { th: '单位', col: 'unit_name', sort:false },
				{ th: '交货数量', col: 'delivery_qty', sort:false ,type:"number"},
                { th: '当前退货数量', col: 'return_qty', sort:false,type:'popup',distype:"number",
                	popup:{
	                	href:false,//默认带index= 数据row下标。
						area:"900px,450px",
						title:"添加当前配货数量",
						column:"isviews",
						links:[{value:"2",href:"cargo.html"},
							   {value:"1",href:"setcargo.html?cargotype=1"},
							]
                	}
                },
                { th: '已上架数量', col: 'have_shelves_qty', sort:false },
                { th: 'ERP批次', col: 'erp_batch',type:'select',sort:false,width:150,data:'erp_batch_list',value:'erp_batch',text:'erp_batch',disabled:"iserp"},
                { th: '库存地点', col: 'location_id',type:'select',sort:false,width:200,data:'loc_list',value:'location_id',text:['location_code','location_name'],disabled:"isselect"},
                { th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"查看",
							href: "noteview.html",
							area: "500px,400px",
							title: "备注信息"
						}
                },
              	{th: '行类型', col: 'ztrvalue', class: "", type: "tr",
                    tr: {
                    value: 1,
                    class: "pinkbg"
                    }
                }
            ],
            data: null,
            checkbox:true,
            resizehead:true,
            percent:30,
            skin:"tablestyle4",//皮
            cselect:function(a,b,c,d,e){
			var test="";
			if(a=="location_id"){
				test="库存地点"
			}
			if(a=="erp_batch"){
				test="ERP批次"
			}
			if(indexdata.body.item_list[c].return_qty!="添加"){
				indexinfo=true;
			}else{
				indexinfo=false;
			}
			if(indexinfo==true){
				layer.confirm("是否改变"+test+"?", {
			            btn: ['确定', '取消'],
			            icon: 3
			      }, function(index) {
			       		indexdata.body.item_list[c].return_qty="添加";
			       		indexdata.body.item_list[c].position_list=[];
			       		menges.splice(c, 1);
			       		g1.showdata(indexdata.body.item_list);
			            layer.close(index);
			        }, function() {
			
			        });
				}
			}
        })
    }else{
        $.each(indexdata.body.item_list,function(i,item){
            if(indexdata.body.status==20){
                item.ztrvalue=1
               }
          if(GetQueryString("id")==null){
    			item.isedit="添加"
    			item.isviews="1";
    			item.isselect=false;
    		}else{
    			item.isedit="查看"
    			item.isviews="2";
    			item.isselect=true;
    		}
        })
        if(GetQueryString("id")!=null){
        	g1.setCheckboxdata(false)
        }
        g1.showdata(indexdata.body.item_list)
    }
}
//点击事件  过帐/删除
function clicksubmit(){
    $(".btnsubmit").click(function () {
        layer.confirm("是否过账？", {
            btn: ['过账', '取消'],
            icon: 3
        }, function () {
            submit(null);
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
    //冲销
    $(".against").click(function() {
        layer.confirm("是否冲销？", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function() {
            writeOff();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
        //同步MEs
     $(".synmes").click(function() {
        layer.confirm("是否同步MES？", {
            btn: ['同步', '取消'],
            icon: 3
        }, function() {
            synmes();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
    //同步冲销MES
    $(".write_synmes").click(function() {
        layer.confirm("是否同步冲销MES？", {
            btn: ['同步', '取消'],
            icon: 3
        }, function() {
            synmes();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
    //删除页面
    $(".btndelete").click(function() {
        layer.confirm("是否删除？", {
            btn: ['删除', '取消'],
            icon: 3
        }, function() {
            deletes();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
    //提交
    $(".submission").click(function(){
   	  layer.confirm("是否提交？", {
            btn: ['提交', '取消'],
            icon: 3
        }, function() {
        	 layer.close(parseInt($(".layui-layer-shade").attr("times")));
            submission();
        }, function() {

        });
    })  
}
//同步mes
function synmes(){
	var url=uriapi+"/biz/return/sale/save_mes_again?return_id="+GetQueryString("id");
	showloading()
	ajax(url,"GET",null,function(data){
      if(data.head.status==true){
      	if(indexdata.body.status==20){
      		$("[c-model='mes_doc_code']").html(data.body.mes_write_off_code);
      		$(".write_synmes").hide();
      	}else{
      		$("[c-model='mes_doc_code']").html(data.body.mes_doc_code);
      		$(".synmes").hide();
      	}
      	layer.close(indexloading);
      }else{
      	layer.close(indexloading);
      }
    },function(e) {
        layer.close(indexloading);
    },true)
	
}
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/return/sale/delete?return_id=" + GetQueryString("id")
    ajax(url, "GET", null, function(data) {
        if(data.head.status == true) {
        	location.href = "list.html";
            layer.close(indexloading);
        } else {
            layer.close(indexloading);
        }

    }, function(e) {
        layer.close(indexloading);
    },true);
}
//获取基础备注信息
function GetRemark(){
	var obj={};
	if(GetQueryString("id")==null){
		obj.isedit=true;
	}else{
		obj.isedit=false;
	}
	if(indexdata!=null&&indexdata.body.remark!=""){
		remarkinfo=indexdata.body.remark;
	}
	obj.remark=remarkinfo;
     return obj;
}

//更换基础备注信息
function SetRemark(data){
	if(indexdata!=null){
		indexdata.body.remark=data;
	}
   remarkinfo=data;
}
//获取行项目备注信息
function remakinfo(value,index){
	indexdata.body.item_list[index].remark=value;
}
//往子页面传递行项目备注信息
function getremarkinfo(index){
	var obj={};
	if(GetQueryString("id")==null){
		obj.isedit=true;
	}else{
		obj.isedit=false;
	}
	obj.remark=indexdata.body.item_list[index].remark
	return obj
}
function refer_receipt(){
	if($(".yldh").val()==""){
		layer.msg("请输入交货单号");
		return false;
	}
	var erplist=[];
	$(".deveryno").html($(".yldh").val());
	var obj={};
	obj.refer_receipt_code=$(".yldh").val();
	showloading();
	var url = uriapi + '/biz/return/sale/get_delivery_order_info?condition='+$(".yldh").val();
	ajax(url, 'GET',null, function(data) {
		if(data.head.status==true){
			indexdata = data;
			$.each(data.body.item_list, function(i,item) {
				item.have_shelves_qty="--"
	    		item.return_qty=(item.return_qty == undefined || item.return_qty == null|| item.return_qty == "") ? "添加" : item.return_qty;
			    var arges=[];
				$.each(item.erp_batch_list,function(j,jtem){
					arges.push(jtem.erp_batch);
				})
				erplist[i]=arges 
	    	});
		$.each(indexdata.body.item_list,function(i,item){
			 if(item.erp_batch!=null&&item.erp_batch!==""){
				var index=erplist[i].indexOf(item.erp_batch)
				if(index!=-1){
					item.iserp=true;
				 }else{
				 	item.iserp=false;
				 	item.erp_batch="";
				 }
	    	}else{
	    		item.iserp=false
	    	}
		})
		databindInit();
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	 }else{
	 	layer.close(indexloading);
	 }
	}, function(e) {
		layer.close(indexloading);
	});
}
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
            indexdata.body.item_list.splice(index,1)
        }
        databindTable();
        layer.close(parseInt($(".layui-layer-shade").attr("times")));
    }, function() {
    });
}
function getCargo(i) {
	//MATNR 物料编码    //ARKTX 物料描述    //MSEHL 计量单位    //zmeng 需求数量    //WERKS 工厂    //LGORT 库存地点
	var item = indexdata.body.item_list[i];
	var position = [];
	if(item.position_list == null || item.position_list == undefined || item.position_list.length == 0) {
		position=[];
	} else {
		position = item.position_list;
	}
	var data = {
		"mat_id":item.mat_id,
		"fty_id":item.fty_id,
		"mat_code":item.mat_code,
		"mat_name":item.mat_name,
		"erp_remark":item.erp_remark,
		"erp_batch":item.erp_batch,
		"return_qty":item.return_qty,
		"unit_name":item.unit_name,
		"location_id":item.location_id,
		"delivery_qty":item.can_return_qty,
		"position_list": position
	};
	return data;
}
//反选数据
function menginfo() {
	return menges;
}
function setCargo(items, index) {
	console.log(items);
	var position_list=[];
	var cksl = 0.0;
	if(menges[index] != undefined && menges[index].length > 0) {
		$.each(menges[index], function(i, item) {
			if(item.indexes == index) {
				menges.splice(index, 1);
			}
		})
	}
	var arrs = [];
	$.each(items.body, function(i, item) {
		item.indexes=index;
		arrs.push(item);
		if(item.qty>0){
			cksl +=item.qty * 1000;
		}	
     if(item.qty>0){
		position_list.push({
			"qty":item.qty,
			"package_type_id":item.package_type_id,
			"product_batch":item.product_batch,
			"quality_batch":item.product_batch,
			"output_batch":item.output_batch,
			"erp_batch":item.erp_batch,
			"output_qty":item.output_qty
			})			
		}
	});
	menges[index] = arrs
	indexdata.body.item_list[index].return_qty = cksl / 1000;
	indexdata.body.item_list[index].position_list = position_list;
	databindTable();

}
//装置联动
function getdevice(){
	var index=parseInt($(".product_line option:selected").attr("data-index"));
	$.each(basedatas.body.product_line_list[index].installations,function(i,item){
			$(".devices").append(returnOption(item.installation_id,item.installation_name))
	})
}
//冲销
function writeOff(value){
    showloading();
	var url = uriapi + "/biz/return/sale/writeoff?return_id="+GetQueryString("id");
	ajax(url, "GET", null, function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.return_id;
			}, 3000);	
		}else{
			layer.close(indexloading);
		}	
	}, function(e) {
		layer.close(indexloading);
	},true);
}
//提交
function submission(){
//	layer.alert("配货数量不能大于未出库数量<br/>配货总数量：（" + formartDecimal_f(countnum / 1000) + "）<br/>未出库数量：（" + (pardata.stock_qty) + "）");
	var count=0,obj={},isChecked=true;
	obj.item_list=[];
	if(indexdata==null||indexdata.body.item_list.length==0){
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
	}else{
		$.each(indexdata.body.item_list,function(i,item){
			if(item.return_qty=="添加"){
				layer.msg("每条数据退货数量必填");
				isChecked=false;
			}
			if(item.return_qty>0){
				obj.item_list.push(item);
			}
		})
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			if(item.return_qty==""){
				layer.msg("请填写退货数量")
				isChecked=false;
			}
			if(item.delivery_qty!=item.return_qty){
				layer.msg("当前退货数量必须与交货数量一致");
				isChecked=false;
			}
    })
}
	obj.delivery_code=$(".deveryno").html();
	obj.class_type_id=$(".shiftinfo").val();
	obj.installation_id=$(".devices").val();
	obj.product_line_id=$(".product_line").val();
	obj.syn_type=$(".tbtype").val();
	obj.remark=remarkinfo;
	obj.return_id="0";
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/return/sale/save";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
		 closeTipCancel();
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.return_id;
			}, 3000);	
		}else{
		layer.close(indexloading);
		}	
	}, function(e) {
		layer.close(indexloading);
	},true);
}
//过账
function submit(){
    showloading();
	var url = uriapi + "/biz/return/sale/post?return_id="+GetQueryString("id");
	showloading();
	ajax(url, "GET", null, function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.return_id;
			}, 3000);	
		}else{
			layer.close(indexloading);
		}	
	}, function(e) {
		layer.close(indexloading);
	},true);
}
$(function(){
    databindTable();
    clicksubmit()
    if(GetQueryString("id")==null){
    	closeTip();
        $(".against").hide();
        $(".btnsubmit").hide();
        $(".delet").show();
        $(".h2red").html("创建");
       $(".delet").click(function(){
        	delTabel();
        })
       shiftinfo();
    }else{
        init(GetQueryString("id"))
    }
    $("input.yldh").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            refer_receipt();
        }
    });
    $(".product_line").change(function(){
    	getdevice();
    })
    $("a.btn-search").click(function () {
        refer_receipt();
    });
    tableedit($("body"))
})