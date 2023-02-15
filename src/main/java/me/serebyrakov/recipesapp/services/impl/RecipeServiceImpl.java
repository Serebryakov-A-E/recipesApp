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
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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

    @Value("${path.to.data.file}")
    private String pathToDataFile;

    @Value("${name.of.recipes.file}")
    private String nameOfRecipes;

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
        if (recipes.containsKey(id)) {
            recipes.remove(id);
            saveFile();
            return true;
        }
        return false;
    }

    @Override
    public Path createCurrentRecipesFile() {
        Path path = fileService.createTempFile("currentRecipes");
        for (Integer integer : recipes.keySet()) {
            try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)){
                Recipe recipe = recipes.get(integer);
                writer.append(recipe.getName()).append("\n");
                writer.append(String.format("Время приготовления: %d минут.\n", recipe.getTime()));
                writer.append(String.format("Количество порций: %d.\n", recipe.getPortions()));
                List<Ingredient> list = recipe.getIngredients();
                writer.append("Ингредиенты: \n");
                for (int i = 0; i < list.size(); i++) {
                    Ingredient ing = list.get(i);
                    writer.append(String.format("- %s - %d %s.\n", ing.getName(), ing.getCount(), ing.getMeasureUnit()));
                }
                writer.append("\nИнструкция приготовления:\n");
                String[] instruction = recipe.getSteps();
                for (int i = 0; i < instruction.length; i++) {
                    writer.append(instruction[i]).append("\n");
                }
                writer.append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return path;
    }

    private void saveFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipes);
            fileService.saveToFile(json, dataFileName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
