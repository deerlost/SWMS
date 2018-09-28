var indexdata=null,pardata=null;
function databind(){
			$("#id_div_grid").iGrid({
			columns: [
				{ th: '包装类型 ', col: 'package_type_code'},
				{ th: '包装规格', col: 'package_standard_ch'},
				{ th: 'ERP批次 ', col: 'erp_batch'},
				{ th: '生产批次', col: 'product_batch'},
				{ th: '质检批次', col: 'quality_batch'},
				{ th: '批次号', col: 'batch'},
				{ th: '包装号', col: 'package_code'},
				{ th: '生产订单号', col:'purchase_order_code'},//bdmng
				{ th: '库存数量', col:'stock_qty'},
				{ th: '保质期', col: 'shelf_life'},//enmng
				{ th: '装置', col: 'installation_name'},
				{ th: '生产线', col: 'product_line_name'},
				{ th: '班次', col: 'class_type_name'},
				{ th: '入库日期', col: 'input_date' },
			],
			data:indexdata.body,
			checkbox: false,
			absolutelyWidth:true,
			selectedrow:{
			args:["stock_output_rid"],
			isHasindex:true
			},
			skin:"tablestyle4",//皮肤
			resizehead:true,
		})
}
function getsninfo(obj){
var obj={
		mat_id:pardata.mat_id,
		mat_name:"",
		mat_code:"",
		position_id:pardata.position_id,
		product_batch:pardata.product_batch,
		erp_batch:pardata.erp_batch,
		area_id:pardata.area_id,
		fty_id:pardata.fty_id,
		wh_id:pardata.wh_id,
		location_id:pardata.location_id,
		pallet_code:"",
		pallet_id:pardata.pallet_id,
		stock_type_id:pardata.stock_type_id,
		input_date_begin:pardata.date_begin,
		input_date_end:pardata.data_end,
	}
	console.log(obj)
	var url = uriapi + "/biz/stockquery/serach_stock_position";
	showloading();
	ajax(url, 'post', JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		if(data.head.status==true){
			indexdata = data;
	 	}
		databind();	
	}, function(e) {
		layer.close(indexloading);
	});
}
$(function(){
	var index = GetQueryString("index");
	pardata=parent.sninfo(index)
	getsninfo();
})
