package com.FirstProject.TinyURL.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAliasRequest(

        @NotBlank(message = "Alias cannot be empty")
        String newAlias
) {
}
