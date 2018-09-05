var indexdata = null,
	pardata = null,
	g1=null;
function dataBind() {
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
            	{ th: '下架作业单', col: 'stock_task_code', sort:false,type:"link",
            		link:{
            			href:"../../ware/soldout/view.html",
            			args:["stock_task_id|id"],
            			target: "_blank"
            		}
            	},
            	{ th: '下架数量', col: 'qty', sort:false },
                { th: '单位', col: 'unit_name', sort:false},
                { th: '创建人', col: 'create_user', sort:false },
                { th: '创建日期', col: 'create_time', sort:false },
            ],
            data: null,
            percent:30,
            skin:"tablestyle4",//皮肤
        })
  }else{
        g1.showdata(indexdata)
    }
}
$(function() {
	dataBind();
	indexdata = parent.lowershelves(parseInt(GetQueryString("index")));
	if(indexdata.length > 0) {
	 	dataBind();
	}
});