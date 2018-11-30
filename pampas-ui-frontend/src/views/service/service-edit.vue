<template>
  <div class="app-container">
    <service-modify :id="service_id"></service-modify>
  </div>
</template>

<script>

  import ServiceModify from './inner/service-modify'

  export default {
    name: "service-edit",
    components: {ServiceModify},
    data() {
      return {
        service_id: undefined
      }
    },
    created() {
      this.service_id = this.$route.query.id ? Number(this.$route.query.id) : undefined
    },
  }
</script>

<style scoped>

</style>
