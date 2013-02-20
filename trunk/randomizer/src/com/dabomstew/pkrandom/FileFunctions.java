package com.dabomstew.pkrandom;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FileFunctions {

	public static final boolean internalConfig = false;

	public static File fixFilename(File original, String defaultExtension) {
		return fixFilename(original, defaultExtension, null);
	}

	// Behavior:
	// if file has no extension, add defaultExtension
	// if there are banned extensions & file has a banned extension, replace
	// with defaultExtension
	// else, leave as is
	public static File fixFilename(File original, String defaultExtension,
			List<String> bannedExtensions) {
		String filename = original.getName();
		if (filename.lastIndexOf('.') >= filename.length() - 5
				&& filename.lastIndexOf('.') != filename.length() - 1
				&& filename.length() > 4 && filename.lastIndexOf('.') != -1) {
			// valid extension, read it off
			String ext = filename.substring(filename.lastIndexOf('.') + 1)
					.toLowerCase();
			if (bannedExtensions != null && bannedExtensions.contains(ext)) {
				// replace with default
				filename = filename.substring(0, filename.lastIndexOf('.') + 1)
						+ defaultExtension;
			}
			// else no change
		} else {
			// add extension
			filename += "." + defaultExtension;
		}
		return new File(original.getAbsolutePath().replace(original.getName(),
				"")
				+ filename);
	}

	public static boolean configExists(String filename) {
		if (internalConfig) {
			return FileFunctions.class
					.getResource("/com/dabomstew/pkrandom/config/" + filename) != null;
		} else {
			File fh = new File("./config/" + filename);
			return fh.exists() && fh.canRead();
		}
	}

	public static InputStream openConfig(String filename)
			throws FileNotFoundException {
		if (internalConfig) {
			return FileFunctions.class
					.getResourceAsStream("/com/dabomstew/pkrandom/config/"
							+ filename);
		} else {
			return new FileInputStream("./config/" + filename);
		}
	}

	public static byte[] getXPPatchFile(String filename) throws IOException {
		InputStream is = FileFunctions.class
				.getResourceAsStream("/com/dabomstew/pkrandom/xppatches/"
						+ filename);
		byte[] buf = new byte[is.available()];
		is.read(buf);
		is.close();
		return buf;
	}

	public static byte[] downloadFile(String url) throws IOException {
		BufferedInputStream in = new BufferedInputStream(
				new URL(url).openStream());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int count;
		while ((count = in.read(buf, 0, 1024)) != -1) {
			out.write(buf, 0, count);
		}
		in.close();
		byte[] output = out.toByteArray();
		return output;
	}
}
