var vue = new Vue({
    el:"#container",
    data:{
        seconds:10
    },
    mounted:function () {
        this.countDown();
    },
    methods:{
        countDown:function () {
            if(this.seconds > 0){
                window.setTimeout(function () {
                    outs("seconds="+vue.seconds);
                    vue.seconds--;
                    vue.countDown();
                },1000);
            }else {
                window.location.href="home";
            }
        }
    }
});