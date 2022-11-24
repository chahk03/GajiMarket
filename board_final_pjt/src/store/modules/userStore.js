import jwtDecode from "jwt-decode";
import router from "@/router";
import http from "@/api/http";
import axios from "axios";

const userStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isValidToken: false,
    age: null,
    ageconfidence: null,
    gender: null,
    genderconfidence: null,
    celebrity: null,
    celebrityconfidence: null,
    myInfo: null,
    myImg: null,
    canUseId: null,
    canUseName: null,
    celebrityImage: null,
  },
  getters: {
    checkUserInfo: function (state) {
      return state.userInfo;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
  },
  mutations: {
    SET_IS_LOGIN: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    SET_IS_LOGIN_ERROR: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO: (state, userInfo) => {
      state.isLogin = true;
      state.userInfo = userInfo;
    },
    CFR_API(state, payload) {
      state.age = payload.faces.faces[0].age.value;
      state.ageconfidence = payload.faces.faces[0].age.confidence;
      state.gender = payload.faces.faces[0].gender.value;
      state.genderconfidence = payload.faces.faces[0].gender.confidence;
      state.celebrity = payload.celebrity.faces[0].celebrity.value;
      state.celebrityconfidence = payload.celebrity.faces[0].celebrity.confidence;
    },
    SIGNUP_DATA_RESET(state) {
      state.age = null;
      state.ageconfidence = null;
      state.gender = null;
      state.genderconfidence = null;
      state.celebrity = null;
      state.celebrityconfidence = null;
    },
    SET_USER_PAGE(state, payload) {
      state.myInfo = payload.myInfo;
      state.myImg = payload.myImg.savedPath;
    },
    MODIFY_USER_INFO(state, payload) {
      state.myInfo = payload.myInfo;
    },
    MODIFY_USER_IMAGE(state, payload) {
      state.myImg = payload.myImg.savedPath;
    },
    USER_ID_CHECK(state, payload) {
      state.canUseId = payload;
    },
    USER_NAME_CHECK(state, payload) {
      state.canUseName = payload;
    },
    GET_CELEBRITY_IMAGE(state, payload) {
      state.celebrityImage = payload;
    },
    RESET_ID_CHECK(state) {
      state.canUseId = null;
    },
    RESET_NAME_CHECK(state) {
      state.canUseName = null;
    },
  },
  actions: {
    userSignup({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      axios.post("http://localhost:8888/board/user/signup", payload).then(({ data }) => {
        console.log(data);
        alert(data);
      });
    },
    getMyInfo({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      http
        .get("/user/mypage?userid=" + payload.userid + "&username=" + payload.username)
        .then(({ data }) => {
          console.log(data);
          commit("SET_USER_PAGE", data);
        });
    },
    updateImg({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      axios.post("http://localhost:8888/board/user/image", payload).then(({ data }) => {
        console.log(data);
        commit("MODIFY_USER_IMAGE", data);
      });
    },
    updateInfo({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      http.post("/user/modify", payload).then(({ data }) => {
        console.log(data);
        alert(data.msg);
        commit("MODIFY_USER_INFO", data);
      });
    },
    async userConfirm({ commit }, user) {
      await http
        .post(`/user/login`, JSON.stringify(user))
        .then(({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            console.log("login success token created!!!! >> ", accessToken, refreshToken);
            commit("SET_IS_LOGIN", true);
            commit("SET_IS_LOGIN_ERROR", false);
            commit("SET_IS_VALID_TOKEN", true);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit("SET_IS_LOGIN", false);
            commit("SET_IS_LOGIN_ERROR", true);
            commit("SET_IS_VALID_TOKEN", false);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    async getUserInfo({ commit, dispatch }, token) {
      let decodeToken = jwtDecode(token);
      console.log("2. getUserInfo() decodeToken :: ", decodeToken);
      http.defaults.headers["access-token"] = sessionStorage.getItem("access-token");
      await http
        .get(`/user/info/${decodeToken.userid}`)
        .then(({ data }) => {
          if (data.message === "success") {
            commit("SET_USER_INFO", data.userInfo);
            // console.log("3. getUserInfo data >> ", data);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        })
        .catch(async (error) => {
          console.log(
            "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
            error.response.status
          );
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        });
    },
    async tokenRegeneration({ commit, state }) {
      console.log("토큰 재발급 >> 기존 토큰 정보 : {}", sessionStorage.getItem("access-token"));
      http.defaults.headers["refresh-token"] = sessionStorage.getItem("refresh-token"); //axios header에 refresh-token 셋팅
      await http
        .post(`/user/refresh`, JSON.stringify(state.userInfo))
        .then(({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit("SET_IS_VALID_TOKEN", true);
          }
        })
        .catch(async (error) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (error.response.status === 401) {
            console.log("갱신 실패");
            // 다시 로그인 전 DB에 저장된 RefreshToken 제거.
            await http
              .get(`/user/logout/${state.userInfo.userid}`)
              .then(({ data }) => {
                if (data.message === "success") {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
                commit("SET_IS_VALID_TOKEN", false);
                router.push({ name: "login" });
              })
              .catch((error) => {
                console.log(error);
                commit("SET_IS_LOGIN", false);
                commit("SET_USER_INFO", null);
              });
          }
        });
    },
    async userLogout({ commit }, userid) {
      await http
        .get(`/user/logout/${userid}`)
        .then(({ data }) => {
          if (data.message === "success") {
            commit("SET_IS_LOGIN", false);
            commit("SET_USER_INFO", null);
            commit("SET_IS_VALID_TOKEN", false);
          } else {
            console.log("유저 정보 없음!!!!");
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    CFRApi({ commit }, payload) {
      axios.post("http://localhost:8888/board/naver", payload).then(({ data }) => {
        console.log(data.celebrity.faces);
        console.log(data.faces.faces);
        commit("CFR_API", data);
      });
    },
    userDataReset({ commit }) {
      commit("SIGNUP_DATA_RESET");
    },
    userIdCheck({ commit }, payload) {
      http.get("/user/idcheck?userid=" + payload).then(({ data }) => {
        console.log(data);
        // alert(data);
        if (data == "이미 존재하는 아이디입니다.") {
          commit("USER_ID_CHECK", false);
        } else {
          commit("USER_ID_CHECK", true);
        }
      });
    },
    userNameCheck({ commit }, payload) {
      http.get("/user/namecheck?username=" + payload).then(({ data }) => {
        console.log(data);
        // alert(data);
        if (data == "이미 존재하는 별명입니다.") {
          commit("USER_NAME_CHECK", false);
        } else {
          commit("USER_NAME_CHECK", true);
        }
      });
    },
    deleteUserInfo({ commit }, payload) {
      http
        .delete("/user?username=" + payload.username + "&userid=" + payload.userid)
        .then(({ data }) => {
          console.log(commit);
          console.log(data);
        });
    },
    getCelebrityImage({ commit }, payload) {
      console.log("셀럽 이름", payload);
      console.log(commit);
      axios
        .post("http://localhost:8888/board/naver/imagesearch?celebrity=" + payload)
        .then(({ data }) => {
          console.log(data);
          commit("GET_CELEBRITY_IMAGE", data);
        });
    },
    resetIdCheck({ commit }) {
      commit("RESET_ID_CHECK");
    },
    resetNameCheck({ commit }) {
      commit("RESET_NAME_CHECK");
    },
  },
};

export default userStore;
