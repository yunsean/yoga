function delArticle(articleId) {
    warningModal("确定要删除该文章吗？", function () {
        $.get(
            "/api/cms/article/delete?id=" + articleId,
            function (data) {
                if (data.code < 0) {
                    alertShow("danger", data.message, 3000);
                } else {
                    window.location.reload();
                }
            },
            "json"
        );
    });
}
function editArticle(articleId, columnId, alone) {
    var param = "?id=" + articleId + "&columnId=" + columnId + "&alone=" + alone;
    window.parent.location.href = "/cms/article/edit" + param;
}

function onlineArticle(articleId, online) {
    $.get(
        "/api/cms/article/online?id=" + articleId + "&online=" + online,
        function (data) {
            if (data.code < 0) {
                alertShow("danger", data.message, 3000);
            } else {
                window.location.reload();
            }
        },
        "json"
    );
}
function sortArticle(data) {
    var name = $("#search_name").val();
    var pageIndex = $("#thisPageIndex").val();
    $.get(
        "/api/cms/article/sort?id=" + data.getAttribute("articleId") + "&sort=" + data.value,
        function (data) {
            if (data.code < 0) {
                alertShow("danger", data.message, 3000);
            } else {
                window.location.reload();
            }
        },
        "json"
    );
}
function addArticle(columnId, alone) {
    window.parent.location.href = "/cms/article/edit?columnId=" + columnId + "&alone=" + alone;
}
function formPageSize() {
    document.getElementById("form1").submit()
    setCookie("pageSize", $("#pageSizeSelect").val());
}
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}