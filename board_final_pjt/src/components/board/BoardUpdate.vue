<template>
  <div style="width: 800px; margin: 0 auto">
    <v-simple-table v-if="show && board">
      <template v-slot:default>
        <tr>
          <th class="text-left" colspan="1">분류</th>
          <td class="text-left" colspan="1">
            <v-select
              style="max-width: 100px; padding-top: 25px"
              v-model="board.category"
              :items="['전체', '구매', '판매', '소통']"
              label="전체"
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
              name="input-7-4"
              v-model="board.content"
            ></v-textarea>
          </td>
        </tr>
        <tr>
          <th class="text-left" colspan="1">이미지 첨부</th>
          <td class="text-left" colspan="11" style="overflow: hidden">
            <v-img
              v-if="file != '' && preview != ''"
              class="pa-12 d-inline-block"
              :src="preview"
              width="30"
            />
            <v-img
              v-if="file == '' && image != ''"
              :src="require(`@/assets/boardImg/${image}`)"
              width="100"
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

    <v-row align="center" justify="end">
      <v-col class="d-flex justify-end" cols="12" sm="2">
        <v-btn variant="outline-primary" @click="onSubmit(board.bno)"
          >수정</v-btn
        >
        &nbsp;&nbsp;&nbsp;&nbsp;
        <v-btn variant="outline-primary" @click="onReset">초기화</v-btn>
      </v-col>
    </v-row>
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";

const boardStore = "boardStore";

export default {
  data() {
    return {
      show: true,
      preview: "",
      file: [],
    };
  },
  created() {
    this.bno = this.board.bno;
    this.onReset();
  },
  methods: {
    async onSubmit(bno) {
      this.boardUpdate(this.board);
      this.modifyImage();
      this.$router.push({ name: "boardread", query: { bno } });
    },
    onReset() {},
    modifyImage() {
      var fd = new FormData();
      console.log(this.board.bno);
      console.log(this.file);
      fd.append("bno", this.board.bno);
      fd.append("uploadFile", this.file);
      this.boardImgUpdate(fd);
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
    ...mapActions(boardStore, ["boardUpdate", "boardImgUpdate"]),
  },
  computed: {
    ...mapState(boardStore, ["board", "image"]),
  },
  watch: {
    file() {
      this.previewFile(this.file);
    },
  },
};
</script>

<style></style>
