package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  Gen1RomHandler.java - randomizer handler for R/B/Y.					--*/
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

public class Gen1RomHandler extends AbstractGBRomHandler {

	// Important RBY Data Structures

	private static final int[] pokeNumToRBYTable = new int[] { 0, 153, 9, 154,
			176, 178, 180, 177, 179, 28, 123, 124, 125, 112, 113, 114, 36, 150,
			151, 165, 166, 5, 35, 108, 45, 84, 85, 96, 97, 15, 168, 16, 3, 167,
			7, 4, 142, 82, 83, 100, 101, 107, 130, 185, 186, 187, 109, 46, 65,
			119, 59, 118, 77, 144, 47, 128, 57, 117, 33, 20, 71, 110, 111, 148,
			38, 149, 106, 41, 126, 188, 189, 190, 24, 155, 169, 39, 49, 163,
			164, 37, 8, 173, 54, 64, 70, 116, 58, 120, 13, 136, 23, 139, 25,
			147, 14, 34, 48, 129, 78, 138, 6, 141, 12, 10, 17, 145, 43, 44, 11,
			55, 143, 18, 1, 40, 30, 2, 92, 93, 157, 158, 27, 152, 42, 26, 72,
			53, 51, 29, 60, 133, 22, 19, 76, 102, 105, 104, 103, 170, 98, 99,
			90, 91, 171, 132, 74, 75, 73, 88, 89, 66, 131, 21, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private static final int[] pokeRBYToNumTable = new int[] { 0, 112, 115, 32,
			35, 21, 100, 34, 80, 2, 103, 108, 102, 88, 94, 29, 31, 104, 111,
			131, 59, 151, 130, 90, 72, 92, 123, 120, 9, 127, 114, 0, 0, 58, 95,
			22, 16, 79, 64, 75, 113, 67, 122, 106, 107, 24, 47, 54, 96, 76, 0,
			126, 0, 125, 82, 109, 0, 56, 86, 50, 128, 0, 0, 0, 83, 48, 149, 0,
			0, 0, 84, 60, 124, 146, 144, 145, 132, 52, 98, 0, 0, 0, 37, 38, 25,
			26, 0, 0, 147, 148, 140, 141, 116, 117, 0, 0, 27, 28, 138, 139, 39,
			40, 133, 136, 135, 134, 66, 41, 23, 46, 61, 62, 13, 14, 15, 0, 85,
			57, 51, 49, 87, 0, 0, 10, 11, 12, 68, 0, 55, 97, 42, 150, 143, 129,
			0, 0, 89, 0, 99, 91, 0, 101, 36, 110, 53, 105, 0, 93, 63, 65, 17,
			18, 121, 1, 3, 73, 0, 118, 119, 0, 0, 0, 0, 77, 78, 19, 20, 33, 30,
			74, 137, 142, 0, 81, 0, 0, 4, 7, 5, 8, 6, 0, 0, 0, 0, 43, 44, 45,
			69, 70, 71, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0 };

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
		table[0x14] = Type.FIRE;
		table[0x15] = Type.WATER;
		table[0x16] = Type.GRASS;
		table[0x17] = Type.ELECTRIC;
		table[0x18] = Type.PSYCHIC;
		table[0x19] = Type.ICE;
		table[0x1A] = Type.DRAGON;
		return table;
	}

