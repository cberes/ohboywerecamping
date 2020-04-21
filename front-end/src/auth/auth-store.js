const state = {
  authToken: null,
  clientId: '',
  userPoolId: ''
}

const mutations = {
  SET_AUTH_TOKEN (state, value) {
    state.authToken = value
  }
}

const actions = {
  setAuthToken ({ commit, state, rootState }, value) {
    commit('SET_AUTH_TOKEN', value)
  }
}

export default {
  namespaced: true,
  state: state,
  mutations: mutations,
  actions: actions
}
