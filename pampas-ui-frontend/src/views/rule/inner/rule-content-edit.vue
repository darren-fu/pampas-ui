<template>
  <div>
    <el-tabs v-model="tab_name" :before-leave="switchTab" stretch>

      <el-tab-pane label="使用表单配置路由规则" name="form" lazy>
        <el-form ref="rule_list_form" class="rule_form" :model="rule_form" size="mini" :rules="valid_rules"
                 label-width="120px" hide-required-asterisk
                 @submit.native.prevent>
          <el-row>
            <el-col :span="24" v-for="(rule, index) in rule_form.rule_list" :key="index" :offset="index > 0 ? 0 : 0">
              <el-card shadow="hover" :body-style="{ padding: '5px'}" style="margin: 5px;border: 1px solid #808080;">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="规则类型" class="rule_label" :prop="'rule_list.' + index + '.type'"
                                  :rules="[
                          { required: true, message: '请选择规则类型', trigger: 'change' },
                        ]">
                      <el-select v-model="rule.type" style="width: 40%">
                        <el-option label="HTTP" value="http"/>
                        <el-option label="DUBBO" value="dubbo"/>
                        <el-option label="GRPC" value="grpc"/>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="路由服务" class="rule_label" :prop="'rule_list.' + index + '.service'"
                                  :rules="[
                          { required: true, message: '请选择路由服务', trigger: 'blur' },
                        ]">
                      <el-select v-model="rule.service"
                                 clearable
                                 placeholder="请选择">
                        <el-option
                          v-for="item in services"
                          :key="item.id"
                          :label="item.service_name"
                          :value="item.service_name">
                        </el-option>
                      </el-select>

                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="匹配请求路径" class="rule_label" :prop="'rule_list.' + index + '.path'">
                      <el-input v-model="rule.path"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12" v-show="rule.type == 'http'">
                    <el-form-item label="路径映射策略" class="rule_label" :prop="'rule_list.' + index + '.mapped_path'">
                      <el-input placeholder="请求路径将按照指定策略进行映射" v-model="rule.mapping_path" class="input-with-select">
                        <el-select v-model="rule.mapping_strategy" slot="prepend">
                          <el-option label="移除字符" value="strip"/>
                          <el-option label="指定路径" value="appoint"/>
                        </el-select>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="目标服务地址" class="rule_label" :prop="'rule_list.' + index + '.mapping_host'"
                                  :rules="[{validator: checkMappingHost, trigger: 'change'}
                                  ]">
                      <el-select v-model="rule.host_strategy" placeholder="请求将路由到目标服务"
                                 style="width: 100%">
                        <el-option :label="'自动使用路由服务的实例'" value="auto"/>
                        <el-option label="指定目标服务地址" value="appoint"/>
                      </el-select>
                      <el-input v-show="rule.host_strategy == 'appoint'" v-model="rule.mapping_host"
                                placeholder="默认映射当前服务下的实例地址" style="width: 100%"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-card :body-style="{ padding: '10px','text-align': 'center'}" style="margin: 10px">
                <el-button type="" icon="el-icon-plus" @click="addRule">添加路由规则</el-button>
              </el-card>
            </el-col>

          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="Yaml格式" name="yaml" lazy>
        <monaco ref="editor_yaml" :content.sync="yaml" type="yaml"></monaco>
      </el-tab-pane>
      <el-tab-pane label="Json格式" name="json" lazy>
        <monaco ref="editor_json" :content.sync="json" type="json"></monaco>
      </el-tab-pane>
      <el-tab-pane key="save" name="save">
           <span slot="label" style="">
                <!--<el-button type="text" icon="el-icon-check" style="color:#aa0000;font-size: 15px">保存</el-button>-->
             <el-button type="primary" size="mini" icon="el-icon-check">保存</el-button>
           </span>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import Monaco from '@/components/Monaco'
  import {formatJson, isJSON, isYAML, toJSON, toYAML} from '@/utils/json'
  import {get_route_rule, save_route_rule} from '@/api/route-rule'
  import {get_service_list} from "@/api/service"


  export default {
    name: "rule-edit",
    props: {
      rule_id: {type: Number, default: undefined},
    },
    components: {Monaco},
    data() {
      return {
        yaml: undefined,
        json: undefined,
        tab_name: "form",
        rule_form: {
          id: undefined,
          rule_list: [],
        },
        valid_rules: {},
        services: []
      }
    },
    created() {
      this.doLoadServiceList()
        .then(_=>{
          if (this.rule_id) {
            this.doLoad(this.rule_id)
            this.yaml = toYAML(this.rule_form.rule_list)
            this.json = toJSON(this.rule_form.rule_list)
          }
        })

    },
    methods: {
      doLoad(rule_id) {
        get_route_rule(rule_id).then(resp => {
          this.rule_form = resp;
        })
      },
      doLoadServiceList() {
        return get_service_list({}, 1, 10000).then(response => {
          this.services = response.data
          return Promise.resolve();
        })
      },
      switchTab(newTabName, oldTabName) {
        if (newTabName == "save") {
          this.doSaveRules()
          return false;
        }
        let content_obj = null
        if (oldTabName == 'json') {
          let jsonContent = this.$refs.editor_json.getContent();
          if (jsonContent && jsonContent.trim() != '') {
            content_obj = isJSON(jsonContent);
            if (!content_obj) {
              this.$message({
                message: 'Json格式错误',
                type: 'warning'
              });
              return false;
            }
          }
        } else if (oldTabName == 'yaml') {
          let yml = this.$refs.editor_yaml.getContent();
          if (yml && yml.trim() != '') {
            content_obj = isYAML(yml)
            if (!content_obj) {
              this.$message({
                message: 'Yaml格式错误',
                type: 'warning'
              });
              return false;
            }
          }
        } else if (oldTabName == 'form') {
        }

        if (content_obj) {
          this.$set(this.rule_form, "rule_list", content_obj)
        }

        if (newTabName == "json") {
          this.json = toJSON(this.rule_form.rule_list)
        }
        if (newTabName == "yaml") {
          this.yaml = toYAML(this.rule_form.rule_list)
        }
      },
      checkMappingHost(rule, value, callback) {
        // 获取数据index
        let a = rule.field.indexOf(".")
        let b = rule.field.lastIndexOf(".");
        let r_idx = Number(rule.field.substring(a + 1, b));
        if (value.trim() == "" && this.rule_form.rule_list[r_idx].host_strategy == "appoint") {
          callback(new Error('请提供目标服务地址'));
        } else {
          callback()
        }
      },
      doSaveRules() {
        let data = this.rule_form;
        data["mode"] = 2
        save_route_rule(data).then(resp => {
          this.$message({
            message: '保存完成',
            type: 'success'
          });
        })
      },
      addRule() {
        let item = {
          "type": "http",
          "host_strategy":"auto",
        };
        if (!this.rule_form.rule_list) {
          this.rule_form.rule_list = []
        }
        this.rule_form.rule_list.push(item)
      }
    }
  }
</script>

<style scoped>


</style>
<style>
  /*.el-select .el-input {*/
  /*color: #111110;*/
  /*}*/
  .rule_label .el-form-item__label {
    color: #1a1818;
  }

  .input-with-select .el-input-group__prepend {
    background-color: #fff;
    width: 110px;
    color: #111110;
  }

  .rule_form .el-form-item--mini.el-form-item {
    margin-bottom: 6px;
  }
</style>
