<template >
  <div>
    <v-btn
      class="ma-2"
      color="primary"
      dark
      @click="dialogMembers = !dialogMembers"
    >
      Miembros
      <v-icon dark right> mdi-account-plus </v-icon>
    </v-btn>

    <v-dialog
      transition="dialog-top-transition"
      max-width="600"
      v-model="dialogMembers"
    >
      <v-card>
        <v-toolbar color="primary" dark>Listado de Miembros</v-toolbar>
        <v-card-text class="pt-2 text-center">
          <v-row v-for="member in members_" :key="member.value">
            <v-col>
              <v-btn class="ma-2" color="success" dark>
                <v-icon white class="pr-4">mdi-account</v-icon>
                {{ member.text }}
              </v-btn>
              <v-btn class="ma-2" small fab color="red darken-1">
                <v-icon color="white" @click="deleteMember(member.value)">
                  mdi-delete
                </v-icon>
              </v-btn>
            </v-col></v-row
          >
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-autocomplete
              v-model="selectedMembers"
              :items="allMembers"
              item-value="id"
              :item-text="(item) => item.name + ' ' + item.lastName"
              chips
              small-chips
              label="Listado de Miembros"
              multiple
              prepend-icon="mdi-human-queue"
            >
            </v-autocomplete>
            <v-btn
              class="ma-2"
              small
              fab
              color="success"
              :disabled="!selectedMembers.length > 0"
              @click="addMembers(selectedMembers)"
            >
              <v-icon color="white"> mdi-plus </v-icon>
            </v-btn>
          </v-form>
        </v-card-text>
        <v-card-actions class="justify-end">
          <v-btn text @click="dialogMembers = !dialogMembers">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>


<script>
import axios from "axios";
export default {
  props: ["members", "project_id"],
  name: "Members",
  data() {
    return {
      apiUrl: process.env.VUE_APP_APIURL,
      members_: null,
      dialogMembers: false,
      allMembers: null,
      selectedMembers: [],
      valid: false,
    };
  },
  mounted() {
    this.members_ = this.members;
    this.getAllMembersAvailable();
  },
  watch: {
    members: function (newVal) {
      this.members_ = newVal;
    },
  },
  methods: {
    async deleteMember(member) {
      const token = localStorage.getItem("token");
      await axios
        .delete(
          this.apiUrl +
            "deleteMemberFromTheProject/" +
            this.project_id +
            "/" +
            member,
          {
            headers: {
              Authorization: token,
            },
          }
        )
        .then(() => this.$emit("event")) //THIS EVENT CALL GET INFO
        .catch((error) => console.log(error));
    },
    async getAllMembersAvailable() {
      const token = localStorage.getItem("token");
      await axios
        .get(
          this.apiUrl +
            "getMembersAvailableToAddToTheProject/" +
            this.project_id,
          {
            headers: {
              Authorization: token,
            },
          }
        )
        .then((response) => (this.allMembers = response.data))
        .catch((error) => console.log(error));
    },
    async addMembers(members) {
      const token = localStorage.getItem("token");
      var membersToAdd = [];
      for (var i = 0; i < members.length; i++) {
        membersToAdd.push(members[i]);
      }
      var formData = new FormData();
      formData.append("newMemberList", membersToAdd);
      formData.append("project_id", this.project_id);
      await axios
        .put(this.apiUrl + "addMemberToProject/", formData, {
          headers: {
            Authorization: token,
          },
        })
        .then(this.$emit("event"))
        .catch((error) => console.log(error));
    },
  },
};
</script>
