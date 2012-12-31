package com.dabomstew.pkrandom.romhandlers;

/*----------------------------------------------------------------------------*/
/*--  AbstractRomHandler.java - a base class for all rom handlers which		--*/
/*--							implements the majority of the actual		--*/
/*--							randomizer logic by building on the base	--*/
/*--							getters & setters provided by each concrete	--*/
/*--							handler.									--*/
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.RandomSource;
import com.dabomstew.pkrandom.RomFunctions;
import com.dabomstew.pkrandom.gui.RandomizerGUI;
import com.dabomstew.pkrandom.pokemon.Encounter;
import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.Evolution;
import com.dabomstew.pkrandom.pokemon.EvolutionData;
import com.dabomstew.pkrandom.pokemon.Move;
import com.dabomstew.pkrandom.pokemon.MoveLearnt;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.TrainerPokemon;
import com.dabomstew.pkrandom.pokemon.Type;

public abstract class AbstractRomHandler implements RomHandler {

	protected List<Evolution> evolutions;

	public AbstractRomHandler() {
		this.evolutions = EvolutionData.evosFor(this);
	}

	@Override
	public List<Evolution> getEvolutions() {
		return evolutions;
	}

	@Override
	public void randomizePokemonStats(boolean evolutionSanity) {
		List<Pokemon> allPokes = this.getPokemon();
		if (evolutionSanity) {
			// Spread stats up MOST evolutions.
			Set<Pokemon> dontCopyPokes = RomFunctions
					.getBasicOrNoCopyPokemon(this);

			for (Pokemon pk : dontCopyPokes) {
				pk.randomizeStatsWithinBST();
			}

			for (Evolution evo : evolutions) {
				if (evo.carryStats) {
					Pokemon to = allPokes.get(evo.to);
					Pokemon from = allPokes.get(evo.from);
					to.copyRandomizedStatsUpEvolution(from);
				}
			}
		} else {
			for (Pokemon pk : allPokes) {
				if (pk != null) {
					pk.randomizeStatsWithinBST();
				}
			}
		}

	}

	@Override
	public void applyCamelCaseNames() {
		List<Pokemon> pokes = getPokemon();
		for (Pokemon pkmn : pokes) {
			if (pkmn == null) {
				continue;
			}
			pkmn.name = RomFunctions.camelCase(pkmn.name);
		}

	}

	@Override
	public void minimumCatchRate(int rate) {
		List<Pokemon> pokes = getPokemon();
		for (Pokemon pkmn : pokes) {
			if (pkmn == null || pkmn.catchRate >= rate) {
				continue;
			}
			pkmn.catchRate = rate;
		}

	}

	@Override
	public Type randomType() {
		return Type.randomType();
	}

	@Override
	public void randomizePokemonTypes(boolean evolutionSanity) {
		if (evolutionSanity) {
			Set<Pokemon> dontCopyPokes = RomFunctions
					.getBasicOrNoCopyPokemon(this);
			// Type randomization
			// Step 1: Basic or Excluded From Copying Pokemon
			// A Basic/EFC pokemon has a 35% chance of a second type if it has
			// an evolution, a 50% chance otherwise
			for (Pokemon pk : dontCopyPokes) {
				pk.primaryType = randomType();
				pk.secondaryType = null;
				if (RomFunctions.pokemonHasEvo(this, pk)) {
					if (RandomSource.random() < 0.35) {
						pk.secondaryType = randomType();
						while (pk.secondaryType == pk.primaryType) {
							pk.secondaryType = randomType();
						}
					}
				} else {
					if (RandomSource.random() < 0.5) {
						pk.secondaryType = randomType();
						while (pk.secondaryType == pk.primaryType) {
							pk.secondaryType = randomType();
						}
					}
				}
			}
			// Step 2: First Evolutions
			// A first evolution has a 15% chance of adding a type if there's a
			// 3rd stage, Or a 25% chance if there's not
			Set<Pokemon> firstEvos = RomFunctions.getFirstEvolutions(this);
			for (Pokemon pk : firstEvos) {
				Pokemon evolvedFrom = RomFunctions.evolvesFrom(this, pk);
				pk.primaryType = evolvedFrom.primaryType;
				pk.secondaryType = evolvedFrom.secondaryType;
				if (pk.secondaryType == null) {
					if (RomFunctions.pokemonHasEvo(this, pk)) {
						if (RandomSource.random() < 0.15) {
							pk.secondaryType = randomType();
							while (pk.secondaryType == pk.primaryType) {
								pk.secondaryType = randomType();
							}
						}
					} else {
						if (RandomSource.random() < 0.25) {
							pk.secondaryType = randomType();
							while (pk.secondaryType == pk.primaryType) {
								pk.secondaryType = randomType();
							}
						}
					}
				}
			}

			// Step 3: Second Evolutions
			// A second evolution has a 25% chance of adding a type
			Set<Pokemon> secondEvos = RomFunctions.getSecondEvolutions(this);
			for (Pokemon pk : secondEvos) {
				Pokemon evolvedFrom = RomFunctions.evolvesFrom(this, pk);
				pk.primaryType = evolvedFrom.primaryType;
				pk.secondaryType = evolvedFrom.secondaryType;
				if (pk.secondaryType == null) {
					if (RandomSource.random() < 0.25) {
						pk.secondaryType = randomType();
						while (pk.secondaryType == pk.primaryType) {
							pk.secondaryType = randomType();
						}
					}
				}
			}
		} else {
			// Entirely random types
			List<Pokemon> allPokes = this.getPokemon();
			for (Pokemon pkmn : allPokes) {
				if (pkmn != null) {
					pkmn.primaryType = randomType();
					pkmn.secondaryType = null;
					if (RandomSource.random() < 0.5) {
						pkmn.secondaryType = randomType();
						while (pkmn.secondaryType == pkmn.primaryType) {
							pkmn.secondaryType = randomType();
						}
					}
				}
			}
		}
	}

