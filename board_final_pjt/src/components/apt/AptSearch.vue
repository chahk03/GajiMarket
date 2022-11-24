<template>
  <div>
    <h3
      class="underline-hotpink"
      style="margin-top: 100px; margin-bottom: 50px"
    >
      <b-icon icon="journals"></b-icon> 집가지마켓
    </h3>
    <v-container fluid>
      <v-row align="center">
        <v-col class="d-flex">
          <v-select
            :items="sidoList"
            label="시도 선택"
            v-model="selectSido"
            dense
            solo
          ></v-select>
        </v-col>
        <v-col class="d-flex">
          <v-select
            :items="gugunList"
            label="구군 선택"
            v-model="selectGugun"
            dense
            solo
          ></v-select>
        </v-col>
        <v-col class="d-flex">
          <v-select
            :items="dongList"
            label="동 선택"
            v-model="selectDong"
            dense
            solo
          ></v-select>
        </v-col>
        <v-col class="d-flex">
          <v-select
            :items="yearList"
            label="년 선택"
            v-model="selectYear"
            dense
            solo
          ></v-select>
        </v-col>
        <v-col class="d-flex" cols="3" sm="3">
          <v-select
            :items="monthList"
            label="월 선택"
            v-model="selectMonth"
            dense
            solo
          ></v-select>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <v-btn variant="outline-primary" @click="selectList"> 검색</v-btn>
        </v-col>
      </v-row>
    </v-container>
    <div>
      <v-row>
        <v-col>
          <div
            v-if="!aptSearchList.length"
            style="overflow-y: hidden; height: 500px; margin-top: 10px"
          ></div>
          <div
            v-if="aptSearchList.length"
            style="overflow-y: scroll; height: 500px; margin-top: 10px"
          >
            <v-simple-table>
              <template v-slot:default>
                <thead>
                  <tr>
                    <th class="text-center">아파트이름</th>
                    <th class="text-center">동이름</th>
                    <th class="text-center">거래가격</th>
                    <th class="text-center">거래년월</th>
                    <th class="text-center">층</th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="(apt, index) in aptSearchList"
                    :key="index"
                    @click="showDialog(index)"
                  >
                    <td>{{ apt.apartmentName }}</td>
                    <td>{{ apt.dongName }}</td>
                    <td>{{ apt.dealAmount }}</td>
                    <td>{{ apt.dealYear }}/{{ apt.dealMonth }}</td>
                    <td>{{ apt.floor }}</td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </div>
        </v-col>
        <v-col class="d-flex justify-end">
          <div
            id="map"
            style="width: 575px; height: 500px; margin-top: 10px"
          ></div>
        </v-col>
      </v-row>
    </div>
    <!--ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 상세보기 dialog ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ -->
    <div class="text-center">
      <v-dialog v-model="dialog" width="1000px" height="1000px">
        <v-card style="overflow-x: hidden">
          <v-card-title class="text-h5">
            <b
              style="
                padding-top: 20px;
                padding-left: 10px;
                font-family: dohyeon;
              "
              >{{ currentDialogItem.apartmentName }}</b
            >&nbsp;&nbsp;
            <v-btn
              v-if="interestMark"
              icon
              small
              @click="registInterest"
              style="padding-top: 15px"
            >
              <v-img
                icon
                max-width="30px"
                src="@/assets/icons/star-full.png"
              ></v-img>
            </v-btn>
            <v-btn
              v-if="!interestMark"
              icon
              small
              @click="registInterest"
              style="padding-top: 15px"
            >
              <v-img
                icon
                max-width="30px"
                src="@/assets/icons/star-empty.png"
              ></v-img>
            </v-btn>
          </v-card-title>
          <v-row>
            <v-col style="padding-top: 70px; padding-left: 50px">
              <v-simple-table id="aptDetail">
                <template v-slot:default>
                  <tr>
                    <th class="text-center">아파트이름</th>
                    <td class="text-center">
                      {{ currentDialogItem.apartmentName }}
                    </td>
                  </tr>
                  <tr>
                    <th class="text-center"><hr /></th>
                    <td><hr /></td>
                  </tr>
                  <tr>
                    <th class="text-center">동이름</th>
                    <td class="text-center">
                      {{ currentDialogItem.dongName }}
                    </td>
                  </tr>
                  <tr>
                    <th class="text-center"><hr /></th>
                    <td><hr /></td>
                  </tr>
                  <tr>
                    <th class="text-center">거래가격</th>
                    <td class="text-center">
                      {{ currentDialogItem.dealAmount }}만원
                    </td>
                  </tr>
                  <tr>
                    <th class="text-center"><hr /></th>
                    <td><hr /></td>
                  </tr>
                  <tr>
                    <th class="text-center">거래년월</th>
                    <td class="text-center">
                      {{ currentDialogItem.dealYear }}/{{
                        currentDialogItem.dealMonth
                      }}
                    </td>
                  </tr>
                  <tr>
                    <th class="text-center"><hr /></th>
                    <td><hr /></td>
                  </tr>
                  <tr>
                    <th class="text-center">층</th>
                    <td class="text-center">{{ currentDialogItem.floor }}</td>
                  </tr>
                </template>
              </v-simple-table>
              <div style="padding-top: 225px">
                <v-btn color="primary" icon small @click="dialog = false">
                  <v-img
                    icon
                    max-width="30px"
                    src="@/assets/icons/exit.png"
                  ></v-img>
                </v-btn>
              </div>
            </v-col>
            <v-col>
              <v-btn color="deep-purple" x-small dark @click="showLoadmap">
                로드뷰 </v-btn
              >&nbsp;&nbsp;
              <v-btn color="deep-purple" x-small dark @click="showSangmap">
                상권
              </v-btn>
              <div
                v-show="mapswitch"
                id="roadview"
                style="
                  width: 500px;
                  height: 500px;
                  margin-top: 10px;
                  margin-right: 40px;
                  margin-bottom: 20px;
                "
              ></div>

              <div class="map_wrap" v-show="!mapswitch">
                <div
                  id="sangview"
                  style="
                    width: 500px;
                    height: 500px;
                    margin-top: 10px;
                    margin-right: 40px;
                    margin-bottom: 20px;
                  "
                ></div>
                <ul id="category">
                  <li id="BK9" @click="id = 'BK9'" data-order="0">
                    <span class="category_bg bank"></span>
                    은행
                  </li>
                  <li id="MT1" @click="id = 'MT1'" data-order="1">
                    <span class="category_bg mart"></span>
                    마트
                  </li>
                  <li id="PM9" @click="id = 'PM9'" data-order="2">
                    <span class="category_bg pharmacy"></span>
                    약국
                  </li>
                  <li id="OL7" @click="id = 'OL7'" data-order="3">
                    <span class="category_bg oil"></span>
                    주유소
                  </li>
                  <li id="CE7" @click="id = 'CE7'" data-order="4">
                    <span class="category_bg cafe"></span>
                    카페
                  </li>
                  <li id="CS2" @click="id = 'CS2'" data-order="5">
                    <span class="category_bg store"></span>
                    편의점
                  </li>
                </ul>
              </div>
            </v-col>
          </v-row>

          <v-divider></v-divider>
        </v-card>
      </v-dialog>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const aptStore = "aptStore";
