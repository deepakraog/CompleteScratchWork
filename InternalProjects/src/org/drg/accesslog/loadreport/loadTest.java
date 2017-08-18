/**
 * @author deepak.gaikwad
 *
 * ${1.0}
 */

package org.drg.accesslog.loadreport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class loadTest {

	public static void main(String[] args) throws IOException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date dateS = new Date();

		System.out.println("Program Execution Started " + dateFormat.format(dateS));
		readLogs rl = new readLogs();
		rl.compress();
		rl.read();
		Date dateF = new Date();
		System.out.println("Program Execution Finished " + dateFormat.format(dateF));
	}
}