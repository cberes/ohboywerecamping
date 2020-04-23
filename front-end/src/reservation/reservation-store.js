import Vue from 'vue'

const state = {
  pending: null
}

const mutations = {
  SET_PENDING (state, value) {
    Vue.set(state, 'pending', value)
  }
}

const actions = {
  setPending ({ commit }, value) {
    commit('SET_PENDING', value)
  },
  clearPending ({ commit }) {
    commit('SET_PENDING', null)
  }
}

export default {
  namespaced: true,
  state: state,
  mutations: mutations,
  actions: actions
}
