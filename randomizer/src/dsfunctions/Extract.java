package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  Extract.java - extracts a NDS rom into individual files           	--*/
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Extract {
	private DSFunctions parent;

	public Extract(DSFunctions parent) {
		this.parent = parent;
	}

	public void ExtractMethod(String outfilename, boolean indirect_offset,
			int offset, boolean indirect_size, int size) throws IOException {
		ExtractMethod(outfilename, indirect_offset, offset, indirect_size,
				size, false);
	}

	public void ExtractMethod(String outfilename, boolean indirect_offset,
			int offset, boolean indirect_size, int size, boolean with_footer)
			throws IOException {
		parent.fNDS = new RandomAccessFile(parent.ndsfilename, "r");
		parent.header = parent.getStruct(new Header());

		if (indirect_offset) {
			offset = parent.header.grabInt(offset);
		}
		if (indirect_size) {
			size = parent.header.grabInt(size);
		}

		parent.fNDS.seek(offset);

		FileOutputStream fos = new FileOutputStream(outfilename);

		byte[] copybuf = new byte[1024];
		while (size > 0) {
			int size2 = (size >= 1024) ? 1024 : size;
			parent.fNDS.read(copybuf, 0, size2);
			fos.write(copybuf, 0, size2);
			size -= size2;
		}

		if (with_footer) {
			byte[] nitrocodeR = new byte[4];
			parent.fNDS.read(nitrocodeR);
			int nitrocode = toInt(nitrocodeR);
			if (DSFunctions.verboseMode)
				System.out.println(nitrocode);
			if (nitrocode == 0xDEC00621) {
				// 0x2106C0DE, version info, reserved?
				for (int i = 0; i < 3; i++) {
					fos.write(nitrocodeR);
					parent.fNDS.read(nitrocodeR);
				}
			}
		}

		fos.close();
		parent.fNDS.close();
	}

	private int toInt(byte[] data) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(data, 0, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	private int readInt() throws IOException {
		byte[] buf = new byte[4];
		parent.fNDS.read(buf);
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(buf, 0, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	private int readUnsignedShort() throws IOException {
		byte[] buf = new byte[2];
		parent.fNDS.read(buf);
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(buf, 0, 2);
		b.put((byte) 0);
		b.put((byte) 0);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	public void ExtractOverlayFiles() throws IOException {
		parent.fNDS = new RandomAccessFile(parent.ndsfilename, "r");
		parent.header = parent.getStruct(new Header());

		MkDir(parent.overlaydir);

		ExtractOverlayFiles2(parent.header.arm9_overlay_offset,
				parent.header.arm9_overlay_size);
		ExtractOverlayFiles2(parent.header.arm7_overlay_offset,
				parent.header.arm7_overlay_size);

		parent.fNDS.close();
	}

	public void ExtractOverlayFiles2(int overlay_offset, int overlay_size)
			throws IOException {
		OverlayEntry overlayEntry;
		if (overlay_size > 0) {
			parent.fNDS.seek(overlay_offset);
			for (int i = 0; i < overlay_size; i += 0x20) {
				overlayEntry = parent.getStruct(new OverlayEntry());
				int file_id = overlayEntry.id;
				String s = String.format(OverlayEntry.OVERLAY_FMT, file_id);
				ExtractFile(parent.overlaydir, "/", s, file_id);
			}
		}
	}

	public void ExtractFiles(String ndsfilename) throws IOException {
		parent.fNDS = new RandomAccessFile(ndsfilename, "r");
		parent.header = parent.getStruct(new Header());

		MkDir(parent.filerootdir);

		ExtractDirectory("/", 0xF000);

		parent.fNDS.close();
	}

	public void ExtractDirectory(String prefix, int dir_id) throws IOException {
		long save_filepos = parent.fNDS.getFilePointer();
		String strbuf = "";

		parent.fNDS.seek(parent.header.fnt_offset + 8 * (dir_id & 0xFFF));
		int entry_start = readInt();
		int top_file_id = readUnsignedShort();
		readUnsignedShort();

		parent.fNDS.seek(parent.header.fnt_offset + entry_start);
		if (DSFunctions.verboseMode)
			System.out.printf("%s\n", prefix);

		for (int file_id = top_file_id;; file_id++) {
			int entry_type_name_length = parent.fNDS.read();
			int name_length = entry_type_name_length & 0x7F;
			boolean entry_type_directory = (entry_type_name_length & 0x80) == 0x80;
			if (name_length == 0) {
				break;
			}

			byte[] entry_nameBA = new byte[name_length];
			parent.fNDS.read(entry_nameBA);
			String entry_name = new String(entry_nameBA, "US-ASCII");
			if (entry_type_directory) {
				int new_dir_id = readUnsignedShort();

				strbuf = parent.filerootdir + prefix + entry_name;
				MkDir(strbuf);

				strbuf = prefix + entry_name + "/";
				ExtractDirectory(strbuf, new_dir_id);
			} else {
				ExtractFile(parent.filerootdir, prefix, entry_name, file_id);
			}
		}

		parent.fNDS.seek(save_filepos);
	}

	public void ExtractFile(String rootdir, String prefix, String entry_name,
			int file_id) throws IOException {
		long save_filepos = parent.fNDS.getFilePointer();

		parent.fNDS.seek(parent.header.fat_offset + 8 * file_id);
		int top = readInt();
		int bottom = readInt();
		int size = bottom - top;

		if (size > (1 << (17 + parent.header.devicecap))) {
			System.err
					.printf("File %d: Size is too big. FAT offset 0x%X contains invalid data.\n",
							file_id, parent.header.fat_offset + 8 * file_id);
			System.exit(1);
			return;
		}
		if (DSFunctions.verboseMode)
			System.out.printf("%5d 0x%08X 0x%08X %9d %s%s\n", file_id, top,
					bottom, size, prefix, entry_name);

		// Extract
		String filename = rootdir + prefix + entry_name;

		parent.fNDS.seek(top);

		FileOutputStream fos = new FileOutputStream(filename);
		byte[] copybuf = new byte[1024];
		while (size > 0) {
			int size2 = (size >= 1024) ? 1024 : size;
			parent.fNDS.read(copybuf, 0, size2);
			fos.write(copybuf, 0, size2);
			size -= size2;
		}
		fos.close();

		parent.fNDS.seek(save_filepos);
	}

	public void MkDir(String name) {
		new File(name).mkdir();
	}

}
