export default {
  getCampsite (id) {
    return Promise.resolve({ data: this.buildCampsite(id) })
  },

  buildCampsite (id) {
    if (id === '2') {
      return JSON.parse('{"id":"2","active":true,"name":"Site B","description":"Offers beautiful views of the lakeshore cliffs. Stay dry!","notes":"infested by ants","type":"CAMPSITE","access":"DRIVE_IN","size":40,"maxOccupancy":6,"maxVehicles":2,"petsAllowed":2,"electric":"UNKNOWN_AMP","water":"YES","sewer":"NO","areaId":"4","campgroundId":"1"}')
    } else {
      return JSON.parse('{"id":"1","active":true,"name":"Site A","description":"Located at the top of Mt. Very High, where the air is very thin.","notes":null,"type":"CAMPSITE","access":"DRIVE_IN","size":20,"maxOccupancy":6,"maxVehicles":1,"petsAllowed":2,"electric":"UNKNOWN_AMP","water":"YES","sewer":"NO","areaId":"4","campgroundId":"1"}')
    }
  }
}
