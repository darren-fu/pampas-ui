<template>
  <div class="app-container">
    <el-row>
      <el-col :span="11" >
        <el-input
          placeholder="输入名称进行筛选网关" size="small"
          v-model="filterGatewayText">
        </el-input>
        <el-radio-group v-model="mode" size="mini" style="margin: 5px 0">
          <el-tooltip effect="dark" content="批量模式不会删除已关联的路由规则" placement="top-start">
            <el-radio-button label="multi">批量模式</el-radio-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="单选模式下，取消勾选的路由规则会被删除关联关系" placement="top-start">
            <el-radio-button label="single">单选模式</el-radio-button>
          </el-tooltip>

        </el-radio-group>
        <el-tree
          ref="gatewayTree"
          empty-text="没有可配置的网关"
          :filter-node-method="filterGatewayTree"
          @node-click="clickGatewayTreeNode"
          class="r_tree"
          :data="gatewayTreeData"
          show-checkbox
          highlight-current
          check-on-click-node
          :expand-on-click-node="false"
          node-key="id"
          :default-expanded-keys="defaultGatewayExpandIds"
          :default-checked-keys="defaultGatewayCheckedIds"
          :props="defaultProps">
        </el-tree>

      </el-col>
      <div class="centerdiv"></div>

      <el-col :span="8">
        <el-input
          style="margin-left: 10px" size="small"
          placeholder="输入名称进行筛选关联的路由"
          v-model="filterRuleText">
        </el-input>
        <el-tree
          ref="ruleTree"
          empty-text="没有符合条件的路由规则"
          class="r_tree"
          :data="ruleTreeData"
          :filter-node-method="filterRuleTree"
          :show-checkbox="showRuleCheckbox"
          check-on-click-node
          :expand-on-click-node="false"
          node-key="id"
          :default-expanded-keys="defaultRuleExpandIds"
          :default-checked-keys="defaultRuleCheckedIds"
          :props="defaultProps"
        >
        <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <!--<el-tag size="mini" type="info" v-if="data.leaf && data.status">已启用</el-tag>-->
          <el-tag size="mini" type="danger" v-if="data.leaf && !data.status">未启用</el-tag>
        </span>
      </span>
        </el-tree>
      </el-col>
      <el-col :span="4" style="margin-left: 30px">
        <el-button size="mini" type="" @click="reset">重置</el-button>
        <el-button type="primary" size="mini" icon="el-icon-check" @click="saveRel">保存</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {get_rule_tree} from '@/api/route-rule'
  import {get_gateway_tree, get_rel_rules, save_gateway_rule_rel} from '@/api/gateway'

  export default {
    name: "gateway-rel-rule",
    data() {
      return {
        mode: 'single',
        gatewayTreeData: [],
        ruleTreeData: [],
        filterGatewayText: '',
        filterRuleText: '',
        defaultGatewayCheckedIds: [],
        defaultGatewayExpandIds: [],
        defaultRuleCheckedIds: [],
        defaultRuleExpandIds: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        showRuleCheckbox: true,
      };
    },
    created() {
      if (this.$route.query.g_id) {
        this.defaultGatewayCheckedIds.push(Number(this.$route.query.g_id))
        this.defaultGatewayExpandIds.push(Number(this.$route.query.g_id))
      }
      this.loadTree().then(_ => {
        this.do_check_rel_rule(this.$route.query.g_id)
      })

    },
    watch: {
      filterGatewayText(val) {
        this.$refs.gatewayTree.filter(val);
      },
      filterRuleText(val) {
        this.$refs.ruleTree.filter(val);
      },
    },
    computed: {},
    methods: {
      loadTree() {
        return get_gateway_tree().then(resp => {
          this.gatewayTreeData = resp.data
          if (this.defaultGatewayExpandIds.length == 0 && this.gatewayTreeData.length > 0) {
            this.defaultGatewayExpandIds.push(this.gatewayTreeData[0].id)
          }
          return Promise.resolve();
        }).then(_ => {
          return get_rule_tree()
        }).then(resp => {
          this.ruleTreeData = resp.data
          return Promise.resolve();
        })
      },
      filterGatewayTree(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1 || (data.group && data.group.indexOf(value) !== -1);
      },
      filterRuleTree(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1 || (data.group && data.group.indexOf(value) !== -1);
      },
      reset() {
      },
      saveRel() {
        let gatewayIds = this.$refs.gatewayTree.getCheckedKeys(true);
        let ruleIds = this.$refs.ruleTree.getCheckedKeys(true);
        if (gatewayIds.length < 1) {
          this.$message({
            message: '必须选择网关',
            type: 'warning'
          });
          return;
        }

        let data = {}
        data["gateway_id_list"] = gatewayIds
        data["rule_id_list"] = ruleIds
        save_gateway_rule_rel(data).then(resp => {
          this.$message({
            message: '保存完成',
            type: 'success'
          });
        })

      },
      clickGatewayTreeNode(data, node, item) {
        if (this.mode == 'multi') {
          return
        }
        //单选模式
        if (node.checked) {
          this.$refs.gatewayTree.setCheckedKeys([data.id])
          this.do_check_rel_rule(data.id)
        } else {
          this.$refs.gatewayTree.setCheckedKeys([])
          this.$refs.ruleTree.setCheckedKeys([])

        }
      },
      do_check_rel_rule(gateway_id) {
        if (!gateway_id && this.ruleTreeData.length > 0) {
          this.defaultRuleExpandIds.push(this.ruleTreeData[0].id)
          return;
        }
        if(!gateway_id){
          return;
        }
        get_rel_rules(gateway_id).then(resp => {
          let ids = resp.data.map(v => {
            return v.id;
          })
          this.$refs.ruleTree.setCheckedKeys(ids || [])
          this.defaultRuleExpandIds = ids
        })
      },
    }
  }
</script>

<style scoped>

  .centerdiv {
    float: left;
    width: 1px;
    border-right: 1px dashed #a1a3a9;
    padding-bottom: 700px; /*关键*/
    margin-top: 50px;
  }

</style>
<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
  .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
    background-color: #e9e9eb;
    color: #ff4d51;
  }
  .el-tree-node__content:hover {
    background-color: #bbbbbb;
  }
</style>
