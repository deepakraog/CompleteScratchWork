package com.drg.Model;

import java.util.LinkedHashMap;

public class ApiValidator {

	public static String methodReplace(LinkedHashMap<String, String> ll, String queryString) {

		String service_id[] = ll.get("service_id").split(",");

		if (queryString.contains("method=" + ll.get("methodReference"))) {
			for (int i = 0; i < service_id.length; i++) {
				if (queryString.contains("service_id=" + service_id[i])) {
					queryString = queryString.replace("method=" + ll.get("methodReference"),
							"method=" + ll.get("methodToReplace"));
					break;
				}
			}
		}
		return queryString;
	}

	public static String urlForm(LinkedHashMap<String, String> lhm, String replacedURL) {
		String hit2PostURL = null;
		if (!lhm.isEmpty() && !replacedURL.isEmpty()) {
			hit2PostURL = "http://" + lhm.get("ip") + ":" + lhm.get("port") + "" + lhm.get("urlD") + replacedURL;
		}
		return hit2PostURL;
	}

}
