<template>
  <div v-show="show">
    <el-form ref="instance_form" :model="instance_form" size="small" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row>
        <el-col :span="12">
          <el-form-item label="服务名称" prop="service_name">
            <el-input v-model="instance_form.service_name" :readonly="specific_service"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务地址" prop="host">
            <el-input v-model="instance_form.host"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="主机名称" prop="host_name">
            <el-input v-model="instance_form.host_name"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="服务端口" prop="port">
            <el-input v-model="instance_form.port"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="版本" prop="version">
            <el-input v-model="instance_form.version"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权重" prop="weight">
            <el-input v-model="instance_form.weight"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="启动时间" prop="start_time">
            <el-date-picker
              v-model="instance_form.start_time"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="请选择时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="instance_form.status" size="small">
              <el-radio-button label="1">正常</el-radio-button>
              <el-radio-button label="0">停用</el-radio-button>
              <el-radio-button label="-1">失效</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属机房" prop="room">
            <el-input v-model="instance_form.room"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="热机耗时（秒）" prop="warmup_seconds">
            <el-input v-model="instance_form.warmup_seconds"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="instance_form.remark"/>
      </el-form-item>

      <el-row v-for="(prop, index) in instance_form.prop_list"
              :key="prop.idx">
        <el-col :span="12">
          <el-form-item
            :label="'属性['+ (index+1) + ']'"
            :prop="'prop_list.' + index + '.key'"

            :rules="{      required: true, message: '属性名不能为空', trigger: 'blur'    }"
          >
            <el-input size="mini" v-model="prop.key"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="9">
          <el-form-item :label="'属性值'"
                        :prop="'prop_list.' + index + '.value'">
            <el-input size="mini" v-model="prop.value"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="3">
          <el-form-item label-width="15px">
            <el-button type="warning" icon="el-icon-delete" size="mini" circle
                       @click.prevent="removeProp(prop)"></el-button>
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
  import moment from 'moment'

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
          weight: 100,
          version: undefined,
          status: 1,
          warmup_seconds: 600,
          start_time: undefined,
          remark: undefined,
          prop_list: []
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
    watch: {
      show: function (newVal, oldVal) {
        if (newVal && this.cur_service_id) {
          this.instance_form.start_time = moment().format('YYYY-MM-DD HH:mm:ss');
        }
      }
    },
    created() {
      this.instance_form.start_time = moment().format('YYYY-MM-DD HH:mm:ss');
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
        this.$refs.instance_form.validate((valid) => {
          if (valid) {
            save_instance(this.instance_form).then(resp => {
              this.$emit('update:show', false)
              this.$refs.instance_form.resetFields()
            })
          } else {
            return false;
          }
        });

      },
      doCancel() {
        this.$emit('update:show', false)
        this.$refs.instance_form.resetFields()
      },
      addProp() {
        this.instance_form.prop_list.push({
          idx: Date.now(),
          key: '',
          value: '',
        });
      },
      removeProp(item) {
        var index = this.instance_form.prop_list.indexOf(item)
        if (index !== -1) {
          this.instance_form.prop_list.splice(index, 1)
        }
      },
    }
  }
</script>

<style scoped>

</style>
