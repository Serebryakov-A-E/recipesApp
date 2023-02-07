package me.serebyrakov.recipesapp.services;

import me.serebyrakov.recipesapp.model.Ingredient;
import me.serebyrakov.recipesapp.model.Recipe;

import java.util.List;

public interface IngredientService {
    int add(Ingredient ingredient);

    Ingredient get(int id);

    List<Ingredient> getAll();

    Ingredient edit(int id, Ingredient ingredient);

    boolean delete(int id);
}
