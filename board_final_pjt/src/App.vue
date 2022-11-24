<template>
  <v-app ref="app">
    <v-card class="overflow-hidden">
      <the-header-navbar style="position: fixed" />
      <v-sheet id="scrolling-techniques-2" class="overflow-y-auto;">
        <v-container-fluid
          style="padding-top: 100px; padding-bottom: 100px; overflow-hidden"
        >
          <router-view />
        </v-container-fluid>
      </v-sheet>
      <the-footer-navbar />
    </v-card>
  </v-app>
</template>

<script>
import TheHeaderNavbar from "@/components/TheHeaderNavbar";
import TheFooterNavbar from "@/components/TheFooterNavbar";

export default {
  name: "App",
  components: {
    TheHeaderNavbar,
    TheFooterNavbar,
  },
};
</script>

<style>
h3 {
  font-family: "dohyeon";
  font-size: 30px;
  color: rgb(64, 0, 124);
}

#app {
  font-family: "dohyeon";
}
</style>
