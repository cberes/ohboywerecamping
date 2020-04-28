<script>
import Layout from '@/common/main'
import Order from './Order'

export default {
  components: { Layout, Order }
}
</script>

<template>
  <Layout>
    <Order :orderId="$route.params.id"></Order>
  </Layout>
</template>

<style scoped>
</style>
