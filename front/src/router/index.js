import Vue from 'vue'
import VueRouter from 'vue-router'
import Auth from "../components/auth/MainAuth.vue"
import App from "../App.vue"
import NoAuth from "../components/no-auth/MainNoAuth.vue"
import DashBoard from "../components/auth/dashboard/DashBoard.vue"
import Proyectos from "../components/auth/proyectos/Proyectos.vue"
import ProyectoDetalle from "../components/auth/proyectos/ProyectosDetalle.vue"
Vue.use(VueRouter)

const routes = [
  { path: '/', component: App },
  { path: '/login', component: NoAuth },
  {
    path: '/app', component: Auth, children: [
      { path: 'dashboard', component: DashBoard },
      { path: 'proyectos', component: Proyectos },
      { path: 'proyectos/:id', component: ProyectoDetalle }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
