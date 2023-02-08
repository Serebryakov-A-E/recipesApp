package me.serebyrakov.recipesapp.controllers;

import me.serebyrakov.recipesapp.model.SomeInfo;
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
        return new SomeInfo().toString();
    }
}

