$(function () {
    // init popover
    //popoverInit($("#username"),"用户名已存在");
    //$("#username").popover("show");
    outs("jq");
});

var vue = new Vue({
    el:'#container',
    data:{
        passwordFirst:"",
        passwordAgain:"",
        user:{
            name:"",
            password:""
        },
        userNameExist:false
    },
    mounted:function () {

    },
    methods:{
        signUp:function(){
            outs("signUp="+this.userNameExist);
            var username = $("#username");
            var password = $("#password");
            if(this.userNameExist){
                username.popover("dispose");
                popoverInit(username,"用户名已存在");
                username.popover("show");
                return;
            }
            if (!notNull(this.user.name)) {
                username.popover("dispose");
                popoverInit(username,"用户名不能为空");
                username.popover("show");
                return;
            }
            if(this.passwordAgain !== this.passwordFirst){//两次不一致
                password.popover("dispose");
                popoverInit(password,"两次密码不一致");
                password.popover("show");
                return;
            }
            if(!notNull(this.user.password)){
                password.popover("dispose");
                popoverInit(password,"密码不能为空");
                password.popover("show");
                return;
            }
            axios.post("signUp",this.user).then(function (value) {
                outs(value.data.message);
                if(value.data.code === 1){
                    if(value.data.result){
                        window.location.href="registerSuccessful";
                    }
                }
            });
        },
        validateUserName:function () {//用户名是否已存在
            var pop = $("#username");
            outs(this.user.name);
            if(notNull(this.user.name)){
                axios.get("validateUserName",{params:{name:this.user.name}}).then(function (value) {
                    outs(value.data.message);
                    if(value.data.result){
                        vue.userNameExist = false;
                        pop.popover("dispose");
                        popoverInit(pop,"用户名可用","#009551");
                        pop.popover("show");
                    }else {
                        vue.userNameExist = true;
                        pop.popover("dispose");
                        popoverInit(pop,"用户名已存在");
                        pop.popover("show");
                    }
                });
            }else {
                outs("validateUserName null");
                this.userNameExist = false;
                pop.popover("dispose");
            }
        },
        validatePasswordFirst:function(){
            var pwf = $("#password");
            if(!notNull(this.passwordFirst)){
                pwf.popover("dispose");
                popoverInit(pwf,"密码不能为空");
                pwf.popover("show");
            }else {
                pwf.popover("dispose");
            }
        },
        validatePasswordAgain:function(){
            outs(this.passwordFirst);
            outs(this.passwordAgain);
            outs(this.passwordAgain !== this.passwordFirst);
            var pwa = $("#passwordAgain");
            if(!notNull(this.passwordAgain)){
                outs("null");
                pwa.popover("dispose");
                popoverInit(pwa,"请再次输入密码");
                pwa.popover("show");
                return;
            }
            if(this.passwordAgain !== this.passwordFirst){//两次不一致
                pwa.popover("dispose");
                popoverInit(pwa,"两次密码不一致");
                pwa.popover("show");
            }else {
                pwa.popover("dispose");
            }
        }
    },
    watch:{
        passwordAgain:function (val) {
            this.user.password = val;
            outs("watch="+this.user.password);
        },
        "user.name":function (val) {
            outs("name watch="+val);
            if(!notNull(val)){
                this.userNameExist = false;
                outs("change")
            }
            outs("exist="+this.userNameExist);
        }
    }
});