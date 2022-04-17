package com.example.demo.utils;

import java.util.List;
import java.util.UUID;

public class CommonUtils {

	public static String getUniqueId() {
		return UUID.randomUUID().toString();
	}

	public static String populateInClause(String query, List<Object> list) {
		if(list!=null && !list.isEmpty()) {
			query += "(";
			for(int i=0; i<list.size(); i++) {
				query+="?";
				if(i!=list.size()-1)
					query+=",";
			}
			query+=")";
		}
		return query;
	}
}
