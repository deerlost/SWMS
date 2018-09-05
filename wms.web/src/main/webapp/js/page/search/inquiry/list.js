var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="",kw="",strsort=true,pldata=[];
function dataBind() {
    $("#id_div_grid").iGrid({
        columns: [
        	{ th: '交货单号', col: 'refer_receipt_code' },
        	{ th: '交货单行项目号', col: 'refer_receipt_rid'},
        	{ th: '物料编码', col: 'mat_code' },
        	{ th: '物料名称', col: 'mat_name'},
        	{ th: '包装规格', col: 'package_standard_ch'},
        	{ th: '客户名称', col: 'receive_name'},
            { th: '单位', col: 'unit_name'},
            { th: 'ERP批次', col: 'erp_batch'},
            { th: '交货单数量', col: 'order_qty',type:"number"},
            { th: '配货数量', col: 'cargo_qty',type:"number"},
            { th: '已下架数量', col: 'task_qty',type:"number" },
            { th: '已出库数量', col: 'output_qty',type:"number"},
            { th: '未出库数量',col:'do_qty',type:"number"}
        ],
        data: indexdata.body,
       	checkbox: false,
		rownumbers:true,
        percent:30,//空占位百分比
        skin:"tablestyle4",//皮肤
        csort:function(a,b){
		if(b=="asc"){
				strsort=true;
			}else{
				strsort=false;
			}
			strorder=a;
            actionpager();
        },
        GetSelectedData:function(a){
			pldata=a;
			
        }
        
    });
}
function files() {
	$("[name='keyword']").val($(".refer_receipt_code").val());//交货单号
	$("[name='create_time_s']").val($(".txtstartdate").val());//起始时间
	$("[name='create_time_e']").val($(".txtenddate").val());//结束时间
	$("#download").attr("action", uriapi + "/biz/query/download_cargo_info");
	$("#download").submit();
}
// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
		is_number: true,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}
function showPage() {
	$.cookie("pageSize" + GetQueryString("key"), $(".showPage").val())
	actionpager()
}

function actionpager(pageindex) {
	var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
	   	showloading();
	    var url = uriapi + '/biz/query/select_cargo_info';
	    var obj={
	    	keyword:$(".refer_receipt_code").val(),
	    	create_time_s:$(".txtstartdate").val(),
	    	create_time_e:$(".txtenddate").val(),
	        page_index: pageindex||1,
	        page_size:$.cookie("pageSize"+GetQueryString("key"))||20,
	        total: totalcount,
	        sort_column:strorder,
			sort_ascend:strsort
	    }
	    ajax(url, 'post', JSON.stringify(obj), function (data) {
		        $.each(data.body,function(i,item){
		        	item.loc_name_code=item.location_code+"-"+item.location_name;
		        })
	        	indexdata = data;
	        if (indexdata.body.length>0) {
	        	$(".ticket").show();
	        	$(".print").show();
	            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
	        }else{
	            $("#J_ListPagination").html("");
	        }
	        dataBind();
	        setTimeout(function () { layer.close(indexloading); }, 50);
	    }, function (e) {
	        layer.close(indexloading);
	        layer.alert("数据请求失败");
	    });
}
$(function () {
	bindDatePicker();
	checkDateDifference('txtstartdate', 'txtenddate');
	$(".btn_search").click(function(){
		actionpager(0)
	})
    actionpager();
    $(".btn_files").click(function(){
    	files();
    })
    tableedit($("body"))
});