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
        prop="spi_interface_desc"
        label="SPI模块" width="130px"/>
      <el-table-column
        prop="spi_class"
        label="SPI实现类"/>
      <el-table-column
        prop="spi_name"
        label="SPI实现名称"/>
      <el-table-column
        prop="spi_desc"
        label="SPI实现描述"/>
      <el-table-column
        prop="status"
        label="状态"
        width="70px">
        <template slot-scope="scope">
          <span style="">{{ scope.row.status?'正常':'停用' }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        width="60">
        <template slot-scope="scope">
          <el-button @click="doEditSpi(scope.row)" type="text" size="small">配置</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="title" width="75%" :visible.sync="dialogFormVisible" @close="doCloseDialog">
      <spi-edit ref="spi_edit" @update-spi="reloadSpi"
                :gateway_group="edit_group"
                :gateway_instance_id="edit_gateway_instance_id"
                :config_spi_interface="edit_spi_interface"
                :config_spi_interface_desc="edit_spi_interface_desc"></spi-edit>
    </el-dialog>
  </div>

</template>

<script>
  import {get_gateway_spi} from '@/api/gateway'
  import SpiEdit from './spi-edit'

  export default {
    name: "gateway-spi",
    components: {SpiEdit},
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
        edit_spi_interface: undefined,
        edit_spi_interface_desc: undefined,
        title:'管理SPI插件',
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
        get_gateway_spi(gateway_id).then(response => {
          this.tableData = response.data
          this.listLoading = false
        })
      },
      doEditSpi(row) {
        this.$message({
          message: '暂未实现',
          type: 'warning'
        });
      },
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        if (columnIndex == 0 || columnIndex == 5) {
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
      reloadSpi() {
        this.doLoad(this.gateway_id);
      },
      doEditSpi(row) {
        this.title = '管理SPI插件 - ' + row.spi_interface_desc||''
        this.dialogFormVisible = true;
        this.edit_group = row.gateway_group
        this.edit_gateway_instance_id = row.gateway_instance_id
        this.edit_spi_interface = row.spi_interface
        this.edit_spi_interface_desc = row.spi_interface_desc;
      },
      doCloseDialog() {
      },
    }
  }
</script>

<style scoped>

</style>
