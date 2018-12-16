<template>
  <div>
    <el-form ref="config_form" :model="config_form" size="small" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row v-if="gateway_group">
        <el-col :span="12">
          <el-form-item label="网关分组：">
            {{gateway_group}}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属配置模块：">
            {{edit_spi_desc}}
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
        <el-col :span="12">
          <el-form-item
            label="属性名："
          >
            <span>{{prop.label}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="属性值"
                        :prop="'list.' + index + '.default_value'"
                        :rules="{required: prop.required, message: '该属性不能为空', trigger: 'blur'    }"
          >
            <el-input size="small" v-model="prop.value" :placeholder="prop.placeholder"></el-input>
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" @click="doSaveInstance">保存</el-button>
            <el-button @click="addProp">新增属性</el-button>
            <el-button @click="doCancel">取消</el-button>
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>


  </div>
</template>

<script>
  import {get_gateway_config} from '@/api/gateway'

  export default {
    name: "config-edit",
    props: {
      gateway_group: {type: String, default: undefined},
      gateway_instance_id: {type: String, default: undefined},
      config_spi_class: {type: String, default: undefined},
      edit_spi_desc: {type: String, default: undefined},
    },
    data() {
      return {

        config_form: {
          list: []
        },
        rules: {},
      }
    },
    watch: {
      config_spi_class: function (newVal, oldVal) {
        if (newVal) {

        }
      }
    },
    created() {
      if (this.gateway_group && this.config_spi_class) {
        this.doLoadConfig(this.gateway_group, this.config_spi_class)
      }
    },
    methods: {
      doLoadConfig(gateway_group, config_spi_class) {
        get_gateway_config(null, gateway_group, null, config_spi_class).then(resp => {
          let data = {}
          data["list"] = resp.data
          this.config_form = data;
        })
      },
    }
  }
</script>

<style scoped>

</style>
