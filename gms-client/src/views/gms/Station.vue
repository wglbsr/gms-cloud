<template>
    <div style="background-color: white;padding: 15px;">
        <div slot="header">
            <el-input size="mini" style="width: 200px;" v-model="keyWord" placeholder="关键字"></el-input>
            <el-button-group style="margin-left: 20px;">
                <el-button size="mini" type="primary" icon="el-icon-search"
                           @click="query">查询
                </el-button>
                <el-button size="mini" type="primary" icon="el-icon-plus"
                           @click="showModifyDialog(false)">新增
                </el-button>
                <el-button size="mini" type="primary" icon="el-icon-plus"
                           @click="showImportDialog(false)">导入
                </el-button>
            </el-button-group>
        </div>
        <el-table :data="stationList" style="width: 100%"
                  stripe highlight-current-row
                  v-loading="$store.state.loading">
            <el-table-column type="expand">
                <template slot-scope="props">
                    <el-form label-position="left">
                        <el-form-item size="mini">
                            <span>{{ props.row.province }}</span>
                            <span>{{ props.row.city }}</span>
                            <span>{{ props.row.district }}</span>
                        </el-form-item>
                        <el-form-item label="类型:" size="mini">
                            <span v-if="props.row.type==1">注入</span>
                            <span v-if="props.row.type==0">自建</span>
                        </el-form-item>
                        <el-form-item label="所属:" size="mini">
                            <span v-if="props.row.carrier==4">移动</span>
                            <span v-if="props.row.carrier==2">联通</span>
                            <span v-if="props.row.carrier==1">电信</span>
                        </el-form-item>
                        <el-form-item label="地区编码:" size="mini">
                            {{props.row.regionId}}
                        </el-form-item>
                        <el-form-item label="客户编号:" size="mini">
                            {{props.row.customerId}}
                        </el-form-item>
                        <el-form-item label="详细地址:" fixed="right" size="mini">
                            <span>{{ props.row.address}}</span>
                        </el-form-item>
                        <el-form-item label="坐标:" fixed="right" size="mini">
                            <span>{{ props.row.longitude?props.row.longitude:'暂无'}}</span>, <span>{{ props.row.latitude?props.row.latitude:'暂无'}}</span>
                        </el-form-item>
                        <el-form-item label="备注:" fixed="right" size="mini">
                            <span>{{ props.row.description?props.row.description:'暂无'}}</span>
                        </el-form-item>
                    </el-form>
                </template>
            </el-table-column>
            <el-table-column prop="code" label="编号"></el-table-column>
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                    <el-button size="mini" type="danger" @click="deleteStation(scope.row.id)">
                        删除
                    </el-button>
                    <el-button size="mini" type="primary" @click="showModifyDialog(true,scope.row.id)">
                        编辑
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

        <el-dialog @open="" :title="editMode?'修改记录':'添加记录'" :visible.sync="dialogVisible">
            <el-form ref="form" :model="targetObject" label-width="80px">
                <el-form-item label="名称:">
                    <el-input type="text" size="mini" v-model="targetObject.name"></el-input>
                </el-form-item>
                <el-form-item label="编号:">
                    <el-input type="text" size="mini" v-model="targetObject.code"></el-input>
                </el-form-item>
                <el-form-item label="所属区域:">
                    <region-cascader :region-id="targetObject.regionId" @change="regionChange"></region-cascader>
                </el-form-item>
                <el-form-item label="所属客户:">
                    <customer-selector :customer-id="targetObject.customerId"
                                       @change="customerChange"></customer-selector>
                </el-form-item>
                <el-form-item label="详细地址:">
                    <el-input type="text" size="mini" v-model="targetObject.address"></el-input>
                </el-form-item>
                <el-form-item label="经度:">
                    <el-input type="text" size="mini" v-model="targetObject.longitude"></el-input>
                </el-form-item>
                <el-form-item label="纬度:">
                    <el-input type="text" size="mini" v-model="targetObject.latitude"></el-input>
                </el-form-item>
                <el-form-item label="备注:">
                    <el-input type="textarea" size="mini" v-model="targetObject.description"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button v-if="!editMode" type="primary" @click="addOrModify(editMode)" size="mini">创建
                    </el-button>
                    <el-button v-if="editMode" type="primary" @click="addOrModify(editMode)" size="mini">修改
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog title="文件导入" :visible="importDataWindowVisible" class="station-import-dialog" width="400px"
                   @close="clearImportWindowDialogData">
            <el-upload
                    :headers="header"
                    class="upload-station"
                    ref="upload"
                    drag
                    :action="uploadUri"
                    :data="uploadParams"
                    accept="xlsx"
                    :before-upload="beforeUploadFile"
                    :on-success="uploadSuccess"
                    :on-error="uploadError"
            >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
        </el-dialog>
    </div>
