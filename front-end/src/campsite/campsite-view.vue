<script>
import Layout from '@/common/main'
import Campsite from './Campsite'

export default {
  components: { Layout, Campsite }
}
</script>

<template>
  <Layout>
    <Campsite :campsiteId="$route.params.id"></Campsite>
  </Layout>
</template>

<style scoped>
</style>
