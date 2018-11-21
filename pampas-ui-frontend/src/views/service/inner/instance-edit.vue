<template>
  <div v-show="show">
    <el-form ref="instance_form" :model="instance_form" size="small" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row>
        <el-col :span="12">
          <el-form-item label="服务名称"
                        prop="service_name">
            <el-input v-model="instance_form.service_name" :readonly="specific_service"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务地址"
                        prop="host">
            <el-input v-model="instance_form.host"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="主机名称"
                        prop="host_name">
            <el-input v-model="instance_form.host_name"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务端口"
                        prop="port">
            <el-input v-model="instance_form.port"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="版本"
                        prop="version">
            <el-input v-model="instance_form.version"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权重"
                        prop="weight">
            <el-input v-model="instance_form.weight"/>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item label="启动时间"
                        prop="start_time">
            <el-input v-model="instance_form.start_time"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态"
                        prop="status">
            <el-input v-model="instance_form.status"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属机房"
                        prop="room">
            <el-input v-model="instance_form.room"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="热机耗时"
                        prop="warmup_seconds">
            <el-input v-model="instance_form.warmup_seconds"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注"
                    prop="remark">
        <el-input v-model="instance_form.remark"/>
      </el-form-item>
      <!--
       :rules="{
            required: true, message: '属性不能为空', trigger: 'blur'
          }"
      -->
      <el-row v-for="(prop, index) in instance_form.props"
              :key="prop.idx"
      >
        <el-col :span="12">
          <el-form-item
            :label="'属性'"
            :prop="'props.' + index + '.key'"
          >
            <el-input size="mini" v-model="prop.key"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item
            :label="'属性值'"
            :prop="'props.' + index + '.value'"
          >
            <el-input size="mini" v-model="prop.value"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item label-width="15px">
            <el-button type="warning" icon="el-icon-delete" size="mini" circle
                       @click.prevent="removeProp(prop)"></el-button>
            <!--<el-button @click.prevent="removeProp(domain)" style="color: #C21F39" type="text" size="mini">删除属性</el-button>-->
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
  import {get_instance, save_instance} from '@/api/instance'

  export default {
    name: "instance-edit",
    props: {
      id: {type: Number, default: undefined},
      show: {type: Boolean, default: true},
      cur_service_id: {type: Number, default: undefined},
      cur_service_name: {type: String, default: undefined},
    },
    data() {
      return {
        instance_form: {
          id: undefined,
          service_id: undefined,
          service_name: undefined,
          host: undefined,
          host_name: undefined,
          port: undefined,
          room: undefined,
          weight: undefined,
          version: undefined,
          status: undefined,
          warmup_seconds: undefined,
          remark: undefined,
          props: []
        },
        rules: {
          service_name: [{required: true, message: '必须提供服务名称'}],
        },
      }
    },
    computed: {
      edit_mode: function () {
        return this.id ? true : false;
      },
      specific_service: function () {
        return this.cur_service_id ? true : false
      }
    },
    created() {
      if (this.id) {
        this.instance_form.id = this.id;
        this.doLoad()
      }
      if (this.cur_service_id) {
        this.instance_form.service_id = this.cur_service_id;
      }
      if (this.cur_service_name) {
        this.instance_form.service_name = this.cur_service_name;
      }
    },
    methods: {
      doLoad() {
        if (this.instance_form.id) {
          get_instance(this.instance_form.id).then(resp => {
            this.instance_form = resp
          })
        }
      },
      doSaveInstance() {
        save_instance(this.instance_form).then(resp => {
          this.instance_form = resp
          this.$emit('update:show', false)
        })
      },
      doCancel() {
        this.$refs.instance_form.resetFields()
        this.$emit('update:show', false)
      },
      addProp() {
        this.instance_form.props.push({
          idx: Date.now(),
          key: '',
          value: '',
        });
      },
      removeProp(item) {
        var index = this.instance_form.props.indexOf(item)
        if (index !== -1) {
          this.instance_form.props.splice(index, 1)
        }
      },

    }
  }
</script>

<style scoped>

</style>
