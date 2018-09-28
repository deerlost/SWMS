function getExcelData(fileName,type){
    if(fileName==null){
        layer.msg("文档上传失败！")
        return false
    }
    var url=uriapi + "/conf/data/get_excel_data?file_name="+fileName+"&type="+type;
    showloading();
    ajax(url,"get",null,function(data){
        layer.close(indexloading);
        if(data.body.isSuccess=="E"){
            layer.msg(data.body.msg)
        }else if(data.body.isSuccess=="S"){
            layer.msg("成功上传"+data.body.count+"条数据")
            setTimeout(vm.methods.init(),1000)
        }
    })
}

$(function(){
    tableedit($("body"))
})