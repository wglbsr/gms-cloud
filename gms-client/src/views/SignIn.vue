<template>
    <div class="login">
        <s-s-o-dialog @afterLogin="afterLogin" :dialog-visible="dialogVisible"></s-s-o-dialog>
    </div>
</template>
<style>
    .login {
        background: url(../assets/bg.png) no-repeat scroll center center / cover;
        background-size: 100% 100%;
        width: 100%;
        height: 100%;
        position: fixed;
    }
</style>
<script>
    import SSODialog from './coms/SSODialog'

    export default {
        name: "Login",
        data() {
            return {
                dialogVisible: true,
            };
        },
        components: {SSODialog},
        mounted: function () {
        },
        methods: {
            afterLogin: function (e) {
                let token = e;
                if (token && /^[0-9a-z]{32}$/.test(token)) {
                    console.log("sign in token:" + token);
                    this.$store.commit("setToken", token);
                    this.dialogVisible = false;
                    this.$router.push({path: "/dashboard"});
                }
            },
        }
    };
</script>
