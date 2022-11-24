<template>
  <v-container class="bv-example-row mt-3 text-center" style="width: 1200px; margin-bottom: 150px">
    <h3 class="underline-hotpink" style="margin-top: 100px; margin-bottom: 50px">
      <b-icon icon="journals"></b-icon> 바가지마켓
    </h3>
    <router-view></router-view>
  </v-container>
</template>
<script>
export default {
  name: "AppBoard",
};
</script>
<style scoped>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(128, 30, 255, 0.3) 30%);
}
</style>
