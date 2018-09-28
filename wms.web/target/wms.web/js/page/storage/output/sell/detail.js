var indexdata=null,g1=null,cjinfo=[],remarkinfo="";

function init(number){
   var url=uriapi+"/biz/output/sale/get_order_view?stock_output_id="+number
    showloading();
    ajax(url,"GET",null,function(data){
        layer.close(indexloading);
        $.each(data.body.item_list,function(i,item){
        	item.loc_name_code=item.location_code+"-"+item.location_name;
        })
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
    	$(".iswriteoff").hide();
    	$("[c-model='create_user']").html(loginuserdata.body.user_name);
    	$("[c-model='create_time']").html(GetCurrData());
    }else{
    	$(".tbtype").attr("disabled","disabled")
    	closeTipCancel();
    	$(".shiftinfo").remove();
		$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_type_name+"</span>");
		$(".reservat").remove();
    	$(".deveryno").html(indexdata.body.refer_receipt_code);
    	$(".tbtype").val(indexdata.body.syn_type.toString())
    	if(indexdata.body.status==0){//未出库
    	$(".btnsubmsion").show();		
        $(".btnsubmit").hide();
        $(".btnoperate").show();
        $(".against,.btnprint").hide();
    }else if(indexdata.body.status==10){ //已出库
    	if(indexdata.body.syn_type!=1){
	    	if(indexdata.body.mes_doc_code==""){
	    		$(".synmes").show();
	    	}
    	}
    	$(".doc_code").show();
    	$(".btnprint").show()
        $(".btnsubmsion").hide();
        $(".against").show();
        $(".btnsubmit").remove();
    }else if(indexdata.body.status==20){//已冲销
    	$(".doc_code").show();
    	$(".synmes").hide();
    	 if(indexdata.body.syn_type!=1){
			 if(indexdata.body.mes_write_off_code==""){
		    	$(".write_synmes").show();
		     }
    	}
	    $(".write_doc_code,.btnprint").show();
    	$(".btnsubmsion").hide();
    	$(".against").hide();
        $(".btnsubmit").remove();
    }else if(indexdata.body.status==8){//已开票
    	$(".doc_code,.btnprint").show();
    	$(".btnsubmsion").remove();
    	$(".against").remove();
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
            	{ th: '交货单行号', col: 'refer_receipt_rid', sort:false },
                { th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false },
                { th: '单位', col: 'unit_name', sort:false },
                { th: '交货数量', col: 'order_qty', sort:false,type:'number'},
                { th: '已下架数量', col: 'output_qty', sort:false,type:'popup',align:'right',distype:"number",
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
                { th: '库存地点', col: 'loc_name_code', sort:false },
              	{ th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"查看",
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
           clickbutton(a,b,c){//Button点击后触发 
//			console.log(a); //返回的字段名
//			console.log(b); //行的索引
//			console.log(c);//当前的对象
			}
        })
    }else{
        $.each(indexdata.body.item_list,function(i,item){
            if(indexdata.body.status==20){
                item.ztrvalue=1
            }
            if(item.output_qty==0){
            	item.distrion="0"
            }else if(item.output_qty>0){
            	item.distrion="1"
            }
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
    
}
//同步mes
function synmes(){
	var url=uriapi+"/biz/output/sale/save_mes_again?stock_output_id="+GetQueryString("id")+"&business_type=202";
	showloading()
	ajax(url,"GET",null,function(data){
      if(data.head.status==true){
      	if(indexdata.body.status==20){
      		$("[c-model='mes_write_off_code']").html(data.body.mes_write_off_code);
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
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/output/sale/get_class_type_list";
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
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/output/sale/delete_order?stock_output_id=" + GetQueryString("id")
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
//获取行项目已下架数量
function lowershelves(index){
	if(indexdata.body.item_list[index].task_list.length>0){
		return indexdata.body.item_list[index].task_list
	}
}
function refer_receipt(a){
	showloading();
	var url = uriapi + '/biz/output/sale/get_sales_view?refer_receipt_code='+a;
	ajax(url, 'GET',null, function(data) {
		if(data.head.status==true){
			 $.each(data.body.item_list,function(i,item){
	        	item.loc_name_code=item.location_code+"-"+item.location_name;
	        })
		indexdata = data;
		$(".reservat").html(indexdata.body.refer_receipt_code);
		databindInit();
	 }
	setTimeout(function() {
			layer.close(indexloading);
		}, 50);	
	}, function(e) {
		layer.close(indexloading);
	});
}
//统计冲销数量
function count() {
    var str = 0;
    $.each($(".body .tablestyle4 :checkbox:checked"), function() {
        str += 1;
    });
    return str
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
//冲销
function writeOff(value){
	showloading();
	ajax(uriapi + "/biz/output/sale/write_off?stock_output_id="+GetQueryString("id"), "GET", null, function(data) {
		layer.close(indexloading);
		if(data.head.status==true){
			init(GetQueryString("id"));
			$(".doc_code").hide()
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
	var url = uriapi + "/biz/output/sale/save_sales_order_sap?stock_output_id="+GetQueryString("id")+"&posting_date="+value;
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
	obj.item_list=[];
	obj.posting_date=value;
	var outputcount=0;
	var ordercount=0;
	var ary=[];
	if(indexdata==null||indexdata.body.item_list.length==0){
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
	}else{
		obj.item_list=indexdata.body.item_list;
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			var flag=true;
			item.stock_output_rid = (i + 1).toString();
			outputcount=countData.add(outputcount,item.output_qty)
			if(ary.length>0){
				$.each(ary,function(j,jtem){
					if(jtem.refer_receipt_rid==item.refer_receipt_rid){
						flag = false
					}
				})
			}
			if(flag){
				ary.push(item)
			}
   })
	
}
	if(ary.length>0){
		$.each(ary,function(i,item){
			ordercount=countData.add(ordercount,item.order_qty)
		})
	}
	if($(".tbtype").val()!=3){
		if(outputcount!=ordercount){
			layer.msg("下架数量必须与交货数量一致")
			isChecked=false;
		}
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
	var url = uriapi + "/biz/output/sale/save_sales_order_sap";
	
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			closeTipCancel();
		  setTimeout(function() {
				location.href = "detail.html?id="+data.body.stock_output_id;
			}, 3000);	
		}else{
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

$(function(){
    databindTable();
    clicksubmit();
    if(GetQueryString("id")==null){
    	closeTip();
    	$(".iswriteoff").hide();
        $(".against,.btnprint").hide();
        $(".h2red").html("创建");
        $(".btndelete").hide();
         $(".btnsubmsion").hide();
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
	
	$(".btnprint").click(function(){
		showloading();
		var jsonlist={param:[]};
		$.each(indexdata.body.item_list,function(i,item){
			var strproduct_batch=[];
			$.each(item.position_list,function(j,jitem){
				strproduct_batch.push(jitem.product_batch);
			});
			jsonlist.param.push({
				samp_name:item.samp_name,
				specification:item.specification,
				product_batch:strproduct_batch.join()
			})
		})
		var url = uriapi + "/biz/print/print_lims";

		ajax(url, "POST", JSON.stringify(jsonlist), function(data) {
			if(data.head.status){
				window.open(data.body.file_name_url);
			}else{
				layer.msg("文件返回失败");
			}
			layer.close(indexloading);
		}, function(e) {
			layer.close(indexloading);
		},1);
	});
    tableedit($("body"))
})