<template>
    <div>
        <el-dialog :visible.sync="visible" width="400px">
            <el-upload @on-success="onSuccess" :headers="header" :multiple="false"
                       class="upload-demo"
                       drag
                       :action="host+'/base-mongodb/file/upload'"
                       multiple>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                visible: false,
                header: {'auth_token': localStorage.getItem("auth_token")},
                host: window.HOST,
            }
        },
        mounted: function () {
            this.visible = this.dialogVisible;
        },
        props: ['dialogVisible'],
        methods: {
            onSuccess(response, file, fileList) {
                if (response.result && response.data) {
                    this.$emit("onSuccess", response.data);
                }
            },
            showDialog(visible) {
                this.visible = visible;
            },
        }
    }
</script>

<style scoped>

</style>