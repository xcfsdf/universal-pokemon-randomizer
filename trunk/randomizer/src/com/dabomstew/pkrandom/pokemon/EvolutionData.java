package com.dabomstew.pkrandom.pokemon;

/*----------------------------------------------------------------------------*/
/*--  EvolutionData.java - hardcoded set of evolutions to date.				--*/
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

import com.dabomstew.pkrandom.romhandlers.RomHandler;

public class EvolutionData {

	private static List<Evolution> evolutions;

	static {
		evolutions = new ArrayList<Evolution>();

		// Kanto
		// Starters
		e(1, 2);
		e(2, 3);
		e(4, 5);
		e(5, 6);
		e(7, 8);
		e(8, 9);
		// Bugs
		e(10, 11);
		e(11, 12);
		e(13, 14);
		e(14, 15);
		// Pidgey
		e(16, 17);
		e(17, 18);
		// Rattata
		e(19, 20);
		// Spearow
		e(21, 22);
		// Ekans
		e(23, 24);
		// Pika
		e(25, 26);
		e(172, 25);
		// Shrew
		e(27, 28);
		// Nidos
		e(29, 30);
		e(30, 31);
		e(32, 33);
		e(33, 34);
		// Clefairy
		e(35, 36);
		e(173, 35);
		// Vulpix
		e(37, 38);
		// Jiggly
		e(39, 40);
		e(174, 39);
		// Zubat
		e(41, 42);
		e(42, 169);
		// Oddish
		e(43, 44);
		e(44, 45);
		e(44, 182);
		// Paras
		e(46, 47);
		// Venonat
		e(48, 49);
		// Diglett
		e(50, 51);
		// Meowth
		e(52, 53);
		// Psyduck
		e(54, 55);
		// Mankey
		e(56, 57);
		// Growlithe
		e(58, 59);
		// Polis
		e(60, 61);
		e(61, 62);
		e(61, 186);
		// Abra
		e(63, 64);
		e(64, 65);
		// Machop
		e(66, 67);
		e(67, 68);
		// Bellsprout
		e(69, 70);
		e(70, 71);
		// Tentacool
		e(72, 73);
		// Geodude
		e(74, 75);
		e(75, 76);
		// Ponyta
		e(77, 78);
		// Slowwwww
		e(79, 80);
		e(79, 199);
		// Magnemite
		e(81, 82);
		e(82, 462);
		// Doduo
		e(84, 85);
		// Seel
		e(86, 87);
		// Grimer
		e(88, 89);
		// Shellder
		e(90, 91);
		// Gastly
		e(92, 93);
		e(93, 94);
		// Onix
		e(95, 208);
		// Drowzee
		e(96, 97);
		// Krabby
		e(98, 99);
		// Voltorb
		e(100, 101);
		// Exeggcute
		e(102, 103);
		// Cubone
		e(104, 105);
		// Hitmons
		e(236, 106, false);
		e(236, 107, false);
		e(236, 237, false);
		// Lickitung
		e(108, 463);
		// Koffing
		e(109, 110);
		// Rhyhorn
		e(111, 112);
		e(112, 464);
		// Chansey
		e(440, 113);
		e(113, 242);
		// Tangela
		e(114, 465);
		// Horsea
		e(116, 117);
		e(117, 230);
		// Goldeen
		e(118, 119);
		// Staryu
		e(120, 121);
		// Mr. Mime
		e(439, 122);
		// Scyther
		e(123, 212);
		// Jynx
		e(238, 124);
		// Electabuzz
		e(239, 125);
		e(125, 466);
		// Magmar
		e(240, 126);
		e(126, 467);
		// Magikarp
		e(129, 130);
		// Eevee
		e(133, 134, false);
		e(133, 135, false);
		e(133, 136, false);
		e(133, 196, false);
		e(133, 197, false);
		e(133, 470, false);
		e(133, 471, false);
		// Porygon
		e(137, 233);
		e(233, 474);
		// Omanyte
		e(138, 139);
		// Kabuto
		e(140, 141);
		// Snorlax
		e(446, 143);
		// Dratini
		e(147, 148);
		e(148, 149);
		// JOHTO
		// Starters
		e(152, 153);
		e(153, 154);
		e(155, 156);
		e(156, 157);
		e(158, 159);
		e(159, 160);
		// Sentret
		e(161, 162);
		// Hoothoot
		e(163, 164);
		// Ledyba
		e(165, 166);
		// Spinarak
		e(167, 168);
		// Chinchou
		e(170, 171);
		// Togepi
		e(175, 176);
		e(176, 468);
		// Natu
		e(177, 178);
		// Mareep
		e(179, 180);
		e(180, 181);
		// Marill
		e(298, 183);
		e(183, 184);
		// Sudowoodo
		e(438, 185);
		// Hoppip
		e(187, 188);
		e(188, 189);
		// Aipom
		e(190, 424);
		// Sunkern
		e(191, 192);
		// Yanma
		e(193, 469);
		// Wooper
		e(194, 195);
		// Murkrow
		e(198, 430);
		// Misdreavus
		e(200, 429);
		// Wobbuffet
		e(360, 202);
		// Pineco
		e(204, 205);
		// Gligar
		e(207, 472);
		// Snubbull
		e(209, 210);
		// Sneasel
		e(215, 461);
		// Teddiursa
		e(216, 217);
		// Slugma
		e(218, 219);
		// Swinub
		e(220, 221);
		e(221, 473);
		// Remoraid
		e(223, 224);
		// Mantine
		e(458, 226);
		// Houndour
		e(228, 229);
		// Phanpy
		e(231, 232);
		// Larvitar
		e(246, 247);
		e(247, 248);
		// HOENN
		// Starters
		e(252, 253);
		e(253, 254);
		e(255, 256);
		e(256, 257);
		e(258, 259);
		e(259, 260);
		// Poochyena
		e(261, 262);
		// Zigzagoon
		e(263, 264);
		// Wurmple
		e(265, 266);
		e(266, 267);
		e(265, 268);
		e(268, 269);
		// Lotad
		e(270, 271);
		e(271, 272);
		// Seedot
		e(273, 274);
		e(274, 275);
		// Taillow
		e(276, 277);
		// Wingull
		e(278, 279);
		// Ralts
		e(280, 281);
		e(281, 282);
		e(281, 475);
		// Surskit
		e(283, 284);
		// Shroomish
		e(285, 286);
		// Slakoth
		e(287, 288);
		e(288, 289);
		// Nincada
		e(290, 291);
		e(290, 292, false);
		// Whismur
		e(293, 294);
		e(294, 295);
		// Makuhita
		e(296, 297);
		// Nosepass
		e(299, 476);
		// Skitty
		e(300, 301);
		// Aron
		e(304, 305);
		e(305, 306);
		// Meditite
		e(307, 308);
		// Electrike
		e(309, 310);
		// Roselia
		e(406, 315);
		e(315, 407);
		// Gulpin
		e(316, 317);
		// Carvanha
		e(318, 319);
		// Wailmer
		e(320, 321);
		// Numel
		e(322, 323);
		// Spoink
		e(325, 326);
		// Trapinch
		e(328, 329);
		e(329, 330);
		// Cacnea
		e(331, 332);
		// Swablu
		e(333, 334);
		// Barboach
		e(339, 340);
		// Corphish
		e(341, 342);
		// Baltoy
		e(343, 344);
		// Lileep
		e(345, 346);
		// Anorith
		e(347, 348);
		// Feebas
		e(349, 350);
		// Shuppet
		e(353, 354);
		// Duskull
		e(355, 356);
		e(356, 477);
		// Chimecho
		e(433, 358);
		// Snorunt
		e(361, 362);
		e(361, 478);
		// Spheal
		e(363, 364);
		e(364, 365);
		// Clamperl
		e(366, 367, false);
		e(366, 368, false);
		// Bagon
		e(371, 372);
		e(372, 373);
		// Beldum
		e(374, 375);
		e(375, 376);
		// SINNOH
		// Starters
		e(387, 388);
		e(388, 389);
		e(390, 391);
		e(391, 392);
		e(393, 394);
		e(394, 395);
		// Starly
		e(396, 397);
		e(397, 398);
		// Bidoof
		e(399, 400);
		// Kricketot
		e(401, 402);
		// Shinx
		e(403, 404);
		e(404, 405);
		// Cranidos
		e(408, 409);
		// Shieldon
		e(410, 411);
		// Burmy
		e(412, 413);
		e(412, 414);
		// Combee
		e(415, 416);
		// Buizel
		e(418, 419);
		// Cherubi
		e(420, 421);
		// Shellos
		e(422, 423);
		// Drifloon
		e(425, 426);
		// Buneary
		e(427, 428);
		// Glameow
		e(431, 432);
		// Stunky
		e(434, 435);
		// Bronzor
		e(436, 437);
		// Gible
		e(443, 444);
		e(444, 445);
		// Riolu
		e(447, 448);
		// Hippopotas
		e(449, 450);
		// Skorupi
		e(451, 452);
		// Croagunk
		e(453, 454);
		// Finneon
		e(456, 457);
		// Snover
		e(459, 460);
		// UNOVA
		// Starters
		e(495, 496);
		e(496, 497);
		e(498, 499);
		e(499, 500);
		e(501, 502);
		e(502, 503);
		// Patrat
		e(504, 505);
		// Lillipup
		e(506, 507);
		e(507, 508);
		// Purrloin
		e(509, 510);
		// Elemental Monkeys
		e(511, 512);
		e(513, 514);
		e(515, 516);
		// Munna
		e(517, 518);
		// Pidove
		e(519, 520);
		e(520, 521);
		// Blitzle
		e(522, 523);
		// Roggenrola
		e(524, 525);
		e(525, 526);
		// Woobat
		e(527, 528);
		// Drilbur
		e(529, 530);
		// Timburr
		e(532, 533);
		e(533, 534);
		// Tympole
		e(535, 536);
		e(536, 537);
		// Sewaddle
		e(540, 541);
		e(541, 542);
		// Venipede
		e(543, 544);
		e(544, 545);
		// Cottonee
		e(546, 547);
		// Petilil
		e(548, 549);
		// Sandile
		e(551, 552);
		e(552, 553);
		// Darumaka
		e(554, 555);
		// Dwebble
		e(557, 558);
		// Scraggy
		e(559, 560);
		// Yamask
		e(562, 563);
		// Tirtouga
		e(564, 565);
		// Archen
		e(566, 567);
		// Trubbish
		e(568, 569);
		// Zorua
		e(570, 571);
		// Minccino
		e(572, 573);
		// Psychics
		e(574, 575);
		e(575, 576);
		e(577, 578);
		e(578, 579);
		// Ducklett
		e(580, 581);
		// Vanillite
		e(582, 583);
		e(583, 584);
		// Deerling
		e(585, 586);
		// Karrablast
		e(588, 589);
		// Foongus
		e(590, 591);
		// Frillish
		e(592, 593);
		// Joltik
		e(595, 596);
		// Ferroseed
		e(597, 598);
		// Klink
		e(599, 600);
		e(600, 601);
		// Tynamo
		e(602, 603);
		e(603, 604);
		// Elgyem
		e(605, 606);
		// Litwick
		e(607, 608);
		e(608, 609);
		// Axew
		e(610, 611);
		e(611, 612);
		// Cubchoo
		e(613, 614);
		// Shelmet
		e(616, 617);
		// Mienfoo
		e(619, 620);
		// Golett
		e(622, 623);
		// Pawniard
		e(624, 625);
		// Rufflet
		e(627, 628);
		// Vullaby
		e(629, 630);
		// Deino
		e(633, 634);
		e(634, 635);
		// Larvesta
		e(636, 637);
	}

	private static void e(int from, int to, boolean carryStats) {
		evolution(from, to, carryStats);
	}

	private static void e(int from, int to) {
		evolution(from, to, true);
	}

	private static void evolution(int from, int to, boolean carryStats) {
		Evolution evo = new Evolution();
		evo.from = from;
		evo.to = to;
		evo.carryStats = carryStats;
		evolutions.add(evo);
	}

	public static void debugPrint() {
		for (Evolution evo : evolutions) {
			System.out.println(evo);
		}
	}

	public static List<Evolution> evosFor(RomHandler thisROM) {
		List<Evolution> evos = new ArrayList<Evolution>();
		for (Evolution e : evolutions) {
			if (thisROM.isInGame(e.from) && thisROM.isInGame(e.to)) {
				evos.add(e);
			}
		}
		return evos;
	}

}
