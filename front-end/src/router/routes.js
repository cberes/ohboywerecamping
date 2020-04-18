export default [
  {
    path: '/',
    name: 'campground',
    component: require('@/views/campground').default
  },
  {
    path: '/campsites',
    name: 'campsite-list',
    component: require('@/views/campsite-list').default
  },
  {
    path: '/campsite/:id',
    name: 'campsite',
    component: require('@/views/campsite').default,
    props: true
  },
  {
    path: '/404',
    name: '404',
    component: require('@/views/404').default,
    props: true
  },
  {
    path: '*',
    redirect: '404'
  }
]
