<template >
  <div v-if="loaded">
    <v-card>
      <v-card-text>
        <v-row class="pa-5">
          <v-col>
            <v-card>
              <v-card-title
                ><v-icon @click="goBack()" color="primary" class="mr-2"
                  >mdi-arrow-left-circle</v-icon
                >{{ sprint.name }} - {{ sprint.objetive }}</v-card-title
              >
              <v-card-text>
                <v-container>
                  <v-data-table
                    
                    :headers="headers"
                    :items="sprint.tasks"
                    :page.sync="page"
                    :items-per-page="itemsPerPage"
                    hide-default-footer
                    class="elevation-1"
                    @page-count="pageCount = $event"
                  >
                    <template v-slot:item.state="{ item }">
                      <v-chip
                        style="width: 100%"
                        v-if="item.state == 'No Iniciado'"
                        color="red"
                        dark
                      >
                        {{ item.state }}
                      </v-chip>
                      <v-chip
                        style="width: 100%"
                        v-if="item.state == 'Iniciado'"
                        color="yellow"
                        dark
                      >
                        {{ item.state }}
                      </v-chip>
                      <v-chip
                        style="width: 100%"
                        v-if="item.state == 'Terminado'"
                        color="green"
                        dark
                      >
                        {{ item.state }}
                      </v-chip>
                    </template>
                    <template v-slot:item.priority="{ item }">
                      <v-chip
                        style="width: 100%"
                        v-if="item.priority == 'Alta'"
                        color="red"
                        dark
                      >
                        {{ item.priority }}
                      </v-chip>
                      <v-chip
                        style="width: 100%"
                        v-if="item.priority == 'Media'"
                        color="yellow"
                        dark
                      >
                        {{ item.priority }}
                      </v-chip>
                      <v-chip
                        style="width: 100%"
                        v-if="item.priority == 'Baja'"
                        color="success"
                        dark
                      >
                        {{ item.priority }}
                      </v-chip>
                    </template>
                    <template v-slot:item.user.name="{ item }">
                      <v-chip style="width: 100%" color="primary">
                        {{ item.user.name }} {{ item.user.lastName }}</v-chip
                      >
                    </template>
                  </v-data-table>
                </v-container>
              </v-card-text>
              <v-btn
                :loading="loading"
                :disabled="loading"
                color="purple"
                class="ma-2 white--text float-right"
                @click="dialog = true"
              >
                Nueva Tarea
                <v-icon right dark> mdi-plus-circle-outline </v-icon>
              </v-btn>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- DIALOG NEW SPRINT -->
    <v-dialog v-model="dialog" max-width="500px">
      <v-card>
        <v-card-title> Crear Nueva Tarea </v-card-title>
        <v-card-text>
          <!-- FORM TO CREATE SPRINT -->
          <v-text-field
            v-model="taskName"
            label="Nombre Tarea"
            prepend-icon="mdi-card-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="taskDescription"
            label="Descripción Tarea"
            prepend-icon="mdi-clipboard-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="taskHours"
            label="Horas Estimadas"
            prepend-icon="mdi-clock"
            clearable
            number
            type="number"
          ></v-text-field>

          <v-select
            class="mb-5"
            v-model="taskPriority"
            :items="priorityList"
            menu-props="auto"
            label="Prioridad"
            hide-details
            prepend-icon="mdi-podium-silver"
            single-line
          ></v-select>

          <v-select
            v-model="responsable"
            :items="members"
            menu-props="auto"
            label="Responsable de Tarea"
            hide-details
            prepend-icon="mdi-account"
            single-line
          ></v-select> </v-card-text
        ><v-card-actions>
          <v-btn color="danger" text @click="dialog = false"> Cerrar </v-btn>
          <v-btn
            color="purple"
            text
            @click="
              dialog = false;
              createTask();
            "
          >
            Crear Tarea
          </v-btn>
        </v-card-actions></v-card
      >
    </v-dialog>
  </div>
</template>


<script>
import axios from "axios";
export default {
  name: "SprintDetalle",

  data() {
    return {
      apiUrl: "http://localhost:8080/",
      loaded: false,
      sprint: null,
      taskName: null,
      taskDescription: null,
      taskHours: null,
      dialog: false,
      page: 1,
      pageCount: 0,
      itemsPerPage: 10,
      headers: [
        { text: "Nombre", value: "name", width: "19%" },
        { text: "Descripción", value: "description", width: "40%" },
        { text: "Estado", value: "state", width: "14%" },
        { text: "Prioridad", value: "priority", width: "12%" },
        { text: "Responsable", value: "user.name", width: "15%" },
      ],
      priorityList: [
        { text: "Alta", value: "Alta" },
        { text: "Media", value: "Media" },
        { text: "Baja", value: "Baja" },
      ],
      project: null,
      members: [],
      responsable: null,
      loading: false,
      taskPriority: null,
    };
  },
  mounted() {
    this.id = this.$route.params.sprint;
    this.projectId = this.$route.params.id;
    this.getInfo();
  },
  methods: {
    getName() {
      return "Hola";
    },
    async createTask() {
      const token = localStorage.getItem("token");
      let formData = new FormData();
      formData.append("name", this.taskName);
      formData.append("description", this.taskDescription);
      formData.append("hours", this.taskHours);
      formData.append("priority", this.taskPriority);
      formData.append("responsable", this.responsable);
      formData.append("sprint", this.id);
      await axios
        .post(this.apiUrl + "createTask", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo())
        .catch((error) => console.log(error));
    },
    goBack() {
      this.$router.go(-1);
    },
    async getInfo() {
      this.members = [];
      let token = localStorage.getItem("token");
      await axios
        .get(this.apiUrl + "getInfoSprint/" + this.id, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.sprint = response.data))
        .catch((error) => console.log(error));
      console.log("THIS SPRINT =>");
      console.log(this.sprint);
      await axios
        .get(this.apiUrl + "getInfoProject/" + this.projectId, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.project = response.data))
        .catch((error) => console.log(error));

      for (var i = 0; i < this.project.members.length; i++) {
        this.members.push({
          text:
            this.project.members[i].name +
            " " +
            this.project.members[i].lastName,
          value: this.project.members[i].id,
        });
      }
      this.loaded = true;
    },
  },
};
</script>