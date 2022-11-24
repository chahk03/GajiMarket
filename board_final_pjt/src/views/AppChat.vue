<template>
  <v-container class="bv-example-row mt-3 text-center" style="width: 1200px; margin-bottom: 225px">
    <h3 class="underline-hotpink" style="margin-top: 100px; margin-bottom: 50px">
      <b-icon icon="journals"></b-icon> 채팅
    </h3>

    <div style="margin-bottom: 330px">
      <v-col class="d-flex justify-end">
        <v-btn variant="outline-primary" @click="dialogMake = true">채팅방 개설</v-btn>
      </v-col>
      <div v-if="roomList.length">
        <v-simple-table>
          <template v-slot:default>
            <colgroup>
              <col width="10%" />
              <col width="20%" />
              <col width="60%" />
              <col width="20%" />
            </colgroup>
            <thead>
              <tr>
                <th class="text-center">번호</th>
                <th class="text-center">이름</th>
                <th class="text-center">설명</th>
                <th class="text-center">관리자</th>
              </tr>
            </thead>
            <tbody>
              <tr
                v-for="(chatRoom, index) in roomList"
                :key="index"
                @click="enterChatRoom(chatRoom)"
              >
                <td>{{ index + 1 }}</td>
                <td>{{ chatRoom.name }}</td>
                <td>{{ chatRoom.description }}</td>
                <td>{{ chatRoom.roomMaker }}</td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </div>
    </div>

    <!-- 채팅방 개설 dialog -->
    <v-dialog v-model="dialogMake" width="500px">
      <v-card style="overflow-x: hidden" height="355px">
        <v-toolbar color="#7E57C2" width="500px" dark style="position: fixed; z-index: 3">
          <b>채팅방 개설</b>
        </v-toolbar>
        <v-simple-table style="margin-top: 70px; margin-left: 20px; margin-right: 20px">
          <template v-slot:default>
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <th class="text-left" colspan="1">채팅방 이름</th>
              <td class="text-left" colspan="11">
                <v-text-field
                  name="chatname"
                  v-model="chatname"
                  :error-messages="chatnameErrorMessages"
                  label="채팅방 이름을 입력하세요."
                  required
                ></v-text-field>
              </td>
            </tr>
            <tr>
              <th class="text-left" colspan="1">채팅방 설명</th>
              <td class="text-left" colspan="11">
                <v-textarea
                  name="chatdescription"
                  v-model="chatdescription"
                  :error-messages="chatdescriptionErrorMessages"
                  label="채팅방 설명을 입력하세요."
                  required
                ></v-textarea>
              </td>
            </tr>
          </template>
        </v-simple-table>
        <v-row class="d-flex justify-end">
          <v-btn
            width="30px"
            style="margin-right: 30px"
            color="deep-purple lighten-2"
            text
            @click="makeChatRoom"
          >
            개설하기
          </v-btn>
        </v-row>
      </v-card>
    </v-dialog>

    <!-- 채팅방 dialog -->
    <v-dialog v-model="dialog" width="500px" height="1000px">
      <v-card style="overflow-x: hidden" id="chatbox" ref="chatbox">
        <v-toolbar color="#7E57C2" width="500px" dark style="position: fixed; z-index: 3">
          <b>{{ this.nowroomname }}</b>
        </v-toolbar>
        <v-list style="height: 630px; margin-top: 60px">
          <div v-for="(item, idx) in recvList" :key="idx">
            <v-list-item class="d-flex justify-end" v-if="item.writer === userInfo.username">
              <span
                >{{ item.message }}&nbsp;
                <b class="underline-hotpink">{{ item.writer }}</b>
                &nbsp;
                <v-img
                  v-if="item.image != null"
                  class="pa-4 primary rounded-circle d-inline-block"
                  :src="require(`@/assets/memberImg/${item.image}`)"
                  width="30"
                  style="margin-bottom: -10px"
                />
              </span>
            </v-list-item>
            <v-list-item class="d-flex justify-start" v-if="item.writer !== userInfo.username">
              <span
                ><v-img
                  v-if="item.image != null"
                  class="pa-4 primary rounded-circle d-inline-block"
                  :src="require(`@/assets/memberImg/${item.image}`)"
                  width="30"
                  style="margin-bottom: -10px"
                />&nbsp; <b class="underline-hotpink">{{ item.writer }}</b
                >&nbsp;&nbsp;{{ item.message }}</span
              >
            </v-list-item>
          </div>
        </v-list>

        <div class="inner-wrap" style="position: fixed">
          <v-text-field
            style="overflow-x: hidden; width: 500px"
            v-model="message"
            placeholder="보낼 메세지를 입력하세요."
            solo
            @keyup="sendMessage"
          ></v-text-field>
        </div>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapState, mapActions } from "vuex";

const chatStore = "chatStore";
const userStore = "userStore";

export default {
  name: "App",
  data() {
    return {
      nowroomid: "",
      nowroomname: "",
      message: "",
      recvList: [],
      chatRoom: [],
      dialog: false,
      dialogMake: false,
      chatname: "",
      chatnameErrorMessages: "",
      chatdescription: "",
      chatdescriptionErrorMessages: "",
    };
  },
  created() {
    this.selectChatRoomList();
  },
  methods: {
    makeChatRoom() {
      let data = {
        name: this.chatname,
        roomMaker: this.userInfo.username,
        description: this.chatdescription,
      };
      this.createChatRoom(data);
      this.dialogMake = false;
    },
    enterChatRoom(chatroom) {
      // 방 입장 시 소켓 연결을 시도합니다.
      console.log(chatroom.roomId);
      this.connect(chatroom.roomId);
      this.nowroomname = chatroom.name;
      this.dialog = true;
    },
    sendMessage(e) {
      if (e.keyCode === 13 && this.userName !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          roomId: this.nowroomid,
          writer: this.userInfo.username,
          message: this.message,
          image: this.myImg,
        };
        console.log("메세지", msg);
        this.stompClient.send("/pub/chat/message", JSON.stringify(msg), {});
      }
    },
    connect(roomid) {
      const serverURL = "http://localhost:8888/board/chat";
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.

          this.selectChatList(roomid);
          this.nowroomid = roomid;
          console.log("nowroomid", this.nowroomid);
          this.stompClient.subscribe("/sub/chat/room/" + this.nowroomid, (res) => {
            console.log("구독으로 받은 메시지 입니다.", res.body);

            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            this.recvList.push(JSON.parse(res.body));
          });
          let data = {
            roomId: this.nowroomid,
            writer: this.userInfo.username,
          };
          console.log(this.userInfo);
          this.stompClient.send("/pub/chat/enter", JSON.stringify(data), {});
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
    ...mapActions(chatStore, ["createChatRoom", "selectChatRoomList", "selectChatList"]),
  },
  computed: {
    ...mapState(chatStore, ["roomList", "chatList"]),
    ...mapState(userStore, ["userInfo", "myImg"]),
  },
  watch: {
    dialog() {
      if (!this.dialog) {
        console.log("다이아로그 끔");
        this.stompClient.disconnect();
      }
    },
    chatList() {
      this.recvList = [];
      this.recvList = this.chatList;
    },
    recvList() {
      setTimeout(function () {
        console.log(this.recvList);
        let chatbox = document.getElementById("chatbox");
        chatbox.scrollTo({ top: chatbox.scrollHeight, behavior: "smooth" });
      }, 10);
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
