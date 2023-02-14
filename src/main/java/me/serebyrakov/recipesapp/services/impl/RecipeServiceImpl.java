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
        saveLikeText();
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

    public void saveLikeText() {
        StringBuilder sb = new StringBuilder();
        for (Integer integer : recipes.keySet()) {
            Recipe recipe = recipes.get(integer);
            sb.append(recipe.getName()).append("\n");
            sb.append(String.format("Время приготовления: %d минут.\n", recipe.getTime()));
            sb.append(String.format("Количество порций: %d.\n", recipe.getPortions()));
            List<Ingredient> list = recipe.getIngredients();
            sb.append("Ингредиенты: \n");
            for (int i = 0; i < list.size(); i++) {
                Ingredient ing = list.get(i);
                sb.append(String.format("- %s - %d %s.\n", ing.getName(), ing.getCount(), ing.getMeasureUnit()));
            }
            sb.append("\nИнструкция приготовления:\n");
            String[] instruction = recipe.getSteps();
            for (int i = 0; i < instruction.length; i++) {
                sb.append(instruction[i]).append("\n");
            }
            sb.append("\n");
            fileService.saveToFile(String.valueOf(sb), nameOfRecipes);
        }
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
