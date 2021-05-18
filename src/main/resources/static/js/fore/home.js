// home page

//carousel index item event
var carouselIndexHover = function () {
    $(".carousel-indicators > li").on("mouseenter",function (event) {
        outs(event.target);
        $(event.target).click();
    });
    var carouselIndex = $("#homeCarousel>ol>li");
    $('#homeCarousel').on('slide.bs.carousel', function (event) {
        // do something…
        outs(event.from);
        carouselIndex.eq(event.from).addClass("carousel-index-bg");
        carouselIndex.eq(event.to).removeClass("carousel-index-bg");
    });
};

var categoryShowAndHiddenTab = function (){
    var textColor = [
        "text-primary",
        "text-secondary",
        "text-success",
        "text-danger",
        "text-warning",
        "text-info",
        "text-dark",
        "text-muted"
    ];

    var textColorIndex = 0;
    // show tab
    $(".list-group-item").on("mouseenter",function (event) {
        event.preventDefault();
        outs("index1="+textColorIndex);
        // init -----
        // 初始化菜单列表样式
        $(".category-menu-item").removeClass("active show bg-white").find("span:last-child").removeClass(textColor[textColorIndex]);
        //初始化icon
        $(".category-menu-item").find("span:first-child").removeClass(textColor[textColorIndex]);
        // 初始化tab 列表样式
        $(".tab-pane-item").removeClass("active show").find(".high-color").removeClass(textColor[textColorIndex]);

        // set -------
        //随机文字颜色
        textColorIndex = Math.round(Math.random()*(textColor.length-1));
        outs("index2="+textColorIndex);
        //当前菜单列表样式
        $(this).tab("show").addClass("bg-white").find("span:last-child").addClass(textColor[textColorIndex]);
        //设置icon
        $(this).find("span:first-child").addClass(textColor[textColorIndex]);
        //tab 中高亮字体
        $(".high-color").addClass(textColor[textColorIndex]);
    });
    // mouseleave  hidden tab  init category menu item
    $(".category-tab-show").on("mouseleave",function (event) {
        event.preventDefault();
        outs("mouse leave");
        outs(textColorIndex);
        if(!$(event.relatedTarget).hasClass("category-tab-show")){
            // 初始化菜单列表样式
            $(".category-menu-item").removeClass("active show bg-white").find("span:last-child").removeClass(textColor[textColorIndex]);
            //初始化icon
            $(".category-menu-item").find("span:first-child").removeClass(textColor[textColorIndex]);
            // 初始化tab样式,隐藏
            $(".tab-pane-item").removeClass("active show");
        }
    });
};


$(function () {
    //页面滚动事件,显示或隐藏置顶菜单
    bodyScrollEvent();
});

var vue = new Vue({
    el:"#container",
    data:{
        categories:[
            {name:""},
            {name:""},
            {name:""},
            {name:""},
            {name:""}
        ],
        productsMap:{},
        productImageMap:{},
        user:{
            name:"",
            password:""
        },
        signInRedirectUrl:"home"
    },
    mounted:function(){
        this.homeData();
    },
    methods:{
        homeData:function () {
            axios.get("homeData").then(function (value) {
                outs(value.data.message);
                if(value.data.code == 1){
                    vue.categories = value.data.result.categories;
                    vue.productsMap = value.data.result.productsMap;
                    vue.productImageMap = value.data.result.productImageMap;
                    // DOM 还没有更新
                    Vue.nextTick(function () {
                        // DOM 更新了
                        //left category menu show tab event
                        categoryShowAndHiddenTab();
                        //carousel index item event
                        carouselIndexHover();
                    })

                }

            });
        },
        subTitleList:function (val) {
            var subTitls = val.split(",");
            if(subTitls.length > 1){
                return subTitls;
            }
            var subTitls = val.split("，");
            if(subTitls.length > 1){
                return subTitls;
            }
            var subTitls = val.split(" ");
            if(subTitls.length > 1){
                return subTitls;
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
        }
    }
});