	private static final int WONDER_GUARD_INDEX = 25;

	@Override
	public void randomizeAbilities(boolean allowWonderGuard) {
		// Abilities don't exist in some games...
		if (this.abilitiesPerPokemon() == 0) {
			return;
		}

		// Deal with "natural" abilities first regardless
		List<Pokemon> allPokes = this.getPokemon();
		int maxAbility = this.highestAbilityIndex();
		for (Pokemon pk : allPokes) {
			if (pk == null) {
				continue;
			}

			// Pick first ability
			pk.ability1 = RandomSource.nextInt(maxAbility) + 1;
			// Wonder guard block
			if (!allowWonderGuard) {
				while (pk.ability1 == WONDER_GUARD_INDEX) {
					pk.ability1 = RandomSource.nextInt(maxAbility) + 1;
				}
			}

			// Second ability?
			if (RandomSource.nextDouble() < 0.5) {
				// Yes, second ability
				pk.ability2 = RandomSource.nextInt(maxAbility) + 1;
				// Wonder guard? Also block first ability from reappearing
				if (allowWonderGuard) {
					while (pk.ability2 == pk.ability1) {
						pk.ability2 = RandomSource.nextInt(maxAbility) + 1;
					}
				} else {
					while (pk.ability2 == WONDER_GUARD_INDEX
							|| pk.ability2 == pk.ability1) {
						pk.ability2 = RandomSource.nextInt(maxAbility) + 1;
					}
				}
			} else {
				// Nope
				pk.ability2 = 0;
			}
		}

		// DW Abilities?
		if (this.abilitiesPerPokemon() == 3) {
			// Give a random DW ability to every Pokemon
			for (Pokemon pk : allPokes) {
				if (pk == null) {
					continue;
				}
				pk.ability3 = RandomSource.nextInt(maxAbility) + 1;
				// Wonder guard? Also block other abilities from reappearing
				if (allowWonderGuard) {
					while (pk.ability3 == pk.ability1
							|| pk.ability3 == pk.ability2) {
						pk.ability3 = RandomSource.nextInt(maxAbility) + 1;
					}
				} else {
					while (pk.ability3 == WONDER_GUARD_INDEX
							|| pk.ability3 == pk.ability1
							|| pk.ability3 == pk.ability2) {
						pk.ability3 = RandomSource.nextInt(maxAbility) + 1;
					}
				}
			}
		}

		// Shedinja
		// He's useless without Wonder Guard...
		// + he exists in every game with abilities.
		// Go fix him up.
		Pokemon shedinja = allPokes.get(292);
		shedinja.ability1 = 25;
		shedinja.ability2 = 0;
		shedinja.ability3 = 0;
	}

	@Override
	public String abilityName(int number) {
		return RomFunctions.abilityNames[number];
	}

	@Override
	public void randomEncounters(boolean useTimeOfDay, boolean catchEmAll,
			boolean typeThemed, boolean noLegendaries) {
		List<EncounterSet> currentEncounters = this.getEncounters(useTimeOfDay);
		// Assume EITHER catch em all OR type themed for now
		if (catchEmAll) {
			List<Pokemon> allPokes = noLegendaries ? allNonLegendaries()
					: allPokemonWithoutNull();
			for (EncounterSet area : currentEncounters) {
				for (Encounter enc : area.encounters) {
					// Pick a random pokemon
					int picked = RandomSource.nextInt(allPokes.size());
					enc.pokemon = allPokes.get(picked);
					allPokes.remove(picked);
					if (allPokes.size() == 0) {
						// Start again
						allPokes = noLegendaries ? allNonLegendaries()
								: allPokemonWithoutNull();
					}
				}
			}
		} else if (typeThemed) {
			Map<Type, List<Pokemon>> cachedPokeLists = new TreeMap<Type, List<Pokemon>>();
			for (EncounterSet area : currentEncounters) {
				Type areaTheme = randomType();
				if (!cachedPokeLists.containsKey(areaTheme)) {
					cachedPokeLists.put(areaTheme,
							pokemonOfType(areaTheme, noLegendaries));
				}
				List<Pokemon> possiblePokemon = cachedPokeLists.get(areaTheme);
				for (Encounter enc : area.encounters) {
					// Pick a random themed pokemon
					enc.pokemon = possiblePokemon.get(RandomSource
							.nextInt(possiblePokemon.size()));
				}
			}
		} else {
			// Entirely random
			for (EncounterSet area : currentEncounters) {
				for (Encounter enc : area.encounters) {
					enc.pokemon = noLegendaries ? randomNonLegendaryPokemon()
							: randomPokemon();
				}
			}
		}

		setEncounters(useTimeOfDay, currentEncounters);
	}

	@Override
	public Pokemon randomNonLegendaryPokemon() {
		Pokemon pkmn;
		while ((pkmn = randomPokemon()).isLegendary()) {
		}
		return pkmn;
	}

	@Override
	public Pokemon randomLegendaryPokemon() {
		Pokemon pkmn;
		while (!(pkmn = randomPokemon()).isLegendary()) {
		}
		return pkmn;
	}

	private List<Pokemon> twoEvoPokes;

