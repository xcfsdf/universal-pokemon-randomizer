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

public class Gen2RomHandler extends AbstractGBRomHandler {

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
		table[0xe9] = '&';
		table[0xe0] = '\'';
		table[0xf4] = ',';
		table[0x4a] = 'ƥ';
		table[0x75] = '…';
		table[0xd4] = 'ṡ';
		table[0xd5] = 'ṫ';

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

	private static int pokeNamesOffsetGS = 0x1B0B74,
			pokeNamesOffsetC = 0x53384;
	private static int pokeStatsOffsetGS = 0x51B0B, pokeStatsOffsetC = 0x51424;
	private static int movesOffsetGS = 0x41AFF, movesOffsetC = 0x41AFC;
	private static final int wildOffsetGS = 0x2AB35, wildOffsetC = 0x2A5E9;
	private final static int trainerOffsetGS = 0x3993E,
			trainerOffsetC = 0x39999;
	private final static int trainerAmountGS = 0x42, trainerAmountC = 0x43;
	private final static int[] trainerClassAmountsC = new int[] { 0, 1, 1, 1,
			1, 1, 1, 1, 1, 15, 0, 1, 3, 1, 1, 1, 1, 1, 1, 1, 5, 1, 14, 24, 19,
			17, 1, 20, 21, 17, 15, 31, 5, 2, 3, 1, 19, 25, 21, 19, 13, 14, 6,
			2, 22, 9, 1, 3, 8, 6, 9, 4, 12, 26, 22, 2, 12, 7, 3, 14, 6, 10, 6,
			1, 1, 2, 5, 1 };
	private final static int[] trainerClassAmountsGS = new int[] { 0, 1, 1, 1,
			1, 1, 1, 1, 1, 15, 0, 1, 3, 1, 1, 1, 1, 1, 1, 1, 5, 1, 12, 18, 19,
			15, 1, 19, 20, 16, 13, 31, 5, 2, 3, 1, 14, 22, 21, 19, 12, 12, 6,
			2, 20, 9, 1, 3, 8, 5, 9, 4, 12, 21, 19, 2, 9, 7, 3, 12, 6, 8, 5, 1,
			1, 2, 5 };
	private static final int movePointersOffsetGS = 0x427BD,
			movePointersOffsetC = 0x425B1;
	private static final int tmMovesOffsetGS = 0x11A66,
			tmMovesOffsetC = 0x1167A;
	private static final int tclassesOffsetGS = 0x1B0955,
			tclassesOffsetC = 0x2C1EF;

	// This ROM's data
	private Pokemon[] pokes;
	private Move[] moves;
	private boolean isCrystal;

	@Override
	public boolean detectRom(byte[] rom) {
		if (rom.length != 2097152) {
			return false; // size check
		}
		if (romSig(rom, "POKEMON_GLDAAUE") || romSig(rom, "POKEMON_SLVAAXE")
				|| romSig(rom, "PM_CRYSTAL")) {
			return true; // right
		}
		return false; // GB rom we don't support yet
	}

	@Override
	public void loadedRom() {
		isCrystal = romSig(rom, "PM_CRYSTAL");
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
		for (int i = 1; i <= 251; i++) {
			System.out.println(pokes[i].toString());
		}
	}

	private void loadPokemonStats() {
		pokes = new Pokemon[252];
		// Fetch our names
		String[] pokeNames = readPokemonNames();
		int offs = isCrystal ? pokeStatsOffsetC : pokeStatsOffsetGS;
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
		int offs = isCrystal ? pokeNamesOffsetC : pokeNamesOffsetGS;
		for (int i = 1; i <= 251; i++) {
			int stringOffset = offs + (i - 1) * 10;
			writeFixedLengthString(pokes[i].name, stringOffset, 10);
		}
		// Write pokemon stats
		int offs2 = isCrystal ? pokeStatsOffsetC : pokeStatsOffsetGS;
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
		int offs = isCrystal ? movesOffsetC : movesOffsetGS;
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
		int offs = isCrystal ? movesOffsetC : movesOffsetGS;
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
	}

