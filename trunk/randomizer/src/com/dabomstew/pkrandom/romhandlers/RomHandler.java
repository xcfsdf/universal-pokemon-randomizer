package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  RomHandler.java - defines the functionality that each randomization	--*/
/*--					handler must implement.								--*/
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

import java.util.List;
import java.util.Map;

import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.Evolution;
import com.dabomstew.pkrandom.pokemon.Move;
import com.dabomstew.pkrandom.pokemon.MoveLearnt;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.Type;

public interface RomHandler {

	// Check whether this ROM is for this handler or not

	public boolean detectRom(String filename);

	// Basic load/save to filenames

	public boolean loadRom(String filename);

	public boolean saveRom(String filename);

	// Functionality
	public boolean isInGame(Pokemon pkmn);

	public boolean isInGame(int pokemonNumber);

	// Get the evolution pairs that apply in this game
	public List<Evolution> getEvolutions();

	// Get a List of Pokemon objects in this game.
	// 0 = null 1-whatever = the Pokemon.
	public List<Pokemon> getPokemon();

	// Randomizer: Starters
	// Get starters, they should be ordered with Pokemon
	// following the one it is SE against.
	// E.g. Grass, Fire, Water or Fire, Water, Grass etc.
	public List<Pokemon> getStarters();

	// Change the starter data in the ROM.
	// Optionally also change the starter used by the rival in
	// the level 5 battle, if there is one.
	public boolean setStarters(List<Pokemon> newStarters);

	// Tells whether this ROM has the ability to have starters changed.
	// Was for before CUE's compressors were found and arm9 was untouchable.
	public boolean canChangeStarters();

	// Randomizer: Pokemon stats

	// Run the stats shuffler on each Pokemon.
	public void shufflePokemonStats();

	// Randomise stats following evolutions for proportions or not (see
	// tooltips)
	public void randomizePokemonStats(boolean evolutionSanity);

	// Give a random Pokemon who's in this game
	public Pokemon randomPokemon();

	// Give a random non-legendary Pokemon who's in this game
	// Business rules for who's legendary are in Pokemon class
	public Pokemon randomNonLegendaryPokemon();

	// Give a random legendary Pokemon who's in this game
	// Business rules for who's legendary are in Pokemon class
	public Pokemon randomLegendaryPokemon();

	// Give a random Pokemon who has 2 evolution stages
	// Should make a good starter Pokemon
	public Pokemon random2EvosPokemon();

	// Randomizer: moves

	// Update all moves to gen5 definitions as much as possible
	// e.g. change typing, power, accuracy, but don't try to
	// stuff around with effects.
	public void applyMoveUpdates();

	// return all the moves valid in this game.
	public List<Move> getMoves();

	// Randomizer: types

	// return a random type valid in this game.
	// straightforward except for gen1 where dark&steel are excluded.
	public Type randomType();

	// randomise Pokemon types, with a switch on whether evolutions
	// should follow the same types or not.
	// some evolutions dont anyway, e.g. Eeveelutions, Hitmons
	public void randomizePokemonTypes(boolean evolutionSanity);

	// Randomizer: wild pokemon
	public List<EncounterSet> getEncounters(boolean useTimeOfDay);

	public void setEncounters(boolean useTimeOfDay,
			List<EncounterSet> encounters);

	public void randomEncounters(boolean useTimeOfDay, boolean catchEmAll,
			boolean typeThemed, boolean noLegendaries);

	public void area1to1Encounters(boolean useTimeOfDay, boolean catchEmAll,
			boolean typeThemed, boolean noLegendaries);

	public void game1to1Encounters(boolean useTimeOfDay, boolean noLegendaries);

	public boolean hasTimeBasedEncounters();

	public List<Pokemon> bannedForWildEncounters();

	// Randomizer: trainer pokemon
	public List<Trainer> getTrainers();

	public void setTrainers(List<Trainer> trainerData);

	public void randomizeTrainerPokes(boolean rivalCarriesStarter,
			boolean usePowerLevels, boolean noLegendaries,
			boolean noEarlyShedinja);

	public void typeThemeTrainerPokes(boolean rivalCarriesStarter,
			boolean usePowerLevels, boolean weightByFrequency,
			boolean noLegendaries, boolean noEarlyShedinja);

	public boolean typeInGame(Type type);

	// Randomizer: moves learnt

	public Map<Pokemon, List<MoveLearnt>> getMovesLearnt();

	public void setMovesLearnt(Map<Pokemon, List<MoveLearnt>> movesets);

	public void randomizeMovesLearnt(boolean typeThemed);

	// Randomizer: static pokemon (except starters)

	public List<Pokemon> getStaticPokemon();

	public boolean setStaticPokemon(List<Pokemon> staticPokemon);

	public void randomizeStaticPokemon(boolean legendForLegend);

	public boolean canChangeStaticPokemon();

	public List<Pokemon> bannedForStaticPokemon();

	// Randomizer: TMs/HMs

	public List<Integer> getTMMoves();

	public List<Integer> getHMMoves();

	public void setTMMoves(List<Integer> moveIndexes);

	public void randomizeTMMoves();

	public int getTMCount();

	public int getHMCount();

	/**
	 * Get TM/HM compatibility data from this rom. The result should contain a
	 * boolean array for each Pokemon indexed as such:
	 * 
	 * 0: blank (false) / 1 - (getTMCount()) : TM compatibility /
	 * (getTMCount()+1) - (getTMCount()+getHMCount()) - HM compatibility
	 * 
	 * @return
	 */

	public Map<Pokemon, boolean[]> getTMHMCompatibility();

	public void setTMHMCompatibility(Map<Pokemon, boolean[]> compatData);

	public void randomizeTMHMCompatibility(boolean preferSameType);

	// Randomizer: move tutors

	public boolean hasMoveTutors();

	public List<Integer> getMoveTutorMoves();

	public void setMoveTutorMoves(List<Integer> moves);

	public void randomizeMoveTutorMoves();

	public Map<Pokemon, boolean[]> getMoveTutorCompatibility();

	public void setMoveTutorCompatibility(Map<Pokemon, boolean[]> compatData);

	public void randomizeMoveTutorCompatibility(boolean preferSameType);

	// Randomizer: trainer names

	public List<String> getTrainerNames();

	public void setTrainerNames(List<String> trainerNames);

	public boolean fixedTrainerNamesLength();

	public int maxTrainerNameLength();

	public void randomizeTrainerNames();

	// Randomizer: trainer class names

	public List<String> getTrainerClassNames();

	public void setTrainerClassNames(List<String> trainerClassNames);

	public boolean fixedTrainerClassNamesLength();

	public int maxTrainerClassNameLength();

	public void randomizeTrainerClassNames();

	// Randomizer: pokemon abilities
	public int abilitiesPerPokemon();

	public int highestAbilityIndex();

	public String abilityName(int number);

	public void randomizeAbilities(boolean allowWonderGuard);

	// Misc

	public void applyCamelCaseNames();

	public void removeTradeEvolutions();

	public boolean isYellow();

	public void minimumCatchRate(int rate);

	public String getROMName();

	public String getROMCode();

	public String getSupportLevel();

	public void fixTypeEffectiveness();

	public void patchForNationalDex();

	public String getDefaultExtension();

	public int internalStringLength(String string);

	public boolean hasBWEXPPatch();

	public void applyBWEXPPatch();

}
