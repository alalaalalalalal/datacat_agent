package com.main.datacat_agent;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TEST  {
	
	public static void main(String[] args) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(timestamp.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.println(sdf.format(timestamp));
		
	}
}




