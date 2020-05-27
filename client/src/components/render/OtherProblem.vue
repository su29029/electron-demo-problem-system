<template>
    <v-container fluid>
        <v-text-field label="请输入.." outlined single-line v-model="answer"></v-text-field>
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
