<template>
  <el-row>
    <slot/>
  </el-row>
</template>

<script>
  import {ContainerMixin} from 'vue-slicksort';

  export default {
    name: "inner-rule-list",
    mixins: [ContainerMixin]
  }
</script>

<style scoped>

</style>
