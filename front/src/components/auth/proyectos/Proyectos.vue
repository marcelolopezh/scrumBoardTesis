<template >
  <v-row class="pa-5">
    <v-col>
      <v-card>
        <v-card-title>Proyectos</v-card-title>
        <v-card-text>
          <v-row>
            <v-col md="6">
              <v-card>
                <v-card-title>Crear Nuevo Proyecto</v-card-title>
                <v-card-text>
                  <v-alert type="error" v-if="errors">
                    {{ errorMsg }}
                  </v-alert>
                  <v-alert type="success" v-if="success">
                    {{ errorMsg }}
                  </v-alert>

                  <v-form ref="form" v-model="valid" lazy-validation>
                    <v-text-field
                      v-model="name"
                      label="Nombre del Proyecto"
                      required
                      prepend-icon="mdi-border-color"
                      :rules="projectRules"
                    ></v-text-field>

                    <v-text-field
                      v-model="description"
                      label="Descripcion del Proyecto"
                      required
                      prepend-icon="mdi-format-list-bulleted"
                      :rules="projectRules"
                    ></v-text-field>

                    <v-text-field
                      v-model="objetive"
                      label="Objetivo del Proyecto"
                      required
                      prepend-icon="mdi-format-list-bulleted"
                      :rules="projectRules"
                    ></v-text-field>

                    <v-autocomplete
                      v-model="selectedMembers"
                      :items="allMembersComputed"
                      item-value="id"
                      :item-text="(item) => item.name + ' ' + item.lastName"
                      chips
                      small-chips
                      label="Listado de Miembros"
                      multiple
                      prepend-icon="mdi-human-queue"
                    >
                    </v-autocomplete>

                    <v-autocomplete
                      v-model="selectedInteresteds"
                      :items="allInterestedsComputed"
                      item-value="id"
                      :item-text="(item) => item.name + ' ' + item.lastName"
                      chips
                      small-chips
                      label="Listado de Clientes"
                      multiple
                      prepend-icon="mdi-human-queue"
                    >
                    </v-autocomplete>

                    <v-btn
                      :disabled="!valid"
                      color="success"
                      class="mr-4"
                      @click="createProject()"
                    >
                      Crear Proyecto
                    </v-btn>
                  </v-form>
                </v-card-text>
              </v-card> </v-col
            ><v-col md="6">
              <v-card>
                <v-card-title>Mis Proyectos</v-card-title>
                <v-list dense v-if="myProjects != null">
                  <v-list-item-group v-model="selectedProject" color="primary">
                    <v-list-item
                      v-for="item in myProjects"
                      :key="item.id"
                      @click="routerTo(item)"
                      class="projectList"
                    >
                      <v-list-item-icon>
                        <v-icon>mdi-file-document-multiple</v-icon>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title
                          v-text="item.name"
                        ></v-list-item-title>
                        {{ item.description }}
                      </v-list-item-content>
                    </v-list-item>
                  </v-list-item-group>
                </v-list>
              </v-card>

              <v-card>
                <v-card-title>Soy Colaborador</v-card-title>
                <v-list dense v-if="myProjectsAsMember != null">
                  <v-list-item-group v-model="selectedProject" color="primary">
                    <v-list-item
                      v-for="item in myProjectsAsMember"
                      :key="item.id"
                      @click="routerTo(item)"
                      class="projectList"
                    >
                      <v-list-item-icon>
                        <v-icon>mdi-file-document-multiple</v-icon>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title
                          v-text="item.name"
                        ></v-list-item-title>
                        {{ item.description }}
                      </v-list-item-content>
                    </v-list-item>
                  </v-list-item-group>
                </v-list>
              </v-card>

              <v-card>
                <v-card-title>Estoy Interesado</v-card-title>
                <v-list dense v-if="myProjectsAsInterested != null">
                  <v-list-item-group v-model="selectedProject" color="primary">
                    <v-list-item
                      v-for="item in myProjectsAsInterested"
                      :key="item.id"
                      @click="routerTo(item)"
                      class="projectList"
                    >
                      <v-list-item-icon>
                        <v-icon>mdi-file-document-multiple</v-icon>
                      </v-list-item-icon>
                      <v-list-item-content>
                        <v-list-item-title
                          v-text="item.name"
                        ></v-list-item-title>
                        {{ item.description }}
                      </v-list-item-content>
                    </v-list-item>
                  </v-list-item-group>
                </v-list>
              </v-card>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>


