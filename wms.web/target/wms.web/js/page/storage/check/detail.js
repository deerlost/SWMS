var indexdata = {},g1=null,selectdata=[],locdata=[],seldata=null,headdata={},item_list=[],pagedata=null,jsonfty=[];

function databindTable() {
	if(g1==null){
		g1=$("#id_div_grid").iGrid({
			columns: [
				{ th: '存储区', col: 'area_code',width:85},
				{ th: '仓位', col: 'position_code', width:100},
				{ th: '物料编码', col: 'mat_code', width:100},
				{ th: '物料描述', col: 'mat_name', width:260},
				{ th: '计量单位', col: 'name_zh', width:80},
				{ th: '库存数量', col: 'stock_qty', type:'number',align:"right", width:80,class:'stock_qty'},
				{ th: '盘点数量', col: 'qty',type:'text',align:"right", width:105,disabled:'isdisabled',
				 	pattern:{regex:'^[0-9]+(.[0-9]{1,3})?$',tips:"格式不正确，请输入数字。"},
				 	decimal:{regex:3,tips:"最多三位数字。"},
				},
				{ th: '差异', col: 'difference', width:100,type:"light",align:'right',disabled:'isdifference',
				 	light:{//上一层的col是显示字段，这里的coldisplay是效果字段，和下面的 type配合使用
						coldisplay:"zyklight",
						type:[
							{value:2,class:"greenbg"},
							{value:0,class:"redbg"},
							{value:1,class:""}
						]
					}
				},
				{ th: '备注', col: 'remark',type:"popup",
				 popup:{
					 display:"remarktext",
					 href:"note.html",
					 area:"500px,400px",
					 title:"备注",
					 column:'',
					 args:["remarkvalue|view"],
					 random:true
					}
				},
			],
			data: null,
			checkbox:true,
			selectedrow:{
				args:["area_code","position_code","mat_id","makekey"],
				isHasindex:true
			},
			skin:"tablestyle4",//皮肤
			percent:50,//空占位百分比
			resizehead:true,
			sortable:false,
			GetSelectedData:function(a){
				selectdata=a;
				console.log(a);
			},
			loadcomplete:function(a){//页面绘制完成，返回所有数据。
				if($("[name='rdo_stock_take_type']:checked").val()==10){
					$(".stock_qty div").html("--")
				}
				if(indexdata.body.stock_take_type==10 && indexdata.body.status=="0"){
					$(".stock_qty div").html("--")
				}
			}

		})
	}else{
		$.each(item_list,function(i,item){
			if(indexdata!=null&&indexdata.body.status!=undefined){
				if(indexdata.body.status!=20 && indexdata.body.status!="0" && indexdata.body.stock_take_type==10){
					item.stock_qty="--"
				}


				if(item.qty==undefined)
					item.qty=0;
				if(indexdata.body.status==0){
					item.remarktext='修改';
					item.isdisabled=true;
					item.remarkvalue=0;
				}
				if(indexdata.body.status==10){
					item.remarktext='修改';
					item.remarkvalue=0;
					item.isdisabled=false;
					item.isdifference=true;
				}
				if(indexdata.body.status==20){
					item.remarktext='查看';
					item.remarkvalue=1;
					item.isdisabled=true;
					item.isdifference=false;
					item.difference=(item.qty*1000-item.stock_qty*1000)/1000;
					if(item.difference>0)
						item.zyklight=2;
					else if(item.difference<0)
						item.zyklight=0;
					else
						item.zyklight=1;
				}else
					item.difference='--';
			}
			else{
				if(item.qty==undefined)
					item.qty="--";
				item.isdisabled=true;
				item.remarkvalue=0;
				item.isdifference=true;
				item.difference='--';
				item.remarktext='添加';
			}
			
			if(item.remark==undefined)
				item.remark="";

		});
		if(indexdata!=null&&(indexdata.body.status!=undefined&&indexdata.body.status!=0)){
			g1.setCheckboxdata(false);
		}
		g1.showdata(item_list)
	}
}

