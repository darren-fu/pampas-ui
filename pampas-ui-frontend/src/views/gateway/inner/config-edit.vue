<template>
  <div>
    <el-form ref="config_form" :model="config_form" size="small" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row v-if="gateway_group">
        <el-col :span="9">
          <el-form-item label="网关分组：">
            {{gateway_group}}
          </el-form-item>
        </el-col>
        <el-col :span="15">
          <el-form-item label="所属配置模块：">
            {{config_spi_desc}}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="gateway_instance_id">
        <el-col :span="24">
          <el-form-item label="网关编号：">
            {{gateway_instance_id}}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-for="(prop, index) in config_form.list"
              :key="prop.id">
        <el-col :span="9">
          <el-form-item
            label="属性名："
          >
            <span>【{{prop.gateway_instance_id?'单独生效':'整组生效'}}】{{prop.label}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="15">
          <el-form-item label="属性值"
                        :prop="'list.' + index + '.value'"
                        :rules="{required: prop.required, message: '该属性不能为空', trigger: 'blur'    }"
          >
            <el-input v-if="!prop.multi" size="small" v-model="prop.value" :placeholder="prop.placeholder"></el-input>
            <el-input v-if="prop.multi" size="small" type="textarea" :rows="5" v-model="prop.value"
                      :placeholder="prop.placeholder"></el-input>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" @click="doSaveConfig">保存</el-button>
            <el-button v-if="gateway_instance_id" @click="doNotify">单独通知网关</el-button>
            <el-button @click="doNotifyGroup">通知网关整组</el-button>
            <!--<el-button @click="doCancel">取消</el-button>-->
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>


  </div>
</template>

<script>
  import {get_gateway_config, notify_gateway_config, save_gateway_config} from '@/api/gateway'

  export default {
    name: "config-edit",
    props: {
      gateway_group: {type: String, default: undefined},
      gateway_instance_id: {type: String, default: undefined},
      config_spi_class: {type: String, default: undefined},
      config_spi_desc: {type: String, default: undefined},
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
        const {gateway_group, gateway_instance_id, config_spi_class} = this
        return {
          gateway_group,
          gateway_instance_id,
          config_spi_class
        }
      }
    },
    watch: {
      config_condition: {
        handler: function (newVal, oldVal) {
          let {gateway_group, gateway_instance_id, config_spi_class} = newVal
          this.doLoadConfig(gateway_group, gateway_instance_id, config_spi_class)
        },
        deep: true
      },
    },
    created() {
      if (this.gateway_group && this.config_spi_class) {
        this.doLoadConfig(this.gateway_group, this.gateway_instance_id, this.config_spi_class)
      }
    },
    methods: {
      doLoadConfig(gateway_group, instance_id, config_spi_class) {
        get_gateway_config(null, gateway_group, instance_id, config_spi_class).then(resp => {
          let data = {}
          data["list"] = resp.data
          this.config_form = data;
        })
      },
      doSaveConfig() {
        this.$refs.config_form.validate().then(v => {
            return this.$confirm('确认保存？')
          }
        ).then(_ => {
          let data = {}
          data['list'] = this.config_form.list
          data['gateway_group'] = this.gateway_group
          data['gateway_instance_id'] = this.gateway_instance_id
          data['config_spi_class'] = this.config_spi_class
          return save_gateway_config(data)
        }).then(resp => {
          this.$message({
            message: '保存成功！',
            type: 'success'
          });
          this.$emit("update-config")
        })
      },
      doNotify() {
        this.$confirm('确认通知对应网关？').then(_ => {
          return notify_gateway_config(this.gateway_group, this.gateway_instance_id, this.config_spi_class)
        }).then(resp => {
          this.$message({
            message: '完成：' + resp,
            type: 'warning',
            duration: 5000
          });
        })
      },
      doNotifyGroup() {
        this.$confirm('确认通知对应网关分组？').then(_ => {
          return notify_gateway_config(this.gateway_group, null, this.config_spi_class)
        }).then(resp => {
          this.$message({
            message: '完成：' + resp,
            type: 'warning',
            duration: 5000
          });
        })
      }
    }
  }
</script>

<style scoped>

</style>
