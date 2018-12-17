<template>
  <div class="app-container">
    <div>
      <el-alert
        title="基本信息" style="color: #fff;background-color: #363636;border-radius: 0px"
        type="success" center :closable="false">
      </el-alert>
      <gateway-detail :gateway_id="id"></gateway-detail>
    </div>
    <div style="margin-top: 15px">
      <el-alert
        title="详情/配置" style="color: #fff;background-color: #363636;border-radius: 0px"
        type="success" center :closable="false">
      </el-alert>
      <el-card style=" border-radius: 0px;" shadow="hover">
        <el-tabs v-model="tabName">
          <el-tab-pane label="SPI插件" name="spi">
            <gateway-spi :gateway_id="id"></gateway-spi>
          </el-tab-pane>
          <el-tab-pane label="自定义配置" name="config" lazy>
            <gateway-config :gateway_id="id"></gateway-config>
          </el-tab-pane>
          <el-tab-pane label="路由关联" name="rel" lazy>
            <gateway-rel-rule :gateway_id="id"></gateway-rel-rule>
          </el-tab-pane>
          <el-tab-pane label="通知网关刷新配置" name="push" lazy>
            <gateway-notify :gateway_id="id"></gateway-notify>
          </el-tab-pane>
        </el-tabs>
      </el-card>

    </div>
  </div>
</template>

<script>
  import GatewayDetail from './inner/gateway-detail'
  import GatewayConfig from './inner/gateway-config'
  import GatewaySpi from './inner/gateway-spi'
  import GatewayRelRule from './inner/gateway-rel-rule'
  import GatewayNotify from './inner/gateway-notify'


  export default {
    name: "gateway-view",
    components: {GatewayDetail, GatewayConfig, GatewaySpi, GatewayRelRule, GatewayNotify},
    data() {
      return {
        id: undefined,
        tabName: 'spi',
      }
    },
    created() {
      this.id = this.$route.query.id ? Number(this.$route.query.id) : undefined

    },
  }
</script>

<style scoped>

</style>
