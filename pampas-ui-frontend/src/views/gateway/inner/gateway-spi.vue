<template>
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
      width="60">
    </el-table-column>
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
      prop="spi_interface_desc"
      label="SPI定义"/>
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
      width="70px"
    >
      <template slot-scope="scope">
        <span style="">{{ scope.row.status?'正常':'停用' }}</span>
      </template>
    </el-table-column>


    <!--<el-table-column-->
    <!--label="操作"-->
    <!--width="60">-->
    <!--<template slot-scope="scope">-->
    <!--<el-button @click="doEditConfig(scope.row)" type="text" size="small">修改</el-button>-->
    <!--</template>-->
    <!--</el-table-column>-->
  </el-table>
</template>

<script>
  import {get_gateway_spi} from '@/api/gateway'

  export default {
    name: "gateway-spi",
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
        get_gateway_spi(gateway_id).then(response => {
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

    }
  }
</script>

<style scoped>

</style>
