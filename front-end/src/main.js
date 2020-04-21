import Vue from 'vue'

// Element
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css'
import locale from 'element-ui/lib/locale/lang/en'

// Amplify
import Amplify, * as AmplifyModules from 'aws-amplify'
import { AmplifyPlugin } from 'aws-amplify-vue'
import awsConfig from './config/aws'

// this app ...
import App from './App.vue'
import router from './router'
import store from './store'

require('normalize-css')

Vue.config.productionTip = false
Vue.use(ElementUI, { locale })

Amplify.configure(awsConfig)
Vue.use(AmplifyPlugin, AmplifyModules)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
