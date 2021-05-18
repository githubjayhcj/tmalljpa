$(function () {
    var vue = new Vue({
        el:"#container",
        data:{
            pid:0,
            product:{},
            productSingle:[],
            productDetail:[],
            properties:[],
            products:[],
            productImageMap:{},
            user:{
                name:"",
                password:""
            },
            orderItem:{
                uid:"",
                pid:"",
                count:1
            },
            signInRedirectUrl:"home"
        },
        watch:{
            pid:function (val,oldVal) {
                this.orderItem.pid = val;
            }
        },
        mounted:function () {
            var urlParams = getPathParams();
            this.pid = urlParams.pid;
            this.getItemData();
            //tab  切换事件，自定义效果
            this.tabEvent();
        },
        methods:{
            bindPreviewImgEvent:function(){
                //绑定 图片列表事件 and 图片预览事件
                //outs("preview");
                var imgItems = $(".product-img-list-item"),
                    currentImgItems = $(".images-container").children("div");
                // bind img list event
                imgItems.on("mouseenter",function (event) {
                    //outs(event.target);
                    var imgItem = $(event.target).closest(".product-img-list-item");
                    //初始化
                    imgItems.removeClass("img-border");
                    currentImgItems.addClass("display-none");

                    //设置当前
                    imgItem.addClass("img-border");
                    var intoId = imgItem.data("href");//获取目标id
                    var imgParent = $("#"+intoId).parents(".img-parent");
                    imgParent.removeClass("display-none");
                });
                //绑定预览图片事件
                //bind preview img event
                var previewImgParents = $(".preview-img-parent"),
                    previewImgParent,
                    imgWH = [],
                    scanningDomWH = [],
                    scanningDomScale = 0.4,
                    monitorCanvasWH = [],
                    scanningDom,
                    monitorCanvas = $("#monitor-canvas"),
                    mouseemterdown = false,
                    imgLoad = false,
                    imgOriginOffset = {},
                    scanningDomXY = [],
                    img,
                    imgNaturalWH = [],
                    ctx;
                //enter
                previewImgParents.on("mouseenter",function (event) {
                    //outs("mouseenter");
                    previewImgParent = $(event.target).closest(".preview-img-parent");
                    //img 对象
                    img = previewImgParent.children("img").get(0);
                    //原始图片大小
                    var image = new Image();
                    image.onload = function (ev) {
                        //outs("onload.....");
                        imgNaturalWH[0] = ev.target.width;
                        imgNaturalWH[1] = ev.target.height;
                        //outs(imgNaturalWH);
                        imgLoad = true;
                    };
                    image.src = img.src;

                    scanningDom = previewImgParent.children("div");
                    imgWH[0] = previewImgParent.width();
                    imgWH[1] = previewImgParent.height();
                    //设置扫描对象
                    scanningDomWH[0] = imgWH[0]*scanningDomScale;
                    scanningDomWH[1] = imgWH[1]*scanningDomScale;
                    scanningDom.css({width:scanningDomWH[0]+"px"});
                    scanningDom.css({height:scanningDomWH[1]+"px"});
                    //设置显示对象
                    if(scanningDomWH[0] > scanningDomWH[1]){
                        monitorCanvasWH[0] = imgWH[0];
                        monitorCanvasWH[1] = scanningDomWH[1]/scanningDomWH[0]*monitorCanvasWH[0];
                    }else {
                        monitorCanvasWH[1] = imgWH[1];
                        monitorCanvasWH[0] = scanningDomWH[0]/scanningDomWH[1]*monitorCanvasWH[1];
                    }
                    monitorCanvas.css({width:monitorCanvasWH[0]+"px"});
                    monitorCanvas.css({height:monitorCanvasWH[1]+"px"});
                    monitorCanvas.attr({width:monitorCanvasWH[0],height:monitorCanvasWH[1]});
                    //显示
                    scanningDom.removeClass("display-none");
                    monitorCanvas.removeClass("display-none");
                    //获取canvas 上下文
                    ctx = monitorCanvas.get(0).getContext("2d");
                    mouseemterdown = true;
                    //outs(scanningDomWH);
                });

                //move
                previewImgParents.on("mousemove",function (event) {
                    if(mouseemterdown && imgLoad){
                        //自适应后的图片相对于文档的位置
                        imgOriginOffset = previewImgParent.offset();
                        //设置扫描对象位置
                        scanningDomXY[0] = event.pageX-imgOriginOffset.left-(scanningDomWH[0]/2);
                        scanningDomXY[1] = event.pageY-imgOriginOffset.top-(scanningDomWH[1]/2);
                        if(scanningDomXY[0] < 0){
                            scanningDomXY[0] = 0;
                        }
                        //imgMaxRange[0] = imgOriginOffset.left+imgWH[0];
                        if(scanningDomXY[0]+scanningDomWH[0] > imgWH[0]){
                            scanningDomXY[0] = imgWH[0]-scanningDomWH[0];
                        }
                        if(scanningDomXY[1] < 0){
                            scanningDomXY[1] = 0;
                        }
                        //imgMaxRange[1] = imgOriginOffset.top+imgWH[1];
                        if(scanningDomXY[1]+scanningDomWH[1] > imgWH[1]){
                            scanningDomXY[1] = imgWH[1]-scanningDomWH[1];
                        }
                        scanningDom.css({left:scanningDomXY[0]+"px",top:scanningDomXY[1]+"px"});
                        //绘制图像
                        ctx.drawImage(img,scanningDomXY[0]/imgWH[0]*imgNaturalWH[0],scanningDomXY[1]/imgWH[1]*imgNaturalWH[1],imgNaturalWH[0]*scanningDomScale,imgNaturalWH[1]*scanningDomScale,0,0,monitorCanvasWH[0],monitorCanvasWH[1]);
                        //outs(scanningDomXY[0]/imgWH[0]);
                    }
                });
                //out
                previewImgParents.on("mouseleave",function (event) {
                    //初始化
                    mouseemterdown = false;
                    imgLoad = false;
                    //隐藏
                    scanningDom.addClass("display-none");
                    monitorCanvas.addClass("display-none");
                    //outs("mouseleave");
                });

            },
            item:function(pid){
                outs(pid);
                window.location.href = "item?pid="+pid;
            },
            getItemData:function () {
                axios.get("getItemData",{params:{pid:this.pid}}).then(function (value) {
                    outs("----"+value.data);
                    if(value.data.code === 1){
                        outs(JSON.stringify(value.data.result));
                        vue.product = value.data.result.product;
                        vue.productSingle = value.data.result.productSingle;
                        vue.productDetail = value.data.result.productDetail;
                        vue.properties = value.data.result.properties;
                        vue.products = value.data.result.products;
                        vue.productImageMap = value.data.result.productImageMap;
                        outs("single="+JSON.stringify(vue.properties));

                        // DOM 还没有更新
                        Vue.nextTick(function () {
                            // DOM 更新了
                            //绑定图片预览事件
                            vue.bindPreviewImgEvent();
                        });

                    }
                    //alert(Math.floor(vue.products.length/3)*3);
                });
            },
            tabEvent:function () {
                outs("tab vent");
                var tabButtons = $(".list-group-custom");
                var tabPanes = $(".tab-pane-custom");
                tabButtons.on("click",function (e) {
                    outs($(e.target).closest(".list-group-custom").data("index"));
                    var jq_li = $(e.target).closest(".list-group-custom");
                    var index = jq_li.data("index");
                    // tab style
                    //init all tab
                    tabButtons.removeClass("tab-active-li-style");
                    tabButtons.children(".list-group-arrow").addClass("display-none");
                    tabButtons.children("span").removeClass("tab-active-span-style").addClass("tm-list-group-right-border");
                    //set target tab

                    jq_li.addClass("tab-active-li-style");
                    jq_li.children(".list-group-arrow").removeClass("display-none");
                    jq_li.children("span").addClass("tab-active-span-style").removeClass("tm-list-group-right-border");
                    if(index > 0){
                        tabButtons.eq(index-1).children("span").removeClass("tm-list-group-right-border");
                    }
                    //tab pane
                    //tabPanes.clearQueue();
                    tabPanes.stop();
                    tabPanes.hide();
                    tabPanes.eq(index).fadeIn(500);
                    return false;//阻止事件冒泡/捕获
                });
            },
            signIn:function () {
                var value = signIn(this.user);
                //value = JSON.stringify(value);
                outs(" item value2="+value.code);
                if (value.code === 1) {
                    //登录成功 ,返回首页
                    outs(" go item");
                    //window.location.href="item?pid="+this.pid;
                    window.location.href=this.signInRedirectUrl;
                }
            },
            // 加入购物车
            addShopCart:function(){
                outs("add shopcart ");
                this.signInRedirectUrl = "item?pid="+this.pid;
                outs(JSON.stringify(this.orderItem));
                var vue = this;
                // 判断是否登录
                var value = inferSignIn();
                outs("user code :"+value.code);
                // 是否登录
                if (value.code === 1){ //  不要带 0 ，其在程序中是八进制值，具有 boolean  false 的含义
                    outs("yes login");
                    vue.orderItem.uid = value.result.id;
                    //add order item
                    //alert(JSON.stringify(this.orderItem));
                    axios.post("addOrderItem",this.orderItem).then(function (value1) {
                        if(value1.data.code === 1){
                            outs("yes login addOrderItem");
                            // open light window
                            openLightWindow();
                        }
                    });

                }else {
                    outs("on logout");
                    //open login modal
                    $("#exampleModal").modal("show");
                }
            },
            // 立刻购买
            buyNow:function(){
                this.signInRedirectUrl = "item?pid="+this.pid;
                var vue = this;
                // 判断是否登录
                var value = inferSignIn();
                outs("user code :"+value.code);
                // 是否登录
                if (value.code === 1){ //  不要带 0 ，其在程序中是八进制值，具有 boolean  false 的含义
                    outs("yes login");
                    vue.orderItem.uid = value.result.id;
                    //add order item
                    //alert(JSON.stringify(this.orderItem));
                    axios.post("addOrderItem",this.orderItem).then(function (value1) {
                        outs(JSON.stringify(value1.data.result));
                        if(value1.data.code === 1){
                            outs("yes login addOrderItem");
                            // get orderitems id
                            var oiids = [],orderItems = value1.data.result.orderItems;
                            outs(orderItems.length);
                            for (var i = 0;i< orderItems.length;i++){
                                outs(orderItems[i].id);
                                oiids.push(orderItems[i].id);
                            }
                            outs("oiids="+oiids);
                            axios.post("sentOrderItems",oiids).then(function (value2) {
                                outs(value2.data.message);
                                if (value2.data.code === 1){
                                    // 跳转结算确认页面 ( 更改浏览器地址栏 )
                                    window.location.href = "orderDetail";
                                }else {
                                    outs(value2.data.message);
                                }
                            });
                        }else {
                            outs(value1.data.message);
                            alert("添加物品订单时错误 (addOrderItem)");
                        }
                    });

                }else {
                    outs("on logout");
                    //open login modal
                    $("#exampleModal").modal("show");
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
            //add count
            addCount:function () {
                this.orderItem.count = Math.abs(this.orderItem.count)+1;
            },
            //sub count
            subCount:function () {
                this.orderItem.count = Math.abs(this.orderItem.count)-1;
                if (this.orderItem.count < 1){
                    this.orderItem.count = 1;
                }
            },
            // input count 手动输入处理
            manualInputCount:function () {
                outs("count="+this.orderItem.count);
                var value = parseInt(this.orderItem.count);
                if (isNaN(value)){
                    this.orderItem.count = 1;
                }
                if (this.orderItem.count < 1){
                    this.orderItem.count = 1;
                }
            }
        }
    });

});