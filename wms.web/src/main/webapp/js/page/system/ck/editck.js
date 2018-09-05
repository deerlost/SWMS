var item_list = [];
function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '库存地点', col: 'location_code', min: 60 },
			{ th: '库存地点描述', col: 'location_name', min: 60 },
			{ th: '工厂', col: 'fty_code' },
			{ th: '工厂名称', col: 'fty_name' },
			{ th: '库存地址', col: 'location_address' },
			{ th: '操作', col: 'location_id', class: "", type: "delete", tips: "确定删除吗？"}
		], 
		data: item_list,
		rownumbers:false,
		percent:60,//空占位百分比
		skin:"tablestyle4",//皮肤
	});

}
function submit(){
	var obj={};
	var locations=[];
		if(GetQueryString('action')=="modify"){
			obj.wh_id=GetQueryString('wh_id');
		}else{
			obj.wh_id=0;
		}
		obj.wh_code=$("#lgnum").val();
		obj.wh_name=$("#lnumt").val();
		obj.address=$("#street").val();
		$.each(item_list,function(i,item){
			locations.push(item.location_id);
		})
		obj.location_id=locations
		var url = uriapi + "/conf/wh/add_or_update_warehouse";
		showloading();
		ajax(url, "POST", JSON.stringify(obj), function(data) {
		layer.close(indexloading);
		setTimeout(function() {
			location.href = "cklist.html";
		}, 3000);
	}, function(e) {
		layer.close(indexloading);
	});
}
function fty_infos(){
	return item_list
}
function appendinfo(value){
	item_list.push(value)
	dataBind();
}

$(function() {
	var action = GetQueryString('action');
	var wh_id= GetQueryString('wh_id')
	if(action=="modify"){
	ajax(httpApi + '/conf/wh/get_warehouse_by_id/'+wh_id, 'GET',null, function(data) {
			if (data.body != null) {
				$("#lgnum").val(data.body.wh_code);
				$("#lnumt").val(data.body.wh_name);
				$("#street").val(data.body.address);
				item_list=data.body.location_list;
				dataBind();
			}
		});
	}
	$(".btn_save").click(function(){
		submit()
	})
	tableedit($("body"));
});