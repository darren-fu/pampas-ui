<template>
  <div>

    <el-card class="box-card">
      <div slot="header" class="clearfix card_header">
        <span>请勾选需要关联此路由规则的网关</span>
        <!--<el-button style="float: right; padding: 3px 10px" type="text">保存</el-button>-->
        <el-button style="float: right;" type="primary" size="mini" icon="el-icon-check" @click="saveRel">保存</el-button>
        <el-button style="float: right; margin-right: 30px" size="mini" type="" plain @click="loadTree">重置</el-button>
      </div>
      <div class="text item">

        <el-tree
          ref="tree"
          class="r_tree"
          :data="treeData"
          show-checkbox
          check-on-click-node
          default-expand-all
          :expand-on-click-node="false"
          node-key="id"
          :default-expanded-keys="[]"
          :default-checked-keys="defaultCheckedIds"
          :props="defaultProps">
        </el-tree>
      </div>
    </el-card>


  </div>
</template>

<script>
  import {get_route_rule_rel, save_route_rule_rel} from '@/api/route-rule'
  import {get_gateway_tree} from '@/api/gateway'


  export default {
    name: "rule-rel-gateway",
    props: {
      rule_id: {type: Number, default: undefined},
    },
    data() {
      return {
        treeData: [],
        defaultCheckedIds: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    created() {
      this.loadTree();
    },
    methods: {
      loadTree() {
        get_gateway_tree().then(resp => {
          this.treeData = resp.data
          return Promise.resolve();
        }).then(_ => {
          return get_route_rule_rel(this.rule_id)
        }).then(resp => {
         let ids = resp.data.map(v => {
            return v.id;
          })
         this.$refs.tree.setCheckedKeys(ids)
        })
      },

      saveRel() {
        let checkedNodes = this.$refs.tree.getCheckedKeys(true);
        let data = {}
        data["rule_id_list"] = [this.rule_id]
        data["gateway_id_list"] = checkedNodes
        save_route_rule_rel(data).then(resp => {
          this.$message({
            message: '保存完成',
            type: 'success'
          });
          this.loadTree()
        })
      }
    },
  }
</script>
<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .card_header {
    line-height: 30px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    min-width: 580px;
  }

  .box-card .el-card__header {
    background-color: #f8f8f8;
  }

  .r_tree .el-tree-node__label {
    font-size: 16px;
  }

  .r_tree .el-tree-node__expand-icon {
    font-size: 16px;
  }

</style>
