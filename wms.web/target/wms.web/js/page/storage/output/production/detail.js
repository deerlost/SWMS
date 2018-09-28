var indexdata=null,g1=null,cjinfo=[],remarkinfo="",menges=[],indexinfo=false;

function init(number){
   var url=uriapi+"/biz/output/straigth/get_order_view?stock_output_id="+number
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
    }, function(e) {
		layer.close(indexloading);
	})
}
//绑定基础数据
function databindInit(){
    CApp.initBase("zmkpf",indexdata.body);
    if(GetQueryString("id")==null){
    	$(".h2red").html("创建");
    	$(".btndump").show();
    	$(".btnsubmit").hide();
    }else{
    	closeTipCancel();
    	$(".shiftinfo").remove();
		$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_type_name+"</span>");
    	$(".create_time").html(indexdata.body.create_time);
    	$(".create_user").html(indexdata.body.create_user);
    	$(".tbtype").val(indexdata.body.syn_type.toString());
    	$(".jhdh").remove();
    	$(".deveryno").html(indexdata.body.refer_receipt_code);
    	if(indexdata.body.status==0){//未完成
    	$(".btnoperate").show();
    	$(".btndump").remove();
    	$(".dumpsub").show();
        $(".btnsubmit").hide();
    }else if(indexdata.body.status==10){ //已完成
    	if(indexdata.body.mes_doc_code_transport==""){
    		$(".zsynmes").show()
    	}
    	if(indexdata.body.mes_doc_code==""){
    		$(".synmes").show()
    	}
    	$(".code_transport").show();
    	$(".doc_code").show();
    	$(".btndump").remove();
    	$(".dumpsub").remove();
        $(".btnsubmit").remove();
    }else if(indexdata.body.status==5){//转储
    	if(indexdata.body.mes_doc_code_transport==""){
    		$(".zsynmes").show()
    	}
    	$(".code_transport").show();
        $(".btnsubmit").show();
        $(".btndump").hide();
        $(".dumpsub").hide();
    }else if(indexdata.body.status==8){//已开票
    	$(".doc_code").show();
    	$(".btndump").remove();
    	$(".dumpsub").remove();
        $(".btnsubmit").remove();
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
                { th: '待直发数量', col: 'output_qty', sort:false,type:'popup',align:'right',distype:"number",
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
                { th: '库存地点', col: 'location_id',type:'select',sort:false,width:200,data:'location_list',value:'location_id',text:['location_code','location_name'],disabled:"isselect"},
              	{ th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"isedit",
							href: "noteview.html",
							area: "500px,400px",
							title: "备注信息",
							column: "bacthinfo"
						}
             },
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
           clickbutton(a,b,c){//Button点击后触发 
//			console.log(a); //返回的字段名
//			console.log(b); //行的索引
//			console.log(c);//当前的对象
			},
			cselect:function(a,b,c,d,e){
				console.log(a);//返回的字段名
				console.log(b);//列的索引
				console.log(c);//行的索引
//				console.log(d);//返回的整条数据
//				console.log(e);//返回的下拉框的整条数据
			var test="";
			if(a=="location_id"){
				test="库存地点"
			}
			if(a=="erp_batch"){
				test="ERP批次"
			}
			if(indexdata.body.item_list[c].output_qty!="输入"){
				indexinfo=true;
			}else{
				indexinfo=false
			}
			if(indexinfo==true){
				layer.confirm("是否改变"+test+"?", {
			            btn: ['确定', '取消'],
			            icon: 3
			      }, function(index) {
			      		menges.splice(c, 1);
			       		indexdata.body.item_list[c].output_qty="输入";
			       		indexdata.body.item_list[c].position_list=[];
			       		g1.showdata(indexdata.body.item_list);
			            layer.close(index);
			        }, function() {
			
			        });
				}
			}
        })
    }else{
        $.each(indexdata.body.item_list,function(i,item){
        	item.istk=false;
        	item.iserp=false;
    		if(GetQueryString("id")==null){
    			item.isedit="添加"
    			item.isviews="1";
    			item.isselect=false;
    		}else{
    			item.isedit="查看"
    			item.isviews="2";
    			item.isselect=true;
    		}
    		if(item.erp_batch!=null&&item.erp_batch!==""){
    			item.iserp=true;
    		}
        })
        g1.showdata(indexdata.body.item_list)
    }
}
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/output/straigth/get_class_type_list";
    showloading();
    ajax(url,"GET",null,function(data){
    layer.close(indexloading);
    $.each(data.body.class_type_list, function(i, item) {
		$(".shiftinfo").append(returnOption(item.class_type_id,item.class_name));
		})
    	 $(".shiftinfo").val(data.body.class_type_id);
    }, function(e) {
		layer.close(indexloading);
	})
   
}
//点击事件  过帐/转储
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
    //提交
    $(".btndump").click(function() {
        layer.confirm("是否转储？", {
            btn: ['转储', '取消'],
            icon: 3
        },function (index) {
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
     //同步出库MEs
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
      //同步转储MEs
     $(".zsynmes").click(function() {
        layer.confirm("是否同步转储MES？", {
            btn: ['同步', '取消'],
            icon: 3
        }, function() {
            zsynmes();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function() {

        });

    });
    //转储
    $(".dumpsub").click(function() {
        layer.confirm("是否转储？", {
            btn: ['转储', '取消'],
            icon: 3
        },function (index) {
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
        }, function() {

        });

    });  
}
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/output/straight/delete_order?stock_output_id=" + GetQueryString("id")
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
//同步出库mes
function synmes(){
	var url=uriapi+"/biz/output/sale/save_mes_again?stock_output_id="+GetQueryString("id")+"&business_type=202";
	showloading()
	ajax(url,"GET",null,function(data){
      if(data.head.status==true){
      	$("[c-model='mes_doc_code']").html(data.body.mes_doc_code);
      	$(".synmes").hide();
      	layer.close(indexloading);
      }else{
      	layer.close(indexloading);
      }
    },function(e) {
        layer.close(indexloading);
    },true)
	
}
//同步转储mes
function zsynmes(){
	var url=uriapi+"/biz/output/sale/save_mes_again?stock_output_id="+GetQueryString("id")+"&business_type=301";
	showloading()
	ajax(url,"GET",null,function(data){
      if(data.head.status==true){
      	$("[c-model='mes_doc_code_transport']").html(data.body.mes_doc_code_transport);
      	$(".zsynmes").hide();
      	layer.close(indexloading);
      }else{
      	layer.close(indexloading);
      }
    },function(e) {
        layer.close(indexloading);
    },true)
	
}
//提交
function submitdump(value){
	var count=0,obj={},isChecked=true;
	obj.posting_date=value
	obj.item_list=[];
	if(indexdata==null||indexdata.body.item_list.length==0){
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
	}else{
		$.each(indexdata.body.item_list,function(i,item){
			if(item.output_qty!="输入"&&item.output_qty>0){
				obj.item_list.push(item)
			}
		})
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			item.stock_output_rid = (i + 1).toString();
			if(item.order_qty!=item.output_qty){
				layer.msg("交货数量必须与待直发出库数量保持一致");
				isChecked=false;
			}
   })
}else{
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
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
	obj.class_type_id=$(".shiftinfo").val();
	obj.syn_type=$(".tbtype").val();
	obj.remark=remarkinfo;
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/output/straigth/save_straigth_order";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			closeTipCancel();
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.stock_output_id;
			}, 3000);	
		}else{
			layer.close(indexloading);
			closeTipCancel();
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
//转储
function dumpsub(value){
	showloading();
	var url = uriapi + "/biz/output/straigth/save_straigth_order?stock_output_id="+GetQueryString("id")+"&posting_date="+value;
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
		arrs.push({
			"indexes": index,
			"cksl": item.cksl,
			"numbercount":item.numbercount,
			"unique_id":item.unique_id
		})
		if(item.cksl>0&&item.cksl!=""){
			cksl +=item.cksl * 1000;
		}	
     if(item.cksl>0){
     	item.output_qty=item.cksl;
     	item.stock_output_position_id=(i+1).toString();
		position_list.push(item)			
		}
	});
	menges[index] = arrs
	indexdata.body.item_list[index].output_qty = cksl / 1000;
	indexdata.body.item_list[index].position_list = position_list;
	databindTable();

}
function refer_receipt(){
	if($(".yldh").val()==""){
		layer.msg("请输入交货单号");
		return false;
	}
	var erplist=[];
	$(".deveryno").html($(".yldh").val());
	showloading();
	var url = uriapi + '/biz/output/straigth/get_sales_view?refer_receipt_code='+$(".yldh").val();
	ajax(url, 'GET',null, function(data) {
		if(data.head.status==true){
		indexdata = data;
		$.each(indexdata.body.item_list,function(i,item){
			item.output_qty=(item.output_qty == undefined || item.output_qty == null|| item.output_qty == "") ? "输入" : item.output_qty;
			var arges=[];
			$.each(item.erpbatch_list,function(j,jtem){
				arges.push(jtem.erp_batch);
			})
			erplist[i]=arges 
      	})
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

function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
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
//出库
function submit(){
    showloading();
	var url = uriapi + "/biz/output/straigth/save_straigth_order_sap?stock_output_id="+GetQueryString("id");
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
$(function(){
    databindTable();
    clicksubmit()
    if(GetQueryString("id")==null){
    	closeTip();
		$(".btndump").show();
		$(".dumpsub").hide();
    	$(".btnsubmit").hide();
        $(".h2red").html("创建");
        $(".create_user").html(loginuserdata.body.user_name);
		$(".create_time").html(GetCurrData());
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
    tableedit($("body"))
})