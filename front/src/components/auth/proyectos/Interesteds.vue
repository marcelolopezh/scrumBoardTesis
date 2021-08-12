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
        <v-card-text class="pt-2 text-center">
          <v-row v-for="interested in interesteds_" :key="interested.value">
            <v-col cols="11">
              <v-btn block color="success" dark>
                <v-icon white class="pr-4">mdi-account</v-icon>
                {{ interested.text }}
              </v-btn>
            </v-col>
            <v-col cols="1">
              <v-btn
                block
                small
                fab
                color="red darken-1"
              >
                <v-icon
                  color="white"
                  @click="deleteInterested(interested.value)"
                >
                  mdi-delete
                </v-icon>
              </v-btn>
            </v-col></v-row
          >
          <v-form
            ref="form"
            v-model="valid"
            lazy-validation
          >
            <v-autocomplete
              v-model="selectedInteresteds"
              :items="allInteresteds"
              item-value="id"
              :item-text="(item) => item.name + ' ' + item.lastName"
              chips
              small-chips
              label="Listado de Interesados"
              multiple
              prepend-icon="mdi-human-queue"
            >
            </v-autocomplete>
            <v-btn
              class="ma-2"
              small
              fab
              color="success"
              :disabled="!selectedInteresteds.length > 0"
              @click="addInteresteds(selectedInteresteds)"
            >
              <v-icon color="white"> mdi-plus </v-icon>
            </v-btn>
          </v-form>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="dialogInteresteds = !dialogInteresteds"
            >Close</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
import axios from "axios";
export default {
  props: ["interesteds", "project", "user"],
  name: "Interesteds",
  data() {
    return {
      apiUrl: process.env.VUE_APP_APIURL,
      interesteds_: null,
      dialogInteresteds: false,
      allInteresteds: null,
      selectedInteresteds: [],
      valid: false,
    };
  },
  mounted() {
    this.interesteds_ = this.interesteds;
    this.getAllInterestedsAvailable();
  },
  watch: {
    interesteds: function (newVal) {
      this.interesteds_ = newVal;
      this.getAllInterestedsAvailable();
    },
  },
  methods: {
    async deleteInterested(interested) {
      const token = localStorage.getItem("token");
      await axios
        .delete(
          this.apiUrl +
            "deleteInterestedFromTheProject/" +
            this.project.id +
            "/" +
            interested,
          {
            headers: {
              Authorization: token,
            },
          }
        )
        .then(() => this.$emit("event")) //THIS EVENT CALL GET INFO
        .catch((error) => console.log(error));
    },
    async getAllInterestedsAvailable() {
      const token = localStorage.getItem("token");
      await axios
        .get(
          this.apiUrl +
            "getInterestedsAvailableToAddToTheProject/" +
            this.project.id,
          {
            headers: {
              Authorization: token,
            },
          }
        )
        .then((response) => (this.allInteresteds = response.data))
        .catch((error) => console.log(error));
    },
    async addInteresteds(interesteds) {
      const token = localStorage.getItem("token");
      var interestedsToAdd = [];
      for (var i = 0; i < interesteds.length; i++) {
        interestedsToAdd.push(interesteds[i]);
      }
      var formData = new FormData();
      formData.append("newInterestedList", interestedsToAdd);
      formData.append("project_id", this.project.id);
      await axios
        .put(this.apiUrl + "addInterestedToProject/", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(() => this.$emit("event"))
        .catch((error) => console.log(error));
    },
  },
};
</script>