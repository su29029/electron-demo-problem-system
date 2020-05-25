<template>
  <v-container>
    <v-container style="position: fixed;margin: 0 auto;width: 100%;left: 0; right: 0">
        <v-alert :type="status" v-if="isShowAlert">
            {{registerMessage}}
        </v-alert>
    </v-container>
    <v-container style="margin-top: 10%;">
        <v-card class="mx-auto" outlined width="400" height="470" :disabled="isShowProgress">
            <v-progress-linear indeterminate color="green" v-if="isShowProgress"></v-progress-linear>
            <v-card-text>
                <p class="text-center" style="margin-top:40px;font-size:24px;">
                注册
                </p>
            </v-card-text>
                <v-form ref="form" class="mx-auto" align="center">
                <v-col align="center">
                    <v-row align="center" style="width: 80%;">
                        <v-text-field 
                            color="blue" 
                            label="请输入用户名" 
                            hint="3-20 characters" 
                            :rules="rules" 
                            v-model="username" 
                            @keyup.enter="register"
                            class="username" 
                            clearable
                        ></v-text-field>
                    </v-row>
                    <v-row align="center" style="width: 80%;">
                        <v-text-field 
                            color="blue" 
                            label="请输入密码" 
                            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" 
                            :type="show1 ? 'text' : 'password'" 
                            hint="3-20 characters"
                            :rules="rules" 
                            @keyup.enter="register"
                            @click:append="show1 = !show1" 
                            v-model="password" 
                            class="password" 
                            clearable
                        ></v-text-field>
                    </v-row>
                    <v-row align="center" style="width: 80%;margin-bottom:20px;">
                        <v-text-field 
                            color="blue" 
                            label="请再次输入密码" 
                            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" 
                            :type="show1 ? 'text' : 'password'" 
                            hint="3-20 characters"
                            :rules="rules" 
                            @keyup.enter="register"
                            @click:append="show1 = !show1" 
                            v-model="repassword" 
                            class="repassword" 
                            clearable
                        ></v-text-field>
                    </v-row>
                </v-col>
                <v-btn color="success" @click="register" class="register">注册</v-btn>
                <v-btn @click="goLogin" text class="login">
                    <span class="mr-2">使用已有账号登录</span>
                </v-btn>
            </v-form>
        </v-card>
        
    </v-container>
  </v-container>
</template>

<script>
export default {
    name: 'Register',
    data: () => ({
        rules: [
            value => !!value || 'Required.',
            value => (value || '').length <= 20 || 'Max 20 characters.',
            value => (value || '').length >= 3 || 'Min 3 characters.',
        ]
        ,
        username: '',
        password: '',
        repassword: '',
        registerMessage: '',
        status: '',
        isShowAlert: false,
        isShowProgress: false,
        show1: false
    }),
    methods: {
        register: function(){
            let that = this;
            if (that.username.length < 3 || that.username.length > 20) {
                that.isShowAlert = true;
                that.status = 'error';
                that.registerMessage = 'Invalid username,the length must between 3 characters and 20 characters.';
                setTimeout(() => {
                    that.isShowAlert = false;
                    that.registerMessage = '';
                },4500);
            } else if (that.password.length < 3 || that.password.length > 20) {
                that.isShowAlert = true;
                that.status = 'error';
                that.registerMessage = 'Invalid password,the length must between 3 characters and 20 characters.';
                setTimeout(() => {
                    that.isShowAlert = false;
                    that.registerMessage = '';
                },4500);
            } else if (that.password !== that.repassword) {
                that.isShowAlert = true;
                that.status = 'error';
                that.registerMessage = 'Password input is inconsistent.';
                setTimeout(() => {
                    that.isShowAlert = false;
                    that.registerMessage = '';
                },4500);
            } else {
                that.isShowProgress = true;
                that.$request.post('/register',{"username":that.username,"password":that.password}).then( ret => {
                    if (ret.data.msg === "success"){
                        that.isShowProgress = false;
                        that.isShowAlert = true;
                        that.status = 'success';
                        that.registerMessage = 'Register success,please login.';
                        setTimeout(() => that.$router.push('/login'),2000);
                    } else if (ret.data.msg === "username has been registered"){
                        that.isShowProgress = false;
                        that.isShowAlert = true;
                        that.status = 'error';
                        that.registerMessage = 'The username has been registered.';
                        setTimeout(() => {
                            that.isShowProgress = false;
                            that.isShowAlert = false;
                            that.registerMessage = '';
                        },4500);
                    } else if (ret.data.msg === "something error"){
                        that.isShowProgress = false;
                        that.isShowAlert = true;
                        that.status = 'error';
                        that.registerMessage = 'Something error, please try again later.';
                        setTimeout(() => {
                            that.isShowProgress = false;
                            that.isShowAlert = false;
                            that.registerMessage = '';
                        },4500);
                    } else {
                        that.isShowProgress = false;
                        that.isShowAlert = true;
                        that.status = 'error';
                        that.registerMessage = 'Unknown error, please try again later.';
                        setTimeout(() => {
                            that.isShowProgress = false;
                            that.isShowAlert = false;
                            that.registerMessage = '';
                        },4500);
                    }
                }).catch( err => {
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'error';
                    that.registerMessage = 'Something error.Please check your network and try again.';
                    setTimeout(() => {
                        that.isShowProgress = false;
                        that.isShowAlert = false;
                        that.registerMessage = '';
                    },4500);
                })
            }
            
        },
        goLogin: function(){
            setTimeout(() => this.$router.push('/login'),150);
            
        }
    }
}
</script>

<style scoped>

.register {
    left: 2%;
    margin: 2px;
}
.login {
    left: 2%;
    margin: 2px;
}
</style>
