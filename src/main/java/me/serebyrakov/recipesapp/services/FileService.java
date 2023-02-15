package me.serebyrakov.recipesapp.services;

import java.io.File;
import java.nio.file.Path;

public interface FileService {

    boolean saveToFile(String json, String dataFileName);

    String reedFromFile(String dataFileName);

    boolean cleanDataFile(String dataFileName);

    Path createTempFile(String suffix);

    File getDataFile(String dataFileName);
}
