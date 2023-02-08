package me.serebyrakov.recipesapp.services.impl;

import me.serebyrakov.recipesapp.model.Recipe;
import me.serebyrakov.recipesapp.services.RecipeService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final Map<Integer, Recipe> recipes = new HashMap<>();
    private static int id;

    public RecipeServiceImpl() {
        id = 1;
    }

    @Override
    public int add(Recipe recipe) {
        recipes.put(id, recipe);
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
            return recipe;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        if(recipes.containsKey(id)) {
            recipes.remove(id);
            return true;
        }
        return false;
    }
}
