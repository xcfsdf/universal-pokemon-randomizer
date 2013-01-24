package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  Gen2RomHandler.java - randomizer handler for G/S/C.					--*/
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.RandomSource;
import com.dabomstew.pkrandom.RomFunctions;
import com.dabomstew.pkrandom.pokemon.Encounter;
import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.Move;
import com.dabomstew.pkrandom.pokemon.MoveLearnt;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.TrainerPokemon;
import com.dabomstew.pkrandom.pokemon.Type;

public class Gen2RomHandler extends AbstractGBRomHandler {

	private static final Type[] typeTable = constructTypeTable();

	private static Type[] constructTypeTable() {
		Type[] table = new Type[256];
		table[0x00] = Type.NORMAL;
		table[0x01] = Type.FIGHTING;
		table[0x02] = Type.FLYING;
		table[0x03] = Type.POISON;
		table[0x04] = Type.GROUND;
		table[0x05] = Type.ROCK;
		table[0x07] = Type.BUG;
		table[0x08] = Type.GHOST;
		table[0x09] = Type.STEEL;
		table[0x14] = Type.FIRE;
		table[0x15] = Type.WATER;
		table[0x16] = Type.GRASS;
		table[0x17] = Type.ELECTRIC;
		table[0x18] = Type.PSYCHIC;
		table[0x19] = Type.ICE;
		table[0x1A] = Type.DRAGON;
		table[0x1B] = Type.DARK;
		return table;
	}

	private static byte typeToByte(Type type) {
		if (type == null) {
			return 0x13; // ???-type
		}
		switch (type) {
		case NORMAL:
			return 0x00;
		case FIGHTING:
			return 0x01;
		case FLYING:
			return 0x02;
		case POISON:
			return 0x03;
		case GROUND:
			return 0x04;
		case ROCK:
			return 0x05;
		case BUG:
			return 0x07;
		case GHOST:
			return 0x08;
		case FIRE:
			return 0x14;
		case WATER:
			return 0x15;
		case GRASS:
			return 0x16;
		case ELECTRIC:
			return 0x17;
		case PSYCHIC:
			return 0x18;
		case ICE:
			return 0x19;
		case DRAGON:
			return 0x1A;
		case STEEL:
			return 0x09;
		case DARK:
			return 0x1B;
		}
		return 0; // normal by default
	}

	private static class RomEntry {
		private String name;
		private String romCode;
		private int version, nonJapanese;
		private String tableFile;
		private boolean isCrystal;
		private int crcInHeader = -1;
		private String expPatch;
		private Map<String, Integer> entries = new HashMap<String, Integer>();
		private Map<String, int[]> arrayEntries = new HashMap<String, int[]>();
		private List<Integer> staticPokemonSingle = new ArrayList<Integer>();
		private Map<Integer, Integer> staticPokemonGameCorner = new TreeMap<Integer, Integer>();
		private Map<Integer, Integer> staticPokemonCopy = new TreeMap<Integer, Integer>();

		private int getValue(String key) {
			if (!entries.containsKey(key)) {
				entries.put(key, 0);
			}
			return entries.get(key);
		}
	}

	private static List<RomEntry> roms;

	static {
		loadROMInfo();
	}

