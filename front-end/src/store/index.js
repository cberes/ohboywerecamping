import Vue from 'vue'
import Vuex from 'vuex'

import campgroundModule from './campground'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    campgrounds: campgroundModule
  }
})
