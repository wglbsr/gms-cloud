<template>
    <el-card shadow="never" :body-style="{ padding: '20px 20px 20px 20px' }">
        <el-tabs>
            <el-tab-pane label="修改密码">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="原密码:">
                        <el-input v-model="passwordObj.oldPassword" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码:">
                        <el-input v-model="passwordObj.newPassword" type="password"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码:">
                        <el-input v-model="passwordObj.newPassword1" type="password"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="changePsw">更新</el-button>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
            <el-tab-pane label="通知管理">
                <el-row type="flex" justify="center">
                    <el-col :span="2">文章推送</el-col>
                    <el-col :span="1">
                        <el-switch></el-switch>
                    </el-col>
                    <el-col :span="2" :offset="4">支付推送</el-col>
                    <el-col :span="1">
                        <el-switch></el-switch>
                    </el-col>
                    <el-col :span="2" :offset="4">订单推送</el-col>
                    <el-col :span="1">
                        <el-switch></el-switch>
                    </el-col>
                    <el-col :span="2" :offset="4">物流推送</el-col>
                    <el-col :span="1">
                        <el-switch></el-switch>
                    </el-col>
                </el-row>
            </el-tab-pane>
        </el-tabs>
    </el-card>
</template>
<script>
    import qs from 'qs'

    export default {
        name: "Profile",
        data() {
            return {
                passwordObj: {
                    newPassword: "",
                    newPassword1: "",
                    oldPassword: ""
                },
                phone: "",
                form: {},
                radio: "",
                test: true
            };
        },
        mounted() {
        },
        methods: {
            changePsw() {
                if (!this.passwordObj.newPassword || !this.passwordObj.newPassword1) {
                    this.$message.error("请输入正确的密码!");
                    return;
                }
                if (this.passwordObj.newPassword != this.passwordObj.newPassword1) {
                    this.$message.error("两次密码输入不一致!");
                    return;
                }
                let size = 6;
                if (this.passwordObj.newPassword.length < size) {
                    this.$message.error("密码长度不能小于" + size + "位!");
                    return;
                }
                this.$http.post("/service-user/user/changePsw", qs.stringify(this.passwordObj)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("修改成功!");
                        this.passwordObj = {
                            newPassword: "",
                            newPassword1: "",
                            oldPassword: ""
                        };
                    } else {
                        this.$message.error("修改失败,请检查原密码!");
                    }
                });
            },
        }
    };
</script>
