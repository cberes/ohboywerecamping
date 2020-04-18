export default {
  getCampground (id) {
    return Promise.resolve({
      id: 1,
      active: true,
      name: 'Campground X',
      description: 'Campground X is a really fun place. It lets you get away ' +
      'from the toil of everyday life. The toil of everyday life where too ' +
      'many things at work are misspelled. Located in the deepest, darkest ' +
      'depths of scenic Lake Ontario.'
    })
  },

  getCampgrounds () {
    return Promise.resolve([])
  },

  getCampsites (id) {
    return Promise.resolve(JSON.parse('[{"id":1,"active":true,"name":"Site A","description":"Located at the top of Mt. Very High, where the air is very thin.","notes":null,"type":"CAMPSITE","access":"DRIVE_IN","size":20,"maxOccupancy":6,"maxVehicles":1,"petsAllowed":2,"electric":"UNKNOWN_AMP","water":"YES","sewer":"NO","areaId":4,"campgroundId":1},{"id":2,"active":true,"name":"Site B","description":"Offers beautiful views of the lakeshore cliffs. Stay dry!","notes":"infested by ants","type":"CAMPSITE","access":"DRIVE_IN","size":40,"maxOccupancy":6,"maxVehicles":2,"petsAllowed":2,"electric":"UNKNOWN_AMP","water":"YES","sewer":"NO","areaId":4,"campgroundId":1}]'))
  }
}
