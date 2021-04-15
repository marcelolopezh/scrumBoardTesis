<template >
  <v-row class="pa-5" v-if="loaded">
    <v-col>
      <v-card>
        <v-card-title
          ><v-icon @click="goBack()" color="primary" class="mr-2"
            >mdi-arrow-left-circle</v-icon
          >{{ project.name }} - {{ project.description }}</v-card-title
        >
        <v-card-text>
          <v-row>
            <v-col>
              <v-card>
                <v-card-title>Miembros Equipo</v-card-title>
                <v-card-text
                  ><v-list-item v-for="item in members" :key="item.id">
                    <v-list-item-icon>
                      <v-icon>mdi-account</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title
                        >{{ item.name }} {{ item.lastName }}</v-list-item-title
                      >
                    </v-list-item-content>
                  </v-list-item></v-card-text
                >
              </v-card>
            </v-col>
            <v-col>
              <v-card>
                <v-card-title>Clientes</v-card-title>
                <v-card-text>
                  <v-list-item v-for="item in clients" :key="item.id">
                    <v-list-item-icon>
                      <v-icon>mdi-account</v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title
                        >{{ item.name }} {{ item.lastName }}</v-list-item-title
                      >
                    </v-list-item-content>
                  </v-list-item></v-card-text
                >
              </v-card>
            </v-col>
          </v-row>
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
      members: [],
      clients: [],
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
          for (var i = 0; i < this.project.members.length; i++) {
            this.members.push(this.project.members[i]);
          }
          for (var j = 0; j < this.project.clients.length; j++) {
            this.clients.push(this.project.clients[j]);
          }
          console.log(this.project);
          this.loaded = true;
        });
    },
  },
};
</script>