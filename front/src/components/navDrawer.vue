<template>
  <v-card class="mx-auto" max-width="300" tile>
    <v-list dense>
      <v-subheader>Secciones</v-subheader>
      <v-list-item-group color="primary">
        <router-link to="/app/dashboard">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>DashBoard</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link to="/app/proyectos">
          <v-list-item> 
            <v-list-item-icon>
              <v-icon>mdi-folder-star-multiple</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Proyectos</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        
        <v-list-item @click="logout()">
          <v-list-item-icon>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>Logout</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
  </v-card>
</template>

<script>
import { defineComponent } from "@vue/composition-api";

export default defineComponent({
  name: "navDrawer",
  setup() {},
  data() {
    return {};
  },
  methods:{
    logout(){
      localStorage.removeItem("token")
      localStorage.removeItem("email")
      this.$router.go("/login")
    }
  }
});
</script>
