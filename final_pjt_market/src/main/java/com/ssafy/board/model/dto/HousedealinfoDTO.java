package com.ssafy.board.model.dto;

public class HousedealinfoDTO {
	private int dno;
    private String apartmentName;
    private String floor;
    private String area;
    private String dongName;
    private String dealAmount;
    private String let;
    private String lng;
    private int dealYear;
    private int dealMonth;
    private String aptCode;
	public HousedealinfoDTO() {	}
	public HousedealinfoDTO(int dno,String apartmentName, String floor, String area, String dongName, String dealAmount,
			String let, String lng, int dealYear, int dealMonth,String aptCode) {
		super();
		this.dno = dno;
		this.apartmentName = apartmentName;
		this.floor = floor;
		this.area = area;
		this.dongName = dongName;
		this.dealAmount = dealAmount;
		this.let = let;
		this.lng = lng;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.aptCode = aptCode;
	}
	public int getDno() {
		return dno;
	}
	public void setDno(int dno) {
		this.dno = dno;
	}
	public String getAptCode() {
		return aptCode;
	}
	public void setAptCode(String aptCode) {
		this.aptCode = aptCode;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDongName() {
		return dongName;
	}
	public void setDongName(String dongName) {
		this.dongName = dongName;
	}
	public String getDealAmount() {
		return dealAmount;
	}
	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}
	public String getLet() {
		return let;
	}
	public void setLet(String let) {
		this.let = let;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public int getDealYear() {
		return dealYear;
	}
	public void setDealYear(int dealYear) {
		this.dealYear = dealYear;
	}
	public int getDealMonth() {
		return dealMonth;
	}
	public void setDealMonth(int dealMonth) {
		this.dealMonth = dealMonth;
	}
	@Override
	public String toString() {
		return "HousedealinfoDTO [dno=" + dno + ", apartmentName=" + apartmentName + ", floor=" + floor + ", area="
				+ area + ", dongName=" + dongName + ", dealAmount=" + dealAmount + ", let=" + let + ", lng=" + lng
				+ ", dealYear=" + dealYear + ", dealMonth=" + dealMonth + ", aptCode=" + aptCode + "]";
	}
}




