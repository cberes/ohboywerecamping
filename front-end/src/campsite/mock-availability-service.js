import moment from 'moment'

export default {
  getArea (id, start, end) {
    return Promise.resolve({ data: {} })
  },

  getCampground (id, start, end) {
    const campsites = [1, 2]
    return Promise.resolve({
      data : {
        campgroundId: id,
        campsites: campsites.map(c => this.mockCampsite(c, start, end))
      }
    })
  },

  mockCampsite (id, start, end) {
    const today = moment().startOf('day')
    const tooSoon = moment().add(2, 'days').startOf('day')

    return {
      id: id,
      availability: this.datesBetween(start, end)
        .filter(date => !date.isBefore(today))
        .map(date => this.mockDate(date, tooSoon))
    }
  },

  datesBetween (start, end) {
    const dates = []
    let currentDate = start
    while (currentDate.isBefore(end)) {
      dates.push(currentDate)
      currentDate = moment(currentDate).add(1, 'days').startOf('day')
    }
    return dates
  },

  mockDate (date, tooSoon) {
    return {
      date: date.format('YYYY-MM-DD'),
      status: !date.isBefore(tooSoon) ? 'AVAILABLE' : 'IN_PERSON_ONLY'
    }
  },

  getCampsite (id, start, end) {
    return Promise.resolve({
      data: {
        campgroundId: 1,
        campsites: [this.mockCampsite(id, start, end)]
      }
    })
  },

  keyByCampsiteId (availability) {
    const keyed = {}
    availability.campsites.forEach(campsite => (keyed[campsite.id] = campsite.availability))
    return keyed
  }
}
