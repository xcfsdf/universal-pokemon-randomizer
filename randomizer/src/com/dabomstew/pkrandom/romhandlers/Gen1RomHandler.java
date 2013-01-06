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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

	private static final char[] textTable = constructTextTable();
	private static final Type[] typeTable = constructTypeTable();

	private static char[] constructTextTable() {
		char[] table = new char[256];
		// Capital letters
		for (int i = 0x80; i < 0x80 + 26; i++) {
			table[i] = (char) ('A' + (i - 0x80));
		}
		// Little letters
		for (int i = 0xa0; i < 0xa0 + 26; i++) {
			table[i] = (char) ('a' + (i - 0xa0));
		}

		// Numbers
		for (int i = 0xf6; i < 0xf6 + 10; i++) {
			table[i] = (char) ('0' + (i - 0xf6));
		}
		// Punctuation
		table[0x4f] = '=';
		table[0x51] = '*';
		table[0x54] = '¶';
		table[0x57] = '»';
		table[0x55] = '+';
		table[0x58] = '$';
		table[0x7f] = ' ';
		table[0x9c] = ':';
		table[0xba] = 'é';
		table[0xe3] = '-';
		table[0xe6] = '?';
		table[0xe7] = '!';
		table[0xe8] = '.';
		table[0xe0] = '\'';
		table[0xf4] = ',';

		// Symbols
		table[0xef] = '♂';
		table[0xf5] = '♀';

		return table;
	}

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

	private static final int pokeNamesOffsetRB = 0x1C21E,
			pokeNamesOffsetY = 0xE8000;
	private static final int pokeStatsOffset = 0x383DE,
			mewStatsOffsetRB = 0x425B;
	private static final int wildsStartRB = 0xD0DF, wildsEndRB = 0xD5C6;
	private static final int wildsStartY = 0xCD8B, wildsEndY = 0xD2ED;
	private static final int movesOffset = 0x38000;
	private static final int typeEffectOffsetRB = 0x3E474,
			typeEffectOffsetY = 0x3E5FA;
	private static final int movePointersOffsetRB = 0x3B05C,
			movePointersOffsetY = 0x3B1E5;

	// This ROM's data
	private Pokemon[] pokes;
	private boolean isYellow;
	private Move[] moves;

	@Override
	public boolean detectRom(byte[] rom) {
		if (rom.length != 1048576) {
			return false; // size check
		}
		if (romSig(rom, "POKEMON RED") || romSig(rom, "POKEMON BLUE")
				|| romSig(rom, "POKEMON YELLOW")) {
			return true; // right
		}
		return false; // GB rom we don't support yet
	}

	@Override
	public void loadedRom() {
		isYellow = romSig(rom, "POKEMON YELLOW");
		loadPokemonStats();
		loadMoves();
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
		for (int i = 1; i <= 151; i++) {
			pokes[i] = new Pokemon();
			pokes[i].number = i;
			loadBasicPokeStats(pokes[i], pokeStatsOffset + (i - 1) * 0x1C);
			// Name?
			pokes[i].name = pokeNames[pokeNumToRBYTable[i]];
		}

		// Mew override for R/B
		if (!isYellow) {
			loadBasicPokeStats(pokes[151], mewStatsOffsetRB);
		}

	}

	private void savePokemonStats() {
		// Write pokemon names
		int offs = isYellow ? pokeNamesOffsetY : pokeNamesOffsetRB;
		for (int i = 1; i <= 151; i++) {
			int rbynum = pokeNumToRBYTable[i];
			int stringOffset = offs + (rbynum - 1) * 10;
			writeFixedLengthString(pokes[i].name, stringOffset, 10);
		}
		// Write pokemon stats
		for (int i = 1; i <= 150; i++) {
			saveBasicPokeStats(pokes[i], pokeStatsOffset + (i - 1) * 0x1C);
		}
		// Write MEW
		int mewOffset = isYellow ? pokeStatsOffset + 150 * 0x1C
				: mewStatsOffsetRB;
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
		int offs = isYellow ? pokeNamesOffsetY : pokeNamesOffsetRB;
		String[] names = new String[191];
		for (int i = 1; i <= 190; i++) {
			names[i] = readFixedLengthString(offs + (i - 1) * 10, 10);
		}
		return names;
	}

	private String readFixedLengthString(int offset, int length) {
		String name = "";
		int offs = 0;
		while (offs < length) {
			if (rom[offset + offs] == 0x50) {
				break;
			}
			int thisbyte = (rom[offset + offs] & 0xFF);
			if (textTable[thisbyte] != 0) {
				name += textTable[thisbyte];
			} else {
				name += "[" + String.format("%2x", thisbyte) + "]";
			}
			offs++;
		}
		return name;
	}

	private void writeFixedLengthString(String str, int offset, int length) {
		for (int i = 0; i < length; i++) {
			if (i >= str.length()) {
				rom[offset + i] = 0x50;
			} else {
				char toFind = str.charAt(i);
				int cnum = -1;
				for (int cno = 0; cno < 255; cno++) {
					if (textTable[cno] == toFind) {
						cnum = cno;
						break;
					}
				}
				if (cnum == -1) {
					cnum = 0x7F; // Space to replace unfound chars
				}
				rom[offset + i] = (byte) cnum;
			}
		}
	}

	public String readVariableLengthString(int offset) {
		String name = "";
		int offs = 0;
		while (true) {
			if (rom[offset + offs] == 0x50) {
				break;
			}
			int thisbyte = (rom[offset + offs] & 0xFF);
			if (textTable[thisbyte] != 0) {
				name += textTable[thisbyte];
			} else {
				name += "[" + String.format("%2x", thisbyte) + "]";
			}
			offs++;
		}
		return name;
	}

	public byte[] traduire(String str) {
		byte[] ret = new byte[str.length()];
		for (int i = 0; i < ret.length; i++) {
			char toFind = str.charAt(i);
			int cnum = -1;
			for (int cno = 0; cno < 255; cno++) {
				if (textTable[cno] == toFind) {
					cnum = cno;
					break;
				}
			}
			if (cnum == -1) {
				cnum = 0x7F; // Space to replace unfound chars
			}
			ret[i] = (byte) cnum;
		}
		return ret;
	}

	public String readVariableLengthScriptString(int offset) {
		String name = "";
		int offs = 0;
		while (true) {
			if (rom[offset + offs] == 0x0) {
				break;
			}
			int thisbyte = (rom[offset + offs] & 0xFF);
			if (textTable[thisbyte] != 0) {
				name += textTable[thisbyte];
			} else {
				name += "[" + String.format("%2x", thisbyte) + "]";
			}
			offs++;
		}
		return name;
	}

	private void writeFixedLengthScriptString(String str, int offset, int length) {
		for (int i = 0; i < length; i++) {
			if (i >= str.length()) {
				rom[offset + i] = 0x00;
			} else {
				char toFind = str.charAt(i);
				int cnum = -1;
				for (int cno = 0; cno < 255; cno++) {
					if (textTable[cno] == toFind) {
						cnum = cno;
						break;
					}
				}
				if (cnum == -1) {
					cnum = 0x7F; // Space to replace unfound chars
				}
				rom[offset + i] = (byte) cnum;
			}
		}
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
		if (isYellow) {
			// For yellow the "starters" are you followed by rivals pokemon,
			// nothing else
			Pokemon start1 = pokes[pokeRBYToNumTable[rom[0x18f19] & 0xFF]];
			Pokemon start2 = pokes[pokeRBYToNumTable[rom[0x3a28a] & 0xFF]];
			return Arrays.asList(start1, start2);
		} else {
			// Read starters
			// order 3a1eb, 3a1e5, 3a1e8
			Pokemon start1 = pokes[pokeRBYToNumTable[rom[0x1d126] & 0xFF]];
			Pokemon start2 = pokes[pokeRBYToNumTable[rom[0x1d104] & 0xFF]];
			Pokemon start3 = pokes[pokeRBYToNumTable[rom[0x1d115] & 0xFF]];
			return Arrays.asList(start1, start2, start3);
		}
	}

	@Override
	public boolean setStarters(List<Pokemon> newStarters) {
		if (isYellow) {
			if (newStarters.size() != 2) {
				return false;
			}
			for (Pokemon pkmn : newStarters) {
				if (!isInGame(pkmn)) {
					return false;
				}
			}
			// Set starters
			byte starter1 = (byte) pokeNumToRBYTable[newStarters.get(0).number];

			rom[0x18f19] = starter1;
			rom[0x1cb41] = starter1;
			rom[0x1cb66] = starter1;

			byte starter2 = (byte) pokeNumToRBYTable[newStarters.get(1).number];

			rom[0x3a28a] = starter2;

			return true;
		} else {
			if (newStarters.size() != 3) {
				return false;
			}
			for (Pokemon pkmn : newStarters) {
				if (!isInGame(pkmn)) {
					return false;
				}
			}
			// Set starters
			// Credit: http://hax.iimarck.us/topic/484/
			byte starter1 = (byte) pokeNumToRBYTable[newStarters.get(0).number];

			rom[0x1cc84] = starter1;
			rom[0x1d10e] = starter1;
			rom[0x1d126] = starter1;
			rom[0x39cf8] = starter1;
			rom[0x50fb3] = starter1;
			rom[0x510dd] = starter1;

			byte starter2 = (byte) pokeNumToRBYTable[newStarters.get(1).number];

			rom[0x19591] = starter2;
			rom[0x1cc88] = starter2;
			rom[0x1cdc8] = starter2;
			rom[0x1d104] = starter2;
			rom[0x1d11f] = starter2;
			rom[0x50faf] = starter2;
			rom[0x510d9] = starter2;
			rom[0x51caf] = starter2;
			rom[0x6060e] = starter2;
			rom[0x61450] = starter2;
			if (romSig(rom, "POKEMON BLUE")) {
				rom[0x75f9f] = starter2;
			} else {
				rom[0x75f9e] = starter2;
			}

			byte starter3 = (byte) pokeNumToRBYTable[newStarters.get(2).number];

			rom[0x19599] = starter3;
			rom[0x1cdd0] = starter3;
			rom[0x1d115] = starter3;
			rom[0x1d130] = starter3;
			rom[0x39cf2] = starter3;
			rom[0x50fb1] = starter3;
			rom[0x510db] = starter3;
			rom[0x51cb7] = starter3;
			rom[0x60616] = starter3;
			rom[0x61458] = starter3;
			if (romSig(rom, "POKEMON BLUE")) {
				rom[0x75fa7] = starter3;
			} else {
				rom[0x75fa6] = starter3;
			}

			// Starter text
			List<Integer> starterTextOffsets = RomFunctions.search(rom,
					traduire("So! You want the"));
			for (int i = 0; i < 3 && i < starterTextOffsets.size(); i++) {
				writeFixedLengthScriptString(
						"So! You want=" + newStarters.get(i).name + "?»",
						starterTextOffsets.get(i),
						readVariableLengthScriptString(
								starterTextOffsets.get(i)).length() + 1);
			}

			// Starter pokedex
			// Branch to our new routine(s)
			writeHexString("C300600000", 0x5C0DC);
			writeHexString("C3206000", 0x5C0E6);
			// RAM offset => value
			// Allows for multiple starters in the same RAM byte
			Map<Integer, Integer> onValues = new TreeMap<Integer, Integer>();
			for (int i = 0; i < 3; i++) {
				int pkDexNum = newStarters.get(i).number;
				int ramOffset = (pkDexNum - 1) / 8 + 0xD2F7;
				int bitShift = (pkDexNum - 1) % 8;
				int writeValue = 1 << bitShift;
				if (onValues.containsKey(ramOffset)) {
					onValues.put(ramOffset, onValues.get(ramOffset)
							| writeValue);
				} else {
					onValues.put(ramOffset, writeValue);
				}
			}

			// Put together the two scripts
			rom[0x5E020] = (byte) 0xAF;
			int turnOnOffset = 0x5E000;
			int turnOffOffset = 0x5E021;
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
			rom[turnOnOffset++] = (byte) 0xE1;
			rom[turnOnOffset++] = 0x40;

			rom[turnOffOffset++] = (byte) 0xC3;
			rom[turnOffOffset++] = (byte) 0xEA;
			rom[turnOffOffset++] = 0x40;

			return true;
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
		int startOffset = isYellow ? wildsStartY : wildsStartRB;
		int endOffset = isYellow ? wildsEndY : wildsEndRB;

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
		int startOffset = isYellow ? wildsStartY : wildsStartRB;
		int endOffset = isYellow ? wildsEndY : wildsEndRB;

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

	private static final int trainerOffsetTableRB = 0x39D3B,
			trainerOffsetTableY = 0x39DD1;
	private static final int[] trainerClassAmountsY = new int[] { 0, 14, 15,
			19, 8, 10, 25, 7, 12, 14, 15, 9, 3, 0, 11, 15, 9, 7, 15, 4, 2, 8,
			6, 17, 9, 3, 3, 0, 13, 3, 49, 10, 8, 1, 1, 1, 1, 1, 1, 1, 1, 5, 10,
			3, 1, 24, 1, 1 };
	private static final int[] trainerClassAmountsRB = new int[] { 0, 13, 14,
			18, 8, 9, 24, 7, 12, 14, 15, 9, 3, 0, 11, 15, 9, 7, 15, 4, 2, 8, 6,
			17, 9, 9, 3, 0, 13, 3, 41, 10, 8, 1, 1, 1, 1, 1, 1, 1, 1, 5, 12, 3,
			1, 24, 1, 1 };

	public List<Trainer> getTrainers() {
		int traineroffset = isYellow ? trainerOffsetTableY
				: trainerOffsetTableRB;
		int traineramount = 47;
		int[] trainerclasslimits = isYellow ? trainerClassAmountsY
				: trainerClassAmountsRB;

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			pointers[i] = 0x38000 + (rom[traineroffset + (i - 1) * 2] & 0xFF)
					+ ((rom[traineroffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		if (isYellow) {
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
		tboffs(trs, 0x39E78, "GYM1");

		tboffs(trs, 0x3a049, "GYM2");
		tboffs(trs, 0x39e9d, "GYM2");

		tboffs(trs, 0x39e74, "GYM3");
		tboffs(trs, 0x3a127, "GYM3");
		tboffs(trs, 0x3a3f7, "GYM3");

		tboffs(trs, 0x39e50, "GYM4");
		tboffs(trs, 0x3a0d1, "GYM4");
		tboffs(trs, 0x39ec6, "GYM4");
		tboffs(trs, 0x3a0d7, "GYM4");
		tboffs(trs, 0x3a0db, "GYM4");
		tboffs(trs, 0x39e54, "GYM4");
		tboffs(trs, 0x3a385, "GYM4");

		tboffs(trs, 0x3a14d, "GYM5");
		tboffs(trs, 0x3a14a, "GYM5");
		tboffs(trs, 0x3a13a, "GYM5");
		tboffs(trs, 0x3a155, "GYM5");
		tboffs(trs, 0x3a151, "GYM5");
		tboffs(trs, 0x3a140, "GYM5");

		tboffs(trs, 0x3a11f, "GYM6");
		tboffs(trs, 0x3a511, "GYM6");
		tboffs(trs, 0x3a50a, "GYM6");
		tboffs(trs, 0x3a50e, "GYM6");
		tboffs(trs, 0x3a115, "GYM6");
		tboffs(trs, 0x3a124, "GYM6");
		tboffs(trs, 0x3a11b, "GYM6");

		tboffs(trs, 0x39ff0, "GYM7");
		tboffs(trs, 0x39ff5, "GYM7");
		tboffs(trs, 0x39ff8, "GYM7");
		tboffs(trs, 0x39f4c, "GYM7");
		tboffs(trs, 0x39f51, "GYM7");
		tboffs(trs, 0x39f57, "GYM7");
		tboffs(trs, 0x39f5a, "GYM7");

		tboffs(trs, 0x3a15d, "GYM8");
		tboffs(trs, 0x3a15a, "GYM8");
		tboffs(trs, 0x3a1d7, "GYM8");
		tboffs(trs, 0x3a35a, "GYM8");
		tboffs(trs, 0x3a37e, "GYM8");
		tboffs(trs, 0x3a1d3, "GYM8");
		tboffs(trs, 0x3a382, "GYM8");
		tboffs(trs, 0x3a1da, "GYM8");
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
		tboffs(trs, 0x39F17, "GYM1");

		tboffs(trs, 0x3a0ee, "GYM2");
		tboffs(trs, 0x39f3f, "GYM2");

		tboffs(trs, 0x39f14, "GYM3");
		tboffs(trs, 0x3a1cc, "GYM3");
		tboffs(trs, 0x3a48e, "GYM3");

		tboffs(trs, 0x39eec, "GYM4");
		tboffs(trs, 0x3a176, "GYM4");
		tboffs(trs, 0x39f68, "GYM4");
		tboffs(trs, 0x3a17c, "GYM4");
		tboffs(trs, 0x39ef0, "GYM4");
		tboffs(trs, 0x3a180, "GYM4");
		tboffs(trs, 0x3a424, "GYM4");

		tboffs(trs, 0x3a1f2, "GYM5");
		tboffs(trs, 0x3a1ef, "GYM5");
		tboffs(trs, 0x3a1df, "GYM5");
		tboffs(trs, 0x3a1fa, "GYM5");
		tboffs(trs, 0x3a1f6, "GYM5");
		tboffs(trs, 0x3a1e5, "GYM5");

		tboffs(trs, 0x3a1c4, "GYM6");
		tboffs(trs, 0x3a1c9, "GYM6");
		tboffs(trs, 0x3a1ba, "GYM6");
		tboffs(trs, 0x3a1c0, "GYM6");
		tboffs(trs, 0x3a595, "GYM6");
		tboffs(trs, 0x3a58e, "GYM6");
		tboffs(trs, 0x3a592, "GYM6");

		tboffs(trs, 0x3a095, "GYM7");
		tboffs(trs, 0x3a09a, "GYM7");
		tboffs(trs, 0x3a09d, "GYM7");
		tboffs(trs, 0x39ff1, "GYM7");
		tboffs(trs, 0x39ff6, "GYM7");
		tboffs(trs, 0x39ffc, "GYM7");
		tboffs(trs, 0x39fff, "GYM7");

		tboffs(trs, 0x3a202, "GYM8");
		tboffs(trs, 0x3a27c, "GYM8");
		tboffs(trs, 0x3a3f9, "GYM8");
		tboffs(trs, 0x3a41d, "GYM8");
		tboffs(trs, 0x3a278, "GYM8");
		tboffs(trs, 0x3a421, "GYM8");
		tboffs(trs, 0x3a1ff, "GYM8");
		tboffs(trs, 0x3a27f, "GYM8");
	}

	public void setTrainers(List<Trainer> trainerData) {
		int traineroffset = isYellow ? trainerOffsetTableY
				: trainerOffsetTableRB;
		int traineramount = 47;
		int[] trainerclasslimits = isYellow ? trainerClassAmountsY
				: trainerClassAmountsRB;

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			pointers[i] = 0x38000 + (rom[traineroffset + (i - 1) * 2] & 0xFF)
					+ ((rom[traineroffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		if (isYellow) {
			// Yellow's table is at 0x39C6B
			// Format is:
			// The following struct repeated, 0xFF-terminated:
			// (Byte) Trainer class (01-2F for Youngster-Lance)
			// (Byte) Number in class (starting from 1)
			// Then the following 3-byte struct repeated as many times
			// as needed, 00-terminated:
			// (Byte) Pokemon Number in Team (1-6)
			// (Byte) Moveslot (1-4)
			// (Byte) Move Number
			// It's pretty useless with new teams etc,
			// so just null it out immediately.
			rom[0x39C6B] = (byte) 0xFF;
		} else {
			// Erase the "custom moves for E4" table only.
			// The "custom moves for Gym Leaders" is driven by TMs,
			// and we may as well keep Gym Leaders' ability to use the
			// moves in the TMs they give you.
			// Format here is just TrainerClass,Move (FF-terminated)
			// Table is at 0x39D32, all we have to do is null immediately
			rom[0x39D32] = (byte) 0xFF; // End Table
		}

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

	private void tboffs(List<Trainer> allTrainers, int offset, String tag) {
		for (Trainer t : allTrainers) {
			if (t.offset == offset) {
				t.tag = tag;
				return;
			}
		}
	}

	@Override
	public boolean isYellow() {
		return isYellow;
	}

	@Override
	public boolean typeInGame(Type type) {
		return type.isInRBY();
	}

	public void debugMoveData() {
		int pointersOffset = 0x3b05c;
		for (int i = 1; i <= 190; i++) {
			int realPointer = 0x38000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
			if (pokeRBYToNumTable[i] != 0) {
				System.out.println("POKEMON "
						+ pokes[pokeRBYToNumTable[i]].name);
				System.out.printf("Pointer is %05X\n", realPointer);
				int statsOffset = 0;
				if (pokeRBYToNumTable[i] == 151 && !isYellow) {
					// Mewww
					statsOffset = mewStatsOffsetRB;
				} else {
					statsOffset = (pokeRBYToNumTable[i] - 1) * 0x1C
							+ pokeStatsOffset;
				}
				for (int delta = 0x0E; delta < 0x12; delta++) {
					if (rom[statsOffset + delta] != 0x00) {
						System.out
								.println(RomFunctions.moveNames[rom[statsOffset
										+ delta] & 0xFF]
										+ " at level 1");
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
					int level = rom[realPointer] & 0xFF;
					int move = rom[realPointer + 1] & 0xFF;
					System.out.println(RomFunctions.moveNames[move]
							+ " at level " + level);
					realPointer += 2;
				}
			}
		}
	}

	@Override
	public void fixTypeEffectiveness() {
		int base = (isYellow) ? typeEffectOffsetY : typeEffectOffsetRB;
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
		int pointersOffset = isYellow ? movePointersOffsetY
				: movePointersOffsetRB;
		for (int i = 1; i <= 190; i++) {
			int realPointer = 0x38000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
			if (pokeRBYToNumTable[i] != 0) {
				Pokemon pkmn = pokes[pokeRBYToNumTable[i]];
				int statsOffset = 0;
				if (pokeRBYToNumTable[i] == 151 && !isYellow) {
					// Mewww
					statsOffset = mewStatsOffsetRB;
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
		int pointersOffset = isYellow ? movePointersOffsetY
				: movePointersOffsetRB;
		for (int i = 1; i <= 190; i++) {
			int realPointer = 0x38000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
			if (pokeRBYToNumTable[i] != 0) {
				Pokemon pkmn = pokes[pokeRBYToNumTable[i]];
				List<MoveLearnt> ourMoves = movesets.get(pkmn);
				int statsOffset = 0;
				if (pokeRBYToNumTable[i] == 151 && !isYellow) {
					// Mewww
					statsOffset = mewStatsOffsetRB;
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

	/* @formatter:off */
	private static int[] staticOffsetsRB = new int[] {
			// Pokeballs
			0x1DD49, // Eevee in Celadon Mansion
			0x5CF5F,
			0x5CF17, // Hitmonchan/Hitmonlee in Fighting Dojo
			// Overworld Battles
			0x1e3d5, 0x1e3dd, 0x1e3e5, 0x1e3ed, 0x1e3f5, 0x1e3fd, 0x1e405,
			0x1e40d, // Voltorb/Electrode in The Power Plant
			0x1e415, // Zapdos in the Power Plant
			0x468e8, // Articuno in the Seafoams
			0x51963, // Moltres in Victory Road
			0x45f44, // Mewtwo in Unk. Dungeon
			0x59630, 0x59970, // Sleeping Snorlaxes
			// Fossils
			0x61064, // Old Amber
			0x61068, 0x6106C, // Helix & Dome Fossils
			// Given by Person
			0x51DAD, // Lapras in Silph. Co
			0x49320, // Magikarp in Mt.Moon Center
			// Game Corner
			0x52859, 0x5285A, 0x5285B, // Abra/Clefairy/Nidorin(a/o) Counter
			0x52864, 0x52865, 0x52866, // (Dratini/Pinsir)/(Scyther/Dratini)/Porygon
										// Counter
	};
	// Yellow item ball sprite is 0x47 not 0x3D
	private static int[] staticOffsetsY = new int[] {
			// Pokeballs
			0x1D652, // Eevee in Celadon Mansion
			0x5CE55,
			0x5CE0D, // Hitmonchan/Hitmonlee in Fighting Dojo
			// Overworld Battles
			0x1DCDF, 0x1DCE7, 0x1DCEF, 0x1DCF7, 0x1DCFF, 0x1DD07, 0x1DD0F,
			0x1DD17, // Voltorb/Electrode in The Power Plant
			0x1DD1F, // Zapdos in the Power Plant
			0x46B5A, // Articuno in the Seafoams
			0x519A5, // Moltres in Victory Road
			0x461A5, // Mewtwo in Unk. Dungeon
			0x594CC, 0x5980C, // Sleeping Snorlaxes
			// Fossils
			0x61050, // Old Amber
			0x61054, 0x61058, // Helix & Dome Fossils
			// Given By Person
			0x51DD6, // Lapras in Silph. Co
			0xF21C0, // Magikarp in Mt.Moon Center
			0x1CF8C, // Bulbasaur in Cerulean
			0x515AF, // Charmander in Route 24/25
			0xF1A45, // Squirtle in Vermillion
			// Game Corner
			0x527BA, 0x527BB, 0x527BC, // Abra/Vulpix/Wigglytuff Counter
			0x527C5, 0x527C6, 0x527C7, // Scyther/Pinsir/Porygon Counter
	};

	/* @formatter:on */

	@Override
	public List<Pokemon> getStaticPokemon() {
		int[] offsets = isYellow ? staticOffsetsY : staticOffsetsRB;
		List<Pokemon> statics = new ArrayList<Pokemon>();
		for (int offset : offsets) {
			statics.add(pokes[pokeRBYToNumTable[rom[offset] & 0xFF]]);
		}
		return statics;
	}

	@Override
	public boolean setStaticPokemon(List<Pokemon> staticPokemon) {
		int[] offsets = isYellow ? staticOffsetsY : staticOffsetsRB;
		if (staticPokemon.size() != offsets.length) {
			return false;
		}
		for (Pokemon pkmn : staticPokemon) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}
		for (int i = 0; i < offsets.length; i++) {
			rom[offsets[i]] = (byte) pokeNumToRBYTable[staticPokemon.get(i).number];
		}
		return true;
	}

	private static final int tmMovesOffsetRB = 0x13773,
			tmMovesOffsetY = 0x1232D;

	@Override
	public List<Integer> getTMMoves() {
		List<Integer> tms = new ArrayList<Integer>();
		int offset = isYellow ? tmMovesOffsetY : tmMovesOffsetRB;
		for (int i = 1; i <= 50; i++) {
			tms.add(rom[offset + (i - 1)] & 0xFF);
		}
		return tms;
	}

	@Override
	public List<Integer> getHMMoves() {
		List<Integer> hms = new ArrayList<Integer>();
		int offset = isYellow ? tmMovesOffsetY : tmMovesOffsetRB;
		for (int i = 1; i <= 5; i++) {
			hms.add(rom[offset + 50 + (i - 1)] & 0xFF);
		}
		return hms;
	}

	@Override
	public void setTMMoves(List<Integer> moveIndexes) {
		int offset = isYellow ? tmMovesOffsetY : tmMovesOffsetRB;
		for (int i = 1; i <= 50; i++) {
			rom[offset + (i - 1)] = moveIndexes.get(i - 1).byteValue();
		}

		// Gym Leader TM Moves (RB only)
		if (!isYellow) {
			int[] tms = new int[] { 34, 11, 24, 21, 6, 46, 38, 27 };
			for (int i = 0; i < tms.length; i++) {
				// Set the special move used by gym (i+1) to
				// the move we just wrote to TM tms[i]
				rom[0x39D23 + i * 2] = moveIndexes.get(tms[i] - 1).byteValue();
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
		for (int i = 1; i <= 151; i++) {
			int baseStatsOffset = (isYellow || i != 151) ? (pokeStatsOffset + (i - 1) * 0x1C)
					: mewStatsOffsetRB;
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
		for (Map.Entry<Pokemon, boolean[]> compatEntry : compatData.entrySet()) {
			Pokemon pkmn = compatEntry.getKey();
			boolean[] flags = compatEntry.getValue();
			int baseStatsOffset = (isYellow || pkmn.number != 151) ? (pokeStatsOffset + (pkmn.number - 1) * 0x1C)
					: mewStatsOffsetRB;
			for (int j = 0; j < 7; j++) {
				rom[baseStatsOffset + 0x14 + j] = getByteFromFlags(flags,
						j * 8 + 1);
			}
		}
	}

	@Override
	public String getROMName() {
		if (isYellow) {
			return "Pokemon Yellow";
		} else if (romSig(rom, "POKEMON RED")) {
			return "Pokemon Red";
		} else {
			return "Pokemon Blue";
		}
	}

	@Override
	public String getROMCode() {
		return "";
	}

	@Override
	public String getSupportLevel() {
		return "Complete";
	}

	@Override
	public void removeTradeEvolutions() {
		// Gen 1: evolution data is right before moveset data
		// So use those pointers
		int pointersOffset = isYellow ? movePointersOffsetY
				: movePointersOffsetRB;
		log("--Removing Trade Evolutions--");
		for (int i = 1; i <= 190; i++) {
			int realPointer = 0x38000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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

	private static final int[][] tclassesOffsets = new int[][] {
			new int[] { 0x27EC2, 0x399FF }, new int[] { 0x27E77, 0x3997E } };
	private static final int[] tclassesCounts = new int[] { 21, 47 };
	private static final List<Integer> singularTrainers = Arrays.asList(28, 32,
			33, 34, 35, 36, 37, 38, 39, 43, 45, 46);

	@Override
	public List<String> getTrainerNames() {
		int[] offsets = isYellow ? tclassesOffsets[1] : tclassesOffsets[0];
		List<String> trainerNames = new ArrayList<String>();
		int offset = offsets[1];
		for (int j = 0; j < tclassesCounts[1]; j++) {
			String name = readVariableLengthString(offset);
			offset += (name.length() + 1);
			if (singularTrainers.contains(j)) {
				trainerNames.add(name);
			}
		}
		return trainerNames;
	}

	@Override
	public void setTrainerNames(List<String> trainerNames) {
		int[] offsets = isYellow ? tclassesOffsets[1] : tclassesOffsets[0];
		Iterator<String> trainerNamesI = trainerNames.iterator();
		int offset = offsets[1];
		for (int j = 0; j < tclassesCounts[1]; j++) {
			String name = readVariableLengthString(offset);
			if (singularTrainers.contains(j)) {
				String newName = trainerNamesI.next();
				writeFixedLengthString(newName, offset, name.length() + 1);
			}
			offset += (name.length() + 1);
		}
	}

	@Override
	public boolean fixedTrainerNamesLength() {
		return true;
	}

	@Override
	public List<String> getTrainerClassNames() {
		int[] offsets = isYellow ? tclassesOffsets[1] : tclassesOffsets[0];
		List<String> trainerClassNames = new ArrayList<String>();
		for (int i = 0; i < offsets.length; i++) {
			int offset = offsets[i];
			for (int j = 0; j < tclassesCounts[i]; j++) {
				String name = readVariableLengthString(offset);
				offset += (name.length() + 1);
				if (i == 0 || !singularTrainers.contains(j)) {
					trainerClassNames.add(name);
				}
			}
		}
		return trainerClassNames;
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		int[] offsets = isYellow ? tclassesOffsets[1] : tclassesOffsets[0];
		Iterator<String> tcNamesIter = trainerClassNames.iterator();
		for (int i = 0; i < offsets.length; i++) {
			int offset = offsets[i];
			for (int j = 0; j < tclassesCounts[i]; j++) {
				String name = readVariableLengthString(offset);
				if (i == 0 || !singularTrainers.contains(j)) {
					String newName = tcNamesIter.next();
					writeFixedLengthString(newName, offset, name.length() + 1);
				}
				offset += (name.length() + 1);
			}
		}

	}

	@Override
	public boolean fixedTrainerClassNamesLength() {
		return true;
	}

	@Override
	public String getDefaultExtension() {
		if (isYellow) {
			return "gbc";
		} else {
			return "gb";
		}
	}

	@Override
	public int abilitiesPerPokemon() {
		return 0;
	}

	@Override
	public int highestAbilityIndex() {
		return 0;
	}
}
