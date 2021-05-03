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
                var vue = this;
                axios.get("getOrder",{params:{oid:oid}}).then(function (value) {
                    outs(value.data.message);
                    if (value.data.code == 1){
                        vue.order = value.data.result;
                    }else {
                        outs(value.data.message);
                    }
                });
            },
            payMoney:function () {
                outs("payMoney");
                axios.post("payMoney",this.order).then(function (value) {
                    if (value.data.code == 1){
                        window.location.href="myOrderList";
                    }else {
                        outs(value.data.message);
                    }
                });
            }
        }
    });
});