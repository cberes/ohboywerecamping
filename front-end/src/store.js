import Vue from 'vue'
import Vuex from 'vuex'

import campgroundModule from '@/campground/campground-store'

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
