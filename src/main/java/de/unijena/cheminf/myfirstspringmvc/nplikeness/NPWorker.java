package de.unijena.cheminf.myfirstspringmvc.nplikeness;

import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.InputParser;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.misc.NPScorerConstants;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.scorer.NPScoreCalculator;


import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.scorer.AtomSignatureGenerator;
import de.unijena.cheminf.myfirstspringmvc.nplikeness.scorer.NPScoreCalculator;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.File;

/**
 * @author mSorok
 */

public class NPWorker {

    boolean dontScore = false;
    NPScoreCalculator scorer;
    File inFile;

    private boolean acceptFileFormat = false;




    private ArrayList<IAtomContainer> moleculesWithScores ;



    public NPWorker(String loadedFile){
        File file = new File(loadedFile);
        System.out.println("\n\n Working on: "+file.getName() + "\n\n");
        System.out.println("\n\n Working on: "+file.getAbsolutePath() + "\n\n");

        inFile = file;

        // check file type
        acceptFileFormat = acceptFile(loadedFile);
        if(acceptFileFormat){

        }


        //TODO options (add options later)

        //todo start the worker and return molecule ids + scores (to be printed by the controller)


        this.moleculesWithScores = doWork();


    }







    private boolean acceptFile(String value) {
        if (value.equalsIgnoreCase("sdf")) {
            return true;
        } else if (value.equalsIgnoreCase("smi")) {
            return true;
        } else if (value.equalsIgnoreCase("json")) {
            return true;
        }

        return false;
    }



    public ArrayList<IAtomContainer> doWork(){

        File fileNP = new File("signaturesdata/npfragments17-09-01-08-52-40serialized.out");
        if (fileNP.exists()) {
            try {
                NPScorerConstants.NP_TRAINING_FILE = fileNP.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NPScorerConstants.EXTERNAL_NP_TRAINING_DATA = true;
        }


        File fileSM = new File("signaturesdata/syntheticfragments17-09-01-08-48-52serialized.out");
        if (fileSM.exists()) {
            try {
                NPScorerConstants.SM_TRAINING_FILE = fileSM.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NPScorerConstants.EXTERNAL_SM_TRAINING_DATA = true;
        }


        System.out.println("Im in NPWorker - 1");
        ArrayList<IAtomContainer> molWithScores = passInputForScoreCalculation();
        System.out.println("Im in NPWorker - 2");
        //printOutput();


        return molWithScores;
    }



    public ArrayList<IAtomContainer> passInputForScoreCalculation() {

        scorer = new NPScoreCalculator();
        scorer.setReconstructFragments(true);
        ArrayList<IAtomContainer> molWithScores = scorer.process(this.inFile);

        return molWithScores;

    }


    public ArrayList<IAtomContainer> getMoleculesWithScores() {
        return moleculesWithScores;
    }



    public void controlMolecules(){
        for(IAtomContainer mol : this.moleculesWithScores){
            if( mol.getProperty("ID") == null ){
                //the molecule has no ID - problem in the SDF file
                mol.setProperty("ID", "Molecule ID not found, molecule number "+mol.getProperty("MOL_NUMBER_IN_FILE") + "from file "+ inFile.getName());
            }

            if (mol.getProperty("score") == null){
                mol.setProperty("score", "Score was not computed");
            }

        }



    }



    public ArrayList<String> getMoleculeString(){
        ArrayList<String> stringMolList = new ArrayList<String>();

        for(IAtomContainer mol : this.moleculesWithScores){
            String line = "";



            if(mol.getProperty("NAME" ) != null){
                line = ""+mol.getProperty("NAME" ) + " (" +  mol.getProperty("ID") + ") : " + mol.getProperty("NATURAL_PRODUCT_LIKENESS_SCORE");
            }
            else{
                line = mol.getProperty("ID") + " : " + mol.getProperty("NATURAL_PRODUCT_LIKENESS_SCORE");
            }
            stringMolList.add(line);
        }
        return stringMolList;
    }

}
