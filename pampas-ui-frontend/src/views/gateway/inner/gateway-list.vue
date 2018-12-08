<template>
  <div >
    <el-form :inline="true" :model="form" size="medium" class="demo-form-inline">
      <el-form-item label="网关编号">
        <el-input v-model="form.instance_id"></el-input>
      </el-form-item>
      <el-form-item label="网关分组">
        <el-input v-model="form.group"></el-input>
      </el-form-item>
      <el-form-item label="主机名">
        <el-input v-model="form.host_name"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doQueryAction()">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableData"
      size="medium"
      header-row-class-name="tb_header"
      stripe
      style="width: 100%;margin-top: 10px" >
      <el-table-column
        prop="id"
        label="ID"
        width="60">
      </el-table-column>
      <el-table-column
        prop="instance_id"
        label="编号"
        width="220">
      </el-table-column>
      <el-table-column
        prop="group"
        label="分组"
        width="120">
      </el-table-column>
      <el-table-column
        prop="server_name"
        label="名称"
        width="120">
      </el-table-column>
      <el-table-column
        prop="host"
        label="地址"
        width="130">
      </el-table-column>
      <el-table-column
        prop="port"
        label="端口"
        width="80">
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="80">
      </el-table-column>

      <el-table-column
        prop="host_name"
        label="主机名"
        width="160"/>
      <el-table-column
        prop="version"
        label="版本"
        width="140"/>

      <el-table-column
        prop="insert_time"
        label="添加时间"
        width="160"/>
      <el-table-column
        prop="remark"
        label="备注"/>
      <el-table-column
        prop=""
        label=""/>
      <!--<el-table-column-->
      <!--fixed="right"-->
      <!--label="操作"-->
      <!--width="120">-->
      <!--<template slot-scope="scope">-->
      <!--<el-button @click="doViewInfo(scope.row)" type="text" size="small"></el-button>-->
      <!--</template>-->
      <!--</el-table-column>-->
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
  import {get_gateway_list} from '@/api/gateway'

  export default {
    name: "gateway-list",
    data() {
      return {
        page_num: 1,
        total_count: 0,
        listLoading: false,
        form: {
          instance_id: '',
          name: '',
          host_name: '',
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
        get_gateway_list(this.form, this.page_num, this.page_size).then(response => {
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
