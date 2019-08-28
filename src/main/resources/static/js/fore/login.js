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
            outs("sign in");
            outs(this.user.name);
            var usernameTip = $("#username-tip"),passwordTip = $("#password-tip");
            usernameTip.text("");
            passwordTip.text("");
            if(!notNull(this.user.name)){
                usernameTip.text("用户名不能为空");
                return;
            }
            if(!notNull(this.user.password)){
                passwordTip.text("密码不能为空");
                return;
            }
            axios.post("signIn",this.user).then(function (value) {
                outs(value.data.message);
                outs(value.data.code);
                if(value.data.code===1){
                    outs("1");
                    var code = value.data.result;
                    outs("code="+code);
                    if(code === 3){
                        usernameTip.text("用户名不存在");
                        return;
                    }
                    if(code === 4){
                        passwordTip.text("密码错误");
                        return;
                    }
                    if(code === 5){
                        //登录成功
                        outs("sign in ok");
                        //回到首页
                        window.location.href="home";
                    }
                }
            })
        }
    }
});