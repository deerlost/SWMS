var indexdata = null, kcdddata=null, searchdata=null,g1=null;

function dataBindBase(item) {
	//CApp.initBase("base", item);
}

function dataBindMaterial() {
	if(g1==null){
		g1=$("#id_div_grid").iGrid({
			columns: [
				{ th: '工厂', col: 'fty_name', sort:false},
				{ th: '库存地点', col: 'location_name', sort:false},
				{ th: '物料编码', col: 'mat_code', sort:false},
				{ th: '物料描述', col: 'mat_name', sort:false},
				{ th: '计量单位', col: 'unit_name', sort:false},
				{ th: 'ERP批次', col: 'erp_batch', sort:false},
				{ th: '特殊库存标识', col: 'spec_stock', sort:false},
				{ th: '特殊库存代码', col: 'spec_stock_code', sort:false},
				{ th: 'SAP库存数量	', col: 'sap_qty', sort:false,type:"number"},
				{ th: '仓储系统库存数量	', col: 'wms_qty', sort:false,type:"number"},
				{ th: '差异', col: 'num', sort:false,type:"light",
					light:{
						coldisplay:"light",
						type:[{value:1,class:"green"},{value:0,class:"red"}]
					}
				}
			],
			data: null,
			skin:"tablestyle4",
			percent:30
		})
	}else{
		$.each(searchdata.body,function(i,item){
			item.light=item.num<0?0:1
		})
		g1.showdata(searchdata.body)

	}



/*    $(".tablestyle1 tr:gt(0)").remove();
    $.each(searchdata.body, function (i, item) {           
        var strtext = "";
        strtext += ("<tr>");
		strtext += ("<td class='wordnumber'>" + formartNullString(item.werks) + "</td>");
        strtext += ("<td class='wordnumber'>" + formartNullString(item.lgort) + "</td>");//	
		strtext += ("<td class='wordnumber'>" + formartNullString(item.matnr) + "</td>");//	
		strtext += ("<td class='wordnumber'>" + formartNullString(item.maktx) + "</td>");//	
		strtext += ("<td class='wordnumber'>" + formartNullString(item.meins) + "</td>");
		strtext += ("<td class='wordnumber'>" + formartNullString(item.sobkz) + "</td>");
		strtext += ("<td class='wordnumber'>" + formartNullString(item.ztskc) + "</td>");
		strtext += ("<td class='word5 showsum1 textright' data-val='" + formartDecimal_f(item.menge) + "'>" + formartDecimal_f(item.menge) + "</td>");
		strtext += ("<td class='word5 showsum2 textright' data-val='" + item.gesme + "'>" + formartDecimal_f(item.gesme) + "</td>");
		strtext += ("<td class='word5 showsum3 textright "+(item.num<0?"red":"green")+"' data-val='" + item.num + "'>" + formartDecimal_f(item.num) + "</td>");
		strtext += ("<td class='wordnumber'><a href='voucher.html?key=132&werks="+item.werks+"&lgort="+item.lgort+"&matnr="+item.matnr+"'>获取凭证</a></td>");
        strtext += ("</tr>");
        $(".tablestyle1").append(strtext);
    });
    tableshowsum();
    dataIsNull($(".tablestyle1"));
	$(".inputdate").datetimepicker('setStartDate', GetCurrData());*/
}

function search(){
	var url = uriapi + "/biz/checkstock/get_storage_list?fty_id="+$(".selectwerks").val()+"&location_id="+$(".selectlgort").val()
	ajax(url, 'GET',null, function (data) {
		layer.close(indexloading);
		searchdata=data;
		dataBindMaterial();
	}, function (e) {
		layer.close(indexloading);
		layer.msg("数据请求失败");
	});
}

function print(){

	var url = uriapi + "/biz/print/print_erp_stock?fty_id="+$(".selectwerks").val()+"&location_id="+$(".selectlgort").val()
	ajax(url, 'GET',null, function (data) {
		layer.close(indexloading);
		if(data.head.status){
			window.open(data.body.file_name_url)
		}
	})
}

function initWerks(){
	var url = uriapi + "/auth/get_fty_location_for_user";
	ajax(url, 'GET', null, function (data) {
		kcdddata=data;
		var werksids=[];
		$.each(kcdddata.body,function(i,item){
			if(werksids.indexOf(item.fty_id)==-1){
				werksids.push(item.fty_id);
				$(".selectwerks").append("<option value='"+item.fty_id+"'>"+item.fty_name+"</option>");
			}
		});
		if(werksids.length>1){
			$(".selectwerks").prepend("<option value='' selected>请选择</option>");
			$(".selectlgort").prepend("<option value='' selected>请选择</option>");
		}else{
			bindlgort($(".selectwerks").val());
		}
		$(".selectwerks").change(function(){
			var value=$(this).val();
			if(value!="")
				bindlgort($(".selectwerks").val());
		});
		layer.close(indexloading);
        //setTimeout(function () { layer.close(indexloading); }, 50);
    }, function (e) {
        layer.close(indexloading);
        layer.msg("数据请求失败");
    });
}

function filebox(){
	var isChecked=true;
	if(!StringIsNull($(".selectwerks").val())){
		layersMoretips("必选项",$(".selectwerks"));
		isChecked= false;
	}

	if(isChecked==false){
		return false;
	}

	$("form [name='fty_id']").val($(".selectwerks").val());
	$("form [name='location_id']").val($(".selectlgort").val());
	$("#download").attr("action", uriapi + "/biz/checkstock/download_storage_list");
	$("#download").submit();

}

function bindlgort(werks){
	$(".selectlgort").html("");
	var lgortids=[]
	$.each(kcdddata.body,function(i,item){
		if(item.fty_id==werks){
			lgortids=item.location_ary
			return false
		}
	});
	$.each(lgortids,function(i,item){
		$(".selectlgort").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>");
	});
	if(lgortids.length>1){
		$(".selectlgort").prepend("<option value=''  class='sycn' selected>请选择</option>");
	}
}

$(function () {
	dataBindMaterial()
    showloading();
    initWerks();

	$(".reconciliation").click(function(){
		var isChecked=true;
		if(!StringIsNull($(".selectwerks").val())){
			layersMoretips("必选项",$(".selectwerks"));
			isChecked= false;
		}

		if(isChecked==false){
			return false;
		}else{
			showloading();
			search();
		}
	});

	$(".print").click(function(){
		var isChecked=true;
		if(!StringIsNull($(".selectwerks").val())){
			layersMoretips("必选项",$(".selectwerks"));
			isChecked= false;
		}

		if(isChecked==false){
			return false;
		}else{
			showloading();
			print();
		}
	});
});