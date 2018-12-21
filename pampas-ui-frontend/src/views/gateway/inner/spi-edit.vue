<template>
  <div>
    <el-form ref="config_form" class="rule_form" :model="config_form" size="small"
             label-width="120px" hide-required-asterisk
             @submit.native.prevent>
      <el-row v-model="config_form.list">
        <el-col v-for="(item, index) in config_form.list" :key="index" :index="index" :item="item">
          <el-card shadow="hover" :body-style="{ padding: '10px'}" style="margin: 5px;border: 1px solid #808080;">
            <el-row>
              <el-col :span="12">
                <el-form-item label="插件名" class="rule_label">
                  {{item.spi_name}}
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="说明" class="rule_label">
                  {{item.spi_desc}}
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="实现类" class="rule_label">
                  {{item.spi_class}}
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="执行顺序" class="rule_label">
                  <el-input size="small" v-model="item.order" placeholder="0-100，越小优先级越高"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否启用" class="rule_label">
                  <el-radio-group v-model="item.status" size="mini">
                    <el-radio-button :label="true">是</el-radio-button>
                    <el-radio-button :label="false">否</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      <el-row style="margin-top: 10px">
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" @click="doSave">保存</el-button>
            <el-button v-if="gateway_instance_id" @click="doNotify">单独通知网关</el-button>
            <!--<el-button @click="doCancel">取消</el-button>-->
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

  </div>
</template>

<script>

  import {get_gateway_spi, notify_gateway_spi, save_gateway_spi} from '@/api/gateway'

  export default {
    name: "spi-edit",
    props: {
      gateway_group: {type: String, default: undefined},
      gateway_instance_id: {type: String, default: undefined},
      config_spi_interface: {type: String, default: undefined},
      config_spi_interface_desc: {type: String, default: undefined},
    },
    data() {
      return {
        config_form: {
          list: []
        },
        rules: {},
      }
    },
    computed: {
      config_condition() {
        const {gateway_group, gateway_instance_id, config_spi_interface} = this
        return {
          gateway_group,
          gateway_instance_id,
          config_spi_interface
        }
      }
    },
    watch: {
      config_condition: {
        handler: function (newVal, oldVal) {
          let {gateway_group, gateway_instance_id, config_spi_interface} = newVal
          this.doLoadSpi(gateway_group, gateway_instance_id, config_spi_interface)
        },
        deep: true
      },
    },
    created() {
      if (this.gateway_group && this.config_spi_interface) {
        this.doLoadSpi(this.gateway_group, this.gateway_instance_id, this.config_spi_interface)
      }
    },
    methods: {
      doLoadSpi(gateway_group, instance_id, config_spi_interface) {
        get_gateway_spi(null, gateway_group, instance_id, config_spi_interface)
          .then(resp => {
            let data = {}
            data["list"] = resp.data
            this.config_form = data;

          })
      },
      doSave() {
        this.$refs.config_form.validate().then(v => {
            return this.$confirm('确认保存？')
          }
        ).then(_ => {
          let data = {}
          data['list'] = this.config_form.list
          data['gateway_group'] = this.gateway_group
          data['gateway_instance_id'] = this.gateway_instance_id
          data['spi_interface'] = this.config_spi_interface
          return save_gateway_spi(data)
        }).then(resp => {
          this.$message({
            message: '保存成功！',
            type: 'success'
          });
          this.$emit("update-spi")
        })
      },
      doNotify() {
        this.$confirm('确认通知对应网关？').then(_ => {
          return notify_gateway_spi(this.gateway_group, this.gateway_instance_id, this.config_spi_interface)
        }).then(resp => {
          this.$message({
            message: '完成：' + resp,
            type: 'warning',
            duration: 5000
          });
        })
      },
    }
  }
</script>

<style scoped>

</style>
