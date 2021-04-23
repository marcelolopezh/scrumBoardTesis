<template  >
  <v-app id="inspire" v-cloak>
    <router-view></router-view>
  </v-app>
</template>

<script>
export default {
  name: "Home",
  components: {},
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
      if (this.isLogged) this.$router.push("/app");
      else this.$router.push("/login");
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
div[data-app='true'] {
  background: url('assets/conectados.png') no-repeat  center bottom   !important;
  background-size: cover;
}
</style>
