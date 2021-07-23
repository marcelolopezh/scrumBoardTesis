<template>
  <div>
    <v-row v-if="loaded">
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
              <h2 class="text-center">{{ project.name.toUpperCase() }}</h2>
              <h3 class="text-center">
                {{ project.description.toUpperCase() }}
              </h3>
            </v-alert>

            <v-container fluid>
              <v-row>
                <v-col cols="12" class="text-right">
                  <v-btn class="ma-2" color="primary" dark>
                    Miembros
                    <v-icon dark right> mdi-account-plus </v-icon>
                  </v-btn>
                  <v-btn class="ma-2" color="primary" dark>
                    Interesados
                    <v-icon dark right> mdi-account-plus </v-icon>
                  </v-btn>
                </v-col>
              </v-row>

              <v-card>
                <v-card-title>Listado de Sprints </v-card-title>
                <v-card-text>
                  <v-row>
                    <v-col
                      class="rounded-lg"
                      v-for="sprint in project.sprints"
                      :key="sprint.id"
                    >
                      <v-card color="#EEEEEE" elevation="2" outlined shaped>
                        <v-card-text>
                          <div>
                            <h3 class="text-center" v-text="sprint.name"></h3>
                            <h4
                              class="text-center"
                              v-text="sprint.objetive"
                            ></h4>
                            <v-divider class="mt-5 mb-5"></v-divider>

                            <v-expansion-panels class="mb-6">
                              <v-expansion-panel
                                v-for="task in sprint.tasks"
                                :key="task.id"
                                v-bind:style="{ maxHeight: 10 + '%' }"
                              >
                                <v-expansion-panel-header>
                                  <div v-text="task.name"></div
                                ></v-expansion-panel-header>
                                <v-expansion-panel-content>
                                  <div
                                    class="text-center"
                                    v-text="task.description"
                                  ></div>
                                  <v-chip-group
                                    active-class="primary--text"
                                    column
                                  >
                                    <v-chip color="indigo" text-color="white">
                                      <span v-text="task.estimatedHours"></span>
                                      <v-avatar>
                                        <v-icon>mdi-calendar-clock</v-icon>
                                      </v-avatar>
                                    </v-chip>
                                  </v-chip-group>
                                  <v-text-field
                                    v-on:keyup.enter="createSubTask(task.id)"
                                    v-model="addSubTask"
                                    placeholder="Check List"
                                  >
                                    <v-icon slot="append" color="green">
                                      mdi-check
                                    </v-icon>
                                  </v-text-field>
                                  <div
                                    v-for="subtask in task.subtasks"
                                    :key="subtask.id"
                                  >
                                    <v-checkbox
                                      :label="subtask.description"
                                      color="success"
                                      :value="subtask.id"
                                      hide-details
                                      :checked="subtask.state"
                                    ></v-checkbox>
                                  </div>
                                </v-expansion-panel-content>
                              </v-expansion-panel>
                            </v-expansion-panels>
                            <div class="my-2 text-center">
                              <v-btn
                                small
                                color="success"
                                dark
                                @click="dialogTask = true"
                              >
                                Crear Tarea
                                <v-icon dark right> mdi-plus </v-icon>
                              </v-btn>
                            </div>
                          </div>
                        </v-card-text>
                      </v-card>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
            </v-container>
            <v-row>
              <v-col cols="12" class="text-right">
                <v-btn
                  class="ma-2"
                  color="primary"
                  dark
                  @click="dialogSprint = true"
                >
                  Crear Sprint
                  <v-icon dark right> mdi-plus </v-icon>
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
    <!-- DIALOG NEW TASK -->
    <v-dialog v-model="dialogTask" max-width="500px">
      <v-card>
        <v-card-title> Crear Nueva Tarea </v-card-title>
        <v-card-text>
          <!-- FORM TO CREATE TASK -->
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
            v-model="taskEstimatedHours"
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
          <v-btn color="danger" text @click="dialogTask = false">
            Cerrar
          </v-btn>
          <v-btn
            color="purple"
            text
            @click="
              dialogTask = false;
              createTask();
            "
          >
            Crear Tarea
          </v-btn>
        </v-card-actions></v-card
      >
    </v-dialog>
    <!-- DIALOG NEW SPRINT -->
    <v-dialog v-model="dialogSprint" max-width="500px">
      <v-card>
        <v-card-title> Crear Nuevo Sprint </v-card-title>
        <v-card-text>
          <!-- FORM TO CREATE SPRINT -->
          <v-text-field
            v-model="sprintName"
            label="Nombre Sprint"
            prepend-icon="mdi-card-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="sprintObjetive"
            label="Objetivo Sprint"
            prepend-icon="mdi-clipboard-text"
            clearable
          ></v-text-field>

          <!-- PICK START DATE -->
          <v-dialog
            ref="dialogStartSprint"
            v-model="modalStartSprint"
            :return-value.sync="sprintStartDate"
            persistent
            width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="sprintStartDate"
                label="Fecha de Inicio"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="sprintStartDate" scrollable>
              <v-btn text color="primary" @click="modalStartSprint = false">
                Cancel
              </v-btn>
              <v-btn
                text
                color="primary"
                @click="$refs.dialogStartSprint.save(sprintStartDate)"
              >
                OK
              </v-btn>
            </v-date-picker>
          </v-dialog>

          <!-- PICK END DATE -->
          <v-dialog
            ref="dialogEndSprint"
            v-model="modalEndSprint"
            :return-value.sync="sprintEndDate"
            persistent
            width="290px"
          >
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="sprintEndDate"
                label="Fecha de Inicio"
                prepend-icon="mdi-calendar"
                readonly
                v-bind="attrs"
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker v-model="sprintEndDate" scrollable>
              <v-spacer></v-spacer>
              <v-btn text color="primary" @click="modalEndSprint = false">
                Cancel
              </v-btn>
              <v-btn
                text
                color="primary"
                @click="$refs.dialogEndSprint.save(sprintEndDate)"
              >
                OK
              </v-btn>
            </v-date-picker>
          </v-dialog> </v-card-text
        ><v-card-actions>
          <v-btn color="danger" text @click="dialogSprint = false">
            Cerrar
          </v-btn>
          <v-btn
            color="success"
            text
            @click="
              dialogSprint = false;
              createSprint();
            "
          >
            Crear Sprint
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
  name: "ProyectosDetalle",

  data() {
    return {
      apiUrl: "http://localhost:8080/",
      loading: false,
      dialogSprint: false,
      dialogTask: false,
      id: null,
      project: null,
      loaded: false,
      members: [],
      interesteds: [],
      page: 1,
      pageCount: 0,
      itemsPerPage: 10,
      headers: [
        { text: "Nombre", value: "name", width: "20%", align: "center" },
        { text: "Objetivo", value: "objetive", width: "20%", align: "center" },
        { text: "Estado", value: "state", align: "center" },
        { text: "Inicio", value: "startDate", align: "center" },
        { text: "Termino", value: "endDate", align: "center" },
        {
          text: "Acciones",
          value: "acciones",
          sortable: false,
          align: "center",
        },
      ],
      sprintName: null,
      sprintObjetive: null,
      sprintStartDate: null,
      sprintEndDate: null,
      modalStartSprint: false,
      modalEndSprint: false,
      menu: false,
      responsable: null,
      taskPriority: null,
      taskName: null,
      taskDescription: null,
      taskEstimatedHours: null,
      addSubTask: null,
      priorityList: [
        { text: "Alta", value: "Alta" },
        { text: "Media", value: "Media" },
        { text: "Baja", value: "Baja" },
      ],
      stateList: [
        { text: "Pendiente", value: "Pendiente" },
        { text: "En Curso", value: "En Curso" },
        { text: "Terminado", value: "Terminado" },
      ],
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    this.getInfo();
  },
  methods: {
    deleteItem(item) {
      Swal.fire({
        title: "Sprint - " + item.name,
        showDenyButton: true,
        confirmButtonText: `Eliminar`,
        icon: "info",
      }).then(async (result) => {
        if (result.isConfirmed) {
          const token = localStorage.getItem("token");
          console.log(this.apiUrl + "deleteSprint/" + item.id);
          await axios
            .delete(this.apiUrl + "deleteSprint/" + item.id, {
              headers: {
                Authorization: token,
              },
            })
            .then(() => {
              Swal.fire({
                title: "Sprint Eliminado!",
                icon: "success",
              });
              for (let sprint of this.project.sprints) {
                if (item == sprint) this.project.sprints.pop(sprint);
              }
            })
            .catch(() =>
              Swal.fire({
                title: "Error!",
                text: "El Sprint no pudo ser eliminado debido a que posee tareas",
                icon: "error",
              })
            );
        }
      });
    },
    goToSprint(item) {
      this.$router.push("/app/proyectos/" + this.project.id + "/" + item.id);
    },
    async createSprint() {
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("name", this.sprintName);
      formData.append("objetive", this.sprintObjetive);
      //2020-04-01T09:18:18Z
      console.log(this.sprintStartDate + " " + this.sprintEndDate);
      formData.append("startDate", this.sprintStartDate + " 00:00:00");
      formData.append("endDate", this.sprintEndDate + " 23:59:59");
      formData.append("projectId", this.project.id);
      await axios
        .post(this.apiUrl + "createSprint", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(async () => this.getInfo())
        .catch((error) => console.log(error));
    },
    goBack() {
      this.$router.go(-1);
    },

    async getInfo() {
      this.members = [];
      const token = localStorage.getItem("token");
      await axios
        .get(this.apiUrl + "getInfoProject/" + this.id, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.project = response.data))
        .catch((error) => console.log(error));

      console.log("THIS PROJECT -> ", this.project);
      for (var i = 0; i < this.project.members.length; i++) {
        this.members.push({
          text:
            this.project.members[i].name +
            " " +
            this.project.members[i].lastName,
          value: this.project.members[i].id,
        });
      }
      console.log("THIS MEMBERS -> ", this.members);
      this.loaded = true;
    },
    async createSubTask(taskId) {
      const token = localStorage.getItem("token");
      let formData = new FormData();
      formData.append("description", this.addSubTask);
      formData.append("taskId", taskId);
      await axios
        .post(this.apiUrl + "createSubTask", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo())
        .catch((error) => console.log(error));
      this.addSubTask = null;
    },
    async createTask() {
      const token = localStorage.getItem("token");
      let formData = new FormData();
      formData.append("name", this.taskName);
      formData.append("description", this.taskDescription);
      formData.append("taskEstimatedHours", this.taskEstimatedHours);
      formData.append("priority", this.taskPriority);
      formData.append("responsable", this.responsable);
      formData.append("sprint", this.id);
      console.log(formData);
      await axios
        .post(this.apiUrl + "createTask", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo())
        .catch((error) => console.log(error));
    },
  },
};
</script>

<style scoped>
.headerClass {
  white-space: nowrap;
  word-break: normal;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>