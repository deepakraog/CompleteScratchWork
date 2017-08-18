/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

public class apiValidator {
	static LinkedHashMap<String, String> in;
	static LinkedHashMap<String, String[]> inM;
	static {
		in = inputFetch.inputValues();
		inM = inputFetch.multipleValues();
	}

	LinkedHashMap<String, String> all = new LinkedHashMap<String, String>();
	LinkedHashMap<String, Long> allCount = new LinkedHashMap<String, Long>();
	LinkedHashMap<String, BigInteger> apivalues = new LinkedHashMap<String, BigInteger>();
	LinkedHashMap<String, String> Maxlogs = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> Timerlogs = new LinkedHashMap<String, String>();
	LinkedHashMap<String, Long> avg = new LinkedHashMap<String, Long>();
	LinkedHashMap<String, LinkedList<Long>> avg90 = new LinkedHashMap<String, LinkedList<Long>>();

	@SuppressWarnings("boxing")
	Long count = 0L;
	int timer = Integer.parseInt(in.get("DATECOLUMN"));
	int exetime = Integer.parseInt(in.get("EXECOLUMN"));
	int succescol = Integer.parseInt(in.get("SUCCESSCOL"));

	@SuppressWarnings("boxing")
	public LinkedHashMap<String, String> readCheck(String log) {
		String[] len = log.split(" ");
		for (Map.Entry<String, String[]> entry : inM.entrySet()) {
			for (String s : entry.getValue()) {
				if (entry.getKey().equals("API") && in.get("ACTIONTYPE").equals("N")) {
					if (log.contains(s)) {
						apivalues.put(s + "Total", (apivalues.get(s + "Total") == null) ? BigInteger.ONE
								: apivalues.get(s + "Total").add(BigInteger.ONE));

						apivalues.put(s + "MAX",
								(BigInteger) ((apivalues.get(s + "MAX") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MAX")) == 1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + "MAX")));
						Maxlogs.put(s,
								(Maxlogs.get(s) == null) ? log
										: BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MAX")) == 0 ? log : Maxlogs.get(s));

						if (Timerlogs.get(s + "TMIN") == null) {
							Timerlogs.put(s + "TMIN", len[timer - 1]);
						}

						Timerlogs.put(s + "TMAX", len[timer - 1]);

						// Timerlogs.put(s + "TMAX",
						// (Timerlogs.get(s + "TMAX") == null) ? len[timer - 1]
						// : len[timer - 1]);

						avg.put(s, (avg.get(s) == null ? Long.parseLong(len[exetime - 1])
								: avg.get(s) + (Long.parseLong(len[exetime - 1]))));

						/*
						 * if(avg90.get(s)==null) { List<Long> ll = new
						 * LinkedList<Long>(); ll.add(Long.parseLong(len[exetime
						 * - 1])); avg90.put(s, (LinkedList<Long>) ll);
						 * 
						 * } else {
						 * 
						 * avg90.get(s).add(Long.parseLong(len[exetime - 1])); }
						 */

						apivalues.put(s + "MIN",
								(BigInteger) ((apivalues.get(s + "MIN") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MIN")) == -1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + "MIN")));

						apivalues.put(s + "SUCCESSHIT",
								(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + "SUCCESSHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + "SUCCESSHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + "SUCCESSHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + "SUCCESSHIT")));

						apivalues.put(s + "FAILUREHIT",
								!(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + "FAILUREHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + "FAILUREHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + "FAILUREHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + "FAILUREHIT")));

						all.put(s,
								apivalues.get(s + "Total") + "#" + apivalues.get(s + "MAX") + "#"
										+ apivalues.get(s + "MIN") + "#" + avg.get(s) + "#"
										+ apivalues.get(s + "SUCCESSHIT") + "#" + apivalues.get(s + "FAILUREHIT") + "#"
										+ Maxlogs.get(s) + "#" + Timerlogs.get("TMIN") + "#" + Timerlogs.get("TMAX"));

						break;

					}

				} else if (entry.getKey().equals("API") && in.get("ACTIONTYPE").equals("Y")
						&& !(Arrays.asList(inM.get("ACTION")).isEmpty())) {
					String ACTION = null;
					for (String st : Arrays.asList(inM.get("ACTION"))) {
						if (log.contains("action=" + st))
							ACTION = st;
					}

					if (log.contains(s) && ACTION != null && log.contains("action=" + ACTION) && s.equals("callback")) {

						apivalues.put(s + ACTION + "Total", (apivalues.get(s + ACTION + "Total") == null)
								? BigInteger.ONE : apivalues.get(s + ACTION + "Total").add(BigInteger.ONE));

						apivalues.put(s + ACTION + "MAX",
								(BigInteger) ((apivalues.get(s + ACTION + "MAX") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + ACTION + "MAX")) == 1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + ACTION + "MAX")));

						Maxlogs.put(s + ACTION,
								(Maxlogs.get(s + ACTION) == null) ? log
										: BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + ACTION + "MAX")) == 0 ? log
														: Maxlogs.get(s + ACTION));

						avg.put(s + ACTION, (avg.get(s + ACTION) == null ? Long.parseLong(len[exetime - 1])
								: avg.get(s + ACTION) + Long.parseLong(len[exetime - 1])));

						if (Timerlogs.get(s + ACTION + "TMIN") == null) {
							Timerlogs.put(s + ACTION + "TMIN", len[timer - 1]);
						}

						Timerlogs.put(s + ACTION + "TMAX", len[timer - 1]);

						apivalues.put(s + ACTION + "MIN",
								(BigInteger) ((apivalues.get(s + ACTION + "MIN") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + ACTION + "MIN")) == -1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + ACTION + "MIN")));

						apivalues.put(s + ACTION + "SUCCESSHIT",
								(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + ACTION + "SUCCESSHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + ACTION + "SUCCESSHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + ACTION + "SUCCESSHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + ACTION + "SUCCESSHIT")));

						apivalues.put(s + ACTION + "FAILUREHIT",
								!(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + ACTION + "FAILUREHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + ACTION + "FAILUREHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + ACTION + "FAILUREHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + ACTION + "FAILUREHIT")));

						all.put(s + "_" + ACTION, apivalues.get(s + ACTION + "Total") + "#"
								+ apivalues.get(s + ACTION + "MAX") + "#" + apivalues.get(s + ACTION + "MIN") + "#"
								+ avg.get(s + ACTION) + "#" + apivalues.get(s + ACTION + "SUCCESSHIT") + "#"
								+ apivalues.get(s + ACTION + "FAILUREHIT") + "#" + Maxlogs.get(s + ACTION) + "#"
								+ Timerlogs.get(s + ACTION + "TMIN") + "#" + Timerlogs.get(s + ACTION + "TMAX"));

						break;

					} else if (log.contains(s) && s != "callback") {

						apivalues.put(s + "Total", (apivalues.get(s + "Total") == null) ? BigInteger.ONE
								: apivalues.get(s + "Total").add(BigInteger.ONE));

						apivalues.put(s + "MAX",
								(BigInteger) ((apivalues.get(s + "MAX") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MAX")) == 1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + "MAX")));

						Maxlogs.put(s,
								(Maxlogs.get(s) == null) ? log
										: BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MAX")) == 0 ? log : Maxlogs.get(s));

						avg.put(s, (avg.get(s) == null ? Long.parseLong(len[exetime - 1])
								: avg.get(s) + Long.parseLong(len[exetime - 1])));

						if (Timerlogs.get(s + "TMIN") == null) {
							Timerlogs.put(s + "TMIN", len[timer - 1]);
						}

						Timerlogs.put(s + "TMAX", len[timer - 1]);

						apivalues.put(s + "MIN",
								(BigInteger) ((apivalues.get(s + "MIN") == null)
										? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
										: (BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
												.compareTo(apivalues.get(s + "MIN")) == -1)
														? BigInteger.valueOf(Long.parseLong(len[exetime - 1]))
														: apivalues.get(s + "MIN")));

						apivalues.put(s + "SUCCESSHIT",
								(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + "SUCCESSHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + "SUCCESSHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + "SUCCESSHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + "SUCCESSHIT")));

						apivalues.put(s + "FAILUREHIT",
								!(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
										? ((apivalues.get(s + "FAILUREHIT") == null) ? BigInteger.ONE
												: apivalues.get(s + "FAILUREHIT").add(BigInteger.ONE))
										: ((apivalues.get(s + "FAILUREHIT") == null) ? BigInteger.ZERO
												: apivalues.get(s + "FAILUREHIT")));

						all.put(s,
								apivalues.get(s + "Total") + "#" + apivalues.get(s + "MAX") + "#"
										+ apivalues.get(s + "MIN") + "#" + avg.get(s) + "#"
										+ apivalues.get(s + "SUCCESSHIT") + "#" + apivalues.get(s + "FAILUREHIT") + "#"
										+ Maxlogs.get(s) + "#" + Timerlogs.get("TMIN") + "#" + Timerlogs.get("TMAX"));

						break;

					}
				}
			}

		}

		count = count + 1L;
		apivalues.put("Total",
				(apivalues.get("Total") == null) ? BigInteger.ONE : apivalues.get("Total").add(BigInteger.ONE));

		apivalues.put("TMAX",
				(BigInteger) ((apivalues.get("TMAX") == null) ? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
						: (BigInteger.valueOf(Long.parseLong(len[exetime - 1])).compareTo(apivalues.get("TMAX")) == 1)
								? BigInteger.valueOf(Long.parseLong(len[exetime - 1])) : apivalues.get("TMAX")));

		avg.put("Total", (avg.get("Total") == null ? Long.parseLong(len[exetime - 1])
				: avg.get("Total") + Long.parseLong(len[exetime - 1])));

		Maxlogs.put("Total",
				(Maxlogs.get("Total") == null) ? log
						: BigInteger.valueOf(Long.parseLong(len[exetime - 1])).compareTo(apivalues.get("TMAX")) == 0
								? log : Maxlogs.get("Total"));

		if (Timerlogs.get("TMIN") == null) {
			Timerlogs.put("TMIN", len[timer - 1]);
		}

		Timerlogs.put("TMAX", len[timer - 1]);

		apivalues.put("TMIN",
				(BigInteger) ((apivalues.get("TMIN") == null) ? BigInteger.valueOf(Long.parseLong((len[(exetime - 1)])))
						: (BigInteger.valueOf(Long.parseLong(len[exetime - 1])).compareTo(apivalues.get("TMIN")) == -1)
								? BigInteger.valueOf(Long.parseLong(len[exetime - 1])) : apivalues.get("TMIN")));

		apivalues.put("TSUCCESSHIT",
				(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
						? ((apivalues.get("TSUCCESSHIT") == null) ? BigInteger.ONE
								: apivalues.get("TSUCCESSHIT").add(BigInteger.ONE))
						: ((apivalues.get("TSUCCESSHIT") == null) ? BigInteger.ZERO : apivalues.get("TSUCCESSHIT")));

		apivalues.put("TFAILUREHIT",
				!(Arrays.asList(inM.get("ERRORCODE")).contains(len[succescol - 1]))
						? ((apivalues.get("TFAILUREHIT") == null) ? BigInteger.ONE
								: apivalues.get("TFAILUREHIT").add(BigInteger.ONE))
						: ((apivalues.get("TFAILUREHIT") == null) ? BigInteger.ZERO : apivalues.get("TFAILUREHIT")));

		all.put("TOTAL",
				apivalues.get("Total") + "#" + apivalues.get("TMAX") + "#" + apivalues.get("TMIN") + "#"
						+ avg.get("Total") + "#" + apivalues.get("TSUCCESSHIT") + "#" + apivalues.get("TFAILUREHIT")
						+ "#" + Maxlogs.get("Total") + "#" + Timerlogs.get("TMIN") + "#" + Timerlogs.get("TMAX"));

		allCount.put("COUNT", count);

		return all;

	}

	public void csvLogs(long TotalTime, LinkedHashMap<String, String> allIn) {

		InputStream input;
		Properties prop = new Properties();
		String OUTDIR;

		dateValidator dtr = new dateValidator();

		SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date date = new Date();

		try {
			input = new FileInputStream("datafeed.properties");
			prop.load(input);
		} catch (IOException e) {
			e.getStackTrace();
		}

		String Format = in.get("TIME_FORMAT");
		String PRECISETPS = in.get("PRECISETPS");
		BufferedWriter bw = null;
		try {
			OUTDIR = prop.getProperty("OUTDIR");
			bw = new BufferedWriter(new FileWriter(OUTDIR + "Out" + dateformat.format(date) + ".csv"));
			String Total = null;
			System.out.println(
					"Info,TOTAL-HITS,Time_Duration(S),TPS,MAX_TIME(ms),MIN_TIME(ms),AVG_TIME(ms),SUCCESS,FAILURE,MAX_LOG");
			bw.write(
					"Info,TOTAL-HITS,Time_Duration(S),TPS,MAX_TIME(ms),MIN_TIME(ms),AVG_TIME(ms),SUCCESS,FAILURE,MAX_LOG"
							+ "\n");
			for (Map.Entry<String, String> entry : allIn.entrySet()) {

				String alt[] = entry.getValue().split("\\#");
				if (PRECISETPS.equals("Y")) {
					String t1 = dtr.datefromLog(alt[7], in);
					String t2 = dtr.datefromLog(alt[8], in);

					TotalTime = dtr.dateC(t1, t2, Format);
				}

				@SuppressWarnings("boxing")
				Double TPS = Double.parseDouble(alt[0]) / (TotalTime);

				@SuppressWarnings("boxing")
				Double AVG = Double.parseDouble(alt[3]) / Double.parseDouble(alt[0]);
				// Total Execution Time / Total No. of Hits
				if (entry.getKey().equals("TOTAL")) {
					Total = entry.getKey() + "," + BigInteger.valueOf(Long.parseLong(alt[0])) + "," + TotalTime + ","
							+ TPS + "," + alt[1] + "," + alt[2] + "," + AVG + "," + alt[4] + "," + alt[5] + ","
							+ alt[6];
					continue;
				}

				System.out.println(entry.getKey() + "," + BigInteger.valueOf(Long.parseLong(alt[0])) + "," + TotalTime
						+ "," + TPS + "," + alt[1] + "," + alt[2] + "," + AVG + "," + alt[3] + "," + alt[4] + ","
						+ alt[5] + "," + alt[6]);

				bw.write(entry.getKey() + "," + BigInteger.valueOf(Long.parseLong(alt[0])) + "," + TotalTime + "," + TPS
						+ "," + alt[1] + "," + alt[2] + "," + AVG + "," + alt[4] + "," + alt[5] + "," + alt[6] + "\n");
			}
			if (Total != null) {
				bw.write(Total);
				System.out.println(Total);
			} else {
				System.out.println("No Matching Entry Found For PARSED IP SET !!");
			}
		} catch (IOException IOE) {
			IOE.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
