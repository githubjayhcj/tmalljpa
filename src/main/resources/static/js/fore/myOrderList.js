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
            productImageMap:{}
        },
        mounted:function () {
            this.getMyOrder();
        },
        methods:{
            getMyOrder:function(start){
                outs("my order list");
                var vue = this;
                axios.get("getMyOrder",{params:{start:start}}).then(function (value) {
                    outs(value.data.message);
                    outs(JSON.stringify(value.data.result.productImagesMap));
                    if (value.data.code == 1){
                        vue.page = value.data.result.navigatorPage;
                        vue.orders = value.data.result.navigatorPage.content;
                        vue.productsMap[vue.c.id] = value.data.result.products;
                        vue.productImageMap = value.data.result.productImagesMap;
                    }else {
                        outs(value.data.message);
                    }
                });
            },
            //购物车
            goShoppingCart:function () {
                outs(" shoppig cart ");
                shoppingCart();
            },
            //我的订单
            orderList:function () {
                outs(" my order list ");
                orderList();
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
                if (value.code == 1){
                    $(e.currentTarget).parent().addClass("d-none");
                    var vue = this;
                    axios.post("confirmOrder",{id:oid}).then(function (value) {
                        outs(value.data.message);
                        outs(JSON.stringify(value.data.result));
                        if (value.data.code == 1){
                            //确认成功
                            alert("确认收货成功");

                        }else {
                            outs(value.data.message);
                        }
                    });
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