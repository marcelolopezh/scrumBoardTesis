<template class="mt-5">
  <v-container fill-height>
    <v-row justify="center" align="center">
      <v-col cols="12" sm="4">
        <v-card class="pa-6">
          <v-card-title>Inicio de Sesión</v-card-title>
          <v-alert type="error" v-if="errorLogin">
            Ups! Algo ha salido mal <v-icon>mdi-emoticon-sad-outline</v-icon>
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
              right
              color="success"
              class="mr-4"
              @click="pre_login()"
            >
              Ingresar
            </v-btn>
          </v-form>
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
        localStorage.setItem("email", this.email)
        const user = {
          id: this.data.data.id,
          completeName: this.data.data.completeName,
        };
        this.$emit("isLogged", true);
        this.$emit("user", user);
        this.$emit("selectedItem", "db");
      } else {
        this.errorLogin = true;
      }
      this.loading = false;
    },
  },
};
</script>
