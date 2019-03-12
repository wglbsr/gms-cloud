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
                        <el-tag v-if="scope.row.locked==1" :type="'danger'">已锁定</el-tag>
                        <el-tag v-else :type="'success'">正常</el-tag>
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
                    <!--<template slot-scope="scope">-->
                        <!--<el-button type="text" size="mini" @click="openRelateRoleDialog(scope.row.id)">角色关联-->
                        <!--</el-button>-->
                    <!--</template>-->
                    <template slot-scope="scope">
                        <el-button type="text" size="mini" @click="openRelateSystemDialog(scope.row.id)">系统关联
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
        <el-dialog>
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="用户名:">
                    <el-input v-model="userInfo.username" type="text" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="密码:">
                    <el-input v-model="userInfo.password" type="text" size="mini"></el-input>
                </el-form-item>
                <el-select v-model="timeout" placeholder="级别" @change="resetTimeout" :disabled="!autoRefresh"
                           size="mini"
                           style="width:100px;">
                    <el-option
                            v-for="item in levelOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <el-form-item>
                    <el-button type="primary" @click="changeUserInfo" size="mini">创建</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>
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
                levelOptions: [
                    {value: 10, label: "10"},
                    {value: 20, label: "20"},
                    {value: 30, label: "30"},
                    {value: 40, label: "40"},
                    {value: 50, label: "50"},
                    {value: 60, label: "60"},
                    {value: 90, label: "90"}],
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
            openRelateSystemDialog(userId) {
                this.$refs.relateRoleDialog.showDialog(userId);
            },
            onSelectionChange(rows) {
                this.selectedRows = rows.map(item => item.userId);
            }
        }
    };
</script>
