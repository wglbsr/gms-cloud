<template>
    <div>
        <el-dialog title="关联系统" :visible.sync="dialogVisible" @open="open" @close="close">
            <el-tabs>
                <el-tab-pane label="油机系统">
                    <el-form ref="form" :model="form" label-width="80px">
                        <el-form-item label="所属客户:">
                            <customer-selector :customer-no="gmsUser.customerId"
                                               @change="customerChange"></customer-selector>
                        </el-form-item>
                        <el-form-item label="管理辖区:">
                            <region-cascader :region-ids="" @change="regionChange"></region-cascader>
                        </el-form-item>
                        <el-form-item>
                            <el-button v-if="!gmsUserExist" type="primary" @click="operateGms('create')" size="mini">
                                确定关联
                            </el-button>
                            <el-button v-if="gmsUserExist" type="primary" @click="operateGms('delete')" size="mini">取消关联
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
            regionChange(regionIds) {
            },
            operateGms(operation) {
                this.$http.post("/mid-user/gmsUser/" + operation, qs.stringify(this.gmsUser)).then(res => {
                    if (res.result && res.data) {
                        this.getGmsUser();
                    }
                });
            },
            getGmsUser() {
                this.$http.post("/mid-user/gmsUser/select", qs.stringify({userId: this.userId})).then(res => {
                    if (res.result && res.data) {
                        this.gmsUserExist = true;
                    } else {
                        this.gmsUserExist = false;
                    }
                });
            }
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
                this.userId = userId;
                this.gmsUser.userId = userId;
                this.dialogVisible = true;
                this.query();
            },
        }
    };
</script>
