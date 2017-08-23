/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

public class dateValidator {

	public long dateC(String time1, String time2, String Format) {
		Date date1 = null, date2 = null;
		SimpleDateFormat format = new SimpleDateFormat(Format);
		try {
			date1 = format.parse(time1);
			date2 = format.parse(time2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long difference = date2.getTime() - date1.getTime();
		return difference / 1000;
	}

	public String datefromLog(String log, LinkedHashMap<String, String> in) {

		String DateS[] = log.split(" ");
		String time1 = null;
		String DAIUSLOG = in.get("DAIUSLOG"), DATECOLUMN = in.get("DATECOLUMN");
		
		if (DAIUSLOG.equals("N")) {
			String dtS[] = DateS[0].split("\\[");
			time1 = dtS[1];
		} else if (DAIUSLOG.equals("Y")) {
			time1 = DateS[Integer.parseInt(DATECOLUMN) - 1] + " " + DateS[Integer.parseInt(DATECOLUMN)];
		}

		return time1;

	}

}
