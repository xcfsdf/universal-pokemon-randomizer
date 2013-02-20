package com.dabomstew.pkrandom;

/*----------------------------------------------------------------------------*/
/*--  RomFunctions.java - contains functions useful throughout the program.	--*/
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.dabomstew.pkrandom.pokemon.Evolution;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.romhandlers.RomHandler;

public class RomFunctions {

	public static final String[] moveNames = new String[] { "", "Pound",
			"Karate Chop", "DoubleSlap", "Comet Punch", "Mega Punch",
			"Pay Day", "Fire Punch", "Ice Punch", "ThunderPunch", "Scratch",
			"ViceGrip", "Guillotine", "Razor Wind", "Swords Dance", "Cut",
			"Gust", "Wing Attack", "Whirlwind", "Fly", "Bind", "Slam",
			"Vine Whip", "Stomp", "Double Kick", "Mega Kick", "Jump Kick",
			"Rolling Kick", "Sand-Attack", "Headbutt", "Horn Attack",
			"Fury Attack", "Horn Drill", "Tackle", "Body Slam", "Wrap",
			"Take Down", "Thrash", "Double-Edge", "Tail Whip", "Poison Sting",
			"Twineedle", "Pin Missile", "Leer", "Bite", "Growl", "Roar",
			"Sing", "Supersonic", "SonicBoom", "Disable", "Acid", "Ember",
			"Flamethrower", "Mist", "Water Gun", "Hydro Pump", "Surf",
			"Ice Beam", "Blizzard", "Psybeam", "BubbleBeam", "Aurora Beam",
			"Hyper Beam", "Peck", "Drill Peck", "Submission", "Low Kick",
			"Counter", "Seismic Toss", "Strength", "Absorb", "Mega Drain",
			"Leech Seed", "Growth", "Razor Leaf", "SolarBeam", "PoisonPowder",
			"Stun Spore", "Sleep Powder", "Petal Dance", "String Shot",
			"Dragon Rage", "Fire Spin", "ThunderShock", "Thunderbolt",
			"Thunder Wave", "Thunder", "Rock Throw", "Earthquake", "Fissure",
			"Dig", "Toxic", "Confusion", "Psychic", "Hypnosis", "Meditate",
			"Agility", "Quick Attack", "Rage", "Teleport", "Night Shade",
			"Mimic", "Screech", "Double Team", "Recover", "Harden", "Minimize",
			"SmokeScreen", "Confuse Ray", "Withdraw", "Defense Curl",
			"Barrier", "Light Screen", "Haze", "Reflect", "Focus Energy",
			"Bide", "Metronome", "Mirror Move", "Selfdestruct", "Egg Bomb",
			"Lick", "Smog", "Sludge", "Bone Club", "Fire Blast", "Waterfall",
			"Clamp", "Swift", "Skull Bash", "Spike Cannon", "Constrict",
			"Amnesia", "Kinesis", "Softboiled", "Hi Jump Kick", "Glare",
			"Dream Eater", "Poison Gas", "Barrage", "Leech Life",
			"Lovely Kiss", "Sky Attack", "Transform", "Bubble", "Dizzy Punch",
			"Spore", "Flash", "Psywave", "Splash", "Acid Armor", "Crabhammer",
			"Explosion", "Fury Swipes", "Bonemerang", "Rest", "Rock Slide",
			"Hyper Fang", "Sharpen", "Conversion", "Tri Attack", "Super Fang",
			"Slash", "Substitute", "Struggle", "Sketch", "Triple Kick",
			"Thief", "Spider Web", "Mind Reader", "Nightmare", "Flame Wheel",
			"Snore", "Curse", "Flail", "Conversion 2", "Aeroblast",
			"Cotton Spore", "Reversal", "Spite", "Powder Snow", "Protect",
			"Mach Punch", "Scary Face", "Faint Attack", "Sweet Kiss",
			"Belly Drum", "Sludge Bomb", "Mud-Slap", "Octazooka", "Spikes",
			"Zap Cannon", "Foresight", "Destiny Bond", "Perish Song",
			"Icy Wind", "Detect", "Bone Rush", "Lock-On", "Outrage",
			"Sandstorm", "Giga Drain", "Endure", "Charm", "Rollout",
			"False Swipe", "Swagger", "Milk Drink", "Spark", "Fury Cutter",
			"Steel Wing", "Mean Look", "Attract", "Sleep Talk", "Heal Bell",
			"Return", "Present", "Frustration", "Safeguard", "Pain Split",
			"Sacred Fire", "Magnitude", "DynamicPunch", "Megahorn",
			"DragonBreath", "Baton Pass", "Encore", "Pursuit", "Rapid Spin",
			"Sweet Scent", "Iron Tail", "Metal Claw", "Vital Throw",
			"Morning Sun", "Synthesis", "Moonlight", "Hidden Power",
			"Cross Chop", "Twister", "Rain Dance", "Sunny Day", "Crunch",
			"Mirror Coat", "Psych Up", "ExtremeSpeed", "AncientPower",
			"Shadow Ball", "Future Sight", "Rock Smash", "Whirlpool",
			"Beat Up", "Fake Out", "Uproar", "Stockpile", "Spit Up", "Swallow",
			"Heat Wave", "Hail", "Torment", "Flatter", "Will-O-Wisp",
			"Memento", "Facade", "Focus Punch", "SmellingSalt", "Follow Me",
			"Nature Power", "Charge", "Taunt", "Helping Hand", "Trick",
			"Role Play", "Wish", "Assist", "Ingrain", "Superpower",
			"Magic Coat", "Recycle", "Revenge", "Brick Break", "Yawn",
			"Knock Off", "Endeavor", "Eruption", "Skill Swap", "Imprison",
			"Refresh", "Grudge", "Snatch", "Secret Power", "Dive",
			"Arm Thrust", "Camouflage", "Tail Glow", "Luster Purge",
			"Mist Ball", "FeatherDance", "Teeter Dance", "Blaze Kick",
			"Mud Sport", "Ice Ball", "Needle Arm", "Slack Off", "Hyper Voice",
			"Poison Fang", "Crush Claw", "Blast Burn", "Hydro Cannon",
			"Meteor Mash", "Astonish", "Weather Ball", "Aromatherapy",
			"Fake Tears", "Air Cutter", "Overheat", "Odor Sleuth", "Rock Tomb",
			"Silver Wind", "Metal Sound", "GrassWhistle", "Tickle",
			"Cosmic Power", "Water Spout", "Signal Beam", "Shadow Punch",
			"Extrasensory", "Sky Uppercut", "Sand Tomb", "Sheer Cold",
			"Muddy Water", "Bullet Seed", "Aerial Ace", "Icicle Spear",
			"Iron Defense", "Block", "Howl", "Dragon Claw", "Frenzy Plant",
			"Bulk Up", "Bounce", "Mud Shot", "Poison Tail", "Covet",
			"Volt Tackle", "Magical Leaf", "Water Sport", "Calm Mind",
			"Leaf Blade", "Dragon Dance", "Rock Blast", "Shock Wave",
			"Water Pulse", "Doom Desire", "Psycho Boost", "Roost", "Gravity",
			"Miracle Eye", "Wake-Up Slap", "Hammer Arm", "Gyro Ball",
			"Healing Wish", "Brine", "Natural Gift", "Feint", "Pluck",
			"Tailwind", "Acupressure", "Metal Burst", "U-turn", "Close Combat",
			"Payback", "Assurance", "Embargo", "Fling", "Psycho Shift",
			"Trump Card", "Heal Block", "Wring Out", "Power Trick",
			"Gastro Acid", "Lucky Chant", "Me First", "Copycat", "Power Swap",
			"Guard Swap", "Punishment", "Last Resort", "Worry Seed",
			"Sucker Punch", "Toxic Spikes", "Heart Swap", "Aqua Ring",
			"Magnet Rise", "Flare Blitz", "Force Palm", "Aura Sphere",
			"Rock Polish", "Poison Jab", "Dark Pulse", "Night Slash",
			"Aqua Tail", "Seed Bomb", "Air Slash", "X-Scissor", "Bug Buzz",
			"Dragon Pulse", "Dragon Rush", "Power Gem", "Drain Punch",
			"Vacuum Wave", "Focus Blast", "Energy Ball", "Brave Bird",
			"Earth Power", "Switcheroo", "Giga Impact", "Nasty Plot",
			"Bullet Punch", "Avalanche", "Ice Shard", "Shadow Claw",
			"Thunder Fang", "Ice Fang", "Fire Fang", "Shadow Sneak",
			"Mud Bomb", "Psycho Cut", "Zen Headbutt", "Mirror Shot",
			"Flash Cannon", "Rock Climb", "Defog", "Trick Room",
			"Draco Meteor", "Discharge", "Lava Plume", "Leaf Storm",
			"Power Whip", "Rock Wrecker", "Cross Poison", "Gunk Shot",
			"Iron Head", "Magnet Bomb", "Stone Edge", "Captivate",
			"Stealth Rock", "Grass Knot", "Chatter", "Judgment", "Bug Bite",
			"Charge Beam", "Wood Hammer", "Aqua Jet", "Attack Order",
			"Defend Order", "Heal Order", "Head Smash", "Double Hit",
			"Roar of Time", "Spacial Rend", "Lunar Dance", "Crush Grip",
			"Magma Storm", "Dark Void", "Seed Flare", "Ominous Wind",
			"Shadow Force", "Hone Claws", "Wide Guard", "Guard Split",
			"Power Split", "Wonder Room", "Psyshock", "Venoshock",
			"Autotomize", "Rage Powder", "Telekinesis", "Magic Room",
			"Smack Down", "Storm Throw", "Flame Burst", "Sludge Wave",
			"Quiver Dance", "Heavy Slam", "Synchronoise", "Electro Ball",
			"Soak", "Flame Charge", "Coil", "Low Sweep", "Acid Spray",
			"Foul Play", "Simple Beam", "Entrainment", "After You", "Round",
			"Echoed Voice", "Chip Away", "Clear Smog", "Stored Power",
			"Quick Guard", "Ally Switch", "Scald", "Shell Smash", "Heal Pulse",
			"Hex", "Sky Drop", "Shift Gear", "Circle Throw", "Incinerate",
			"Quash", "Acrobatics", "Reflect Type", "Retaliate", "Final Gambit",
			"Bestow", "Inferno", "Water Pledge", "Fire Pledge", "Grass Pledge",
			"Volt Switch", "Struggle Bug", "Bulldoze", "Frost Breath",
			"Dragon Tail", "Work Up", "Electroweb", "Wild Charge", "Drill Run",
			"Dual Chop", "Heart Stamp", "Horn Leech", "Sacred Sword",
			"Razor Shell", "Heat Crash", "Leaf Tornado", "Steamroller",
			"Cotton Guard", "Night Daze", "Psystrike", "Tail Slap",
			"Hurricane", "Head Charge", "Gear Grind", "Searing Shot",
			"Techno Blast", "Relic Song", "Secret Sword", "Glaciate",
			"Bolt Strike", "Blue Flare", "Fiery Dance", "Freeze Shock",
			"Ice Burn", "Snarl", "Icicle Crash", "V-create", "Fusion Flare",
			"Fusion Bolt" };

