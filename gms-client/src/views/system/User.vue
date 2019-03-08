<template>
    <div>
        <el-card shadow="never" :body-style="{ padding: '0px' }">
            <div slot="header">
                <el-row>
                    <el-col :span="4">
                        <el-input placeholder="关键字" v-model="keyWord" size="mini"></el-input>
                    </el-col>
                    <el-col :span="2" :offset="1">
                        <el-button type="primary" icon="el-icon-search" @click="query" size="mini">查询</el-button>
                    </el-col>
                </el-row>

            </div>
            <el-table max-height="650" :data="userList" style="width: 100%" stripe highlight-current-row
                      v-loading="$store.state.loading" @selection-change="onSelectionChange">
                <el-table-column type="selection" width="55" align="center">
                </el-table-column>
                <el-table-column prop="username" label="用户名"></el-table-column>
                <el-table-column label="手机号码" width="150">
                    <template slot-scope="scope">
                        {{ scope.row.phone }}
                    </template>
                </el-table-column>
                <el-table-column label="状态" width="120">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.activated==1" :type="'success'">已启用</el-tag>
                        <el-tag v-else :type="'danger'">已禁用</el-tag>
                    </template>
                </el-table-column>
                <!--<el-table-column prop="gender" label="性别" width="100"></el-table-column>-->
                <el-table-column label="创建日期" width="160">
                    <template slot-scope="scope">
                        {{ scope.row.createTime | moment('YYYY-MM-DD hh:mm') }}
                    </template>
                </el-table-column>
                <el-table-column label="最后登陆日期" align="center" width="160">
                    <template slot-scope="scope">
                        {{ scope.row.lastLogin | moment('YYYY-MM-DD hh:mm') }}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="190">
                    <template slot-scope="scope">
                        <el-button v-if="(scope.row.laundry)" type="text" size="mini"
                                   @click="openRelateLaundryDialog(scope.row.id)">关联洗衣店
                        </el-button>
                        <el-button type="text" size="mini" @click="openRelateRoleDialog(scope.row.id)">角色关联
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination align="center"
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="pageNum"
                           :page-sizes="[10,20,50]"
                           :page-size="pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="totalNum">
            </el-pagination>
        </el-card>
        <role-dialog ref="relateRoleDialog" @close="query"></role-dialog>

    </div>
</template>
<style>
    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }
</style>
<script>
    import qs from 'qs';
    import RoleDialog from "../coms/RoleDialog"

    export default {
        name: "user",
        data() {
            return {
                state: null,
                dateRange: null,
                selectedRows: [],
                selectedLaundryRows: [],
                pageNum: 1,
                pageSize: 20,
                keyWord: "",
                totalNum: 0,
                userList: [],
                laundryList: []
            };
        },
        components: {
            RoleDialog
        },
        mounted() {
            this.query();
        },
        methods: {
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.query();
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                this.query();
            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/mid-user/admin/allUser", qs.stringify(params)).then(res => {
                    this.userList = res.data.data;
                    this.pageNum = res.data.pageNum;
                    this.pageSize = res.data.pageSize;
                    this.totalNum = res.data.totalNum;
                });
            },
            openRelateRoleDialog(userId) {
                this.$refs.relateRoleDialog.showDialog(userId);
            },
            onSelectionChange(rows) {
                this.selectedRows = rows.map(item => item.userId);
            }
        }
    };
</script>
