$(function () {
    new Vue({
        el:"#container",
        data:{
            order:{},
            commentSize:0,
            signInRedirectUrl:"home"
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
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var vue = this;
                    axios.post("getCurrentOrder",{id:oid}).then(function (value) {
                        outs(value.data.message);
                        //outs(JSON.stringify(value.data.result));
                        if (value.data.code === 1){
                            vue.order = value.data.result;
                            vue.commentSize = value.data.result.length;
                            outs("------------");
                            outs(JSON.stringify(vue.order));
                        }else {
                            outs(value.data.message);
                        }
                    });
                }else {
                    window.location.href="login";
                }
            },
            postComment:function(e){
                // 判断是否登录
                var value = inferSignIn();
                outs("login infer code :"+value.code);
                if (value.code === 1) {//登录
                    var commentContent = $(e.currentTarget).parent().parent().prev().find("textarea").val();
                    outs(commentContent);
                    if (strTrim(commentContent) !== ""){
                        // 删除 评价 栏
                        $(e.currentTarget).parents(".comment-container").addClass("d-none");
                        var uid = this.order.orderItems[0].user.id;
                        var pid = this.order.orderItems[0].product.id;
                        var oid = this.order.id;
                        var vue = this;
                        axios.post("postComment",{
                            oid:oid,
                            uid:uid,
                            pid:pid,
                            commentContent:commentContent
                        }).then(function (value) {
                            if (value.data.code === 1){
                                alert("评论提交成功");
                                vue.commentSize -= 1;
                                if (vue.commentSize <= 0){//全部评论完，则跳转
                                    window.location.href="home";
                                }
                            }else {
                                outs(value.data.message);
                            }
                        });
                    }else {
                        $(e.currentTarget).parent().parent().prev().find(".comment-popover").popover({
                            content:"评论内容不能为空"
                        }).popover("show");
                    }
                }else {
                    window.location.href="login";
                }
            },
            removePopover:function(e){
                $(e.currentTarget).parent().prev().children().popover("hide");
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