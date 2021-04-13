<template>
  <div></div>
</template>

<script>
export default {
  name: "Logout",
  components: {},

  data() {
    return {
     
    };
  },

  mounted() {
    localStorage.removeItem("token")
    localStorage.removeItem("email")
    this.$router.push("/login")
  },

};
</script>
