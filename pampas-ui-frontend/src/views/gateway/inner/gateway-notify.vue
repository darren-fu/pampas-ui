<template>
  <div>
    <el-row>
      <el-col :span="24">
        <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px" shadow="hover">
          <el-button type="" icon="el-icon-refresh" @click="doPushSpiChange">刷新路由规则配置</el-button>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px" shadow="hover">
          <el-button type="" icon="el-icon-refresh" @click="doPushSpiChange">刷新SPI插件配置</el-button>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px" shadow="hover">
          <el-button type="" icon="el-icon-refresh" @click="doPushSpiChange">刷新自定义配置</el-button>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px" shadow="hover">
          <el-button type="" icon="el-icon-refresh" @click="doPushSpiChange">刷新限流配置</el-button>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  export default {
    name: "gateway-notify",
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
