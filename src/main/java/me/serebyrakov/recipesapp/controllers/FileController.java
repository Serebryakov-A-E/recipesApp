package me.serebyrakov.recipesapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.serebyrakov.recipesapp.services.FileService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @Value("${name.of.data.ingredients.file}")
    private String ingredientsDataFileName;

    @Value("${name.of.data.recipe.file}")
    private String recipesDataFileName;

    @Value("${name.of.recipes.file}")
    private String recipesFile;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(value = "/ingredients/export")
    @Operation(summary = "Получить файл со всеми ингредиентами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файла не существует"
            )
    }
    )
    public ResponseEntity<InputStreamResource> downloadIngredientsDataFile() throws FileNotFoundException {
        File file = fileService.getDataFile(ingredientsDataFileName);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"IngredientsLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/recipes/export")
    @Operation(summary = "Получить файл со всеми рецептами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файла не существует"
            )
    }
    )
    public ResponseEntity<InputStreamResource> downloadRecipesDataFile() throws FileNotFoundException {
        File file = fileService.getDataFile(recipesDataFileName);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RecipesLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/recipes/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить файл с рецептами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Файл успешно загружен"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Ошибка на стороне сервера"
            )
    }
    )
    public ResponseEntity<Void> uploadRecipesDataFile(@RequestParam MultipartFile file) {
        fileService.cleanDataFile(recipesDataFileName);
        File dataFile = fileService.getDataFile(recipesDataFileName);

        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping(value = "/recipes/exportTxt")
    @Operation(summary = "Получить txt файл со всеми рецептами")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно"
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файла не существует"
            )
    }
    )
    public ResponseEntity<InputStreamResource> downloadRecipesTxt() throws FileNotFoundException {
        File file = fileService.getDataFile(recipesFile);

        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Recipes.txt\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