	private String[] readPokemonNames() {
		int offs = isCrystal ? pokeNamesOffsetC : pokeNamesOffsetGS;
		String[] names = new String[252];
		for (int i = 1; i <= 251; i++) {
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

	public String readVariableLengthScriptString(int offset) {
		String name = "";
		int offs = 0;
		while (true) {
			if (rom[offset + offs] == 0x0 || rom[offset + offs] == 0x50) {
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
		return (pkmn.number >= 1 && pkmn.number <= 251);
	}

	@Override
	public boolean isInGame(int pokemonNumber) {
		return (pokemonNumber >= 1 && pokemonNumber <= 251);
	}

	@Override
	public List<Pokemon> getStarters() {
		// Read starters
		if (isCrystal) {
			Pokemon start1 = pokes[rom[0x78C7F] & 0xFF];
			Pokemon start2 = pokes[rom[0x78CC1] & 0xFF];
			Pokemon start3 = pokes[rom[0x78CFD] & 0xFF];
			return Arrays.asList(start1, start2, start3);
		} else {
			Pokemon start1 = pokes[rom[0x1800D2] & 0xFF];
			Pokemon start2 = pokes[rom[0x180114] & 0xFF];
			Pokemon start3 = pokes[rom[0x180150] & 0xFF];
			return Arrays.asList(start1, start2, start3);
		}
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
		// 4 pointers for us, 1 for rival per starter
		// 0 1 2 us => 1 2 0 them

		byte starter0 = (byte) newStarters.get(0).number;
		byte starter1 = (byte) newStarters.get(1).number;
		byte starter2 = (byte) newStarters.get(2).number;

		if (isCrystal) {

			rom[0x78C7F] = starter0;
			rom[0x78C81] = starter0;
			rom[0x78C98] = starter0;
			rom[0x78CA3] = starter0;

			rom[0x78CC1] = starter1;
			rom[0x78CC3] = starter1;
			rom[0x78CDA] = starter1;
			rom[0x78CE5] = starter1;

			rom[0x78CFD] = starter2;
			rom[0x78CFF] = starter2;
			rom[0x78D16] = starter2;
			rom[0x78D21] = starter2;

		} else {
			rom[0x1800D2] = starter0;
			rom[0x1800D4] = starter0;
			rom[0x1800EB] = starter0;
			rom[0x1800F6] = starter0;

			rom[0x180114] = starter1;
			rom[0x180116] = starter1;
			rom[0x18012D] = starter1;
			rom[0x180138] = starter1;

			rom[0x180150] = starter2;
			rom[0x180152] = starter2;
			rom[0x180169] = starter2;
			rom[0x180174] = starter2;
		}

		// Attempt to replace text
		List<Integer> cyndaTexts = RomFunctions.search(rom,
				traduire("CYNDAQUIL"));
		int offset = cyndaTexts.get(isCrystal ? 1 : 0);
		String pokeName = newStarters.get(0).name;
		writeFixedLengthScriptString(pokeName + "?»", offset,
				readVariableLengthScriptString(offset).length() + 1);

		List<Integer> totoTexts = RomFunctions
				.search(rom, traduire("TOTODILE"));
		offset = totoTexts.get(isCrystal ? 1 : 0);
		pokeName = newStarters.get(1).name;
		writeFixedLengthScriptString(pokeName + "?»", offset,
				readVariableLengthScriptString(offset).length() + 1);

		List<Integer> chikoTexts = RomFunctions.search(rom,
				traduire("CHIKORITA"));
		offset = chikoTexts.get(isCrystal ? 1 : 0);
		pokeName = newStarters.get(2).name;
		writeFixedLengthScriptString(pokeName + "?»", offset,
				readVariableLengthScriptString(offset).length() + 1);

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
		int offset = isCrystal ? wildOffsetC : wildOffsetGS;
		List<EncounterSet> areas = new ArrayList<EncounterSet>();
		offset = readLandEncounters(offset, areas, useTimeOfDay); // Johto
		offset = readSeaEncounters(offset, areas); // Johto
		offset = readLandEncounters(offset, areas, useTimeOfDay); // Kanto
		offset = readSeaEncounters(offset, areas); // Kanto
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
		int offset = isCrystal ? wildOffsetC : wildOffsetGS;
		Iterator<EncounterSet> areas = encounters.iterator();
		offset = writeLandEncounters(offset, areas, useTimeOfDay); // Johto
		offset = writeSeaEncounters(offset, areas); // Johto
		offset = writeLandEncounters(offset, areas, useTimeOfDay); // Kanto
		offset = writeSeaEncounters(offset, areas); // Kanto
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
		int traineroffset = isCrystal ? trainerOffsetC : trainerOffsetGS;
		int traineramount = isCrystal ? trainerAmountC : trainerAmountGS;
		int[] trainerclasslimits = isCrystal ? trainerClassAmountsC
				: trainerClassAmountsGS;

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
		return allTrainers;
	}

	@Override
	public void setTrainers(List<Trainer> trainerData) {
		int traineroffset = isCrystal ? trainerOffsetC : trainerOffsetGS;
		int traineramount = isCrystal ? trainerAmountC : trainerAmountGS;
		int[] trainerclasslimits = isCrystal ? trainerClassAmountsC
				: trainerClassAmountsGS;

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

		// Gym Trainers (new approach)
		tbnom(allTrainers, 24, "ROD", "GYM1");
		tbnom(allTrainers, 24, "ABE", "GYM1");

		tbnom(allTrainers, 36, "BENNY", "GYM2");
		tbnom(allTrainers, 36, "AL", "GYM2");
		tbnom(allTrainers, 36, "JOSH", "GYM2");
		tbnom(allTrainers, 61, "AMY & MAY", "GYM2");

		tbnom(allTrainers, 25, "CARRIE", "GYM3");
		tbnom(allTrainers, 25, "BRIDGET", "GYM3");
		tbnom(allTrainers, 29, "VICTORIA", "GYM3");
		tbnom(allTrainers, 29, "SAMANTHA", "GYM3");

		tbnom(allTrainers, 56, "JEFFREY", "GYM4");
		tbnom(allTrainers, 56, "PING", "GYM4");
		tbnom(allTrainers, 57, "MARTHA", "GYM4");
		tbnom(allTrainers, 57, "GRACE", "GYM4");

		tbnom(allTrainers, 50, "YOSHI", "GYM5");
		tbnom(allTrainers, 50, "LAO", "GYM5");
		tbnom(allTrainers, 50, "NOB", "GYM5");
		tbnom(allTrainers, 50, "LUNG", "GYM5");

		tbnom(allTrainers, 58, "RONALD", "GYM7");
		tbnom(allTrainers, 58, "BRAD", "GYM7");
		tbnom(allTrainers, 58, "DOUGLAS", "GYM7");
		tbnom(allTrainers, 33, "ROXANNE", "GYM7");
		tbnom(allTrainers, 33, "CLARISSA", "GYM7");

		tbnom(allTrainers, 27, "PAUL", "GYM8");
		tbnom(allTrainers, 27, "MIKE", "GYM8");
		tbnom(allTrainers, 27, "CODY", "GYM8");
		tbnom(allTrainers, 28, "FRAN", "GYM8");
		tbnom(allTrainers, 28, "LOLA", "GYM8");

		tbnom(allTrainers, 54, "JERRY", "GYM9");

		tbnom(allTrainers, 38, "PARKER", "GYM10");
		tbnom(allTrainers, 39, "DIANA", "GYM10");
		tbnom(allTrainers, 39, "BRIANA", "GYM10");

		tbnom(allTrainers, 49, "HORTON", "GYM11");
		tbnom(allTrainers, 43, "VINCENT", "GYM11");
		tbnom(allTrainers, 32, "GREGORY", "GYM11");

		tbnom(allTrainers, 61, "JO & ZOE", "GYM12");
		tbnom(allTrainers, 25, "MICHELLE", "GYM12");
		tbnom(allTrainers, 53, "TANYA", "GYM12");
		tbnom(allTrainers, 29, "JULIA", "GYM12");

		tbnom(allTrainers, 25, "ALICE", "GYM13");
		tbnom(allTrainers, 25, "LINDA", "GYM13");
		tbnom(allTrainers, 53, "CINDY", "GYM13");
		tbnom(allTrainers, 54, "BARRY", "GYM13");

		tbnom(allTrainers, 57, "REBECCA", "GYM14");
		tbnom(allTrainers, 57, "DORIS", "GYM14");
		tbnom(allTrainers, 52, "FRANKLIN", "GYM14");
		tbnom(allTrainers, 52, "JARED", "GYM14");

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

	private void tbnom(List<Trainer> allTrainers, int classNum, String name,
			String tag) {
		for (Trainer t : allTrainers) {
			if (t.trainerclass == classNum) {
				if (t.name.equalsIgnoreCase(name)) {
					t.tag = tag;
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
		int pointersOffset = isCrystal ? movePointersOffsetC
				: movePointersOffsetGS;
		for (int i = 1; i <= 251; i++) {
			int realPointer = 0x40000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		int pointersOffset = isCrystal ? movePointersOffsetC
				: movePointersOffsetGS;
		for (int i = 1; i <= 251; i++) {
			int realPointer = 0x40000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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

	/* @formatter:off */
	private static int[] staticOffsetsGS = new int[] { 
		// Overworld Battles
		0x111775, // Lapras in Union Cave
		0x114DBD, 0x114DE8, 0x114E13, // Electrodes in Rocket Base
		0x11C1B6, // Lugia in Whirlpool (Gold) (Copied at 11C1C1 for Silver)
		0x124F7A, // Red Gyarados in Lake of Rage
		0x12E1D6, // Sudowoodo on Route 36
		0x13D2AB, // Snorlax in front of Digletts Cave
		0x16E929, // Ho-Oh on Tin Tower (Gold) (Copied at 16E934 for Silver)
		// Rocket Base Tiles
		0x1146FE, 0x114711, 0x114724, // Voltorb, Geodude, Koffing
		// Given by Person
		0x73E6, // Shuckle in Cinawood
		0x119F20, // Tyrogue from Fighting Master
		0x15924F, // Togepi Egg from Elm Assistant
		0x1599FC, // Spearow Mail Delivery
		0x15CC10, // Eevee from Bill
		// Game Corner
		0x15E8B7, 0x15E8E5, 0x15E913, // Abra, Ekans, Dratini (Gold)
		0x15E99C, 0x15E9CA, 0x15E9F8, // Abra, Sandshrew, Dratini (Silver)
		0x179B9C, 0x179BCA, 0x179BF8, // Mr.Mime, Eevee, Porygon
	};
	private static int[] staticOffsetsC = new int[] {
		// Overworld Battles
		0x5A324, // Lapras in Union Cave
		0x6D105, 0x6D130, 0x6D15B, // Electrodes in Rocket Base
		0x18C52A, // Lugia in Whirlpool
		0x7006E, // Red Gyarados in Lake of Rage
		0x194068, // Sudowoodo on Route 36
		0x1AA9B8, // Snorlax in front of Digletts Cave
		0x77256, // Ho-Oh on Tin Tower
		0x1850EA, // Suicune on Tin Tower ground floor
		// Rocket Base Tiles
		0x6CA43, 0x6CA56, 0x6CA69, // Voltorb, Geodude, Koffing
		// Given by Person
		0x730A, // Shuckle in Cinawood
		0x7E22A, // Tyrogue from Fighting Master
		0x694E2, // Togepi Egg from Elm Assistant
		0x69D65, // Spearow Mail Delivery
		0x54C06, // Eevee from Bill
		0x18D1D7, // Dratini from Dragons Den
		// Game Corner
		0x56D34, 0x56D62, 0x56D90, // Abra, Cubone, Wobbuffet
		0x727FB, 0x72829, 0x72857, // Pikachu, Porygon, Larvitar
		// Odd Egg
		0x1FB56E, 0x1FB5A9, // Pichu
		0x1FB5E4, 0x1FB61F, // Cleffa
		0x1FB65A, 0x1FB695, // Igglybuff
		0x1FB6D0, 0x1FB70B, // Smoochum
		0x1FB746, 0x1FB781, // Magby
		0x1FB7BC, 0x1FB7F7, // Elekid
		0x1FB832, 0x1FB86D, // Tyrogue
	};
	/* @formatter:on */

	@Override
	public List<Pokemon> getStaticPokemon() {
		int[] offsets = isCrystal ? staticOffsetsC : staticOffsetsGS;
		List<Pokemon> statics = new ArrayList<Pokemon>();
		for (int offset : offsets) {
			statics.add(pokes[rom[offset] & 0xFF]);
		}
		return statics;
	}

	@Override
	public boolean setStaticPokemon(List<Pokemon> staticPokemon) {
		int[] offsets = isCrystal ? staticOffsetsC : staticOffsetsGS;
		if (staticPokemon.size() != offsets.length) {
			return false;
		}
		for (Pokemon pkmn : staticPokemon) {
			if (!isInGame(pkmn)) {
				return false;
			}
		}
		for (int i = 0; i < offsets.length; i++) {
			rom[offsets[i]] = (byte) staticPokemon.get(i).number;
		}

		// Tidy-up game corner & Ho-Oh/Lugia
		if (isCrystal) {
			// Goldenrod Game Corner
			// Extra Offsets
			rom[0x56D34 + 0x11] = rom[0x56D34];
			rom[0x56D34 + 0x16] = rom[0x56D34];

			rom[0x56D62 + 0x11] = rom[0x56D62];
			rom[0x56D62 + 0x16] = rom[0x56D62];

			rom[0x56D90 + 0x11] = rom[0x56D90];
			rom[0x56D90 + 0x16] = rom[0x56D90];

			// Text
			writePaddedPokemonName(pokes[rom[0x56D34] & 0xFF].name, 11, 0x56DBA);
			writePaddedPokemonName(pokes[rom[0x56D62] & 0xFF].name, 11, 0x56DCA);
			writePaddedPokemonName(pokes[rom[0x56D90] & 0xFF].name, 11, 0x56DDA);

			// Celadon Game Corner
			// Extra Offsets
			rom[0x727FB + 0x11] = rom[0x727FB];
			rom[0x727FB + 0x16] = rom[0x727FB];

			rom[0x72829 + 0x11] = rom[0x72829];
			rom[0x72829 + 0x16] = rom[0x72829];

			rom[0x72857 + 0x11] = rom[0x72857];
			rom[0x72857 + 0x16] = rom[0x72857];

			// Text
			writePaddedPokemonName(pokes[rom[0x727FB] & 0xFF].name, 11, 0x72881);
			writePaddedPokemonName(pokes[rom[0x72829] & 0xFF].name, 11, 0x72891);
			writePaddedPokemonName(pokes[rom[0x72857] & 0xFF].name, 11, 0x728A1);
		} else {
			// G/S Lugia & Ho-Oh
			rom[0x11C1C1] = rom[0x11C1B6];
			rom[0x16E934] = rom[0x16E929];

			// Goldenrod Game Corner
			// Extra Offsets
			rom[0x15E8B7 + 0x11] = rom[0x15E8B7];
			rom[0x15E8B7 + 0x16] = rom[0x15E8B7];

			rom[0x15E8E5 + 0x11] = rom[0x15E8E5];
			rom[0x15E8E5 + 0x16] = rom[0x15E8E5];

			rom[0x15E913 + 0x11] = rom[0x15E913];
			rom[0x15E913 + 0x16] = rom[0x15E913];

			rom[0x15E99C + 0x11] = rom[0x15E99C];
			rom[0x15E99C + 0x16] = rom[0x15E99C];

			rom[0x15E9CA + 0x11] = rom[0x15E9CA];
			rom[0x15E9CA + 0x16] = rom[0x15E9CA];

			rom[0x15E9F8 + 0x11] = rom[0x15E9F8];
			rom[0x15E9F8 + 0x16] = rom[0x15E9F8];

			// Text
			writePaddedPokemonName(pokes[rom[0x15E8B7] & 0xFF].name, 11,
					0x15E93D);
			writePaddedPokemonName(pokes[rom[0x15E8E5] & 0xFF].name, 11,
					0x15E94D);
			writePaddedPokemonName(pokes[rom[0x15E913] & 0xFF].name, 11,
					0x15E95D);

			writePaddedPokemonName(pokes[rom[0x15E99C] & 0xFF].name, 11,
					0x15EA22);
			writePaddedPokemonName(pokes[rom[0x15E9CA] & 0xFF].name, 11,
					0x15EA32);
			writePaddedPokemonName(pokes[rom[0x15E9F8] & 0xFF].name, 11,
					0x15EA42);

			// Celadon Game Corner
			// Extra Offsets
			rom[0x179B9C + 0x11] = rom[0x179B9C];
			rom[0x179B9C + 0x16] = rom[0x179B9C];

			rom[0x179BCA + 0x11] = rom[0x179BCA];
			rom[0x179BCA + 0x16] = rom[0x179BCA];

			rom[0x179BF8 + 0x11] = rom[0x179BF8];
			rom[0x179BF8 + 0x16] = rom[0x179BF8];

			// Text
			writePaddedPokemonName(pokes[rom[0x179B9C] & 0xFF].name, 11,
					0x179C22);
			writePaddedPokemonName(pokes[rom[0x179BCA] & 0xFF].name, 11,
					0x179C32);
			writePaddedPokemonName(pokes[rom[0x179BF8] & 0xFF].name, 11,
					0x179C42);
		}
		return true;
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
		int offset = isCrystal ? tmMovesOffsetC : tmMovesOffsetGS;
		for (int i = 1; i <= 50; i++) {
			tms.add(rom[offset + (i - 1)] & 0xFF);
		}
		return tms;
	}

	@Override
	public List<Integer> getHMMoves() {
		List<Integer> hms = new ArrayList<Integer>();
		int offset = isCrystal ? tmMovesOffsetC : tmMovesOffsetGS;
		for (int i = 1; i <= 7; i++) {
			hms.add(rom[offset + 50 + (i - 1)] & 0xFF);
		}
		return hms;
	}

	@Override
	public void setTMMoves(List<Integer> moveIndexes) {
		int offset = isCrystal ? tmMovesOffsetC : tmMovesOffsetGS;
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
			int baseStatsOffset = (isCrystal ? pokeStatsOffsetC
					: pokeStatsOffsetGS) + (i - 1) * 0x20;
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
			int baseStatsOffset = (isCrystal ? pokeStatsOffsetC
					: pokeStatsOffsetGS) + (pkmn.number - 1) * 0x20;
			for (int j = 0; j < 8; j++) {
				if (!isCrystal || j != 7) {
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

	public void debugMoveData() {
		int pointersOffset = 0x427bd;
		for (int i = 1; i <= 251; i++) {
			int realPointer = 0x40000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;

			System.out.println("POKEMON " + pokes[i].name);
			System.out.printf("Pointer is %05X\n", realPointer);
			// Skip over evolution data
			while (rom[realPointer] != 0) {
				System.out.println(rom[realPointer] + " "
						+ rom[realPointer + 1] + " " + rom[realPointer + 2]
						+ " " + rom[realPointer + 3] + " "
						+ rom[realPointer + 4] + " " + rom[realPointer + 5]
						+ " " + rom[realPointer + 6]);
				System.out.println();
				if (rom[realPointer] == 5) {
					realPointer += 4;
				} else {
					realPointer += 3;
				}
			}
			realPointer++;
			while (rom[realPointer] != 0) {
				int level = rom[realPointer] & 0xFF;
				int move = rom[realPointer + 1] & 0xFF;
				System.out.println(RomFunctions.moveNames[move] + " at level "
						+ level);
				realPointer += 2;
			}
		}
	}

	@Override
	public String getROMName() {
		if (isCrystal) {
			return "Pokemon Crystal";
		} else if (romSig(rom, "POKEMON_GLDAAUE")) {
			return "Pokemon Gold";
		} else {
			return "Pokemon Silver";
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
	public boolean hasTimeBasedEncounters() {
		return true; // All GSC do
	}

	@Override
	public void removeTradeEvolutions() {
		// Evolution data is stored before learnmoves.
		int pointersOffset = isCrystal ? movePointersOffsetC
				: movePointersOffsetGS;
		log("--Removing Trade Evolutions--");
		for (int i = 1; i <= 251; i++) {
			int realPointer = 0x40000
					+ (rom[pointersOffset + (i - 1) * 2] & 0xFF)
					+ ((rom[pointersOffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		int traineroffset = isCrystal ? trainerOffsetC : trainerOffsetGS;
		int traineramount = isCrystal ? trainerAmountC : trainerAmountGS;
		int[] trainerclasslimits = isCrystal ? trainerClassAmountsC
				: trainerClassAmountsGS;

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			pointers[i] = 0x38000 + (rom[traineroffset + (i - 1) * 2] & 0xFF)
					+ ((rom[traineroffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		int traineroffset = isCrystal ? trainerOffsetC : trainerOffsetGS;
		int traineramount = isCrystal ? trainerAmountC : trainerAmountGS;
		int[] trainerclasslimits = isCrystal ? trainerClassAmountsC
				: trainerClassAmountsGS;

		int[] pointers = new int[traineramount + 1];
		for (int i = 1; i <= traineramount; i++) {
			pointers[i] = 0x38000 + (rom[traineroffset + (i - 1) * 2] & 0xFF)
					+ ((rom[traineroffset + (i - 1) * 2 + 1] & 0xFF) - 0x40)
					* 0x100;
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
		int amount = (isCrystal ? trainerClassAmountsC.length
				: trainerClassAmountsGS.length) - 1;
		int offset = isCrystal ? tclassesOffsetC : tclassesOffsetGS;
		List<String> trainerClassNames = new ArrayList<String>();
		for (int j = 0; j < amount; j++) {
			String name = readVariableLengthString(offset);
			offset += (name.length() + 1);
			trainerClassNames.add(name);
		}
		return trainerClassNames;
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		int amount = (isCrystal ? trainerClassAmountsC.length
				: trainerClassAmountsGS.length) - 1;
		int offset = isCrystal ? tclassesOffsetC : tclassesOffsetGS;
		Iterator<String> trainerClassNamesI = trainerClassNames.iterator();
		for (int j = 0; j < amount; j++) {
			String name = readVariableLengthString(offset);
			String newName = trainerClassNamesI.next();
			writeFixedLengthString(newName, offset, name.length() + 1);
			offset += (name.length() + 1);
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

}
