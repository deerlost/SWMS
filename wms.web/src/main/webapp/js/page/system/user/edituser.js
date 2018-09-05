var indexdata=[],is_user=true

//base接口
function init(){
    showloading();
    var num=0
    var no=GetQueryString("no")||""
    var url=uriapi+"/auth/user_info?uid="+no
    ajax(url,"get",null,function(data){
        indexdata=data.body;
        fty(indexdata.user.corp_id)
        department(indexdata.user.fty_id)
        num++
        dataData(num)
    })
    var url=uriapi+"/conf/corp/list_corp"
    ajax(url,"get",null,function(data){
        $('[c-model="corp_id"] option').remove()
        $('[c-model="corp_id"]').append("<option value='0' style='display:none'>请选择公司</option>")
        $.each(data.body,function(i,item){
            $('[c-model="corp_id"]').append("<option value='"+item.corp_id+"'>"+item.corp_code+"-"+item.corp_name+"</option>")
        })
        num++
        dataData(num)
    })
}
	function returnOption(value, text) {
		return "<option value='" + value + "'>" + text + "</option>";
 }
function getprintinfo(){
	var url=uriapi+"/auth/get_printer_list";
	ajax(url,"GET",null,function(data){
		layer.close(indexloading);
		if(data.head.status==true){
			$.each(data.body.label_list,function(i,item){
			$(".label_print").append(returnOption(item.label_printer_id,item.label_printer_name))
		 })	
		 $.each(data.body.paper_list,function(i,item){
			$(".paper_print").append(returnOption(item.paper_printer_id,item.paper_printer_name))
		 })	
	 }
	}, function(e) {
			layer.close(indexloading);
		})
}
//工厂
function fty(data){
    if(data==null || data==0){
        $('[c-model="fty_id"] option').remove()
        $('[c-model="fty_id"]').append("<option value='0' style='display:none'>请选择工厂</option>")
        return false
    }
    var url=uriapi+"/auth/get_fty_for_corpid"
    var obj={
        corp_id:data
    }
    ajax(url,"post",JSON.stringify(obj),function(data){
        $('[c-model="fty_id"] option').remove()
        $('[c-model="fty_id"]').append("<option value='0' style='display:none'>请选择工厂</option>")
        $.each(data.body,function(i,item){
            $('[c-model="fty_id"]').append("<option value='"+item.fty_id+"'>"+item.fty_code+"-"+item.fty_name+"</option>")
        })
        $('[c-model="fty_id"]').val(indexdata.user.fty_id)
    })

}

//部门
function department(data){
    if(data==null || data==0){
        $('[c-model="department"] option').remove()
        $('[c-model="department"]').append("<option value='0' style='display:none'>请选择部门</option>")
        return false
    }
    var url=uriapi+"/conf/approvedept/list_department_by_ftyid?fty_id="+data
    ajax(url,"get",null,function(data){
        $('[c-model="department"] option').remove()
        $('[c-model="department"]').append("<option value='0' style='display:none'>请选择部门</option>")
        $.each(data.body,function(i,item){
            $('[c-model="department"]').append("<option value='"+item.department_id+"'>"+item.department_id+"-"+item.name+"</option>")
        })
        $('[c-model="department"]').val(indexdata.user.department)
    })
}

//库存地点
function get_location(fty_id){
    var url=uriapi+"/auth/get_location_for_ftyid"
    var obj={
        fty_id:fty_id
    }
    $(".loc_left option").remove()
    ajax(url,"post",JSON.stringify(obj),function(data){
        $.each(data.body,function(i,item){
            var is_equal=false
            $(".loc_right option").each(function(){
                if(item.location_id==$(this).attr("value")){
                    is_equal=true
                    return false
                }
            })
            if(is_equal==false){
                $(".loc_left").append("<option value='"+item.location_id+"'>"+item.fty_code+"-"+item.location_code+"-"+item.location_name+"</option>")
            }
        })
    })
}

//基础数据加载
function dataData(num){
    if(num!=2)return false
    if(GetQueryString("no")!=null){
        CApp.initBase("base",indexdata.user)
    }
    $.each(indexdata.all_role_list,function(i,item){
        $(".all_role_list").append("<option value='"+item.role_id+"'>"+item.role_name+"</option>")
    })
    $.each(indexdata.role_list,function(i,item){
        $(".role_list").append("<option value='"+item.role_id+"'>"+item.role_name+"</option>")
    })
    $.each(indexdata.product_line,function(i,item){
    	$(".product_line").append("<option value='"+item.product_line_id+"'>"+item.product_line_name+"</option>")
    })
    $(".all_fty_list").append("<option value='' style='display:none'>请选择工厂</option>")
    $.each(indexdata.all_fty_list,function(i,item){
        $(".all_fty_list").append("<option value='"+item.fty_id+"'>"+item.fty_code+"-"+item.fty_name+"</option>")
    })
    $.each(indexdata.location_list,function(i,item){
        $(".loc_right").append("<option value='"+item.loc_id+"'>"+item.fty_code+"-"+item.loc_code+"-"+item.loc_name+"</option>")
    })
	if(GetQueryString("no")!=null){
		$(".product_line").val(indexdata.product_line_id)
		$(".paper_print").val(indexdata.paper_printer_id)
		$(".label_print").val(indexdata.label_printer_id)
	}
    layer.close(indexloading);
}
//判断用户id
function userId(id){
//  if(!FormDataCheck.isUserName(id,4,10)) {
//      layersMoretips("以字母开头4-10位字母加数字用户ID", $('[ c-model="user_id"]'));
//      return false
//  }
    var url=uriapi+"/auth/is_user_id"
    var obj={
        user_id:id
    }
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.body==true){
            is_user=false
        }else{
            is_user=true
            layersMoretips("用户ID已存在",$('[ c-model="user_id"]'));
        }
    })
}

