var vue = new Vue({
    el:"#container",
    data:{
        user:{
            name:"",
            password:""
        }
    },
    mounted:function () {
        
    },
    methods:{
        signIn:function () {
            var value = signIn(this.user);
            //value = JSON.stringify(value);
            outs("value2="+value.code);
            if (value.code === 1) {
                //登录成功 ,返回首页
                outs(" go home");
                window.location.href="home";
            }
        }
    }
});