<template>
  <div class="app-container">
    <el-form :inline="true" :model="form" size="mini" class="demo-form-inline">

      <el-form-item label="服务名">

        <el-input v-model="form.service_name"></el-input>

        <!--<el-select v-model="form.service_name" :clearable="true" filterable default-first-option placeholder="请选择">-->
        <!--<el-option-->
        <!--v-for="item in services"-->
        <!--:key="item"-->
        <!--:label="item"-->
        <!--:value="item">-->
        <!--</el-option>-->
        <!--</el-select>-->
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="doQueryAction()">查询</el-button>
        <el-button type="" @click="doAddService()">添加服务</el-button>

      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableData"
      size="mini"
      header-row-class-name="tb_header"
      stripe
      style="width: 100%;margin-top: 10px">
      <el-table-column
        prop="service_name"
        label="服务名称"
        width="320">
      </el-table-column>
      <el-table-column
        prop="remark"
        label="备注"
        width="180">
      </el-table-column>
      <el-table-column
        prop="insert_time"
        label="添加时间"
        width="200">
      </el-table-column>
      <el-table-column
        prop="worker_count"
        label="worker数量"
        width="100">
      </el-table-column>

      <el-table-column
        prop=""
        label=""
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="180">
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
  import {get_service_list} from "@/api/service"

  export default {
    name: "service-index",
    data() {
      return {
        page_num: 1,
        total_count: 0,
        listLoading: false,
        form: {
          service_name: '',
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
        console.log('this.$store.state.app.page_size', this.$store.state.app.page_size);
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
        get_service_list(this.form, this.page_num, this.page_size).then(response => {
          this.tableData = response.data
          this.total_count = response.count
          this.listLoading = false
          console.log(response, response.data);
        })
      },
      doAddService() {
        this.$router.push({path: '/service/add'})
      },
      doChangePage(cur_page_num) {
        this.page_num = cur_page_num
        this.doQuery()
      },
      handleSizeChange(size) {
        // this.page_size = size
        this.$store.dispatch('ChangePageSize', size);
        this.page_num = 1
        this.doQuery();
      },
      doViewInfo(row) {
        this.$router.push({path: '/service/edit', query: {id: row.id}})

      },

    }
  }
</script>

<style>

</style>
