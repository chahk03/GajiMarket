<template>
  <div>
    <div>
      <!-- 조건 -->
      <v-row align="center" justify="space-between">
        <v-col class="d-flex justify-start">
          <v-select
            style="max-width: 120px"
            v-model="sort"
            :items="[
              { name: '최신순', value: 'BNO' },
              { name: '조회수', value: 'READ_COUNT' },
              { name: '추천수', value: 'RECOMMEND' },
            ]"
            item-text="name"
            item-value="value"
            label="최신순"
            dense
            solo
          >
            <!-- <option value="bno">최신순</option>
            <option value="readCount">조회수</option> -->
          </v-select>
          &nbsp;&nbsp;&nbsp;
          <v-select
            style="max-width: 120px"
            v-model="category"
            :items="[
              { name: '전체', value: '' },
              { name: '구매', value: '구매' },
              { name: '판매', value: '판매' },
              { name: '소통', value: '소통' },
            ]"
            item-text="name"
            item-value="value"
            label="전체"
            dense
            solo
          >
            <!-- <option value="전체">전체</option> -->
            <!-- <option :value="other">조회수</option> -->
          </v-select>
        </v-col>

        <v-col class="d-flex justify-end">
          <v-select
            style="max-width: 120px"
            v-model="key"
            :items="[
              { name: '제목', value: 'title' },
              { name: '작성자', value: 'writer' },
            ]"
            item-text="name"
            item-value="value"
            label="제목"
            dense
            solo
          >
          </v-select>
          &nbsp;&nbsp;&nbsp;
          <v-text-field
            type="text"
            id="word"
            name="word"
            v-model="word"
            ref="word"
            label="검색어"
            style="max-width: 120px; margin-top: -10px"
          ></v-text-field>
          &nbsp;&nbsp;&nbsp;
          <v-btn variant="outline-primary" @click="selectList">검색</v-btn>
          &nbsp;&nbsp;&nbsp;
          <v-btn tile color="success" to="/board/create">
            <v-icon left> mdi-pencil </v-icon>
            작성
          </v-btn>
        </v-col>
      </v-row>
    </div>

    <!-- 게시글 목록 -->
    <div v-if="boardList.length">
      <v-simple-table>
        <template v-slot:default>
          <colgroup>
            <col width="10%" />
            <col width="10%" />
            <col width="30%" />
            <col width="10%" />
            <col width="20%" />
            <col width="10%" />
            <col width="10%" />
          </colgroup>
          <thead>
            <tr>
              <th class="text-center">번호</th>
              <th class="text-center">분류</th>
              <th class="text-center">제목</th>
              <th class="text-center">작성자</th>
              <th class="text-center">작성일시</th>
              <th class="text-center">조회수</th>
              <th class="text-center">추천수</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(board, index) in boardList" :key="index" @click="titleClick(board.bno)">
              <td>{{ board.bno }}</td>
              <td>{{ board.category }}</td>
              <td v-if="board.soldout === '0'" style="color: #3949ab">
                <b>{{ board.title }}</b>
              </td>
              <td v-if="board.soldout === '1'" style="color: #0277bd">
                {{ board.title }}
              </td>
              <td
                v-if="board.soldout === '2'"
                style="text-decoration: line-through; color: #26a69a"
              >
                {{ board.title }}
              </td>
              <td>{{ board.writer }}</td>
              <td>{{ board.writeDate }}</td>
              <td>{{ board.readCount }}</td>
              <td>{{ board.recommend }}</td>
            </tr>
          </tbody>
        </template>
      </v-simple-table>
      <br />

      <!-- 페이지 처리 -->
      <div class="text-center">
        <v-pagination
          v-model="page"
          :length="totalPage"
          :total-visible="8"
          prev-icon="mdi-menu-left"
          next-icon="mdi-menu-right"
          @input="movePage(page)"
        ></v-pagination>
      </div>
    </div>
    <div v-else class="text-center">게시글이 없습니다.</div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const boardStore = "boardStore";

export default {
  mounted() {
    this.movePage(1);
  },
  data() {
    return {
      sort: "BNO",
      category: "",
      word: "",
      key: "title",
      page: 1,
    };
  },
  methods: {
    movePage(page) {
      this.boardGetSearch({
        pg: page,
        spp: 10,
        category: this.category,
        sort: this.sort,
        key: this.key,
        word: this.word,
      });
    },
    titleClick(bno) {
      this.$router.push({ name: "boardread", query: { bno } });
    },
    selectList() {
      this.boardGetSearch({
        pg: 1,
        spp: 10,
        category: this.category,
        sort: this.sort,
        key: this.key,
        word: this.word,
      });
    },
    ...mapActions(boardStore, ["boardGetList", "boardGetSearch"]),
  },
  computed: {
    ...mapState(boardStore, ["boardList", "startPage", "totalPage", "endPage", "currPage"]),
  },
  watch: {
    sort() {
      this.selectList();
    },
    category() {
      this.selectList();
    },
  },
};
</script>

<style>
tbody tr :hover {
  cursor: pointer;
}
v-select {
  max-width: 10px;
}
</style>
