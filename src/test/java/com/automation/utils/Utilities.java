package com.automation.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Utilities {

	String result = "";
	private String filename = "";

	public Utilities(String filename) {
		this.filename = filename;
	}

	public String getInputdata(String inputkey) throws IOException {

		Properties prop = new Properties();
		try {
			// load a properties file from class path, inside static method
			prop.load(Utilities.class.getClassLoader().getResourceAsStream(this.filename));

			result = prop.getProperty(inputkey);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public String uniqueEmailidCreation(String email) {
		int indexofAppos = email.indexOf("@");
		String originalemaild = email.substring(0, indexofAppos);
		Random rd = new Random();
		return email.replaceAll(originalemaild, originalemaild + "+" + Math.abs(rd.nextInt()));
	}

}