	public static final String[] abilityNames = new String[] { "N/A", "Stench",
			"Drizzle", "Speed Boost", "Battle Armor", "Sturdy", "Damp",
			"Limber", "Sand Veil", "Static", "Volt Absorb", "Water Absorb",
			"Oblivious", "Cloud Nine", "Compoundeyes", "Insomnia",
			"Color Change", "Immunity", "Flash Fire", "Shield Dust",
			"Own Tempo", "Suction Cups", "Intimidate", "Shadow Tag",
			"Rough Skin", "Wonder Guard", "Levitate", "Effect Spore",
			"Synchronize", "Clear Body", "Natural Cure", "Lightningrod",
			"Serene Grace", "Swift Swim", "Chlorophyll", "Illuminate", "Trace",
			"Huge Power", "Poison Point", "Inner Focus", "Magma Armor",
			"Water Veil", "Magnet Pull", "Soundproof", "Rain Dish",
			"Sand Stream", "Pressure", "Thick Fat", "Early Bird", "Flame Body",
			"Run Away", "Keen Eye", "Hyper Cutter", "Pickup", "Truant",
			"Hustle", "Cute Charm", "Plus", "Minus", "Forecast", "Sticky Hold",
			"Shed Skin", "Guts", "Marvel Scale", "Liquid Ooze", "Overgrow",
			"Blaze", "Torrent", "Swarm", "Rock Head", "Drought", "Arena Trap",
			"Vital Spirit", "White Smoke", "Pure Power", "Shell Armor",
			"Air Lock", "Tangled Feet", "Motor Drive", "Rivalry", "Steadfast",
			"Snow Cloak", "Gluttony", "Anger Point", "Unburden", "Heatproof",
			"Simple", "Dry Skin", "Download", "Iron Fist", "Poison Heal",
			"Adaptability", "Skill Link", "Hydration", "Solar Power",
			"Quick Feet", "Normalize", "Sniper", "Magic Guard", "No Guard",
			"Stall", "Technician", "Leaf Guard", "Klutz", "Mold Breaker",
			"Super Luck", "Aftermath", "Anticipation", "Forewarn", "Unaware",
			"Tinted Lens", "Filter", "Slow Start", "Scrappy", "Storm Drain",
			"Ice Body", "Solid Rock", "Snow Warning", "Honey Gather", "Frisk",
			"Reckless", "Multitype", "Flower Gift", "Bad Dreams", "Pickpocket",
			"Sheer Force", "Contrary", "Unnerve", "Defiant", "Defeatist",
			"Cursed Body", "Healer", "Friend Guard", "Weak Armor",
			"Heavy Metal", "Light Metal", "Multiscale", "Toxic Boost",
			"Flare Boost", "Harvest", "Telepathy", "Moody", "Overcoat",
			"Poison Touch", "Regenerator", "Big Pecks", "Sand Rush",
			"Wonder Skin", "Analytic", "Illusion", "Imposter", "Infiltrator",
			"Mummy", "Moxie", "Justified", "Rattled", "Magic Bounce",
			"Sap Sipper", "Prankster", "Sand Force", "Iron Barbs", "Zen Mode",
			"Victory Star", "Turboblaze", "Teravolt" };

