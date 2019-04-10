package io.rainfall.store.controllers;

import io.rainfall.store.values.Case;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

class CaseForm {

    @Size(min=1, max=255, message = "Name must be 1-255 characters long")
    @NotNull
    @Setter
    @Getter
    @NonNull
    private String name;

    @Size(max=1024, message = "Description must be up to 1024 characters long")
    @Setter
    @Getter
    @NonNull
    private String description;

    Case build() {
        return Case.builder()
                .name(name)
                .description(description)
                .build();
    }
}
