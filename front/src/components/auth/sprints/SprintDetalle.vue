<template >
  <div>SPRINT CSM</div>
</template>


<script>
import axios from "axios";
export default {
  name: "SprintDetalle",

  data() {
    return {
      apiUrl: "http://localhost:8080/",
      sprint: null,
    };
  },
  mounted() {
    this.id = this.$route.params.sprint;
    this.getInfo();
  },
  methods: {
    async getInfo() {
      let token = localStorage.getItem("token");
      await axios
        .get(this.apiUrl + "getInfoSprint/" + this.id, {
          headers: {
            Authorization: token,
          },
        })
        .then((response) => (this.sprint = response.data))
        .catch((error) => console.log(error));
      console.log(this.sprint);
    },
  },
};
</script>