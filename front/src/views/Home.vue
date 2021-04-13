<template>
  <v-app id="inspire" dark>
    <div v-if="!isLogged">
      <v-bottom-navigation color="primary">
        <v-btn
          @click="
            showLogin = true;
            showRegister = false;
          "
        >
          <span>Login</span>
          <v-icon>mdi-location-enter</v-icon>
        </v-btn>
        <v-btn
          @click="
            showLogin = false;
            showRegister = true;
          "
        >
          <span>Registro</span>
          <v-icon>mdi-account-plus-outline</v-icon>
        </v-btn>
      </v-bottom-navigation>
    </div>

    <div v-if="!isLogged">
      <transition name="bounce">
        <div class="mt-5" v-if="showLogin">
          <Login
            class="mt-5"
            @isLogged="isLogged = $event"
            @selectedItem="selectedItem = $event"
            @user="user = $event"
          />
        </div>
      </transition>
      <transition name="bounce">
        <div class="mt-5" v-if="showRegister">
          <Registro class="mt-5" />
        </div>
      </transition>
    </div>

    <div v-if="isLogged">
      <v-navigation-drawer v-model="drawer" app dark>
        <navDrawer
          @itemFromNav="selectedItem = $event"
          @drawerOut="drawer = $event"
        />
      </v-navigation-drawer>

      <v-app-bar app dark>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

        <v-toolbar-title>ScrumBoard</v-toolbar-title>
      </v-app-bar>
    </div>

    <v-main>
      <v-container>
     
        <v-row>
          <v-col>
            <div v-if="selectedItem == 'db'">
              <DashBoard />
            </div>
            <div v-if="selectedItem == 'mp'">
              <Proyectos />
            </div>
            <div v-if="selectedItem == 'mt'">
              <Tareas />
            </div>
            <div v-if="selectedItem == 'lo'">
              <Logout
                @isLoggedFalse="isLogged = $event"
                @showLoginTrue="showLogin = $event"
              />
            </div>
          </v-col>
        </v-row>
      </v-container>
    </v-main>
    <v-footer dark padless>
      <Footer />
    </v-footer>
  </v-app>
</template>

<script>
import navDrawer from "../components/navDrawer";
import DashBoard from "../components/dashboard/DashBoard";
import Proyectos from "../components/proyectos/Proyectos";
import Tareas from "../components/tareas/Tareas";
import Footer from "../components/Footer";
import Login from "../components/login/Login";
import Logout from "../components/login/Logout";
import Registro from "../components/registro/Registro";
export default {
  name: "Home",
  components: {
    navDrawer,
    DashBoard,
    Proyectos,
    Tareas,
    Footer,
    Login,
    Logout,
    Registro,
  },
  data() {
    return {
      drawer: false,
      selectedItem: null,
      isLogged: false,
      token: null,
      showLogin: false,
      showRegister: false,
      user: null,
      apiUrl: "http://localhost:8080/",
      response: null,
      errors: null,
    };
  },
  mounted() {
    // CONFIRMAR TOKEN
    this.isTokenPresent();
  },
  methods: {
    async isTokenPresent() {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      if (token == null || email == null) this.isLogged = false;
      else {
        this.selectedItem = "db";
        const axios = require("axios");
        await axios
          .get(this.apiUrl, {
            headers: {
              Authorization: token,
            },
          })
          .then(() => (this.isLogged = true))
          .catch(() => (this.isLogged = false));
      }
      if (!this.isLogged) this.showLogin = true;
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.bounce-enter-active {
  animation: bounce-in 0.2s;
}
.bounce-leave-active {
  animation: bounce-in 0.2s reverse;
}
@keyframes bounce-in {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);
  }
}
</style>
