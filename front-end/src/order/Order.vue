<script>
import authService from '../auth/auth-service'
import orderService from './order-service'

export default {
  name: 'order',
  props: {
    orderId: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      order: null,
      campsite: null,
      error: null,
      loading: true
    }
  },
  created () {
    this.loadOrder()
  },
  methods: {
    async loadOrder () {
      const session = await authService.currentSession()
      const authToken = session.getIdToken().getJwtToken()
      const result = await orderService.getOrder(this.orderId, authToken)
      this.campsite = result.data.order.reservations[0].campsite
      this.order = result.data.order
      this.loading = false
    }
  }
}
</script>

<template>
  <div class="container" v-if="order">
    <h2>Order {{ order.id }}</h2>
    Campsite {{ campsite.id }} is reserved for the following days
    <ul>
      <li :key="res.date" v-for="res in order.reservations">
        {{ res.date }}
      </li>
    </ul>
  </div>
  <div class="error" v-else-if="error">{{ error }}</div>
  <div class="loading" v-else>Loading...</div>
</template>

<style scoped>
</style>
