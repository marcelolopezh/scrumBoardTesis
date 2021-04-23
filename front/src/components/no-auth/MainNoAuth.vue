<template>
  <v-container  fill-height>
    <v-row>
      <v-col cols="5" class="justify-center ma-auto full-height">
        <h1>ScrumBoard - Taller de Integracion V</h1>
      </v-col>
      <v-col cols="7">
        <div v-if="showLogin">
          <Login
            @showLogin="showLogin = $event"
            @showRegister="showRegister = $event"
          />
        </div>
        <div v-if="showRegister">
          <Registro
            @showLogin="showLogin = $event"
            @showRegister="showRegister = $event"
          />
        </div>
      </v-col>
    </v-row>
  </v-container>
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

<style scoped>

</style>