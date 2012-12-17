package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  DSFunctions.java - bootstraps the ported code to extract/create .nds. --*/
/*--  Code derived from "Nintendo DS rom tool", copyright (C) DevkitPro     --*/
/*--  Original Code by Rafael Vuijk, Dave Murphy, Alexei Karpenko			--*/
/*--																		--*/
/*--  Ported to Java by Dabomstew under the terms of the GPL:				--*/
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

import java.io.IOException;
import java.io.RandomAccessFile;

public class DSFunctions {

	public RandomAccessFile fNDS;
	public Header header;
	public String ndsfilename;
	public String overlaydir;
	public String filerootdir;
	public String headerfilename;
	public String arm9filename;
	public String arm7filename;
	public String bannerfilename;
	public String arm9ovltablefilename;
	public String arm7ovltablefilename;

	public static boolean verboseMode = false;

	public static void extract(String filename, String dataFolder)
			throws IOException {
		new DSFunctions("-x", filename, dataFolder);
	}

	public static void create(String filename, String dataFolder)
			throws IOException {
		new DSFunctions("-c", filename, dataFolder);
	}

	public DSFunctions(String... args) throws IOException {
		ndsfilename = args[1];
		String dataFolder = args[2] + "/";
		overlaydir = dataFolder + "overlay";
		filerootdir = dataFolder + "root";
		arm9filename = dataFolder + "arm9.bin";
		arm7filename = dataFolder + "arm7.bin";
		bannerfilename = dataFolder + "banner.bin";
		headerfilename = dataFolder + "header.bin";
		arm9ovltablefilename = dataFolder + "overarm9.bin";
		arm7ovltablefilename = dataFolder + "overarm7.bin";
		if (args[0].equals("-x")) {

			fNDS = new RandomAccessFile(args[1], "r");
			header = getStruct(new Header());

			Extract ex = new Extract(this);

			ex.ExtractMethod(arm9filename, true, 0x20, true, 0x2C, true);
			ex.ExtractMethod(arm7filename, true, 0x30, true, 0x3C);
			ex.ExtractMethod(bannerfilename, true, 0x68, false, 0x840);
			ex.ExtractMethod(headerfilename, false, 0, false, 0x200);
			ex.ExtractMethod(arm9ovltablefilename, true, 0x50, true, 0x54);
			ex.ExtractMethod(arm7ovltablefilename, true, 0x58, true, 0x5C);
			ex.ExtractOverlayFiles();
			ex.ExtractFiles(ndsfilename);
		} else {
			Create cr = new Create(this);
			cr.CreateMethod();
		}
	}

	public <T extends NDSStruct> T getStruct(T base) throws IOException {
		byte[] raw = new byte[base.size()];
		fNDS.read(raw);
		base.populate(raw);
		return base;
	}
}
