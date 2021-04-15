<template >
  <v-app id="inspire" dark v-cloak>
    <div>
      <v-navigation-drawer v-model="drawer" app dark>
        <navDrawer />
      </v-navigation-drawer>

      <v-app-bar app dark>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

        <v-toolbar-title>ScrumBoard</v-toolbar-title>
      </v-app-bar>
    </div>

    <v-main>
      <v-container>
        <router-view />
        <v-row> </v-row>
      </v-container>
    </v-main>
    <v-footer dark padless>
      <Footer />
    </v-footer>
  </v-app>
</template>


<script>
import navDrawer from "../navDrawer";
export default {
  name: "Main",
  components: {
    navDrawer,
  },
  data() {
    return {
      drawer: true,
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
    },
  },
};
</script>