const userStore = "userStore";

export default {
  created() {
    this.getSidoNames();
  },
  data() {
    return {
      yearList: [
        2022, 2021, 2020, 2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011,
        2010, 2009,
      ],
      monthList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      selectSido: "",
      selectGugun: "",
      selectDong: "",
      selectYear: "",
      selectMonth: "",
      map: null,
      dialog: false,
      currentDialogItem: {},
      mapswitch: true,
      markers: [],
      currCategory: "",
      ps: null,
      placeOverlay: null,
      sangmap: null,
      contentNode: null,
      id: "",
      className: {
        BK9: "",
        MT1: "",
        PM9: "",
        OL7: "",
        CE7: "",
        CS2: "",
      },
      interestMark: "",
    };
  },
  watch: {
    selectSido() {
      this.getGugunNames(this.selectSido);
    },
    selectGugun() {
      let data = { sidoName: this.selectSido, gugunName: this.selectGugun };
      this.getDongNames(data);
    },
    aptSearchList() {
      if (window.kakao && window.kakao.maps) {
        this.loadMap();
      } else {
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.loadMap);
        script.src =
          "//dapi.kakao.com/v2/maps/sdk.js?appkey=78b26667d20a38aa946ea1f6a8384730&autoload=false&libraries=services";
        document.head.appendChild(script);
      }
    },
    markers() {
      console.log(this.markers);
    },
    isInterest() {
      this.interestMark = this.isInterest;
    },
  },
  methods: {
    registInterest() {
      if (this.userInfo) {
        let interestdto = {
          userid: this.userInfo.userid,
          aptname: this.currentDialogItem.apartmentName,
          aptcode: this.currentDialogItem.aptCode,
          dongname: this.currentDialogItem.dongName,
          lat: this.currentDialogItem.let,
          lng: this.currentDialogItem.lng,
        };
        this.registAptInterest(interestdto);
        this.interestMark = true;
      } else {
        alert("로그인이 필요한 기능입니다.");
      }
    },
    selectList() {
      let data = {
        sido: this.selectSido,
        dong: this.selectDong,
        year: this.selectYear,
        month: this.selectMonth,
      };
      this.getSearchList(data);
    },
    loadMaker() {
      // 마커 이미지의 이미지 주소입니다
      var imageSrc =
        "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";

      for (var i = 0; i < this.aptSearchList.length; i++) {
        // 마커 이미지의 이미지 크기 입니다
        var imageSize = new kakao.maps.Size(24, 35);

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
        let latlng = new kakao.maps.LatLng(
          this.aptSearchList[i].let,
          this.aptSearchList[i].lng
        );
        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
          map: this.map, // 마커를 표시할 지도
          position: latlng, // 마커를 표시할 위치
          title: this.aptSearchList[i].apartmentName, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
          image: markerImage, // 마커 이미지
        });
        marker.setMap(this.map);
      }
    },
    loadMap() {
      if (this.aptSearchList.length != 0) {
        const container = document.getElementById("map");
        const options = {
          center: new kakao.maps.LatLng(
            this.aptSearchList[0].let,
            this.aptSearchList[0].lng
          ),
          level: 5,
        };

        this.map = new window.kakao.maps.Map(container, options);
        this.loadMaker();
      }
    },
    showDialog(no) {
      this.currentDialogItem = this.aptSearchList[no];
      if (this.userInfo) {
        let data = {
          userid: this.userInfo.userid,
          aptname: this.currentDialogItem.apartmentName,
        };
        this.aptInterestCount(data);
        console.log(this.isInterest);
      }
      this.dialog = true;
      setTimeout(this.loadView, 100);
    },
    showLoadmap() {
      this.mapswitch = true;
      var sangviewContainer = document.getElementById("sangview"); //로드뷰를 표시할 div
      sangviewContainer.innerHTML = "";
      setTimeout(this.loadView, 100);
    },
    showSangmap() {
      this.mapswitch = false;
      var roadviewContainer = document.getElementById("roadview"); //로드뷰를 표시할 div
      roadviewContainer.innerHTML = "";
      setTimeout(this.sangView, 100);
    },
    loadView() {
      var roadviewContainer = document.getElementById("roadview"); //로드뷰를 표시할 div
      var roadview = new kakao.maps.Roadview(roadviewContainer); //로드뷰 객체
      var roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체

      var position = new kakao.maps.LatLng(
        this.currentDialogItem.let,
        this.currentDialogItem.lng
      );
      // 특정 위치의 좌표와 가까운 로드뷰의 panoId를 추출하여 로드뷰를 띄운다.
      roadviewClient.getNearestPanoId(position, 150, function (panoId) {
        roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
      });
    },
    sangView() {
      this.placeOverlay = new kakao.maps.CustomOverlay({ zIndex: 1 });
      this.contentNode = document.createElement("div"); // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다

      var mapContainer = document.getElementById("sangview"), // 지도를 표시할 div
        mapOption = {
          center: new kakao.maps.LatLng(
            this.currentDialogItem.let,
            this.currentDialogItem.lng
          ), // 지도의 중심좌표
          level: 5, // 지도의 확대 레벨
        };

      // 지도를 생성합니다
      this.sangmap = new kakao.maps.Map(mapContainer, mapOption);

      // 장소 검색 객체를 생성합니다
      this.ps = new kakao.maps.services.Places(this.sangmap);

      // 지도에 idle 이벤트를 등록합니다
      kakao.maps.event.addListener(this.sangmap, "idle", this.searchPlaces);

      // 커스텀 오버레이의 컨텐츠 노드에 css class를 추가합니다
      this.contentNode.className = "placeinfo_wrap";

      // 커스텀 오버레이의 컨텐츠 노드에 mousedown, touchstart 이벤트가 발생했을때
      // 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록합니다
      this.addEventHandle(
        this.contentNode,
        "mousedown",
        kakao.maps.event.preventMap
      );
      this.addEventHandle(
        this.contentNode,
        "touchstart",
        kakao.maps.event.preventMap
      );

      // 커스텀 오버레이 컨텐츠를 설정합니다
      this.placeOverlay.setContent(this.contentNode);

      // 각 카테고리에 클릭 이벤트를 등록합니다
      this.addCategoryClickEvent();
    },
    addEventHandle(target, type, callback) {
      if (target.addEventListener) {
        target.addEventListener(type, callback);
      } else {
        target.attachEvent("on" + type, callback);
      }
    },

    // 카테고리 검색을 요청하는 함수입니다
    searchPlaces() {
      if (!this.currCategory) {
        return;
      }

      // 커스텀 오버레이를 숨깁니다
      this.placeOverlay.setMap(null);

      // 지도에 표시되고 있는 마커를 제거합니다
      this.removeMarker();

      this.ps.categorySearch(this.currCategory, this.placesSearchCB, {
        useMapBounds: true,
      });
    },

    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    placesSearchCB(data, status) {
      if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면 지도에 마커를 표출합니다
        this.displayPlaces(data);
      } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
      } else if (status === kakao.maps.services.Status.ERROR) {
        // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
      }
    },

    // 지도에 마커를 표출하는 함수입니다
    displayPlaces(places) {
      // 몇번째 카테고리가 선택되어 있는지 얻어옵니다
      // 이 순서는 스프라이트 이미지에서의 위치를 계산하는데 사용됩니다
      var order = document
        .getElementById(this.currCategory)
        .getAttribute("data-order");

      for (var i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = this.addMarker(
          new kakao.maps.LatLng(places[i].y, places[i].x),
          order
        );

        // 마커와 검색결과 항목을 클릭 했을 때
        // 장소정보를 표출하도록 클릭 이벤트를 등록합니다
        (function (marker, place) {
          kakao.maps.event.addListener(marker, "click", function () {
            this.displayPlaceInfo(place);
          });
        })(marker, places[i]);
      }
    },

    // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
    addMarker(position, order) {
      var imageSrc =
          "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png", // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(27, 28), // 마커 이미지의 크기
        imgOptions = {
          spriteSize: new kakao.maps.Size(72, 208), // 스프라이트 이미지의 크기
          spriteOrigin: new kakao.maps.Point(46, order * 36), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
          offset: new kakao.maps.Point(11, 28), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(
          imageSrc,
          imageSize,
          imgOptions
        ),
        marker = new kakao.maps.Marker({
          position: position, // 마커의 위치
          image: markerImage,
        });
      marker.setMap(this.sangmap); // 지도 위에 마커를 표출합니다
      this.markers.push(marker); // 배열에 생성된 마커를 추가합니다

      return marker;
    },

    // 지도 위에 표시되고 있는 마커를 모두 제거합니다
    removeMarker() {
      for (var i = 0; i < this.markers.length; i++) {
        this.markers[i].setMap(null);
      }
      this.markers = [];
    },

    // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수입니다
    displayPlaceInfo(place) {
      var content =
        '<div class="placeinfo">' +
        '   <a class="title" href="' +
        place.place_url +
        '" target="_blank" title="' +
        place.place_name +
        '">' +
        place.place_name +
        "</a>";

      if (place.road_address_name) {
        content +=
          '    <span title="' +
          place.road_address_name +
          '">' +
          place.road_address_name +
          "</span>" +
          '  <span class="jibun" title="' +
          place.address_name +
          '">(지번 : ' +
          place.address_name +
          ")</span>";
      } else {
        content +=
          '    <span title="' +
          place.address_name +
          '">' +
          place.address_name +
          "</span>";
      }

      content +=
        '    <span class="tel">' +
        place.phone +
        "</span>" +
        "</div>" +
        '<div class="after"></div>';

      this.contentNode.innerHTML = content;
      this.placeOverlay.setPosition(new kakao.maps.LatLng(place.y, place.x));
      this.placeOverlay.setMap(this.sangmap);
    },

    // 각 카테고리에 클릭 이벤트를 등록합니다
    addCategoryClickEvent() {
      var category = document.getElementById("category"),
        children = category.children;

      for (var i = 0; i < children.length; i++) {
        children[i].onclick = this.onClickCategory;
      }
    },

    // 카테고리를 클릭했을 때 호출되는 함수입니다
    onClickCategory() {
      var id = this.id,
        className = this.className.id;
      console.log(this.id, className);
      this.placeOverlay.setMap(null);

      if (className === "on") {
        this.currCategory = "";
        this.changeCategoryClass();
        this.removeMarker();
      } else {
        this.currCategory = id;
        this.changeCategoryClass(this);
        this.searchPlaces();
      }
    },

    // 클릭된 카테고리에만 클릭된 스타일을 적용하는 함수입니다
    changeCategoryClass(el) {
      var category = document.getElementById("category"),
        children = category.children,
        i;
      console.log(children);
      for (i = 0; i < children.length; i++) {
        children[i].className = "";
      }

      if (el) {
        el.className = "on";
      }
    },
    ...mapActions(aptStore, [
      "getSidoNames",
      "getGugunNames",
      "getDongNames",
      "getSearchList",
      "registAptInterest",
      "aptInterestCount",
    ]),
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.loadMap();
    } else {
      const script = document.createElement("script");
      script.onload = () => kakao.maps.load(this.loadMap);
      script.src =
        "//dapi.kakao.com/v2/maps/sdk.js?appkey=78b26667d20a38aa946ea1f6a8384730&autoload=false&libraries=services";
      document.head.appendChild(script);
    }
  },
  computed: {
    ...mapState(aptStore, [
      "sidoList",
      "gugunList",
      "dongList",
      "aptSearchList",
      "isInterest",
    ]),
    ...mapState(userStore, ["userInfo"]),
  },
};
</script>