//提交
function submit(){
    var checked=true,role_list=[],location_list=[];

//    if(!FormDataCheck.isUserName($('[ c-model="user_id"]').val(),4,10)){
//        layersMoretips("以字母开头4-10位字母加数字用户ID",$('[c-model="user_id"]'));
//        $('[ c-model="user_id"]').focus();
//        checked=false
//    }else
    	
    if(is_user==true){
        layersMoretips("用户ID已存在",$('[c-model="user_id"]'));
       	$('[c-model="user_id"]').focus();
        checked=false
    }

    if(FormDataCheck.isNull($('[ c-model="user_name"]').val())){
        layersMoretips("用户姓名不能为空",$('[c-model="user_name"]'));
        $('[c-model="user_name"]').focus();
        checked=false
    }
	if($('[ c-model="mail"]').val()!=""){
		if(!FormDataCheck.isEmail($('[ c-model="mail"]').val())){
	        layersMoretips("请正确填写邮箱地址",$('[ c-model="mail"]'));
	        $('[ c-model="mail"]').focus()
	        checked=false
	    }
	}
	if($('[ c-model="mobile"]').val()!=""){
		if(!FormDataCheck.isCellPhone($('[ c-model="mobile"]').val())){
	        layersMoretips("请正确填写手机号",$('[ c-model="mobile"]'));
	        $('[ c-model="mobile"]').focus();
	        checked=false
	    }	
	}

    if(checked==false) return false

    $(".role_list option").each(function(){
        role_list.push({
            user_id:$('[c-model="user_id"]').val(),
            role_id:$(this).attr("value")
        })
    })

    $(".loc_right option").each(function(){
        location_list.push({
            user_id:$('[c-model="user_id"]').val(),
            fty_code:$(this).html().split("-")[0],
            location_code:$(this).html().split("-")[1]
        })
    })

    var obj={
        edit:GetQueryString("no")!=null?true:false,
        user_id:$('[c-model="user_id"]').val(),
        user_name:$('[c-model="user_name"]').val(),
        sex:$('[c-model="sex"]').val(),
        employer_number:$('[c-model="employer_number"]').val(),
        mail:$('[c-model="mail"]').val(),
        mobile:$('[c-model="mobile"]').val(),
        product_line_id:$('[c-model="product_line_id"]').val(),
        corp_id:$('[c-model="corp_id"]').val(),
        fty_id:$('[c-model="fty_id"]').val(),
        department:$('[c-model="department"]').val(),
        paper_printer_id:$(".paper_print").val(),
        label_printer_id:$(".label_print").val(),
        location_list:location_list,
        role_list:role_list,
        password:hex_md5("Init1234")
    }
	if(GetQueryString("no")!=null){
		obj.id=indexdata.id
	}
    showloading()
    var url=uriapi+"/auth/edit_user"
    ajax(url,"post",JSON.stringify(obj),function(data){
        if(data.head.status==true){
            setTimeout(function () { location.href = "userlist.html"; },0);
        }
        layer.close(indexloading)
    })
}

$(function(){
	 getprintinfo();
    if(GetQueryString("no")!=null){
    	$(".user_dy").html("修改用户");
        $(".spanno").html(GetQueryString("no"))
        is_user=false
    }
    init();
    $('[c-model="corp_id"]').change(function(){
        indexdata.user.fty_id=""
        indexdata.user.department=""
        fty($(this).val())
        department()
    })

    $('[c-model="fty_id"]').change(function(){
        indexdata.user.department=""
        department($(this).val())
    })

    $(".all_fty_list").change(function(){
        get_location($(this).val())
    })
	if(GetQueryString("no")==null){
	 $('[c-model="user_id"]').blur(function(){
        userId($(this).val())
    	})
	}
    //右移
    $(".btn_left").click(function(){
        var left=$(this).parent().parent().find("[name='left']");
        var right=$(this).parent().parent().find("[name='right']");
        if (left.find("option").length == 0 || left.get(0).selectedIndex == -1) {
            return;
        }
        // 得到左选中的option
        var opt = left.find("option").eq(left.get(0).selectedIndex);

        // 右-加option
        right.append(opt)
        //选中第一个
        left.get(0).selectedIndex=0;
        right.get(0).selectedIndex=0
    })

    //左移
    $(".btn_right").click(function(){
        var loc_left=[]
        $(".loc_left option").each(function(){
            loc_left.push($(this).attr("value"))
        })
        var left=$(this).parent().parent().parent().find("[name='left']");
        var right=$(this).parent().parent().parent().find("[name='right']");
        if (right.find("option").length == 0 || right.get(0).selectedIndex == -1) {
            return;
        }
        // 得到左选中的option
        var opt = right.find("option").eq(right.get(0).selectedIndex);
        if($(this).attr("data-index")=="1") {
            var fty=opt[0].innerHTML.split("-")[0]
            if($(".all_fty_list option:selected").html().split("-")[0]==fty){
                left.append(opt)
            }else{
                opt.remove()
            }
        }else{
            left.append(opt)
        }

        //选中第一个
        left.get(0).selectedIndex=0;
        right.get(0).selectedIndex=0
    })

})