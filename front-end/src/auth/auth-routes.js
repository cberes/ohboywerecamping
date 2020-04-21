export default [
  {
    path: '/register',
    name: 'register',
    component: require('./register-view').default
  },
  {
    path: '/signin',
    name: 'signin',
    component: require('./signin-view').default
  },
  {
    path: '/verify',
    name: 'verify',
    component: require('./verify-view').default
  },
  {
    path: '/authenticate',
    name: 'authenticate',
    component: require('./authenticate-view').default
  }
]
