package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  Gen4RomHandler.java - randomizer handler for D/P/Pt/HG/SS.			--*/
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import thenewpoketext.PokeTextData;
import thenewpoketext.TextToPoke;

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

public class Gen4RomHandler extends AbstractDSRomHandler {

	// Statics
	private static final Type[] typeTable = constructTypeTable();

	private static Type[] constructTypeTable() {
		Type[] table = new Type[256];
		table[0x00] = Type.NORMAL;
		table[0x01] = Type.FIGHTING;
		table[0x02] = Type.FLYING;
		table[0x03] = Type.POISON;
		table[0x04] = Type.GROUND;
		table[0x05] = Type.ROCK;
		table[0x06] = Type.BUG;
		table[0x07] = Type.GHOST;
		table[0x08] = Type.STEEL;
		table[0x0A] = Type.FIRE;
		table[0x0B] = Type.WATER;
		table[0x0C] = Type.GRASS;
		table[0x0D] = Type.ELECTRIC;
		table[0x0E] = Type.PSYCHIC;
		table[0x0F] = Type.ICE;
		table[0x10] = Type.DRAGON;
		table[0x11] = Type.DARK;
		return table;
	}

	private static byte typeToByte(Type type) {
		if (type == null) {
			return 0x09; // ???-type
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
			return 0x06;
		case GHOST:
			return 0x07;
		case FIRE:
			return 0x0A;
		case WATER:
			return 0x0B;
		case GRASS:
			return 0x0C;
		case ELECTRIC:
			return 0x0D;
		case PSYCHIC:
			return 0x0E;
		case ICE:
			return 0x0F;
		case DRAGON:
			return 0x10;
		case STEEL:
			return 0x08;
		case DARK:
			return 0x11;
		}
		return 0; // normal by default
	}

	public static class RomEntry {
		private String name;
		private String romCode;
		private int romType;
		private Map<String, String> strings = new HashMap<String, String>();
		public Map<String, Integer> numbers = new HashMap<String, Integer>();
		private Map<String, int[]> arrayEntries = new HashMap<String, int[]>();

		private int getInt(String key) {
			if (!numbers.containsKey(key)) {
				numbers.put(key, 0);
			}
			return numbers.get(key);
		}

