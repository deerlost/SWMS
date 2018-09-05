var indexdata = null,
	pardata = null,
	alldata=null,
	singdata=null
	g1 = null,
	indexesinfo = [],
	args = [],
	req=/^\d+(?:\.\d{0,3})?$/,//一位小数
	req1=/^[0-9]*[1-9][0-9]*$///整数
//基础数据加载
function init() {
	var obj = {};
	obj.mat_id = pardata.mat_id;
	obj.batch = pardata.batch;
	obj.location_id = pardata.location_id;
	obj.refer_receipt_type = pardata.receipt_type
	var url = uriapi + "/biz/task/removal/stock_position";
	showloading();
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			layer.close(indexloading)
			$.each(data.body, function(i, item) {
				item.out_qty = "";
				item.number="";
			})
			alldata = data;
			if(args.length==0){
				recommendPosition();
			}else{
				$(".showall").show();
			}
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	})
	}
//绑定表格
function dataBindTable() {
	if(g1 == null) {
		g1 = $("#id_div_grid").iGrid({
			columns: [{
					th: '存储区',
					col: 'area_code',
					sort: false
				},
				{
					th: '仓位',
					col: 'position_code',
					sort: false
				},
				{
					th: '生产批次',
					col: 'production_batch',
					sort: false
				},
				{
					th: 'ERP批次',
					col: 'erp_batch',
					sort: false
				},
				{
					th: '库存数量',
					col: 'qty',
					sort: false
				},
				{
					th: '入库时间',
					col: 'input_time',
					sort: false
				},
				{
					th: '件数',
					col: 'number',
					sort: false,
					type: "text",
					disabled: "isedit"
				},
				{
					th: '下架数量',
					col: 'out_qty',
					sort: false,
					type: "text",
					disabled: "isedit",
					class:"out_qty",
					dowhile:{regex:'>rowdata[qty]',tips:"下架数量不能大于库存数量"}
				}

			],
			data: null,
			percent: 30,
			skin: "tablestyle4",
			cblur:function(a,b,c,d,e,f){//文本框失去焦点触发
//                 console.log(a);//返回的字段名
//                 console.log(b);//列的索引
//                 console.log(c);//行的索引
//                 console.log(d);//返回的文本框数据
//                 console.log(e);//返回的整条数据
//                 console.log(f);//返回的文本框
				if(a=="out_qty"){
					if(e.out_qty==""){
							e.number=e.out_qty
							g1.showdata(indexdata.body)
					}else{
						if(!req.test(e.out_qty)){
					        layersMoretips("下架数量小数位不能大于三位",f)
					        return false
						}else{
							var ares=[]
							e.number=Math.ceil(countData.div(e.out_qty,pardata.package_standard))
							g1.showdata(indexdata.body);
						}
					}
			}else if(a=="number"){
				if(e.number==""){
					e.out_qty=e.number
					g1.showdata(indexdata.body)
				}else{
					if(!req1.test(e.number)){
				        layersMoretips("件数必须为整数",f)
				        return false
					}else{
						var arse=[]
						e.out_qty=countData.mul(e.number,pardata.package_standard)
						g1.showdata(indexdata.body)
					}
				}
			}
          }
		})
	} else {
		$.each(indexdata.body, function(i, item) {
			if(GetQueryString("view") != null) {
				item.isedit = true;
			} else {
				item.isedit = false;
			}
			
		})
		g1.showdata(indexdata.body)
	}
}
//加载推荐仓位
function recommendPosition(){
        var obj={
            location_id:pardata.location_id,
            qty: pardata.unstock_task_qty,
            mat_id:pardata.mat_id,
            batch:pardata.batch,
            refer_receipt_type:pardata.receipt_type
        }
        var url=uriapi+"/biz/task/removal/advise_area_list"
        ajax(url,"post",JSON.stringify(obj),function(data){
            layer.close(indexloading);
            if(data.head.status){
            	var ares=[]
            	singdata=data
				$.each(singdata.body,function(i,item){
					$.each(alldata.body,function(j,jtem){
						if(item.position_id==jtem.position_id){
							item.erp_batch=jtem.erp_batch;
							item.id=jtem.id;
							item.out_qty="";
							item.number="";
							item.production_batch=jtem.production_batch;
							item.quality_batch=jtem.quality_batch;
							item.input_time=jtem.input_time;
						}
					})
				})
				indexdata=singdata;
				dataBindTable();
				$(".showall").show();
            }else{
                return false
            }
        })
}
function showall(){
	if(args.length==0){
		if(alldata.body.length==indexdata.body.length){
			layer.msg("当前为全部仓位")
		}else{
			$.each(alldata.body,function(i,item){
				$.each(indexdata.body,function(j,jtem){
					if(item.position_id==jtem.position_id){
						item.out_qty=jtem.out_qty;
						item.number=jtem.number;
					}
				})
			})
			indexdata=alldata;
			g1.showdata(indexdata.body)
		}
	}else{
		if(alldata.body.length==args.length){
			layer.msg("当前为全部仓位")
		}else{
			$.each(alldata.body,function(i,item){
				$.each(args,function(j,jtem){
					if(item.position_id==jtem.position_id){
						item.out_qty=jtem.out_qty;
						item.number=jtem.number;
					}
				})
			})
			indexdata=alldata;
			g1.showdata(indexdata.body)
		}
	}
}
//提交
function submit() {
	var isChecked = true;
	var count = 0;
	if(g1.checkData()==false){
		isChecked=false;
		return false;
	}
	$.each(indexdata.body,function(i,item) {
		if(item.out_qty == "") {
			return true;
		}else{
			if(!FormDataCheck.isDecimal(item.out_qty)) {
				layer.msg("下架数量必须为数字")
				isChecked = false;
			} else {
				var num = item.out_qty * 1000;
				count += num;
				item.out_qty = num / 1000;
			}
		}
		if(countData.mod(item.out_qty,pardata.package_standard)!=0){
                layersMoretips("下架数量为包装规格的整数倍,包装规格("+pardata.package_type_code+")",$(".out_qty").eq(i))
                isChecked=false
          }
	});
	if(count == 0) {
		layer.msg("请填写下数量");
		isChecked = false;
	}

	if(count > pardata.unstock_task_qty * 1000) {
		layer.msg("当前下架数量总和不能大于未下架数量")
		isChecked = false;
	}
	if(isChecked == false) {
		return false;
	}
	parent.setPosition1(indexdata,parseInt(GetQueryString("index")));
	$(".btn_iframe_close_window").click();
}

