package org.drg.MonthlyReport.airtel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;

public class reportLogs {

	public void reportDaily(LinkedHashMap<String, Double> actProdKeyPP, LinkedHashMap<String, Double> renProdKeyPP,
			int i, String dateName) throws IOException {
		Properties prop = new Properties();
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);

			String PRODUCT = prop.getProperty("PRODUCT");
			String OUTDIR = prop.getProperty("OUTDIR");
			String ALLVALUE = prop.getProperty("ALLVALUE");
			String fileName = PRODUCT.replace("\"", "") + "_Product_Revenue_" + dateName;
			br = new BufferedReader(new FileReader(ALLVALUE));
			bw = new BufferedWriter(new FileWriter(OUTDIR + fileName + ".csv"));
			bw.write("KEYWORD,PRODUCT_ID,PRICE-POINT,ACT_REVENUE,RENEWAL_REVENUE\n");
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.equals("N/A")) {
					String[] str = line.split(",");
					line = str[0] + "," + str[1] + "," + str[2] + ","
							+ (actProdKeyPP.containsKey(str[1]) ? actProdKeyPP.get(str[1]) : str[3]) + ","
							+ (renProdKeyPP.containsKey(str[1]) ? renProdKeyPP.get(str[1]) : str[4]);
					bw.write(line + "\n");
				}

			}

		} catch (IOException e) {
			e.getStackTrace();
		} finally {

			if (br != null && bw != null)
				try {
					br.close();
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

	}
}