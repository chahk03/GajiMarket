import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store";

Vue.use(VueRouter);

const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["userStore/checkUserInfo"];
  const checkToken = store.getters["userStore/checkToken"];
  let token = sessionStorage.getItem("access-token");
  console.log("로그인 처리 전", checkUserInfo, token);

  if (checkUserInfo != null && token) {
    console.log("토큰 유효성 체크하러 가자!!!!");
    await store.dispatch("userStore/getUserInfo", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요한 페이지입니다.");
    // next({ name: "login" });
    router.push({ name: "user" });
  } else {
    console.log("로그인 했다!!!!!!!!!!!!!.");
    next();
  }
};

const routes = [
  {
    path: "/",
    name: "main",
    component: () => import("@/views/AppMain"),
  },
  {
    path: "/chat",
    name: "chat",
    beforeEnter: onlyAuthUser,
    component: () => import("@/views/AppChat"),
  },
  {
    path: "/board",
    name: "board",
    component: () => import("@/views/AppBoard"),
    redirect: "/board/list",
    children: [
      {
        path: "list",
        name: "boardlist",
        component: () => import("@/components/board/BoardList"),
      },
      {
        path: "read",
        name: "boardread",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardRead"),
      },
      {
        path: "create",
        name: "boardcreate",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardCreate"),
      },
      {
        path: "update",
        name: "boardupdate",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/board/BoardUpdate"),
      },
    ],
  },
  {
    path: "/apt",
    name: "apt",
    component: () => import("@/views/AppApt"),
    redirect: "/apt/search",
    children: [
      {
        path: "search",
        name: "aptsearch",
        component: () => import("@/components/apt/AptSearch"),
      },
      {
        path: "interest",
        name: "aptinterest",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/apt/AptInterest"),
      },
    ],
  },
  {
    path: "/user",
    name: "user",
    component: () => import("@/views/AppUser"),
    redirect: "/user/login",
    children: [
      {
        path: "login",
        name: "userlogin",
        component: () => import("@/components/user/UserLogin"),
      },
      {
        path: "signup",
        name: "usersignup",
        component: () => import("@/components/user/UserSignup"),
      },
      {
        path: "mypage",
        name: "usermypage",
        component: () => import("@/components/user/UserMypage"),
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
