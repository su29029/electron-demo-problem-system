<template>
    <v-container fluid>
        <v-list rounded dense>
            <v-subheader>请选择..</v-subheader>
            <v-list-item-group v-model="selected" color="blue" :multiple="isMultiple">
                <v-list-item v-for="option in options" :key="option.content">
                    <v-list-item-icon>
                        <v-icon v-text="option.value"></v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                        <v-list-item-title v-text="option.content"></v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
            </v-list-item-group>  
        </v-list>
    </v-container>
</template>

<script>
export default {
    name: 'SelectProblem',
    data: () => ({
        selected:[]
    }),
    props:{
        options:Object,
        isMultiple:Boolean
    },
    create:function(){
        
    },
    methods:{
        
    }
}
</script>

<style scoped>

</style>
