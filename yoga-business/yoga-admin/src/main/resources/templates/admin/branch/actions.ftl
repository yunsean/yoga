<#macro branchChoice>
<script>
    function setupAutoCompleteBranch(input, result, value, tableId, url = '/admin/operator/branch/filter.json?filter=') {
        input.keydown(function (event) {
            var k = window.event ? event.keyCode : event.which;
            if (k == 13) return false;
            else return true;
        });
        var onkeyup = function (event) {
            value.val(null)
            var left = input.position().left;
            var top = input.position().top + input.outerHeight();
            result.css("left", left + "px");
            result.css("top", top + "px");
            result.css("width", input.outerWidth() + "px");
            var key = window.event ? event.keyCode : event.which;
            if (key != 38 && key != 40 && key != 13) {
                $.get(url + input.val(), function (data) {
                    if (data.result && data.result.length > 0) {
                        var layer = "";
                        layer = "<table id='" + tableId + "'>";
                        $.each(data.result, function (idx, item) {
                        layer += "<tr style='width: 100%; height: 36px' class='line' data-id='" + item.id + "' data-name='" + item.name + "'>";
                            layer += "<td>" + item.name + "</td>";
                            layer += "</tr>";
                        });
                        layer += "</table>";
                        result.empty();
                        result.append(layer);
                        $(".line:first").addClass("hover");
                        result.css("display", "");
                        $(".line").hover(function () {
                            $(".line").removeClass("hover");
                            $(this).addClass("hover");
                        }, function () {
                            $(this).removeClass("hover");
                        });
                        $(".line").click(function () {
                            value.val($(this).attr('data-id'));
                            input.val($(this).attr('data-name'));
                            result.css("display", "none");
                        });
                    } else {
                        result.empty();
                        result.css("display", "none");
                    }
                });
            } else if (key == 38) {
                $('#' + tableId + ' tr.hover').prev().addClass("hover");
                $('#' + tableId + ' tr.hover').next().removeClass("hover");
                value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                input.val($('#' + tableId + ' tr.hover').attr('data-name'));
            } else if (key == 40) {
                $('#' + tableId + ' tr.hover').next().addClass("hover");
                $('#' + tableId + ' tr.hover').prev().removeClass("hover");
                value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                input.val($('#' + tableId + ' tr.hover').attr('data-name'));
            } else if (key == 13) {
                value.val($('#' + tableId + ' tr.hover').attr('data-id'));
                input.val($('#' + tableId + ' tr.hover').attr('data-name'));
                result.empty();
                result.css("display", "none");
            } else {
                result.empty();
                result.css("display", "none");
            }
        };
        input.blur(function() {
            if (!result.is(":focus")) {
                setTimeout(function () {
                    result.css("display", "none");
                }, 500);
            }
        });
        input.click(function () {
            onkeyup(event);
        });
        input.focus(function (event) {
            onkeyup(event);
        });
        input.keyup(function (event) {
            onkeyup(event);
        });
    }
</script>
</#macro>

