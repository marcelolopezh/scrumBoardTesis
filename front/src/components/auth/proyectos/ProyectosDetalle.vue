<template >
  <div>
    <v-row class="pa-5" v-if="loaded">
      <v-col>
        <v-card>
          <v-card-title
            ><v-icon @click="goBack()" color="primary" class="mr-2"
              >mdi-arrow-left-circle</v-icon
            >{{ project.name }} - {{ project.description }}</v-card-title
          >
          <v-card-text>
            <v-container
              ><v-row>
                <v-col cols="3">
                  <v-card>
                    <v-card-text>
                      <v-list dense>
                        <v-subheader>Miembros del Equipo</v-subheader
                        ><v-list-item v-for="item in members" :key="item.id">
                          <v-list-item-icon>
                            <v-icon>mdi-account</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title
                              >{{ item.name }}
                              {{ item.lastName }}</v-list-item-title
                            >
                          </v-list-item-content>
                        </v-list-item></v-list
                      >
                      <v-list dense>
                        <v-subheader>Clientes</v-subheader>
                        <v-list-item v-for="item in clients" :key="item.id">
                          <v-list-item-icon>
                            <v-icon>mdi-account</v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title
                              >{{ item.name }}
                              {{ item.lastName }}</v-list-item-title
                            >
                          </v-list-item-content>
                        </v-list-item></v-list
                      >
                    </v-card-text>
                  </v-card>
                </v-col>
                <v-col cols="9">
                  <v-card>
                    <v-card-title>Sprints</v-card-title>
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
            clearable
          ></v-text-field>

          <v-text-field
            v-model="sprintObjetive"
            label="Objetivo Sprint"
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
        {
          text: "Id",

          sortable: false,
          value: "id",
        },
        { text: "Nombre", value: "name" },
        { text: "Objetivo", value: "objetive" },
        { text: "Estado", value: "state" },
        { text: "Inicio", value: "startDate" },
        { text: "Termino", value: "endDate" },
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
    async createSprint() {
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("name", this.sprintName);
      formData.append("objetive", this.sprintObjetive);
      //2020-04-01T09:18:18Z
      formData.append("startDate", this.sprintStartDate+"T00:00:00Z");
      formData.append("endDate", this.sprintEndDate+"T23:59:00Z");
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