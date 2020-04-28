import httpService from '@/common/http-service'
import apiConfig from '@/config/api'

const baseUrl = apiConfig.url + '/api/campsites'

export default {
  getReservations (campsiteId) {
    return httpService.get(`${baseUrl}/${campsiteId}/reservations`)
  },
  createReservation (campsiteId, days, authToken) {
    return httpService.post(`${baseUrl}/${campsiteId}/reservations`,
      { days }, { Authorization: authToken })
  }
}
