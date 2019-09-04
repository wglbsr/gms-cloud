<template>
  <el-tag :type="tagTypeList[factorCalcType]">{{factorCalcType?operatorTypeList[factorCalcType]:'无'}}</el-tag>
</template>

<script>
  import {operatorOptions} from "../../data/options";

  export default {
        name: "OperatorTag",
        props: ['factorCalcType'],
        data() {
            return {
                operatorTypeList: operatorOptions,
                tagTypeList: {1: "warning", 2: "info", 3: "danger", 4: "success"},

            }
        },
        methods: {},
    }
</script>

<style scoped>

</style>
