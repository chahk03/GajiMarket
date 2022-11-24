<template>
  <div>
    <div id="aptDiv">
      <div class="banner1-component">
        <v-container>
          <!-- -----------------------------------------------
            Start Banner1
        ----------------------------------------------- -->
          <v-row justify="center">
            <v-col cols="12" md="7" lg="6" class="d-flex align-center">
              <div class="text-center text-md-left">
                <h2
                  class="banner1-title font-weight-bold text-uppercase"
                  style="font-size: 50px"
                >
                  어떤 집을 찾고 계세요?
                </h2>
                <p class="op-8 font-weight-regular" style="font-size: 18px">
                  집 알아보기 힘드시죠? 집가지마켓에는 최근 10년 동안의 아파트
                  거래 내역이 있습니다. 아파트 거래 내역을 분석하여 성공적인
                  아파트 매매 하세요. 주변 상권, 로드뷰 등을 참고할 수 있습니다.
                </p>
                <div class="mt-16 pt-2">
                  <v-btn
                    large
                    rounded
                    :to="{ name: 'aptsearch' }"
                    class="btn-custom-md white--text"
                    depressed
                    color="deep-purple lighten-1"
                    elevation="0"
                  >
                    나만의 집 찾으러 가기
                  </v-btn>
                </div>
              </div>
            </v-col>
            <v-col cols="12" md="5" lg="5">
              <v-img :src="require('@/assets/apt-purple.gif')" alt="banner" />
            </v-col>
          </v-row>

          <!-- -----------------------------------------------
            End Banner1
        ----------------------------------------------- -->
        </v-container>
      </div>
    </div>
    <div id="boardDiv" class="reveal">
      <div class="banner1-component">
        <v-container>
          <!-- -----------------------------------------------
            Start Banner2
        ----------------------------------------------- -->
          <v-row justify="center">
            <v-col cols="12" md="7" lg="6" class="d-flex align-center">
              <div class="text-center text-md-left">
                <h2
                  class="banner1-title font-weight-bold text-uppercase"
                  style="font-size: 50px"
                >
                  새 것같은 중고제품 찾고 계세요?
                </h2>
                <p class="op-8 font-weight-regular" style="font-size: 18px">
                  물가가 오른 요즘 새 제품을 구매하기 부담 되시나요? 바가지마켓
                  커뮤니티를 통해 이웃과 합리적인 거래 하세요. 공지글을
                  참고하시어 기본적인 매너를 지켜주세요.
                </p>
                <div class="mt-16 pt-2">
                  <v-btn
                    large
                    rounded
                    :to="{ name: 'boardlist' }"
                    class="btn-custom-md white--text"
                    depressed
                    color="deep-purple lighten-1"
                    elevation="0"
                  >
                    물건 건지러 가기
                  </v-btn>
                </div>
              </div>
            </v-col>
            <v-col cols="12" md="5" lg="5">
              <v-img :src="require('@/assets/shop.png')" alt="banner" />
            </v-col>
          </v-row>

          <!-- -----------------------------------------------
            End Banner2
        ----------------------------------------------- -->
        </v-container>
      </div>
    </div>
    <div id="photoDiv" class="reveal">
      <div class="banner1-component">
        <v-container>
          <!-- -----------------------------------------------
            Start Banner3
          ----------------------------------------------- -->
          <v-row justify="center">
            <v-col cols="12" md="7" lg="6" class="d-flex align-center">
              <div class="text-center text-md-left">
                <h2
                  class="banner1-title font-weight-bold text-uppercase"
                  style="font-size: 50px"
                >
                  닮은꼴을 찾아보세염.
                </h2>
                <p class="op-8 font-weight-regular" style="font-size: 18px">
                  프로필 사진을 등록하여 추정 나이, 성별, 닮은꼴 연예인을
                  확인해보세요. 다른 회원에게 얼굴이 공개되니 매너있는
                  가지마켓을 함께 만들어보아요.
                </p>
                <div class="mt-16 pt-2">
                  <v-btn
                    large
                    rounded
                    :to="{ name: 'usersignup' }"
                    class="btn-custom-md white--text"
                    depressed
                    color="deep-purple lighten-1"
                    elevation="0"
                    v-if="userInfo == null"
                  >
                    분석하러 가기
                  </v-btn>
                  <v-btn
                    large
                    rounded
                    :to="{ name: 'usermypage' }"
                    class="btn-custom-md white--text"
                    depressed
                    color="deep-purple lighten-1"
                    elevation="0"
                    v-if="userInfo != null"
                  >
                    분석하러 가기
                  </v-btn>
                </div>
              </div>
            </v-col>
            <v-col cols="12" md="5" lg="5">
              <v-img
                :src="require('@/assets/sangmin4.jpg')"
                alt="banner"
                max-width="500px"
                style="margin-left: 140px"
              />
            </v-col>
          </v-row>

          <!-- -----------------------------------------------
            End Banner3
          ----------------------------------------------- -->
        </v-container>
      </div>
    </div>

    <!-- 카드 3장 -->
    <div id="myDiv" class="reveal">
      <div class="banner1-component">
        <v-container>
          <!-- -----------------------------------------------
            Start Banner4
          ----------------------------------------------- -->
          <v-row justify="center">
            <v-col cols="12" md="9" lg="9" class="d-flex justify-center">
              <!-- 프로필 -->
              <v-card
                class="mx-auto my-12"
                min-width="475"
                min-height="575"
                style="position: relative"
              >
                <v-card-title style="margin-bottom: -15px"
                  >Profile</v-card-title
                >
                <v-divider class="mx-4"></v-divider>
                <div style="text-align: center; padding-top: 6px">
                  <v-img
                    v-if="userInfo"
                    max-width="200"
                    max-height="200"
                    class="pa-12 primary rounded-circle d-inline-block"
                    :src="require(`@/assets/memberImg/${myImg}`)"
                  ></v-img>
                  <v-img
                    v-if="userInfo == null"
                    max-width="200"
                    max-height="200"
                    class="pa-12 primary rounded-circle d-inline-block"
                    :src="require(`@/assets/mimoticon.gif`)"
                  ></v-img>
                </div>
                <v-divider class="mx-4"></v-divider>
                <v-card-text v-if="userInfo" style="margin-top: -18px">
                  <div class="my-4">{{ myInfo.username }}님 안녕하세요.</div>
                  <div class="my-4">{{ myInfo.age }}살로 보이시네요.</div>
                  <div class="my-4" v-if="myInfo.gender == 'female'">
                    여성 맞으세요?
                  </div>
                  <div class="my-4" v-if="myInfo.gender == 'male'">
                    남성 맞으세요?
                  </div>
                  <div class="my-4">{{ myInfo.celebrity }} 닮았어요 !</div>
                </v-card-text>
                <v-card-text v-if="userInfo == null">
                  프로필을 등록해보세요!
                </v-card-text>

                <!-- <v-divider class="mx-4"></v-divider> -->

                <v-card-actions style="position: absolute; bottom: 0px">
                  <v-btn
                    v-if="userInfo"
                    color="deep-purple lighten-2"
                    text
                    :to="{ name: 'usermypage' }"
                  >
                    MYPAGE
                  </v-btn>
                  <v-btn
                    v-if="userInfo == null"
                    color="deep-purple lighten-2"
                    text
                    :to="{ name: 'usersignup' }"
                  >
                    SIGNUP
                  </v-btn>
                </v-card-actions>
              </v-card>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

              <!-- 게시판 -->
              <v-card class="mx-auto my-12" min-width="475" min-height="575">
                <v-card-title style="margin-bottom: -15px">Notice</v-card-title>
                <v-divider class="mx-4"></v-divider>
                <v-card-text>
                  <v-simple-table>
                    <thead>
                      <tr>
                        <th>No.</th>
                        <th>제목</th>
                        <th>작성일</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr
                        v-for="(admin, index) in adminList"
                        :key="index"
                        @click="titleClick(admin.bno)"
                      >
                        <td>{{ index + 1 }}</td>
                        <td>{{ admin.title }}</td>
                        <td>
                          {{
                            admin.writeDate.split(" ")[0].replaceAll("-", ".")
                          }}
                        </td>
                      </tr>
                    </tbody>
                  </v-simple-table>
                </v-card-text>
                <!-- <v-divider class="mx-4"></v-divider> -->
              </v-card>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

              <!-- 최근거래 -->
              <v-card class="mx-auto my-12" min-width="475" min-height="575">
                <v-card-title style="margin-bottom: -15px">House</v-card-title>
                <v-divider class="mx-4"></v-divider>
                <v-card-text>
                  <v-simple-table>
                    <thead>
                      <tr>
                        <th>아파트</th>
                        <th>동</th>
                        <th>날짜</th>
                        <th>거래가</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(apt, index) in aptRecentList" :key="index">
                        <td>{{ apt.apartmentName }}</td>
                        <td>{{ apt.dongName }}</td>
                        <td>{{ apt.dealYear }}.{{ apt.dealMonth }}</td>
                        <td>{{ apt.dealAmount }}</td>
                      </tr>
                    </tbody>
                  </v-simple-table>
                </v-card-text>
                <!-- <v-divider class="mx-4"></v-divider> -->
                <v-card-actions style="position: absolute; bottom: 0px">
                  <v-btn
                    color="deep-purple lighten-2"
                    text
                    :to="{ name: 'aptsearch' }"
                  >
                    MORE
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>

          <!-- -----------------------------------------------
        End Banner4
    ----------------------------------------------- -->
        </v-container>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

