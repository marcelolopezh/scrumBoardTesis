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

            <v-container fluid
              ><v-row>
                <v-col cols="3">
                  <v-card>
                    <v-card-text>
                      <v-list dense>
                        <v-subheader>Miembros del Equipo</v-subheader
                        ><v-list-item v-for="item in members" :key="item.id">
                          <v-chip
                            class="ma-2"
                            color="primary"
                            text-color="white"
                          >
                            <v-icon left>mdi-account </v-icon>
                            <v-list-item-content>
                              <v-list-item-title
                                >{{ item.name }}
                                {{ item.lastName }}</v-list-item-title
                              >
                            </v-list-item-content></v-chip
                          >
                        </v-list-item></v-list
                      >
                      <v-list dense>
                        <v-subheader>Clientes</v-subheader>
                        <v-list-item v-for="item in clients" :key="item.id">
                          <v-chip
                            class="ma-2"
                            color="primary"
                            text-color="white"
                          >
                            <v-icon left>mdi-account</v-icon>

                            <v-list-item-content>
                              <v-list-item-title
                                >{{ item.name }}
                                {{ item.lastName }}</v-list-item-title
                              >
                            </v-list-item-content></v-chip
                          >
                        </v-list-item></v-list
                      >
                    </v-card-text>
                  </v-card>
                </v-col>
                <v-col cols="9">
                  <v-card>
                    <v-card-title>Listado de Sprints </v-card-title>
                    <v-card-text
                      ><div>
                        <v-data-table
                          :headers="headers"
                          :items="project.sprints"
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
                              outlined
                            >
                              {{ item.state }}
                            </v-chip>
                            <v-chip
                              style="width: 100%"
                              v-if="item.state == 'Iniciado'"
                              color="blue"
                              dark
                            >
                              {{ item.state }}
                            </v-chip>
                            <v-chip
                              style="width: 100%"
                              v-if="item.state == 'Terminado'"
                              color="success"
                              dark
                            >
                              {{ item.state }}
                            </v-chip>
                          </template>
                          <template v-slot:[`item.startDate`]="{ item }">
                            {{ item.startDate | moment("DD-MM-YYYY") }}
                          </template>
                          <template v-slot:[`item.endDate`]="{ item }">
                            {{ item.endDate | moment("DD-MM-YYYY") }}
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
                                @click="goToSprint(item)"
                              >
                                mdi-eye
                              </v-icon></v-chip
                            >
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
                      color="success"
                      class="ma-2 white--text float-right"
                      @click="dialog = true"
                    >
                      Nuevo Sprint
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
              <v-spacer></v-spacer>
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
          <v-btn color="danger" text @click="dialog = false"> Cerrar </v-btn>
          <v-btn
            color="success"
            text
            @click="
              dialog = false;
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
      dialog: false,
      id: null,
      project: null,
      loaded: false,
      members: [],
      clients: [],
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
                title: "Eliminado",
                icon: "success",
              });
              for (let sprint of this.project.sprints) {
                if (item == sprint) this.project.sprints.pop(sprint);
              }
            })
            .catch(() =>
              Swal.fire({
                title: "Error!",
                text:
                  "El Sprint no pudo ser eliminado debido a que posee tareas",
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
      this.clients = [];
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