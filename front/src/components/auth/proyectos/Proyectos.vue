<template >
  <v-row class="pa-5">
    <v-col>
      <v-card>
        <v-card-title>Proyectos</v-card-title>
        <v-card-text>
          <v-row>
            <v-col md="6">
              <v-card>
                <v-card-title>Mis Proyectos</v-card-title>
                <v-card-text>Listado de Proyectos</v-card-text>
                <v-list dense v-if="myProjects!=null">
                  <v-list-item-group v-model="selectedProject" color="primary">
                    <v-list-item v-for="(item) in myProjects" :key="item.id" @click="routerTo(item)">
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
                      label="Nombre de Proyecto"
                      required
                    ></v-text-field>

                    <v-text-field
                      v-model="description"
                      label="Descripcion de Proyecto"
                      required
                    ></v-text-field>

                    <v-autocomplete
                      v-model="selectedMembers"
                      :items="allMembers"
                      item-value="id"
                      :item-text="(item) => item.name + ' ' + item.lastName"
                      chips
                      small-chips
                      label="Listado de Miembros"
                      multiple
                    >
                    </v-autocomplete>

                    <v-autocomplete
                      v-model="selectedClients"
                      :items="allMembers"
                      item-value="id"
                      :item-text="(item) => item.name + ' ' + item.lastName"
                      chips
                      small-chips
                      label="Listado de Clientes"
                      multiple
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
      allMembers: [],
      selectedMembers: [],
      selectedClients: [],
      apiUrl: "http://localhost:8080/",
      errors: false,
      success: false,
      errorMsg: "",
      myProjects: null,
      selectedProject : null
    };
  },
  mounted() {
    this.getMyProjects();
    this.getAllMembers();
  },
  methods: {
    routerTo(item){
      this.$router.push('/proyectos/'+item.id)
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
    async getAllMembers() {
      const token = localStorage.getItem("token");
      await axios
        .get(this.apiUrl + "getAllMembers", {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.allMembers = response.data))
        .catch((error) => console.log(error));
    },
    async createProject() {
      const token = localStorage.getItem("token");
      const email = localStorage.getItem("email");
      let formData = new FormData();
      formData.append("name", this.name);
      formData.append("description", this.description);
      formData.append("selectedMembers", this.selectedMembers);
      formData.append("selectedClients", this.selectedClients);
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
          this.myProjects.push(response.data)
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