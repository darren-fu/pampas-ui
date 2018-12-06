import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '../views/layout/Layout'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)


/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {path: '/login', component: () => import('@/views/login/index'), hidden: true},
  {path: '/404', component: () => import('@/views/404'), hidden: true},

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },

  {
    path: '/service',
    component: Layout,
    redirect: '/service/index',
    name: 'service',
    meta: {title: '服务管理', icon: 'example'},
    children: [

      {
        path: 'index',
        name: 'service-index',
        component: () => import('@/views/service/index'),
        meta: {title: '服务列表', icon: 'list'}
      },
      {
        path: 'add',
        name: 'service-add',
        component: () => import('@/views/service/service-add'),
        meta: {title: '添加服务', icon: 'edit', noCache: true},
        hidden: false
      },
      {
        path: 'edit',
        name: 'service-edit',
        component: () => import('@/views/service/service-edit'),
        meta: {title: '编辑服务', icon: 'edit'},
        hidden: true
      },
      {
        path: 'registry/index',
        name: 'registry-index',
        component: () => import('@/views/registry/index'),
        meta: {title: '注册中心', icon: 'list'}
      },
      {
        path: 'registry/add',
        name: 'registry-add',
        component: () => import('@/views/registry/registry-edit'),
        meta: {title: '添加注册中心', icon: 'list'},
        hidden: true
      },
      {
        path: 'registry/edit',
        name: 'registry-edit',
        component: () => import('@/views/registry/registry-edit'),
        meta: {title: '编辑注册中心', icon: 'list'},
        hidden: true
      },
    ]
  },
  {
    path: '/rule',
    component: Layout,
    redirect: '/rule/index',
    name: 'rule',
    meta: {title: '路由管理', icon: 'nested'},
    hidden: false,
    children: [{
      path: 'index',
      name: 'rule-index',
      component: () => import('@/views/rule/index'),
      meta: {title: '路由列表', icon: 'list'},

    },
      {
        path: 'add',
        name: 'rule-add',
        component: () => import('@/views/rule/rule-edit'),
        meta: {title: '新增路由', icon: 'edit'},
      },
      {
        path: 'edit',
        name: 'rule-edit',
        component: () => import('@/views/rule/rule-edit'),
        meta: {title: '配置路由', icon: 'edit'},
        hidden: true
      }]
  },
  {
    path: '/gateway',
    component: Layout,
    redirect: '/gateway/index',
    name: 'gateway',
    meta: {title: '网关管理', icon: 'guide'},
    hidden: false,
    children: [{
      path: 'index',
      name: 'gateway-index',
      component: () => import('@/views/gateway/index'),
      meta: {title: '网关列表', icon: 'list'},
    },
      {
        path: 'add',
        name: 'gateway-rel-rule',
        component: () => import('@/views/gateway/gateway-rel-rule'),
        meta: {title: '关联路由', icon: 'drag'},
      }]
  },
  {path: '*', redirect: '/404', hidden: true}
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
