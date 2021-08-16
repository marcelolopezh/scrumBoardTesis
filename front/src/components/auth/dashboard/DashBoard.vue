<template>
  <div>
    <v-container fluid>
      <div v-for="project in MyProjectsAsOwner" :key="project.id">
        <v-card v-for="sprint in project.sprints" :key="sprint.id">
          <v-card-title
            >Resumen de Mi Proyecto : {{ project.name }}, Sprint
            {{ sprint.name }}</v-card-title
          >
          <v-card-text>
            <v-row>
              <v-col cols="9">
                <line-chart
                  :chartDataLabels="getLabelsBurnDown(sprint)"
                  :chartEstimatedWork="chartEstimatedWork(sprint)"
                  :hoursPerDay="hoursPerDay(sprint)"
                ></line-chart
              ></v-col>
              <v-col cols="3 mt-5">
                <div class="text-subtitle-1 mt-5 text-center">Líder</div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ project.user.name }} {{ project.user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">Miembros</div>
                <hr />
                <div
                  class="text-subtitle-1 mt-1 text-center font-weight-black"
                  v-for="user in project.members"
                  :key="user.id"
                >
                  {{ user.name }} {{ user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Tareas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ sprint.tasks.length }}
                </div>

                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Horas Estimadas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ getTotalHours(sprint.tasks) }}
                </div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </div>

      <div v-for="project in MyProjectsAsMember" :key="project.id">
        <v-card v-for="sprint in project.sprints" :key="sprint.id">
          <v-card-title
            >Resumen de Mi como Colaborador : {{ project.name }}, Sprint
            {{ sprint.name }}</v-card-title
          >
          <v-card-text>
            <v-row>
              <v-col cols="9">
                <line-chart
                  :chartDataLabels="getLabelsBurnDown(sprint)"
                  :chartEstimatedWork="chartEstimatedWork(sprint)"
                  :hoursPerDay="hoursPerDay(sprint)"
                ></line-chart
              ></v-col>
              <v-col cols="3 mt-5">
                <div class="text-subtitle-1 mt-5 text-center">Líder</div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ project.user.name }} {{ project.user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">Miembros</div>
                <hr />
                <div
                  class="text-subtitle-1 mt-1 text-center font-weight-black"
                  v-for="user in project.members"
                  :key="user.id"
                >
                  {{ user.name }} {{ user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Tareas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ sprint.tasks.length }}
                </div>

                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Horas Estimadas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ getTotalHours(sprint.tasks) }}
                </div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </div>

      <div v-for="project in MyProjectsAsIntersted" :key="project.id">
        <v-card v-for="sprint in project.sprints" :key="sprint.id">
          <v-card-title
            >Resumen de Mi Proyecto de Interés : {{ project.name }}, Sprint
            {{ sprint.name }}</v-card-title
          >
          <v-card-text>
            <v-row>
              <v-col cols="9">
                <line-chart
                  :chartDataLabels="getLabelsBurnDown(sprint)"
                  :chartEstimatedWork="chartEstimatedWork(sprint)"
                  :hoursPerDay="hoursPerDay(sprint)"
                ></line-chart
              ></v-col>
              <v-col cols="3 mt-5">
                <div class="text-subtitle-1 mt-5 text-center">Líder</div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ project.user.name }} {{ project.user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">Miembros</div>
                <hr />
                <div
                  class="text-subtitle-1 mt-1 text-center font-weight-black"
                  v-for="user in project.members"
                  :key="user.id"
                >
                  {{ user.name }} {{ user.lastName }}
                </div>
                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Tareas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ sprint.tasks.length }}
                </div>

                <div class="text-subtitle-1 mt-5 text-center">
                  Total de Horas Estimadas
                </div>
                <hr />
                <div class="text-subtitle-1 mt-1 text-center font-weight-black">
                  {{ getTotalHours(sprint.tasks) }}
                </div>
              </v-col>
            </v-row>
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
      MyProjectsAsOwner: null,
      MyProjectsAsMember: null,
      MyProjectsAsIntersted: null,
      chartData: null,
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
      },
    };
  },
  mounted() {
    this.getMyProjectsAsOwner();
    this.getMyProjectsAsMember();
    this.getMyProjectsAsInterested();
  },
  methods: {
    getLabelsBurnDown(sprint) {
      var startDate = Date.parse(sprint.startDate);
      var endDate = Date.parse(sprint.endDate);
      var diff = Math.round((endDate - startDate) / (1000 * 3600 * 24));
      var labels = [];

      var _startDate = moment(sprint.startDate, "YYYY-MM-DDTHH:mm");
      for (var i = 0; i < diff; i++) {
        var day = moment(_startDate, "DD-MM-YYYY").add(i, "days");
        labels[i] = day.format("DD-MM");
      }
      return labels;
    },
    chartEstimatedWork(sprint) {
      var data = [];
      var totalhours = 0;
      for (var i = 0; i < sprint.tasks.length; i++) {
        totalhours += sprint.tasks[i].estimatedHours;
      }
      var totaldays = this.getLabelsBurnDown(sprint).length - 1;
      var division = totalhours / totaldays;
      for (i = 0; i <= totaldays; i++) {
        data[i] = totalhours - division * i;
      }
      data[data.length - 1] = 0;
      return data;
    },
    hoursPerDay(sprint) {
      var allData = [];
      var startDate = moment(sprint.startDate, "YYYY-MM-DDTHH:mm");
      var endDate = moment(sprint.endDate, "YYYY-MM-DDTHH:mm");
      var difference = moment(endDate.diff(startDate, "days"));
      difference = difference._i;

      startDate = moment(startDate, "DD-MM-YYYY").add(1, "days");
      allData[0] = startDate.format();
      var hoursWorkedPerDay = [];
      hoursWorkedPerDay[0] = 0;
      for (var i = 1; i <= difference; i++) {
        var aux = moment(startDate, "DD-MM-YYYY").add(i, "days");
        allData[i] = aux.format();
        hoursWorkedPerDay[i] = 0;
      }
      var taskData = [];
      for (i = 0; i < sprint.tasks.length; i++) {
        aux = [
          moment(sprint.tasks[i].finished_at, "YYYY-MM-DDTHH:mm"),
          sprint.tasks[i].estimatedHours,
        ];
        taskData[i] = [aux[0].format(), aux[1]];
      }

      for (i = 0; i < allData.length; i++) {
        for (var j = 0; j < taskData.length; j++) {
          if (allData[i].split("T")[0] == taskData[j][0].split("T")[0]) {
            hoursWorkedPerDay[i] = hoursWorkedPerDay[i] + taskData[j][1];
          }
        }
      }
      var totalHours = 0;
      for (i = 0; i < sprint.tasks.length; i++) {
        totalHours = totalHours + sprint.tasks[i].estimatedHours;
      }
      hoursWorkedPerDay[0] = totalHours;
      for (i = 1; i < hoursWorkedPerDay.length; i++) {
        aux = hoursWorkedPerDay[i];
        hoursWorkedPerDay[i] = totalHours - hoursWorkedPerDay[i];
        totalHours = totalHours - aux;
      }
      return hoursWorkedPerDay;
    },
    async getMyProjectsAsOwner() {
      let email = localStorage.getItem("email");
      let token = localStorage.getItem("token");
      const axios = require("axios");
      let formData = new FormData();
      formData.append("email", email);
      formData.append("token", token);
      axios
        .post(this.apiUrl + "getMyProjects", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.MyProjectsAsOwner = response.data;
        })
        .catch((error) => console.log("error=>" + error));
    },
    async getMyProjectsAsMember() {
      let email = localStorage.getItem("email");
      let token = localStorage.getItem("token");
      const axios = require("axios");
      let formData = new FormData();
      formData.append("email", email);
      formData.append("token", token);
      axios
        .post(this.apiUrl + "getMyProjectsAsMember", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.MyProjectsAsMember = response.data;
        })
        .catch((error) => console.log("error=>" + error));
    },
    async getMyProjectsAsInterested() {
      let email = localStorage.getItem("email");
      let token = localStorage.getItem("token");
      const axios = require("axios");
      let formData = new FormData();
      formData.append("email", email);
      formData.append("token", token);
      axios
        .post(this.apiUrl + "getMyProjectsAsInterested", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.MyProjectsAsIntersted = response.data;
        })
        .catch((error) => console.log("error=>" + error));
    },
    getTotalHours(tasks) {
      var totalHours = 0;
      for (var i = 0; i < tasks.length; i++) {
        totalHours += tasks[i].estimatedHours;
      }
      return totalHours;
    },
  },
};
</script>
