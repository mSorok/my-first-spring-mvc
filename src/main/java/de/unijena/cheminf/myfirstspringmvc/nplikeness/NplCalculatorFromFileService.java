package de.unijena.cheminf.myfirstspringmvc.nplikeness;

import de.unijena.cheminf.myfirstspringmvc.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class NplCalculatorFromFileService implements NplService {

    private final Path rootLocation;

    @Autowired
    public NplCalculatorFromFileService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void getFileFromStorage() {

    }

    @Override
    public boolean controlFileType(File file) {
        return false;
    }

    @Override
    public boolean checkOptions() {
        return false;
    }

    @Override
    public boolean doWork(File file) {
        return false;
    }

    @Override
    public void provideScores() {

    }
}
