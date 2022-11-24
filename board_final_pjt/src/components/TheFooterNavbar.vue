<template>
  <v-card height="45">
    <v-footer absolute :padless="padless">
      <v-card flat tile width="100%" color="#7E57C2" class="text-center">
        <!-- <v-card-text>
          <v-btn v-for="icon in icons" :key="icon" class="mx-4" icon>
            <v-icon size="24px">
              {{ icon }}
            </v-icon>
          </v-btn>
        </v-card-text> -->

        <v-card-text class="white--text">
          {{ new Date().getFullYear() }} — <strong>chacha & sangmin</strong>
        </v-card-text>
      </v-card>
    </v-footer>
  </v-card>
</template>

<script>
export default {
  data: () => ({
    icons: ["mdi-home", "mdi-email", "mdi-calendar", "mdi-delete"],
    padless: true,
  }),
  computed: {},
};
</script>

<style></style>
