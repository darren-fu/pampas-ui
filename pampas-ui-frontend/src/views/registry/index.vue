<template>
  <div class="app-container">
    <el-form :inline="true" :model="form" size="medium" class="demo-form-inline">
      <el-form-item label="名称">
        <el-input v-model="form.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="doQueryAction()">查询</el-button>
        <el-button type="" @click="doAddRegistry()">添加注册中心</el-button>

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
        prop="id"
        label="ID"
        width="80">
      </el-table-column>
      <el-table-column
        prop="name"
        label="名称"
        width="200">
      </el-table-column>
      <el-table-column
        prop="type"
        label="类型"
        width="100">
      </el-table-column>
      <el-table-column
        prop="address"
        label="地址"
        width="300">
      </el-table-column>
      <el-table-column
        prop="insert_time"
        label="添加时间"
        width="200">
      </el-table-column>

      <el-table-column
        prop=""
        label=""
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="doViewInfo(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="deDelete(scope.row)" type="text" size="small">删除</el-button>
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
  import {delete_registry, get_registry_list} from "@/api/registry"

  export default {
    name: "registry-index",
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
        get_registry_list(this.form, this.page_num, this.page_size).then(response => {
          this.tableData = response.data
          this.total_count = response.count
          this.listLoading = false
        })
      },
      doAddRegistry() {
        this.$router.push({path: '/service/registry/add'})
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
        this.$router.push({path: '/service/registry/edit', query: {id: row.id}})
      },
      deDelete(row) {
        this.$confirm('确认删除？')
          .then(_ => {
            return delete_registry(row.id)
          })
          .then(resp => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
            this.doQueryAction()
          })
          .catch(e => {
            console.log(e)
          })
      }
    }
  }
</script>

<style>

</style>
