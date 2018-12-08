<template>
  <div class="app-container">
    <el-form ref="service_form" :model="service_form" size="small" :rules="rules"
             label-width="120px"
             label-position="left"
             hide-required-asterisk
             @submit.native.prevent>
      <el-row>
        <el-col :span="12">
          <el-form-item label="服务名称"
                        prop="service_name">
            <el-input v-model="service_form.service_name" v-show="edit_mode"/>
            <span v-show="!edit_mode">{{service_form.service_name}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="11" :offset="1">
          <el-form-item label="服务状态"
                        prop="status">
            <el-select v-model="service_form.status" v-show="edit_mode" placeholder="请选择">
              <el-option label="启用" :value="1"/>
              <el-option label="停用" :value="0"/>
            </el-select>
            <span v-show="!edit_mode">{{service_form.status == 1 ?"启用" :"停用"}}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="服务类型"
                        prop="type">
            <el-select v-model="service_form.type" v-show="edit_mode" placeholder="请选择">
              <el-option label="RESTful" value="RESTful"/>
              <el-option label="dubbo" value="dubbo"/>
              <el-option label="grpc" value="grpc"/>
            </el-select>
            <el-select v-model="service_form.protocol" v-show="special_type" placeholder="请选择协议">
              <el-option label="http" value="http"/>
              <el-option label="grpc" value="grpc"/>
              <el-option label="其他" value="其他"/>
            </el-select>
            <span v-show="!edit_mode">{{service_form.type}}</span>
          </el-form-item>
        </el-col>
        <el-col :span="11" :offset="1">
          <el-form-item label="注册中心"
                        prop="registry_id">
            <el-select v-model="service_form.registry_id" v-show="edit_mode"
                       clearable
                       placeholder="请选择">
              <el-option
                v-for="item in registrys"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
            <span v-show="!edit_mode">{{service_form.registry_name}}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20">
          <el-form-item label="服务备注" placeholder="可输入备注信息"
                        prop="remark">
            <el-input v-model="service_form.remark" v-show="edit_mode"/>
            <span v-show="!edit_mode">{{service_form.remark}}</span>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" v-show="edit_mode" @click="doSave">保存</el-button>
        <el-button v-show="service_form.id  && edit_mode&& !service_form.registry_id " type="" @click="doAddInstance">
          添加实例
        </el-button>
        <el-button v-show="!edit_mode && service_form.id && service_form.registry_id " type=""
                   @click="doRefreshInstance">刷新注册中心实例
        </el-button>
        <el-button v-show="edit_mode && service_form.id && service_form.registry_id " type="" @click="doCheckInstance">
          查看注册中心实例
        </el-button>
      </el-form-item>
    </el-form>

    <el-tabs value="first" v-show="service_form.id" type="border-card">
      <el-tab-pane label="服务实例列表" name="first">
        <instance-list ref="instance_list" @edit_instance="doEditInstance" v-show="service_form.id" :enable_page="false"
                       :enable_search="false"
                       :service_id="service_form.id"></instance-list>
      </el-tab-pane>
      <el-tab-pane label="网关列表" name="second">
        xxx
      </el-tab-pane>
    </el-tabs>


    <el-dialog :title="dialogFormTitle" :visible.sync="dialogFormVisible" @close="doCloseDialog">
      <instance-edit ref="instance_edit" :show.sync="dialogFormVisible" :id.sync="cur_instance_id"
                     :cur_service_id="service_form.id"
                     :cur_service_name="service_form.service_name"></instance-edit>
    </el-dialog>
    <el-dialog title="实例列表" :visible.sync="dialogListVisible" @close="doCloseListDialog">
      <instance-list ref="instance_list" v-show="service_form.id" :enable_page="false" :enable_search="false"
                     :service_id="service_form.id"></instance-list>
    </el-dialog>
  </div>
</template>

<script>
  import {check_service_instance_list, get_service, save_service} from '@/api/service'
  import {get_instance_in_service} from '@/api/instance'
  import {get_registry_list} from "@/api/registry"

  import InstanceList from './instance-list'
  import InstanceEdit from './instance-edit'

  export default {
    name: "service-modify",
    props: {
      id: {type: Number, default: undefined},
      edit_mode: {type: Boolean, default: true},
    },
    components: {
      InstanceList, InstanceEdit
    },

    data() {
      return {
        service_form: {
          id: undefined,
          service_name: undefined,
          registry_id: undefined,
          type: "RESTful",
          status: 0,
          remark: undefined,
          registry_name: undefined
        },
        cur_instance_id: undefined,
        rules: {
          service_name: [{required: true, message: '必须提供服务名称'}],
          type: [{required: true, message: '必须选择服务类型'}],
          registry_id: [{validator: this.validRegistry, trigger: ['blur', 'change']}],
        },
        dialogFormVisible: false,
        dialogListVisible: false,
        dialogFormTitle: "",
        registrys: []
      }
    },
    created() {
      const id = this.$route.query.id;
      if (this.edit_mode) {
        this.doGetRegistry().then(_ => {
          this.doQueryService(id)
        })
      } else {
        this.doQueryService(id)
      }
    },
    watch: {
      // 如果 `question` 发生改变，这个函数就会运行
      // cur_instance_id: function (newVal, oldVal) {
      //   if (typeof (newVal) != 'undefined') {
      //     this.dialogFormTitle = "编辑实例"
      //     this.dialogFormVisible = true
      //   } else {
      //     this.dialogFormVisible = false
      //   }
      // }
    },
    computed: {
      add_mode: function () {
        return this.id ? false : true;
      },
      special_type: function () {
        let type = this.service_form.type;
        return type && type != "RESTful" && type != "dubbo" && type != "grpc"
      }
    },
    methods: {
      doQueryService(id) {
        console.log('service', this.$route)
        if (!id) {
          return
        }
        get_service(id).then(resp => {
          this.service_form = resp
          return Promise.resolve(resp);
        })
      },
      doSave() {
        this.$refs.service_form.validate().then(v => {
            return this.$confirm('确认保存？')
          }
        ).then(v => {
          return save_service(this.service_form)
        }).then(resp => {
          if (this.add_mode) {
            this.$store.dispatch('delView', this.$route)
            this.$router.push({path: '/service/edit', query: {id: resp.id}})
          }
          this.$message({
            message: '保存成功！',
            type: 'success'
          });
        }).catch(e => {
          console.log(e)
        })

      },
      doAddInstance() {
        this.dialogFormTitle = "添加实例"
        this.dialogFormVisible = true
        this.cur_instance_id = undefined
      },
      doEditInstance(instance_id) {
        this.dialogFormTitle = "修改实例"
        this.dialogFormVisible = true
        this.cur_instance_id = instance_id
        if (this.$refs.instance_edit) {
          this.$refs.instance_edit.doLoad()
        }
      },

      doCloseDialog() {
        this.cur_instance_id = undefined
        this.$refs.instance_list.reload()
      },
      doCloseListDialog() {
      },
      doGetRegistry() {
        return get_registry_list()
          .then(resp => {
            this.registrys = resp.data
            return Promise.resolve()
          })
      },
      validRegistry(rule, value, callback) {
        if (this.service_form.type == 'dubbo' && !this.service_form.registry_id) {
          callback(new Error('DUBBO服务需要选择注册中心'));
        }
        callback()
      },
      doRefreshInstance() {
      },
      doCheckInstance() {
        this.dialogListVisible = true
        check_service_instance_list(this.service_form.type, this.service_form.service_name, this.service_form.registry_id)
      },
    }
  }
</script>

<style scoped>

</style>
