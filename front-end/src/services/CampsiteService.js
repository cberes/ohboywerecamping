import httpService from './HttpService'

const baseUrl = '/api/campsites'

export default {
  getCampsite (id) {
    const url = baseUrl + '/' + id
    return httpService.get(url)
  }
}
