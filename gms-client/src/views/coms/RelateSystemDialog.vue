<template>
    <div>
        <el-dialog title="关联系统" :visible.sync="dialogVisible" @open="open" @close="close">
            <el-tabs>
                <el-tab-pane label="油机系统">
                    <el-form ref="form" :model="gmsUser" label-width="80px">
                        <el-form-item label="所属客户:">
                            <customer-selector :customer-id="gmsUser.customerId"
                                               @change="customerChange"></customer-selector>
                        </el-form-item>
                        <el-form-item label="管理辖区:">
                            <region-cascader :region-id="gmsUser.regionId" @change="regionChange"></region-cascader>
                        </el-form-item>
                        <el-form-item>
                            <el-button v-if="!gmsUserExist" type="primary" @click="operateGms('create')" size="mini">
                                确定关联
                            </el-button>
                            <el-button v-if="gmsUserExist" type="danger" @click="operateGms('delete')" size="mini">取消关联
                            </el-button>
                            <el-button v-if="gmsUserExist" type="primary" @click="operateGms('change')" size="mini">修改
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="云柜系统">
                </el-tab-pane>
            </el-tabs>
        </el-dialog>
    </div>
</template>
<style>


</style>
<script>
    import qs from 'qs';
    import CustomerSelector from './CustomerSelector';
    import RegionCascader from './RegionCascader'

    export default {
        data() {
            return {
                gmsUserExist: false,
                customerOptions: [],
                gmsUser: {regionId: 0, customerId: 0},
                keyWord: "",
                dialogVisible: false,
                state: null,
                roleList: [],
                editMode: false,
                userId: 0,
                userRoleList: [],
            };
        },
        components: {CustomerSelector, RegionCascader},
        mounted() {
        },
        methods: {
            regionChange(regionId) {
                this.gmsUser.regionId = regionId;
            },
            operateGms(operation) {
                this.$http.post("/biz-g1/gmsUser/" + operation, qs.stringify(this.gmsUser)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.getGmsUser();
                        this.$message.success("操作成功!");
                        this.dialogVisible = false;
                    }
                });
            },
            getGmsUser(callback) {
                this.$http.post("/biz-g1/gmsUser/select", qs.stringify({userId: this.userId})).then(res => {
                    if (res.data.result && res.data.data) {
                        this.gmsUser = res.data.data;
                        this.gmsUserExist = true;
                    } else {
                        this.gmsUserExist = false;
                    }
                    callback && callback();
                });
            },
            customerChange(customerId) {
                this.gmsUser.customerId = customerId;
            },
            open() {
                this.$emit("open");
            },
            close() {
                this.$emit("close");
            },
            showDialog(userId) {
                let that = this;
                this.userId = userId;
                this.gmsUser.userId = userId;
                this.getGmsUser(function () {
                    that.dialogVisible = true;
                });
            },
        }
    };
</script>
