<template>
  <div style="width: 800px; margin: 0 auto; margin-bottom: 150px">
    <h3
      class="underline-hotpink"
      style="margin-top: 100px; margin-bottom: 50px"
    >
      <b-icon icon="journals"></b-icon>
      <!-- <v-img src="@/assets/eggplant-logo.png" width="40px"></v-img> -->
      회원가입
    </h3>
    <v-form id="sendForm" ref="sendFrom" enctype="multipart/form-data">
      <v-simple-table v-if="show">
        <template v-slot:default>
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <th class="text-left" colspan="1">아이디</th>
            <td class="text-left" colspan="11">
              <v-text-field
                name="id"
                v-model="id"
                :error-messages="idErrorMessages"
                label="아이디를 입력하세요."
                required
                @blur="checkid"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">비밀번호</th>
            <td class="text-left" colspan="11">
              <v-text-field
                name="pwd"
                v-model="pwd"
                :error-messages="pwdErrorMessages"
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
              <v-text-field
                name="name"
                v-model="name"
                :error-messages="nameErrorMessages"
                label="이름을 입력하세요."
                required
                @blur="checkname"
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">이메일</th>
            <td class="text-left" colspan="10">
              <v-text-field
                name="email"
                v-model="email"
                :error-messages="emailErrorMessages"
                label="이메일을 입력하세요."
                required
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <br />
          </tr>
          <tr>
            <td colspan="12">
              얼굴 사진을 첨부하면 추정 나이, 성별, 닮은꼴 연예인을
              알려드립니다. 아쉽지만 필! 수! 에요.
            </td>
          </tr>
          <tr>
            <br />
          </tr>
          <tr>
            <th class="text-left" colspan="1">이미지 첨부</th>
            <td class="text-left" colspan="11" style="overflow: hidden">
              <v-img
                v-if="preview !== ''"
                class="pa-12 rounded-circle d-inline-block"
                :src="preview"
                width="30"
              />
              <v-file-input
                type="file"
                name="uploadFile"
                v-model="file"
                :rules="fileRules"
                :error-messages="fileErrorMessages"
                required
              />
              <!-- <v-btn variant="outline-primary" @click="naverCFRApi">사진제출</v-btn> -->
            </td>
          </tr>
          <tr>
            <br />
          </tr>
          <tr>
            <th class="text-left" colspan="1">나이</th>
            <td class="text-left" colspan="11">
              <v-text-field
                v-if="age"
                name="age"
                v-model="age"
                readonly
              ></v-text-field>
              <v-text-field v-if="!age" readonly></v-text-field>
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">성별</th>
            <td class="text-left" colspan="11">
              <v-text-field
                v-if="gender"
                name="gender"
                v-model="gender"
                readonly
              ></v-text-field>
              <v-text-field v-if="!gender" readonly></v-text-field>
            </td>
          </tr>
          <tr>
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
        </template>
      </v-simple-table>
    </v-form>

    <br />
    <v-row align="center" justify="end">
      <v-col class="d-flex flex-row-reverse" cols="12" sm="1">
        <v-btn variant="outline-primary" @click="onSubmit"
          ><span style="font-size: 16px">가입</span></v-btn
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
            <v-progress-circular
              indeterminate
              color="purple"
            ></v-progress-circular>
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
                :src="celebrityImage.items[1].link"
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
                >{{ Math.round(genderconfidence * 10000) / 100 }}% 확률로 남성
                입니다.</span
              >
              <span v-if="gender === 'female'" style="font-size: 25px"
                >{{ Math.round(genderconfidence * 10000) / 100 }}% 확률로 여성
                입니다.</span
              >
            </v-list-item>
            <v-list-item class="d-flex justify-center">
              <span style="font-size: 25px"
                >{{ Math.round(celebrityconfidence * 10000) / 100 }}% 확률로
                {{ celebrity }}을(를) 닮았습니다.</span
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
      id: "",
      pwd: "",
      name: "",
      email: "",
      file: [],
      show: true,
      idErrorMessages: "",
      pwdErrorMessages: "",
      nameErrorMessages: "",
      emailErrorMessages: "",
      fileErrorMessages: "",
      fileRules: [(v) => !!v || "이미지를 첨부해주세요."],
      dialog: false,
      loading: true,
      preview: "",
    };
  },
  created() {
    this.userDataReset();
  },
  mounted() {
    this.userDataReset();
  },
  methods: {
    checkid() {
      this.userIdCheck(this.id);
    },
    checkname() {
      this.userNameCheck(this.name);
    },
    async onSubmit() {
      if (this.inputCheck()) {
        var fd = new FormData();
        fd.append("uploadFile", this.file);
        fd.append("userid", this.id);
        fd.append("userpwd", this.pwd);
        fd.append("username", this.name);
        fd.append("email", this.email);
        fd.append("age", this.age);
        fd.append("gender", this.gender);
        fd.append("celebrity", this.celebrity);

        console.log(fd);
        await this.userSignup(fd);
        this.$router.push({ name: "userlogin" });
      }
    },
    async naverCFRApi() {
      var fd = new FormData();
      fd.append("loadFile", this.file);
      console.log(this.file);

      await this.CFRApi(fd);
    },
    inputCheck() {
      if (!this.id) {
        this.idErrorMessages = "아이디를 입력해주세요.";
      }
      if (!this.pwd) {
        this.pwdErrorMessages = "비밀번호를 입력해주세요.";
      }
      if (!this.name) {
        this.nameErrorMessages = "이름을 입력해주세요.";
      }
      if (!this.email) {
        this.emailErrorMessages = "이메일을 입력해주세요.";
      }
      if (this.file.length == 0) {
        this.fileErrorMessages = "이미지를 첨부해주세요.";
      }

      if (
        this.idErrorMessages ||
        this.pwdErrorMessages ||
        this.nameErrorMessages ||
        this.emailErrorMessages ||
        this.fileErrorMessages
      ) {
        return false;
      }

      return true;
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
      "CFRApi",
      "userDataReset",
      "userIdCheck",
      "userNameCheck",
      "getCelebrityImage",
      "resetIdCheck",
      "resetNameCheck",
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
      "canUseId",
      "canUseName",
      "celebrityImage",
    ]),
  },
  watch: {
    id() {
      this.idErrorMessages = "";
      this.resetIdCheck();
    },
    pwd() {
      this.pwdErrorMessages = "";
    },
    name() {
      this.nameErrorMessages = "";
      this.resetNameCheck();
    },
    email() {
      this.emailErrorMessages = "";
    },
    file() {
      this.fileErrorMessages = "";
      this.previewFile(this.file);
      this.naverCFRApi();
      this.dialog = true;
    },
    canUseId() {
      if (!this.canUseId) {
        this.idErrorMessages = "이미 사용 중인 아이디입니다.";
      } else {
        this.idErrorMessages = "";
      }
    },
    canUseName() {
      if (this.canUseName) {
        this.nameErrorMessages = "";
      } else {
        this.nameErrorMessages = "이미 사용 중인 닉네임입니다.";
      }
    },
    celebrity() {
      this.getCelebrityImage(this.celebrity);
      setTimeout(3000);
      if (this.celebrity) {
        console.log("사진 왔따.");
        this.loading = false;
      }
    },
  },
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

span {
  font-family: "dohyeon";
  font-size: 25px;
}
</style>
