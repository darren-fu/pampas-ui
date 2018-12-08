<template>
  <div class="app-container">
    <el-tabs value="first" tab-position="top">
      <el-tab-pane label="列表查询" name="first" >
        <gateway-list></gateway-list>
      </el-tab-pane>
      <el-tab-pane label="分组管理" name="second" :lazy="true">
        <gateway-group></gateway-group>

      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
  import {get_gateway_list} from '@/api/gateway'
  import GatewayList from './inner/gateway-list'
  import GatewayGroup from './inner/gateway-group'

  export default {
    name: "gateway-index",
    components: {GatewayList, GatewayGroup},
    data() {
      return {
        mode: 'list'
      }
    },
    created() {
    },
    computed: {},
    methods: {}
  }
</script>

<style scoped>

</style>
