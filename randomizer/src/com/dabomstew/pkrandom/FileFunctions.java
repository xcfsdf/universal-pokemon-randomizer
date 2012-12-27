package com.dabomstew.pkrandom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FileFunctions {

	private static final boolean internalConfig = false;

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
				&& filename.lastIndexOf('.') != filename.length() - 1) {
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
}