	private static byte typeToByte(Type type) {
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
			return 0x17; // turn steel into electric, to account for
							// magnemite/magneton
		case DARK:
			return 0x08; // turn dark into ghost
		}
		return 0; // normal by default
	}

	private static class RomEntry {
		private String name;
		private String romName;
		private int version, nonJapanese;
		private String tableFile;
		private boolean isYellow;
		private int crcInHeader = -1;
		private String expPatch;
		private Map<String, Integer> entries = new HashMap<String, Integer>();
		private Map<String, int[]> arrayEntries = new HashMap<String, int[]>();
		private List<Integer> staticPokemonSingle = new ArrayList<Integer>();
		private List<GameCornerPokemon> staticPokemonGameCorner = new ArrayList<GameCornerPokemon>();

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

	private static class GameCornerPokemon {
		private int[] offsets;

		public String toString() {
			return Arrays.toString(offsets);
		}
	}

	private static void loadROMInfo() {
		roms = new ArrayList<RomEntry>();
		RomEntry current = null;
		try {
			Scanner sc = new Scanner(
					FileFunctions.openConfig("gen1_offsets.ini"), "UTF-8");
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
							if (r[1].startsWith("[") && r[1].endsWith("]")) {
								String[] offsets = r[1].substring(1,
										r[1].length() - 1).split(",");
								int[] offs = new int[offsets.length];
								int c = 0;
								for (String off : offsets) {
									offs[c++] = parseRIInt(off);
								}
								GameCornerPokemon gc = new GameCornerPokemon();
								gc.offsets = offs;
								current.staticPokemonGameCorner.add(gc);
							} else {
								int offs = parseRIInt(r[1]);
								GameCornerPokemon gc = new GameCornerPokemon();
								gc.offsets = new int[] { offs };
								current.staticPokemonGameCorner.add(gc);
							}
						} else if (r[0].equals("Game")) {
							current.romName = r[1];
						} else if (r[0].equals("Version")) {
							current.version = parseRIInt(r[1]);
						} else if (r[0].equals("NonJapanese")) {
							current.nonJapanese = parseRIInt(r[1]);
						} else if (r[0].equals("Type")) {
							if (r[1].equalsIgnoreCase("Yellow")) {
								current.isYellow = true;
							} else {
								current.isYellow = false;
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
												.addAll(otherEntry.staticPokemonGameCorner);
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
		if (rom.length > 1048576) {
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
			if (romSig(rom, re.romName) && re.version == version
					&& re.nonJapanese == nonjap
					&& re.crcInHeader == crcInHeader) {
				return re;
			}
		}
		// Now check for non-specific-CRC entries
		for (RomEntry re : roms) {
			if (romSig(rom, re.romName) && re.version == version
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
		for (int i = 1; i <= 151; i++) {
			System.out.println(pokes[i].toStringRBY());
		}
	}

	public void printMoves() {
		for (int i = 1; i <= 165; i++) {
			System.out.println(moves[i]);
		}
	}

	private void loadMoves() {
		moves = new Move[166];
		int movesOffset = romEntry.getValue("MoveDataOffset");
		for (int i = 1; i <= 165; i++) {
			moves[i] = new Move();
			moves[i].name = RomFunctions.moveNames[i];
			moves[i].number = i;
			moves[i].effectIndex = rom[movesOffset + (i - 1) * 6 + 1] & 0xFF;
			moves[i].hitratio = ((rom[movesOffset + (i - 1) * 6 + 4] & 0xFF) + 0) / 255.0 * 100;
			moves[i].power = rom[movesOffset + (i - 1) * 6 + 2] & 0xFF;
			moves[i].pp = rom[movesOffset + (i - 1) * 6 + 5] & 0xFF;
			moves[i].type = typeTable[rom[movesOffset + (i - 1) * 6 + 3]];
		}

	}

	private void saveMoves() {
		int movesOffset = romEntry.getValue("MoveDataOffset");
		for (int i = 1; i <= 165; i++) {
			rom[movesOffset + (i - 1) * 6 + 1] = (byte) moves[i].effectIndex;
			rom[movesOffset + (i - 1) * 6 + 2] = (byte) moves[i].power;
			rom[movesOffset + (i - 1) * 6 + 3] = typeToByte(moves[i].type);
			int hitratio = (int) Math.round(moves[i].hitratio * 2.55);
			if (hitratio < 0) {
				hitratio = 0;
			}
			if (hitratio > 255) {
				hitratio = 255;
			}
			rom[movesOffset + (i - 1) * 6 + 4] = (byte) hitratio;
			rom[movesOffset + (i - 1) * 6 + 5] = (byte) moves[i].pp;
		}
	}

	public void applyMoveUpdates() {
		log("--Move Updates--");
		moves[2].type = Type.FIGHTING; // Karate Chop => FIGHTING (gen1)
		log("Made Karate Chop Fighting type");
		moves[13].setAccuracy(100); // Razor Wind => 100% accuracy (gen1/2)
		log("Made Razor Wind have 100% accuracy");
		moves[16].type = Type.FLYING; // Gust => FLYING (gen1)
		log("Made Gust Flying type");
		moves[17].power = 60; // Wing Attack => 60 power (gen1)
		log("Made Wing Attack have 60 power");
		moves[19].power = 90; // Fly => 90 power (gen1/2/3)
		log("Made Fly have 90 power");
		moves[20].setAccuracy(85); // Bind => 85% accuracy (gen1-4)
		log("Made Bind have 85% accuracy");
		moves[22].pp = 15; // Vine Whip => 15 pp (gen1/2/3)
		log("Made Vine Whip have 15 PP");
		moves[26].pp = 10;
		moves[26].power = 100; // Jump Kick => 10 pp, 100 power (gen1-4)
		log("Made Jump kick have 10 PP and 100 power");
		moves[28].type = Type.GROUND; // Sand Attack => GROUND (gen1)
		log("Made Sand Attack Ground type");
		moves[33].power = 50; // Tackle => 50 power, 100% accuracy
		moves[33].setAccuracy(100); // (gen1-4)
		log("Made Tackle have 50 power and 100% accuracy");
		moves[35].setAccuracy(90); // Wrap => 90% accuracy (gen1-4)
		log("Made Wrap have 90% accuracy");
		moves[37].pp = 10;
		moves[37].power = 120; // Thrash => 120 power, 10pp (gen1-4)
		log("Made Thrash have 10 PP and 120 power");
		moves[38].power = 120; // Double-Edge => 120 power (gen1)
		log("Made Double-Edge have 120 power");
		// Move 44, Bite, becomes dark (but doesn't exist anyway)
		moves[50].setAccuracy(100); // Disable => 100% accuracy (gen1-4)
		log("Made Disable have 100% accuracy");
		moves[59].setAccuracy(70); // Blizzard => 70% accuracy (gen1)
		log("Made Blizzard have 70% accuracy");
		// Move 67, Low Kick, has weight-based power in gen3+
		moves[67].setAccuracy(100); // Low Kick => 100% accuracy (gen1)
		log("Made Low Kick have 100% accuracy");
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
		moves[88].setAccuracy(90); // Rock Throw => 90% accuracy (gen1)
		log("Made Rock Throw have 90% accuracy");
		moves[91].power = 80; // Dig => 80 power (gen1/2/3)
		log("Made Dig have 80 power");
		moves[92].setAccuracy(90); // Toxic => 90% accuracy (gen1-4)
		log("Made Toxic have 90% accuracy");
		// move 95, Hypnosis, needs 60% accuracy in DP (its correct here)
		moves[105].pp = 10; // Recover => 10pp (gen1/2/3)
		log("Made Recover have 10 PP");
		moves[120].power = 200; // SelfDestruct => 200power (gen1)
		log("Made SelfDestruct have 200 power");
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
		moves[153].power = 250; // Explosion => 250 power (gen1)
		log("Made Explosion have 250 power");
		logBlankLine();
	}

	public List<Move> getMoves() {
		return Arrays.asList(moves);
	}

	private void loadPokemonStats() {
		pokes = new Pokemon[152];
		// Fetch our names
		String[] pokeNames = readPokemonNames();
		// Get base stats
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 151; i++) {
			pokes[i] = new Pokemon();
			pokes[i].number = i;
			loadBasicPokeStats(pokes[i], pokeStatsOffset + (i - 1) * 0x1C);
			// Name?
			pokes[i].name = pokeNames[pokeNumToRBYTable[i]];
		}

		// Mew override for R/B
		if (!romEntry.isYellow) {
			loadBasicPokeStats(pokes[151], romEntry.getValue("MewStatsOffset"));
		}

	}

	private void savePokemonStats() {
		// Write pokemon names
		int offs = romEntry.getValue("PokemonNamesOffset");
		int nameLength = romEntry.getValue("PokemonNamesLength");
		for (int i = 1; i <= 151; i++) {
			int rbynum = pokeNumToRBYTable[i];
			int stringOffset = offs + (rbynum - 1) * nameLength;
			writeFixedLengthString(pokes[i].name, stringOffset, nameLength);
		}
		// Write pokemon stats
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 150; i++) {
			saveBasicPokeStats(pokes[i], pokeStatsOffset + (i - 1) * 0x1C);
		}
		// Write MEW
		int mewOffset = romEntry.isYellow ? pokeStatsOffset + 150 * 0x1C
				: romEntry.getValue("MewStatsOffset");
		saveBasicPokeStats(pokes[151], mewOffset);
	}

	private void loadBasicPokeStats(Pokemon pkmn, int offset) {
		pkmn.hp = rom[offset + 1] & 0xFF;
		pkmn.attack = rom[offset + 2] & 0xFF;
		pkmn.defense = rom[offset + 3] & 0xFF;
		pkmn.speed = rom[offset + 4] & 0xFF;
		pkmn.special = rom[offset + 5] & 0xFF;
		pkmn.spatk = pkmn.special;
		pkmn.spdef = pkmn.special;
		// Type
		pkmn.primaryType = typeTable[rom[offset + 6] & 0xFF];
		pkmn.secondaryType = typeTable[rom[offset + 7] & 0xFF];
		// Only one type?
		if (pkmn.secondaryType == pkmn.primaryType) {
			pkmn.secondaryType = null;
		}

		pkmn.catchRate = rom[offset + 8] & 0xFF;
	}

	private void saveBasicPokeStats(Pokemon pkmn, int offset) {
		rom[offset + 1] = (byte) pkmn.hp;
		rom[offset + 2] = (byte) pkmn.attack;
		rom[offset + 3] = (byte) pkmn.defense;
		rom[offset + 4] = (byte) pkmn.speed;
		rom[offset + 5] = (byte) pkmn.special;
		rom[offset + 6] = typeToByte(pkmn.primaryType);
		if (pkmn.secondaryType == null) {
			rom[offset + 7] = rom[offset + 6];
		} else {
			rom[offset + 7] = typeToByte(pkmn.secondaryType);
		}
		rom[offset + 8] = (byte) pkmn.catchRate;
	}

	private String[] readPokemonNames() {
		int offs = romEntry.getValue("PokemonNamesOffset");
		int nameLength = romEntry.getValue("PokemonNamesLength");
		String[] names = new String[191];
		for (int i = 1; i <= 190; i++) {
			names[i] = readFixedLengthString(offs + (i - 1) * nameLength,
					nameLength);
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

	private void writeFixedLengthString(String str, int offset, int length) {
		byte[] translated = translateString(str);
		int len = Math.min(translated.length, length);
		System.arraycopy(translated, 0, rom, offset, len);
		while (len < length) {
			rom[offset + len] = 0x50;
			len++;
		}
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

	public String readVariableLengthString(int offset) {
		return readString(offset, Integer.MAX_VALUE);
	}

	public byte[] traduire(String str) {
		return translateString(str);
	}

	public String readVariableLengthScriptString(int offset) {
		return readString(offset, Integer.MAX_VALUE);
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

	private boolean romSig(byte[] rom, String sig) {
		try {
			int sigOffset = 0x134;
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
		return (pkmn.number >= 1 && pkmn.number <= 151);
	}

	@Override
	public boolean isInGame(int pokemonNumber) {
		return (pokemonNumber >= 1 && pokemonNumber <= 151);
	}

	@Override
	public List<Pokemon> getStarters() {
		// Get the starters
		List<Pokemon> starters = new ArrayList<Pokemon>();
		starters.add(pokes[pokeRBYToNumTable[rom[romEntry.arrayEntries
				.get("StarterOffsets1")[0]] & 0xFF]]);
		starters.add(pokes[pokeRBYToNumTable[rom[romEntry.arrayEntries
				.get("StarterOffsets2")[0]] & 0xFF]]);
		if (!romEntry.isYellow) {
			starters.add(pokes[pokeRBYToNumTable[rom[romEntry.arrayEntries
					.get("StarterOffsets3")[0]] & 0xFF]]);
		}
		return starters;
	}

	@Override
	public boolean setStarters(List<Pokemon> newStarters) {
		// Amount?
		int starterAmount = 2;
		if (!romEntry.isYellow) {
			starterAmount = 3;
		}

		// Basic checks
		if (newStarters.size() != starterAmount) {
			return false;
		}

		for (Pokemon pkmn : newStarters) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}

		// Patch starter bytes
		for (int i = 0; i < starterAmount; i++) {
			byte starter = (byte) pokeNumToRBYTable[newStarters.get(i).number];
			int[] offsets = romEntry.arrayEntries.get("StarterOffsets"
					+ (i + 1));
			for (int offset : offsets) {
				rom[offset] = starter;
			}
		}

		// Special stuff for non-Yellow only

		if (!romEntry.isYellow) {

			// Starter text
			if (romEntry.getValue("CanChangeStarterText") > 0) {
				List<Integer> starterTextOffsets = RomFunctions.search(rom,
						traduire("So! You want the"));
				for (int i = 0; i < 3 && i < starterTextOffsets.size(); i++) {
					writeFixedLengthScriptString(
							"So! You want=" + newStarters.get(i).name + "?»",
							starterTextOffsets.get(i),
							lengthOfStringAt(starterTextOffsets.get(i)) + 1);
				}
			}

			// Patch starter pokedex routine?
			// Can only do in 1M roms because of size concerns
			if (romEntry.getValue("PatchPokedex") > 0) {

				// Starter pokedex required RAM values
				// RAM offset => value
				// Allows for multiple starters in the same RAM byte
				Map<Integer, Integer> onValues = new TreeMap<Integer, Integer>();
				for (int i = 0; i < 3; i++) {
					int pkDexNum = newStarters.get(i).number;
					int ramOffset = (pkDexNum - 1) / 8
							+ romEntry.getValue("PokedexRamOffset");
					int bitShift = (pkDexNum - 1) % 8;
					int writeValue = 1 << bitShift;
					if (onValues.containsKey(ramOffset)) {
						onValues.put(ramOffset, onValues.get(ramOffset)
								| writeValue);
					} else {
						onValues.put(ramOffset, writeValue);
					}
				}

				// Starter pokedex offset/pointer calculations

				int pkDexOnOffset = romEntry.getValue("StarterPokedexOnOffset");
				int pkDexOffOffset = romEntry
						.getValue("StarterPokedexOffOffset");

				int sizeForOnRoutine = 5 * onValues.size() + 3;
				int writeOnRoutineTo = romEntry
						.getValue("StarterPokedexBranchOffset");
				int writeOffRoutineTo = writeOnRoutineTo + sizeForOnRoutine;
				int offsetForOnRoutine = makeGBPointer(writeOnRoutineTo);
				int offsetForOffRoutine = makeGBPointer(writeOffRoutineTo);
				int retOnOffset = makeGBPointer(pkDexOnOffset + 5);
				int retOffOffset = makeGBPointer(pkDexOffOffset + 4);

				// Starter pokedex
				// Branch to our new routine(s)

				// Turn bytes on
				rom[pkDexOnOffset] = (byte) 0xC3;
				writeWord(pkDexOnOffset + 1, offsetForOnRoutine);
				rom[pkDexOnOffset + 3] = 0x00;
				rom[pkDexOnOffset + 4] = 0x00;

				// Turn bytes off
				rom[pkDexOffOffset] = (byte) 0xC3;
				writeWord(pkDexOffOffset + 1, offsetForOffRoutine);
				rom[pkDexOffOffset + 3] = 0x00;

				// Put together the two scripts
				rom[writeOffRoutineTo] = (byte) 0xAF;
				int turnOnOffset = writeOnRoutineTo;
				int turnOffOffset = writeOffRoutineTo + 1;
				for (int ramOffset : onValues.keySet()) {
					int onValue = onValues.get(ramOffset);
					// Turn on code
					rom[turnOnOffset++] = 0x3E;
					rom[turnOnOffset++] = (byte) onValue;
					// Turn on code for ram writing
					rom[turnOnOffset++] = (byte) 0xEA;
					rom[turnOnOffset++] = (byte) (ramOffset % 0x100);
					rom[turnOnOffset++] = (byte) (ramOffset / 0x100);
					// Turn off code for ram writing
					rom[turnOffOffset++] = (byte) 0xEA;
					rom[turnOffOffset++] = (byte) (ramOffset % 0x100);
					rom[turnOffOffset++] = (byte) (ramOffset / 0x100);
				}
				// Jump back
				rom[turnOnOffset++] = (byte) 0xC3;
				writeWord(turnOnOffset, retOnOffset);

				rom[turnOffOffset++] = (byte) 0xC3;
				writeWord(turnOffOffset, retOffOffset);
			}

		}

		return true;

	}

	@Override
	public void shufflePokemonStats() {
		for (int i = 1; i <= 151; i++) {
			pokes[i].shuffleStats();
		}
	}

	@Override
	public Pokemon randomPokemon() {
		return pokes[(int) (RandomSource.random() * 151 + 1)];
	}

	@Override
	public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
		List<EncounterSet> encounters = new ArrayList<EncounterSet>();
		int startOffset = romEntry.getValue("WildPokemonStartOffset");
		int endOffset = romEntry.getValue("WildPokemonEndOffset");

		int offs = startOffset;
		while (offs < endOffset) {
			if (rom[offs] != 0) {
				EncounterSet thisSet = new EncounterSet();
				thisSet.rate = rom[offs] & 0xFF;
				offs++;
				while (rom[offs] != 0) {
					Encounter enc = new Encounter();
					enc.level = rom[offs] & 0xFF;
					enc.pokemon = pokes[pokeRBYToNumTable[rom[offs + 1] & 0xFF]];
					offs += 2;
					thisSet.encounters.add(enc);
					if (thisSet.encounters.size() == 10 && rom[offs] != 0) {
						offs--;
						break;
					}
				}
				encounters.add(thisSet);
			}
			offs++;
		}
		return encounters;
	}

	@Override
	public void setEncounters(boolean useTimeOfDay,
			List<EncounterSet> encounters) {
		Iterator<EncounterSet> encsetit = encounters.iterator();
		int startOffset = romEntry.getValue("WildPokemonStartOffset");
		int endOffset = romEntry.getValue("WildPokemonEndOffset");

		int offs = startOffset;
		while (offs < endOffset) {
			if (rom[offs] != 0) {
				EncounterSet thisSet = encsetit.next();
				Iterator<Encounter> encit = thisSet.encounters.iterator();
				offs++;
				while (rom[offs] != 0) {
					Encounter enc = encit.next();
					rom[offs] = (byte) enc.level;
					rom[offs + 1] = (byte) pokeNumToRBYTable[enc.pokemon.number];
					offs += 2;
					if (!encit.hasNext() && rom[offs] != 0) {
						offs--;
						break;
					}
				}
			}
			offs++;
		}
	}

	@Override
	public List<Pokemon> getPokemon() {
		return Arrays.asList(pokes);
	}

	@Override
	public Type randomType() {
		List<Type> bannedTypes = Arrays.asList(Type.DARK, Type.STEEL);
		Type type;
		while (bannedTypes.contains(type = Type.randomType())) {
		}
		return type;
	}

	public List<Trainer> getTrainers() {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = 47;
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int tPointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), tPointer);
		}

		List<Trainer> allTrainers = new ArrayList<Trainer>();
		for (int i = 1; i <= traineramount; i++) {
			int offs = pointers[i];
			int limit = trainerclasslimits[i];
			for (int trnum = 0; trnum < limit; trnum++) {
				Trainer tr = new Trainer();
				tr.offset = offs;
				tr.trainerclass = i;
				int dataType = rom[offs] & 0xFF;
				if (dataType == 0xFF) {
					// "Special" trainer
					tr.poketype = 1;
					offs++;
					while (rom[offs] != 0x0) {
						TrainerPokemon tpk = new TrainerPokemon();
						tpk.level = rom[offs] & 0xFF;
						tpk.pokemon = pokes[pokeRBYToNumTable[rom[offs + 1] & 0xFF]];
						tr.pokemon.add(tpk);
						offs += 2;
					}
				} else {
					tr.poketype = 0;
					int fixedLevel = dataType;
					offs++;
					while (rom[offs] != 0x0) {
						TrainerPokemon tpk = new TrainerPokemon();
						tpk.level = fixedLevel;
						tpk.pokemon = pokes[pokeRBYToNumTable[rom[offs] & 0xFF]];
						tr.pokemon.add(tpk);
						offs++;
					}
				}
				offs++;
				allTrainers.add(tr);
			}
		}
		tagTrainersUniversal(allTrainers);
		if (romEntry.isYellow) {
			tagTrainersYellow(allTrainers);
		} else {
			tagTrainersRB(allTrainers);
		}
		return allTrainers;
	}

	private void tagTrainersUniversal(List<Trainer> trs) {
		// Gym Leaders
		tbc(trs, 34, 0, "GYM1");
		tbc(trs, 35, 0, "GYM2");
		tbc(trs, 36, 0, "GYM3");
		tbc(trs, 37, 0, "GYM4");
		tbc(trs, 38, 0, "GYM5");
		tbc(trs, 40, 0, "GYM6");
		tbc(trs, 39, 0, "GYM7");
		tbc(trs, 29, 2, "GYM8");

		// Other giovanni teams
		tbc(trs, 29, 0, "GIO1");
		tbc(trs, 29, 1, "GIO2");

		// Elite 4
		tbc(trs, 44, 0, "ELITE1");
		tbc(trs, 33, 0, "ELITE2");
		tbc(trs, 46, 0, "ELITE3");
		tbc(trs, 47, 0, "ELITE4");
	}

	private void tagTrainersRB(List<Trainer> trs) {
		// Gary Battles
		tbc(trs, 25, 0, "RIVAL1-0");
		tbc(trs, 25, 1, "RIVAL1-1");
		tbc(trs, 25, 2, "RIVAL1-2");

		tbc(trs, 25, 3, "RIVAL2-0");
		tbc(trs, 25, 4, "RIVAL2-1");
		tbc(trs, 25, 5, "RIVAL2-2");

		tbc(trs, 25, 6, "RIVAL3-0");
		tbc(trs, 25, 7, "RIVAL3-1");
		tbc(trs, 25, 8, "RIVAL3-2");

		tbc(trs, 42, 0, "RIVAL4-0");
		tbc(trs, 42, 1, "RIVAL4-1");
		tbc(trs, 42, 2, "RIVAL4-2");

		tbc(trs, 42, 3, "RIVAL5-0");
		tbc(trs, 42, 4, "RIVAL5-1");
		tbc(trs, 42, 5, "RIVAL5-2");

		tbc(trs, 42, 6, "RIVAL6-0");
		tbc(trs, 42, 7, "RIVAL6-1");
		tbc(trs, 42, 8, "RIVAL6-2");

		tbc(trs, 42, 9, "RIVAL7-0");
		tbc(trs, 42, 10, "RIVAL7-1");
		tbc(trs, 42, 11, "RIVAL7-2");

		tbc(trs, 43, 0, "RIVAL8-0");
		tbc(trs, 43, 1, "RIVAL8-1");
		tbc(trs, 43, 2, "RIVAL8-2");

		// Gym Trainers
		tbc(trs, 5, 0, "GYM1");

		tbc(trs, 15, 0, "GYM2");
		tbc(trs, 6, 0, "GYM2");

		tbc(trs, 4, 7, "GYM3");
		tbc(trs, 20, 0, "GYM3");
		tbc(trs, 41, 2, "GYM3");

		tbc(trs, 3, 16, "GYM4");
		tbc(trs, 3, 17, "GYM4");
		tbc(trs, 6, 10, "GYM4");
		tbc(trs, 18, 0, "GYM4");
		tbc(trs, 18, 1, "GYM4");
		tbc(trs, 18, 2, "GYM4");
		tbc(trs, 32, 0, "GYM4");

		tbc(trs, 21, 2, "GYM5");
		tbc(trs, 21, 3, "GYM5");
		tbc(trs, 21, 6, "GYM5");
		tbc(trs, 21, 7, "GYM5");
		tbc(trs, 22, 0, "GYM5");
		tbc(trs, 22, 1, "GYM5");

		tbc(trs, 19, 0, "GYM6");
		tbc(trs, 19, 1, "GYM6");
		tbc(trs, 19, 2, "GYM6");
		tbc(trs, 19, 3, "GYM6");
		tbc(trs, 45, 21, "GYM6");
		tbc(trs, 45, 22, "GYM6");
		tbc(trs, 45, 23, "GYM6");

		tbc(trs, 8, 8, "GYM7");
		tbc(trs, 8, 9, "GYM7");
		tbc(trs, 8, 10, "GYM7");
		tbc(trs, 8, 11, "GYM7");
		tbc(trs, 11, 3, "GYM7");
		tbc(trs, 11, 4, "GYM7");
		tbc(trs, 11, 5, "GYM7");

		tbc(trs, 22, 2, "GYM8");
		tbc(trs, 22, 3, "GYM8");
		tbc(trs, 24, 5, "GYM8");
		tbc(trs, 24, 6, "GYM8");
		tbc(trs, 24, 7, "GYM8");
		tbc(trs, 31, 0, "GYM8");
		tbc(trs, 31, 8, "GYM8");
		tbc(trs, 31, 9, "GYM8");
	}

	private void tagTrainersYellow(List<Trainer> trs) {
		// Rival Battles
		tbc(trs, 25, 0, "IRIVAL");

		tbc(trs, 25, 1, "RIVAL1-0");

		tbc(trs, 25, 2, "RIVAL2-0");

		tbc(trs, 42, 0, "RIVAL3-0");

		tbc(trs, 42, 1, "RIVAL4-0");
		tbc(trs, 42, 2, "RIVAL4-1");
		tbc(trs, 42, 3, "RIVAL4-2");

		tbc(trs, 42, 4, "RIVAL5-0");
		tbc(trs, 42, 5, "RIVAL5-1");
		tbc(trs, 42, 6, "RIVAL5-2");

		tbc(trs, 42, 7, "RIVAL6-0");
		tbc(trs, 42, 8, "RIVAL6-1");
		tbc(trs, 42, 9, "RIVAL6-2");

		tbc(trs, 43, 0, "RIVAL7-0");
		tbc(trs, 43, 1, "RIVAL7-1");
		tbc(trs, 43, 2, "RIVAL7-2");

		// Rocket Jessie & James
		tbc(trs, 30, 41, "THEMED:JESSIE&JAMES");
		tbc(trs, 30, 42, "THEMED:JESSIE&JAMES");
		tbc(trs, 30, 43, "THEMED:JESSIE&JAMES");
		tbc(trs, 30, 44, "THEMED:JESSIE&JAMES");

		// Gym Trainers
		tbc(trs, 5, 0, "GYM1");

		tbc(trs, 6, 0, "GYM2");
		tbc(trs, 15, 0, "GYM2");

		tbc(trs, 4, 7, "GYM3");
		tbc(trs, 20, 0, "GYM3");
		tbc(trs, 41, 2, "GYM3");

		tbc(trs, 3, 16, "GYM4");
		tbc(trs, 3, 17, "GYM4");
		tbc(trs, 6, 10, "GYM4");
		tbc(trs, 18, 0, "GYM4");
		tbc(trs, 18, 1, "GYM4");
		tbc(trs, 18, 2, "GYM4");
		tbc(trs, 32, 0, "GYM4");

		tbc(trs, 21, 2, "GYM5");
		tbc(trs, 21, 3, "GYM5");
		tbc(trs, 21, 6, "GYM5");
		tbc(trs, 21, 7, "GYM5");
		tbc(trs, 22, 0, "GYM5");
		tbc(trs, 22, 1, "GYM5");

		tbc(trs, 19, 0, "GYM6");
		tbc(trs, 19, 1, "GYM6");
		tbc(trs, 19, 2, "GYM6");
		tbc(trs, 19, 3, "GYM6");
		tbc(trs, 45, 21, "GYM6");
		tbc(trs, 45, 22, "GYM6");
		tbc(trs, 45, 23, "GYM6");

		tbc(trs, 8, 8, "GYM7");
		tbc(trs, 8, 9, "GYM7");
		tbc(trs, 8, 10, "GYM7");
		tbc(trs, 8, 11, "GYM7");
		tbc(trs, 11, 3, "GYM7");
		tbc(trs, 11, 4, "GYM7");
		tbc(trs, 11, 5, "GYM7");

		tbc(trs, 22, 2, "GYM8");
		tbc(trs, 22, 3, "GYM8");
		tbc(trs, 24, 5, "GYM8");
		tbc(trs, 24, 6, "GYM8");
		tbc(trs, 24, 7, "GYM8");
		tbc(trs, 31, 0, "GYM8");
		tbc(trs, 31, 8, "GYM8");
		tbc(trs, 31, 9, "GYM8");
	}

	public void setTrainers(List<Trainer> trainerData) {
		int traineroffset = romEntry.getValue("TrainerDataTableOffset");
		int traineramount = 47;
		int[] trainerclasslimits = romEntry.arrayEntries
				.get("TrainerDataClassCounts");

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			int tPointer = readWord(traineroffset + (i - 1) * 2);
			pointers[i] = calculateOffset(bankOf(traineroffset), tPointer);
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
				Iterator<TrainerPokemon> tPokes = tr.pokemon.iterator();
				// Write their pokemon based on poketype
				if (tr.poketype == 0) {
					// Regular trainer
					int fixedLevel = tr.pokemon.get(0).level;
					rom[offs] = (byte) fixedLevel;
					offs++;
					while (tPokes.hasNext()) {
						TrainerPokemon tpk = tPokes.next();
						rom[offs] = (byte) pokeNumToRBYTable[tpk.pokemon.number];
						offs++;
					}
				} else {
					// Special trainer
					rom[offs] = (byte) 0xFF;
					offs++;
					while (tPokes.hasNext()) {
						TrainerPokemon tpk = tPokes.next();
						rom[offs] = (byte) tpk.level;
						rom[offs + 1] = (byte) pokeNumToRBYTable[tpk.pokemon.number];
						offs += 2;
					}
				}
				rom[offs] = 0;
				offs++;
			}
		}

		// Custom Moves AI Table
		// Zero it out entirely.
		rom[romEntry.getValue("ExtraTrainerMovesTableOffset")] = (byte) 0xFF;

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
	public boolean isYellow() {
		return romEntry.isYellow;
	}

	@Override
	public boolean typeInGame(Type type) {
		return type.isInRBY();
	}

	@Override
	public void fixTypeEffectiveness() {
		int base = romEntry.getValue("TypeEffectivenessOffset");
		log("--Fixing Type Effectiveness--");
		// Change Poison SE to bug (should be neutral)
		// to Ice NE to Fire (is currently neutral)
		log("Replaced: Poison super effective vs Bug => Ice not very effective vs Fire");
		rom[base + 135] = typeToByte(Type.ICE);
		rom[base + 136] = typeToByte(Type.FIRE);
		rom[base + 137] = 5; // Not very effective
		// Change BUG SE to Poison to Bug NE to Poison
		log("Changed: Bug super effective vs Poison => Bug not very effective vs Poison");
		rom[base + 203] = 5; // Not very effective
		// Change Ghost 0E to Psychic to Ghost SE to Psychic
		log("Changed: Psychic immune to Ghost => Ghost super effective vs Psychic");
		rom[base + 227] = 20; // Super effective
		logBlankLine();
	}

	@Override
	public Map<Pokemon, List<MoveLearnt>> getMovesLearnt() {
		Map<Pokemon, List<MoveLearnt>> movesets = new TreeMap<Pokemon, List<MoveLearnt>>();
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 190; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			if (pokeRBYToNumTable[i] != 0) {
				Pokemon pkmn = pokes[pokeRBYToNumTable[i]];
				int statsOffset = 0;
				if (pokeRBYToNumTable[i] == 151 && !romEntry.isYellow) {
					// Mewww
					statsOffset = romEntry.getValue("MewStatsOffset");
				} else {
					statsOffset = (pokeRBYToNumTable[i] - 1) * 0x1C
							+ pokeStatsOffset;
				}
				List<MoveLearnt> ourMoves = new ArrayList<MoveLearnt>();
				for (int delta = 0x0F; delta < 0x13; delta++) {
					if (rom[statsOffset + delta] != 0x00) {
						MoveLearnt learnt = new MoveLearnt();
						learnt.level = 1;
						learnt.move = rom[statsOffset + delta] & 0xFF;
						ourMoves.add(learnt);
					}
				}
				// Skip over evolution data
				while (rom[realPointer] != 0) {
					if (rom[realPointer] == 1) {
						realPointer += 3;
					} else if (rom[realPointer] == 2) {
						realPointer += 4;
					} else if (rom[realPointer] == 3) {
						realPointer += 3;
					}
				}
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
		}
		return movesets;
	}

	@Override
	public void setMovesLearnt(Map<Pokemon, List<MoveLearnt>> movesets) {
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 190; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			if (pokeRBYToNumTable[i] != 0) {
				Pokemon pkmn = pokes[pokeRBYToNumTable[i]];
				List<MoveLearnt> ourMoves = movesets.get(pkmn);
				int statsOffset = 0;
				if (pokeRBYToNumTable[i] == 151 && !romEntry.isYellow) {
					// Mewww
					statsOffset = romEntry.getValue("MewStatsOffset");
				} else {
					statsOffset = (pokeRBYToNumTable[i] - 1) * 0x1C
							+ pokeStatsOffset;
				}
				int movenum = 0;
				while (movenum < 4 && ourMoves.size() > movenum
						&& ourMoves.get(movenum).level == 1) {
					rom[statsOffset + 0x0F + movenum] = (byte) ourMoves
							.get(movenum).move;
					movenum++;
				}
				// Write out the rest of zeroes
				for (int mn = movenum; mn < 4; mn++) {
					rom[statsOffset + 0x0F + mn] = 0;
				}
				// Skip over evolution data
				while (rom[realPointer] != 0) {
					if (rom[realPointer] == 1) {
						realPointer += 3;
					} else if (rom[realPointer] == 2) {
						realPointer += 4;
					} else if (rom[realPointer] == 3) {
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
	}

	@Override
	public List<Pokemon> getStaticPokemon() {
		List<Pokemon> statics = new ArrayList<Pokemon>();
		if (romEntry.getValue("StaticPokemonSupport") > 0) {
			for (int offset : romEntry.staticPokemonSingle) {
				statics.add(pokes[pokeRBYToNumTable[rom[offset] & 0xFF]]);
			}
			for (GameCornerPokemon gcp : romEntry.staticPokemonGameCorner) {
				statics.add(pokes[pokeRBYToNumTable[rom[gcp.offsets[0]] & 0xFF]]);
			}
		}
		return statics;
	}

	@Override
	public boolean setStaticPokemon(List<Pokemon> staticPokemon) {
		if (romEntry.getValue("StaticPokemonSupport") == 0) {
			return false;
		}
		// Checks
		int singleSize = romEntry.staticPokemonSingle.size();
		int gcSize = romEntry.staticPokemonGameCorner.size();
		if (staticPokemon.size() != singleSize + gcSize) {
			return false;
		}
		for (Pokemon pkmn : staticPokemon) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}

		// Singular entries
		for (int i = 0; i < singleSize; i++) {
			rom[romEntry.staticPokemonSingle.get(i)] = (byte) pokeNumToRBYTable[staticPokemon
					.get(i).number];
		}

		// Game corner
		for (int i = 0; i < gcSize; i++) {
			byte pokeNum = (byte) pokeNumToRBYTable[staticPokemon.get(i
					+ singleSize).number];
			int[] offsets = romEntry.staticPokemonGameCorner.get(i).offsets;
			for (int offset : offsets) {
				rom[offset] = pokeNum;
			}
		}
		return true;
	}

	@Override
	public boolean canChangeStaticPokemon() {
		return (romEntry.getValue("StaticPokemonSupport") > 0);
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
		for (int i = 1; i <= 5; i++) {
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

		// Gym Leader TM Moves (RB only)
		if (!romEntry.isYellow) {
			int[] tms = new int[] { 34, 11, 24, 21, 6, 46, 38, 27 };
			int glMovesOffset = romEntry.getValue("GymLeaderMovesTableOffset");
			for (int i = 0; i < tms.length; i++) {
				// Set the special move used by gym (i+1) to
				// the move we just wrote to TM tms[i]
				rom[glMovesOffset + i * 2] = moveIndexes.get(tms[i] - 1)
						.byteValue();
			}
		}

		// Rudimentary TM text
		for (int i = 0; i < 50; i++) {
			int tm = i + 1;
			List<Integer> foundTexts = RomFunctions.search(rom, traduire("TM"
					+ String.format("%02d", tm) + " "));
			if (foundTexts.size() == 1) {
				// bingo
				int textOffset = foundTexts.get(0);

				String newText = "TM" + String.format("%02d", tm) + " teaches="
						+ RomFunctions.moveNames[moveIndexes.get(i)] + "!»";
				if (tm == 28) {
					// Tombstoner quiz, pick any move but the real TM28
					int fakeMove = moveIndexes.get(27);
					while (fakeMove == moveIndexes.get(27)) {
						fakeMove = RandomSource
								.nextInt(RomFunctions.moveNames.length - 1) + 1;
					}
					newText = "TM" + String.format("%02d", tm) + " is="
							+ RomFunctions.moveNames[fakeMove] + "?»";
				}
				writeFixedLengthScriptString(newText, textOffset,
						newText.length() + 1);
			}
		}
	}

	@Override
	public int getTMCount() {
		return 50;
	}

	@Override
	public int getHMCount() {
		return 5;
	}

	@Override
	public Map<Pokemon, boolean[]> getTMHMCompatibility() {
		Map<Pokemon, boolean[]> compat = new TreeMap<Pokemon, boolean[]>();
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (int i = 1; i <= 151; i++) {
			int baseStatsOffset = (romEntry.isYellow || i != 151) ? (pokeStatsOffset + (i - 1) * 0x1C)
					: romEntry.getValue("MewStatsOffset");
			Pokemon pkmn = pokes[i];
			boolean[] flags = new boolean[56];
			for (int j = 0; j < 7; j++) {
				readByteIntoFlags(flags, j * 8 + 1, baseStatsOffset + 0x14 + j);
			}
			compat.put(pkmn, flags);
		}
		return compat;
	}

	@Override
	public void setTMHMCompatibility(Map<Pokemon, boolean[]> compatData) {
		int pokeStatsOffset = romEntry.getValue("PokemonStatsOffset");
		for (Map.Entry<Pokemon, boolean[]> compatEntry : compatData.entrySet()) {
			Pokemon pkmn = compatEntry.getKey();
			boolean[] flags = compatEntry.getValue();
			int baseStatsOffset = (romEntry.isYellow || pkmn.number != 151) ? (pokeStatsOffset + (pkmn.number - 1) * 0x1C)
					: romEntry.getValue("MewStatsOffset");
			for (int j = 0; j < 7; j++) {
				rom[baseStatsOffset + 0x14 + j] = getByteFromFlags(flags,
						j * 8 + 1);
			}
		}
	}

	@Override
	public boolean hasMoveTutors() {
		return false;
	}

	@Override
	public List<Integer> getMoveTutorMoves() {
		return new ArrayList<Integer>();
	}

	@Override
	public void setMoveTutorMoves(List<Integer> moves) {
		// Do nothing
	}

	@Override
	public Map<Pokemon, boolean[]> getMoveTutorCompatibility() {
		return new TreeMap<Pokemon, boolean[]>();
	}

	@Override
	public void setMoveTutorCompatibility(Map<Pokemon, boolean[]> compatData) {
		// Do nothing
	}

	@Override
	public String getROMName() {
		return "Pokemon " + romEntry.name;
	}

	@Override
	public String getROMCode() {
		return romEntry.romName + " (" + romEntry.version + "/"
				+ romEntry.nonJapanese + ")";
	}

	@Override
	public String getSupportLevel() {
		return (romEntry.getValue("StaticPokemonSupport") > 0) ? "Complete"
				: "No Static Pokemon";
	}

	@Override
	public void removeTradeEvolutions() {
		// Gen 1: evolution data is right before moveset data
		// So use those pointers
		int pointersOffset = romEntry.getValue("PokemonMovesetsTableOffset");
		log("--Removing Trade Evolutions--");
		for (int i = 1; i <= 190; i++) {
			int pointer = readWord(pointersOffset + (i - 1) * 2);
			int realPointer = calculateOffset(bankOf(pointersOffset), pointer);
			if (pokeRBYToNumTable[i] != 0) {
				// Evolution data
				// All the 4 trade evos (Abra, Geodude, Gastly, Machop)
				// evolve at 25
				// So make this "3rd stage" to 37
				while (rom[realPointer] != 0) {
					if (rom[realPointer] == 1) {
						realPointer += 3;
					} else if (rom[realPointer] == 2) {
						realPointer += 4;
					} else if (rom[realPointer] == 3) {
						int otherPoke = pokeRBYToNumTable[rom[realPointer + 2] & 0xFF];
						log("Made " + pokes[pokeRBYToNumTable[i]].name
								+ " evolve into " + pokes[otherPoke].name
								+ " at level 37");
						// Trade evo
						rom[realPointer] = 1;
						rom[realPointer + 1] = 37;
						realPointer += 3;
					}
				}
			}
		}
		logBlankLine();
	}

	private static final int[] tclassesCounts = new int[] { 21, 47 };
	private static final List<Integer> singularTrainers = Arrays.asList(28, 32,
			33, 34, 35, 36, 37, 38, 39, 43, 45, 46);

	@Override
	public List<String> getTrainerNames() {
		int[] offsets = romEntry.arrayEntries.get("TrainerClassNamesOffsets");
		List<String> trainerNames = new ArrayList<String>();
		int offset = offsets[offsets.length - 1];
		for (int j = 0; j < tclassesCounts[1]; j++) {
			String name = readVariableLengthString(offset);
			offset += (internalStringLength(name) + 1);
			if (singularTrainers.contains(j)) {
				trainerNames.add(name);
			}
		}
		return trainerNames;
	}

	@Override
	public void setTrainerNames(List<String> trainerNames) {
		if (romEntry.getValue("CanChangeTrainerText") > 0) {
			int[] offsets = romEntry.arrayEntries
					.get("TrainerClassNamesOffsets");
			Iterator<String> trainerNamesI = trainerNames.iterator();
			int offset = offsets[offsets.length - 1];
			for (int j = 0; j < tclassesCounts[1]; j++) {
				String name = readVariableLengthString(offset);
				if (singularTrainers.contains(j)) {
					String newName = trainerNamesI.next();
					writeFixedLengthString(newName, offset,
							internalStringLength(name) + 1);
				}
				offset += (internalStringLength(name) + 1);
			}
		}
	}

	@Override
	public boolean fixedTrainerNamesLength() {
		return true;
	}

	@Override
	public List<String> getTrainerClassNames() {
		int[] offsets = romEntry.arrayEntries.get("TrainerClassNamesOffsets");
		List<String> trainerClassNames = new ArrayList<String>();
		if (offsets.length == 2) {
			for (int i = 0; i < offsets.length; i++) {
				int offset = offsets[i];
				for (int j = 0; j < tclassesCounts[i]; j++) {
					String name = readVariableLengthString(offset);
					offset += (internalStringLength(name) + 1);
					if (i == 0 || !singularTrainers.contains(j)) {
						trainerClassNames.add(name);
					}
				}
			}
		} else {
			int offset = offsets[0];
			for (int j = 0; j < tclassesCounts[1]; j++) {
				String name = readVariableLengthString(offset);
				offset += (internalStringLength(name) + 1);
				if (!singularTrainers.contains(j)) {
					trainerClassNames.add(name);
				}
			}
		}
		return trainerClassNames;
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		if (romEntry.getValue("CanChangeTrainerText") > 0) {
			int[] offsets = romEntry.arrayEntries
					.get("TrainerClassNamesOffsets");
			Iterator<String> tcNamesIter = trainerClassNames.iterator();
			if (offsets.length == 2) {
				for (int i = 0; i < offsets.length; i++) {
					int offset = offsets[i];
					for (int j = 0; j < tclassesCounts[i]; j++) {
						String name = readVariableLengthString(offset);
						if (i == 0 || !singularTrainers.contains(j)) {
							String newName = tcNamesIter.next();
							writeFixedLengthString(newName, offset,
									internalStringLength(name) + 1);
						}
						offset += (internalStringLength(name) + 1);
					}
				}
			} else {
				int offset = offsets[0];
				for (int j = 0; j < tclassesCounts[1]; j++) {
					String name = readVariableLengthString(offset);
					if (!singularTrainers.contains(j)) {
						String newName = tcNamesIter.next();
						writeFixedLengthString(newName, offset,
								internalStringLength(name) + 1);
					}
					offset += (internalStringLength(name) + 1);
				}
			}
		}

	}

	@Override
	public boolean fixedTrainerClassNamesLength() {
		return true;
	}

	@Override
	public String getDefaultExtension() {
		if (((rom[0x143] & 0xFF) & 0x80) > 0) {
			return "gbc";
		}
		return "sgb";
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

		if (romEntry.expPatch.equals("rbEN")) {
			// jump for main routine
			writeHexString("C3006B", 0x552A1);
			// jump for # of party members
			writeHexString("C30071", 0x5547D);
			// # of party members
			writeHexString("EAFECFFE02D8EA1ED1C38354", 0x57100);
			// display of EXP num
			writeHexString("EDCF38", 0x89BEF);
			// cut down EXP text to fit with 8 nums
			writeHexString("E758", 0x89BF7);

			// main code
			try {
				byte[] patch = FileFunctions
						.getXPPatchFile("redblue_patch_main.bin");
				System.arraycopy(patch, 0, rom, 0x56B00, patch.length);
			} catch (IOException e) {
				return;
			}
		} else if (romEntry.expPatch.equals("yellowEN")) {
			// jump for main routine
			writeHexString("C3006B", 0x552B0);
			// jump for # of party members
			writeHexString("C30071", 0x5548C);
			// # of party members
			writeHexString("EAFDCFFE02D8EA1DD1C39254", 0x57100);
			// display of EXP num
			writeHexString("ECCF38", 0x9FAD9);
			// cut down EXP text to fit with 8 nums
			writeHexString("E758", 0x9FAE1);

			// main code
			try {
				byte[] patch = FileFunctions
						.getXPPatchFile("yellow_patch_main.bin");
				System.arraycopy(patch, 0, rom, 0x56B00, patch.length);
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