	public static String[][] itemNames = new String[][] {
			{ "", "Master Ball", "Ultra Ball", "Great Ball", "Poké Ball",
					"Town Map", "Bicycle", "?????", "Safari Ball", "Pokédex",
					"Moon Stone", "Antidote", "Burn Heal", "Ice Heal",
					"Awakening", "Parlyz Heal", "Full Restore", "Max Potion",
					"Hyper Potion", "Super Potion", "Potion", "BoulderBadge",
					"CascadeBadge", "ThunderBadge", "RainbowBadge",
					"SoulBadge", "MarshBadge", "VolcanoBadge", "EarthBadge",
					"Escape Rope", "Repel", "Old Amber", "Fire Stone",
					"Thunderstone", "Water Stone", "HP Up", "Protein", "Iron",
					"Carbos", "Calcium", "Rare Candy", "Dome Fossil",
					"Helix Fossil", "Secret Key", "?????", "Bike Voucher",
					"X Accuracy", "Leaf Stone", "Card Key", "Nugget", "PP Up",
					"Poké Doll", "Full Heal", "Revive", "Max Revive",
					"Guard Spec.", "Super Repel", "Max Repel", "Dire Hit",
					"Coin", "Fresh Water", "Soda Pop", "Lemonade",
					"S.S. Ticket", "Gold Teeth", "X Attack", "X Defend",
					"X Speed", "X Special", "Coin Case", "Oak's Parcel",
					"Itemfinder", "Silph Scope", "Poké Flute", "Lift Key",
					"Exp. All", "Old Rod", "Good Rod", "Super Rod", "PP Up",
					"Ether", "Max Ether", "Elixir", "Max Elixir", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "HM01", "HM02", "HM03", "HM04", "HM05",
					"TM01", "TM02", "TM03", "TM04", "TM05", "TM06", "TM07",
					"TM08", "TM09", "TM10", "TM11", "TM12", "TM13", "TM14",
					"TM15", "TM16", "TM17", "TM18", "TM19", "TM20", "TM21",
					"TM22", "TM23", "TM24", "TM25", "TM26", "TM27", "TM28",
					"TM29", "TM30", "TM31", "TM32", "TM33", "TM34", "TM35",
					"TM36", "TM37", "TM38", "TM39", "TM40", "TM41", "TM42",
					"TM43", "TM44", "TM45", "TM46", "TM47", "TM48", "TM49",
					"TM50", "TM51", "TM52", "TM53", "TM54", "TM55", },
			{ "", "Master Ball", "Ultra Ball", "BrightPowder", "Great Ball",
					"Poké Ball", "Bicycle", "Teru-sama", "Moon Stone",
					"Antidote", "Burn Heal", "Ice Heal", "Awakening",
					"Parlyz Heal", "Full Restore", "Max Potion",
					"Hyper Potion", "Super Potion", "Potion", "Escape Rope",
					"Repel", "Max Elixer", "Fire Stone", "Thunderstone",
					"Water Stone", "Teru-sama", "HP Up", "Protein", "Iron",
					"Carbos", "Lucky Punch", "Calcium", "Rare Candy",
					"X Accuracy", "Leaf Stone", "Metal Powder", "Nugget",
					"Poké Doll", "Full Heal", "Revive", "Max Revive",
					"Guard Spec.", "Super Repel", "Max Repel", "Dire Hit",
					"Teru-sama", "Fresh Water", "Soda Pop", "Lemonade",
					"X Attack", "Teru-sama", "X Defend", "X Speed",
					"X Special", "Coin Case", "Itemfinder", "Teru-sama",
					"Exp Share", "Old Rod", "Good Rod", "Silver Leaf",
					"Super Rod", "PP Up", "Ether", "Max Ether", "Elixer",
					"Red Scale", "SecretPotion", "S.S. Ticket", "Mystery Egg",
					"Clear Bell", "Silver Wing", "Moomoo Milk", "Quick Claw",
					"PSNCureBerry", "Gold Leaf", "Soft Sand", "Sharp Beak",
					"PRZCureBerry", "Burnt Berry", "Ice Berry", "Poison Barb",
					"King's Rock", "Bitter Berry", "Mint Berry",
					"Red Apricorn", "TinyMushroom", "Big Mushroom",
					"SilverPowder", "Blu Apricorn", "Teru-sama", "Amulet Coin",
					"Ylw Apricorn", "Grn Apricorn", "Cleanse Tag",
					"Mystic Water", "TwistedSpoon", "Wht Apricorn",
					"Black Belt", "Blk Apricorn", "Teru-sama", "Pnk Apricorn",
					"BlackGlasses", "SlowpokeTail", "Pink Bow", "Stick",
					"Smoke Ball", "NeverMeltIce", "Magnet", "MiracleBerry",
					"Pearl", "Big Pearl", "Everstone", "Spell Tag",
					"RageCandyBar", "GS Ball*", "Blue Card*", "Miracle Seed",
					"Thick Club", "Focus Band", "Teru-sama", "EnergyPowder",
					"Energy Root", "Heal Powder", "Revival Herb", "Hard Stone",
					"Lucky Egg", "Card Key", "Machine Part", "Egg Ticket*",
					"Lost Item", "Stardust", "Star Piece", "Basement Key",
					"Pass", "Teru-sama", "Teru-sama", "Teru-sama", "Charcoal",
					"Berry Juice", "Scope Lens", "Teru-sama", "Teru-sama",
					"Metal Coat", "Dragon Fang", "Teru-sama", "Leftovers",
					"Teru-sama", "Teru-sama", "Teru-sama", "MysteryBerry",
					"Dragon Scale", "Berserk Gene", "Teru-sama", "Teru-sama",
					"Teru-sama", "Sacred Ash", "Heavy Ball", "Flower Mail",
					"Level Ball", "Lure Ball", "Fast Ball", "Teru-sama",
					"Light Ball", "Friend Ball", "Moon Ball", "Love Ball",
					"Normal Box", "Gorgeous Box", "Sun Stone", "Polkadot Bow",
					"Teru-sama", "Up-Grade", "Berry", "Gold Berry",
					"SquirtBottle", "Teru-sama", "Park Ball", "Rainbow Wing",
					"Teru-sama", "Brick Piece", "Surf Mail", "Litebluemail",
					"Portraitmail", "Lovely Mail", "Eon Mail", "Morph Mail",
					"Bluesky Mail", "Music Mail", "Mirage Mail", "Teru-sama",
					"TM01", "TM02", "TM03", "TM04", "TM04", "TM05", "TM06",
					"TM07", "TM08", "TM09", "TM10", "TM11", "TM12", "TM13",
					"TM14", "TM15", "TM16", "TM17", "TM18", "TM19", "TM20",
					"TM21", "TM22", "TM23", "TM24", "TM25", "TM26", "TM27",
					"TM28", "Teru-sama", "TM29", "TM30", "TM31", "TM32",
					"TM33", "TM34", "TM35", "TM36", "TM37", "TM38", "TM39",
					"TM40", "TM41", "TM42", "TM43", "TM44", "TM45", "TM46",
					"TM47", "TM48", "TM49", "TM50", "HM01", "HM02", "HM03",
					"HM04", "HM05", "HM06", "HM07", "HM08", "HM09", "HM10",
					"HM11", "HM12", "Cancel", },
			{ "Nothing", "Master Ball", "Ultra Ball", "Great Ball",
					"Poké Ball", "Safari Ball", "Net Ball", "Dive Ball",
					"Nest Ball", "Repeat Ball", "Timer Ball", "Luxury Ball",
					"Premier Ball", "Potion", "Antidote", "Burn Heal",
					"Ice Heal", "Awakening", "Parlyz Heal", "Full Restore",
					"Max Potion", "Hyper Potion", "Super Potion", "Full Heal",
					"Revive", "Max Revive", "Fresh Water", "Soda Pop",
					"Lemonade", "Moomoo Milk", "EnergyPowder", "Energy Root",
					"Heal Powder", "Revival Herb", "Ether", "Max Ether",
					"Elixir", "Max Elixir", "Lava Cookie", "Blue Flute",
					"Yellow Flute", "Red Flute", "Black Flute", "White Flute",
					"Berry Juice", "Sacred Ash", "Shoal Salt", "Shoal Shell",
					"Red Shard", "Blue Shard", "Yellow Shard", "Green Shard",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "HP Up", "Protein", "Iron", "Carbos",
					"Calcium", "Rare Candy", "PP Up", "Zinc", "PP Max", "???",
					"Guard Spec.", "Dire Hit", "X Attack", "X Defend",
					"X Speed", "X Accuracy", "X Special", "Poké Doll",
					"Fluffy Tail", "???", "Super Repel", "Max Repel",
					"Escape Rope", "Repel", "???", "???", "???", "???", "???",
					"???", "Sun Stone", "Moon Stone", "Fire Stone",
					"Thunderstone", "Water Stone", "Leaf Stone", "???", "???",
					"???", "???", "TinyMushroom", "Big Mushroom", "???",
					"Pearl", "Big Pearl", "Stardust", "Star Piece", "Nugget",
					"Heart Scale", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "Orange Mail", "Harbor Mail",
					"Glitter Mail", "Mech Mail", "Wood Mail", "Wave Mail",
					"Bead Mail", "Shadow Mail", "Tropic Mail", "Dream Mail",
					"Fab Mail", "Retro Mail", "Cheri Berry", "Chesto Berry",
					"Pecha Berry", "Rawst Berry", "Aspear Berry",
					"Leppa Berry", "Oran Berry", "Persim Berry", "Lum Berry",
					"Sitrus Berry", "Figy Berry", "Wiki Berry", "Mago Berry",
					"Aguav Berry", "Iapapa Berry", "Razz Berry", "Bluk Berry",
					"Nanab Berry", "Wepear Berry", "Pinap Berry",
					"Pomeg Berry", "Kelpsy Berry", "Qualot Berry",
					"Hondew Berry", "Grepa Berry", "Tamato Berry",
					"Cornn Berry", "Magost Berry", "Rabuta Berry",
					"Nomel Berry", "Spelon Berry", "Pamtre Berry",
					"Watmel Berry", "Durin Berry", "Belue Berry",
					"Liechi Berry", "Ganlon Berry", "Salac Berry",
					"Petaya Berry", "Apicot Berry", "Lansat Berry",
					"Starf Berry", "Enigma Berry", "???", "???", "???",
					"BrightPowder", "White Herb", "Macho Brace", "Exp. Share",
					"Quick Claw", "Soothe Bell", "Mental Herb", "Choice Band",
					"King's Rock", "SilverPowder", "Amulet Coin",
					"Cleanse Tag", "Soul Dew", "DeepSeaTooth", "DeepSeaScale",
					"Smoke Ball", "Everstone", "Focus Band", "Lucky Egg",
					"Scope Lens", "Metal Coat", "Leftovers", "Dragon Scale",
					"Light Ball", "Soft Sand", "Hard Stone", "Miracle Seed",
					"BlackGlasses", "Black Belt", "Magnet", "Mystic Water",
					"Sharp Beak", "Poison Barb", "NeverMeltIce", "Spell Tag",
					"TwistedSpoon", "Charcoal", "Dragon Fang", "Silk Scarf",
					"Up-Grade", "Shell Bell", "Sea Incense", "Lax Incense",
					"Lucky Punch", "Metal Powder", "Thick Club", "Stick",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "???", "Red Scarf", "Blue Scarf",
					"Pink Scarf", "Green Scarf", "Yellow Scarf", "Mach Bike",
					"Coin Case", "Itemfinder", "Old Rod", "Good Rod",
					"Super Rod", "S.S. Ticket", "Contest Pass", "???",
					"Wailmer Pail", "Devon Goods", "Soot Sack", "Basement Key",
					"Acro Bike", "Pokéblock Case", "Letter", "Eon Ticket",
					"Red Orb", "Blue Orb", "Scanner", "Go-Goggles",
					"Meteorite", "Rm. 1 Key", "Rm. 2 Key", "Rm. 4 Key",
					"Rm. 6 Key", "Storage Key", "Root Fossil", "Claw Fossil",
					"Devon Scope", "TM01", "TM02", "TM03", "TM04", "TM05",
					"TM06", "TM07", "TM08", "TM09", "TM10", "TM11", "TM12",
					"TM13", "TM14", "TM15", "TM16", "TM17", "TM18", "TM19",
					"TM20", "TM21", "TM22", "TM23", "TM24", "TM25", "TM26",
					"TM27", "TM28", "TM29", "TM30", "TM31", "TM32", "TM33",
					"TM34", "TM35", "TM36", "TM37", "TM38", "TM39", "TM40",
					"TM41", "TM42", "TM43", "TM44", "TM45", "TM46", "TM47",
					"TM48", "TM49", "TM50", "HM01", "HM02", "HM03", "HM04",
					"HM05", "HM06", "HM07", "HM08", "???", "???",
					"Oak's Parcel", "Poké Flute", "Secret Key", "Bike Voucher",
					"Gold Teeth", "Old Amber", "Card Key", "Lift Key",
					"Dome Fossil", "Helix Fossil", "Silph Scope", "Bicycle",
					"Town Map", "Vs. Seeker", "Fame Checker", "TM Case",
					"Berry Pouch", "Teachy TV", "Tri-Pass", "Rainbow Pass",
					"Tea", "MysticTicket", "AuroraTicket", "Powder Jar",
					"Ruby", "Sapphire", "Magma Emblem", "Old Sea Map", },
			{ "None", "Master Ball", "Ultra Ball", "Great Ball", "Poké Ball",
					"Safari Ball", "Net Ball", "Dive Ball", "Nest Ball",
					"Repeat Ball", "Timer Ball", "Luxury Ball", "Premier Ball",
					"Dusk Ball", "Heal Ball", "Quick Ball", "Cherish Ball",
					"Potion", "Antidote", "Burn Heal", "Ice Heal", "Awakening",
					"Parlyz Heal", "Full Restore", "Max Potion",
					"Hyper Potion", "Super Potion", "Full Heal", "Revive",
					"Max Revive", "Fresh Water", "Soda Pop", "Lemonade",
					"Moomoo Milk", "EnergyPowder", "Energy Root",
					"Heal Powder", "Revival Herb", "Ether", "Max Ether",
					"Elixir", "Max Elixir", "Lava Cookie", "Berry Juice",
					"Sacred Ash", "HP Up", "Protein", "Iron", "Carbos",
					"Calcium", "Rare Candy", "PP Up", "Zinc", "PP Max",
					"Old Gateau", "Guard Spec.", "Dire Hit", "X Attack",
					"X Defend", "X Speed", "X Accuracy", "X Special",
					"X Sp. Def", "Poké Doll", "Fluffy Tail", "Blue Flute",
					"Yellow Flute", "Red Flute", "Black Flute", "White Flute",
					"Shoal Salt", "Shoal Shell", "Red Shard", "Blue Shard",
					"Yellow Shard", "Green Shard", "Super Repel", "Max Repel",
					"Escape Rope", "Repel", "Sun Stone", "Moon Stone",
					"Fire Stone", "Thunderstone", "Water Stone", "Leaf Stone",
					"TinyMushroom", "Big Mushroom", "Pearl", "Big Pearl",
					"Stardust", "Star Piece", "Nugget", "Heart Scale", "Honey",
					"Growth Mulch", "Damp Mulch", "Stable Mulch",
					"Gooey Mulch", "Root Fossil", "Claw Fossil",
					"Helix Fossil", "Dome Fossil", "Old Amber", "Armor Fossil",
					"Skull Fossil", "Rare Bone", "Shiny Stone", "Dusk Stone",
					"Dawn Stone", "Oval Stone", "Odd Keystone", "Griseous Orb",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "???", "???", "???", "???", "Adamant Orb",
					"Lustrous Orb", "Grass Mail", "Flame Mail", "Bubble Mail",
					"Bloom Mail", "Tunnel Mail", "Steel Mail", "Heart Mail",
					"Snow Mail", "Space Mail", "Air Mail", "Mosaic Mail",
					"Brick Mail", "Cheri Berry", "Chesto Berry", "Pecha Berry",
					"Rawst Berry", "Aspear Berry", "Leppa Berry", "Oran Berry",
					"Persim Berry", "Lum Berry", "Sitrus Berry", "Figy Berry",
					"Wiki Berry", "Mago Berry", "Aguav Berry", "Iapapa Berry",
					"Razz Berry", "Bluk Berry", "Nanab Berry", "Wepear Berry",
					"Pinap Berry", "Pomeg Berry", "Kelpsy Berry",
					"Qualot Berry", "Hondew Berry", "Grepa Berry",
					"Tamato Berry", "Cornn Berry", "Magost Berry",
					"Rabuta Berry", "Nomel Berry", "Spelon Berry",
					"Pamtre Berry", "Watmel Berry", "Durin Berry",
					"Belue Berry", "Occa Berry", "Passho Berry", "Wacan Berry",
					"Rindo Berry", "Yache Berry", "Chople Berry",
					"Kebia Berry", "Shuca Berry", "Coba Berry", "Payapa Berry",
					"Tanga Berry", "Charti Berry", "Kasib Berry",
					"Haban Berry", "Colbur Berry", "Babiri Berry",
					"Chilan Berry", "Liechi Berry", "Ganlon Berry",
					"Salac Berry", "Petaya Berry", "Apicot Berry",
					"Lansat Berry", "Starf Berry", "Enigma Berry",
					"Micle Berry", "Custap Berry", "Jaboca Berry",
					"Rowap Berry", "BrightPowder", "White Herb", "Macho Brace",
					"Exp. Share", "Quick Claw", "Soothe Bell", "Mental Herb",
					"Choice Band", "King's Rock", "SilverPowder",
					"Amulet Coin", "Cleanse Tag", "Soul Dew", "DeepSeaTooth",
					"DeepSeaScale", "Smoke Ball", "Everstone", "Focus Band",
					"Lucky Egg", "Scope Lens", "Metal Coat", "Leftovers",
					"Dragon Scale", "Light Ball", "Soft Sand", "Hard Stone",
					"Miracle Seed", "BlackGlasses", "Black Belt", "Magnet",
					"Mystic Water", "Sharp Beak", "Poison Barb",
					"NeverMeltIce", "Spell Tag", "TwistedSpoon", "Charcoal",
					"Dragon Fang", "Silk Scarf", "Up-Grade", "Shell Bell",
					"Sea Incense", "Lax Incense", "Lucky Punch",
					"Metal Powder", "Thick Club", "Stick", "Red Scarf",
					"Blue Scarf", "Pink Scarf", "Green Scarf", "Yellow Scarf",
					"Wide Lens", "Muscle Band", "Wise Glasses", "Expert Belt",
					"Light Clay", "Life Orb", "Power Herb", "Toxic Orb",
					"Flame Orb", "Quick Powder", "Focus Sash", "Zoom Lens",
					"Metronome", "Iron Ball", "Lagging Tail", "Destiny Knot",
					"Black Sludge", "Icy Rock", "Smooth Rock", "Heat Rock",
					"Damp Rock", "Grip Claw", "Choice Scarf", "Sticky Barb",
					"Power Bracer", "Power Belt", "Power Lens", "Power Band",
					"Power Anklet", "Power Weight", "Shed Shell", "Big Root",
					"Choice Specs", "Flame Plate", "Splash Plate", "Zap Plate",
					"Meadow Plate", "Icicle Plate", "Fist Plate",
					"Toxic Plate", "Earth Plate", "Sky Plate", "Mind Plate",
					"Insect Plate", "Stone Plate", "Spooky Plate",
					"Draco Plate", "Dread Plate", "Iron Plate", "Odd Incense",
					"Rock Incense", "Full Incense", "Wave Incense",
					"Rose Incense", "Luck Incense", "Pure Incense",
					"Protector", "Electirizer", "Magmarizer", "Dubious Disc",
					"Reaper Cloth", "Razor Claw", "Razor Fang", "TM01", "TM02",
					"TM03", "TM04", "TM05", "TM06", "TM07", "TM08", "TM09",
					"TM10", "TM11", "TM12", "TM13", "TM14", "TM15", "TM16",
					"TM17", "TM18", "TM19", "TM20", "TM21", "TM22", "TM23",
					"TM24", "TM25", "TM26", "TM27", "TM28", "TM29", "TM30",
					"TM31", "TM32", "TM33", "TM34", "TM35", "TM36", "TM37",
					"TM38", "TM39", "TM40", "TM41", "TM42", "TM43", "TM44",
					"TM45", "TM46", "TM47", "TM48", "TM49", "TM50", "TM51",
					"TM52", "TM53", "TM54", "TM55", "TM56", "TM57", "TM58",
					"TM59", "TM60", "TM61", "TM62", "TM63", "TM64", "TM65",
					"TM66", "TM67", "TM68", "TM69", "TM70", "TM71", "TM72",
					"TM73", "TM74", "TM75", "TM76", "TM77", "TM78", "TM79",
					"TM80", "TM81", "TM82", "TM83", "TM84", "TM85", "TM86",
					"TM87", "TM88", "TM89", "TM90", "TM91", "TM92", "HM01",
					"HM02", "HM03", "HM04", "HM05", "HM06", "HM07", "HM08",
					"Explorer Kit", "Loot Sack", "Rule Book", "Poké Radar",
					"Point Card", "Journal", "Seal Case", "Fashion Case",
					"Seal Bag", "Pal Pad", "Works Key", "Old Charm",
					"Galactic Key", "Red Chain", "Town Map", "Vs. Seeker",
					"Coin Case", "Old Rod", "Good Rod", "Super Rod",
					"Sprayduck", "Poffin Case", "Bicycle", "Suite Key",
					"Oak's Letter", "Lunar Wing", "Member Card", "Azure Flute",
					"S.S. Ticket", "Contest Pass", "Magma Stone", "Parcel",
					"Coupon 1", "Coupon 2", "Coupon 3", "Storage Key",
					"SecretPotion", "Vs. Recorder", "Gracidea", "Secret Key",
					"Apricorn Box", "Unown Report", "Berry Pots",
					"Dowsing MCHN", "Blue Card", "SlowpokeTail", "Clear Bell",
					"Card Key", "Basement Key", "SquirtBottle", "Red Scale",
					"Lost Item", "Pass", "Machine Part", "Silver Wing",
					"Rainbow Wing", "Mystery Egg", "Red Apricorn",
					"Ylw Apricorn", "Blu Apricorn", "Grn Apricorn",
					"Pnk Apricorn", "Wht Apricorn", "Blk Apricorn",
					"Fast Ball", "Level Ball", "Lure Ball", "Heavy Ball",
					"Love Ball", "Friend Ball", "Moon Ball", "Sport Ball",
					"Park Ball", "Photo Album", "GB Sounds", "Tidal Bell",
					"RageCandyBar", "Data Card 01", "Data Card 02",
					"Data Card 03", "Data Card 04", "Data Card 05",
					"Data Card 06", "Data Card 07", "Data Card 08",
					"Data Card 09", "Data Card 10", "Data Card 11",
					"Data Card 12", "Data Card 13", "Data Card 14",
					"Data Card 15", "Data Card 16", "Data Card 17",
					"Data Card 18", "Data Card 19", "Data Card 20",
					"Data Card 21", "Data Card 22", "Data Card 23",
					"Data Card 24", "Data Card 25", "Data Card 26",
					"Data Card 27", "Jade Orb", "Lock Capsule", "Red Orb",
					"Blue Orb", "Enigma Stone", },
			{ "None", "Master Ball", "Ultra Ball", "Great Ball", "Poké Ball",
					"Safari Ball", "Net Ball", "Dive Ball", "Nest Ball",
					"Repeat Ball", "Timer Ball", "Luxury Ball", "Premier Ball",
					"Dusk Ball", "Heal Ball", "Quick Ball", "Cherish Ball",
					"Potion", "Antidote", "Burn Heal", "Ice Heal", "Awakening",
					"Parlyz Heal", "Full Restore", "Max Potion",
					"Hyper Potion", "Super Potion", "Full Heal", "Revive",
					"Max Revive", "Fresh Water", "Soda Pop", "Lemonade",
					"Moomoo Milk", "EnergyPowder", "Energy Root",
					"Heal Powder", "Revival Herb", "Ether", "Max Ether",
					"Elixir", "Max Elixir", "Lava Cookie", "Berry Juice",
					"Sacred Ash", "HP Up", "Protein", "Iron", "Carbos",
					"Calcium", "Rare Candy", "PP Up", "Zinc", "PP Max",
					"Old Gateau", "Guard Spec.", "Dire Hit", "X Attack",
					"X Defend", "X Speed", "X Accuracy", "X Special",
					"X Sp. Def", "Poké Doll", "Fluffy Tail", "Blue Flute",
					"Yellow Flute", "Red Flute", "Black Flute", "White Flute",
					"Shoal Salt", "Shoal Shell", "Red Shard", "Blue Shard",
					"Yellow Shard", "Green Shard", "Super Repel", "Max Repel",
					"Escape Rope", "Repel", "Sun Stone", "Moon Stone",
					"Fire Stone", "Thunderstone", "Water Stone", "Leaf Stone",
					"TinyMushroom", "Big Mushroom", "Pearl", "Big Pearl",
					"Stardust", "Star Piece", "Nugget", "Heart Scale", "Honey",
					"Growth Mulch", "Damp Mulch", "Stable Mulch",
					"Gooey Mulch", "Root Fossil", "Claw Fossil",
					"Helix Fossil", "Dome Fossil", "Old Amber", "Armor Fossil",
					"Skull Fossil", "Rare Bone", "Shiny Stone", "Dusk Stone",
					"Dawn Stone", "Oval Stone", "Odd Keystone", "Griseous Orb",
					"???", "???", "???", "Douse Drive", "Shock Drive",
					"Burn Drive", "Chill Drive", "???", "???", "???", "???",
					"???", "???", "???", "???", "???", "???", "???", "???",
					"???", "???", "Sweet Heart", "Adamant Orb", "Lustrous Orb",
					"Greet Mail", "Favored Mail", "RSVP Mail", "Thanks Mail",
					"Inquiry Mail", "Like Mail", "Reply Mail", "BridgeMail S",
					"BridgeMail D", "BridgeMail T", "BridgeMail V",
					"BridgeMail M", "Cheri Berry", "Chesto Berry",
					"Pecha Berry", "Rawst Berry", "Aspear Berry",
					"Leppa Berry", "Oran Berry", "Persim Berry", "Lum Berry",
					"Sitrus Berry", "Figy Berry", "Wiki Berry", "Mago Berry",
					"Aguav Berry", "Iapapa Berry", "Razz Berry", "Bluk Berry",
					"Nanab Berry", "Wepear Berry", "Pinap Berry",
					"Pomeg Berry", "Kelpsy Berry", "Qualot Berry",
					"Hondew Berry", "Grepa Berry", "Tamato Berry",
					"Cornn Berry", "Magost Berry", "Rabuta Berry",
					"Nomel Berry", "Spelon Berry", "Pamtre Berry",
					"Watmel Berry", "Durin Berry", "Belue Berry", "Occa Berry",
					"Passho Berry", "Wacan Berry", "Rindo Berry",
					"Yache Berry", "Chople Berry", "Kebia Berry",
					"Shuca Berry", "Coba Berry", "Payapa Berry", "Tanga Berry",
					"Charti Berry", "Kasib Berry", "Haban Berry",
					"Colbur Berry", "Babiri Berry", "Chilan Berry",
					"Liechi Berry", "Ganlon Berry", "Salac Berry",
					"Petaya Berry", "Apicot Berry", "Lansat Berry",
					"Starf Berry", "Enigma Berry", "Micle Berry",
					"Custap Berry", "Jaboca Berry", "Rowap Berry",
					"BrightPowder", "White Herb", "Macho Brace", "Exp. Share",
					"Quick Claw", "Soothe Bell", "Mental Herb", "Choice Band",
					"King's Rock", "SilverPowder", "Amulet Coin",
					"Cleanse Tag", "Soul Dew", "DeepSeaTooth", "DeepSeaScale",
					"Smoke Ball", "Everstone", "Focus Band", "Lucky Egg",
					"Scope Lens", "Metal Coat", "Leftovers", "Dragon Scale",
					"Light Ball", "Soft Sand", "Hard Stone", "Miracle Seed",
					"BlackGlasses", "Black Belt", "Magnet", "Mystic Water",
					"Sharp Beak", "Poison Barb", "NeverMeltIce", "Spell Tag",
					"TwistedSpoon", "Charcoal", "Dragon Fang", "Silk Scarf",
					"Up-Grade", "Shell Bell", "Sea Incense", "Lax Incense",
					"Lucky Punch", "Metal Powder", "Thick Club", "Stick",
					"Red Scarf", "Blue Scarf", "Pink Scarf", "Green Scarf",
					"Yellow Scarf", "Wide Lens", "Muscle Band", "Wise Glasses",
					"Expert Belt", "Light Clay", "Life Orb", "Power Herb",
					"Toxic Orb", "Flame Orb", "Quick Powder", "Focus Sash",
					"Zoom Lens", "Metronome", "Iron Ball", "Lagging Tail",
					"Destiny Knot", "Black Sludge", "Icy Rock", "Smooth Rock",
					"Heat Rock", "Damp Rock", "Grip Claw", "Choice Scarf",
					"Sticky Barb", "Power Bracer", "Power Belt", "Power Lens",
					"Power Band", "Power Anklet", "Power Weight", "Shed Shell",
					"Big Root", "Choice Specs", "Flame Plate", "Splash Plate",
					"Zap Plate", "Meadow Plate", "Icicle Plate", "Fist Plate",
					"Toxic Plate", "Earth Plate", "Sky Plate", "Mind Plate",
					"Insect Plate", "Stone Plate", "Spooky Plate",
					"Draco Plate", "Dread Plate", "Iron Plate", "Odd Incense",
					"Rock Incense", "Full Incense", "Wave Incense",
					"Rose Incense", "Luck Incense", "Pure Incense",
					"Protector", "Electirizer", "Magmarizer", "Dubious Disc",
					"Reaper Cloth", "Razor Claw", "Razor Fang", "TM01", "TM02",
					"TM03", "TM04", "TM05", "TM06", "TM07", "TM08", "TM09",
					"TM10", "TM11", "TM12", "TM13", "TM14", "TM15", "TM16",
					"TM17", "TM18", "TM19", "TM20", "TM21", "TM22", "TM23",
					"TM24", "TM25", "TM26", "TM27", "TM28", "TM29", "TM30",
					"TM31", "TM32", "TM33", "TM34", "TM35", "TM36", "TM37",
					"TM38", "TM39", "TM40", "TM41", "TM42", "TM43", "TM44",
					"TM45", "TM46", "TM47", "TM48", "TM49", "TM50", "TM51",
					"TM52", "TM53", "TM54", "TM55", "TM56", "TM57", "TM58",
					"TM59", "TM60", "TM61", "TM62", "TM63", "TM64", "TM65",
					"TM66", "TM67", "TM68", "TM69", "TM70", "TM71", "TM72",
					"TM73", "TM74", "TM75", "TM76", "TM77", "TM78", "TM79",
					"TM80", "TM81", "TM82", "TM83", "TM84", "TM85", "TM86",
					"TM87", "TM88", "TM89", "TM90", "TM91", "TM92", "HM01",
					"HM02", "HM03", "HM04", "HM05", "HM06", "???", "???",
					"Explorer Kit", "Loot Sack", "Rule Book", "Poké Radar",
					"Point Card", "Journal", "Seal Case", "Fashion Case",
					"Seal Bag", "Pal Pad", "Works Key", "Old Charm",
					"Galactic Key", "Red Chain", "Town Map", "Vs. Seeker",
					"Coin Case", "Old Rod", "Good Rod", "Super Rod",
					"Sprayduck", "Poffin Case", "Bicycle", "Suite Key",
					"Oak's Letter", "Lunar Wing", "Member Card", "Azure Flute",
					"S.S. Ticket", "Contest Pass", "Magma Stone", "Parcel",
					"Coupon 1", "Coupon 2", "Coupon 3", "Storage Key",
					"SecretPotion", "Vs. Recorder", "Gracidea", "Secret Key",
					"Apricorn Box", "Unown Report", "Berry Pots",
					"Dowsing MCHN", "Blue Card", "SlowpokeTail", "Clear Bell",
					"Card Key", "Basement Key", "Squirtbottle", "Red Scale",
					"Lost Item", "Pass", "Machine Part", "Silver Wing",
					"Rainbow Wing", "Mystery Egg", "Red Apricorn",
					"Ylw Apricorn", "Blu Apricorn", "Grn Apricorn",
					"Pnk Apricorn", "Wht Apricorn", "Blk Apricorn",
					"Fast Ball", "Level Ball", "Lure Ball", "Heavy Ball",
					"Love Ball", "Friend Ball", "Moon Ball", "Sport Ball",
					"Park Ball", "Photo Album", "GB Sounds", "Tidal Bell",
					"RageCandyBar", "Data Card 01", "Data Card 02",
					"Data Card 03", "Data Card 04", "Data Card 05",
					"Data Card 06", "Data Card 07", "Data Card 08",
					"Data Card 09", "Data Card 10", "Data Card 11",
					"Data Card 12", "Data Card 13", "Data Card 14",
					"Data Card 15", "Data Card 16", "Data Card 17",
					"Data Card 18", "Data Card 19", "Data Card 20",
					"Data Card 21", "Data Card 22", "Data Card 23",
					"Data Card 24", "Data Card 25", "Data Card 26",
					"Data Card 27", "Jade Orb", "Lock Capsule", "Red Orb",
					"Blue Orb", "Enigma Stone", "Prism Scale", "Eviolite",
					"Float Stone", "Rocky Helmet", "Air Balloon", "Red Card",
					"Ring Target", "Binding Band", "Absorb Bulb",
					"Cell Battery", "Eject Button", "Fire Gem", "Water Gem",
					"Electric Gem", "Grass Gem", "Ice Gem", "Fighting Gem",
					"Poison Gem", "Ground Gem", "Flying Gem", "Psychic Gem",
					"Bug Gem", "Rock Gem", "Ghost Gem", "Dragon Gem",
					"Dark Gem", "Steel Gem", "Normal Gem", "Health Wing",
					"Muscle Wing", "Resist Wing", "Genius Wing", "Clever Wing",
					"Swift Wing", "Pretty Wing", "Cover Fossil",
					"Plume Fossil", "Liberty Pass", "Pass Orb", "Dream Ball",
					"Poké Toy", "Prop Case", "Dragon Skull", "BalmMushroom",
					"Big Nugget", "Pearl String", "Comet Shard",
					"Relic Copper", "Relic Silver", "Relic Gold", "Relic Vase",
					"Relic Band", "Relic Statue", "Relic Crown",
					"Casteliacone", "Dire Hit 2", "X Speed 2", "X Special 2",
					"X Sp. Def 2", "X Defend 2", "X Attack 2", "X Accuracy 2",
					"X Speed 3", "X Special 3", "X Sp. Def 3", "X Defend 3",
					"X Attack 3", "X Accuracy 3", "X Speed 6", "X Special 6",
					"X Sp. Def 6", "X Defend 6", "X Attack 6", "X Accuracy 6",
					"Ability Urge", "Item Drop", "Item Urge", "Reset Urge",
					"Dire Hit 3", "Light Stone", "Dark Stone", "TM93", "TM94",
					"TM95", "Xtransceiver", "God Stone", "Gram 1", "Gram 2",
					"Gram 3", "Xtransceiver", "Medal Box", "DNA Splicers",
					"DNA Splicers", "Permit", "Oval Charm", "Shiny Charm",
					"Plasma Card", "Grubby Hanky", "Colress MCHN",
					"Dropped Item", "Dropped Item", "Reveal Glass", } };

