<script>
import moment from 'moment'
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
      this.orders = response.data.orders
      this.loading = false
    },
    orderDate (order) {
      return moment(order.created).format('YYYY-MM-DD')
    }
  }
}
</script>

<template>
  <div class="container" v-if="orders">
    <h2>Your orders</h2>
    <ul>
      <li :key="order.id" v-for="order in orders">
        Order <router-link :to="{ name: 'order', params: { id: order.id }}">{{ order.id }}</router-link> on {{ orderDate(order) }}
      </li>
    </ul>
  </div>
  <div class="error" v-else-if="error">{{ error }}</div>
  <div class="loading" v-else>Loading...</div>
</template>

<style scoped>
</style>
