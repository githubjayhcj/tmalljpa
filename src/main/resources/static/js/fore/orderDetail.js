$(function () {
    new Vue({
        el:'#container',
        data:{
            orderItems:[],
            totalPrice:'0.00',
            order:{
                address:'浙江 杭州 西湖 古荡 文一路xxx村南x区xx号楼',
                receiver:"天猫客户",
                mobile:"15088659428",
                userMessage:""
            },
            user:{
                name:"",
                password:""
            },
            signInRedirectUrl:"home"
        },
        mounted:function(){
            this.getSessionOrderItem();

        },
        methods:{
            getSessionOrderItem:function () {
                outs("getSessionOrderItem");
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var vue = this;
                    axios.post("getSessionOrderItem").then(function (value) {
                        outs(JSON.stringify(value.data));
                        if (value.data.code === 1){
                            outs(JSON.stringify(value.data.message));
                            outs(JSON.stringify(value.data.result));
                            vue.orderItems = value.data.result.orderItems;
                            vue.totalPrice = value.data.result.totalPrice;
                            vue.order.receiver = value.data.result.userName;
                            Vue.nextTick(function () {
                                vue.startInit();
                            });
                        }else if (value.data.code === 3){//session 中没有订单信息
                            window.location.href="home";
                        }
                    });
                }else {
                    window.location.href="login";
                }
            },
            signIn:function () {
                var value = signIn(this.user);
                //value = JSON.stringify(value);
                outs("home value2="+value.code);
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go url");
                    window.location.href=this.signInRedirectUrl;
                }
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
            //提交订单
            confirmOrderItem:function () {
                outs("confirmOrderItem");
                outs(this.order.address);
                if (notNull(this.order.address)){
                    outs(this.order.receiver);
                    if (notNull(this.order.receiver)){
                        outs(this.order.mobile);
                        if(notNull(this.order.mobile)){
                            var pattern = new RegExp("^1[34578][0-9]{9}$", 'i');
                            if (this.order.mobile.length === 11 && pattern.test(this.order.mobile)){
                                // 判断是否登录
                                var value = inferSignIn();
                                outs("login infer code :"+value.code);
                                // 是否登录
                                if (value.code === 1){//登录
                                    axios.post("addOrder",this.order).then(function (value) {
                                        outs(value.data.message);
                                        if (value.data.code === 1){
                                            window.location.href = "payPage?oid="+value.data.result;
                                        }else {
                                            outs(value.data.message);
                                        }
                                    });
                                }else {//没登录
                                    window.location.href = "login";
                                }

                            }else {
                                $("#mobile").attr('data-original-title', '手机号格式错误')
                                    .tooltip("enable").tooltip("show").tooltip("disable");
                                window.location.href = "#anchor-point";
                            }
                        }else {
                            $("#mobile").attr('data-original-title', '请填写手机号')
                                .tooltip("enable").tooltip("show").tooltip("disable");
                            window.location.href = "#anchor-point";
                        }
                    }else {
                        $("#receiver").attr('data-original-title', '请填写收收件人名字')
                            .tooltip("enable").tooltip("show").tooltip("disable");
                        window.location.href = "#anchor-point";
                    }
                }else {
                    $("#address").attr('data-original-title', '请填写收货地址')
                        .tooltip("enable").tooltip("show").tooltip("disable");
                    window.location.href = "#anchor-point";
                }
            },
            closeTooltip:function(e){
                outs($(e.currentTarget).parent("div"));
                $(e.currentTarget).parent("div").tooltip("hide").tooltip("disable");
            },
            startInit:function () {
                // init methods
                $('[data-toggle="tooltip"]').tooltip({
                    trigger:"click",
                    template:'<div class="tooltip" role="tooltip"><div class="arrow my-type-22"></div><div class="tooltip-inner bg-color-4"></div></div>'
                });
                $('[data-toggle="tooltip"]').tooltip("disable");
            }
        }
    });
});