	public static final boolean[] bannedRandomMoves = new boolean[560],
			bannedForDamagingMove = new boolean[560];
	static {
		bannedRandomMoves[15] = true; // Cut HM01
		bannedRandomMoves[19] = true; // Fly HM02
		bannedRandomMoves[57] = true; // Surf HM03
		bannedRandomMoves[70] = true; // Strength HM04
		bannedRandomMoves[148] = true; // Flash HM05 in some games
		bannedRandomMoves[250] = true; // Whirlpool HM06 in some games
		bannedRandomMoves[127] = true; // Waterfall HM07 in some games
		bannedRandomMoves[291] = true; // Dive HM08 in some games
		bannedRandomMoves[431] = true; // Rock Climb HM08 in some games
		bannedRandomMoves[249] = true; // Rock Smash HM05 in some games
		bannedRandomMoves[432] = true; // Defog HM06 in some games
		bannedRandomMoves[144] = true; // Transform, glitched in RBY
		bannedRandomMoves[165] = true; // Struggle, self explanatory

		bannedForDamagingMove[120] = true; // SelfDestruct
		bannedForDamagingMove[138] = true; // Dream Eater
		bannedForDamagingMove[153] = true; // Explosion
		bannedForDamagingMove[173] = true; // Snore
		bannedForDamagingMove[206] = true; // False Swipe
		bannedForDamagingMove[248] = true; // Future Sight
		bannedForDamagingMove[252] = true; // Fake Out
		bannedForDamagingMove[264] = true; // Focus Punch
		bannedForDamagingMove[353] = true; // Doom Desire
		bannedForDamagingMove[364] = true; // Feint
		bannedForDamagingMove[387] = true; // Last Resort
		bannedForDamagingMove[389] = true; // Sucker Punch

	}

