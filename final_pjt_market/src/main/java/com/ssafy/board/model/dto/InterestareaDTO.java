package com.ssafy.board.model.dto;

public class InterestareaDTO {
    private String userid;
    private String aptname;
    private String aptcode;
    private String dongname;
    private String lat;
    private String lng;

    public InterestareaDTO() {
    }

    public InterestareaDTO(String userid, String aptname, String aptcode, String dongname, String lat, String lng) {
        this.userid = userid;
        this.aptname = aptname;
        this.aptcode = aptcode;
        this.dongname = dongname;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAptname() {
        return aptname;
    }

    public void setAptname(String aptname) {
        this.aptname = aptname;
    }

    public String getAptcode() {
        return aptcode;
    }

    public void setAptcode(String aptcode) {
        this.aptcode = aptcode;
    }

    public String getDongname() {
        return dongname;
    }

    public void setDongname(String dongname) {
        this.dongname = dongname;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "InterestareaDTO [id=" + userid + ", aptname=" + aptname + ", aptcode=" + aptcode + ", dongname=" + dongname
                + ", lat=" + lat + ", lng=" + lng + "]";
    }

}