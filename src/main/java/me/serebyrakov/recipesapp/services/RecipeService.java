package me.serebyrakov.recipesapp.services;

import me.serebyrakov.recipesapp.model.Recipe;

import java.util.List;

public interface RecipeService {
    int add(Recipe recipe);

    Recipe get(int id);

    List<Recipe> getAll();

    Recipe edit(int id, Recipe recipe);

    boolean delete(int id);
}
