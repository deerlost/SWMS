var indexdata = [], pagecount = 10,s_date="",e_date="", strorder="", kw = "", strsort=true,pldata=[];

function dataBind() {
	$("#id_div_grid").iGrid({
		columns: [
			{ th: '包装物类型', col: 'package_type_code'},
			{ th: '包装物类型描述', col: 'package_type_name' },
			{ th: '计量单位', col: 'name_zh' },
			{ th: '供应商编码', col: 'supplier_code' },
			{ th: '供应商描述', col: 'supplier_name', min: 60},
			{ th: '是否冻结', col: 'is_freeze_name', min: 60},
			{ th: '供应日期(起)', col: 'supplier_time_star' },
			{ th: '供应日期(止)', col: 'supplier_time_end', min: 60},
			{ th: '操作', col: 'package_type_id', class:"", sort:false, min:60,align: 'left',type:"popup",
			popup:{
					display:"修改",
					href:"edit.html?action=edit",//默认带index= 数据row下标。
					area:"700px,400px",
					title:"修改",
					args:[]//还需要什么参数，都写在这里，如例子：合同编号、采购订单号
				}
			},
		], 
		data: indexdata.body,
		checkbox: true,
		rownumbers:false,
		absolutelyWidth:true,
		selectedrow:{
			args:["dic_supplier_package_type_id"],
			isHasindex:true
		},
		percent:50,//空占位百分比
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
		GetSelectedData(a){
		console.log(a);
			pldata=a;
			}
	   });
}
// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
        callback: function(event, page) {
            actionpager(page);
        }
    });
}
function frozen(){
	var obj={};
	var packagelist=[];
	if(pldata.length==0){
		layer.msg("请选择要冻结的数据");
		return false;
	}else{
		$.each(pldata,function(i,item){
			packagelist.push(item.dic_supplier_package_type_id)
		})
	}
	obj.dic_supplier_package_type_id=packagelist.join(",")
	showloading();
    var url = uriapi + '/conf/supplier_package/update_freeze_by_sup_type_ids';
 	ajax(url, 'POST', JSON.stringify(obj), function(data) {
	  setTimeout(function() {
		layer.close(indexloading);}, 50);
		 if(data.head.status == true) {
				actionpager();
		}
	}, function(e) {
		layer.close(indexloading);
	});   
}
function clicksubmit(){
    $(".froen").click(function () {
        layer.confirm("是否冻结选中数据？", {
            btn: ['冻结', '取消'],
            icon: 3
        }, function () {
            frozen();
            layer.close(parseInt($(".layui-layer-shade").attr("times")));
        }, function () {
        });
    });
}
function getinfo(index){
	return indexdata.body[index];
}
function actionpager(pageindex) {
 var totalcount=-1
	if(pageindex==0||pageindex==undefined){
		totalcount=-1
	}else{
		totalcount=indexdata.head.total
	}
    showloading();
    var url = uriapi + '/conf/supplier_package/get_supplier_package_type_list';
    var obj={
        page_index: pageindex||1,
        page_size: $.cookie("pageSize"+GetQueryString("key"))||20,
        total: totalcount,
        condition:kw,
        sort_column:strorder,
		sort_ascend:strsort
    }
    ajax(url, 'post', JSON.stringify(obj), function (data) {
    	$.each(data.body,function(i,item){
    		if(item.is_freeze==0){
    			item.is_freeze_name="否";
    		}else{
    			item.is_freeze_name="是";
    		}
    	})
        indexdata = data;
        if (indexdata.body.length>0) {
        	$(".froen").show();
            initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
        }else{
        	$(".froen").hide();
            $("#J_ListPagination").html("");
        }
        dataBind();
        setTimeout(function () { layer.close(indexloading); }, 50);
    }, function (e) {
        layer.close(indexloading);
    });
}
function getinfo(index){
	return indexdata.body[index];
}	
$(function () {
    actionpager();
     clicksubmit();
    $("input.search").keyup(function (e) {
        var keycode = e.which;
        if (keycode == 13) {
            kw=$("input.search").val()
            actionpager(0);
        }
    });
    $("a.search").click(function () {
        kw=$("input.search").val()
        actionpager();
    });
    tableedit($("body"))
});