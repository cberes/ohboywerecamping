import httpService from './HttpService'

const baseUrl = '/api/availability'

export default {
  getArea (id, start, end) {
    const url = baseUrl + '/area/' + id + '?' + this.buildRangeQuery(start, end)
    return httpService.get(url)
  },

  buildRangeQuery (start, end) {
    return 'start=' + start.format('YYYY-MM-DD') + '&end=' + end.format('YYYY-MM-DD')
  },

  getCampground (id, start, end) {
    const url = baseUrl + '/campground/' + id + '?' + this.buildRangeQuery(start, end)
    return httpService.get(url)
  },

  getCampsite (id, start, end) {
    const url = baseUrl + '/campsite/' + id + '?' + this.buildRangeQuery(start, end)
    return httpService.get(url)
  },

  keyByCampsiteId (availability) {
    const keyed = {}
    availability.campsites.forEach(campsite => (keyed[campsite.id] = campsite.availability))
    return keyed
  }
}
