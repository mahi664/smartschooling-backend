package com.example.demo.constant;

public enum Religion {

	HINDU("HINDU"),MUSLIM("MUSLIM"),CHRISTIAN("CHRISTIAN");

	private String religion;
	
	Religion(String religion) {
		this.religion = religion;
	}
	
	public String getReligion() {
		return this.religion;
	}
	
}
