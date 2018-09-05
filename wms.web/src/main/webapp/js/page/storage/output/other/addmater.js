var indexdata = null,
	pardata = null,
	cjinfo=[],
	args=[],
	g1=null;
function dataBind() {
    if(g1==null){
        g1=$("#id_div_grid").iGrid({
            columns: [
            	{ th: '物料编码', col: 'mat_code', sort:false },
                { th: '物料描述', col: 'mat_name', sort:false},
                { th: '包装类型', col: 'package_type_code', sort:false },
                { th: '库存数量', col: 'qty', sort:false },
                { th: '生产批次', col: 'product_batch', sort:false },
                { th: 'ERP批次', col: 'erp_batch', sort:false},
                { th: '质检批次', col: 'quality_batch', sort:false },
                { th: '入库时间', col: 'input_date', sort:false },
            ],
            data: null,
            checkbox: true,
            percent:30,
            skin:"tablestyle4",//皮肤
            GetSelectedData(a) {
				cjinfo = a;
				console.log(a)
			},
        })
  }else{
        g1.showdata(indexdata.body)
    }
}
function getwlinfo(){
	var obj={};
	obj.keyword1=$(".mat_info").val();
	obj.keyword2=$(".batch_info").val();
	obj.fty_id=pardata.fty_id;
	obj.is_default=pardata.is_default;
	obj.location_id=pardata.location_id;
	obj.wh_id=pardata.wh_id;
	obj.stock_type_id="1";
	var url = uriapi + "/biz/output/other/get_mat_list_position";
	showloading();
	ajax(url, 'POST', JSON.stringify(obj), function(data) {
		if(data.head.status==true){
			var indexs=[];
			if(args.length>0){
				$.each(data.body,function(i,item){
					$.each(args,function(j,jtem){
						if(item.mat_id == jtem.mat_id&&item.batch == jtem.batch&&item.package_type_id == jtem.package_type_id&&item.location_id == jtem.location_id){
							indexs.push(i);
						}
					})
				})
			}
			for(var i=indexs.length-1;i>=0;i--)
			data.body=data.body.del(indexs[i]);
			indexdata=data;
			dataBind();
		}
		layer.close(indexloading);
	}, function(e) {
		layer.close(indexloading);
	});
}
function setwlinfo(){
	if(cjinfo.length==0){
		layer.msg("请至少选择一条物料信息");
		return false;
	}else{
		parent.setmateriel(cjinfo);
		$(".btn_iframe_close_window").click();
	}
}
$(function() {
	pardata=parent.getransfer();
	args=parent.removeitems();
	dataBind();
	if(pardata.fty_id==""){
		layer.msg("请选择工厂和库存地点");
		setTimeout(function() {
              $(".btn_iframe_close_window").click();
         }, 1000);
		return false;
	}	
	$(".btnsearch").click(function(){
		getwlinfo()
	})
	$(".btnsubmit").click(function(){
		setwlinfo()
	})
});