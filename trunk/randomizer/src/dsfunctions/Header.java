package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  Header.java - represents the NDS rom header struct            		--*/
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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header implements NDSStruct {

	byte[] title, gamecode, makercode;
	byte unitcode, devicetype, devicecap;
	byte[] reserved1;
	byte romversion, reserved2;
	int arm9_rom_offset, arm9_entry_address, arm9_ram_address, arm9_size;
	int arm7_rom_offset, arm7_entry_address, arm7_ram_address, arm7_size;
	int fnt_offset, fnt_size, fat_offset, fat_size;
	int arm9_overlay_offset, arm9_overlay_size;
	int arm7_overlay_offset, arm7_overlay_size;
	int rom_control_info1, rom_control_info2;
	int banner_offset;
	short secure_area_crc, rom_control_info3;
	byte[] reserved3;
	int application_end_offset, rom_header_size;
	byte[] reserved4, logo;
	short logo_crc, header_crc;
	byte[] reserved5, zero;
	byte regionmask;
	byte[] h1B1;
	byte appflags;
	int dsi9_rom_offset, dsi9_entry_address, dsi9_ram_address, dsi9_size;
	int dsi7_rom_offset, dsi7_entry_address, dsi7_ram_address, dsi7_size;
	byte[] reserved6;
	int dsi_region_start, dsi_region_size;
	int hash1_start, hash1_size, hash2_start, hash2_size;

	private byte[] raw;

	public void populate(byte[] data) {
		this.raw = data;
		title = grab(0, 0xC);
		gamecode = grab(0xC, 4);
		makercode = grab(0x10, 2);
		unitcode = data[0x12];
		devicetype = data[0x13];
		devicecap = data[0x14];
		reserved1 = grab(0x15, 9);
		romversion = data[0x1E];
		reserved2 = data[0x1F];
		arm9_rom_offset = grabInt(0x20);
		arm9_entry_address = grabInt(0x24);
		arm9_ram_address = grabInt(0x28);
		arm9_size = grabInt(0x2C);
		arm7_rom_offset = grabInt(0x30);
		arm7_entry_address = grabInt(0x34);
		arm7_ram_address = grabInt(0x38);
		arm7_size = grabInt(0x3C);
		fnt_offset = grabInt(0x40);
		fnt_size = grabInt(0x44);
		fat_offset = grabInt(0x48);
		fat_size = grabInt(0x4C);
		arm9_overlay_offset = grabInt(0x50);
		arm9_overlay_size = grabInt(0x54);
		arm7_overlay_offset = grabInt(0x58);
		arm7_overlay_size = grabInt(0x5C);
		rom_control_info1 = grabInt(0x60);
		rom_control_info2 = grabInt(0x64);
		banner_offset = grabInt(0x68);
		secure_area_crc = grabShort(0x6C);
		rom_control_info3 = grabShort(0x6E);
		reserved3 = grab(0x70, 0x10);
		application_end_offset = grabInt(0x80);
		rom_header_size = grabInt(0x84);
		reserved4 = grab(0x88, 0x38);
		logo = grab(0xC0, 0x9C);
		logo_crc = grabShort(0x15C);
		header_crc = grabShort(0x15E);
		reserved5 = grab(0x160, 0x10);
		zero = grab(0x170, 0x40);
		regionmask = data[0x1B0];
		h1B1 = grab(0x1B1, 0xE);
		appflags = data[0x1BF];
		dsi9_rom_offset = grabInt(0x1C0);
		dsi9_entry_address = grabInt(0x1C4);
		dsi9_ram_address = grabInt(0x1C8);
		dsi9_size = grabInt(0x1CC);
		dsi7_rom_offset = grabInt(0x1D0);
		dsi7_entry_address = grabInt(0x1D4);
		dsi7_ram_address = grabInt(0x1D8);
		dsi7_size = grabInt(0x1DC);
		reserved6 = grab(0x1E0, 8);
		dsi_region_start = grabInt(0x1E8);
		dsi_region_size = grabInt(0x1EC);
		hash1_start = grabInt(0x1F0);
		hash1_size = grabInt(0x1F4);
		hash2_start = grabInt(0x1F8);
		hash2_size = grabInt(0x1FC);
	}

	public void updateRaw() {
		raw = new byte[0x200];
		put(raw, 0, title);
		put(raw, 0x0C, gamecode);
		put(raw, 0x10, makercode);
		raw[0x12] = unitcode;
		raw[0x13] = devicetype;
		raw[0x14] = devicecap;
		put(raw, 0x15, reserved1);
		raw[0x1E] = romversion;
		raw[0x1F] = reserved2;
		putInt(raw, 0x20, arm9_rom_offset);
		putInt(raw, 0x24, arm9_entry_address);
		putInt(raw, 0x28, arm9_ram_address);
		putInt(raw, 0x2C, arm9_size);
		putInt(raw, 0x30, arm7_rom_offset);
		putInt(raw, 0x34, arm7_entry_address);
		putInt(raw, 0x38, arm7_ram_address);
		putInt(raw, 0x3C, arm7_size);
		putInt(raw, 0x40, fnt_offset);
		putInt(raw, 0x44, fnt_size);
		putInt(raw, 0x48, fat_offset);
		putInt(raw, 0x4C, fat_size);
		putInt(raw, 0x50, arm9_overlay_offset);
		putInt(raw, 0x54, arm9_overlay_size);
		putInt(raw, 0x58, arm7_overlay_offset);
		putInt(raw, 0x5C, arm7_overlay_size);
		putInt(raw, 0x60, rom_control_info1);
		putInt(raw, 0x64, rom_control_info2);
		putInt(raw, 0x68, banner_offset);
		putShort(raw, 0x6C, secure_area_crc);
		putShort(raw, 0x6E, rom_control_info3);
		put(raw, 0x70, reserved3);
		putInt(raw, 0x80, application_end_offset);
		putInt(raw, 0x84, rom_header_size);
		put(raw, 0x88, reserved4);
		put(raw, 0xC0, logo);
		putShort(raw, 0x15C, logo_crc);
		putShort(raw, 0x15E, header_crc);
		put(raw, 0x160, reserved5);
		put(raw, 0x170, zero);
		raw[0x1B0] = regionmask;
		put(raw, 0x1B1, h1B1);
		raw[0x1BF] = appflags;
		putInt(raw, 0x1C0, dsi9_rom_offset);
		putInt(raw, 0x1C4, dsi9_entry_address);
		putInt(raw, 0x1C8, dsi9_ram_address);
		putInt(raw, 0x1CC, dsi9_size);
		putInt(raw, 0x1D0, dsi7_rom_offset);
		putInt(raw, 0x1D4, dsi7_entry_address);
		putInt(raw, 0x1D8, dsi7_ram_address);
		putInt(raw, 0x1DC, dsi7_size);
		put(raw, 0x1E0, reserved6);
		putInt(raw, 0x1E8, dsi_region_start);
		putInt(raw, 0x1EC, dsi_region_size);
		putInt(raw, 0x1F0, hash1_start);
		putInt(raw, 0x1F4, hash1_size);
		putInt(raw, 0x1F8, hash2_start);
		putInt(raw, 0x1FC, hash2_size);
	}

	private void putInt(byte[] data, int offset, int value) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(value);
		b.rewind();
		b.get(data, offset, 4);
	}

	private void putShort(byte[] data, int offset, short value) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putShort(value);
		b.rewind();
		b.get(data, offset, 2);
	}

	private void put(byte[] data, int offset, byte[] putData) {
		System.arraycopy(putData, 0, data, offset, putData.length);
	}

	public int grabInt(int offset) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(raw, offset, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	public short grabShort(int offset) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.put(raw, offset, 2);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getShort();
	}

	public byte[] grab(int offset, int length) {
		byte[] ret = new byte[length];
		System.arraycopy(raw, offset, ret, 0, length);
		return ret;
	}

	@Override
	public int size() {
		return 0x200;
	}

	@Override
	public byte[] get() {
		updateRaw();
		return raw;
	}

	public static short CalcLogoCRC(Header header) {
		return CRC16.calculate(header.get(), 0xC0, 156);
	}

	public static short CalcHeaderCRC(Header header) {
		return CRC16.calculate(header.get(), 0, 0x15E);
	}
}
