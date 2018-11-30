<template>
  <div class="app-container">

    <el-form ref="service_form" :model="service_form" size="small" :rules="rules"
             label-width="120px"
             label-position="left"
             hide-required-asterisk
             status-icon
             @submit.native.prevent>
      <el-row>
        <el-col :span="20">
          <el-form-item label="服务名称"
                        prop="service_name">
            <el-input v-model="service_form.service_name"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="20">
          <el-form-item label="服务备注"
                        prop="remark">
            <el-input v-model="service_form.remark"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" @click="doSave">保存</el-button>
        <el-button v-show="service_form.id" type="" @click="doAddInstance">添加实例</el-button>
      </el-form-item>
    </el-form>

    <el-tabs value="first" v-show="service_form.id" type="border-card" >
      <el-tab-pane label="服务实例列表" name="first">
        <instance-list ref="instance_list" v-show="service_form.id" :enable_page="false" :enable_search="false"
                       :service_id="service_form.id"></instance-list>
      </el-tab-pane>
      <el-tab-pane label="网关列表" name="second">
        xxx
      </el-tab-pane>

    </el-tabs>


    <el-dialog :title="dialogFormTitle" :visible.sync="dialogFormVisible" @close="doCloseDialog">
      <instance-edit :show.sync="dialogFormVisible" :cur_service_id="service_form.id"
                     :cur_service_name="service_form.service_name"></instance-edit>
    </el-dialog>
  </div>
</template>

<script>
  import {get_service, save_service} from '@/api/service'
  import {get_instance_in_service} from '@/api/instance'
  import InstanceList from './instance-list'
  import InstanceEdit from './instance-edit'

  export default {
    name: "service-modify",
    props: {
      id: {type: Number, default: undefined},
    },
    components: {
      InstanceList, InstanceEdit
    },
    data() {
      return {
        service_form: {
          id: undefined,
          service_name: undefined,
          remark: undefined
        },
        rules: {
          service_name: [{required: true, message: '必须提供服务名称'}],
        },
        dialogFormVisible: false,
        dialogFormTitle: "",
      }
    },
    created() {
      const id = this.$route.query.id;
      this.doQueryService(id)
    },
    computed: {
      edit_mode: function () {
        return this.id ? true : false;
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
          if (!this.edit_mode) {
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
      },
      doCloseDialog() {
        console.log('close');
        this.$refs.instance_list.reload()
      },
    }
  }
</script>

<style scoped>

</style>
