package me.serebyrakov.recipesapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.serebyrakov.recipesapp.model.Ingredient;
import me.serebyrakov.recipesapp.services.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Ингридиенты", description = "Операции взаимодействия с ингридиентами")
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    @Operation(summary = "добавление ингридиента")
    public ResponseEntity<Integer> addIngredient(@RequestBody Ingredient ingredient) {
        int id = ingredientService.add(ingredient);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "получение ингридиента по id")
    public ResponseEntity<Ingredient> getIngredient(@PathVariable int id) {
        Ingredient ingredient = ingredientService.get(id);
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(ingredient);
        }
    }

    @GetMapping("/")
    @Operation(summary = "получение списка всех ингредиентов")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAll();
        return ResponseEntity.ok(ingredients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "изменение ингридиента по id")
    public ResponseEntity<Ingredient> editIngredient(@PathVariable int id, @RequestBody Ingredient ingredient) {
        if (ingredientService.get(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            ingredientService.edit(id, ingredient);
            return ResponseEntity.ok(ingredient);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "удаление ингридиента по id")
    public ResponseEntity<Void> deleteIngredient(@PathVariable int id) {
        if (ingredientService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
