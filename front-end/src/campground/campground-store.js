import campgroundService from './campground-service'

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

    return campgroundService.getCurrentCampground().then(result => {
      commit('SET_CURRENT_CAMPGROUND', result.data.campground)
      return result.data.campground
    })
  }
}

export default {
  namespaced: true,
  state: state,
  mutations: mutations,
  actions: actions
}
