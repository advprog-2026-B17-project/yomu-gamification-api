package com.yomu.gamification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateClanRequest {
    @NotBlank(message = "Clan name is required")
    @Size(min = 3, max = 100, message = "Clan name must be between 3 and 100 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}