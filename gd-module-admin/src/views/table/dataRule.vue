<template>
  <div class="app-container">
    <div style="margin: 10px 10px 10px 0px;">
      <el-input size="mini" style="width: 250px;" v-model="keyword" placeholder="请输入搜索内容"></el-input>
      <el-button type="primary" size="mini" icon="el-icon-search" v-on:click="fetchData">搜索</el-button>

      <el-button-group style="float: right">
        <el-button type="primary" size="mini" icon="el-icon-plus" v-on:click="editOrAddDialogVisible=true">添加规则
        </el-button>
        <el-button type="warning" size="mini" icon="el-icon-edit">编辑规则</el-button>
        <el-button type="danger" size="mini" icon="el-icon-delete">删除规则</el-button>
      </el-button-group>
    </div>
    <el-table
      stripe
      size="mini"
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
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
      <!--      <el-table-column label="创建时间" width="90" align="center">-->
      <!--        <template slot-scope="scope">-->
      <!--          {{ scope.row.suffix }}-->
      <!--        </template>-->
      <!--      </el-table-column>-->
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

    <el-dialog :visible="editOrAddDialogVisible">
      <el-form ref="form" :model="dataRuleForm" label-width="80px" size="mini">
        <el-form-item label="(key)键">
          <el-input v-model="dataRuleForm.dataKey"></el-input>
        </el-form-item>
        <el-form-item label="通讯ID(10进制)">
          <el-input v-model="dataRuleForm.communicateId"></el-input>
        </el-form-item>
        <!--        <el-form-item label="活动区域">-->
        <!--          <el-select v-model="dataRuleForm.region" placeholder="请选择活动区域">-->
        <!--            <el-option label="区域一" value="shanghai"></el-option>-->
        <!--            <el-option label="区域二" value="beijing"></el-option>-->
        <!--          </el-select>-->
        <!--        </el-form-item>-->
        <!--          <el-checkbox-group v-model="dataRuleForm.factor">-->
        <!--            <el-checkbox-button label="美食/餐厅线上活动" name="type"></el-checkbox-button>-->
        <!--            <el-checkbox-button label="地推活动" name="type"></el-checkbox-button>-->
        <!--            <el-checkbox-button label="线下主题活动" name="type"></el-checkbox-button>-->
        <!--          </el-checkbox-group>-->
        <el-form-item label="开始下标">
          <el-input v-model="dataRuleForm.startIndex"></el-input>
        </el-form-item>
        <el-form-item label="布尔下标">
          <el-input v-model="dataRuleForm.bitIndex" v-on:change="bitIndexChange"></el-input>
        </el-form-item>
        <el-form-item label="长度" v-if="!bitRelVisible">
          <el-input v-model="dataRuleForm.size"></el-input>
        </el-form-item>

        <el-form-item label="因数" v-if="!bitRelVisible">
          <el-input v-model="dataRuleForm.factor" v-on:change="factorChange"></el-input>
        </el-form-item>
        <el-form-item label="计算方式" v-if="factorRelVisible">
          <el-radio-group v-model="dataRuleForm.factorCalcType" size="mini">
            <el-radio v-for="(item,index) in operatorOptions"
                      :key="index"
                      :label="item"
                      :value="index"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="因数类型" v-if="factorRelVisible">
          <el-radio-group v-model="dataRuleForm.factorClass" size="mini">
            <el-radio v-for="(item,index) in classOptions"
                      :key="index"
                      :label="item"
                      :value="index"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="获取类型" v-if="!bitRelVisible">
          <el-radio-group v-model="dataRuleForm.oriClass" size="mini">
            <el-radio v-for="(item,index) in classOptions"
                      :key="index"
                      :label="item"
                      :value="index"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="结果类型" v-if="!bitRelVisible">
          <el-radio-group v-model="dataRuleForm.targetClass" size="mini">
            <el-radio v-for="(item,index) in classOptions"
                      :key="index"
                      :label="item"
                      :value="index"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="前缀"  v-if="!bitRelVisible">
          <el-input v-model="dataRuleForm.prefix"></el-input>
        </el-form-item>
        <el-form-item label="后缀"  v-if="!bitRelVisible">
          <el-input v-model="dataRuleForm.suffix"></el-input>
        </el-form-item>
        <el-form-item size="large">
          <el-button size="mini" type="primary" @click="onSubmit">立即创建</el-button>
          <el-button size="mini">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
    import {getList} from '@/api/dataRule'
    import OperatorTag from "../coms/OperatorTag";
    import ClassTag from "../coms/ClassTag";
    import {classOptions, operatorOptions} from "../../data/options";

    export default {
        components: {ClassTag, OperatorTag},
        filters: {
            statusFilter(status) {
                const statusMap = {
                    published: 'success',
                    draft: 'gray',
                    deleted: 'danger'
                };
                return statusMap[status]
            }
        },
        comments: {OperatorTag, ClassTag},
        data() {
            return {
                classOptions: classOptions,
                operatorOptions: operatorOptions,
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
                listLoading: true
            }
        },
        created() {
            this.fetchData()
        },
        methods: {
            bitIndexChange(val) {
                this.bitRelVisible = (val >= 0);
            },
            factorChange(val) {
                this.factorRelVisible = !!val;
            },
            onSubmit() {
                this.fetchData();
            },
            handleSizeChange(pageSize) {
                this.pageSize = pageSize;
                this.fetchData();
            },
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.fetchData();
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
