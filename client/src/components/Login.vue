<template>
  <v-container>
    <v-container style="position: fixed;margin: 0 auto;width: 100%;">
      <v-alert :type="status" v-if="isShowAlert">
          {{loginMessage}}
      </v-alert>
    </v-container>
    <v-container style="margin-top: 10%;">
      <v-card class="mx-auto" outlined width="400" height="470" :disabled="isShowProgress">
        <v-progress-linear indeterminate absolute color="green" v-if="isShowProgress"></v-progress-linear>
        <v-card-text>
          <p class="text-center" style="margin-top:40px;font-size:24px;">
            登录
          </p>
        </v-card-text>
        <v-form ref="form" class="mx-auto" align="center">
          <v-col align="center">
            <v-row align="center" style="width: 80%;margin-top:15px;">
              <v-text-field 
                color="blue" 
                label="请输入用户名" 
                hint="3-20 characters" 
                :rules="rules" 
                v-model="username" 
                @keyup.enter="login"
                class="username" 
                clearable
              ></v-text-field>
            </v-row>
            <v-row align="center" style="width: 80%;margin-top:15px;margin-bottom:20px">
              <v-text-field 
                color="blue" 
                label="请输入密码" 
                :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" 
                :type="show1 ? 'text' : 'password'" 
                hint="3-20 characters"
                :rules="rules" 
                @click:append="show1 = !show1" 
                @keyup.enter="login"
                v-model="password" 
                class="password" 
                clearable
              ></v-text-field>
            </v-row>
          </v-col>
          <v-btn color="success" @click="login" class="login">登录</v-btn>
          <v-btn @click="goRegister" text class="register">
            <span class="mr-2">注册新账号</span>
          </v-btn>
      </v-form>
      </v-card>
      
    </v-container>
  </v-container>
  
</template>

<script>
  export default {
    name: 'Login',

    data: () => ({
      rules: [
        value => !!value || 'Required.',
        value => (value || '').length <= 20 || 'Max 20 characters.',
        value => (value || '').length >= 3 || 'Min 3 characters.'
      ],
      username: '',
      password: '',
      loginMessage: '',
      status: '',
      isShowAlert: false,
      isShowProgress: false,
      show1: false
    }),
    create:function(){

    },
    methods:{
      login:function(){
          let that = this;
          if (that.username.length < 3 || that.username.length > 20) {
              that.isShowAlert = true;
              that.status = 'error';
              that.loginMessage = 'Invalid username,the length must between 3 characters and 20 characters.';
              setTimeout(() => {
                  that.isShowAlert = false;
                  that.loginMessage = '';
              },4500);
          } else if (that.password.length < 3 || that.password.length > 20) {
              that.isShowAlert = true;
              that.status = 'error';
              that.loginMessage = 'Invalid password,the length must between 3 characters and 20 characters.';
              setTimeout(() => {
                  that.isShowAlert = false;
                  that.loginMessage = '';
              },4500);
          } else {
              that.isShowProgress = true;
              that.$request.post('/login',{"username":that.username,"password":that.password}).then( ret => {
                if (ret.data.msg === "success"){
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'success';
                    that.loginMessage = 'Login success.';
                    sessionStorage.setItem("user", that.username);
                    that.$store.state.user = that.username;
                    setTimeout(() => that.$router.push('/index'),2000);
                } else if (ret.data.msg === "username not exist"){
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'error';
                    that.loginMessage = ret.data.msg;
                    setTimeout(() => {
                        that.isShowAlert = false;
                        that.loginMessage = '';
                    },4500);
                } else if (ret.data.msg === "password wrong") {
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'error';
                    that.loginMessage = ret.data.msg;
                    setTimeout(() => {
                        that.isShowAlert = false;
                        that.loginMessage = '';
                    },4500);
                } else if (ret.data.msg === "something error") {
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'error';
                    that.loginMessage = 'Something error, please try again later.';
                    setTimeout(() => {
                        that.isShowProgress = false;
                        that.isShowAlert = false;
                        that.loginMessage = '';
                    },4500);
                } else if (ret.data.msg === "unknown error") {
                    that.isShowProgress = false;
                    that.isShowAlert = true;
                    that.status = 'error';
                    that.loginMessage = 'Unknown error, please try again later.';
                    setTimeout(() => {
                        that.isShowProgress = false;
                        that.isShowAlert = false;
                        that.loginMessage = '';
                    },4500);
                }
            }).catch(err => {
                that.isShowProgress = false;
                that.isShowAlert = true;
                that.status = 'error';
                that.loginMessage = 'Something error.Please check your network and try again.';
                setTimeout(() => {
                    that.isShowAlert = false;
                    that.loginMessage = '';
                },4500);
            })
          }  
      },
      goRegister:function(){
        setTimeout(() => this.$router.push('/register'),150);
      }
    }
  }
</script>

<style scoped>
.login {
  left: 2%;
  margin: 2px;
}
.register{
  left: 2%;
  margin: 2px;
}

</style>
