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
                          show-expand
                          right
                        >
                          <template v-slot:[`item.state`]="{ item }">
                            <v-chip
                              v-if="item.state == 'Pendiente'"
                              color="info"
                              dark
                            >
                              {{ item.state }}
                            </v-chip>
                            <v-chip
                              v-if="item.state == 'En Curso'"
                              color="warning"
                              dark
                            >
                              {{ item.state }}
                            </v-chip>
                            <v-chip
                              v-if="item.state == 'Terminado'"
                              color="success"
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
                              color="orange"
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
                          <template v-slot:[`item.estimatedHours`]="{ item }">
                            <v-chip color="primary">
                              {{ item.estimatedHours }} H
                            </v-chip>
                          </template>
                          <template v-slot:[`item.user.name`]="{ item }">
                            <v-chip style="width: 100%" color="primary">
                              {{ item.user.name }}
                              {{ item.user.lastName }}</v-chip
                            >
                          </template>
                          <template v-slot:[`item.acciones`]="{ item }">
                            <v-chip color="primary">
                              <v-icon
                                dense
                                class="mx-1"
                                @click="editItem(item)"
                              >
                                mdi-pencil
                              </v-icon>
                              <v-icon
                                dense
                                class="mx-1"
                                @click="deleteItem(item)"
                              >
                                mdi-delete
                              </v-icon>
                              <v-icon
                                dense
                                class="mx-1"
                                @click="
                                  dialogSubTask = true;
                                  selectedTask = item;
                                "
                              >
                                mdi-plus
                              </v-icon>
                            </v-chip>
                          </template>

                          <template v-slot:expanded-item="{ headers, item }">
                            <td :colspan="headers.length">
                              <v-data-table
                                dense
                                hide-default-footer
                                disable-sort
                                :headers="headersSubTask"
                                :items="getSubTasksFromTask(item)"
                                item-key="name"
                                style="margin-left: -1rem"
                              >
                                <template v-slot:[`item.state`]="{ item }">
                                  <v-chip
                                    v-if="item.state == 'Pendiente'"
                                    color="info"
                                    dark
                                  >
                                    {{ item.state }}
                                  </v-chip>
                                  <v-chip
                                    v-if="item.state == 'En Curso'"
                                    color="warning"
                                    dark
                                  >
                                    {{ item.state }}
                                  </v-chip>
                                  <v-chip
                                    v-if="item.state == 'Terminado'"
                                    color="success"
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
                                    color="orange"
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
                                <template
                                  v-slot:[`item.estimatedHours`]="{ item }"
                                >
                                  <v-chip color="primary">
                                    {{ item.estimatedHours }} H
                                  </v-chip>
                                </template>

                                <template v-slot:[`item.acciones`]="{ item }">
                                  <v-chip color="primary">
                                    <v-icon
                                      dense
                                      class="mx-1"
                                      @click="editSubTask(item)"
                                    >
                                      mdi-pencil
                                    </v-icon>
                                    <v-icon
                                      dense
                                      class="mx-1"
                                      @click="deleteItem(item)"
                                    >
                                      mdi-delete
                                    </v-icon>
                                  </v-chip>
                                </template>
                              </v-data-table>
                            </td>
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

            <!-- TABLERO KANBAN-->
            <v-container>
              <v-card>
                <v-card-title>Tablero KanBan</v-card-title>
                <v-card-text>
                  <v-row>
                    <v-col cols="4">
                      <v-card>
                        <v-card-title>Pendiente</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="info"
                            elevation="2"
                          >
                            <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list v-if="item.state == 'Pendiente'" dense>
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title
                                        v-text="item.name"
                                      ></v-list-item-title>
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div> </v-alert
                        ></v-card-text>
                      </v-card>
                    </v-col>
                    <v-col cols="4">
                      <v-card>
                        <v-card-title>En Curso</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="warning"
                            elevation="2"
                          >
                            <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list v-if="item.state == 'En Curso'" dense>
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title
                                        v-text="item.name"
                                      ></v-list-item-title>
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div></v-alert
                        ></v-card-text>
                      </v-card> </v-col
                    ><v-col cols="4">
                      <v-card>
                        <v-card-title>Terminado</v-card-title>
                        <v-card-text
                          ><v-alert
                            border="top"
                            colored-border
                            color="success"
                            elevation="2"
                          >
                           <div v-for="item in sprint.tasks" :key="item.id">
                              <v-list v-if="item.state == 'Terminado'" dense>
                                <v-list-item-group>
                                  <v-list-item>
                                    <v-list-item-content>
                                      <v-list-item-title
                                        v-text="item.name"
                                      ></v-list-item-title>
                                    </v-list-item-content>
                                  </v-list-item>
                                </v-list-item-group>
                              </v-list>
                            </div> </v-alert
                        ></v-card-text>
                      </v-card>
                    </v-col>
                  </v-row>
                </v-card-text>
              </v-card>
            </v-container>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <!-- DIALOG NEW TASK -->
    <v-dialog v-model="dialog" max-width="500px">
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

    <!-- DIALOG NEW TASK -->
    <v-dialog v-model="dialogSubTask" max-width="500px">
      <v-card>
        <v-card-title> Crear Nueva Sub Tarea </v-card-title>
        <v-card-text>
          <!-- FORM TO CREATE TASK -->
          <v-text-field
            v-model="taskName"
            label="Nombre Sub Tarea"
            prepend-icon="mdi-card-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="taskDescription"
            label="Descripción Sub Tarea"
            prepend-icon="mdi-clipboard-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="taskEstimatedHours"
            label="Horas Estimadas Sub Tarea"
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
            label="Prioridad Sub Tarea"
            hide-details
            prepend-icon="mdi-podium-silver"
            single-line
          ></v-select>

          <!--<v-select
            v-model="responsable"
            :items="members"
            menu-props="auto"
            label="Responsable de Tarea"
            hide-details
            prepend-icon="mdi-account"
            single-line
          ></v-select>--> </v-card-text
        ><v-card-actions>
          <v-btn color="danger" text @click="dialogSubTask = false">
            Cerrar
          </v-btn>
          <v-btn
            color="purple"
            text
            @click="
              dialog = false;
              createSubTask();
            "
          >
            Crear Sub Tarea
          </v-btn>
        </v-card-actions></v-card
      >
    </v-dialog>

    <!-- DIALOG EDIT SUBTASK -->
    <v-dialog v-model="dialogSubTaskEdit" max-width="500px">
      <v-card>
        <v-card-title> Editar {{ subTaskName }} </v-card-title>
        <v-card-text>
          <!-- FORM TO CREATE TASK -->
          <v-text-field
            v-model="subTaskName"
            label="Nombre Sub Tarea"
            prepend-icon="mdi-card-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="subTaskDescription"
            label="Descripción Sub Tarea"
            prepend-icon="mdi-clipboard-text"
            clearable
          ></v-text-field>

          <v-text-field
            v-model="subTaskEstimatedHours"
            label="Horas Estimadas Sub Tarea"
            prepend-icon="mdi-clock"
            clearable
            number
            type="number"
          ></v-text-field>

          <v-select
            class="mb-5"
            v-model="subTaskPriority"
            :items="priorityList"
            menu-props="auto"
            label="Prioridad Sub Tarea"
            hide-details
            prepend-icon="mdi-podium-silver"
            single-line
          ></v-select>

          <v-select
            class="mb-5"
            v-model="subTaskState"
            :items="stateList"
            menu-props="auto"
            label="Estado"
            hide-details
            prepend-icon="mdi-podium-silver"
            single-line
          ></v-select> </v-card-text
        ><v-card-actions>
          <v-btn color="danger" text @click="dialogSubTaskEdit = false">
            Cerrar
          </v-btn>
          <v-btn color="purple" text @click="_editSubTask()">
            <v-icon right dark> mdi-content-save-outline </v-icon>
            Editar Sub Tarea
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
      selectedTask: null,
      loaded: false,
      sprint: null,
      taskName: null,
      taskDescription: null,
      taskEstimatedHours: null,
      dialog: false,
      dialogSubTask: false,
      dialogSubTaskEdit: false,
      page: 1,
      pageCount: 0,
      itemsPerPage: 10,
      headers: [
        { text: "Nombre", value: "name", align: "start", width: "15%" },
        {
          text: "Descripción",
          value: "description",
          width: "25%",
          align: "center",
        },
        { text: "Estado", value: "state", width: "10% ", align: "center" },
        {
          text: "Prioridad",
          value: "priority",
          width: "10% ",
          align: "center",
        },
        {
          text: "Estimado",
          value: "estimatedHours",
          width: "10% ",
          align: "center",
        },
        {
          text: "Responsable",
          value: "user.name",
          width: "15% ",
          align: "center",
        },
        {
          text: "Acciones",
          value: "acciones",
          sortable: false,
          width: "15%",
          align: "center",
        },
        { text: "", value: "data-table-expand", align: "center" },
      ],
      headersSubTask: [
        { text: "", value: "name", align: "start", width: "15%" },
        {
          text: "",
          value: "description",
          width: "25%",
          align: "center",
        },
        { text: "", value: "state", width: "10% ", align: "center" },
        {
          text: "",
          value: "priority",
          width: "10% ",
          align: "center",
        },
        {
          text: "",
          value: "estimatedHours",
          width: "10% ",
          align: "center",
        },
        {
          text: "",
          value: "user.name",
          width: "15% ",
          align: "center",
        },
        {
          text: "",
          value: "acciones",
          sortable: false,
          width: "15%",
          align: "center",
        },
        { text: "", value: "data-table-expand", align: "center" },
      ],
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
      project: null,
      members: [],
      responsable: null,
      loading: false,
      taskPriority: null,
      subTaskId: null,
      subTaskName: null,
      subTaskDescription: null,
      subTaskEstimatedHours: null,
      subTaskPriority: null,
      subTaskState: null,
    };
  },
  mounted() {
    this.id = this.$route.params.sprint;
    this.projectId = this.$route.params.id;
    this.getInfo();
  },
  methods: {
    getSubTasksFromTask(item) {
      return item.subtasks;
    },

    editSubTask(item) {
      this.subTaskId = item.id;
      this.subTaskName = item.name;
      this.subTaskDescription = item.description;
      this.subTaskEstimatedHours = item.estimatedHours;
      this.subTaskPriority = item.priority;
      this.subTaskState = item.state;
      this.dialogSubTaskEdit = true;
    },
    async _editSubTask() {
      let formData = new FormData();
      formData.append("name", this.subTaskName);
      formData.append("description", this.subTaskDescription);
      formData.append("estimatedHours", this.subTaskEstimatedHours);
      formData.append("priority", this.subTaskPriority);
      formData.append("state", this.subTaskState);
      const token = localStorage.getItem("token");
      await axios
        .post(this.apiUrl + "editSubTask/" + this.subTaskId, formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.getInfo())
        .catch((error) => console.log(error));
    },

    async deleteItem(item) {
      Swal.fire({
        title: "Tarea - " + item.name,
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
            .then(() => Swal.fire("Tarea Eliminada!", "", "success"))
            .catch((error) => console.log(error));
        }
      });
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
      this.dialogSubTaskEdit = false;
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
      /*console.log("THIS SPRINT =>");
      console.log(this.sprint);*/
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

    async createSubTask() {
      const token = localStorage.getItem("token");
      let formData = new FormData();
      formData.append("name", this.taskName);
      formData.append("description", this.taskDescription);
      formData.append("estimatedHours", this.taskEstimatedHours);
      formData.append("priority", this.taskPriority);
      formData.append("taskId", this.selectedTask.id);
      await axios
        .post(this.apiUrl + "createSubTask", formData, {
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
<style>
swal-title {
  margin: 0px;
  font-size: 245px;
  box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.21);
  margin-bottom: 28px;
  color: red;
}
.swal-modal {
  background-color: rgba(63, 255, 106, 0.69);
  border: 3px solid white;
}
</style>