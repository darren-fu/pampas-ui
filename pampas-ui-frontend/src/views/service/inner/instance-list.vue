<template>
  <div>
    <el-form v-show="enable_search" :inline="true" :model="form" size="mini" class="demo-form-inline">
      <el-form-item label="分组">
        <el-select v-model="form.service_name" :clearable="true" filterable placeholder="请选择">
          <el-option
            v-for="item in services"
            :key="item"
            :label="item"
            :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="doQueryAction()">查询</el-button>
      </el-form-item>
    </el-form>
    <!--v-show="enable_search || tableData.length > 0"-->

    <el-table
      v-loading="listLoading"
      :data="tableData"
      empty-text="没有服务实例"
      header-row-class-name="tb_header"

      stripe
      style="width: 100%;margin-top: 10px">
      <el-table-column
        prop="id"
        label="ID"
        width="80">
      </el-table-column>
      <el-table-column
        prop="instance_id"
        label="实例ID"
        width="120">
      </el-table-column>
      <el-table-column
        prop="service_name"
        label="服务名称"
        width="120">
      </el-table-column>
      <el-table-column
        prop="host"
        label="地址"
        width="120">
      </el-table-column>
      <el-table-column
        prop="port"
        label="端口"
        width="60">
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="80">
      </el-table-column>
      <el-table-column
        prop="start_time"
        label="启动时间"
        width="120">
      </el-table-column>
      <el-table-column
        prop="weight"
        label="权重"
        sortable
        width="80">
      </el-table-column>
      <el-table-column
        prop="version"
        label="版本"
        width="120"/>
      <el-table-column
        prop="warmup_seconds"
        label="热机"
        width="120"/>

      <el-table-column
        prop="props"
        label="额外属性"
        width="120"/>
      <el-table-column
        prop="insert_time"
        label="创建时间"
      />
      <el-table-column
        prop="remark"
        label="备注"
      />
      >
      <el-table-column
        fixed="right"
        label="操作"
        width="180">
        <template slot-scope="scope">
          <!--<el-button @click="doFetchLimitConfig(scope.row)" type="text" size="small">获取远程限流配置</el-button>-->
          <!--<el-button @click="doViewInfo(scope.row)" type="text" size="small">查看</el-button>-->
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container">
      <el-pagination
        v-show="enable_page && total_count > 0"
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
  import {get_instance_in_service} from '@/api/instance'

  export default {
    name: "instance-list",
    props: {
      enable_search: {type: Boolean, default: true},
      enable_page: {type: Boolean, default: true},
      service_id: {type: Number, default: undefined},
      service_name: {type: String, default: undefined},
    },
    data() {
      return {
        page_num: 1,
        page_size: 20,
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
      if (!this.enable_search && this.service_id) {
        this.doLoadByServiceId(this.service_id);
      }
    },
    watch: {
      // 如果 `question` 发生改变，这个函数就会运行
      service_id: function (newVal, oldVal) {
        if (typeof (newVal) != 'undefined') {
          console.log('newVal', newVal);
          this.doLoadByServiceId(newVal);
        }
      }
    },
    methods: {
      reload() {
        if (!this.enable_search && this.service_id) {
          this.doLoadByServiceId(this.service_id);
        }
        if (this.enable_search) {
          this.doQueryAction()
        }
      },
      doLoadByServiceId(service_id) {
        get_instance_in_service(service_id).then(resp => {
          this.tableData = resp.data
          this.total_count = resp.count
        })
      },
      doQueryAction() {
        this.page_num = 1
        this.doLoadByForm();
      },
      doLoadByForm() {
        // this.listLoading = true
        // queryNginxList(this.form.group, this.form.host_name, this.page_num, this.page_size).then(response => {
        //   this.tableData = response.data
        //   this.total_count = response.meta.count
        //   this.listLoading = false
        // })
      },

      doChangePage(cur_page_num) {
        this.page_num = cur_page_num
        this.doLoadByForm()
      },
      handleSizeChange(size) {
        this.page_size = size
        this.page_num = 1

        this.doLoadByForm();
      },
    }
  }
</script>

<style scoped>

</style>
