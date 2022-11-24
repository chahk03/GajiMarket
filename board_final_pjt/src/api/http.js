import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "http://localhost:8888/board",
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});
