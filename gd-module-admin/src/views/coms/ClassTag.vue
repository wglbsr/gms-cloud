<template>
  <el-tag :type="tagTypeList[classType]">{{classType||classType==0?classTypeList[classType]:'无'}}</el-tag>
</template>

<script>
    import {classOptions} from "../../data/options";

    export default {
        name: "ClassTag",
        props: ['classType'],
        data() {
            return {
                classTypeList: classOptions,
                tagTypeList: {0: "success", 1: "warning", 2: "info", 3: "danger"},
            }
        },
        methods: {},
    }
</script>

<style scoped>

</style>
