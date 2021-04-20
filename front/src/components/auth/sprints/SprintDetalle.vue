<template >
  <div v-if="loaded">
    <v-row>
      <v-col>
        <v-card color="#FAFAFA">
          <v-card-text>
            <v-alert>
              <v-chip
                @click="goBack()"
                class="ma-2"
                color="primary"
                text-color="white"
              >
                <v-icon left> mdi-arrow-left-circle </v-icon>
                Volver
              </v-chip>
              <h2 class="text-center">{{ sprint.name.toUpperCase() }}</h2>
              <h3 class="text-center">
                {{ sprint.objetive.toUpperCase() }}
              </h3>
            </v-alert>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-card>
                    <v-card-title>Listado de Tareas</v-card-title>
                    <v-card-text>
                      <div>
                        <v-data-table
                          :headers="headers"
                          :items="sprint.tasks"
                          :page.sync="page"
                          :items-per-page="itemsPerPage"
                          hide-default-footer
                          class="elevation-1"
                          @page-count="pageCount = $event"
                        >
                          <template v-slot:[`item.state`]="{ item }">
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
                          <template v-slot:[`item.priority`]="{ item }">
                            <v-chip
                              v-if="item.priority == 'Alta'"
                              color="red"
                              dark
                            >
                              {{ item.priority }}
                            </v-chip>
                            <v-chip
                              v-if="item.priority == 'Media'"
                              color="yellow"
                              dark
                            >
                              {{ item.priority }}
                            </v-chip>
                            <v-chip
                              v-if="item.priority == 'Baja'"
                              color="success"
                              dark
                            >
                              {{ item.priority }}
                            </v-chip>
                          </template>
                          <template v-slot:[`item.hours`]="{ item }">
                            <v-chip color="primary">
                              {{ item.hours }} H
                            </v-chip>
                          </template>
                          <template v-slot:[`item.user.name`]="{ item }">
                            <v-chip style="width: 100%" color="primary">
                              {{ item.user.name }}
                              {{ item.user.lastName }}</v-chip
                            >
                          </template>
                          <template v-slot:[`item.acciones`]="{ item }">
                            <v-icon dense class="mr-2" @click="editItem(item)">
                              mdi-pencil
                            </v-icon>
                            <v-icon dense @click="deleteItem(item)">
                              mdi-delete
                            </v-icon>
                          </template>
                        </v-data-table>
                        <div class="text-center pt-2">
                          <v-pagination
                            v-model="page"
                            :length="pageCount"
                            circle
                          ></v-pagination>
                        </div>
                      </div> </v-card-text
                    ><v-btn
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
            </v-container>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

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
import Swal from "sweetalert2";
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
        { text: "Nombre", value: "name", align: "center" },
        {
          text: "Descripción",
          value: "description",
          align: "center",
        },
        { text: "Estado", value: "state", align: "center" },
        {
          text: "Prioridad",
          value: "priority",
          align: "center ",
        },
        { text: "Estimado", value: "hours", align: "center" },
        {
          text: "Responsable",
          value: "user.name",
          align: "center",
        },
        { text: "Acciones", value: "acciones", sortable: false },
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
    deleteItem(item) {
      Swal.fire({
        title: "Tarea : " + item.name,
        showDenyButton: true,
        confirmButtonText: `Eliminar`,
      }).then(async (result) => {
        if (result.isConfirmed) {
          for (let task of this.sprint.tasks) {
            if (item == task) this.sprint.tasks.pop(task);
          }
          const token = localStorage.getItem("token");
          await axios
            .delete(this.apiUrl + "deleteTask/" + item.id, {
              headers: {
                Authorization: token,
              },
            })
            .then(() => Swal.fire("Eliminado!", "", "success"))
            .catch((error) => console.log(error));
        }
      });
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
<style scoped>
swal-title {
  margin: 0px;
  font-size: 245px;
  box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.21);
  margin-bottom: 28px;
  color: red;
}
</style>