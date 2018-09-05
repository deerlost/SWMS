$(function() {
	showloading();
	var text = "修改成功"
	var obj = {
		"role_id": GetQueryString("role_id")
	};
	if(GetQueryString("action") == "add") {
		var action = false
		text = "添加成功"
	} else {
		var action = true
		$("#roleId").attr("disabled", "disabled")
	}
	var url = uriapi + "/auth/role/show_role_msg"
	ajax(url, "POST", JSON.stringify(obj), function(data) {
		$("#roleName").val(data.body.role.role_name);
		$("#role_Name").val(data.body.role.role_name);
		$("#roleId").val(data.body.role.role_id);
		$.each(data.body.ex_resources_ary, function(i, item) {
			$("#leftSelect").append("<option value='" + item.resources_id + "'>" + item.resources_name + "</option>")
		});
		$.each(data.body.resources_ary, function(i, item) {
			$("#rightSelect").append("<option value='" + item.resources_id + "'>" + item.resources_name + "</option>")
		});
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	}, function(e) {
		setTimeout(function() {
			layer.close(indexloading);
		}, 50);
	});

	// 确定授权
	$("#grantResources").click(function() {
		showloading();
		var resourcesIds = '';
		$("#rightSelect option").each(function() {
			resourcesIds += $(this).val() + ',';
		});

		if(resourcesIds.length > 0) {
			resourcesIds = resourcesIds.substring(0, resourcesIds.length - 1);
		}
        var obj = {
            "role_id" : $("#roleId").val(),
            "role_name" : $("#roleName").val(),
            "edit" : action,
            "resources_ids" : resourcesIds,
            "role": "role",
        };
		if(!FormDataCheck.isNumber(obj.role_id, 1, 10)) {
			layersMoretips("请填写1至10位数字", $("#roleId"));
			layer.close(indexloading)
			return false
		}
		if(FormDataCheck.isNull(obj.role_name)) {
			layersMoretips("角色名称不能为空", $("#roleName"));
			layer.close(indexloading)
			return false
		}
		var url = uriapi + "/auth/role/modify_role";

		ajax(url, "POST", JSON.stringify(obj), function(data) {
			if(data.body.number == 0) {
				setTimeout(function() {
					parent.location.href = "rolelist.html";
				},0);
			}
			if(data.body.number == 1) {
				layersMoretips("角色ID重复", $("#roleId"))
			}
			if(data.body.number == 2) {
				layersMoretips("角色名称重复", $("#roleName"))
			}
			if(data.body.number == 3) {
				layer.msg("角色ID与角色名称名称重复")
			}
			setTimeout(function() {
				layer.close(indexloading);
			}, 50);
		});
	});
	//右移
	$("#leftGotoRight").click(function() {
		var leftSelect = document.getElementById("leftSelect");
		var rightSelect = document.getElementById("rightSelect");
		if(leftSelect.length == 0 || leftSelect.selectedIndex == -1) {
			return;
		}
		// 得到左选中的option
		var opt = leftSelect.options[leftSelect.selectedIndex];
		// 右-加option
		rightSelect.options.add(new Option(opt.text, opt.value));
		// 左-删除选中option
		leftSelect.options.remove(leftSelect.selectedIndex);
		leftSelect.selectedIndex = 0;
		rightSelect.selectedIndex = 0;
	});

	//左移
	$("#rightGotoLeft").click(function() {

		var leftSelect = document.getElementById("leftSelect");
		var rightSelect = document.getElementById("rightSelect");
		if(rightSelect.length == 0 || rightSelect.selectedIndex == -1) {
			return;
		}
		// 得到右选中的option
		var opt = rightSelect.options[rightSelect.selectedIndex];
		// 左-加option
		leftSelect.options.add(new Option(opt.text, opt.value));
		// 右-删除选中option
		rightSelect.options.remove(rightSelect.selectedIndex);
		leftSelect.selectedIndex = 0;
		rightSelect.selectedIndex = 0;
	});

	//修改角色ID
if(GetQueryString("action")=="add"){
		$("#roleId").blur(function() {
			isrole("id")
		});
		$("#roleName").blur(function() {
			if($("#role_Name").val() != $("#roleName").val()) {
				isrole("name")
			}
		});
}
	function isrole(text) {
		var obj = {
			role_id: $("#roleId").val(),
			role_name: $("#roleName").val()
		};
		if(!FormDataCheck.isNumber(obj.role_id, 1, 10) && text == "id") {
			layersMoretips("请填写1至10位数字", $("#roleId"));
			return false
		}
		var url = uriapi + "/auth/role/is_role_id";
		ajax(url, "POST", JSON.stringify(obj), function(data) {
			if(text == "id" && (data.body.number == 1 || data.body.number == 3)) {
				layersMoretips("角色ID重复", $("#roleId"));
			} else if(text == "name" && (data.body.number == 2 || data.body.number == 3)) {
				layersMoretips("角色名称重复", $("#roleName"));
			}
			setTimeout(function() {
				layer.close(indexloading);
			}, 50);

		});
	}
});