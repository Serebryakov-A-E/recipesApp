package me.serebyrakov.recipesapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.serebyrakov.recipesapp.model.Ingredient;
import me.serebyrakov.recipesapp.model.Recipe;
import me.serebyrakov.recipesapp.services.FileService;
import me.serebyrakov.recipesapp.services.RecipeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private Map<Integer, Recipe> recipes = new HashMap<>();
    private static int id = 1;

    private final FileService fileService;

    @Value("${name.of.data.recipe.file}")
    private String dataFileName;

    public RecipeServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    @Override
    public int add(Recipe recipe) {
        recipes.put(id, recipe);
        saveFile();
        return id++;
    }

    @Override
    public Recipe get(int id) {
        return recipes.get(id);
    }

    @Override
    public List<Recipe> getAll() {
        return recipes.values().stream().toList();
    }

    @Override
    public Recipe edit(int id, Recipe recipe) {
        if (recipes.containsKey(id)) {
            recipes.put(id, recipe);
            saveFile();
            return recipe;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        if(recipes.containsKey(id)) {
            recipes.remove(id);
            saveFile();
            return true;
        }
        return false;
    }

    private void saveFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipes);
            fileService.saveToFile(json, dataFileName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        try {
            String json = fileService.reedFromFile(dataFileName);
            if (StringUtils.isBlank(json)) {
                recipes = new HashMap<>();
            } else {
                recipes = new ObjectMapper().readValue(json, new TypeReference<Map<Integer, Recipe>>() {
                });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
