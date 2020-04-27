import httpService from '@/common/http-service'

const baseUrl = '/api/orders'

export default {
  getOrders (authToken) {
    return httpService.get(`${baseUrl}/orders`, { Authorization: authToken })
  },
  getOrder (orderId, authToken) {
    return httpService.post(`${baseUrl}/orders/${orderId}`,
      { Authorization: authToken })
  }
}
