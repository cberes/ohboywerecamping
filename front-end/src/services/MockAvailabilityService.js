import moment from 'moment'

export default {
  getArea (id, start, end) {
    return Promise.resolve({})
  },

  getCampground (id, start, end) {
    const campsites = [1, 2]
    return Promise.resolve({
      campgroundId: id,
      campsites: campsites.map(c => this.mockCampsite(c, start, end))
    })
  },

  mockCampsite (id, start, end) {
    return {
      id: id,
      availability: this.datesBetween(start, end).map(this.mockDate)
    }
  },

  datesBetween (start, end) {
    const dates = []
    let currentDate = start
    while (currentDate.isBefore(end)) {
      dates.push(currentDate)
      currentDate = moment(currentDate).add(1, 'days')
    }
    return dates
  },

  mockDate (date) {
    return {
      date: date.format('YYYY-MM-DD'),
      status: 'AVAILABLE'
    }
  },

  getCampsite (id, start, end) {
    return Promise.resolve({
      campgroundId: 1,
      campsites: [this.mockCampsite(id, start, end)]
    })
  },

  keyByCampsiteId (availability) {
    const keyed = {}
    availability.campsites.forEach(campsite => (keyed[campsite.id] = campsite.availability))
    return keyed
  }
}
