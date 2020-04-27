<script>
import authService from '../auth/auth-service'
import orderService from './order-service'

export default {
  name: 'orders',
  data () {
    return {
      orders: [],
      error: null,
      loading: true
    }
  },
  created () {
    this.loadOrders()
  },
  methods: {
    async loadOrders () {
      const session = await authService.currentSession()
      const authToken = session.getIdToken().getJwtToken()
      const response = await orderService.getOrders(authToken)
      this.orders = response.orders
      this.loading = false
    }
  }
}
</script>

<template>
  <div class="container" v-if="order">
    <h2>Your orders</h2>
    <ul>
      <li :key="order.id" v-for="order in orders">
        Order {{ order.id }} on {{ order.created }}
      </li>
    </ul>
  </div>
  <div class="error" v-else-if="error">{{ error }}</div>
  <div class="loading" v-else>Loading...</div>
</template>

<style scoped>
</style>
