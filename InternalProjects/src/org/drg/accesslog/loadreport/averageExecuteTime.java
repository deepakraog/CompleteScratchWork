/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

public class averageExecuteTime {

	public static Double avg90Percent(LinkedHashMap<String, String> avg90, String K) {
		Double Total = 0.0d;
		Set<String> keys = avg90.keySet();
		for (String s : keys) {
			if (s.contains(K)) {
				String[] str = avg90.get(K).split(",");

				for (int i = 0; i < str.length * (0.9); i++) {
					Total += Integer.parseInt(str[i]);
				}

			}

		}
		return Total;
	}

	public static String TotalValue(LinkedList<Double> avgT, String Total, LinkedHashMap<String, String> avg90) {
		String Temp = "";
		Double T = 0.0;
		for (Double d : avgT) {
			T += d;
		}
		T = T / avg90.size();
		String[] s = Total.split(",");
		for (int i = 0; i < s.length; i++) {
			if (i == 7) {
				Temp += T + ",";
			} else {

				Temp += s[i] + ",";
			}

		}
		return Temp;
	}
}