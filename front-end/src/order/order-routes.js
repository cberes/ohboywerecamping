export default [
  {
    path: '/orders',
    name: 'orders',
    component: require('./orders-view').default
  },
  {
    path: '/order/:id',
    name: 'order',
    component: require('./order-view').default,
    props: true
  }
]
