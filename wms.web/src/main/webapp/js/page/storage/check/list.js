var indexdata = {}, pagecount = 10, pagecount = -1,totalcount=-1,g=null,strorder="", strsort=true;

function dataBind() {
	if(g==null){
		$("#id_div_grid").iGrid({
			columns: [ 
				{th: '盘点凭证号', col: 'stock_take_code', align: 'left',class:"",type:"link",
					link:{
						href:"detail.html",
						args:["stock_take_id|id"],
						autocreate:false
					} 
				} ,
				{ th: '库存地点', col: ['location_code','location_name'], min: 60 },
				{ th: '创建人', col: 'create_user_name' }, 
				{ th: '创建日期', col: 'create_time' }, 
				{ th: '状态', col: 'status_name', sort:false }//送货单状态  0 草稿 1未验收2部分验收3已完成
			], 
			data: indexdata.body,
			percent:30,//空占位百分比
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
	}else{
		console.log(indexdata.body)
		g.showdata(indexdata.body)
	}
	

}

function search(page) {
	
	var haspage=page||-1;
    showloading();
    var url = (uriapi + '/biz/stocktake/list');

	var status= [];
	$(".chkbox :checkbox:checked").each(function(){
		status.push(parseInt($(this).val()));
	});

	if(status.length==0){
		indexdata.body=[]
		$("#J_ListPagination").html("")
		databindTable();
		return false
	}

	var jsondata={
		ymbh: 1,// 1创建盘点表 2盘点结果录入
		condition:$("input.search").val(),
        status: status.join('-'),
		page_index:page||1,
		page_size:$.cookie("pageSize"+GetURLKey())||20,
		total:totalcount,
		sort_column:strorder,
		sort_ascend:strsort
	}
	
    ajax(url, 'POST', JSON.stringify(jsondata), function (data) {
		if(data.head.status==true){
			indexdata = data;
			console.log(data)
			dataBind();
			if (!page) {
				if(data.body,length>0){
					totalcount=data.head.total;
					initPagination(Math.ceil(data.head.total / data.head.page_size), data.head.page_index);
				}else
					$("#J_ListPagination").html("");

			}
			setTimeout(function () { layer.close(indexloading); }, 50);
		}else{
			layer.close(indexloading)
		}
	}, function(e) {
		layer.close(indexloading);
	});
}

// 初始化分页
function initPagination(total, current) {
    $("#J_ListPagination").unbind().pagination({
        total_pages: total,
        current_page: current,
		is_number:true,
        callback: function(event, page) {
            search(page);
        }
    });
}

function getstatusInfo() {

		dataBind();
		search();
		
		$(":checkbox").change(function () {
			search();
		});

		$("input.search").keyup(function (e) {
			var keycode = e.which;
			if (keycode == 13) {
				search();
			}
		});
		$("a.search").click(function () {
			search();
		});
}

$(function () {
	getstatusInfo();
	indexdata.body=null;
	
    
    
});