<template>
  <v-container
    class="bv-example-row mt-3 text-center"
    style="width: 1200px; margin-bottom: 150px"
  >
    <router-view></router-view>
  </v-container>
</template>

<script>
export default {
  name: "AppApt",
};
</script>

<style>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(
    180deg,
    rgba(255, 255, 255, 0) 70%,
    rgba(128, 30, 255, 0.3) 30%
  );
}
</style>
