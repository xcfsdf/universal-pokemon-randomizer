package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  AbstractDSRomHandler.java - a base class for DS rom handlers			--*/
/*--                              which standardises common DS functions.	--*/
/*--  																		--*/
/*--  Part of "Universal Pokemon Randomizer" by Dabomstew					--*/
/*--  Pokemon and any associated names and the like are						--*/
/*--  trademark and (C) Nintendo 1996-2012.									--*/
/*--  																		--*/
/*--  The custom code written here is licensed under the terms of the GPL:	--*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dabomstew.pkrandom.RomFunctions;
import com.dabomstew.pkrandom.pokemon.Type;

import cuecompressors.BLZCoder;
import dsfunctions.DSFunctions;

public abstract class AbstractDSRomHandler extends AbstractRomHandler {

	protected String dataFolder;

	private boolean sigMode;
	private boolean zeroSigMode;
	private int szOffset;
	private int szMode = 0;
	private boolean compressFlag;

	@Override
	public boolean detectRom(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			fis.skip(0x0C);
			byte[] sig = new byte[4];
			fis.read(sig);
			fis.close();
			String ndsCode = new String(sig, "US-ASCII");
			return detectNDSRom(ndsCode);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	protected abstract boolean detectNDSRom(String ndsCode);

	private static final byte[] arm9sig = new byte[] { 0x21, 0x06, (byte) 0xC0,
			(byte) 0xDE, (byte) 0xA0, 0x0B, 0, 0, 0, 0, 0, 0 };
	private static final byte[] arm90sig = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0 };

	@Override
	public boolean loadRom(String filename) {
		if (!detectRom(filename)) {
			return false;
		}
		// Make our data folder
		File f = new File(filename);
		String fn = f.getName();
		dataFolder = "tmp_" + fn.substring(0, fn.lastIndexOf('.'));
		// remove nonsensical chars
		dataFolder = dataFolder.replaceAll("[^A-Za-z0-9_]+", "");
		File df = new File("./" + dataFolder);
		df.mkdir();
		try {
			DSFunctions.extract(filename, dataFolder);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			byte[] arm9 = readFile("arm9.bin");
			sigMode = false;
			int arm9length = arm9.length;
			// Look for typical signature
			if (sigtail(arm9, arm9.length - arm9sig.length, arm9sig)) {
				sigMode = true;
				arm9length -= arm9sig.length;
			}
			// Look for zeroes signature
			if (sigtail(arm9, arm9.length - arm90sig.length, arm90sig)
					&& arm9[arm9length - 17] >= 0x08
					&& arm9[arm9length - 17] <= 0x0B) {
				zeroSigMode = true;
				arm9length -= arm90sig.length;
			}

			compressFlag = false;
			szOffset = 0;
			if (((int) arm9[arm9length - 5]) >= 0x08
					&& ((int) arm9[arm9length - 5]) <= 0x0B) {
				int compSize = read3byte(arm9, arm9length - 8);
				if (compSize > (arm9length * 9 / 10)
						&& compSize < (arm9length * 11 / 10)) {
					compressFlag = true;
					byte[] compLength = get3byte(arm9length);
					List<Integer> foundOffsets = RomFunctions.search(arm9,
							compLength);
					if (foundOffsets.size() == 1) {
						szMode = 1;
						szOffset = foundOffsets.get(0);
					} else {
						byte[] compLength2 = get3byte(arm9length + 0x4000);
						List<Integer> foundOffsets2 = RomFunctions.search(arm9,
								compLength2);
						if (foundOffsets2.size() == 1) {
							szMode = 2;
							szOffset = foundOffsets2.get(0);
						} else {
						}
					}
				}
			}

			// Save?
			if (arm9length != arm9.length) {
				writeFile("arm9.bin", arm9, 0, arm9length);
			}

			// Compression?
			if (compressFlag) {
				BLZCoder.main(new String[] { "-d", dataFolder + "/arm9.bin" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadedROM();
		return true;
	}

	private int read3byte(byte[] arm9, int offset) {
		return (arm9[offset] & 0xFF) | ((arm9[offset + 1] & 0xFF) << 8)
				| ((arm9[offset + 2] & 0xFF) << 16);
	}

	private byte[] get3byte(int amount) {
		byte[] ret = new byte[3];
		ret[0] = (byte) (amount & 0xFF);
		ret[1] = (byte) ((amount >> 8) & 0xFF);
		ret[2] = (byte) ((amount >> 16) & 0xFF);
		return ret;
	}

	private boolean sigtail(byte[] arm9, int offset, byte[] sig) {
		for (int i = 0; i < sig.length; i++) {
			if (arm9[offset + i] != sig[i]) {
				return false;
			}
		}
		return true;
	}

	protected abstract void loadedROM();

	protected abstract void savingROM();

	@Override
	public boolean saveRom(String filename) {
		savingROM();
		// Recompress ARM9
		try {
			if (compressFlag) {
				BLZCoder.main(new String[] { "-en9", dataFolder + "/arm9.bin" });
			}
			byte[] arm9 = readFile("arm9.bin");
			boolean madeChanges = false;
			if (compressFlag && szOffset > 0) {
				madeChanges = true;
				int newValue = szMode == 1 ? arm9.length : arm9.length + 0x4000;
				byte[] newCompLength = get3byte(newValue);
				System.arraycopy(newCompLength, 0, arm9, szOffset, 3);
			}
			if (sigMode) {
				madeChanges = true;
				byte[] newarm9 = new byte[arm9.length + arm9sig.length];
				System.arraycopy(arm9, 0, newarm9, 0, arm9.length);
				System.arraycopy(arm9sig, 0, newarm9, arm9.length,
						arm9sig.length);
				arm9 = newarm9;
			}

			if (zeroSigMode) {
				madeChanges = true;
				byte[] newarm9 = new byte[arm9.length + arm90sig.length];
				System.arraycopy(arm9, 0, newarm9, 0, arm9.length);
				System.arraycopy(arm90sig, 0, newarm9, arm9.length,
						arm90sig.length);
				arm9 = newarm9;
			}

			if (madeChanges) {
				writeFile("arm9.bin", arm9);
			}
		} catch (Exception e) {
			return false;
		}
		try {
			DSFunctions.create(filename, dataFolder);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canChangeStaticPokemon() {
		return false;
	}

	public List<String> runProgram(String command) throws IOException,
			InterruptedException {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);

		BufferedReader input = new BufferedReader(new InputStreamReader(
				pr.getInputStream()));
		String line = null;
		List<String> returned = new ArrayList<String>();

		while ((line = input.readLine()) != null) {
			returned.add(line);
		}

		pr.waitFor();
		return returned;
	}

	public NARCContents readNARC(String subpath) throws IOException {
		File fh = new File("./" + dataFolder + "/root/" + subpath);
		if (!fh.exists() || !fh.canRead() || !fh.isFile()) {
			return null;
		}
		Map<String, byte[]> frames = readNitroFrames("./" + dataFolder
				+ "/root/" + subpath);
		if (!frames.containsKey("FATB") || !frames.containsKey("FNTB")
				|| !frames.containsKey("FIMG")) {
			System.err.println("Not a valid narc file");
			return null;
		}
		// File contents
		NARCContents narc = new NARCContents();
		byte[] fatbframe = frames.get("FATB");
		byte[] fimgframe = frames.get("FIMG");
		int fileCount = readLong(fatbframe, 0);
		for (int i = 0; i < fileCount; i++) {
			int startOffset = readLong(fatbframe, 4 + i * 8);
			int endOffset = readLong(fatbframe, 8 + i * 8);
			int length = (endOffset - startOffset);
			byte[] thisFile = new byte[length];
			try {
				System.arraycopy(fimgframe, startOffset, thisFile, 0, length);
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.out.printf(
						"AIOBEX: start %d length %d size of frame %d\n",
						startOffset, length, fimgframe.length);
			}
			narc.files.add(thisFile);
		}
		// Filenames?
		byte[] fntbframe = frames.get("FNTB");
		int unk1 = readLong(fntbframe, 0);
		if (unk1 == 8) {
			// Filenames exist
			narc.hasFilenames = true;
			int offset = 8;
			for (int i = 0; i < fileCount; i++) {
				int fnLength = (fntbframe[offset] & 0xFF);
				offset++;
				byte[] filenameBA = new byte[fnLength];
				System.arraycopy(fntbframe, offset, filenameBA, 0, fnLength);
				String filename = new String(filenameBA, "US-ASCII");
				narc.filenames.add(filename);
			}
		} else {
			narc.hasFilenames = false;
			for (int i = 0; i < fileCount; i++) {
				narc.filenames.add(null);
			}
		}
		return narc;
	}

	public void writeNARC(String subpath, NARCContents narc) throws IOException {
		File fh = new File("./" + dataFolder + "/root/" + subpath);
		// Get bytes required for FIMG frame
		int bytesRequired = 0;
		for (byte[] file : narc.files) {
			bytesRequired += Math.ceil(file.length / 4.0) * 4;
		}
		// FIMG frame & FATB frame build

		// 4 for numentries, 8*size for entries, 8 for nitro header
		byte[] fatbFrame = new byte[4 + narc.files.size() * 8 + 8];
		// bytesRequired + 8 for nitro header
		byte[] fimgFrame = new byte[bytesRequired + 8];

		// Nitro headers
		fatbFrame[0] = 'B';
		fatbFrame[1] = 'T';
		fatbFrame[2] = 'A';
		fatbFrame[3] = 'F';
		writeLong(fatbFrame, 4, fatbFrame.length);

		fimgFrame[0] = 'G';
		fimgFrame[1] = 'M';
		fimgFrame[2] = 'I';
		fimgFrame[3] = 'F';
		writeLong(fimgFrame, 4, fimgFrame.length);
		int offset = 0;

		writeLong(fatbFrame, 8, narc.files.size());
		for (int i = 0; i < narc.files.size(); i++) {
			byte[] file = narc.files.get(i);
			int bytesRequiredForFile = (int) (Math.ceil(file.length / 4.0) * 4);
			System.arraycopy(file, 0, fimgFrame, offset + 8, file.length);
			for (int filler = file.length; filler < bytesRequiredForFile; filler++) {
				fimgFrame[offset + 8 + filler] = (byte) 0xFF;
			}
			writeLong(fatbFrame, 12 + i * 8, offset);
			writeLong(fatbFrame, 16 + i * 8, offset + file.length);
			offset += bytesRequiredForFile;
		}

		// FNTB Frame
		int bytesForFNTBFrame = 16;
		if (narc.hasFilenames) {
			for (String filename : narc.filenames) {
				bytesForFNTBFrame += filename.getBytes("US-ASCII").length + 1;
			}
		}
		byte[] fntbFrame = new byte[bytesForFNTBFrame];

		fntbFrame[0] = 'B';
		fntbFrame[1] = 'T';
		fntbFrame[2] = 'N';
		fntbFrame[3] = 'F';
		writeLong(fntbFrame, 4, fntbFrame.length);

		if (narc.hasFilenames) {
			writeLong(fntbFrame, 8, 8);
			writeLong(fntbFrame, 12, 0x10000);
			int fntbOffset = 16;
			for (String filename : narc.filenames) {
				byte[] fntbfilename = filename.getBytes("US-ASCII");
				fntbFrame[fntbOffset] = (byte) fntbfilename.length;
				System.arraycopy(fntbfilename, 0, fntbFrame, fntbOffset + 1,
						fntbfilename.length);
				fntbOffset += 1 + fntbfilename.length;
			}
		} else {
			writeLong(fntbFrame, 8, 4);
			writeLong(fntbFrame, 12, 0x10000);
		}

		// Now for the actual Nitro file
		int nitrolength = 16 + fatbFrame.length + fntbFrame.length
				+ fimgFrame.length;
		byte[] nitroFile = new byte[nitrolength];
		nitroFile[0] = 'N';
		nitroFile[1] = 'A';
		nitroFile[2] = 'R';
		nitroFile[3] = 'C';
		writeWord(nitroFile, 4, 0xFFFE);
		writeWord(nitroFile, 6, 0x0100);
		writeLong(nitroFile, 8, nitrolength);
		writeWord(nitroFile, 12, 0x10);
		writeWord(nitroFile, 14, 3);
		System.arraycopy(fatbFrame, 0, nitroFile, 16, fatbFrame.length);
		System.arraycopy(fntbFrame, 0, nitroFile, 16 + fatbFrame.length,
				fntbFrame.length);
		System.arraycopy(fimgFrame, 0, nitroFile, 16 + fatbFrame.length
				+ fntbFrame.length, fimgFrame.length);
		FileOutputStream fos = new FileOutputStream(fh);
		fos.write(nitroFile);
		fos.close();
	}

	private Map<String, byte[]> readNitroFrames(String filename)
			throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		byte[] wholeFile = new byte[fis.available()];
		fis.read(wholeFile);
		fis.close();

		// Read the number of frames
		int frameCount = readWord(wholeFile, 0x0E);

		// each frame
		int offset = 0x10;
		Map<String, byte[]> frames = new TreeMap<String, byte[]>();
		for (int i = 0; i < frameCount; i++) {
			byte[] magic = new byte[] { wholeFile[offset + 3],
					wholeFile[offset + 2], wholeFile[offset + 1],
					wholeFile[offset] };
			String magicS = new String(magic, "US-ASCII");
			int frame_size = readLong(wholeFile, offset + 4);
			byte[] frame = new byte[frame_size - 8];
			System.arraycopy(wholeFile, offset + 8, frame, 0, frame_size - 8);
			frames.put(magicS, frame);
			offset += frame_size;
		}
		return frames;
	}

	protected int readWord(byte[] data, int offset) {
		return (data[offset] & 0xFF) + ((data[offset + 1] & 0xFF) << 8);
	}

	protected int readLong(byte[] data, int offset) {
		return (data[offset] & 0xFF) + ((data[offset + 1] & 0xFF) << 8)
				+ ((data[offset + 2] & 0xFF) << 16)
				+ ((data[offset + 3] & 0xFF) << 24);
	}

	protected void writeWord(byte[] data, int offset, int value) {
		data[offset] = (byte) (value & 0xFF);
		data[offset + 1] = (byte) ((value >> 8) & 0xFF);
	}

	protected void writeLong(byte[] data, int offset, int value) {
		data[offset] = (byte) (value & 0xFF);
		data[offset + 1] = (byte) ((value >> 8) & 0xFF);
		data[offset + 2] = (byte) ((value >> 16) & 0xFF);
		data[offset + 3] = (byte) ((value >> 24) & 0xFF);
	}

	protected byte[] readFile(String location) throws IOException {
		FileInputStream fis = new FileInputStream("./" + dataFolder + "/"
				+ location);
		byte[] file = new byte[fis.available()];
		fis.read(file);
		fis.close();
		return file;
	}

	protected void writeFile(String location, byte[] data) throws IOException {
		writeFile(location, data, 0, data.length);
	}

	protected void writeFile(String location, byte[] data, int offset,
			int length) throws IOException {
		FileOutputStream fos = new FileOutputStream("./" + dataFolder + "/"
				+ location);
		fos.write(data, offset, length);
		fos.close();
	}

	protected void readByteIntoFlags(byte[] data, boolean[] flags,
			int offsetIntoFlags, int offsetIntoData) {
		int thisByte = data[offsetIntoData] & 0xFF;
		for (int i = 0; i < 8 && (i + offsetIntoFlags) < flags.length; i++) {
			flags[offsetIntoFlags + i] = ((thisByte >> i) & 0x01) == 0x01;
		}
	}

	protected byte getByteFromFlags(boolean[] flags, int offsetIntoFlags) {
		int thisByte = 0;
		for (int i = 0; i < 8 && (i + offsetIntoFlags) < flags.length; i++) {
			thisByte |= (flags[offsetIntoFlags + i] ? 1 : 0) << i;
		}
		return (byte) thisByte;
	}

	protected int typeTMPaletteNumber(Type t) {
		switch (t) {
		case FIGHTING:
			return 398;
		case DRAGON:
			return 399;
		case WATER:
			return 400;
		case PSYCHIC:
			return 401;
		case NORMAL:
			return 402;
		case POISON:
			return 403;
		case ICE:
			return 404;
		case GRASS:
			return 405;
		case FIRE:
			return 406;
		case DARK:
			return 407;
		case STEEL:
			return 408;
		case ELECTRIC:
			return 409;
		case GROUND:
			return 410;
		case GHOST:
		default:
			return 411; // for CURSE
		case ROCK:
			return 412;
		case FLYING:
			return 413;
		case BUG:
			return 610;
		}
	}

}
