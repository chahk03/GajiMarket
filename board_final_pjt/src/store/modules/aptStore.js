import http from "@/api/http";
const aptStore = {
  namespaced: true,
  state: {
    sidoList: [],
    gugunList: [],
    dongList: [],
    aptSearchList: [],
    aptInterestList: [],
    aptRecentList: [],
    isInterest: "",
  },
  getters: {},
  mutations: {
    GET_SIDO_NAMES(state, payload) {
      state.sidoList = payload;
    },
    GET_GUGUN_NAMES(state, payload) {
      state.gugunList = payload;
    },
    GET_DONG_NAMES(state, payload) {
      state.dongList = payload;
    },
    GET_SEARCH_LIST(state, payload) {
      state.aptSearchList = payload;
    },
    APT_INTEREST_LIST(state, payload) {
      state.aptInterestList = payload.interestList;
    },
    APT_GET_RECENT_LIST(state, payload) {
      state.aptRecentList = payload;
    },
    GET_IS_INTEREST(state, payload) {
      state.isInterest = payload;
    },
    IS_INTEREST_REFRESH(state) {
      state.isInterest = null;
    },
  },
  actions: {
    getSidoNames({ commit }) {
      http.get("/apt/sido").then(({ data }) => {
        console.log(data);
        commit("GET_SIDO_NAMES", data);
      });
    },
    getGugunNames({ commit }, payload) {
      http.get("/apt/gugun?sidoName=" + payload).then(({ data }) => {
        console.log(data);
        commit("GET_GUGUN_NAMES", data);
      });
    },
    getDongNames({ commit }, payload) {
      console.log(payload);
      http.get("/apt/dong?sidoName=" + payload.sidoName + "&gugunName=" + payload.gugunName).then(({ data }) => {
        console.log(data);
        commit("GET_DONG_NAMES", data);
      });
    },
    getSearchList({ commit }, payload) {
      console.log(payload.sido);
      console.log(payload.dong);

      http
        .get(
          "/apt/search?sido=" +
            payload.sido +
            "&dong=" +
            payload.dong +
            "&dealYear=" +
            payload.year +
            "&dealMonth=" +
            payload.month
        )
        .then(({ data }) => {
          console.log(data);
          commit("GET_SEARCH_LIST", data);
        });
    },
    registAptInterest({ commit }, payload) {
      console.log(commit);
      http.post("/apt/registinterest", payload).then(({ data }) => {
        alert(data.msg);
      });
    },
    AptInterestList({ commit }, payload) {
      http.get("/apt/listinterest?userid=" + payload).then(({ data }) => {
        console.log(data.interestList);
        commit("APT_INTEREST_LIST", data);
      });
    },
    AptInterestDelete({ commit }, payload) {
      console.log(commit);
      http.delete("/apt?userid=" + payload.userid + "&aptname=" + payload.aptname).then(({ data }) => {
        alert(data.msg);
        if (data.msg === "관심지역 해제 성공") {
          commit("APT_INTEREST_LIST", data);
        }
      });
    },
    aptGetRecentList({ commit }) {
      http.get("/apt/recent").then(({ data }) => {
        console.log(data);
        commit("APT_GET_RECENT_LIST", data);
      });
    },
    aptInterestCount({ commit }, payload) {
      console.log(commit);
      commit("IS_INTEREST_REFRESH");
      http.get("/apt/isinterest?userid=" + payload.userid + "&aptname=" + payload.aptname).then(({ data }) => {
        commit("GET_IS_INTEREST", data);
      });
    },
  },
};

export default aptStore;