	public static Set<Pokemon> getBasicOrNoCopyPokemon(RomHandler baseRom) {
		List<Pokemon> allPokes = baseRom.getPokemon();
		List<Evolution> evos = baseRom.getEvolutions();

		Set<Pokemon> doCopyPokes = new TreeSet<Pokemon>();
		for (Evolution e : evos) {
			if (e.carryStats) {
				doCopyPokes.add(allPokes.get(e.to));
			}
		}
		Set<Pokemon> dontCopyPokes = new TreeSet<Pokemon>();
		for (Pokemon pkmn : allPokes) {
			if (pkmn != null) {
				if (doCopyPokes.contains(pkmn) == false) {
					dontCopyPokes.add(pkmn);
				}
			}
		}
		return dontCopyPokes;
	}

	public static Set<Pokemon> getFirstEvolutions(RomHandler baseRom) {
		List<Pokemon> allPokes = baseRom.getPokemon();
		List<Evolution> evos = baseRom.getEvolutions();
		Set<Pokemon> basicPokemon = getBasicOrNoCopyPokemon(baseRom);

		Set<Pokemon> firstEvos = new TreeSet<Pokemon>();
		for (Evolution e : evos) {
			if (basicPokemon.contains(allPokes.get(e.from))) {
				firstEvos.add(allPokes.get(e.to));
			}
		}
		return firstEvos;
	}

