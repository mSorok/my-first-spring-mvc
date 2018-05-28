package de.unijena.cheminf.myfirstspringmvc.nplikeness;

import java.io.File;

public interface NplService {

    void getFileFromStorage();
    boolean controlFileType(File file);
    boolean checkOptions();
    boolean doWork(File file);
    void provideScores();
}
