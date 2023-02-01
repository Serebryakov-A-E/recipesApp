package me.serebyrakov.recipesapp.controllers;

import me.serebyrakov.recipesapp.SomeInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class FirstController {

    @GetMapping("/")
    public String getAppInfo() {
        return "Приложение запущено";
    }

    @GetMapping("/info")
    public SomeInfo studentInfo() {
        return new SomeInfo();
    }
}

