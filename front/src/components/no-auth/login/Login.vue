<template class="mt-5">
  <v-container>
    <v-row>
      <v-col cols="12" >
        <v-card class="pa-6">
          <v-card-title>Inicio de Sesión</v-card-title>
          <v-card-text>
            <v-alert type="error" v-if="errorLogin">
              Ups! Algo ha salido mal
            </v-alert>
            <v-form ref="form">
              <v-text-field
                v-model="email"
                label="Username"
                required
                prepend-icon="mdi-account"
              ></v-text-field>
              <v-text-field
                v-model="password"
                label="Password"
                required
                type="password"
                prepend-icon="mdi-lock"
                autocomplete="on"
              ></v-text-field>
              <v-btn
                :loading="loading"
                color="success"
                class="mr-4"
                @click="pre_login()"
              >
                Ingresar
              </v-btn>

              <v-btn color="secondary" class="mr-4" @click="changeForms()">
                Registrarse
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "Login",
  components: {},

  data() {
    return {
      apiUrl: "http://localhost:8080/login",
      email: null,
      password: null,
      token: null,
      errors: null,
      errorLogin: false,
      isLogged: false,
      data: null,
      loading: false,
    };
  },

  mounted() {},

  methods: {
    changeForms() {
      this.$emit("showLogin", false);
      this.$emit("showRegister", true);
    },
    pre_login() {
      this.loading = true;
      this.login();
    },
    async login() {
      this.errors = null;
      let formData = new FormData();
      formData.append("email", this.email);
      formData.append("password", this.password);
      formData.append("date", Date());

      const axios = require("axios");
      // eslint-disable-next-line no-unused-vars
      const responsex = await axios
        .post(this.apiUrl, formData)
        // eslint-disable-next-line no-unused-vars
        .then((response) => (this.data = response))
        .catch((error) => {
          this.errors = error;
          console.log(this.errors);
        });
      if (this.errors == null) {
        /*
        # Es necesario Utilizar
        # JWT y almacenarlo en
        # LocalStorage
        */
        localStorage.setItem("token", this.data.data.token);
        localStorage.setItem("email", this.email);
        this.$router.go("/app");
      } else {
        this.errorLogin = true;
      }
      this.loading = false;
    },
  },
};
</script>
