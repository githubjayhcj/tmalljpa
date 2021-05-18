$(function () {
    //alert("shopcart");
    new Vue({
        el:"#container",
        data:{
            orderItems:[],
            timer:"",
            oiids:[],
            productImageMap:{},
            productsMap:{
                "2":[]  // 该值任意，需对应 c.id 即可
            },
            c:{
                id:"2"
            },
            user:{
                name:"",
                password:""
            },
            signInRedirectUrl:"home"

        },
        mounted:function () {
            this.getOrderItem();
            // 底部推荐栏 5 条 ---------
            this.getRecommendProduct();
        },
        methods:{
            getOrderItem:function(){
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var vue = this;
                    axios.get("getOrderItem").then(function (value) {
                        outs(" getOrderItem ");
                        outs(value.data);
                        if (value.data.code === 1){
                            vue.orderItems = value.data.result;
                        }else {
                            outs(value.data.message);
                        }
                    });
                }else {
                    window.location.href="login";
                }
            },
            // 底部推荐栏 5 条 ---------
            getRecommendProduct:function(){
                var vue = this;
                axios.get("getRecommendProduct").then(function (value) {
                    outs(" getRecommendProduct ");
                    outs(value.data);
                    if (value.data.code === 1){
                        vue.productsMap[vue.c.id] = value.data.result.products;
                        vue.productImageMap = value.data.result.productImageMap;
                    }else {
                        outs(value.data.message);
                    }
                });
            },
            //购物车
            goShoppingCart:function () {
                outs(" shoppig cart ");
                this.signInRedirectUrl = "shopCart";
                var value = shoppingCart();
                outs("home value2="+value.code);
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go url");
                    window.location.href=this.signInRedirectUrl;
                }
            },
            //我的订单
            orderList:function () {
                outs(" my order list ");
                this.signInRedirectUrl = "myOrderList";
                var value = orderList();
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go url");
                    window.location.href=this.signInRedirectUrl;
                }
            },
            signIn:function () {
                var value = signIn(this.user);
                //value = JSON.stringify(value);
                outs(" item value2="+value.code);
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go item");
                    window.location.href=this.signInRedirectUrl;
                }
            },
            countTotalPrice:function(){
                outs("count total price");
                var checkData = $(".check-data");
                var totalPrice = 0.00;
                checkData.each(function (index,el) {// el is html dom
                    totalPrice+=$(el).data("price")*$(el).val();
                });
                totalPrice = totalPrice.toFixed(2);
                // set total price data (title menu
                var countEl = $(".total-count"),totalPriceEl = $(".total-price"),sealButton = $(".seal-button");
                if (totalPrice != 0){
                    countEl.text(checkData.length);
                    totalPriceEl.text(totalPrice);
                    sealButton.removeAttr("disabled").removeClass("bg-color-2").addClass("bg-color-4");
                }else {
                    countEl.text(checkData.length);
                    totalPriceEl.text(totalPrice);
                    sealButton.attr("disabled","disabled").removeClass("bg-color-4").addClass("bg-color-2");
                }
            },
            checkAll:function(e){
                outs("check all");
                var checkAllInput = $(".check-all"),checkOneInput = $(".check-box"),checkDataInput = $(".check-data-input");
                outs("check value="+$(e.currentTarget).children("input").prop("checked"));
                if ($(e.currentTarget).children("input").prop("checked")){// 实时交互属性需用 prop
                    outs("checkall");
                    checkAllInput.prop("checked",true);// checked 另一个复选框
                    checkOneInput.prop("checked",true);// checked 所有单个复选框
                    checkDataInput.addClass("check-data");// 标记数据文档

                }else {
                    outs("no check");
                    checkAllInput.prop("checked",false);
                    checkOneInput.prop("checked",false);
                    checkDataInput.removeClass("check-data");
                }
                // count total peice
                this.countTotalPrice();
            },
            checkOne:function(e){
                outs("check one");
                var checkBoxInput = $(e.currentTarget);
                var checkDataInput = $("#check-data-"+checkBoxInput.attr("data-check-data-id"));
                var checkAllInput = $(".check-all");
                if (checkBoxInput.prop("checked")) {
                    checkDataInput.addClass("check-data");
                }else {
                    checkDataInput.removeClass("check-data");
                    checkAllInput.prop("checked",false);
                }
                // count total peice
                this.countTotalPrice();
            },
            //add count
            addCount:function (e) {
                outs("add count");
                var input = $(e.currentTarget).siblings("input").eq(0);
                outs(e.currentTarget);
                var count = Math.abs(1 + parseInt(input.val()));
                input.val(count);
                //count price
                var totalPrice = input.data("price") * parseInt(count);
                outs(totalPrice);
                var oiid = input.attr("data-oiid");
                $("#count-price-"+oiid).text(totalPrice);
                this.updateCount(oiid,count);

            },
            //sub count
            subCount:function (e) {
                outs("subCount count");
                var input = $(e.currentTarget).siblings("input").eq(0);
                outs(e.currentTarget);
                var count = Math.abs(parseInt(input.val()) - 1);
                if (count < 1){
                    count = 1;
                }
                input.val(count);
                //count price
                var totalPrice = input.data("price") * parseInt(count);
                outs(totalPrice);
                var oiid = input.attr("data-oiid");
                $("#count-price-"+oiid).text(totalPrice);
                this.updateCount(oiid,count);
            },
            // input count 手动输入处理
            manualInputCount:function (e) {
                outs("subCount count");
                var input = $(e.currentTarget);
                var count = Math.abs(parseInt(input.val()));
                if (isNaN(count)){
                    count = 1;
                }
                if (count < 1){
                    count = 1;
                }
                input.val(count);
                //count price
                var totalPrice = input.data("price") * parseInt(count);
                outs(totalPrice);
                var oiid = input.attr("data-oiid");
                $("#count-price-"+oiid).text(totalPrice);
                this.updateCount(oiid,count);
            },
            updateCount:function (oiid,count) {
                outs(" update count ");
                window.clearTimeout(this.timer);
                this.timer = window.setTimeout(function () {
                    var value = inferSignIn();
                    //value = JSON.stringify(value);
                    outs(" item value2="+value.code);
                    if (value.code == 1) {
                        axios.post("updateOrderItemCount",{id:oiid,count:count}).then(function (value) {
                            outs(value.data.message);
                        });
                    }
                },1000);
            },
            sentOrderItems:function () {
                outs(" sentOrderItems now ");
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var checkData = $(".check-data"),oiids=[];
                    checkData.each(function (index,el) {
                        outs($(el).data("id"));
                        oiids.push($(el).data("id"));
                    });
                    outs("oiids");
                    outs(oiids);
                    axios.post("sentOrderItems",oiids).then(function (value) {
                        outs(value.data.message);
                        if (value.data.code === 1){
                            // 跳转结算确认页面 ( 更改浏览器地址栏 )
                            window.location.href = "orderDetail";
                        }else {
                            outs(value.data.message);
                        }
                    });
                }else {
                    window.location.href="login";
                }
            }

        }
    });
});