import http from "@/api/http";

const chatStore = {
  namespaced: true,
  state: {
    roomList: [],
    chatList: [],
  },
  getters: {},
  mutations: {
    SELECT_CHAT_ROOM_LIST(state, payload) {
      state.roomList = payload;
    },
    SELECT_CHAT_LIST(state, payload) {
      state.chatList = payload;
    },
  },
  actions: {
    async createChatRoom({ commit }, payload) {
      console.log(commit);
      console.log(payload);
      await http.post("/chat/room", payload).then(({ data }) => {
        let msg = "채팅방 개설에 실패했습니다.";
        if (data === "success") {
          msg = "채팅방이 개설되었습니다.";
          console.log(data);
        }
        alert(msg);
      });
      http.get("/chat/rooms").then(({ data }) => {
        console.log(data);
        commit("SELECT_CHAT_ROOM_LIST", data);
      });
    },
    selectChatRoomList({ commit }) {
      http.get("/chat/rooms").then(({ data }) => {
        console.log(data);
        commit("SELECT_CHAT_ROOM_LIST", data);
      });
    },
    selectChatList({ commit }, payload) {
      http.get("/chat/chats?roomId=" + payload).then(({ data }) => {
        console.log(data);
        commit("SELECT_CHAT_LIST", data);
      });
    },
  },
};

export default chatStore;