$(function() {
	dataBindTable();
	if((GetQueryString("view")== null)){
	indexesinfo = parent.menginfo();
	//当前选择第几条数据
	if(indexesinfo[GetQueryString("index")] != undefined && indexesinfo[GetQueryString("index")].length > 0) {
		args = indexesinfo[GetQueryString("index")]
	}
 }else{
 	args=[];
 }

	pardata = parent.getDataMateriel(GetQueryString("index"));
	CApp.initBase("base", pardata)
	$("[c-model='unstock_task_qty']").html(pardata.unstock_task_qty)
	if(GetQueryString("view") != null) {
		$(".btn_submit,.add_position").remove()
		$(".btn_iframe_close_window").html("关闭")
	}
	if(GetQueryString("view") == null) {
		var count=0;
		if(args.length>0){
			indexdata={};
			indexdata.body=args;
			$.each(args, function(j, jtem) {
				count=countData.add(count,jtem.out_qty)
				$("[c-model='task_qty']").html(count);
			})
			dataBindTable()
		}	
		init();
	} else {
		$("[c-model='task_qty']").html(pardata.qty)
		$.each(pardata.pallet_package_list,function(i,item){
			item.isedit=true;
			item.out_qty=item.qty;
			item.number=Math.ceil(countData.div(item.qty,pardata.package_standard))
		})
		g1.showdata(pardata.pallet_package_list);
	}
	$(".btn_submit").click(function() {
		submit()
	})
	$(".showall").click(function(){
		showall()
	})
});