function comcomplete(){
	if(GetQueryString("id")==null){
		if(locdata!=null&&seldata!=null){
			var idfty=[];
			$.each(locdata,function(i,item){
				if(idfty.indexOf(item.fty_id)==-1){
					idfty.push(item.fty_id);
					jsonfty.push(item);
				}
			});
			$(".selectfty").iSelect({
				data:jsonfty,
				value:"fty_id",
				text:"fty_name",
				//default:3,
				//disabled:true,
				change:function(data){
					console.log(data);
					headdata.fty_id=data.fty_id;
					//第二级Select的 绑定
					$(".selectloc").iSelect({
						data:locdata,
						value:"location_id",
						text:["location_code","location_name"],
						//default:9,
						//disabled:true,
						equal:{field:"fty_id",value:data.fty_id},
						change:function(data2){
							console.log(data);
							console.log(data2);
							headdata.location_id=data2.location_id;
						}
					});

				}
			});
			
			$(".selectshift").iSelect({
				data:seldata.body.work_shift_list,
				value:"class_type_id",
				text:"class_name",
				default:seldata.body.work_shift_id,
				//disabled:true,
				change:function(data){
					console.log(data);	
					headdata.work_shift=data.class_type_id;
				}
			});

			$(".selectstatus").iSelect({
				data:seldata.body.stock_take_status_list,
				value:"value",
				text:"name",
				//default:3,
				//disabled:true,
				change:function(data){
					console.log(data);	
					headdata.stock_take_status=data.value;
				}
			});
			$(".divreadonly").hide();
			$(".readonly").css({"display":"table-cell"});
			$(".readonly:last").css({"display":"inline-block"});
			$(".add_item_list,.btn_delete_mat,.btnsave,.btnsubmit").show();
			layer.close(indexloading);
		}
	}else{
		if(locdata!=null&&seldata!=null&&pagedata!=null){
			layer.close(indexloading);
			headdata=indexdata.body;
			//indexdata.body.status=10
			//headdata.status='10'
			//草稿状态下。
			if(headdata.status=='0'){
				$(".btndelete").show();

				var idfty=[];
				$.each(locdata,function(i,item){
					if(idfty.indexOf(item.fty_id)==-1){
						idfty.push(item.fty_id);
						jsonfty.push(item);
					}
				});
				$(".selectfty").iSelect({
					data:jsonfty,
					value:"fty_id",
					text:"fty_name",
					default:headdata.fty_id,
					//disabled:true,
					change:function(data){
						console.log(data);
						headdata.fty_id=data.fty_id;
						//第二级Select的 绑定
						$(".selectloc").iSelect({
							data:locdata,
							value:"location_id",
							text:"location_name",
							default:headdata.location_id,
							//disabled:true,
							equal:{field:"fty_id",value:data.fty_id},
							change:function(data2){
								console.log(data);
								console.log(data2);
								headdata.location_id=data2.location_id;
							}
						});

					}
				});

				$(".selectshift").iSelect({
					data:seldata.body.work_shift_list,
					value:"value",
					text:"name",
					default:headdata.work_shift,
					//disabled:true,
					change:function(data){
						console.log(data);	
						headdata.work_shift=data.value;
					}
				});

				$(".selectstatus").iSelect({
					data:seldata.body.stock_take_status_list,
					value:"value",
					text:"name",
					default:headdata.stock_take_status,
					//disabled:true,
					change:function(data){
						console.log(data);	
						headdata.stock_take_status=data.value;
					}
				});
				$(".txtdatepicker").val(headdata.stock_take_time);
				if(headdata.stock_take_type=='10')
					$("[name='rdo_stock_take_type']").eq(1).prop('checked','checked');
				else
					$("[name='rdo_stock_take_type']").eq(0).prop('checked','checked');

				if(headdata.stock_take_mode=='10')
					$("[name='rdo_stock_take_mode']").eq(1).prop('checked','checked');
				else
					$("[name='rdo_stock_take_mode']").eq(0).prop('checked','checked');

				if(headdata.stock_take_mode=='10')
					$(".shadedisable").eq(1).hide();
				else
					$(".shadedisable").eq(0).hide();
				
				$(".divreadonly").hide();
				$(".readonly").css({"display":"table-cell"});
				$(".readonly:last").css({"display":"inline-block"});
			}
			beadfull();
			
			$.each(headdata.item_list,function(i,item){
				item.makekey=item.area_code+"-"+item.position_code+"-"+item.mat_id;
				item_list.push(item);
			});

			databindTable();
			CApp.initBase("head",headdata);
			
			if(indexdata.body.status==0){
				$(".add_item_list,.btndelete,.btn_delete_mat,.btnsave,.btnsubmit").show();
			}
			if(indexdata.body.status==10){
				$(".btnsavecheck,.btnsubmitcheck").show();
			}
			if(indexdata.body.status==20){
				$(".btnrecheck").show();
			}
			
		}
	}
}

