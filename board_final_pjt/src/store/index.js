import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";

import boardStore from "@/store/modules/boardStore";
import aptStore from "@/store/modules/aptStore";
import userStore from "@/store/modules/userStore";
import chatStore from "@/store/modules/chatStore";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    boardStore,
    aptStore,
    userStore,
    chatStore,
  },
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
});
