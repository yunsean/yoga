function clickLocation(xValue, yValue, keyword) {
    clickLocation(keyword, function (x, y) {
        $("input[name='" + xValue + "']").attr("value", x);
        $("input[name='" + yValue + "']").attr("value", y);
    })
}
function clickLocation(keyword, callback, longitude, latitude) {
    var editorDivs = document.getElementsByClassName('editorDiv');
    for (var i = 0; i < editorDivs.length; i++) {
        var editor = editorDivs[i];
        editor.style.display = "none";
    }
    var mapContent =
        '<div id="location_popup_background" class="popBackground" style="z-index: 9999; background-color:rgba(0,0,0,0.4);position:fixed;width:100%;height:100%;left:0px;top:0px;z-index:800">' +
        '<div id="location_popup_view" class="popWindow" style="width:1000px">' +
        '<div class="popWindow-header">' +
        '<h3>地理坐标选择</h3>' +
        '</div>' +
        '<div class="popWindow-body" style="text-align:center;">' +
        '<div style="margin-bottom: 20px">' +
        '<div class="form-group col-md-4">' +
        '<input id="location_popup_keyword" class="form-control" placeholder="请输入关键字搜索"/>' +
        '<input type="hidden" id="location_popup_address"/>' +
        '</div>'+
        '<div class="form-group col-md-4">' +
        '<label class="control-label col-md-4" style="text-align:right; margin-top: 5px">经度</label>' +
        '<div class="col-md-8">' +
        '<input class="form-control" readonly id="location_popup_longitude"/>' +
        '</div>' +
        '</div>' +
        '<div class="form-group col-md-4">' +
        '<label class="control-label col-md-4" style="text-align:right; margin-top: 5px">纬度</label>' +
        '<div class="col-md-8">' +
        '<input class="form-control" readonly id="location_popup_latitude" />' +
        '</div>' +
        '</div>' +
        '</div>' +
        '<div id="location_popup_mapview" style="width:100%;height:500px;margin:5px">' +
        '</div>' +
        '<div>' +
        '<table>' +
        '</table>' +
        '</div>' +
        '</div>' +
        '<div class="popWindow-footer" style="margin: 20px">' +
        '<button type="button" id="location_popup_confirm" class="btn blue">确定</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp' +
        '<button type="button" id="location_popup_cancel" class="btn default" >取消</button>' +
        '</div>' +
        '</div>' +
        '</div>';
    $("body").append(mapContent);
    var top = ($(window).height() - 500) / 2;
    var left = ($(window).width() - 1000) / 2;
    $("#location_popup_view").css({
        "position": "fixed",
        "left": left + "px",
        "top": 15 + "px",
        "z-index": "801",
        "background-color": "white",
        "padding": "10px",
        "text-align": "center"
    });
    $("#location_popup_cancel").on("click", function () {
        $("#location_popup_background").remove();
        var editorDivs = document.getElementsByClassName('editorDiv');
        for (var i = 0; i < editorDivs.length; i++) {
            var editor = editorDivs[i];
            editor.style.display = "block";
        }
    });
    var title = keyword;
    $("#location_popup_keyword").val(title);
    var provinceName = $("#officesBox").find("#province").find("option:selected").text();
    var cityName = $("#officesBox").find("#city").find("option:selected").text();
    var districtName = $("#officesBox").find("#district").find("option:selected").text();
    var streetName = $("[name='address']").val();
    var locationDetail = provinceName + cityName + districtName + streetName;
    var map;
    if (latitude && longitude) {
        map = new AMap.Map("location_popup_mapview", {
            zoom: 15,
            center: [longitude, latitude],
            resizeEnable: true
        });
    } else {
        map = new AMap.Map("location_popup_mapview", {
            zoom: 15,
            resizeEnable: true
        });
    }
    AMap.plugin(['AMap.ToolBar', 'AMap.Scale'], function () {
        var toolBar = new AMap.ToolBar();
        var scale = new AMap.Scale();
        map.addControl(toolBar);
        map.addControl(scale);
    });
    AMap.service(["AMap.PlaceSearch"], function () {
        var placeSearch = new AMap.PlaceSearch({
            pageSize: 5,
            pageIndex: 1,
            city: cityName,
            map: map
        });
        if (keyword) placeSearch.search(keyword);
        else placeSearch.search(locationDetail);
    });

    var auto = new AMap.Autocomplete({input: "location_popup_keyword"});
    var placeSearch = new AMap.PlaceSearch({
            map: map
    }); //设置搜索查询
    AMap.event.addListener(auto, "select", select );

    function select(e){
        placeSearch.setCity(e.poi.adcode);
        placeSearch.search(e.poi.name);
    }

    // 点击地图marker点回调
    AMap.event.addListener(placeSearch, "markerClick", function(e) {
        $("#location_popup_longitude").val(e.data.location.lng);
        $("#location_popup_latitude").val(e.data.location.lat);
        $("#location_popup_address").val(e.data.address);
    });
    if (keyword) placeSearch.search(keyword);
    var marker, N = 0;
    if (latitude && longitude) {
        marker = new AMap.Marker({
            map: map,
            title: title,
            icon: "/admin/map/map_marker.png",
            position: [longitude, latitude]
        });
    } else {
        marker = new AMap.Marker({
            map: map,
            icon: "/admin/map/map_marker.png",
            title: title
        });
    }

    map.on("click", function (e) {
        var xx = e.lnglat.getLng();
        var yy = e.lnglat.getLat();
        marker.setPosition([xx, yy]);
        marker.setTitle(title);
        marker.setLabel({
            offset: new AMap.Pixel(20, 20),//修改label相对于maker的位置
            content: title
        });
        var location_popup_longitude = document.getElementById("location_popup_longitude");
        location_popup_longitude.value = xx;
        var location_popup_latitude = document.getElementById("location_popup_latitude");
        location_popup_latitude.value = yy;
        N = N + 1;
    });
    $("#location_popup_confirm").on("click", function () {
        var x = $("#location_popup_longitude").val();
        var y = $("#location_popup_latitude").val();
        var a = $("#location_popup_address").val();
        if (x == '' || y == "") {
            alertShow("请先在地图上点选", 2000, "warning");
        } else {
            $("#location_popup_background").remove();
            var editorDivs = document.getElementsByClassName('editorDiv');
            for (var i = 0; i < editorDivs.length; i++) {
                var editor = editorDivs[i];
                editor.style.display = "block";
            }
            if (callback) {
                callback(x, y, a);
            }
        }
    });
    $("#location_popup_background").on("click", function (e) {
        if (e.target.className == "popBackground") {
            $("#location_popup_background").remove();
        }
    });
}


