<template>
  <v-app id="inspire" dark v-cloak>
    <div>
      <v-navigation-drawer v-model="drawer" app dark v-if="isLogged">
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
        <router-view />
        <v-row>
        
        </v-row>
      </v-container>
    </v-main>
    <v-footer dark padless>
      <Footer />
    </v-footer>
  </v-app>
</template>

<script>
import navDrawer from "./components/navDrawer";
import Footer from "./components/Footer";
export default {
  name: "Home",
  components: {
    navDrawer,
    Footer,
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
      if (token == null || email == null) {
        this.isLogged = false;
      } else {
        this.selectedItem = "db";
        const axios = require("axios");
        await axios
          .get(this.apiUrl, {
            headers: {
              Authorization: token,
            },
          })
          .then(() => {
            this.isLogged = true;
          })
          .catch(() => {
            this.isLogged = false;
          });
      }
      if (!this.isLogged)
        this.$router.push("/login").catch((err) => {
          if (
            err.name !== "NavigationDuplicated" &&
            !err.message.includes(
              "Avoided redundant navigation to current location"
            )
          ) {
            console.log(err);
          }
        });
      else {
        console.log("te wa tirar al dash");
        this.$router.push("/app").catch((err) => {
          if (
            err.name !== "NavigationDuplicated" &&
            !err.message.includes(
              "Avoided redundant navigation to current location"
            )
          ) {
            console.log(err);
          }
        });
      }
    },
  },
};
</script>

<style>
router-link,
a {
  text-decoration: none;
}
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
