<template>
  <div>
    <el-table
      v-loading="listLoading"
      :data="tableData"
      size="medium"
      header-row-class-name="tb_header"
      :span-method="objectSpanMethod"
      stripe
      style="width: 100%;margin-top: 10px">

      <el-table-column
        prop="gateway_instance_id"
        label="生效级别"
        width="90px"
      >
        <template slot-scope="scope">
          <span style="">{{ scope.row.gateway_instance_id?'实例级别':'分组级别' }}</span>
        </template>
      </el-table-column>
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

    <el-dialog title="编辑自定义配置" width="75%" :visible.sync="dialogFormVisible" @close="doCloseDialog">
      <config-edit ref="config_edit" :gateway_group="edit_group" :config_spi_class="edit_spi_class"
                   :edit_spi_desc="edit_spi_desc"></config-edit>
    </el-dialog>
  </div>
</template>

<script>
  import {get_gateway_config} from '@/api/gateway'
  import ConfigEdit from './config-edit'

  export default {
    name: "gateway-config",
    components: {ConfigEdit},
    props: {
      gateway_id: {type: Number, default: undefined},
    },
    data() {
      return {
        listLoading: false,
        tableData: [],
        dialogFormVisible: false,
        edit_group: undefined,
        edit_gateway_instance_id: undefined,
        edit_spi_class: undefined,
        edit_spi_desc: undefined,
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
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 5) {
          if (row.row_span != null) {
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
      doEditConfig(row) {
        this.dialogFormVisible = true;
        this.edit_group = row.gateway_group
        this.edit_gateway_instance_id = row.gateway_instance_id
        this.edit_spi_class = row.config_spi_class
        this.edit_spi_desc = row.config_spi_desc;
      },

      doCloseDialog() {
      },

    }
  }
</script>

<style scoped>

</style>
