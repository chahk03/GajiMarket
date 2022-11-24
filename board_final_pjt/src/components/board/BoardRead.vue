<template>
  <div>
    <!-- 상세 글 -->
    <div v-if="board">
      <v-row align="center">
        <v-col class="d-flex" cols="12" sm="12" style="font-size: 20px">
          <span style="color: #b39ddb">{{ board.category }} &nbsp;</span>
          <span
            ><b>{{ board.title }} &nbsp;</b></span
          >
          <span v-if="board.soldout === '0'">(판매 중)</span>
          <span v-if="board.soldout === '1'">(예약 완료)</span>
          <span v-if="board.soldout === '2'">(판매 완료)</span>
        </v-col>
      </v-row>
      <v-row justify="space-between">
        <v-col class="d-flex flex-row">
          <span>
            <v-img
              class="pa-4 primary rounded-circle d-inline-block"
              :src="require(`@/assets/memberImg/${writerImg.savedPath}`)"
              width="30"
            />
          </span>
          &nbsp;&nbsp;
          <span style="font-size: 15px; margin-top: 5px"
            >{{ board.writer }} &nbsp; | &nbsp; {{ board.writeDate }}</span
          >
        </v-col>
        <v-col class="d-flex flex-row-reverse">
          <span style="font-size: 12px">
            <v-icon>{{ icons.mdiAccount }}</v-icon>
            {{ board.readCount }}
            <v-btn
              v-if="isrecommend"
              icon
              color="blue lighten-2"
              @click="updateRecommend"
            >
              <v-icon>mdi-thumb-up</v-icon>
            </v-btn>
            <v-btn v-if="!isrecommend" icon @click="updateRecommend">
              <v-icon>mdi-thumb-up</v-icon>
            </v-btn>
            {{ board.recommend }}</span
          >
        </v-col>
      </v-row>
      <hr />
      <v-row align="center" style="margin-top: 10px">
        <v-col class="d-flex" cols="12" sm="12">
          <div class="view" v-html="board.content"></div>
        </v-col>
      </v-row>
      <br />
      <v-img
        v-if="image"
        :src="require(`@/assets/boardImg/${image}`)"
        width="600"
        style="margin: 0 auto"
      />
    </div>
    <br />

    <!-- 수정 삭제 버튼 -->
    <div style="padding-top: 15px" v-if="board.writer === userInfo.username">
      <v-row align="center" justify="end">
        <v-col class="d-flex justify-end">
          <v-btn variant="outline-primary" v-on:click="updateBoard">수정</v-btn>
          &nbsp;&nbsp;&nbsp;&nbsp;
          <v-btn variant="outline-primary" v-on:click="deleteBoard">삭제</v-btn>
        </v-col>
      </v-row>
      <!-- <v-btn variant="outline-primary" v-on:click="updateBoard">수정</v-btn>
      &nbsp;&nbsp;
      <v-btn variant="outline-primary" v-on:click="deleteBoard">삭제</v-btn> -->
    </div>
    <hr />

    <!-- 댓글 목록 -->
    <div v-if="cList.length">
      <v-simple-table>
        <template v-slot:default>
          <colgroup>
            <col width="10%" />
            <col width="80%" />
            <col width="10%" />
          </colgroup>
          <thead>
            <tr>
              <th>댓글</th>
              <th></th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(comment, index) in cList" :key="index">
              <td class="d-flex flex-row">
                <span style="margin-top: 8px">
                  <v-img
                    class="pa-4 primary rounded-circle d-inline-block"
                    :src="
                      require(`@/assets/memberImg/${cListImg[index].savedPath}`)
                    "
                    width="30" /></span
                >&nbsp;&nbsp;
                <span style="margin-top: 15px">{{ comment.cwriter }}</span>
              </td>
              <td class="text-left" v-if="flag || index !== cIndex">
                {{ comment.ccontent }}
              </td>
              <td v-if="!flag && index === cIndex">
                <v-text-field
                  dense
                  v-model="comment.ccontent"
                  required
                  style="font-size: 14px; margin-top: 10px"
                ></v-text-field>
              </td>
              <td>
                <v-btn
                  icon
                  x-small
                  @click="
                    flag = !flag;
                    cIndex = index;
                  "
                  v-if="flag && comment.cwriter === userInfo.username"
                  ><v-icon>{{ icons.mdiPencil }}</v-icon></v-btn
                >
                <v-btn
                  icon
                  x-small
                  @click="updateComment(comment)"
                  v-if="!flag && comment.cwriter === userInfo.username"
                  ><v-icon>{{ icons.mdiPencil }}</v-icon></v-btn
                >
                &nbsp;
                <v-btn
                  icon
                  x-small
                  @click="deleteComment(comment.cno)"
                  v-if="comment.cwriter === userInfo.username"
                  ><v-icon>
                    {{ icons.mdiDelete }}
                  </v-icon></v-btn
                >
              </td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
      <hr />
    </div>
    <br />

    <!-- 댓글 작성 -->
    <div class="comment">
      <v-form ref="form" lazy-validation v-if="show">
        <!-- <v-text-field
          v-model="comment.cwriter"
          label="작성자"
          required
        ></v-text-field> -->
        <v-text-field v-model="comment.ccontent" label="내용" required
          ><v-icon slot="append" color="red" @click="writecomment">
            mdi-plus
          </v-icon></v-text-field
        >
      </v-form>
    </div>
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";
import { mdiAccount, mdiPencil, mdiDelete } from "@mdi/js";

