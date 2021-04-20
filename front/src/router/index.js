import Vue from 'vue'
import VueRouter from 'vue-router'
import Auth from "../components/auth/MainAuth.vue"
import App from "../App.vue"
import NoAuth from "../components/no-auth/MainNoAuth.vue"
import Home from "../components/auth/home/Home.vue"
import DashBoard from "../components/auth/dashboard/DashBoard.vue"
import Proyectos from "../components/auth/proyectos/Proyectos.vue"
import ProyectoDetalle from "../components/auth/proyectos/ProyectosDetalle.vue"
import SprintDetalle from "../components/auth/sprints/SprintDetalle.vue"
Vue.use(VueRouter)

const routes = [
  { path: '/', component: App },
  { path: '/login', component: NoAuth },
  {
    path: '/app', component: Auth, children: [
      { path: 'home', component : Home},
      { path: 'dashboard', component: DashBoard },
      { path: 'proyectos', component: Proyectos },
      { path: 'proyectos/:id', component: ProyectoDetalle },
      { path: 'proyectos/:id/:sprint', component: SprintDetalle }
    ]
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
