<template>
  <el-table
    v-loading="listLoading"
    :data="tableData"
    size="medium"
    header-row-class-name="tb_header"
    :span-method="objectSpanMethod"
    stripe
    style="width: 100%;margin-top: 10px">
    <!--<el-table-column-->
      <!--prop="id"-->
      <!--label="ID"-->
      <!--width="60">-->
    <!--</el-table-column>-->
    <!--<el-table-column-->
      <!--prop="gateway_group"-->
      <!--label="分组"-->
      <!--width="120">-->
    <!--</el-table-column>-->
    <!--<el-table-column-->
    <!--prop="gateway_instance_id"-->
    <!--label="网关编号"-->
    <!--width="120">-->
    <!--</el-table-column>-->

    <el-table-column
      prop="config_spi_desc"
      label="所属分类"
      width="160">
    </el-table-column>
    <el-table-column
      prop="key"
      label="配置项"
      width="160">
    </el-table-column>
    <el-table-column
      prop="label"
      label="配置项说明"
      width="180">
    </el-table-column>
    <el-table-column
      prop="value"
      label="配置值"
    >
    </el-table-column>

    <el-table-column
      label="操作"
      width="60">
      <template slot-scope="scope">
        <el-button @click="doEditConfig(scope.row)" type="text" size="small">修改</el-button>
      </template>
    </el-table-column>

  </el-table>
</template>

<script>
  import {get_gateway_config} from '@/api/gateway'

  export default {
    name: "gateway-config",
    props: {
      gateway_id: {type: Number, default: undefined},
    },
    data() {
      return {
        listLoading: false,
        tableData: [],
      }
    },
    created() {
      if (this.gateway_id) {
        this.doLoad(this.gateway_id)
      }
    },
    methods: {
      doLoad(gateway_id) {
        this.listLoading = true
        get_gateway_config(gateway_id).then(response => {
          this.tableData = response.data
          this.listLoading = false
        })
      },
      doEditConfig(row) {
        this.$message({
          message: '暂未实现',
          type: 'warning'
        });
      },
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        if (columnIndex == 0|| columnIndex == 4) {
          if (row.row_span != null) {
            console.log(row, column, rowIndex, columnIndex);
            return {
              rowspan: row.row_span,
              colspan: 1
            }
          } else {
            return {
              rowspan: 0,
              colspan: 0
            }
          }

        }

      },

    }
  }
</script>

<style scoped>

</style>
