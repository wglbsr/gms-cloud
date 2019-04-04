<template>
    <el-card style="width: 100%;">
        <div slot="header">显示栏目选择</div>
        <el-row :gutter="20">
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.machNameColumnShow" border>基站名称
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.offlineTypeColumnShow" border>离线原因
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.lankPerColumnShow" border>剩余油量</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.stCurrentColumnShow" border>输出电流
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.sysModeColumnShow" border>当前模式</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.startVoColumnShow" border>启动电压</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.outputVoltageColumnShow" border>输出电压
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.alVoltageColumnShow" border>电池电压
                </el-checkbox>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.activatedColumnShow" border>已激活
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.loadModeTimeColumnShow" border>带载
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.temperatureColumnShow" border>电机温度
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.maintainTimeColumnShow" border>保养时间
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.stStateColumnShow" border>联网状态</el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.cityElectricityColumnShow" border>市电状态
                </el-checkbox>
            </el-col>
            <el-col :span="3">
                <el-checkbox size="mini" @change="onChange" v-model="column.machTypeColumnShow" border>油机类型
                </el-checkbox>
            </el-col>
        </el-row>


    </el-card>
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
            onChange(val) {
                this.updateMyColumnSetting();
            },
            getMyColumnSetting() {
                let that = this;
                this.$http.get("/mid-user/setting/column").then(res => {
                    if (res.data.result) {
                        if (!res.data.data) {
                            this.createMyColumnSetting();
                        } else {
                            let temp = JSON.parse(res.data.data);
                            delete temp._id;
                            this.column = temp;
                        }
                    }
                }).catch(error => {

                })
            },
            updateMyColumnSetting() {
                let that = this;
                this.$http.put("/mid-user/setting/column", that.column).then(res => {
                    if (res.data.result && res.data.data) {
                        that.getMyColumnSetting();
                    }
                }).catch(error => {

                })
            },
            createMyColumnSetting() {
                let that = this;
                this.$http.post('/mid-user/setting/column', that.column).then(res => {
                    if (res.data.result && res.data.data) {

                    }
                }).catch(error => {

                })
            }


        }
    }
</script>

<style scoped>
    .el-row {
        margin-bottom: 20px;
    }
</style>