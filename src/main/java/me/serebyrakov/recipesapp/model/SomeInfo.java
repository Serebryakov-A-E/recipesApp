package me.serebyrakov.recipesapp.model;

import lombok.Data;

@Data
public class SomeInfo {
    private String name;
    private String projectName;
    private String date;
    private String description;

    public SomeInfo() {
        this.name = "Алексей Серебряков";
        this.projectName = "Сайт рецептов";
        this.date = "01.02.2023";
        this.description = "Проект на java. Фреймворк - Spring.";
    }
}
