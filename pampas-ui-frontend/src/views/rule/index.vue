<template>
  <div class="app-container">
    <el-form :inline="true" :model="form" size="medium" class="demo-form-inline">
      <el-form-item label="规则命名">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="服务名">
        <el-input v-model="form.service"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doQueryAction()">查询</el-button>
        <el-button type="" @click="doAddRule()">添加路由规则</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableData"
      size="medium"
      header-row-class-name="tb_header"
      stripe
      style="width: 100%;margin-top: 10px">
      <el-table-column
        prop="name"
        label="路由规则名称"
        width="240">
      </el-table-column>
      <el-table-column
        prop="mapping_host"
        label="匹配HOST头"
        width="240">
      </el-table-column>

      <el-table-column
        prop="insert_time"
        label="添加时间"
        width="160"/>
      <el-table-column
        prop="remark"
        label="备注"
      >
      </el-table-column>
      <el-table-column
        prop=""
        label=""
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="120">
        <template slot-scope="scope">
          <el-button @click="doViewInfo(scope.row)" type="text" size="small">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination
        v-show="total_count>0"
        layout="sizes, prev, pager, next, total"
        :page-size="page_size"
        :total="total_count"
        :current-page="page_num"
        :page-sizes="[10,20,50,100]"
        @size-change="handleSizeChange"
        @current-change="doChangePage">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import {get_rule_list} from '@/api/route-rule'

  export default {
    name: "rule-index",
    data() {
      return {
        page_num: 1,
        total_count: 0,
        listLoading: false,
        form: {
          name: '',
          service: '',
          status: undefined,
        },
        tableData: [],
        services: [],
      }
    },
    created() {
      this.doQueryAction()
    },
    computed: {
      page_size: function () {
        return this.$store.state.app.page_size
      }
    },
    methods: {
      doQueryAction() {
        this.page_num = 1
        this.doQuery();
      },

      doQuery() {
        this.listLoading = true
        get_rule_list(this.form.name, this.form.status, this.page_num, this.page_size).then(response => {
          this.tableData = response.data
          this.total_count = response.count
          this.listLoading = false
        })
      },
      doAddRule() {
        this.$router.push({path: '/rule/add'})
      },
      doChangePage(cur_page_num) {
        this.page_num = cur_page_num
        this.doQuery()
      },
      handleSizeChange(size) {
        this.$store.dispatch('ChangePageSize', size);
        this.page_num = 1
        this.doQuery();
      },
      doViewInfo(row) {
        this.$router.push({path: '/rule/edit', query: {id: row.id}})
      },
    }
  }
</script>

<style scoped>

</style>
