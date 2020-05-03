import httpService from '@/common/http-service'

const baseUrl = '/api/campgrounds'

export default {
  getCampground (id) {
    const url = baseUrl + '/' + id
    return httpService.get(url)
  },

  getCurrentCampground () {
    const url = baseUrl.substring(0, baseUrl.length - 1)
    return httpService.get(url)
  },

  getCampgrounds () {
    return httpService.get(baseUrl)
  },

  getCampsites (id) {
    const url = baseUrl + '/' + id + '/campsites'
    return httpService.get(url)
  }
}
