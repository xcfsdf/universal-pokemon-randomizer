/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dabomstew.pkrandom.gui;

/*----------------------------------------------------------------------------*/
/*--  RandomizerGUI.java - the main GUI for the randomizer, containing the	--*/
/*--					   various options available and such.				--*/
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.zip.CRC32;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.DatatypeConverter;

import com.dabomstew.pkrandom.FileFunctions;
import com.dabomstew.pkrandom.RandomSource;
import com.dabomstew.pkrandom.RomFunctions;
import com.dabomstew.pkrandom.pokemon.Encounter;
import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.MoveLearnt;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.pokemon.Trainer;
import com.dabomstew.pkrandom.pokemon.TrainerPokemon;
import com.dabomstew.pkrandom.romhandlers.Gen1RomHandler;
import com.dabomstew.pkrandom.romhandlers.Gen2RomHandler;
import com.dabomstew.pkrandom.romhandlers.Gen3RomHandler;
import com.dabomstew.pkrandom.romhandlers.Gen4RomHandler;
import com.dabomstew.pkrandom.romhandlers.Gen5RomHandler;
import com.dabomstew.pkrandom.romhandlers.RomHandler;

/**
 * 
 * @author Stewart
 */
public class RandomizerGUI extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 637989089525556154L;
	private RomHandler romHandler;
	protected RomHandler[] checkHandlers;
	public static final byte PRESET_FILE_VERSION = 120;
	public static final int UPDATE_VERSION = 1200;

	public static PrintStream verboseLog = System.out;

	public OperationDialog opDialog;
	public boolean presetMode;

	/**
	 * Creates new form RandomizerGUI
	 */
	public RandomizerGUI() {
		testForRequiredConfigs();
		checkHandlers = new RomHandler[] { new Gen1RomHandler(),
				new Gen2RomHandler(), new Gen3RomHandler(),
				new Gen4RomHandler(), new Gen5RomHandler() };
		initComponents();
		initialiseState();
		setLocationRelativeTo(null);
		new UpdateCheckThread(this, false).start();
	}

	public void testForRequiredConfigs() {
		String[] required = new String[] { "gameboy_jap.tbl",
				"rby_english.tbl", "green_bootleg.tbl", "gsc_english.tbl",
				"gba_english.tbl", "gba_jap.tbl", "Generation4.tbl",
				"Generation5.tbl", "gen1_offsets.ini", "gen2_offsets.ini",
				"gen3_offsets.ini", "gen4_offsets.ini", "gen5_offsets.ini",
				"trainerclasses.txt", "trainernames.txt" };
		for (String filename : required) {
			if (!FileFunctions.configExists(filename)) {
				JOptionPane
						.showMessageDialog(
								null,
								"The file "
										+ filename
										+ " is missing from the configuration and so this program cannot start.\n"
										+ "Please make sure you extract the program from the ZIP file before running it.");
				System.exit(1);
				return;
			}
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		pokeStatChangesButtonGroup = new javax.swing.ButtonGroup();
		pokeTypesButtonGroup = new javax.swing.ButtonGroup();
		pokeMovesetsButtonGroup = new javax.swing.ButtonGroup();
		trainerPokesButtonGroup = new javax.swing.ButtonGroup();
		wildPokesButtonGroup = new javax.swing.ButtonGroup();
		wildPokesARuleButtonGroup = new javax.swing.ButtonGroup();
		starterPokemonButtonGroup = new javax.swing.ButtonGroup();
		romOpenChooser = new javax.swing.JFileChooser();
		romSaveChooser = new javax.swing.JFileChooser();
		qsOpenChooser = new javax.swing.JFileChooser();
		qsSaveChooser = new javax.swing.JFileChooser();
		staticPokemonButtonGroup = new javax.swing.ButtonGroup();
		tmMovesButtonGroup = new javax.swing.ButtonGroup();
		tmHmCompatibilityButtonGroup = new javax.swing.ButtonGroup();
		pokeAbilitiesButtonGroup = new javax.swing.ButtonGroup();
		mtMovesButtonGroup = new javax.swing.ButtonGroup();
		mtCompatibilityButtonGroup = new javax.swing.ButtonGroup();
		generalOptionsPanel = new javax.swing.JPanel();
		goUpdateTypesCheckBox = new javax.swing.JCheckBox();
		goUpdateMovesCheckBox = new javax.swing.JCheckBox();
		goRemoveTradeEvosCheckBox = new javax.swing.JCheckBox();
		goLowerCaseNamesCheckBox = new javax.swing.JCheckBox();
		goNationalDexCheckBox = new javax.swing.JCheckBox();
		romInfoPanel = new javax.swing.JPanel();
		riRomNameLabel = new javax.swing.JLabel();
		riRomCodeLabel = new javax.swing.JLabel();
		riRomSupportLabel = new javax.swing.JLabel();
		optionsScrollPane = new javax.swing.JScrollPane();
		optionsContainerPanel = new javax.swing.JPanel();
		baseStatsPanel = new javax.swing.JPanel();
		pbsChangesUnchangedRB = new javax.swing.JRadioButton();
		pbsChangesShuffleRB = new javax.swing.JRadioButton();
		pbsChangesRandomEvosRB = new javax.swing.JRadioButton();
		pbsChangesRandomTotalRB = new javax.swing.JRadioButton();
		pokemonTypesPanel = new javax.swing.JPanel();
		ptUnchangedRB = new javax.swing.JRadioButton();
		ptRandomFollowEvosRB = new javax.swing.JRadioButton();
		ptRandomTotalRB = new javax.swing.JRadioButton();
		pokemonMovesetsPanel = new javax.swing.JPanel();
		pmsUnchangedRB = new javax.swing.JRadioButton();
		pmsRandomTypeRB = new javax.swing.JRadioButton();
		pmsRandomTotalRB = new javax.swing.JRadioButton();
		trainersPokemonPanel = new javax.swing.JPanel();
		tpUnchangedRB = new javax.swing.JRadioButton();
		tpRandomRB = new javax.swing.JRadioButton();
		tpTypeThemedRB = new javax.swing.JRadioButton();
		tpPowerLevelsCB = new javax.swing.JCheckBox();
		tpTypeWeightingCB = new javax.swing.JCheckBox();
		tpRivalCarriesStarterCB = new javax.swing.JCheckBox();
		tpNoLegendariesCB = new javax.swing.JCheckBox();
		tnRandomizeCB = new javax.swing.JCheckBox();
		tcnRandomizeCB = new javax.swing.JCheckBox();
		tpNoEarlyShedinjaCB = new javax.swing.JCheckBox();
		wildPokemonPanel = new javax.swing.JPanel();
		wpUnchangedRB = new javax.swing.JRadioButton();
		wpRandomRB = new javax.swing.JRadioButton();
		wpArea11RB = new javax.swing.JRadioButton();
		wpGlobalRB = new javax.swing.JRadioButton();
		wildPokemonARulePanel = new javax.swing.JPanel();
		wpARNoneRB = new javax.swing.JRadioButton();
		wpARCatchEmAllRB = new javax.swing.JRadioButton();
		wpARTypeThemedRB = new javax.swing.JRadioButton();
		wpUseTimeCB = new javax.swing.JCheckBox();
		wpNoLegendariesCB = new javax.swing.JCheckBox();
		wpCatchRateCB = new javax.swing.JCheckBox();
		starterPokemonPanel = new javax.swing.JPanel();
		spUnchangedRB = new javax.swing.JRadioButton();
		spCustomRB = new javax.swing.JRadioButton();
		spCustomPoke1Chooser = new javax.swing.JComboBox();
		spCustomPoke2Chooser = new javax.swing.JComboBox();
		spCustomPoke3Chooser = new javax.swing.JComboBox();
		spRandomRB = new javax.swing.JRadioButton();
		spRandom2EvosRB = new javax.swing.JRadioButton();
		staticPokemonPanel = new javax.swing.JPanel();
		stpUnchangedRB = new javax.swing.JRadioButton();
		stpRandomL4LRB = new javax.swing.JRadioButton();
		stpRandomTotalRB = new javax.swing.JRadioButton();
		tmhmsPanel = new javax.swing.JPanel();
		tmMovesPanel = new javax.swing.JPanel();
		tmmUnchangedRB = new javax.swing.JRadioButton();
		tmmRandomRB = new javax.swing.JRadioButton();
		tmHmCompatPanel = new javax.swing.JPanel();
		thcUnchangedRB = new javax.swing.JRadioButton();
		thcRandomTypeRB = new javax.swing.JRadioButton();
		thcRandomTotalRB = new javax.swing.JRadioButton();
		abilitiesPanel = new javax.swing.JPanel();
		paUnchangedRB = new javax.swing.JRadioButton();
		paRandomizeRB = new javax.swing.JRadioButton();
		paWonderGuardCB = new javax.swing.JCheckBox();
		moveTutorsPanel = new javax.swing.JPanel();
		mtMovesPanel = new javax.swing.JPanel();
		mtmUnchangedRB = new javax.swing.JRadioButton();
		mtmRandomRB = new javax.swing.JRadioButton();
		mtCompatPanel = new javax.swing.JPanel();
		mtcUnchangedRB = new javax.swing.JRadioButton();
		mtcRandomTypeRB = new javax.swing.JRadioButton();
		mtcRandomTotalRB = new javax.swing.JRadioButton();
		mtNoExistLabel = new javax.swing.JLabel();
		openROMButton = new javax.swing.JButton();
		saveROMButton = new javax.swing.JButton();
		usePresetsButton = new javax.swing.JButton();
		aboutButton = new javax.swing.JButton();
		otherOptionsPanel = new javax.swing.JPanel();
		bwEXPPatchCB = new javax.swing.JCheckBox();
		raceModeCB = new javax.swing.JCheckBox();
		randomizeHollowsCB = new javax.swing.JCheckBox();
		brokenMovesCB = new javax.swing.JCheckBox();
		heldItemsCB = new javax.swing.JCheckBox();
		loadQSButton = new javax.swing.JButton();
		saveQSButton = new javax.swing.JButton();

		romOpenChooser.setFileFilter(new ROMFilter());

		romSaveChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
		romSaveChooser.setFileFilter(new ROMFilter());

		qsOpenChooser.setFileFilter(new QSFileFilter());

		qsSaveChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
		qsSaveChooser.setFileFilter(new QSFileFilter());

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		java.util.ResourceBundle bundle = java.util.ResourceBundle
				.getBundle("com/dabomstew/pkrandom/gui/Bundle"); // NOI18N
		setTitle(bundle.getString("RandomizerGUI.title")); // NOI18N

		generalOptionsPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.generalOptionsPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		goUpdateTypesCheckBox.setText(bundle
				.getString("RandomizerGUI.goUpdateTypesCheckBox.text")); // NOI18N
		goUpdateTypesCheckBox.setToolTipText(bundle
				.getString("RandomizerGUI.goUpdateTypesCheckBox.toolTipText")); // NOI18N

		goUpdateMovesCheckBox.setText(bundle
				.getString("RandomizerGUI.goUpdateMovesCheckBox.text")); // NOI18N
		goUpdateMovesCheckBox.setToolTipText(bundle
				.getString("RandomizerGUI.goUpdateMovesCheckBox.toolTipText")); // NOI18N

		goRemoveTradeEvosCheckBox.setText(bundle
				.getString("RandomizerGUI.goRemoveTradeEvosCheckBox.text")); // NOI18N
		goRemoveTradeEvosCheckBox
				.setToolTipText(bundle
						.getString("RandomizerGUI.goRemoveTradeEvosCheckBox.toolTipText")); // NOI18N

		goLowerCaseNamesCheckBox.setText(bundle
				.getString("RandomizerGUI.goLowerCaseNamesCheckBox.text")); // NOI18N
		goLowerCaseNamesCheckBox
				.setToolTipText(bundle
						.getString("RandomizerGUI.goLowerCaseNamesCheckBox.toolTipText")); // NOI18N

		goNationalDexCheckBox.setText(bundle
				.getString("RandomizerGUI.goNationalDexCheckBox.text")); // NOI18N
		goNationalDexCheckBox.setToolTipText(bundle
				.getString("RandomizerGUI.goNationalDexCheckBox.toolTipText")); // NOI18N

		javax.swing.GroupLayout generalOptionsPanelLayout = new javax.swing.GroupLayout(
				generalOptionsPanel);
		generalOptionsPanel.setLayout(generalOptionsPanelLayout);
		generalOptionsPanelLayout
				.setHorizontalGroup(generalOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								generalOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												generalOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																goUpdateTypesCheckBox)
														.addComponent(
																goUpdateMovesCheckBox)
														.addComponent(
																goRemoveTradeEvosCheckBox)
														.addComponent(
																goLowerCaseNamesCheckBox)
														.addComponent(
																goNationalDexCheckBox))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		generalOptionsPanelLayout
				.setVerticalGroup(generalOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								generalOptionsPanelLayout
										.createSequentialGroup()
										.addComponent(goUpdateTypesCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(goUpdateMovesCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(goRemoveTradeEvosCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(goLowerCaseNamesCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(goNationalDexCheckBox)));

		romInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null,
				bundle.getString("RandomizerGUI.romInfoPanel.border.title"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		riRomNameLabel.setText(bundle
				.getString("RandomizerGUI.riRomNameLabel.text")); // NOI18N

		riRomCodeLabel.setText(bundle
				.getString("RandomizerGUI.riRomCodeLabel.text")); // NOI18N

		riRomSupportLabel.setText(bundle
				.getString("RandomizerGUI.riRomSupportLabel.text")); // NOI18N

		javax.swing.GroupLayout romInfoPanelLayout = new javax.swing.GroupLayout(
				romInfoPanel);
		romInfoPanel.setLayout(romInfoPanelLayout);
		romInfoPanelLayout
				.setHorizontalGroup(romInfoPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								romInfoPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												romInfoPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																riRomNameLabel)
														.addComponent(
																riRomCodeLabel)
														.addComponent(
																riRomSupportLabel))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		romInfoPanelLayout
				.setVerticalGroup(romInfoPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								romInfoPanelLayout
										.createSequentialGroup()
										.addGap(5, 5, 5)
										.addComponent(riRomNameLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(riRomCodeLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(riRomSupportLabel)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		baseStatsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null,
				bundle.getString("RandomizerGUI.baseStatsPanel.border.title"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		pokeStatChangesButtonGroup.add(pbsChangesUnchangedRB);
		pbsChangesUnchangedRB.setText(bundle
				.getString("RandomizerGUI.pbsChangesUnchangedRB.text")); // NOI18N
		pbsChangesUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.pbsChangesUnchangedRB.toolTipText")); // NOI18N

		pokeStatChangesButtonGroup.add(pbsChangesShuffleRB);
		pbsChangesShuffleRB.setText(bundle
				.getString("RandomizerGUI.pbsChangesShuffleRB.text")); // NOI18N
		pbsChangesShuffleRB.setToolTipText(bundle
				.getString("RandomizerGUI.pbsChangesShuffleRB.toolTipText")); // NOI18N

		pokeStatChangesButtonGroup.add(pbsChangesRandomEvosRB);
		pbsChangesRandomEvosRB.setText(bundle
				.getString("RandomizerGUI.pbsChangesRandomEvosRB.text")); // NOI18N
		pbsChangesRandomEvosRB.setToolTipText(bundle
				.getString("RandomizerGUI.pbsChangesRandomEvosRB.toolTipText")); // NOI18N

		pokeStatChangesButtonGroup.add(pbsChangesRandomTotalRB);
		pbsChangesRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.pbsChangesRandomTotalRB.text")); // NOI18N
		pbsChangesRandomTotalRB
				.setToolTipText(bundle
						.getString("RandomizerGUI.pbsChangesRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout baseStatsPanelLayout = new javax.swing.GroupLayout(
				baseStatsPanel);
		baseStatsPanel.setLayout(baseStatsPanelLayout);
		baseStatsPanelLayout
				.setHorizontalGroup(baseStatsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								baseStatsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												baseStatsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																pbsChangesUnchangedRB)
														.addComponent(
																pbsChangesShuffleRB)
														.addComponent(
																pbsChangesRandomEvosRB)
														.addComponent(
																pbsChangesRandomTotalRB))
										.addContainerGap(196, Short.MAX_VALUE)));
		baseStatsPanelLayout
				.setVerticalGroup(baseStatsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								baseStatsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(pbsChangesUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pbsChangesShuffleRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pbsChangesRandomEvosRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pbsChangesRandomTotalRB)
										.addGap(27, 27, 27)));

		pokemonTypesPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.pokemonTypesPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		pokeTypesButtonGroup.add(ptUnchangedRB);
		ptUnchangedRB.setSelected(true);
		ptUnchangedRB.setText(bundle
				.getString("RandomizerGUI.ptUnchangedRB.text")); // NOI18N
		ptUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.ptUnchangedRB.toolTipText")); // NOI18N

		pokeTypesButtonGroup.add(ptRandomFollowEvosRB);
		ptRandomFollowEvosRB.setText(bundle
				.getString("RandomizerGUI.ptRandomFollowEvosRB.text")); // NOI18N
		ptRandomFollowEvosRB.setToolTipText(bundle
				.getString("RandomizerGUI.ptRandomFollowEvosRB.toolTipText")); // NOI18N

		pokeTypesButtonGroup.add(ptRandomTotalRB);
		ptRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.ptRandomTotalRB.text")); // NOI18N
		ptRandomTotalRB.setToolTipText(bundle
				.getString("RandomizerGUI.ptRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout pokemonTypesPanelLayout = new javax.swing.GroupLayout(
				pokemonTypesPanel);
		pokemonTypesPanel.setLayout(pokemonTypesPanelLayout);
		pokemonTypesPanelLayout
				.setHorizontalGroup(pokemonTypesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pokemonTypesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pokemonTypesPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																ptUnchangedRB)
														.addComponent(
																ptRandomFollowEvosRB)
														.addComponent(
																ptRandomTotalRB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		pokemonTypesPanelLayout
				.setVerticalGroup(pokemonTypesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pokemonTypesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(ptUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(ptRandomFollowEvosRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(ptRandomTotalRB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		pokemonMovesetsPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.pokemonMovesetsPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		pokeMovesetsButtonGroup.add(pmsUnchangedRB);
		pmsUnchangedRB.setSelected(true);
		pmsUnchangedRB.setText(bundle
				.getString("RandomizerGUI.pmsUnchangedRB.text")); // NOI18N
		pmsUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.pmsUnchangedRB.toolTipText")); // NOI18N

		pokeMovesetsButtonGroup.add(pmsRandomTypeRB);
		pmsRandomTypeRB.setText(bundle
				.getString("RandomizerGUI.pmsRandomTypeRB.text")); // NOI18N
		pmsRandomTypeRB.setToolTipText(bundle
				.getString("RandomizerGUI.pmsRandomTypeRB.toolTipText")); // NOI18N

		pokeMovesetsButtonGroup.add(pmsRandomTotalRB);
		pmsRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.pmsRandomTotalRB.text")); // NOI18N
		pmsRandomTotalRB.setToolTipText(bundle
				.getString("RandomizerGUI.pmsRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout pokemonMovesetsPanelLayout = new javax.swing.GroupLayout(
				pokemonMovesetsPanel);
		pokemonMovesetsPanel.setLayout(pokemonMovesetsPanelLayout);
		pokemonMovesetsPanelLayout
				.setHorizontalGroup(pokemonMovesetsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pokemonMovesetsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												pokemonMovesetsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																pmsUnchangedRB)
														.addComponent(
																pmsRandomTypeRB)
														.addComponent(
																pmsRandomTotalRB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		pokemonMovesetsPanelLayout
				.setVerticalGroup(pokemonMovesetsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								pokemonMovesetsPanelLayout
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(pmsUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pmsRandomTypeRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(pmsRandomTotalRB)));

		trainersPokemonPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.trainersPokemonPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		trainerPokesButtonGroup.add(tpUnchangedRB);
		tpUnchangedRB.setSelected(true);
		tpUnchangedRB.setText(bundle
				.getString("RandomizerGUI.tpUnchangedRB.text")); // NOI18N
		tpUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.tpUnchangedRB.toolTipText")); // NOI18N
		tpUnchangedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tpUnchangedRBActionPerformed(evt);
			}
		});

		trainerPokesButtonGroup.add(tpRandomRB);
		tpRandomRB.setText(bundle.getString("RandomizerGUI.tpRandomRB.text")); // NOI18N
		tpRandomRB.setToolTipText(bundle
				.getString("RandomizerGUI.tpRandomRB.toolTipText")); // NOI18N
		tpRandomRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tpRandomRBActionPerformed(evt);
			}
		});

		trainerPokesButtonGroup.add(tpTypeThemedRB);
		tpTypeThemedRB.setText(bundle
				.getString("RandomizerGUI.tpTypeThemedRB.text")); // NOI18N
		tpTypeThemedRB.setToolTipText(bundle
				.getString("RandomizerGUI.tpTypeThemedRB.toolTipText")); // NOI18N
		tpTypeThemedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tpTypeThemedRBActionPerformed(evt);
			}
		});

		tpPowerLevelsCB.setText(bundle
				.getString("RandomizerGUI.tpPowerLevelsCB.text")); // NOI18N
		tpPowerLevelsCB.setToolTipText(bundle
				.getString("RandomizerGUI.tpPowerLevelsCB.toolTipText")); // NOI18N
		tpPowerLevelsCB.setEnabled(false);

		tpTypeWeightingCB.setText(bundle
				.getString("RandomizerGUI.tpTypeWeightingCB.text")); // NOI18N
		tpTypeWeightingCB.setToolTipText(bundle
				.getString("RandomizerGUI.tpTypeWeightingCB.toolTipText")); // NOI18N
		tpTypeWeightingCB.setEnabled(false);

		tpRivalCarriesStarterCB.setText(bundle
				.getString("RandomizerGUI.tpRivalCarriesStarterCB.text")); // NOI18N
		tpRivalCarriesStarterCB
				.setToolTipText(bundle
						.getString("RandomizerGUI.tpRivalCarriesStarterCB.toolTipText")); // NOI18N
		tpRivalCarriesStarterCB.setEnabled(false);

		tpNoLegendariesCB.setText(bundle
				.getString("RandomizerGUI.tpNoLegendariesCB.text")); // NOI18N
		tpNoLegendariesCB.setEnabled(false);

		tnRandomizeCB.setText(bundle
				.getString("RandomizerGUI.tnRandomizeCB.text")); // NOI18N
		tnRandomizeCB.setToolTipText(bundle
				.getString("RandomizerGUI.tnRandomizeCB.toolTipText")); // NOI18N

		tcnRandomizeCB.setText(bundle
				.getString("RandomizerGUI.tcnRandomizeCB.text")); // NOI18N
		tcnRandomizeCB.setToolTipText(bundle
				.getString("RandomizerGUI.tcnRandomizeCB.toolTipText")); // NOI18N

		tpNoEarlyShedinjaCB.setText(bundle
				.getString("RandomizerGUI.tpNoEarlyShedinjaCB.text")); // NOI18N
		tpNoEarlyShedinjaCB.setToolTipText(bundle
				.getString("RandomizerGUI.tpNoEarlyShedinjaCB.toolTipText")); // NOI18N

		javax.swing.GroupLayout trainersPokemonPanelLayout = new javax.swing.GroupLayout(
				trainersPokemonPanel);
		trainersPokemonPanel.setLayout(trainersPokemonPanelLayout);
		trainersPokemonPanelLayout
				.setHorizontalGroup(trainersPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								trainersPokemonPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												trainersPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																tpTypeThemedRB)
														.addGroup(
																trainersPokemonPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				trainersPokemonPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								tpUnchangedRB)
																						.addComponent(
																								tpRandomRB))
																		.addGap(47,
																				47,
																				47)
																		.addGroup(
																				trainersPokemonPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								tpNoEarlyShedinjaCB)
																						.addGroup(
																								trainersPokemonPanelLayout
																										.createSequentialGroup()
																										.addGroup(
																												trainersPokemonPanelLayout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING,
																																false)
																														.addComponent(
																																tpTypeWeightingCB,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE)
																														.addComponent(
																																tpRivalCarriesStarterCB,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE)
																														.addComponent(
																																tpPowerLevelsCB,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE)
																														.addComponent(
																																tpNoLegendariesCB,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																Short.MAX_VALUE))
																										.addGap(18,
																												18,
																												18)
																										.addGroup(
																												trainersPokemonPanelLayout
																														.createParallelGroup(
																																javax.swing.GroupLayout.Alignment.LEADING)
																														.addComponent(
																																tnRandomizeCB)
																														.addComponent(
																																tcnRandomizeCB))))))
										.addContainerGap(62, Short.MAX_VALUE)));
		trainersPokemonPanelLayout
				.setVerticalGroup(trainersPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								trainersPokemonPanelLayout
										.createSequentialGroup()
										.addGroup(
												trainersPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																tpUnchangedRB)
														.addComponent(
																tpRivalCarriesStarterCB)
														.addComponent(
																tnRandomizeCB))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												trainersPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																tpRandomRB)
														.addComponent(
																tpPowerLevelsCB)
														.addComponent(
																tcnRandomizeCB))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												trainersPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																tpTypeThemedRB)
														.addComponent(
																tpTypeWeightingCB))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(tpNoLegendariesCB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(tpNoEarlyShedinjaCB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		wildPokemonPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.wildPokemonPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		wildPokesButtonGroup.add(wpUnchangedRB);
		wpUnchangedRB.setSelected(true);
		wpUnchangedRB.setText(bundle
				.getString("RandomizerGUI.wpUnchangedRB.text")); // NOI18N
		wpUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpUnchangedRB.toolTipText")); // NOI18N
		wpUnchangedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				wpUnchangedRBActionPerformed(evt);
			}
		});

		wildPokesButtonGroup.add(wpRandomRB);
		wpRandomRB.setText(bundle.getString("RandomizerGUI.wpRandomRB.text")); // NOI18N
		wpRandomRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpRandomRB.toolTipText")); // NOI18N
		wpRandomRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				wpRandomRBActionPerformed(evt);
			}
		});

		wildPokesButtonGroup.add(wpArea11RB);
		wpArea11RB.setText(bundle.getString("RandomizerGUI.wpArea11RB.text")); // NOI18N
		wpArea11RB.setToolTipText(bundle
				.getString("RandomizerGUI.wpArea11RB.toolTipText")); // NOI18N
		wpArea11RB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				wpArea11RBActionPerformed(evt);
			}
		});

		wildPokesButtonGroup.add(wpGlobalRB);
		wpGlobalRB.setText(bundle.getString("RandomizerGUI.wpGlobalRB.text")); // NOI18N
		wpGlobalRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpGlobalRB.toolTipText")); // NOI18N
		wpGlobalRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				wpGlobalRBActionPerformed(evt);
			}
		});

		wildPokemonARulePanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle
						.getString("RandomizerGUI.wildPokemonARulePanel.border.title"))); // NOI18N

		wildPokesARuleButtonGroup.add(wpARNoneRB);
		wpARNoneRB.setSelected(true);
		wpARNoneRB.setText(bundle.getString("RandomizerGUI.wpARNoneRB.text")); // NOI18N
		wpARNoneRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpARNoneRB.toolTipText")); // NOI18N
		wpARNoneRB.setEnabled(false);

		wildPokesARuleButtonGroup.add(wpARCatchEmAllRB);
		wpARCatchEmAllRB.setText(bundle
				.getString("RandomizerGUI.wpARCatchEmAllRB.text")); // NOI18N
		wpARCatchEmAllRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpARCatchEmAllRB.toolTipText")); // NOI18N
		wpARCatchEmAllRB.setEnabled(false);

		wildPokesARuleButtonGroup.add(wpARTypeThemedRB);
		wpARTypeThemedRB.setText(bundle
				.getString("RandomizerGUI.wpARTypeThemedRB.text")); // NOI18N
		wpARTypeThemedRB.setToolTipText(bundle
				.getString("RandomizerGUI.wpARTypeThemedRB.toolTipText")); // NOI18N
		wpARTypeThemedRB.setEnabled(false);

		javax.swing.GroupLayout wildPokemonARulePanelLayout = new javax.swing.GroupLayout(
				wildPokemonARulePanel);
		wildPokemonARulePanel.setLayout(wildPokemonARulePanelLayout);
		wildPokemonARulePanelLayout
				.setHorizontalGroup(wildPokemonARulePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								wildPokemonARulePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												wildPokemonARulePanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																wpARNoneRB)
														.addComponent(
																wpARCatchEmAllRB)
														.addComponent(
																wpARTypeThemedRB))
										.addContainerGap(30, Short.MAX_VALUE)));
		wildPokemonARulePanelLayout
				.setVerticalGroup(wildPokemonARulePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								wildPokemonARulePanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(wpARNoneRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(wpARCatchEmAllRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												2, Short.MAX_VALUE)
										.addComponent(wpARTypeThemedRB)));

		wpUseTimeCB.setText(bundle.getString("RandomizerGUI.wpUseTimeCB.text")); // NOI18N
		wpUseTimeCB.setToolTipText(bundle
				.getString("RandomizerGUI.wpUseTimeCB.toolTipText")); // NOI18N

		wpNoLegendariesCB.setText(bundle
				.getString("RandomizerGUI.wpNoLegendariesCB.text")); // NOI18N

		wpCatchRateCB.setText(bundle
				.getString("RandomizerGUI.wpCatchRateCB.text")); // NOI18N
		wpCatchRateCB.setToolTipText(bundle
				.getString("RandomizerGUI.wpCatchRateCB.toolTipText")); // NOI18N

		javax.swing.GroupLayout wildPokemonPanelLayout = new javax.swing.GroupLayout(
				wildPokemonPanel);
		wildPokemonPanel.setLayout(wildPokemonPanelLayout);
		wildPokemonPanelLayout
				.setHorizontalGroup(wildPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								wildPokemonPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												wildPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																wpUnchangedRB)
														.addComponent(
																wpRandomRB)
														.addComponent(
																wpArea11RB)
														.addComponent(
																wpGlobalRB))
										.addGap(18, 18, 18)
										.addComponent(
												wildPokemonARulePanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addGroup(
												wildPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																wpUseTimeCB)
														.addComponent(
																wpNoLegendariesCB)
														.addComponent(
																wpCatchRateCB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		wildPokemonPanelLayout
				.setVerticalGroup(wildPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.TRAILING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.LEADING,
								wildPokemonPanelLayout
										.createSequentialGroup()
										.addComponent(
												wildPokemonARulePanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
						.addGroup(
								javax.swing.GroupLayout.Alignment.LEADING,
								wildPokemonPanelLayout
										.createSequentialGroup()
										.addGroup(
												wildPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																wildPokemonPanelLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				wpUnchangedRB)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				wpRandomRB)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				wpArea11RB)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				wpGlobalRB))
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																wildPokemonPanelLayout
																		.createSequentialGroup()
																		.addGap(28,
																				28,
																				28)
																		.addComponent(
																				wpUseTimeCB)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				wpNoLegendariesCB)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				wpCatchRateCB)))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		starterPokemonPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.starterPokemonPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		starterPokemonButtonGroup.add(spUnchangedRB);
		spUnchangedRB.setSelected(true);
		spUnchangedRB.setText(bundle
				.getString("RandomizerGUI.spUnchangedRB.text")); // NOI18N
		spUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.spUnchangedRB.toolTipText")); // NOI18N
		spUnchangedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spUnchangedRBActionPerformed(evt);
			}
		});

		starterPokemonButtonGroup.add(spCustomRB);
		spCustomRB.setText(bundle.getString("RandomizerGUI.spCustomRB.text")); // NOI18N
		spCustomRB.setToolTipText(bundle
				.getString("RandomizerGUI.spCustomRB.toolTipText")); // NOI18N
		spCustomRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spCustomRBActionPerformed(evt);
			}
		});

		spCustomPoke1Chooser.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		spCustomPoke1Chooser.setEnabled(false);

		spCustomPoke2Chooser.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		spCustomPoke2Chooser.setEnabled(false);

		spCustomPoke3Chooser.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		spCustomPoke3Chooser.setEnabled(false);

		starterPokemonButtonGroup.add(spRandomRB);
		spRandomRB.setText(bundle.getString("RandomizerGUI.spRandomRB.text")); // NOI18N
		spRandomRB.setToolTipText(bundle
				.getString("RandomizerGUI.spRandomRB.toolTipText")); // NOI18N
		spRandomRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spRandomRBActionPerformed(evt);
			}
		});

		starterPokemonButtonGroup.add(spRandom2EvosRB);
		spRandom2EvosRB.setText(bundle
				.getString("RandomizerGUI.spRandom2EvosRB.text")); // NOI18N
		spRandom2EvosRB.setToolTipText(bundle
				.getString("RandomizerGUI.spRandom2EvosRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout starterPokemonPanelLayout = new javax.swing.GroupLayout(
				starterPokemonPanel);
		starterPokemonPanel.setLayout(starterPokemonPanelLayout);
		starterPokemonPanelLayout
				.setHorizontalGroup(starterPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								starterPokemonPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												starterPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																spUnchangedRB)
														.addGroup(
																starterPokemonPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				spCustomRB)
																		.addGap(18,
																				18,
																				18)
																		.addComponent(
																				spCustomPoke1Chooser,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				90,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				spCustomPoke2Chooser,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				90,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				spCustomPoke3Chooser,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				90,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																spRandomRB)
														.addComponent(
																spRandom2EvosRB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		starterPokemonPanelLayout
				.setVerticalGroup(starterPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								starterPokemonPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(spUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												starterPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																spCustomRB)
														.addComponent(
																spCustomPoke1Chooser,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																spCustomPoke2Chooser,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																spCustomPoke3Chooser,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(spRandomRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(spRandom2EvosRB)
										.addContainerGap(11, Short.MAX_VALUE)));

		staticPokemonPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.staticPokemonPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		staticPokemonButtonGroup.add(stpUnchangedRB);
		stpUnchangedRB.setSelected(true);
		stpUnchangedRB.setText(bundle
				.getString("RandomizerGUI.stpUnchangedRB.text")); // NOI18N
		stpUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.stpUnchangedRB.toolTipText")); // NOI18N

		staticPokemonButtonGroup.add(stpRandomL4LRB);
		stpRandomL4LRB.setText(bundle
				.getString("RandomizerGUI.stpRandomL4LRB.text")); // NOI18N
		stpRandomL4LRB.setToolTipText(bundle
				.getString("RandomizerGUI.stpRandomL4LRB.toolTipText")); // NOI18N

		staticPokemonButtonGroup.add(stpRandomTotalRB);
		stpRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.stpRandomTotalRB.text")); // NOI18N
		stpRandomTotalRB.setToolTipText(bundle
				.getString("RandomizerGUI.stpRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout staticPokemonPanelLayout = new javax.swing.GroupLayout(
				staticPokemonPanel);
		staticPokemonPanel.setLayout(staticPokemonPanelLayout);
		staticPokemonPanelLayout
				.setHorizontalGroup(staticPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								staticPokemonPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												staticPokemonPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																stpUnchangedRB)
														.addComponent(
																stpRandomL4LRB)
														.addComponent(
																stpRandomTotalRB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		staticPokemonPanelLayout
				.setVerticalGroup(staticPokemonPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								staticPokemonPanelLayout
										.createSequentialGroup()
										.addComponent(stpUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(stpRandomL4LRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(stpRandomTotalRB)));

		tmhmsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				bundle.getString("RandomizerGUI.tmhmsPanel.border.title"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		tmMovesPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(bundle
						.getString("RandomizerGUI.tmMovesPanel.border.title"))); // NOI18N

		tmMovesButtonGroup.add(tmmUnchangedRB);
		tmmUnchangedRB.setSelected(true);
		tmmUnchangedRB.setText(bundle
				.getString("RandomizerGUI.tmmUnchangedRB.text")); // NOI18N
		tmmUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.tmmUnchangedRB.toolTipText")); // NOI18N

		tmMovesButtonGroup.add(tmmRandomRB);
		tmmRandomRB.setText(bundle.getString("RandomizerGUI.tmmRandomRB.text")); // NOI18N
		tmmRandomRB.setToolTipText(bundle
				.getString("RandomizerGUI.tmmRandomRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout tmMovesPanelLayout = new javax.swing.GroupLayout(
				tmMovesPanel);
		tmMovesPanel.setLayout(tmMovesPanelLayout);
		tmMovesPanelLayout
				.setHorizontalGroup(tmMovesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmMovesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												tmMovesPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																tmmUnchangedRB)
														.addComponent(
																tmmRandomRB))
										.addContainerGap(118, Short.MAX_VALUE)));
		tmMovesPanelLayout
				.setVerticalGroup(tmMovesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmMovesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(tmmUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(tmmRandomRB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		tmHmCompatPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle
						.getString("RandomizerGUI.tmHmCompatPanel.border.title"))); // NOI18N

		tmHmCompatibilityButtonGroup.add(thcUnchangedRB);
		thcUnchangedRB.setSelected(true);
		thcUnchangedRB.setText(bundle
				.getString("RandomizerGUI.thcUnchangedRB.text")); // NOI18N
		thcUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.thcUnchangedRB.toolTipText")); // NOI18N

		tmHmCompatibilityButtonGroup.add(thcRandomTypeRB);
		thcRandomTypeRB.setText(bundle
				.getString("RandomizerGUI.thcRandomTypeRB.text")); // NOI18N
		thcRandomTypeRB.setToolTipText(bundle
				.getString("RandomizerGUI.thcRandomTypeRB.toolTipText")); // NOI18N

		tmHmCompatibilityButtonGroup.add(thcRandomTotalRB);
		thcRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.thcRandomTotalRB.text")); // NOI18N
		thcRandomTotalRB.setToolTipText(bundle
				.getString("RandomizerGUI.thcRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout tmHmCompatPanelLayout = new javax.swing.GroupLayout(
				tmHmCompatPanel);
		tmHmCompatPanel.setLayout(tmHmCompatPanelLayout);
		tmHmCompatPanelLayout
				.setHorizontalGroup(tmHmCompatPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmHmCompatPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												tmHmCompatPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																thcUnchangedRB)
														.addComponent(
																thcRandomTypeRB)
														.addComponent(
																thcRandomTotalRB))
										.addContainerGap(79, Short.MAX_VALUE)));
		tmHmCompatPanelLayout
				.setVerticalGroup(tmHmCompatPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmHmCompatPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(thcUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(thcRandomTypeRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(thcRandomTotalRB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		javax.swing.GroupLayout tmhmsPanelLayout = new javax.swing.GroupLayout(
				tmhmsPanel);
		tmhmsPanel.setLayout(tmhmsPanelLayout);
		tmhmsPanelLayout
				.setHorizontalGroup(tmhmsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmhmsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												tmMovesPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												tmHmCompatPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));
		tmhmsPanelLayout
				.setVerticalGroup(tmhmsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tmhmsPanelLayout
										.createSequentialGroup()
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												tmhmsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																tmHmCompatPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																tmMovesPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));

		abilitiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null,
				bundle.getString("RandomizerGUI.abilitiesPanel.border.title"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		pokeAbilitiesButtonGroup.add(paUnchangedRB);
		paUnchangedRB.setSelected(true);
		paUnchangedRB.setText(bundle
				.getString("RandomizerGUI.paUnchangedRB.text")); // NOI18N
		paUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.paUnchangedRB.toolTipText")); // NOI18N
		paUnchangedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				paUnchangedRBActionPerformed(evt);
			}
		});

		pokeAbilitiesButtonGroup.add(paRandomizeRB);
		paRandomizeRB.setText(bundle
				.getString("RandomizerGUI.paRandomizeRB.text")); // NOI18N
		paRandomizeRB.setToolTipText(bundle
				.getString("RandomizerGUI.paRandomizeRB.toolTipText")); // NOI18N
		paRandomizeRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				paRandomizeRBActionPerformed(evt);
			}
		});

		paWonderGuardCB.setText(bundle
				.getString("RandomizerGUI.paWonderGuardCB.text")); // NOI18N
		paWonderGuardCB.setToolTipText(bundle
				.getString("RandomizerGUI.paWonderGuardCB.toolTipText")); // NOI18N

		javax.swing.GroupLayout abilitiesPanelLayout = new javax.swing.GroupLayout(
				abilitiesPanel);
		abilitiesPanel.setLayout(abilitiesPanelLayout);
		abilitiesPanelLayout
				.setHorizontalGroup(abilitiesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								abilitiesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												abilitiesPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																paUnchangedRB)
														.addComponent(
																paRandomizeRB)
														.addComponent(
																paWonderGuardCB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		abilitiesPanelLayout
				.setVerticalGroup(abilitiesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								abilitiesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(paUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(paRandomizeRB)
										.addGap(18, 18, 18)
										.addComponent(paWonderGuardCB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		moveTutorsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null,
				bundle.getString("RandomizerGUI.moveTutorsPanel.border.title"),
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		mtMovesPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(bundle
						.getString("RandomizerGUI.mtMovesPanel.border.title"))); // NOI18N

		mtMovesButtonGroup.add(mtmUnchangedRB);
		mtmUnchangedRB.setSelected(true);
		mtmUnchangedRB.setText(bundle
				.getString("RandomizerGUI.mtmUnchangedRB.text")); // NOI18N
		mtmUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.mtmUnchangedRB.toolTipText")); // NOI18N
		mtmUnchangedRB.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mtmUnchangedRBActionPerformed(evt);
			}
		});

		mtMovesButtonGroup.add(mtmRandomRB);
		mtmRandomRB.setText(bundle.getString("RandomizerGUI.mtmRandomRB.text")); // NOI18N
		mtmRandomRB.setToolTipText(bundle
				.getString("RandomizerGUI.mtmRandomRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout mtMovesPanelLayout = new javax.swing.GroupLayout(
				mtMovesPanel);
		mtMovesPanel.setLayout(mtMovesPanelLayout);
		mtMovesPanelLayout
				.setHorizontalGroup(mtMovesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mtMovesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mtMovesPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																mtmUnchangedRB)
														.addComponent(
																mtmRandomRB))
										.addContainerGap(118, Short.MAX_VALUE)));
		mtMovesPanelLayout
				.setVerticalGroup(mtMovesPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mtMovesPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(mtmUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(mtmRandomRB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		mtCompatPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle
						.getString("RandomizerGUI.mtCompatPanel.border.title"))); // NOI18N

		mtCompatibilityButtonGroup.add(mtcUnchangedRB);
		mtcUnchangedRB.setSelected(true);
		mtcUnchangedRB.setText(bundle
				.getString("RandomizerGUI.mtcUnchangedRB.text")); // NOI18N
		mtcUnchangedRB.setToolTipText(bundle
				.getString("RandomizerGUI.mtcUnchangedRB.toolTipText")); // NOI18N

		mtCompatibilityButtonGroup.add(mtcRandomTypeRB);
		mtcRandomTypeRB.setText(bundle
				.getString("RandomizerGUI.mtcRandomTypeRB.text")); // NOI18N
		mtcRandomTypeRB.setToolTipText(bundle
				.getString("RandomizerGUI.mtcRandomTypeRB.toolTipText")); // NOI18N

		mtCompatibilityButtonGroup.add(mtcRandomTotalRB);
		mtcRandomTotalRB.setText(bundle
				.getString("RandomizerGUI.mtcRandomTotalRB.text")); // NOI18N
		mtcRandomTotalRB.setToolTipText(bundle
				.getString("RandomizerGUI.mtcRandomTotalRB.toolTipText")); // NOI18N

		javax.swing.GroupLayout mtCompatPanelLayout = new javax.swing.GroupLayout(
				mtCompatPanel);
		mtCompatPanel.setLayout(mtCompatPanelLayout);
		mtCompatPanelLayout
				.setHorizontalGroup(mtCompatPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mtCompatPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												mtCompatPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																mtcUnchangedRB)
														.addComponent(
																mtcRandomTypeRB)
														.addComponent(
																mtcRandomTotalRB))
										.addContainerGap(79, Short.MAX_VALUE)));
		mtCompatPanelLayout
				.setVerticalGroup(mtCompatPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								mtCompatPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(mtcUnchangedRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(mtcRandomTypeRB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(mtcRandomTotalRB)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		mtNoExistLabel.setText(bundle
				.getString("RandomizerGUI.mtNoExistLabel.text")); // NOI18N

		javax.swing.GroupLayout moveTutorsPanelLayout = new javax.swing.GroupLayout(
				moveTutorsPanel);
		moveTutorsPanel.setLayout(moveTutorsPanelLayout);
		moveTutorsPanelLayout
				.setHorizontalGroup(moveTutorsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								moveTutorsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												moveTutorsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																moveTutorsPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				mtMovesPanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(
																				mtCompatPanel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																moveTutorsPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				mtNoExistLabel)
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		moveTutorsPanelLayout
				.setVerticalGroup(moveTutorsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								moveTutorsPanelLayout
										.createSequentialGroup()
										.addComponent(mtNoExistLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												moveTutorsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																mtCompatPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																mtMovesPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));

		javax.swing.GroupLayout optionsContainerPanelLayout = new javax.swing.GroupLayout(
				optionsContainerPanel);
		optionsContainerPanel.setLayout(optionsContainerPanelLayout);
		optionsContainerPanelLayout
				.setHorizontalGroup(optionsContainerPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(pokemonTypesPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(pokemonMovesetsPanel,
								javax.swing.GroupLayout.Alignment.TRAILING,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(trainersPokemonPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(wildPokemonPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(starterPokemonPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(staticPokemonPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(tmhmsPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addGroup(
								optionsContainerPanelLayout
										.createSequentialGroup()
										.addComponent(
												baseStatsPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												abilitiesPanel,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))
						.addComponent(moveTutorsPanel,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE));
		optionsContainerPanelLayout
				.setVerticalGroup(optionsContainerPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								optionsContainerPanelLayout
										.createSequentialGroup()
										.addGroup(
												optionsContainerPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																baseStatsPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																abilitiesPanel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addGap(14, 14, 14)
										.addComponent(
												starterPokemonPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												pokemonTypesPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												pokemonMovesetsPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												trainersPokemonPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(15, 15, 15)
										.addComponent(
												wildPokemonPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												staticPokemonPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												tmhmsPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												moveTutorsPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		optionsScrollPane.setViewportView(optionsContainerPanel);

		openROMButton.setText(bundle
				.getString("RandomizerGUI.openROMButton.text")); // NOI18N
		openROMButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openROMButtonActionPerformed(evt);
			}
		});

		saveROMButton.setText(bundle
				.getString("RandomizerGUI.saveROMButton.text")); // NOI18N
		saveROMButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveROMButtonActionPerformed(evt);
			}
		});

		usePresetsButton.setText(bundle
				.getString("RandomizerGUI.usePresetsButton.text")); // NOI18N
		usePresetsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				usePresetsButtonActionPerformed(evt);
			}
		});

		aboutButton.setText(bundle.getString("RandomizerGUI.aboutButton.text")); // NOI18N
		aboutButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutButtonActionPerformed(evt);
			}
		});

		otherOptionsPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(
						null,
						bundle.getString("RandomizerGUI.otherOptionsPanel.border.title"),
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		bwEXPPatchCB.setText(bundle
				.getString("RandomizerGUI.bwEXPPatchCB.text")); // NOI18N
		bwEXPPatchCB.setToolTipText(bundle
				.getString("RandomizerGUI.bwEXPPatchCB.toolTipText")); // NOI18N

		raceModeCB.setText(bundle.getString("RandomizerGUI.raceModeCB.text")); // NOI18N
		raceModeCB.setToolTipText(bundle
				.getString("RandomizerGUI.raceModeCB.toolTipText")); // NOI18N

		randomizeHollowsCB.setText(bundle
				.getString("RandomizerGUI.randomizeHollowsCB.text")); // NOI18N
		randomizeHollowsCB.setToolTipText(bundle
				.getString("RandomizerGUI.randomizeHollowsCB.toolTipText")); // NOI18N

		brokenMovesCB.setText(bundle
				.getString("RandomizerGUI.brokenMovesCB.text")); // NOI18N
		brokenMovesCB.setToolTipText(bundle
				.getString("RandomizerGUI.brokenMovesCB.toolTipText")); // NOI18N

		heldItemsCB.setText(bundle.getString("RandomizerGUI.heldItemsCB.text")); // NOI18N
		heldItemsCB.setToolTipText(bundle
				.getString("RandomizerGUI.heldItemsCB.toolTipText")); // NOI18N

		javax.swing.GroupLayout otherOptionsPanelLayout = new javax.swing.GroupLayout(
				otherOptionsPanel);
		otherOptionsPanel.setLayout(otherOptionsPanelLayout);
		otherOptionsPanelLayout
				.setHorizontalGroup(otherOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								otherOptionsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												otherOptionsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																bwEXPPatchCB)
														.addComponent(
																raceModeCB)
														.addComponent(
																randomizeHollowsCB)
														.addComponent(
																brokenMovesCB)
														.addComponent(
																heldItemsCB))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		otherOptionsPanelLayout
				.setVerticalGroup(otherOptionsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								otherOptionsPanelLayout
										.createSequentialGroup()
										.addComponent(bwEXPPatchCB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(raceModeCB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(randomizeHollowsCB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(brokenMovesCB)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												3, Short.MAX_VALUE)
										.addComponent(heldItemsCB)));

		loadQSButton.setText(bundle
				.getString("RandomizerGUI.loadQSButton.text")); // NOI18N
		loadQSButton.setToolTipText(bundle
				.getString("RandomizerGUI.loadQSButton.toolTipText")); // NOI18N
		loadQSButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadQSButtonActionPerformed(evt);
			}
		});

		saveQSButton.setText(bundle
				.getString("RandomizerGUI.saveQSButton.text")); // NOI18N
		saveQSButton.setToolTipText(bundle
				.getString("RandomizerGUI.saveQSButton.toolTipText")); // NOI18N
		saveQSButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveQSButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														optionsScrollPane,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														716, Short.MAX_VALUE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		generalOptionsPanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(
																		otherOptionsPanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										romInfoPanel,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addGap(18,
																										18,
																										18))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(10,
																										10,
																										10)
																								.addComponent(
																										loadQSButton)
																								.addGap(29,
																										29,
																										29)
																								.addComponent(
																										saveQSButton)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)))
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						saveROMButton,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						123,
																						Short.MAX_VALUE)
																				.addComponent(
																						openROMButton,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						usePresetsButton,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addComponent(
																						aboutButton,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										openROMButton)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																								.addComponent(
																										saveROMButton)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																								.addComponent(
																										usePresetsButton))
																				.addComponent(
																						romInfoPanel,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						aboutButton)
																				.addGroup(
																						layout.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.BASELINE)
																								.addComponent(
																										loadQSButton)
																								.addComponent(
																										saveQSButton))))
												.addComponent(
														otherOptionsPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														generalOptionsPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(optionsScrollPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										382, Short.MAX_VALUE).addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initialiseState() {
		this.romHandler = null;
		initialFormState();
		this.romOpenChooser.setCurrentDirectory(new File("./"));
		this.romSaveChooser.setCurrentDirectory(new File("./"));
		if (new File("./settings/").exists()) {
			this.qsOpenChooser.setCurrentDirectory(new File("./settings/"));
			this.qsSaveChooser.setCurrentDirectory(new File("./settings/"));
		} else {
			this.qsOpenChooser.setCurrentDirectory(new File("./"));
			this.qsSaveChooser.setCurrentDirectory(new File("./"));
		}
	}

	private void initialFormState() {
		// Disable all rom components
		this.goRemoveTradeEvosCheckBox.setEnabled(false);
		this.goUpdateMovesCheckBox.setEnabled(false);
		this.goUpdateTypesCheckBox.setEnabled(false);
		this.goLowerCaseNamesCheckBox.setEnabled(false);
		this.goNationalDexCheckBox.setEnabled(false);

		this.goRemoveTradeEvosCheckBox.setSelected(false);
		this.goUpdateMovesCheckBox.setSelected(false);
		this.goUpdateTypesCheckBox.setSelected(false);
		this.goLowerCaseNamesCheckBox.setSelected(false);
		this.goNationalDexCheckBox.setSelected(false);

		this.bwEXPPatchCB.setEnabled(false);
		this.bwEXPPatchCB.setSelected(false);
		this.raceModeCB.setEnabled(false);
		this.raceModeCB.setSelected(false);
		this.randomizeHollowsCB.setEnabled(false);
		this.randomizeHollowsCB.setSelected(false);
		this.brokenMovesCB.setEnabled(false);
		this.brokenMovesCB.setSelected(false);
		this.heldItemsCB.setEnabled(false);
		this.heldItemsCB.setSelected(false);

		this.riRomNameLabel.setText("NO ROM LOADED");
		this.riRomCodeLabel.setText("");
		this.riRomSupportLabel.setText("");

		this.loadQSButton.setEnabled(false);
		this.saveQSButton.setEnabled(false);

		this.pbsChangesUnchangedRB.setEnabled(false);
		this.pbsChangesRandomEvosRB.setEnabled(false);
		this.pbsChangesRandomTotalRB.setEnabled(false);
		this.pbsChangesShuffleRB.setEnabled(false);
		this.pbsChangesUnchangedRB.setSelected(true);

		this.abilitiesPanel.setVisible(true);
		this.paUnchangedRB.setEnabled(false);
		this.paRandomizeRB.setEnabled(false);
		this.paWonderGuardCB.setEnabled(false);
		this.paUnchangedRB.setSelected(true);
		this.paWonderGuardCB.setSelected(false);

		this.spCustomPoke1Chooser.setEnabled(false);
		this.spCustomPoke2Chooser.setEnabled(false);
		this.spCustomPoke3Chooser.setEnabled(false);
		this.spCustomPoke1Chooser.setSelectedIndex(0);
		this.spCustomPoke1Chooser.setModel(new DefaultComboBoxModel(
				new String[] { "--" }));
		this.spCustomPoke2Chooser.setSelectedIndex(0);
		this.spCustomPoke2Chooser.setModel(new DefaultComboBoxModel(
				new String[] { "--" }));
		this.spCustomPoke3Chooser.setSelectedIndex(0);
		this.spCustomPoke3Chooser.setModel(new DefaultComboBoxModel(
				new String[] { "--" }));
		this.spCustomRB.setEnabled(false);
		this.spRandomRB.setEnabled(false);
		this.spRandom2EvosRB.setEnabled(false);
		this.spUnchangedRB.setEnabled(false);
		this.spUnchangedRB.setSelected(true);

		this.pmsRandomTotalRB.setEnabled(false);
		this.pmsRandomTypeRB.setEnabled(false);
		this.pmsUnchangedRB.setEnabled(false);
		this.pmsUnchangedRB.setSelected(true);

		this.ptRandomFollowEvosRB.setEnabled(false);
		this.ptRandomTotalRB.setEnabled(false);
		this.ptUnchangedRB.setEnabled(false);
		this.ptUnchangedRB.setSelected(true);

		this.tpPowerLevelsCB.setEnabled(false);
		this.tpRandomRB.setEnabled(false);
		this.tpRivalCarriesStarterCB.setEnabled(false);
		this.tpTypeThemedRB.setEnabled(false);
		this.tpTypeWeightingCB.setEnabled(false);
		this.tpNoLegendariesCB.setEnabled(false);
		this.tpNoEarlyShedinjaCB.setEnabled(false);
		this.tpNoEarlyShedinjaCB.setVisible(true);
		this.tpUnchangedRB.setEnabled(false);
		this.tpUnchangedRB.setSelected(true);
		this.tpPowerLevelsCB.setSelected(false);
		this.tpRivalCarriesStarterCB.setSelected(false);
		this.tpTypeWeightingCB.setSelected(false);
		this.tpNoLegendariesCB.setSelected(false);
		this.tpNoEarlyShedinjaCB.setSelected(false);

		this.tnRandomizeCB.setEnabled(false);
		this.tcnRandomizeCB.setEnabled(false);

		this.tnRandomizeCB.setSelected(false);
		this.tcnRandomizeCB.setSelected(false);

		this.wpUnchangedRB.setEnabled(false);
		this.wpRandomRB.setEnabled(false);
		this.wpArea11RB.setEnabled(false);
		this.wpGlobalRB.setEnabled(false);
		this.wpUnchangedRB.setSelected(true);

		this.wpARNoneRB.setEnabled(false);
		this.wpARCatchEmAllRB.setEnabled(false);
		this.wpARTypeThemedRB.setEnabled(false);
		this.wpARNoneRB.setSelected(true);

		this.wpUseTimeCB.setEnabled(false);
		this.wpUseTimeCB.setVisible(true);
		this.wpUseTimeCB.setSelected(false);

		this.wpNoLegendariesCB.setEnabled(false);
		this.wpNoLegendariesCB.setSelected(false);

		this.wpCatchRateCB.setEnabled(false);
		this.wpCatchRateCB.setSelected(false);

		this.stpRandomL4LRB.setEnabled(false);
		this.stpRandomTotalRB.setEnabled(false);
		this.stpUnchangedRB.setEnabled(false);
		this.stpUnchangedRB.setSelected(true);

		this.tmmRandomRB.setEnabled(false);
		this.tmmUnchangedRB.setEnabled(false);
		this.tmmUnchangedRB.setSelected(true);

		this.thcRandomTotalRB.setEnabled(false);
		this.thcRandomTypeRB.setEnabled(false);
		this.thcUnchangedRB.setEnabled(false);
		this.thcUnchangedRB.setSelected(true);

		this.mtmRandomRB.setEnabled(false);
		this.mtmUnchangedRB.setEnabled(false);
		this.mtmUnchangedRB.setSelected(true);

		this.mtcRandomTotalRB.setEnabled(false);
		this.mtcRandomTypeRB.setEnabled(false);
		this.mtcUnchangedRB.setEnabled(false);
		this.mtcUnchangedRB.setSelected(true);

		this.mtMovesPanel.setVisible(true);
		this.mtCompatPanel.setVisible(true);
		this.mtNoExistLabel.setVisible(false);

	}

	private void loadROM() {
		romOpenChooser.setSelectedFile(null);
		int returnVal = romOpenChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			final File fh = romOpenChooser.getSelectedFile();
			for (RomHandler rh : checkHandlers) {
				if (rh.detectRom(fh.getAbsolutePath())) {
					this.romHandler = rh;
					opDialog = new OperationDialog("Loading...", this, true);
					Thread t = new Thread() {
						@Override
						public void run() {
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									opDialog.setVisible(true);
								}
							});
							try {
								RandomizerGUI.this.romHandler.loadRom(fh
										.getAbsolutePath());
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(
										RandomizerGUI.this, "ROM load failed.");
							}
							SwingUtilities.invokeLater(new Runnable() {
								@Override
								public void run() {
									RandomizerGUI.this.opDialog
											.setVisible(false);
									RandomizerGUI.this.initialFormState();
									RandomizerGUI.this.romLoaded();
								}
							});
						}
					};
					t.start();

					return;
				}
			}
			JOptionPane.showMessageDialog(this,
					"Could not load " + fh.getName()
							+ " - it's not a supported ROM.");
		}

	}

	private void romLoaded() {
		this.riRomNameLabel.setText(this.romHandler.getROMName());
		this.riRomCodeLabel.setText(this.romHandler.getROMCode());
		this.riRomSupportLabel.setText("Support: "
				+ this.romHandler.getSupportLevel());

		if (romHandler instanceof Gen1RomHandler) {
			this.goUpdateMovesCheckBox.setSelected(false);
			this.goUpdateTypesCheckBox.setEnabled(true);
		}
		if (!(romHandler instanceof Gen5RomHandler)) {
			this.goUpdateMovesCheckBox.setSelected(false);
			this.goUpdateMovesCheckBox.setEnabled(true);
		}
		this.goRemoveTradeEvosCheckBox.setSelected(false);
		this.goRemoveTradeEvosCheckBox.setEnabled(true);
		if (!(romHandler instanceof Gen5RomHandler)
				&& !(romHandler instanceof Gen4RomHandler)) {
			this.goLowerCaseNamesCheckBox.setSelected(false);
			this.goLowerCaseNamesCheckBox.setEnabled(true);
		}
		if (romHandler instanceof Gen3RomHandler) {
			this.goNationalDexCheckBox.setSelected(false);
			this.goNationalDexCheckBox.setEnabled(true);
		}
		this.raceModeCB.setSelected(false);
		this.raceModeCB.setEnabled(true);

		this.bwEXPPatchCB.setSelected(false);
		this.bwEXPPatchCB.setEnabled(romHandler.hasBWEXPPatch());

		this.randomizeHollowsCB.setSelected(false);
		this.randomizeHollowsCB.setEnabled(romHandler.hasHiddenHollowPokemon());

		this.brokenMovesCB.setSelected(false);
		this.brokenMovesCB.setEnabled(true);

		this.heldItemsCB.setSelected(false);
		this.heldItemsCB.setEnabled(!(romHandler instanceof Gen1RomHandler));

		this.loadQSButton.setEnabled(true);
		this.saveQSButton.setEnabled(true);

		this.pbsChangesUnchangedRB.setEnabled(true);
		this.pbsChangesUnchangedRB.setSelected(true);
		this.pbsChangesRandomEvosRB.setEnabled(true);
		this.pbsChangesRandomTotalRB.setEnabled(true);
		this.pbsChangesShuffleRB.setEnabled(true);

		if (romHandler.abilitiesPerPokemon() > 0) {
			this.paUnchangedRB.setEnabled(true);
			this.paUnchangedRB.setSelected(true);
			this.paRandomizeRB.setEnabled(true);
			this.paWonderGuardCB.setEnabled(false);
		} else {
			this.abilitiesPanel.setVisible(false);
		}

		this.spUnchangedRB.setEnabled(true);
		this.spUnchangedRB.setSelected(true);

		this.spCustomPoke3Chooser.setVisible(true);
		if (romHandler.canChangeStarters()) {
			this.spCustomRB.setEnabled(true);
			this.spRandomRB.setEnabled(true);
			this.spRandom2EvosRB.setEnabled(true);
			if (romHandler.isYellow()) {
				this.spCustomPoke3Chooser.setVisible(false);
			}
			populateDropdowns();
		}

		this.pmsRandomTotalRB.setEnabled(true);
		this.pmsRandomTypeRB.setEnabled(true);
		this.pmsUnchangedRB.setEnabled(true);
		this.pmsUnchangedRB.setSelected(true);

		this.ptRandomFollowEvosRB.setEnabled(true);
		this.ptRandomTotalRB.setEnabled(true);
		this.ptUnchangedRB.setEnabled(true);
		this.ptUnchangedRB.setSelected(true);

		this.tpRandomRB.setEnabled(true);
		this.tpTypeThemedRB.setEnabled(true);
		this.tpUnchangedRB.setEnabled(true);
		this.tpUnchangedRB.setSelected(true);
		this.tnRandomizeCB.setEnabled(true);
		this.tcnRandomizeCB.setEnabled(true);

		if (romHandler instanceof Gen1RomHandler
				|| romHandler instanceof Gen2RomHandler) {
			this.tpNoEarlyShedinjaCB.setVisible(false);
		} else {
			this.tpNoEarlyShedinjaCB.setVisible(true);
		}
		this.tpNoEarlyShedinjaCB.setSelected(false);

		this.wpArea11RB.setEnabled(true);
		this.wpGlobalRB.setEnabled(true);
		this.wpRandomRB.setEnabled(true);
		this.wpUnchangedRB.setEnabled(true);
		this.wpUnchangedRB.setSelected(true);
		this.wpUseTimeCB.setEnabled(false);
		this.wpNoLegendariesCB.setEnabled(false);
		if (!romHandler.hasTimeBasedEncounters()) {
			this.wpUseTimeCB.setVisible(false);
		}
		this.wpCatchRateCB.setEnabled(true);

		this.stpUnchangedRB.setEnabled(true);
		if (this.romHandler.canChangeStaticPokemon()) {
			this.stpRandomL4LRB.setEnabled(true);
			this.stpRandomTotalRB.setEnabled(true);

		}

		this.tmmRandomRB.setEnabled(true);
		this.tmmUnchangedRB.setEnabled(true);

		this.thcRandomTotalRB.setEnabled(true);
		this.thcRandomTypeRB.setEnabled(true);
		this.thcUnchangedRB.setEnabled(true);

		if (this.romHandler.hasMoveTutors()) {
			this.mtmRandomRB.setEnabled(true);
			this.mtmUnchangedRB.setEnabled(true);

			this.mtcRandomTotalRB.setEnabled(true);
			this.mtcRandomTypeRB.setEnabled(true);
			this.mtcUnchangedRB.setEnabled(true);
		} else {
			this.mtCompatPanel.setVisible(false);
			this.mtMovesPanel.setVisible(false);
			this.mtNoExistLabel.setVisible(true);
		}

	}

	private void populateDropdowns() {
		List<Pokemon> currentStarters = romHandler.getStarters();
		List<Pokemon> allPokes = romHandler.getPokemon();
		String[] pokeNames = new String[allPokes.size() - 1];
		for (int i = 1; i < allPokes.size(); i++) {
			pokeNames[i - 1] = allPokes.get(i).name;
		}
		this.spCustomPoke1Chooser.setModel(new DefaultComboBoxModel(pokeNames));
		this.spCustomPoke1Chooser
				.setSelectedIndex(currentStarters.get(0).number - 1);
		this.spCustomPoke2Chooser.setModel(new DefaultComboBoxModel(pokeNames));
		this.spCustomPoke2Chooser
				.setSelectedIndex(currentStarters.get(1).number - 1);
		if (!romHandler.isYellow()) {
			this.spCustomPoke3Chooser.setModel(new DefaultComboBoxModel(
					pokeNames));
			this.spCustomPoke3Chooser
					.setSelectedIndex(currentStarters.get(2).number - 1);
		}
	}

	private void enableOrDisableSubControls() {
		// This isn't for a new ROM being loaded (that's romLoaded)
		// This is just for when a radio button gets selected or state is loaded
		// and we need to enable/disable secondary controls
		// e.g. wild pokemon / trainer pokemon "modifier"
		// and the 3 starter pokemon dropdowns
		if (this.spCustomRB.isSelected()) {
			this.spCustomPoke1Chooser.setEnabled(true);
			this.spCustomPoke2Chooser.setEnabled(true);
			this.spCustomPoke3Chooser.setEnabled(true);
		} else {
			this.spCustomPoke1Chooser.setEnabled(false);
			this.spCustomPoke2Chooser.setEnabled(false);
			this.spCustomPoke3Chooser.setEnabled(false);
		}

		if (this.paRandomizeRB.isSelected()) {
			this.paWonderGuardCB.setEnabled(true);
		} else {
			this.paWonderGuardCB.setEnabled(false);
			this.paWonderGuardCB.setSelected(false);
		}

		if (this.tpUnchangedRB.isSelected()) {
			this.tpPowerLevelsCB.setEnabled(false);
			this.tpRivalCarriesStarterCB.setEnabled(false);
			this.tpNoLegendariesCB.setEnabled(false);
			this.tpNoEarlyShedinjaCB.setEnabled(false);
			this.tpNoEarlyShedinjaCB.setSelected(false);
		} else {
			this.tpPowerLevelsCB.setEnabled(true);
			this.tpRivalCarriesStarterCB.setEnabled(true);
			this.tpNoLegendariesCB.setEnabled(true);
			this.tpNoEarlyShedinjaCB.setEnabled(true);
		}

		if (this.tpTypeThemedRB.isSelected()) {
			this.tpTypeWeightingCB.setEnabled(true);
		} else {
			this.tpTypeWeightingCB.setEnabled(false);
		}

		if (this.wpArea11RB.isSelected() || this.wpRandomRB.isSelected()) {
			this.wpARNoneRB.setEnabled(true);
			this.wpARCatchEmAllRB.setEnabled(true);
			this.wpARTypeThemedRB.setEnabled(true);
		} else {
			this.wpARNoneRB.setEnabled(false);
			this.wpARNoneRB.setSelected(true);
			this.wpARCatchEmAllRB.setEnabled(false);
			this.wpARTypeThemedRB.setEnabled(false);
		}

		if (this.wpUnchangedRB.isSelected()) {
			this.wpUseTimeCB.setEnabled(false);
			this.wpNoLegendariesCB.setEnabled(false);
		} else {
			this.wpUseTimeCB.setEnabled(true);
			this.wpNoLegendariesCB.setEnabled(true);
		}
	}

	private void saveROM() {
		if (romHandler == null) {
			return; // none loaded
		}
		if (raceModeCB.isSelected() && tpUnchangedRB.isSelected()
				&& wpUnchangedRB.isSelected()) {
			JOptionPane
					.showMessageDialog(
							this,
							"You can't use Race Mode without randomizing either the wild Pokemon or the trainer Pokemon.\nReview this and try again.");
			return;
		}
		romSaveChooser.setSelectedFile(null);
		int returnVal = romSaveChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fh = romSaveChooser.getSelectedFile();
			// Fix or add extension
			List<String> extensions = new ArrayList<String>(Arrays.asList(
					"sgb", "gbc", "gba", "nds"));
			extensions.remove(this.romHandler.getDefaultExtension());
			fh = FileFunctions.fixFilename(fh,
					this.romHandler.getDefaultExtension(), extensions);
			// Get a seed
			long seed = RandomSource.pickSeed();
			// Apply it
			RandomSource.seed(seed);
			presetMode = false;
			performRandomization(fh.getAbsolutePath(), seed, null, null);
		}
	}

	private String getConfigString() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		baos.write(makeByteSelected(this.goLowerCaseNamesCheckBox,
				this.goNationalDexCheckBox, this.goRemoveTradeEvosCheckBox,
				this.goUpdateMovesCheckBox, this.goUpdateTypesCheckBox,
				this.tnRandomizeCB, this.tcnRandomizeCB));

		baos.write(makeByteSelected(this.pbsChangesRandomEvosRB,
				this.pbsChangesRandomTotalRB, this.pbsChangesShuffleRB,
				this.pbsChangesUnchangedRB, this.paUnchangedRB,
				this.paRandomizeRB, this.paWonderGuardCB));

		baos.write(makeByteSelected(this.ptRandomFollowEvosRB,
				this.ptRandomTotalRB, this.ptUnchangedRB, this.bwEXPPatchCB,
				this.raceModeCB, this.randomizeHollowsCB, this.brokenMovesCB,
				this.heldItemsCB));

		baos.write(makeByteSelected(this.spCustomRB, this.spRandomRB,
				this.spUnchangedRB, this.spRandom2EvosRB));

		writePokemonIndex(baos, this.spCustomPoke1Chooser);
		writePokemonIndex(baos, this.spCustomPoke2Chooser);
		writePokemonIndex(baos, this.spCustomPoke3Chooser);

		baos.write(makeByteSelected(this.pmsRandomTotalRB,
				this.pmsRandomTypeRB, this.pmsUnchangedRB));

		// Consistent with reading - we include both no legendary boxes
		// in the trainer byte because the wild byte is already full.
		baos.write(makeByteSelected(this.tpPowerLevelsCB, this.tpRandomRB,
				this.tpRivalCarriesStarterCB, this.tpTypeThemedRB,
				this.tpTypeWeightingCB, this.tpUnchangedRB,
				this.tpNoLegendariesCB, this.wpNoLegendariesCB));

		baos.write(makeByteSelected(this.wpARCatchEmAllRB, this.wpArea11RB,
				this.wpARNoneRB, this.wpARTypeThemedRB, this.wpGlobalRB,
				this.wpRandomRB, this.wpUnchangedRB, this.wpUseTimeCB));

		baos.write(makeByteSelected(this.stpUnchangedRB, this.stpRandomL4LRB,
				this.stpRandomTotalRB, this.wpCatchRateCB,
				this.tpNoEarlyShedinjaCB));

		baos.write(makeByteSelected(this.thcRandomTotalRB,
				this.thcRandomTypeRB, this.thcUnchangedRB, this.tmmRandomRB,
				this.tmmUnchangedRB));

		baos.write(makeByteSelected(this.mtcRandomTotalRB,
				this.mtcRandomTypeRB, this.mtcUnchangedRB, this.mtmRandomRB,
				this.mtmUnchangedRB));

		try {
			byte[] romName = romHandler.getROMName().getBytes("US-ASCII");
			baos.write(romName.length);
			baos.write(romName);
		} catch (UnsupportedEncodingException e) {
			baos.write(0);
		} catch (IOException e) {
			baos.write(0);
		}

		byte[] current = baos.toByteArray();
		CRC32 checksum = new CRC32();
		checksum.update(current);

		try {
			writeChecksum(baos, (int) checksum.getValue());
			writeChecksum(baos, getFileChecksum("trainerclasses.txt"));
			writeChecksum(baos, getFileChecksum("trainernames.txt"));
		} catch (IOException e) {
		}

		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	private static int getFileChecksum(String filename) {
		try {
			Scanner sc = new Scanner(FileFunctions.openConfig(filename));
			CRC32 checksum = new CRC32();
			while (sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				if (!line.isEmpty()) {
					checksum.update(line.getBytes("UTF-8"));
				}
			}
			sc.close();
			return (int) checksum.getValue();
		} catch (IOException e) {
			return 0;
		}

	}

	public static String getValidRequiredROMName(String config,
			byte[] trainerClasses, byte[] trainerNames)
			throws UnsupportedEncodingException,
			InvalidSupplementFilesException {
		byte[] data = DatatypeConverter.parseBase64Binary(config);

		if (data.length < 29) {
			return null; // too short
		}

		// Check the checksum
		ByteBuffer buf = ByteBuffer.allocate(4).put(data, data.length - 12, 4);
		buf.rewind();
		int crc = buf.getInt();

		CRC32 checksum = new CRC32();
		checksum.update(data, 0, data.length - 12);
		if ((int) checksum.getValue() != crc) {
			return null; // checksum failure
		}

		// Check the trainerclass & trainernames crc
		if (trainerClasses == null
				&& !checkOtherCRC(data, 0, 6, "trainerclasses.txt",
						data.length - 8)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Can't use this preset because you have a different set of random trainer class names to the creator.\nHave them make you a rndp file instead.");
			throw new InvalidSupplementFilesException();
		}
		if (trainerNames == null
				&& !checkOtherCRC(data, 0, 5, "trainernames.txt",
						data.length - 4)) {
			JOptionPane
					.showMessageDialog(
							null,
							"Can't use this preset because you have a different set of random trainer names to the creator.\nHave them make you a rndp file instead.");
			throw new InvalidSupplementFilesException();
		}

		int nameLength = data[16] & 0xFF;
		if (data.length != 29 + nameLength) {
			return null; // not valid length
		}
		String name = new String(data, 17, nameLength, "US-ASCII");
		return name;
	}

	private static boolean checkOtherCRC(byte[] data, int byteIndex,
			int switchIndex, String filename, int offsetInData) {
		// If the switch at data[byteIndex].switchIndex is on,
		// then check that the CRC at
		// data[offsetInData] ... data[offsetInData+3]
		// matches the CRC of filename.
		// If not, return false.
		// If any other case, return true.
		int switches = data[byteIndex] & 0xFF;
		if (((switches >> switchIndex) & 0x01) == 0x01) {
			// have to check the CRC
			ByteBuffer buf = ByteBuffer.allocate(4).put(data, offsetInData, 4);
			buf.rewind();
			int crc = buf.getInt();

			if (getFileChecksum(filename) != crc) {
				return false;
			}
		}
		return true;
	}

	private boolean restoreFrom(String config) {
		// Need to add enables
		byte[] data = DatatypeConverter.parseBase64Binary(config);

		// Check the checksum
		ByteBuffer buf = ByteBuffer.allocate(4).put(data, data.length - 12, 4);
		buf.rewind();
		int crc = buf.getInt();

		CRC32 checksum = new CRC32();
		checksum.update(data, 0, data.length - 12);

		if ((int) checksum.getValue() != crc) {
			return false; // checksum failure
		}

		// Restore the actual controls
		restoreStates(data[0], this.goLowerCaseNamesCheckBox,
				this.goNationalDexCheckBox, this.goRemoveTradeEvosCheckBox,
				this.goUpdateMovesCheckBox, this.goUpdateTypesCheckBox,
				this.tnRandomizeCB, this.tcnRandomizeCB);

		restoreStates(data[1], this.pbsChangesRandomEvosRB,
				this.pbsChangesRandomTotalRB, this.pbsChangesShuffleRB,
				this.pbsChangesUnchangedRB, this.paUnchangedRB,
				this.paRandomizeRB, this.paWonderGuardCB);

		restoreStates(data[2], this.ptRandomFollowEvosRB, this.ptRandomTotalRB,
				this.ptUnchangedRB, this.bwEXPPatchCB, this.raceModeCB,
				this.randomizeHollowsCB, this.brokenMovesCB, this.heldItemsCB);

		restoreStates(data[3], this.spCustomRB, this.spRandomRB,
				this.spUnchangedRB, this.spRandom2EvosRB);

		restoreSelectedIndex(data, 4, this.spCustomPoke1Chooser);
		restoreSelectedIndex(data, 6, this.spCustomPoke2Chooser);
		restoreSelectedIndex(data, 8, this.spCustomPoke3Chooser);

		restoreStates(data[10], this.pmsRandomTotalRB, this.pmsRandomTypeRB,
				this.pmsUnchangedRB);

		// Read from the trainer byte for wild no legendaries
		// Because the wild byte is already full.
		restoreStates(data[11], this.tpPowerLevelsCB, this.tpRandomRB,
				this.tpRivalCarriesStarterCB, this.tpTypeThemedRB,
				this.tpTypeWeightingCB, this.tpUnchangedRB,
				this.tpNoLegendariesCB, this.wpNoLegendariesCB);

		restoreStates(data[12], this.wpARCatchEmAllRB, this.wpArea11RB,
				this.wpARNoneRB, this.wpARTypeThemedRB, this.wpGlobalRB,
				this.wpRandomRB, this.wpUnchangedRB, this.wpUseTimeCB);

		// Add the catch rate here because wild byte is full
		// Add trainer no shedinja here too

		restoreStates(data[13], this.stpUnchangedRB, this.stpRandomL4LRB,
				this.stpRandomTotalRB, this.wpCatchRateCB,
				this.tpNoEarlyShedinjaCB);

		restoreStates(data[14], this.thcRandomTotalRB, this.thcRandomTypeRB,
				this.thcUnchangedRB, this.tmmRandomRB, this.tmmUnchangedRB);
		restoreStates(data[15], this.mtcRandomTotalRB, this.mtcRandomTypeRB,
				this.mtcUnchangedRB, this.mtmRandomRB, this.mtmUnchangedRB);

		this.enableOrDisableSubControls();

		// Name data is ignored here - we should've used it earlier.
		return true;
	}

	private void restoreSelectedIndex(byte[] data, int offset,
			JComboBox comboBox) {
		int selIndex = (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
		if (comboBox.getModel().getSize() > selIndex) {
			comboBox.setSelectedIndex(selIndex);
		} else if (this.spCustomRB.isSelected()) {
			JOptionPane
					.showMessageDialog(
							this,
							"Could not set one of the custom starters from the settings file because it does not exist in this generation.");
		}
	}

	private void restoreStates(byte b, AbstractButton... switches) {
		int value = b & 0xFF;
		for (int i = 0; i < switches.length; i++) {
			int realValue = (value >> i) & 0x01;
			switches[i].setSelected(realValue == 0x01);
		}
	}

	private void writeChecksum(ByteArrayOutputStream baos, int checksum)
			throws IOException {
		byte[] crc = ByteBuffer.allocate(4).putInt(checksum).array();
		baos.write(crc);

	}

	private void writePokemonIndex(ByteArrayOutputStream baos,
			JComboBox comboBox) {
		baos.write(comboBox.getSelectedIndex() & 0xFF);
		baos.write((comboBox.getSelectedIndex() >> 8) & 0xFF);

	}

	private int makeByteSelected(AbstractButton... switches) {
		if (switches.length > 8) {
			// No can do
			return 0;
		}
		int initial = 0;
		int state = 1;
		for (AbstractButton b : switches) {
			initial |= b.isSelected() ? state : 0;
			state *= 2;
		}
		return initial;
	}

	private void performRandomization(final String filename, final long seed,
			byte[] trainerClasses, byte[] trainerNames) {
		final boolean raceMode = raceModeCB.isSelected();
		int checkValue = 0;
		final long startTime = System.currentTimeMillis();
		// Setup verbose log
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String nl = System.getProperty("line.separator");
		try {
			verboseLog = new PrintStream(baos, false, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			verboseLog = new PrintStream(baos);
		}

		// Update type effectiveness in RBY?
		if (romHandler instanceof Gen1RomHandler
				&& this.goUpdateTypesCheckBox.isSelected()) {
			romHandler.fixTypeEffectiveness();
		}

		// Move updates
		if (!(romHandler instanceof Gen5RomHandler)
				&& this.goUpdateMovesCheckBox.isSelected()) {
			romHandler.applyMoveUpdates();
		}

		// Trade evolutions removal
		if (this.goRemoveTradeEvosCheckBox.isSelected()) {
			romHandler.removeTradeEvolutions();
		}

		// Camel case?
		if (!(romHandler instanceof Gen5RomHandler)
				&& !(romHandler instanceof Gen4RomHandler)
				&& this.goLowerCaseNamesCheckBox.isSelected()) {
			romHandler.applyCamelCaseNames();
		}

		// National dex gen3?
		if (romHandler instanceof Gen3RomHandler
				&& this.goNationalDexCheckBox.isSelected()) {
			romHandler.patchForNationalDex();
		}

		// BW Exp Patch?
		if (romHandler.hasBWEXPPatch() && this.bwEXPPatchCB.isSelected()) {
			romHandler.applyBWEXPPatch();
		}

		// Hollows?
		if (romHandler.hasHiddenHollowPokemon()
				&& this.randomizeHollowsCB.isSelected()) {
			romHandler.randomizeHiddenHollowPokemon();
		}

		List<Pokemon> allPokes = romHandler.getPokemon();

		// Base stats changing
		if (this.pbsChangesShuffleRB.isSelected()) {
			romHandler.shufflePokemonStats();
		} else if (this.pbsChangesRandomEvosRB.isSelected()) {
			romHandler.randomizePokemonStats(true);
		} else if (this.pbsChangesRandomTotalRB.isSelected()) {
			romHandler.randomizePokemonStats(false);
		}

		// Abilities? (new 1.0.2)
		if (this.romHandler.abilitiesPerPokemon() > 0
				&& this.paRandomizeRB.isSelected()) {
			romHandler.randomizeAbilities(this.paWonderGuardCB.isSelected());
		}

		// Pokemon Types
		if (this.ptRandomFollowEvosRB.isSelected()) {
			romHandler.randomizePokemonTypes(true);
		} else if (this.ptRandomTotalRB.isSelected()) {
			romHandler.randomizePokemonTypes(false);
		}

		// Wild Held Items?
		String[] itemNames = romHandler.getItemNames();
		if (this.heldItemsCB.isSelected()) {
			romHandler.randomizeWildHeldItems();
		}

		// Log base stats & types if changed at all
		if (this.pbsChangesUnchangedRB.isSelected()
				&& this.ptUnchangedRB.isSelected()
				&& this.paUnchangedRB.isSelected()
				&& this.heldItemsCB.isSelected() == false) {
			verboseLog.println("Pokemon base stats & type: unchanged" + nl);
		} else {
			verboseLog.println("--Pokemon Base Stats & Types--");
			if (romHandler instanceof Gen1RomHandler) {
				verboseLog
						.println("NUM|NAME      |TYPE             |  HP| ATK| DEF| SPE|SPEC");
				for (Pokemon pkmn : allPokes) {
					if (pkmn != null) {
						String typeString = pkmn.primaryType.toString();
						if (pkmn.secondaryType != null) {
							typeString += "/" + pkmn.secondaryType.toString();
						}
						verboseLog.printf("%3d|%-10s|%-17s|%4d|%4d|%4d|%4d|%4d"
								+ nl, pkmn.number, pkmn.name, typeString,
								pkmn.hp, pkmn.attack, pkmn.defense, pkmn.speed,
								pkmn.special);
					}

				}
			} else {
				verboseLog
						.print("NUM|NAME      |TYPE             |  HP| ATK| DEF| SPE|SATK|SDEF");
				int abils = romHandler.abilitiesPerPokemon();
				for (int i = 0; i < abils; i++) {
					verboseLog.print("|ABILITY" + (i + 1) + "    ");
				}
				verboseLog.print("|ITEM");
				verboseLog.println();
				for (Pokemon pkmn : allPokes) {
					if (pkmn != null) {
						String typeString = pkmn.primaryType.toString();
						if (pkmn.secondaryType != null) {
							typeString += "/" + pkmn.secondaryType.toString();
						}
						verboseLog.printf(
								"%3d|%-10s|%-17s|%4d|%4d|%4d|%4d|%4d|%4d",
								pkmn.number, pkmn.name, typeString, pkmn.hp,
								pkmn.attack, pkmn.defense, pkmn.speed,
								pkmn.spatk, pkmn.spdef);
						if (abils > 0) {
							verboseLog.printf("|%-12s|%-12s",
									romHandler.abilityName(pkmn.ability1),
									romHandler.abilityName(pkmn.ability2));
							if (abils > 2) {
								verboseLog.printf("|%-12s",
										romHandler.abilityName(pkmn.ability3));
							}
						}
						verboseLog.print("|");
						if (pkmn.guaranteedHeldItem > 0) {
							verboseLog.print(itemNames[pkmn.guaranteedHeldItem]
									+ " (100%)");
						} else {
							int itemCount = 0;
							if (pkmn.commonHeldItem > 0) {
								itemCount++;
								verboseLog.print(itemNames[pkmn.commonHeldItem]
										+ " (common)");
							}
							if (pkmn.rareHeldItem > 0) {
								if (itemCount > 0) {
									verboseLog.print(", ");
								}
								itemCount++;
								verboseLog.print(itemNames[pkmn.rareHeldItem]
										+ " (rare)");
							}
							if (pkmn.darkGrassHeldItem > 0) {
								if (itemCount > 0) {
									verboseLog.print(", ");
								}
								itemCount++;
								verboseLog
										.print(itemNames[pkmn.darkGrassHeldItem]
												+ " (dark grass only)");
							}
						}
						verboseLog.println();
					}

				}
			}
			if (raceMode) {
				for (Pokemon pkmn : allPokes) {
					if (pkmn != null) {
						checkValue = addToCV(checkValue, pkmn.hp, pkmn.attack,
								pkmn.defense, pkmn.speed, pkmn.spatk,
								pkmn.spdef, pkmn.ability1, pkmn.ability2,
								pkmn.ability3);
					}
				}
			}
			verboseLog.println();
		}

		// Starter Pokemon
		// Applied after type to update the strings correctly based on new types
		if (romHandler.canChangeStarters()) {
			if (this.spCustomRB.isSelected()) {
				verboseLog.println("--Custom Starters--");
				Pokemon pkmn1 = allPokes.get(this.spCustomPoke1Chooser
						.getSelectedIndex() + 1);
				verboseLog.println("Set starter 1 to " + pkmn1.name);
				Pokemon pkmn2 = allPokes.get(this.spCustomPoke2Chooser
						.getSelectedIndex() + 1);
				verboseLog.println("Set starter 2 to " + pkmn2.name);
				if (romHandler.isYellow()) {
					romHandler.setStarters(Arrays.asList(pkmn1, pkmn2));
				} else {
					Pokemon pkmn3 = allPokes.get(this.spCustomPoke3Chooser
							.getSelectedIndex() + 1);
					verboseLog.println("Set starter 3 to " + pkmn3.name);
					romHandler.setStarters(Arrays.asList(pkmn1, pkmn2, pkmn3));
				}
				verboseLog.println();

			} else if (this.spRandomRB.isSelected()) {
				// Randomise
				verboseLog.println("--Random Starters--");
				int starterCount = 3;
				if (romHandler.isYellow()) {
					starterCount = 2;
				}
				List<Pokemon> starters = new ArrayList<Pokemon>();
				for (int i = 0; i < starterCount; i++) {
					Pokemon pkmn = romHandler.randomPokemon();
					while (starters.contains(pkmn)) {
						pkmn = romHandler.randomPokemon();
					}
					verboseLog.println("Set starter " + (i + 1) + " to "
							+ pkmn.name);
					starters.add(pkmn);
				}
				romHandler.setStarters(starters);
				verboseLog.println();
			} else if (this.spRandom2EvosRB.isSelected()) {
				// Randomise
				verboseLog.println("--Random 2-Evolution Starters--");
				int starterCount = 3;
				if (romHandler.isYellow()) {
					starterCount = 2;
				}
				List<Pokemon> starters = new ArrayList<Pokemon>();
				for (int i = 0; i < starterCount; i++) {
					Pokemon pkmn = romHandler.random2EvosPokemon();
					while (starters.contains(pkmn)) {
						pkmn = romHandler.random2EvosPokemon();
					}
					verboseLog.println("Set starter " + (i + 1) + " to "
							+ pkmn.name);
					starters.add(pkmn);
				}
				romHandler.setStarters(starters);
				verboseLog.println();
			}
			if (this.heldItemsCB.isSelected()
					&& (romHandler instanceof Gen1RomHandler) == false) {
				romHandler.randomizeStarterHeldItems();
			}
		}

		// Movesets
		boolean noBrokenMoves = this.brokenMovesCB.isSelected();
		if (this.pmsRandomTypeRB.isSelected()) {
			romHandler.randomizeMovesLearnt(true, noBrokenMoves);
		} else if (this.pmsRandomTotalRB.isSelected()) {
			romHandler.randomizeMovesLearnt(false, noBrokenMoves);
		}

		// Show the new movesets if applicable
		if (this.pmsUnchangedRB.isSelected()) {
			verboseLog.println("Pokemon Movesets: Unchanged." + nl);
		} else {
			verboseLog.println("--Pokemon Movesets--");
			List<String> movesets = new ArrayList<String>();
			Map<Pokemon, List<MoveLearnt>> moveData = romHandler
					.getMovesLearnt();
			for (Pokemon pkmn : moveData.keySet()) {
				StringBuilder sb = new StringBuilder();
				sb.append(String
						.format("%03d %-10s : ", pkmn.number, pkmn.name));
				List<MoveLearnt> data = moveData.get(pkmn);
				boolean first = true;
				for (MoveLearnt ml : data) {
					if (!first) {
						sb.append(", ");
					}
					sb.append(RomFunctions.moveNames[ml.move] + " at level "
							+ ml.level);
					first = false;
				}
				movesets.add(sb.toString());
			}
			Collections.sort(movesets);
			for (String moveset : movesets) {
				verboseLog.println(moveset);
			}
			verboseLog.println();
		}

		// Trainer Pokemon
		if (this.tpRandomRB.isSelected()) {
			romHandler.randomizeTrainerPokes(
					this.tpRivalCarriesStarterCB.isSelected(),
					this.tpPowerLevelsCB.isSelected(),
					this.tpNoLegendariesCB.isSelected(),
					this.tpNoEarlyShedinjaCB.isSelected());
		} else if (this.tpTypeThemedRB.isSelected()) {
			romHandler.typeThemeTrainerPokes(
					this.tpRivalCarriesStarterCB.isSelected(),
					this.tpPowerLevelsCB.isSelected(),
					this.tpTypeWeightingCB.isSelected(),
					this.tpNoLegendariesCB.isSelected(),
					this.tpNoEarlyShedinjaCB.isSelected());
		}

		if (this.tpUnchangedRB.isSelected()) {
			verboseLog.println("Trainers: Unchanged." + nl);
		} else {
			verboseLog.println("--Trainers Pokemon--");
			List<Trainer> trainers = romHandler.getTrainers();
			int idx = 0;
			for (Trainer t : trainers) {
				idx++;
				verboseLog.print("#" + idx + " ");
				if (t.name != null) {
					verboseLog.print("(" + t.name + ")");
				}
				if (t.offset != idx && t.offset != 0) {
					verboseLog.printf("@%X", t.offset);
				}
				verboseLog.print(" - ");
				boolean first = true;
				for (TrainerPokemon tpk : t.pokemon) {
					if (!first) {
						verboseLog.print(", ");
					}
					verboseLog.print(tpk.pokemon.name + " Lv" + tpk.level);
					first = false;
				}
				verboseLog.println();
			}
			verboseLog.println();
		}

		if (raceMode) {
			List<Trainer> trainers = romHandler.getTrainers();
			for (Trainer t : trainers) {
				for (TrainerPokemon tpk : t.pokemon) {
					checkValue = addToCV(checkValue, tpk.level,
							tpk.pokemon.number);
				}
			}
		}

		// Trainer names & class names randomization

		if (this.tcnRandomizeCB.isSelected()) {
			romHandler.randomizeTrainerClassNames(trainerClasses);
		}

		if (this.tnRandomizeCB.isSelected()) {
			romHandler.randomizeTrainerNames(trainerNames);
		}

		// Wild Pokemon
		if (this.wpRandomRB.isSelected()) {
			romHandler.randomEncounters(this.wpUseTimeCB.isSelected(),
					this.wpARCatchEmAllRB.isSelected(),
					this.wpARTypeThemedRB.isSelected(),
					this.wpNoLegendariesCB.isSelected());
		} else if (this.wpArea11RB.isSelected()) {
			romHandler.area1to1Encounters(this.wpUseTimeCB.isSelected(),
					this.wpARCatchEmAllRB.isSelected(),
					this.wpARTypeThemedRB.isSelected(),
					this.wpNoLegendariesCB.isSelected());
		} else if (this.wpGlobalRB.isSelected()) {
			romHandler.game1to1Encounters(this.wpUseTimeCB.isSelected(),
					this.wpNoLegendariesCB.isSelected());
		}

		if (this.wpUnchangedRB.isSelected()) {
			verboseLog.println("Wild Pokemon: Unchanged." + nl);
		} else {
			verboseLog.println("--Wild Pokemon--");
			List<EncounterSet> encounters = romHandler
					.getEncounters(this.wpUseTimeCB.isSelected());
			int idx = 0;
			for (EncounterSet es : encounters) {
				idx++;
				verboseLog.print("Set #" + idx + " ");
				verboseLog.print("(rate=" + es.rate + ")");
				verboseLog.print(" - ");
				boolean first = true;
				for (Encounter e : es.encounters) {
					if (!first) {
						verboseLog.print(", ");
					}
					verboseLog.print(e.pokemon.name + " Lv");
					if (e.maxLevel > 0 && e.maxLevel != e.level) {
						verboseLog.print("s " + e.level + "-" + e.maxLevel);
					} else {
						verboseLog.print(e.level);
					}
					first = false;
				}
				verboseLog.println();
			}
			verboseLog.println();
		}

		if (raceMode) {
			List<EncounterSet> encounters = romHandler
					.getEncounters(this.wpUseTimeCB.isSelected());
			for (EncounterSet es : encounters) {
				for (Encounter e : es.encounters) {
					checkValue = addToCV(checkValue, e.level, e.pokemon.number);
				}
			}
		}

		// Static Pokemon

		if (romHandler.canChangeStaticPokemon()) {
			List<Pokemon> oldStatics = romHandler.getStaticPokemon();
			if (this.stpRandomL4LRB.isSelected()) {
				romHandler.randomizeStaticPokemon(true);
			} else if (this.stpRandomTotalRB.isSelected()) {
				romHandler.randomizeStaticPokemon(false);
			}
			List<Pokemon> newStatics = romHandler.getStaticPokemon();
			if (this.stpUnchangedRB.isSelected()) {
				verboseLog.println("Static Pokemon: Unchanged." + nl);
			} else {
				verboseLog.println("--Static Pokemon--");
				Map<Pokemon, Integer> seenPokemon = new TreeMap<Pokemon, Integer>();
				for (int i = 0; i < oldStatics.size(); i++) {
					Pokemon oldP = oldStatics.get(i);
					Pokemon newP = newStatics.get(i);
					if (raceMode) {
						checkValue = addToCV(checkValue, newP.number);
					}
					verboseLog.print(oldP.name);
					if (seenPokemon.containsKey(oldP)) {
						int amount = seenPokemon.get(oldP);
						verboseLog.print("(" + (++amount) + ")");
						seenPokemon.put(oldP, amount);
					} else {
						seenPokemon.put(oldP, 1);
					}
					verboseLog.println(" => " + newP.name);
				}
				verboseLog.println();
			}
		}

		// TMs
		if (this.tmmRandomRB.isSelected()) {
			romHandler.randomizeTMMoves(noBrokenMoves);
			verboseLog.println("--TM Moves--");
			List<Integer> tmMoves = romHandler.getTMMoves();
			for (int i = 0; i < tmMoves.size(); i++) {
				verboseLog.printf("TM%02d %s" + nl, i + 1,
						RomFunctions.moveNames[tmMoves.get(i)]);
				if (raceMode) {
					checkValue = addToCV(checkValue, tmMoves.get(i));
				}
			}
			verboseLog.println();
		} else {
			verboseLog.println("TM Moves: Unchanged." + nl);
		}

		// TM/HM compatibility
		if (this.thcRandomTypeRB.isSelected()) {
			romHandler.randomizeTMHMCompatibility(true);
		} else if (this.thcRandomTotalRB.isSelected()) {
			romHandler.randomizeTMHMCompatibility(false);
		}

		// Move Tutors (new 1.0.3)
		if (this.romHandler.hasMoveTutors()) {
			if (this.mtmRandomRB.isSelected()) {
				List<Integer> oldMtMoves = romHandler.getMoveTutorMoves();
				romHandler.randomizeMoveTutorMoves(noBrokenMoves);
				verboseLog.println("--Move Tutor Moves--");
				List<Integer> newMtMoves = romHandler.getMoveTutorMoves();
				for (int i = 0; i < newMtMoves.size(); i++) {
					verboseLog.printf("%s => %s" + nl,
							RomFunctions.moveNames[oldMtMoves.get(i)],
							RomFunctions.moveNames[newMtMoves.get(i)]);
					if (raceMode) {
						checkValue = addToCV(checkValue, newMtMoves.get(i));
					}
				}
				verboseLog.println();
			} else {
				verboseLog.println("Move Tutor Moves: Unchanged." + nl);
			}

			// Compatibility
			if (this.mtcRandomTypeRB.isSelected()) {
				romHandler.randomizeMoveTutorCompatibility(true);
			} else if (this.mtcRandomTotalRB.isSelected()) {
				romHandler.randomizeMoveTutorCompatibility(false);
			}
		}

		// Signature...
		romHandler.applySignature();

		// Save
		final int finishedCV = checkValue;
		opDialog = new OperationDialog("Saving...", this, true);
		Thread t = new Thread() {
			@Override
			public void run() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						opDialog.setVisible(true);
					}
				});
				try {
					RandomizerGUI.this.romHandler.saveRom(filename);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(RandomizerGUI.this,
							"ROM save failed.");
				}
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						RandomizerGUI.this.opDialog.setVisible(false);
						// Log tail
						verboseLog
								.println("------------------------------------------------------------------");
						verboseLog.println("Randomization of "
								+ romHandler.getROMName() + " completed.");
						verboseLog.println("Time elapsed: "
								+ (System.currentTimeMillis() - startTime)
								+ "ms");
						verboseLog.println("RNG Calls: "
								+ RandomSource.callsSinceSeed());
						verboseLog
								.println("------------------------------------------------------------------");

						// Log?
						verboseLog.close();
						byte[] out = baos.toByteArray();
						verboseLog = System.out;

						if (raceMode) {
							JOptionPane
									.showMessageDialog(
											RandomizerGUI.this,
											"Your check value for the race is:\n"
													+ String.format("%08X",
															finishedCV)
													+ "\nDistribute this along with the preset file, if you're the race maker!\n"
													+ "If you received this in a race, compare it to the value the race maker gave.");
						} else {
							int response = JOptionPane
									.showConfirmDialog(
											RandomizerGUI.this,
											"Do you want to save a log file of the randomization performed?\nThis may allow you to gain an unfair advantage, do not do so if you are doing something like a race.",
											"Save Log?",
											JOptionPane.YES_NO_OPTION);
							if (response == JOptionPane.YES_OPTION) {
								try {
									FileOutputStream fos = new FileOutputStream(
											filename + ".log");
									fos.write(0xEF);
									fos.write(0xBB);
									fos.write(0xBF);
									fos.write(out);
									fos.close();
								} catch (IOException e) {
									JOptionPane.showMessageDialog(
											RandomizerGUI.this,
											"Could not save log file!");
									return;
								}
								JOptionPane.showMessageDialog(
										RandomizerGUI.this,
										"Log file saved to\n" + filename
												+ ".log");
							}
						}
						if (presetMode) {
							JOptionPane
									.showMessageDialog(RandomizerGUI.this,
											"Randomization Complete. You can now play!");
							// Done
							RandomizerGUI.this.romHandler = null;
							initialFormState();
						} else {
							// Compile a config string
							String configString = getConfigString();
							// Show the preset maker
							new PresetMakeDialog(RandomizerGUI.this, seed,
									configString);

							// Done
							RandomizerGUI.this.romHandler = null;
							initialFormState();
						}
					}
				});
			}
		};
		t.start();

	}

	private int addToCV(int checkValue, int... values) {
		for (int value : values) {
			checkValue = Integer.rotateLeft(checkValue, 3);
			checkValue ^= value;
		}
		return checkValue;
	}

	private void presetLoader() {
		PresetLoadDialog pld = new PresetLoadDialog(this);
		if (pld.isCompleted()) {
			// Apply it
			long seed = pld.getSeed();
			String config = pld.getConfigString();
			this.romHandler = pld.getROM();
			this.romLoaded();
			this.restoreFrom(config);
			romSaveChooser.setSelectedFile(null);
			int returnVal = romSaveChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File fh = romSaveChooser.getSelectedFile();
				// Fix or add extension
				List<String> extensions = new ArrayList<String>(Arrays.asList(
						"sgb", "gbc", "gba", "nds"));
				extensions.remove(this.romHandler.getDefaultExtension());
				fh = FileFunctions.fixFilename(fh,
						this.romHandler.getDefaultExtension(), extensions);
				// Apply the seed we were given
				RandomSource.seed(seed);
				presetMode = true;
				performRandomization(fh.getAbsolutePath(), seed,
						pld.getTrainerClasses(), pld.getTrainerNames());

			} else {
				this.romHandler = null;
				initialFormState();
			}
		}

	}

	public void updateFound(int newVersion, String changelog) {
		new UpdateFoundDialog(this, newVersion, changelog);
	}

	public void noUpdateFound() {
		JOptionPane.showMessageDialog(this, "No new updates found.");
	}

	private void loadQSButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_loadQSButtonActionPerformed
		if (this.romHandler == null) {
			return;
		}
		qsOpenChooser.setSelectedFile(null);
		int returnVal = qsOpenChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fh = qsOpenChooser.getSelectedFile();
			try {
				FileInputStream fis = new FileInputStream(fh);
				int version = fis.read();
				if (version < PRESET_FILE_VERSION) {
					JOptionPane
							.showMessageDialog(
									this,
									"This quick settings file is for an old randomizer version.\nYou should make a new file.");
					fis.close();
					return;
				} else if (version > PRESET_FILE_VERSION) {
					JOptionPane
							.showMessageDialog(
									this,
									"This quick settings file is for a newer randomizer version.\nYou should upgrade your randomizer.");
					fis.close();
					return;
				}
				int cslength = fis.read();
				byte[] csBuf = new byte[cslength];
				fis.read(csBuf);
				fis.close();
				String configString = new String(csBuf, "UTF-8");
				String romName = getValidRequiredROMName(configString,
						new byte[] {}, new byte[] {});
				if (romName == null) {
					JOptionPane.showMessageDialog(this,
							"Settings file is not valid.");
				}
				// now we just load it
				initialFormState();
				romLoaded();
				restoreFrom(configString);
				JCheckBox[] checkboxes = new JCheckBox[] { this.brokenMovesCB,
						this.bwEXPPatchCB, this.goLowerCaseNamesCheckBox,
						this.goNationalDexCheckBox,
						this.goRemoveTradeEvosCheckBox,
						this.goUpdateMovesCheckBox, this.goUpdateTypesCheckBox,
						this.heldItemsCB, this.paWonderGuardCB,
						this.raceModeCB, this.randomizeHollowsCB,
						this.tcnRandomizeCB, this.tnRandomizeCB,
						this.tpNoEarlyShedinjaCB, this.tpNoLegendariesCB,
						this.tpPowerLevelsCB, this.tpRivalCarriesStarterCB,
						this.tpTypeWeightingCB, this.wpCatchRateCB,
						this.wpNoLegendariesCB, this.wpUseTimeCB };
				for (JCheckBox cb : checkboxes) {
					if (!cb.isEnabled() || !cb.isVisible()) {
						cb.setSelected(false);
					}
				}
				JOptionPane.showMessageDialog(this, "Settings loaded from "
						+ fh.getName());
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this,
						"Settings file load failed. Please try again.");
			} catch (InvalidSupplementFilesException e) {
				// not possible
			}
		}
	}// GEN-LAST:event_loadQSButtonActionPerformed

	private void saveQSButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveQSButtonActionPerformed
		if (this.romHandler == null) {
			return;
		}
		qsSaveChooser.setSelectedFile(null);
		int returnVal = qsSaveChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fh = qsSaveChooser.getSelectedFile();
			// Fix or add extension
			fh = FileFunctions.fixFilename(fh, "rnqs");
			// Save now?
			try {
				FileOutputStream fos = new FileOutputStream(fh);
				fos.write(PRESET_FILE_VERSION);
				byte[] configString = getConfigString().getBytes("UTF-8");
				fos.write(configString.length);
				fos.write(configString);
				fos.close();
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this,
						"Settings file save failed. Please try again.");
			}
		}
	}// GEN-LAST:event_saveQSButtonActionPerformed

	private void mtmUnchangedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mtmUnchangedRBActionPerformed
	}// GEN-LAST:event_mtmUnchangedRBActionPerformed

	private void paUnchangedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_paUnchangedRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_paUnchangedRBActionPerformed

	private void paRandomizeRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_paRandomizeRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_paRandomizeRBActionPerformed

	private void aboutButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_aboutButtonActionPerformed
		new AboutDialog(this, true).setVisible(true);
	}// GEN-LAST:event_aboutButtonActionPerformed

	private void openROMButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openROMButtonActionPerformed
		loadROM();
	}// GEN-LAST:event_openROMButtonActionPerformed

	private void saveROMButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveROMButtonActionPerformed
		saveROM();
	}// GEN-LAST:event_saveROMButtonActionPerformed

	private void usePresetsButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_usePresetsButtonActionPerformed
		presetLoader();
	}// GEN-LAST:event_usePresetsButtonActionPerformed

	private void wpUnchangedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_wpUnchangedRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_wpUnchangedRBActionPerformed

	private void tpUnchangedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tpUnchangedRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_tpUnchangedRBActionPerformed

	private void tpRandomRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tpRandomRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_tpRandomRBActionPerformed

	private void tpTypeThemedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_tpTypeThemedRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_tpTypeThemedRBActionPerformed

	private void spUnchangedRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_spUnchangedRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_spUnchangedRBActionPerformed

	private void spCustomRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_spCustomRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_spCustomRBActionPerformed

	private void spRandomRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_spRandomRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_spRandomRBActionPerformed

	private void wpRandomRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_wpRandomRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_wpRandomRBActionPerformed

	private void wpArea11RBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_wpArea11RBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_wpArea11RBActionPerformed

	private void wpGlobalRBActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_wpGlobalRBActionPerformed
		this.enableOrDisableSubControls();
	}// GEN-LAST:event_wpGlobalRBActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());
			// for (javax.swing.UIManager.LookAndFeelInfo info :
			// javax.swing.UIManager
			// .getInstalledLookAndFeels()) {
			// if ("Nimbus".equals(info.getName())) {
			// javax.swing.UIManager.setLookAndFeel(info.getClassName());
			// break;
			// }
			// }
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(RandomizerGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(RandomizerGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(RandomizerGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(RandomizerGUI.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new RandomizerGUI().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel abilitiesPanel;
	private javax.swing.JButton aboutButton;
	private javax.swing.JPanel baseStatsPanel;
	private javax.swing.JCheckBox brokenMovesCB;
	private javax.swing.JCheckBox bwEXPPatchCB;
	private javax.swing.JPanel generalOptionsPanel;
	private javax.swing.JCheckBox goLowerCaseNamesCheckBox;
	private javax.swing.JCheckBox goNationalDexCheckBox;
	private javax.swing.JCheckBox goRemoveTradeEvosCheckBox;
	private javax.swing.JCheckBox goUpdateMovesCheckBox;
	private javax.swing.JCheckBox goUpdateTypesCheckBox;
	private javax.swing.JCheckBox heldItemsCB;
	private javax.swing.JButton loadQSButton;
	private javax.swing.JPanel moveTutorsPanel;
	private javax.swing.JPanel mtCompatPanel;
	private javax.swing.ButtonGroup mtCompatibilityButtonGroup;
	private javax.swing.ButtonGroup mtMovesButtonGroup;
	private javax.swing.JPanel mtMovesPanel;
	private javax.swing.JLabel mtNoExistLabel;
	private javax.swing.JRadioButton mtcRandomTotalRB;
	private javax.swing.JRadioButton mtcRandomTypeRB;
	private javax.swing.JRadioButton mtcUnchangedRB;
	private javax.swing.JRadioButton mtmRandomRB;
	private javax.swing.JRadioButton mtmUnchangedRB;
	private javax.swing.JButton openROMButton;
	private javax.swing.JPanel optionsContainerPanel;
	private javax.swing.JScrollPane optionsScrollPane;
	private javax.swing.JPanel otherOptionsPanel;
	private javax.swing.JRadioButton paRandomizeRB;
	private javax.swing.JRadioButton paUnchangedRB;
	private javax.swing.JCheckBox paWonderGuardCB;
	private javax.swing.JRadioButton pbsChangesRandomEvosRB;
	private javax.swing.JRadioButton pbsChangesRandomTotalRB;
	private javax.swing.JRadioButton pbsChangesShuffleRB;
	private javax.swing.JRadioButton pbsChangesUnchangedRB;
	private javax.swing.JRadioButton pmsRandomTotalRB;
	private javax.swing.JRadioButton pmsRandomTypeRB;
	private javax.swing.JRadioButton pmsUnchangedRB;
	private javax.swing.ButtonGroup pokeAbilitiesButtonGroup;
	private javax.swing.ButtonGroup pokeMovesetsButtonGroup;
	private javax.swing.ButtonGroup pokeStatChangesButtonGroup;
	private javax.swing.ButtonGroup pokeTypesButtonGroup;
	private javax.swing.JPanel pokemonMovesetsPanel;
	private javax.swing.JPanel pokemonTypesPanel;
	private javax.swing.JRadioButton ptRandomFollowEvosRB;
	private javax.swing.JRadioButton ptRandomTotalRB;
	private javax.swing.JRadioButton ptUnchangedRB;
	private javax.swing.JFileChooser qsOpenChooser;
	private javax.swing.JFileChooser qsSaveChooser;
	private javax.swing.JCheckBox raceModeCB;
	private javax.swing.JCheckBox randomizeHollowsCB;
	private javax.swing.JLabel riRomCodeLabel;
	private javax.swing.JLabel riRomNameLabel;
	private javax.swing.JLabel riRomSupportLabel;
	private javax.swing.JPanel romInfoPanel;
	private javax.swing.JFileChooser romOpenChooser;
	private javax.swing.JFileChooser romSaveChooser;
	private javax.swing.JButton saveQSButton;
	private javax.swing.JButton saveROMButton;
	private javax.swing.JComboBox spCustomPoke1Chooser;
	private javax.swing.JComboBox spCustomPoke2Chooser;
	private javax.swing.JComboBox spCustomPoke3Chooser;
	private javax.swing.JRadioButton spCustomRB;
	private javax.swing.JRadioButton spRandom2EvosRB;
	private javax.swing.JRadioButton spRandomRB;
	private javax.swing.JRadioButton spUnchangedRB;
	private javax.swing.ButtonGroup starterPokemonButtonGroup;
	private javax.swing.JPanel starterPokemonPanel;
	private javax.swing.ButtonGroup staticPokemonButtonGroup;
	private javax.swing.JPanel staticPokemonPanel;
	private javax.swing.JRadioButton stpRandomL4LRB;
	private javax.swing.JRadioButton stpRandomTotalRB;
	private javax.swing.JRadioButton stpUnchangedRB;
	private javax.swing.JCheckBox tcnRandomizeCB;
	private javax.swing.JRadioButton thcRandomTotalRB;
	private javax.swing.JRadioButton thcRandomTypeRB;
	private javax.swing.JRadioButton thcUnchangedRB;
	private javax.swing.JPanel tmHmCompatPanel;
	private javax.swing.ButtonGroup tmHmCompatibilityButtonGroup;
	private javax.swing.ButtonGroup tmMovesButtonGroup;
	private javax.swing.JPanel tmMovesPanel;
	private javax.swing.JPanel tmhmsPanel;
	private javax.swing.JRadioButton tmmRandomRB;
	private javax.swing.JRadioButton tmmUnchangedRB;
	private javax.swing.JCheckBox tnRandomizeCB;
	private javax.swing.JCheckBox tpNoEarlyShedinjaCB;
	private javax.swing.JCheckBox tpNoLegendariesCB;
	private javax.swing.JCheckBox tpPowerLevelsCB;
	private javax.swing.JRadioButton tpRandomRB;
	private javax.swing.JCheckBox tpRivalCarriesStarterCB;
	private javax.swing.JRadioButton tpTypeThemedRB;
	private javax.swing.JCheckBox tpTypeWeightingCB;
	private javax.swing.JRadioButton tpUnchangedRB;
	private javax.swing.ButtonGroup trainerPokesButtonGroup;
	private javax.swing.JPanel trainersPokemonPanel;
	private javax.swing.JButton usePresetsButton;
	private javax.swing.JPanel wildPokemonARulePanel;
	private javax.swing.JPanel wildPokemonPanel;
	private javax.swing.ButtonGroup wildPokesARuleButtonGroup;
	private javax.swing.ButtonGroup wildPokesButtonGroup;
	private javax.swing.JRadioButton wpARCatchEmAllRB;
	private javax.swing.JRadioButton wpARNoneRB;
	private javax.swing.JRadioButton wpARTypeThemedRB;
	private javax.swing.JRadioButton wpArea11RB;
	private javax.swing.JCheckBox wpCatchRateCB;
	private javax.swing.JRadioButton wpGlobalRB;
	private javax.swing.JCheckBox wpNoLegendariesCB;
	private javax.swing.JRadioButton wpRandomRB;
	private javax.swing.JRadioButton wpUnchangedRB;
	private javax.swing.JCheckBox wpUseTimeCB;
	// End of variables declaration//GEN-END:variables
}
