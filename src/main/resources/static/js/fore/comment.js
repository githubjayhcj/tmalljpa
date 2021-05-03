$(function () {
    new Vue({
        el:"#container",
        data:{
            order:{}
        },
        mounted:function(){
            //init
            this.initPopover();
            var oid = getPathParams().oid;
            outs("mounted");
            this.getOrderItem(oid);
        },
        methods:{
            getOrderItem:function(oid){
                var vue = this;
                axios.post("getCurrentOrder",{id:oid}).then(function (value) {
                    outs(value.data.message);
                    //outs(JSON.stringify(value.data.result));
                    if (value.data.code == 1){
                        vue.order = value.data.result;
                        outs("------------");
                        outs(JSON.stringify(vue.order));
                    }else {
                        outs(value.data.message);
                    }
                });
            },
            postComment:function(e){
                var commentContent = $(e.currentTarget).parent().parent().prev().find("textarea").val();
                outs(commentContent);
                if (strTrim(commentContent) != ""){
                    // 删除 评价 栏
                    $(e.currentTarget).parents(".comment-container").addClass("d-none");
                    var uid = this.order.orderItems[0].user.id;
                    var pid = this.order.orderItems[0].product.id;
                    var oid = this.order.id;
                    axios.post("postComment",{
                        oid:oid,
                        uid:uid,
                        pid:pid,
                        commentContent:commentContent
                    }).then(function (value) {
                        if (value.data.code == 1){
                            alert("评论提交成功");
                        }else {
                            outs(value.data.message);
                        }
                    });
                }else {
                    $(e.currentTarget).parent().parent().prev().find(".comment-popover").popover({
                        content:"评论内容不能为空"
                    }).popover("show");
                }

            },
            removePopover:function(e){
                $(e.currentTarget).parent().prev().children().popover("hide");
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
            // star  score event
            starScoreOver:function (e) {
                starScoreOver(e);
            },
            // star  score event
            starScoreOut:function (e) {
                starScoreOut(e);
            },
            // init
            initPopover:function () {
                $(".comment-popover").popover();
            }
        }
    });
});