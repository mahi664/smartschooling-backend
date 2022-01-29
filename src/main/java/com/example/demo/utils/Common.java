package com.example.demo.utils;

import java.util.logging.FileHandler;

import org.jboss.logging.Logger;

import com.example.demo.DemoApplication;

public class Common 
{
	public static Logger logger = Logger.getLogger(DemoApplication.class.getName());
	public static FileHandler logfile = null;
	
	public static void initializeLogger()
	{
		
	}

}