	private static void loadROMInfo() {
		roms = new ArrayList<RomEntry>();
		RomEntry current = null;
		try {
			Scanner sc = new Scanner(
					FileFunctions.openConfig("gen2_offsets.ini"), "UTF-8");
			while (sc.hasNextLine()) {
				String q = sc.nextLine().trim();
				if (q.contains("//")) {
					q = q.substring(0, q.indexOf("//")).trim();
				}
				if (!q.isEmpty()) {
					if (q.startsWith("[") && q.endsWith("]")) {
						// New rom
						current = new RomEntry();
						current.name = q.substring(1, q.length() - 1);
						roms.add(current);
					} else {
						String[] r = q.split("=", 2);
						if (r.length == 1) {
							System.err.println("invalid entry " + q);
							continue;
						}
						if (r[1].endsWith("\r\n")) {
							r[1] = r[1].substring(0, r[1].length() - 2);
						}
						r[1] = r[1].trim();
						r[0] = r[0].trim();
						// Static Pokemon?
						if (r[0].equals("StaticPokemonGameCorner[]")) {
							String[] offsets = r[1].substring(1,
									r[1].length() - 1).split(",");
							if (offsets.length != 2) {
								continue;
							}
							int[] offs = new int[offsets.length];
							int c = 0;
							for (String off : offsets) {
								offs[c++] = parseRIInt(off);
							}
							current.staticPokemonGameCorner.put(offs[0],
									offs[1]);
						} else if (r[0].equals("StaticPokemonCopy[]")) {
							String[] offsets = r[1].substring(1,
									r[1].length() - 1).split(",");
							if (offsets.length != 2) {
								continue;
							}
							int[] offs = new int[offsets.length];
							int c = 0;
							for (String off : offsets) {
								offs[c++] = parseRIInt(off);
							}
							current.staticPokemonCopy.put(offs[0], offs[1]);
						} else if (r[0].equals("Game")) {
							current.romCode = r[1];
						} else if (r[0].equals("Version")) {
							current.version = parseRIInt(r[1]);
						} else if (r[0].equals("NonJapanese")) {
							current.nonJapanese = parseRIInt(r[1]);
						} else if (r[0].equals("Type")) {
							if (r[1].equalsIgnoreCase("Crystal")) {
								current.isCrystal = true;
							} else {
								current.isCrystal = false;
							}
						} else if (r[0].equals("TableFile")) {
							current.tableFile = r[1];
						} else if (r[0].equals("CRCInHeader")) {
							current.crcInHeader = parseRIInt(r[1]);
						} else if (r[0].equals("BWXPPatch")) {
							current.expPatch = r[1];
						} else if (r[0].equals("CopyFrom")) {
							for (RomEntry otherEntry : roms) {
								if (r[1].equalsIgnoreCase(otherEntry.name)) {
									// copy from here
									current.arrayEntries
											.putAll(otherEntry.arrayEntries);
									current.entries.putAll(otherEntry.entries);
									if (current.getValue("CopyStaticPokemon") == 1) {
										current.staticPokemonSingle
												.addAll(otherEntry.staticPokemonSingle);
										current.staticPokemonGameCorner
												.putAll(otherEntry.staticPokemonGameCorner);
										current.staticPokemonCopy
												.putAll(otherEntry.staticPokemonCopy);
										current.entries.put(
												"StaticPokemonSupport", 1);
									} else {
										current.entries.put(
												"StaticPokemonSupport", 0);
									}
									current.tableFile = otherEntry.tableFile;
								}
							}
						} else {
							if (r[1].startsWith("[") && r[1].endsWith("]")) {
								String[] offsets = r[1].substring(1,
										r[1].length() - 1).split(",");
								int[] offs = new int[offsets.length];
								int c = 0;
								for (String off : offsets) {
									offs[c++] = parseRIInt(off);
								}
								if (r[0].startsWith("StaticPokemon")) {
									for (int off : offs) {
										current.staticPokemonSingle.add(off);
									}
								} else {
									current.arrayEntries.put(r[0], offs);
								}
							} else {
								int offs = parseRIInt(r[1]);
								current.entries.put(r[0], offs);
							}
						}
					}
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
		}

	}

	private static int parseRIInt(String off) {
		int radix = 10;
		off = off.trim().toLowerCase();
		if (off.startsWith("0x") || off.startsWith("&h")) {
			radix = 16;
			off = off.substring(2);
		}
		try {
			return Integer.parseInt(off, radix);
		} catch (NumberFormatException ex) {
			System.err.println("invalid base " + radix + "number " + off);
			return 0;
		}
	}

	// This ROM's data
	private Pokemon[] pokes;
	private RomEntry romEntry;
	private Move[] moves;
	private String[] tb = new String[256];
	private Map<String, Byte> d = new HashMap<String, Byte>();
	private int longestTableToken;

	@Override
	public boolean detectRom(byte[] rom) {
		if (rom.length > 2097152) {
			return false; // size check
		}
		return checkRomEntry(rom) != null; // so it's OK if it's a valid ROM
	}

	@Override
	public void loadedRom() {
		romEntry = checkRomEntry(this.rom);
		readTextTable();
		loadPokemonStats();
		loadMoves();
	}

	private RomEntry checkRomEntry(byte[] rom) {
		int version = rom[0x14C] & 0xFF;
		int nonjap = rom[0x14A] & 0xFF;
		// Check for specific CRC first
		int crcInHeader = ((rom[0x14E] & 0xFF) << 8) | (rom[0x14F] & 0xFF);
		for (RomEntry re : roms) {
			if (romSig(rom, re.romCode) && re.version == version
					&& re.nonJapanese == nonjap
					&& re.crcInHeader == crcInHeader) {
				return re;
			}
		}
		// Now check for non-specific-CRC entries
		for (RomEntry re : roms) {
			if (romSig(rom, re.romCode) && re.version == version
					&& re.nonJapanese == nonjap && re.crcInHeader == -1) {
				return re;
			}
		}
		// Not found
		return null;
	}

	private void readTextTable() {
		try {
			Scanner sc = new Scanner(
					FileFunctions.openConfig(romEntry.tableFile + ".tbl"),
					"UTF-8");
			longestTableToken = 0;
			while (sc.hasNextLine()) {
				String q = sc.nextLine();
				if (!q.trim().isEmpty()) {
					String[] r = q.split("=", 2);
					if (r[1].endsWith("\r\n")) {
						r[1] = r[1].substring(0, r[1].length() - 2);
					}
					tb[Integer.parseInt(r[0], 16)] = r[1];
					longestTableToken = Math.max(longestTableToken,
							r[1].length());
					d.put(r[1], (byte) Integer.parseInt(r[0], 16));
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
		}

	}

	@Override
	public void savingRom() {
		savePokemonStats();
		saveMoves();
	}

	// DEBUG
	public void printPokemonStats() {
		for (int i = 1; i <= 251; i++) {
			System.out.println(pokes[i].toString());
		}
	}

	private void loadPokemonStats() {
		pokes = new Pokemon[252];
		// Fetch our names
		String[] pokeNames = readPokemonNames();
		int offs = romEntry.getValue("PokemonStatsOffset");
		// Get base stats
		for (int i = 1; i <= 251; i++) {
			pokes[i] = new Pokemon();
			pokes[i].number = i;
			loadBasicPokeStats(pokes[i], offs + (i - 1) * 0x20);
			// Name?
			pokes[i].name = pokeNames[i];
		}

	}

	private void savePokemonStats() {
		// Write pokemon names
		int offs = romEntry.getValue("PokemonNamesOffset");
		int len = romEntry.getValue("PokemonNamesLength");
		for (int i = 1; i <= 251; i++) {
			int stringOffset = offs + (i - 1) * len;
			writeFixedLengthString(pokes[i].name, stringOffset, len);
		}
		// Write pokemon stats
		int offs2 = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 251; i++) {
			saveBasicPokeStats(pokes[i], offs2 + (i - 1) * 0x20);
		}
	}

	public void printMoves() {
		for (int i = 1; i <= 251; i++) {
			System.out.println(moves[i]);
		}
	}

	private void loadMoves() {
		moves = new Move[252];
		int offs = romEntry.getValue("MoveDataOffset");
		for (int i = 1; i <= 251; i++) {
			moves[i] = new Move();
			moves[i].name = RomFunctions.moveNames[i];
			moves[i].number = i;
			moves[i].effectIndex = rom[offs + (i - 1) * 7] & 0xFF;
			moves[i].hitratio = ((rom[offs + (i - 1) * 7 + 3] & 0xFF) + 0) / 255.0 * 100;
			moves[i].power = rom[offs + (i - 1) * 7 + 1] & 0xFF;
			moves[i].pp = rom[offs + (i - 1) * 7 + 4] & 0xFF;
			moves[i].type = typeTable[rom[offs + (i - 1) * 7 + 2]];
		}

	}

	private void saveMoves() {
		int offs = romEntry.getValue("MoveDataOffset");
		for (int i = 1; i <= 251; i++) {
			rom[offs + (i - 1) * 7] = (byte) moves[i].effectIndex;
			rom[offs + (i - 1) * 7 + 1] = (byte) moves[i].power;
			rom[offs + (i - 1) * 7 + 2] = typeToByte(moves[i].type);
			int hitratio = (int) Math.round(moves[i].hitratio * 2.55);
			if (hitratio < 0) {
				hitratio = 0;
			}
			if (hitratio > 255) {
				hitratio = 255;
			}
			rom[offs + (i - 1) * 7 + 3] = (byte) hitratio;
			rom[offs + (i - 1) * 7 + 4] = (byte) moves[i].pp;
		}
	}

	public void applyMoveUpdates() {
		log("--Move Updates--");
		moves[13].setAccuracy(100); // Razor Wind => 100% accuracy (gen1/2)
		log("Made Razor Wind have 100% accuracy");
		moves[19].power = 90; // Fly => 90 power (gen1/2/3)
		log("Made Fly have 90 power");
		moves[20].setAccuracy(85); // Bind => 85% accuracy (gen1-4)
		log("Made Bind have 85% accuracy");
		moves[22].pp = 15; // Vine Whip => 15 pp (gen1/2/3)
		log("Made Vine Whip have 15 PP");
		moves[26].pp = 10;
		moves[26].power = 100; // Jump Kick => 10 pp, 100 power (gen1-4)
		log("Made Jump kick have 10 PP and 100 power");
		moves[33].power = 50; // Tackle => 50 power, 100% accuracy
		moves[33].setAccuracy(100); // (gen1-4)
		log("Made Tackle have 50 power and 100% accuracy");
		moves[35].setAccuracy(90); // Wrap => 90% accuracy (gen1-4)
		log("Made Wrap have 90% accuracy");
		moves[37].pp = 10;
		moves[37].power = 120; // Thrash => 120 power, 10pp (gen1-4)
		log("Made Thrash have 10 PP and 120 power");
		moves[50].setAccuracy(100); // Disable => 100% accuracy (gen1-4)
		log("Made Disable have 100% accuracy");
		// Move 67, Low Kick, has weight-based power in gen3+
		moves[71].pp = 25; // Absorb => 25pp (gen1/2/3)
		log("Made Absorb have 25 PP");
		moves[72].pp = 15; // Mega Drain => 15pp (gen1/2/3)
		log("Made Mega Drain have 15 PP");
		moves[80].pp = 10;
		moves[80].power = 120; // Petal Dance => 120power, 10pp (gen1-4)
		log("Made Petal Dance have 10 PP and 120 power");
		moves[83].setAccuracy(85);
		moves[83].power = 35; // Fire Spin => 35 power, 85% acc (gen1-4)
		log("Made Fire Spin have 35 power and 85% accuracy");
		moves[91].power = 80; // Dig => 80 power (gen1/2/3)
		log("Made Dig have 80 power");
		moves[92].setAccuracy(90); // Toxic => 90% accuracy (gen1-4)
		log("Made Toxic have 90% accuracy");
		// move 95, Hypnosis, needs 60% accuracy in DP (its correct here)
		moves[105].pp = 10; // Recover => 10pp (gen1/2/3)
		log("Made Recover have 10 PP");
		moves[128].setAccuracy(85); // Clamp => 85% acc (gen1-4)
		log("Made Clamp have 85% accuracy");
		moves[136].pp = 10;
		moves[136].power = 130; // HJKick => 130 power, 10pp (gen1-4)
		log("Made Hi-Jump Kick have 130 power and 10 PP");
		moves[137].setAccuracy(90); // Glare => 90% acc (gen1-4)
		log("Made Glare have 90% accuracy");
		moves[139].setAccuracy(80); // Poison Gas => 80% acc (gen1-4)
		log("Made Poison Gas have 80% accuracy");
		moves[148].setAccuracy(100); // Flash => 100% acc (gen1/2/3)
		log("Made Flash have 100% accuracy");
		moves[152].setAccuracy(90); // Crabhammer => 90% acc (gen1-4)
		log("Made Crabhammer have 90% accuracy");
		// GEN2+ moves only from here
		moves[174].type = Type.GHOST; // Curse => GHOST (gen2-4)
		log("Made Curse Ghost type");
		moves[178].setAccuracy(100); // Cotton Spore => 100% acc (gen2-4)
		log("Made Cotton Spore have 100% accuracy");
		moves[184].setAccuracy(100); // Scary Face => 100% acc (gen2-4)
		log("Made Scary Face have 100% accuracy");
		moves[198].setAccuracy(90); // Bone Rush => 90% acc (gen2-4)
		log("Made Bone Rush have 90% accuracy");
		moves[200].power = 120; // Outrage => 120 power (gen2-3)
		log("Made Outrage have 120 power");
		moves[202].pp = 10; // Giga Drain => 10pp (gen2-3)
		moves[202].power = 75; // Giga Drain => 75 power (gen2-4)
		log("Made Giga Drain have 10 PP and 75 power");
		moves[210].power = 20; // Fury Cutter => 20 power (gen2-4)
		log("Made Fury Cutter have 20 power");
		// Future Sight => 10 pp, 100 power, 100% acc (gen2-4)
		moves[248].pp = 10;
		moves[248].power = 100;
		moves[248].setAccuracy(100);
		log("Made Future Sight have 10 PP, 100 power and 100% accuracy");
		moves[249].power = 40; // Rock Smash => 40 power (gen2-3)
		log("Made Rock Smash have 40 power");
		moves[250].power = 35;
		moves[250].setAccuracy(85); // Whirlpool => 35 pow, 85% acc (gen2-4)
		log("Made Whirlpool have 35 power and 85% accuracy");
		logBlankLine();
	}

	public List<Move> getMoves() {
		return Arrays.asList(moves);
	}

	private void loadBasicPokeStats(Pokemon pkmn, int offset) {
		pkmn.hp = rom[offset + 1] & 0xFF;
		pkmn.attack = rom[offset + 2] & 0xFF;
		pkmn.defense = rom[offset + 3] & 0xFF;
		pkmn.speed = rom[offset + 4] & 0xFF;
		pkmn.spatk = rom[offset + 5] & 0xFF;
		pkmn.spdef = rom[offset + 6] & 0xFF;
		// Type
		pkmn.primaryType = typeTable[rom[offset + 7] & 0xFF];
		pkmn.secondaryType = typeTable[rom[offset + 8] & 0xFF];
		// Only one type?
		if (pkmn.secondaryType == pkmn.primaryType) {
			pkmn.secondaryType = null;
		}
		pkmn.catchRate = rom[offset + 9] & 0xFF;
	}

	private void saveBasicPokeStats(Pokemon pkmn, int offset) {
		rom[offset + 1] = (byte) pkmn.hp;
		rom[offset + 2] = (byte) pkmn.attack;
		rom[offset + 3] = (byte) pkmn.defense;
		rom[offset + 4] = (byte) pkmn.speed;
		rom[offset + 5] = (byte) pkmn.spatk;
		rom[offset + 6] = (byte) pkmn.spdef;
		rom[offset + 7] = typeToByte(pkmn.primaryType);
		if (pkmn.secondaryType == null) {
			rom[offset + 8] = rom[offset + 7];
		} else {
			rom[offset + 8] = typeToByte(pkmn.secondaryType);
		}
		rom[offset + 9] = (byte) pkmn.catchRate;
	}

	private String[] readPokemonNames() {
		int offs = romEntry.getValue("PokemonNamesOffset");
		int len = romEntry.getValue("PokemonNamesLength");
		String[] names = new String[252];
		for (int i = 1; i <= 251; i++) {
			names[i] = readFixedLengthString(offs + (i - 1) * len, len);
		}
		return names;
	}

	private String readString(int offset, int maxLength) {
		StringBuilder string = new StringBuilder();
		for (int c = 0; c < maxLength; c++) {
			int currChar = rom[offset + c] & 0xFF;
			if (tb[currChar] != null) {
				string.append(tb[currChar]);
			} else {
				if (currChar == 0x50 || currChar == 0x00) {
					break;
				} else {
					string.append("\\x" + String.format("%02X", currChar));
				}
			}
		}
		return string.toString();
	}

	public byte[] translateString(String text) {
		List<Byte> data = new ArrayList<Byte>();
		while (text.length() != 0) {
			int i = Math.max(0, longestTableToken - text.length());
			if (text.charAt(0) == '\\' && text.charAt(1) == 'x') {
				data.add((byte) Integer.parseInt(text.substring(2, 4), 16));
				text = text.substring(4);
			} else {
				while (!(d
						.containsKey(text.substring(0, longestTableToken - i)) || (i == longestTableToken))) {
					i++;
				}
				if (i == longestTableToken) {
					text = text.substring(1);
				} else {
					data.add(d.get(text.substring(0, longestTableToken - i)));
					text = text.substring(longestTableToken - i);
				}
			}
		}
		byte[] ret = new byte[data.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = data.get(i);
		}
		return ret;
	}

	private String readFixedLengthString(int offset, int length) {
		return readString(offset, length);
	}

	public String readVariableLengthString(int offset) {
		return readString(offset, Integer.MAX_VALUE);
	}

	public String readVariableLengthScriptString(int offset) {
		return readString(offset, Integer.MAX_VALUE);
	}

	public byte[] traduire(String str) {
		return translateString(str);
	}

	private void writeFixedLengthString(String str, int offset, int length) {
		byte[] translated = translateString(str);
		int len = Math.min(translated.length, length);
		System.arraycopy(translated, 0, rom, offset, len);
		while (len < length) {
			rom[offset + len] = 0x50;
			len++;
		}
	}

	private void writeFixedLengthScriptString(String str, int offset, int length) {
		byte[] translated = translateString(str);
		int len = Math.min(translated.length, length);
		System.arraycopy(translated, 0, rom, offset, len);
		while (len < length) {
			rom[offset + len] = 0x00;
			len++;
		}
	}

	private int lengthOfStringAt(int offset) {
		int len = 0;
		while (rom[offset + len] != 0x50 && rom[offset + len] != 0x00) {
			len++;
		}
		return len;
	}

	private int makeGBPointer(int offset) {
		if (offset < 0x4000) {
			return offset;
		} else {
			return (offset % 0x4000) + 0x4000;
		}
	}

	private int bankOf(int offset) {
		return (offset / 0x4000);
	}

	private int calculateOffset(int bank, int pointer) {
		if (pointer < 0x4000) {
			return pointer;
		} else {
			return (pointer % 0x4000) + bank * 0x4000;
		}
	}

	private boolean romSig(byte[] rom, String sig) {
		try {
			int sigOffset = 0x13F;
			byte[] sigBytes = sig.getBytes("US-ASCII");
			for (int i = 0; i < sigBytes.length; i++) {
				if (rom[sigOffset + i] != sigBytes[i]) {
					return false;
				}
			}
			return true;
		} catch (UnsupportedEncodingException ex) {
			return false;
		}

	}

	@Override
	public boolean isInGame(Pokemon pkmn) {
		return (pkmn.number >= 1 && pkmn.number <= 251);
	}

	@Override
	public boolean isInGame(int pokemonNumber) {
		return (pokemonNumber >= 1 && pokemonNumber <= 251);
	}

	@Override
	public List<Pokemon> getStarters() {
		// Get the starters
		List<Pokemon> starters = new ArrayList<Pokemon>();
		starters.add(pokes[rom[romEntry.arrayEntries.get("StarterOffsets1")[0]] & 0xFF]);
		starters.add(pokes[rom[romEntry.arrayEntries.get("StarterOffsets2")[0]] & 0xFF]);
		starters.add(pokes[rom[romEntry.arrayEntries.get("StarterOffsets3")[0]] & 0xFF]);
		return starters;
	}

	@Override
	public boolean setStarters(List<Pokemon> newStarters) {
		if (newStarters.size() != 3) {
			return false;
		}
		for (Pokemon pkmn : newStarters) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}

		// Actually write

		for (int i = 0; i < 3; i++) {
			byte starter = (byte) newStarters.get(i).number;
			int[] offsets = romEntry.arrayEntries.get("StarterOffsets"
					+ (i + 1));
			for (int offset : offsets) {
				rom[offset] = starter;
			}
		}

		// Attempt to replace text
		if (romEntry.getValue("CanChangeStarterText") > 0) {
			List<Integer> cyndaTexts = RomFunctions.search(rom,
					traduire("CYNDAQUIL"));
			int offset = cyndaTexts.get(romEntry.isCrystal ? 1 : 0);
			String pokeName = newStarters.get(0).name;
			writeFixedLengthScriptString(pokeName + "?»", offset,
					lengthOfStringAt(offset) + 1);

			List<Integer> totoTexts = RomFunctions.search(rom,
					traduire("TOTODILE"));
			offset = totoTexts.get(romEntry.isCrystal ? 1 : 0);
			pokeName = newStarters.get(1).name;
			writeFixedLengthScriptString(pokeName + "?»", offset,
					lengthOfStringAt(offset) + 1);

			List<Integer> chikoTexts = RomFunctions.search(rom,
					traduire("CHIKORITA"));
			offset = chikoTexts.get(romEntry.isCrystal ? 1 : 0);
			pokeName = newStarters.get(2).name;
			writeFixedLengthScriptString(pokeName + "?»", offset,
					lengthOfStringAt(offset) + 1);
		}
		return true;
	}

	@Override
	public void shufflePokemonStats() {
		for (int i = 1; i <= 251; i++) {
			pokes[i].shuffleStats();
		}
	}

	@Override
	public Pokemon randomPokemon() {
		return pokes[(int) (RandomSource.random() * 251 + 1)];
	}

	@Override
	public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
		int offset = romEntry.getValue("WildPokemonOffset");
		List<EncounterSet> areas = new ArrayList<EncounterSet>();
		offset = readLandEncounters(offset, areas, useTimeOfDay); // Johto
		offset = readSeaEncounters(offset, areas); // Johto
		offset = readLandEncounters(offset, areas, useTimeOfDay); // Kanto
		offset = readSeaEncounters(offset, areas); // Kanto
		offset = readLandEncounters(offset, areas, useTimeOfDay); // Specials
		offset = readSeaEncounters(offset, areas); // Specials

		// Fishing Data
		offset = romEntry.getValue("FishingWildsOffset");
		for (int k = 0; k < 12; k++) {
			EncounterSet es = new EncounterSet();
			for (int i = 0; i < 11; i++) {
				if (i == 6 || i == 8) {
					offset += 3;
					continue;
				}
				offset++;
				int pokeNum = rom[offset++] & 0xFF;
				int level = rom[offset++] & 0xFF;
				Encounter enc = new Encounter();
				enc.pokemon = pokes[pokeNum];
				enc.level = level;
				es.encounters.add(enc);
			}
			areas.add(es);
		}

		for (int k = 0; k < 11; k++) {
			EncounterSet es = new EncounterSet();
			for (int i = 0; i < 4; i++) {
				int pokeNum = rom[offset++] & 0xFF;
				int level = rom[offset++] & 0xFF;
				Encounter enc = new Encounter();
				enc.pokemon = pokes[pokeNum];
				enc.level = level;
				es.encounters.add(enc);
			}
			areas.add(es);
		}

		// Headbutt Data
		offset = romEntry.getValue("HeadbuttWildsOffset");
		int limit = romEntry.getValue("HeadbuttTableSize");
		for (int i = 0; i < limit; i++) {
			EncounterSet es = new EncounterSet();
			while ((rom[offset] & 0xFF) != 0xFF) {
				offset++;
				int pokeNum = rom[offset++] & 0xFF;
				int level = rom[offset++] & 0xFF;
				Encounter enc = new Encounter();
				enc.pokemon = pokes[pokeNum];
				enc.level = level;
				es.encounters.add(enc);
			}
			offset++;
			areas.add(es);
		}

		return areas;
	}

	private int readLandEncounters(int offset, List<EncounterSet> areas,
			boolean useTimeOfDay) {
		while ((rom[offset] & 0xFF) != 0xFF) {
			if (useTimeOfDay) {
				for (int i = 0; i < 3; i++) {
					EncounterSet encset = new EncounterSet();
					encset.rate = rom[offset + 2 + i] & 0xFF;
					for (int j = 0; j < 7; j++) {
						Encounter enc = new Encounter();
						enc.level = rom[offset + 5 + (i * 14) + (j * 2)] & 0xFF;
						enc.maxLevel = 0;
						enc.pokemon = pokes[rom[offset + 5 + (i * 14) + (j * 2)
								+ 1] & 0xFF];
						encset.encounters.add(enc);
					}
					areas.add(encset);
				}
			} else {
				// Use Day only
				EncounterSet encset = new EncounterSet();
				encset.rate = rom[offset + 3] & 0xFF;
				for (int j = 0; j < 7; j++) {
					Encounter enc = new Encounter();
					enc.level = rom[offset + 5 + 14 + (j * 2)] & 0xFF;
					enc.maxLevel = 0;
					enc.pokemon = pokes[rom[offset + 5 + 14 + (j * 2) + 1] & 0xFF];
					encset.encounters.add(enc);
				}
				areas.add(encset);
			}
			offset += 47;
		}
		return offset + 1;
	}

	private int readSeaEncounters(int offset, List<EncounterSet> areas) {
		while ((rom[offset] & 0xFF) != 0xFF) {
			EncounterSet encset = new EncounterSet();
			encset.rate = rom[offset + 2] & 0xFF;
			for (int j = 0; j < 3; j++) {
				Encounter enc = new Encounter();
				enc.level = rom[offset + 3 + (j * 2)] & 0xFF;
				enc.maxLevel = 0;
				enc.pokemon = pokes[rom[offset + 3 + (j * 2) + 1] & 0xFF];
				encset.encounters.add(enc);
			}
			areas.add(encset);
			offset += 9;
		}
		return offset + 1;
	}

	@Override
	public void setEncounters(boolean useTimeOfDay,
			List<EncounterSet> encounters) {
		int offset = romEntry.getValue("WildPokemonOffset");
		Iterator<EncounterSet> areas = encounters.iterator();
		offset = writeLandEncounters(offset, areas, useTimeOfDay); // Johto
		offset = writeSeaEncounters(offset, areas); // Johto
		offset = writeLandEncounters(offset, areas, useTimeOfDay); // Kanto
		offset = writeSeaEncounters(offset, areas); // Kanto
		offset = writeLandEncounters(offset, areas, useTimeOfDay); // Specials
		offset = writeSeaEncounters(offset, areas); // Specials

		// Fishing Data
		offset = romEntry.getValue("FishingWildsOffset");
		for (int k = 0; k < 12; k++) {
			EncounterSet es = areas.next();
			Iterator<Encounter> encs = es.encounters.iterator();
			for (int i = 0; i < 11; i++) {
				if (i == 6 || i == 8) {
					offset += 3;
					continue;
				}
				offset++;
				Encounter enc = encs.next();
				rom[offset++] = (byte) enc.pokemon.number;
				rom[offset++] = (byte) enc.level;
			}
		}

		for (int k = 0; k < 11; k++) {
			EncounterSet es = areas.next();
			Iterator<Encounter> encs = es.encounters.iterator();
			for (int i = 0; i < 4; i++) {
				Encounter enc = encs.next();
				rom[offset++] = (byte) enc.pokemon.number;
				rom[offset++] = (byte) enc.level;
			}
		}

		// Headbutt Data
		offset = romEntry.getValue("HeadbuttWildsOffset");
		int limit = romEntry.getValue("HeadbuttTableSize");
		for (int i = 0; i < limit; i++) {
			EncounterSet es = areas.next();
			Iterator<Encounter> encs = es.encounters.iterator();
			while ((rom[offset] & 0xFF) != 0xFF) {
				Encounter enc = encs.next();
				offset++;
				rom[offset++] = (byte) enc.pokemon.number;
				rom[offset++] = (byte) enc.level;
			}
			offset++;
		}

	}

	private int writeLandEncounters(int offset, Iterator<EncounterSet> areas,
			boolean useTimeOfDay) {
		while ((rom[offset] & 0xFF) != 0xFF) {
			if (useTimeOfDay) {
				for (int i = 0; i < 3; i++) {
					EncounterSet encset = areas.next();
					Iterator<Encounter> encountersHere = encset.encounters
							.iterator();
					for (int j = 0; j < 7; j++) {
						rom[offset + 5 + (i * 14) + (j * 2) + 1] = (byte) encountersHere
								.next().pokemon.number;
					}
				}
			} else {
				// Write the set to all 3 equally
				EncounterSet encset = areas.next();
				for (int i = 0; i < 3; i++) {
					Iterator<Encounter> encountersHere = encset.encounters
							.iterator();
					for (int j = 0; j < 7; j++) {
						rom[offset + 5 + (i * 14) + (j * 2) + 1] = (byte) encountersHere
								.next().pokemon.number;
					}
				}
			}
			offset += 47;
		}
		return offset + 1;
	}

	private int writeSeaEncounters(int offset, Iterator<EncounterSet> areas) {
		while ((rom[offset] & 0xFF) != 0xFF) {
			EncounterSet encset = areas.next();
			Iterator<Encounter> encountersHere = encset.encounters.iterator();
			for (int j = 0; j < 3; j++) {
				rom[offset + 3 + (j * 2) + 1] = (byte) encountersHere.next().pokemon.number;
			}
			offset += 9;
		}
		return offset + 1;
	}

	@Override
	public List<Trainer> getTrainers() {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = romEntry.getValue("TrainerClassAmount");
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int pointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), pointer);
		}

		List<Trainer> allTrainers = new ArrayList<Trainer>();
		for (int i = 1; i <= traineramount; i++) {
			int offs = pointers[i];
			int limit = trainerclasslimits[i];
			for (int trnum = 0; trnum < limit; trnum++) {
				Trainer tr = new Trainer();
				tr.offset = offs;
				tr.trainerclass = i;
				String name = readVariableLengthString(offs);
				tr.name = name;
				offs += name.length() + 1;
				int dataType = rom[offs] & 0xFF;
				tr.poketype = dataType;
				offs++;
				while ((rom[offs] & 0xFF) != 0xFF) {
					TrainerPokemon tp = new TrainerPokemon();
					tp.level = rom[offs] & 0xFF;
					tp.pokemon = pokes[rom[offs + 1] & 0xFF];
					offs += 2;
					if (dataType == 2 || dataType == 3) {
						tp.heldItem = rom[offs] & 0xFF;
						offs++;
					}
					if (dataType % 2 == 1) {
						tp.move1 = rom[offs] & 0xFF;
						tp.move2 = rom[offs + 1] & 0xFF;
						tp.move3 = rom[offs + 2] & 0xFF;
						tp.move4 = rom[offs + 3] & 0xFF;
						offs += 4;
					}
					tr.pokemon.add(tp);
				}
				allTrainers.add(tr);
				offs++;
			}
		}
		universalTrainerTags(allTrainers);
		if (romEntry.isCrystal) {
			crystalTags(allTrainers);
		} else {
			goldSilverTags(allTrainers);
		}
		return allTrainers;
	}

	@Override
	public void setTrainers(List<Trainer> trainerData) {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = romEntry.getValue("TrainerClassAmount");
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int pointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), pointer);
		}

		Iterator<Trainer> allTrainers = trainerData.iterator();
		for (int i = 1; i <= traineramount; i++) {
			int offs = pointers[i];
			int limit = trainerclasslimits[i];
			for (int trnum = 0; trnum < limit; trnum++) {
				Trainer tr = allTrainers.next();
				if (tr.trainerclass != i) {
					System.err.println("Trainer mismatch: " + tr.name);
				}
				// Write their name
				writeFixedLengthString(tr.name, offs, tr.name.length() + 1);
				offs += tr.name.length() + 1;
				// Poketype
				tr.poketype = 0; // remove held items and moves
				rom[offs++] = (byte) tr.poketype;
				Iterator<TrainerPokemon> tPokes = tr.pokemon.iterator();
				for (int tpnum = 0; tpnum < tr.pokemon.size(); tpnum++) {
					TrainerPokemon tp = tPokes.next();
					rom[offs] = (byte) tp.level;
					rom[offs + 1] = (byte) tp.pokemon.number;
					offs += 2;
					if (tr.poketype == 2 || tr.poketype == 3) {
						rom[offs] = (byte) tp.heldItem;
						offs++;
					}
					if (tr.poketype % 2 == 1) {
						rom[offs] = (byte) tp.move1;
						rom[offs + 1] = (byte) tp.move2;
						rom[offs + 2] = (byte) tp.move3;
						rom[offs + 3] = (byte) tp.move4;
						offs += 4;
					}
				}
				rom[offs] = (byte) 0xFF;
				offs++;
			}
		}

	}

	private void universalTrainerTags(List<Trainer> allTrainers) {
		// Gym Leaders
		tbc(allTrainers, 1, 0, "GYM1");
		tbc(allTrainers, 3, 0, "GYM2");
		tbc(allTrainers, 2, 0, "GYM3");
		tbc(allTrainers, 4, 0, "GYM4");
		tbc(allTrainers, 7, 0, "GYM5");
		tbc(allTrainers, 6, 0, "GYM6");
		tbc(allTrainers, 5, 0, "GYM7");
		tbc(allTrainers, 8, 0, "GYM8");
		tbc(allTrainers, 17, 0, "GYM9");
		tbc(allTrainers, 18, 0, "GYM10");
		tbc(allTrainers, 19, 0, "GYM11");
		tbc(allTrainers, 21, 0, "GYM12");
		tbc(allTrainers, 26, 0, "GYM13");
		tbc(allTrainers, 35, 0, "GYM14");
		tbc(allTrainers, 46, 0, "GYM15");
		tbc(allTrainers, 64, 0, "GYM16");

		// Elite 4 & Red
		tbc(allTrainers, 11, 0, "ELITE1");
		tbc(allTrainers, 15, 0, "ELITE2");
		tbc(allTrainers, 13, 0, "ELITE3");
		tbc(allTrainers, 14, 0, "ELITE4");
		tbc(allTrainers, 16, 0, "CHAMPION");
		tbc(allTrainers, 63, 0, "UBER");

		// Silver
		// Order in rom is BAYLEEF, QUILAVA, CROCONAW teams
		// Starters go CYNDA, TOTO, CHIKO
		// So we want 0=CROCONAW/FERALI, 1=BAYLEEF/MEGAN, 2=QUILAVA/TYPHLO
		tbc(allTrainers, 9, 0, "RIVAL1-1");
		tbc(allTrainers, 9, 1, "RIVAL1-2");
		tbc(allTrainers, 9, 2, "RIVAL1-0");

		tbc(allTrainers, 9, 3, "RIVAL2-1");
		tbc(allTrainers, 9, 4, "RIVAL2-2");
		tbc(allTrainers, 9, 5, "RIVAL2-0");

		tbc(allTrainers, 9, 6, "RIVAL3-1");
		tbc(allTrainers, 9, 7, "RIVAL3-2");
		tbc(allTrainers, 9, 8, "RIVAL3-0");

		tbc(allTrainers, 9, 9, "RIVAL4-1");
		tbc(allTrainers, 9, 10, "RIVAL4-2");
		tbc(allTrainers, 9, 11, "RIVAL4-0");

		tbc(allTrainers, 9, 12, "RIVAL5-1");
		tbc(allTrainers, 9, 13, "RIVAL5-2");
		tbc(allTrainers, 9, 14, "RIVAL5-0");

		tbc(allTrainers, 42, 0, "RIVAL6-1");
		tbc(allTrainers, 42, 1, "RIVAL6-2");
		tbc(allTrainers, 42, 2, "RIVAL6-0");

		tbc(allTrainers, 42, 3, "RIVAL7-1");
		tbc(allTrainers, 42, 4, "RIVAL7-2");
		tbc(allTrainers, 42, 5, "RIVAL7-0");

		// Female Rocket Executive (Ariana)
		tbc(allTrainers, 55, 0, "THEMED:ARIANA");
		tbc(allTrainers, 55, 1, "THEMED:ARIANA");

		// others (unlabeled in this game, using HGSS names)
		tbc(allTrainers, 51, 2, "THEMED:PETREL");
		tbc(allTrainers, 51, 3, "THEMED:PETREL");

		tbc(allTrainers, 51, 1, "THEMED:PROTON");
		tbc(allTrainers, 31, 0, "THEMED:PROTON");

		// Sprout Tower
		tbc(allTrainers, 56, 0, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 1, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 2, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 3, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 6, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 7, "THEMED:SPROUTTOWER");
		tbc(allTrainers, 56, 8, "THEMED:SPROUTTOWER");
	}

	private void goldSilverTags(List<Trainer> allTrainers) {
		tbc(allTrainers, 24, 0, "GYM1");
		tbc(allTrainers, 24, 1, "GYM1");
		tbc(allTrainers, 36, 4, "GYM2");
		tbc(allTrainers, 36, 5, "GYM2");
		tbc(allTrainers, 36, 6, "GYM2");
		tbc(allTrainers, 61, 0, "GYM2");
		tbc(allTrainers, 61, 3, "GYM2");
		tbc(allTrainers, 25, 0, "GYM3");
		tbc(allTrainers, 25, 1, "GYM3");
		tbc(allTrainers, 29, 0, "GYM3");
		tbc(allTrainers, 29, 1, "GYM3");
		tbc(allTrainers, 56, 4, "GYM4");
		tbc(allTrainers, 56, 5, "GYM4");
		tbc(allTrainers, 57, 0, "GYM4");
		tbc(allTrainers, 57, 1, "GYM4");
		tbc(allTrainers, 50, 1, "GYM5");
		tbc(allTrainers, 50, 3, "GYM5");
		tbc(allTrainers, 50, 4, "GYM5");
		tbc(allTrainers, 50, 6, "GYM5");
		tbc(allTrainers, 58, 0, "GYM7");
		tbc(allTrainers, 58, 1, "GYM7");
		tbc(allTrainers, 58, 2, "GYM7");
		tbc(allTrainers, 33, 0, "GYM7");
		tbc(allTrainers, 33, 1, "GYM7");
		tbc(allTrainers, 27, 2, "GYM8");
		tbc(allTrainers, 27, 4, "GYM8");
		tbc(allTrainers, 27, 3, "GYM8");
		tbc(allTrainers, 28, 2, "GYM8");
		tbc(allTrainers, 28, 3, "GYM8");
		tbc(allTrainers, 54, 17, "GYM9");
		tbc(allTrainers, 38, 20, "GYM10");
		tbc(allTrainers, 39, 17, "GYM10");
		tbc(allTrainers, 39, 18, "GYM10");
		tbc(allTrainers, 49, 2, "GYM11");
		tbc(allTrainers, 43, 1, "GYM11");
		tbc(allTrainers, 32, 2, "GYM11");
		tbc(allTrainers, 61, 4, "GYM12");
		tbc(allTrainers, 61, 5, "GYM12");
		tbc(allTrainers, 25, 8, "GYM12");
		tbc(allTrainers, 53, 18, "GYM12");
		tbc(allTrainers, 29, 13, "GYM12");
		tbc(allTrainers, 25, 2, "GYM13");
		tbc(allTrainers, 25, 5, "GYM13");
		tbc(allTrainers, 53, 4, "GYM13");
		tbc(allTrainers, 54, 4, "GYM13");
		tbc(allTrainers, 57, 5, "GYM14");
		tbc(allTrainers, 57, 6, "GYM14");
		tbc(allTrainers, 52, 1, "GYM14");
		tbc(allTrainers, 52, 10, "GYM14");
	}

	private void crystalTags(List<Trainer> allTrainers) {
		tbc(allTrainers, 24, 0, "GYM1");
		tbc(allTrainers, 24, 1, "GYM1");
		tbc(allTrainers, 36, 4, "GYM2");
		tbc(allTrainers, 36, 5, "GYM2");
		tbc(allTrainers, 36, 6, "GYM2");
		tbc(allTrainers, 61, 0, "GYM2");
		tbc(allTrainers, 61, 3, "GYM2");
		tbc(allTrainers, 25, 0, "GYM3");
		tbc(allTrainers, 25, 1, "GYM3");
		tbc(allTrainers, 29, 0, "GYM3");
		tbc(allTrainers, 29, 1, "GYM3");
		tbc(allTrainers, 56, 4, "GYM4");
		tbc(allTrainers, 56, 5, "GYM4");
		tbc(allTrainers, 57, 0, "GYM4");
		tbc(allTrainers, 57, 1, "GYM4");
		tbc(allTrainers, 50, 1, "GYM5");
		tbc(allTrainers, 50, 3, "GYM5");
		tbc(allTrainers, 50, 4, "GYM5");
		tbc(allTrainers, 50, 6, "GYM5");
		tbc(allTrainers, 58, 0, "GYM7");
		tbc(allTrainers, 58, 1, "GYM7");
		tbc(allTrainers, 58, 2, "GYM7");
		tbc(allTrainers, 33, 0, "GYM7");
		tbc(allTrainers, 33, 1, "GYM7");
		tbc(allTrainers, 27, 2, "GYM8");
		tbc(allTrainers, 27, 4, "GYM8");
		tbc(allTrainers, 27, 3, "GYM8");
		tbc(allTrainers, 28, 2, "GYM8");
		tbc(allTrainers, 28, 3, "GYM8");
		tbc(allTrainers, 54, 17, "GYM9");
		tbc(allTrainers, 38, 20, "GYM10");
		tbc(allTrainers, 39, 17, "GYM10");
		tbc(allTrainers, 39, 18, "GYM10");
		tbc(allTrainers, 49, 2, "GYM11");
		tbc(allTrainers, 43, 1, "GYM11");
		tbc(allTrainers, 32, 2, "GYM11");
		tbc(allTrainers, 61, 4, "GYM12");
		tbc(allTrainers, 61, 5, "GYM12");
		tbc(allTrainers, 25, 8, "GYM12");
		tbc(allTrainers, 53, 18, "GYM12");
		tbc(allTrainers, 29, 13, "GYM12");
		tbc(allTrainers, 25, 2, "GYM13");
		tbc(allTrainers, 25, 5, "GYM13");
		tbc(allTrainers, 53, 4, "GYM13");
		tbc(allTrainers, 54, 4, "GYM13");
		tbc(allTrainers, 57, 5, "GYM14");
		tbc(allTrainers, 57, 6, "GYM14");
		tbc(allTrainers, 52, 1, "GYM14");
		tbc(allTrainers, 52, 10, "GYM14");
	}

	private void tbc(List<Trainer> allTrainers, int classNum, int number,
			String tag) {
		int currnum = -1;
		for (Trainer t : allTrainers) {
			if (t.trainerclass == classNum) {
				currnum++;
				if (currnum == number) {
					t.tag = tag;
					return;
				}
			}
		}
	}

	@Override
	public List<Pokemon> getPokemon() {
		return Arrays.asList(pokes);
	}

	@Override
	public Map<Pokemon, List<MoveLearnt>> getMovesLearnt() {
		Map<Pokemon, List<MoveLearnt>> movesets = new TreeMap<Pokemon, List<MoveLearnt>>();
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		for (int i = 1; i <= 251; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			Pokemon pkmn = pokes[i];
			// Skip over evolution data
			while (rom[realPointer] != 0) {
				if (rom[realPointer] == 5) {
					realPointer += 4;
				} else {
					realPointer += 3;
				}
			}
			List<MoveLearnt> ourMoves = new ArrayList<MoveLearnt>();
			realPointer++;
			while (rom[realPointer] != 0) {
				MoveLearnt learnt = new MoveLearnt();
				learnt.level = rom[realPointer] & 0xFF;
				learnt.move = rom[realPointer + 1] & 0xFF;
				ourMoves.add(learnt);
				realPointer += 2;
			}
			movesets.put(pkmn, ourMoves);
		}
		return movesets;
	}

	@Override
	public void setMovesLearnt(Map<Pokemon, List<MoveLearnt>> movesets) {
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		for (int i = 1; i <= 251; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			Pokemon pkmn = pokes[i];
			List<MoveLearnt> ourMoves = movesets.get(pkmn);
			int movenum = 0;
			// Skip over evolution data
			while (rom[realPointer] != 0) {
				if (rom[realPointer] == 5) {
					realPointer += 4;
				} else {
					realPointer += 3;
				}
			}
			realPointer++;
			while (rom[realPointer] != 0 && movenum < ourMoves.size()) {
				rom[realPointer] = (byte) ourMoves.get(movenum).level;
				rom[realPointer + 1] = (byte) ourMoves.get(movenum).move;
				realPointer += 2;
				movenum++;
			}
			// Make sure we finish off the moveset
			rom[realPointer] = 0;
		}
	}

	@Override
	public List<Pokemon> getStaticPokemon() {
		List<Pokemon> statics = new ArrayList<Pokemon>();
		if (romEntry.getValue("StaticPokemonSupport") > 0) {
			for (int offset : romEntry.staticPokemonSingle) {
				statics.add(pokes[rom[offset] & 0xFF]);
			}
			// Game Corner
			for (int offset : romEntry.staticPokemonGameCorner.keySet()) {
				statics.add(pokes[rom[offset] & 0xFF]);
			}
		}
		return statics;
	}

	@Override
	public boolean setStaticPokemon(List<Pokemon> staticPokemon) {
		if (romEntry.getValue("StaticPokemonSupport") == 0) {
			return false;
		}
		if (staticPokemon.size() != romEntry.staticPokemonSingle.size()
				+ romEntry.staticPokemonGameCorner.size()) {
			return false;
		}
		for (Pokemon pkmn : staticPokemon) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}

		Iterator<Pokemon> statics = staticPokemon.iterator();
		for (int offset : romEntry.staticPokemonSingle) {
			rom[offset] = (byte) statics.next().number;
		}

		int gcNameLength = romEntry.getValue("GameCornerPokemonNameLength");

		// Sort out static Pokemon
		for (int offset : romEntry.staticPokemonGameCorner.keySet()) {
			rom[offset] = (byte) statics.next().number;
			rom[offset + 0x11] = rom[offset];
			rom[offset + 0x16] = rom[offset];
			writePaddedPokemonName(pokes[rom[offset] & 0xFF].name,
					gcNameLength, romEntry.staticPokemonGameCorner.get(offset));
		}

		// Copies?
		for (int offset : romEntry.staticPokemonCopy.keySet()) {
			int copyTo = romEntry.staticPokemonCopy.get(offset);
			rom[copyTo] = rom[offset];
		}
		return true;
	}

	@Override
	public boolean canChangeStaticPokemon() {
		return (romEntry.getValue("StaticPokemonSupport") > 0);
	}

	@Override
	public List<Pokemon> bannedForStaticPokemon() {
		return Arrays.asList(pokes[201]); // Unown banned
	}

	private void writePaddedPokemonName(String name, int length, int offset) {
		String paddedName = String.format("%-" + length + "s", name);
		byte[] rawData = traduire(paddedName);
		for (int i = 0; i < length; i++) {
			rom[offset + i] = rawData[i];
		}
	}

	@Override
	public List<Integer> getTMMoves() {
		List<Integer> tms = new ArrayList<Integer>();
		int offset = romEntry.getValue("TMMovesOffset");
		for (int i = 1; i <= 50; i++) {
			tms.add(rom[offset + (i - 1)] & 0xFF);
		}
		return tms;
	}

	@Override
	public List<Integer> getHMMoves() {
		List<Integer> hms = new ArrayList<Integer>();
		int offset = romEntry.getValue("TMMovesOffset");
		for (int i = 1; i <= 7; i++) {
			hms.add(rom[offset + 50 + (i - 1)] & 0xFF);
		}
		return hms;
	}

	@Override
	public void setTMMoves(List<Integer> moveIndexes) {
		int offset = romEntry.getValue("TMMovesOffset");
		for (int i = 1; i <= 50; i++) {
			rom[offset + (i - 1)] = moveIndexes.get(i - 1).byteValue();
		}
	}

	@Override
	public int getTMCount() {
		return 50;
	}

	@Override
	public int getHMCount() {
		return 7;
	}

	@Override
	public Map<Pokemon, boolean[]> getTMHMCompatibility() {
		Map<Pokemon, boolean[]> compat = new TreeMap<Pokemon, boolean[]>();
		for (int i = 1; i <= 251; i++) {
			int baseStatsOffset = romEntry.getValue("PokemonStatsOffset")
					+ (i - 1) * 0x20;
			Pokemon pkmn = pokes[i];
			boolean[] flags = new boolean[58];
			for (int j = 0; j < 8; j++) {
				readByteIntoFlags(flags, j * 8 + 1, baseStatsOffset + 0x18 + j);
			}
			compat.put(pkmn, flags);
		}
		return compat;
	}

	@Override
	public void setTMHMCompatibility(Map<Pokemon, boolean[]> compatData) {
		for (Map.Entry<Pokemon, boolean[]> compatEntry : compatData.entrySet()) {
			Pokemon pkmn = compatEntry.getKey();
			boolean[] flags = compatEntry.getValue();
			int baseStatsOffset = romEntry.getValue("PokemonStatsOffset")
					+ (pkmn.number - 1) * 0x20;
			for (int j = 0; j < 8; j++) {
				if (!romEntry.isCrystal || j != 7) {
					rom[baseStatsOffset + 0x18 + j] = getByteFromFlags(flags,
							j * 8 + 1);
				} else {
					// Move tutor data
					// bits 1,2,3 of byte 7
					int changedByte = getByteFromFlags(flags, j * 8 + 1) & 0xFF;
					int currentByte = rom[baseStatsOffset + 0x18 + j];
					changedByte |= ((currentByte >> 1) & 0x01) << 1;
					changedByte |= ((currentByte >> 2) & 0x01) << 2;
					changedByte |= ((currentByte >> 3) & 0x01) << 3;
					rom[baseStatsOffset + 0x18 + j] = (byte) changedByte;
				}
			}
		}
	}

	@Override
	public boolean hasMoveTutors() {
		return romEntry.isCrystal;
	}

	@Override
	public List<Integer> getMoveTutorMoves() {
		if (romEntry.isCrystal) {
			List<Integer> mtMoves = new ArrayList<Integer>();
			for (int offset : romEntry.arrayEntries.get("MoveTutorMoves")) {
				mtMoves.add(rom[offset] & 0xFF);
			}
			return mtMoves;
		}
		return new ArrayList<Integer>();
	}

	@Override
	public void setMoveTutorMoves(List<Integer> moves) {
		if (!romEntry.isCrystal) {
			return;
		}
		if (moves.size() != 3) {
			return;
		}
		Iterator<Integer> mvList = moves.iterator();
		for (int offset : romEntry.arrayEntries.get("MoveTutorMoves")) {
			rom[offset] = mvList.next().byteValue();
		}

		// Construct a new menu
		String[] names = new String[] { RomFunctions.moveNames[moves.get(0)],
				RomFunctions.moveNames[moves.get(1)],
				RomFunctions.moveNames[moves.get(2)], "CANCEL" };
		int menuOffset = romEntry.getValue("MoveTutorMenuNewSpace");
		rom[menuOffset++] = (byte) 0x80;
		rom[menuOffset++] = 0x4;
		for (int i = 0; i < 4; i++) {
			byte[] trans = traduire(names[i]);
			System.arraycopy(trans, 0, rom, menuOffset, trans.length);
			menuOffset += trans.length;
			rom[menuOffset++] = 0x50;
		}
		int pointerOffset = romEntry.getValue("MoveTutorMenuOffset");
		writeWord(pointerOffset,
				makeGBPointer(romEntry.getValue("MoveTutorMenuNewSpace")));
	}

	@Override
	public Map<Pokemon, boolean[]> getMoveTutorCompatibility() {
		if (!romEntry.isCrystal) {
			return new TreeMap<Pokemon, boolean[]>();
		}
		Map<Pokemon, boolean[]> compat = new TreeMap<Pokemon, boolean[]>();
		for (int i = 1; i <= 251; i++) {
			int baseStatsOffset = romEntry.getValue("PokemonStatsOffset")
					+ (i - 1) * 0x20;
			Pokemon pkmn = pokes[i];
			boolean[] flags = new boolean[4];
			int mtByte = rom[baseStatsOffset + 0x1F] & 0xFF;
			for (int j = 1; j <= 3; j++) {
				flags[j] = ((mtByte >> j) & 0x01) > 0;
			}
			compat.put(pkmn, flags);
		}
		return compat;
	}

	@Override
	public void setMoveTutorCompatibility(Map<Pokemon, boolean[]> compatData) {
		if (!romEntry.isCrystal) {
			return;
		}
		for (Map.Entry<Pokemon, boolean[]> compatEntry : compatData.entrySet()) {
			Pokemon pkmn = compatEntry.getKey();
			boolean[] flags = compatEntry.getValue();
			int baseStatsOffset = romEntry.getValue("PokemonStatsOffset")
					+ (pkmn.number - 1) * 0x20;
			int origMtByte = rom[baseStatsOffset + 0x1F] & 0xFF;
			int mtByte = origMtByte & 0x01;
			for (int j = 1; j <= 3; j++) {
				mtByte |= flags[j] ? (1 << j) : 0;
			}
			rom[baseStatsOffset + 0x1F] = (byte) mtByte;
		}
	}

	@Override
	public String getROMName() {
		return "Pokemon " + romEntry.name;
	}

	@Override
	public String getROMCode() {
		return romEntry.romCode;
	}

	@Override
	public String getSupportLevel() {
		return "Complete";
	}

	@Override
	public boolean hasTimeBasedEncounters() {
		return true; // All GSC do
	}

	@Override
	public void removeTradeEvolutions() {
		// Evolution data is stored before learnmoves.
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		log("--Removing Trade Evolutions--");
		for (int i = 1; i <= 251; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			// Evolution data
			// Type 1: 01 LV PK
			// Type 2: 02 ITEM PK
			// Type 3: 03 ITEM/FF PK (trade, FF for no item reqd)
			// Type 4: 04 HAPPCOND PK
			// Type 5: 05 LV TYROGUECOND PK
			// HAPPCOND: 01 none 02 day only 03 night only
			// TYROGUECOND: 01 Attack>Defense 02 Attack<Defense
			// 03 Attack=Defense

			while (rom[realPointer] != 0) {
				if (rom[realPointer] == 5) {
					realPointer += 4;
				} else if (rom[realPointer] == 3) {
					// What to do?
					if (i == 79) {
						// Slowpoke: Make water stone => Slowking
						log("Made Slowpoke evolve into Slowking using a Water Stone");
						rom[realPointer] = 2;
						rom[realPointer + 1] = 0x18;
					} else if (i == 117) {
						// Seadra: level 40
						log("Made Seadra evolve into Kingdra at level 40");
						rom[realPointer] = 1;
						rom[realPointer + 1] = 40;
					} else if (i == 61 || (rom[realPointer + 1] & 0xFF) == 0xFF) {
						// Poliwhirl or any of the original 4 trade evos
						// Level 37
						log("Made " + pokes[i].name + " evolve into "
								+ pokes[rom[realPointer + 2] & 0xFF].name
								+ " at level 37");
						rom[realPointer] = 1;
						rom[realPointer + 1] = 37;
					} else {
						// A new trade evo of a single stage Pokemon
						// level 30
						log("Made " + pokes[i].name + " evolve into "
								+ pokes[rom[realPointer + 2] & 0xFF].name
								+ " at level 30");
						rom[realPointer] = 1;
						rom[realPointer + 1] = 30;
					}
					// Regardless of whatever we did, advance past it
					realPointer += 3;
				} else {
					realPointer += 3;
				}
			}
		}
		logBlankLine();

	}

	@Override
	public List<String> getTrainerNames() {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = romEntry.getValue("TrainerClassAmount");
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int pointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), pointer);
		}

		List<String> allTrainers = new ArrayList<String>();
		for (int i = 1; i <= traineramount; i++) {
			int offs = pointers[i];
			int limit = trainerclasslimits[i];
			for (int trnum = 0; trnum < limit; trnum++) {
				String name = readVariableLengthString(offs);
				allTrainers.add(name);
				offs += name.length() + 1;
				int dataType = rom[offs] & 0xFF;
				offs++;
				while ((rom[offs] & 0xFF) != 0xFF) {
					offs += 2;
					if (dataType == 2 || dataType == 3) {
						offs++;
					}
					if (dataType % 2 == 1) {
						offs += 4;
					}
				}
				offs++;
			}
		}
		return allTrainers;
	}

	@Override
	public void setTrainerNames(List<String> trainerNames) {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = romEntry.getValue("TrainerClassAmount");
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int pointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), pointer);
		}

		Iterator<String> allTrainers = trainerNames.iterator();
		for (int i = 1; i <= traineramount; i++) {
			int offs = pointers[i];
			int limit = trainerclasslimits[i];
			for (int trnum = 0; trnum < limit; trnum++) {
				String name = readVariableLengthString(offs);
				String newName = allTrainers.next();
				writeFixedLengthString(newName, offs, name.length() + 1);
				offs += name.length() + 1;
				int dataType = rom[offs] & 0xFF;
				offs++;
				while ((rom[offs] & 0xFF) != 0xFF) {
					offs += 2;
					if (dataType == 2 || dataType == 3) {
						offs++;
					}
					if (dataType % 2 == 1) {
						offs += 4;
					}
				}
				offs++;
			}
		}

	}

	@Override
	public boolean fixedTrainerNamesLength() {
		return true;
	}

	@Override
	public List<String> getTrainerClassNames() {
		int amount = romEntry.getValue("TrainerClassAmount") - 1;
		int offset = romEntry.getValue("TrainerClassNamesOffset");
		List<String> trainerClassNames = new ArrayList<String>();
		for (int j = 0; j < amount; j++) {
			String name = readVariableLengthString(offset);
			offset += lengthOfStringAt(offset) + 1;
			trainerClassNames.add(name);
		}
		return trainerClassNames;
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		int amount = romEntry.getValue("TrainerClassAmount") - 1;
		int offset = romEntry.getValue("TrainerClassNamesOffset");
		Iterator<String> trainerClassNamesI = trainerClassNames.iterator();
		for (int j = 0; j < amount; j++) {
			int len = lengthOfStringAt(offset) + 1;
			String newName = trainerClassNamesI.next();
			writeFixedLengthString(newName, offset, len);
			offset += len;
		}
	}

	@Override
	public boolean fixedTrainerClassNamesLength() {
		return true;
	}

	@Override
	public String getDefaultExtension() {
		return "gbc";
	}

	@Override
	public int abilitiesPerPokemon() {
		return 0;
	}

	@Override
	public int highestAbilityIndex() {
		return 0;
	}

	@Override
	public int internalStringLength(String string) {
		return translateString(string).length;
	}

	@Override
	public boolean hasBWEXPPatch() {
		return romEntry.expPatch != null;
	}

	@Override
	public void applyBWEXPPatch() {
		if (romEntry.expPatch == null) {
			return;
		}

		if (romEntry.expPatch.equals("gsEN")) {
			// jump for XP addition
			writeHexString("C3EE6C", 0x3ED44);
			// jump for # of party members
			writeHexString("C3007F", 0x3EF0D);
			// # of party members
			writeHexString("EAFBD0FE02D8EA51D1C3136F", 0x3FF00);
			// display of EXP num
			writeHexString("F1D038", 0x191E29);
			writeHexString("F1D038", 0x191E3F);
			// cut down EXP text to fit with 8 nums
			writeHexString("E758", 0x191E31);
			writeHexString("E758", 0x191E47);

			// main code
			try {
				byte[] patchboot = FileFunctions
						.getXPPatchFile("gs_patch_boot.bin");
				System.arraycopy(patchboot, 0, rom, 0x3ECE1, patchboot.length);
				byte[] patchmain = FileFunctions
						.getXPPatchFile("gs_patch_main.bin");
				System.arraycopy(patchmain, 0, rom, 0x16AB00, patchmain.length);
			} catch (IOException e) {
				return;
			}
		} else if (romEntry.expPatch.equals("crystalEN")) {
			// jump for XP addition
			writeHexString("C3BB6E", 0x3EF11);
			// jump for # of party members
			writeHexString("C3007F", 0x3F0E4);
			// # of party members
			writeHexString("EA12D2FE02D8EA65D2C3EA70", 0x3FF00);
			// display of EXP num
			writeHexString("08D238", 0x1C02B7);
			writeHexString("08D238", 0x1C02CD);
			// cut down EXP text to fit with 8 nums
			writeHexString("E758", 0x1C02BF);
			writeHexString("E758", 0x1C02D5);

			// main code
			try {
				byte[] patchboot = FileFunctions
						.getXPPatchFile("crystal_patch_boot.bin");
				System.arraycopy(patchboot, 0, rom, 0x3EEAE, patchboot.length);
				byte[] patchmain = FileFunctions
						.getXPPatchFile("crystal_patch_main.bin");
				System.arraycopy(patchmain, 0, rom, 0x16AB00, patchmain.length);
			} catch (IOException e) {
				return;
			}
		}
	}

	private void writeHexString(String hexString, int offset) {
		if (hexString.length() % 2 != 0) {
			return; // error
		}
		for (int i = 0; i < hexString.length() / 2; i++) {
			rom[offset + i] = (byte) Integer.parseInt(
					hexString.substring(i * 2, i * 2 + 2), 16);
		}
	}

}
