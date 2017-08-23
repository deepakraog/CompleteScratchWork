/**
 * 
 */

/**
 * @author deepak.gaikwad
 *
 */
package org.drg.accesslog.loadreport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Gzip {

	public File gzipFile(String source_filepath, String destinaton_zip_filepath, String dELETE_UNZIP_FILES) {

		File fileIn = new File(source_filepath);
		File fileOut = new File(destinaton_zip_filepath);
		byte[] buffer = new byte[8192];
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(destinaton_zip_filepath);
			GZIPOutputStream gzipOuputStream = new GZIPOutputStream(fileOutputStream);

			FileInputStream fileInput = new FileInputStream(source_filepath);

			int bytes_read;

			while ((bytes_read = fileInput.read(buffer)) > 0) {
				gzipOuputStream.write(buffer, 0, bytes_read);
			}

			fileInput.close();
			gzipOuputStream.finish();
			gzipOuputStream.close();

			if (dELETE_UNZIP_FILES.equals("Y")) {
				fileIn.delete();
			}
			System.out.println("Compression Done!! O/P file :: " + fileOut.getAbsolutePath());

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return fileOut;
	}

}