var indexdata=null,selectdata=null,g1=null,obj=null
function tishowinfo() {
	var indextips = -1;
	$(".tiinfo").unbind("hover");
	$(".tiinfo").hover(function() {
		if(indextips == -1) {
			indextips = layer.tips("1、支持多物料查询，用' , '分隔：60000001,60000002,60000003<br />2、支持物料号段查询，用' - '连接：60000001-60000010<br />3、支持以上俩种一起使用：60000001-60000010,60000030-60000050,60000070-60000090", $(".tiinfo"), {
				tips: [2, '#f7b824'],
				tipsMore: true,
				time: 5000,
				end: function(index) {
					indextips = -1
				}
			});
		}
	})
}
//加载下拉
function init(){
    var url=uriapi+"/conf/warring/to_coming/get_fty_location_for_user"
    showloading()
    ajax(url,"get",null,function(data){
        layer.close(indexloading)
        selectdata=data.body
        dataBindSelect()
    }, function(e) {
		layer.close(indexloading);
	})
}

//绑定下拉数据
function dataBindSelect(){
    $("#fty").append("<option value='' data-index=''>请选择</option>")
    $.each(selectdata,function(i,item){
        $("#fty").append("<option value='"+item.fty_id+"' data-index='"+i+"'>"+item.fty_name+"</option>")
    })
    loc()
}
//获取仓库号下拉数据
function getwhlist(){
 var url = uriapi + "/conf/warring/get_warehouse_list";
	ajax(url, 'GET', null, function(data) {
		kcdddata = data;
		$.each(kcdddata.body, function(i, item) {
				$(".selectwh").append("<option value='" + item.wh_id + "'data-index='" + i + "'>" +item.wh_name + "</option>");
		});
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
//加载库存地点下拉
function loc(){
    $("#location option").remove()
    $("#location").append("<option value=''>请选择</option>")
    var index=$("#fty option:selected").attr("data-index")
    if(index==""||index==undefined) return false
    $.each(selectdata[index].location_ary,function(i,item){
        $("#location").append("<option value='"+item.loc_id+"'>"+item.loc_code+"-"+item.loc_name+"</option>")
    })
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

//搜索
function search(pageindex){
    var is_checked=true
    var day_start=$("#day_start").val()
    var day_end=$("#day_end").val()
    var reg=/^\d{1,3}$/
    if(!reg.test(day_start) && day_start!=""){
        layersMoretips("请填写三位整数",$("#day_start"))
        is_checked=false
    }else if(!reg.test(day_end) && day_end!=""){
        layersMoretips("请填写三位整数",$("#day_end"))
        is_checked=false
    }
	if($("#day").val()!=""&&$("#day_to").val()!=""){
		if($("#day").val()>$("#day_to").val()){
			layersMoretips("库龄开始天数不能大于结束天数",$("#day_to"));
		}
	}

    if(is_checked==false){
        return false
    }
    var totalcount=pageindex?indexdata.head.total:-1
	
    obj={
		mat_code:$(".matnr").val(),//物料编码
		mat_name:$(".maktx").val(),//物料描述
		position_code:$(".position_code").val(),//仓位
		area_id:$(".areainfo").val(),//存储区
		fty_id:$(".selectwerks").val(),//工厂
		location_id:$(".selectlgort").val(),//库存地点
		stock_type_id:$(".invent_state").val(),//库存类型
        day_start:day_start==""?"":parseInt(day_start),//临期天数
        day_end:day_end==""?"":parseInt(day_end),//临期天数
        warranty_date:$(".radio input:checked").val(),//全部库存
        wh_id:$(".selectwh").val(),//仓库号
//		sort_column: strorder,
//		sort_ascend: strsort,
        page_index: pageindex||1,
        page_size: $.cookie("pageSize" + GetQueryString("key")) || 20,
        total: totalcount,
    }
    var url=uriapi+"/conf/warring/select_warranty_date_onpaging"
    showloading()
	console.log(JSON.stringify(obj));
    ajax(url,"post",JSON.stringify(obj),function(data){
        layer.close(indexloading)
        indexdata=data
        if (indexdata.body.length>0) {
            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
        }else{
            $("#J_ListPagination").html("")
        }
        dataBindTable();
        setTimeout(function () { layer.close(indexloading); }, 50);
    })
}

//绑定表格
function dataBindTable(){
	var warranty_date= $(".radio input:checked").val();
	if(indexdata!=null){
		$.each(indexdata.body,function(i,item){
			item.sign=0;
			if(item.warranty_date<item.reminding_day_one||warranty_date==1){
				item.sign=1;
			}
			else if((item.warranty_date>=item.reminding_day_one&&item.warranty_date<=item.reminding_day_two)||warranty_date==2){
				item.sign=2;
			}
		});
	}
    g1=$("#id_div_grid").iGrid({
        columns: [
            { th: '物料编码', col: 'mat_code', min: 60},
			{ th: '物料描述', col: 'mat_name'},
			{ th: '工厂', col: 'fty_name', min: 60 },
			{ th: '库存地点', col: 'location_name'},
			{ th: '存储区', col: 'area_code'}, 
			{ th: '仓位', col: 'position_code'}, 
			{ th: '仓位库存数量', col: 'stock_qty', type:"number",}, 
			{ th: '计量单位', col: 'unit_name', min: 60},
			{ th: '公斤数量', col: 'qty', type:"number"},
			{ th: '库存类型', col: 'stock_type_name' },
			{ th: '临期天数', col: 'warranty_date',type:"light",light:{
				coldisplay:"sign",
				type:[{value:0,class:""},{value:1,class:"redbg"},{value:2,class:"yellowbg"}]
			} },
			{ th: '包装类型', col: 'package_type_code' },
			{ th: '入库时间', col: 'create_time' },
			{ th: '生产批次', col: 'product_batch' }
        ],
        data: null,
        skin:"tablestyle4",
        absolutelyWidth:true,
    });
	if(indexdata!=null){
		g1.showdata(indexdata.body);
	}
}
function getareainfo(){
	$(".areainfo option:gt(0)").remove();
	if($(".selectwh").val()!=""){
		var url = uriapi + "/biz/stockquery/get_warehouse_area?wh_id="+$(".selectwh").val();
		ajax(url, 'GET', null, function(data) {
			$.each(data.body, function(i, item) {
				$(".areainfo").append("<option value='" + item.area_id + "'data-index='" + i + "'>" +item.area_name + "</option>");
			});
			layer.close(indexloading);
		}, function(e) {
			layer.close(indexloading);
		});
	}
}
//获取行项目sn数据
function sninfo(index){
    return indexdata.body[index]
}

//获取行项目批次信息数据
function batchinfo(index){
    var item=indexdata.body[index];
    var arrs={
        "shelf_life":item.shelf_life,
        "purchase_order_code":item.purchase_order_code,
        "class_type_name":item.class_type_name,
        "installation_name":item.installation_name,
        "product_line_name":item.product_line_name
    }
    return arrs
}

//批量导出
function files() {
    if(obj==null){
        layer.msg("请搜索出导出")
        return false
    }
			
			
    $("[name='fty_id']").val(obj.fty_id)
    $("[name='location_id']").val(obj.location_id)
    $("[name='position_code']").val(obj.position_code)
    $("[name='area_id']").val($(".areainfo").val())
    $("[name='mat_code']").val(obj.mat_code)
    $("[name='mat_name']").val(obj.mat_name)
    $("[name='day_start']").val(obj.day_start)
    $("[name='day_end']").val(obj.day_end)
    $("[name='is_reminding']").val(obj.is_reminding)
    $("[name='wh_id']").val($(".selectwh").val());//仓库号
	$("[name='stock_type_id']").val(obj.stock_type_id)//库存类型
	$("[name='warranty_date']").val(obj.warranty_date)//全部库存

    $("#download").attr("action", uriapi + "/conf/warring/download_warranty_date");
    $("#download").submit();
}

function returnOption(value, text) {
	return "<option value='" + value + "'>" + text + "</option>";
}

function returnOption1(value, value1, text) {
	return "<option value='" + value + "' data-wh_id='" + value1 + "'>" + text + "</option>";
}

//库存状态下拉信息
function getBaseInfo() {
	showloading();
	ajax(httpApi + '/biz/stockquery/get_status_and_spec_stock', 'GET', null, function(data) {
		layer.close(indexloading);
		if(data.head.status == true) {
				$.each(data.body.stock_type,function(i,item){
					$(".invent_state").append(returnOption(item.value,item.name));
				})
		} else {
			layer.msg(data.head.msg, {
				time: 2000
			});
		}
	});
}

$(function () {
	getwhlist()
    init()
	getBaseInfo();
	tishowinfo();
    dataBindTable()
    $(".btn_search").click(function () {
        search()
    })
    $("#fty").change(function(){
		if($("#fty").val()==""){
			$(".selectwh").removeAttr("disabled");
		}else{
			$(".selectwh").attr("disabled","disabled");
		}
		loc();
	});
	$(".selectwh").change(function(){
		if($(".selectwh").val()==""){
			$("#fty").removeAttr("disabled");
			$("#location").removeAttr("disabled");
		}else{
			$("#fty").attr("disabled","disabled");
			$("#location").attr("disabled","disabled");
		}
		getareainfo();
	})
    $(".btn_files").click(function(){
        files()
    })
});