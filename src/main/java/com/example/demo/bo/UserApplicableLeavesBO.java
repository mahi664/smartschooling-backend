package com.example.demo.bo;

import java.util.Date;

public class UserApplicableLeavesBO {

	private String userLeaveRecId;
	private LeaveDetailsBO leaveDetailsBO;
	private Date effDate;
	private Date endDate;
	public String getUserLeaveRecId() {
		return userLeaveRecId;
	}
	public void setUserLeaveRecId(String userLeaveRecId) {
		this.userLeaveRecId = userLeaveRecId;
	}
	public LeaveDetailsBO getLeaveDetailsBO() {
		return leaveDetailsBO;
	}
	public void setLeaveDetailsBO(LeaveDetailsBO leaveDetailsBO) {
		this.leaveDetailsBO = leaveDetailsBO;
	}
	public Date getEffDate() {
		return effDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "UserApplicableLeavesBO [userLeaveRecId=" + userLeaveRecId + ", leaveDetailsBO=" + leaveDetailsBO
				+ ", effDate=" + effDate + ", endDate=" + endDate + "]";
	}
	
	
}
