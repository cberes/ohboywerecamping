import campgroundService from './mock-campground-service'

// TODO get the actual campground ID
const campgroundId = 2

const state = {
  campground: null
}

const mutations = {
  SET_CURRENT_CAMPGROUND (state, newValue) {
    state.campground = newValue
  }
}

const actions = {
  fetchCampground ({ commit, state, rootState }) {
    if (state.campground) {
      return Promise.resolve(state.campground)
    }

    return campgroundService.getCampground(campgroundId).then(result => {
      commit('SET_CURRENT_CAMPGROUND', result)
      return result
    })
  }
}

export default {
  namespaced: true,
  state: state,
  mutations: mutations,
  actions: actions
}
