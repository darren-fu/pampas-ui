<template>
  <div>
    <el-form ref="base_form" :model="base_form" :rules="rules"
             label-width="120px"
             @submit.native.prevent>
      <el-row>
        <el-col :span="24">
          <el-form-item label="规则名称" prop="name">
            <el-input v-model="base_form.name" placeholder="指定路由规则的名称"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="匹配Host" prop="mapping_host">
            <el-input v-model="base_form.mapping_host" placeholder="指定匹配路由规则的Host头"/>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
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
  import {get_route_rule, save_route_rule} from '@/api/route-rule'

  export default {
    name: "rule-base-edit",
    props: {
      rule_id: {type: Number, default: undefined},
    },
    data() {
      return {
        base_form: {
          name: undefined,
          group: undefined,
          mapping_host: undefined,
          remark: undefined,
        },
        rules: {},
      }
    },
    created() {
      if (this.rule_id) {
        this.doLoad(this.rule_id)
      }
    },
    methods: {
      doLoad(rule_id) {
        get_route_rule(rule_id).then(resp => {
          this.base_form = resp;
        })
      },
      doSaveBaseInfo() {
        let data = this.base_form
        data["mode"] = 1
        save_route_rule(data).then(resp => {
          if(!this.rule_id ){
            this.$store.dispatch('delView', this.$route)

            this.$router.push({path: '/rule/edit', query: {id: resp.id}})
          }
          // this.base_form = resp
          // this.$emit('update:rule_id', resp.id)
        })
      },
    }
  }
</script>

<style scoped>

</style>
