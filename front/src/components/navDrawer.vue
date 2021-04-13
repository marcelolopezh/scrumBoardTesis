<template>
  <v-card class="mx-auto" max-width="300" tile>
    <v-list rounded>
      <v-subheader>Secciones</v-subheader>
      <v-list-item-group color="primary">
        <router-link to="/">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-view-dashboard</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>DashBoard</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <router-link to="/proyectos">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-folder-star-multiple</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Proyectos</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-help-box</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>Tareas</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        <router-link to="/logout">
          <v-list-item>
            <v-list-item-icon>
              <v-icon>mdi-exit-to-app</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Logout</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </router-link>
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
});
</script>
