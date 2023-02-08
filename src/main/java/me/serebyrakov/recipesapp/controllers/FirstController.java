package me.serebyrakov.recipesapp.controllers;

import com.google.gson.Gson;
import me.serebyrakov.recipesapp.model.SomeInfo;
import me.serebyrakov.recipesapp.services.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @GetMapping("/")
    public String getAppInfo() {
        return "Приложение запущено";
    }

    @GetMapping("/info")
    public String studentInfo() {
        return new Gson().toJson(new SomeInfo());
    }
}