</template>

<script>
    import qs from 'qs'
    import RegionCascader from '../coms/RegionCascader'
    import CustomerSelector from '../coms/CustomerSelector'

    export default {
        data() {
            return {
                token: localStorage.getItem("auth_token"),
                header: {'auth_token': localStorage.getItem("auth_token")},
                uploadParams: {},
                uploadUri: "http://localhost:8010/base-mongodb/file/upload",
                importDataWindowVisible: false,
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
                stationList: [],
                editMode: false,
                pageNum: 1,
                pageSize: 20,
                totalNum: 0,
            };
        },
        components: {RegionCascader, CustomerSelector},
        mounted() {
            this.query();
        },
        methods: {
            clearImportWindowDialogData() {

            },
            beforeUploadFile: function (file) {
                let fileName = file.name;
            },
            uploadSuccess: function (response, file, fileList) {
                let that = this;
                if (response.result && response.data) {
                    let fileId = response.data;
                    this.$http.post("/biz-g1/station/import", qs.stringify({
                        fileId: fileId,
                        suffix: ".xlsx"
                    })).then(res => {
                        if (res.data.result) {
                            let errorList = res.data.data;
                            if (errorList != null && errorList.length > 0) {
                                alert("部分数据导入成功,第" + JSON.stringify(errorList) + "条数据存在问题!");
                            } else {
                                this.$message.success("操作成功!");
                            }
                            that.importDataWindowVisible = false;
                        }
                        this.query();
                    });
                } else {
                    this.$message.error("操作失败,请检查文件类型!");
                }
            },
            uploadError: function (response, file, fileList) {
                this.$message.error("操作失败,请检查文件类型!");
            },
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
            showImportDialog() {
                this.importDataWindowVisible = true;
            },
            showModifyDialog(editMode, id) {
                let that = this;
                this.editMode = editMode;
                if (editMode) {
                    this.queryById(id, function () {
                        that.dialogVisible = true;
                    });
                } else {
                    this.dialogVisible = true;
                }
            },
            modify(id) {

            },
            query() {
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, keyWord: this.keyWord};
                this.$http.post("/biz-g1/station/select", qs.stringify(params)).then(res => {
                    if (res.data.result && res.data.data) {
                        this.stationList = res.data.data;
                        this.pageNum = res.data.pageNum;
                        this.pageSize = res.data.pageSize;
                        this.totalNum = res.data.totalNum;
                    }
                });
            },
            queryById(id, callback) {
                if (!!id) {
                    this.$http.post("/biz-g1/station/select/" + id).then(res => {
                        if (res.data.result && res.data.data) {
                            this.targetObject = res.data.data;
                            callback && callback();
                        }
                    });
                }
            },
            addOrModify(modify) {
                let operation = modify ? "modify" : "create";
                this.$http.post("/biz-g1/station/" + operation, this.targetObject).then(res => {
                    if (res.data.result && res.data.data) {
                        this.$message.success("操作成功!");
                        this.dialogVisible = false;
                    }
                    this.query();
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

<style scoped>
</style>