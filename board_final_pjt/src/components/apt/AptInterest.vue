<template>
  <div>
    <h3
      class="underline-hotpink"
      style="margin-top: 100px; margin-bottom: 50px"
    >
      <b-icon icon="journals"></b-icon>
      <!-- <v-img src="@/assets/eggplant-logo.png" width="40px"></v-img> -->
      북마크
    </h3>
    <div style="margin-bottom: 20px">
      <div v-if="!aptInterestList.length" style="margin-bottom: 550px"></div>
      <v-sheet
        class="mx-auto"
        max-width="auto"
        elevation="8"
        v-if="aptInterestList.length"
      >
        <v-slide-group multiple show-arrows class="pa-4">
          <v-slide-item
            v-for="(interest, index) in aptInterestList"
            :key="index"
          >
            <v-card class="ma-4" max-width="374" v-if="aptInterestList.length">
              <v-img
                height="250"
                width="300"
                :src="require(`@/assets/apt${index + 1}.jpg`)"
              ></v-img>

              <v-card-title>{{ interest.aptname }}</v-card-title>

              <v-card-text>
                <div class="my-4 text-start" style="font-family: dohyeon">
                  {{ interest.dongname }}<br />
                  법정코드 : {{ interest.aptcode }} <br />
                </div>
              </v-card-text>
              <v-col class="d-flex justify-end">
                <v-btn color="deep-purple" x-small dark
                  ><a
                    :href="`https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=${interest.dongname}${interest.aptname}`"
                    target="”_blank”"
                  >
                    네이버 검색
                  </a>
                </v-btn>
                &nbsp;&nbsp;
                <v-btn
                  color="deep-purple"
                  x-small
                  dark
                  @click="deleteInterest(interest.aptname)"
                  >북마크 해제</v-btn
                >
              </v-col>
            </v-card>
          </v-slide-item>
        </v-slide-group>
      </v-sheet>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const aptStore = "aptStore";
const userStore = "userStore";
export default {
  data() {
    return {
      url: [],
    };
  },
  created() {
    this.AptInterestList(this.userInfo.userid);
  },
  watch: {},
  methods: {
    deleteInterest(aname) {
      let data = {
        userid: this.userInfo.userid,
        aptname: aname,
      };
      this.AptInterestDelete(data);
    },
    ...mapActions(aptStore, ["AptInterestList", "AptInterestDelete"]),
  },
  computed: {
    ...mapState(aptStore, ["aptInterestList"]),
    ...mapState(userStore, ["userInfo"]),
  },
};
</script>

<style>
a:link {
  color: rgb(255, 255, 255);
  text-decoration: none;
}
a:visited {
  color: rgb(255, 255, 255);
  text-decoration: none;
}
a:hover {
  color: rgb(255, 255, 255);
  text-decoration: none;
}

div {
  font-family: "dohyeon";
}
</style>
