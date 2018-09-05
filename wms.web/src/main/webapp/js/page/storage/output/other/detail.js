var indexdata=null,g1=null,cjinfo=[],isclose=false,menges = [],numbers=-1,indexes=-1,nextval=0,remarkinfo="",ftydata=null,is_default=-1;
function init(number){
   var url=uriapi+"/biz/output/other/get_order_view?stock_output_id="+number
    showloading();
    ajax(url,"GET",null,function(data){
        layer.close(indexloading);
        indexdata=data;
        $.each(indexdata.body.item_list,function(i,item){
        	item.numbercount=Math.ceil(countData.div(item.order_qty,item.package_standard))
        })
        databindInit()
    }, function(e) {
		layer.close(indexloading);
	},function(e) {
		layer.close(indexloading);
	})
	
}
function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}
function getftyinfo() {
	var url = uriapi + "/auth/get_fty_location_for_user";
	ajax(url, 'GET', null, function(data) {
		ftydata=data;
		$.each(data.body, function(i, item) {
			$(".issue_ftyinfo").append("<option value='" + item.fty_id + "'data-index='" + i + "'data_fty_type='"+item.fty_type+"'>" + item.fty_code +"-"+item.fty_name+"</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function getlocinfo(){
	$(".issue_locinfo option:gt(0)").remove();
	var index=parseInt($(".issue_ftyinfo option:selected").attr("data-index"));
	if(index!=-1){
		if(ftydata.body[index].fty_type==1){
			is_default=1
		}else if(ftydata.body[index].fty_type==2){
			is_default=0
		}
		 $.each(ftydata.body[index].location_ary,function(i,item){
			$(".issue_locinfo").append("<option value='" + item.loc_id +"'data-whid='" + item.wh_id+ "'>" + item.loc_code+"-"+item.loc_name + "</option>")
		 })
	}
	if(indexdata!=null){
		 g1=null;
         indexdata=null;
         databindTable();
	}
}
//班次信息
function shiftinfo(){
	var url=uriapi+"/biz/output/other/get_class_type_list";
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
//往添加物料子页面传值
function getransfer(){
	var obj={};
	obj.is_default=is_default;
	obj.fty_id=$(".issue_ftyinfo").val();
	obj.location_id=$(".issue_locinfo").val();
	obj.wh_id=$(".issue_locinfo option:selected").attr("data-whid");
	return obj
}
function removeitems(){
	menges=[];
	if(indexdata!=null&&indexdata.body.item_list.length>0){
		$.each(indexdata.body.item_list,function(i,item){
				menges.push({
				"mat_id":item.mat_id,
				"batch":item.batch,
				"package_type_id":item.package_type_id,
				"location_id":item.location_id
			})
	 })
	}
	return menges
}
//绑定基础数据
function databindInit(){
	 CApp.initBase("zmkpf",indexdata.body);
    	$(".issue_ftyinfo").remove();
    	$(".issue_locinfo").remove();
    	$(".move_type").remove();
    	$(".issue_fty").append("<span class='issue_ftyinfo'>"+indexdata.body.fty_code+"-"+indexdata.body.fty_name+"</span>");
    	$(".issue_loc").append("<span class='issue_locinfo'>"+indexdata.body.location_code+"-"+indexdata.body.location_name+"</span>")
    	$(".move_type_info").append("<span class='move_type'>"+indexdata.body.move_type_code+"-"+indexdata.body.move_type_name+"</span>")
    	$(".shiftinfo").remove();
		$(".shift").append("<span class='shiftinfo'>"+indexdata.body.class_type_name+"</span>");
	 if(indexdata.body.status==10){  //已完成
			closeTipCancel();
	        $(".h2red").html("已出库")
	        $(".submission").remove();
	        $(".btnsubmit").remove();
	    }else if(indexdata.body.status==3){//已作业
	    	closeTipCancel();
	    	$(".btnoperate").show();
	    	 $(".h2red").html("已作业")
	    	$(".submission").hide();
	    	$(".btnsubmit").show();
	    }else if(indexdata.body.status==2){//已提交
	    	closeTipCancel();
	    	$(".btnoperate").show();
	    	 $(".h2red").html("已提交")
	    	$(".submission").hide();
	    	$(".btnsubmit").hide();
	    }
	 $(".spanno").html(indexdata.body.stock_output_code)
     $(".tbtype").val(indexdata.body.syn_type.toString())
    databindTable()
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
//绑定表格
function databindTable(){
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
                { th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false },
                { th: '单位', col: 'unit_name', sort:false },
                { th: '包装类型', col: 'package_type_code', sort:false },
                { th: '生产批次', col: 'product_batch', sort:false },
                { th: 'ERP批次', col: 'erp_batch', sort:false },
                { th: '质检批次', col: 'quality_batch', sort:false },
                { th: '批次', col: 'batch', sort:false },
                {th: '库存数量', col: 'qty', sort:false,type:'number'},
                { th: '件数', col: 'numbercount',width:80, sort:false, type:"text", align:"right",disabled:"isdisabled",distype:"number",
                	pattern:{regex:/^[0-9]+\d*$/,tips:"件数必须为整数"},
                },
                { th: '出库数量', col: 'order_qty', width:100,sort:false, type:"text", align:"right",disabled:"isdisabled",class:"out_qty",distype:"number",
					pattern:{regex:/^\d+(?:\.\d{0,3})?$/,tips:"出库数量最多不能大于3位小数。"},
				},
                { th: '已下架数量', col: 'output_qty', sort:false,type:'popup',align:'right',
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
                { th: '备注', col: 'remark', sort:false,type:"popup",
              		popup:{
              				display:"查看",
							href: "noteview.html",
							area: "500px,400px",
							title: "备注信息",
							column: "bacthinfo"
						}
               }
            ],
            data: null,
            checkbox:true,
            resizehead:true,
            percent:30,
            skin:"tablestyle4",//皮肤
           clickbutton(a,b,c){//Button点击后触发 
           	layer.confirm("是否关闭？", {
	            btn: ['关闭', '取消'],
	            icon: 3
	        }, function() {
	            $(".table_button").eq(b).html("已关闭")
	            layer.close(parseInt($(".layui-layer-shade").attr("times")));
	        }, function() {
	
	        });
//			console.log(a); //返回的字段名
//			console.log(b); //行的索引
//			console.log(c);//当前的对象
			},
			cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
				  	var test1=/^\d+(?:\.\d{0,3})?$/;
				  	var test2=/^[0-9]+\d*$/;
					if(a=="order_qty"){
						if(f.val() == "") {
							e.numbercount= "";
							e.order_qty= "";
						}else{
							if(!test1.test(f.val())){
								e.numbercount= "";
								e.order_qty= "";
								return false;
							}else{
								e.cargo_qty=f.val();
								e.numbercount=Math.ceil(countData.div(f.val(),e.package_standard));
							}
						}
						g1.showdata(indexdata.body.item_list)
					}
				 if(a=="numbercount"){
					if(f.val() == "") {
						e.order_qty= "";
						e.numbercount="";
					}else{
						if(!test2.test(f.val())){
							e.numbercount= "";
							e.output_qty= "";
							return false;
						}else{
							e.numbercount=f.val();
							e.order_qty=countData.mul(f.val(),e.package_standard)
						}	
					}
					g1.showdata(indexdata.body.item_list)
				  }
				}

        })
    }else{
        var checkData=[]
        $.each(indexdata.body.item_list,function(i,item){
            if(item.output_qty==0){
            	item.distrion="0"
            }else if(item.output_qty>0){
            	item.distrion="1"
            }
            if(GetQueryString("id")==null){
            	item.isdisabled=false;
            }else{
            	item.isdisabled=true;
            }
        })
        if(GetQueryString("id")!=null){
        	g1.setCheckboxdata(false)
        }
        g1.showdata(indexdata.body.item_list)
    }
}
//获取行项目已下架数量
function lowershelves(index){
	if(indexdata.body.item_list[index].task_list.length>0){
		return indexdata.body.item_list[index].task_list
	}
}
//移动类型下拉
function getmovetype(){
	var url = uriapi + "/biz/output/other/get_move_type_list";
	ajax(url, 'GET', null, function(data) {
		$.each(data.body, function(i, item) {
			$(".move_type").append(returnOption(item.move_type_id,item.move_type_code+"-"+item.move_type_name));
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
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
        },function () {
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
//删除页面
function deletes() {
	showloading();
    var url = uriapi + "/biz/output/other/delete_order?stock_output_id=" + GetQueryString("id")
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
//获取物料信息
function setmateriel(data){
	if(indexdata==null){
		indexdata={};
		indexdata.body={};
		indexdata.body.remark="";
		indexdata.body.item_list=[];
	}
	$.each(data,function(i,item){
		item.stock_output_rid=(i+1).toString();
		item.output_qty=0;
		item.order_qty="";
		indexdata.body.item_list.push(item);
	})
	databindTable();
}
//提交
function submission(){
	var count=0,obj={},isChecked=true;
	obj.item_list=[];
	if(g1.checkData()==false){
		isChecked=false;
		return false;
	}
	if(indexdata==null||indexdata.body.item_list.length==0){
		layer.msg("请至少添加一条出库数据");
		isChecked=false;
	}else{
		if($(".move_type").val()==""){
			layersMoretips("请选择移动类型",$(".move_type"))
			isChecked=false;
		}
		$.each(indexdata.body.item_list,function(i,item){
			if(item.order_qty>0){
				obj.item_list.push(item)
			}
		})
	}
	if(obj.item_list.length>0){
		$.each(obj.item_list,function(i,item){
			item.stock_output_rid = (i + 1).toString();
			if(item.sn_used==1){
				if(countData.mod(item.order_qty,item.package_standard)!=0){
					layersMoretips("出库数量必须是包装规格的整数倍",$(".out_qty").eq(i))
					isChecked=false;
					return false;
				}
			}
			if(countData.mod(item.order_qty,item.package_standard)!=0){
                layersMoretips("出库数量为包装规格的整数倍",$(".out_qty").eq(i))
                isChecked=false
                return false;
            }
			if(item.order_qty>item.qty){
				layersMoretips("出库数量不能大于库存数量",$(".out_qty").eq(i));
				isChecked=false;
				return false;
			}
    })
}else{
	layer.msg("请至少配一条出库数据");
	isChecked=false;
}
	if(GetQueryString("id")!=null){
		obj.stock_output_id=indexdata.body.stock_output_id;
	}
	obj.move_type_id=$(".move_type").val();
	obj.class_type_id=$(".shiftinfo").val();
	obj.syn_type=$(".tbtype").val();
	obj.remark=remarkinfo;
	if(!isChecked){
		return false;
	}
    showloading();
	var url = uriapi + "/biz/output/other/save_other_order";
	showloading();
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
//过账
function submit(value){
    showloading();
	var url = uriapi + "/biz/output/other/save_other_output?stock_output_id="+GetQueryString("id")+"&posting_date="+value;
	showloading();
	ajax(url, "GET", null, function(data) {
		if(data.head.status==true){
		  setTimeout(function() {
				location.href = "detail.html?id="+GetQueryString("id");
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
    	$(".wlsinfo").hide();
        $(".h2red").html("创建");
        $(".spanno").html("新建出库单");
    	$(".addmarinfo").show();
    	$(".btnsubmit").hide();
    	$(".delet").show();
    	$(".leading").html(loginuserdata.body.user_name);
    	$(".applicant").html(GetCurrData());
        getftyinfo();
        getmovetype();
        $(".issue_ftyinfo").change(function(){
        	getlocinfo()
        })
        shiftinfo();
        $(".delet").click(function(){
        	delTabel();
        })
    }else{
        init(GetQueryString("id"))
    }
    tableedit($("body"))
})