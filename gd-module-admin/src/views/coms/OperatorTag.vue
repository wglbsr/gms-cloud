<template>
  <el-tag :type="tagTypeList[factorCalcType]">{{factorCalcType?operatorTypeList[factorCalcType]:'无'}}</el-tag>
</template>

<script>
    export default {
        name: "OperatorTag",
        props: ['factorCalcType'],
        data() {
            return {
                operatorTypeList: {1: "加(+)", 2: "减(-)", 3: "乘(*)", 4: "除(/)"},
                tagTypeList: {1: "warning", 2: "info", 3: "danger", 4: "success"},

            }
        },
        methods: {},
    }
</script>

<style scoped>

</style>
