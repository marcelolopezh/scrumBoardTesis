<template >
  <v-app id="inspire" dark v-cloak>
    <div>
      <v-navigation-drawer v-model="drawer" app dark>
        <navDrawer />
      </v-navigation-drawer>
      <router-link to="/app/home" id="routerLinkTo"></router-link>

      <v-app-bar app dark>
        <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

        <v-toolbar-title>ScrumBoard</v-toolbar-title>
        <v-spacer></v-spacer>

        <v-btn icon>
          <v-icon>mdi-account</v-icon>
        </v-btn>

        <v-menu left bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-bell-ring</v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item v-for="n in 3" :key="n" @click="() => {}">
              <v-list-item-title>Option {{ n }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-app-bar>
    </div>

    <v-main>
      <v-container>
        <router-view />

        <v-row> </v-row>
      </v-container>
    </v-main>
    <v-footer dark>
      <v-col class="justify-center" cols="12">
        <v-card class="d-flex align-center justify-center pa-4 mx-auto">
          <div>
            Plataforma virtual para gestión de proyectos mediante metodología
            Scrum. Trabajo de Título en desarrollo por Marcelo López.
          </div>
        </v-card>
      </v-col>
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
    document.querySelector("#routerLinkTo").click();
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