<style>
.map_wrap,
.map_wrap * {
  margin: 0;
  padding: 0;
  font-size: 12px;
}
.map_wrap {
  position: relative;
  width: 100%;
}
#category {
  position: absolute;
  top: 10px;
  left: 10px;
  border-radius: 5px;
  border: 1px solid #909090;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.4);
  background: #fff;
  overflow: hidden;
  z-index: 2;
}
#category li {
  float: left;
  list-style: none;
  width: 50px;
  border-right: 1px solid #acacac;
  padding: 6px 0;
  text-align: center;
  cursor: pointer;
}
#category li.on {
  background: #eee;
}
#category li:hover {
  background: #ffe6e6;
  border-left: 1px solid #acacac;
  margin-left: -1px;
}
#category li:last-child {
  margin-right: 0;
  border-right: 0;
}
#category li span {
  display: block;
  margin: 0 auto 3px;
  width: 27px;
  height: 28px;
}
#category li .category_bg {
  background: url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png)
    no-repeat;
}
#category li .bank {
  background-position: -10px 0;
}
#category li .mart {
  background-position: -10px -36px;
}
#category li .pharmacy {
  background-position: -10px -72px;
}
#category li .oil {
  background-position: -10px -108px;
}
#category li .cafe {
  background-position: -10px -144px;
}
#category li .store {
  background-position: -10px -180px;
}
#category li.on .category_bg {
  background-position-x: -46px;
}
.placeinfo_wrap {
  position: absolute;
  bottom: 28px;
  left: -150px;
  width: 300px;
}
.placeinfo {
  position: relative;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ccc;
  border-bottom: 2px solid #ddd;
  padding-bottom: 10px;
  background: #fff;
}
.placeinfo:nth-of-type(n) {
  border: 0;
  box-shadow: 0px 1px 2px #888;
}
.placeinfo_wrap .after {
  content: "";
  position: relative;
  margin-left: -12px;
  left: 50%;
  width: 22px;
  height: 12px;
  background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png");
}
.placeinfo a,
.placeinfo a:hover,
.placeinfo a:active {
  color: #fff;
  text-decoration: none;
}
.placeinfo a,
.placeinfo span {
  display: block;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
.placeinfo span {
  margin: 5px 5px 0 5px;
  cursor: default;
  font-size: 13px;
}
.placeinfo .title {
  font-weight: bold;
  font-size: 14px;
  border-radius: 6px 6px 0 0;
  margin: -1px -1px 0 -1px;
  padding: 10px;
  color: #fff;
  background: #d95050;
  background: #d95050
    url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png)
    no-repeat right 14px center;
}
.placeinfo .tel {
  color: #0f7833;
}
.placeinfo .jibun {
  color: #999;
  font-size: 11px;
  margin-top: 0;
}
/* .v-application .text-left {
  background-color: rgb(253, 253, 255);
} */
</style>