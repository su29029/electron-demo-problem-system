<template>
  <div id="index">
    <Navigator id="navigator"></Navigator>
    <!-- <v-container> -->
      <v-alert :type="status" v-if="isShowAlert" style="position:absolute;width:100%;">
        {{selectResult}}
      </v-alert>
    <!-- </v-container> -->
    
    <v-container style="margin-top:5%">
      
      <v-card class="mx-auto" outlined width="400" height="470" :disabled="isButtonLoading">
        <v-progress-linear indeterminate color="green" absolute v-if="isShowProgress"></v-progress-linear>
      
        <v-card-text>
          <p class="text-center" style="margin-top:40px;font-size:22px;">
            请选择
          </p>
        </v-card-text>

        <v-col style="width: 80%;margin:0 auto;">
          <v-row>
            <v-select :items="items.language" label="语言" :disabled="false" style="width:80%;margin-top:10px;" v-model="language" @change="save"></v-select>
          </v-row>
          <v-row>
            <v-select :items="items.difficulty" label="难度" :disabled="false" style="width:80%;margin-top:10px;" v-model="difficulty" @change="save"></v-select>
          </v-row>
          <v-row>
            <v-select :items="items.problemNumber" label="题目数" :disabled="false" style="width:80%;margin-top:10px;" v-model="problemNumber" @change="save"></v-select>
          </v-row>
        </v-col>
        <v-col align="center">
          <v-btn width="150" style="margin-top:10px;" color="primary" @click="start" :loading="isButtonLoading">开始答题</v-btn>
        </v-col>
        
      </v-card>
    </v-container>
    
    
  </div>
</template>

<script>
import Navigator from './Navigator'
export default {
    name:"Index",
    components:{
      Navigator
    },
    data: () => ({
      items:{
        language:["C","C++(暂未开放)","JavaScript","Java(暂未开放)","PHP(暂未开放)","Golang(暂未开放)","Python(暂未开放)","Rust(暂未开放)","Ruby(暂未开放)","C#(暂未开放)"],
        difficulty:["baby","easy","normal","hard"],
        problemNumber:["5","10"]
      },
      isShowProgress:false,
      status:"error",
      isShowAlert:false,
      selectResult:"",
      language:sessionStorage.getItem("language") || "",
      difficulty:sessionStorage.getItem("difficulty") || "",
      problemNumber:sessionStorage.getItem("problemNumber") || "",
      isButtonLoading:false 
    }),
    methods:{
      start:function(){
        let that = this;
        if (!that.$store.state.user) {
          that.$router.push('/login');
          return ;
        }
        if (that.language && that.difficulty && that.problemNumber){
          if (that.language != "C" && that.language != "JavaScript") {
            alert("暂不支持该语言，敬请期待！");
            return ;
          }
          that.$store.state.problem.difficulty = that.difficulty;
          that.$store.state.problem.language = that.language;
          that.$store.state.problem.problemNumber = that.problemNumber;
          that.$router.push('/problem').catch( err => console.log(err));
        } else {
          that.isShowAlert = true;
          that.selectResult = "Please select all the options.";
          setTimeout(() => that.isShowAlert = false,2500);
        }
      },
      save:function(){
        sessionStorage.setItem("language", this.language);
        sessionStorage.setItem("difficulty", this.difficulty);
        sessionStorage.setItem("problemNumber", this.problemNumber);
      }
    }
}
</script>

<style scoped>

</style>


