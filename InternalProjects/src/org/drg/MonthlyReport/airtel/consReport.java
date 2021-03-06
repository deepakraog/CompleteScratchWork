package org.drg.MonthlyReport.airtel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class consReport {
	public consReport() {
	}

	public void consolidatedReport() throws IOException {
		Properties prop = new Properties();
		try {
			java.io.InputStream input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException e) {
			e.getStackTrace();
		}

		@SuppressWarnings("unused")
		String PRODUCT = prop.getProperty("PRODUCT");
		String PRODUCTFILENAME = prop.getProperty("PRODUCTFILENAME");
		String KEYWORDFILENAME = prop.getProperty("KEYWORDFILENAME");
		String OFFLINE_KEYWORD_DETAILS = prop.getProperty("OFFLINE_KEYWORD_DETAILS");
		String OUTDIR = prop.getProperty("OUTDIR");
		String RESULTDIR = prop.getProperty("RESULTDIR");

		String doubPrice = null;
		Double allPrice = Double.valueOf(0.0D);
		String Header = "KEYWORD,PRODUCT_ID";
		String HeaderforKeyword = "KEYWORD";
		BufferedWriter bwproduct = new BufferedWriter(new FileWriter(RESULTDIR + PRODUCTFILENAME));
		BufferedWriter bwkeyword = new BufferedWriter(new FileWriter(RESULTDIR + KEYWORDFILENAME));
		File file = new File(OUTDIR);
		BufferedReader br = new BufferedReader(new FileReader(OFFLINE_KEYWORD_DETAILS));

		LinkedHashMap<String, String> storedvalues = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> sumValue = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Double> tempSumValue = new LinkedHashMap<String, Double>();
		LinkedHashMap<String, Double> tempSumValueNew = new LinkedHashMap<String, Double>();
		LinkedHashMap<String, Double> storedDatavalues = new LinkedHashMap<String, Double>();
		String linename;
		while ((linename = br.readLine()) != null) {
			if (!linename.contains("KEYWORD")) {

				String[] fnm = linename.split(",");
				storedvalues.put(fnm[1] + "," + fnm[0], "");
				sumValue.put(fnm[1], "0.0");
				tempSumValue.put(fnm[1], Double.valueOf(0.0D));
			}
		}
		BufferedReader bb;
		if ((file != null) && (file.exists())) {
			File[] listOfFiles = file.listFiles();
			java.util.Arrays.sort(listOfFiles);
			if (listOfFiles != null) {
				bb = null;
				for (int i = 0; i < listOfFiles.length; i++) {
					Map.Entry<String, Double> amt;
					if (listOfFiles[i].isFile()) {
						bb = new BufferedReader(new FileReader(listOfFiles[i]));
						String[] nameVoid = listOfFiles[i].getName().split("_");
						Header = Header + "," + nameVoid[3] + "_" + nameVoid[4] + "_" + nameVoid[5].replace(".csv", "");
						HeaderforKeyword = HeaderforKeyword + "," + nameVoid[3] + "_" + nameVoid[4] + "_"
								+ nameVoid[5].replace(".csv", "");
						allPrice = Double.valueOf(0.0D);
						tempSumValue.clear();
						String line;
						while ((line = bb.readLine()) != null) {
							if (!line.contains("KEYWORD")) {

								String[] okd = line.split(",");
								String keyProd = okd[0] + "," + okd[1];
								String keyAll = okd[0];

								doubPrice = storedvalues.containsKey(keyProd)
										? (String) storedvalues.get(keyProd) + ","
												+ (Double.parseDouble(okd[3]) + Double.parseDouble(okd[4]))
										: Double.toString(Double.parseDouble(okd[3]) + Double.parseDouble(okd[4]));

								allPrice = Double.valueOf(tempSumValue.containsKey(keyAll)
										? ((Double) tempSumValue.get(keyAll)).doubleValue() + Double.parseDouble(okd[3])
												+ Double.parseDouble(okd[4])
										: Double.parseDouble(okd[3]) + Double.parseDouble(okd[4]));

								storedvalues.put(keyProd, doubPrice);
								tempSumValue.put(keyAll, allPrice);
							}
						}
						for (Iterator<Entry<String, Double>> localIterator = tempSumValue.entrySet()
								.iterator(); localIterator.hasNext();) {
							amt = (Entry<String, Double>) localIterator.next();
							tempSumValueNew.put((String) amt.getKey(), (Double) amt.getValue());
						}
					}

					for (Map.Entry<String, Double> alas : tempSumValueNew.entrySet()) {
						storedDatavalues.put((String) alas.getKey(), alas.getValue());
					}
				}
				bb.close();
			}
		}

		bwproduct.write(Header + "\n");
		bwkeyword.write(HeaderforKeyword + "\n");

		for (Map.Entry<String, String> entry : storedvalues.entrySet()) {
			bwproduct.write((String) entry.getKey() + (String) entry.getValue() + "\n");
		}

		for (Entry<String, Double> entry : storedDatavalues.entrySet()) {
			bwkeyword.write((String) entry.getKey() + "," + entry.getValue() + "\n");
		}

		bwproduct.close();
		bwkeyword.close();
		br.close();
	}
}
