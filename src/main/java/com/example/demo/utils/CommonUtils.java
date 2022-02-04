package com.example.demo.utils;

import java.util.UUID;

public class CommonUtils {

	public static String getUniqueId() {
		return UUID.randomUUID().toString();
	}
}
