$(function () {
    new Vue({
        el:"#container",
        data:{
            orders:[],
            page:{},
            c:{
                id:"1"
            },
            productsMap:{},
            productImageMap:{},
            user:{
                name:"",
                password:""
            },
            signInRedirectUrl:"home"
        },
        mounted:function () {
            this.getMyOrder();
            // 底部推荐栏 5 条 ---------
            this.getRecommendProduct();
        },
        methods:{
            getMyOrder:function(start){
                outs("my order list");
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var vue = this;
                    axios.get("getMyOrder",{params:{start:start}}).then(function (value) {
                        outs(value.data.message);
                        outs(JSON.stringify(value.data.result.content));
                        if (value.data.code === 1){
                            vue.page = value.data.result;
                            vue.orders = value.data.result.content;
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
                        if(value.data.code === 2){
                            //重新登录当前页面
                            vue.goShoppingCart();
                        }
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
            showFlag:function () {
                $("#flag").toggleClass("invisible").toggleClass("visible");
            },
            pageTurning:function (start) {
                outs(" page turning");
                outs(start);
                this.getMyOrder(start);
            },
            payNow:function (oid) {
                window.location.href = "payPage?oid="+oid;
            },
            confirmOrder:function(oid,e){
                outs("confirmOrder");
                // 判断是否登录
                var value = inferSignIn();
                outs(value);
                if (value.code === 1){
                    $(e.currentTarget).parent().addClass("d-none");
                    var vue = this;
                    axios.post("confirmOrder",{id:oid}).then(function (value) {
                        outs(value.data.message);
                        outs(JSON.stringify(value.data.result));
                        if (value.data.code === 1){
                            //确认成功
                            alert("确认收货成功");
                            window.location.reload();
                        }else {
                            outs(value.data.message);
                        }
                    });
                }else {
                    window.location.href="login";
                }
            },
            comment:function (oid) {
                window.location.href = "comment?oid="+oid;
            }
        },
        filters:{
            setDate:function(date,temp){
                return dataFormat(date,temp);
            }
        }
    });
});