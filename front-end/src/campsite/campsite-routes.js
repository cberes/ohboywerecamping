export default [
  {
    path: '/campsites',
    name: 'campsite-list',
    component: require('./campsite-list-view').default
  },
  {
    path: '/campsite/:id',
    name: 'campsite',
    component: require('./campsite-view').default,
    props: true
  }
]
