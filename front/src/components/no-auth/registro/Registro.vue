<template class="mt-5">
  <v-container>
    <v-row>
      <v-col cols="12">
        <v-card class="pa-6">
          <v-card-title>Registro</v-card-title>
          <v-card-text>
            <v-alert type="error" v-if="errorRegister">
              {{ errorMsg }}
            </v-alert>
            <v-alert type="success" v-if="successRegister">
              {{ successMsg }}
            </v-alert>
            <v-form ref="form">
              <v-text-field
                v-model="name"
                label="Nombre"
                required
                prepend-icon="mdi-account"
              ></v-text-field>
              <v-text-field
                v-model="lastName"
                label="Apellidos"
                required
                prepend-icon="mdi-account"
              ></v-text-field>
              <v-text-field
                v-model="email"
                label="Email"
                required
                prepend-icon="mdi-email"
              ></v-text-field>
              <v-text-field
                v-model="password"
                label="Password"
                required
                type="password"
                prepend-icon="mdi-lock"
              ></v-text-field>
              <v-text-field
                v-model="passwordConfirm"
                label="Confirmar Password"
                required
                type="password"
                prepend-icon="mdi-lock"
              ></v-text-field>
              <v-btn
                right
                color="secondary"
                class="mr-4"
                @click="pre_register()"
              >
                Registrarse
              </v-btn>

              <v-btn color="success" class="mr-4" @click="changeForms()">
                Ingresar
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
  name: "Registro",
  components: {},

  data() {
    return {
      apiUrl: process.env.VUE_APP_APIURL,
      name: null,
      lastName: null,
      password: null,
      email: null,
      passwordConfirm: null,
      token: null,
      errors: null,
      errorRegister: false,
      errorMsg: null,
      successMsg: null,
      successRegister: false,
    };
  },

  mounted() {},

  methods: {
    changeForms() {
      this.$emit("showLogin", true);
      this.$emit("showRegister", false);
    },
    validateEmail(email) {
      const re =
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(String(email).toLowerCase());
    },
    pre_register() {
      this.errorRegister = false;
      this.successRegister = false;

      if (
        this.name == null &&
        this.lastName == null &&
        this.email == null &&
        this.password == null &&
        this.passwordConfirm == null
      ) {
        this.errorMsg = "Campos Incorrectos";
        this.errorRegister = true;
        return;
      }
      if (!this.validateEmail(this.email)) {
        this.errorMsg = "El Email ingresado no posee el formato establecido";
        this.errorRegister = true;
        return;
      }
      if (this.password != this.passwordConfirm) {
        this.errorMsg = "Las contraseñas no coinciden";
        this.errorRegister = true;
        return;
      }
      this.register();
    },
    async register() {
      this.errors = null;
      let formData = new FormData();
      formData.append("name", this.name);
      formData.append("lastName", this.lastName);
      formData.append("email", this.email);
      formData.append("password", this.password);

      const axios = require("axios");
      // eslint-disable-next-line no-unused-vars
      const responsex = await axios
        .post(this.apiUrl, formData)
        .then((response) => console.log(response.data))
        .catch((error) => {
          this.errors = error;
          this.errorMsg = "El Correo ya se encuentra registrado";
          this.errorRegister = true;
        });
      if (this.errors != null) {
        this.errorMsg = "Algo ha salido mal";
        this.errorRegister = true;
      } else {
        this.successMsg =
          "Se ha registrado correctamente el email " + this.email;
        this.successRegister = true;
      }
    },
  },
};
</script>
