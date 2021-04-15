import Vue from 'vue'
import VueRouter from 'vue-router'
import Auth from "../components/auth/Main.vue"
import NoAuth from "../components/no-auth/Main.vue"
import DashBoard from "../components/auth/dashboard/DashBoard.vue"
import Proyectos from "../components/auth/proyectos/Proyectos.vue"
Vue.use(VueRouter)

const routes = [

  { path: '/login', component: NoAuth },
  {
    path: '/app', component: Auth, children: [
      { path: 'dashboard', component: DashBoard },
      { path: 'proyectos', component: Proyectos }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
