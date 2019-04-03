<template>
    <div>
    </div>
</template>
<style lang="scss">
    .el-row {
        margin-bottom: 20px;
        &:last-child {
            margin-bottom: 0;
        }
    }
</style>
<script>
    import G2 from "@antv/g2";
    import qs from 'qs'

    export default {
        name: "Dashboard",
        data() {
            return {
                days: 30,
                statisticData: [],
            };
        },
        methods: {
        },
        mounted() {
        }
    };
</script>
