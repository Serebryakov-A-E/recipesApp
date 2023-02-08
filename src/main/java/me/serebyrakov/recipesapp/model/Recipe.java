package me.serebyrakov.recipesapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class Recipe {
    private String name;
    private int time;
    private int portions;
    private List<Ingredient> ingredients;
    private List<String> steps;
}
