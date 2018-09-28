var indexdata=null,g1=null,cjinfo=[],remarkinfo="",indexinfo=false,menges=[];
function init(number){
   var url=uriapi+"/biz/output/book/get_order_view?stock_output_id="+number
    showloading();
    ajax(url,"GET",null,function(data){
        layer.close(indexloading);
        indexdata=data;
        $.each(indexdata.body.item_list,function(i,item){
        	 if(item.erp_batch!=null&&item.erp_batch!==""){
    			item.iserp=true;
    		}
        })
        databindInit()
    },function(e) {
		layer.close(indexloading);
	})
}
//绑定基础数据
function databindInit(){
    CApp.initBase("zmkpf",indexdata.body);
    if(GetQueryString("id")==null){
    	$(".h2red").html("创建");
    	$(".iswriteoff").hide();
    	$("[c-model='create_user']").html(loginuserdata.body.user_name);
    	$("[c-model='create_time']").html(GetCurrData());
    }else{
    	$(".stock_type").remove();
    	if(indexdata.body.stock_type_id=="1"){
    		$(".stockinfo").append("<span class='stock_type'>正常库存</span>")
    	}else if(indexdata.body.stock_type_id=="4"){
    		$(".stockinfo").append("<span class='stock_type'>记账库存</span>")
    	}
    	$(".shiftinfo").remove();
      	$(".tbtype").val(indexdata.body.syn_type);
		$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_type_name+"</span>");
    	$(".jhdh").remove();
    	$(".deveryno").html(indexdata.body.refer_receipt_code);
    	if(indexdata.body.status==0){//未完成
    	$(".btnoperate").show();	
    	$(".btnsubmsion").show();		
        $(".btnsubmit").hide();
        $(".against").hide();
        $(".treasury").hide();
    }else if(indexdata.body.status==7){//待出库
//  if(indexdata.body.mes_doc_code==""){
//		    $(".synmes").show();
//		}
    		$(".doc_code").show();
	    	$(".btnsubmsion").hide();		
	        $(".btnsubmit").hide();
	        $(".against").show();
	        $(".treasury").show();
    }else if(indexdata.body.status==10){  //已完成
//	    if(indexdata.body.mes_doc_code==""){
//		    $(".synmes").show();
//		}
    	$(".doc_code").show();
        $(".btnsubmsion").hide();
        $(".against").show();
        $(".btnsubmit").remove();
        $(".treasury").hide();
    }else if(indexdata.body.status==20){//已冲销
//		   	if(indexdata.body.mes_write_off_code==""){
//	    		$(".write_synmes").show();
//	    	}
		$(".doc_code").show();
	    $(".write_doc_code").show();
    	$(".btnsubmsion").hide();
    	$(".against").hide();
        $(".btnsubmit").remove();
        $(".treasury").hide();
    }else if(indexdata.body.status==8){//已开票
    	$(".doc_code").show();
    	$(".btnsubmsion").hide();
    	$(".against").hide();
        $(".btnsubmit").remove();
        $(".treasury").hide();
    }
    	$(".spanno").html(indexdata.body.stock_output_code)
   }
    databindTable()
}
//绑定表格
function databindTable(){
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
                { th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false },
                { th: '单位', col: 'unit_name', sort:false },
                { th: '交货数量', col: 'order_qty', sort:false,type:'number'},
                { th: '出库数量', col: 'output_qty', sort:false,type:'popup',align:'right',distype:"number",
                	popup:{
	                	href:false,//默认带index= 数据row下标。
						area:"900px,450px",
						title:"配货数量",
						column:"isviews",
						links:[{value:"2",href:"cargo.html"},
							   {value:"1",href:"setcargo.html?cargotype=1"},
							]
                	}
                },
                { th: 'ERP批次', col: 'erp_batch',type:'select',sort:false,width:150,data:'erpbatch_list',value:'erp_batch',text:'erp_batch',disabled:"iserp"},
                { th: '库存地点', col: 'location_id',type:'select',sort:false,width:200,data:'loclist',value:'location_id',text:['location_code','location_name'],disabled:"isselect"},
              	{ th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"isedit",
							href: "noteview.html",
							area: "500px,400px",
							title: "备注信息",
							column: "bacthinfo"
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
            checkbox:false,
            percent:30,
            skin:"tablestyle4",//皮肤
            GetSelectedData(a) {
		   		cjinfo = a;
			},
            loadcomplete:function(){//页面绘制完成，返回所有数据。
				
            },
           clickbutton:function(a,b,c){//Button点击后触发 
//			console.log(a); //返回的字段名
//			console.log(b); //行的索引
//			console.log(c);//当前的对象
			},
			cselect:function(a,b,c,d,e){
//				console.log(a);//返回的字段名
//				console.log(b);//列的索引
//				console.log(c);//行的索引
//				console.log(d);//返回的整条数据
//				console.log(e);//返回的下拉框的整条数据
			if(indexdata.body.item_list[c].output_qty!="输入"){
				indexinfo=true;
			}else{
				indexinfo=false;
			}
			if(indexinfo==true){
				layer.confirm("确定要改变库存地点吗", {
			            btn: ['确定', '取消'],
			            icon: 3
			      }, function(index) {
			       		indexdata.body.item_list[c].output_qty="输入";
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
        	if(item.erp_batch==undefined){
        		item.erp_batch=""
        	}
        	if(item.location_id==undefined){
        		item.location_id=""
        	}
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
            item.loclist=[];
    		$.each(item.location_list,function(j,jtem){
    			item.loclist.push({
    				"location_id":jtem.location_id,
    				"location_name":jtem.location_name,
    				"location_code":jtem.location_code
    			})
    		});
        })
        g1.showdata(indexdata.body.item_list)
    }
}
//点击事件  过帐/删除
function clicksubmit(){
    $(".btnsubmit").click(function () {
        layer.confirm("是否过账？", {
            btn: ['过账', '取消'],
            icon: 3
        }, function (index) {
 				layer.close(index);
                var title = "过账信息";
				var href = "passdata.html";
				var size = ["500px","400px"];
				layer.open({
					type: 2,
					title: title,
					shadeClose: false,
					shade: 0.8,
					area: size,
					content: href
				});
        }, function () {
        });
    });
     //未完成过账
     $(".btnsubmsion").click(function () {
        layer.confirm("是否过账？", {
            btn: ['过账', '取消'],
            icon: 3
        }, function (index) {
        	layer.close(index);
                var title = "过账信息";
				var href = "passinfo.html";
				var size = ["500px","400px"];
				layer.open({
					type: 2,
					title: title,
					shadeClose: false,
					shade: 0.8,
					area: size,
					content: href
				});
        }, function () {
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
    //同步MEs
     $(".synmes").click(function() {
        layer.confirm("是否同步出库MES？", {
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
    //出库
     $(".treasury").click(function () {
        layer.confirm("是否出库？", {
            btn: ['出库', '取消'],
            icon: 3
        }, function (index) {
            treasury();
            layer.close(index);
        }, function () {
        });
    });
    //冲销
    $(".against").click(function() {
        layer.confirm("是否冲销？", {
            btn: ['冲销', '取消'],
            icon: 3
        }, function(index) {
            writeOff();
            layer.close(index);
        }, function() {

        });

    }); 
}
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/output/book/delete_order?stock_output_id=" + GetQueryString("id")
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
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/output/book/get_class_type_list";
    showloading();
    ajax(url,"GET",null,function(data){
    layer.close(indexloading);
    $.each(data.body.class_type_list, function(i, item) {
		$(".shiftinfo").append(returnOption(item.class_type_id,item.class_name));
		})
    	 $(".shiftinfo").val(data.body.class_type_id);
    },function(e) {
		layer.close(indexloading);
	})
   
}
//同步mes
//function synmes(){
//	var url=uriapi+"/biz/output/book/save_mes_again?stock_output_id="+GetQueryString("id")+"&business_type=202";
//	showloading()
//	ajax(url,"GET",null,function(data){
//    if(data.head.status==true){
//    	if(indexdata.body.status==20){
//    		$("[c-model='mes_write_off_code']").html(data.body.mes_write_off_code);
//    		$(".write_synmes").hide();
//    	}else{
//    		$("[c-model='mes_doc_code']").html(data.body.mes_doc_code);
//    		$(".synmes").hide();
//    	}
//    	layer.close(indexloading);
//    }else{
//    	layer.close(indexloading);
//    }
//  },function(e) {
//      layer.close(indexloading);
//  },true)
//	
//}
//获取行项目已下架数量
function lowershelves(index){
	if(indexdata.body.item_list[index].task_list.length>0){
		return indexdata.body.item_list[index].task_list
	}
}
function refer_receipt(){
	if($(".yldh").val()==""){
		layer.msg("请输入交货单号");
		return false;
	}
	var erplist=[];
	$(".deveryno").html($(".yldh").val());
	showloading();
	var url = uriapi + '/biz/output/book/get_sales_view?refer_receipt_code='+$(".yldh").val();
	ajax(url, 'GET',null, function(data) {
		if(data.head.status==true){
			indexdata = data;
	 $.each(indexdata.body.item_list, function(i,item) {
    		item.output_qty=(item.output_qty == undefined || item.output_qty == null|| item.output_qty == "") ? "输入" : item.output_qty;
   			 var arges=[];
				$.each(item.erpbatch_list,function(j,jtem){
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
	 }
	setTimeout(function() {
			layer.close(indexloading);
		}, 50);	
	}, function(e) {
		layer.close(indexloading);
	});
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function stockType(){
	if(indexdata!=null&&indexdata.body.item_list.length>0){
		layer.confirm("确定要改变库存类型", {
			btn: ['确定', '取消'],
			icon: 3
		}, function(index) {
			$.each(indexdata.body.item_list,function(i,item){
					item.output_qty="输入"
					item.location_id="";
					item.position_list=[];
				})
				menges=[];
				databindTable();
				layer.close(index);
		}, function() {
				
		});
	}
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
		"stock_type_val":$(".stock_type").val(),
		"stock_type_id":$(".stock_type").val(),
		"mat_id":item.mat_id,
		"fty_id":item.fty_id,
		"mat_code":item.mat_code,
		"mat_name":item.mat_name,
		"erp_remark":item.erp_remark,
		"erp_batch":item.erp_batch,
		"output_qty":item.output_qty,
		"unit_name":item.unit_name,
		"location_id":item.location_id,
		"order_qty":item.order_qty,
		"position_list": position
	};
	return data;
}
//反选数据
function menginfo() {
	return menges;
}
function setCargo(items, index) {
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
		arrs.push({
			"indexes": index,
			"cksl": item.cksl,
			"numbercount":item.numbercount,
			"unique_id":item.unique_id
		})
		if(item.cksl>0){
			cksl +=item.cksl * 1000;
		}	
     if(item.cksl>0&&item.cksl!=""){
     	item.stock_output_position_id=(i+1).toString();
     	item.output_qty=item.cksl;
		position_list.push(item)			
		}
	});
	menges[index] = arrs
	indexdata.body.item_list[index].output_qty = cksl / 1000;
	indexdata.body.item_list[index].position_list = position_list;
	databindTable();

}
//冲销
function writeOff(value){
	showloading();
	ajax(uriapi + "/biz/output/book/write_off?stock_output_id="+GetQueryString("id"), "GET", null, function(data) {
		layer.close(indexloading);
		if(data.head.status==true){
			init(GetQueryString("id"));
			$(".doc_code").show()
		}else{
			layer.close(indexloading);
		}
	}, function(e) {
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
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
//过账
function submission(value){
	var url = uriapi + "/biz/output/book/save_book_order_sap?stock_output_id="+GetQueryString("id")+"&posting_date="+value;
	showloading();
	ajax(url, "GET", null, function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
		  		closeTipCancel();
				location.href = "detail.html?id="+data.body.stock_output_id;
			}, 3000);	
		}else{
			layer.close(indexloading);
		}
	}, function(e) {
		layer.close(indexloading);
	},true);
}
//出库
function treasury(){
	var url = uriapi + "/biz/output/book/save_book_output?stock_output_id="+GetQueryString("id");
	showloading();
	ajax(url, "GET", null, function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.stock_output_id;
			}, 3000);	
		}else{
			layer.close(indexloading);
		}
	}, function(e) {
		layer.close(indexloading);
	},true);
}
//提交
function submit(value){
	var count=0,obj={},isChecked=true;
	obj.posting_date=value
	obj.item_list=[];
	if(indexdata==null||indexdata.body.item_list.length==0){
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
	}else{
		$.each(indexdata.body.item_list,function(i,item){
			if(item.output_qty>0){
				obj.item_list.push(item)
			}
		})
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			item.stock_output_rid = (i + 1).toString();
		if(item.order_qty!=item.output_qty){
				layer.msg("交货数量必须与下架数量保持一致");
				isChecked=false;
		 }
    })
}
	if(GetQueryString("id")!=null){
		obj.stock_output_id=indexdata.body.stock_output_id;
	}
	obj.refer_receipt_code=indexdata.body.refer_receipt_code;
	obj.sale_order_code=indexdata.body.sale_order_code;
	obj.preorder_id=indexdata.body.preorder_id;
	obj.receive_code=indexdata.body.receive_code;
	obj.receive_name=indexdata.body.receive_name;
	obj.order_type=indexdata.body.order_type;
	obj.order_type_name=indexdata.body.order_type_name;
	obj.stock_type_id=$(".stock_type").val();
	obj.class_type_id=$(".shiftinfo").val();
	obj.syn_type=$(".tbtype").val();
	obj.remark=remarkinfo;
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/output/book/save_book_order_sap";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
		  		closeTipCancel();
				location.href = "detail.html?id="+data.body.stock_output_id;
			}, 3000);	
		}else{
			layer.alert(data.head.msg, {
                icon: 5,
                title: "操作失败",
                end: function(index, layero) {
                    location.href = "detail.html?id="+data.body.stock_output_id;
                    layer.close(index);
                }
            });
		}	
	}, function(e) {
		layer.close(indexloading);
	},1);

}
$(function(){
    databindTable();
    clicksubmit();
    if(GetQueryString("id")==null){
    	closeTip();
    	$(".iswriteoff").hide();
        $(".against").hide();
        $(".h2red").html("创建");
        $(".btnsubmsion").hide();
        $(".treasury").hide();
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
    $("a.btn-search").click(function () {
        refer_receipt();
    });
    $(".stock_type").change(function(){
    	stockType()
    })
    tableedit($("body"))
})