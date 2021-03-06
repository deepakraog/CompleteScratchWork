/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;

public class readLogs {

	public void compress() throws IOException {

		String LOGPATH, CAN_GZIP, DELETE_UNZIP_FILES;

		LinkedHashMap<String, String> in = new LinkedHashMap<String, String>();

		in = inputFetch.inputValues();

		Gzip gz = new Gzip();

		LOGPATH = in.get("LOGPATH");
		CAN_GZIP = in.get("CAN_GZIP");
		DELETE_UNZIP_FILES = in.get("DELETE_UNZIP_FILES");

		String source_filepath = null, destinaton_zip_filepath = null;
		File file = new File(LOGPATH);
		if (file != null && file.exists()) {
			File[] listOfFiles = file.listFiles();
			Arrays.sort(listOfFiles);

			if (listOfFiles != null) {

				for (int i = 0; i < listOfFiles.length; i++) {
					System.out.println("####### File " + listOfFiles[i].getName() + " Compression Started #######");
					if (listOfFiles[i].isFile()) {
						if (CAN_GZIP.equals("Y") && !(listOfFiles[i].getName().endsWith(".gz"))) {

							source_filepath = listOfFiles[i].getAbsolutePath();
							destinaton_zip_filepath = listOfFiles[i].getAbsolutePath() + ".gz";
							gz.gzipFile(source_filepath, destinaton_zip_filepath, DELETE_UNZIP_FILES);

						} else {
							System.out.println("File Compression Not Required!! ");
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "resource" })
	public void read() throws IOException {

		String LOGPATH, LOADIP, DATECOLUMN, FORMAT, DAIUSLOG, ERRORPATH, PRECISETPS;
		long TotalTime = 0, loglinesRead = 0L, errorLineCounter = 0L;

		LinkedHashMap<String, String> all = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> in = new LinkedHashMap<String, String>();

		in = inputFetch.inputValues();

		apiValidator avdtr = new apiValidator();
		dateValidator dtr = new dateValidator();

		SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date date = new Date();

		LOGPATH = in.get("LOGPATH");
		LOADIP = in.get("LOADIP");
		DATECOLUMN = in.get("DATECOLUMN");
		FORMAT = in.get("TIME_FORMAT");
		DAIUSLOG = in.get("DAIUSLOG");
		ERRORPATH = in.get("ERRORPATH");
		PRECISETPS = in.get("PRECISETPS");

		// Multiple IP Handling
		String[] IP = LOADIP.split(",");
		BufferedWriter bw = null;
		bw = new BufferedWriter(new FileWriter(ERRORPATH + "Error_" + dateformat.format(date) + ".txt"));
		String line;

		String strLine = null, firstLine = null;
		File file = new File(LOGPATH);
		if (file != null && file.exists()) {
			File[] listOfFiles = file.listFiles();
			Arrays.sort(listOfFiles);
			if (listOfFiles != null) {
				for (int i = 0; i < listOfFiles.length; i++) {
					System.out.println("####### File " + listOfFiles[i].getName() + " cached for Processing #######");

					if (listOfFiles[i].isFile()) {
						FileInputStream fin = new FileInputStream(listOfFiles[i]);
						InputStreamReader isr = null;
						if (listOfFiles[i].getName().endsWith(".gz")) {

							GZIPInputStream gzis = new GZIPInputStream(fin);
							isr = new InputStreamReader(gzis);

						} else {
							isr = new InputStreamReader(fin);
						}
						BufferedReader br = new BufferedReader(isr);
						while ((line = br.readLine()) != null) {
							loglinesRead += 1L;
							for (int x = 0; x < IP.length; x++) {
								if (line.contains(IP[x])) {
									if (firstLine == null) {
										firstLine = line;
									}
									strLine = line;
									try {
										all = avdtr.readCheck(strLine);
									} catch (Exception e) {
										try {
											errorLineCounter += 1L;
											System.out.println("LineNumber " + loglinesRead);
											System.out.println("ReadLine " + line);
											e.printStackTrace();
										} finally {
											bw.write(loglinesRead + " :: " + line);
										}
									}
									break;
								}
							}
						}

					}

				}

				if (PRECISETPS.equals("N")) {
					if (firstLine != null && strLine != null) {
						String DateS[] = firstLine.split(" ");
						String DateF[] = strLine.split(" ");

						String time1 = null, time2 = null;

						if (DAIUSLOG.equals("N")) {
							String dtS[] = DateS[Integer.parseInt(DATECOLUMN) - 1].split("\\[");
							String dtF[] = DateF[Integer.parseInt(DATECOLUMN) - 1].split("\\[");

							time1 = dtS[1];
							time2 = dtF[1];
						} else if (DAIUSLOG.equals("Y")) {

							time1 = DateS[Integer.parseInt(DATECOLUMN) - 1] + " " + DateS[Integer.parseInt(DATECOLUMN)];
							time2 = DateF[Integer.parseInt(DATECOLUMN) - 1] + " " + DateF[Integer.parseInt(DATECOLUMN)];
						}
						TotalTime = dtr.dateC(time1, time2, FORMAT);
					}
				}
			}

		}
		avdtr.csvLogs(TotalTime, all);
		System.out.println("Total Lines Read " + loglinesRead);
		if (errorLineCounter != 0) {
			System.out.println("Total Input Error Records " + errorLineCounter);
		}
		bw.close();
	}

}
