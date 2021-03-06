import Vue from 'vue'
import VueRouter from 'vue-router'
import authRoutes from '@/auth/auth-routes'
import campgroundRoutes from '@/campground/campground-routes'
import campsiteRoutes from '@/campsite/campsite-routes'
import orderRoutes from '@/order/order-routes'
import reservationRoutes from '@/reservation/reservation-routes'

Vue.use(VueRouter)

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    ...authRoutes,
    ...campgroundRoutes,
    ...campsiteRoutes,
    ...orderRoutes,
    ...reservationRoutes,
    {
      path: '/404',
      name: '404',
      component: require('./common/404').default,
      props: true
    },
    {
      path: '*',
      redirect: '404'
    }
  ]
})

export default router
