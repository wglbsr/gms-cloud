<template>
    <div>
        <el-dialog title="编辑角色" :visible.sync="dialogVisible" @open="open" @close="close">
            <el-card shadow="never" :body-style="{ padding: '0px' }">
                <div slot="header">
                    <el-input size="mini" style="width: 200px;" v-model="keyWord" placeholder="关键字"></el-input>
                    <el-button size="mini" style="margin-left: 20px;" type="primary" icon="el-icon-search"
                               @click="query">查询
                    </el-button>
                </div>
                <el-table height="450" :data="roleList" style="width: 100%"
                          stripe highlight-current-row
                          v-loading="$store.state.loading">
                    <el-table-column prop="roleName" label="名称"></el-table-column>
                    <el-table-column prop="description" label="描述"></el-table-column>
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
                roleObj: {
                    roleName: "",
                    description: ""
                },
                roleList: [],
                editMode: false,
                pageNum: 1,
                pageSize: 10,
                totalNum: 0,
                userId: 0,
                userRoleList: [],
            };
        },
        mounted() {

        },
        methods: {
            open() {
                this.$emit("open");
            },
            close() {
                this.$emit("close");
            },
            showDialog(userId) {
                this.userId = userId;
                this.dialogVisible = true;
                this.query();
            },
            queryRole(tempRoleList) {
                this.$http.post("/role/selectByUserId", qs.stringify({userId: this.userId})).then(res => {
                    if (res.data.result && res.data.data) {
                        this.userRoleList = res.data.data;
                        for (let key in tempRoleList) {
                            tempRoleList[key].related = this.isContain(tempRoleList[key].id);
                        }
                        this.roleList = tempRoleList;
                    }
                });
            },
            isContain(roleId) {
                for (let key in this.userRoleList) {
                    if (roleId == this.userRoleList[key].id) {
                        return true;
                    }
                }
                return false;
            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/role/select", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.pageNum = res.data.pageNum;
                        this.pageSize = res.data.pageSize;
                        this.totalNum = res.data.totalNum;
                        this.queryRole(res.data.data);
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
                let params = {roleId: id, userId: this.userId};
                let operation = del ? "unRelate" : "relate";
                this.$http.post("/relUserRole/" + operation, qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("操作成功!");
                        this.query();
                    }
                });
            }
        }
    };
</script>
