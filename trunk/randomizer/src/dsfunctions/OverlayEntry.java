package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  OverlayEntry.java - represents an arm9/arm7 overlay entry            	--*/
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

public class OverlayEntry implements NDSStruct {
	int id, ram_address, ram_size, bss_size;
	int sinit_init, sinit_init_end, file_id, reserved;

	private byte[] raw;
	public static String OVERLAY_FMT = "overlay_%04d.bin";

	public void populate(byte[] data) {
		this.raw = data;
		this.id = grabInt(0x0);
		this.ram_address = grabInt(0x4);
		this.ram_size = grabInt(0x8);
		this.bss_size = grabInt(0xC);
		this.sinit_init = grabInt(0x10);
		this.sinit_init_end = grabInt(0x14);
		this.file_id = grabInt(0x18);
		this.reserved = grabInt(0x1C);
	}

	public void updateRaw() {
		raw = new byte[0x20];
		putInt(raw, 0, this.id);
		putInt(raw, 0x4, this.ram_address);
		putInt(raw, 0x8, this.ram_size);
		putInt(raw, 0xC, this.bss_size);
		putInt(raw, 0x10, this.sinit_init);
		putInt(raw, 0x14, this.sinit_init_end);
		putInt(raw, 0x18, this.file_id);
		putInt(raw, 0x1C, this.reserved);
	}

	public int grabInt(int offset) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.put(raw, offset, 4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.rewind();
		return b.getInt();
	}

	private void putInt(byte[] data, int offset, int value) {
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(value);
		b.rewind();
		b.get(data, offset, 4);
	}

	@Override
	public int size() {
		return 0x20;
	}

	@Override
	public byte[] get() {
		updateRaw();
		return raw;
	}
}
