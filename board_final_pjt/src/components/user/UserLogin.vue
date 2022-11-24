<template>
  <div>
    <h3 class="underline-hotpink" style="margin-top: 100px; margin-bottom: 95px">
      <b-icon icon="journals"></b-icon>
      <!-- <v-img src="@/assets/eggplant-logo.png" width="40px"></v-img> -->
      로그인
    </h3>

    <div style="width: 500px; margin: 0 auto; margin-bottom: 330px">
      <v-simple-table>
        <thead>
          <tr>
            <th class="text-left">아이디</th>
          </tr>
          <tr>
            <v-text-field
              id="userid"
              v-model="userid"
              :error-messages="idErrorMessages"
              label="아이디를 입력해주세요."
              @keyup.enter="confirm"
              single-line
              outlined
              dense
            ></v-text-field>
          </tr>
        </thead>
      </v-simple-table>
      <v-simple-table style="margin-bottom: 20px">
        <thead>
          <tr>
            <th class="text-left">비밀번호</th>
          </tr>
          <tr>
            <v-text-field
              id="userpwd"
              type="password"
              v-model="userpwd"
              :error-messages="pwdErrorMessages"
              label="비밀번호를 입력해주세요."
              @keyup.enter="confirm"
              single-line
              outlined
              dense
              style="font-family: monospace"
            ></v-text-field>
          </tr>
        </thead>
      </v-simple-table>
      <v-btn @click="confirm" width="85px">로그인</v-btn>&nbsp;&nbsp;
      <v-btn :to="{ name: 'usersignup' }" width="85px">회원가입</v-btn>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

const userStore = "userStore";

export default {
  name: "UserLogin",
  data() {
    return {
      // isLoginError: false,
      userid: null,
      userpwd: null,
      idErrorMessages: "",
      pwdErrorMessages: "",
    };
  },
  computed: {
    ...mapState(userStore, ["isLogin", "isLoginError", "userInfo"]),
  },
  methods: {
    ...mapActions(userStore, ["userConfirm", "getUserInfo"]),
    async confirm() {
      if (this.inputCheck()) {
        let user = {
          userid: this.userid,
          userpwd: this.userpwd,
        };
        await this.userConfirm(user);
        let token = sessionStorage.getItem("access-token");
        console.log("1. confirm() token >> " + token);
        if (this.isLogin) {
          console.log("if문 안에");
          await this.getUserInfo(token);
          console.log("4. confirm() userInfo :: ", this.userInfo);
          this.$router.push({ name: "usermypage" });
        } else {
          alert("아이디와 비밀번호를 확인해주세요.");
          this.userid = "";
          this.userpwd = "";
        }
      }
    },
    movePage() {
      this.$router.push({ name: "join" });
    },
    inputCheck() {
      if (!this.userid) {
        this.idErrorMessages = "아이디를 입력해주세요.";
      }

      if (!this.userpwd) {
        this.pwdErrorMessages = "비밀번호를 입력해주세요.";
      }

      if (this.idErrorMessages || this.pwdErrorMessages) {
        return false;
      }

      return true;
    },
  },
  watch: {
    userid() {
      this.idErrorMessages = "";
    },
    userpwd() {
      this.pwdErrorMessages = "";
    },
  },
};
</script>

<style>
.underline-hotpink {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(128, 30, 255, 0.3) 30%);
}
</style>
