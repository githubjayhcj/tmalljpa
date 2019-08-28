console.log("main.js load");
var outs = function (string) {
    console.log(string);
};

var strTrim = function (string) {
    return string = string.replace(/\s*/g,"");//去除字符串所有空格
};

var notNull = function (string) {
    string = strTrim(string);
    if(string.length>0){
        return true;
    }
    return false;
};

var getPathParams = function () {
    //outs("editCategory1111");
    var paramStr = window.location.search;
    paramStr = paramStr.substr(1);
    var entrys = paramStr.split("&");
    var params = {};
    //outs(entrys);
    for (var index in entrys){
        var entry = entrys[index];
        var key = entry.substring(0,entry.indexOf("="));
        var value = entry.substring(entry.indexOf("=")+1);
        params[key] = value;
        //outs(params.id);
    }
    outs(params);
    return params;
};

var popoverInit = function(jqDom,content,color){
    if(color === undefined){
        color = "rgb(255,0,54)";
    }
    jqDom.popover({
        animation:true,
        content:content,
        trigger:"manual",
        template:'<div class="popover" role="tooltip">' +
            '<div class="arrow"></div>' +
            //'<h3 class="popover-header"></h3>' +
            '<div class="popover-body" style="color: '+color+';"></div>' +
            '</div>'
    });
};

//页面滚动事件,显示或隐藏置顶菜单
var bodyScrollEvent = function () {
    var containerFluid = $(".container-fluid");
    var slideDown = false;
    $(window).on("scroll",function () {
        if($(this).scrollTop() > 300){//300显示
            if(!slideDown){//改变，则执行
                $("#fixed-nav").slideDown("normal");
                slideDown = true;
            }
        }else {
            if(slideDown){//改变，则执行
                $("#fixed-nav").slideUp("normal");
                slideDown = false;
            }
        }
    });
};