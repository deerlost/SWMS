var indexdata=null,g1=null,cjinfo=[],remarkinfo="",menges=[],arrys=[],indexinfo=false;

function init(number){
   var url=uriapi+"/biz/output/urgent/get_order_view?stock_output_id="+number
    showloading();
    ajax(url,"GET",null,function(data){
        layer.close(indexloading);
        $.each(data.body.item_list, function(i,item) {
    		item.output_qty=(item.output_qty == undefined || item.output_qty == null|| item.output_qty == "") ? "输入" : item.output_qty;
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
//绑定基础数据
function databindInit(){
	CApp.initBase("zmkpf",indexdata.body);
    if(GetQueryString("id")==null){
    	$(".h2red").html("创建");
    	$(".btnsubmit").hide();
        $(".btntreasury").hide();
        $(".submission").show();
    }else{
    	$(".hurrytype").remove();
    	if(indexdata.body.is_urgent==false){
    		$(".kctype").append("<span>正常库存</span>")
    	}else{
    		$(".kctype").append("<span>临时库存</span>")
    	}
    	$(".vehicleinfo").remove();
    	$(".driverinfo").remove();
    	$(".applicant").remove();
    	$(".vehicle").append("<span class='vehicleinfo'>"+indexdata.body.delivery_vehicle_name+"</span>")
    	$(".driver").append("<span class='driverinfo'>"+indexdata.body.delivery_driver_name+"</span>")
    	$(".recename").append("<span class='applicant'>"+indexdata.body.receive_name+"</span>")
    	$(".shiftinfo").remove();
		$(".shift").append("<span>"+indexdata.body.class_type_name+"</span>");
      	$(".tbtype").val(indexdata.body.syn_type.toString());
    	$(".create_user").html(indexdata.body.create_user);
		$(".create_time").html(indexdata.body.create_time);
    	$(".issue_ftyinfo").remove();
    	$(".issue_fty").append("<span class='issue_ftyinfo'>"+indexdata.body.fty_code+"-"+indexdata.body.fty_name+"</span>");
    	$(".jhdh").remove();
	 if(indexdata.body.status==2){//已提交
	 	$(".btnoperate").show();
    	$(".btnsubmit").hide();
    	$(".submission").hide();
		$(".btntreasury").hide()
    }else if(indexdata.body.status==3){//已作业
    	$(".btnoperate").show();
    	$(".btnsubmit").hide();
        $(".btntreasury").show();
        $(".submission").remove();
    }else if(indexdata.body.status==6){  //已拣货
        $(".btnsubmit").show();
        $(".btntreasury").remove();
        $(".submission").remove();
    }else if(indexdata.body.status==10){  //已完成
    	if(indexdata.body.mes_doc_code==""){
		    $(".synmes").show();
		}
    	$(".doc_code").show();
        $(".btnsubmit").remove();
        $(".btntreasury").remove();
        $(".submission").remove();
    }else if(indexdata.body.status==8){
    	$(".doc_code").show();
        $(".btnsubmit").remove();
        $(".btntreasury").remove();
        $(".submission").remove();
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
                { th: '交货单数量', col: 'order_qty', sort:false,type:'text',disabled:"isfrom",align:"right",distype:"number",
                	pattern:{regex:'^[0-9]+(.[0-9]{1,3})?$',tips:"格式不正确，请输入数字。"}
                },
               { th: '已下架数量', col: 'send_qty', sort:false,type:'popup',align:'right',
                	popup:{
                		href:false,
                		area:"600px,500px",
                		title:"已下架数量",
                		column:"distrion",
                		links:[{value:"1",href:"batch.html"},
						   	   {value:"0",href:""},
						]
                	}
                },
                { th: '出库数量', col: 'output_qty', sort:false,type:'popup',align:'right',
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
            ],
            data: null,
            checkbox:true,
            percent:30,
            resizehead:true,
            absolutelyWidth:true,
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
				indexinfo=false;
			}
			if(indexinfo==true){
				layer.confirm("是否改变"+test+"?", {
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
       	  if(item.send_qty==0){
            	item.distrion="0"
            }else if(item.send_qty>0){
            	item.distrion="1"
            }
        	if(item.erp_batch==undefined){
        		item.erp_batch=""
        	}
        	if(item.location_id==undefined){
        		item.location_id=""
        	}
        	item.istk=false;
        	item.isfrom=false;
    		if(GetQueryString("id")==null){
    			item.isedit="添加"
    			item.isviews="1";
    			item.isselect=false;
    		}else{
    			item.isedit="查看"
    			item.isviews="2";
    			item.isselect=true;
    		}
    		if(GetQueryString("id")!=null||$(".deveryno").html()!="--"){
        			item.isfrom=true;
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
        if(GetQueryString("id")!=null||$(".deveryno").html()!="--"){
        	g1.setCheckboxdata(false)
        }
        g1.showdata(indexdata.body.item_list)
    }
}
//点击事件  过帐
function clicksubmit(){
    $(".btnsubmit").click(function () {
        layer.confirm("是否过账？", {
            btn: ['过账', '取消'],
            icon: 3
        }, function (index) {
         layer.close(index);	
        if($(".deveryno").html()=="--"){
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
        	}else{
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
        	}
        }, function () {
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
    $(".submission").click(function () {
        layer.confirm("是否提交？", {
            btn: ['提交', '取消'],
            icon: 3
        }, function (index) {
           submission();
            layer.close(index);
        }, function () {
        });
    });
     $(".btntreasury").click(function () {
        layer.confirm("是否出库？", {
            btn: ['出库', '取消'],
            icon: 3
        }, function () {
           treasury();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
}
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/output/urgent/delete_order?stock_output_id=" + GetQueryString("id")
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
function lowershelves(index){
	if(indexdata.body.item_list[index].task_list.length>0){
		return indexdata.body.item_list[index].task_list
	}
}
//同步mes
function synmes(){
	var url=uriapi+"/biz/output/book/save_mes_again?stock_output_id="+GetQueryString("id")+"&business_type=202";
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
function getCargo(i) {
	//MATNR 物料编码    //ARKTX 物料描述    //MSEHL 计量单位    //zmeng 需求数量    //WERKS 工厂    //LGORT 库存地点
	var item = indexdata.body.item_list[i];
	if($(".hurrytype").val()=="1"){
		item.is_urgent=true;
	}else{
		item.is_urgent=false;
	}
	var position = [];
	if(item.position_list == null || item.position_list == undefined || item.position_list.length == 0) {
		position=[];
	} else {
		position = item.position_list;
	}
	var data = {
		"is_urgent":item.is_urgent,
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
function refer_receipt(){
	if(indexdata!=null){
		indexdata=null;
	}
	if($(".yldh").val()==""){
		layer.msg("请输入交货单号");
		return false;
	}else{
		$(".wlsinfo").show();
		$(".addmarinfo").hide();
		$(".delet").hide();
		$(".hurrytype").attr("disabled","disabled");
		$(".hurrytype").val("1");
		$(".isdjbh").hide();
		$(".applicant").remove()
		$(".recename").append("<span class='applicant' c-model='receive_name'></span>")
	}
	var erplist=[];
	$(".deveryno").html($(".yldh").val());
	showloading();
	var url = uriapi + '/biz/output/urgent/get_sales_view?refer_receipt_code='+$(".yldh").val();
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
function treasury(){
    showloading();
	var url = uriapi + "/biz/output/urgent/save_urgent_output?stock_output_id="+GetQueryString("id");
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
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/output/urgent/get_class_type_list";
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
//过账
function submit(value1,value2){
	var is_urgent=false;
	var url=uriapi;
	if($(".hurrytype").val()=="1"){
		is_urgent=true;
	}else{
		is_urgent=false;
	}
	if(value2==null){
		url=url+"/biz/output/urgent/save_urgent_order_sap?stock_output_id="+GetQueryString("id")+"&posting_date="+value1;
	}else{
		url=url+"/biz/output/urgent/save_urgent_order_sap?stock_output_id="+GetQueryString("id")+"&refer_receipt_code="+value2+"&posting_date="+value1;
	}
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
function changewl(){
	if(indexdata!=null){
	layer.confirm("改变库存类型物料将会清空<br />是否改变？", {
            btn: ['是', '取消'],
            icon: 3
        }, function (index) {
        	g1=null;
           	indexdata=null;
           	databindTable();
            layer.close(index);
        }, function () {
        });
	}
}
//获取工厂信息
function getftyinfo() {
	var url = uriapi + "/auth/get_fty_location_for_user?fty_type=2";
	ajax(url, 'GET', null, function(data) {
		ftydata=data;
		$.each(data.body, function(i, item) {
			$(".issue_ftyinfo").append("<option value='" + item.fty_id + "'data-index='" + i + "'>" + item.fty_code +"-"+item.fty_name+ "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function changefty(){
	if(indexdata!=null){
	 layer.confirm("改变工厂将会清空<br />是否改变？", {
            btn: ['是', '取消'],
            icon: 3
        }, function (index) {
        	g1=null;
           	indexdata=null;
           	databindTable();
            layer.close(index);
        }, function () {
        });
	}
}
//获取司机 车辆下拉
function getdelivery(){
	var url=uriapi+"/biz/output/urgent/get_derver_vehicle_info"
    showloading();
    ajax(url,"GET",null,function(data){
        layer.close(indexloading);
		$.each(data.body.delivery_driver_list,function(i,item){
			$(".driverinfo").append(returnOption(item.delivery_driver_id,item.delivery_driver_name));
		})
		$.each(data.body.delivery_vehicle_list,function(i,item){
			$(".vehicleinfo").append(returnOption(item.delivery_vehicle_id,item.delivery_vehicle_name));
		})
    },function(e) {
		layer.close(indexloading);
	})
}
//获取物料信息
function setmateriel(data){
	if(indexdata==null){
		indexdata={};
		indexdata.body={};
		indexdata.body.item_list=[];
		indexdata.body.remark="";
	}
	$.each(data,function(i,item){
		item.fty_id=$(".issue_ftyinfo").val();
		item.stock_output_rid=(i+1).toString();
		item.output_qty="输入";
		item.order_qty="";
		indexdata.body.item_list.push(item);
	})
	databindTable();
}
function getransfer(){
	var obj={};
	obj.fty_id=$(".issue_ftyinfo").val();
	return obj;
}
//提交
function submission(){
	var count=0,obj={},isChecked=true,aryes=[];
	obj.item_list=[];
	if(g1.checkData()==false){
		isChecked=false;
		return false;
	}
	if($(".deveryno").html()=="--"){
		if($(".applicant").val()==""){
			layersMoretips("请填写客户名称",$(".applicant"));
			$(".applicant").focus();
			isChecked=false;
		}
	}
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
				aryes.push(
					""+item.mat_id+item.erp_batch+item.location_id
				)
			if(item.order_qty==""){
				layer.msg("请填写交货数量")
			}
			if(item.order_qty!=item.output_qty){
				layer.msg("交货数量必须与出库数量一致");
				isChecked=false;
			}
    })
}else{
	layer.msg("请至少添加一条出库数据");
	isChecked=false;
}	
	if(aryes.length>1){
		aryes=aryes.sort();
		for(var i=0;i<aryes.length-1;i++){
			if(aryes[i]==aryes[i+1]){
				layer.msg("行项目存在重复信息，请确认")
				isChecked=false;
				return false;
			}
		}
	}
	if(GetQueryString("id")!=null){
		obj.stock_output_id=indexdata.body.stock_output_id;
	}
	if($(".deveryno").html()!="--"){
			obj.refer_receipt_code=indexdata.body.refer_receipt_code;
			obj.receive_code=indexdata.body.receive_code;
			obj.receive_name=indexdata.body.receive_name;
			obj.preorder_id=indexdata.body.preorder_id;
			obj.order_type=indexdata.body.order_type;
			obj.order_type_name=indexdata.body.order_type_name;	
			obj.sale_order_code=indexdata.body.sale_order_code;	
	}else{
		obj.receive_name=$(".applicant").val();
	}
	if($(".hurrytype").val()=="1"){
		obj.is_urgent=true;
	}else{
		obj.is_urgent=false;
	}
	obj.delivery_vehicle_id=$(".vehicleinfo").val();
	obj.delivery_vehicle_name=$(".vehicleinfo option:selected").html();
	obj.delivery_driver_id=$(".driverinfo").val();
	obj.delivery_driver_name=$(".driverinfo option:selected").html();
	obj.class_type_id=$(".shiftinfo").val();
	obj.syn_type=$(".tbtype").val();
	obj.remark=remarkinfo;
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/output/urgent/save_urgent_order";
	ajax(url, "POST", JSON.stringify(obj), function(data) {
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
$(function(){
    databindTable();
    clicksubmit()
    if(GetQueryString("id")!=null){
    	$(".delet").hide();
    	$(".addmarinfo").hide();
    	$(".wlsinfo").show();
        init(GetQueryString("id"))
    }else{
    	closeTip();
    	shiftinfo();
    	getftyinfo();
    	getdelivery()
    	$(".create_user").html(loginuserdata.body.user_name);
		$(".create_time").html(GetCurrData());
    	$(".addmarinfo").show();
    	$(".btntreasury").hide();
    	$(".btnsubmit").hide();
    	$('.h2red').html("新建");
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
    $(".delet").click(function(){
    	delTabel();
    })
    $(".hurrytype").change(function(){
    	changewl();
    })
    $(".issue_ftyinfo").change(function(){
    	changefty()
    })
    tableedit($("body"))
})