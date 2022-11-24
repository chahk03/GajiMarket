import http from "@/api/http";
import axios from "axios";

const boardStore = {
  namespaced: true,
  state: {
    boardList: [],
    adminList: [],
    startPage: "",
    currPage: "",
    totalPage: "",
    endPage: "",
    board: {},
    writerImg: {},
    isrecommend: "",
    cList: [],
    cListImg: [],
    image: "",
  },
  getters: {},
  mutations: {
    BOARD_GET_LIST(state, payload) {
      state.boardList = payload.boardList;
      state.startPage = payload.startPage;
      state.currPage = payload.currPage;
      state.totalPage = payload.totalPage;
      state.endPage = payload.endPage;
    },
    BOARD_GET_ADMIN_LIST(state, payload) {
      state.adminList = payload.adminList;
    },
    BOARD_GET_ONE(state, payload) {
      state.board = payload.board;
      state.writerImg = payload.writerImg;
      state.isrecommend = payload.isrecommend;
      state.cList = payload.cList;
      state.cListImg = payload.cListImg;
      if (payload.file.length != 0) {
        state.image = payload.file[0].savedPath;
      } else {
        state.image = "";
      }
    },
    BOARD_UPDATE(state, payload) {
      state.board = payload;
    },
    BOARD_IMG_UPDATE(state, payload) {
      state.image = payload.file[0].savedPath;
    },
    COMMENT_WRITE(state, payload) {
      state.cList = payload.cList;
      state.cListImg = payload.cListImg;
    },
    BOARD_RECOMMEND_UPDATE(state, payload) {
      state.board.recommend = payload;
      state.isrecommend = !state.isrecommend;
    },
  },
  actions: {
    boardGetList({ commit }, payload) {
      console.log(payload);
      http.get("/board?page=" + payload).then(({ data }) => {
        console.log("data : ", data);
        console.log("commit : ", commit);
        commit("BOARD_GET_LIST", data);
      });
    },
    boardGetAdminList({ commit }) {
      http.get("/board/adminlist").then(({ data }) => {
        console.log(data);
        commit("BOARD_GET_ADMIN_LIST", data);
      });
    },
    boardGetOne({ commit }, payload) {
      console.log(payload);
      http
        .get("/board/read?bno=" + payload.bno + "&userid=" + payload.userid + "&username=" + payload.username)
        .then(({ data }) => {
          console.log(data);
          commit("BOARD_GET_ONE", data);
        });
    },
    boardGetSearch({ commit }, payload) {
      http.post("/board/list", payload).then(({ data }) => {
        commit("BOARD_GET_LIST", data);
      });
    },
    boardCreate({ commit }, payload) {
      console.log(commit);
      axios.post("http://localhost:8888/board/board", payload).then(({ data }) => {
        console.log(data);
      });
    },
    boardDelete({ commit }, payload) {
      console.log(commit);
      http.delete("/board/" + payload).then(({ data }) => {
        console.log(data);
        alert(data);
      });
    },
    boardUpdate({ commit }, payload) {
      console.log(payload);
      http.put("/board/modify", payload).then(({ data }) => {
        console.log(data);
        if (data.msg === "success") {
          commit("BOARD_UPDATE", data.board);
        }
      });
    },
    boardImgUpdate({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      axios.post("http://localhost:8888/board/board/image", payload).then(({ data }) => {
        console.log(data);
        commit("BOARD_IMG_UPDATE", data);
      });
    },
    boardRecommendUpdate({ commit }, payload) {
      console.log("추천수 업데이트");
      console.log(payload);
      http.get("/board/recommend?userid=" + payload.userid + "&bno=" + payload.bno).then(({ data }) => {
        console.log(data);
        commit("BOARD_RECOMMEND_UPDATE", data);
      });
    },
    commentWrite({ commit }, payload) {
      console.log(payload);
      console.log("commit : ", commit);
      http.post(`/board/comment`, payload).then(({ data }) => {
        if (data.msg === "success") {
          console.log(data);
          commit("COMMENT_WRITE", data);
        }
      });
    },
    commentUpdate({ commit }, payload) {
      console.log(payload);
      http.post(`board/comment/update`, payload).then(({ data }) => {
        if (data.msg === "success") {
          console.log(data);
          commit("COMMENT_WRITE", data);
        }
      });
    },
    commentDelete({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      http.delete("/board/comment/delete?bno=" + payload.bno + "&cno=" + payload.cno).then(({ data }) => {
        if (data.msg === "success") {
          console.log(data);
          commit("COMMENT_WRITE", data);
        }
      });
    },
  },
};

export default boardStore;