	public static Set<Pokemon> getSecondEvolutions(RomHandler baseRom) {
		List<Pokemon> allPokes = baseRom.getPokemon();
		List<Evolution> evos = baseRom.getEvolutions();
		Set<Pokemon> firstEvos = getFirstEvolutions(baseRom);

		Set<Pokemon> secondEvos = new TreeSet<Pokemon>();
		for (Evolution e : evos) {
			if (firstEvos.contains(allPokes.get(e.from))) {
				secondEvos.add(allPokes.get(e.to));
			}
		}
		return secondEvos;
	}

	public static boolean pokemonHasEvo(RomHandler baseRom, Pokemon pkmn) {
		List<Evolution> evos = baseRom.getEvolutions();
		for (Evolution evo : evos) {
			if (evo.from == pkmn.number) {
				return true;
			}
		}
		return false;
	}

	public static Pokemon evolvesFrom(RomHandler baseRom, Pokemon pkmn) {
		List<Evolution> evos = baseRom.getEvolutions();
		for (Evolution evo : evos) {
			if (evo.to == pkmn.number) {
				return baseRom.getPokemon().get(evo.from);
			}
		}
		return null;
	}

	public static String camelCase(String original) {
		char[] string = original.toLowerCase().toCharArray();
		boolean docap = true;
		for (int j = 0; j < string.length; j++) {
			char current = string[j];
			if (docap && Character.isLetter(current)) {
				string[j] = Character.toUpperCase(current);
				docap = false;
			} else {
				if (!docap && !Character.isLetter(current) && current != '\'') {
					docap = true;
				}
			}
		}
		return new String(string);
	}

