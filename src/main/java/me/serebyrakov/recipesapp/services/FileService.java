package me.serebyrakov.recipesapp.services;

public interface FileService {

    boolean saveToFile(String json, String dataFileName);

    String reedFromFile(String dataFileName);

    boolean cleanDataFile(String dataFileName);
}
