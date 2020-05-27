<template>
    <v-container fluid>
        <v-textarea label="请输入.." auto-grow clearable v-model="answer"></v-textarea>
    </v-container>
</template>

<script>
export default {
    name: 'OtherProblem',
    data: () => ({
        answer:""
    }),
    create:function(){

    },
    methods:{
        
    }
}
</script>

<style scoped>

</style>
