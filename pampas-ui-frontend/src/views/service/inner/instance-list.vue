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

    <el-table
      v-loading="listLoading"
      :data="tableData"
      stripe
      empty-text="没有服务实例"
      header-row-class-name="tb_header"
      :row-class-name="tableRowClassName"
      style="width: 100%;margin-top: 0px">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand" label-width="120px">
            <el-form-item label="服务名称">
              <span>{{ scope.row.service_name }}</span>
            </el-form-item>
            <el-form-item label="实例ID">
              <span>{{ scope.row.instance_id }}</span>
            </el-form-item>
            <el-form-item label="地址">
              <span>{{ scope.row.host }}</span>
            </el-form-item>
            <el-form-item label="主机名">
              <span>{{ scope.row.host_name }}</span>
            </el-form-item>
            <el-form-item label="端口">
              <span>{{ scope.row.port }}</span>
            </el-form-item>
            <el-form-item label="启动时间">
              <span>{{ scope.row.start_time }}</span>
            </el-form-item>
            <el-form-item label="状态">
              <span>{{ scope.row.status }}</span>
            </el-form-item>
            <el-form-item label="权重">
              <span>{{ scope.row.weight }}</span>
            </el-form-item>
            <el-form-item label="版本">
              <span>{{ scope.row.version }}</span>
            </el-form-item>
            <el-form-item label="热机耗时（秒）">
              <span>{{ scope.row.warmup_seconds }}</span>
            </el-form-item>
            <el-form-item label="备注">
              <span>{{ scope.row.remark }}</span>
            </el-form-item>
            <el-row v-for="(prop, index) in scope.row.prop_list"
                    :key="index">
              <el-col :span="12">
                <el-form-item
                  :label="'属性['+ ++index + ']'">
                  <span>{{ prop.key }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  :label="'属性值'">
                  <span>{{ prop.value }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="service_name" label="服务名称"/>
      <el-table-column prop="host" label="地址"/>
      <el-table-column prop="port" label="端口"/>
      <el-table-column prop="status" label="状态"/>
      <el-table-column prop="start_time" label="启动时间" min-width="110"/>
      <el-table-column prop="weight" label="权重" sortable/>
      <el-table-column prop="version" label="版本"/>

      <el-table-column label="操作">
        <template slot-scope="scope">
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
      tableRowClassName({row, rowIndex}) {
        // return 'warning-row';
        if (rowIndex % 2 != 0) {
          return 'success-row';
        }
        return '';
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
  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }

</style>

<style>
  .el-table .warning-row {
    background-color: oldlace;
  }

  .el-table .success-row {
    background-color: #f0f9eb;
  }
</style>