	public static int freeSpaceFinder(byte[] rom, byte freeSpace, int amount,
			int offset) {
		// by default align to 4 bytes to make sure things don't break
		return freeSpaceFinder(rom, freeSpace, amount, offset, true);
	}

	public static int freeSpaceFinder(byte[] rom, byte freeSpace, int amount,
			int offset, boolean longAligned) {
		if (!longAligned) {
			// Find 1 more than necessary and return 1 into it,
			// to preserve stuff like FF terminators for strings
			byte[] searchNeedle = new byte[amount + 1];
			for (int i = 0; i < amount + 1; i++) {
				searchNeedle[i] = freeSpace;
			}
			return searchForFirst(rom, offset, searchNeedle) + 1;
		} else {
			// Find 4 more than necessary and return into it as necessary for
			// 4-alignment,
			// to preserve stuff like FF terminators for strings
			byte[] searchNeedle = new byte[amount + 4];
			for (int i = 0; i < amount + 4; i++) {
				searchNeedle[i] = freeSpace;
			}
			return (searchForFirst(rom, offset, searchNeedle) + 4) & ~3;
		}
	}

	public static List<Integer> search(byte[] haystack, byte[] needle) {
		return search(haystack, 0, needle);
	}

	public static List<Integer> search(byte[] haystack, int beginOffset,
			byte[] needle) {
		int currentMatchStart = beginOffset;
		int currentCharacterPosition = 0;

		int docSize = haystack.length;
		int needleSize = needle.length;

		int[] toFillTable = buildKMPSearchTable(needle);
		List<Integer> results = new ArrayList<Integer>();

		while ((currentMatchStart + currentCharacterPosition) < docSize) {

			if (needle[currentCharacterPosition] == (haystack[currentCharacterPosition
					+ currentMatchStart])) {
				currentCharacterPosition = currentCharacterPosition + 1;

				if (currentCharacterPosition == (needleSize)) {
					results.add(currentMatchStart);
					currentCharacterPosition = 0;
					currentMatchStart = currentMatchStart + needleSize;

				}

			} else {
				currentMatchStart = currentMatchStart
						+ currentCharacterPosition
						- toFillTable[currentCharacterPosition];

				if (toFillTable[currentCharacterPosition] > -1) {
					currentCharacterPosition = toFillTable[currentCharacterPosition];
				}

				else {
					currentCharacterPosition = 0;

				}

			}
		}
		return results;
	}

