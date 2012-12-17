package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  Create.java - creates NDS roms from extracted/changed data            --*/
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Create {
	public int arm9_align = 0x1FF, arm7_align = 0x1FF;
	public int fnt_align = 0x1FF, fat_align = 0x1FF;
	public int banner_align = 0x1FF, file_align = 0x1FF;

	public int overlay_files = 0;

	private NDSTree tree = new NDSTree();

	public byte[] romcontrol = new byte[] { 0x00, 0x60, 0x58, 0x00,
			(byte) 0xF8, 0x08, 0x18, 0x00 };

	public static final byte[] nintendo_logo = new byte[] { 0x24, (byte) 0xFF,
			(byte) 0xAE, 0x51, 0x69, (byte) 0x9A, (byte) 0xA2, 0x21, 0x3D,
			(byte) 0x84, (byte) 0x82, 0x0A, (byte) 0x84, (byte) 0xE4, 0x09,
			(byte) 0xAD, 0x11, 0x24, (byte) 0x8B, (byte) 0x98, (byte) 0xC0,
			(byte) 0x81, 0x7F, 0x21, (byte) 0xA3, 0x52, (byte) 0xBE, 0x19,
			(byte) 0x93, 0x09, (byte) 0xCE, 0x20, 0x10, 0x46, 0x4A, 0x4A,
			(byte) 0xF8, 0x27, 0x31, (byte) 0xEC, 0x58, (byte) 0xC7,
			(byte) 0xE8, 0x33, (byte) 0x82, (byte) 0xE3, (byte) 0xCE,
			(byte) 0xBF, (byte) 0x85, (byte) 0xF4, (byte) 0xDF, (byte) 0x94,
			(byte) 0xCE, 0x4B, 0x09, (byte) 0xC1, (byte) 0x94, 0x56,
			(byte) 0x8A, (byte) 0xC0, 0x13, 0x72, (byte) 0xA7, (byte) 0xFC,
			(byte) 0x9F, (byte) 0x84, 0x4D, 0x73, (byte) 0xA3, (byte) 0xCA,
			(byte) 0x9A, 0x61, 0x58, (byte) 0x97, (byte) 0xA3, 0x27,
			(byte) 0xFC, 0x03, (byte) 0x98, 0x76, 0x23, 0x1D, (byte) 0xC7,
			0x61, 0x03, 0x04, (byte) 0xAE, 0x56, (byte) 0xBF, 0x38,
			(byte) 0x84, 0x00, 0x40, (byte) 0xA7, 0x0E, (byte) 0xFD,
			(byte) 0xFF, 0x52, (byte) 0xFE, 0x03, 0x6F, (byte) 0x95, 0x30,
			(byte) 0xF1, (byte) 0x97, (byte) 0xFB, (byte) 0xC0, (byte) 0x85,
			0x60, (byte) 0xD6, (byte) 0x80, 0x25, (byte) 0xA9, 0x63,
			(byte) 0xBE, 0x03, 0x01, 0x4E, 0x38, (byte) 0xE2, (byte) 0xF9,
			(byte) 0xA2, 0x34, (byte) 0xFF, (byte) 0xBB, 0x3E, 0x03, 0x44,
			0x78, 0x00, (byte) 0x90, (byte) 0xCB, (byte) 0x88, 0x11, 0x3A,
			(byte) 0x94, 0x65, (byte) 0xC0, 0x7C, 0x63, (byte) 0x87,
			(byte) 0xF0, 0x3C, (byte) 0xAF, (byte) 0xD6, 0x25, (byte) 0xE4,
			(byte) 0x8B, 0x38, 0x0A, (byte) 0xAC, 0x72, 0x21, (byte) 0xD4,
			(byte) 0xF8, 0x07, };

	private DSFunctions parent;

	public Create(DSFunctions parent) {
		this.parent = parent;
	}

	public void CreateMethod() throws IOException {
		parent.fNDS = new RandomAccessFile(parent.ndsfilename, "rw");

		boolean bSecureSyscalls = false;
		String headerfilename = parent.headerfilename;
		int headersize = 0x200;

		FileInputStream fis = new FileInputStream(headerfilename);
		parent.header = getStruct(fis, new Header());
		headersize = parent.header.rom_header_size;
		fis.close();

		if ((parent.header.arm9_ram_address + 0x800 == parent.header.arm9_entry_address)
				|| (parent.header.rom_header_size > 0x200)) {
			bSecureSyscalls = true;
		}

		// Write ninty logo
		if (bSecureSyscalls) {
			System.arraycopy(nintendo_logo, 0, parent.header.logo, 0, 156);
		}

		parent.fNDS.seek(headersize);

		// ARM9 binary START
		parent.header.arm9_rom_offset = ((int) (parent.fNDS.getFilePointer() + arm9_align))
				& (~arm9_align);
		if (DSFunctions.verboseMode)
			System.out.println("placing arm9 at "
					+ parent.header.arm9_rom_offset);
		int entry_address = parent.header.arm9_entry_address;
		int ram_address = parent.header.arm9_ram_address;
		if (ram_address == 0 && entry_address != 0) {
			ram_address = entry_address;
		}
		if (ram_address != 0 && entry_address == 0) {
			entry_address = ram_address;
		}
		if (ram_address == 0) {
			ram_address = entry_address = 0x02000000;
		}

		// dummy area for secure syscalls
		parent.header.arm9_size = 0;
		if (bSecureSyscalls) {
			FileInputStream fARM9 = new FileInputStream(parent.arm9filename);
			int x = readInt(fARM9);
			fARM9.close();
			if (x != 0xE7FFDEFF) {
				// not already exist?
				x = 0xE7FFDEFF;
				for (int i = 0; i < 0x200; i++) {
					writeInt(x);
				}
				parent.header.arm9_size = 0x800;
			}
		}

		int size = CopyFromBinWithFooterCheck(parent.arm9filename);
		parent.header.arm9_entry_address = entry_address;
		parent.header.arm9_ram_address = ram_address;
		parent.header.arm9_size = parent.header.arm9_size + ((size + 3) & ~3);
		// ARM9 binary END

		// ARM9 overlay table START
		writeInt(0xDEC00621);
		writeInt(0x00000AD8);
		writeInt(0x00000000);

		// don't align arm9 ovl
		parent.header.arm9_overlay_offset = (int) parent.fNDS.getFilePointer();
		if (DSFunctions.verboseMode)
			System.out.println("placing arm9ovl at "
					+ parent.header.arm9_overlay_offset);
		int size_ovl = CopyFromBinNormal(parent.arm9ovltablefilename);
		parent.header.arm9_overlay_size = size_ovl;
		overlay_files += size_ovl / 0x20;
		if (size_ovl <= 0) {
			parent.header.arm9_overlay_offset = 0;
		}

		// ARM9 overlay table END

		// COULD BE HERE: ARM9 overlay files, no padding before or between. end
		// is padded with 0xFF's and then followed by ARM7 binary
		// fseek(fNDS, 1388772, SEEK_CUR); // test for ASME

		// ARM7 binary START
		parent.header.arm7_rom_offset = ((int) (parent.fNDS.getFilePointer() + arm7_align))
				& ~arm7_align;
		if (DSFunctions.verboseMode)
			System.out.println("placing arm7 at "
					+ parent.header.arm7_rom_offset);
		parent.fNDS.seek(parent.header.arm7_rom_offset);

		int entry_address7 = parent.header.arm7_entry_address;
		int ram_address7 = parent.header.arm7_ram_address;
		if (ram_address7 == 0 && entry_address7 != 0) {
			ram_address7 = entry_address7;
		}
		if (ram_address7 != 0 && entry_address7 == 0) {
			entry_address7 = ram_address7;
		}
		if (ram_address7 == 0) {
			ram_address7 = entry_address7 = 0x037f8000;
		}

		int size7 = CopyFromBinNormal(parent.arm7filename);

		parent.header.arm7_entry_address = entry_address7;
		parent.header.arm7_ram_address = ram_address7;
		parent.header.arm7_size = ((size7 + 3) & ~3);

		// ARM7 binary END

		// ARM7 overlay table START

		// don't align arm7 ovl
		parent.header.arm7_overlay_offset = (int) parent.fNDS.getFilePointer();
		if (DSFunctions.verboseMode)
			System.out.println("placing arm7ovl at "
					+ parent.header.arm7_overlay_offset);
		int size_ovl7 = CopyFromBinNormal(parent.arm7ovltablefilename);
		parent.header.arm7_overlay_size = size_ovl7;
		overlay_files += size_ovl7 / 0x20;
		if (size_ovl7 <= 0) {
			parent.header.arm7_overlay_offset = 0;
		}

		// ARM7 overlay table END

		// Filesystem START
		tree.free_file_id = overlay_files;
		tree.free_dir_id++;
		tree.directory_count++;
		TreeNode filetree = tree.ReadDirectory(new TreeNode(),
				parent.filerootdir);

		// Calculate offsets required for FNT and FAT
		tree._entry_start = 8 * tree.directory_count;
		parent.header.fnt_offset = ((int) (parent.fNDS.getFilePointer() + fnt_align))
				& ~fnt_align;
		if (DSFunctions.verboseMode)
			System.out.println("placing fnt at " + parent.header.fnt_offset);
		parent.header.fnt_size = tree._entry_start + tree.total_name_size
				+ tree.directory_count * 4 + tree.file_count - 3;
		tree.file_count += overlay_files;
		parent.header.fat_offset = (parent.header.fnt_offset
				+ parent.header.fnt_size + fat_align)
				& ~fat_align;
		if (DSFunctions.verboseMode)
			System.out.println("placing fat at " + parent.header.fat_offset);
		parent.header.fat_size = tree.file_count * 8;

		// banner after FNT/FAT
		parent.header.banner_offset = (parent.header.fat_offset
				+ parent.header.fat_size + banner_align)
				& ~banner_align;
		if (DSFunctions.verboseMode)
			System.out.println("placing banner at "
					+ parent.header.banner_offset);
		tree.file_top = parent.header.banner_offset + 0x840;
		parent.fNDS.seek(parent.header.banner_offset);
		CopyFromBinNormal(parent.bannerfilename);

		tree.file_end = tree.file_top; // no file data as yet

		// add (hidden) overlay files
		for (int i = 0; i < overlay_files; i++) {
			String s = String.format(OverlayEntry.OVERLAY_FMT, i);
			this.AddFile(parent.overlaydir, "/", s, i);
		}

		// add all other (visible) files
		this.AddDirectory(filetree, "/", 0xF000, tree.directory_count);
		parent.fNDS.seek(tree.file_end);
		if (DSFunctions.verboseMode) {
			System.out.printf("%d directories.\n", tree.directory_count);
			System.out.printf("%d normal files.\n", tree.file_count
					- overlay_files);
			System.out.printf("%d overlay files.\n", overlay_files);
		}

		// Filesystem END

		// align file size
		int newfilesize = tree.file_end;
		newfilesize = (newfilesize + 3) & ~3;
		parent.header.application_end_offset = newfilesize;
		if (newfilesize != tree.file_end) {
			parent.fNDS.seek(newfilesize - 1);
			parent.fNDS.write(0);
		}

		// calculate device capacity;
		newfilesize |= newfilesize >> 16;
		newfilesize |= newfilesize >> 8;
		newfilesize |= newfilesize >> 4;
		newfilesize |= newfilesize >> 2;
		newfilesize |= newfilesize >> 1;
		newfilesize++;
		if (newfilesize <= 128 * 1024) {
			newfilesize = 128 * 1024;
		}
		int devcap = -18;
		int x = newfilesize;
		while (x != 0) {
			x >>= 1;
			devcap++;
		}
		parent.header.devicecap = (byte) ((devcap < 0) ? 0 : devcap);

		// fix up header CRCs and write header
		parent.header.logo_crc = Header.CalcLogoCRC(parent.header);
		parent.header.header_crc = Header.CalcHeaderCRC(parent.header);

		parent.fNDS.seek(0);
		parent.fNDS.write(parent.header.get());

		// done!
		parent.fNDS.close();
	}

	private void AddDirectory(TreeNode node, String prefix, int this_dir_id,
			int _parent_id) throws IOException {

		// skip dummy node
		node = node.next;
		if (DSFunctions.verboseMode)
			System.out.printf("%s\n", prefix);

		// write directory info
		parent.fNDS.seek(parent.header.fnt_offset + 8 * (this_dir_id & 0xFFF));
		int entry_start = tree._entry_start;
		writeInt(entry_start);
		int _top_file_id = tree.free_file_id;
		writeUnsignedShort(_top_file_id);
		writeUnsignedShort(_parent_id);

		// start of directory entrynames
		parent.fNDS.seek(parent.header.fnt_offset + tree._entry_start);

		// write filenames
		for (TreeNode t = node; t != null; t = t.next) {
			if (t.directory == null) {
				int namelen = t.name.length();
				parent.fNDS.write((t.directory != null) ? (128 | namelen)
						: namelen);
				tree._entry_start++;
				parent.fNDS.write(t.name.getBytes("US-ASCII"), 0, namelen);
				tree._entry_start += namelen;
				tree.free_file_id++;
			}
		}

		// write directorynames
		for (TreeNode t = node; t != null; t = t.next) {
			if (t.directory != null) {
				int namelen = t.name.length();
				parent.fNDS.write((t.directory != null) ? (128 | namelen)
						: namelen);
				tree._entry_start++;
				parent.fNDS.write(t.name.getBytes("US-ASCII"), 0, namelen);
				tree._entry_start += namelen;

				writeUnsignedShort(t.dir_id);
				tree._entry_start += 2;
			}
		}

		parent.fNDS.write(0);
		tree._entry_start++;
		// end of directory entrynames

		// add files
		int local_file_id = _top_file_id;
		for (TreeNode t = node; t != null; t = t.next) {
			if (t.directory == null) {
				AddFile(parent.filerootdir, prefix, t.name, local_file_id++);
			}
		}

		// add subdirectories
		for (TreeNode t = node; t != null; t = t.next) {
			if (t.directory != null) {
				String strbuf = prefix + t.name + "/";
				AddDirectory(t.directory, strbuf, t.dir_id, this_dir_id);
			}
		}
	}

	private void AddFile(String rootdir, String prefix, String entry_name,
			int file_id) throws IOException {

		// Make filename
		String strbuf = rootdir + prefix + entry_name;

		tree.file_top = (tree.file_top + file_align) & ~file_align;
		parent.fNDS.seek(tree.file_top);

		FileInputStream fis = new FileInputStream(strbuf);
		int size = (int) new File(strbuf).length();
		int file_bottom = tree.file_top + size;

		// print
		if (DSFunctions.verboseMode)
			System.out.printf("%5d 0x%08X 0x%08X %9d %s%s\n", file_id,
					tree.file_top, file_bottom, size, prefix, entry_name);

		// write data
		int sizeof_copybuf = 256 * 1024;
		byte[] copybuf = new byte[sizeof_copybuf];

		while (size > 0) {
			int size2 = (size >= sizeof_copybuf) ? sizeof_copybuf : size;
			fis.read(copybuf, 0, size2);
			parent.fNDS.write(copybuf, 0, size2);
			size -= size2;
		}
		copybuf = null;
		fis.close();
		if (parent.fNDS.getFilePointer() > tree.file_end) {
			tree.file_end = (int) parent.fNDS.getFilePointer();
		}

		// write fat
		parent.fNDS.seek(parent.header.fat_offset + 8 * file_id);
		writeInt(tree.file_top);
		writeInt(file_bottom);

		tree.file_top = file_bottom;
	}

	private int CopyFromBinWithFooterCheck(String binFilename)
			throws IOException {
		FileInputStream fis = new FileInputStream(binFilename);
		int _size = 0;
		byte[] buffer = new byte[1024];
		while (true) {
			int bytesread = fis.read(buffer);
			if (bytesread <= 0) {
				break;
			}
			parent.fNDS.write(buffer, 0, bytesread);
			_size += bytesread;
		}
		fis.close();

		int _size_without_footer = _size;

		long _fNDScurrPtr = parent.fNDS.getFilePointer();
		parent.fNDS.seek(_fNDScurrPtr - 12);
		int nitrocode = readfNDSInt();
		if (nitrocode == 0xDEC00621) {
			_size_without_footer = _size - 12;
		}
		parent.fNDS.seek(_fNDScurrPtr);
		return _size_without_footer;
	}

	private int CopyFromBinNormal(String binFilename) throws IOException {
		FileInputStream fis = new FileInputStream(binFilename);
		int _size = 0;
		byte[] buffer = new byte[1024];
		while (true) {
			int bytesread = fis.read(buffer);
			if (bytesread <= 0) {
				break;
			}
			parent.fNDS.write(buffer, 0, bytesread);
			_size += bytesread;
		}
		fis.close();

		return _size;
	}

	private void writeInt(int x) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(x);
		b.rewind();
		byte[] buf = new byte[4];
		b.get(buf);
		parent.fNDS.write(buf);
	}

	private void writeUnsignedShort(int x) throws IOException {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(x);
		b.rewind();
		byte[] buf = new byte[2];
		b.get(buf);
		parent.fNDS.write(buf);
	}

	private int readInt(InputStream is) throws IOException {
		byte[] buf = new byte[4];
		is.read(buf);
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(buf, 0, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	private int readfNDSInt() throws IOException {
		byte[] buf = new byte[4];
		parent.fNDS.read(buf);
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(buf, 0, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	public <T extends NDSStruct> T getStruct(InputStream is, T base)
			throws IOException {
		byte[] raw = new byte[base.size()];
		is.read(raw);
		base.populate(raw);
		return base;
	}
}