<script>
import axios from "axios";
export default {
  name: "Proyectos",
  data() {
    return {
      name: null,
      description: null,
      valid: true,
      objetive: null,
      allMembers: [],
      allInteresteds: [],
      selectedMembers: [],
      selectedInteresteds: [],
      apiUrl: process.env.VUE_APP_APIURL,
      errors: false,
      success: false,
      errorMsg: "",
      myProjects: null,
      myProjectsAsMember: null,
      myProjectsAsInterested: null,
      selectedProject: null,
      projectRules: [
        (value) => !!value || "Campo Requerido",
        (value) => (value && value.length >= 6) || "Mínimo 6 Caracteres",
        (value) => (value && value.length < 255) || "Máximo 255 Caracteres",
      ],
    };
  },
  mounted() {
    this.getMyProjects();
    this.getMyProjectsAsMember();
    this.getMyProjectsAsInterested();
    this.getAllMembers();
  },
  computed: {
    allMembersComputed: function () {
      var aux = [];
      var flag = false;
      for (var i = 0; i < this.allMembers.length; i++) {
        flag = false;
        for (var j = 0; j < this.selectedInteresteds.length; j++) {
          if (this.allMembers[i].id == this.selectedInteresteds[j]) {
            flag = true;
          }
        }
        if (!flag) {
          aux.push(this.allMembers[i]);
        }
      }
      return aux;
    },
    allInterestedsComputed: function () {
      var aux = [];
      var flag = false;
      for (var i = 0; i < this.allMembers.length; i++) {
        flag = false;
        for (var j = 0; j < this.selectedMembers.length; j++) {
          if (this.allMembers[i].id == this.selectedMembers[j]) {
            flag = true;
          }
        }
        if (!flag) {
          aux.push(this.allMembers[i]);
        }
      }
      return aux;
    },
  },
  methods: {
    routerTo(item) {
      this.$router.push("/app/proyectos/" + item.id);
    },
    async getMyProjects() {
      const email = localStorage.getItem("email");
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("email", email);
      await axios
        .post(this.apiUrl + "getMyProjects", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.myProjects = response.data))
        .catch((error) => console.log(error));
    },
    async getMyProjectsAsMember() {
      const email = localStorage.getItem("email");
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("email", email);
      await axios
        .post(this.apiUrl + "getMyProjectsAsMember", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.myProjectsAsMember = response.data))
        .catch((error) => console.log(error));
    },
    async getMyProjectsAsInterested() {
      const email = localStorage.getItem("email");
      const token = localStorage.getItem("token");
      var formData = new FormData();
      formData.append("email", email);
      await axios
        .post(this.apiUrl + "getMyProjectsAsInterested", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.myProjectsAsInterested = response.data))
        .catch((error) => console.log(error));
    },
    async getAllMembers() {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      await axios
        .get(this.apiUrl + "getAllMembers", {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.allMembers = response.data))
        .catch((error) => console.log(error));
      var index = 0;
      for (var i = 0; i < this.allMembers.length; i++) {
        if (this.allMembers[i].email == email) {
          index = i;
        }
      }
      this.allMembers.splice(index, 1);
      this.allInteresteds = this.allMembers;
    },
    async createProject() {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      let formData = new FormData();
      if (
        this.name == null ||
        this.description == null ||
        this.objetive == null
      ) {
        this.errors = true;
        this.success = false;
        this.errorMsg = "Ha ocurrido un error, campos nulos";
        return;
      }
      formData.append("name", this.name);
      formData.append("description", this.description);
      formData.append("objetive", this.objetive);
      formData.append("selectedMembers", this.selectedMembers);
      formData.append("selectedInteresteds", this.selectedInteresteds);
      formData.append("email", email);
      await axios
        .post(this.apiUrl + "createProject", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => {
          this.errors = false;
          this.success = true;
          this.errorMsg = "Proyecto Creado!";
          this.myProjects.push(response.data);
          this.name = null;
          this.description = null;
          this.objetive = null;
          this.selectedMembers = [];
          this.selectedInteresteds = [];
        })
        .catch(() => {
          this.errors = true;
          this.success = false;
          this.errorMsg = "Ha ocurrido un error, campos nulos";
        });
    },
  },
};
</script>
