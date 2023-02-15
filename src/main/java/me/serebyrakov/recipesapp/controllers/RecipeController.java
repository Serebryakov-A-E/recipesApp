package me.serebyrakov.recipesapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.serebyrakov.recipesapp.model.Recipe;
import me.serebyrakov.recipesapp.services.RecipeService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/recipes")
@Tag(name = "Рецепты", description = "Операции взаимодействия с рецептами")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @GetMapping(value = "/exportTxt")
    @Operation(summary = "Получить txt файл со всеми рецептами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файла не существует или он пуст"
            )
    }
    )
    public ResponseEntity<Object> downloadRecipesTxt() {
        try {
            Path path = recipeService.createCurrentRecipesFile();
            if (Files.size(path) == 0) {
                return ResponseEntity.noContent().build();
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"recipes.txt\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    @Operation(summary = "добавление рецепта в список рецептов")
    @ApiResponse(
            responseCode = "200",
            description = "рецепт был добавлен"
    )
    public ResponseEntity<Integer> addRecipe(@RequestBody Recipe recipe) {
        int id = recipeService.add(recipe);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "получение рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "рецепт найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Recipe.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "рецепт не был найден"
            )
    }
    )
    public ResponseEntity<Recipe> getRecipe(@PathVariable int id) {
        Recipe recipe = recipeService.get(id);
        if (ObjectUtils.isNotEmpty(recipe)) {
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/")
    @Operation(summary = "получение списка всех рецептов")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "выведен список всех рецептов",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Recipe.class))
                            )
                    }
            )
    }
    )
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAll();
        return ResponseEntity.ok(recipes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "изменение рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "рецепт был изменён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "рецепт не был найден"
            )
    }
    )
    public ResponseEntity<Recipe> editRecipe(@PathVariable int id, @RequestBody Recipe recipe) {
        if (ObjectUtils.isNotEmpty(recipeService.get(id))) {
            recipeService.edit(id, recipe);
            return ResponseEntity.ok(recipe);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "удаление рецепта по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "рецепт был удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "рецепт не был найден"
            )
    }
    )
    public ResponseEntity<Void> deleteRecipe(@PathVariable int id) {
        if (recipeService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
