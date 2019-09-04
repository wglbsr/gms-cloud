<template>
  <div class="app-container">
    <div style="margin: 10px 10px 10px 0px;">
      <el-input size="mini" style="width: 250px;" v-model="keyword" placeholder="请输入搜索内容"></el-input>
      <el-button type="primary" size="mini" icon="el-icon-search" v-on:click="fetchData">搜索</el-button>
      <el-button-group style="float: right">
        <el-button type="primary" size="mini" icon="el-icon-plus" v-on:click="addRule">添加规则
        </el-button>
        <el-button type="warning" size="mini" icon="el-icon-edit" v-on:click="editRule">编辑规则</el-button>
        <el-button type="danger" size="mini" icon="el-icon-delete" v-on:click="deleteRule">删除规则</el-button>
      </el-button-group>
    </div>
    <el-table
      @selection-change="handleSelectionChange"
      @row-dblclick="clickRow"
      stripe
      size="mini"
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        type="selection"
        width="35">
      </el-table-column>
      <el-table-column align="center" label="键" width="165">
        <template slot-scope="scope">
          {{ scope.row.dataKey }}
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column label="通讯ID" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.communicateId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下标" width="60" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.startIndex }}</span>
        </template>
      </el-table-column>
      <el-table-column label="长度" width="60" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.size }}</span>
        </template>
      </el-table-column>
      <el-table-column label="布尔下标" width="80" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.bitIndex?scope.row.bitIndex:'无' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="因数" width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.factor ?scope.row.factor:'无'}}
        </template>
      </el-table-column>
      <el-table-column label="计算方式" width="90" align="center">
        <template slot-scope="scope">
          <operator-tag :factor-calc-type="scope.row.factorCalcType "></operator-tag>
        </template>
      </el-table-column>
      <el-table-column label="因数类型" width="80" align="center">
        <template slot-scope="scope">
          <class-tag :class-type="scope.row.factorClass"></class-tag>
        </template>
      </el-table-column>
      <el-table-column label="获取类型" width="80" align="center">
        <template slot-scope="scope">
          <class-tag :class-type="scope.row.oriClass"></class-tag>
        </template>
      </el-table-column>
      <el-table-column label="结果类型" width="80" align="center">
        <template slot-scope="scope">
          <class-tag :class-type="scope.row.targetClass"></class-tag>
        </template>
      </el-table-column>
      <el-table-column label="前缀" width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.prefix?scope.row.prefix:'无' }}
        </template>
      </el-table-column>
      <el-table-column label="后缀" width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.suffix?scope.row.suffix:'无' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;padding-top: 5px;width: 100%;">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[ 10, 20, 50, 70]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalNum">
      </el-pagination>
    </div>

    <el-dialog
      :visible.sync="editOrAddDialogVisible"
      v-on:closed="editOrAddDialogClosed"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
    >
      <el-form ref="form" :model="dataRuleForm" label-width="110px" size="mini">
        <el-form-item label="(key)键">
          <el-input v-model="dataRuleForm.dataKey"></el-input>
        </el-form-item>
        <el-form-item label="通讯ID(10进制)">
          <el-input v-model="dataRuleForm.communicateId"></el-input>
        </el-form-item>
        <el-form-item label="开始下标">
          <el-input v-model="dataRuleForm.startIndex"></el-input>
        </el-form-item>
        <el-form-item label="布尔下标">
          <el-select v-model="dataRuleForm.bitIndex" size="mini">
            <el-option v-for="item in booleanIndexOption"
                       :key="item.key"
                       :label="item.label"
                       :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="长度">
          <el-input v-model="dataRuleForm.size"></el-input>
        </el-form-item>
        <el-form-item label="因数">
          <el-input v-model="dataRuleForm.factor" v-on:change="factorChange"></el-input>
        </el-form-item>
        <el-form-item label="计算方式">
          <el-select v-model="dataRuleForm.factorCalcType" size="mini">
            <el-option v-for="item in operatorOptions"
                       :key="item.key"
                       :label="item.label"
                       :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="因数类型">
          <el-select v-model="dataRuleForm.factorClass" size="mini">
            <el-option v-for="item in classOptions"
                       :key="item.key"
                       :label="item.label"
                       :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="获取类型">
          <el-select v-model="dataRuleForm.oriClass" size="mini">
            <el-option v-for="item in classOptions"
                       :key="item.key"
                       :label="item.label"
                       :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果类型">
          <el-select v-model="dataRuleForm.targetClass" size="mini">
            <el-option v-for="item in classOptions"
                       :key="item.key"
                       :label="item.label"
                       :value="item.key"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="前缀">
          <el-input v-model="dataRuleForm.prefix"></el-input>
        </el-form-item>
        <el-form-item label="后缀">
          <el-input v-model="dataRuleForm.suffix"></el-input>
        </el-form-item>

        <el-form-item label="备注">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入"
            v-model="dataRuleForm.remark">
          </el-input>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="editOrAddDialogVisible=false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
    import {getList, getOne, deleteByKeys, create, update} from '@/api/dataRule'
    import OperatorTag from "../coms/OperatorTag";
    import ClassTag from "../coms/ClassTag";
    import {classOptions2, operatorOptions2,booleanIndexOption} from "../../data/options";

    export default {
        components: {ClassTag, OperatorTag},
        data() {
            return {
                booleanIndexOption:booleanIndexOption,
                classOptions: classOptions2,
                operatorOptions: operatorOptions2,
                editMode: false,
                dataRuleForm: {},
                editOrAddDialogVisible: false,
                keyword: "",
                bitRelVisible: false,
                factorRelVisible: false,
                list: null,
                pageNum: 1,
                pageSize: 20,
                totalNum: 0,
                listLoading: true,
                rows: [],
            }
        },
        created() {
            this.fetchData()
        },
        methods: {
            clickRow(row) {
                this.dataRuleForm = row;
                this.editMode = true;
                this.editOrAddDialogVisible = true;
            },
            handleSelectionChange(rows) {
                this.rows = rows;
            },
            editOrAddDialogClosed() {
                this.dataRuleForm = {};
            },
            bitIndexChange(val) {
                this.bitRelVisible = (val >= 0);
            },
            factorChange(val) {
                this.factorRelVisible = !!val;
            },
            onSubmit() {
                if (this.editMode) {
                    update(this.dataRuleForm).then(res => {
                        if (res.result) {
                            this.fetchData();
                        }
                    }).catch(error => {

                    });
                } else {
                    create(this.dataRuleForm).then(res => {
                        if (res.result) {
                            this.fetchData();
                        }
                    }).catch(error => {

                    });
                }
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                this.fetchData();
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.fetchData();
            },
            deleteRule() {
                this.$confirm('此操作将不可撤销, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let keys = [];
                    this.rows.forEach((val, index) => {
                        keys.push(val.dataKey);
                    });
                    deleteByKeys(keys);
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            editRule() {
                if (this.rows.length == 0) {
                    this.$message('请先选择一条记录!');
                }
                if (this.rows.length > 1) {
                    this.$message('只能选择一条记录进行编辑!');
                }
                if (this.rows.length == 1) {
                    this.editMode = true;
                    this.editOrAddDialogVisible = true;
                    this.dataRuleForm = this.rows[0];
                }
            },
            addRule() {
                this.dataRuleForm = {};
                this.editMode = false;
                this.editOrAddDialogVisible = true;
            },
            fetchData() {
                this.listLoading = true;
                let params = {pageNum: this.pageNum, pageSize: this.pageSize, otherCons: {}, keyword: this.keyword};
                getList(params).then(response => {
                    if (response.result) {
                        this.list = response.data;
                        this.pageNum = response.pageNum;
                        this.pageSize = response.pageSize;
                        this.totalNum = response.totalNum;
                    }
                    this.listLoading = false;
                }).catch(error => {
                    console.log(error);
                });
            }
        }
    }
</script>
