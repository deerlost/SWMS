(function ($) {
	$.fn.extend({
		"iSelect": function (options) {
			var opts = $.extend({}, defaluts, options);
			var thisselect=this;
			this.opts=opts;
			thisselect.empty();
			if(opts.placeholder!=null)
				thisselect.append("<option value=''>"+opts.placeholder+"</option>");
			
			var currdata=opts.data;
			if(opts.child!=null)
				currdata=currdata[opts.child];
			$.each(currdata,function(i,item){
				
				var value=item[opts.value]
					
				var text="";
				if(Array.isArray(opts.text)){
					$.each(opts.text,function(j,jitem){
						if(opts.text[j+1]==undefined)
							text+=(item[jitem]);
						else
							text+=(item[jitem]+"-");
					});
				}else
					text=item[opts.text]
					
				var property_checked="";
				if(opts.default!=null&&opts.default==value)
					property_checked=" selected='selected'";

				if(opts.equal==null||opts.child!=null){
					thisselect.append("<option value='"+value+"'"+property_checked+">"+text+"</option>");
				}else{
					if(item[opts.equal.field]==opts.equal.value)
						thisselect.append("<option value='"+value+"'"+property_checked+">"+text+"</option>");
				}
			});
			
			thisselect.change(function(){
				$(this).find("[value='']").remove();
				thisselect.selectchange();
			});
			
			if(opts.default!=null)
				thisselect.selectchange();
			
			if(opts.disabled)
				thisselect.attr("disabled","disabled");
			
			return this;
		},
		"selectchange":function(){
			var thisselect=this;
			var opts=this.opts;
			var thisoption=thisselect.find(":selected");
			var selectdata=null;
			var currdata=opts.data;
			if(opts.child!=null)
				currdata=currdata[opts.child];
			$.each(currdata,function(i,item){
				if(thisoption.val()==item[opts.value]){
					selectdata=item;
				}
			});
			if(opts.change!=undefined)
				opts.change(selectdata);
		}
	});
	var defaluts = {
		data:null,
		value:"value",
		text:"text",
		equal:null,
		default:null,
		disabled:false,
		child:null,
		placeholder:"请选择"
	};
})(window.jQuery);