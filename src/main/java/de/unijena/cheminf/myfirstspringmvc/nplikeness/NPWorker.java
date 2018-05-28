package de.unijena.cheminf.myfirstspringmvc.nplikeness;

import de.unijena.cheminf.myfirstspringmvc.nplikeness.scorer.NPScoreCalculator;

import java.io.File;

/**
 * @author mSorok
 */

public class NPWorker {

    boolean dontScore = false;
    NPScoreCalculator scorer;
    File inFile;


    public NPWorker(String loadedFile){
        File file = new File(loadedFile);
        System.out.println("\n\n Working on: "+file.getName() + "\n\n");
        System.out.println("\n\n Working on: "+file.getAbsolutePath() + "\n\n");

        dontScore = false;
        inFile = file;



        //TODO check file type

        //TODO options (add options later)

        //todo start the worker and return molecule ids + scores (to be printed by the controller)


    }
}
