<template>
    <div style="width: 100%;">
        <el-row :gutter="20" style="text-align: center">显示栏目选择</el-row>
        <el-row :gutter="20">
            <el-col :span="3">
                <el-checkbox v-model="column.machNameColumnShow">基站名称</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.offlineTypeColumnShow">离线原因</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.lankPerColumnShow">剩余油量</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.stCurrentColumnShow">输出电流</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.sysModeColumnShow">当前模式</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.startVoColumnShow">启动电压</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.outputVoltageColumnShow">输出电压</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.alVoltageColumnShow">电池电压</el-checkbox>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="3">
                <el-checkbox v-model="column.activatedColumnShow">已激活</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.loadModeTimeColumnShow">带载</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.temperatureColumnShow">电机温度</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.maintainTimeColumnShow">保养时间</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.stStateColumnShow">联网状态</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.cityElectricityColumnShow">市电状态</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox v-model="column.machTypeColumnShow">油机类型</el-checkbox>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "ColumnSetting",
        data() {
            return {
                column: {
                    machNameColumnShow: true,
                    offlineTypeColumnShow: true,
                    lankPerColumnShow: true,
                    stCurrentColumnShow: true,
                    sysModeColumnShow: true,
                    startVoColumnShow: true,
                    outputVoltageColumnShow: true,
                    alVoltageColumnShow: true,
                    activatedColumnShow: true,
                    loadModeTimeColumnShow: true,
                    temperatureColumnShow: true,
                    stStateColumnShow: true,
                    cityElectricityColumnShow: true,
                    machTypeColumnShow: true,
                    maintainTimeColumnShow: true,
                }
            }
        },
        mounted: function () {
            this.getMyColumnSetting();
        },
        methods: {
            getMyColumnSetting() {
                this.$http.get("/mid-user/setting/column").then(res => {
                    console.log(res);
                    if (res.data.result) {
                        if (res.data.data != 1) {

                        } else {
                            this.createMyColumnSetting();
                        }

                    }
                }).catch(error => {

                })
            },
            updateMyColumnSetting() {
                this.$http.put("/mid-user/setting/column", {setting: "123123123"}).then(res => {
                    console.log(res);
                    if (res.result && res.data) {

                    }
                }).catch(error => {

                })
            },
            createMyColumnSetting() {
                this.$http.post("/mid-user/setting/column", JSON.stringify({"setting": "123123123"})).then(res => {
                    console.log(res);
                    if (res.result && res.data) {

                    }
                }).catch(error => {

                })
            }


        }
    }
</script>

<style scoped>

</style>