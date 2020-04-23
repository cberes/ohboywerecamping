import Vue from 'vue'
import Vuex from 'vuex'
import authModule from '@/auth/auth-store'
import campgroundModule from '@/campground/campground-store'
import reservationModule from '@/reservation/reservation-store'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    auth: authModule,
    campgrounds: campgroundModule,
    reservation: reservationModule
  }
})
