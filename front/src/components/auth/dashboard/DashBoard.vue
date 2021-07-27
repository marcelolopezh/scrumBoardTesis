<template>
  <div>
    <v-container fluid>
      <v-card>
        <v-card-title>Resumen de Proyectos</v-card-title>
        <v-card-text>
          <v-list-item v-for="project in allData" :key="project.id">
            <v-col cols="6" align="center" justify="center">
              <v-card class="mt-4" max-width="400">
                <v-sheet
                  class="v-sheet--offset mx-auto"
                  color="cyan"
                  max-width="calc(100% - 32px)"
                >
                  <v-sparkline
                    :labels="labels"
                    :value="value"
                    color="white"
                    line-width="2"
                    padding="16"
                  ></v-sparkline>
                </v-sheet>

                <v-card-text class="pt-0">
                  <div class="title font-weight-light mb-2">
                    {{ project.name.toUpperCase() }}
                  </div>
                  <div class="subheading font-weight-light grey--text">
                    {{ project.description.toUpperCase() }}
                  </div>
                  <v-divider class="my-2"></v-divider>
                  <v-icon class="mr-2" small> mdi-clock </v-icon>
                  <span class="caption grey--text font-weight-light"
                    >Última modificación {{  project.updatedAt | moment("YYYY-MM-DD - HH:MM")}}</span
                  >
                </v-card-text>
              </v-card>
            </v-col>
          </v-list-item>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>
<script>
export default {
  name: "DashBoard",
  data() {
    return {
      apiUrl: process.env.APIURL,
      labels: ["12am", "3am", "6am", "9am", "12pm", "3pm", "6pm", "9pm"],
      value: [200, 675, 410, 390, 310, 460, 250, 240],
      allData: null,
    };
  },
  mounted() {
    this.getInfo();
  },
  methods: {
    async getInfo() {
      let email = localStorage.getItem("email");
      let token = localStorage.getItem("token");
      const axios = require("axios");
      let formData = new FormData();
      formData.append("email", email);
      axios
        .post(this.apiUrl + "getAllData", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.allData = response.data;
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>