	@Override
	public Pokemon random2EvosPokemon() {
		if (twoEvoPokes == null) {
			// Prepare the list
			List<Pokemon> allPokes = this.getPokemon();
			List<Pokemon> remainingPokes = allPokemonWithoutNull();
			List<Evolution> allEvos = this.getEvolutions();
			Map<Pokemon, Pokemon> reverseKeepPokemon = new TreeMap<Pokemon, Pokemon>();
			for (Evolution e : allEvos) {
				reverseKeepPokemon
						.put(allPokes.get(e.to), allPokes.get(e.from));
			}
			remainingPokes.retainAll(reverseKeepPokemon.values());
			// All pokemon with evolutions are left
			// Look for the evolutions themselves again in the evo-list
			Set<Pokemon> keepFor2Evos = new TreeSet<Pokemon>();
			for (Evolution e : allEvos) {
				Pokemon from = allPokes.get(e.from);
				if (reverseKeepPokemon.containsKey(from)) {
					keepFor2Evos.add(reverseKeepPokemon.get(from));
				}
			}
			remainingPokes.retainAll(keepFor2Evos);
			twoEvoPokes = remainingPokes;
		}
		return twoEvoPokes.get(RandomSource.nextInt(twoEvoPokes.size()));
	}

	@Override
	public void area1to1Encounters(boolean useTimeOfDay, boolean catchEmAll,
			boolean typeThemed, boolean noLegendaries) {
		List<EncounterSet> currentEncounters = this.getEncounters(useTimeOfDay);
		// Assume EITHER catch em all OR type themed for now
		if (catchEmAll) {
			List<Pokemon> allPokes = noLegendaries ? allNonLegendaries()
					: allPokemonWithoutNull();
			for (EncounterSet area : currentEncounters) {
				// Poke-set
				Set<Pokemon> inArea = pokemonInArea(area);
				// Build area map using catch em all
				Map<Pokemon, Pokemon> areaMap = new TreeMap<Pokemon, Pokemon>();
				for (Pokemon areaPk : inArea) {
					int picked = RandomSource.nextInt(allPokes.size());
					areaMap.put(areaPk, allPokes.get(picked));
					allPokes.remove(picked);
					if (allPokes.size() == 0) {
						// Start again
						allPokes = noLegendaries ? allNonLegendaries()
								: allPokemonWithoutNull();
					}
				}
				for (Encounter enc : area.encounters) {
					// Apply the map
					enc.pokemon = areaMap.get(enc.pokemon);
				}
			}
		} else if (typeThemed) {
			Map<Type, List<Pokemon>> cachedPokeLists = new TreeMap<Type, List<Pokemon>>();
			for (EncounterSet area : currentEncounters) {
				Type areaTheme = randomType();
				if (!cachedPokeLists.containsKey(areaTheme)) {
					cachedPokeLists.put(areaTheme,
							pokemonOfType(areaTheme, noLegendaries));
				}
				List<Pokemon> possiblePokemon = new ArrayList<Pokemon>(
						cachedPokeLists.get(areaTheme));
				// Poke-set
				Set<Pokemon> inArea = pokemonInArea(area);
				// Build area map using type theme, reset the list if needed
				Map<Pokemon, Pokemon> areaMap = new TreeMap<Pokemon, Pokemon>();
				for (Pokemon areaPk : inArea) {
					int picked = RandomSource.nextInt(possiblePokemon.size());
					areaMap.put(areaPk, possiblePokemon.get(picked));
					possiblePokemon.remove(picked);
					if (possiblePokemon.size() == 0) {
						// Start again
						possiblePokemon.addAll(cachedPokeLists.get(areaTheme));
					}
				}
				for (Encounter enc : area.encounters) {
					// Apply the map
					enc.pokemon = areaMap.get(enc.pokemon);
				}
			}
		} else {
			// Entirely random
			for (EncounterSet area : currentEncounters) {
				// Poke-set
				Set<Pokemon> inArea = pokemonInArea(area);
				// Build area map using randoms
				Map<Pokemon, Pokemon> areaMap = new TreeMap<Pokemon, Pokemon>();
				for (Pokemon areaPk : inArea) {
					Pokemon picked = noLegendaries ? randomNonLegendaryPokemon()
							: randomPokemon();
					while (areaMap.containsValue(picked)) {
						picked = noLegendaries ? randomNonLegendaryPokemon()
								: randomPokemon();
					}
					areaMap.put(areaPk, picked);
				}
				for (Encounter enc : area.encounters) {
					// Apply the map
					enc.pokemon = areaMap.get(enc.pokemon);
				}
			}
		}

		setEncounters(useTimeOfDay, currentEncounters);

	}

	@Override
	public void game1to1Encounters(boolean useTimeOfDay, boolean noLegendaries) {
		// Build the full 1-to-1 map
		Map<Pokemon, Pokemon> translateMap = new TreeMap<Pokemon, Pokemon>();
		List<Pokemon> remainingLeft = noLegendaries ? allNonLegendaries()
				: allPokemonWithoutNull();
		List<Pokemon> remainingRight = noLegendaries ? allNonLegendaries()
				: allPokemonWithoutNull();
		while (remainingLeft.isEmpty() == false) {
			int pickedLeft = RandomSource.nextInt(remainingLeft.size());
			int pickedRight = RandomSource.nextInt(remainingRight.size());
			Pokemon pickedLeftP = remainingLeft.remove(pickedLeft);
			Pokemon pickedRightP = remainingRight.get(pickedRight);
			while (pickedLeftP.number == pickedRightP.number
					&& remainingRight.size() != 1) {
				// Reroll for a different pokemon if at all possible
				pickedRight = RandomSource.nextInt(remainingRight.size());
				pickedRightP = remainingRight.get(pickedRight);
			}
			remainingRight.remove(pickedRight);
			translateMap.put(pickedLeftP, pickedRightP);
		}

		List<EncounterSet> currentEncounters = this.getEncounters(useTimeOfDay);

		for (EncounterSet area : currentEncounters) {
			for (Encounter enc : area.encounters) {
				// Apply the map
				enc.pokemon = translateMap.get(enc.pokemon);
			}
		}

		setEncounters(useTimeOfDay, currentEncounters);

	}

