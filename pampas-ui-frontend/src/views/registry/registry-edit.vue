<template>
  <div class="app-container">
    <el-form ref="base_form" :model="base_form" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row>
        <el-col :span="24">
          <el-form-item label="名称" prop="name">
            <el-input v-model="base_form.name" placeholder="指定注册中心的名称"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="类型" prop="type">
            <el-select v-model="base_form.type" placeholder="请选择类型">
              <el-option label="consul" value="consul"/>
              <el-option label="zookeeper" value="zookeeper"/>
              <el-option label="etcd" value="etcd"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="模式" prop="pattern">
            <el-select v-model="base_form.pattern" placeholder="请选择模式">
              <el-option label="spring cloud" value="spring cloud"/>
              <el-option label="dubbo" value="dubbo"/>
              <el-option label="grpc" value="grpc"/>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="地址" prop="address">
            <el-input v-model="base_form.address" placeholder="输入注册中心地址"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model="base_form.remark"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button type="primary" @click="doSaveBaseInfo">保存</el-button>
            <!--<el-button @click="doCancel">取消</el-button>-->
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>


  </div>
</template>

<script>
  import {get_registry, save_registry} from "@/api/registry";

  export default {
    name: "registry-edit",
    data() {
      return {

        base_form: {
          id: undefined,
          name: undefined,
          type: undefined,
          pattern: undefined,
          address: undefined,
          remark: undefined,
        },
        rules: {
          name: [{required: true, message: '必须提供名称'}],
          type: [{required: true, message: '必须提供类型'}],
          pattern: [{required: true, message: '必须提供服务发现/注册模式'}],
          address: [{required: true, message: '必须提供地址'}],

        },
      }
    },
    created() {
      this.base_form.id = this.$route.query.id ? Number(this.$route.query.id) : undefined
      if (this.base_form.id) {
        this.doLoad(this.base_form.id)
      }
    },
    methods: {
      doLoad(id) {
        get_registry(id).then(resp => {
          this.base_form = resp;
        })
      },
      doSaveBaseInfo() {
        this.$refs.base_form.validate().then(v => {
            return this.$confirm('确认保存？')
          }
        ).then(_ => {
          let data = this.base_form
          data["mode"] = 1
          return save_registry(data)
        }).then(resp => {
          this.$message({
            message: '保存成功！',
            type: 'success'
          });
          if (!this.rule_id) {
            this.$store.dispatch('delView', this.$route)
            this.$router.push({path: '/service/registry/edit', query: {id: resp.id}})
          }
        })
      },
    }
  }
</script>

<style scoped>

</style>
