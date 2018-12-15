<template>
  <div>
    <el-row v-if="tableData.length == 0">
      <el-col :span="24">
        <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px">
          <el-button type="" icon="el-icon-plus" @click="doPushSpiChange">关联路由规则</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "gateway-push",
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
      }
    },
    methods: {
      doLoad(gateway_id) {
      },
      doPushSpiChange() {
      },

    }
  }
</script>

<style scoped>

</style>
