<template >
  <v-row class="pa-5">
    <v-col>
      <v-card-text v-if="showLogin"
        ><Login
          @showLogin="showLogin = $event"
          @showRegister="showRegister = $event"
      /></v-card-text>
      <v-card-text v-if="showRegister"
        ><Registro
          @showLogin="showLogin = $event"
          @showRegister="showRegister = $event"
      /></v-card-text>
    </v-col>
  </v-row>
</template>


<script>
import Login from "./login/Login";
import Registro from "./registro/Registro";
export default {
  name: "Main",
  components: { Login, Registro },
  data() {
    return {
      showLogin: true,
      showRegister: false,
    };
  },
  mounted() {},
  methods: {},
};
</script>