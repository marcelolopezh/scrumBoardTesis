<template >
  <v-app id="inspire" dark v-cloak>
    <div>
      <v-navigation-drawer v-model="drawer" app dark >
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
import navDrawer from "../navDrawer"
export default {
  name: "Main",
  components : {
    navDrawer,
  },
  data() {
    return {
       drawer: true,
    };
  },
  mounted() {},
  methods: {},
};
</script>