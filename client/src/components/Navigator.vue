<template>
    <div id="nav">
        <v-app-bar color="deep-purple accent-4" dense dark >
        <v-app-bar-nav-icon></v-app-bar-nav-icon>

            <v-toolbar-title>xx题目系统</v-toolbar-title>

            <v-spacer></v-spacer>
            <v-text text id="user">{{message}}</v-text>
            <v-btn text v-if="isLogin" :to="profile" style="margin-left: 5px;">{{btn1}}</v-btn>
            <v-btn text @click.native="run(isLogin)" :to="login">
                {{btn2}}
            </v-btn>
        </v-app-bar>
        
    </div>
</template>

<script>
export default {
    name:"Navigator",
    props:{
        
    },
    data:() => ({
        message:"你好，请先登录！",
        // isProfile:false,
        btn1:"我的信息",
        btn2:"登录",
        login:"/login",
        profile:"",
        isLogin:0,
        
    }),
    created:function(){
        let that = this;
        console.log(this.$route.name);
        that.profile = this.$route.name === "Profile" ? "/index" : "/profile";
        that.btn1 = this.$route.name === "Profile" ? "返回主页" : "我的信息";
        if (this.$store.state.user) {
            this.message = "欢迎你，" + this.$store.state.user;
            this.isLogin = 1;
            this.btn2 = "退出登录";
            this.login = "";
        } else {
            this.$request.get('/isLogin').then(ret => {
                if (ret.data.msg === "pass"){
                    that.btn2 = "退出登录";
                    that.isLogin = 1;
                    that.login = "/logout";
                    that.message = "欢迎你，" + ret.data.user.username;
                    that.$store.state.user = ret.data.user.username;
                }
            })
        }
    },
    methods: {
        run:function(isLogin){
            var that = this;
            this.login = "";
            if (isLogin){
                this.$request.post('/logout',{username:this.$store.state.user}).then(ret => {
                    if (ret.data.msg === "success"){
                        that.$store.state.user = "";
                        sessionStorage.removeItem("user");
                        sessionStorage.removeItem("language");
                        sessionStorage.removeItem("difficulty");
                        sessionStorage.removeItem("problemNumber");
                        alert("您已成功退出！");
                        that.$router.push('/login').catch(err => {console.log(err)});
                    } else {
                        alert("退出失败");
                    }
                }).catch( err => {console.log(err)})
            }
        }
    },
}
</script>

<style>
#nav {
    position: absolute;
    width: 100%;
}
#user {
    position: relative;
    padding-left: 10px;
}
</style>