const userStore = "userStore";
const boardStore = "boardStore";
const aptStore = "aptStore";

export default {
  data() {
    return {};
  },
  created() {
    this.boardGetAdminList();
    this.aptGetRecentList();
  },
  methods: {
    titleClick(bno) {
      this.$router.push({ name: "boardread", query: { bno } });
    },
    ...mapActions(boardStore, ["boardGetAdminList"]),
    ...mapActions(aptStore, ["aptGetRecentList"]),
  },
  computed: {
    ...mapState(userStore, [
      "userInfo",
      "myImg",
      "myInfo",
      "celebrity",
      "age",
      "gender",
    ]),
    ...mapState(boardStore, ["adminList"]),
    ...mapState(aptStore, ["aptRecentList"]),
  },
};

function reveal() {
  var reveals = document.querySelectorAll(".reveal");

  for (var i = 0; i < reveals.length; i++) {
    var windowHeight = window.innerHeight;
    var elementTop = reveals[i].getBoundingClientRect().top;
    var elementVisible = 150;

    if (elementTop < windowHeight - elementVisible) {
      reveals[i].classList.add("active");
    } else {
      reveals[i].classList.remove("active");
    }
  }
}

window.addEventListener("scroll", reveal);
</script>

<style>
#aptDiv {
  padding-top: 50px;
}
#boardDiv {
  background-color: #ede7f6;
}
#myDiv {
  background-color: #ede7f6;
}

.banner-title {
  font-size: 29px;
  line-height: 40px;
}

/* Banner1 */
.banner1-component {
  padding: 60px 0;
}
.banner1-title {
  font-size: 35px;
  line-height: 50px;
  margin: 20px 0 15px;
}

.reveal {
  position: relative;
  transform: translateY(200px);
  opacity: 0;
  transition: 1s all ease;
}

.reveal.active {
  transform: translateY(0);
  opacity: 1;
}

tbody tr :hover {
  cursor: pointer;
}
</style>
