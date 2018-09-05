var indexdata = null,
	totalcount = -1,
	strorder="",
	ckdata=null,
	strsort=true;
function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [ 
			{ th: '仓储区', col: 'area_code', min: 60 },
			{ th: '仓储区名称', col: 'area_name', min: 60 },
			{ th: '仓库号', col: 'wh_code' },
			{ th: '仓库描述', col: 'wh_name' },
			{ th: '入库策略', col: 'type_input_name',sort:false },
			{ th: '出库策略', col: 'type_output_name',sort:false },
			{ th: '是否允许混储', col: 'mix_name', sort:false },
			{ th: '能力检查方', col: 'check_method_name',sort:false },
			{ th: '操作', col: 'area_id', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"editccq.html?action=modify",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:['area_id','area_code']//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		rownumbers:false,
		percent:60,//空占位百分比
		skin:"tablestyle4",//皮肤
		csort:function(a,b){
			if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
			search();
		}
	});

}
function search(page) {
	var haspage = page || -1;
	showloading();
	var url = uriapi + '/conf/area/warehouse_area_list';
	var jsondata = {
		condition: $("input.search").val(),
		page_index: page || 1,
		page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
		total: totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}
	ajax(url, 'POST', JSON.stringify(jsondata) , function(data) {
		var type_output_name="",
			type_input_name="",
			mix_name="",
			check_method_name="";
		$.each(data.body,function(i,item){
		switch (item.type_output){
                case 1:type_output_name="A-最小数量先出";break;
                case 6:type_output_name="F-先进先出";break;
                case 8:type_output_name="H-货架寿命的到期日";break;
                case 12:type_output_name="L-后进先出";break;
            }
		switch (item.type_input){
                case 2:type_input_name="B-大容量仓储";break;
                case 6:type_input_name="F-固定仓位";break;
                case 9:type_input_name="I-增加现有库存";break;
                case 12:type_input_name="L-下一个空仓位";break;
                case 16:type_input_name="P-仓储单位类型";break;
            }
		switch (item.mix){
                case 0:mix_name="允许 ";break;
                case 1:mix_name="不允许 ";break;
            }
		switch (item.check_method){
                case 1:check_method_name="W-重量 ";break;
                case 2:check_method_name="V-体积 ";break;
                case 3:check_method_name="M-混合 ";break;
            }
			item.type_output_name=type_output_name;
			item.type_input_name=type_input_name;
			item.mix_name=mix_name;
			item.check_method_name=check_method_name;
		})
		indexdata = data;
		dataBind();
		if(!page) {
			if(data.body != null) {
				if(data.body.length > 0) {
					totalcount = data.head.total;
					initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
				} else {
					$("#J_ListPagination").html("");
				}
			} else
				$("#J_ListPagination").html("");
		}
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	}, function(e) {
		layer.close(indexloading);
		layer.alert("数据请求失败");
	});
}

// 初始化分页
function initPagination(total, current) {
	$("#J_ListPagination").unbind().pagination({
		total_pages: total,
		current_page: current,
		is_number: true,
		callback: function(event, page) {
			search(page);
		}
	});
}

function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	search()
}
 function getckh() {
            ajax(uriapi + '/conf/area/warehouse_and_area', 'GET', null, function(data) {
                if (data.head.error_code == 0) {
                    ckdata = data.body;
					$.each(ckdata,function(i,item){
						$("#selectlgnum").append("<option value='"+item.wh_code+"'data-index="+i+">"+item.wh_name+"</option>")
					});
                } else {
                    layer.msg(data.head.error_string, { time: 2000 });
                }
            });
        }
$(function() {
    search();
    getckh();
    $("input.search").keyup(function(e) {
		var keycode = e.which;
		if(keycode == 13) {
			totalcount = -1
			search();
		}
	});
	$("a.search").click(function() {
		totalcount = -1
		search();
	});
    tableedit($("body"));
	$(".btnprint").click(function(){
		var dialog = $('#J_printDialog');
        layer.open({
            type: 1,
            shadeClose: false,
            shade: 0.8,
            title: dialog.data('dialog-title'),
            area: [dialog.data('dialog-width'), dialog.data('dialog-height')],
            content: dialog
        });
	});
	
	$("#selectlgnum").change(function(){
		if($("#selectlgnum").val()!=""){
			var index=parseInt($("#selectlgnum option:checked").attr("data-index"));
			$("#selectlgtyp option:gt(0)").remove();
			$.each(ckdata[index].area_list,function(i,item){
				$("#selectlgtyp").append("<option value='"+item.area_id+"|"+item.area_code+"'>"+item.area_name+"</option>")
			});
		}else{
			$("#selectlgtyp option:gt(0)").remove();
		}
	});
	
	$(".btnprintall").click(function(){
		var ischecked=true;
		if(($("#txtstart").val()==""||$("#txtend").val()=="")){
			layersMoretips("请补全打印条数。",$("#txtstart"));
			ischecked=false;
		}
		if(ischecked==false)return false;
		if(FormDataCheck.isNumber($("#txtstart").val(),0,5)==false){
			layersMoretips("数字格式不正确。",$("#txtstart"));
			ischecked=false;
		}
		if(FormDataCheck.isNumber($("#txtend").val(),0,5)==false){
			layersMoretips("数字格式不正确。",$("#txtend"));
			ischecked=false;
		}
		if(ischecked==false)return false;
		
		if(parseInt($("#txtstart").val())>parseInt($("#txtend").val())){
			layersMoretips("开始条件不能大于结束条件。",$("#txtstart"));
			ischecked=false;
		}
		if(ischecked==false)return false;
		if((parseInt($("#txtend").val())-parseInt($("#txtstart").val()))>1000){
			layersMoretips("打印条数不能大于1000。",$("#txtstart"));
			ischecked=false;
		}
		if(ischecked==false)return false;
		var datainfo=$("#selectlgtyp").val();
		var	data_area=datainfo.split("|");
		showloading();
		var jsondata={
			"area_id":data_area[0],
			"area_code":data_area[1],
			"min_position_code":$("#txtformlgpla").val(),
			"max_position_code":$("#txttolgpla").val(),
			"min_count":$("#txtstart").val(),
			"max_count":$("#txtend").val()
		}
		var url = (uriapi + '/biz/print/print_for_area_and_position');
		ajax(url, 'POST', JSON.stringify(jsondata), function (data) {
			if(data.body.url==undefined||data.body.url=="")
				layer.alert("打印失败。");
			else
				window.open(data.body.url);
			layer.close(indexloading);
		}, function (e) {
			layer.close(indexloading);
		});
	});
});