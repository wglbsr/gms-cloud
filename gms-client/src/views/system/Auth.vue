<template>
    <div>
        <el-card shadow="never" :body-style="{ padding: '0px' }">
            <div slot="header">
                <el-input size="mini" style="width: 200px;" v-model="keyWord" placeholder="关键字"></el-input>
                <el-button size="mini" style="margin-left: 20px;" type="primary" icon="el-icon-search" @click="query">查询
                </el-button>
                <el-button size="mini" style="margin-left: 20px;" type="primary" icon="el-icon-plus"
                           @click="openEditOrInsertDialog(false)">新增
                </el-button>
            </div>
            <el-table max-height="650" :data="authList" style="width: 100%"
                      @row-dblclick="editAuth"
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
                        <el-button type="text" :type="scope.row.activated?'danger':'primary'" size="mini"
                                   @click="activate(!scope.row.activated,scope.row.id)">
                            {{scope.row.activated?'禁用':'启用'}}
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination align="center"
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="pageNum"
                           :page-sizes="[10, 20, 50]"
                           :page-size="pageSize"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="totalNum">
            </el-pagination>
        </el-card>
        <el-dialog :title="editMode?'修改角色':'新增角色'" :visible.sync="dialogVisible" @close="authObj={}">
            <el-form :model="authObj" :label-position="'right'" label-width="80px">
                <el-form-item label="名称">
                    <el-input v-model="authObj.authName"></el-input>
                </el-form-item>
                <el-form-item label="权限">
                    <el-input v-model="authObj.permission"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input type="textarea" v-model="authObj.description"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="insertOrUpdate">确 定</el-button>
            </div>
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
                pageSize: 20,
                totalNum: 0
            };
        },
        mounted() {
            this.query();
        },
        methods: {
            editAuth(row, event) {
                this.authObj = row;
                this.openEditOrInsertDialog(true);
            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/auth/select", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.authList = res.data.data;
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
            },
            insert() {
                let that = this;
                that.$http.post("/auth/create", that.authObj).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("创建成功!");
                        this.query();
                    }
                    that.dialogVisible = false;
                });
            },
            update() {
                let that = this;
                that.$http.post("/auth/update", that.authObj).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("修改成功!");
                        this.query();
                    }
                    that.dialogVisible = false;
                });
            },
            insertOrUpdate() {
                if (this.editMode) {
                    this.update();
                } else {
                    this.insert();
                }
            },
            openEditOrInsertDialog(editMode) {
                this.dialogVisible = true;
                if (editMode) {
                    this.editMode = true;
                } else {
                    this.editMode = false;
                }
            },
            activate(activate, id) {
                let params = {activate: activate, id: id};
                this.$http.post("/auth/activate", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("操作成功!");
                        this.query();
                    }
                });
            },
        }
    };
</script>
