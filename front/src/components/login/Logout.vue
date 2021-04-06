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
    this.$emit("isLoggedFalse",false)
    this.$emit("showLogginTrue", true)
  },

};
</script>
