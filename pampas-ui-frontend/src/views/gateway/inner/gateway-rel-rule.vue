<template>
  <div>


    <el-table v-if="tableData.length > 0"
              v-loading="listLoading"
              :data="tableData"
              size="medium"
              header-row-class-name="tb_header"
              stripe
              style="width: 100%;margin-top: 10px">
      <el-table-column
        prop="id"
        label="路由规则id"/>

      <el-table-column
        prop="name"
        label="路由规则"/>

      <el-table-column
        prop="mapping_host"
        label="匹配HOST"/>
      <el-table-column
        prop="mapping_host"
        label="匹配HOST"/>
      <el-table-column
        prop="remark"
        label="备注"/>

      <el-table-column
        label="操作"
        width="140">
        <template slot-scope="scope">
          <el-button @click="doViewRule(scope.row)" type="text" size="small">查看规则</el-button>
          <el-button @click="doEditConfig(scope.row)" type="text" size="small">关联规则</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row >
      <el-col :span="24">
        <el-card :body-style="{ padding: '5px','text-align': 'center'}" style="margin-top: 10px">
          <el-button type="" icon="el-icon-plus" @click="doRelRule">关联路由规则</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {get_rel_rules} from '@/api/gateway'

  export default {
    name: "gateway-rel-rule",
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
        get_rel_rules(gateway_id).then(response => {
          this.tableData = response.data
          this.listLoading = false
        })
      },
      doEditConfig(row) {
        this.$router.push({path: 'rel-rul', query: {g_id: this.gateway_id}})
      },
      doViewRule(row) {
        this.$router.push({path: '/rule/edit', query: {id: row.id}})

      },
      doRelRule() {
        this.$router.push({path: '/gateway/rel-rul', query: {g_id: this.gateway_id}})
      },

    }
  }
</script>

<style scoped>

</style>
