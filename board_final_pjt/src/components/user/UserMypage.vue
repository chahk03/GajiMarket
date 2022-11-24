<template>
  <div style="width: 800px; margin: 0 auto">
    <h3 class="underline-hotpink" style="margin-top: 100px; margin-bottom: 50px">
      <b-icon icon="journals"></b-icon>
      <!-- <v-img src="@/assets/eggplant-logo.png" width="40px"></v-img> -->
      마이페이지
    </h3>
    <v-form id="sendForm" ref="sendFrom" enctype="multipart/form-data">
      <v-simple-table v-if="show && myInfo && myImg">
        <template v-slot:default>
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <th class="text-left" colspan="1">아이디</th>
            <td class="text-left" colspan="11">
              <v-text-field name="id" v-model="myInfo.userid" required readonly></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">비밀번호</th>
            <td class="text-left" colspan="11">
              <v-text-field
                v-if="flag"
                name="pwd"
                v-model="myInfo.userpwd"
                required
                readonly
                type="password"
                style="font-family: monospace"
              ></v-text-field>
              <v-text-field
                v-if="!flag"
                name="pwd"
                v-model="myInfo.userpwd"
                label="비밀번호를 입력하세요."
                required
                type="password"
                style="font-family: monospace"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">이름</th>
            <td class="text-left" colspan="11">
              <v-text-field name="name" v-model="myInfo.username" required readonly></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">이메일</th>
            <td class="text-left" colspan="10">
              <v-text-field
                v-if="flag"
                name="email"
                v-model="myInfo.email"
                required
                readonly
              ></v-text-field>
              <v-text-field
                v-if="!flag"
                name="email"
                v-model="myInfo.email"
                label="이메일을 입력하세요."
                required
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <br />
          </tr>
          <!-- <tr>
            <td colspan="12">
              사진을 첨부해주세요 추정나이, 성별, 닮은꼴 연예인을 알려드립니다 아쉽지만 필수에요
            </td>
          </tr> -->
          <tr>
            <th class="text-left" colspan="1">사진</th>
            <td class="text-left" colspan="11" style="overflow: hidden">
              <v-img
                v-if="flag"
                class="pa-12 primary rounded-circle d-inline-block"
                :src="require(`@/assets/memberImg/${myImg}`)"
                width="30"
              />
              <v-img
                v-if="!flag && preview === ''"
                class="pa-12 primary rounded-circle d-inline-block"
                :src="require(`@/assets/memberImg/${myImg}`)"
                width="30"
              />
              <v-img
                v-if="!flag && preview !== ''"
                class="pa-12 primary rounded-circle d-inline-block"
                :src="preview"
                width="30"
              />
              <v-file-input v-if="!flag" type="file" name="uploadFile" v-model="file" />
              <!-- <v-btn variant="outline-primary" @click="naverCFRApi">사진제출</v-btn> -->
            </td>
          </tr>
          <tr>
            <br />
          </tr>
        </template>
      </v-simple-table>

      <v-simple-table v-if="show && myInfo">
        <template v-slot:default>
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr style="padding-top: 100px" v-if="!flag && age">
            <th class="text-left" colspan="1">나이</th>
            <td class="text-left" colspan="11">
              <v-text-field v-if="age" name="age" v-model="age" readonly></v-text-field>
              <v-text-field v-if="!age" readonly></v-text-field>
            </td>
          </tr>
          <tr v-if="flag || !age">
            <th class="text-left" colspan="1">나이</th>
            <td class="text-left" colspan="10">
              <v-text-field name="age" v-model="myInfo.age" required readonly></v-text-field>
            </td>
          </tr>
          <tr id="naver" v-if="!flag && gender">
            <th class="text-left" colspan="1">성별</th>
            <td class="text-left" colspan="11">
              <v-text-field
                v-if="gender && gender === 'male'"
                name="gender"
                value="남성"
                readonly
              ></v-text-field>
              <v-text-field
                v-if="gender && gender === 'female'"
                name="gender"
                value="여성"
                readonly
              ></v-text-field>
              <v-text-field v-if="!gender" readonly></v-text-field>
            </td>
          </tr>
          <tr v-if="flag || !gender">
            <th class="text-left" colspan="1">성별</th>
            <td class="text-left" colspan="11">
              <v-text-field
                name="gender"
                v-if="myInfo.gender === 'male'"
                value="남성"
                required
                readonly
              ></v-text-field>
              <v-text-field
                name="gender"
                v-if="myInfo.gender === 'female'"
                value="여성"
                required
                readonly
              ></v-text-field>
            </td>
          </tr>
          <tr id="naver" v-if="!flag && celebrity">
            <th class="text-left" colspan="1">닮은꼴</th>
            <td class="text-left" colspan="11">
              <v-text-field
                v-if="celebrity"
                name="celebrity"
                v-model="celebrity"
                readonly
              ></v-text-field>
              <v-text-field v-if="!celebrity" readonly></v-text-field>
            </td>
          </tr>
          <tr v-if="flag || !celebrity">
            <th class="text-left" colspan="1">닮은꼴</th>
            <td class="text-left" colspan="11">
              <v-text-field
                name="celebrity"
                v-model="myInfo.celebrity"
                required
                readonly
              ></v-text-field>
            </td>
          </tr>
        </template>
      </v-simple-table>
    </v-form>
    <br />
    <v-row align="center" justify="end">
      <v-col class="d-flex justify-end" cols="12" sm="1">
        <v-btn variant="outline-primary" v-if="flag" @click="flag = !flag"
          ><span style="font-size: 16px">수정</span></v-btn
        >
        <v-btn variant="outline-primary" v-if="!flag" @click="modifyBtn"
          ><span style="font-size: 16px">수정</span></v-btn
        >
        &nbsp;&nbsp;&nbsp;&nbsp;
        <v-btn variant="outline-primary" @click="deleteInfo"
          ><span style="font-size: 16px">탈퇴</span></v-btn
        >
      </v-col>
    </v-row>

    <!-- 닮은꼴 분석 결과 dialog -->
    <v-dialog v-model="dialog" width="600px" height="450px">
      <v-card style="overflow-x: hidden">
        <v-toolbar
          color="#7E57C2"
          width="600px"
          dark
          style="position: fixed; z-index: 3; font-size: 25px"
        >
          <b>분석 결과</b>
        </v-toolbar>

        <v-list style="height: 450px; margin-top: 60px; margin-bottom: 5px">
          <div class="text-center" style="margin-top: 200px" v-if="loading">
            <v-progress-circular indeterminate color="purple"></v-progress-circular>
          </div>
          <div v-if="!loading">
            <v-list-item
              class="d-flex justify-center"
              style="margin-top: 20px; margin-bottom: 10px"
            >
              <v-img
                v-if="preview !== ''"
                class="pa-12 rounded-circle d-inline-block"
                :src="preview"
                max-height="250"
                max-width="250"
              />&nbsp;&nbsp;&nbsp;&nbsp;
              <v-img
                v-if="celebrityImage != null"
                class="pa-12 rounded-circle d-inline-block"
                :src="celebrityImage.items[0].link"
                max-height="250"
                max-width="250"
              />
            </v-list-item>
            <v-list-item class="d-flex justify-center" style="font-size: 25px">
              <span>{{ Math.round(ageconfidence * 10000) / 100 }}% 확률로 </span
              ><span style="background-color: white">{{ age }}살 입니다.</span>
            </v-list-item>
            <v-list-item class="d-flex justify-center">
              <span v-if="gender === 'male'" style="font-size: 25px"
                >{{ Math.round(genderconfidence * 10000) / 100 }}% 확률로 남성 입니다.</span
              >
              <span v-if="gender === 'female'" style="font-size: 25px"
                >{{ Math.round(genderconfidence * 10000) / 100 }}% 확률로 여성 입니다.</span
              >
            </v-list-item>
            <v-list-item class="d-flex justify-center">
              <span style="font-size: 25px"
                >{{ Math.round(celebrityconfidence * 10000) / 100 }}% 확률로 {{ celebrity }}을(를)
                닮았습니다.</span
              >
            </v-list-item>
          </div>
        </v-list>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

