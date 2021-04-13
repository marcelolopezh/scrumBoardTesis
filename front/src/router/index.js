import Vue from 'vue'
import VueRouter from 'vue-router'
import ProyectoDetalle from '../components/proyectos/ProyectosDetalle.vue'
import Logout from "../components/login/Logout.vue"
import Login from "../components/login/Login.vue"
import Registro from "../components/registro/Registro.vue"
import Dashboard from "../components/dashboard/DashBoard.vue"
Vue.use(VueRouter)

const routes = [
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/proyecto/:id',
    name: 'Proyecto',
    component: ProyectoDetalle,
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/logout',
    name: 'logout',
    component: Logout
  }, {
    path: '/registro',
    name: 'registro',
    component: Registro
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
