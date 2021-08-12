<template>
  <div>
    <v-container fluid>
      <v-card>
        <v-card-title></v-card-title>
        <v-card-text>
          <v-alert
            dense
            type="info"
            class="text-center"
            icon="mdi-school"
            border="left"
            prominent
          >
            <h1 class="my-4"><strong>ScrumBoard</strong></h1>
            <h2 class="my-4">Proyecto de Título - Taller de Integración V</h2>
            <h3 class="my-4">
              Plataforma virtual para gestión de proyectos mediante metodología
              Scrum, enfocado e los Talleres de Integración de la Carrera de
              Ingeniería Civil en Informática de la Universidad Católica de
              Temuco.
            </h3>
          </v-alert>
        </v-card-text>
      </v-card>
      <v-card>
        <v-card-text>
          <v-alert border="top" type="">
            <v-list color="rgba(255,255,255,0)" dense>
              <v-list-item-group>
                <v-list-item>
                  <v-list-item-icon>
                    <v-btn icon color="success">
                      <v-icon large>mdi-check</v-icon>
                    </v-btn>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title class="mt-3">Planifica</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-btn icon color="success">
                      <v-icon large>mdi-check</v-icon>
                    </v-btn>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title class="mt-3">Controla</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-icon>
                    <v-btn icon color="success">
                      <v-icon large>mdi-check</v-icon>
                    </v-btn>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title class="mt-3">Monitorea</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-alert>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>


 

<script>
export default {
  name: "Home",
  data() {
    return {};
  },
  mounted() {},
  methods: {},
};
</script>