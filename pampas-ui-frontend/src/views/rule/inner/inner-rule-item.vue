<template>
  <el-col :span="24">
    <slot></slot>
  </el-col>
</template>

<script>
  import {ContainerMixin, ElementMixin} from 'vue-slicksort';

  export default {
    name: "inner-rule-item",
    mixins:[ElementMixin]
  }
</script>

<style scoped>

</style>