	public static int searchForFirst(byte[] haystack, int beginOffset,
			byte[] needle) {
		int currentMatchStart = beginOffset;
		int currentCharacterPosition = 0;

		int docSize = haystack.length;
		int needleSize = needle.length;

		int[] toFillTable = buildKMPSearchTable(needle);

		while ((currentMatchStart + currentCharacterPosition) < docSize) {

			if (needle[currentCharacterPosition] == (haystack[currentCharacterPosition
					+ currentMatchStart])) {
				currentCharacterPosition = currentCharacterPosition + 1;

				if (currentCharacterPosition == (needleSize)) {
					return currentMatchStart;
				}

			} else {
				currentMatchStart = currentMatchStart
						+ currentCharacterPosition
						- toFillTable[currentCharacterPosition];

				if (toFillTable[currentCharacterPosition] > -1) {
					currentCharacterPosition = toFillTable[currentCharacterPosition];
				}

				else {
					currentCharacterPosition = 0;

				}

			}
		}
		return -1;
	}

	private static int[] buildKMPSearchTable(byte[] needle) {
		int[] stable = new int[needle.length];
		int pos = 2;
		int j = 0;
		stable[0] = -1;
		stable[1] = 0;
		while (pos < needle.length) {
			if (needle[pos - 1] == needle[j]) {
				stable[pos] = j + 1;
				pos++;
				j++;
			} else if (j > 0) {
				j = stable[j];
			} else {
				stable[pos] = 0;
				pos++;
			}
		}
		return stable;
	}

	public static String rewriteDescriptionForNewLineSize(String moveDesc,
			String newline, int lineSize, StringSizeDeterminer ssd) {
		// We rewrite the description we're given based on some new chars per
		// line.
		moveDesc = moveDesc.replace("-" + newline, "").replace(newline, " ");
		// Keep spatk/spdef as one word on one line
		moveDesc = moveDesc.replace("Sp. Atk", "Sp__Atk");
		moveDesc = moveDesc.replace("Sp. Def", "Sp__Def");
		moveDesc = moveDesc.replace("SP. ATK", "SP__ATK");
		moveDesc = moveDesc.replace("SP. DEF", "SP__DEF");
		String[] words = moveDesc.split(" ");
		StringBuilder fullDesc = new StringBuilder();
		StringBuilder thisLine = new StringBuilder();
		int currLineWC = 0;
		int currLineCC = 0;
		int linesWritten = 0;
		for (int i = 0; i < words.length; i++) {
			// Reverse the spatk/spdef preservation from above
			words[i] = words[i].replace("SP__", "SP. ");
			words[i] = words[i].replace("Sp__", "Sp. ");
			int reqLength = ssd.lengthFor(words[i]);
			if (currLineWC > 0) {
				reqLength++;
			}
			if (currLineCC + reqLength <= lineSize) {
				// add to current line
				if (currLineWC > 0) {
					thisLine.append(' ');
				}
				thisLine.append(words[i]);
				currLineWC++;
				currLineCC += reqLength;
			} else {
				// Save current line, if applicable
				if (currLineWC > 0) {
					if (linesWritten > 0) {
						fullDesc.append(newline);
					}
					fullDesc.append(thisLine.toString());
					linesWritten++;
					thisLine = new StringBuilder();
				}
				// Start the new line
				thisLine.append(words[i]);
				currLineWC = 1;
				currLineCC = ssd.lengthFor(words[i]);
			}
		}

		// If the last line has anything add it
		if (currLineWC > 0) {
			if (linesWritten > 0) {
				fullDesc.append(newline);
			}
			fullDesc.append(thisLine.toString());
			linesWritten++;
		}

		return fullDesc.toString();
	}

	public interface StringSizeDeterminer {
		public int lengthFor(String encodedText);
	}

	public static class StringLengthSD implements StringSizeDeterminer {

		@Override
		public int lengthFor(String encodedText) {
			return encodedText.length();
		}

	}

}
