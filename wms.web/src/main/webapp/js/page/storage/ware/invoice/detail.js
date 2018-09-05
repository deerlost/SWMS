var indexdata=null,g1=null,cjinfo=[],menges = [],indexes=-1,receipt_code="",remarkinfo='',indexinfo=false;

function init(number){
	var obj={};
	obj.allocate_cargo_id=number;
   var url=uriapi+"/biz/distribution/get_distrib_view"
    showloading();
    ajax(url,"POST",JSON.stringify(obj),function(data){
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
    	$(".spanno").html("新建配货单");
    	$(".h2red").html("创建");
    	$("[c-model='create_user']").html(loginuserdata.body.user_name);
    	$("[c-model='create_time']").html(GetCurrData());
    	if(indexdata.body.operation_type!=undefined){
    		$(".operatype").val(indexdata.body.operation_type);
    		if(indexdata.body.operation_type==2){
    			$(".operatype").attr("disabled","disabled");
    		}
    	}
    }else{
    	$(".operatype").remove();
    	$(".operatypeinfo").append("<span class='operatype'>"+indexdata.body.operation_type_name+"</span>");
    	$(".btnoperate").show();
    	closeTipCancel();
    	$(".jhdh").remove();
    	$(".vehicleinfo").remove();
    	$(".driverinfo").remove();
    	$(".shiftinfo").remove();
    	$(".vehicle").append("<span class='vehicleinfo'>"+indexdata.body.delivery_vehicle+"</span>")
    	$(".driver").append("<span class='driverinfo'>"+indexdata.body.delivery_driver+"</span>")
    	$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_name+"</span>")
    	$(".btnsubmit").remove();
    	$(".deveryno").html(indexdata.body.refer_receipt_code);
    	if(indexdata.body.status=="3"){//已作业
    		
	    }else if(indexdata.body.status=="10"){//已完成
	        
	    }
    	$(".spanno").html(indexdata.body.allocate_cargo_code)
    }
    databindTable()
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function getshiftinfo(){
	var url= uriapi+"/biz/distribution/get_class_type";
		ajax(url, 'GET', null, function(data) {
			$.each(data.body, function(i, item) {
					$(".shiftinfo").append(returnOption(item.class_type_id,item.class_name));
			})
}, function(e) {
		layer.msg("数据请求失败");
	});
}

//绑定表格
function databindTable(){
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
                { th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false },
                { th: '单位', col: 'name_zh', sort:false },
                { th: '交货数量', col: 'order_qty', sort:false,type:'number'},
                { th: '已配货数量', col: 'output_qtyed', sort:false,type:'number'},
                { th: '配货数量', col: 'output_qty', sort:false,type:'popup',disabled:"istk",align:"right",distype:"number",
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
                { th: 'ERP批次', col: 'erp_batch',type:'select',sort:false,width:150,data:'erp_list',value:'erp_batch',text:'erp_batch',disabled:"iserp"},
                { th: '库存地点', col: 'location_id',type:'select',sort:false,width:200,data:'loclist',value:'location_id',text:['location_code','location_name'],disabled:"isselect"},
              	{ th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"isedit",
              				args:["isstatus"],
							href: "noteview.html",
							area: "500px,400px",
							title: "备注信息",
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
           clickbutton(a,b,c){//Button点击后触发 
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
    		item.istk=false;
    		if(GetQueryString("id")==null){
    			item.isedit="添加"
    			item.isviews="1";
    			item.isstatus="1";
    			item.isselect=false;
    		}else{
    			item.isedit="查看";
    			item.isstatus="2";
    			item.isviews="2";
    			item.isselect=true;
    		}
    		if((item.order_qty-item.output_qtyed)==0&&item.output_qty==0){
    			item.istk=true;
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
        layer.confirm("是否提交？", {
            btn: ['提交', '取消'],
            icon: 3
        }, function () {
            submit(null);
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
    $(".print").click(function(){
		print()
	})
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
}
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/distribution/delete_cargo?allocate_cargo_id=" + GetQueryString("id")
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
//获取行项目备注信息
function remakinfo(value,index){
	indexdata.body.item_list[index].item_remark=value;
}
//往子页面传递行项目备注信息
function getremarkinfo(index){
	var obj={};
	if(GetQueryString("id")==null){
		obj.isedit=true;
	}else{
		obj.isedit=false;
	}
	obj.remark=indexdata.body.item_list[index].item_remark
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
	var url = uriapi + '/biz/distribution/get_sale_masseg';
	ajax(url, 'POST',JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			indexdata = data;
			$.each(indexdata.body.item_list,function(i,item){
			item.output_qty=(item.output_qty == undefined || item.output_qty == null|| item.output_qty == "") ? "输入" : item.output_qty;
			 if(item.erp_batch!=null&&item.erp_batch!==""){
    			item.iserp=true;
    		}
			var arges=[];
			$.each(item.erp_list,function(j,jtem){
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
function print(){
	var url = uriapi + "/biz/print/print_output/"+GetQueryString("id")
	showloading()
	ajax(url, 'get',null, function (data) {
		layer.close(indexloading);
		if(data.head.status){
			window.open(data.body.file_name_url)
		}
	})
}

//更换基础备注信息
function SetRemark(data){
	if(indexdata!=null){
		indexdata.body.remark=data;
	}
   remarkinfo=data;
}
function getCargo(i) {
	//MATNR 物料编码    //ARKTX 物料描述    //MSEHL 计量单位    //zmeng 需求数量    //WERKS 工厂    //LGORT 库存地点
	var item = indexdata.body.item_list[i];
	if(item.location_id==undefined||item.location_id==null||item.location_id==""){
		item.location_id="";
	}
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
		"order_qty":item.order_qty,
		"output_qtyed":item.output_qtyed,
		"name_zh":item.name_zh,
		"location_id":item.location_id,
		"output_qty":item.output_qty,
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
			"mat_id":item.mat_id,
			"batch":item.batch,
			"package_type_id":item.package_type_id,
			"location_id":item.location_id
		})
		if(item.cksl>0&&item.cksl!=""){
			cksl +=item.cksl * 1000;
		}	
     if(item.cksl>0){
		position_list.push({
			"mat_id":item.mat_id,
			"package_type_id":item.package_type_id,
			"location_id":item.location_id,
			"batch":item.batch,
			"cargo_qty":item.cksl,
			"package_type_id":item.package_type_id,
			"product_batch":item.product_batch,
			"quality_batch":item.quality_batch,
			"wh_id":item.wh_id,
			"stock_type_id":item.stock_type_id,
			"stock_qty":item.stock_qty,
			"stock_type_id":item.stock_type_id,
			"input_date":item.input_date
			})			
		}
	});
	menges[index] = arrs
	indexdata.body.item_list[index].output_qty = cksl / 1000;
	indexdata.body.item_list[index].position_list = position_list;
	databindTable();

}
//提交
function submit(){
	var count=0,obj={},isChecked=true;
	obj.item_list=[];
	$.each(indexdata.body.item_list,function(i,item){
		if(item.output_qty!="输入" && item.output_qty>0){
			count=countData.add(count,item.output_qty)
			obj.item_list.push(item)
		}
	})
	if(count==0){
		layer.msg("请至少填写一条出库数量");
		isChecked=false;
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			item.location_list=[];
			indexes += 1;
			item.allocate_cargo_rid = indexes + 1;
			$.each(item.position_list,function(j,jtem){
				jtem.allocate_cargo_rid=j+1;
				jtem.erp_batch=item.erp_batch;
			});
			if(item.output_qty>(item.order_qty-item.output_qtyed)){
				layer.msg("配货数量不能大于(交货数量-已配货数量)");
				isChecked=false;
			}
			if(item.sn_used==1){
				if(countData.mod(item.output_qty,item.package_standard)!=0){
					layer.alert("出库数量必须是包装规格的整数倍<br />出库数量:("+(item.output_qty)+")<br />包装规格:("+(item.package_standard)+")")
					isChecked=false;
					return false;
				}
			}
    	})
	}
	obj.operation_type=$(".operatype").val();
	obj.order_type_name=indexdata.body.order_type_name;
	obj.order_type=indexdata.body.order_type;
	obj.sale_order_code=indexdata.body.sale_order_code;
	obj.refer_receipt_code=indexdata.body.refer_receipt_code;
	obj.preorder_id=indexdata.body.preorder_id;
	obj.receive_name=indexdata.body.receive_name;
	obj.receive_code=indexdata.body.receive_code;
	obj.remark=remarkinfo;
	obj.delivery_vehicle=$(".vehicleinfo").val();
	obj.delivery_driver=$(".driverinfo").val();
	if(GetQueryString("id")==null){
		obj.class_type_id=$(".shiftinfo").val();	
	}else{
		obj.class_type_id=indexdata.body.class_type_id;
	}
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/distribution/submit";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			closeTipCancel();
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.allocate_cargo_id;
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
    	$(".iswriteoff").hide();
        $(".against").hide();
        $(".h2red").html("创建");
        $(".btndelete").hide();
        getshiftinfo();
    }else{
    	$(".spanno").html(GetQueryString("id"))
    	$(".resernum").empty();
    	$(".print").show();
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