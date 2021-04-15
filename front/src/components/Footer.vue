<template class="text-center">
  <v-footer padless>
    <v-col class="text-center" cols="12">
      {{ new Date().getFullYear() }} — <strong>Vuetify</strong>
    </v-col>
  </v-footer>
</template>

<script>
import { defineComponent } from "@vue/composition-api";
export default defineComponent({
  name: "Footer",
});
</script>