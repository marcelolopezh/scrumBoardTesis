<template>
  <v-card class="mx-auto" max-width="300" tile>
    <v-list rounded>
      <v-subheader>Secciones</v-subheader>
      <v-list-item-group v-model="selectedItem" color="primary">
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          @click="itemClickled(item.value)"
        >
          <v-list-item-icon>
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="item.text"></v-list-item-title>
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
    return {
      items: [
        { text: "Dashboard", icon: "mdi-view-dashboard", value: "db" },
        { text: "Proyectos", icon: "mdi-folder-star-multiple", value: "mp" },
        { text: "Mis Tareas", icon: "mdi-help-box", value: "mt" },
        { text: "Logout", icon: "mdi-exit-to-app", value: "lo" },
      ],
      selectedItem: null,
    };
  },
  methods: {
    itemClickled(item) {
      this.selectedItem = item;
      this.$emit("itemFromNav", this.selectedItem);
      this.$emit("drawerOut",false)
    },
  },
});
</script>
