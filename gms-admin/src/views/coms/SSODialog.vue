<template>
    <el-dialog :visible.sync="visible" :close-on-click-modal="false" :modal-append-to-body="false" width="390px" title="登陆" :show-close="false">
        <iframe ref="ssoIframe" style="width: 350px;height: 240px"
                src="http://localhost:8010/mid-user/sso/loginPage?url=www.baidu.com">
        </iframe>
    </el-dialog>
</template>

<script>
    export default {
        data() {
            return {
                visible: true,
                isCollapse: false,
            };
        },
        props: ['dialogVisible'],
        created: function () {
            this.visible = this.dialogVisible;
            let that = this;
            if (window.addEventListener) {
                window.addEventListener("message", that.messageHandler, false);
            } else {
                window.attachEvent('onmessage', that.messageHandler);
            }
        },
        mounted: function () {
        },
        methods: {
            messageHandler(e) {
                let that = this;
                let token = e.data;
                console.log('sso token:' + token);
                that.$emit('afterLogin', token);
            },
        }
    };
</script>

<style scoped>

</style>