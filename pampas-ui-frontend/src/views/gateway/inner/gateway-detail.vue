<template>
  <div>
    <el-card class="box-card" shadow="hover" :body-style="{ padding: '8px' }">
      <el-form ref="base_form" :model="base_form"
               label-width="120px" size="mini"
               @submit.native.prevent>
        <el-row>
          <el-col :span="8">
            <el-form-item label="网关编号：">
              <!--<el-input v-model="base_form.instance_id" />-->
              <span>{{base_form.instance_id}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="网关分组：">
              <span>{{base_form.group}}</span>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="网关地址：">
              <span>{{base_form.host}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="网关端口：">
              <span>{{base_form.proxy_port}}</span>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="主机名称：">
              <span>{{base_form.host_name}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="网关状态：">
              <span>{{base_form.status == 1 ?'正常':'失败'}}</span>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="版本信息：">
              <span>{{base_form.version}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="启动时间：">
              <span>{{base_form.update_time}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

  </div>
</template>

<script>
  import {get_gateway} from '@/api/gateway'

  export default {
    name: "gateway-detail",
    props: {
      gateway_id: {type: Number, default: undefined},
    },
    data() {
      return {
        base_form: {
          name: undefined,
          group: undefined,
          mapping_host: undefined,
          remark: undefined,
        },
        services: [],
      }
    },
    created() {
      if (this.gateway_id) {
        this.doLoad(this.gateway_id)
      }
    },
    methods: {
      doLoad(gateway_id) {
        get_gateway(gateway_id).then(resp => {
          this.base_form = resp;
        })
      },
    }
  }
</script>

<style scoped>

</style>
