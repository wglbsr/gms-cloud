<template>
  <el-tag :type="tagTypeList[classType]">{{classType||classType==0?classTypeList[classType]:'无'}}</el-tag>
</template>

<script>
    export default {
        name: "ClassTag",
        props: ['classType'],
        data() {
            return {
                classTypeList: {0: "布尔型", 1: "整型", 2: "浮点型", 3: "字符型"},
                tagTypeList: {0: "success", 1: "warning", 2: "info", 3: "danger"},
            }
        },
        methods: {},
    }
</script>

<style scoped>

</style>
