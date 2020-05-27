<template>
    <div id="problem">
        <v-container fluid>
            <v-alert :type="problemAlertType" v-if="isShowAlert" style="position:absolute;width:100%;">
                {{problemAlert}}
            </v-alert>
            <v-btn @click="$router.push('/index')">返回主页</v-btn>
            <v-card outlined class="mx-auto" style="margin: 0 auto;margin-top:100px;z-index:10" align="center" width="450" min-height="520" ref="main">
                <v-progress-linear indeterminate color="green" absolute v-if="isShowProgress"></v-progress-linear>
                <v-card flat v-if="!isConfirmed" :disabled="isConfirming">
                    <v-card flat align="center">
                        <h2 style="margin: 20px;margin-top:100px;">您选择的题目信息</h2>
                        <h3 style="margin: 20px;margin-top:40px;">语言：{{problemStatus.language}}</h3>
                        <h3 style="margin: 20px;margin-top:40px;">难度：{{problemStatus.difficulty}}</h3>
                        <h3 style="margin: 20px;margin-top:40px;">题目数量：{{problemStatus.problemNumber}}</h3>
                    </v-card>
                    <v-btn @click="confirm" width="200" style="margin: 30px;">开始答题</v-btn>
                </v-card>
                <v-card flat :style="isShowProblemZone" :disabled="isLoading">
                
                    <v-card elevation="1" min-height="180" style="margin: 10px;margin-top:20px;">{{problemContent}}</v-card>

                    <v-card elevation="1" min-height="220" style="margin: 10px" ref="answeringZone"></v-card>

                    <v-container>
                        <v-row align-content="end" style="margin: 10px">
                            <v-btn id="previous" color="success" @click="getProblem(-1)">上一题</v-btn>
                            <v-spacer></v-spacer>
                            <v-card flat style="margin-top:6px;">{{problemNo}}/{{problemNumber}}</v-card>
                            <v-spacer></v-spacer>
                            <v-btn id="forward" color="success" @click="getProblem(1)" v-if="problemNo < problemNumber">下一题</v-btn>
                            <v-btn color="blue" @click="showDialog" v-else>提交</v-btn>
                            <v-dialog v-model="isSubmit" max-width="490">
                                <v-card>
                                    <v-card-title class="headline">确定提交答案？</v-card-title>

                                    <v-card-text>{{submitText}}</v-card-text>

                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="green darken-1" text @click="isSubmit = false">取消</v-btn>

                                        <v-btn color="green darken-1" text @click="submit">确认提交</v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                            <v-dialog v-model="isSubmitSuccess" max-width="490">
                                <v-card>
                                    <v-card-title class="headline">提交结果</v-card-title>

                                    <v-card-text>{{submitSuccessText}}</v-card-text>

                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="green darken-1" text @click="restart">再来一次</v-btn>

                                        <v-btn color="green darken-1" text @click="backToIndex">返回主页</v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-row>
                    </v-container>
                </v-card>
            </v-card>
        </v-container>        
    </div>
</template>

