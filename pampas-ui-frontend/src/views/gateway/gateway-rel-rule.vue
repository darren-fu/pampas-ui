<template>
  <div class="app-container">
    <el-row>
      <el-col :span="10">
        <el-input
          placeholder="输入名称进行筛选"
          v-model="filterGatewayText">
        </el-input>
        <el-radio-group v-model="mode" size="mini" style="margin: 5px 0">
          <el-radio-button label="multi">批量</el-radio-button>
          <el-radio-button label="single">单选</el-radio-button>
        </el-radio-group>
        <el-tree
          ref="gatewayTree"
          empty-text="没有可配置的网关"
          :filter-node-method="filterGatewayTree"
          @node-click="clickGatewayTreeNode"
          class="r_tree"
          :data="gatewayTreeData"
          show-checkbox
          check-on-click-node
          default-expand-all
          :expand-on-click-node="false"
          node-key="id"
          :default-expanded-keys="[]"
          :default-checked-keys="defaultCheckedIds"
          :props="defaultProps">
        </el-tree>
      </el-col>
      <el-col :span="8" :offset="0">
        <el-input
          style="margin-left: 10px"
          placeholder="输入名称进行筛选"
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
          default-expand-all
          :expand-on-click-node="false"
          node-key="id"
          :default-expanded-keys="[]"
          :default-checked-keys="defaultCheckedIds"
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
      <el-col :span="5" :offset="1">
        <el-button size="mini" type="" @click="reset">重置</el-button>
        <el-button type="primary" size="mini" icon="el-icon-check"  @click="saveRel">保存
        </el-button>

      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {get_route_rule_rel, get_rule_list, get_rule_tree, save_route_rule_rel} from '@/api/route-rule'
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
        defaultCheckedIds: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        showRuleCheckbox: true,
      };
    },
    created() {
      this.loadTree();
    },
    watch: {
      filterGatewayText(val) {
        this.$refs.gatewayTree.filter(val);
      },
      filterRuleText(val) {
        this.$refs.ruleTree.filter(val);
      },
    },
    computed: {
    },
    methods: {
      loadTree() {
        get_gateway_tree().then(resp => {
          this.gatewayTreeData = resp.data
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
        console.log(data, node, item);

        if (node.checked) {
          this.$refs.gatewayTree.setCheckedKeys([data.id])
          get_rel_rules(data.id).then(resp => {
            let ids = resp.data.map(v => {
              return v.id;
            })
            this.$refs.ruleTree.setCheckedKeys(ids || [])
          })
        } else {
          this.$refs.gatewayTree.setCheckedKeys([])
          this.$refs.ruleTree.setCheckedKeys([])

        }

      },
    }
  }
</script>

<style scoped>

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
</style>
