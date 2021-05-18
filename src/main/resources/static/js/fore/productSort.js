jQuery( function () {
    //alert(" product sort");
    new Vue({
        el:"#container",
        data:{
            cid:"",
            user:{
                name:"",
                password:""
            },
            response:{},
            products:[],
            page:{},
            sortType:{
                zh:"zh"
            },
            currentSortType:"zh",//default sort
            productsMap:{},
            productImageMap:{},
            c:{
                id:""
            },
            signInRedirectUrl:"home"
        },
        mounted:function () {
            this.cid = getPathParams().cid;
            //this.c.id = this.cid;
            outs("cid"+this.cid);
            this.productSortByZH(0,6,this.cid);
        },
        watch:{
            cid:function (val,oldVal) {
                this.c.id = val;
            }
        },
        methods:{
            signIn:function () {
                var value = signIn(this.user);
                //value = JSON.stringify(value);
                outs("product sort value3="+value.code);
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go product sort");
                    // window.location.href="shopCart";
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
            //综合查询
            productSortByZH:function (start,size,cid) {
                var vue = this;
                axios.get("productSortByZH",{params:{start:start,size:size,cid:cid}}).then(function (value) {
                    outs("productSortByZH");
                    vue.response = value.data;
                    vue.products = vue.response.result.content;
                    vue.page = vue.response.result;
                    outs(JSON.stringify(vue.response));
                    vue.setData(vue.products);
                    outs(vue.c.id);
                });
            },
            pageTurning:function (start) {
                if (this.currentSortType === "zh"){
                    this.productSortByZH(start,6,this.cid);
                }else if(this.currentSortType === "other"){

                }
            },
            setData:function (oraginData) {
                outs("set data ");
                var products = [];
                //outs(JSON.stringify(oraginData));
                for (var i =0;i < oraginData.length;i++) {
                    products[i] = oraginData[i];
                    outs(oraginData[i]);
                    this.productImageMap[oraginData[i].id] = oraginData[i].productImages[0];
                    if (i > 3){
                        break;
                    }
                }
                this.productsMap[this.cid] = products;
                outs("set data 22");
                // outs(JSON.stringify(this.productsMap));
                // outs(JSON.stringify(this.productImageMap));
            }
        }
    });

} );