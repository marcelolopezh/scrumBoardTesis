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
import moment from "moment";
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
      for (var i = 0; i < diff; i++) {
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
      var totaldays = this.getLabelsBurnDown(sprint).length-1;
      console.log("TOTALDAYS ="+ totaldays)
      var division = totalhours / totaldays;
      for (i = 0; i <= totaldays; i++) {
        data[i] = totalhours - division * (i);
      }
      return data;
    },
    hoursPerDay(sprint) {
      var allData = []
      var startDate = moment(sprint.startDate, "YYYY-MM-DDTHH:mm");
      var endDate = moment(sprint.endDate, "YYYY-MM-DDTHH:mm");
      var difference = moment(endDate.diff(startDate,'days'));
      difference=difference._i;

      startDate = moment(startDate, "DD-MM-YYYY").add(1, 'days');
      allData[0] = startDate.format();
      var hoursWorkedPerDay = []
      hoursWorkedPerDay[0] = 0
      for(var i = 1; i<=difference;i++){
        var aux = moment(startDate, "DD-MM-YYYY").add(i, 'days')
        allData[i] = aux.format();
        hoursWorkedPerDay[i] = 0;
      }
      var taskData = []
      for(i=0;i<sprint.tasks.length;i++){
        aux = [moment(sprint.tasks[i].finished_at, 'YYYY-MM-DDTHH:mm'),sprint.tasks[i].estimatedHours];
        taskData[i] = [aux[0].format(),aux[1]];
      }

      for(i=0;i<allData.length;i++){
        for(var j=0;j<taskData.length;j++){
          if(allData[i].split("T")[0] == taskData[j][0].split("T")[0]){
            hoursWorkedPerDay[i] = hoursWorkedPerDay[i] + taskData[j][1]
          }
        }
      }
      var totalHours = 0
      for( i = 0; i<sprint.tasks.length;i++){
        totalHours = totalHours + sprint.tasks[i].estimatedHours;
      }
      hoursWorkedPerDay[0] = totalHours;
      for (i=1;i<hoursWorkedPerDay.length;i++){
        console.log(totalHours - hoursWorkedPerDay[i])
        aux = hoursWorkedPerDay[i]
        hoursWorkedPerDay[i] = totalHours - hoursWorkedPerDay[i]
        totalHours = totalHours - aux
      }
      console.log(hoursWorkedPerDay)
      return hoursWorkedPerDay
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
        .catch((error) => console.log("error=>" + error));
    },
  },
};
</script>
