$(function () {
    new Vue({
        el:"#container",
        data:{
            order:{
                id:0,
                totalPrice:"0.00"
            }
        },
        mounted:function () {
            this.payOrder();
        },
        methods:{
            payOrder:function () {
                var oid = getPathParams().oid;
                outs(oid);
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var vue = this;
                    axios.get("getOrder",{params:{oid:oid}}).then(function (value) {
                        outs(value.data.message);
                        if (value.data.code === 1){
                            vue.order = value.data.result;
                        }else {
                            outs(value.data.message);
                            alert("没有订单信息");
                            window.location.href="home";
                        }
                    });
                }else {
                    window.location.href="login";
                }
            },
            payMoney:function () {
                outs("payMoney");
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    axios.post("payMoney",this.order).then(function (value) {
                        if (value.data.code === 1){
                            alert("支付成功");
                            window.location.href="myOrderList";
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