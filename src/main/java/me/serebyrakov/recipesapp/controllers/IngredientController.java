package me.serebyrakov.recipesapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.serebyrakov.recipesapp.model.Ingredient;
import me.serebyrakov.recipesapp.services.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
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
    @Operation(summary = "добавление ингредиента")
    @ApiResponse(
            responseCode = "200",
            description = "ингредиент был добавлен"
    )
    public ResponseEntity<Integer> addIngredient(@RequestBody Ingredient ingredient) {
        int id = ingredientService.add(ingredient);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "получение ингредиента по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ингредиент найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ingredient.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ингредиент не был найден"
            )
    }
    )
    public ResponseEntity<Ingredient> getIngredient(@PathVariable int id) {
        Ingredient ingredient = ingredientService.get(id);
        if (ObjectUtils.isEmpty(ingredient)) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(ingredient);
        }
        /*
        if (ingredient == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(ingredient);
        }
         */
    }

    @GetMapping("/")
    @Operation(summary = "получение списка всех ингредиентов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "выведен список всех ингредиентов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Ingredient.class))
                            )
                    }
            )
    }
    )
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.getAll();
        return ResponseEntity.ok(ingredients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "изменение ингредиента по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ингредиента был изменён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ингредиента не был найден"
            )
    }
    )
    public ResponseEntity<Ingredient> editIngredient(@PathVariable int id, @RequestBody Ingredient ingredient) {
        if (ingredientService.get(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            ingredientService.edit(id, ingredient);
            return ResponseEntity.ok(ingredient);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "удаление ингредиента по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "ингредиента был удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "ингредиента не был найден"
            )
    }
    )
    public ResponseEntity<Void> deleteIngredient(@PathVariable int id) {
        if (ingredientService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
