/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Properties;

public class inputFetch {

	static InputStream input;
	static Properties prop;
	static {
		prop = new Properties();
		try {
			input = new FileInputStream("datafeed.properties");
			prop.load(input);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	public static LinkedHashMap<String, String> inputValues() {
		LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();

		params.put("LOADIP", prop.getProperty("LOADIP"));
		params.put("LOGPATH", prop.getProperty("LOGPATH"));
		params.put("ERRORPATH", prop.getProperty("ERRORPATH"));
		params.put("DATECOLUMN", prop.getProperty("DATECOLUMN"));
		params.put("EXECOLUMN", prop.getProperty("EXECOLUMN"));
		params.put("SUCCESSCOL", prop.getProperty("SUCCESSCOL"));
		params.put("ACTIONTYPE", prop.getProperty("ACTIONTYPE"));
		params.put("DAIUSLOG", prop.getProperty("DAIUSLOG"));
		params.put("TIME_FORMAT", prop.getProperty("TIME_FORMAT"));
		params.put("CAN_GZIP", prop.getProperty("CAN_GZIP"));
		params.put("DELETE_UNZIP_FILES", prop.getProperty("DELETE_UNZIP_FILES"));
		params.put("PRECISETPS", prop.getProperty("PRECISETPS"));

		return params;
	}

	public static LinkedHashMap<String, String[]> multipleValues() {
		LinkedHashMap<String, String[]> pArray = new LinkedHashMap<String, String[]>();

		String[] API = null, ACTION = null, ERRORCODE = null;

		API = (prop.getProperty("API").isEmpty() ? API : (String[]) prop.getProperty("API").split(","));
		ACTION = (prop.getProperty("ACTION").isEmpty() ? ACTION : (String[]) prop.getProperty("ACTION").split(","));
		ERRORCODE = (prop.getProperty("ERRORCODE").isEmpty() ? ERRORCODE
				: (String[]) prop.getProperty("ERRORCODE").split(","));

		try {
			if (API.length > 0 && (API != null))
				pArray.put("API", API);

			if (ERRORCODE.length > 0 && (ERRORCODE != null))
				pArray.put("ERRORCODE", ERRORCODE);

			if ((ACTION.length > 0 && (ACTION != null)))
				pArray.put("ACTION", ACTION);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return pArray;
	}

}
