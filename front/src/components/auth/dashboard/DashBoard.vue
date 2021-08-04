<template>
  <div>
    <v-container fluid>
      <div v-for="project in allData" :key="project.id">
        <v-card v-for="sprint in project.sprints" :key="sprint.id">
          <v-card-title
            >Resumen de {{ project.name }}, Sprint
            {{ sprint.name }}</v-card-title
          >
          <v-card-text>
            <line-chart
              :chartDataLabels="getLabelsBurnDown(sprint)"
              :chartEstimatedWork="chartEstimatedWork(sprint)"
              :hoursPerDay="hoursPerDay(sprint)"
            ></line-chart>
          </v-card-text>
        </v-card>
      </div>
    </v-container>
  </div>
</template>
<script>
import LineChart from "./LineChart.vue";
export default {
  components: { LineChart },
  name: "DashBoard",
  data() {
    return {
      apiUrl: process.env.VUE_APP_APIURL,
      allData: null,
      chartData: null,
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
  mounted() {
    this.getInfo();
  },
  methods: {
    getLabelsBurnDown(sprint) {
      var startDate = Date.parse(sprint.startDate);
      var endDate = Date.parse(sprint.endDate);
      var diff = Math.round((endDate - startDate) / (1000 * 3600 * 24));
      var labels = [];
      for (var i = 0; i <= diff; i++) {
        labels[i] = parseInt(i);
      }
      return labels;
    },
    chartEstimatedWork(sprint) {
      var data = [];
      var totalhours = 0;
      for (var i = 0; i < sprint.tasks.length; i++) {
        totalhours += sprint.tasks[i].estimatedHours;
      }
      var totaldays = this.getLabelsBurnDown(sprint).length;
      var division = totalhours / totaldays;
      for (i = 0; i < totaldays; i++) {
        data[i] = totalhours - division * (i + 1);
      }
      return data;
    },
    hoursPerDay(sprint) {
      console.log("estoy en hoursPerDay");
      console.log(sprint)
      for (var i = 0; i < sprint.tasks.length; i++) {
        if (sprint.tasks[i].started_at!=null && sprint.tasks[i].finished_at!=null) {
          var startDate = Date.parse(sprint.tasks[i].started_at);
            console.log(startDate)
        }
      }
      return sprint.id;
    },
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
