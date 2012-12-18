Universal Pokemon Randomizers 1.0.0
by Dabomstew, 2012

Homepage: http://pokehacks.dabomstew.com/randomizer/index.php
Source: https://code.google.com/p/universal-pokemon-randomizer/

Contents
--------
1. Introduction
2. Acknowledgements
3. Libraries Used
4. Features
5. How To Use
6. Games/ROMs supported
7. License
8. Known Issues
9. Contact

Introduction
------------

This program allows you to customize your experience playing the Pokemon games
by randomizing many aspects of them. This means that the Pokemon you get at
the start of the game, the Pokemon you fight in the wild and the Pokemon 
trainers have can all be made completely different from the original game.

Acknowledgements
----------------
Many people have put countless hours of their time into researching the
structures contained within Pokemon games over the years. Without the research
done by these people, this randomizer would not exist, or would have taken a
lot longer to create.

To see the full list of contributions, see 
http://pokehacks.dabomstew.com/randomizer/acks.php

Libraries Used
--------------
  * thenewpoketext by loadingNOW for generation 4 text handling
    http://pokeguide.filb.de/ (source @ https://github.com/magical/ppre )
  * PPTXT by ProjectPokemon for generation 5 text handling
    http://projectpokemon.org/forums/showthread.php?11582-PPTXT-Text-editing-tool
  * Code from ndstool for NDS file extraction/creation (under GPL)
    http://sourceforge.net/p/devkitpro/ndstool/
  * Code from CUE's Nintendo DS Compressors for arm9.bin (de)compressing
    (under GPL)
	http://gbatemp.net/threads/nintendo-ds-gba-compressors.313278/
  * DSDecmp for LZ11 decompression (under MIT)
    http://code.google.com/p/dsdecmp/
 
Features
--------
Below is a list of what exactly can be randomized. You may not understand all
of it if you haven't played Pokemon games much before.
 
  * The Starter Pokemon choices
  * The Wild Pokemon you encounter in grass, caves and other places
  * The Pokemon that Trainers use against you.
  * The base stats which define the potential of each Pokemon
  * The elemental types of each Pokemon
  * The moves that Pokemon learn by gaining levels
  * The contents of each TM which can be taught to Pokemon to give them
    additional moves 
	(HM moves are not changed to make sure you can still beat the game)
  * The ability of each Pokemon to learn each TM or HM move
  * The "static" Pokemon which you either are given, fight on the overworld,
    or are sold.
  * The names of trainers & the classes they belong in
  
How To Use
----------
Extract this ZIP file before doing anything else!!!

Make sure you have Java 1.6 installed, then run the included JAR file.

In some situations, you will be able to just double-click on the JAR file and
the program will run. If not, execute the following command from your command
line in the directory where you have extracted the program:

java -jar randomizer.jar

From there you can open a ROM (legally acquired), customize what you want to be
randomized, then save the randomized ROM.

Games/ROMs supported
--------------------

Version 1.0.0 supports the following ROMs:

  * Pokemon Red (U/E)
  * Pokemon Blue (U/E)
  * Pokemon Yellow (U/E)
  * Pokemon Gold (U/E)
  * Pokemon Silver (U/E)
  * Pokemon Crystal (U/E)
  * Pokemon Ruby (U) and (E)
  * Pokemon Sapphire (U) and (E)
  * Pokemon Emerald (U)
  * Pokemon FireRed (U) (1.0 and 1.1)
  * Pokemon LeafGreen (U) (1.0 and 1.1)
  * Pokemon Diamond (U)
  * Pokemon Pearl (U)
  * Pokemon Platinum (U)
  * Pokemon HeartGold (U)
  * Pokemon SoulSilver (U)
  * Pokemon Black (U)
  * Pokemon White (U)
  * Pokemon Black2 (U)
  * Pokemon White2 (U)
  
As you can see, pretty much every US 1.0 game is supported. Future releases
should improve the compatibility with foreign-language games, though the DS
games may work right now anyway, albeit with some random English text inserted.

License
-------
This project and the majority of the libraries used are under the GNU GPL v3,
attached as LICENSE.txt.

Source code can be obtained from:
https://code.google.com/p/universal-pokemon-randomizer/source/browse/

Other libraries used are under more liberal licenses, compatible with the GPL.

Known Issues
------------
See https://code.google.com/p/universal-pokemon-randomizer/wiki/KnownIssues

Contact
-------
If you have bugs, suggestions, or other concerns to tell me, contact me at
http://pokehacks.dabomstew.com/randomizer/comments.php