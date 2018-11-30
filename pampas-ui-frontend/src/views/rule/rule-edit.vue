<template>
  <div class="app-container">
    <el-tabs value="base" tab-position="right" :before-leave="switchTab">
      <el-tab-pane label="定义基础信息" name="base" lazy>
        <rule-base-editor :rule_id.sync="rule_id"></rule-base-editor>
      </el-tab-pane>
      <el-tab-pane label="定义路由规则" name="rule" lazy>
        <rule-content-editor :rule_id="rule_id"></rule-content-editor>
      </el-tab-pane>
      <el-tab-pane label="关联网关实例" name="gateway" lazy>
        <rule-rel-gateway :rule_id="rule_id"></rule-rel-gateway>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import RuleContentEditor from './inner/rule-content-edit'
  import RuleBaseEditor from './inner/rule-base-edit'
  import RuleRelGateway from './inner/rule-rel-gateway'
  import {get_route_rule, save_route_rule} from '@/api/route-rule'

  export default {
    name: "rule-edit",
    components: {RuleBaseEditor, RuleContentEditor, RuleRelGateway},
    data() {
      return {
        rule_id: undefined,
      }
    },
    watch: {},
    created() {
      this.rule_id = this.$route.query.id ? Number(this.$route.query.id) : undefined
    },
    methods: {
      switchTab(newTabName, oldTabName) {
        if (newTabName != "form" && !this.rule_id) {
          this.$message({
            message: '请先保存基本信息',
            type: 'warning'
          });
          return false
        }
      },

    }
  }
</script>

<style scoped>

</style>
<style>
  /*.el-select .el-input {*/
  /*color: #111110;*/
  /*}*/

  .input-with-select .el-input-group__prepend {
    background-color: #fff;
    width: 110px;
    color: #111110;
  }
</style>
