package org.drg.MonthlyReport.airtel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.GZIPInputStream;

public class readGZIP {
	public static void main(String[] argv) throws IOException {
		//Initiate the application
		staticCol.initializeStaticVaraibles();
		consReport cn = null;
		BufferedReader is = null;
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException e) {
			e.getStackTrace();
		}

		String DUMPSPATH = prop.getProperty("DUMPSPATH");

		generateFileNames gf = new generateFileNames();
		keyPriceDetails key = new keyPriceDetails();
		reportLogs rpl = new reportLogs();
		File file = new File(DUMPSPATH);
		try {
			if ((file != null) && (file.exists())) {
				File[] listOfFiles = file.listFiles();
				Arrays.sort(listOfFiles);

				if (listOfFiles != null) {
					for (int i = 0; i < listOfFiles.length; i++) {
						String dateName = gf.names(listOfFiles[i]);
						System.out.println("File_Name " + listOfFiles[i].getName());
						System.out.println("dateName " + dateName);

						if (listOfFiles[i].isFile()) {
							FileInputStream fin = new FileInputStream(listOfFiles[i]);
							GZIPInputStream gzis = new GZIPInputStream(fin);
							InputStreamReader xover = new InputStreamReader(gzis);
							is = new BufferedReader(xover);
							String line;
							while ((line = is.readLine()) != null) {
								if ((line.contains("New-Subscription"))
										&& (line.contains("New-Subscription Success"))) {
									String[] delimitline = line.replace("\"", "").split(",");
									key.keyPriceAct(delimitline[3], Double.valueOf(Double.parseDouble(delimitline[9])));
								}

								if ((line.contains("Re-Subscription")) && (line.contains("Charging Success"))) {
									String[] delimitline = line.replace("\"", "").split(",");
									key.keyPriceRen(delimitline[3], Double.valueOf(Double.parseDouble(delimitline[9])));
								}
							}

						}

						rpl.reportDaily(keyPriceDetails.actProdKeyPP, keyPriceDetails.renProdKeyPP, i, dateName);
						keyPriceDetails.actProdKeyPP.clear();
						keyPriceDetails.renProdKeyPP.clear();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		cn = new consReport();
		cn.consolidatedReport();
		System.out.println("Monthly Offline Report generation is Done....");
	}
}