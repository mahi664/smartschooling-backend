package com.example.demo.bo;

public class MonthMasterDetailsBO {

	private int monthId;
	private String monthName;
	public int getMonthId() {
		return monthId;
	}
	public void setMonthId(int monthId) {
		this.monthId = monthId;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	@Override
	public String toString() {
		return "MonthMasterDetailsBO [monthId=" + monthId + ", monthName=" + monthName + "]";
	}
	
	
}
