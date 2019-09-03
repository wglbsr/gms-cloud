<template>
  <div class="app-container">
    <div style="margin: 10px 10px 10px 0px;">
      <el-input size="mini" style="width: 250px;" v-model="keyword" placeholder="请输入搜索内容"></el-input>
      <el-button type="primary" size="mini" icon="el-icon-search" v-on:click="fetchData">搜索</el-button>

      <el-button-group style="float: right">
        <el-button type="primary" size="mini" icon="el-icon-plus">添加规则</el-button>
        <el-button type="warning" size="mini" icon="el-icon-edit">编辑规则</el-button>
        <el-button type="danger" size="mini" icon="el-icon-delete">删除规则</el-button>
      </el-button-group>
    </div>
    <el-table
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
  </div>
</template>

<script>
    import {getList} from '@/api/dataRule'
    import OperatorTag from "../coms/OperatorTag";
    import ClassTag from "../coms/ClassTag";

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
                keyword: "",
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
