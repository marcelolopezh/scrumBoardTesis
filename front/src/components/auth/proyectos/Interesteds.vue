<template >
  <div>
    <v-btn
      class="ma-2"
      color="primary"
      dark
      @click="dialogInteresteds = !dialogInteresteds"
    >
      Interesados
      <v-icon dark right> mdi-account-plus </v-icon>
    </v-btn>

    <v-dialog
      transition="dialog-top-transition"
      max-width="600"
      v-model="dialogInteresteds"
    >
      <v-card>
        <v-toolbar color="primary" dark>Listado de Interesados</v-toolbar>
        <v-card-text class="pt-6 text-center">
          <v-row v-for="interested in interesteds_" :key="interested.value">
            <v-col>
              <v-btn class="ma-2" color="success" dark>
                <v-icon white class="pr-4">mdi-account</v-icon>
                {{ interested.text }}
              </v-btn>
              <v-btn class="ma-2" small fab color="red darken-1">
                <v-icon color="white" @click="deleteInterested(interested.value)">
                  mdi-delete
                </v-icon>
              </v-btn>
            </v-col></v-row
          >
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="dialogInteresteds = !dialogInteresteds">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
//import axios from "axios";
export default {
  props: ["interesteds"],
  name: "Interesteds",
  data() {
    return {
      interesteds_: null,
      dialogInteresteds: false,
    };
  },
  mounted() {
    this.interesteds_ = this.interesteds;
  },
  methods: {
    deleteInterested(interested) {
      console.log(interested);
    },
  },
};
</script>
