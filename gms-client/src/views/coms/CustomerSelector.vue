<template>
    <el-select size="mini" v-model="customerId" placeholder="请选择客户" class="customer-select" @change="selectedCustomer">
        <el-option
                v-for="item in customerListData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.code }}</span>
        </el-option>
    </el-select>
</template>

<script>
    export default {
        name: 'CustomerSelector',
        data() {
            return {
                customerListData: [],
            }
        },
        props: ['customerId'],
        mounted: function () {
            this.getAllCustomer();
        },
        methods: {
            getAllCustomer: function () {
                this.$http.post("/biz-g1/customer/selectAll").then(res => {
                    if (res.body.result && res.body.data) {
                        this.customerListData = res.body.data;
                        this.customerListData.push({
                            id: 0,
                            code: "0",
                            name: "全部"
                        });
                    }
                }).catch(function (res) {
                    this.$message.error("未知错误,请稍后重试!");
                });
            },
            selectedCustomer: function (value) {
                this.$emit("change", value);
            }
        },
    }
</script>

<style scoped>
    .customer-select {

    }
</style>
