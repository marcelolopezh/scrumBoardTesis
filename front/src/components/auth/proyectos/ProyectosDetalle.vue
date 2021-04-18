<template >
  <div>
    <v-row class="pa-5" v-if="loaded">
      <v-col>
        <v-card>
          <v-card-title>
            <v-chip
              @click="goBack()"
              class="ma-2"
              color="primary"
              text-color="white"
            >
              <v-icon left> mdi-arrow-left-circle </v-icon>
              Volver
            </v-chip>
          </v-card-title>

          <v-card-text>
            <v-alert color="#E0E0E0">
              <h2 class="mx-4 text-center">
                {{ project.name }}
              </h2>
              <h3 class="mx-4 text-center">
                {{ project.description }}
              </h3>
            </v-alert>

            <v-container
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
                            color="amber"
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
                          @click:row="sprintClick"
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
                          <template v-slot:item.startDate="{ item }">
                            {{ item.startDate | moment("DD-MM-YYYY") }}
                          </template>
                          <template v-slot:item.endDate="{ item }">
                            {{ item.endDate | moment("DD-MM-YYYY") }}
                          </template>
                        </v-data-table>
                        <div class="text-center pt-2">
                          <v-pagination
                            v-model="page"
                            :length="pageCount"
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
        { text: "Nombre", value: "name", width: "15%" },
        { text: "Objetivo", value: "objetive", width: "40%" },
        { text: "Estado", value: "state", width: "15%" },
        { text: "Inicio", value: "startDate", width: "15%" },
        { text: "Termino", value: "endDate", width: "15%" },
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
    sprintClick(value) {
      let sprint = value.id;
      this.$router.push("/app/proyectos/" + this.project.id + "/" + sprint);
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