const userStore = "userStore";

export default {
  data() {
    return {
      file: [],
      show: true,
      flag: true,
      preview: "",
      loading: true,
      dialog: false,
      value: 0,
    };
  },
  created() {
    this.userDataReset();
    let data = {
      userid: this.userInfo.userid,
      username: this.userInfo.username,
    };
    this.getMyInfo(data);
  },
  mounted() {
    this.userDataReset();
    let data = {
      userid: this.userInfo.userid,
      username: this.userInfo.username,
    };
    this.getMyInfo(data);
    console.log("mount", this.myInfo);
  },
  methods: {
    async naverCFRApi() {
      var fd = new FormData();
      fd.append("loadFile", this.file);
      console.log(this.file);

      await this.CFRApi(fd);
    },
    modifyInfo() {
      let data = {
        userid: this.myInfo.userid,
        username: this.myInfo.username,
        userpwd: this.myInfo.userpwd,
        email: this.myInfo.email,
        age: this.age,
        gender: this.gender,
        celebrity: this.celebrity,
      };
      console.log(data);
      this.updateInfo(data);
    },
    async modifyImg() {
      var fd = new FormData();
      fd.append("username", this.myInfo.username);
      fd.append("uploadFile", this.file);
      this.updateImg(fd);
    },
    async modifyBtn() {
      this.modifyInfo();
      this.modifyImg();
      this.userDataReset();
      this.flag = true;
    },
    deleteInfo() {
      let data = {
        username: this.myInfo.username,
        userid: this.myInfo.userid,
      };
      this.deleteUserInfo(data);

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
      alert("탈퇴되었습니다.");
      if (this.$route.path != "/") this.$router.push({ name: "main" });
    },
    previewFile(file) {
      const fileData = (data) => {
        this.preview = data;
      };
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.addEventListener(
        "load",
        function () {
          fileData(reader.result);
        },
        false
      );
    },
    ...mapActions(userStore, [
      "userSignup",
      "getMyInfo",
      "updateImg",
      "updateInfo",
      "CFRApi",
      "userDataReset",
      "userLogout",
      "deleteUserInfo",
      "getCelebrityImage",
    ]),
  },
  computed: {
    ...mapState(userStore, [
      "userInfo",
      "age",
      "ageconfidence",
      "gender",
      "genderconfidence",
      "celebrity",
      "celebrityconfidence",
      "celebrityImage",
      "myInfo",
      "myImg",
    ]),
  },
  watch: {
    file() {
      this.previewFile(this.file);
      this.naverCFRApi();
      this.dialog = true;
    },
    celebrity() {
      this.getCelebrityImage(this.celebrity);
      setTimeout(3000);
      if (this.celebrity) {
        console.log("사진 왔따.");
        this.loading = false;
        console.log("나이", this.age);
      }
    },
  },
};
</script>

<style>
#naver {
  margin-top: 130px;
}
</style>
