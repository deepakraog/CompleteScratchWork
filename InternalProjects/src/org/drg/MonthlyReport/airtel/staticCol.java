package org.drg.MonthlyReport.airtel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class staticCol {
	@SuppressWarnings("rawtypes")
	public static void initializeStaticVaraibles() {
		LinkedHashMap<String, String> prodKey = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> prodPP = new LinkedHashMap<String, String>();

		//Code to read property files before start of program
		System.out.println("Static block");

		BufferedReader brKeyword = null;
		BufferedReader brPP = null;
		FileWriter out = null;
		BufferedWriter bw = null;

		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
			String OFFLINE_KEYWORD_DETAILS = prop.getProperty("OFFLINE_KEYWORD_DETAILS");
			String OFFLINE_PRODUCTID_DETAILS = prop.getProperty("OFFLINE_PRODUCTID_DETAILS");
			@SuppressWarnings("unused")
			String PRODUCT = prop.getProperty("PRODUCT");
			String ALLVALUE = prop.getProperty("ALLVALUE");
			out = new FileWriter(ALLVALUE);
			bw = new BufferedWriter(out);
			brKeyword = new BufferedReader(new FileReader(OFFLINE_KEYWORD_DETAILS));
			brPP = new BufferedReader(new FileReader(OFFLINE_PRODUCTID_DETAILS));
			String[] str = null;
			String line;
			while ((line = brKeyword.readLine()) != null) {
				str = line.split(",");
				for (int i = 0; i < str.length - 1; i++) {
					prodKey.put(str[i], str[(i + 1)]);
				}
			}
			while ((line = brPP.readLine()) != null) {
				str = line.split(",");
				for (int i = 0; i < str.length - 1; i++) {
					prodPP.put(str[i], str[(i + 1)]);
				}
			}
			for (Map.Entry entry : prodKey.entrySet()) {
				line = prodPP.containsKey(entry.getKey()) ? (String) entry.getValue() + "," + (String) entry.getKey()
						+ "," + (String) prodPP.get(entry.getKey()) + ",0.0,0.0" : "N/A";
				bw.write(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();

			if (bw != null)
				try {
					bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}