var alertShow=function(type,infoContest,closeTime,ondismiss){
	var testInfo=/(success|info|warning|danger)/i;
	if(!testInfo.test(type)){
		return;
	}else{
		$("#alertShow").remove();
		var AlertContent=
			'<div id="alertShow" class="alert alert-'+type+' alert-dismissable" style="width:500px">'+
				'<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>'+
				'<h5>提示</h5>'+
		   		'<p style="text-align:center">'+infoContest.replace(/\n/g,"<br>")+'</p>'+
			'</div>';
		$("body").append(AlertContent);
		var totalWidth=$(window).width();
		var alertWidth=$("#alertShow").width();
		var leftPosition=(totalWidth-alertWidth)/2+"px";
		$("#alertShow").css({
			"position":"fixed",
			"top":"30px",
			"left":leftPosition,
			"z-index":"10000"
		});
		if(typeof(closeTime)=="number"){
			setTimeout(function(){
				$("#alertShow").alert("close");
				if (ondismiss) ondismiss();
			}, closeTime);
		}
		
	}	
};
var warningModal=function(warningInfo,handleMethod,titleInfo){
    if(null == titleInfo || "" == titleInfo){
        titleInfo = "警告";
    }
    var modalContent=
        '<div class="modal fade modal-warning" id="warningBox" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'+
        '<div class="modal-dialog">'+
        '<div class="modal-content">'+
        '<div class="modal-header">'+
        '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
        '<h4 class="modal-title">'+titleInfo+'</h4>'+
        '</div>'+
        '<div class="modal-body">'+
        '<p style="text-align:center">'+warningInfo.replace(/\n/g,"<br>")+'</p>'+
        '</div>'+
        '<div class="modal-footer">'+
        '<button type="button" class="btn btn-primary confirmButton">确定</button>'+
        '<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'
    $("#warningBox").detach();
    $("body").append(modalContent);
    $("#warningBox").modal("show");
    $(".confirmButton").one("click", function () {
        $("#warningBox").modal("hide");
        if (typeof(handleMethod) == "function") {
            handleMethod();
        }
    });
    $("#warningBox").on("hidden.zui.modal",function(){
        $(this).next(".modal-backdrop").remove();
        $("#warningBox").detach();
        $("#warningBox").remove();
    });
};
var confirmModal=function(confirmInfo,handleMethod, titleInfo){
	if(null == titleInfo || "" == titleInfo){
		titleInfo = "请确认";
	}
	var modalContent=
			'<div id="confirmBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="confirmBox" aria-hidden="true">'+
				'<div class="modal-dialog">'+
					'<div class="modal-content">'+
						'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-label="Close" aria-hidden="true"></button>'+
							'<h4 class="modal-title">'+titleInfo+'</h4>'+
						'</div>'+
						'<div class="modal-body">'+
								'<p style="text-align:center">'+
									 confirmInfo.replace(/\n/g,"<br>")+
							 	'</p0>'+
						'</div>'+
						'<div class="modal-footer">'+
							'<button type="button" class="btn btn-primary confirmButton">确认</button>'+
							'<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'
    $("#confirmBox").detach();
	$("body").append(modalContent);
	$("#confirmBox").modal("show");
	$(".confirmButton").on("click",function(){
		if(typeof(handleMethod)=="function"){
			handleMethod();
		}
		$("#confirmBox").modal("hide");
	});
	$("#confirmBox").on("hidden.zui.modal",function(){
		$(this).next(".modal-backdrop").remove();
        $("#confirmBox").detach();
    	$("#confirmBox").remove();
	});
};





