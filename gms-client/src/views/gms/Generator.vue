<template>
    <div style="background-color: white;padding: 15px;">
        <div slot="header">
            <el-input size="mini" style="width: 200px;" v-model="keyWord" placeholder="关键字"></el-input>
            <el-button-group style="margin-left: 20px;">
                <el-button size="mini" type="primary" icon="el-icon-search"
                           @click="query">查询
                </el-button>
            </el-button-group>
        </div>
        <el-table :data="dataList" style="width: 100%"
                  stripe highlight-current-row
                  v-loading="$store.state.loading">
            <!--<el-table-column-->
            <!--type="selection"-->
            <!--width="30">-->
            <!--</el-table-column>-->
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="扩展1">
                            <span>{{ props.row.ext1 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                        <el-form-item label="扩展2">
                            <span>{{ props.row.ext2 }}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column align="left" width="40"
                             type="index"
            >
            </el-table-column>
            <el-table-column align="center"
                             prop="stationName"
                             label="基站名称"
            >
            </el-table-column>
            <el-table-column align="center"
                             prop="generatorCode"
                             label="油机编码"
            >
            </el-table-column>
        </el-table>
        <el-pagination align="center"
                       @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="pageNum"
                       :page-sizes="[5,10, 20]"
                       :page-size="pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="totalNum">
        </el-pagination>

    </div>
</template>

<style scoped>
    .demo-table-expand {
        font-size: 0;
    }
    .demo-table-expand label {
        width: 90px;
        color: #99a9bf;
    }
    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0;
        width: 50%;
    }
</style>
<script>
    import qs from 'qs'
    import RegionCascader from '../coms/RegionCascader'
    import CustomerSelector from '../coms/CustomerSelector'

    export default {
        data() {
            return {
                dialogVisible: false,
                targetObject: {
                    customerId: 0,
                    regionId: 0,
                    name: "",
                    latitude: "",
                    longitude: ""
                },
                keyWord: "",
                state: null,
                dataList: [{stationName: "123", generatorCode: "123", ext1: "123", ext2: "123"}],
                editMode: false,
                pageNum: 1,
                pageSize: 20,
                totalNum: 0,
            };
        },
        components: {RegionCascader, CustomerSelector},
        mounted() {
        },
        methods: {
            regionChange(val) {
                this.targetObject.regionId = val;
            },
            customerChange(val) {
                this.targetObject.customerId = val;
            },
            deleteStation(id) {
                this.$confirm('此操作将不可撤销, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post("/biz-g1/station/delete/" + id).then(res => {
                        if (res.data.result && res.data.data) {
                            this.$message.success("操作成功!");
                            this.dialogVisible = false;
                        }
                        this.query();
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    });
                });
            },
            modify(id) {

            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/biz-g1/station/select", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.dataList = res.data.data;
                        this.pageNum = res.data.pageNum;
                        this.pageSize = res.data.pageSize;
                        this.totalNum = res.data.totalNum;
                    }
                });
            },
            handleSizeChange(size) {
                this.pageSize = size;
                this.query();
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.query();
            }
        }
    };
</script>
