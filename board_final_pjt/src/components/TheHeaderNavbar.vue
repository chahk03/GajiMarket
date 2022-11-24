<template fixed>
  <!-- <v-card class="overflow-hidden"> -->
  <v-app-bar
    id="navbar"
    absolute
    color="#7E57C2"
    elevate-on-scroll
    scroll-target="#scrolling-techniques-7"
  >
    <v-menu bottom left offset-y open-on-hover>
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon color="white" v-bind="attrs" v-on="on">
          <v-app-bar-nav-icon></v-app-bar-nav-icon>
        </v-btn>
      </template>

      <v-list>
        <v-list-item to="/">
          <v-list-item-title>메인</v-list-item-title>
        </v-list-item>
        <v-list-item :to="{ name: 'apt' }">
          <v-list-item-title>아파트</v-list-item-title>
        </v-list-item>
        <v-list-item :to="{ name: 'board' }">
          <v-list-item-title>게시판</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>

    <v-app-bar-title>뿅가지마켓!</v-app-bar-title>

    <v-spacer></v-spacer>

    <v-btn icon :to="{ name: 'chat' }">
      <v-icon>mdi-message-text</v-icon>
    </v-btn>

    <v-btn :to="{ name: 'aptinterest' }" icon>
      <v-icon>mdi-heart</v-icon>
    </v-btn>

    <v-menu bottom left offset-y open-on-hover>
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon color="white" v-bind="attrs" v-on="on">
          <v-icon>mdi-account-circle</v-icon>
        </v-btn>
      </template>

      <!-- 로그인 되었을 때 -->
      <v-list v-if="userInfo">
        <v-list-item :to="{ name: 'usermypage' }">
          <v-list-item-title>마이페이지</v-list-item-title>
        </v-list-item>
        <v-list-item @click.prevent="onClickLogout">
          <v-list-item-title>로그아웃</v-list-item-title>
        </v-list-item>
      </v-list>

      <!-- 로그인 안되었을 때 -->
      <v-list v-else>
        <v-list-item :to="{ name: 'user' }">
          <v-list-item-title>로그인</v-list-item-title>
        </v-list-item>
        <v-list-item :to="{ name: 'usersignup' }">
          <v-list-item-title>회원가입</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script>
import { mapState, mapGetters, mapActions } from "vuex";

const userStore = "userStore";

export default {
  name: "TheHeaderNavbar",
  data() {
    return {};
  },
  computed: {
    ...mapState(userStore, ["isLogin", "userInfo"]),
    ...mapGetters(["checkUserInfo"]),
  },
  methods: {
    ...mapActions(userStore, ["userLogout"]),
    // ...mapMutations(memberStore, ["SET_IS_LOGIN", "SET_USER_INFO"]),
    onClickLogout() {
      // this.SET_IS_LOGIN(false);
      // this.SET_USER_INFO(null);
      // sessionStorage.removeItem("access-token");
      // if (this.$route.path != "/") this.$router.push({ name: "main" });
      console.log(this.userInfo.userid);
      //vuex actions에서 userLogout 실행(Backend에 저장 된 리프레시 토큰 없애기
      //+ satate에 isLogin, userInfo 정보 변경)
      // this.$store.dispatch("userLogout", this.userInfo.userid);
      this.userLogout(this.userInfo.userid);
      sessionStorage.removeItem("access-token"); //저장된 토큰 없애기
      sessionStorage.removeItem("refresh-token"); //저장된 토큰 없애기
      if (this.$route.path != "/") this.$router.push({ name: "main" });
    },
  },
};
</script>

<style>
@font-face {
  font-family: "jua";
  src: url("@/assets/fonts/BMJUA_ttf.ttf");
}
@font-face {
  font-family: "dohyeon";
  src: url("@/assets/fonts/BMDOHYEON_ttf.ttf");
}
#navbar {
  font-family: "dohyeon";
  font-size: 30px;
  color: yellow;
}
</style>
