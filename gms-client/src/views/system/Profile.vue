<template>
    <el-card shadow="never" :body-style="{ padding: '20px 20px 20px 20px' }">
        <el-tabs>
            <el-tab-pane label="修改密码">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="原密码:">
                        <el-input v-model="passwordObj.oldPassword" type="password" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码:">
                        <el-input v-model="passwordObj.newPassword" type="password" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码:">
                        <el-input v-model="passwordObj.newPassword1" type="password" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="changePsw" size="mini">修改</el-button>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
            <el-tab-pane label="个人信息">
                <el-form ref="form" :model="form" label-width="80px">
                    <el-form-item label="头像:">
                        <avatar-uploader :avatar-url="userInfo.avatar"
                                         @avatarUploaded="avatarUploaded"></avatar-uploader>
                    </el-form-item>
                    <el-form-item label="名称:">
                        <el-input v-model="userInfo.nickname" type="text" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item label="备注:">
                        <el-input v-model="userInfo.description" type="text" size="mini"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="changeUserInfo" size="mini">修改</el-button>
                    </el-form-item>
                </el-form>
            </el-tab-pane>
            <el-tab-pane label="栏位设置">
                <column-setting></column-setting>
            </el-tab-pane>
        </el-tabs>
    </el-card>
</template>
<style>

</style>
<script>
    import qs from 'qs'
    import UploadDialog from "../coms/UploadDialog"
    import AvatarUploader from "../coms/AvatarUploader"
    import ColumnSetting from "../gms/coms/ColumnSetting"

    export default {
        name: "Profile",
        data() {
            return {
                passwordObj: {
                    newPassword: "",
                    newPassword1: "",
                    oldPassword: ""
                },
                imageUrl: '',
                userInfo: {},
                phone: "",
                form: {},
                radio: "",
                token: "",
                test: true,
                uploadDialogVisible: false,
                host: window.HOST,
            };
        },
        components: {UploadDialog, AvatarUploader, ColumnSetting},
        mounted() {
            this.token = localStorage.getItem("auth_token");
            this.getUserInfo();
        },
        methods: {
            avatarUploaded(fileId) {
                console.log(fileId);
                if (!!fileId) {
                    this.$http.post("/mid-user/user/changeAvatar", qs.stringify({fileId: fileId})).then(res => {
                        if (res.data.result && res.data.data) {
                            this.userInfo.avatar = res.data.data;
                        }
                    });
                }

            },
            getUserInfo() {
                this.$http.post("/mid-user/user/userInfo").then(res => {
                    if (res.data.result && res.data.data) {
                        this.userInfo = res.data.data;
                        console.log(this.userInfo.avatar);
                    }
                });
            },
            changeUserInfo() {
                this.$http.post("/mid-user/user/changeInfo", this.userInfo).then(res => {
                    if (res.data.result && res.data.data) {
                        this.userInfo = res.data.data;
                    }
                });
            },
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
                this.$http.post("/mid-user/user/changePsw", qs.stringify(this.passwordObj)).then(res => {
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
