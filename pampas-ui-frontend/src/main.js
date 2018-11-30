import Vue from 'vue'

//对应的css
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
// import 'element-ui/lib/theme-chalk/index.css'
import '../theme/element/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css
import '@/styles/custom.css' // global css
import '@/styles/cover-el.css' // global css

import App from './App'
import router from './router'
import store from './store'

import '@/icons' // icon
// import '@/permission' // permission control


Vue.use(ElementUI, { locale })

Vue.config.productionTip = false


new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
