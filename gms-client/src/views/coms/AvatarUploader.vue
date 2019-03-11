<template>
    <div>
        <el-upload
                class="avatar-uploader"
                :headers="header"
                :action="host+'/base-mongodb/file/upload'"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload">
            <img v-if="avatarUrl" :src="host+avatarUrl+'?auth_token='+token" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                url:'',
                token: localStorage.getItem("auth_token"),
                header: {'auth_token': localStorage.getItem("auth_token")},
                host: window.HOST,
            }
        },
        mounted: function () {
            // this.url = this.avatarUrl;
        },
        props: ['avatarUrl'],
        methods: {
            handleAvatarSuccess(res, file) {
                if (res.result && res.data) {
                    this.$emit('avatarUploaded', res.data);
                }
            },
            beforeAvatarUpload(file) {
                this.$emit('beforeUpload', file);
            },
        },
    }
</script>

<style scoped>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 128px;
        height: 128px;
        line-height: 128px;
        text-align: center;
    }

    .avatar {
        width: 128px;
        height: 128px;
        display: block;
    }
</style>