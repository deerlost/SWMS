var indexdata = null,
	totalcount = -1,
	fty_info=[],
	kw="";	
function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '交货单号', col: 'refer_receipt_code', min: 70,sort:false },
			{ th: '订单类型', col: 'order_type_name', min: 70,sort:false },
			{ th: '客户编号', col: 'receive_code',sort:false },
			{ th: '客户名称', col: 'receive_name',min: 100,sort:false },
			{ th: '操作', col: 'refer_receipt_code', class:"", min:60,align: 'left',type:"button",sort:false,
			button:{
					text:"选择",
					class:"",//默认带index= 数据row下标。
					//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		rownumbers:true,
		percent:60,//空占位百分比
		skin:"tablestyle4",//皮肤
		clickbutton(a,b,c){//Button点击后触发
//					console.log(a); //返回的字段名
//					console.log(b); //行的索引
//					console.log(c);//当前的对象
				console.log(c)
				parent.refer_receipt(c.refer_receipt_code);
				$(".btn_iframe_close_window").click();
			},
		csort:function(a,b){
		}
	});

}
function getyldah(){
	showloading();
	var obj={};
	obj.code=kw;
	var url=uriapi+"/biz/output/sale/get_cargo_order_list";
		ajax(url, 'POST',JSON.stringify(obj), function(data) {
			indexdata=data;
			dataBind();
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	}, function(e) {
		layer.close(indexloading);
		layer.alert("数据请求失败");
	});
}
function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	search()
}

$(function() {
	getyldah();
	$("input.search").keyup(function(e) {
		var keycode = e.which;
		if(keycode == 13) {
			kw=$("input.search").val()
			getyldah();
		}
	});
	$("a.search").click(function() {
		kw=$("input.search").val()
		getyldah();
	});
	tableedit($("body"));
});