function submit(savestatus) {
	var isChecked = true,CheckedCount= 0,submitdata=[],required=true;

	if(item_list.length==0){
		layer.msg("没有可提交的数据。");
		return false;
	}
	
	/*if(!g1.checkData())
		isChecked=false;*/

	if (isChecked == false) return false;

	var objsubmit = headdata;
	objsubmit.status=savestatus.toString();
	
	$.each(item_list,function(i,item){
		item.stock_take_rid=(i+1).toString();
		item.stock_qty=item.stock_qty=="--"?0:item.stock_qty
	});
	objsubmit.item_list=item_list;
	
	console.log(objsubmit)

	//return false;
	showloading();
	var url = uriapi + "/biz/stocktake/save";

	ajax(url, "POST", JSON.stringify(objsubmit), function (data) {
		if(data.head.status==true){
			setTimeout(function () {
				if(data.body.stock_take_id==undefined)
					location.href = "list.html";
				else
					location.href = "detail.html?id="+data.body.stock_take_id;

			}, 1000);
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	},true);
}

function check(savestatus){
	var sumitdata={};
	sumitdata.status=savestatus;
	sumitdata.stock_take_id=headdata.stock_take_id;
	sumitdata.item_list=[];
	$.each(item_list,function(i,item){
		sumitdata.item_list.push({
			item_id:item.item_id,
			qty:item.qty,
			remark:item.remark,
		});
	});

	if(!g1.checkData()){
		return false
	}

	showloading();
	var url = uriapi + '/biz/stocktake/take';
	ajax(url, 'post', JSON.stringify(sumitdata), function (data) {
		if(data.head.status==true){
			setTimeout(function(){location.href=location.href;},1000);
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	},true);
}

function add_item_list(code){
	var url = uriapi + "/biz/stocktake/query_item_list";

	showloading();
	ajax(url, 'post', JSON.stringify(headdata), function (data) {
		if(data.head.status==true){
			//console.log(data);
			$.each(data.body,function(i,item){
				var makekey=item.area_id+"-"+item.position_id+"-"+item.mat_id+"-"+item.pallet_id+"-"+item.stock_type_id+"-"+item.location_id;
				item.makekey=makekey;
				var isHas=false;
				$.each(item_list,function(j,jitem){
					if(jitem.makekey==makekey){
						isHas=true;
						return false;
					}
				});
				if(isHas==false){
					item_list.push(item);
				}
			});
			databindTable();
			layer.close(indexloading);
			//submit(0);
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	});
}

function GetNote(i){
	if(i==null){
		if(indexdata==null)
			return "";
		else
			return indexdata.body.remark;
	}else{
		return item_list[i].remark;
	}
}

function SetNote(i,text){
	if(i==null){
		indexdata.body.remark=text;
		headdata.remark=text;
		CApp.initBase("head",headdata);
	}else{
		item_list[i].remark=text;
	}
}

//获取基础备注信息
function GetRemark(){
	return headdata.remark||""
}

//更换基础备注信息
function SetRemark(data){
	headdata.remark=data
}

function beadfull(){
	if(headdata.stock_take_time!=""&&headdata.work_shift!=""&&headdata.fty_id!=""&&headdata.location_id!=""&&headdata.stock_take_status!=""&&headdata.stock_take_type!==""&&headdata.stock_take_mode!==""){
		$(".shadedisable").hide();
	}else{
		$(".shadedisable").show();
	}
	if(item_list.length>0){
		item_list=[];
		layer.msg("数据已清空。");
		databindTable();
	}
}

$(function () {
	indexdata.body={};
	databindTable();
	showloading();
	var url = uriapi + "/auth/get_fty_location_for_user";
	
	ajax(url, 'get', null, function (data) {
		if(data.head.status==true){
			$.each(data.body,function(i,item){
				$.each(item.location_ary,function(j,itemer){
					locdata.push({
						wh_id:itemer.wh_id,
						fty_id:item.fty_id,
						fty_code:item.fty_code,
						fty_name:item.fty_name,
						location_id:itemer.loc_id,
						location_code:itemer.loc_code,
						location_name:itemer.loc_name
					})
				})
			})
			comcomplete();
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	});

	
	var url = uriapi + "/biz/stocktake/select_list";
	
	ajax(url, 'get', null, function (data) {
		if(data.head.status==true){
			seldata=data;
			comcomplete();
			//databindTable();
			//comcomplete();
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	});
	
	if(GetQueryString('id')!=null){
		$(".btn_remark").attr("data-href","../../../html/common/note.html?view=1")
		var url = uriapi + "/biz/stocktake/info/"+GetQueryString('id');
		ajax(url, 'get', null, function (data) {
			if(data.head.status==true){
				pagedata=data;
				indexdata=data;
				$(".spanno").html(indexdata.body.stock_take_code)
				comcomplete();
				console.log(data);
			}else{
				layer.close(indexloading)
			}
		}, function(e) {
			layer.close(indexloading);
		});
	}else{
		$(".btn_remark").html("添加")
		headdata={
			stock_take_id:"",
			stock_take_code:"",
			stock_take_time:"",
			work_shift:"",
			work_shift_name:"",
			fty_id:"",
			fty_name:"",
			location_id:"",
			location_name:"",
			stock_take_status:"",
			stock_take_status_name:"",
			stock_take_type:"",
			stock_take_type_name:"",
			stock_take_mode:"0",
			stock_take_mode_name:"",
			remark:"",
			create_user_name:loginuserdata.body.user_name,
			create_user:loginuserdata.body.user_id,
			create_time:GetCurrData(),
			status_name:'创建'
		};
		CApp.initBase("head",headdata);
	}
	
	$(".txtdatepicker").on('changeDate', function(ev){
		headdata.stock_take_time=$(".txtdatepicker").val();
		beadfull();
	});
	
	$("[name='rdo_stock_take_type']").change(function(){
		headdata.stock_take_type=$("[name='rdo_stock_take_type']:checked").val();
		beadfull();
	});
	
	$("[name='rdo_stock_take_mode']").change(function(){
		headdata.stock_take_mode=$("[name='rdo_stock_take_mode']:checked").val();
		beadfull();
	});
	
	$("select").change(function(){
		beadfull();
	});
	
	$("input.txt_area_code").keyup(function (e) {
		var keycode = e.which;
		if (keycode == 13) {
			$(".btnSearch").click();
		}
	});
	
	$(".btnSearch").click(function(){
		headdata.code=$(".txt_area_code").val();
		if(headdata.code==""){
			layersMoretips("必填项",$(".txt_area_code"));
			return false;
		}
		add_item_list();
	});
	
	$(".btn_delete_mat").click(function(){
		if(selectdata.length==0){
			layer.msg("没有选中的物料");
			return false;
		}
		var indexs=[];
		$.each(selectdata,function(i,item){
			indexs.push(parseInt(item.index));
		});
		for(var i=indexs.length-1;i>=0;i--){
			item_list=item_list.del(indexs[i]);
		}
		databindTable();
	});
	
	tableedit($("body"));
	bindDatePicker($("body"));

	$(".btnsave").click(function () {
		if(selectdata.length>0){
			layer.msg("存在选中的物料，只能点删除按键，或取消选中的物料，再保存。");
			return false;
		}
		layer.confirm("是否保存？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function () {
			submit(0);
			layer.close(parseInt($(".layui-layer-shade").attr("times")));
		}, function () {

		});
	});
	
	
	$(".btnsubmit").click(function () {
		if(selectdata.length>0){
			layer.msg("存在选中的物料，只能点删除按键，或取消选中的物料，再提交。");
			return false;
		}
		layer.confirm("是否提交？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function () {
			submit(10);
			layer.close(parseInt($(".layui-layer-shade").attr("times")));
		}, function () {

		});

	});
	
	$(".btndelete").click(function(){
		layer.confirm("确定删除吗？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function (index) {
			showloading();
			var url = (uriapi + '/biz/stocktake/delete/'+GetQueryString("id"));
			ajax(url, 'delete', null, function (data) {
				if(data.head.status==true){
					setTimeout(function(){location.href="list.html";},2000);
				}else{
					layer.close(indexloading)
				}
			}, function(e) {
				layer.close(indexloading);
			},true);
			layer.close(index);
		}, function () {

		});
		
	})

	$(".btnprint").click(function(){
		var jsondata=indexdata.body;
		showloading();
		var url = (uriapi + '/biz/print/print_registration');
		ajax(url, 'POST', JSON.stringify(jsondata), function (data) {
			if(data.head.status==true){
				if(data.body.url==undefined||data.body.url=="")
					layer.alert("打印失败。");
				else
					window.open(data.body.url);
				layer.close(indexloading);
			}else{
				layer.close(indexloading)
			}
		}, function(e) {
			layer.close(indexloading);
		});
	});
	
	$(".btnsavecheck").click(function () {
		layer.confirm("是否保存？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function (index) {
			check(0);
			layer.close(index);
		}, function () {

		});

	});
	
	$(".btnsubmitcheck").click(function () {
		layer.confirm("是否提交？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function (index) {
			check(1);
			layer.close(index);
		}, function () {

		});

	});
	
	$(".btnrecheck").click(function () {
		layer.confirm("确定重盘吗？", {
			btn: ['确定', '取消'],
			icon: 3
		}, function (index) {
			showloading();
			var url = (uriapi + '/biz/stocktake/reset/'+headdata.stock_take_id);
			ajax(url, 'get', null, function (data) {
				if(data.head.status==true){
					location.href=location.href;
					layer.close(indexloading);
				}else{
					layer.close(indexloading)
				}
			}, function(e) {
				layer.close(indexloading);
			});
			layer.close(index);
		}, function () {

		});

	});
	
});