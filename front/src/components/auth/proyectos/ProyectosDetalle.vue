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
                <v-col cols="12" class="d-flex">
                  <Members
                    @event="getInfo"
                    :members="members"
                    :project="project"
                    :user="user"
                  ></Members>
                  <Interesteds
                    @event="getInfo"
                    :interesteds="interesteds"
                    :project="project"
                    :user="user"
                  ></Interesteds>
                </v-col>
              </v-row>

              <v-card>
                <v-card-title class="justify-center"
                  >Listado de Sprints
                </v-card-title>
                <v-card-text>
                  <v-row align="center">
                    <v-col>
                      <v-window v-model="window" reverse>
                        <v-window-item
                          v-for="sprint in project.sprints"
                          :key="sprint.id"
                        >
                          <v-card color="#EEEEEE" elevation="2" outlined shaped>
                            <v-card-text>
                              <div>
                                <v-col class="text-right">
                                  <v-btn
                                    class="mx-2"
                                    fab
                                    dark
                                    small
                                    color="primary"
                                    @click="editSprint(sprint)"
                                  >
                                    <v-icon>mdi-pencil</v-icon>
                                  </v-btn>
                                  <v-btn
                                    v-if="user.id == project.user.id"
                                    class="mx-2"
                                    fab
                                    dark
                                    small
                                    color="red darken-1"
                                    @click="deleteSprint(sprint)"
                                  >
                                    <v-icon>mdi-delete</v-icon>
                                  </v-btn>
                                </v-col>

                                <h3
                                  class="text-center text-overline"
                                  v-text="sprint.name"
                                ></h3>
                                <h5
                                  class="text-center text-overline">
                                  Objetivo : {{sprint.objetive}}
                                </h5>

                                <v-divider class="mt-5 mb-5"></v-divider>

                                <v-expansion-panels class="mb-6">
                                  <v-expansion-panel
                                    v-for="task in sprint.tasks"
                                    :key="task.id"
                                  >
                                    <v-expansion-panel-header>
                                      <div class="text-overline">
                                        {{ task.name + " - " }}
                                        <v-chip
                                          color="secondary"
                                          text-color="white"
                                          class="ma-2"
                                        >
                                          <v-avatar>
                                            <v-icon>mdi-account</v-icon>
                                          </v-avatar>
                                          <span
                                            v-text="
                                              task.user.name +
                                              ' ' +
                                              task.user.lastName
                                            "
                                          ></span>
                                        </v-chip>
                                      </div>
                                      <span
                                        class="d-flex justify-end mr-5"
                                        v-if="task.priority == 'Alta'"
                                      >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                      </span>
                                      <span
                                        class="d-flex justify-end mr-5"
                                        v-if="task.priority == 'Media'"
                                      >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                      </span>
                                      <span
                                        class="d-flex justify-end mr-5"
                                        v-if="task.priority == 'Baja'"
                                      >
                                        <v-icon color="orange">
                                          mdi-star</v-icon
                                        >
                                      </span>
                                      <template v-slot:actions>
                                        <v-icon
                                          color="red"
                                          v-if="task.state == 'Pendiente'"
                                        >
                                          mdi-stop-circle-outline
                                        </v-icon>
                                        <v-icon
                                          color="warning"
                                          v-if="task.state == 'En Curso'"
                                        >
                                          mdi-progress-check
                                        </v-icon>
                                        <v-icon
                                          color="teal"
                                          v-if="task.state == 'Terminado'"
                                        >
                                          mdi-check
                                        </v-icon>
                                      </template>
                                    </v-expansion-panel-header>
                                    <v-expansion-panel-content>
                                      <div
                                        class="text-center text-overline"
                                        
                                      > Descripción : {{task.description}}</div>
                                      <v-chip-group
                                        active-class="primary--text"
                                        column
                                      >
                                        <v-chip
                                          color="secondary"
                                          text-color="white"
                                          class="ma-2"
                                        >
                                          <v-avatar>
                                            <v-icon
                                              >mdi-clock-time-three-outline</v-icon
                                            >
                                          </v-avatar>
                                          <span
                                            v-text="task.estimatedHours + ' H'"
                                          ></span>
                                        </v-chip>
                                    
                                        <v-chip
                                          color="secondary"
                                          text-color="white"
                                          class="ma-2"
                                        >
                                          <v-avatar>
                                            <v-icon>mdi-calendar-clock-outline</v-icon>
                                          </v-avatar>
                                          <span>
                                          Término : {{formatDate(task.finished_at)}}
                                          </span>
                                        </v-chip>

                                        <v-col class="text-right">
                                          <v-btn
                                            class="mx-2"
                                            fab
                                            dark
                                            small
                                            color="primary"
                                            @click="editTask(task)"
                                          >
                                            <v-icon>mdi-tools</v-icon>
                                          </v-btn>
                                          <v-btn
                                            v-if="user.id == project.user.id"
                                            class="mx-2"
                                            fab
                                            dark
                                            small
                                            color="red darken-1"
                                            @click="deleteTask(task)"
                                          >
                                            <v-icon>mdi-delete</v-icon>
                                          </v-btn>
                                        </v-col>
                                      </v-chip-group>
                                      <v-text-field
                                        v-on:keyup.enter="
                                          createSubTask(task.id)
                                        "
                                        v-model="addSubTask"
                                        placeholder="Check List Sub Tareas"
                                      >
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
                                          v-model="subtask.state"
                                          :true-value="true"
                                          :false-value="false"
                                          @click="checkSubTask(subtask.id)"
                                          ><v-icon
                                            slot="append"
                                            color="red"
                                            @click="deleteSubTask(subtask.id)"
                                          >
                                            mdi-delete
                                          </v-icon></v-checkbox
                                        >
                                      </div>
                                    </v-expansion-panel-content>
                                  </v-expansion-panel>
                                </v-expansion-panels>
                                <div class="my-2 text-center">
                                  <v-btn
                                    v-if="user.id == project.user.id"
                                    small
                                    color="success"
                                    dark
                                    @click="
                                      dialogTask = true;
                                      clearVars();
                                      sprintIdCreateTask = sprint.id;
                                    "
                                  >
                                    Crear Tarea
                                    <v-icon dark right> mdi-plus </v-icon>
                                  </v-btn>
                                </div>
                              </div>
                            </v-card-text>

                            <v-card-actions class="justify-space-between">
                              <v-btn text @click="prev">
                                <v-icon>mdi-chevron-left</v-icon>
                              </v-btn>
                              <v-item-group
                                v-model="window"
                                class="text-center"
                                mandatory
                              >
                                <v-item
                                  v-for="n in project.sprints.length"
                                  :key="`btn-${n}`"
                                  v-slot="{ active, toggle }"
                                >
                                  <v-btn
                                    :input-value="active"
                                    icon
                                    @click="toggle"
                                  >
                                    <v-icon>mdi-record</v-icon>
                                  </v-btn>
                                </v-item>
                              </v-item-group>
                              <v-btn text @click="next">
                                <v-icon>mdi-chevron-right</v-icon>
                              </v-btn>
                            </v-card-actions>
                          </v-card>
                        </v-window-item>
                      </v-window>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
            </v-container>
            <v-row>
              <v-col cols="12" class="text-right">
                <v-btn
                  v-if="user.id == project.user.id"
                  class="ma-2"
                  color="primary"
                  dark
                  @click="
                    dialogSprint = true;
                    sprintEdit = false;
                    clearVars();
                  "
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
      <v-form ref="form" v-model="valid" lazy-validation>
        <v-card>
          <v-card-title v-if="!taskEdit"> Crear Nueva Tarea </v-card-title>
          <v-card-title v-if="taskEdit"> Editar Tarea </v-card-title>
          <v-card-text>
            <!-- FORM TO CREATE TASK -->

            <v-select
              v-if="taskEdit"
              class="mb-5"
              v-model="taskState"
              :items="stateList"
              menu-props="auto"
              label="Estado de Tarea"
              hide-details
              prepend-icon="mdi-podium-silver"
              single-line
              color="success"
            ></v-select>

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
              v-if="user && project && user.id == project.user.id"
              v-model="responsable"
              :items="members"
              menu-props="auto"
              label="Responsable de Tarea"
              hide-details
              prepend-icon="mdi-account"
              single-line
            ></v-select> </v-card-text
          ><v-card-actions>
            <v-btn
              color="danger"
              text
              @click="
                dialogTask = false;
                clearVars();
              "
            >
              Cerrar
            </v-btn>
            <v-btn
              v-if="!taskEdit"
              color="success"
              text
              @click="
                dialogTask = false;
                createTask();
              "
            >
              Crear Tarea
            </v-btn>
            <v-btn
              v-if="taskEdit"
              color="success"
              text
              @click="
                dialogTask = false;
                _editTask();
              "
            >
              Editar Tarea
            </v-btn>
          </v-card-actions></v-card
        ></v-form
      >
    </v-dialog>

    <!-- DIALOG NEW SPRINT -->
    <v-dialog v-model="dialogSprint" max-width="500px">
      <v-card>
        <v-card-title v-if="!sprintEdit"> Crear Nuevo Sprint </v-card-title>
        <v-card-title v-if="sprintEdit"> Editar Sprint </v-card-title>
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
          <v-btn
            color="danger"
            text
            @click="
              dialogSprint = false;
              clearVars();
            "
          >
            Cerrar
          </v-btn>
          <v-btn
            v-if="!sprintEdit"
            color="success"
            text
            @click="
              dialogSprint = false;
              createSprint();
            "
          >
            Crear Sprint
          </v-btn>
          <v-btn
            v-if="sprintEdit"
            color="success"
            text
            @click="
              dialogSprint = false;
              _editSprint();
            "
          >
            Editar Sprint
          </v-btn>
        </v-card-actions></v-card
      >
    </v-dialog>

    <!-- LOADING MODAL -->
    <v-dialog v-model="loadingModal" hide-overlay persistent width="300">
      <v-card color="primary" dark>
        <v-card-text>
          Cargando, Por favor Espere ...
          <v-progress-linear
            indeterminate
            color="white"
            class="mb-0"
          ></v-progress-linear>
        </v-card-text>
      </v-card>
    </v-dialog>

    <!-- DELETE SPRINT MODAL -->
    <v-dialog v-model="dialogDeleteSprint" max-width="50%">
      <v-card>
        <v-card-title class="text-h5"
          >Eliminar Sprint : '{{ sprintToDelete.name }}'</v-card-title
        >
        <v-card-text class="text-h6"
          >Si elimina el sprint se eliminarán tareas y subtareas
          asociadas</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialogDeleteSprint = false">
            Cerrar
          </v-btn>

          <v-btn
            color="green darken-1"
            text
            @click="
              dialogDeleteSprint = false;
              _deleteSprint();
            "
          >
            Aceptar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- DELETE TASK MODAL -->
    <v-dialog v-model="dialogDeleteTask" max-width="50%">
      <v-card>
        <v-card-title class="text-h5"
          >Eliminar Tarea : '{{ taskToDelete.name }}'</v-card-title
        >
        <v-card-text class="text-h6"
          >Si elimina la tarea se eliminarán subtareas asociadas</v-card-text
        >
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="red darken-1" text @click="dialogDeleteTask = false">
            Cerrar
          </v-btn>

          <v-btn
            color="green darken-1"
            text
            @click="
              dialogDeleteTask = false;
              _deleteTask();
            "
          >
            Aceptar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