	@Override
	public void randomizeTrainerPokes(boolean rivalCarriesStarter,
			boolean usePowerLevels, boolean noLegendaries) {
		List<Trainer> currentTrainers = this.getTrainers();
		cachedReplacementLists = new TreeMap<Type, List<Pokemon>>();
		cachedAllList = noLegendaries ? allNonLegendaries()
				: allPokemonWithoutNull();

		// Fully random is easy enough - randomize then worry about rival
		// carrying starter at the end
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.equals("IRIVAL")) {
				continue; // skip
			}
			for (TrainerPokemon tp : t.pokemon) {
				tp.pokemon = pickReplacement(tp.pokemon, usePowerLevels, null,
						noLegendaries);
			}
		}

		// Rival carries starter?
		if (rivalCarriesStarter) {
			rivalCarriesStarterUpdate(currentTrainers, "RIVAL", 1);
			rivalCarriesStarterUpdate(currentTrainers, "FRIEND", 2);
		}

		// Save it all up
		this.setTrainers(currentTrainers);
	}

	@Override
	public void typeThemeTrainerPokes(boolean rivalCarriesStarter,
			boolean usePowerLevels, boolean weightByFrequency,
			boolean noLegendaries) {
		List<Trainer> currentTrainers = this.getTrainers();
		cachedReplacementLists = new TreeMap<Type, List<Pokemon>>();
		cachedAllList = noLegendaries ? allNonLegendaries()
				: allPokemonWithoutNull();
		typeWeightings = new TreeMap<Type, Integer>();
		totalTypeWeighting = 0;

		// Construct groupings for types
		// Anything starting with GYM or ELITE or CHAMPION is a group
		Set<Trainer> assignedTrainers = new TreeSet<Trainer>();
		Map<String, List<Trainer>> groups = new TreeMap<String, List<Trainer>>();
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.equals("IRIVAL")) {
				continue; // skip
			}
			String group = t.tag == null ? "" : t.tag;
			if (group.contains("-")) {
				group = group.substring(0, group.indexOf('-'));
			}
			if (group.startsWith("GYM") || group.startsWith("ELITE")
					|| group.startsWith("CHAMPION")
					|| group.startsWith("THEMED")) {
				// Yep this is a group
				if (!groups.containsKey(group)) {
					groups.put(group, new ArrayList<Trainer>());
				}
				groups.get(group).add(t);
				assignedTrainers.add(t);
			} else if (group.startsWith("GIO")) {
				// Giovanni has same grouping as his gym, gym 8
				if (!groups.containsKey("GYM8")) {
					groups.put("GYM8", new ArrayList<Trainer>());
				}
				groups.get("GYM8").add(t);
				assignedTrainers.add(t);
			}
		}

		// Give a type to each group
		// Gym & elite types have to be unique
		// So do uber types, including the type we pick for champion
		Set<Type> usedGymTypes = new TreeSet<Type>();
		Set<Type> usedEliteTypes = new TreeSet<Type>();
		Set<Type> usedUberTypes = new TreeSet<Type>();
		for (String group : groups.keySet()) {
			List<Trainer> trainersInGroup = groups.get(group);
			Type typeForGroup = pickType(weightByFrequency, noLegendaries);
			if (group.startsWith("GYM")) {
				while (usedGymTypes.contains(typeForGroup)) {
					typeForGroup = pickType(weightByFrequency, noLegendaries);
				}
				usedGymTypes.add(typeForGroup);
			}
			if (group.startsWith("ELITE")) {
				while (usedEliteTypes.contains(typeForGroup)) {
					typeForGroup = pickType(weightByFrequency, noLegendaries);
				}
				usedEliteTypes.add(typeForGroup);
			}
			if (group.equals("CHAMPION")) {
				usedUberTypes.add(typeForGroup);
			}
			// Themed groups just have a theme, no special criteria
			for (Trainer t : trainersInGroup) {
				for (TrainerPokemon tp : t.pokemon) {
					tp.pokemon = pickReplacement(tp.pokemon, usePowerLevels,
							typeForGroup, noLegendaries);
				}
			}
		}

		// Give a type to each unassigned trainer
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.equals("IRIVAL")) {
				continue; // skip
			}

			if (!assignedTrainers.contains(t)) {
				Type typeForTrainer = pickType(weightByFrequency, noLegendaries);
				// Ubers: can't have the same type as each other
				if (t.tag != null && t.tag.equals("UBER")) {
					while (usedUberTypes.contains(typeForTrainer)) {
						typeForTrainer = pickType(weightByFrequency,
								noLegendaries);
					}
					usedUberTypes.add(typeForTrainer);
				}
				for (TrainerPokemon tp : t.pokemon) {
					tp.pokemon = pickReplacement(tp.pokemon, usePowerLevels,
							typeForTrainer, noLegendaries);
				}
			}
		}

		// Rival carries starter?
		if (rivalCarriesStarter) {
			rivalCarriesStarterUpdate(currentTrainers, "RIVAL", 1);
			rivalCarriesStarterUpdate(currentTrainers, "FRIEND", 2);
		}

		// Save it all up
		this.setTrainers(currentTrainers);
	}

	@Override
	public boolean typeInGame(Type type) {
		return true;
	}

	@Override
	public void randomizeMovesLearnt(boolean typeThemed) {
		// Get current sets

		Map<Pokemon, List<MoveLearnt>> movesets = this.getMovesLearnt();
		for (Pokemon pkmn : movesets.keySet()) {
			Set<Integer> learnt = new TreeSet<Integer>();
			List<MoveLearnt> moves = movesets.get(pkmn);
			// First move should be replaced with a damaging one
			int damagingMove = pickMove(pkmn, typeThemed, true);
			moves.get(0).move = damagingMove;
			learnt.add(damagingMove);
			// Rest replace with randoms
			for (int i = 1; i < moves.size(); i++) {
				int picked = pickMove(pkmn, typeThemed, false);
				while (learnt.contains(picked)) {
					picked = pickMove(pkmn, typeThemed, false);
				}
				moves.get(i).move = picked;
				learnt.add(picked);
			}
		}
		// Done, save
		this.setMovesLearnt(movesets);

	}

	@Override
	public void fixTypeEffectiveness() {
		// DEFAULT: do nothing

	}

	@Override
	public boolean hasTimeBasedEncounters() {
		// DEFAULT: no
		return false;
	}

	@Override
	public void randomizeStaticPokemon(boolean legendForLegend) {
		// Load
		List<Pokemon> currentStaticPokemon = this.getStaticPokemon();
		List<Pokemon> replacements = new ArrayList<Pokemon>();

		if (legendForLegend) {
			for (int i = 0; i < currentStaticPokemon.size(); i++) {
				Pokemon old = currentStaticPokemon.get(i);
				Pokemon newPK = old.isLegendary() ? randomLegendaryPokemon()
						: randomNonLegendaryPokemon();
				while (replacements.contains(newPK)) {
					newPK = old.isLegendary() ? randomLegendaryPokemon()
							: randomNonLegendaryPokemon();
				}
				replacements.add(newPK);
			}
		} else {
			for (int i = 0; i < currentStaticPokemon.size(); i++) {
				Pokemon newPK = randomPokemon();
				while (replacements.contains(newPK)) {
					newPK = randomPokemon();
				}
				replacements.add(newPK);
			}
		}

		// Save
		this.setStaticPokemon(replacements);
	}

	@Override
	public void randomizeTMMoves() {
		// Pick some random TM moves.
		int tmCount = this.getTMCount();
		List<Move> allMoves = this.getMoves();
		List<Integer> newTMs = new ArrayList<Integer>();
		for (int i = 0; i < tmCount; i++) {
			int chosenMove = RandomSource.nextInt(allMoves.size() - 1) + 1;
			while (newTMs.contains(chosenMove)
					|| RomFunctions.bannedRandomMoves[chosenMove]) {
				chosenMove = RandomSource.nextInt(allMoves.size() - 1) + 1;
			}
			newTMs.add(chosenMove);
		}
		this.setTMMoves(newTMs);
	}

	@Override
	public void randomizeTMHMCompatibility(boolean preferSameType) {
		// Get current compatibility
		Map<Pokemon, boolean[]> compat = this.getTMHMCompatibility();
		List<Integer> tmHMs = new ArrayList<Integer>(this.getTMMoves());
		tmHMs.addAll(this.getHMMoves());
		List<Move> moveData = this.getMoves();
		for (Map.Entry<Pokemon, boolean[]> compatEntry : compat.entrySet()) {
			Pokemon pkmn = compatEntry.getKey();
			boolean[] flags = compatEntry.getValue();
			for (int i = 1; i <= tmHMs.size(); i++) {
				int move = tmHMs.get(i - 1);
				Move mv = moveData.get(move);
				double probability = 0.5;
				if (preferSameType) {
					if (pkmn.primaryType.equals(mv.type)
							|| (pkmn.secondaryType != null && pkmn.secondaryType
									.equals(mv.type))) {
						probability = 0.9;
					} else if (mv.type != null && mv.type.equals(Type.NORMAL)) {
						probability = 0.5;
					} else {
						probability = 0.25;
					}
				}
				flags[i] = (RandomSource.random() < probability);
			}
		}

		// Set the new compatibility
		this.setTMHMCompatibility(compat);
	}

	@SuppressWarnings("unchecked")
	private static List<String>[] allTrainerNames = new List[] {
			new ArrayList<String>(), new ArrayList<String>() };
	@SuppressWarnings("unchecked")
	private static Map<Integer, List<String>> trainerNamesByLength[] = new Map[] {
			new TreeMap<Integer, List<String>>(),
			new TreeMap<Integer, List<String>>() };
	private static boolean trainerNamesInited = false;

	@Override
	public void randomizeTrainerNames() {
		if (!trainerNamesInited) {
			trainerNamesInited = true;
			String tnamesFile = "trainernames.txt";
			// Check for the file
			if (FileFunctions.configExists(tnamesFile)) {
				try {
					Scanner sc = new Scanner(
							FileFunctions.openConfig(tnamesFile), "UTF-8");
					while (sc.hasNextLine()) {
						String trainername = sc.nextLine().trim();
						if (trainername.isEmpty()) {
							continue;
						}
						int idx = trainername.contains("&") ? 1 : 0;
						allTrainerNames[idx].add(trainername);
						int len = trainername.length();
						if (trainerNamesByLength[idx].containsKey(len)) {
							trainerNamesByLength[idx].get(len).add(trainername);
						} else {
							List<String> namesOfThisLength = new ArrayList<String>();
							namesOfThisLength.add(trainername);
							trainerNamesByLength[idx].put(len,
									namesOfThisLength);
						}
					}
					sc.close();
				} catch (FileNotFoundException e) {
					// Can't read, just don't load anything
				}
			}
		}

		// Get the current trainer names data
		List<String> currentTrainerNames = this.getTrainerNames();
		if (currentTrainerNames.size() == 0) {
			// RBY have no trainer names
			return;
		}
		boolean mustBeSameLength = this.fixedTrainerNamesLength();
		int maxLength = this.maxTrainerNameLength();

		// Init the translation map and new list
		Map<String, String> translation = new HashMap<String, String>();
		List<String> newTrainerNames = new ArrayList<String>();

		// Start choosing
		for (String trainerName : currentTrainerNames) {
			if (translation.containsKey(trainerName)
					&& trainerName.equalsIgnoreCase("GRUNT") == false
					&& trainerName.equalsIgnoreCase("EXECUTIVE") == false) {
				// use an already picked translation
				newTrainerNames.add(translation.get(trainerName));
			} else {
				int idx = trainerName.contains("&") ? 1 : 0;
				List<String> pickFrom = allTrainerNames[idx];
				if (mustBeSameLength) {
					pickFrom = trainerNamesByLength[idx].get(trainerName
							.length());
				}
				String changeTo = trainerName;
				if (pickFrom != null && pickFrom.size() > 0
						&& trainerName.length() > 1) {
					changeTo = pickFrom.get(RandomSource.nextInt(pickFrom
							.size()));
					while (changeTo.length() > maxLength) {
						changeTo = pickFrom.get(RandomSource.nextInt(pickFrom
								.size()));
					}
				}
				translation.put(trainerName, changeTo);
				newTrainerNames.add(changeTo);
			}
		}

		// Done choosing, save
		this.setTrainerNames(newTrainerNames);
	}

	@Override
	public int maxTrainerNameLength() {
		// default: no real limit
		return Integer.MAX_VALUE;
	}

	@SuppressWarnings("unchecked")
	private static List<String> allTrainerClasses[] = new List[] {
			new ArrayList<String>(), new ArrayList<String>() };
	@SuppressWarnings("unchecked")
	private static Map<Integer, List<String>> trainerClassesByLength[] = new Map[] {
			new HashMap<Integer, List<String>>(),
			new HashMap<Integer, List<String>>() };
	private static boolean trainerClassesInited = false;

	@Override
	public void randomizeTrainerClassNames() {
		if (!trainerClassesInited) {
			trainerClassesInited = true;
			String tclassesFile = "trainerclasses.txt";
			// Check for the file
			if (FileFunctions.configExists(tclassesFile)) {
				try {
					Scanner sc = new Scanner(
							FileFunctions.openConfig(tclassesFile), "UTF-8");
					while (sc.hasNextLine()) {
						String trainerClassName = sc.nextLine().trim();
						if (trainerClassName.isEmpty()) {
							continue;
						}
						String checkName = trainerClassName.toLowerCase();
						int idx = (checkName.endsWith("couple")
								|| checkName.contains(" and ")
								|| checkName.endsWith("kin")
								|| checkName.endsWith("team")
								|| checkName.contains(" & ") || (checkName
								.endsWith("s") && !checkName.endsWith("ss"))) ? 1
								: 0;
						allTrainerClasses[idx].add(trainerClassName);
						int len = trainerClassName.length();
						if (trainerClassesByLength[idx].containsKey(len)) {
							trainerClassesByLength[idx].get(len).add(
									trainerClassName);
						} else {
							List<String> namesOfThisLength = new ArrayList<String>();
							namesOfThisLength.add(trainerClassName);
							trainerClassesByLength[idx].put(len,
									namesOfThisLength);
						}
					}
					sc.close();
				} catch (FileNotFoundException e) {
					// Can't read, just don't load anything
				}
			}
		}

		// Get the current trainer names data
		List<String> currentClassNames = this.getTrainerClassNames();
		boolean mustBeSameLength = this.fixedTrainerClassNamesLength();
		int maxLength = this.maxTrainerClassNameLength();

		// Init the translation map and new list
		Map<String, String> translation = new HashMap<String, String>();
		List<String> newClassNames = new ArrayList<String>();

		// Start choosing
		for (String trainerClassName : currentClassNames) {
			if (translation.containsKey(trainerClassName)) {
				// use an already picked translation
				newClassNames.add(translation.get(trainerClassName));
			} else {
				String checkName = trainerClassName.toLowerCase();
				int idx = (checkName.endsWith("couple")
						|| checkName.contains(" and ")
						|| checkName.endsWith("kin")
						|| checkName.endsWith("team")
						|| checkName.contains(" & ") || (checkName
						.endsWith("s") && !checkName.endsWith("ss"))) ? 1 : 0;
				List<String> pickFrom = allTrainerClasses[idx];
				if (mustBeSameLength) {
					pickFrom = trainerClassesByLength[idx].get(trainerClassName
							.length());
				}
				String changeTo = trainerClassName;
				if (pickFrom != null && pickFrom.size() > 0) {
					changeTo = pickFrom.get(RandomSource.nextInt(pickFrom
							.size()));
					while (changeTo.length() > maxLength) {
						changeTo = pickFrom.get(RandomSource.nextInt(pickFrom
								.size()));
					}
				}
				translation.put(trainerClassName, changeTo);
				newClassNames.add(changeTo);
			}
		}

		// Done choosing, save
		this.setTrainerClassNames(newClassNames);
	}

	@Override
	public int maxTrainerClassNameLength() {
		// default: no real limit
		return Integer.MAX_VALUE;
	}

	private int pickMove(Pokemon pkmn, boolean typeThemed, boolean damaging) {
		// If damaging, we want a move with at least 80% accuracy and 2 power
		List<Move> allMoves = this.getMoves();
		Type typeOfMove = null;
		double picked = RandomSource.random();
		// Type?
		if (typeThemed) {
			if (pkmn.primaryType == Type.NORMAL
					|| pkmn.secondaryType == Type.NORMAL) {
				if (pkmn.secondaryType == null) {
					// Pure NORMAL: 75% normal, 25% random
					if (picked < 0.75) {
						typeOfMove = Type.NORMAL;
					}
					// else random
				} else {
					// Find the other type
					// Normal/OTHER: 30% normal, 55% other, 15% random
					Type otherType = pkmn.primaryType;
					if (otherType == Type.NORMAL) {
						otherType = pkmn.secondaryType;
					}
					if (picked < 0.3) {
						typeOfMove = Type.NORMAL;
					} else if (picked < 0.85) {
						typeOfMove = otherType;
					}
					// else random
				}
			} else if (pkmn.secondaryType != null) {
				// Primary/Secondary: 50% primary, 30% secondary, 5% normal, 15%
				// random
				if (picked < 0.5) {
					typeOfMove = pkmn.primaryType;
				} else if (picked < 0.8) {
					typeOfMove = pkmn.secondaryType;
				} else if (picked < 0.85) {
					typeOfMove = Type.NORMAL;
				}
				// else random
			} else {
				// Primary/None: 60% primary, 20% normal, 20% random
				if (picked < 0.6) {
					typeOfMove = pkmn.primaryType;
				} else if (picked < 0.8) {
					typeOfMove = Type.NORMAL;
				}
				// else random
			}
		}
		// Filter by type, and if necessary, by damage
		List<Move> canPick = new ArrayList<Move>();
		for (Move mv : allMoves) {
			if (mv != null && !RomFunctions.bannedRandomMoves[mv.number]
					&& (mv.type == typeOfMove || typeOfMove == null)) {
				if (!damaging
						|| (mv.power > 1 && mv.hitratio > 79 && !RomFunctions.bannedForDamagingMove[mv.number])) {
					canPick.add(mv);
				}
			}
		}
		// If we ended up with no results, reroll
		if (canPick.size() == 0) {
			return pickMove(pkmn, typeThemed, damaging);
		} else {
			// pick a random one
			return canPick.get(RandomSource.nextInt(canPick.size())).number;
		}
	}

	private List<Pokemon> pokemonOfType(Type type, boolean noLegendaries) {
		List<Pokemon> allPokes = this.getPokemon();
		List<Pokemon> typedPokes = new ArrayList<Pokemon>();
		for (Pokemon pk : allPokes) {
			if (pk != null && (!noLegendaries || !pk.isLegendary())) {
				if (pk.primaryType == type || pk.secondaryType == type) {
					typedPokes.add(pk);
				}
			}
		}
		return typedPokes;
	}

	private List<Pokemon> allPokemonWithoutNull() {
		List<Pokemon> allPokes = new ArrayList<Pokemon>(this.getPokemon());
		allPokes.remove(0);
		return allPokes;
	}

	private List<Pokemon> allNonLegendaries() {
		List<Pokemon> allPokes = allPokemonWithoutNull();
		Set<Pokemon> toRemove = new TreeSet<Pokemon>();
		for (Pokemon pkmn : allPokes) {
			if (pkmn.isLegendary()) {
				toRemove.add(pkmn);
			}
		}
		allPokes.removeAll(toRemove);
		return allPokes;
	}

	private Set<Pokemon> pokemonInArea(EncounterSet area) {
		Set<Pokemon> inArea = new TreeSet<Pokemon>();
		for (Encounter enc : area.encounters) {
			inArea.add(enc.pokemon);
		}
		return inArea;
	}

	private Map<Type, Integer> typeWeightings;
	private int totalTypeWeighting;

	private Type pickType(boolean weightByFrequency, boolean noLegendaries) {
		if (totalTypeWeighting == 0) {
			// Determine weightings
			for (Type t : Type.values()) {
				if (typeInGame(t)) {
					int pkWithTyping = pokemonOfType(t, noLegendaries).size();
					typeWeightings.put(t, pkWithTyping);
					totalTypeWeighting += pkWithTyping;
				}
			}
		}

		if (weightByFrequency) {
			int typePick = RandomSource.nextInt(totalTypeWeighting);
			int typePos = 0;
			for (Type t : typeWeightings.keySet()) {
				int weight = typeWeightings.get(t);
				if (typePos + weight > typePick) {
					return t;
				}
				typePos += weight;
			}
			return null;
		} else {
			return randomType();
		}
	}

	private void rivalCarriesStarterUpdate(List<Trainer> currentTrainers,
			String prefix, int pokemonOffset) {
		// Find the highest rival battle #
		int highestRivalNum = 0;
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.startsWith(prefix)) {
				highestRivalNum = Math.max(
						highestRivalNum,
						Integer.parseInt(t.tag.substring(prefix.length(),
								t.tag.indexOf('-'))));
			}
		}

		if (highestRivalNum == 0) {
			// This rival type not used in this game
			return;
		}

		// Get the starters
		// us 0 1 2 => them 0+n 1+n 2+n
		List<Pokemon> starters = this.getStarters();

		// Yellow needs its own case, unfortunately.
		if (isYellow()) {
			// The rival's starter is index 1
			Pokemon rivalStarter = starters.get(1);
			int timesEvolves = timesEvolves(rivalStarter);
			// Apply evolutions as appropriate
			if (timesEvolves == 0) {
				for (int j = 1; j <= 3; j++) {
					changeStarterWithTag(currentTrainers, prefix + j + "-0",
							rivalStarter);
				}
				for (int j = 4; j <= 7; j++) {
					for (int i = 0; i < 3; i++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, rivalStarter);
					}
				}
			} else if (timesEvolves == 1) {
				for (int j = 1; j <= 3; j++) {
					changeStarterWithTag(currentTrainers, prefix + j + "-0",
							rivalStarter);
				}
				rivalStarter = firstEvolution(rivalStarter);
				for (int j = 4; j <= 7; j++) {
					for (int i = 0; i < 3; i++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, rivalStarter);
					}
				}
			} else if (timesEvolves == 2) {
				for (int j = 1; j <= 2; j++) {
					changeStarterWithTag(currentTrainers, prefix + j + "-" + 0,
							rivalStarter);
				}
				rivalStarter = firstEvolution(rivalStarter);
				changeStarterWithTag(currentTrainers, prefix + "3-0",
						rivalStarter);
				for (int i = 0; i < 3; i++) {
					changeStarterWithTag(currentTrainers, prefix + "4-" + i,
							rivalStarter);
				}
				rivalStarter = firstEvolution(rivalStarter);
				for (int j = 5; j <= 7; j++) {
					for (int i = 0; i < 3; i++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, rivalStarter);
					}
				}
			}
		} else {
			// Replace each starter as appropriate
			// Use level to determine when to evolve, not number anymore
			for (int i = 0; i < 3; i++) {
				// Rival's starters are pokemonOffset over from each of ours
				int starterToUse = (i + pokemonOffset) % 3;
				Pokemon thisStarter = starters.get(starterToUse);
				int timesEvolves = timesEvolves(thisStarter);
				// If a fully evolved pokemon, use throughout
				// Otherwise split by evolutions as appropriate
				if (timesEvolves == 0) {
					for (int j = 1; j <= highestRivalNum; j++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
				} else if (timesEvolves == 1) {
					int j = 1;
					for (; j <= highestRivalNum / 2; j++) {
						if (getLevelOfStarter(currentTrainers, prefix + j + "-"
								+ i) >= 28) {
							break;
						}
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
					thisStarter = firstEvolution(thisStarter);
					for (; j <= highestRivalNum; j++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
				} else if (timesEvolves == 2) {
					int j = 1;
					for (; j <= highestRivalNum; j++) {
						if (getLevelOfStarter(currentTrainers, prefix + j + "-"
								+ i) >= 18) {
							break;
						}
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
					thisStarter = firstEvolution(thisStarter);
					for (; j <= highestRivalNum; j++) {
						if (getLevelOfStarter(currentTrainers, prefix + j + "-"
								+ i) >= 36) {
							break;
						}
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
					thisStarter = firstEvolution(thisStarter);
					for (; j <= highestRivalNum; j++) {
						changeStarterWithTag(currentTrainers, prefix + j + "-"
								+ i, thisStarter);
					}
				}
			}
		}

	}

	private int getLevelOfStarter(List<Trainer> currentTrainers, String tag) {
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.equals(tag)) {
				// Bingo, get highest level
				// If it's tagged the same we can assume it's the same team
				// just the opposite gender or something like that...
				// So no need to check other trainers with same tag.
				int highestLevel = t.pokemon.get(0).level;
				for (int i = 1; i < t.pokemon.size(); i++) {
					if (t.pokemon.get(i).level > highestLevel) {
						highestLevel = t.pokemon.get(i).level;
					}
				}
				return highestLevel;
			}
		}
		return 0;
	}

	private void changeStarterWithTag(List<Trainer> currentTrainers,
			String tag, Pokemon starter) {
		for (Trainer t : currentTrainers) {
			if (t.tag != null && t.tag.equals(tag)) {
				// Bingo
				// CHANGE: change the highest level pokemon, not the last.
				TrainerPokemon bestPoke = t.pokemon.get(0);
				for (int i = 1; i < t.pokemon.size(); i++) {
					if (t.pokemon.get(i).level > bestPoke.level) {
						bestPoke = t.pokemon.get(i);
					}
				}
				bestPoke.pokemon = starter;
			}
		}

	}

	private int timesEvolves(Pokemon pk) {
		// This method works ASSUMING a pokemon has no weird split evolutions
		// with different levels on each side
		// Which is true for every pokemon so far.
		List<Evolution> evos = this.getEvolutions();
		List<Pokemon> pokes = this.getPokemon();
		for (Evolution e : evos) {
			if (e.from == pk.number) {
				return timesEvolves(pokes.get(e.to)) + 1;
			}
		}
		return 0;
	}

	private Pokemon firstEvolution(Pokemon pk) {
		List<Evolution> evos = this.getEvolutions();
		List<Pokemon> pokes = this.getPokemon();
		for (Evolution e : evos) {
			if (e.from == pk.number) {
				return pokes.get(e.to);
			}
		}
		return null;
	}

	private Map<Type, List<Pokemon>> cachedReplacementLists;
	private List<Pokemon> cachedAllList;

	private Pokemon pickReplacement(Pokemon current, boolean usePowerLevels,
			Type type, boolean noLegendaries) {
		List<Pokemon> pickFrom = cachedAllList;
		if (type != null) {
			if (!cachedReplacementLists.containsKey(type)) {
				cachedReplacementLists.put(type,
						pokemonOfType(type, noLegendaries));
			}
			pickFrom = cachedReplacementLists.get(type);
		}

		if (usePowerLevels) {
			// start with within 10% and add 5% either direction till we find
			// something
			int currentBST = current.bstForPowerLevels();
			int minTarget = currentBST - currentBST / 10;
			int maxTarget = currentBST + currentBST / 10;
			List<Pokemon> canPick = new ArrayList<Pokemon>();
			while (canPick.isEmpty()) {
				for (Pokemon pk : pickFrom) {
					if (pk.bstForPowerLevels() >= minTarget
							&& pk.bstForPowerLevels() <= maxTarget) {
						canPick.add(pk);
					}
				}
				minTarget -= currentBST / 20;
				maxTarget += currentBST / 20;
			}
			return canPick.get(RandomSource.nextInt(canPick.size()));
		} else {
			return pickFrom.get(RandomSource.nextInt(pickFrom.size()));
		}
	}

	@Override
	public boolean isYellow() {
		return false;
	}

	@Override
	public void patchForNationalDex() {
		// Default: Do Nothing.

	}

	@Override
	public boolean canChangeStarters() {
		return true;
	}

	protected void log(String log) {
		RandomizerGUI.verboseLog.println(log);
	}

	protected void logBlankLine() {
		RandomizerGUI.verboseLog.println();
	}

}
