// init methods  
function initFunction() {
    $('[data-toggle="tooltip"]').tooltip();
}

//console.log("main.js load");
var outs = function (string) {
    console.log(string);
};

var strTrim = function (string) {
    return string = string.replace(/\s*/g,"");//去除字符串所有空格
};

var notNull = function (string) {//字符串不为空
    string = strTrim(string);
    if(string.length>0){
        return true;
    }
    return false;
};

var getPathParams = function () {//获取url 路径参数
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

var popoverInit = function(jqDom,content,color){//tootip 提示工具初始化
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

//类切换
var toggleClass = function (removeTarget,addTarget,class_) {
    removeTarget.removeClass(class_);
    addTarget.addClass(class_);
};

//日期格式转换
var dataFormat = function (date,template) {
    var newDate = new Date(date);
    var tempDate = "";
    template = template.split("");    //日期最多7个单位
    // 年月日时分秒毫秒
    if (template.length  > 0){
        tempDate+=newDate.getFullYear();
        tempDate+=template[0];
    }
    if(template.length > 1){
        tempDate+=(newDate.getMonth()+1);
        tempDate+=template[1];
    }
    if(template.length > 2){
        tempDate+=newDate.getDate();
        tempDate+=template[2];
    }
    if(template.length > 3){
        tempDate+=newDate.getHours();
        tempDate+=template[3];
    }
    if(template.length > 4){
        tempDate+=(newDate.getMinutes()+1);
        tempDate+=template[4];
    }
    if(template.length > 5){
        tempDate+=(newDate.getSeconds()+1);
        tempDate+=template[5];
    }
    if(template.length > 6){
        tempDate+=(newDate.getMilliseconds()+1);
        tempDate+=template[6];
    }
    return tempDate;
};

//账号登录 验证方法
var signIn = function (user) {
    outs("sign in");
    outs(user.name);
    var usernameTip = $("#username-tip"),passwordTip = $("#password-tip");
    usernameTip.text("");
    passwordTip.text("");
    //返回值
    var responseValue = {
        code:0
    };
    if(!notNull(user.name)){
        usernameTip.text("用户名不能为空");
        return responseValue;
    }
    if(!notNull(user.password)){
        passwordTip.text("密码不能为空");
        return responseValue;
    }
    $.ajax({
        url:"signIn",
        async:false,//  默认 true 异步
        contentType:"application/json;charset=utf-8",// @RequestBody 获取参数，需要将上传参数转json格式字符串,默认的 application/x-www-form-urlencoded  是key/value形式的文本需要用 @ReqestPram  获取
        dataType:"json",// 接收数据格式
        type:"post",
        data:JSON.stringify(user),//复杂对象， application/x-www-form-urlencoded  这种形式是没有办法将复杂的 JSON 组织成键值对形式。你传进去之后可以发送请求，但是服务端收到数据为空
        error:function (XMLHttpRequest_,errorMessage,errorTarget) {
            console.log("请求发生异常");
            console.log(XMLHttpRequest_);
            console.log(errorMessage);
            console.log(errorTarget);
        },
        success:function (value) {
            outs(" ajax request");
            outs(value);
            var code = value.code;
            outs("code="+code);
            if(code === 4){
                usernameTip.text("用户名错误");
                return;
            }
            if(code === 5){
                passwordTip.text("密码错误");
                return;
            }
            if(code === 1){
                //登录成功
                outs("sign in ok");
                //回到首页
                //window.location.href=hrefUrl;
                // 返回结果，需要时可取值
                outs("value1="+value);
                responseValue = value;
            }
        }
    });
    return responseValue;
};

//购物车
var shoppingCart = function () {
    outs(" shoppig cart 2 ");
    // 判断是否登录
    var value = inferSignIn();
    outs("user code :"+value.code);
    // 是否登录
    if (value.code !== 1){//没登录
        //open login modal
        $("#exampleModal").modal("show");
    }
    return value;
};

//我的订单
var orderList = function () {
    outs(" my order list 2 ");
    // 判断是否登录
    var value = inferSignIn();
    outs("user code :"+value.code);
    // 是否登录
    if (value.code !== 1){
        //open login modal
        $("#exampleModal").modal("show");
    }
    return value;
};

// 判断是否登录
function inferSignIn() {
    outs("infer sign in");
    var responseVaue = {};
    $.ajax({
        url:"inferSignIn",
        async:false,//  默认 true 异步
        contentType:"application/json;charset=utf-8",// @RequestBody 获取参数，需要将上传参数转json格式字符串,默认的 application/x-www-form-urlencoded  是key/value形式的文本需要用 @ReqestPram  获取
        dataType:"json",// 接收数据格式
        type:"get",
        data:{},//复杂对象， application/x-www-form-urlencoded  这种形式是没有办法将复杂的 JSON 组织成键值对形式。你传进去之后可以发送请求，但是服务端收到数据为空
        error:function (XMLHttpRequest_,errorMessage,errorTarget) {
            console.log("请求发生异常");
            console.log(XMLHttpRequest_);
            console.log(errorMessage);
            console.log(errorTarget);
            responseVaue = {
                code:3,
                message:"request error"
            };
        },
        success:function (value) {
            outs(" ajax request");
            outs(value);
            responseVaue = value;
        }
    });
    return responseVaue;
}

var timer;
// light window open
function openLightWindow() {
    $(".light-window").removeClass("d-none");
    // set auto close
    timer = window.setTimeout(function () {
        $(".light-window").addClass("d-none");
    },3000);
}

//close light window
function closeLightWindow() {
    $(".light-window").addClass("d-none");
    //clear timer
    window.clearTimeout(timer);
}

// star score event
function starScoreOver(e) {
    outs("start score over");
    outs($(e.currentTarget));
    var target = $(e.currentTarget);
    var id = target.attr("id");
    outs(id == 1);
    if (id == 1){
        target.children(".bi-star").addClass("invisible");
        target.children(".bi-star-fill").removeClass("invisible").addClass("visible");
    }
    if (id == 2){
        target.prevAll().each(function (index,el) {
            el = $(el);
            el.children(".bi-star").addClass("invisible");
            el.children(".bi-star-fill").removeClass("invisible").addClass("visible");
        });
        target.children(".bi-star").addClass("invisible");
        target.children(".bi-star-fill").removeClass("invisible").addClass("visible");
    }
    if (id == 3 || id == 4){
        target.prevAll().each(function (index,el) {
            el = $(el);
            el.children(".bi-star").addClass("invisible");
            el.children(".bi-star-fill").removeClass("invisible").addClass("visible font-color-14");
        });
        target.children(".bi-star").addClass("invisible");
        target.children(".bi-star-fill").removeClass("invisible").addClass("visible font-color-14");
    }
    if (id == 5){
        target.prevAll().each(function (index,el) {
            el = $(el);
            el.children(".bi-star").addClass("invisible");
            el.children(".bi-star-fill").removeClass("invisible").addClass("visible font-color-7");
        });
        target.children(".bi-star").addClass("invisible");
        target.children(".bi-star-fill").removeClass("invisible").addClass("visible font-color-7");
    }
}

// star  score event
function starScoreOut(e) {
    outs("start score out");
    var allStar = $(".bi-star") , allStarFill = $(".bi-star-fill") ;
    allStar.removeClass("invisible").addClass("visible");
    allStarFill.removeClass("visible font-color-14 font-color-7").addClass("invisible");
}
