<template>
    <div>
        <el-dialog title="编辑权限" :visible.sync="dialogVisible">
            <el-card shadow="never" :body-style="{ padding: '0px' }">
                <div slot="header">
                    <el-input size="mini" style="width: 200px;" v-model="keyWord" placeholder="关键字"></el-input>
                    <el-button size="mini" style="margin-left: 20px;" type="primary" icon="el-icon-search"
                               @click="query">查询
                    </el-button>
                </div>
                <el-table height="450" :data="authList" style="width: 100%"
                          stripe highlight-current-row
                          v-loading="$store.state.loading">
                    <el-table-column prop="authName" label="名称"></el-table-column>
                    <el-table-column prop="description" label="描述"></el-table-column>
                    <el-table-column prop="permission" label="权限"></el-table-column>
                    <el-table-column label="状态" width="120">
                        <template slot-scope="scope">
                            <el-tag v-if="scope.row.activated" :type="'success'">可用</el-tag>
                            <el-tag v-else :type="'danger'">禁用</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="80">
                        <template slot-scope="scope">
                            <el-button type="text" size="mini" :type="scope.row.related?'danger':'success'"
                                       @click="addOrDel(scope.row.related ,scope.row.id)"
                                       :disabled="!scope.row.activated">{{scope.row.related?'移除':'关联'}}
                            </el-button>
                        </template>
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
            </el-card>
        </el-dialog>
    </div>
</template>
<style>


</style>
<script>
    import qs from 'qs'

    export default {
        data() {
            return {
                keyWord: "",
                dialogVisible: false,
                state: null,
                authObj: {
                    authName: "",
                    description: ""
                },
                authList: [],
                editMode: false,
                pageNum: 1,
                pageSize: 5,
                totalNum: 0,
                roleId: 0,
                roleAuthList: [],
            };
        },
        mounted() {

        },
        methods: {
            showDialog(roleId) {
                this.roleId = roleId;
                this.dialogVisible = true;
                this.query();
            },
            queryAuth(tempAuthList) {
                this.$http.post("/auth/selectByRoleId", qs.stringify({roleIdList: [this.roleId].join(",")})).then(res => {
                    if (res.data.result && res.data.data) {
                        this.roleAuthList = res.data.data;
                        for (let key in tempAuthList) {
                            tempAuthList[key].related = this.isContain(tempAuthList[key].id);
                        }
                        this.authList = tempAuthList;
                    }
                });
            },
            isContain(authId) {
                for (let key in this.roleAuthList) {
                    if (authId == this.roleAuthList[key].id) {
                        return true;
                    }
                }
                return false;
            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/auth/select", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.pageNum = res.data.pageNum;
                        this.pageSize = res.data.pageSize;
                        this.totalNum = res.data.totalNum;
                        this.queryAuth(res.data.data);
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
            },
            addOrDel(del, id) {
                let params = {authId: id, roleId: this.roleId};
                let operation = del ? "unRelate" : "relate";
                this.$http.post("/relRoleAuth/" + operation, qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("操作成功!");
                        this.query();
                    }
                });
            }
        }
    };
</script>