<script>
import store from '../store/index'
import SelectProblem from './render/SelectProblem'
import OtherProblem from './render/OtherProblem'
import Vue from 'vue'
export default {
    name: 'Problem',
    data: () => ({
        isConfirmed:false,
        isConfirming:false,
        isLoading:false,
        isShowProgress:false,
        isShowAlert:false,
        isShowProblemZone: {display:"none"},
        problemAlert:"",
        problemContent:"",
        problemNo:0,
        problemNumber:store.state.problem.problemNumber || sessionStorage.getItem("problemNumber") || 0,
        problemStatus:{
            language:sessionStorage.getItem("language") || store.state.problem.language || "",
            difficulty:sessionStorage.getItem("difficulty") || store.state.problem.difficulty || "",
            problemNumber:sessionStorage.getItem("problemNumber") || store.state.problem.problemNumber || ""
        },
        problemAlertType:"error",
        comp:{},
        submitText:"",
        submitSuccessText:"",
        isSubmit:false,
        isSubmitSuccess:false,
        currentProblemType:""
    }),
    components:{
        SelectProblem,
        OtherProblem
    },
    create:function(){

    },
    methods:{
        confirm:function(){
            let that = this;
            that.isShowProgress = true;
            that.isConfirming = true;
            that.$request.post('/selectProblem', {"user":{"username":that.$store.state.user}, "language":that.language, "difficulty":that.difficulty, "problemNumber":that.problemNumber}).then( ret => {
                if (ret.data.msg == "Not Support") {
                    alert("暂不支持该语言，敬请期待！");
                    that.$router.push('/index').catch( err => console.log(err));
                }
                that.$request.get('/getProblem?flag=1').then( ret => {
                    that.isConfirmed = true;
                    that.isConfirming = false;
                    that.isShowProgress = false;
                    that.isShowProblemZone = {display: "block"};
                    
                    that.comp = that.generateProblem(ret);
                    that.comp.selected = Infinity;
                    that.problemNo++;
                    console.log(that.comp);
                }).catch( err => {
                    that.isShowProgress = false;
                    that.isConfirming = false;
                    that.isShowAlert = true;
                    that.problemAlert = "Something error.Please check your network and try again."
                    console.log(err);
                    setTimeout(() => {that.isShowAlert = false},2500);
                })
          }).catch( err => {
                that.isShowProgress = false;
                that.isButtonLoading = false;
                that.isShowAlert = true;
                that.selectResult = "Something error.Please check your network and try again.";
                setTimeout(() => that.isShowAlert = false,2500);
                console.log(err);
          });
            
            
        },
        getProblem:function(flag){
            let that = this;
            if (flag == -1 && that.problemNo == 1) return;
            else {
                that.isShowProgress = true;
                that.isLoading = true;
            }
            console.log("that.comp.selected:" + that.comp.selected);
            that.$store.state.answer[that.problemNo] = that.comp.selected >= 0 ? that.comp.selected : Infinity;
            if ((flag == 1 && that.problemNo <= that.problemNumber) || (flag == -1 && that.problemNo > 1)){
                that.$request.get('/getProblem?flag=' + flag).then( ret => {
                    that.comp = that.generateProblem(ret);
                    flag == 1 ? that.problemNo++ : that.problemNo--;
                    if (ret.data.problemType == "select"){
                        that.currentProblemType = "select";
                        that.$store.state.answer[that.problemNo] >= 0 ? that.comp.selected = that.$store.state.answer[that.problemNo] : that.comp.selected = Infinity;
                    } else if (ret.data.problemType == "other"){
                        that.currentProblemType = "other";
                        that.$store.state.answer[that.problemNo] >= 0 ? that.comp.answer = that.$store.state.answer[that.problemNo] : that.comp.answer = "";
                    }
                    that.isShowProgress = false;
                    that.isLoading = false;
                }).catch( err => {
                    that.isShowProgress = false;
                    that.isLoading = false;
                    that.isShowAlert = true;
                    that.problemAlert = "Something error.Please check your network and try again."
                    console.log(err);
                    setTimeout(() => {that.isShowAlert = false},2500);
                })
            }
        },
        generateProblem:function(ret){  // testing
            let that = this, comp = {};
            that.$refs.answeringZone.$el.innerHTML = "";
            let div = document.createElement('div');
            div.setAttribute('id','answeringZone');
            that.$refs.answeringZone.$el.appendChild(div);
            if (ret.data.problemType == "select" || ret.data.problemType == "multi"){
                let options = [];
                ret.data.options = JSON.parse(ret.data.options);
                for (let value in ret.data.options){
                    options.push({content:ret.data.options[value], value:value});
                }   
                that.problemContent = ret.data.problemContent;
                let Action = Vue.extend(SelectProblem);
                comp = new Action({
                    propsData:{
                        options:options,  // a list
                        isMultiple:ret.data.problemType == "select" ? false : true // true or false
                    },
                    attr:{

                    }
                }).$mount("#answeringZone");
            } else if (ret.data.problemType == "other"){
                that.problemContent = ret.data.problemContent;
                let Action = Vue.extend(OtherProblem);
                comp = new Action({}).$mount("answeringZone");
            }
            
            return comp;
        },
        showDialog:function(){
            let that = this;
            let unfinished = [];
            console.log("that.comp.selected:" + that.comp.selected);
            that.$store.state.answer[that.problemNo] = that.comp.selected;
            for(let i in that.$store.state.answer) {
                if (that.$store.state.answer[i] === Infinity) unfinished.push(i + 1);
            }
            if (unfinished.length == 0) that.submitText = "确定提交吗？";
            else that.submitText = "您尚有" + unfinished.length + "题未完成，分别为：" + unfinished.toString() + "，确定要提交吗？";
            that.isSubmit = true;
        },
        submit:function(){
            let that = this;
            that.isShowProgress = true;
            that.isLoading = true;
            that.isSubmit = false;
            let answer = [];
            
            console.log(that.$store.state.answer);
            for(let i in that.$store.state.answer){
                answer.push(that.$store.state.answer[i]);
            }
            console.log("answer:" + answer);
            that.$request.post('/submit',answer).then( ret => {
                that.problemAlertType = "success";
                that.isShowProgress = false;
                that.isLoading = false;
                that.isShowAlert = true;
                that.problemAlert = "提交成功！";
                that.submitSuccessText = "提交成功，您的分数为：" + ret.data.score * (100 / that.problemNumber) + "，您可选择再来一次或返回主页";
                that.isSubmitSuccess = true;
                setTimeout(() => {that.isShowAlert = false},2500);
            }).catch( err => {
                that.isShowProgress = false;
                that.isLoading = false;
                that.isShowAlert = true;
                that.problemAlert = "Something error.Please check your network and try again.";
                console.log(err);
                setTimeout(() => {that.isShowAlert = false},2500);
            })
        },
        restart:function(){
            let that = this;
            that.isSubmitSuccess = false;
            that.$store.state.answer = {};
            setTimeout(() => {
                that.$router.go(0).catch( err => console.log(err));
            },200);
        },
        backToIndex:function(){
            let that = this;
            that.isSubmitSuccess = false;
            that.$store.state.answer = {};
            setTimeout(() => {
                that.$router.push('/index').catch( err => console.log(err));
            },200);
        }
    }
}
</script>

<style scoped>

</style>
