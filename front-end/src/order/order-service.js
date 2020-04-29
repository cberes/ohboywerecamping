import httpService from '@/common/http-service'
import apiConfig from '@/config/api'

const baseUrl = apiConfig.url + '/api/orders'

export default {
  getOrders (authToken) {
    return httpService.get(`${baseUrl}`,
      { Authorization: authToken })
  },
  getOrder (orderId, authToken) {
    return httpService.get(`${baseUrl}/${orderId}`,
      { Authorization: authToken })
  }
}
