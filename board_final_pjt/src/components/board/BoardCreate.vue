<template>
  <div style="width: 800px; margin: 0 auto">
    <v-form id="sendForm" ref="sendFrom" enctype="multipart/form-data">
      <v-simple-table v-if="show">
        <template v-slot:default>
          <colgroup>
            <col width="20%" />
            <col width="80%" />
          </colgroup>
          <tr>
            <th class="text-left" colspan="1">분류</th>
            <td class="text-left" colspan="1">
              <v-select
                v-if="board.writer !== '관리자'"
                style="max-width: 100px; padding-top: 25px"
                name="category"
                v-model="board.category"
                :items="['전체', '구매', '판매', '소통']"
                label="전체"
                dense
                solo
              >
              </v-select>
              <v-select
                v-if="board.writer === '관리자'"
                style="max-width: 100px; padding-top: 25px"
                name="category"
                v-model="board.category"
                :items="['공지']"
                label="공지"
                dense
                solo
              >
              </v-select>
            </td>
            <!-- <th colspan="1">작성자</th>
          <td colspan="9">
            <v-text-field
              v-model="board.writer"
              label="작성자를 입력하세요."
              required
            ></v-text-field>
          </td> -->
          </tr>
          <tr>
            <th class="text-left" colspan="1">제목</th>
            <td class="text-left" colspan="11">
              <v-text-field
                name="title"
                v-model="board.title"
                label="제목을 입력하세요."
                required
              ></v-text-field>
            </td>
          </tr>
          <tr>
            <br /><br />
          </tr>
          <tr>
            <th class="text-left" colspan="1" style="padding-bottom: 40px">
              내용
            </th>
            <td class="text-left" colspan="11">
              <v-textarea
                outlined
                name="content"
                v-model="board.content"
              ></v-textarea>
            </td>
          </tr>

          <tr>
            <th class="text-left" colspan="1">이미지 첨부</th>
            <td class="text-left" colspan="11" style="overflow: hidden">
              <v-img
                v-if="file && preview"
                class="pa-12 d-inline-block"
                :src="preview"
                width="30"
              />
              <v-file-input type="file" name="uploadFile" v-model="file" />
            </td>
          </tr>
          <tr>
            <th class="text-left" colspan="1">판매 여부</th>
            <td class="text-left" colspan="11">
              <v-radio-group v-model="board.soldout" row>
                <v-radio label="판매 중" value="0"></v-radio>
                <v-radio label="예약 완료" value="1"></v-radio>
                <v-radio label="판매 완료" value="2"></v-radio>
              </v-radio-group>
            </td>
          </tr>
        </template>
      </v-simple-table>
    </v-form>
    <br />
    <v-row align="center" justify="end">
      <v-col class="d-flex flex-row-reverse" cols="12" sm="1">
        <v-btn variant="outline-primary" @click="onSubmit">작성</v-btn>
      </v-col>
    </v-row>
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";

const userStore = "userStore";
const boardStore = "boardStore";

export default {
  data() {
    return {
      board: {
        title: "",
        writer: "",
        content: "",
        category: "전체",
        soldout: "0",
      },
      file: [],
      show: true,
      preview: "",
    };
  },
  created() {
    console.log(this.userInfo.username);
    this.board.writer = this.userInfo.username;
  },
  methods: {
    async onSubmit() {
      var fd = new FormData();
      fd.append("uploadFile", this.file);
      fd.append("title", this.board.title);
      fd.append("writer", this.board.writer);
      fd.append("content", this.board.content);
      fd.append("category", this.board.category);
      fd.append("soldout", this.board.soldout);
      // let request = {
      //   title: this.board.title,
      //   writer: this.board.writer,
      //   content: this.board.content,
      //   category: this.board.category,
      // };
      // console.log(request);
      // fd.append("board", request);
      console.log(fd);
      await this.boardCreate(fd);
      this.$router.push({ name: "boardlist" });
    },

    onReset() {},
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
    ...mapActions(boardStore, ["boardCreate"]),
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
  },
  watch: {
    file() {
      this.previewFile(this.file);
    },
  },
};
</script>

<style></style>
