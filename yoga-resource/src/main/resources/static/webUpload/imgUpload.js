function delCity(){
	 var selOpt = $("#m_area #c_select");
	 selOpt.remove();
}
function imgUpload(fileId,uploadUrl){
	var $ = jQuery;
    $list = $('#fileList');
    var ratio = window.devicePixelRatio || 1;//获取屏幕的分辨率
    var thumbnailWidth = 80 * ratio;
    var thumbnailHeight = 80 *ratio;
    var uploader;
	var defaultValue={
    	auto: true,//选好文件后就上传
    	server:uploadUrl,//上传服务器地址
    	pick: {
        	id:"#"+fileId,
        	multiple:false
        },//上传文件的DOM
        fileNumLimit: 1,//只允许上传一个
        accept: {
	        title: 'Images',
	        extensions: 'gif,jpg,jpeg,bmp,png',
	        mimeTypes: 'image/jpg,image/jpeg,image/png'
	    }
    }
    var mybrowser = browserType();
    if(mybrowser=="IE"){
    	jQuery.extend(defaultValue,{
    		//swf:Base_pro+"//"+Base_host+"/plugins/webuploader/Uploader.swf"
    		swf:"/scripts/Uploader.swf"
    	})
    }
    //if ( !WebUploader.Uploader.support() ) {
      //  alert( 'Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
      // throw new Error( 'WebUploader does not support the browser you are using.' );
   // }
	uploader = WebUploader.create(defaultValue);

	uploader.on( 'fileQueued', function( file ) {
		//初始化省
		$('#fileList').empty();
	    var $li = $(
	            '<div id="' + file.id + '" class="file-item" style="margin-bottom:5px">' +
	                '<img width="30%">' +
	                //'<div class="info">' + file.name + '</div>' +
	            '</div>'
	            ),
	    $img = $li.find('img');
	    $list.append( $li );
	
	    uploader.makeThumb( file, function( error, src ) {
	        if ( error ) {
	            $img.replaceWith('<span>不能预览</span>');
	            return;
	        }
	
	        $img.attr( 'src', src );
	    }, thumbnailWidth, thumbnailHeight );
	});

	uploader.on( 'uploadProgress', function( file, percentage ) {
	    var $li = $( '#'+file.id ),
	        $percent = $li.find('.progress span');
	
	    if ( !$percent.length ) {
	        $percent = $('<p class="progress"><span></span></p>')
	                .appendTo( $li )
	                .find('span');
	    }
	    $percent.css( 'width', percentage * 100 + '%' );
	});

	uploader.on( 'uploadSuccess', function( file ) {
	    $( '#'+file.id ).addClass('upload-state-done');
	    $("#"+file.id).find(".state").text("上传成功");
	});
	
	uploader.on('uploadError', function(file) {
    	var $filecontainer = $('#' + file.id);
        var $state=$filecontainer.find(".state");
        $state.text("上传失败");
    });
	
	uploader.on( 'uploadComplete', function( file ) {
	    $( '#'+file.id ).find('.progress').remove();
	});
	
	uploader.on('uploadAccept', function(file, response) {
		console.log(file);
	    console.log($('#' + file.id).html());
	    console.log(response);
	    var result = response.result;
	    var file = result[0];
	    var url = file.filepath;
	    var filename = file.filename;
	    url += "?n=" + filename;
	    var prefix = filename.substring(filename.lastIndexOf(".")+1);
	    $("#avatar").val(url);
	    $("#filePrefix").val(prefix);
	    if (response.code == 1) {
	        // 通过return false来告诉组件，此文件上传有错。
	        return false;
	    }
	});
	// 先从文件队列中移除之前上传的图片，第一次上传则跳过
    $("#filePicker").on('click', function() {
    	
        if (!WebUploader.Uploader.support()) {
            var error = "上传控件不支持您的浏览器！请尝试升级flash版本或者使用Chrome引擎的浏览器。<a target='_blank' href='http://se.360.cn'>下载页面</a>";
            console.log(error);
            return;
        }
        var id = $list.find("div").attr("id");
        if (undefined != id) {
            uploader.removeFile(uploader.getFile(id));
            $list.empty();
            $("#avatar").val("");
        	$("#filePrefix").val("");
        }
    });
}
function browserType(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    if (userAgent.indexOf("Opera") > -1) {
        return "Opera"
    }; //判断是否Opera浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
    	return "Chrome";
    }
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE")) {
        return "IE";
    }; //判断是否IE浏览器
}