const boardStore = "boardStore";
const userStore = "userStore";

export default {
  data: function () {
    return {
      // tmp: "test.png",
      comment: {
        bno: this.$route.query.bno,
        cwriter: "",
        ccontent: "",
      },
      bno: this.$route.query.bno,
      show: true,
      icons: {
        mdiPencil,
        mdiDelete,
        mdiAccount,
      },
      flag: true,
      cIndex: "",
    };
  },
  // props: ["money"],
  created() {
    this.findboard(this.$route.query.bno);
    this.comment.cwriter = this.userInfo.username;
  },
  methods: {
    findboard(bno) {
      let data = {
        bno: bno,
        userid: this.userInfo.userid,
        username: this.userInfo.username,
      };
      this.boardGetOne(data);
    },
    enterToBr(str) {
      // 문자열에 enter값을 <br />로 변경.(html상에서 줄바꿈 처리)
      return str.replace(/(?:\r\n|\r|\n)/g, "<br />");
    },
    deleteBoard() {
      console.log(this.$route.query.bno);
      this.boardDelete(this.$route.query.bno);
      this.$router.push({ name: "boardlist" });
    },
    writecomment() {
      this.comment.cwriter = this.userInfo.username;
      this.commentWrite(this.comment);
      this.comment.cwriter = "";
      this.comment.ccontent = "";
    },
    updateComment(comment) {
      this.commentUpdate(comment);
      this.flag = true;
    },
    deleteComment(cno) {
      let data = {
        bno: this.$route.query.bno,
        cno: cno,
      };
      console.log(data);
      this.commentDelete(data);
    },
    updateBoard() {
      this.$router.push({ name: "boardupdate" });
    },
    updateRecommend() {
      console.log(this.userInfo.userid);
      console.log(this.$route.query.bno);
      let data = {
        userid: this.userInfo.userid,
        bno: this.$route.query.bno,
      };
      this.boardRecommendUpdate(data);
    },
    ...mapActions(boardStore, [
      "boardGetOne",
      "boardDelete",
      "boardRecommendUpdate",
      "commentWrite",
      "commentUpdate",
      "commentDelete",
    ]),
  },
  filters: {
    numberWithCommas(x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    },
  },
  computed: {
    ...mapState(boardStore, [
      "board",
      "writerImg",
      "isrecommend",
      "cList",
      "cListImg",
      "image",
    ]),
    ...mapState(userStore, ["userInfo"]),
  },
};
</script>

<style>
.comment {
  border: 1px solid rgb(190, 190, 190);
  border-radius: 10px;
  padding-left: 25px;
  padding-right: 25px;
}
</style>
