package me.serebyrakov.recipesapp.services.impl;

import me.serebyrakov.recipesapp.model.Ingredient;
import me.serebyrakov.recipesapp.services.IngredientService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngredientServiceImpl implements IngredientService {
    private final Map<Integer, Ingredient> ingredients = new HashMap<>();

    private static int id;

    public IngredientServiceImpl() {
        id = 1;
    }


    @Override
    public int add(Ingredient ingredient) {
        ingredients.put(id, ingredient);
        return id++;
    }

    @Override
    public Ingredient get(int id) {
        return ingredients.getOrDefault(id, null);
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredients.values().stream().toList();
    }

    @Override
    public Ingredient edit(int id, Ingredient ingredient) {
        if (ingredients.containsKey(id)) {
            ingredients.put(id, ingredient);
            return ingredient;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        if(ingredients.containsKey(id)) {
            ingredients.remove(id);
            return true;
        }
        return false;
    }
}