import axios from "axios";
import Members from "./Members.vue";
import Interesteds from "./Interesteds.vue";
// eslint-disable-next-line no-unused-vars
import moment from "moment";
export default {
  components: { Members, Interesteds },
  name: "ProyectosDetalle",

  data() {
    return {
      user: null,
      apiUrl: process.env.VUE_APP_APIURL,
      loadingModal: false,
      loading: false,
      dialogSprint: false,
      dialogTask: false,
      id: null,
      project: null,
      loaded: false,
      members: [],
      interesteds: [],
      sprintName: null,
      sprintObjetive: null,
      sprintStartDate: null,
      sprintEndDate: null,
      sprintId: null,
      modalStartSprint: false,
      modalEndSprint: false,
      menu: false,
      responsable: null,
      taskPriority: null,
      taskName: null,
      taskDescription: null,
      taskEstimatedHours: null,
      taskState: null,
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
      sprintIdCreateTask: null,
      sprintList: null,
      window: 0,
      sprintEdit: false,
      taskEdit: false,
      sprintToDelete: {},
      dialogDeleteSprint: false,
      taskToDelete: {},
      dialogDeleteTask: false,
      projectRules: [(value) => !!value || "Campo Requerido"],
      valid: true,
    };
  },
  created() {
    this.interval = setInterval(() => this.getInfo(), 60000);
  },

  mounted() {
    this.id = this.$route.params.id;
    this.getInfo();
  },
  methods: {
    formatDate(date) {
         if (date) {
           return moment(String(date)).format('DD-MM-YYYY')
          } 
    },

    next() {
      this.window =
        this.window + 1 === this.project.sprints.length ? 0 : this.window + 1;
    },
    prev() {
      this.window =
        this.window - 1 < 0 ? this.project.sprints.length - 1 : this.window - 1;
    },
    deleteSprint(sprint) {
      this.sprintToDelete = sprint;
      this.dialogDeleteSprint = true;
    },
    async _deleteSprint() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      await axios
        .delete(this.apiUrl + "deleteSprint/" + this.sprintToDelete.id, {
          headers: { Authorization: token },
        })
        .then(async () => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error));
    },
    deleteTask(task) {
      this.taskToDelete = task;
      this.dialogDeleteTask = true;
    },
    async _deleteTask() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      await axios
        .delete(this.apiUrl + "deleteTask/" + this.taskToDelete.id, {
          headers: { Authorization: token },
        })
        .then(async () => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error));
    },
    async createSprint() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("name", this.sprintName);
      formData.append("objetive", this.sprintObjetive);
      formData.append("startDate", this.sprintStartDate + " 00:00:00");
      formData.append("endDate", this.sprintEndDate + " 23:59:59");
      formData.append("projectId", this.project.id);
      await axios
        .post(this.apiUrl + "createSprint", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(async () => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error), this.getInfo());
    },
    editSprint(sprint) {
      this.sprintName = sprint.name;
      this.sprintObjetive = sprint.objetive;
      this.sprintStartDate = sprint.startDate.split("T")[0];
      this.sprintEndDate = sprint.endDate.split("T")[0];
      this.sprintId = sprint.id;
      this.dialogSprint = true;
      this.sprintEdit = true;
    },
    editTask(task) {
      this.responsable = task.user.id;
      this.taskPriority = task.priority;
      this.taskName = task.name;
      this.taskDescription = task.description;
      this.taskEstimatedHours = task.estimatedHours;
      this.taskId = task.id;
      this.taskState = task.state;
      this.dialogTask = true;
      this.taskEdit = true;
    },
    async checkSubTask(subtask) {
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("subTask", subtask);
      await axios
        .put(this.apiUrl + "checkUncheckSubTask/", formData, {
          headers: {
            Authorization: token,
          },
        })
        .catch((error) => console.log(error));
    },
    async _editSprint() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("name", this.sprintName);
      formData.append("objetive", this.sprintObjetive);
      formData.append("startDate", this.sprintStartDate + " 00:00:00");
      formData.append("endDate", this.sprintEndDate + " 23:59:59");
      formData.append("id", this.sprintId);
      await axios
        .put(this.apiUrl + "editSprint", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(async () => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error));
    },
    async _editTask() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("name", this.taskName);
      formData.append("description", this.taskDescription);
      formData.append("estimatedHours", this.taskEstimatedHours);
      formData.append("priority", this.taskPriority);
      formData.append("responsable", this.responsable);
      formData.append("task_id", this.taskId);
      formData.append("state", this.taskState);
      await axios
        .put(this.apiUrl + "editTask", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(async () => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error));
    },
    goBack() {
      this.$router.go(-1);
    },

    async getInfo() {
      this.members = [];
      this.interesteds = [];
      var formData = new FormData();
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      formData.append("email", email);
      formData.append("token", token);
      await axios
        .post(this.apiUrl + "getInfoProject/" + this.id, formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(
          (response) => (
            (this.project = response.data.project),
            (this.user = response.data.user)
          )
        )
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
      this.members.push({
        text: this.project.user.name + " " + this.project.user.lastName,
        value: this.project.user.id,
      });
      for (var j = 0; j < this.project.interesteds.length; j++) {
        this.interesteds.push({
          text:
            this.project.interesteds[j].name +
            " " +
            this.project.interesteds[j].lastName,
          value: this.project.interesteds[j].id,
        });
      }
      this.sprintList = this.project.sprints;
      this.loaded = true;
      this.loadingModal = false;
    },
    async createSubTask(taskId) {
      this.loadingModal = true;
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
    async deleteSubTask(subTaskId) {
      const token = localStorage.getItem("token");
      await axios
        .delete(this.apiUrl + "deleteSubTask/" + subTaskId, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo())
        .catch((error) => console.log(error));
    },
    async createTask() {
      this.loadingModal = true;
      const token = localStorage.getItem("token");
      let formData = new FormData();
      formData.append("name", this.taskName);
      formData.append("description", this.taskDescription);
      formData.append("taskEstimatedHours", this.taskEstimatedHours);
      formData.append("priority", this.taskPriority);
      formData.append("responsable", this.responsable);
      formData.append("sprint", this.sprintIdCreateTask);
      await axios
        .post(this.apiUrl + "createTask", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo(), this.clearVars())
        .catch((error) => console.log(error), this.getInfo());
    },

    clearVars() {
      this.taskName = null;
      this.taskDescription = null;
      this.taskEstimatedHours = null;
      this.taskPriority = null;
      this.responsable = null;
      this.taskState = null;
      this.sprintIdCreateTask = null;
      this.sprintName = null;
      this.sprintObjetive = null;
      this.sprintStartDate = null;
      this.sprintEndDate = null;
      this.sprintId = null;
      this.taskEdit = false;
      this.sprintEdit = false;
    },
  },
};
</script>
