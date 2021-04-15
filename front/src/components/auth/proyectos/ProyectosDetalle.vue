<template >
  <v-row class="pa-5" v-if="loaded">
    <v-col>
      <v-card>
        <v-card-title
          ><v-icon @click="goBack()" color="primary" class="mr-2">mdi-arrow-left-circle</v-icon
          >{{ project.name }}</v-card-title
        >
        <v-card-text>
          {{ project.description }} <br />
          miembros =>{{ project.members }} <br />
          clientes =>{{ project.clients }}
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>


<script>
import axios from "axios";
export default {
  name: "ProyectosDetalle",

  data() {
    return {
      apiUrl: "http://localhost:8080/",
      id: null,
      project: null,
      loaded: false,
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.getInfo();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },

    async getInfo() {
      const token = localStorage.getItem("token");
      await axios
        .get(this.apiUrl + "getInfoProject/" + this.id, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.project = response.data;
          this.loaded = true;
        });
    },
  },
};
</script>