package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  Gen5RomHandler.java - randomizer handler for B/W/B2/W2.				--*/
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pptxt.PPTxtHandler;

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

import dsdecmp.HexInputStream;
import dsdecmp.JavaDSDecmp;

public class Gen5RomHandler extends AbstractDSRomHandler {

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
		table[0x09] = Type.FIRE;
		table[0x0A] = Type.WATER;
		table[0x0B] = Type.GRASS;
		table[0x0C] = Type.ELECTRIC;
		table[0x0D] = Type.PSYCHIC;
		table[0x0E] = Type.ICE;
		table[0x0F] = Type.DRAGON;
		table[0x10] = Type.DARK;
		return table;
	}

	private static byte typeToByte(Type type) {
		if (type == null) {
			return 0x00; // normal?
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
			return 0x09;
		case WATER:
			return 0x0A;
		case GRASS:
			return 0x0B;
		case ELECTRIC:
			return 0x0C;
		case PSYCHIC:
			return 0x0D;
		case ICE:
			return 0x0E;
		case DRAGON:
			return 0x0F;
		case STEEL:
			return 0x08;
		case DARK:
			return 0x10;
		}
		return 0; // normal by default
	}

	private static final String[] pokeStatsNARC = new String[] { "a/0/1/6",
			"a/0/1/6" };
	private static final String[] movesNARC = new String[] { "a/0/2/1",
			"a/0/2/1" };
	private static final String[] encountersNARC = new String[] { "a/1/2/6",
			"a/1/2/7" };
	private static final String[] movesetsNARC = new String[] { "a/0/1/8",
			"a/0/1/8" };
	private static final String[] trainersNARC = new String[] { "a/0/9/2",
			"a/0/9/1" };
	private static final String[] trpokesNARC = new String[] { "a/0/9/3",
			"a/0/9/2" };
	private static String[] evolutionsNARC = new String[] { "a/0/1/9",
			"a/0/1/9" };

	// Strings
	private static final int[] trainerNamesFile = new int[] { 190, 382 };
	private static final int[] trainerClassesFile = new int[] { 191, 383 };
	private static final int[] mugshotsFile = new int[] { 176, 368 };
	private static final int[] moveDescriptionsFile = new int[] { 202, 402 };
	private static final int[] itemDescriptionsFile = new int[] { 53, 63 };
	private static int[] pokeNamesFile = new int[] { 70, 90 };

	// This ROM
	private Pokemon[] pokes;
	private Move[] moves;
	private int type;
	private String ndsCode;
	private byte[] arm9;

	private static final int Type_BW = 0;
	private static final int Type_BW2 = 1;

	private NARCContents pokeNarc, moveNarc, stringsNarc, storyTextNarc;

	@Override
	protected boolean detectNDSRom(String ndsCode) {
		this.ndsCode = ndsCode;
		List<Character> validBWgames = Arrays.asList('A', 'B', 'D', 'E');
		return ndsCode.charAt(0) == 'I' && ndsCode.charAt(1) == 'R'
				&& validBWgames.contains(ndsCode.charAt(2));
	}

	@Override
	protected void loadedROM() {
		if (ndsCode.charAt(2) == 'A' || ndsCode.charAt(2) == 'B') {
			type = Type_BW;
		} else {
			type = Type_BW2;
		}
		try {
			arm9 = readFile("arm9.bin");
		} catch (IOException e) {
			arm9 = new byte[0];
		}
		try {
			stringsNarc = readNARC("a/0/0/2");
			storyTextNarc = readNARC("a/0/0/3");
		} catch (IOException e) {
			stringsNarc = null;
			storyTextNarc = null;
		}
		loadPokemonStats();
		loadMoves();
	}

	// DEBUG
	public void printPokemonStats() {
		for (int i = 1; i <= 649; i++) {
			System.out.println(pokes[i].toString());
		}
	}

	private void loadPokemonStats() {
		try {
			pokeNarc = this.readNARC(pokeStatsNARC[type]);
			String[] pokeNames = readPokemonNames();
			pokes = new Pokemon[650];
			for (int i = 1; i <= 649; i++) {
				pokes[i] = new Pokemon();
				pokes[i].number = i;
				loadBasicPokeStats(pokes[i], pokeNarc.files.get(i));
				// Name?
				pokes[i].name = pokeNames[i];
			}
		} catch (IOException e) {
			// uh-oh?
			e.printStackTrace();
		}

	}

	private void loadMoves() {
		try {
			moveNarc = this.readNARC(movesNARC[type]);
			moves = new Move[560];
			for (int i = 1; i <= 559; i++) {
				byte[] moveData = moveNarc.files.get(i);
				moves[i] = new Move();
				moves[i].name = RomFunctions.moveNames[i];
				moves[i].number = i;
				moves[i].hitratio = (moveData[4] & 0xFF);
				moves[i].power = moveData[3] & 0xFF;
				moves[i].pp = moveData[5] & 0xFF;
				moves[i].type = typeTable[moveData[0] & 0xFF];
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
		// Abilities for debugging later
		pkmn.ability1 = stats[24] & 0xFF;
		pkmn.ability2 = stats[25] & 0xFF;
		pkmn.ability3 = stats[26] & 0xFF;
	}

	private String[] readPokemonNames() {
		String[] pokeNames = new String[650];
		List<String> nameList = getStrings(false, pokeNamesFile[type]);
		for (int i = 1; i <= 649; i++) {
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
			writeNARC("a/0/0/2", stringsNarc);
			writeNARC("a/0/0/3", storyTextNarc);
		} catch (IOException e) {
		}
	}

	private void saveMoves() {
		for (int i = 1; i <= 559; i++) {
			byte[] data = moveNarc.files.get(i);
			data[3] = (byte) moves[i].power;
			data[0] = typeToByte(moves[i].type);
			int hitratio = (int) Math.round(moves[i].hitratio);
			if (hitratio < 0) {
				hitratio = 0;
			}
			if (hitratio > 100) {
				hitratio = 100;
			}
			data[4] = (byte) hitratio;
			data[5] = (byte) moves[i].pp;
		}

		try {
			this.writeNARC(movesNARC[type], moveNarc);
		} catch (IOException e) {
			// // change this later
			e.printStackTrace();
		}

	}

	private void savePokemonStats() {
		List<String> nameList = getStrings(false, pokeNamesFile[type]);
		for (int i = 1; i <= 649; i++) {
			saveBasicPokeStats(pokes[i], pokeNarc.files.get(i));
			nameList.set(i, pokes[i].name);
		}
		setStrings(false, pokeNamesFile[type], nameList);
		try {
			this.writeNARC(pokeStatsNARC[type], pokeNarc);
		} catch (IOException e) {
			// uh-oh?
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

		stats[24] = (byte) pkmn.ability1;
		stats[25] = (byte) pkmn.ability2;
		stats[26] = (byte) pkmn.ability3;
	}

	@Override
	public boolean isInGame(Pokemon pkmn) {
		return isInGame(pkmn.number);
	}

	@Override
	public boolean isInGame(int pokemonNumber) {
		return pokemonNumber >= 1 && pokemonNumber <= 649;
	}

	@Override
	public List<Pokemon> getPokemon() {
		return Arrays.asList(pokes);
	}

	@Override
	public List<Pokemon> getStarters() {
		if (type == Type_BW) {
			try {
				NARCContents scriptNARC = this.readNARC("a/0/5/7");
				byte[] starterScripts = scriptNARC.files.get(782);
				return Arrays.asList(pokes[readWord(starterScripts, 639)],
						pokes[readWord(starterScripts, 687)],
						pokes[readWord(starterScripts, 716)]);
			} catch (IOException e) {
				return Arrays.asList(pokes[495], pokes[498], pokes[501]);
			}

		} else {
			try {
				NARCContents scriptNARC = this.readNARC("a/0/5/6");
				byte[] starterScripts = scriptNARC.files.get(854);
				return Arrays.asList(pokes[readWord(starterScripts, 1419)],
						pokes[readWord(starterScripts, 1472)],
						pokes[readWord(starterScripts, 1506)]);
			} catch (IOException e) {
				return Arrays.asList(pokes[495], pokes[498], pokes[501]);
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
		if (type == Type_BW) {
			// Try change
			try {
				NARCContents scriptNARC = this.readNARC("a/0/5/7");
				byte[] starterScripts = scriptNARC.files.get(782);

				writeWord(starterScripts, 639, newStarters.get(0).number);
				writeWord(starterScripts, 644, newStarters.get(0).number);

				writeWord(starterScripts, 687, newStarters.get(1).number);
				writeWord(starterScripts, 692, newStarters.get(1).number);

				writeWord(starterScripts, 716, newStarters.get(2).number);
				writeWord(starterScripts, 721, newStarters.get(2).number);

				this.writeNARC("a/0/5/7", scriptNARC);

				// Try replace sprites
				NARCContents starterNARC = this.readNARC("a/2/0/5");
				NARCContents pokespritesNARC = this.readNARC("a/0/0/4");
				replaceStarterFiles(starterNARC, pokespritesNARC, 0,
						newStarters.get(0).number);
				replaceStarterFiles(starterNARC, pokespritesNARC, 1,
						newStarters.get(1).number);
				replaceStarterFiles(starterNARC, pokespritesNARC, 2,
						newStarters.get(2).number);
				writeNARC("a/2/0/5", starterNARC);

				// Text
				List<String> yourHouseStrings = getStrings(true, 430);
				for (int i = 0; i < 3; i++) {
					yourHouseStrings.set(18 - i, "\\xF000\\xBD02\\x0000The "
							+ newStarters.get(i).primaryType.camelCase()
							+ "-type Pok\\x00E9mon\\xFFFE\\xF000\\xBD02\\x0000"
							+ newStarters.get(i).name);
				}
				// Update what the friends say
				yourHouseStrings
						.set(26,
								"Cheren: Hey, how come you get to pick\\xFFFEout my Pok\\x00E9mon?"
										+ "\\xF000\\xBE01\\x0000\\xFFFEOh, never mind. I wanted this one\\xFFFEfrom the start, anyway."
										+ "\\xF000\\xBE01\\x0000");
				yourHouseStrings
						.set(53,
								"It's decided. You'll be my opponent...\\xFFFEin our first Pok\\x00E9mon battle!"
										+ "\\xF000\\xBE01\\x0000\\xFFFELet's see what you can do, \\xFFFEmy Pok\\x00E9mon!"
										+ "\\xF000\\xBE01\\x0000");

				// rewrite
				setStrings(true, 430, yourHouseStrings);
			} catch (IOException e) {
				return false;
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		} else {
			// Try change
			try {
				NARCContents scriptNARC = this.readNARC("a/0/5/6");
				byte[] starterScripts = scriptNARC.files.get(854);

				writeWord(starterScripts, 0x58B, newStarters.get(0).number);
				writeWord(starterScripts, 0x590, newStarters.get(0).number);
				writeWord(starterScripts, 0x595, newStarters.get(0).number);

				writeWord(starterScripts, 0x5C0, newStarters.get(1).number);
				writeWord(starterScripts, 0x5C5, newStarters.get(1).number);
				writeWord(starterScripts, 0x5CA, newStarters.get(1).number);

				writeWord(starterScripts, 0x5E2, newStarters.get(2).number);
				writeWord(starterScripts, 0x5E7, newStarters.get(2).number);
				writeWord(starterScripts, 0x5EC, newStarters.get(2).number);

				this.writeNARC("a/0/5/6", scriptNARC);

				// Try replace sprites
				NARCContents starterNARC = this.readNARC("a/2/0/2");
				NARCContents pokespritesNARC = this.readNARC("a/0/0/4");
				replaceStarterFiles(starterNARC, pokespritesNARC, 0,
						newStarters.get(0).number);
				replaceStarterFiles(starterNARC, pokespritesNARC, 1,
						newStarters.get(1).number);
				replaceStarterFiles(starterNARC, pokespritesNARC, 2,
						newStarters.get(2).number);
				writeNARC("a/2/0/2", starterNARC);

				// Text
				List<String> starterTownStrings = getStrings(true, 169);
				for (int i = 0; i < 3; i++) {
					starterTownStrings.set(37 - i, "\\xF000\\xBD02\\x0000The "
							+ newStarters.get(i).primaryType.camelCase()
							+ "-type Pok\\x00E9mon\\xFFFE\\xF000\\xBD02\\x0000"
							+ newStarters.get(i).name);
				}
				// Update what the rival says
				starterTownStrings
						.set(60,
								"\\xF000\\x0100\\x0001\\x0001: Let's see how good\\xFFFEa Trainer you are!"
										+ "\\xF000\\xBE01\\x0000\\xFFFEI'll use my Pok\\x00E9mon"
										+ "\\xFFFEthat I raised from an Egg!\\xF000\\xBE01\\x0000");

				// rewrite
				setStrings(true, 169, starterTownStrings);
			} catch (IOException e) {
				return false;
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}
	}

	private void replaceStarterFiles(NARCContents starterNARC,
			NARCContents pokespritesNARC, int starterIndex, int pokeNumber)
			throws IOException, InterruptedException {
		starterNARC.files.set(starterIndex * 2,
				pokespritesNARC.files.get(pokeNumber * 20 + 18));
		// Get the picture...
		byte[] compressedPic = pokespritesNARC.files.get(pokeNumber * 20);
		// Decompress it with JavaDSDecmp
		int[] ucp = JavaDSDecmp.Decompress(new HexInputStream(
				new ByteArrayInputStream(compressedPic)));
		byte[] uncompressedPic = convIntArrToByteArr(ucp);
		starterNARC.files.set(12 + starterIndex, uncompressedPic);
	}

	private byte[] convIntArrToByteArr(int[] arg) {
		byte[] out = new byte[arg.length];
		for (int i = 0; i < arg.length; i++) {
			out[i] = (byte) arg[i];
		}
		return out;
	}

	@Override
	public void shufflePokemonStats() {
		for (int i = 1; i <= 649; i++) {
			pokes[i].shuffleStats();
		}

	}

	@Override
	public Pokemon randomPokemon() {
		return pokes[(int) (RandomSource.random() * 649 + 1)];
	}

	@Override
	public void applyMoveUpdates() {
		// this is gen 5 already, no need for anything
	}

	@Override
	public List<Move> getMoves() {
		return Arrays.asList(moves);
	}

	@Override
	public List<EncounterSet> getEncounters(boolean useTimeOfDay) {
		try {
			NARCContents encounterNARC = readNARC(encountersNARC[type]);
			List<EncounterSet> encounters = new ArrayList<EncounterSet>();
			for (byte[] entry : encounterNARC.files) {
				if (entry.length > 232 && useTimeOfDay) {
					for (int i = 0; i < 4; i++) {
						processEncounterEntry(encounters, entry, i * 232);
					}
				} else {
					processEncounterEntry(encounters, entry, 0);
				}
			}
			return encounters;
		} catch (IOException e) {
			// whuh-oh
			e.printStackTrace();
			return new ArrayList<EncounterSet>();
		}
	}

	private void processEncounterEntry(List<EncounterSet> encounters,
			byte[] entry, int startOffset) {
		int[] amounts = new int[] { 12, 12, 12, 5, 5, 5, 5 };

		int offset = 8;
		for (int i = 0; i < 7; i++) {
			int rate = entry[startOffset + i] & 0xFF;
			if (rate != 0) {
				List<Encounter> encs = readEncounters(entry, startOffset
						+ offset, amounts[i]);
				EncounterSet area = new EncounterSet();
				area.rate = rate;
				area.encounters = encs;
				encounters.add(area);
			}
			offset += amounts[i] * 4;
		}

	}

	private List<Encounter> readEncounters(byte[] data, int offset, int number) {
		List<Encounter> encs = new ArrayList<Encounter>();
		for (int i = 0; i < number; i++) {
			Encounter enc1 = new Encounter();
			enc1.pokemon = pokes[((data[offset + i * 4] & 0xFF) + ((data[offset
					+ 1 + i * 4] & 0x03) << 8))];
			enc1.level = data[offset + 2 + i * 4] & 0xFF;
			enc1.maxLevel = data[offset + 3 + i * 4] & 0xFF;
			encs.add(enc1);
		}
		return encs;
	}

	@Override
	public void setEncounters(boolean useTimeOfDay,
			List<EncounterSet> encountersList) {
		try {
			NARCContents encounterNARC = readNARC(encountersNARC[type]);
			Iterator<EncounterSet> encounters = encountersList.iterator();
			for (byte[] entry : encounterNARC.files) {
				writeEncounterEntry(encounters, entry, 0);
				if (entry.length > 232) {
					if (useTimeOfDay) {
						for (int i = 1; i < 4; i++) {
							writeEncounterEntry(encounters, entry, i * 232);
						}
					} else {
						// copy for other 3 seasons
						System.arraycopy(entry, 0, entry, 232, 232);
						System.arraycopy(entry, 0, entry, 464, 232);
						System.arraycopy(entry, 0, entry, 696, 232);
					}
				}
			}

			// Save
			writeNARC(encountersNARC[type], encounterNARC);
		} catch (IOException e) {
			// whuh-oh
			e.printStackTrace();
		}

	}

	private void writeEncounterEntry(Iterator<EncounterSet> encounters,
			byte[] entry, int startOffset) {
		int[] amounts = new int[] { 12, 12, 12, 5, 5, 5, 5 };

		int offset = 8;
		for (int i = 0; i < 7; i++) {
			int rate = entry[startOffset + i] & 0xFF;
			if (rate != 0) {
				EncounterSet area = encounters.next();
				for (int j = 0; j < amounts[i]; j++) {
					Encounter enc = area.encounters.get(j);
					writeWord(entry, startOffset + offset + j * 4,
							enc.pokemon.number);
					entry[startOffset + offset + j * 4 + 2] = (byte) enc.level;
					entry[startOffset + offset + j * 4 + 3] = (byte) enc.maxLevel;
				}
			}
			offset += amounts[i] * 4;
		}
	}

	@Override
	public List<Trainer> getTrainers() {
		List<Trainer> allTrainers = new ArrayList<Trainer>();
		try {
			NARCContents trainers = this.readNARC(trainersNARC[type]);
			NARCContents trpokes = this.readNARC(trpokesNARC[type]);
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
					// Structure is
					// AI SB LV LV SP SP FRM FRM
					// (HI HI)
					// (M1 M1 M2 M2 M3 M3 M4 M4)
					// where SB = 0 0 Ab Ab 0 0 Fm Ml
					// Ab Ab = ability number, 0 for random
					// Fm = 1 for forced female
					// Ml = 1 for forced male
					// There's also a trainer flag to force gender, but
					// this allows fixed teams with mixed genders.

					int ailevel = trpoke[pokeOffs] & 0xFF;
					// int secondbyte = trpoke[pokeOffs + 1] & 0xFF;
					int level = readWord(trpoke, pokeOffs + 2);
					int species = readWord(trpoke, pokeOffs + 4);
					// int formnum = readWord(trpoke, pokeOffs + 6);
					TrainerPokemon tpk = new TrainerPokemon();
					tpk.level = level;
					tpk.pokemon = pokes[species];
					tpk.AILevel = ailevel;
					pokeOffs += 8;
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
					tr.pokemon.add(tpk);
				}
				allTrainers.add(tr);
			}
			if (type == Type_BW) {
				tagTrainersBW(allTrainers);
			} else {
				tagTrainersBW2(allTrainers);
			}
		} catch (IOException ex) {
			// change this later
			ex.printStackTrace();
		}
		return allTrainers;
	}

	private void tagTrainersBW(List<Trainer> trs) {
		// We use different Gym IDs to cheat the system for the 3 n00bs
		// Chili, Cress, and Cilan
		// Cilan can be GYM1, then Chili is GYM9 and Cress GYM10
		// Also their *trainers* are GYM11 lol

		// Gym Trainers
		tag(trs, "GYM11", 0x09, 0x0A);
		tag(trs, "GYM2", 0x56, 0x57, 0x58);
		tag(trs, "GYM3", 0xC4, 0xC6, 0xC7, 0xC8);
		tag(trs, "GYM4", 0x42, 0x43, 0x44, 0x45);
		tag(trs, "GYM5", 0xC9, 0xCA, 0xCB, 0x5F, 0xA8);
		tag(trs, "GYM6", 0x7D, 0x7F, 0x80, 0x46, 0x47);
		tag(trs, "GYM7", 0xD7, 0xD8, 0xD9, 0xD4, 0xD5, 0xD6);
		tag(trs, "GYM8", 0x109, 0x10A, 0x10F, 0x10E, 0x110, 0x10B, 0x113, 0x112);

		// Gym Leaders
		tag(trs, 0x0C, "GYM1"); // Cilan
		tag(trs, 0x0B, "GYM9"); // Chili
		tag(trs, 0x0D, "GYM10"); // Cress
		tag(trs, 0x15, "GYM2"); // Lenora
		tag(trs, 0x16, "GYM3"); // Burgh
		tag(trs, 0x17, "GYM4"); // Elesa
		tag(trs, 0x18, "GYM5"); // Clay
		tag(trs, 0x19, "GYM6"); // Skyla
		tag(trs, 0x83, "GYM7"); // Brycen
		tag(trs, 0x84, "GYM8"); // Iris or Drayden
		tag(trs, 0x85, "GYM8"); // Iris or Drayden

		// Elite 4
		tag(trs, 0xE4, "ELITE1"); // Shauntal
		tag(trs, 0xE6, "ELITE2"); // Grimsley
		tag(trs, 0xE7, "ELITE3"); // Caitlin
		tag(trs, 0xE5, "ELITE4"); // Marshal

		// Elite 4 R2
		tag(trs, 0x233, "ELITE1"); // Shauntal
		tag(trs, 0x235, "ELITE2"); // Grimsley
		tag(trs, 0x236, "ELITE3"); // Caitlin
		tag(trs, 0x234, "ELITE4"); // Marshal
		tag(trs, 0x197, "CHAMPION"); // Alder

		// Ubers?
		tag(trs, 0x21E, "UBER"); // Game Freak Guy
		tag(trs, 0x237, "UBER"); // Cynthia
		tag(trs, 0xE8, "UBER"); // Ghetsis
		tag(trs, 0x24A, "UBER"); // N-White
		tag(trs, 0x24B, "UBER"); // N-Black

		// Rival - Cheren
		tagRivalBW(trs, "RIVAL1", 0x35);
		tagRivalBW(trs, "RIVAL2", 0x11F);
		tagRivalBW(trs, "RIVAL3", 0x38); // used for 3rd battle AND tag battle
		tagRivalBW(trs, "RIVAL4", 0x193);
		tagRivalBW(trs, "RIVAL5", 0x5A); // 5th battle & 2nd tag battle
		tagRivalBW(trs, "RIVAL6", 0x21B);
		tagRivalBW(trs, "RIVAL7", 0x24C);
		tagRivalBW(trs, "RIVAL8", 0x24F);

		// Rival - Bianca
		tagRivalBW(trs, "FRIEND1", 0x3B);
		tagRivalBW(trs, "FRIEND2", 0x1F2);
		tagRivalBW(trs, "FRIEND3", 0x1FB);
		tagRivalBW(trs, "FRIEND4", 0x1EB);
		tagRivalBW(trs, "FRIEND5", 0x1EE);
		tagRivalBW(trs, "FRIEND6", 0x252);
	}

	private void tagTrainersBW2(List<Trainer> trs) {
		// Use GYM9/10/11 for the retired Chili/Cress/Cilan.
		// Lenora doesn't have a team, or she'd be 12.
		// Likewise for Brycen

		// Some trainers have TWO teams because of Challenge Mode
		// I believe this is limited to Gym Leaders, E4, Champ...
		// The "Challenge Mode" teams have levels at similar to regular,
		// but have the normal boost applied too.

		// Gym Trainers
		tag(trs, "GYM1", 0xab, 0xac);
		tag(trs, "GYM2", 0xb2, 0xb3);
		tag(trs, "GYM3", 0x2de, 0x2df, 0x2e0, 0x2e1);
		// GYM4: old gym site included to give the city a theme
		tag(trs, "GYM4", 0x26d, 0x94, 0xcf, 0xd0, 0xd1); // 0x94 might be 0x324
		tag(trs, "GYM5", 0x13f, 0x140, 0x141, 0x142, 0x143, 0x144, 0x145);
		tag(trs, "GYM6", 0x95, 0x96, 0x97, 0x98, 0x14c);
		tag(trs, "GYM7", 0x17d, 0x17e, 0x17f, 0x180, 0x181);
		tag(trs, "GYM8", 0x15e, 0x15f, 0x160, 0x161, 0x162, 0x163);

		// Gym Leaders
		// Order: Normal, Challenge Mode
		// All the challenge mode teams are near the end of the ROM
		// which makes things a bit easier.
		tag(trs, "GYM1", 0x9c, 0x2fc); // Cheren
		tag(trs, "GYM2", 0x9d, 0x2fd); // Roxie
		tag(trs, "GYM3", 0x9a, 0x2fe); // Burgh
		tag(trs, "GYM4", 0x99, 0x2ff); // Elesa
		tag(trs, "GYM5", 0x9e, 0x300); // Clay
		tag(trs, "GYM6", 0x9b, 0x301); // Skyla
		tag(trs, "GYM7", 0x9f, 0x302); // Drayden
		tag(trs, "GYM8", 0xa0, 0x303); // Marlon

		// Elite 4 / Champion
		// Order: Normal, Challenge Mode, Rematch, Rematch Challenge Mode
		tag(trs, "ELITE1", 0x26, 0x304, 0x8f, 0x309);
		tag(trs, "ELITE2", 0x28, 0x305, 0x91, 0x30a);
		tag(trs, "ELITE3", 0x29, 0x307, 0x92, 0x30c);
		tag(trs, "ELITE4", 0x27, 0x306, 0x90, 0x30b);
		tag(trs, "CHAMPION", 0x155, 0x308, 0x218, 0x30d);

		// Rival - Hugh
		tagRivalBW(trs, "RIVAL1", 0xa1); // Start
		tagRivalBW(trs, "RIVAL2", 0xa4); // Floccessy Ranch
		tagRivalBW(trs, "RIVAL3", 0x24c); // Tag Battles in the sewers
		tagRivalBW(trs, "RIVAL4", 0x170); // Tag Battle on the Plasma Frigate
		tagRivalBW(trs, "RIVAL5", 0x17a); // Undella Town 1st visit
		tagRivalBW(trs, "RIVAL6", 0x2bd); // Lacunosa Town Tag Battle
		tagRivalBW(trs, "RIVAL7", 0x31a); // 2nd Plasma Frigate Tag Battle
		tagRivalBW(trs, "RIVAL8", 0x2ac); // Victory Road
		tagRivalBW(trs, "RIVAL9", 0x2b5); // Undella Town Post-E4
		tagRivalBW(trs, "RIVAL10", 0x2b8); // Driftveil Post-Undella-Battle

		// Tag Battle with Opposite Gender Hero
		tagRivalBW(trs, "FRIEND1", 0x168);
		tagRivalBW(trs, "FRIEND1", 0x16b);

		// Tag Battles with Cheren
		tag(trs, "GYM1", 0x173, 0x278);

		// The Restaurant Brothers
		tag(trs, "GYM9", 0x1f0); // Cilan
		tag(trs, "GYM10", 0x1ee); // Chili
		tag(trs, "GYM11", 0x1ef); // Cress

		// Themed Trainers
		tag(trs, "THEMED:ZINZOLIN", 0x2c0, 0x248, 0x15b);
		tag(trs, "THEMED:COLRESS", 0x166, 0x158, 0x32d);
		tag(trs, "THEMED:SHADOW1", 0x247, 0x15c, 0x2af);
		tag(trs, "THEMED:SHADOW2", 0x1f2, 0x2b0);
		tag(trs, "THEMED:SHADOW3", 0x1f3, 0x2b1);

		// Uber-Trainers
		// There are *fourteen* ubers of 17 allowed (incl. the champion)
		// It's a rather stacked game...
		tag(trs, 0x246, "UBER"); // Alder
		tag(trs, 0x1c8, "UBER"); // Cynthia
		tag(trs, 0xca, "UBER"); // Benga/BlackTower
		tag(trs, 0xc9, "UBER"); // Benga/WhiteTreehollow
		tag(trs, 0x5, "UBER"); // N/Zekrom
		tag(trs, 0x6, "UBER"); // N/Reshiram
		tag(trs, 0x30e, "UBER"); // N/Spring
		tag(trs, 0x30f, "UBER"); // N/Summer
		tag(trs, 0x310, "UBER"); // N/Autumn
		tag(trs, 0x311, "UBER"); // N/Winter
		tag(trs, 0x159, "UBER"); // Ghetsis
		tag(trs, 0x8c, "UBER"); // Game Freak Guy
		tag(trs, 0x24f, "UBER"); // Game Freak Leftovers Guy

	}

	private void tagRivalBW(List<Trainer> allTrainers, String tag, int offset) {
		allTrainers.get(offset - 1).tag = tag + "-0";
		allTrainers.get(offset).tag = tag + "-1";
		allTrainers.get(offset + 1).tag = tag + "-2";

	}

	private void tag(List<Trainer> allTrainers, int number, String tag) {
		allTrainers.get(number - 1).tag = tag;
	}

	private void tag(List<Trainer> allTrainers, String tag, int... numbers) {
		for (int num : numbers) {
			allTrainers.get(num - 1).tag = tag;
		}
	}

	@Override
	public void setTrainers(List<Trainer> trainerData) {
		Iterator<Trainer> allTrainers = trainerData.iterator();
		try {
			NARCContents trainers = this.readNARC(trainersNARC[type]);
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

				int bytesNeeded = 8 * numPokes;
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
					trpoke[pokeOffs] = (byte) tpk.AILevel;
					// no gender or ability info, so no byte 1
					writeWord(trpoke, pokeOffs + 2, tpk.level);
					writeWord(trpoke, pokeOffs + 4, tpk.pokemon.number);
					// no form info, so no byte 6/7
					pokeOffs += 8;
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
				}
				trpokes.files.add(trpoke);
			}
			this.writeNARC(trainersNARC[type], trainers);
			this.writeNARC(trpokesNARC[type], trpokes);
		} catch (IOException ex) {
			// change this later
			ex.printStackTrace();
		}
	}

	@Override
	public Map<Pokemon, List<MoveLearnt>> getMovesLearnt() {
		Map<Pokemon, List<MoveLearnt>> movesets = new TreeMap<Pokemon, List<MoveLearnt>>();
		try {
			NARCContents movesLearnt = this.readNARC(movesetsNARC[type]);
			for (int i = 1; i <= 649; i++) {
				Pokemon pkmn = pokes[i];
				byte[] movedata = movesLearnt.files.get(i);
				int moveDataLoc = 0;
				List<MoveLearnt> learnt = new ArrayList<MoveLearnt>();
				while (readWord(movedata, moveDataLoc) != 0xFFFF
						|| readWord(movedata, moveDataLoc + 2) != 0xFFFF) {
					int move = readWord(movedata, moveDataLoc);
					int level = readWord(movedata, moveDataLoc + 2);
					MoveLearnt ml = new MoveLearnt();
					ml.level = level;
					ml.move = move;
					learnt.add(ml);
					moveDataLoc += 4;
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
		try {
			NARCContents movesLearnt = readNARC(movesetsNARC[type]);
			for (int i = 1; i <= 649; i++) {
				Pokemon pkmn = pokes[i];
				List<MoveLearnt> learnt = movesets.get(pkmn);
				int sizeNeeded = learnt.size() * 4 + 4;
				byte[] moveset = new byte[sizeNeeded];
				int j = 0;
				for (; j < learnt.size(); j++) {
					MoveLearnt ml = learnt.get(j);
					writeWord(moveset, j * 4, ml.move);
					writeWord(moveset, j * 4 + 2, ml.level);
				}
				writeWord(moveset, j * 4, 0xFFFF);
				writeWord(moveset, j * 4 + 2, 0xFFFF);
				movesLearnt.files.set(i, moveset);
			}
			// Save
			this.writeNARC(movesetsNARC[type], movesLearnt);
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
		String tmDataPrefix = "87038803";
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			List<Integer> tms = new ArrayList<Integer>();
			for (int i = 0; i < 92; i++) {
				tms.add(readWord(arm9, offset + i * 2));
			}
			// Skip past first 92 TMs and 6 HMs
			offset += 196;
			for (int i = 0; i < 3; i++) {
				tms.add(readWord(arm9, offset + i * 2));
			}
			return tms;
		} else {
			return null;
		}
	}

	@Override
	public List<Integer> getHMMoves() {
		String tmDataPrefix = "87038803";
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			offset += 184; // TM data
			List<Integer> hms = new ArrayList<Integer>();
			for (int i = 0; i < 6; i++) {
				hms.add(readWord(arm9, offset + i * 2));
			}
			return hms;
		} else {
			return null;
		}
	}

	@Override
	public void setTMMoves(List<Integer> moveIndexes) {
		String tmDataPrefix = "87038803";
		int offset = find(arm9, tmDataPrefix);
		if (offset > 0) {
			offset += 4; // because it was a prefix
			for (int i = 0; i < 92; i++) {
				writeWord(arm9, offset + i * 2, moveIndexes.get(i));
			}
			// Skip past those 92 TMs and 6 HMs
			offset += 196;
			for (int i = 0; i < 3; i++) {
				writeWord(arm9, offset + i * 2, moveIndexes.get(i + 92));
			}

			// Update TM item descriptions
			List<String> itemDescriptions = getStrings(false,
					itemDescriptionsFile[type]);
			List<String> moveDescriptions = getStrings(false,
					moveDescriptionsFile[type]);
			// TM01 is item 328 and so on
			for (int i = 0; i < 92; i++) {
				itemDescriptions.set(i + 328,
						moveDescriptions.get(moveIndexes.get(i)));
			}
			// TM93-95 are 618-620
			for (int i = 0; i < 3; i++) {
				itemDescriptions.set(i + 618,
						moveDescriptions.get(moveIndexes.get(i + 92)));
			}
			// Save the new item descriptions
			setStrings(false, itemDescriptionsFile[type], itemDescriptions);
			// Palettes
			String baseOfPalettes;
			if (type == Type_BW) {
				baseOfPalettes = "E903EA03020003000400050006000700";
			} else {
				baseOfPalettes = "FD03FE03020003000400050006000700";
			}
			int offsPals = find(arm9, baseOfPalettes);
			if (offsPals > 0) {
				// Write pals
				for (int i = 0; i < 92; i++) {
					int itmNum = 328 + i;
					Move m = this.moves[moveIndexes.get(i)];
					int pal = this.typeTMPaletteNumber(m.type);
					writeWord(arm9, offsPals + itmNum * 4 + 2, pal);
				}
				for (int i = 0; i < 3; i++) {
					int itmNum = 618 + i;
					Move m = this.moves[moveIndexes.get(i + 92)];
					int pal = this.typeTMPaletteNumber(m.type);
					writeWord(arm9, offsPals + itmNum * 4 + 2, pal);
				}
			}
		} else {
		}
	}

	@SuppressWarnings("unused")
	private static RomFunctions.StringSizeDeterminer ssd = new RomFunctions.StringSizeDeterminer() {

		@Override
		public int lengthFor(String encodedText) {
			int offs = 0;
			int len = encodedText.length();
			while (encodedText.indexOf("\\x", offs) != -1) {
				len -= 5;
				offs = encodedText.indexOf("\\x", offs) + 1;
			}
			return len;
		}
	};

	@Override
	public int getTMCount() {
		return 95;
	}

	@Override
	public int getHMCount() {
		return 6;
	}

	@Override
	public Map<Pokemon, boolean[]> getTMHMCompatibility() {
		Map<Pokemon, boolean[]> compat = new TreeMap<Pokemon, boolean[]>();
		for (int i = 1; i <= 649; i++) {
			byte[] data = pokeNarc.files.get(i);
			Pokemon pkmn = pokes[i];
			boolean[] flags = new boolean[102];
			for (int j = 0; j < 13; j++) {
				readByteIntoFlags(data, flags, j * 8 + 1, 0x28 + j);
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
				data[0x28 + j] = getByteFromFlags(flags, j * 8 + 1);
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

	private List<String> getStrings(boolean isStoryText, int index) {
		NARCContents baseNARC = isStoryText ? storyTextNarc : stringsNarc;
		byte[] rawFile = baseNARC.files.get(index);
		return new ArrayList<String>(PPTxtHandler.readTexts(rawFile));
	}

	private void setStrings(boolean isStoryText, int index, List<String> strings) {
		NARCContents baseNARC = isStoryText ? storyTextNarc : stringsNarc;
		byte[] oldRawFile = baseNARC.files.get(index);
		byte[] newRawFile = PPTxtHandler.saveEntry(oldRawFile, strings);
		baseNARC.files.set(index, newRawFile);
	}

	@Override
	public String getROMName() {
		switch (type) {
		case Type_BW:
			if (ndsCode.charAt(2) == 'B') {
				return "Pokemon Black";
			} else {
				return "Pokemon White";
			}
		case Type_BW2:
			if (ndsCode.charAt(2) == 'E') {
				return "Pokemon Black 2";
			} else {
				return "Pokemon White 2";
			}
		default:
			return "???";
		}
	}

	@Override
	public String getROMCode() {
		return ndsCode;
	}

	@Override
	public String getSupportLevel() {
		return "No Static Pokemon";
	}

	@Override
	public boolean hasTimeBasedEncounters() {
		return true; // All BW/BW2 do [seasons]
	}

	@Override
	public void removeTradeEvolutions() {
		// Read NARC
		try {
			NARCContents evoNARC = readNARC(evolutionsNARC[type]);
			log("--Removing Trade Evolutions--");
			for (int i = 1; i <= 649; i++) {
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
							writeWord(evoEntry, evo * 6, 8);
							writeWord(evoEntry, evo * 6 + 2, 84);
						} else {
							log("Made "
									+ pokes[i].name
									+ " evolve into "
									+ pokes[species].name
									+ " by leveling up holding the item it had to be traded with before");
							// Replace, for this entry, w/
							// Level up w/ Held Item at Day
							writeWord(evoEntry, evo * 6, 19);
							// Now look for a free slot to put
							// Level up w/ Held Item at Night
							for (int evo2 = evo + 1; evo2 < 7; evo2++) {
								if (readWord(evoEntry, evo2 * 6) == 0) {
									// Bingo, blank entry
									writeWord(evoEntry, evo2 * 6, 20);
									writeWord(evoEntry, evo2 * 6 + 2, item);
									writeWord(evoEntry, evo2 * 6 + 4, species);
									break;
								}
							}
						}
					} else if (evoType == 7) {
						// This is the karrablast <-> shelmet trade
						// Replace it with Level up w/ Other Species in Party
						// (22)
						// Based on what species we're currently dealing with
						writeWord(evoEntry, evo * 6, 22);
						writeWord(evoEntry, evo * 6 + 2, (i == 588 ? 616 : 588));
						log("Made " + pokes[i].name + " evolve into "
								+ pokes[species].name + " by leveling up with "
								+ pokes[(i == 588 ? 616 : 588)].name
								+ " in the party");
					}
				}
			}
			writeNARC(evolutionsNARC[type], evoNARC);
			logBlankLine();
		} catch (IOException e) {
			// can't do anything
		}

	}

	@Override
	public List<String> getTrainerNames() {
		List<String> tnames = getStrings(false, trainerNamesFile[type]);
		tnames.remove(0); // blank one
		// Tack the mugshot names on the end
		List<String> mnames = getStrings(false, mugshotsFile[type]);
		for (String mname : mnames) {
			if (!mname.isEmpty()
					&& (mname.charAt(0) >= 'A' && mname.charAt(0) <= 'Z')) {
				tnames.add(mname);
			}
		}
		return tnames;
	}

	@Override
	public int maxTrainerNameLength() {
		return 10;// based off the english ROMs
	}

	@Override
	public void setTrainerNames(List<String> trainerNames) {
		List<String> tnames = getStrings(false, trainerNamesFile[type]);
		// Grab the mugshot names off the back of the list of trainer names
		// we got back
		List<String> mnames = getStrings(false, mugshotsFile[type]);
		int trNamesSize = trainerNames.size();
		for (int i = mnames.size() - 1; i >= 0; i--) {
			String origMName = mnames.get(i);
			if (!origMName.isEmpty()
					&& (origMName.charAt(0) >= 'A' && origMName.charAt(0) <= 'Z')) {
				// Grab replacement
				String replacement = trainerNames.remove(--trNamesSize);
				mnames.set(i, replacement);
			}
		}
		// Save back mugshot names
		setStrings(false, mugshotsFile[type], mnames);

		// Now save the rest of trainer names
		List<String> newTNames = new ArrayList<String>(trainerNames);
		newTNames.add(0, tnames.get(0)); // the 0-entry, preserve it
		setStrings(false, trainerNamesFile[type], newTNames);

	}

	@Override
	public boolean fixedTrainerNamesLength() {
		return false;
	}

	@Override
	public List<String> getTrainerClassNames() {
		return getStrings(false, trainerClassesFile[type]);
	}

	@Override
	public void setTrainerClassNames(List<String> trainerClassNames) {
		setStrings(false, trainerClassesFile[type], trainerClassNames);
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
		return 3;
	}

	@Override
	public int highestAbilityIndex() {
		return 164;
	}
}