		private String getString(String key) {
			if (!strings.containsKey(key)) {
				strings.put(key, "");
			}
			return strings.get(key);
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
					FileFunctions.openConfig("gen4_offsets.ini"), "UTF-8");
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
						if (r[0].equals("Game")) {
							current.romCode = r[1];
						} else if (r[0].equals("Type")) {
							if (r[1].equalsIgnoreCase("DP")) {
								current.romType = Type_DP;
							} else if (r[1].equalsIgnoreCase("Plat")) {
								current.romType = Type_Plat;
							} else if (r[1].equalsIgnoreCase("HGSS")) {
								current.romType = Type_HGSS;
							} else {
								System.err.println("unrecognised rom type: "
										+ r[1]);
							}
						} else if (r[0].equals("CopyFrom")) {
							for (RomEntry otherEntry : roms) {
								if (r[1].equalsIgnoreCase(otherEntry.romCode)) {
									// copy from here
									current.arrayEntries
											.putAll(otherEntry.arrayEntries);
									current.numbers.putAll(otherEntry.numbers);
									current.strings.putAll(otherEntry.strings);
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
								current.arrayEntries.put(r[0], offs);
							} else if (r[0].endsWith("Offset")) {
								int offs = parseRIInt(r[1]);
								current.numbers.put(r[0], offs);
							} else {
								current.strings.put(r[0], r[1]);
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

	// This rom
	private Pokemon[] pokes;
	private Move[] moves;
	private NARCContents pokeNarc, moveNarc, msgNarc;
	private byte[] arm9;

	public RomEntry romEntry;

	private static final int Type_DP = 0;
	private static final int Type_Plat = 1;
	private static final int Type_HGSS = 2;

	@Override
	protected boolean detectNDSRom(String ndsCode) {
		for (RomEntry re : roms) {
			if (ndsCode.equals(re.romCode)) {
				this.romEntry = re;
				return true; // match
			}
		}
		return false;
	}

	@Override
	protected void loadedROM() {
		try {
			arm9 = readFile("arm9.bin");
		} catch (IOException e) {
			arm9 = new byte[0];
		}
		try {
			msgNarc = readNARC(romEntry.getString("Text"));
		} catch (IOException e) {
			msgNarc = null;
		}
		loadPokemonStats();
		loadMoves();
	}

	private void loadMoves() {
		try {
			moveNarc = this.readNARC(romEntry.getString("MoveData"));
			moves = new Move[468];
			for (int i = 1; i <= 467; i++) {
				byte[] moveData = moveNarc.files.get(i);
				moves[i] = new Move();
				moves[i].name = RomFunctions.moveNames[i];
				moves[i].number = i;
				moves[i].effectIndex = readWord(moveData, 0);
				moves[i].hitratio = (moveData[5] & 0xFF);
				moves[i].power = moveData[3] & 0xFF;
				moves[i].pp = moveData[6] & 0xFF;
				moves[i].type = typeTable[moveData[4] & 0xFF];
			}
		} catch (IOException e) {
			// change this later
			e.printStackTrace();
		}

	}

	// DEBUG
	public void printPokemonStats() {
		for (int i = 1; i <= 493; i++) {
			System.out.println(pokes[i].toString());
		}
	}

	private void loadPokemonStats() {
		try {
			String pstatsnarc = romEntry.getString("PokemonStats");
			pokeNarc = this.readNARC(pstatsnarc);
			String[] pokeNames = readPokemonNames();
			pokes = new Pokemon[494];
			for (int i = 1; i <= 493; i++) {
				pokes[i] = new Pokemon();
				pokes[i].number = i;
				loadBasicPokeStats(pokes[i], pokeNarc.files.get(i));
				// Name?
				pokes[i].name = pokeNames[i];
			}
		} catch (IOException e) {
			// change this later
			e.printStackTrace();
		}

	}

	private void loadBasicPokeStats(Pokemon pkmn, byte[] stats) {
		pkmn.hp = stats[0] & 0xFF;
		pkmn.attack = stats[1] & 0xFF;
		pkmn.defense = stats[2] & 0xFF;
		pkmn.speed = stats[3] & 0xFF;
		pkmn.spatk = stats[4] & 0xFF;
		pkmn.spdef = stats[5] & 0xFF;
		// Type
		pkmn.primaryType = typeTable[stats[6] & 0xFF];
		pkmn.secondaryType = typeTable[stats[7] & 0xFF];
		// Only one type?
		if (pkmn.secondaryType == pkmn.primaryType) {
			pkmn.secondaryType = null;
		}
		pkmn.catchRate = stats[8] & 0xFF;

		// Abilities
		pkmn.ability1 = stats[22] & 0xFF;
		pkmn.ability2 = stats[23] & 0xFF;
	}

	private String[] readPokemonNames() {
		String[] pokeNames = new String[494];
		List<String> nameList = getStrings(romEntry
				.getInt("PokemonNamesTextOffset"));
		for (int i = 1; i <= 493; i++) {
			pokeNames[i] = nameList.get(i);
		}
		return pokeNames;
	}

	@Override
	protected void savingROM() {
		savePokemonStats();
		saveMoves();
		try {
			writeFile("arm9.bin", arm9);
		} catch (IOException e) {
		}
		try {
			writeNARC(romEntry.getString("Text"), msgNarc);
		} catch (IOException e) {
		}
	}

	private void saveMoves() {
		for (int i = 1; i <= 467; i++) {
			byte[] data = moveNarc.files.get(i);
			writeWord(data, 0, moves[i].effectIndex);
			data[3] = (byte) moves[i].power;
			data[4] = typeToByte(moves[i].type);
			int hitratio = (int) Math.round(moves[i].hitratio);
			if (hitratio < 0) {
				hitratio = 0;
			}
			if (hitratio > 100) {
				hitratio = 100;
			}
			data[5] = (byte) hitratio;
			data[6] = (byte) moves[i].pp;
		}

		try {
			this.writeNARC(romEntry.getString("MoveData"), moveNarc);
		} catch (IOException e) {
			// // change this later
			e.printStackTrace();
		}

	}

	private void savePokemonStats() {
		// Update the "a/an X" list too, if it exists
		List<String> namesList = getStrings(romEntry
				.getInt("PokemonNamesTextOffset"));
		if (romEntry.getString("HasExtraPokemonNames").equalsIgnoreCase("Yes")) {
			List<String> namesList2 = getStrings(romEntry
					.getInt("PokemonNamesTextOffset") + 1);
			for (int i = 1; i <= 493; i++) {
				saveBasicPokeStats(pokes[i], pokeNarc.files.get(i));
				String oldName = namesList.get(i);
				namesList.set(i, pokes[i].name);
				namesList2.set(i,
						namesList2.get(i).replace(oldName, pokes[i].name));
			}
			setStrings(romEntry.getInt("PokemonNamesTextOffset") + 1,
					namesList2, false);
		} else {
			for (int i = 1; i <= 493; i++) {
				saveBasicPokeStats(pokes[i], pokeNarc.files.get(i));
				namesList.set(i, pokes[i].name);
			}
		}
		setStrings(romEntry.getInt("PokemonNamesTextOffset"), namesList, false);

		try {
			String pstatsnarc = romEntry.getString("PokemonStats");
			this.writeNARC(pstatsnarc, pokeNarc);
		} catch (IOException e) {
			// change this later
			e.printStackTrace();
		}

	}

	private void saveBasicPokeStats(Pokemon pkmn, byte[] stats) {
		stats[0] = (byte) pkmn.hp;
		stats[1] = (byte) pkmn.attack;
		stats[2] = (byte) pkmn.defense;
		stats[3] = (byte) pkmn.speed;
		stats[4] = (byte) pkmn.spatk;
		stats[5] = (byte) pkmn.spdef;
		stats[6] = typeToByte(pkmn.primaryType);
		if (pkmn.secondaryType == null) {
			stats[7] = stats[6];
		} else {
			stats[7] = typeToByte(pkmn.secondaryType);
		}
		stats[8] = (byte) pkmn.catchRate;

		stats[22] = (byte) pkmn.ability1;
		stats[23] = (byte) pkmn.ability2;
	}

	@Override
	public boolean isInGame(Pokemon pkmn) {
		return isInGame(pkmn.number);
	}

	@Override
	public boolean isInGame(int pokemonNumber) {
		return pokemonNumber >= 1 && pokemonNumber <= 493;
	}

	@Override
	public List<Pokemon> getPokemon() {
		return Arrays.asList(pokes);
	}

	@Override
	public List<Pokemon> getStarters() {
		if (romEntry.romType == Type_HGSS) {
			List<Integer> tailOffsets = RomFunctions.search(arm9, new byte[] {
					0x03, 0x03, 0x1A, 0x12, 0x1, 0x23, 0x0, 0x0 });
			if (tailOffsets.size() == 1) {
				// Found starters
				int starterOffset = tailOffsets.get(0) - 13;
				int poke1 = readWord(arm9, starterOffset);
				int poke2 = readWord(arm9, starterOffset + 4);
				int poke3 = readWord(arm9, starterOffset + 8);
				return Arrays.asList(pokes[poke1], pokes[poke2], pokes[poke3]);
			} else {
				return Arrays.asList(pokes[152], pokes[155], pokes[158]);
			}
		} else {
			try {
				byte[] starterData = readFile(romEntry
						.getString("StarterPokemon"));
				int poke1 = readWord(starterData,
						romEntry.getInt("StarterPokemonOffset"));
				int poke2 = readWord(starterData,
						romEntry.getInt("StarterPokemonOffset") + 4);
				int poke3 = readWord(starterData,
						romEntry.getInt("StarterPokemonOffset") + 8);
				return Arrays.asList(pokes[poke1], pokes[poke2], pokes[poke3]);
			} catch (IOException e) {
				return Arrays.asList(pokes[387], pokes[390], pokes[393]);
			}
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
		if (romEntry.romType == Type_HGSS) {
			List<Integer> tailOffsets = RomFunctions.search(arm9, new byte[] {
					0x03, 0x03, 0x1A, 0x12, 0x1, 0x23, 0x0, 0x0 });
			if (tailOffsets.size() == 1) {
				// Found starters
				int starterOffset = tailOffsets.get(0) - 13;
				writeWord(arm9, starterOffset, newStarters.get(0).number);
				writeWord(arm9, starterOffset + 4, newStarters.get(1).number);
				writeWord(arm9, starterOffset + 8, newStarters.get(2).number);
				// Go fix the rival scripts, which rely on fixed pokemon numbers
				// The logic to be changed each time is roughly:
				// Set 0x800C = player starter
				// If(0x800C==152) { trainerbattle rival w/ cynda }
				// ElseIf(0x800C==155) { trainerbattle rival w/ totodile }
				// Else { trainerbattle rival w/ chiko }
				// So we basically have to adjust the 152 and the 155.
				int[] filesWithRivalScript = new int[] { 7, 23, 96, 110, 819,
						850, 866 };
				// below code represents a rival script for sure
				// it means: StoreStarter2 0x800C; If 0x800C 152; CheckLR B_!=
				// <offset to follow>
				byte[] magic = new byte[] { (byte) 0xCE, 0x00, 0x0C,
						(byte) 0x80, 0x11, 0x00, 0x0C, (byte) 0x80, (byte) 152,
						0, 0x1C, 0x00, 0x05 };
				try {
					NARCContents scriptNARC = this.readNARC(romEntry
							.getString("Scripts"));
					for (int i = 0; i < filesWithRivalScript.length; i++) {
						int fileCheck = filesWithRivalScript[i];
						byte[] file = scriptNARC.files.get(fileCheck);
						List<Integer> rivalOffsets = RomFunctions.search(file,
								magic);
						if (rivalOffsets.size() == 1) {
							// found, adjust
							int baseOffset = rivalOffsets.get(0);
							// Replace 152 (chiko) with first starter
							writeWord(file, baseOffset + 8,
									newStarters.get(0).number);
							int jumpAmount = readLong(file, baseOffset + 13);
							int secondBase = jumpAmount + baseOffset + 17;
							if (file[secondBase] != 0x11
									|| (file[secondBase + 4] & 0xFF) != 155) {
								// This isn't what we were expecting...
							} else {
								// Replace 155 (cynda) with 2nd starter
								writeWord(file, secondBase + 4,
										newStarters.get(1).number);
							}
						}
					}
					this.writeNARC(romEntry.getString("Scripts"), scriptNARC);
				} catch (IOException e) {
				}
				// Fix starter text
				List<String> spStrings = getStrings(romEntry
						.getInt("StarterScreenTextOffset"));
				String[] intros = new String[] { "So, you like", "You’ll take",
						"Do you want" };
				for (int i = 0; i < 3; i++) {
					Pokemon newStarter = newStarters.get(i);
					int color = (i == 0) ? 3 : i;
					String newStarterDesc = "Professor Elm: " + intros[i]
							+ " \\vFF00\\z000" + color + newStarter.name
							+ "\\vFF00\\z0000,\\nthe "
							+ newStarter.primaryType.camelCase()
							+ "-type Pokémon?";
					spStrings.set(i + 1, newStarterDesc);
					String altStarterDesc = "\\vFF00\\z000" + color
							+ newStarter.name + "\\vFF00\\z0000, the "
							+ newStarter.primaryType.camelCase()
							+ "-type Pokémon, is\\nin this Poké Ball!";
					spStrings.set(i + 4, altStarterDesc);
				}
				setStrings(romEntry.getInt("StarterScreenTextOffset"),
						spStrings);
				return true;
			} else {
				return false;
			}
		} else {
			try {
				byte[] starterData = readFile(romEntry
						.getString("StarterPokemon"));
				writeWord(starterData, romEntry.getInt("StarterPokemonOffset"),
						newStarters.get(0).number);
				writeWord(starterData,
						romEntry.getInt("StarterPokemonOffset") + 4,
						newStarters.get(1).number);
				writeWord(starterData,
						romEntry.getInt("StarterPokemonOffset") + 8,
						newStarters.get(2).number);
				writeFile(romEntry.getString("StarterPokemon"), starterData);
				// Patch DPPt-style rival scripts
				// these have a series of IfJump commands
				// following pokemon IDs
				// the jumps either go to trainer battles, or a HoF times
				// checker, or the StarterBattle command (Pt only)
				// the HoF times checker case is for the Fight Area or Survival
				// Area (depending on version).
				// the StarterBattle case is for Route 201 in Pt.
				int[] filesWithRivalScript = (romEntry.romType == Type_Plat) ? new int[] {
						31, 36, 112, 123, 186, 427, 429, 1096 }
						: new int[] { 34, 90, 118, 180, 195, 394 };
				byte[] magic = new byte[] { (byte) 0xDE, 0x00, 0x0C,
						(byte) 0x80, 0x11, 0x00, 0x0C, (byte) 0x80,
						(byte) 0x83, 0x01, 0x1C, 0x00, 0x01 };
				NARCContents scriptNARC = this.readNARC(romEntry
						.getString("Scripts"));
				for (int i = 0; i < filesWithRivalScript.length; i++) {
					int fileCheck = filesWithRivalScript[i];
					byte[] file = scriptNARC.files.get(fileCheck);
					List<Integer> rivalOffsets = RomFunctions.search(file,
							magic);
					if (rivalOffsets.size() > 0) {
						for (int baseOffset : rivalOffsets) {
							// found, check for trainer battle or HoF
							// check at jump
							int jumpLoc = baseOffset + magic.length;
							int jumpTo = readLong(file, jumpLoc) + jumpLoc + 4;
							if (readWord(file, jumpTo) != 0xE5
									&& readWord(file, jumpTo) != 0x28F
									&& (readWord(file, jumpTo) != 0x125 || romEntry.romType != Type_Plat)) {
								continue; // not a rival script
							}
							// Replace the two starter-words 387 and 390
							writeWord(file, baseOffset + 0x8,
									newStarters.get(0).number);
							writeWord(file, baseOffset + 0x15,
									newStarters.get(1).number);
						}
					}
				}
				// Tag battles with rival or friend
				// Have their own script magic
				// 2 for Lucas/Dawn (=4 occurrences), 1 or 2 for Barry
				byte[] tagBattleMagic = new byte[] { (byte) 0xDE, 0x00, 0x0C,
						(byte) 0x80, 0x28, 0x00, 0x04, (byte) 0x80 };
				byte[] tagBattleMagic2 = new byte[] { 0x11, 0x00, 0x0C,
						(byte) 0x80, (byte) 0x86, 0x01, 0x1C, 0x00, 0x01 };
				int[] filesWithTagBattleScript = (romEntry.romType == Type_Plat) ? new int[] {
						2, 136, 201, 236 }
						: new int[] { 2, 131, 230 };
				for (int i = 0; i < filesWithTagBattleScript.length; i++) {
					int fileCheck = filesWithTagBattleScript[i];
					byte[] file = scriptNARC.files.get(fileCheck);
					List<Integer> tbOffsets = RomFunctions.search(file,
							tagBattleMagic);
					if (tbOffsets.size() > 0) {
						for (int baseOffset : tbOffsets) {
							// found, check for second part
							int secondPartStart = baseOffset
									+ tagBattleMagic.length + 2;
							if (secondPartStart + tagBattleMagic2.length > file.length) {
								continue; // match failed
							}
							boolean valid = true;
							for (int spo = 0; spo < tagBattleMagic2.length; spo++) {
								if (file[secondPartStart + spo] != tagBattleMagic2[spo]) {
									valid = false;
									break;
								}
							}
							if (!valid) {
								continue;
							}
							// Make sure the jump following the second
							// part jumps to a <return> command
							int jumpLoc = secondPartStart
									+ tagBattleMagic2.length;
							int jumpTo = readLong(file, jumpLoc) + jumpLoc + 4;
							if (readWord(file, jumpTo) != 0x1B) {
								continue; // not a tag battle script
							}
							// Replace the two starter-words
							if (readWord(file, baseOffset + 0x21) == 387) {
								// first starter
								writeWord(file, baseOffset + 0x21,
										newStarters.get(0).number);
							} else {
								// third starter
								writeWord(file, baseOffset + 0x21,
										newStarters.get(2).number);
							}
							// second starter
							writeWord(file, baseOffset + 0xE,
									newStarters.get(1).number);
						}
					}
				}
				this.writeNARC(romEntry.getString("Scripts"), scriptNARC);
				// Fix starter script text
				// The starter picking screen
				List<String> spStrings = getStrings(romEntry
						.getInt("StarterScreenTextOffset"));
				// Get pokedex info
				List<String> pokedexSpeciesStrings = getStrings(romEntry
						.getInt("PokedexSpeciesTextOffset"));
				for (int i = 0; i < 3; i++) {
					Pokemon newStarter = newStarters.get(i);
					int color = (i == 0) ? 3 : i;
					String newStarterDesc = "\\vFF00\\z000" + color
							+ pokedexSpeciesStrings.get(newStarter.number)
							+ " " + newStarter.name
							+ "\\vFF00\\z0000!\\nWill you take this Pokémon?";
					spStrings.set(i + 1, newStarterDesc);
				}
				// rewrite starter picking screen
				setStrings(romEntry.getInt("StarterScreenTextOffset"),
						spStrings);
				if (romEntry.romType == Type_DP) {
					// what rival says after we get the Pokemon
					List<String> lakeStrings = getStrings(romEntry
							.getInt("StarterLocationTextOffset"));
					lakeStrings
							.set(19,
									"\\v0103\\z0000: Fwaaah!\\nYour Pokémon totally rocked!\\rBut mine was way tougher\\nthan yours!\\r...They were other people’s\\nPokémon, though...\\rBut we had to use them...\\nThey won’t mind, will they?\\r");
					setStrings(romEntry.getInt("StarterLocationTextOffset"),
							lakeStrings);
				} else {
					// what rival says after we get the Pokemon
					List<String> r201Strings = getStrings(romEntry
							.getInt("StarterLocationTextOffset"));
					r201Strings
							.set(36,
									"\\v0103\\z0000\\z0000: Then, I choose you!\\nI’m picking this one!\\r");
					setStrings(romEntry.getInt("StarterLocationTextOffset"),
							r201Strings);
				}
			} catch (IOException e) {
				return false;
			}
			return true;
		}
	}

	@Override
	public void shufflePokemonStats() {
		for (int i = 1; i <= 493; i++) {
			pokes[i].shuffleStats();
		}
	}

	@Override
	public Pokemon randomPokemon() {
		return pokes[(int) (RandomSource.random() * 493 + 1)];
	}

	@Override
	public void applyMoveUpdates() {
		log("--Move Updates--");
		moves[20].setAccuracy(85); // Bind => 85% accuracy (gen1-4)
		log("Made Bind have 85% accuracy");
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
		moves[80].pp = 10;
		moves[80].power = 120; // Petal Dance => 120power, 10pp (gen1-4)
		log("Made Petal Dance have 10 PP and 120 power");
		moves[83].setAccuracy(85);
		moves[83].power = 35; // Fire Spin => 35 power, 85% acc (gen1-4)
		log("Made Fire Spin have 35 power and 85% accuracy");
		moves[92].setAccuracy(90); // Toxic => 90% accuracy (gen1-4)
		log("Made Toxic have 90% accuracy");
		// move 95, Hypnosis, needs 60% accuracy in DP (its correct here)
		if (romEntry.romType == Type_DP) {
			moves[95].setAccuracy(60);
			log("Made Hypnosis have 60% accuracy");
		}
		moves[128].setAccuracy(85); // Clamp => 85% acc (gen1-4)
		log("Made Clamp have 85% accuracy");
		moves[136].pp = 10;
		moves[136].power = 130; // HJKick => 130 power, 10pp (gen1-4)
		log("Made Hi-Jump Kick have 130 power and 10 PP");
		moves[137].setAccuracy(90); // Glare => 90% acc (gen1-4)
		log("Made Glare have 90% accuracy");
		moves[139].setAccuracy(80); // Poison Gas => 80% acc (gen1-4)
		log("Made Poison Gas have 80% accuracy");
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
		moves[202].power = 75; // Giga Drain => 75 power (gen2-4)
		log("Made Giga Drain have 75 power");
		moves[210].power = 20; // Fury Cutter => 20 power (gen2-4)
		log("Made Fury Cutter have 20 power");
		// Future Sight => 10 pp, 100 power, 100% acc (gen2-4)
		moves[248].pp = 10;
		moves[248].power = 100;
		moves[248].setAccuracy(100);
		log("Made Future Sight have 10 PP, 100 power and 100% accuracy");
		moves[250].power = 35;
		moves[250].setAccuracy(85); // Whirlpool => 35 pow, 85% acc (gen2-4)
		log("Made Whirlpool have 35 power and 85% accuracy");
		// GEN3+ only moves from here
		moves[253].power = 90; // Uproar => 90 power (gen3-4)
		log("Made Uproar have 90 power");
		moves[328].power = 35;
		moves[328].setAccuracy(85); // Sand Tomb => 35 pow, 85% acc (gen3-4)
		log("Made Sand Tomb have 35 power and 85% accuracy");
		moves[331].power = 25; // Bullet Seed => 25 power (gen3-4)
		log("Made Bullet Seed have 25 power");
		moves[333].power = 25; // Icicle Spear => 25 power (gen3-4)
		log("Made Icicle Spear have 25 power");
		moves[343].power = 60; // Covet => 60 power (gen3-4)
		log("Made Covet have 60 power");
		moves[350].setAccuracy(90); // Rock Blast => 90% acc (gen3-4)
		log("Made Rock Blast have 90% accuracy");
		moves[353].power = 140; // Doom Desire => 140 pow, 100% acc
		moves[353].setAccuracy(100); // (gen3-4)
		log("Made Doom Desire have 140 power and 100% accuracy");
		// GEN4+ only moves from here
		moves[364].power = 30; // Feint => 30 pow
		log("Made Feint have 30 power");
		moves[387].power = 140; // Last Resort => 140 pow
		log("Made Last Resort have 140 power");
		moves[409].pp = 10;
		moves[409].power = 75; // Drain Punch => 10 pp, 75 pow
		log("Made Drain Punch have 10 PP and 75 power");
		moves[463].setAccuracy(75); // Magma Storm => 75% acc
		log("Made Magma Storm have 75% accuracy");
		logBlankLine();
	}

	@Override
	public List<Move> getMoves() {
		return Arrays.asList(moves);
	}

	@Override
	public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
		try {
			if (romEntry.romType == Type_HGSS) {
				return getEncountersHGSS(useTimeOfDay);
			} else {
				return getEncountersDPPt();
			}
		} catch (IOException ex) {
			// Uh-oh
			ex.printStackTrace();
			return new ArrayList<EncounterSet>();
		}
	}

	private List<EncounterSet> getEncountersDPPt() throws IOException {
		// Determine file to use
		String encountersFile = romEntry.getString("WildPokemon");

		NARCContents encounterData = readNARC(encountersFile);
		List<EncounterSet> encounters = new ArrayList<EncounterSet>();
		// Credit for
		// https://github.com/magical/pokemon-encounters/blob/master/nds/encounters-gen4-sinnoh.py
		// for the structure for this.
		for (byte[] b : encounterData.files) {
			int grassRate = readLong(b, 0);
			if (grassRate != 0) {
				// up to 4
				List<Encounter> grassEncounters = readEncountersDPPt(b, 4, 12);
				EncounterSet grass = new EncounterSet();
				grass.encounters = grassEncounters;
				grass.rate = grassRate;
				encounters.add(grass);
			}
			// up to 204, 5 special ones to go
			int offset = 204;
			for (int i = 0; i < 5; i++) {
				int rate = readLong(b, offset);
				offset += 4;
				List<Encounter> encountersHere = readSeaEncountersDPPt(b,
						offset, 5);
				offset += 40;
				if (rate == 0 || i == 1) {
					continue;
				}
				EncounterSet other = new EncounterSet();
				other.encounters = encountersHere;
				other.rate = rate;
				encounters.add(other);
			}
		}
		return encounters;
	}

	private List<Encounter> readEncountersDPPt(byte[] data, int offset,
			int amount) {
		List<Encounter> encounters = new ArrayList<Encounter>();
		for (int i = 0; i < amount; i++) {
			int level = readLong(data, offset + i * 8);
			int pokemon = readLong(data, offset + 4 + i * 8);
			Encounter enc = new Encounter();
			enc.level = level;
			enc.pokemon = pokes[pokemon];
			encounters.add(enc);
		}
		return encounters;
	}

	private List<Encounter> readSeaEncountersDPPt(byte[] data, int offset,
			int amount) {
		List<Encounter> encounters = new ArrayList<Encounter>();
		for (int i = 0; i < amount; i++) {
			int level = readLong(data, offset + i * 8);
			int pokemon = readLong(data, offset + 4 + i * 8);
			Encounter enc = new Encounter();
			enc.level = level >> 8;
			enc.maxLevel = level & 0xFF;
			enc.pokemon = pokes[pokemon];
			encounters.add(enc);
		}
		return encounters;
	}

	private List<EncounterSet> getEncountersHGSS(boolean useTimeOfDay)
			throws IOException {
		String encountersFile = romEntry.getString("WildPokemon");
		NARCContents encounterData = readNARC(encountersFile);
		List<EncounterSet> encounters = new ArrayList<EncounterSet>();
		// Credit for
		// https://github.com/magical/pokemon-encounters/blob/master/nds/encounters-gen4-johto.py
		// for the structure for this.
		int[] amounts = new int[] { 0, 5, 2, 5, 5, 5 };
		for (byte[] b : encounterData.files) {
			int[] rates = new int[6];
			rates[0] = b[0] & 0xFF;
			rates[1] = b[1] & 0xFF;
			rates[2] = b[2] & 0xFF;
			rates[3] = b[3] & 0xFF;
			rates[4] = b[4] & 0xFF;
			rates[5] = b[5] & 0xFF;
			// Up to 8 after the rates
			// Grass has to be handled on its own because the levels
			// are reused for every time of day
			int[] grassLevels = new int[12];
			for (int i = 0; i < 12; i++) {
				grassLevels[i] = b[8 + i] & 0xFF;
			}
			// Up to 20 now (12 for levels)
			Pokemon[][] grassPokes = new Pokemon[3][12];
			grassPokes[0] = readPokemonHGSS(b, 20, 12);
			grassPokes[1] = readPokemonHGSS(b, 44, 12);
			grassPokes[2] = readPokemonHGSS(b, 68, 12);
			// Up to 92 now (12*2*3 for pokemon)
			if (rates[0] != 0) {
				if (!useTimeOfDay) {
					// Just write "day" encounters
					List<Encounter> grassEncounters = stitchEncsToLevels(
							grassPokes[1], grassLevels);
					EncounterSet grass = new EncounterSet();
					grass.encounters = grassEncounters;
					grass.rate = rates[0];
					encounters.add(grass);
				} else {
					for (int i = 0; i < 3; i++) {
						EncounterSet grass = new EncounterSet();
						grass.encounters = stitchEncsToLevels(grassPokes[i],
								grassLevels);
						grass.rate = rates[0];
						encounters.add(grass);
					}
				}
			}
			// Up to 100 now... 2*2*2 for radio pokemon
			int offset = 100;
			for (int i = 1; i < 6; i++) {
				List<Encounter> encountersHere = readSeaEncountersHGSS(b,
						offset, amounts[i]);
				offset += 4 * amounts[i];
				if (rates[i] != 0) {
					// Valid area.
					EncounterSet other = new EncounterSet();
					other.encounters = encountersHere;
					other.rate = rates[i];
					encounters.add(other);
				}
			}
		}
		return encounters;
	}

	private Pokemon[] readPokemonHGSS(byte[] data, int offset, int amount) {
		Pokemon[] pokesHere = new Pokemon[amount];
		for (int i = 0; i < amount; i++) {
			pokesHere[i] = pokes[readWord(data, offset + i * 2)];
		}
		return pokesHere;
	}

	private List<Encounter> readSeaEncountersHGSS(byte[] data, int offset,
			int amount) {
		List<Encounter> encounters = new ArrayList<Encounter>();
		for (int i = 0; i < amount; i++) {
			int level = readWord(data, offset + i * 4);
			int pokemon = readWord(data, offset + 2 + i * 4);
			Encounter enc = new Encounter();
			enc.level = level & 0xFF;
			enc.maxLevel = level >> 8;
			enc.pokemon = pokes[pokemon];
			encounters.add(enc);
		}
		return encounters;
	}

	@Override
	public void setEncounters(boolean useTimeOfDay,
			List<EncounterSet> encounters) {
		try {
			if (romEntry.romType == Type_HGSS) {
				setEncountersHGSS(useTimeOfDay, encounters);
			} else {
				setEncountersDPPt(encounters);
			}
		} catch (IOException ex) {
			// Uh-oh
			ex.printStackTrace();
		}
	}

	private void setEncountersDPPt(List<EncounterSet> encounterList)
			throws IOException {
		// Determine file to use
		String encountersFile = romEntry.getString("WildPokemon");
		NARCContents encounterData = readNARC(encountersFile);
		Iterator<EncounterSet> encounters = encounterList.iterator();
		// Credit for
		// https://github.com/magical/pokemon-encounters/blob/master/nds/encounters-gen4-sinnoh.py
		// for the structure for this.
		for (byte[] b : encounterData.files) {
			int grassRate = readLong(b, 0);
			if (grassRate != 0) {
				// grass encounters are a-go
				EncounterSet grass = encounters.next();
				writeEncountersDPPt(b, 4, grass.encounters);
			}
			// up to 204, 5 special ones to go
			// This is for surf, filler, old rod, good rod, super rod
			// so we skip index 1 (filler)
			int offset = 204;
			for (int i = 0; i < 5; i++) {
				int rate = readLong(b, offset);
				offset += 4;
				if (rate == 0 || i == 1) {
					offset += 40;
					continue;
				}

				EncounterSet other = encounters.next();
				writeSeaEncountersDPPt(b, offset, other.encounters);
				offset += 40;
			}
		}

		// Save
		writeNARC(encountersFile, encounterData);

	}

	private void writeEncountersDPPt(byte[] data, int offset,
			List<Encounter> encounters) {
		int enclength = encounters.size();
		for (int i = 0; i < enclength; i++) {
			Encounter enc = encounters.get(i);
			writeLong(data, offset + i * 8, enc.level);
			writeLong(data, offset + i * 8 + 4, enc.pokemon.number);
		}
	}

	private void writeSeaEncountersDPPt(byte[] data, int offset,
			List<Encounter> encounters) {
		int enclength = encounters.size();
		for (int i = 0; i < enclength; i++) {
			Encounter enc = encounters.get(i);
			writeLong(data, offset + i * 8, (enc.level << 8) + enc.maxLevel);
			writeLong(data, offset + i * 8 + 4, enc.pokemon.number);
		}
	}

	private void setEncountersHGSS(boolean useTimeOfDay,
			List<EncounterSet> encounterList) throws IOException {
		String encountersFile = romEntry.getString("WildPokemon");
		NARCContents encounterData = readNARC(encountersFile);
		Iterator<EncounterSet> encounters = encounterList.iterator();
		// Credit for
		// https://github.com/magical/pokemon-encounters/blob/master/nds/encounters-gen4-johto.py
		// for the structure for this.
		int[] amounts = new int[] { 0, 5, 2, 5, 5, 5 };
		for (byte[] b : encounterData.files) {
			int[] rates = new int[6];
			rates[0] = b[0] & 0xFF;
			rates[1] = b[1] & 0xFF;
			rates[2] = b[2] & 0xFF;
			rates[3] = b[3] & 0xFF;
			rates[4] = b[4] & 0xFF;
			rates[5] = b[5] & 0xFF;
			// Up to 20 after the rates & levels
			// Grass has to be handled on its own because the levels
			// are reused for every time of day
			if (rates[0] != 0) {
				if (!useTimeOfDay) {
					// Get a single set of encounters...
					// Write the encounters we get 3x for morning, day, night
					EncounterSet grass = encounters.next();
					writePokemonHGSS(b, 20, grass.encounters);
					writePokemonHGSS(b, 44, grass.encounters);
					writePokemonHGSS(b, 68, grass.encounters);
				} else {
					for (int i = 0; i < 3; i++) {
						EncounterSet grass = encounters.next();
						writePokemonHGSS(b, 20 + i * 24, grass.encounters);
					}
				}
			}
			// Up to 100 now... 2*2*2 for radio pokemon
			// Write rock smash, surf, et al
			int offset = 100;
			for (int i = 1; i < 6; i++) {
				if (rates[i] != 0) {
					// Valid area.
					EncounterSet other = encounters.next();
					writeSeaEncountersHGSS(b, offset, other.encounters);
				}
				offset += 4 * amounts[i];
			}
		}

		// Save
		writeNARC(encountersFile, encounterData);

	}

	private void writePokemonHGSS(byte[] data, int offset,
			List<Encounter> encounters) {
		int enclength = encounters.size();
		for (int i = 0; i < enclength; i++) {
			writeWord(data, offset + i * 2, encounters.get(i).pokemon.number);
		}

	}

	private void writeSeaEncountersHGSS(byte[] data, int offset,
			List<Encounter> encounters) {
		int enclength = encounters.size();
		for (int i = 0; i < enclength; i++) {
			Encounter enc = encounters.get(i);
			data[offset] = (byte) enc.level;
			data[offset + 1] = (byte) enc.maxLevel;
			writeWord(data, offset + 2, enc.pokemon.number);
		}

	}

	private List<Encounter> stitchEncsToLevels(Pokemon[] pokemon, int[] levels) {
		List<Encounter> encounters = new ArrayList<Encounter>();
		for (int i = 0; i < pokemon.length; i++) {
			Encounter enc = new Encounter();
			enc.level = levels[i];
			enc.pokemon = pokemon[i];
			encounters.add(enc);
		}
		return encounters;
	}

	@Override
	public List<Trainer> getTrainers() {
		List<Trainer> allTrainers = new ArrayList<Trainer>();
		try {
			NARCContents trainers = this.readNARC(romEntry
					.getString("TrainerData"));
			NARCContents trpokes = this.readNARC(romEntry
					.getString("TrainerPokemon"));
			printBA(trpokes.files.get(0));
			int trainernum = trainers.files.size();
			for (int i = 1; i < trainernum; i++) {
				byte[] trainer = trainers.files.get(i);
				byte[] trpoke = trpokes.files.get(i);
				Trainer tr = new Trainer();
				tr.poketype = trainer[0] & 0xFF;
				tr.offset = i;
				int numPokes = trainer[3] & 0xFF;
				int pokeOffs = 0;
				// printBA(trpoke);
				for (int poke = 0; poke < numPokes; poke++) {
					int ailevel = readWord(trpoke, pokeOffs);
					int level = trpoke[pokeOffs + 2] & 0xFF;
					int species = (trpoke[pokeOffs + 4] & 0xFF)
							+ ((trpoke[pokeOffs + 5] & 0x01) << 8);
					// int formnum = (trpoke[pokeOffs + 5] >> 2);
					TrainerPokemon tpk = new TrainerPokemon();
					tpk.level = level;
					tpk.pokemon = pokes[species];
					tpk.AILevel = ailevel;
					pokeOffs += 6;
					if (tr.poketype >= 2) {
						int heldItem = readWord(trpoke, pokeOffs);
						tpk.heldItem = heldItem;
						pokeOffs += 2;
					}
					if (tr.poketype % 2 == 1) {
						int attack1 = readWord(trpoke, pokeOffs);
						int attack2 = readWord(trpoke, pokeOffs + 2);
						int attack3 = readWord(trpoke, pokeOffs + 4);
						int attack4 = readWord(trpoke, pokeOffs + 6);
						tpk.move1 = attack1;
						tpk.move2 = attack2;
						tpk.move3 = attack3;
						tpk.move4 = attack4;
						pokeOffs += 8;
					}
					// Plat/HGSS have another random pokeOffs +=2 here.
					if (romEntry.romType != Type_DP) {
						pokeOffs += 2;
					}
					tr.pokemon.add(tpk);
				}
				allTrainers.add(tr);
			}
			if (romEntry.romType == Type_DP) {
				tagTrainersDP(allTrainers);
			} else if (romEntry.romType == Type_Plat) {
				tagTrainersPt(allTrainers);
			} else {
				tagTrainersHGSS(allTrainers);
			}
		} catch (IOException ex) {
			// change this later
			ex.printStackTrace();
		}
		return allTrainers;
	}

	private void tagTrainersDP(List<Trainer> trs) {
		// Gym Trainers
		tag(trs, "GYM1", 0xf4, 0xf5);
		tag(trs, "GYM2", 0x144, 0x103, 0x104, 0x15C);
		tag(trs, "GYM3", 0x135, 0x136, 0x137, 0x138);
		tag(trs, "GYM4", 0x1f1, 0x1f2, 0x191, 0x153, 0x125, 0x1E3);
		tag(trs, "GYM5", 0x165, 0x145, 0x10a, 0x14a, 0x154, 0x157, 0x118, 0x11c);
		tag(trs, "GYM6", 0x13a, 0x100, 0x101, 0x117, 0x16f, 0xe8, 0x11b);
		tag(trs, "GYM7", 0x10c, 0x10d, 0x10e, 0x10f, 0x33b, 0x33c);
		tag(trs, "GYM8", 0x158, 0x155, 0x12d, 0x12e, 0x12f, 0x11d, 0x119);

		// Gym Leaders
		tag(trs, 0xf6, "GYM1");
		tag(trs, 0x13b, "GYM2");
		tag(trs, 0x13d, "GYM3"); // Maylene
		tag(trs, 0x13c, "GYM4"); // Wake
		tag(trs, 0x13e, "GYM5"); // Fantina
		tag(trs, 0xfa, "GYM6"); // Byron
		tag(trs, 0x13f, "GYM7"); // Candice
		tag(trs, 0x140, "GYM8"); // Volkner

		// Elite 4
		tag(trs, 0x105, "ELITE1");
		tag(trs, 0x106, "ELITE2");
		tag(trs, 0x107, "ELITE3");
		tag(trs, 0x108, "ELITE4");
		tag(trs, 0x10b, "CHAMPION");

		// Rival battles (8)
		tagRivalConsecutive(trs, "RIVAL1", 0xf8);
		tagRivalConsecutive(trs, "RIVAL2", 0x1d7);
		tagRivalConsecutive(trs, "RIVAL3", 0x1da);
		tagRivalConsecutive(trs, "RIVAL4", 0x1dd);
		// Tag battle is not following ze usual format
		tag(trs, 0x26b, "RIVAL5-0");
		tag(trs, 0x26c, "RIVAL5-1");
		tag(trs, 0x25f, "RIVAL5-2");
		// Back to normal
		tagRivalConsecutive(trs, "RIVAL6", 0x1e0);
		tagRivalConsecutive(trs, "RIVAL7", 0x346);
		tagRivalConsecutive(trs, "RIVAL8", 0x349);

		// Themed
		tag(trs, "THEMED:CYRUS", 0x193, 0x194);
		tag(trs, "THEMED:MARS", 0x127, 0x195, 0x210);
		tag(trs, "THEMED:JUPITER", 0x196, 0x197);
		tag(trs, "THEMED:SATURN", 0x198, 0x199);

		// Lucas & Dawn tag battles
		tagFriendConsecutive(trs, "FRIEND1", 0x265);
		tagFriendConsecutive(trs, "FRIEND1", 0x268);
		tagFriendConsecutive2(trs, "FRIEND2", 0x26D);
		tagFriendConsecutive2(trs, "FRIEND2", 0x270);

	}

	private void tagTrainersPt(List<Trainer> trs) {
		// Gym Trainers
		tag(trs, "GYM1", 0xf4, 0xf5);
		tag(trs, "GYM2", 0x144, 0x103, 0x104, 0x15C);
		tag(trs, "GYM3", 0x165, 0x145, 0x154, 0x157, 0x118, 0x11c);
		tag(trs, "GYM4", 0x135, 0x136, 0x137, 0x138);
		tag(trs, "GYM5", 0x1f1, 0x1f2, 0x191, 0x153, 0x125, 0x1E3);
		tag(trs, "GYM6", 0x13a, 0x100, 0x101, 0x117, 0x16f, 0xe8, 0x11b);
		tag(trs, "GYM7", 0x10c, 0x10d, 0x10e, 0x10f, 0x33b, 0x33c);
		tag(trs, "GYM8", 0x158, 0x155, 0x12d, 0x12e, 0x12f, 0x11d, 0x119, 0x14b);

		// Gym Leaders
		tag(trs, 0xf6, "GYM1");
		tag(trs, 0x13b, "GYM2");
		tag(trs, 0x13e, "GYM3"); // Fantina
		tag(trs, 0x13d, "GYM4"); // Maylene
		tag(trs, 0x13c, "GYM5"); // Wake
		tag(trs, 0xfa, "GYM6"); // Byron
		tag(trs, 0x13f, "GYM7"); // Candice
		tag(trs, 0x140, "GYM8"); // Volkner

		// Elite 4
		tag(trs, 0x105, "ELITE1");
		tag(trs, 0x106, "ELITE2");
		tag(trs, 0x107, "ELITE3");
		tag(trs, 0x108, "ELITE4");
		tag(trs, 0x10b, "CHAMPION");

		// Rival battles (10)
		tagRivalConsecutive(trs, "RIVAL1", 0x353);
		tagRivalConsecutive(trs, "RIVAL2", 0xf8);
		tagRivalConsecutive(trs, "RIVAL3", 0x1d7);
		tagRivalConsecutive(trs, "RIVAL4", 0x1da);
		tagRivalConsecutive(trs, "RIVAL5", 0x1dd);
		// Tag battle is not following ze usual format
		tag(trs, 0x26b, "RIVAL6-0");
		tag(trs, 0x26c, "RIVAL6-1");
		tag(trs, 0x25f, "RIVAL6-2");
		// Back to normal
		tagRivalConsecutive(trs, "RIVAL7", 0x1e0);
		tagRivalConsecutive(trs, "RIVAL8", 0x346);
		tagRivalConsecutive(trs, "RIVAL9", 0x349);
		tagRivalConsecutive(trs, "RIVAL10", 0x368);

		// Battleground Gym Leaders
		tag(trs, 0x35A, "GYM1");
		tag(trs, 0x359, "GYM2");
		tag(trs, 0x35C, "GYM3");
		tag(trs, 0x356, "GYM4");
		tag(trs, 0x35B, "GYM5");
		tag(trs, 0x358, "GYM6");
		tag(trs, 0x355, "GYM7");
		tag(trs, 0x357, "GYM8");

		// Match vs Volkner and Flint in Battle Frontier
		tag(trs, 0x399, "GYM8");
		tag(trs, 0x39A, "ELITE3");

		// E4 rematch
		tag(trs, 0x362, "ELITE1");
		tag(trs, 0x363, "ELITE2");
		tag(trs, 0x364, "ELITE3");
		tag(trs, 0x365, "ELITE4");
		tag(trs, 0x366, "CHAMPION");

		// Themed
		tag(trs, "THEMED:CYRUS", 0x391, 0x193, 0x194);
		tag(trs, "THEMED:MARS", 0x127, 0x195, 0x210, 0x39e);
		tag(trs, "THEMED:JUPITER", 0x196, 0x197, 0x39f);
		tag(trs, "THEMED:SATURN", 0x198, 0x199);

		// Lucas & Dawn tag battles
		tagFriendConsecutive(trs, "FRIEND1", 0x265);
		tagFriendConsecutive(trs, "FRIEND1", 0x268);
		tagFriendConsecutive2(trs, "FRIEND2", 0x26D);
		tagFriendConsecutive2(trs, "FRIEND2", 0x270);

	}

	private void tagTrainersHGSS(List<Trainer> trs) {
		// Gym Trainers
		tag(trs, "GYM1", 0x32, 0x1D);
		tag(trs, "GYM2", 0x43, 0x44, 0x45, 0x0a);
		tag(trs, "GYM3", 0x05, 0x46, 0x47, 0x16);
		tag(trs, "GYM4", 0x57, 0x58, 0x59, 0x2e);
		tag(trs, "GYM5", 0x9c, 0x9d, 0x9f, 0xfb);
		tag(trs, "GYM7", 0x1e0, 0x1e1, 0x1e2, 0x1e3, 0x1e4);
		tag(trs, "GYM8", 0x6e, 0x6f, 0x70, 0x75, 0x77);

		tag(trs, "GYM9", 0x134, 0x2ad);
		tag(trs, "GYM10", 0x2a4, 0x2a5, 0x2a6, 0x129, 0x12a);
		tag(trs, "GYM11", 0x18c, 0xe8, 0x151);
		tag(trs, "GYM12", 0x150, 0x146, 0x164, 0x15a);
		tag(trs, "GYM13", 0x53, 0x54, 0xb7, 0x88);
		tag(trs, "GYM14", 0x170, 0x171, 0xe6, 0x19f);
		tag(trs, "GYM15", 0x2b1, 0x2b2, 0x2b3, 0x2b4, 0x2b5, 0x2b6);
		tag(trs, "GYM16", 0x2a9, 0x2aa, 0x2ab, 0x2ac);

		// Gym Leaders
		tag(trs, 0x14, "GYM1");
		tag(trs, 0x15, "GYM2");
		tag(trs, 0x1e, "GYM3");
		tag(trs, 0x1f, "GYM4");
		tag(trs, 0x22, "GYM5");
		tag(trs, 0x21, "GYM6");
		tag(trs, 0x20, "GYM7");
		tag(trs, 0x23, "GYM8");

		tag(trs, 0xFD, "GYM9");
		tag(trs, 0xFE, "GYM10");
		tag(trs, 0xFF, "GYM11");
		tag(trs, 0x100, "GYM12");
		tag(trs, 0x101, "GYM13");
		tag(trs, 0x102, "GYM14");
		tag(trs, 0x103, "GYM15");
		tag(trs, 0x105, "GYM16");

		// Elite 4
		tag(trs, 0xf5, "ELITE1");
		tag(trs, 0xf7, "ELITE2");
		tag(trs, 0x1a2, "ELITE3");
		tag(trs, 0xf6, "ELITE4");
		tag(trs, 0xf4, "CHAMPION");

		// Red
		tag(trs, 0x104, "UBER");

		// Gym Rematches
		tag(trs, 0x2c8, "GYM1");
		tag(trs, 0x2c9, "GYM2");
		tag(trs, 0x2ca, "GYM3");
		tag(trs, 0x2cb, "GYM4");
		tag(trs, 0x2ce, "GYM5");
		tag(trs, 0x2cd, "GYM6");
		tag(trs, 0x2cc, "GYM7");
		tag(trs, 0x2cf, "GYM8");

		tag(trs, 0x2d0, "GYM9");
		tag(trs, 0x2d1, "GYM10");
		tag(trs, 0x2d2, "GYM11");
		tag(trs, 0x2d3, "GYM12");
		tag(trs, 0x2d4, "GYM13");
		tag(trs, 0x2d5, "GYM14");
		tag(trs, 0x2d6, "GYM15");
		tag(trs, 0x2d7, "GYM16");

		// Elite 4 Rematch
		tag(trs, 0x2be, "ELITE1");
		tag(trs, 0x2bf, "ELITE2");
		tag(trs, 0x2c0, "ELITE3");
		tag(trs, 0x2c1, "ELITE4");
		tag(trs, 0x2bd, "CHAMPION");

		// Rival Battles
		tagRivalConsecutive(trs, "RIVAL1", 0x1F0);

		tag(trs, 0x10a, "RIVAL2-0");
		tag(trs, 0x10d, "RIVAL2-1");
		tag(trs, 0x1, "RIVAL2-2");

		tag(trs, 0x10B, "RIVAL3-0");
		tag(trs, 0x10E, "RIVAL3-1");
		tag(trs, 0x107, "RIVAL3-2");

		tag(trs, 0x121, "RIVAL4-0");
		tag(trs, 0x10f, "RIVAL4-1");
		tag(trs, 0x120, "RIVAL4-2");

		tag(trs, 0x10C, "RIVAL5-0");
		tag(trs, 0x110, "RIVAL5-1");
		tag(trs, 0x108, "RIVAL5-2");

		tagRivalConsecutive(trs, "RIVAL6", 0x11e);
		tagRivalConsecutive(trs, "RIVAL7", 0x2e0); // dragons den tag battle
		tagRivalConsecutive(trs, "RIVAL8", 0x1EA);

		// Clair & Lance match in Dragons Den
		tag(trs, 0x2DE, "GYM8");
		tag(trs, 0x2DD, "CHAMPION");

		// Themed
		tag(trs, "THEMED:ARIANA", 0x1df, 0x1de);
		tag(trs, "THEMED:PETREL", 0x1e8, 0x1e7);
		tag(trs, "THEMED:PROTON", 0x1e6, 0x2c2);
		tag(trs, "THEMED:SPROUTTOWER", 0x2b, 0x33, 0x34, 0x35, 0x36, 0x37,
				0x122);

	}

	private void tag(List<Trainer> allTrainers, int number, String tag) {
		allTrainers.get(number - 1).tag = tag;
	}

	private void tag(List<Trainer> allTrainers, String tag, int... numbers) {
		for (int num : numbers) {
			allTrainers.get(num - 1).tag = tag;
		}
	}

	private void tagRivalConsecutive(List<Trainer> allTrainers, String tag,
			int offsetFire) {
		allTrainers.get(offsetFire - 1).tag = tag + "-0";
		allTrainers.get(offsetFire).tag = tag + "-1";
		allTrainers.get(offsetFire - 2).tag = tag + "-2";

	}

	private void tagFriendConsecutive(List<Trainer> allTrainers, String tag,
			int offsetGrass) {
		allTrainers.get(offsetGrass - 1).tag = tag + "-1";
		allTrainers.get(offsetGrass).tag = tag + "-2";
		allTrainers.get(offsetGrass + 1).tag = tag + "-0";

	}

	private void tagFriendConsecutive2(List<Trainer> allTrainers, String tag,
			int offsetWater) {
		allTrainers.get(offsetWater - 1).tag = tag + "-0";
		allTrainers.get(offsetWater).tag = tag + "-1";
		allTrainers.get(offsetWater + 1).tag = tag + "-2";

	}

	@Override
	public void setTrainers(List<Trainer> trainerData) {
		Iterator<Trainer> allTrainers = trainerData.iterator();
		try {
			NARCContents trainers = this.readNARC(romEntry
					.getString("TrainerData"));
			NARCContents trpokes = new NARCContents();
			// empty entry
			trpokes.files.add(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
			int trainernum = trainers.files.size();
			for (int i = 1; i < trainernum; i++) {
				byte[] trainer = trainers.files.get(i);
				Trainer tr = allTrainers.next();
				tr.poketype = 0; // write as type 0 for no item/moves
				trainer[0] = (byte) tr.poketype;
				int numPokes = tr.pokemon.size();
				trainer[3] = (byte) numPokes;

				int bytesNeeded = 6 * numPokes;
				if (romEntry.romType != Type_DP) {
					bytesNeeded += 2 * numPokes;
				}
				if (tr.poketype % 2 == 1) {
					bytesNeeded += 8 * numPokes;
				}
				if (tr.poketype >= 2) {
					bytesNeeded += 2 * numPokes;
				}
				byte[] trpoke = new byte[bytesNeeded];
				int pokeOffs = 0;
				Iterator<TrainerPokemon> tpokes = tr.pokemon.iterator();
				for (int poke = 0; poke < numPokes; poke++) {
					TrainerPokemon tpk = tpokes.next();
					writeWord(trpoke, pokeOffs, tpk.AILevel);
					writeWord(trpoke, pokeOffs + 2, tpk.level);
					writeWord(trpoke, pokeOffs + 4, tpk.pokemon.number);
					pokeOffs += 6;
					if (tr.poketype >= 2) {
						writeWord(trpoke, pokeOffs, tpk.heldItem);
						pokeOffs += 2;
					}
					if (tr.poketype % 2 == 1) {
						writeWord(trpoke, pokeOffs, tpk.move1);
						writeWord(trpoke, pokeOffs + 2, tpk.move2);
						writeWord(trpoke, pokeOffs + 4, tpk.move3);
						writeWord(trpoke, pokeOffs + 6, tpk.move4);
						pokeOffs += 8;
					}
					// Plat/HGSS have another random pokeOffs +=2 here.
					if (romEntry.romType != Type_DP) {
						pokeOffs += 2;
					}
				}
				trpokes.files.add(trpoke);
			}
			this.writeNARC(romEntry.getString("TrainerData"), trainers);
			this.writeNARC(romEntry.getString("TrainerPokemon"), trpokes);
		} catch (IOException ex) {
			// change this later
			ex.printStackTrace();
		}

	}

	@Override
	public Map<Pokemon, List<MoveLearnt>> getMovesLearnt() {
		Map<Pokemon, List<MoveLearnt>> movesets = new TreeMap<Pokemon, List<MoveLearnt>>();
		try {
			NARCContents movesLearnt = this.readNARC(romEntry
					.getString("PokemonMovesets"));
			for (int i = 1; i <= 493; i++) {
				Pokemon pkmn = pokes[i];
				byte[] rom = movesLearnt.files.get(i);
				int moveDataLoc = 0;
				List<MoveLearnt> learnt = new ArrayList<MoveLearnt>();
				while ((rom[moveDataLoc] & 0xFF) != 0xFF
						|| (rom[moveDataLoc + 1] & 0xFF) != 0xFF) {
					int move = (rom[moveDataLoc] & 0xFF);
					int level = (rom[moveDataLoc + 1] & 0xFE) >> 1;
					if ((rom[moveDataLoc + 1] & 0x01) == 0x01) {
						move += 256;
					}
					MoveLearnt ml = new MoveLearnt();
					ml.level = level;
					ml.move = move;
					learnt.add(ml);
					moveDataLoc += 2;
				}
				movesets.put(pkmn, learnt);
			}
		} catch (IOException e) {
			// change this later
			e.printStackTrace();
		}
		return movesets;
	}

	@Override
	public void setMovesLearnt(Map<Pokemon, List<MoveLearnt>> movesets) {
		int[] extraLearnSets = new int[] { 7, 13, 13 };
		// Build up a new NARC
		NARCContents movesLearnt = new NARCContents();
		// The blank moveset
		byte[] blankSet = new byte[] { (byte) 0xFF, (byte) 0xFF, 0, 0 };
		movesLearnt.files.add(blankSet);
		for (int i = 1; i <= 493; i++) {
			Pokemon pkmn = pokes[i];
			List<MoveLearnt> learnt = movesets.get(pkmn);
			int sizeNeeded = learnt.size() * 2 + 2;
			if ((sizeNeeded % 4) != 0) {
				sizeNeeded += 2;
			}
			byte[] moveset = new byte[sizeNeeded];
			int j = 0;
			for (; j < learnt.size(); j++) {
				MoveLearnt ml = learnt.get(j);
				moveset[j * 2] = (byte) (ml.move & 0xFF);
				int levelPart = (ml.level << 1) & 0xFE;
				if (ml.move > 255) {
					levelPart++;
				}
				moveset[j * 2 + 1] = (byte) levelPart;
			}
			moveset[j * 2] = (byte) 0xFF;
			moveset[j * 2 + 1] = (byte) 0xFF;
			movesLearnt.files.add(moveset);
		}
		for (int j = 0; j < extraLearnSets[romEntry.romType]; j++) {
			movesLearnt.files.add(blankSet);
		}
		// Save
		try {
			this.writeNARC(romEntry.getString("PokemonMovesets"), movesLearnt);
		} catch (IOException e) {
			// change this later
			e.printStackTrace();
		}

	}

	@Override
	public List<Pokemon> getStaticPokemon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setStaticPokemon(List<Pokemon> staticPokemon) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> getTMMoves() {
		String tmDataPrefix;
		if (romEntry.romType == Type_DP || romEntry.romType == Type_Plat) {
			tmDataPrefix = "D300D400";
		} else {
			tmDataPrefix = "1E003200";
		}
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			List<Integer> tms = new ArrayList<Integer>();
			for (int i = 0; i < 92; i++) {
				tms.add(readWord(arm9, offset + i * 2));
			}
			return tms;
		} else {
			return null;
		}
	}

	@Override
	public List<Integer> getHMMoves() {
		String tmDataPrefix;
		if (romEntry.romType == Type_DP || romEntry.romType == Type_Plat) {
			tmDataPrefix = "D300D400";
		} else {
			tmDataPrefix = "1E003200";
		}
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			offset += 184; // TM data
			List<Integer> hms = new ArrayList<Integer>();
			for (int i = 0; i < 8; i++) {
				hms.add(readWord(arm9, offset + i * 2));
			}
			return hms;
		} else {
			return null;
		}
	}

	@Override
	public void setTMMoves(List<Integer> moveIndexes) {
		String tmDataPrefix;
		if (romEntry.romType == Type_DP || romEntry.romType == Type_Plat) {
			tmDataPrefix = "D300D400";
		} else {
			tmDataPrefix = "1E003200";
		}
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			for (int i = 0; i < 92; i++) {
				writeWord(arm9, offset + i * 2, moveIndexes.get(i));
			}

			// Update TM item descriptions
			List<String> itemDescriptions = getStrings(romEntry
					.getInt("ItemDescriptionsTextOffset"));
			List<String> moveDescriptions = getStrings(romEntry
					.getInt("MoveDescriptionsTextOffset"));
			// TM01 is item 328 and so on
			for (int i = 0; i < 92; i++) {
				// Rewrite 5-line move descs into 3-line item descs
				itemDescriptions.set(i + 328, RomFunctions
						.rewriteDescriptionForNewLineSize(
								moveDescriptions.get(moveIndexes.get(i)),
								"\\n", 40, ssd));
			}
			// Save the new item descriptions
			setStrings(romEntry.getInt("ItemDescriptionsTextOffset"),
					itemDescriptions);
			// Palettes update
			String baseOfPalettes = "8D018E01210133018D018F0122013401";
			if (romEntry.romType == Type_DP) {
				baseOfPalettes = "8D018E01210132018D018F0122013301";
			}
			int offsPals = find(arm9, baseOfPalettes);
			if (offsPals > 0) {
				// Write pals
				for (int i = 0; i < 92; i++) {
					Move m = this.moves[moveIndexes.get(i)];
					int pal = this.typeTMPaletteNumber(m.type);
					writeWord(arm9, offsPals + i * 8 + 2, pal);
				}
			}
			// if we can't update the palettes its not a big deal...
		} else {
		}
	}

	private static RomFunctions.StringSizeDeterminer ssd = new RomFunctions.StringLengthSD();

	@Override
	public int getTMCount() {
		return 92;
	}

	@Override
	public int getHMCount() {
		return 8;
	}

	@Override
	public Map<Pokemon, boolean[]> getTMHMCompatibility() {
		Map<Pokemon, boolean[]> compat = new TreeMap<Pokemon, boolean[]>();
		for (int i = 1; i <= 493; i++) {
			byte[] data = pokeNarc.files.get(i);
			Pokemon pkmn = pokes[i];
			boolean[] flags = new boolean[101];
			for (int j = 0; j < 13; j++) {
				readByteIntoFlags(data, flags, j * 8 + 1, 0x1C + j);
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
			byte[] data = pokeNarc.files.get(pkmn.number);
			for (int j = 0; j < 13; j++) {
				data[0x1C + j] = getByteFromFlags(flags, j * 8 + 1);
			}
		}
	}

	private int find(byte[] data, String hexString) {
		if (hexString.length() % 2 != 0) {
			return -3; // error
		}
		byte[] searchFor = new byte[hexString.length() / 2];
		for (int i = 0; i < searchFor.length; i++) {
			searchFor[i] = (byte) Integer.parseInt(
					hexString.substring(i * 2, i * 2 + 2), 16);
		}
		List<Integer> found = RomFunctions.search(data, searchFor);
		if (found.size() == 0) {
			return -1; // not found
		} else if (found.size() > 1) {
			return -2; // not unique
		} else {
			return found.get(0);
		}
	}

	private static void printBA(byte[] array) {
		System.out.print("[");
		boolean first = true;
		for (byte b : array) {
			if (!first) {
				System.out.print(" ");
			}
			System.out.printf("%02X", b);
			first = false;
		}
		System.out.println("]");
	}

	private boolean lastStringsCompressed = false;

	public List<String> getStrings(int index) {
		PokeTextData pt = new PokeTextData(msgNarc.files.get(index));
		pt.decrypt();
		lastStringsCompressed = pt.compressFlag;
		return new ArrayList<String>(pt.strlist);
	}

	private void setStrings(int index, List<String> newStrings) {
		setStrings(index, newStrings, false);
	}

	private void setStrings(int index, List<String> newStrings,
			boolean compressed) {
		byte[] rawUnencrypted = TextToPoke.MakeFile(newStrings, compressed);

		// make new encrypted name set
		PokeTextData encrypt = new PokeTextData(rawUnencrypted);
		encrypt.SetKey(0xD00E);
		encrypt.encrypt();

		// rewrite
		msgNarc.files.set(index, encrypt.get());
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
		return "No Static Pokemon";
	}

	@Override
	public boolean hasTimeBasedEncounters() {
		// dppt technically do but we ignore them completely
		return romEntry.romType == Type_HGSS;
	}

	@Override
	public boolean canChangeStarters() {
		return true;
	}

	@Override
	public void removeTradeEvolutions() {
		// Read NARC
		try {
			NARCContents evoNARC = readNARC(romEntry
					.getString("PokemonEvolutions"));
			log("--Removing Trade Evolutions--");
			for (int i = 1; i <= 493; i++) {
				byte[] evoEntry = evoNARC.files.get(i);
				for (int evo = 0; evo < 7; evo++) {
					int evoType = readWord(evoEntry, evo * 6);
					int species = readWord(evoEntry, evo * 6 + 4);
					if (evoType == 5) {
						// Replace w/ level 37
						log("Made " + pokes[i].name + " evolve into "
								+ pokes[species].name + " at level 37");
						writeWord(evoEntry, evo * 6, 1);
						writeWord(evoEntry, evo * 6 + 2, 37);
					} else if (evoType == 6) {
						// Get the current item & evolution
						int item = readWord(evoEntry, evo * 6 + 2);
						if (i == 79) {
							// Slowpoke is awkward - he already has a level evo
							// So we can't do Level up w/ Held Item for him
							// Put Water Stone instead
							log("Made " + pokes[i].name + " evolve into "
									+ pokes[species].name
									+ " using a Water Stone");
							writeWord(evoEntry, evo * 6, 7);
							writeWord(evoEntry, evo * 6 + 2, 84);
						} else {
							log("Made "
									+ pokes[i].name
									+ " evolve into "
									+ pokes[species].name
									+ " by leveling up holding the item it had to be traded with before");
							// Replace, for this entry, w/
							// Level up w/ Held Item at Day
							writeWord(evoEntry, evo * 6, 18);
							// Now look for a free slot to put
							// Level up w/ Held Item at Night
							for (int evo2 = evo + 1; evo2 < 7; evo2++) {
								if (readWord(evoEntry, evo2 * 6) == 0) {
									// Bingo, blank entry
									writeWord(evoEntry, evo2 * 6, 19);
									writeWord(evoEntry, evo2 * 6 + 2, item);
									writeWord(evoEntry, evo2 * 6 + 4, species);
									break;
								}
							}
						}
					}
				}
			}
			writeNARC(romEntry.getString("PokemonEvolutions"), evoNARC);
			logBlankLine();
		} catch (IOException e) {
			// can't do anything
		}

	}

	@Override
	public List<String> getTrainerNames() {
		List<String> tnames = new ArrayList<String>(
				getStrings(romEntry.getInt("TrainerNamesTextOffset")));
		tnames.remove(0); // blank one
		for (int i = 0; i < tnames.size(); i++) {
			if (tnames.get(i).contains("\\and")) {
				tnames.set(i, tnames.get(i).replace("\\and", "&"));
			}
		}
		return tnames;
	}

	@Override
	public int maxTrainerNameLength() {
		return 10;// based off the english ROMs fixed
	}

	@Override
	public void setTrainerNames(List<String> trainerNames) {
		List<String> oldTNames = getStrings(romEntry
				.getInt("TrainerNamesTextOffset"));
		List<String> newTNames = new ArrayList<String>(trainerNames);
		for (int i = 0; i < newTNames.size(); i++) {
			if (newTNames.get(i).contains("&")) {
				newTNames.set(i, newTNames.get(i).replace("&", "\\and"));
			}
		}
		newTNames.add(0, oldTNames.get(0)); // the 0-entry, preserve it

		// rewrite, only compressed if they were compressed before
		setStrings(romEntry.getInt("TrainerNamesTextOffset"), newTNames,
				lastStringsCompressed);

	}

	@Override
	public boolean fixedTrainerNamesLength() {
		return false;
	}

	@Override
	public List<String> getTrainerClassNames() {
		return getStrings(romEntry.getInt("TrainerClassesTextOffset"));
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		setStrings(romEntry.getInt("TrainerClassesTextOffset"),
				trainerClassNames);
	}

	@Override
	public int maxTrainerClassNameLength() {
		return 12;// based off the english ROMs
	}

	@Override
	public boolean fixedTrainerClassNamesLength() {
		return false;
	}

	@Override
	public String getDefaultExtension() {
		return "nds";
	}

	@Override
	public int abilitiesPerPokemon() {
		return 2;
	}

	@Override
	public int highestAbilityIndex() {
		return 123;
	}

}
